<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:import href="style_common.xsl"/>

<xsl:output 
  method="html"
  encoding="ISO-8859-1"
  doctype-public="-//W3C//DTD HTML 4.01//EN"
  doctype-system="http://www.w3.org/TR/html4/strict.dtd"
  indent="yes" />
  
<xsl:template match="sujet">
  <html>
    <head>
      <link rel="stylesheet" type="text/css" href="img/style.css"/>	
    </head>
    <body>
      <xsl:call-template name="banner"/>
      <xsl:apply-templates/>
    </body>
  </html>
</xsl:template>
  
<xsl:template match="titre">
  <h1>
    <a style="padding:0;margin:0;text-decoration:none" href="about-all.htm"><img src="img/home.png"/> </a> <xsl:text>
    </xsl:text>
    <xsl:value-of select="." />
  </h1>
  <div align="right">[<a href="#notes">introduction</a>] [<a href="#works">travail proposé</a>] [<a href="#footnotes">remarques</a>]</div><br/><br/>
</xsl:template>

<xsl:template match="objectif" >
  <div class="soustitre"><p>Objectif : </p></div>	
  <br/>
  <div id="objectif">
    <table align="center" width="80%"><tr><td align="left"><xsl:apply-templates/></td></tr></table>
  </div>
  <br/>	
</xsl:template>
	
<xsl:template match="notes" >
  <div class="soustitre"><p>Introduction : </p></div>	
  <br/>
  <ul id="notes">
    <xsl:apply-templates select="note"/>
  </ul>
  <br/>
</xsl:template>
	
<xsl:template match="note" >
  <li><div><b><xsl:value-of select="@title"/> </b>: <xsl:apply-templates/></div><br/></li>
</xsl:template>
  
<xsl:template match="works" >
  <div class="soustitre"><p>Travail proposé :</p></div>
  <br/>
  <ul id="works">
    <xsl:apply-templates select="work"/>
  </ul>
</xsl:template>
	
<xsl:template match="work" >
  <li><b><xsl:value-of select="@title"/> </b>: <xsl:apply-templates/><br/></li>
</xsl:template>
  
</xsl:stylesheet>
