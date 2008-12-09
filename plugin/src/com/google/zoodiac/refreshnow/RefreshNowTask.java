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
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import java.io.File;
import java.util.TimerTask;

class RefreshNowTask extends TimerTask {

  private static final String REFRESH_NOW_COMPLETED_MESSAGE = "Refresh now completed";
  private static final String REFRESH_NOW_IN_PROGRESS_MESSAGE = "Refresh now in progress";

  /**
   * Prevents some actions (such as shutdown) from happening at the very first 
   * time this is run. 
   */
  private boolean firstTime = true;
  
  @Override
  public synchronized void run() {
    try {
      IWorkspaceRoot root;

      try {
        root = ResourcesPlugin.getWorkspace().getRoot();
      } catch (IllegalStateException e) {
        // The workspace has been closed (i.e. Eclipse is exiting). Ignoring it.
        return;
      }

      File shutdownNow = 
        new File (new File(root.getLocationURI()), "shutdownnow");
      
      if (shutdownNow.delete()) {
        shutdownnow();
      } else {
        IProject[] projects = root.getProjects();
        for (IProject project : projects) {
          File uri = new File(project.getLocationURI());
          File refreshNow = new File(uri, "refreshnow");
          if (refreshNow.delete()) {
            refreshnow(project);
          } else {
            shutdownNow = new File(uri, "shutdownnow");
            if (shutdownNow.delete()) {
              shutdownnow();
            }
          }
        }
      }
      firstTime = false;
    } catch (final CoreException e) {
      String balloonTitle = "RefreshNow plugin";
      String message = e.getMessage();
      displayMessage(balloonTitle, message, false);
    }
  }

  private void refreshnow(IProject project) throws CoreException {
    String balloonTitle = String
      .format("Project '%s'", project.getName());
    displayMessage(balloonTitle, REFRESH_NOW_IN_PROGRESS_MESSAGE, true);
    project.refreshLocal(IResource.DEPTH_INFINITE, null);

    project.build(
        IncrementalProjectBuilder.INCREMENTAL_BUILD,
        null);
    
    displayMessage(balloonTitle, REFRESH_NOW_COMPLETED_MESSAGE, true);
  }

  private void shutdownnow() {
    if (firstTime) {
      return;
    }
    Runnable shutdownRunnable = new Runnable() {
    
      public void run() {
        IWorkbench workbench = PlatformUI.getWorkbench();
        workbench.close();
      }
    };
    Display.getDefault().asyncExec(
      shutdownRunnable);
  }

  private void displayMessage(String balloonTitle, String message,
      boolean autoHide) {
    Display.getDefault().asyncExec(
      new RefreshNowMessage(balloonTitle, message, autoHide));
  }
}