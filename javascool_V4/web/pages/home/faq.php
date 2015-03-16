<?php
    showBrowser(
        array(
            array("Java's Cool","index.php"),
            array("Accueil","index.php"),
            array("FAQ","")
        ),
        array(
	    array("Manifeste","?page=home&action=manifest"),
            array("About","?page=home&action=about")
        )
    );
?>


<?php echo wiki_get_contents('JavaScool:Faq'); ?>