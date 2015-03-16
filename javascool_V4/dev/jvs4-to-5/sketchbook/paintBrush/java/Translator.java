/*******************************************************************************
* David.Pichardie@sophia.inria.fr, Copyright (C) 2009.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.paintBrush;

/** Defines a Jvs code to Java standard code translation for this proglet.
 * @see <a href="Translator.java.html">code source</a>
 * @serial exclude
 */
public class Translator extends org.javascool.core.Translator {
  @Override
  public String getImports() {
    return "import org.javascool.proglets.paintBrush.PaintBrushManipImage ;";
  }
}
