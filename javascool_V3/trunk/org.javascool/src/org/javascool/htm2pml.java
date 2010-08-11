/* Automatically generated by the makefile: DO NOT EDIT !*/ package org.javascool; class htm2pml { static String xsl = 
"<?xml version='1.0' encoding='utf-8'?>\n"+
"<!--\n"+
"                        XML2PML\n"+
"      ______________________________________________\n"+
"     | By Philippe Vienne <philoumailabo@gmail.com> |\n"+
"     | Distrubuted on GNU General Public Licence    |\n"+
"     | Revision 1                                   |\n"+
"     | © 2010 INRIA, All rights reserved            |\n"+
"     |______________________________________________|\n"+
"\n"+
"-->\n"+
"<xsl:stylesheet xmlns:xsl='http://www.w3.org/1999/XSL/Transform' version='1.0'>\n"+
"<xsl:output method='xml' encoding='utf-8' omit-xml-declaration='yes'/>\n"+
"<!-- Page Balise -->\n"+
"<xsl:template match=\"page\">{page title=\"<xsl:value-of select=\"@title\"/>\" author=\"<xsl:value-of select=\"@author\"/>\" email-author=\"<xsl:value-of select=\"@email-author\"/>\" tags=\"<xsl:value-of select=\"@tags\"/>\" desc=\"<xsl:value-of select=\"@desc\"/>\" css=\"<xsl:value-of select=\"@css\"/>\" javascript=\"<xsl:value-of select=\"@javascript\"/>\"<xsl:apply-templates/>}</xsl:template>\n"+
"<!-- Div Balise -->\n"+
"<xsl:template match=\"div\">{div id=\"<xsl:value-of select=\"@id\"/>\" title=\"<xsl:value-of select=\"@title\"/>\" class=\"<xsl:value-of select=\"@class\"/>\"\n"+
"<xsl:apply-templates/>\n"+
"}</xsl:template>\n"+
"<!-- P Balise -->\n"+
"<xsl:template match=\"p\">\n"+
"{p\n"+
"<xsl:apply-templates/>\n"+
"}</xsl:template>\n"+
"<!-- L Balise -->\n"+
"<xsl:template match=\"l\">{l text=\"<xsl:value-of select=\"@text\"/>\" img=\"<xsl:value-of select=\"@img\"/>\" link=\"<xsl:value-of select=\"@link\"/>\" class=\"<xsl:value-of select=\"@class\"/>\"}</xsl:template>\n"+
"<!-- S Balise-->\n"+
"<xsl:template match=\"s\">{s class=\"<xsl:value-of select=\"@class\"/>\"<xsl:apply-templates/>}</xsl:template>\n"+
"<!-- Style (i,b,u) Balises -->\n"+
"<xsl:template match=\"i|b|u\">{<xsl:value-of select=\"name(.)\"/> <xsl:value-of select=\".\"/>}</xsl:template>\n"+
"<!-- END -->\n"+
"</xsl:stylesheet>\n"+
"\n"+
"<!--\n"+
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
"</xsl:stylesheet>\n"+
"-->\n"+
"";}