<?php
    showBrowser(
        array(
            array("Java's Cool","?"),
            array("Développeurs","?page=developers")
        ),array(
	    array("Syndication du wiki")
	)
    );
?>

<?php echo wiki_get_contents('JavaScool:SyndicationWiki'); ?>
