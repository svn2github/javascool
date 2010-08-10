<?xml version='1.0' encoding='utf-8'?>
<xsl:stylesheet xmlns:xsl='http://www.w3.org/1999/XSL/Transform' version='1.0'>
  <xsl:output method='html' encoding='utf-8'/>
  <xsl:template match='l'><xsl:element name='a'>
    <xsl:if test='count(@class) > 0'><xsl:attribute name='class'><xsl:value-of select='@class'/></xsl:attribute></xsl:if>
    <xsl:if test='count(@link) > 0'><xsl:attribute name='href'><xsl:value-of select='@link'/></xsl:attribute></xsl:if>
    <xsl:value-of select='@text'/>
  </xsl:element></xsl:template>
  <xsl:template match='s'><xsl:element name='span'>
    <xsl:if test='count(@class) > 0'><xsl:attribute name='class'><xsl:value-of select='@class'/></xsl:attribute></xsl:if>
    <xsl:apply-templates/>
  </xsl:element></xsl:template>
  <xsl:template match='*'><xsl:element name='{name(.)}'>
    <xsl:for-each select='@*'><xsl:attribute name='{name(.)}'><xsl:value-of select='.'/></xsl:attribute></xsl:for-each>
    <xsl:apply-templates/>
  </xsl:element></xsl:template>
</xsl:stylesheet>
