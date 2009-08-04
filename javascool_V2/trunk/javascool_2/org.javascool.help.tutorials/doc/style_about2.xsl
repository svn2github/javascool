<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:saxon="http://icl.com/saxon">

<xsl:import href="style_common.xsl"/>

<xsl:output 
  method="html"
  encoding="ISO-8859-1"
  doctype-public="-//W3C//DTD HTML 4.01//EN"
  doctype-system="http://www.w3.org/TR/html4/strict.dtd"
  indent="yes" />
  
<xsl:template match="/p">
  <html>
    <head>
      <link rel="stylesheet" type="text/css" href="img/style.css"/>	
    </head>
    <body>
      <div id="banner">
        <img src="img/help_banner.jpg" alt="Help banner"/>
      </div>
      <h1><xsl:value-of select="@title"/></h1>
      <xsl:call-template name="toc"/><hr/>
      <xsl:apply-templates/>
    </body>
  </html>
</xsl:template>

<xsl:template name="toc">
  <xsl:param name="id" select="''"/>
  <xsl:if test="count(p/@title)>0"><ul><xsl:for-each select="p[count(@title)>0]"><li>
    <a href="{concat('#',saxon:line-number())}"><xsl:value-of select="@title"/></a>
    <xsl:call-template name="toc"><xsl:with-param name="id" select="concat($id,'.',position())"/></xsl:call-template>
  </li></xsl:for-each></ul></xsl:if>
</xsl:template>
  
<xsl:template match="p">
  <p style="text-indent: 30px" id="{saxon:line-number()}">
    <xsl:if test="count(@title)=1"><xsl:choose>
    <xsl:when test=".. = /"><h2><xsl:value-of select="@title"/>.</h2></xsl:when>
      <xsl:when test="../.. = /"><h3><xsl:value-of select="@title"/>.</h3></xsl:when>
      <xsl:when test="../../.. = /"><h4><xsl:value-of select="@title"/>.</h4></xsl:when>
      <xsl:otherwise><b><xsl:value-of select="@title"/>.</b><xsl:text> </xsl:text></xsl:otherwise>
    </xsl:choose></xsl:if>
    <xsl:apply-templates/>
  </p>
</xsl:template>

</xsl:stylesheet>
