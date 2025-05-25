/*******************************************************************************
 * Copyright (c) 2025 Tim De Baets
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Luiz-Otavio "Z" Zorzella
 *     Tim De Baets
 *******************************************************************************/
package com.google.zoodiac.refreshnow;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;

public class RefreshNowJob extends WorkspaceJob {

  static final String JOB_NAME_REFRESHING = "Refreshing files (Zoodiac)";
  static final String JOB_NAME_BUILDING = "Building project (Zoodiac)";
  
  static final String REFRESH_NOW_COMPLETED_MESSAGE = "Refresh now completed";
  static final String REFRESH_NOW_IN_PROGRESS_MESSAGE = "Refresh now in progress";
  
  private IProject project;
  
  public RefreshNowJob(IProject project) {
    // Create job with an empty name for now, we will change the name in
    // runInWorkspace().
    super("");
    this.project = project;
  }
  
  public IStatus runInWorkspace(IProgressMonitor monitor) throws CoreException {
    String balloonTitle = String
      .format("Project '%s'", project.getName());
    RefreshNowTask.displayMessage(
        balloonTitle,
        REFRESH_NOW_IN_PROGRESS_MESSAGE,
        true);
     
    setName(JOB_NAME_REFRESHING);
    logInfo(JOB_NAME_REFRESHING);
    
    project.refreshLocal(IResource.DEPTH_INFINITE, monitor);

    setName(JOB_NAME_BUILDING);
    logInfo(JOB_NAME_BUILDING);
   
    // TODO: make optional?
    project.build(
        IncrementalProjectBuilder.INCREMENTAL_BUILD,
        monitor);
    
    logInfo(REFRESH_NOW_COMPLETED_MESSAGE);
       
    RefreshNowTask.displayMessage(
        balloonTitle,
        REFRESH_NOW_COMPLETED_MESSAGE,
        true);

    return Status.OK_STATUS;
  }
  
  // TODO: make common
  void logInfo(String message) {
    ILog log = Platform.getLog(getClass());
    log.log(new Status(Status.INFO, "com.google.zoodiac", message));
  }

}
