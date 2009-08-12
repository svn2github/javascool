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
    <script language="javascript">
      // The function window.query("name") allows to get a http:location?name=value query parameter
      window.query = function(name) {
        var q = window.location.search.substring(1).split("&amp;");  
        for (var i = 0; i &lt; q.length; i++) {  
          var p = q[i].split("=");  
          if (p[0] == name) return p[1];
        }
       } 
    </script>
    <applet code="proglet.InterfacePrincipale.class" archive="../proglet.jar" width="920" height="720">
      <param name="proglet" value="{@name}"/>
    </applet>
  </div>
</xsl:template>

</xsl:stylesheet>
