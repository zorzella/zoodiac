# Zoodiac

Update site: http://www.zorzella.com/zoodiac/update

Here's a few of the tasks being worked on, completed or being entertained:

## Refresh Now / Shut Down Now

Just touch a file named shutdownnow either at the root of your workspace or at the root of any project in that workspace, and Eclipse will cleanly shut down.

Likewise for a refreshnow file, and Eclipse will "F5" refresh all projects (or that specific project).

## Source Menu contributions

In case you did not notice, Eclipse has 2 parallel models for things that change the Abstract Syntax Tree. The heavyweight model -- refactorings -- performs across-file changes, at the cost of speed. The lightweight mode -- source changes -- simply changes the current file, and is quite snappy and interactive, encouraging more frequent use. These are some of the improvement contributed in that area:

  * Increase/Decrease visibility (Shift+Control+= and Shift+Control+-): changes the current method/class/field's visibility (a few bugs remain)

  * Add "private final": adds a 'private final' to a class, as well as the same as a constructor param for each constructor declared in the class, and a "this.foo = foo" statement to each constructor
  
