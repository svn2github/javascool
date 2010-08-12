<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <!-- This translates the HML code to HTML proglet's doc (dml) to HTML3.2 http://www.w3.org/TR/REC-html32-19970114) -->

<xsl:import href="../src/org/javascool/hml2htm.xslt"/>

<xsl:output 
  method="html"
  encoding="UTF-8"
  doctype-public="-//W3C//DTD HTML 3.2 Final//EN"
  indent="yes" />

<!-- These tags produce dml specific constructs -->

<xsl:template match="div|p">
  <xsl:choose>
    <xsl:when test="@class = 'sujet'">
      <div align="right">[<a href="#intros">introduction</a>] [<a href="#works">travail proposé</a>] [<a href="#notes">remarques</a>]</div>
      <xsl:call-template name="div"/>
    </xsl:when>
    <xsl:when test="@class = 'objectif'"><h2>Objectif.</h2><xsl:call-template name="div"/></xsl:when>
    <xsl:when test="@class = 'intros' or @class = 'works' or @class = 'notes'">
      <xsl:choose>
        <xsl:when test="@class = 'intros'"><h2>Introduction.</h2></xsl:when>
        <xsl:when test="@class = 'works'"><h2>Travail proposé.</h2></xsl:when>
        <xsl:when test="@class = 'notes'"><h2>Remarques.</h2></xsl:when>
      </xsl:choose>
      <div id="{@class}"><ol><xsl:for-each select="*"><li><xsl:call-template name="div"/></li></xsl:for-each></ol></div>
    </xsl:when>
    <xsl:when test="@class = 'code'"><table witdh="90%" border="1" align="center"><tr><td><xsl:call-template name="div"/></td></tr></table></xsl:when>
    <xsl:otherwise><xsl:call-template name="div"/></xsl:otherwise>
  </xsl:choose>
</xsl:template>

<xsl:template match="l">
  <xsl:choose>
    <xsl:when test="@class = 'javascool'"><tt><a style="padding:0;margin:0;text-decoration:none" href="http://javascool.gforge.inria.fr">Java'sCool</a></tt></xsl:when>
    <xsl:when test="@class = 'proglet'">
      <applet code="org.javascool.ProgletApplet" archive="http://javascool.gforge.inria.fr/v3/javascool.jar" width="560" height="720">
        <param name="proglet" value="{@name}"/>
      </applet>
    </xsl:when>
    <xsl:otherwise><xsl:call-template name="l"/></xsl:otherwise>
  </xsl:choose>
</xsl:template>

<!-- These tags allows to show pieces of code: 
     <r></r> : reserved words
     <n></n> : name
     <v></v> : string's value
     <m></m> : comment ligne
     <T/>    : tabulation
-->

<xsl:template match="r">
  <font color="#990000"><b><xsl:apply-templates/></b></font>
</xsl:template>

<xsl:template match="n">
  <font color="#505000"><xsl:apply-templates/></font>
</xsl:template>

<xsl:template match="v">
  <font color="#008000">&quot;<xsl:apply-templates/>&quot;</font>
</xsl:template>

<xsl:template match="m">
  <p><tt><big><font color="#808080">//</font></big></tt>&#160;<xsl:apply-templates/></p>
</xsl:template>

<xsl:template match="T">
  &#160;&#160;&#160;
</xsl:template>

</xsl:stylesheet>
