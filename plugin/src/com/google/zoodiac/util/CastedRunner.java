// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.zoodiac.util;

/**
 * @author zorzella
 */
public class CastedRunner {

  public interface ToRun<T> {
    void run(T arg);
  }

  @SuppressWarnings("unchecked")
  public static <T> void runIfOfType(Class<T> clazz, Object o, ToRun<T> toRun) {
    if (clazz.isInstance(o)) {
      toRun.run((T)o);
    }
  }
}
