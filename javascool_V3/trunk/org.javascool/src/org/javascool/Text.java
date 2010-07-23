/*******************************************************************************
 * Philippe.Vienne@linux-azur.org, Copyright (C) 2010.  All rights reserved.   *
 *******************************************************************************/

package org.javascool;

import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Pattern;

/** Defines a Test as a PML data structure.
 *
 * @see <a href="../../org/javascool/Text.java">source code</a>
 */
public class Text extends Pml { /**/public Text() { }
  private static final long serialVersionUID = 1L;

  public Pml load(String location, String format) {
    if ("xml".equals(format) || "htm".equals(format) || "html".equals(format)) {
      return reset(Utils.xml2xml(Utils.xml2xml(Utils.htm2xml(Utils.loadString(location)),htm2pml),xml2pml));
    } else {     
      return super.load(location, format);
    }
  }
  private static String htm2pml = 
    "<?xml version='1.0' encoding='utf-8'?>"+
    "<xsl:stylesheet xmlns:xsl='http://www.w3.org/1999/XSL/Transform' version='1.0'>"+
    "  <xsl:output method='xml' encoding='utf-8' omit-xml-declaration='yes'/>"+
    "  <xsl:template match='a'><xsl:element name='l'>"+
    "    <xsl:if test='count(@class) > 0'><xsl:attribute name='class'><xsl:value-of select='@class'/></xsl:attribute></xsl:if>"+
    "    <xsl:if test='count(@href) > 0'><xsl:attribute name='link'><xsl:value-of select='@href'/></xsl:attribute></xsl:if>"+
    "    <xsl:attribute name='text'><xsl:value-of select='.'/></xsl:attribute>"+
    "  </xsl:element></xsl:template>"+
    "  <xsl:template match='*'><xsl:element name='{name(.)}'>"+
    "    <xsl:for-each select='@*'><xsl:attribute name='{name(.)}'><xsl:value-of select='.'/></xsl:attribute></xsl:for-each>"+
    "    <xsl:apply-templates/>"+
    "  </xsl:element></xsl:template>"+
    "</xsl:stylesheet>";

  /** Used to check the syntax the well-formedness by mirroring the structure in a normalized format.
   * @param args Usage <tt>java org.javascool.Pml file-name</tt>
   * <p>- The file name can be a PML, XML or HTML file name, with the corresponding extensions</p>.
   */
  public static void main(String[] args) {
    if (args.length == 1) {
      Text pml = new Text(); pml.load(args[0]); pml.save("stdout:", "plain");
    } else {
      System.out.println("Usage: java org.javascool.Text file-name");
    }
  }
}
