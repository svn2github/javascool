<?php
    showBrowser(
        array(
            array("Java's Cool","?"),
            array("Développeurs","?page=developers"),
	    array("Faire des proglets","?page=developers&action=doc-proglets"),
	    array("HML","")
        ),array(
	    array("Java's Cool Builder","?page=developers&action=doc-javascoolbuilder"),
	    array("FAQ des développeurs","?page=developers&action=faq-developers")
	)
    );
?>

<?php echo wiki_get_contents('JavaScool:DocFormatHml'); ?>
