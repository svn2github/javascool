<?
function get_page_contents($name) {
  // Manage cache mechanism
  $cname = rawurlencode($name);
  $cache = $_SERVER["DOCUMENT_ROOT"].'.htcache';
  if (!file_exists($cache)) mkdir($cache, 0777);
  if (file_exists($cache.'/'.$cname)) return file_get_contents($cache.'/'.$cname);
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
      $base = ereg_replace("api/", "", substr(realpath(dirname($file)), strlen($pwd)+4));
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
  file_put_contents($cache.'/'.$cname, $page); chmod($cache.'/'.$cname, 0666);
  return $page;
}