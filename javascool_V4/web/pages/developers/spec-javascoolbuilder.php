<?php
    showBrowser(
        array(
            array("Java's Cool","?"),
            array("Développeurs","?page=developers"),
	    array("Comment ça marche","?page=developers&action=spec-proglets"),
	    array("Java's Cool Builder","")
        ),array(
	    array("HML","?page=developers&action=spec-hml")
	)
    );
?>

<?php echo wiki_get_contents('JavaScool:SpecJavaScoolBuilder'); ?>
