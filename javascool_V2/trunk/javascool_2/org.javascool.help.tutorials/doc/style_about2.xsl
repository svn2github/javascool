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
      <title>JavaScool . . and Java is so cool.</title>	
      <link rel="shortcut icon" href="img/icon.jpg"/>
    </head>
    <body>
      <xsl:call-template name="banner"/>
      <table width="100%"><tr>
        <td><a style="padding:0;margin:0;text-decoration:none" href="about-all.htm"><img src="img/home.png"/> </a></td>
        <td><h1><xsl:value-of select="@title"/></h1></td>
        <xsl:if test="count(@fuscia)=1">
          <td align="right">
            <a style="padding:0;margin:0;text-decoration:none" href="http:www.fuscia.info"><img height="30" src="http://www.fuscia.info/im/fusciaLogo11_pt.gif"/></a>
          </td>
        </xsl:if>
      </tr></table>
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
  <xsl:if test="count(@title)=1"><xsl:choose>
    <xsl:when test=".. = /"><h2><xsl:value-of select="@title"/>.</h2></xsl:when>
    <xsl:when test="../.. = /"><h3><xsl:value-of select="@title"/>.</h3></xsl:when>
    <xsl:when test="../../.. = /"><h4><xsl:value-of select="@title"/>.</h4></xsl:when>
    <xsl:otherwise><b><xsl:value-of select="@title"/>.</b><xsl:text> </xsl:text></xsl:otherwise>
  </xsl:choose></xsl:if>
  <div style="text-indent: 30px" id="{saxon:line-number()}">
    <xsl:if test="count(@align)=1"><xsl:attribute name="align"><xsl:value-of select="@align"/></xsl:attribute></xsl:if>
    <xsl:apply-templates/>
  </div>
</xsl:template>

</xsl:stylesheet>
