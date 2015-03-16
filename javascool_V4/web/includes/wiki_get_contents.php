<?php

// Renvoie le texte sur http://wiki.inria.fr/sciencinfolycee avec mise en forme des liens
function wiki_get_contents($name) {
  $file = "./cache/".$name;
  if (file_exists($file)) {
    $text = file_get_contents($file);
    if ($text === FALSE)
      echo "<hr><tt>Erreur de lecture du fichier '".getcwd()."/".$file."'</tt><hr>";
    else
      return $text;
  } else
    echo "<hr><tt>Erreur d'accès au fichier '".getcwd()."/".$file."'</tt><hr>";
}

?>