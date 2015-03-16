<?php
  /** Renvoie une page HTML avec les liens correctement mis à jour pour être produite à n'importe quel endroit du site.
   * @param file Fichier où se trouve le texte HTML à normaliser.
   * @param base Référence d'une URL où se situe la page, utilisée pour les chemins relatifs des liens.
   * @param path Racine d'un fichier où va se situer la page, utilisée pour les chemins relatifs des images.
   * @return Le texte avec les liens mis à jours.
   */
function html_get_contents($file, $base, $path) {
  return cache_get_contents_exists($file) ? cache_get_contents_get($file) : cache_get_contents_put($file, html_get_contents_load($file, $base, $path));
}
function html_get_contents_load($file, $base, $path) {
  $body = file_get_contents($file);
  // Ne conserve que le corps de page
  $body = ereg_replace(".*<(body|BODY)[^>]*>", "", ereg_replace("</(body|BODY)[^>]*>", "", $body));
  // Quote les liens externes par un préfixe /
  $body = ereg_replace("(src|SRC|href|HREF) *= *([\"'])([a-z]+:)", "\\1=\\2/\\3", $body);
  // Ajoute une base au liens web relatif
  $body = ereg_replace("(href|HREF) *= *([\"'])([\\.a-zA-Z][^#\"']*)", "\\1=\\2".$base."/\\3", $body);
  // Ajoute une racine au liens icone relatif
  $body = ereg_replace("(src|SRC) *= *([\"'])([\\.a-zA-Z][^#\"']*)", "\\1=\\2".$path."/\\3", $body);
  // Dé-quote les liens externes
  $body =  ereg_replace("(src|SRC|href|HREF)=([\"'])/([a-z]+:)", "\\1=\\2\\3", $body);
  return $body;
}

?>