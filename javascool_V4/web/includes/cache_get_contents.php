<?php
  /* Mécanique de cache: s'uilise à travers la construction
   return cache_get_contents_exists($name) ? cache_get_contents_get($name) : cache_get_contents_put($name, $text);
  */
$cache_get_contents_folder = file_exists('/home/groups/javascool/htdocs') ? '/home/groups/javascool/htdocs/.http.cache' : '.http.cache';

function cache_get_contents_exists($name) {
  global $cache_get_contents_folder;
  $name = rawurlencode($name);
  if (!file_exists($cache_get_contents_folder)) 
    mkdir($cache_get_contents_folder, 0777);
  return file_exists($cache_get_contents_folder.'/'.$name);
}
function cache_get_contents_put($name, $body) {
  global $cache_get_contents_folder;
  $name = rawurlencode($name);
  file_put_contents($cache_get_contents_folder.'/'.$name, $body);
  chmod($cache_get_contents_folder.'/'.$name, 0666);
  return $body;
}
function cache_get_contents_get($name) {
  global $cache_get_contents_folder;
  $name = rawurlencode($name);
  return file_get_contents($cache_get_contents_folder.'/'.$name);
}

// Vide le cache 
if(isset($_GET['kezako']) && $_GET['kezako'] == 'niquelekacheux') { 
  passthru("rm -rf $cache_get_contents_folder"); echo 'wraz'; exit; 
}

?>