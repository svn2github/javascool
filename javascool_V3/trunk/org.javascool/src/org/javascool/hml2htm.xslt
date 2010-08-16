<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <!-- This XSLT translates HML code to HTML 4 output -->

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
        <!-- Possible improvement: take <meta http-equiv="$name" content="$value" /> or other <link rel="$name" href="$value" /> constructs into account !-->
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
    <xsl:when test="@class = 'table'">
      <table align="center" width="90%" bgcolor="#eeeeee"><xsl:call-template name="div-2"/></table>
    </xsl:when>
    <xsl:when test="@class = 'ul' or @class = 'ol'">
      <xsl:element name="{@class}"><xsl:call-template name="div-2"/></xsl:element>
    </xsl:when>
    <xsl:when test="../@class = 'ul' or ../@class = 'ol'"><li><xsl:call-template name="div-2"/></li></xsl:when>
    <xsl:when test="../@class = 'table'"><tr><xsl:call-template name="div-2"/></tr></xsl:when>
    <xsl:when test="../../@class = 'table'"><td valign="top"><xsl:call-template name="div-2"/></td></xsl:when>
    <xsl:when test="@class = 'tag'"><xsl:call-template name="tag"/></xsl:when>
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

<!-- 1.1 : Pml construct description production -->

<xsl:template name="tag"><table bgcolor="#eeeeee" width="90%">
  <tr><td colspan="4"><b>{</b><xsl:text> </xsl:text><xsl:value-of select="@title"/></td></tr>
  <tr><td align="center"><b>name</b></td><td align="center"><b>type</b></td><td align="center"><b>default value</b></td><td></td></tr>
  <xsl:for-each select="param"><tr>
     <td><font color="#505000"><xsl:value-of select="@name"/></font></td>
     <td><font color="#990000"><xsl:value-of select="@type"/></font></td>
     <td><xsl:choose>
        <xsl:when test="count(@value)=1"><font color="#008000">"<xsl:value-of select="@value"/>"</font></xsl:when>
        <xsl:otherwise><i>mandatory</i></xsl:otherwise>
     </xsl:choose></td>
     <td><xsl:apply-templates/></td>
  </tr></xsl:for-each>
  <tr><td colspan="4"><xsl:choose>
    <xsl:when test="count(elements/@type) > 0">Structure: "<font color="#202080"><xsl:value-of select="elements/@type"/>"</font></xsl:when>
    <xsl:otherwise><i>no element</i></xsl:otherwise>
  </xsl:choose></td></tr>
  <tr><td colspan="4"><hr/></td></tr>
  <tr><td colspan="4">
    <xsl:for-each select="*[name(.) != 'param' and name(.) != 'elements']"><xsl:apply-templates/></xsl:for-each>
  </td></tr>
  <tr><td colspan="4"><b>}</b></td></tr>
</table></xsl:template>

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
    <xsl:when test="@class = 'replace'"><script language="javascript">location.replace("<xsl:value-of select="@link"/>");</script></xsl:when>
    <xsl:when test="count(@link) = 1"><a href="{@link}"> 
      <xsl:if test="count(@class)=1"><xsl:choose>
        <xsl:when test="@class = 'new'"><xsl:attribute name="target">_blank</xsl:attribute></xsl:when>
        <xsl:otherwise><xsl:attribute name="class"><xsl:value-of select="@class"/></xsl:attribute></xsl:otherwise>
      </xsl:choose></xsl:if>
      <xsl:if test="count(@icon) = 1"><img src="{@icon}" alt="{@text}"/></xsl:if>
      <xsl:if test="count(@text)=1"><xsl:value-of select="@text"/></xsl:if>
      <xsl:if test="count(@text)=0">[.]</xsl:if>
    </a></xsl:when>
    <xsl:when test="count(@icon) = 1"><img src="{@icon}" alt="{@text}">
      <xsl:if test="count(@class)=1"><xsl:attribute name="class"><xsl:value-of select="@class"/></xsl:attribute></xsl:if>
    </img></xsl:when>
    <xsl:when test="count(@text) = 1"><span>
      <xsl:if test="count(@class)=1"><xsl:attribute name="class"><xsl:value-of select="@class"/></xsl:attribute></xsl:if>
      <xsl:value-of select="@text"/>
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
