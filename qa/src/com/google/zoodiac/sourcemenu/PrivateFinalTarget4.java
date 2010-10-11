package com.google.zoodiac.sourcemenu;

public class PrivateFinalTarget4 {

  static int baz;
  // BUG: ideally, inserted member should come here -- after the "static", but
  // before the non-static, i.e. not after "foo", even if "foo" is static,
  // because "foo" is misplaced, but in fact...
  String buz;
  static String foo;
  // ... it may come here. Either seems acceptable
  String bar;
  
  public PrivateFinalTarget4(String foo, String bar) {
    super();
    this.foo = foo;
    this.bar = bar;
  }
  
  /* TARGET */
}
