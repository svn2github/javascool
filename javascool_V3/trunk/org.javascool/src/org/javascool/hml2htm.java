/* Automatically generated by the makefile: DO NOT EDIT !*/ package org.javascool;
class hml2htm { static String xsl =
                  "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                  "<xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">\n" +
                  "\n" +
                  "  <!-- This XSLT translates HML code to HTML 4 output -->\n" +
                  "\n" +
                  "<xsl:output \n" +
                  "  method=\"html\"\n" +
                  "  encoding=\"UTF-8\"\n" +
                  "  doctype-public=\"-//W3C//DTD HTML 4.01//EN\"\n" +
                  "  doctype-system=\"http://www.w3.org/TR/html4/strict.dtd\"\n" +
                  "  indent=\"yes\" />\n" +
                  "\n" +
                  "<!-- 0 : Page production -->\n" +
                  "\n" +
                  "<xsl:template match=\"/*\">\n" +
                  "  <html>\n" +
                  "    <head>\n" +
                  "      <xsl:for-each select=\"@*\"><xsl:choose>\n" +
                  "        <xsl:when test=\"name(.) = 'title'\"><title><xsl:value-of select=\".\"/></title></xsl:when>\n" +
                  "        <xsl:when test=\"name(.) = 'icon'\"><link rel=\"shortcut icon\" href=\"{.}\"/></xsl:when>\n" +
                  "        <xsl:when test=\"name(.) = 'style'\"><link rel=\"stylesheet\" href=\"{.}\"/></xsl:when>\n" +
                  "        <xsl:when test=\"name(.) = 'script'\"><script type=\"text/javascript\" src=\"{.}\"/></xsl:when>\n" +
                  "        <xsl:otherwise><meta name=\"{name(.)}\" content=\"{.}\"/></xsl:otherwise> \n" +
                  "        <!-- Possible improvement: take <meta http-equiv=\"$name\" content=\"$value\" /> or other <link rel=\"$name\" href=\"$value\" /> constructs into account !-->\n" +
                  "      </xsl:choose></xsl:for-each>\n" +
                  "    </head>\n" +
                  "    <body>\n" +
                  "      <xsl:if test=\"count(@title)=1\"><h1><xsl:value-of select=\"@title\"/>.</h1></xsl:if>\n" +
                  "      <xsl:apply-templates/>\n" +
                  "    </body>\n" +
                  "  </html>\n" +
                  "</xsl:template>\n" +
                  "\n" +
                  "<!-- 1 : Section production -->\n" +
                  "\n" +
                  "<xsl:template match=\"div|p\"><xsl:call-template name=\"div\"/></xsl:template>\n" +
                  "<xsl:template name=\"div\">\n" +
                  "  <xsl:choose>\n" +
                  "    <xsl:when test=\"@class = 'table'\">\n" +
                  "      <p><table align=\"center\" width=\"90%\" cellpadding=\"5px\" bgcolor=\"#eeeeee\"><xsl:call-template name=\"div-2\"/></table><br/></p>\n" +
                  "    </xsl:when>\n" +
                  "    <xsl:when test=\"@class = 'ul' or @class = 'ol'\">\n" +
                  "      <xsl:element name=\"{@class}\"><xsl:call-template name=\"div-2\"/></xsl:element>\n" +
                  "    </xsl:when>\n" +
                  "    <xsl:when test=\"../@class = 'ul' or ../@class = 'ol'\"><li><xsl:call-template name=\"div-2\"/></li></xsl:when>\n" +
                  "    <xsl:when test=\"../@class = 'table'\"><tr><xsl:call-template name=\"div-2\"/></tr></xsl:when>\n" +
                  "    <xsl:when test=\"../../@class = 'table'\"><td valign=\"top\"><xsl:call-template name=\"div-2\"/></td></xsl:when>\n" +
                  "    <xsl:when test=\"@class = 'margin'\"><div align=\"right\"><xsl:call-template name=\"div-2\"/></div></xsl:when>\n" +
                  "    <xsl:when test=\"@class = 'center'\"><div align=\"center\"><xsl:call-template name=\"div-2\"/></div></xsl:when>\n" +
                  "    <xsl:when test=\"@class = 'tag'\"><xsl:call-template name=\"tag\"/></xsl:when>\n" +
                  "    <xsl:otherwise><div><xsl:call-template name=\"div-2\"/></div></xsl:otherwise>\n" +
                  "  </xsl:choose>\n" +
                  "</xsl:template>\n" +
                  "<xsl:template name=\"div-2\">\n" +
                  "  <xsl:if test=\"count(@id)=1\"><xsl:attribute name=\"id\"><xsl:value-of select=\"@id\"/></xsl:attribute></xsl:if>\n" +
                  "  <xsl:if test=\"count(@class)=1\"><xsl:attribute name=\"class\"><xsl:value-of select=\"@class\"/></xsl:attribute></xsl:if>\n" +
                  "  <xsl:if test=\"count(@title)=1\"><xsl:choose>\n" +
                  "    <xsl:when test=\".. = /\"><h2><xsl:value-of select=\"@title\"/>.</h2></xsl:when>\n" +
                  "    <xsl:when test=\"../.. = /\"><h3><xsl:value-of select=\"@title\"/>.</h3></xsl:when>\n" +
                  "    <xsl:when test=\"../../.. = /\"><h4><xsl:value-of select=\"@title\"/>.</h4></xsl:when>\n" +
                  "    <xsl:otherwise><b><xsl:value-of select=\"@title\"/>.</b><xsl:text> </xsl:text></xsl:otherwise>\n" +
                  "  </xsl:choose></xsl:if>\n" +
                  "  <xsl:apply-templates/>\n" +
                  "</xsl:template>\n" +
                  "\n" +
                  "<!-- 1.1 : Pml construct description production -->\n" +
                  "\n" +
                  "<xsl:template name=\"tag\"><table bgcolor=\"#eeeeee\" width=\"90%\">\n" +
                  "  <tr><td colspan=\"4\"><b>{</b><xsl:text> </xsl:text><xsl:value-of select=\"@title\"/></td></tr>\n" +
                  "  <tr><td align=\"center\"><b>name</b></td><td align=\"center\"><b>type</b></td><td align=\"center\"><b>default value</b></td><td></td></tr>\n" +
                  "  <xsl:for-each select=\"param\"><tr>\n" +
                  "     <td><font color=\"#505000\"><xsl:value-of select=\"@name\"/></font></td>\n" +
                  "     <td><font color=\"#990000\"><xsl:value-of select=\"@type\"/></font></td>\n" +
                  "     <td><xsl:choose>\n" +
                  "        <xsl:when test=\"count(@value)=1\"><font color=\"#008000\">\"<xsl:value-of select=\"@value\"/>\"</font></xsl:when>\n" +
                  "        <xsl:otherwise><i>mandatory</i></xsl:otherwise>\n" +
                  "     </xsl:choose></td>\n" +
                  "     <td><xsl:apply-templates/></td>\n" +
                  "  </tr></xsl:for-each>\n" +
                  "  <tr><td colspan=\"4\"><xsl:choose>\n" +
                  "    <xsl:when test=\"count(elements/@type) > 0\">Structure: \"<font color=\"#202080\"><xsl:value-of select=\"elements/@type\"/>\"</font></xsl:when>\n" +
                  "    <xsl:otherwise><i>no element</i></xsl:otherwise>\n" +
                  "  </xsl:choose></td></tr>\n" +
                  "  <tr><td colspan=\"4\"><hr/></td></tr>\n" +
                  "  <tr><td colspan=\"4\">\n" +
                  "    <xsl:for-each select=\"*[name(.) != 'param' and name(.) != 'elements']\"><xsl:apply-templates/></xsl:for-each>\n" +
                  "  </td></tr>\n" +
                  "  <tr><td colspan=\"4\"><b>}</b></td></tr>\n" +
                  "</table></xsl:template>\n" +
                  "\n" +
                  "<!-- 2 : Span production -->\n" +
                  "\n" +
                  "<xsl:template match=\"s\">\n" +
                  "  <span>\n" +
                  "    <xsl:if test=\"count(@class)=1\"><xsl:attribute name=\"class\"><xsl:value-of select=\"@class\"/></xsl:attribute></xsl:if>\n" +
                  "    <xsl:apply-templates/>\n" +
                  "  </span>\n" +
                  "</xsl:template>\n" +
                  "<xsl:template match=\"b\"><b><xsl:apply-templates/></b></xsl:template>\n" +
                  "<xsl:template match=\"i\"><i><xsl:apply-templates/></i></xsl:template>\n" +
                  "<xsl:template match=\"c\"><tt><xsl:apply-templates/></tt></xsl:template>\n" +
                  "<xsl:template match=\"S\"><sup><xsl:apply-templates/></sup></xsl:template>\n" +
                  "<xsl:template match=\"I\"><sub><xsl:apply-templates/></sub></xsl:template>\n" +
                  "\n" +
                  "<!-- 3 : Link production -->\n" +
                  "\n" +
                  "<xsl:template match=\"l\"><xsl:call-template name=\"l\"/></xsl:template>\n" +
                  "<xsl:template name=\"l\">\n" +
                  "  <xsl:choose>\n" +
                  "    <xsl:when test=\"@class = 'replace'\"><script language=\"javascript\">location.replace(\"<xsl:value-of select=\"@link\"/>\");</script></xsl:when>\n" +
                  "    <xsl:when test=\"@class = 'include'\"><xsl:apply-templates select=\"document(@link)\"/></xsl:when>\n" +
                  "    <xsl:when test=\"count(@link) = 1\"><a href=\"{@link}\"> \n" +
                  "      <xsl:if test=\"count(@class)=1\"><xsl:choose>\n" +
                  "        <xsl:when test=\"@class = 'new'\"><xsl:attribute name=\"target\">_blank</xsl:attribute></xsl:when>\n" +
                  "        <xsl:otherwise><xsl:attribute name=\"class\"><xsl:value-of select=\"@class\"/></xsl:attribute></xsl:otherwise>\n" +
                  "      </xsl:choose></xsl:if>\n" +
                  "      <xsl:if test=\"count(@icon) = 1\"><img src=\"{@icon}\" alt=\"{@text}\"/></xsl:if>\n" +
                  "      <xsl:if test=\"count(@text)=1 and count(@icon)=1\"><xsl:text> </xsl:text></xsl:if>\n" +
                  "      <xsl:if test=\"count(@text)=1\"><xsl:value-of select=\"@text\"/></xsl:if>\n" +
                  "      <xsl:if test=\"count(@text)=0 and count(@icon)=0\">[.]</xsl:if>\n" +
                  "    </a></xsl:when>\n" +
                  "    <xsl:when test=\"count(@icon) = 1\"><img src=\"{@icon}\" alt=\"{@text}\">\n" +
                  "      <xsl:if test=\"count(@class)=1\"><xsl:attribute name=\"class\"><xsl:value-of select=\"@class\"/></xsl:attribute></xsl:if>\n" +
                  "    </img></xsl:when>\n" +
                  "    <xsl:when test=\"count(@text) = 1\"><span>\n" +
                  "      <xsl:if test=\"count(@class)=1\"><xsl:attribute name=\"class\"><xsl:value-of select=\"@class\"/></xsl:attribute></xsl:if>\n" +
                  "      <xsl:value-of select=\"@text\"/>\n" +
                  "    </span></xsl:when>\n" +
                  "  </xsl:choose>\n" +
                  "</xsl:template>\n" +
                  "\n" +
                  "<!--- 4: Spurious translation -->\n" +
                  "\n" +
                  "<xsl:template match=\"*\"><xsl:text>\n" +
                  "</xsl:text>  \n" +
                  "<xsl:message>Unexpected tag: <xsl:value-of select=\"name(.)\"/> !!</xsl:message>\n" +
                  "<xsl:element name=\"{name(.)}\">\n" +
                  "  <xsl:for-each select=\"@*\"><xsl:attribute name=\"{name(.)}\"><xsl:value-of select=\".\"/></xsl:attribute></xsl:for-each>\n" +
                  "  <xsl:apply-templates/>\n" +
                  "</xsl:element>\n" +
                  "</xsl:template>\n" +
                  "\n" +
                  "</xsl:stylesheet>\n" +
                  "";
}
