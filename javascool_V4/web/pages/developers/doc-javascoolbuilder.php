<?php
    showBrowser(
        array(
            array("Java's Cool","?"),
            array("Développeurs","?page=developers"),
	    array("Faire des proglets","?page=developers&action=doc-proglets"),
	    array("Java's Cool Builder","")
        ),array(
	    array("HML","?page=developers&action=doc-hml"),
	    array("FAQ des développeurs","?page=developers&action=faq-developers")
	)
    );
?>

<?php echo wiki_get_contents('JavaScool:DocJavaScoolBuilder'); ?>
