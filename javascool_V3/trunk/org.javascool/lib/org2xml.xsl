<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:import href="../src/org/javascool/htm2hml.xslt"/>

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

<xsl:template match="objectif|works"><div class="{name(.)}"><xsl:apply-templates/></div></xsl:template>
<xsl:template match="notes"><div class="intros"><xsl:apply-templates/></div></xsl:template>
<xsl:template match="footnotes"><div class="notes"><xsl:apply-templates/></div></xsl:template>

<xsl:template match="note|work">
  <div>
    <xsl:if test="count(@title)=1"><xsl:attribute name="title"><xsl:value-of select="@title"/></xsl:attribute></xsl:if>
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
<xsl:template mode="code" match="s"><v><xsl:apply-templates mode="code"/></v></xsl:template>
<xsl:template mode="code" match="t"><T/></xsl:template>

<xsl:template match="r|c">
  <xsl:element name="{name(.)}"><xsl:apply-templates mode="code"/></xsl:element>
</xsl:template>
<xsl:template match="s"><v><xsl:apply-templates/></v></xsl:template>

<xsl:template match="proglet">
  <xsl:if test="@mode = 'demo'">
    <div class="table"><p><p><xsl:apply-templates/></p><p><l class="proglet" l="{@name}"/></p></p></div>
  </xsl:if>
</xsl:template>

<xsl:template match="footnote">
  <l class="note" link="{concat('#', @id)}"/>
</xsl:template>

<xsl:template match="javascool">
  <l class="javascool"/>
</xsl:template>

</xsl:stylesheet>
