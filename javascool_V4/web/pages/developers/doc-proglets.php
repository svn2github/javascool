<?php
    showBrowser(
        array(
            array("Java's Cool","?"),
            array("Développeurs","?page=developers"),
	    array("Faire des proglets","")
        ),array(
	    array("HML","?page=developers&action=doc-hml"),
	    array("Java's Cool Builder","?page=developers&action=doc-javascoolbuilder"),
	    array("FAQ des développeurs","?page=developers&action=faq-developers")
	)
    );
?>

<?php echo wiki_get_contents('JavaScool:DocCreationProgletExemple'); ?>
