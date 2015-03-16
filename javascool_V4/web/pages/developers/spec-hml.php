<?php
    showBrowser(
        array(
            array("Java's Cool","?"),
            array("Développeurs","?page=developers"),
	    array("Comment ça marche","?page=developers&action=spec-proglets"),
	    array("HML","")
        ),array(
	    array("Java's Cool Builder","?page=developers&action=spec-javascoolbuilder")
	)
    );
?>

<?php echo wiki_get_contents('JavaScool:DocumentsHml'); ?>
