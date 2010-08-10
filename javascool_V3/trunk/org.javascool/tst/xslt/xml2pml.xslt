<?xml version='1.0' encoding='utf-8'?>
<!--
                        XML2PML
      ______________________________________________
     | By Philippe Vienne <philoumailabo@gmail.com> |
     | Distrubuted on GNU General Public Licence    |
     | Revision 1                                   |
     | © 2010 INRIA, All rights reserved            |
     |______________________________________________|

-->
<xsl:stylesheet xmlns:xsl='http://www.w3.org/1999/XSL/Transform' version='1.0'>
<xsl:output method='xml' encoding='utf-8' omit-xml-declaration='yes'/>
<!-- Page Balise -->
<xsl:template match="page">{page title="<xsl:value-of select="@title"/>" author="<xsl:value-of select="@author"/>" email-author="<xsl:value-of select="@email-author"/>" tags="<xsl:value-of select="@tags"/>" desc="<xsl:value-of select="@desc"/>" css="<xsl:value-of select="@css"/>" javascript="<xsl:value-of select="@javascript"/>"<xsl:apply-templates/>}</xsl:template>
<!-- Div Balise -->
<xsl:template match="div">{div id="<xsl:value-of select="@id"/>" title="<xsl:value-of select="@title"/>" class="<xsl:value-of select="@class"/>"
<xsl:apply-templates/>
}</xsl:template>
<!-- P Balise -->
<xsl:template match="p">
{p
<xsl:apply-templates/>
}</xsl:template>
<!-- L Balise -->
<xsl:template match="l">{l text="<xsl:value-of select="@text"/>" img="<xsl:value-of select="@img"/>" link="<xsl:value-of select="@link"/>" class="<xsl:value-of select="@class"/>"}</xsl:template>
<!-- S Balise-->
<xsl:template match="s">{s class="<xsl:value-of select="@class"/>"<xsl:apply-templates/>}</xsl:template>
<!-- Style (i,b,u) Balises -->
<xsl:template match="i|b|u">{<xsl:value-of select="name(.)"/> <xsl:value-of select="."/>}</xsl:template>
<!-- END -->
</xsl:stylesheet>
