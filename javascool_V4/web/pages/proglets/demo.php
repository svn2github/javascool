<?php
if (!isset($_GET['id']))
    die('Error');
$id = $_GET['id'];
Sal::validateProgletId($id);
if (!is_file("proglets/" . $id . "/proglet.php"))
    die("La proglet " . $id . " n'a pas de fichier proglet.php");
?>

<?php showBrowser(array(array("Java's Cool", "index.php"), array("Proglets", "index.php?page=proglets"), array($name, ""))); ?>

<?php showButton(array('Voir la documentation', '?page=proglets&action=show&id=' . $id)); ?>
<div class="news">
    <table class="news">
        <tr class="news-top">
            <td colspan="5" class="news-top"></td>
        </tr>
        <tr class="news-center">
            <td class="news-left"></td>
            <td class="news-leftborder"></td>
            <td class="news-center">
		<?php include('proglets/'.$id.'/applet-tag.htm'); ?>
	    </td>
            <td class="news-rightborder"></td>
            <td class="news-right"></td>
        </tr>
        <tr class="news-bottom">
            <td colspan="5" class="news-bottom"></td>
        </tr>
    </table>
</div>
