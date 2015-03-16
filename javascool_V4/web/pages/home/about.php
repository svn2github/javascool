<?php
    showBrowser(
        array(
            array("Java's Cool","index.php"),
            array("Accueil","index.php"),
            array("About","")
        ),
        array(
	      array("Manifeste","?page=home&action=manifest"),
            array("FAQ","?page=home&action=faq")
        )
    );
?>


<?php echo wiki_get_contents('JavaScool:About'); ?>