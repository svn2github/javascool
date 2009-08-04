<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<xsl:template match="javascool">
  <tt>Java'sCool</tt>
</xsl:template>

<xsl:template match="footnotes">
  <div class="soustitre"><p>Remarques :</p></div>
  <div id="footnotes"><xsl:for-each select="*">
  <p id="{position()}"><sup><xsl:value-of select="position()"/></sup> <b><i><xsl:value-of select="@title"/></i></b> <xsl:apply-templates/></p>
  </xsl:for-each></div>
</xsl:template>
	
<xsl:template match="footnote">
  <sup><a href="{concat('#',@id)}"><xsl:value-of select="@id"/></a></sup>
</xsl:template>

<!-- These tags allows to show pieces of code: 
     <r></r> : reserved words
     <s></s> : strings
     <h></h> : hightlighted words
     <m></m> : to define a comment ligne
     <p></p> : to define a ligne
     <t/>    : tabulation
     <c></c> : piece of code (outside the code tag)
-->

<xsl:template match="code">
  <table border="1" align="center" width="80%"><tr><th align="left">
  <xsl:apply-templates/>
  </th></tr></table>
</xsl:template>

<xsl:template match="r">
  <font color="#990000"><b><xsl:apply-templates/></b></font>
</xsl:template>

<xsl:template match="s">
  <font color="#008000">&quot;<xsl:apply-templates/>&quot;</font>
</xsl:template>

<xsl:template match="h">
  <span style="background: #00dcff"><xsl:apply-templates/></span>
</xsl:template>

<xsl:template match="t">
  &#160;&#160;&#160;
</xsl:template>

<xsl:template match="m">
  <p><tt><big><font color="#808080">//</font></big></tt>&#160;<xsl:apply-templates/></p>
</xsl:template>

<xsl:template match="c">
  <tt><xsl:apply-templates/></tt>
</xsl:template>

<!-- All other allowed tags are mirrored in the output -->

<xsl:template match="p|ul|ol|li|table|tr|td|span|sup|i|b|br|a|img"><xsl:text>
</xsl:text>
<xsl:element name="{name(.)}">
  <xsl:for-each select="@*"><xsl:if test="name(.) != 'class' and name(.) != 'style'"><xsl:attribute name="{name(.)}"><xsl:value-of select="."/></xsl:attribute></xsl:if></xsl:for-each>
  <xsl:apply-templates/>
</xsl:element>
</xsl:template>

<xsl:template match="*">
  <xsl:message>Undefine tag: <xsl:value-of select="name(.)"/> !!</xsl:message>
</xsl:template>

</xsl:stylesheet>
