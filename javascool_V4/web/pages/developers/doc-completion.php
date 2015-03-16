<?php
    showBrowser(
        array(
            array("Java's Cool","?"),
            array("Développeurs","?page=developers"),
	    array("Faire des proglets","?page=developers&action=doc-proglets")
        ),array(
	    array("Complétion","")
	)
    );
?>

<?php echo wiki_get_contents('JavaScool:EditorCompletion'); ?>
