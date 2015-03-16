<?php 
    showBrowser(
        array(
            array("Java's Cool","index.php"),
            array("Accueil","")
        ),
        array(
            array("Manifeste","?page=home&action=manifest"),
            array("FAQ","?page=home&action=faq"),
            array("About","?page=home&action=about")
        )
    );
?>

<div class="home">
    <table class="home"><tr><td class="news">
                <div class="news">
                    <table class="news">
<tr class="news-top"><td colspan="5" class="news-top"></td></tr>
<tr class="news-center"><td class="news-left"></td><td class="news-leftborder"></td>
  <td class="news-center"><?php echo wiki_get_contents('JavaScool:Actualite'); ?></td>
<td class="news-rightborder"></td><td class="news-right"></td></tr>
<tr class="news-bottom"><td colspan="5" class="news-bottom"></td></tr>
<tr><td></td><td></td><td class="news-trailer"><br/>
<?php showLink('http://www.inria.fr','<img src="images/logo_INRIA0.png" width = "150" alt="INRIA" class="imagelink"/>','external');?><br/>
<?php showLink('http://www.unisciel.fr','<img src="images/logo_unisciel.png" width = "150" alt="Unisciel" class="imagelink"/>','external');?><br/>
<?php showLink('http://fuscia.info','<img src="images/logo_fuscia.png" width = "150" alt="Fuscia" class="imagelink"/>','external');?><br/><br/>
<?php showLink('http://unice.fr','<img src="images/logo_unice.gif" width = "150" alt="INRIA" class="imagelink"/>','external');?>
</tr></table>
                </div>
            </td><td class="main">
                <div class="main">
                    <table class="main">
                        <tr class="main-top">
                            <td class="main-topspacer"></td>
                            <td class="main-topleft"></td>
                            <td class="main-top"></td>
                            <td class="main-topright"></td>
                            <td class="main-topspacer"></td>
                        </tr>

                        <tr class="main-center">
                            <td class="main-left"></td>
                            <td class="main-leftborder"></td>
                            <td class="main-center">
                                
<?php echo wiki_get_contents('JavaScool:Accueil');  ?>
                            </td>
                            <td class="main-rightborder"></td>
                            <td class="main-right"></td>
                        </tr>

                        <tr class="main-bottom">
                            <td class="main-bottomspacer"></td>
                            <td class="main-bottomleft"></td>
                            <td class="main-bottom"></td>
                            <td class="main-bottomright"></td>
                            <td class="main-bottomspacer"></td>
                        </tr>
                    </table>
                </div>
            </td>
        </tr>
    </table>
</div>
