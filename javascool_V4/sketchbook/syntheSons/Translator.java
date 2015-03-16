/*******************************************************************************
* Thierry.Vieville@sophia.inria.fr, Copyright (C) 2009.  All rights reserved. *
*******************************************************************************/
package org.javascool.proglets.syntheSons;

/** Defines a Jvs code to Java standard code translation for this proglet.
 * @see <a href="Translator.java.html">code source</a>
 * @serial exclude
 */
public class Translator extends org.javascool.core.Translator {
  @Override
  public String getImports() {
    return "import org.javascool.tools.sound.InputSoundBit;" +
           "import org.javascool.tools.sound.FileSoundBit;" +
           "import org.javascool.tools.sound.SoundBit;" +
           "import org.javascool.proglets.syntheSons.NotesSoundBit;" +
           "import org.javascool.proglets.syntheSons.SoundBitPanel;";
  }
  @Override
  public String translate(String code) {
    // Translates the  @tone macro
    return code.replaceAll("@tone:(.*)\\s*;",
                           "/* @tone:$1 @<nojavac*/org.javascool.proglets.syntheSons.Functions.tone = new org.javascool.tools.sound.SoundBit() { public double get(char c, double t) { return $1; } }; org.javascool.proglets.syntheSons.Functions.setNotes(\"16 a\");/*@nojavac>*/");
  }
}
