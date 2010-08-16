/*******************************************************************************
 * Thierry.Vieville@sophia.inria.fr, Copyright (C) 2009.  All rights reserved. *
 *******************************************************************************/

package org.javascool;

/** Indicates that this class is a graphic editor that get/set text. */
public interface Editor {

  /** Sets the editing text. 
   * @param text The text to edit.
   * @return This, allowing to use the <tt>new Editor().setText(..)</tt> construct.
   */
  public Editor setText(String text);

  /** Gets the edited text. */
  public String getText();

  /** Checks if the text has been modified. */
  public boolean isModified();
}
