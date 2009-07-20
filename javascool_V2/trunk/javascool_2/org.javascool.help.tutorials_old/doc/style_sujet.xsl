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
      <div id="banner">
        <img src="img/help_banner.jpg" alt="Help banner"/>
      </div>
      <xsl:apply-templates/>
    </body>
  </html>
</xsl:template>
  
<xsl:template match="titre">
  <h1><xsl:value-of select="." /></h1><div align="right">[<a href="#notes">introduction</a>] [<a href="#works">travail proposé</a>] [<a href="#footnotes">remarques</a>]</div><br/><br/>
</xsl:template>

<xsl:template match="objectif" >
  <div class="soustitre"><p>Objectif : </p></div>	
  <br/>
  <div id="objectif">
    <div align="center"><xsl:apply-templates/></div>
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
  
<xsl:template match="footnotes">
  <div class="soustitre"><p>Remarques :</p></div>
  <div id="footnotes"><xsl:for-each select="*">
  <p id="{position()}"><sup><xsl:value-of select="position()"/></sup> <b><i><xsl:value-of select="@title"/></i></b> <xsl:apply-templates/></p>
  </xsl:for-each></div>
</xsl:template>
	
<xsl:template match="footnote">
  <sup><a href="{concat('#',@id)}"><xsl:value-of select="@id"/></a></sup>
</xsl:template>

</xsl:stylesheet>
