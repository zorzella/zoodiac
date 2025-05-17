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
      if (shutdownNowWorkspace.exists()) {
        Thread deleteShutdownnowFile = new Thread(new Runnable() {
          @Override
          public void run() {
            shutdownNowWorkspace.delete();
          }
        });
        Runtime.getRuntime().addShutdownHook(deleteShutdownnowFile);
        shutdownnow(shutdownNowWorkspace);
      }
    } else {
      IProject[] projects = root.getProjects();
      for (IProject project : projects) {
        File uri = new File(project.getLocationURI());
        File refreshNow = new File(uri, "refreshnow");
        if (refreshAll || refreshNow.delete()) {
          refreshnow(project);
        } else {
          final File shutdownNow = new File(uri, "shutdownnow");
          if (shutdownNow.exists()) {
            Thread deleteShutdownnowFile = new Thread(new Runnable() {
              @Override
              public void run() {
                // We delete the "shutdownnow" file as a JVM exit hook, so
                // its absence can be used as an indicator that the shutdown
                // is complete
                shutdownNow.delete();
              }
            });
            Runtime.getRuntime().addShutdownHook(deleteShutdownnowFile);
            shutdownnow(shutdownNow);
          }
        }
      }
    }
    firstTime = false;
  }

  private void refreshnow(IProject project) {
    RefreshNowJob job = new RefreshNowJob(project);
    job.schedule();
  }

  private AtomicBoolean shuttingDown = new AtomicBoolean();
  
  private void shutdownnow(File shutdownNow) {
    
    if (firstTime) {
      shutdownNow.delete();
      return;
    }
    // After we trigger the shutdown, let's not add more shut down Runnables
    if (shuttingDown.getAndSet(true)) {
      return;
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
  
  void logInfo(String message) {
    ILog log = Platform.getLog(getClass());
    log.log(new Status(Status.INFO, "com.google.zoodiac", message));
  }

  static void displayMessage(String balloonTitle, String message,
      boolean autoHide) {
    Display.getDefault().asyncExec(
      new RefreshNowMessage(balloonTitle, message, autoHide));
  }
}