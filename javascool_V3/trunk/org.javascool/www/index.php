<?php
///////////////////////////////////////////////////////////////////
// Script sous licence CeCILL                                    //
// Auteur : Philippe Vienne et Thierry Viéville, inria.sophia.fr //
///////////////////////////////////////////////////////////////////
//
// Mécanisme de syndication des pages web à partir de http://wiki.inria.fr/sciencinfolycee
//

function get_page_contents($name) {
  if(ereg('^api:.*', $name)) {
    // Traitement d'une demande de page de doc java
    $name = ereg_replace('^api:', '', $name);
    // Recuperation de la page javadoc
    $page = file_get_contents('http://javascool.gforge.inria.fr/v3/api//'.$name);
    // Remplace tous les liens entre pages par des pages vues du site
    $page = ereg_replace('\.\./\.\./', '?page=api:', $page);
  } else {
    // Recuperation de la page sur le wiki
    $page = file_get_contents('http://wiki.inria.fr/sciencinfolycee/JavaScool:'.$name.'?printable=yes&action=render');
    // Remplace tous les liens entre pages par des pages vues du site
    $page = ereg_replace('href="http://wiki.inria.fr/sciencinfolycee/JavaScool:', 'href="?page=', $page);
    // Remplace tous les liens wikis locaux pas des liens distants
    $page = ereg_replace('src="/wikis/sciencinfolycee', 'src="http://wiki.inria.fr/wikis/sciencinfolycee', $page); 
    // Si le wiki signale une erreur alors on affiche proprement une page en erreur
    if (ereg("<title>Erreur</title>", $page))
      $page="<h1>Désolé ! Cette page est en construction où inacessible ..</h1><a href=\"javascript:history.back()\">Revenir en arri&egrave;re</a>";
  }
  return $page;
}

// Usage: http://javascool.gforge.inria.fr/?page=<page>
  $name = isset($_GET['page']) ? $_GET['page'] : "Accueil";
  $page = get_page_contents($name);
  $menu = get_page_contents("Menu");
?>
<!DOCTYPE html PUBliC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/Dtd/xhtml1-transitional.dtd"> 
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="fr" lang="fr"> 
<head> 
<title>Java's Cool - <? echo $name; ?></title> 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /> 
<meta http-equiv="Pragma" content="no-cache" /> 
<meta http-equiv="Content-Style-Type" content="text/css" /> 
<meta http-equiv="Content-Script-Type" content="text/javascript" /> 
<meta http-equiv="Content-Language" content="fr" /> 
<meta name="robots" content="index,follow" /> 

<link href="styles/style.css" type="text/css" rel="stylesheet" /> 
<link href="styles/style_menu_right.css" rel="stylesheet" type="text/css">
</head> 
<body>
  <div class="greybox_right">
    <div class="greybox_left">
      <div class="greybox_top">
        <div class="greybox_top_l">
          <div class="greybox_top_r">
            <div class="greybox_bottom">
              <div class="greybox_bottom_l">
                <div class="greybox_bottom_r"><div align="center">
                  <a href="index.php"><img src="images/logo.gif" align="center" alt="logo java's Cool" border="0"></a>
                   <img src="images/logo_lycees.jpg" width="350" align="right" alt="Avec les lycées de PACA" border="0"></a>
                </div></div> 
              </div>
            </div>
          </div> 
        </div>
      </div>
    </div>
  </div>
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
                    <li><a href="./javascool.jar">T&eacute;l&eacute;chargement</a></li> 
                    <li><a href="?page=Activites">Activit&eacute;s</a></li> 
                    <li><a href="?page=Ressources">Ressources</a></li> 
                    <li><a href="?page=Developpement">D&eacute;veloppement</a></li> 
                    <li><a href="?page=Contacts">Contacts</a></li> 
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
  <tr><td valign="top"> 
    <div class="menuright">
      <div class="menuright_top">
        <div class="menuright_right">
          <div class="menuright_bottom">
            <?php echo $menu; ?>
          </div>
        </div>
      </div>
    </div>
  </td><td valign="top" width="100%"> 
  <div class="greybox_right">
    <div class="greybox_left">
      <div class="greybox_top">
        <div class="greybox_top_l">
          <div class="greybox_top_r">
            <div class="greybox_bottom">
              <div class="greybox_bottom_l">
                <div class="greybox_bottom_r">
                  <div class="content">
                    <?php echo $page; ?>
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
</td></tr>	
</table>
<div class="blackbox_right">
  <div class="blackbox_left">
    <div class="blackbox_top">
      <div class="blackbox_top_l">
        <div class="blackbox_top_r">
          <div class="blackbox_bottom">
            <div class="blackbox_bottom_l">
              <div class="blackbox_bottom_r">
                  <table class="blackbox_bottom_r" width="100%"><tr>
                    <td width="20%" align="center" valign="middle">
                      <a href="http://www.unisciel.fr"><img width="200" src="images/logo_unisciel.png" alt="logo UNISCIEL" border="0"></a></td>
                    <td width="20%" align="center" valign="middle">
                      <a href="http://www.fuscia.info"><img width="200" src="images/logo_fuscia.gif" alt="logo FUSCIA" border="0"></a></td>
                    <td width="20%" align="center" valign="middle">
                      <a href="http://www.linux-azur.org"><img width="200" src="images/logo_linuxazur.png" alt="logo LINUXAZUR" border="0"></a></td>
                    <td width="20%" align="center" valign="middle">
                      <a href="http://unice.fr"><img width="200" src="images/logo_unice.gif" alt="logo UNS" border="0"></a></td>
                    <td width="20%" align="center" valign="middle">
                      <a href="http://www.inria.fr/sophia"><img width="300" src="images/logo_inria.gif" alt="logo INRIA" border="0"></a></td>
		    <td></td><td></td>
                  </tr></table>
                  &nbsp;&copy;&nbsp; Java's Cool 2008 - 2010.
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
