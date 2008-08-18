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

import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolTip;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;
import org.osgi.framework.Bundle;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

class RefreshNowMessage implements Runnable {

  static final String PNG_NAME = "z.png";
  static final String PLUGIN_NAME = "com.zorzella.eclipse.refreshnow";

  private String ballonTitle;
  private String message;
  private boolean autoHide;

  RefreshNowMessage(final String balloonTitle, final String message,
      final boolean autoHide) {
    this.ballonTitle = balloonTitle;
    this.message = message;
    this.autoHide = autoHide;
  }

  public void run() {
    showBalloon();
  }

  private void showBalloon() {

    Shell shell = new Shell();

    final ToolTip tip = new ToolTip(shell, SWT.BALLOON | SWT.ICON_INFORMATION);
    tip.setText(ballonTitle);
    tip.setMessage(message);
    tip.setAutoHide(autoHide);

    Image image = null;

    Display display = 
      Display.getDefault();
//    shell.getDisplay();
    
    Tray tray = display.getSystemTray();
    TrayItem item = null;
    if (tray != null) {
      item = new TrayItem(tray, SWT.NONE);
      InputStream eclipsePng;
      try {
        Bundle bundle = Platform.getBundle(PLUGIN_NAME);
        URL entry = bundle.getEntry(PNG_NAME);

        eclipsePng = entry.openStream();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
      image = new Image(display, eclipsePng);
      item.setImage(image);
      item.setToolTip(tip);
    } else {
      tip.setLocation(400, 400);
    }
    tip.setVisible(true);
    while (tip.getVisible()) {
      if (!display.readAndDispatch()) {
        display.sleep();
      }
    }
    if (item != null) {
      item.dispose();
    }
    if (image != null) {
      image.dispose();
    }
  }

}