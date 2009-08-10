<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:output 
  method="html"
  encoding="ISO-8859-1"
  doctype-public="-//W3C//DTD HTML 4.01//EN"
  doctype-system="http://www.w3.org/TR/html4/strict.dtd"
  indent="yes" />
  
<xsl:template match="proglet">
  <div align = "center">
    <applet code="proglet.InterfacePrincipale.class" archive="../proglet.jar" width="920" height="720">
      <param name="proglet" value="{@name}"/>
      <!--param name="cache_archive" value="http://interstices.info/upload/hamdi/tools.jar"/-->
    </applet>
  </div>
</xsl:template>

</xsl:stylesheet>
