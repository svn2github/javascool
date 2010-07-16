<?php
/*
/////////////////////////////////////////////////////////////
// Script sous licence CeCILL                              //
// Auteur : Philippe Vienne et Thierry Viéville, INRIA Sop //
/////////////////////////////////////////////////////////////
/
/
/ Mécanisme de syndication des pages web à partir de http://wiki.inria.fr/sciencinfolycee
/
/ Usage: http://javascool.gforge.inria.fr/?page=<page>
*/
$page = isset($_GET['page'])) ? $_GET['page'] : "Accueil";
// Recuperation de la page sur le wiki
$page = file_get_contents('http://wiki.inria.fr/sciencinfolycee/'.$page.'?printable=yes&action=render');
// Remplace tous les liens entre page wiki par des pages vues du site
$page = ereg_replace('href="http://wiki.inria.fr/sciencinfolycee/', 'href="?page=', $page);
// Remplace tous les liens wikis locaux pas des liens distants
$page = ereg_replace('src="/wikis/sciencinfolycee', 'src="http://wiki.inria.fr/wikis/sciencinfolycee', $page); 
// Si le wiki signale une erreur alors on affiche proprement une page en erreur
if (ereg("<title>Erreur</title>", $page))
  $page="<h1>Erreur 403 - Accès interdit</h1><a href=\"javascript:history.back()\">Revenir en arri&egrave;re</a>";
?>
<!DOCTYPE html PUBliC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/Dtd/xhtml1-transitional.dtd"> 
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="fr" lang="fr"> 
<head> 
	<title>Java's Cool - <? echo $page; ?></title> 
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /> 
	<meta http-equiv="Pragma" content="no-cache" /> 
	<meta http-equiv="Content-Style-Type" content="text/css" /> 
	<meta http-equiv="Content-Script-Type" content="text/javascript" /> 
	<meta http-equiv="Content-Language" content="fr" /> 
	<meta name="robots" content="index,follow" /> 
 
	<link href="styles/style.css" type="text/css" rel="stylesheet" /> 
    <liNK href="styles/style_menu_right.css" rel="stylesheet" type="text/css">
</head> 
<body>
<div class="greybox_right">
	<div class="greybox_left">
	<div class="greybox_top">
		<div class="greybox_top_l">
		<div class="greybox_top_r">
		<div class="greybox_bottom">
			<div class="greybox_bottom_l">

			<div class="greybox_bottom_r">
				<div> 
					<CENTER>
					<a href="index.html"><img src="images/logo.gif" alt="logo java's Cool" border=0></a>
					<a href="index.html"><img src="images/logo_unice.gif" alt="logo université de Nice" border=0 align="left" height="5%"></a>
					&nbsp;&nbsp;&nbsp;
					<a href="index.html"><img src="images/logo_inria.gif" alt="logo INRIA" border=0 height="60px" align="right"></a>
					</CENTER>
				</div> 
				
			</div> 
			</div>
		</div>
		</div> 
		</div>
	</div>
	</div>
</div>
<!--the menu barre--> 
<div class="greybox_right">
	<div class="greybox_left">
		<div class="greybox_top">
		<div class="greybox_top_l">
		<div class="greybox_top_r">
			<div class="greybox_bottom">
			<div class="greybox_bottom_l">
			<div class="greybox_bottom_r">
				<div class="menu">
					<ul> 
						<li><a href="?page=Accueil">Accueil</a></li>
						<li><a href="?page=download">T&eacute;l&eacute;chargement</a></li> 
						<li><a href="?page=manuels">Ressources</a></li> 
						<li><a href="?page=dev">D&eacute;veloppement</a></li> 
						<li><a href="?page=contacts">Contacts</a></li> 
					</ul>
				</div>
			</div>
			</div>
			</div>
		</div>
		</div>
		</div>
	</div>
</div>	
<table>
	<tr>
	<tr> 
	<td valign="top"> 
			<div class="menuright">
				<div class="menuright_top">
				<div class="menuright_right">
				<div class="menuright_bottom">
					<!-- RSS Link
					<ul class="rss">
						<li><img src="images/rss.gif" height="16" width="16" border="0" class="rss_icon" /><a href="rss.xml" title="Index">&nbsp;&nbsp;&nbsp;&nbsp;s'abonner</a></li>
					</ul>
					<!-- TODO Remove RSS -->
					<!-- Menu Start -->
					<h3>General</h3>
					<ul>
						<li><a href="?page=accueil" title="Index">Accueil</a></li>
						<li><a href="?page=news" title="Nouveauté">News</a></li>
						<li><a href="./proglet/doc/about-faq.htm" title="FAQ">FAQ</a></li>
						<li><a href="?page=download" title="T&eacute;l&eacute;chargement">T&eacute;l&eacute;chargement</a></li>
						<li><a href="./proglet/doc/about-autres.htm" title="Ailleurs aussi">Ailleurs aussi ...</a></li>
					</ul>
					<h3>Recherche</h3>
					<div>
						<form action="http://www.google.com/search" method="get" style="font-size:10px;">
							<input name="ie" type="hidden" value="UTF-8"></input>
							<input name="oe" type="hidden" value="UTF-8"></input>
							<input maxlength="255" name="q" size="15" type="text" value=""></input>
							<input class="buttonSearch" name="btnG" type="submit" value="Rechercher" ></input>
							<input name="domains" type="hidden" value="http://javascool.gforge.inria.fr"></input>
							<input name="sitesearch" type="hidden" value="http://javascool.gforge.inria.fr"></input>
						</form>
					</div>
					<h3>T&eacute;l&eacute;chargement</h3>
					<ul>
						<li><a href="?page=download" title="Toutes les versions">Toutes les versions </a></li>
					</ul>
					<h3>Ressource</h3>
					<ul>
						<li><a href="?page=manuels" title="Manuels d'utilisation">Manuels d'utilisation</a></li>
						<li><a href="?page=manuels" title="Tutoriels">Tutoriels</a></li>
						<li><a href="?page=screenshot" title="">Screenshots</a></li>
						
					</ul>
					<h3>D&eacute;veloppement</h3>
					<ul>
						<li><a href="?page=licence" title="">Licence</a></li>
						<li><a href="?page=forge" title="">Sourceforge de L'INRIA</a></li>
						<li><a href="?page=rapport" title="">Rapport de TER</a></li>
						
					</ul>
					<h3>Contacts</h3>
					<ul>
						<li><a href="?page=contacts" title="">Support</a></li>
						<li><a href="?page=contacts" title="">Auteurs</a></li>
					</ul>
 					<!-- Menu Stop -->
				</div>
				</div>
				</div>
   			</div>
		</td>
		<td valign="top" width="100%"> 
			<!-- le corps de la page-->
			<div class="greybox_right">
			<div class="greybox_left">
			<div class="greybox_top">
			<div class="greybox_top_l">
			<div class="greybox_top_r">
			<div class="greybox_bottom">
			<div class="greybox_bottom_l">
			<div class="greybox_bottom_r">
					<div class="content">
					<?php
					// On affiche la page au bonne endroit
					echo $page;
					// On affiiche la source
					echo("<br/><hr/>Source : <a href=\"http://wiki.inria.fr/sciencinfolycee/".$page."\">http://wiki.inria.fr/sciencinfolycee/".$page."</a>");
					?>
					<br/><br/>
					</div>			
			</div> 
			</div>
			</div>
			</div> 
			</div>
			</div>
			</div>
			</div>	 
		</td>
	</tr>	
</table>
<div class="blackbox_right">
	<div class="blackbox_left">
		<div class="blackbox_top">
		<div class="blackbox_top_l">
		<div class="blackbox_top_r">
			<div class="blackbox_bottom">
			<div class="blackbox_bottom_l">
			<div class="blackbox_bottom_r">
				<div class="footer">
					&nbsp;&copy;&nbsp; Java's Cool 2008 - 2010. <!-- All rights reserved -->
				</div>
			</div>
			</div>
			</div>
		</div>
		</div>
		</div>
	</div>
</div>
</body>
</html>
