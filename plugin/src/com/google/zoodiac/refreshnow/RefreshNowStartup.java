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

import com.google.zoodiac.ZoodiacPluginActivator;

import org.eclipse.ui.IStartup;

import java.util.Date;
import java.util.Timer;

public class RefreshNowStartup implements IStartup {

  private static final long SECOND = 1000;
  private static final long INTERVAL = 5 * SECOND;

  public void earlyStartup() {
    ZoodiacPluginActivator.getDefault().timer = new Timer(true);
    Date now = new Date();
    ZoodiacPluginActivator.getDefault()
      .timer
        .schedule(
          new RefreshNowTask(), now, INTERVAL);
  }
}
