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
			<h1 class="title1"> Gforge INRIA</h1>
            
	<br />
	<br />
			<h2 class="title2"> Liens Pratiques </h2>
            <p class="content"><br/>
			<br/>
			<br/>
              Le projet est sous SVN, il peut etre récupéré dans un shell par la commande : <br />
              svn checkout svn://scm.gforge.inria.fr/svn/javascool. <br />
              <br />             
                <p class="content">
            	<br />
                <br />
				Le projet est installé sur la gforge INRIA, on peut acceder à son domaine par l'adresse : <br/>
            <a href="https://gforge.inria.fr/projects/javascool/" >https://gforge.inria.fr/projects/javascool/ </a>. </p>
            <p class="content">&nbsp;</p>
            </p>
			<br/>
			<h2 class = "title2"> Notes pour les développeurs </h2>
			<p class="content">
            	<br />
                <br />
				Plug-in Orphy : <br/>
					Pour que le plug in soit opérationnel après avoir checkout le projet, il faut construire le fichier stub nécessaire pour RMI du coté client(il est créé par copie automatiquement du coté serveur), en effet celui ci est effacé par le système de sauvegarde.<br />
					Pour construire le stub :<br />
					<ul>
					<li>se placer au niveau du dossier bin dans le plugin orphy</li>
					<li>dans un shell, faire la commande : "rmic org.unice.javascool.orphy.Orphy".</li>
					</ul>

            <p class="content">&nbsp;</p>
		</div>
		<!-- the right of the web pages-->
		<?php include("page_right.html")?>
		

		
		<!-- the foot page -->
		<?php include("foot.html")?>
	</div>
</div>
</body>
</html>
