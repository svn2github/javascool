<? // This interfaces with the javascool/proglet mechanism

// Page parameters
$prog = isset($_REQUEST['prog']) ? $_REQUEST['prog'] : "Konsol"; // Proglet to use at execution time: Konsol, Dicho, Smiley, Scope, Tortue
$main = isset($_REQUEST['main']) ? $_REQUEST['main'] : "";       // Java class name
$path = isset($_REQUEST['path']) ? $_REQUEST['path'] : "";       // Java source file path
$body = isset($_REQUEST['body']) ? $_REQUEST['body'] : "";       // Java source file body

echo '<html><head></head><body><table><tr>
<td width="570" height="730" valign="top" align="center"><applet code="proglet.InterfacePrincipale.class" archive="proglet.jar" width="560" height="720">
  <param name="proglet" value="'.$prog.'"/>
  <param name="edit" value="true"/>'.
  (strlen($path) > 0 ? '  <param name="path" value="'.$path.'"/>' : '').
'</applet></td><td width="570" height="730" valign="top" align="center">';

// Compilation and execution
if(strlen($main) > 0 && strlen($body) > 0) {
  file_put_contents($main.".java", $body);
  unlink($main.".class");
  $comp = trim(shell_exec("sh -c 'javac -cp proglet.jar ".$main.".java 2>&1'"));
  unlink($main.".java");

  if(strlen($comp) > 0) {
    // Compilation error
    echo'<div><b>Le programme '.$main.' a des erreurs de compilation:</b></div><div align="left" style="background:#DDDDDD;"><pre>'.$comp.'</pre></div>';  
  } else { 
    // Applet execution
    echo '<applet code="'.$main.'.class" codebase="." archive="proglet.jar" width="560" height="720"><param name="proglet" value="'.$prog.'"/></applet>';
  }
} else {
  echo'&nbsp;';
}

echo '</td></tr></table></body></html>';
?>
