<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:output method="xml" encoding="utf-8" omit-xml-declaration="yes"/>

<!--- 1: Division translation -->

<xsl:template match="ul|ol|table">
  <div class="{name(.)}"> 
    <xsl:apply-templates/>
  </div>
</xsl:template>

<xsl:template match="div|p|li|tr|td">
  <div>
    <xsl:if test="count(@title)=1"><xsl:attribute name="title"><xsl:value-of select="@title"/></xsl:attribute></xsl:if>
    <xsl:choose>
      <xsl:when test="count(@class)=1"><xsl:attribute name="class"><xsl:value-of select="@class"/></xsl:attribute></xsl:when>
      <xsl:when test="count(@align)=1"><xsl:attribute name="class"><xsl:value-of select="@align"/></xsl:attribute></xsl:when>
      <xsl:when test="count(@valign)=1"><xsl:attribute name="class"><xsl:value-of select="@valign"/></xsl:attribute></xsl:when>
    </xsl:choose>
    <xsl:if test="count(@id)=1"><xsl:attribute name="id"><xsl:value-of select="@id"/></xsl:attribute></xsl:if>
    <xsl:apply-templates/>
  </div>
</xsl:template>

<!--- 2: Span translation -->

<xsl:template match="span">
  <s>    
    <xsl:if test="count(@class)=1"><xsl:attribute name="class"><xsl:value-of select="class"/></xsl:attribute></xsl:if>
    <xsl:apply-templates/>
  </s>
</xsl:template>
<xsl:template match="b|i|u">
  <xsl:element name="{name(.)}"><xsl:apply-templates/></xsl:element>
</xsl:template>
<xsl:template match="tt"><c><xsl:apply-templates/></c></xsl:template>
<xsl:template match="sup"><S><xsl:apply-templates/></S></xsl:template>
<xsl:template match="sub"><I><xsl:apply-templates/></I></xsl:template>

<!--- 3: Link translation -->

<xsl:template match="a">
  <l>    
    <xsl:if test="count(@class)=1"><xsl:attribute name="class"><xsl:value-of select="class"/></xsl:attribute></xsl:if>
    <xsl:if test="count(@href)=1"><xsl:attribute name="link"><xsl:value-of select="@href"/></xsl:attribute></xsl:if>
    <xsl:if test="count(img/@src)=1"><xsl:attribute name="icon"><xsl:value-of select="img/@src"/></xsl:attribute></xsl:if>
    <xsl:attribute name="text"><xsl:value-of select="text()"/></xsl:attribute>
  </l>
</xsl:template>

<xsl:template match="img">
  <l class="icon">    
    <xsl:if test="count(@src)=1"><xsl:attribute name="icon"><xsl:value-of select="@src"/></xsl:attribute></xsl:if>
    <xsl:if test="count(@alt)=1"><xsl:attribute name="text"><xsl:value-of select="@alt"/></xsl:attribute></xsl:if>
  </l>
</xsl:template>

<!--- 4: Spurious translation -->

<xsl:template match="br|hr|form|input|select|option">
  <xsl:message>Unexpected tag: <xsl:value-of select="name(.)"/> !!</xsl:message>
</xsl:template>

<xsl:template match="*">
  <xsl:message>Undefined tag: <xsl:value-of select="name(.)"/> !!</xsl:message>
</xsl:template>

</xsl:stylesheet>
