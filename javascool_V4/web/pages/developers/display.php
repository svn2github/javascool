<?php
    showBrowser(
        array(
            array("Java's Cool","?"),
            array("Développeurs","")
        ),
	array(
	    array("Faire une proglet","?page=developers&action=doc-proglets"),
	    array("Comment ça marche","?page=developers&action=spec-proglets"),
	    array("API","?page=api")
	)
    );
?>

<?php echo wiki_get_contents('JavaScool:Developpement'); ?>
