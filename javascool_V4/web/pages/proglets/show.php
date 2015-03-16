<?php
    if (!isset($_GET['id']))
        die('Error');
    $id = $_GET['id'];
    Sal::validateProgletId($id);
    if (!is_file("proglets/" . $id . "/proglet.php"))
        die("La proglet " . $id . " n'est pas définie sur le site web.");
    $pml=null;
    include("proglets/" . $id . "/proglet.php");  //TODO testme
    if (isset($pml['name'])) $name=$pml['name']; else $name="";
    if (isset($pml['description'])) $desc=$pml['description']; else $desc="";
    if (isset($pml['icon'])) $icon='proglets/'.$id.'/'.$pml['icon']; else $icon="";
    if ($name=="") $name=$id;

    $defaulticon="images/defaultProglet.png";
                
    if ($icon=="")
        $icon=$defaulticon;
    if (!is_file($icon))
        $icon=$defaulticon;

    $helpFile = isset($_GET['helpFile']) ? html_get_normalized_url($_GET['helpFile']) : 'help.htm';
?>

<?php showBrowser(array(array("Java's Cool","index.php"),array("Proglets","index.php?page=proglets"),array($name,""))); ?>

<table>
    <tr>
        <?php
        echo('<script type="text/javascript">document.write(\'<td class="progletclickable" onClick="gotolocnow(\\\'proglets/'.$id.'/javascool-proglet-' . $id . '.jar\\\')"><span>' . $id . '</span><span class="proglet-image"><img src="'.$icon.'" alt=""/></span></td>\');</script>');
                    echo('<noscript><td class="progletclickable"><a href="proglets/'.$id.'/javascool-proglet-' . $id . '.jar"><span>' . $id . '</span><span class="proglet-image"><img style="border: 0px" src="'.$icon.'" alt=""/></span></a></td></noscript>');
                    ?>
<td width="200" align="right"><tt><?php 
echo(ereg_replace('<(http[^>]*)>', "[<a href='\\1'>*</a>]", ereg_replace('<([^>@]*@[^>]*)>', "[<a href='mailto:\\1?subject=a propos javascool'>x</a>]", $pml['author'])));
?></tt></td>
    </tr>
</table>
<br />

<!--?php showButton(array('Voir la d&eacute;monstration','?page=proglets&action=demo&id='.$id)); ?-->
<div class="news">
    <table class="news">
        <tr class="news-top">
            <td colspan="5" class="news-top"></td>
        </tr>
        <tr class="news-center">
            <td class="news-left"></td>
            <td class="news-leftborder"></td>
            <td class="news-center">
                <p><div style="max-width: 100%"><?php
        $help = html_get_contents("proglets/$id/$helpFile", "?page=proglets&action=show&id=$id&helpFile=".dirname($helpFile), "/proglets/$id/".dirname($helpFile));
    	$help=preg_replace('#<TD ([^>]*)WIDTH="([0-9]+%)"([^>]*)>#i','<TD $1 style="width:$2" $3>',$help);
	echo '<div id="javadoc">'.$help.'</div>';
		?></div></p>
            </td>
            <td class="news-rightborder"></td>
            <td class="news-right"></td>
        </tr>
        <tr class="news-bottom">
            <td colspan="5" class="news-bottom"></td>
        </tr>
    </table>
</div>
