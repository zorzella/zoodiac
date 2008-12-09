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
public class /* TARGET */ ChangeVisibilityTarget {
  
  protected static class /* TARGET */ Foo {
    
    String /* TARGET */ baz;
    
    protected void /* TARGET */ a () {}

    protected 
    void /* TARGET (BUG make sure to leave this split in 5 lines) */ 
    b 
    () 
    {}

    protected static void /* TARGET */ d () {}

    static private void /* BUG -- when default visibility, does not preserve the "static protected" order */ e () {}

    @Deprecated
    protected void /* TARGET */ foo () {}
  }

}

class /* TARGET */ Bar {}

interface /* TARGET */ Biz {}

enum /* TARGET */ Baz {}

@interface /* TARGET */ Zoo {}

