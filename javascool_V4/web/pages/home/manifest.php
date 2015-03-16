<?php
    showBrowser(
        array(
            array("Java's Cool","index.php"),
            array("Accueil","index.php"),
            array("Manifeste","")
        ),
        array(
	    array("FAQ","?page=home&action=faq"),
            array("About","?page=home&action=about")
        )
    );
?>


<?php echo wiki_get_contents('JavaScool:Manifeste'); ?>