/*******************************************************************************
 * Copyright (c) 2025 Tim De Baets
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Tim De Baets
 *******************************************************************************/
package com.google.zoodiac.refreshnow;

import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;

public class RefreshNowJobListener extends JobChangeAdapter {
  
  private RefreshNowTask task;
  
  public RefreshNowJobListener(RefreshNowTask task) {
    this.task = task;
  }
  
  /* IJobChangeListener */
  public void done(IJobChangeEvent event) {
    // Passing null because the shutdownNow file was already deleted
    task.shutdownnow(null);
  }
  
}
