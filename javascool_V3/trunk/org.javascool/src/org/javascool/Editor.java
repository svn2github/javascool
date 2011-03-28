/*******************************************************************************
* Thierry.Vieville@sophia.inria.fr, Copyright (C) 2009.  All rights reserved. *
*******************************************************************************/

package org.javascool;

/** Indicates that this class is a graphic editor that get/set text. */
public interface Editor {
  /** Resets the editor.
   * @param editable True to edit the text. False to view it.
   * @return This, allowing to use the <tt>new Editor().reset(..)</tt> construct.
   */
  public Editor reset(boolean editable);

  /** Sets the editing text.
   * @param text The text to edit.
   * @return This, allowing to use the <tt>new Editor().setText(..)</tt> construct.
   */
  public Editor setText(String text);

  /** Gets the edited text. */
  public String getText();

  /** Checks if the text has been modified. */
  public boolean isModified();

  /** Checks if the editor is in editable or lock mode. */
  public boolean isEditable();
}
