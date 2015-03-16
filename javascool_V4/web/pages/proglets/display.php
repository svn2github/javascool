<?php showBrowser(array(array("Java's Cool", "index.php"), array("Proglets", "")),array(array("Qu'est-ce qu'une proglet","?page=proglets&action=info"))); ?>
<div class="proglets">
    <table class="proglets">
        <tr>
    <?php
   $sketchbook = opendir('proglets');
    $proglets = array();
    $i = 0;
    while ($file = readdir($sketchbook)) {
	if (!preg_match('#^\.#', $file)) {
	    $proglets[$i] = $file;
	    $i++;
	}
    }
    sort($proglets);
    foreach ($proglets as $id) {
	if (!preg_match('#^\.#',$id)) {
	    Sal::validateProgletId($id);
	    if (!is_file("proglets/" . $id . "/proglet.php"))
		die("La proglet " . $id . " n'a pas de fichier proglet.php");
	    $pml=null;
	    include("proglets/" . $id . "/proglet.php");
	    if (isset($pml['title'])) $name=$pml['title']; else $name="";
	    if (isset($pml['description'])) $desc=$pml['description']; else $desc="";
	    if (isset($pml['icon'])) $icon='proglets/'.$id.'/'.$pml['icon']; else $icon="";
	    if ($name=="") $name=$id;

	    $defaulticon="images/defaultProglet.png";
	    if ($icon=="")
		$icon=$defaulticon;
	    if (!is_file($icon))
		$icon=$defaulticon;

	    $escapedName=str_replace("'","\\'",$name);

	    echo('<script type="text/javascript">document.write(\'<td class="progletclickable" onClick="gotoloc(\\\'index.php?page=proglets&action=show&id=' . $id . '\\\')"><div title="Description : '.$escapedName.'" class="tooltip"><span>' . $id . '</span><span class="proglet-image"><img src="'.$icon.'" alt=""/></span></div>	</td>\');</script>');
	    echo('<noscript><td class="progletclickable"><div title="Description : '.$escapedName.'" class="tooltip"><a href="index.php?page=proglets&action=show&id=' . $id . '"><span>' . $id . '</span><span class="proglet-image"><img style="border: 0px" src="'.$icon.'" alt=""/></span></a></div></td></noscript>');
	}
    }
    ?>
        </tr>
    </table>
</div>
