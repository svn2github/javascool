<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="fr" lang="fr">
<head>
	<title>Java's Cool</title> 
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta http-equiv="Pragma" content="no-cache" />
	<meta http-equiv="Content-Style-Type" content="text/css" />
	<meta http-equiv="Content-Script-Type" content="text/javascript" />
	<meta http-equiv="Content-Language" content="fr" />
	<meta name="robots" content="index,follow" />

	<link href="./styles.css" type="text/css" rel="stylesheet" />
</head>
<body>
<div id="pageMain">
	<div id="pageObject">
		
		<?php include("header.html"); ?>
		
		<div id="pageContent">
			<h1 class="title1"> Download </h1>
	           	<br />
                <br />
                
            <h2 class = "title2">    Derniere version de Java's Cool : </h2>
                <br />
                <br />
                <a href="fichiers/jjavascool_1.0.1-installer.jar" target="_blank"> Windows</a> 
                
                <br />
				<br />
                <a href="fichiers/javascool-linux-installer.jar" target="_blank"> Linux </a>
				
				<br />
				<br />
			<h2 class = "title2"> Notes </h2>
			<br />
			
			<p class="content"> Version Linux : 
					<ul>
					<li>non d&eacute;finitive, probl&egrave;	me de rapidit&eacute; de l'application sur certains ordinateurs.</li>
					<li>impl&eacute;mentation du plug-in Orphy &agrave; venir.</li>
					</ul>
			
			<br />
			Version Windows : manipulations &agrave; faire si le plug-in Orphy bloque l'application :
			 		<ul>
					<li>D&eacute;sactiver les p&eacute;rif&eacute;riques "COM"(autre que celui où est branch&eacute; Orphy GTS 2) dans le "gestionnaire des p&eacute;rif&eacute;riques" pour être sur que	Java's cool trouve l'Orphy GTS 2.</li>
					<li>Copier les fichiers rxtxSerial.dll et RXTXcomm.jar situ&eacute;s dans "C:\Program File\javascool\plugins\org.unice.javascool.orphy\lib"
					<ul>
					<li> rxtxSerial.dll dans le dossier bin du jre install&eacute;, par exemple dans : C:\Program File\java\jre6\bin. </li>
					<li> RXTXcomm.jar dans le dossier lib\ext du jre install&eacute;, par exemple dans : C:\Program File\java\jre6\lib\ext. </li>
					</ul>
					</li>
					</ul>
					Les chemins d'acc&egrave;s ne sont donn&eacute;s qu'&agrave; titre indicatifs, si vos installations sont diff&eacute;rentes, pri&egrave;re de modifier en cons&eacute;quence.
            
		</div>
		
		<!-- the right of the web pages-->
		<?php include("page_right.html")?>
		

		
		<!-- the foot page -->
		<?php include("foot.html")?>
		
		
	</div>
</div>
</body>
</html>
