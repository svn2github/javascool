<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:output 
  method="html"
  encoding="UTF-8"
  doctype-public="-//W3C//DTD HTML 4.01//EN"
  doctype-system="http://www.w3.org/TR/html4/strict.dtd"
  indent="yes" />

<!-- 0 : Page production -->

<xsl:template match="/*">
  <html>
    <head>
      <xsl:for-each select="@*"><xsl:choose>
        <xsl:when test="name(.) = 'title'"><title><xsl:value-of select="."/></title></xsl:when>
        <xsl:when test="name(.) = 'icon'"><link rel="shortcut icon" href="{.}"/></xsl:when>
        <xsl:when test="name(.) = 'style'"><link rel="stylesheet" href="{.}"/></xsl:when>
        <xsl:when test="name(.) = 'script'"><script type="text/javascript" src="{.}"/></xsl:when>
        <xsl:otherwise><meta name="{name(.)}" content="{.}"/></xsl:otherwise> 
        <!-- Possible improvement: take <meta http-equiv='$name' content='$value' /> or other <link rel='$name' href='$value' /> constructs into account !-->
      </xsl:choose></xsl:for-each>
    </head>
    <body>
      <xsl:if test="count(@title)=1"><h1><xsl:value-of select="@title"/>.</h1></xsl:if>
      <xsl:apply-templates/>
    </body>
  </html>
</xsl:template>

<!-- 1 : Section production -->

<xsl:template match="div|p"><xsl:call-template name="div"/></xsl:template>
<xsl:template name="div">
  <xsl:choose>
    <xsl:when test="@class = 'table' or @class = 'ul' or @class = 'ol'">
      <xsl:element name="{@class}"><xsl:call-template name="div-2"/></xsl:element>
    </xsl:when>
    <xsl:when test="../@class = 'ul' or ../@class = 'ol'"><li><xsl:call-template name="div-2"/></li></xsl:when>
    <xsl:when test="../@class = 'table'"><tr><xsl:call-template name="div-2"/></tr></xsl:when>
    <xsl:when test="../../@class = 'table'"><td><xsl:call-template name="div-2"/></td></xsl:when>
    <xsl:otherwise><div><xsl:call-template name="div-2"/></div></xsl:otherwise>
  </xsl:choose>
</xsl:template>
<xsl:template name="div-2">
  <xsl:if test="count(@id)=1"><xsl:attribute name="id"><xsl:value-of select="@id"/></xsl:attribute></xsl:if>
  <xsl:if test="count(@class)=1"><xsl:attribute name="class"><xsl:value-of select="@class"/></xsl:attribute></xsl:if>
  <xsl:if test="count(@title)=1"><xsl:choose>
    <xsl:when test=".. = /"><h2><xsl:value-of select="@title"/>.</h2></xsl:when>
    <xsl:when test="../.. = /"><h3><xsl:value-of select="@title"/>.</h3></xsl:when>
    <xsl:when test="../../.. = /"><h4><xsl:value-of select="@title"/>.</h4></xsl:when>
    <xsl:otherwise><b><xsl:value-of select="@title"/>.</b><xsl:text> </xsl:text></xsl:otherwise>
  </xsl:choose></xsl:if>
  <xsl:apply-templates/>
</xsl:template>

<!-- 2 : Span production -->
<xsl:template match="s">
  <span>
    <xsl:if test="count(@class)=1"><xsl:attribute name="class"><xsl:value-of select="@class"/></xsl:attribute></xsl:if>
    <xsl:apply-templates/>
  </span>
</xsl:template>
<xsl:template match="b"><b><xsl:apply-templates/></b></xsl:template>
<xsl:template match="i"><i><xsl:apply-templates/></i></xsl:template>
<xsl:template match="c"><tt><xsl:apply-templates/></tt></xsl:template>
<xsl:template match="S"><sup><xsl:apply-templates/></sup></xsl:template>
<xsl:template match="I"><sub><xsl:apply-templates/></sub></xsl:template>

<!-- 3 : Link production -->
<xsl:template match="l"><xsl:call-template name="l"/></xsl:template>
<xsl:template name="l">
  <xsl:choose>
    <xsl:when test='count(@link) = 1'><a href="{@link}"> 
      <xsl:if test="count(@class)=1"><xsl:attribute name="class"><xsl:value-of select="@class"/></xsl:attribute></xsl:if>
      <xsl:if test='count(@icon) = 1'><img src="{@icon}" alt="{@text}"/></xsl:if>
      <xsl:value-of select='@text'/>
    </a></xsl:when>
    <xsl:when test='count(@icon) = 1'><img src="{@icon}" alt="{@text}">
      <xsl:if test="count(@class)=1"><xsl:attribute name="class"><xsl:value-of select="@class"/></xsl:attribute></xsl:if>
    </img></xsl:when>
    <xsl:when test='count(@text) = 1'><span>
      <xsl:if test="count(@class)=1"><xsl:attribute name="class"><xsl:value-of select="@class"/></xsl:attribute></xsl:if>
      <xsl:value-of select='@text'/>
    </span></xsl:when>
  </xsl:choose>
</xsl:template>

<!--- 4: Spurious translation -->

<xsl:template match="*"><xsl:text>
</xsl:text>  
<xsl:message>Unexpected tag: <xsl:value-of select="name(.)"/> !!</xsl:message>
<xsl:element name="{name(.)}">
  <xsl:for-each select="@*"><xsl:attribute name="{name(.)}"><xsl:value-of select="."/></xsl:attribute></xsl:for-each>
  <xsl:apply-templates/>
</xsl:element>
</xsl:template>

</xsl:stylesheet>
