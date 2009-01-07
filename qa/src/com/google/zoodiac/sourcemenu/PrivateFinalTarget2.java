package com.google.zoodiac.sourcemenu;

public class PrivateFinalTarget2 {
  
  public enum Foo {
    ;

    Foo () {
    }

  }

  public PrivateFinalTarget2() {
    /* BUG -- it should not mess with Foo's constructor */
  }
}
