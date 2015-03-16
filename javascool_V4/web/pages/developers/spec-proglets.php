<?php
    showBrowser(
        array(
            array("Java's Cool","?"),
            array("Développeurs","?page=developers"),
	    array("Comment ça marche","")
        ),array(
	    array("HML","?page=developers&action=spec-hml"),
	    array("Java's Cool Builder","?page=developers&action=spec-javascoolbuilder")
	)
    );
?>

<?php echo wiki_get_contents('JavaScool:DocCreationProglet'); ?>
