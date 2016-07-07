<?php
    showBrowser(
        array(
            array("Java's Cool","?"),
            array("Développeurs","")
        ),
	array(
	    array("Faire une proglet","?page=developers&action=doc-proglets"),
	    array("Comment ça marche","?page=developers&action=spec-proglets"),
	    array("API","http://javascool.gforge.inria.fr/v4/api/overview-summary.html")
	)
    );
?>

<?php echo wiki_get_contents('JavaScool:Developpement'); ?>
