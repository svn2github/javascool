/*******************************************************************************
 * Philippe.Vienne@linux-azur.org, Copyright (C) 2010.  All rights reserved.   *
 *******************************************************************************/

package org.javascool;

import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Pattern;

/** Defines a text as a PML data structure.
 * See the semantic <a href="doc-files/about-hml.htm">description</a> (in French) for more info.
 *
 * @see <a href="Hml.java.html">source code</a>
 * @serial exclude
 */
public class Hml extends Pml { /**/public Hml() { }
  private static final long serialVersionUID = 1L;

  /** Resets the logical-structure, parsing the given string. 
   * @param value The value following the <tt>"{tag name = value .. element .. }"</tt> syntax.
   * @param format <ul>
   * <li>"pml" Reads in pml format (default value).</li>
   * <li>"xml, "htm" or "html" Reads in HTML format, translating HTML structure to HML, considering the Text convention defined here.</li>
   * </ul> 
   * @return This, allowing to use the <tt>new Hml().reset(..)</tt> construct.
   */
  public Pml reset(String value, String format) {
    if ("htm".equals(format) || "html".equals(format)) {
      return reset(Utils.xml2xml(Utils.xml2xml(Utils.htm2xml(value), htm2hml.xsl), xml2pml));
    } else if ("xml".equals(format)) {
      return reset(Utils.xml2xml(Utils.xml2xml(value, htm2hml.xsl), xml2pml));
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

  /** Used to check the syntax the well-formedness by mirroring the Hml structure in a normalized format.
   * @param usage <tt>java org.javascool.Pml input-file [output-file]</tt>
   * <p>- The file name be a PML, XML or HTML file name, with the corresponding extensions</p>.
   */
  public static void main(String[] usage) {
    if (usage.length > 0) new Hml().load(usage[0]).save(usage.length > 1 ? usage[0] : "stdout:");
  }
}
