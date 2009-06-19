<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

  <xsl:output method="xml"/>

  <!-- These tags are treated in the output -->

  <xsl:template match="tr">
    { "<xsl:value-of select="td[1]/a/img/@alt"/>", "<xsl:value-of select="td[1]/a/img/@src"/>", "<xsl:value-of select="td[2]/a/@href"/>" },
  </xsl:template>

  <!-- These tags, but not their contents, are not skipped in the output -->

  <xsl:template match="tbody"><xsl:apply-templates/></xsl:template>

  <!-- These tags are removed in the output -->

  <xsl:template match="*"/>

</xsl:stylesheet>
