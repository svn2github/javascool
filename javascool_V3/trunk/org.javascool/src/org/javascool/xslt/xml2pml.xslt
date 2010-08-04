<?xml version='1.0' encoding='utf-8'?>
<xsl:stylesheet xmlns:xsl='http://www.w3.org/1999/XSL/Transform' version='1.0'>
<xsl:output method='xml' encoding='utf-8' omit-xml-declaration='yes'/>
<xsl:template match="div">
{div id="<xsl:value-of select="@id"/>" title="<xsl:value-of select="@title"/>" class="<xsl:value-of select="@class"/>"
<xsl:apply-templates/>
}
</xsl:template>
<xsl:template match="div/p">
{p
<xsl:value-of select="."/>
}
</xsl:template>
<xsl:template match="page">
{page title="<xsl:value-of select="@title"/>" author="<xsl:value-of select="@author"/>" email-author="<xsl:value-of select="@email-author"/>" tags="<xsl:value-of select="@tags"/>" desc="<xsl:value-of select="@desc"/>" css="<xsl:value-of select="@css"/>" javascript="<xsl:value-of select="@javascript"/>"
<xsl:apply-templates/>
}
</xsl:template>

</xsl:stylesheet>
