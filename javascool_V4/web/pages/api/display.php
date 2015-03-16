<?php
    $apiurl = isset($_GET['api']) ? html_get_normalized_url($_GET['api']) : 'overview-summary.html';
    Sal::validateApiUrl($apiurl);
    $api = html_get_contents("api/".$apiurl, "?page=api&api=".dirname($apiurl), "/api/".dirname($apiurl));
    echo '<div id="javadoc">'.$api.'</div>';
?>
