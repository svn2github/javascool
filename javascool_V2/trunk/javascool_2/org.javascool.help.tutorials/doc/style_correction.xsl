<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:output 
  method="html"
  encoding="ISO-8859-1"
  doctype-public="-//W3C//DTD HTML 4.01//EN"
  doctype-system="http://www.w3.org/TR/html4/strict.dtd"
  indent="yes" />
  
	<xsl:template match="correction">

	<html>
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"/>
			<LINK rel="stylesheet" type="text/css" href="../../doc/style.css"/>	
		</head>

		<body>
			<div id="banner">
				<img src="../../doc/help_banner.jpg" alt="Help banner"/>
			</div>

      		<xsl:apply-templates select="*" />

		</body>

		</html>
	</xsl:template >


	<!-- traitment of title-->
	<xsl:template match="titre" >
		<h1><xsl:value-of select="." /></h1><br/><br/><br/>
	</xsl:template >
	
	<!-- traitment of infos-->
	<xsl:template match="info" >
		<div id="content">
			<p>
				<xsl:value-of select="." />
			</p>
			<br/>
		</div>
		<br/>
		
	</xsl:template >
	
	<!-- traitment of answers-->
	<xsl:template match="answers" >
		<div class="soustitre"><p>les fichiers correspondants aux corrections : </p></div>	
		<br/>
		<xsl:apply-templates select="answer" />
		
	</xsl:template >
	
	
	<!-- traitment of answer-->
	<xsl:template match="answer" >
		<div id="content">
			<p>
				<a href="{href}">
					<xsl:value-of select="@name" />
				</a>
					<xsl:text>  </xsl:text><xsl:value-of select="@sup" />
			</p>	
		</div>
		<br/>
		
	</xsl:template >
	
</xsl:stylesheet>