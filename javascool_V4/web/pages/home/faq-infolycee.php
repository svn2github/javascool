<?php
    showBrowser(
        array(
            array("Java's Cool","index.php"),
            array("Accueil","index.php"),
            array("FAQ","?page=home&action=faq"),
	    array("Usage au lycée","")
        )
    );
?>


<?php echo wiki_get_contents('JavaScool:InfoAuLycee'); ?>