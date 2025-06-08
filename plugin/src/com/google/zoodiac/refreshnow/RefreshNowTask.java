/*******************************************************************************
 * Copyright (c) 2007 Google, Inc.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Google, Inc.
 *******************************************************************************/
package com.google.zoodiac.refreshnow;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import java.io.File;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

class RefreshNowTask extends TimerTask {

  /**
   * Prevents some actions (such as shutdown) from happening at the very first 
   * time this is run. 
   */
  private boolean firstTime = true;
  
  @Override
  public synchronized void run() {
    IWorkspaceRoot root;

    try {
      root = ResourcesPlugin.getWorkspace().getRoot();
    } catch (IllegalStateException e) {
      // The workspace has been closed (i.e. Eclipse is exiting). Ignoring it.
      return;
    }

    final File shutdownNowWorkspace = 
      new File (new File(root.getLocationURI()), "shutdownnow");

    final File refreshNowWorkspace = 
      new File (new File(root.getLocationURI()), "refreshnow");
    
    boolean refreshAll = refreshNowWorkspace.delete();
    
    if (shutdownNowWorkspace.exists()) {
      shutdownnow(shutdownNowWorkspace);
    } else {
      IProject[] projects = root.getProjects();
      
      for (IProject project : projects) {
        final File uri = new File(project.getLocationURI());
        final File refreshNow = new File(uri, "refreshnow");
        final File shutdownNow = new File(uri, "shutdownnow");
        
        if (refreshAll || refreshNow.delete()) {
          refreshnow(project, shutdownNow);
        } else if (shutdownNow.exists()) {
          shutdownnow(shutdownNow);
        }
      }
    }
    
    firstTime = false;
  }

  private void refreshnow(IProject project, File shutdownNow) {
    RefreshNowJob job = new RefreshNowJob(project);
    
    // If `shutdownNow` exists, it means there's both a "shutdownnow" and a
    // "refreshnow" file. Let's delay shutting down until the
    // refresh/build/indexer is completed.
    if (shutdownNow.exists()) {
      // We must delete the shutdownNow file now, otherwise run() would initiate
      // another shutdown before the refresh job has completed.
      // TODO: find a way to keep the file here and only delete on actual shutdown (to allow canceling a pending shutdown)
      shutdownNow.delete();
      
      // Register a listener to the `RefreshNowJob` that initiates a shutdown
      // when that job (and possible subsequent indexer update) is done.
      RefreshNowJobListener listener = new RefreshNowJobListener(this);
      job.addJobChangeListener(listener);
    }

    job.schedule();
  }

  private AtomicBoolean shuttingDown = new AtomicBoolean();
  
  public void shutdownnow(File shutdownNow) {
    if (firstTime) {
      deleteFileIfNonNull(shutdownNow);
      return;
    }
    
    // After we trigger the shutdown, let's not add more shut down Runnables
    if (shuttingDown.getAndSet(true)) {
      deleteFileIfNonNull(shutdownNow);
      return;
    }
    
    if (shutdownNow != null) {
      Thread deleteShutdownnowFile = new Thread(new Runnable() {
        @Override
        public void run() {
          // We delete the "shutdownnow" file as a JVM exit hook, so its absence
          // can be used as an indicator that the shutdown is complete
          shutdownNow.delete();
        }
      });
      Runtime.getRuntime().addShutdownHook(deleteShutdownnowFile);
    }
    
    Runnable shutdownRunnable = new Runnable() {
      @Override
      public void run() {
        logInfo("Shutting down now");
        IWorkbench workbench = PlatformUI.getWorkbench();
        workbench.saveAllEditors(false);
        workbench.close();
      }
    };
    Display.getDefault().asyncExec(shutdownRunnable);
  }
  
  void deleteFileIfNonNull(File file) {
    if (file != null) {
      file.delete();
    }
  }
  
  static void logInfo(String message) {
    ILog log = Platform.getLog(RefreshNowTask.class);
    log.log(new Status(Status.INFO, "com.google.zoodiac", message));
  }

  static void displayMessage(String balloonTitle, String message,
      boolean autoHide) {
    Display.getDefault().asyncExec(
      new RefreshNowMessage(balloonTitle, message, autoHide));
  }
}