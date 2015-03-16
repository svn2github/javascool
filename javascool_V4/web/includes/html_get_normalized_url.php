<?php
/** Renvoie le path debarassé des «../» et des «./» redondants. */
function html_get_normalized_url($path) {
  $path = urldecode($path);
  while(true) {
    $newpath = ereg_replace("^\./", "", ereg_replace("([:/])[^\.:/][^/:]*/\.\.", "\\1", ereg_replace("(/+|/\./)", "/", $path)));
    // echo "$path -> $newpath\n";
    if (ereg("^:?/?\.\.", $newpath))
      return "";
    if ($newpath == $path)
      return $path;
    $path = $newpath;
  }
}
?>
