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

import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.cdt.core.CCorePlugin;
import org.eclipse.cdt.core.index.IIndexerStateEvent;
import org.eclipse.cdt.core.index.IIndexerStateListener;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;

public class RefreshNowJobListener extends JobChangeAdapter
    implements IIndexerStateListener {
  
  private static final long SECOND = 1000;
  private static final long WAIT_SECONDS = 15;
  private static final long WAIT_TIME = WAIT_SECONDS * SECOND;
  
  private RefreshNowTask task;
  private Timer timer;
  private boolean jobDone = false;
  
  public RefreshNowJobListener(RefreshNowTask task) {
    this.task = task;
  }
  
  private void startTimer() {
    stopTimer();
    TimerTask timerTask = new TimerTask() {
      @Override
      public void run() {
        stopTimer();
        indexIdle();
      }
    };
    timer = new Timer(true /* isDaemon */);
    timer.schedule(timerTask, WAIT_TIME, WAIT_TIME);
  }
  
  private void stopTimer() {
    if (timer != null) {
      timer.cancel();
    }
  }
  
  private void indexIdle() {
    CCorePlugin.getIndexManager().removeIndexerStateListener(this);
    // Passing null because the shutdownNow file was already deleted
    task.shutdownnow(null);
  }
  
  /* IJobChangeListener */
  public void done(IJobChangeEvent event) {
    jobDone = true;
    CCorePlugin.getIndexManager().addIndexerStateListener(this);
    if (CCorePlugin.getIndexManager().isIndexerIdle()) {
      RefreshNowTask.logInfo("Job done and indexer is idle, starting timer");
      startTimer();
    }
  }
  
  /* IIndexerStateListener */
  @Override
  public void indexChanged(IIndexerStateEvent event) {
    RefreshNowTask.logInfo("Indexer state changed, idle: " +
        event.indexerIsIdle() + ", job done: " +  jobDone);
    
    if (!jobDone) {
      return;
    }
    
    if (event.indexerIsIdle()) {
      startTimer();
    } else {
      stopTimer();
    }
  }

}
