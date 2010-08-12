<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:output 
  method="html"
  encoding="UTF-8"
  doctype-public="-//W3C//DTD HTML 4.01//EN"
  doctype-system="http://www.w3.org/TR/html4/strict.dtd"
  indent="yes" />

<!--- Defines the about files tages -->
  
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
      <xsl:apply-templates/>
    </body>
  </html>
</xsl:template>

<xsl:template match="p">
  <xsl:if test="count(@title)=1"><xsl:choose>
    <xsl:when test=".. = /"><h2><xsl:value-of select="@title"/>.</h2></xsl:when>
    <xsl:when test="../.. = /"><h3><xsl:value-of select="@title"/>.</h3></xsl:when>
    <xsl:when test="../../.. = /"><h4><xsl:value-of select="@title"/>.</h4></xsl:when>
    <xsl:otherwise><b><xsl:value-of select="@title"/>.</b><xsl:text> </xsl:text></xsl:otherwise>
  </xsl:choose></xsl:if>
  <div class="text">
    <xsl:if test="count(@align)=1"><xsl:attribute name="align"><xsl:value-of select="@align"/></xsl:attribute></xsl:if>
    <xsl:apply-templates/>
  </div>
</xsl:template>

<!--- Defines the subject description files tages -->

<xsl:template match="sujet">
  <html>
    <head>
      <link rel="stylesheet" type="text/css" href="img/style.css"/>	
      <title>JavaScool . . and Java is so cool.</title>	  
      <link rel="shortcut icon" href="img/icon.jpg"/>
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
  <div align="right">[<a href="#notes">introduction</a>] [<a href="#works">travail proposé</a>] [<a href="#footnotes">remarques</a>] [<a href="about-memo.htm">mémo des fonctions</a>]</div><br/><br/>
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
  
<xsl:template name="banner">
  <table width="100%" style="border:0;margin:0;padding:0;background-image:url('img/bg-banner.jpg');" ><tr style="border:0;margin:0;padding:0">
    <td width="33%" style="border:0;padding:0;margin:0">
      <a style="padding:0;margin:0;text-decoration:none" href="http://javascool.gforge.inria.fr"><img height="36" width="150" src="img/logo-javascool.jpg"/></a></td>
    <td width="33%" align="center" style="border:0;padding:0;margin:0">
      <a style="padding:0;margin:0;text-decoration:none" href="about-all.htm#4"><img height="36" src="img/logo-lycees.jpg"/></a></td>
    <td width="33%" align="right" style="border:0;padding:0;margin:0">      
      <a style="padding:0;margin:0;text-decoration:none" href="http://www.inria.fr"><img height="36" src="img/logo-inria.jpg"/></a>
    </td>
  </tr></table>
</xsl:template>

<!--- Defines the general files tages -->

<xsl:template match="goto">
  <script language="javascript">location.replace("<xsl:value-of select="@href"/>");</script>
</xsl:template>

<xsl:template match="javascool">
  <tt><a style="padding:0;margin:0;text-decoration:none" href="http://javascool.gforge.inria.fr">Java'sCool</a></tt>
</xsl:template>

<!-- These tag defines proglet's links and embedding,
     Usage: <proglet name="proglet-name" mode="demo">proglet description</proglet> to embed the proglet demo to describe how it works.
     Usage: <proglet name="proglet-name" mode="edit"/> to insert a link towards the javascool code editor fot this proglet.
     Usage: <proglet name="proglet-name"/> to insert a link towards the proglet description page.
-->

<xsl:template match="proglet">
  <xsl:choose>
    <xsl:when test="@mode='demo'">
      <table><tr><td valign="top" width="560"><xsl:apply-templates/></td><td valign="top">
        <applet code="proglet.InterfacePrincipale.class" archive="../wproglet.jar" width="560" height="720">
          <param name="proglet" value="{@name}"/>
        </applet>
      </td></tr></table>
    </xsl:when>
    <xsl:when test="@mode='edit'">
      <div align="right">
        Utiliser la <i>«proglet» </i> <tt><a href="{concat('http://facets.inria.fr/javascool?prog=',@name)}"><src img="img/execute.png"/><xsl:value-of select="@name"/></a></tt>.
      </div>
    </xsl:when>
    <xsl:otherwise>  
    <i>«proglet» </i> <tt><a href="{concat('about-proglet-',@name,'.htm')}"><xsl:value-of select="@name"/></a></tt>
    </xsl:otherwise>
  </xsl:choose>
</xsl:template>

<!-- These tags define the footnoting mechanism -->

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

<xsl:template match="ul|ol|li|table|tr|td|span|sup|i|b|br|a|img|form|input|select|option|hr"><xsl:text>
</xsl:text>
<xsl:element name="{name(.)}">
  <xsl:for-each select="@*"><xsl:attribute name="{name(.)}"><xsl:value-of select="."/></xsl:attribute></xsl:for-each>
  <xsl:apply-templates/>
</xsl:element>
</xsl:template>

<xsl:template match="*">
  <xsl:message>Undefined tag: <xsl:value-of select="name(.)"/> !!</xsl:message>
</xsl:template>

</xsl:stylesheet>
