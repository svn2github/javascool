<?php
///////////////////////////////////////////////////////////////////
// Script sous licence CeCILL                                    //
// Auteur : Philippe Vienne et Thierry Viéville, inria.sophia.fr //
///////////////////////////////////////////////////////////////////
//
// Mécanisme de syndication des pages web à partir de http://wiki.inria.fr/sciencinfolycee
//
// Usage: http://javascool.gforge.inria.fr/?page=<page>
  $page = isset($_GET['page']) ? $_GET['page'] : "Accueil";
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
                    <li><a href="?page=Activites">Activit&eacute;s</a></li> 
                    <li><a href="?page=Ressources">Ressources</a></li> 
                    <li><a href="?page=Developpement">D&eacute;veloppement</a></li> 
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
  <tr><td valign="top"> 
    <div class="menuright">
      <div class="menuright_top">
        <div class="menuright_right">
          <div class="menuright_bottom">
            <h3>General</h3>
            <ul>
              <li><a href="?page=Accueil" title="Index">Accueil</a></li>
              <li><a href="?page=Faq" title="FAQ">FAQ</a></li>
              <li><a href="?page=Ailleurs" title="Ailleurs aussi">Ailleurs aussi ...</a></li>
            </ul>
            <h3>Recherche</h3>
            <div>
              <form action="http://www.google.com/search" method="get" style="font-size:10px;">
                <input name="ie" type="hidden" value="UTF-8"></input>
                <input name="oe" type="hidden" value="UTF-8"></input>
                <input maxlength="255" name="q" size="10" type="text" value=""></input>
                <input class="buttonSearch" name="btnG" type="submit" value="Rechercher" ></input>
                <input name="domains" type="hidden" value="http://javascool.gforge.inria.fr"></input>
                <input name="sitesearch" type="hidden" value="http://javascool.gforge.inria.fr"></input>
              </form>
            </div>
            <h3>Activit&eacute;s</h3>
            <ul>
              <li><a href="?page=Activite" title="Toutes les activités">Toutes les activités </a></li>
            </ul>
            <h3>Ressources</h3>
            <ul>
              <li><a href="?page=Activite" title="Toutes les ressources">Toutes les ressources </a></li>
            </ul>
            <h3>D&eacute;veloppement</h3>
            <ul>
              <li><a href="?page=Licence" title="Licence">Licence</a></li>
              <li><a href="?page=Crédits" title="Crédits">Crédits</a></li>
  	      <li><a href="api/org/javascool/package-summary.html" title="Doc Java">Doc Java</a></li>
              <li><a href="https://gforge.inria.fr/projects/javascool" title="Forge">Forge logicielle</a></li>
            </ul>
            <h3>Contacts</h3>
            <ul>
              <li><a href="?page=Contacts" title="Contacts">Aide et Support</a></li>
            </ul>
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
                    <?php
                      // On affiche la page au bon endroit
                      echo $page;
                      // On affiche la source
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
                    <td width="25%" align="center" valign="middle">
                      <a href="http://www.unisciel.fr"><img width="200" src="images/logo_unisciel.png" alt="logo UNISCIEL" border="0"></a></td>
                    <td width="25%" align="center" valign="middle">
                      <a href="http://www.linux-azur.org"><img width="200" src="images/logo_linuxazur.png" alt="logo LINUXAZUR" border="0"></a></td>
                    <td width="25%" align="center" valign="middle">
                      <a href="http://unice.fr"><img width="200" src="images/logo_unice.gif" alt="logo UNS" border="0"></a></td>
                    <td width="25%" align="center" valign="middle">
                      <a href="http://www.inria.fr/sophia"><img width="300" src="images/logo_inria.gif" alt="logo INRIA" border="0"></a></td>
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
