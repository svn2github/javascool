<?php
    showBrowser(
        array(
            array("Java's Cool","?"),
            array("Développeurs","?page=developers"),
	    array("Faire des proglets","?page=developers&action=doc-proglets"),
	    array("FAQ","")
        ),array(
	    array("HML","?page=developers&action=doc-hml"),
	    array("Java's Cool Builder","?page=developers&action=doc-javascoolbuilder")
	)
    );
?>

<?php echo wiki_get_contents('JavaScool:FaqDeveloppement'); ?>
