<?
#####################################################################################################################################
##  Hamdi.Ben_Abdallah@inria.fr et Thierry.Vieville@sophia.inria.fr, Copyright (C) 2009.  All rights reserved.                     ##
#####################################################################################################################################

// This script interfaces with the javascool/proglet mechanism implemented in proglet.InterfacePrincipale

// Page parameters
$prog = isset($_REQUEST['prog']) ? $_REQUEST['prog'] : "Konsol"; // Proglet to use at execution time: Konsol, Dicho, Smiley, Scope, Tortue
$main = isset($_REQUEST['main']) ? $_REQUEST['main'] : "";       // Java class name
$path = isset($_REQUEST['path']) ? $_REQUEST['path'] : "";       // Java source file path
$body = isset($_REQUEST['body']) ? $_REQUEST['body'] : "";       // Java source file body

echo '<html><head></head><body><table><tr><td valign="top"><a href="http://javascool.gforge.inria.fr/proglet"><img src="home.png"/></a></td>
<td width="570" height="730" valign="top" align="center"><applet code="proglet.InterfacePrincipale.class" archive="proglet.jar" width="560" height="720">
  <param name="proglet" value="'.$prog.'"/>
  <param name="edit" value="true"/>'.
  (strlen($path) > 0 ? '  <param name="path" value="'.$path.'"/>' : '').
'</applet></td><td width="570" height="730" valign="top" align="center">';

// Cleanup on request
if (isset($_REQUEST['clean-up'])) shell_exec("sh -c '/bin/rm -rf ./tmp-*'");

// Log the access
{
  $eh = set_error_handler(create_function('$errno, $errstr', 'return true;')); $date = getdate(); set_error_handler($eh); 
  $date = $date['year'].'-'.$date['mon'].'-'.$date['mday'].':'.$date['hours'].':'.$date['minutes'].':'.$date['seconds'];
  $fp = fopen('access.log', 'a'); fwrite($fp, $_SERVER['REMOTE_ADDR'].'@'.$date.';prog='.$prog.(strlen($main) > 0 ? ';main='.$main : ';edit')); fclose($fp);
}

// Compilation and execution
if(strlen($main) > 0 && strlen($body) > 0) {
  $dir = "tmp-".$_SERVER['REMOTE_ADDR'];
  mkdir($dir, 0777);
  file_put_contents($dir."/".$main.".java", $body);
  unlink($dir."/".$main.".class");
  $comp = trim(shell_exec("sh -c 'javac -cp proglet.jar ".$dir."/".$main.".java 2>&1'"));
  unlink($dir."/".$main.".java");
  chmod($dir."/".$main.".class", 0777);

  if(strlen($comp) > 0) {
    // Compilation error
    echo'<div><b>Le programme '.$main.' a des erreurs de compilation:</b></div><div align="left" style="background:#DDDDDD;"><pre>'.$comp.'</pre></div>';  
  } else { 
    // Applet execution
    echo '<applet code="'.$main.'.class" codebase="'.$dir.'" archive="../proglet.jar" width="560" height="720"><param name="proglet" value="'.$prog.'"/></applet>';
  }
} else {
  echo'&nbsp;';
}

echo '</td></tr></table></body></html>';
?>
