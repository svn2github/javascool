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

echo '<html><head></head><body>';
//in case of debug// print_r($_REQUEST);
echo '<table><tr><td valign="top"><a href="http://javascool.gforge.inria.fr/proglet"><img src="home.png"/></a></td>
<td width="570" height="730" valign="top" align="center"><applet code="proglet.InterfacePrincipale.class" archive="proglet.jar" width="560" height="720">
  <param name="proglet" value="'.$prog.'"/>
  <param name="edit" value="true"/>'.
  (strlen($path) > 0 ? '  <param name="path" value="'.$path.'"/>' : '').
'</applet></td><td width="570" height="730" valign="top" align="center">
';

// Cleanup on request
if (isset($_REQUEST['clean-up'])) shell_exec("sh -c '/bin/rm -rf ./tmp-*'");

// Log the access
{
  $eh = set_error_handler(create_function('$errno, $errstr', 'return true;')); $date = getdate(); set_error_handler($eh); 
  $date = $date['year'].'-'.$date['mon'].'-'.$date['mday'].':'.$date['hours'].':'.$date['minutes'].':'.$date['seconds'];
  $fp = fopen('access.log', 'a'); fwrite($fp, $_SERVER['REMOTE_ADDR'].'@'.$date.';prog='.$prog.(strlen($main) > 0 ? ';main='.$main : ';edit')."\n"); fclose($fp);
}

// Compilation and execution
if(strlen($main) > 0 && strlen($body) > 0) {
  // Makes one directory per remote client
  $dir = "tmp-".$_SERVER['REMOTE_ADDR']; mkdir($dir, 0777);
  // Changes the java name at each run in order to reload a fresh class event with older java's versions
  {
    for($nn = 0; file_exists($dir."/".$main."_".$nn.".java") && $nn < 10000; $nn++);
    $body =  ereg_replace("public class ".$main, "public class ".$main."_".$nn, $body);
    $main = $main."_".$nn;
  }
  // Creates the local java copy
  if(strlen($body) > 10000) {
    echo'<div><b>Le programme '.$main.' de taille '.strlen($body).'octets est bien trop long !:</b></div><div align="left" style="background:#DDDDDD;"><pre>Impossible de le compiler ici !</pre></div>'; 
    exit(0);
  }
  file_put_contents($dir."/".$main.".java",$body); chmod($dir."/".$main.".java", 0777);
  // Runs the server's compiler
  unlink($dir."/".$main.".class");
  $comp = ereg_replace("$dir"."/", "", trim(shell_exec("sh -c 'javac -cp proglet.jar ".$dir."/".$main.".java 2>&1'")));
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
