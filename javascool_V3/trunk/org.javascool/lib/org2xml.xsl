<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:output method="xml" encoding="utf-8" omit-xml-declaration="yes"/>

<!--- Specific JavascoolV2 dos translation -->

<xsl:template match="sujet">
  <div class="sujet" title="{titre}">
    <xsl:apply-templates/>
  </div>
</xsl:template>
<xsl:template match="titre">
  <xsl:if test="name(..) != 'sujet'"><xsl:message>Error: sujet/titre</xsl:message></xsl:if>
</xsl:template>

<xsl:template match="objectif|notes|works|footnotes">
  <div class="{name(.)}">
    <xsl:choose>
      <xsl:when test="name(.) = 'objectif'"><xsl:attribute name="title">Objectif:</xsl:attribute></xsl:when>
      <xsl:when test="name(.) = 'notes'"><xsl:attribute name="title">Introduction:</xsl:attribute></xsl:when>
      <xsl:when test="name(.) = 'works'"><xsl:attribute name="title">Travail proposé :</xsl:attribute></xsl:when>
      <xsl:when test="name(.) = 'footnotes'"><xsl:attribute name="title">Remarques :</xsl:attribute></xsl:when>
    </xsl:choose>
    <xsl:apply-templates/>
  </div>
</xsl:template>

<xsl:template match="note|work|footnote">
  <div>
    <xsl:if test="count(@title)=1"><xsl:attribute name="title"><xsl:value-of select="@title"/></xsl:attribute></xsl:if>
    <xsl:attribute name="class"><xsl:value-of select="name(.)"/></xsl:attribute>
    <xsl:if test="count(@id)=1"><xsl:attribute name="id"><xsl:value-of select="@id"/></xsl:attribute></xsl:if>
    <xsl:apply-templates/>
  </div>
</xsl:template>

<!-- These tags allows to show pieces of code: 
     <r></r> : reserved words.
     <s></s> : strings.
     <h></h> : hightlighted words.
     <m></m> : to define a comment ligne.
     <c></c> : piece of code (outside the code tag).
     <t/>    : tabulation.
     <p></p> : to define a ligne.
-->

<xsl:template match="code">
 <div class="{name(.)}"> 
    <xsl:apply-templates mode="code"/>
  </div>
</xsl:template>

<xsl:template mode="code" match="r|p|c|m|h">
  <xsl:element name="{name(.)}"><xsl:apply-templates mode="code"/></xsl:element>
</xsl:template>
<xsl:template mode="code" match="s"><t><xsl:apply-templates mode="code"/></t></xsl:template>
<xsl:template mode="code" match="t"><T/></xsl:template>

<xsl:template match="r|c">
  <xsl:element name="{name(.)}"><xsl:apply-templates mode="code"/></xsl:element>
</xsl:template>
<xsl:template match="s"><t><xsl:apply-templates/></t></xsl:template>

<xsl:template match="proglet">
  <l class="proglet" l="{@name}"/>
</xsl:template>

<xsl:template match="javascool">
  <s class="javascool">JavaScool</s>
</xsl:template>

<!--- Translates html tags to pml syntax -->

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
<xsl:template match="sub"><s><xsl:apply-templates/></s></xsl:template>

<!--- 3: Link translation -->

<xsl:template match="a">
  <l>    
    <xsl:if test="count(@class)=1"><xsl:attribute name="class"><xsl:value-of select="class"/></xsl:attribute></xsl:if>
    <xsl:if test="count(@href)=1"><xsl:attribute name="link"><xsl:value-of select="@href"/></xsl:attribute></xsl:if>
    <xsl:if test="count(img/@src)=1"><xsl:attribute name="icon"><xsl:value-of select="img/@src"/></xsl:attribute></xsl:if>
    <xsl:attribute name="text"><xsl:value-of select="."/></xsl:attribute>
  </l>
</xsl:template>

<xsl:template match="img">
  <l class="icon">    
    <xsl:if test="count(@src)=1"><xsl:attribute name="icon"><xsl:value-of select="@src"/></xsl:attribute></xsl:if>
    <xsl:if test="count(@alt)=1"><xsl:attribute name="text"><xsl:value-of select="@alt"/></xsl:attribute></xsl:if>
  </l>
</xsl:template>

<!--- 4: Spurious translation -->

<xsl:template match="br|hr"/>

<xsl:template match="form|input|select|option">
  <xsl:message>Unexpected tag: <xsl:value-of select="name(.)"/> !!</xsl:message>
</xsl:template>

<xsl:template match="nothingatall"><xsl:text>
</xsl:text>
<xsl:element name="{name(.)}">
  <xsl:for-each select="@*"><xsl:attribute name="{name(.)}"><xsl:value-of select="."/></xsl:attribute></xsl:for-each>
  <xsl:apply-templates/>
</xsl:element>
</xsl:template>

<xsl:template match="*">
  <xsl:message>Undefined tag: <xsl:value-of select="name(.)"/> !!</xsl:message>
</xsl:template>

</xsl:stylesheet>
