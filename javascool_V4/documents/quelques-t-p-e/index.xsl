<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:saxon="http://icl.com/saxon" extension-element-prefixes="saxon" version="1.0">

  <xsl:output method="html"/>

  <xsl:template match="/"><xsl:comment> AUTOMATICALLY GENERATED: DO NOT EDIT ! </xsl:comment><xsl:apply-templates/></xsl:template>

  <xsl:template match="items"><p><table border="1"><xsl:apply-templates/></table></p></xsl:template>

  <xsl:template match="item"><tr class="{@class}"><xsl:apply-templates/></tr></xsl:template>

  <xsl:template match="title|target|type|what|tool"><td id="{name(.)}"><xsl:apply-templates/></td></xsl:template>

  <xsl:template match="name|mail|when|where|need"/>

  <xsl:template match="tpes"><big><big><xsl:value-of select="@title"/></big></big><xsl:apply-templates/></xsl:template>

  <xsl:template match="tpe"><table width="100%">
  <tr><td valign="top" width="120" rowspan="4"><img src="{@icon}" width="120" height="120"/></td><td><b>Problématique:</b></td><td><b><i><u><xsl:value-of select="@title"/></u></i></b></td></tr>
  <tr><td><b>Thème</b>:</td><td><i><xsl:choose>
      <xsl:when test="@theme = 'A'">-A- : Modélisation des phénomènes physiques</xsl:when>
      <xsl:when test="@theme = 'B'">-B- : Sciences, informatique et société</xsl:when>
      <xsl:when test="@theme = 'C'">-C- : Sciences de la vie et de l'information</xsl:when>
      <xsl:when test="@theme = 'D'">-D- : Au coeur des Sciences et Technologies de l'Information</xsl:when>
      <xsl:otherwise>???????????</xsl:otherwise>
    </xsl:choose></i></td></tr>
    <xsl:if test="count(text()|*[name(.) != 'link']) > 0">
      <tr><td><b>Eléments <br/> de cadrage:</b></td><td><xsl:apply-templates select="text()|*[name(.) != 'link']"/></td></tr>
    </xsl:if>
    <xsl:if test="count(link) > 0">
      <tr><td><b>Ressources:</b></td><td><ul><xsl:for-each select="link"><li><xsl:apply-templates select="."/></li></xsl:for-each></ul></td></tr>
    </xsl:if>
  </table><hr/></xsl:template>

  <xsl:template match="autres-tpes"><br/><br/><big><big>Autres idées de sujets</big> (elles seront développées sur <a href="mailto:science-participative@sophia.inria.fr?subject=A_propos_de_TPEs">simple demande</a>)</big><br/><br/></xsl:template>

  <xsl:template match="link"><div><a target="_blank" href="{@l}"><xsl:value-of select="saxon:if(count(@n)>0, @n, @l)"/></a></div></xsl:template>

  <xsl:template match="a"><a target="_blank" href="{@href}"><xsl:value-of select="."/></a></xsl:template>

  <xsl:template match="*|@*"><xsl:copy><xsl:apply-templates select="@*"/><xsl:apply-templates/></xsl:copy></xsl:template>

</xsl:stylesheet>
