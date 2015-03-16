<?php 

/** mails a message.
 * @param header The message uri: 
 * <tt>mailto:<i>addresses</i>?subject=<i>my-subject</i>&from=<i>address</i>&cc=<i>addresses</i>&bcc=<i>addresses</i>&format=(html|plain)</tt>
 * @param string The message body
 */
function mailto($header, $string) {
  if (strncmp("mailto:", $header, 7) == 0) {
    $url = parse_url($header); 
    if (isset($url["path"]) && isset($url["query"])) {
      parse_str($url["query"], $query);
      $address = $url["path"];
      $subject = isset($query["subject"]) ? $query["subject"] : "no-subject";
      $header = "";
      if (isset($query["from"]))      $header .= "Reply-To: ".$query["from"]."\r\n";
      if (isset($query["cc"]))        $header .= "Cc: ".$query["cc"]."\r\n";
      if (isset($query["bcc"]))       $header .= "Bcc: ".$query["bcc"]."\r\n";
      if (isset($query["format"]) && $query["format"] == "html") $header .= "MIME-Version: 1.0\r\nContent-type: text/html; charset=iso-8859-1\r\n";
      mail($address, $subject, $string, $header);
    } else
      trigger_error("Bad URL $header in string_send()", E_USER_ERROR);
  }
}

?>
