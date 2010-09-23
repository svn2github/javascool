<?php
include("includes/Page.class.php");
// Usage: http://javascool.gforge.inria.fr/?kezako=niquelekacheux
/*if(isset($_GET['kezako']) && $_GET['kezako'] == 'niquelekacheux') { passthru("rm -rf v3/.htcache .htcache", &$status); echo "wraz.status = $status\n"; exit; }

/* Usage: http://javascool.gforge.inria.fr/?page=<page>
  $name = isset($_GET['page']) ? $_GET['page'] : "Accueil";
  $page = get_page_contents($name);
  $menu = get_page_contents("Menu");*/
$name = isset($_GET['page']) ? $_GET['page'] : "Accueil";
$page=new Page();
?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Java Scool - <? echo $name; ?></title>
<meta name="keywords" content="javascool,java,scool,apprendre,learn,algorithme,jar,javascool.jar,sciences,science info,info,lycée,lycee,son,proglet,proglets" />
<meta name="description" content="JavaScool est un logiciel conçut pour apprendre aux lycéens à manipuler des algorithme informatique, programmer, créer des son et faire des activité" />
<link href="style/templatemo_style.css" rel="stylesheet" type="text/css" />
</head>
<body>

<div id="templatemo_wrapper">
<div id="templatemo_menu">
	<ul>
		<li><a href="http://www.javascool.fr" class="current">Site</a></li>
		<li><a href="http://www.javascool.fr/javascool.jar">Lancer</a></li>
		<li><a href="http://www.javascool.fr/index_old.html">Wiki</a></li>
		<li><a href="http://www.javascool.fr/index_old.html">Forum</a></li>
	</ul>
	
</div> 
<!-- end of templatemo_menu -->
<div id="templatemo_left_column">
	<div id="templatemo_header">
		<div id="site_title">
			<h1><a href="http://www.javascool.fr" target="_parent"><span><img src="images/logo.png" width=325px height=140px /></span></a></h1>
		</div><!-- end of site_title -->    
	</div> <!-- end of header -->
	<div id="templatemo_sidebar">
		<h4><a href="?page=Activites" title="JavaScool:Activites">Activités</a></h4>
		<ul class="templatemo_list">
			<li> <a href="?page=Telechargement" title="JavaScool:Telechargement">Démarrage</a></li>
		</ul>
		<h4><a href="?page=Ressources">Ressources</a></h4>
		<ul class="templatemo_list">
			<li><a href="http://javascool.gforge.inria.fr/?page=api:/org/javascool/doc-files/about-proglets.htm" class="external text" rel="nofollow">Les proglets</a></li>
			<li><a href="http://interstices.info"><img height="20" src="http://javascool.gforge.inria.fr/v3/images/logo_interstices.jpg"/></a></li>
		</ul>
		<h4><a href="?page=Developpement" title="JavaScool:Developpement">Développement</a></h4>
		<ul class="templatemo_list">
			<li><a href="http://wiki.inria.fr/sciencinfolycee" class="external text" rel="nofollow">Wiki de travail</a></li>
			<li><a href="http://javascool.gforge.inria.fr/v3/?page=api:org/javascool/package-summary.html" class="external text" rel="nofollow">Doc Java</a></li>
			<li><a href="?page=Licence" title="JavaScool:Licence">Licence</a> </li>
			<li><a href="?page=Cr%C3%A9dits" title="JavaScool:Crédits">Crédits</a></li>
		</ul>
		<h4><a href="?page=Contacts" title="JavaScool:Contacts">Contacts</a></h4>
		<ul class="templatemo_list">
			<li> <a href="?page=Faq" title="JavaScool:Faq">F.A.Q.</a></li>
			<li> <a href="http://www.fuscia.info/accueil">Bureau d'accueil</a></li>
		</ul>
		<h4>Rechercher</h4>
		<!-- Start of Google Search -->
		<script type="text/javascript" src="http://www.google.com/jsapi"></script>
		<script type="text/javascript">
			google.load('search', '1');
			google.setOnLoadCallback(function() {
				google.search.CustomSearchControl.attachAutoCompletion(
					'000567687519946432762:unyqz7x9tiu',
					document.getElementById('q'),
					'cse-search-box');
			});
		</script>
		<form action="http://www.google.com/cse" id="cse-search-box">
			<div>
				<input type="hidden" name="cx" value="000567687519946432762:unyqz7x9tiu" />
				<input type="hidden" name="ie" value="UTF-8" />
				<input type="text" name="q" id="q" autocomplete="off" size="10" />
				<input type="submit" name="sa" value="Rechercher" />
			</div>
		</form>
		<script type="text/javascript" src="http://www.google.com/cse/brand?form=cse-search-box&amp;lang=fr"></script>
		<!-- End of google search -->
	</div> <!-- end of templatemo_sidebar --> 
</div> 
<!-- end of templatemo_left_column -->
<!--templatemo_main -->
<div id="templatemo_right_column">
	<div id="templatemo_main">
		<h1 class="title"><? echo $name; ?></h1>
		<?php echo $page->getContent();?>
	</div>
	<div class="cleaner"></div>
</div> 
<!-- end of templatemo_main -->
<div class="cleaner_h20"></div>
<div id="templatemo_footer">
Copyright © 2010 <a href="http://www.inria.fr">INRIA</a>, All rights reserved | Valid&eacute; <a href="http://validator.w3.org/check?uri=referer">XHTML</a> &amp; <a href="http://jigsaw.w3.org/css-validator/check/referer">CSS</a></div>
<div class="cleaner"></div>
</div> <!-- end of warpper -->
</body>
</html>
