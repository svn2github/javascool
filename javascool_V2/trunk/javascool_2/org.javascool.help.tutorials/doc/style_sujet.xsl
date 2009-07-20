<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:output 
  method="html"
  encoding="ISO-8859-1"
  doctype-public="-//W3C//DTD HTML 4.01//EN"
  doctype-system="http://www.w3.org/TR/html4/strict.dtd"
  indent="yes" />
  
	<xsl:template match="sujet">

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
	
	<!-- traitment of objectif-->
	<xsl:template match="objectif" >
		<div class="soustitre"><p>Objectif : </p></div>	
		<br/>
		
		<div id="content">
			<P><xsl:value-of select="." /></P>
		</div>
		<br/>	
	</xsl:template >
	
	<!-- traitment of notes-->
	<xsl:template match="notes" >
		<div class="soustitre"><p>Notes : </p></div>	
		<br/>
		<div id="content">
			<xsl:apply-templates select="note" />
		</div>
		<br />
	</xsl:template >
	
	<!-- traitment of note-->
	<xsl:template match="note" >
		<P><xsl:value-of select="." /></P>
	</xsl:template >
	
	<!-- traitment of work-->
	<xsl:template match="work" >
		<div class="soustitre"><p>Travaille demandé :</p></div>
		<br/>
		<div id="content">
			<xsl:value-of select="." />
		</div>
	</xsl:template >
	
	<!-- traitment of footnote-->
	<xsl:template match="footnote" >
		<xsl:value-of select="." />
	</xsl:template >
	
</xsl:stylesheet>