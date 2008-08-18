package com.google.zoodiac.refreshnow;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import java.util.Timer;

/**
 */
public class RefreshNowPlugin extends AbstractUIPlugin {

  Timer timer;

  // The singleton
  private static RefreshNowPlugin plugin;

  /**
   * The constructor.
   */
  public RefreshNowPlugin() {
    plugin = this;
  }

  /**
   * This method is called upon plug-in activation
   */
  @Override
  public void start(BundleContext context) throws Exception {
    super.start(context);
  }

  /**
   * This method is called when the plug-in is stopped
   */
  @Override
  public void stop(BundleContext context) throws Exception {
    super.stop(context);
    timer.cancel();
    plugin = null;
  }

  /**
   * Returns the shared instance.
   */
  public static RefreshNowPlugin getDefault() {
    return plugin;
  }

  /**
   * Returns an image descriptor for the image file at the given plug-in
   * relative path.
   * 
   * @param path the path
   * @return the image descriptor
   */
  public static ImageDescriptor getImageDescriptor(String path) {
    return AbstractUIPlugin.imageDescriptorFromPlugin(
      "com.zorzella.eclipse.refreshnow", path);
  }
}
