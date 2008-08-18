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
package com.google.zoodiac.sourcemenu;

/**
 * Use this class for manually QA-ing the "change visibility" functionality. 
 * 
 * <p>For each place where there's a "TARGET" comment, put the cursor there and
 * try out changing the visibility.
 * 
 * @author zorzella
 */
public class /* BUG -- should work with classes */ ChangeVisibilityTarget {
  
  public static class /* BUG -- should work with classes */ Foo {
    
    protected String /* TARGET */ baz;
    
    protected void /* TARGET */ a () {}

    protected 
    void /* TARGET (make sure to leave this split in 5 lines) */ 
    b 
    () 
    {}

    protected static void /* TARGET */ d () {}

    static protected void /* BUG -- changes the order when it's "static protected" rather than "protected static" */ e () {}

    @Deprecated
    public void /* BUG -- it messes with formatting/order */ c () {}
  
  }

}

/* BUG -- should work with classes */
class Bar {
  
}