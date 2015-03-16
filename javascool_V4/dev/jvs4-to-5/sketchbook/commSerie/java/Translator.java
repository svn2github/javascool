/*******************************************************************************
* Thierry.Vieville@sophia.inria.fr, Copyright (C) 2009.  All rights reserved. *
*******************************************************************************/
package org.javascool.proglets.commSerie;

/** Defines a Jvs code to Java standard code translation for this proglet.
 * @see <a href="Translator.java.html">code source</a>
 * @serial exclude
 */
public class Translator extends org.javascool.core.Translator {
  @Override
  public String getImports() {
    return "import org.javascool.proglets.commSerie.Panel;" +
           "import org.javascool.proglets.commSerie.ConvertisseurPanel;" +
           "import org.javascool.proglets.commSerie.SerialInterface;" +
           "import org.javascool.proglets.commSerie.SerialInterfacePanel;";
  }
}
