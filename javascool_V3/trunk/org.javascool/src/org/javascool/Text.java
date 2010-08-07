/*******************************************************************************
 * Philippe.Vienne@linux-azur.org, Copyright (C) 2010.  All rights reserved.   *
 *******************************************************************************/

package org.javascool;

import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Pattern;

/** Defines a Test as a PML data structure.
 *
 * @see <a href="Text.java">source code</a>
 */
public class Text extends Pml { /**/public Text() { }
  private static final long serialVersionUID = 1L;

  public Pml reset(String value, String format) {
    if ("xml".equals(format) || "htm".equals(format) || "html".equals(format)) {
      return reset(Utils.xml2xml(Utils.xml2xml(Utils.htm2xml(value),htm2pml),xml2pml));
    } else {     
      return super.reset(value, format);
    }
  }
  private static String htm2pml = 
    "<?xml version='1.0' encoding='utf-8'?>\n"+
    "<xsl:stylesheet xmlns:xsl='http://www.w3.org/1999/XSL/Transform' version='1.0'>\n"+
    "  <xsl:output method='xml' encoding='utf-8' omit-xml-declaration='yes'/>\n"+
    "  <xsl:template match='a'><xsl:element name='l'>\n"+
    "    <xsl:if test='count(@class) > 0'><xsl:attribute name='class'><xsl:value-of select='@class'/></xsl:attribute></xsl:if>\n"+
    "    <xsl:if test='count(@href) > 0'><xsl:attribute name='link'><xsl:value-of select='@href'/></xsl:attribute></xsl:if>\n"+
    "    <xsl:if test='count(img/@src) > 0'><xsl:attribute name='img'><xsl:value-of select='img/@src'/></xsl:attribute></xsl:if>\n"+
    "    <xsl:attribute name='text'><xsl:if test='count(img/@alt) > 0'><xsl:value-of select='img/@alt'/></xsl:if> <xsl:value-of select='.'/></xsl:attribute>\n"+
    "  </xsl:element></xsl:template>\n"+
    "  <xsl:template match='img'><xsl:element name='l'>\n"+
    "    <xsl:if test='count(@src) > 0'><xsl:attribute name='img'><xsl:value-of select='@src'/></xsl:attribute></xsl:if>\n"+
    "    <xsl:if test='count(@alt) > 0'><xsl:attribute name='text'><xsl:value-of select='@alt'/></xsl:attribute></xsl:if>\n"+
    "    <xsl:attribute name='text'><xsl:value-of select='.'/></xsl:attribute>\n"+
    "  </xsl:element></xsl:template>\n"+
    "  <xsl:template match='span'><xsl:element name='s'>\n"+
    "    <xsl:if test='count(@class) > 0'><xsl:attribute name='class'><xsl:value-of select='@class'/></xsl:attribute></xsl:if>\n"+
    "    <xsl:apply-templates/>\n"+
    "  </xsl:element></xsl:template>\n"+
    "  <xsl:template match='*'><xsl:element name='{name(.)}'>\n"+
    "    <xsl:for-each select='@*'><xsl:attribute name='{name(.)}'><xsl:value-of select='.'/></xsl:attribute></xsl:for-each>\n"+
    "    <xsl:apply-templates/>\n"+
    "  </xsl:element></xsl:template>\n"+
    "</xsl:stylesheet>";
  
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
      return Utils.xml2xml(toString("xml"), pml2htm);
    } else {     
      return super.toString(format);
    }
  }
  private static String pml2htm = 
    "<?xml version='1.0' encoding='utf-8'?>\n"+
    "<xsl:stylesheet xmlns:xsl='http://www.w3.org/1999/XSL/Transform' version='1.0'>\n"+
    "  <xsl:output method='html' encoding='utf-8'/>\n"+
    "  <xsl:template match='l'><xsl:element name='a'>\n"+
    "    <xsl:if test='count(@class) > 0'><xsl:attribute name='class'><xsl:value-of select='@class'/></xsl:attribute></xsl:if>\n"+
    "    <xsl:if test='count(@link) > 0'><xsl:attribute name='href'><xsl:value-of select='@link'/></xsl:attribute></xsl:if>\n"+
    "    <xsl:value-of select='@text'/>\n"+
    "  </xsl:element></xsl:template>\n"+
    "  <xsl:template match='s'><xsl:element name='span'>\n"+
    "    <xsl:if test='count(@class) > 0'><xsl:attribute name='class'><xsl:value-of select='@class'/></xsl:attribute></xsl:if>\n"+
    "    <xsl:apply-templates/>\n"+
    "  </xsl:element></xsl:template>\n"+
    "  <xsl:template match='*'><xsl:element name='{name(.)}'>\n"+
    "    <xsl:for-each select='@*'><xsl:attribute name='{name(.)}'><xsl:value-of select='.'/></xsl:attribute></xsl:for-each>\n"+
    "    <xsl:apply-templates/>\n"+
    "  </xsl:element></xsl:template>\n"+
    "</xsl:stylesheet>";

  /** Used to check the syntax the well-formedness by mirroring the structure in a HTML format.
   * @param args Usage <tt>java org.javascool.Pml file-name</tt>
   * <p>- The file name can be a PML, XML or HTML file name, with the corresponding extensions</p>.
   */
  public static void main(String[] args) {
    if (args.length == 1) {
      Text pml = new Text(); pml.load(args[0]); pml.save("stdout:", "htm");
    } else {
      System.out.println("Usage: java org.javascool.Text file-name");
    }
  }
}
