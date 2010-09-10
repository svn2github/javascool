/*******************************************************************************
 * Thierry.Vieville@linux-azur.org, Copyright (C) 2010.  All rights reserved.  *
 *******************************************************************************/

package org.javascool;

import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Pattern;

/** Defines an expression as a PML data structure.
 * See the semantic <a href="doc-files/about-eml.htm">description</a> (in French) for more info.
 *
 * @see <a href="Eml.java.html">source code</a>
 * @serial exclude
 */
public class Eml extends Pml { /**/public Eml() { }
  private static final long serialVersionUID = 1L;

  /** Resets the logical-structure, parsing the given string. 
   * @param value The value following the usual expression syntax.
   * @param format <ul>
   * <li>"eml" Reads in eml format (default value).</li>
   * <li>"pml" Reads in pml format.</li>
   * <li>"xml, "htm" or "html" Reads in HTML format, translating HTML structure to HML, considering the Text convention defined here.</li>
   * </ul> 
   * @return This, allowing to use the <tt>new Eml().reset(..)</tt> construct.
   */
  /*
  public Pml reset(String value, String format) {
    if ("eml".equals(format)) {
      return reset(value); 
    } else {     
      return super.reset(value, format);
    }
  }
  */
  /*public Pml reset(String value) {
    return this;
  }
  */

  /** Returns this logical-structure structure as a one-line string.
   * @param format <ul>
   * <li>"eml" To write as a standard expression (default).</li>
   * <li>"txt" To write in a normalized 2D plain text format.</li>
   * <li>"xml" To write in XML format, reducing tag and attribute names to valid XML names, and considering Pml without any attribute or elements as string.</li>
   * </ul>
   */
  public String toString(String format) { 
    if ("eml".equals(format)) {
      return toString();
    } else {     
      return super.toString(format);
    }
  }
  /*public String toString() {
    return null;
  }
  */

  /** Used to check the syntax the well-formedness by mirroring the Eml structure in a normalized format.
   * @param usage <tt>java org.javascool.Pml input-file [output-file]</tt>
   * <p>- The file name be a EML, PML, XML or HTML file name, with the corresponding extensions</p>.
   */
  public static void main(String[] usage) {
    if (usage.length > 0) new Eml().load(usage[0]).save(usage.length > 1 ? usage[0] : "stdout:");
  }
}
