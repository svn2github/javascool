<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:sx="http://icl.com/saxon" extension-element-prefixes="sx">

  <!-- This XSLT translates HML code to HTML 4 output -->

<xsl:output 
  method="html"
  encoding="UTF-8"
  doctype-public="-//W3C//DTD HTML 4.01//EN"
  doctype-system="http://www.w3.org/TR/html4/strict.dtd"
  indent="yes" />

  <!-- This XSLT translates the proglet's HML documentation to HTML3.2 http://www.w3.org/TR/REC-html32-19970114) -->

  <sx:function name="sx:jvs2htm" xmlns:jvs2htm="java:org.javascool.builder.Jvs2Html">
    <xsl:param name="string"/>
    <sx:return select="jvs2htm:run($string)"/>
  </sx:function>
  
  <xsl:param name="output" select="'jvs'"/>

<!-- These tags produce javasccool's doc specific constructs -->

<xsl:template match="div|p">
  <xsl:choose>
    <xsl:when test="@class = 'sujet'">
      <div align="right">
        <xsl:if test ="count(intros) > 0">[<a href="#intros">introduction</a>]</xsl:if>
        <xsl:if test ="count(works) > 0">[<a href="#works">travail proposé</a>]</xsl:if>
        <xsl:if test ="count(notes) > 0">[<a href="#notes">remarques</a>]</xsl:if>
      </div>
      <xsl:call-template name="div"/>
    </xsl:when>
    <xsl:when test="@class = 'objectif'"><h2>Objectif</h2><xsl:call-template name="div"/></xsl:when>
    <xsl:when test="@class = 'intros' or @class = 'works' or @class = 'notes'">
      <xsl:choose>
        <xsl:when test="@class = 'intros'"><h2>Introduction</h2></xsl:when>
        <xsl:when test="@class = 'works'"><h2>Travail proposé</h2></xsl:when>
        <xsl:when test="@class = 'notes'"><h2>Remarques</h2></xsl:when>
      </xsl:choose>
      <div id="{@class}"><ol><xsl:for-each select="*"><li><xsl:call-template name="div-2"/></li></xsl:for-each></ol></div>
    </xsl:when>
    <xsl:when test="@class = 'code'"><div class="code"><table witdh="90%" border="1" align="center"><tr><td><xsl:call-template name="div"/></td></tr></table></div></xsl:when>
    <xsl:otherwise><xsl:call-template name="div"/></xsl:otherwise>
  </xsl:choose>
</xsl:template>

<xsl:template match="l">
  <xsl:choose>
    <xsl:when test="@class = 'javascool'"><tt><a href="http://javascool.gforge.inria.fr" style="padding:0;margin:0;text-decoration:none">Java'sCool</a></tt></xsl:when>
    <xsl:when test="@class = 'note'"><sup><a href="{concat('#', @link)}"><xsl:value-of select="@link"/></a></sup></xsl:when>
    <xsl:when test="(@class = 'editor' or @class = 'newtab') and $output = 'jvs'"> 
    <a href="{concat('http://',@class,'?', @link)}"><xsl:value-of select="@text"/></a>
   </xsl:when>
   <xsl:when test="@class = 'editor'"> 
      <a class="{@class}" href="{concat(@link,'.html')}"><xsl:value-of select="@text"/></a>
   </xsl:when>  
   <xsl:otherwise><xsl:call-template name="l"/></xsl:otherwise>
  </xsl:choose>
</xsl:template>

<!-- These tags performs the Jvs2Htm conversion -->

<xsl:template match="code">
  <div class="code"><table><tr><td>
    <xsl:value-of disable-output-escaping="yes" select="sx:jvs2htm(.)"/>
  </td></tr></table></div>
</xsl:template>

<!-- These tags allows to show pieces of code -->

<xsl:template match="r">
  <font color="#990000"><b><xsl:apply-templates/></b></font>
</xsl:template>

<xsl:template match="n">
  <font color="#505000"><xsl:apply-templates/></font>
</xsl:template>

<xsl:template match="v"> 
  <xsl:choose>
    <xsl:when test="$output = 'jvs'">
      <font color="#008000">&quot;<xsl:apply-templates/>&quot;</font>
    </xsl:when>
    <xsl:otherwise>
      <font color="#00FF00">&quot;<xsl:apply-templates/>&quot;</font>
    </xsl:otherwise>
  </xsl:choose>
</xsl:template>

<xsl:template match="m">
  <p><tt><font color="#202080"><big>//</big>&#160;<xsl:apply-templates/></font></tt></p>
</xsl:template>

<xsl:template match="T">
  &#160;&#160;&#160;
</xsl:template>

<!-- Here we manually include hml2htm.xslt to avoid file access -->

<!-- 0 : Page production -->

<xsl:template match="/*">
  <html>
    <head>
      <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
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

<xsl:template name="div">
  <xsl:choose>
    <xsl:when test="@class = 'table'">
      <p><table align="center" width="90%" cellpadding="5px" bgcolor="#eeeeee"><xsl:call-template name="div-2"/></table><br/></p>
    </xsl:when>
    <xsl:when test="@class = 'ul' or @class = 'ol'">
      <xsl:element name="{@class}"><xsl:call-template name="div-2"/></xsl:element>
    </xsl:when>
    <xsl:when test="../@class = 'ul' or ../@class = 'ol'"><li><xsl:call-template name="div-2"/></li></xsl:when>
    <xsl:when test="../@class = 'table'"><tr><xsl:call-template name="div-2"/></tr></xsl:when>
    <xsl:when test="../../@class = 'table'"><td valign="top"><xsl:call-template name="div-2"/></td></xsl:when>
    <xsl:when test="@class = 'margin'"><div align="right"><xsl:call-template name="div-2"/></div></xsl:when>
    <xsl:when test="@class = 'center'"><div align="center"><xsl:call-template name="div-2"/></div></xsl:when>
    <xsl:when test="@class = 'tag'"><xsl:call-template name="tag"/></xsl:when>
    <xsl:otherwise><div><xsl:call-template name="div-2"/></div></xsl:otherwise>
  </xsl:choose>
</xsl:template>
<xsl:template name="div-2">
  <xsl:if test="count(@id)=1"><xsl:attribute name="id"><xsl:value-of select="@id"/></xsl:attribute></xsl:if>
  <xsl:if test="count(@class)=1"><xsl:attribute name="class"><xsl:value-of select="@class"/></xsl:attribute></xsl:if>
  <xsl:if test="count(@id)=1"><a name="{@id}"></a></xsl:if>
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

<xsl:template name="l">
  <xsl:choose>
    <xsl:when test="@class = 'replace'"><script language="javascript">location.replace("<xsl:value-of select="@link"/>");</script></xsl:when>
    <xsl:when test="@class = 'include'"><xsl:apply-templates select="document(@link)"/></xsl:when>
    <xsl:when test="count(@link) = 1"><a href="{@link}"> 
      <xsl:if test="count(@class)=1"><xsl:choose>
        <xsl:when test="@class = 'newtab'"><xsl:attribute name="target">_blank</xsl:attribute></xsl:when>
        <xsl:otherwise><xsl:attribute name="class"><xsl:value-of select="@class"/></xsl:attribute></xsl:otherwise>
      </xsl:choose></xsl:if>
      <xsl:if test="count(@icon) = 1"><img src="{@icon}" alt="{@text}"/></xsl:if>
      <xsl:if test="count(@text)=1 and count(@icon)=1"><xsl:text> </xsl:text></xsl:if>
      <xsl:if test="count(@text)=1"><xsl:value-of select="@text"/></xsl:if>
      <xsl:if test="count(@text)=0 and count(@icon)=0">[.]</xsl:if>
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

<!--- 4: Code copy

<xsl:template match="code">
  <xsl:apply-templates mode ="code"/>
</xsl:template>
    
<xsl:template match="*" mode="code"><xsl:text>
</xsl:text>  
  <xsl:element name="{name(.)}">
  <xsl:for-each select="@*"><xsl:attribute name="{name(.)}"><xsl:value-of select="."/></xsl:attribute></xsl:for-each>
  <xsl:apply-templates/>
</xsl:element>
</xsl:template>
 -->
 
<!--- 5: Spurious translation -->

<xsl:template match="*"><xsl:text>
</xsl:text>  
<!--xsl:message>Unexpected tag: <xsl:value-of select="name(.)"/> !!</xsl:message-->
<xsl:element name="{name(.)}">
  <xsl:for-each select="@*"><xsl:attribute name="{name(.)}"><xsl:value-of select="."/></xsl:attribute></xsl:for-each>
  <xsl:apply-templates/>
</xsl:element>
</xsl:template>

</xsl:stylesheet>
