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

/* BUG */

/* BUG note to self -- take care of requesting team permission to modify file */

/**
 * Use this class for manually QA-ing the "add private final" functionality. 
 * 
 * <p>For each place where there's a "TARGET" comment, put the cursor there and
 * try out adding a private final. Make sure to undo all the additions before
 * checking in.
 * 
 * @author zorzella
 */
public class PrivateFinalTarget {

  public class Foo {
    public Foo() {
    }
  }
  
  class Baz {
    /* TARGET */
    
    void some(){}
    
  }
  
  /* TARGET */
  
  public PrivateFinalTarget() {
    /* TARGET */
  }
  
  /* TARGET - QUIRK this ends up being applied to "Bar" */

  class Bar {
     /* TARGET */
  }
  
  class Zee {
    /* TARGET */
    Zee (String... foo) {
    }
  }
  
}

