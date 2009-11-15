<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<!-- This tag defines how to produce bms specification's tag -->

<xsl:template match="bml">
  <table align="center" width="90%" border="1"><tr><td>
  <xsl:value-of select="@title"/><br/>
  <xsl:choose>
    <!-- Parameter table -->
    <xsl:when test="count(@name) > 0">
      <b><xsl:value-of select="@name"/>{ .. } :</b>
      <table align="center" width="90%" bgcolor="#FFEEFF">
        <tr>
          <th align="left" width="5%"><xsl:if test="count(bml/@abbrv) > 0"><xsl:attribute name="colspan">2</xsl:attribute></xsl:if>Name</th>
          <th align="left" width="5%">Type</th>
          <xsl:if test="count(bml/@value) > 0"><th align="left" width="5%">Value</th></xsl:if>
          <th align="left">Description</th>
        </tr>
        <xsl:for-each select="bml">
          <tr>
            <td><b><xsl:value-of select="@name"/></b></td>
            <xsl:if test="count(../bml/@abbrv) > 0"><td><xsl:if test="count(@abbrv) > 0">(<b><tt><xsl:value-of select="@abbrv"/></tt></b>)</xsl:if></td></xsl:if>
            <td><i><xsl:value-of select="@type"/></i></td>
            <xsl:if test="count(../bml/@value) > 0"><td><i><xsl:value-of select="@value"/></i></td></xsl:if>
            <td><xsl:apply-templates/></td>
          </tr>
        </xsl:for-each>
      </table>
    </xsl:when>
    <xsl:when test="@class = 'abbrv'">
      <table align="center" width="90%" bgcolor="#FFEEFF">
        <tr>
          <th align="left" width="5%">Name</th>
          <th align="left">Description</th>
        </tr>
        <xsl:for-each select="bml">
          <tr>
            <td><b><xsl:value-of select="@name"/></b></td>
            <td><xsl:apply-templates/></td>
          </tr>
        </xsl:for-each>
      </table>
    </xsl:when>
    <xsl:otherwise>
      BML?????
    </xsl:otherwise>
  </xsl:choose>
  </td></tr></table>
</xsl:template>

</xsl:stylesheet>
