/*******************************************************************************
 * Philippe.Vienne@linux-azur.org, Copyright (C) 2010.  All rights reserved.   *
 *******************************************************************************/

package org.javascool;

import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Pattern;

/** Defines a text as a PML data structure.
 *
 * @see <a href="Hml.java">source code</a>
 */
public class Hml extends Pml { /**/public Hml() { }
  private static final long serialVersionUID = 1L;

  public Pml reset(String value, String format) {
    if ("xml".equals(format) || "htm".equals(format) || "html".equals(format)) {
      return reset(Utils.xml2xml(Utils.xml2xml(Utils.htm2xml(value), htm2hml.xsl), xml2pml));
    } else {     
      return super.reset(value, format);
    }
  }

  /** Returns this logical-structure structure as a one-line string.
   * @param format <ul>
   * <li>"raw" To write in a normalized 1D plain text format (default).</li>
   * <li>"txt" To write in a normalized 2D plain text format.</li>
   * <li>"xml" To write in XML format, reducing tag and attribute names to valid XML names, and considering Pml without any attribute or elements as string.</li>
   * <li>"htm" To write in XHTML format, considering the Text convention defined here.</li>
   * </ul>
   */
  public String toString(String format) { 
    if ("htm".equals(format) || "html".equals(format)) {
      return Utils.xml2xml(toString("xml"), hml2htm.xsl);
    } else {     
      return super.toString(format);
    }
  }

  /** Used to check the syntax the well-formedness by mirroring the structure in a HTML format.
   * @param args Usage <tt>java org.javascool.Pml file-name</tt>
   * <p>- The file name can be a PML, XML or HTML file name, with the corresponding extensions</p>.
   */
  public static void main(String[] args) {
    if (args.length == 1) {
      Hml pml = new Hml(); pml.load(args[0]); pml.save("stdout:", "htm");
    } else {
      System.out.println("Usage: java org.javascool.Hml file-name");
    }
  }
}
