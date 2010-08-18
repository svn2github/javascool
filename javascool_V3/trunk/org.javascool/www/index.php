<?php
///////////////////////////////////////////////////////////////////
// Script sous licence CeCILL                                    //
// Auteurs : (Philippe Vienne&Thierry Viéville)@inria.sophia.fr  //
///////////////////////////////////////////////////////////////////
//
// Mécanisme de syndication des pages web à partir de http://wiki.inria.fr/sciencinfolycee
//

/* Fonction utilisée pour cracher les erreurs
 function debug_error_handler($errno, $errstr, $errfile, $errline, $errcontext) { if ($errno != 8192) {
   echo "<br/>\n".str_repeat("-", 120)."<br/>\nError #$errno: '$errstr' [in $errfile line $errline]<br/>\n"; echo str_repeat("-", 120)."<br/>\n<br/>\n";
 } }
 set_error_handler('debug_error_handler');
*/

/* Utilisé pour tester en local
echo '<hr>';
*/

// Récupère la page par syndication de la javadoc ou du wiki et gere les liens avec un mécanisme de cache
function get_page_contents($name) {
  // Manage cache mechanism
  $cname = rawurlencode($name);
  if (!file_exists('.htcache')) mkdir('.htcache', 0777);
  if (file_exists('.htcache/'.$cname)) return file_get_contents('.htcache/'.$cname);
  {
    $notfound = "<h1>Désolé ! Cette page est en construction ou inacessible ..</h1><a href=\"javascript:history.back()\">Revenir en arri&egrave;re</a>"; 
    if(ereg('^api:.*', $name)) {
      // Traitement d'une demande de page de doc java
      $name = ereg_replace('^api:', '', $name);
      // Recuperation de la page javadoc
      $pwd = getcwd(); $file = $pwd.'/api/'.$name; 
      if (!file_exists($file)) return $notfound;
      $page = file_get_contents($file);
      // Remplace tous les liens entre pages par des pages vues du site
      $base = substr(realpath(dirname($file)), strlen($pwd)+4);
      $page = ereg_replace('(href|HREF)="([^/#][^:"]*)"', '\\1="?page=api:'.$base.'/\\2"', $page);
      $page = ereg_replace('(src|SRC)="([^/#][^:"]*)"', '\\1="api/'.$base.'/\\2"', $page);
      // Passe en <pre></pre> les pages de source
      if (ereg("\.java$", $name)) $page = "<pre>".$page."</pre>";
    } else {
      // Recuperation de la page sur le wiki
      $page = file_get_contents('http://wiki.inria.fr/sciencinfolycee/JavaScool:'.$name.'?printable=yes&action=render');
      // Remplace tous les liens entre pages par des pages vues du site
      $page = ereg_replace('href="http://wiki.inria.fr/sciencinfolycee/JavaScool:', 'href="?page=', $page);
      // Remplace tous les liens wikis locaux pas des liens distants
      $page = ereg_replace('src="/wikis/sciencinfolycee', 'src="http://wiki.inria.fr/wikis/sciencinfolycee', $page); 
      // Si le wiki signale une erreur alors on affiche proprement une page en erreur
      if (ereg("<title>Erreur</title>", $page)) return $notfound;
    }
  }
  file_put_contents('.htcache/'.$cname, $page); chmod('.htcache/'.$cname, 0666);
  return $page;
}
// Usage: http://javascool.gforge.inria.fr/?kezako=niquelekacheux
if(isset($_GET['kezako']) && $_GET['kezako'] == 'niquelekacheux') { exec("rm -rf .htcache"); exit; }

// Usage: http://javascool.gforge.inria.fr/?page=<page>
  $name = isset($_GET['page']) ? $_GET['page'] : "Accueil";
  $page = get_page_contents($name);
  $menu = get_page_contents("Menu");
?>
<!DOCTYPE html PUBliC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/Dtd/xhtml1-transitional.dtd"> 
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="fr" lang="fr"> 
<head> 
<title>Java's Cool - <?php echo $name; ?></title> 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /> 
<meta http-equiv="Pragma" content="no-cache" /> 
<meta http-equiv="Content-Style-Type" content="text/css" /> 
<meta http-equiv="Content-Script-Type" content="text/javascript" /> 
<meta http-equiv="Content-Language" content="fr" /> 
<meta name="robots" content="index,follow" /> 
<link rel="shortcut icon" href="images/icon_js.png"/>
<link href="styles/style.css" type="text/css" rel="stylesheet" /> 
<link href="styles/style_menu_right.css" rel="stylesheet" type="text/css">
<script type="text/javascript">

  var _gaq = _gaq || [];
  _gaq.push(['_setAccount', 'UA-17973020-2']);
  _gaq.push(['_trackPageview']);

  (function() {
    var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
    ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
    var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
  })();

</script>
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
                  <a href="index.php"><img src="images/logo.gif" width="50%" align="center" alt="logo java's Cool" border="0"></a>
                   <img src="images/logo_lycees.jpg" width="30%" align="right" alt="Avec les lycées de PACA" border="0"></a>
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
                    <li><a href="?page=Telechargement">T&eacute;l&eacute;chargement</a></li> 
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
<div class="greybox_right">
  <div class="greybox_left">
    <div class="greybox_top">
      <div class="greybox_top_l">
        <div class="greybox_top_r">
          <div class="greybox_bottom">
            <div class="greybox_bottom_l">
              <div class="greybox_bottom_r">
                  <table class="" width="100%"><tr>
                    <td width="20%" align="center" valign="middle">
                      <a href="http://www.unisciel.fr"><img width="90%" src="images/logo_unisciel.png" alt="logo UNISCIEL" border="0"></a></td>
                    <td width="20%" align="center" valign="middle">
                      <a href="http://www.fuscia.info"><img width="90%" src="images/logo_fuscia.gif" alt="logo FUSCIA" border="0"></a></td>
                    <td width="20%" align="center" valign="middle">
                      <a href="http://www.linux-azur.org"><img width="90%" src="images/logo_linuxazur.png" alt="logo LINUXAZUR" border="0"></a></td>
                    <td width="20%" align="center" valign="middle">
                      <a href="http://unice.fr"><img width="90%" src="images/logo_unice.gif" alt="logo UNS" border="0"></a></td>
                    <td width="20%" align="center" valign="middle">
                      <a href="http://www.inria.fr/sophia"><img width="90%" src="images/logo_inria.gif" alt="logo INRIA" border="0"></a></td>
		    <td></td><td></td>
                  </tr></table>
                  <center>&nbsp;&copy;&nbsp; Java's Cool 2008 - 2010.</center>
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
