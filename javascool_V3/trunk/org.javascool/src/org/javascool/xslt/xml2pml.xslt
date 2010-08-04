<?xml version='1.0' encoding='utf-8'?>
<xsl:stylesheet xmlns:xsl='http://www.w3.org/1999/XSL/Transform' version='1.0'>
	<xsl:output method='xml' encoding='utf-8' omit-xml-declaration='yes'/>
	<xsl:template match="div">
	{ div id="<xsl:value-of select="@id"/>" title="<xsl:value-of select="@title"/>" class="<xsl:value-of select="@class"/>"
	<xsl:value-of select="."/>
	}
	</xsl:template>
	<xsl:template match="*">
	{<xsl:value-of select='name(.)'/><xsl:text> </xsl:text>
	<xsl:for-each select='@*'><xsl:value-of select='name(.)'/>="<xsl:value-of select="."/>"<xsl:text> </xsl:text></xsl:for-each>
	<xsl:value-of select="."/>	
	}
	</xsl:template>
</xsl:stylesheet>
