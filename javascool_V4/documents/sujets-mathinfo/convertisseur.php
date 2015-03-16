<? 

/*
 * The xlate source for the website http://paulschou.com/tools/xlate/
 * 
 *   By using this source code you agree to do the following:
 *   - Link back to my site in some manner to show support
 *   - Use -> Modify -> Share (aka: GNU License)
 *
 * If you find youself unable to perform these tasks, this source code
 *   is *not* for you and should be deleted now.
 *
 */ // P.S. Donations are always appreciated. :-)

static $binary;
static $ascii;
static $hex;
static $b64;
static $char;

?><html>
<head>
<title>Convertisseur ASCII/binaire/hexa</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<META NAME="description" CONTENT="Utilitaire en ligne pour convertir ASCII/ANSI, HEX, Binary, Base64, etc.">
<META NAME="keywords" CONTENT="convertisseur,binaire,hexadécimal,binary encoder, binary decoder, binary translator, binary tools, binary converter, hexadecimal encoder/decoder, base 64">
<style>
body { 
size: 11pt;
font-family:arial,helvetica,sans;
text-align: center;
}

textarea {
margin: 4px;
}

h2 {
text-align: center;
}

.ff{size: 11pt;
	color: #333333;
	background: #eee;
	}
.btn{
	size: 12pt;
	color: #333333;
	background: #eee;
	}
</style>
</head>
<body>
<h2>Convertisseur</h2>
Cet utilitaire code et décode des caractères
<a target="_parent" href="http://fr.wikipedia.org/wiki/ASCII">ASCII</a> ainsi que des caractères étendus selon le codage
<a target="_parent" href="http://fr.wikipedia.org/wiki/Windows-1252">ANSI</a>. Le code ASCII proprement dit ne couvre que les caractères de numéros 0 à 127.<br />

<table border=0 cellspacing=8 cellpadding=0 align="center">
<tr>

<!-- ASCII -->
<form method="POST" action="http://igmaths.infos.st/outils/convertisseur.php">
<td align=center valign=top>

<b>[ <a target="_parent" href="http://fr.wikipedia.org/wiki/Ascii">TEXTE</a> ]</b><br>
<textarea cols=35 rows=10 wrap="virtual" name="ascii" class="ff"><?php

set_magic_quotes_runtime(0);
foreach($_POST as $key=>$val){ $$key = stripslashes($val); }

#$_POST[ascii] = str_replace("\\'","'",$_POST[ascii]);
#$_POST[ascii] = str_replace("\\\"","\"",$_POST[ascii]);
#$_POST[ascii] = str_replace("\\\\","\\",$_POST[ascii]);
if($ascii != "") print htmlentities($ascii);
else {

if($binary != "") {
	$binary_ = preg_replace("/[^01]/","", $binary);
	for($i = 0; $i < strlen($binary_); $i = $i + 8)
	$ascii = $ascii.chr(bindec(substr($binary_, $i, 8)));
}

if($hex != "") {
	$hex_ = preg_replace("/[^0-9a-fA-F]/","", $hex);
	for($i = 0; $i < strlen($hex_); $i = $i + 2)
	$ascii = $ascii.chr(hexdec(substr($hex_, $i, 2)));
}

if($b64 != "") {
	//$ascii = gzinflate($gzip);
	$ascii = base64_decode($b64);
}

if($char != "") {
	$char_ = preg_split("/\\D+/",trim($char));
	foreach ($char_ as $key)
	$ascii = $ascii.chr($key);
}

echo htmlentities($ascii);
}
?></textarea>
<br>
<input type="submit" class="btn" value="ENCODE">
</td></form>

<!--CHAR-->
<form method="POST"><td align=center valign=top>

<b>[ <a target="_parent" href="http://fr.wikipedia.org/wiki/ASCII">ASCII Décimal </a> ]</b><br>
<textarea cols=34 rows=10 wrap="virtual" name="char" class="ff"><?php
if($char != "") echo $char;
else if($ascii != "") {
for($i = 0; $i < strlen($ascii); $i = $i + 1)
	{
	$nb = ord(substr($ascii, $i, 1));
	if($nb<100) 
		echo "0".$nb." ";
		else
		echo $nb." ";
}
}
?></textarea>
<br>
<input type="submit" class="btn" value="DÉCODE">
</td></form>
</tr>

<tr>
<!-- BINAIRE -->
<form method="POST"><td align=center valign=top>
<b>[ <a target="_parent" href="http://fr.wikipedia.org/wiki/Code_binaire"><acronym title="Binaire">BINAIRE</acronym></a> ]</b><br>
<textarea cols=35 rows=10 wrap="virtual" name="binary" class="ff"><?php

if($binary != "") echo $binary;
else if($ascii != "") {
$val = strval(decbin(ord(substr($ascii, 0, 1))));
echo str_repeat("0", 8-strlen($val)).$val;
for($i = 1; $i < strlen($ascii); $i = $i + 1) {
$val = strval(decbin(ord(substr($ascii, $i, 1))));
echo " ".str_repeat("0", 8-strlen($val)).$val;
}
}
?>
</textarea>
<br>
<input type="submit" class="btn" value="DÉCODE">
</td></form>

<!-- HEXA -->
<form method="POST"><td align=center valign=top>

<b>[ <a target="_parent" href="http://fr.wikipedia.org/wiki/Hexad%C3%A9cimal"><acronym title="Hexadécimal">HEXA</acronym></a> ]</b><br>
<textarea cols=34 rows=10  wrap="virtual" name="hex" class="ff"><?php

if($hex != "") echo $hex;
else if($ascii != "") {
$val = dechex(ord(substr($ascii, 0, 1)));
echo str_repeat("0", 2-strlen($val)).$val." ";
for($i = 1; $i < strlen($ascii); $i = $i + 1) {
  $val = dechex(ord(substr($ascii, $i, 1)));
  echo " ".str_repeat("0", 2-strlen($val)).$val." ";
}
}
?></textarea>
<br>
<input type="submit" class="btn" value="DÉCODE">
</td></form>
</tr>

<tr>
<!--BASE 64-->
<form method="POST"><td align=center valign=top>

<b>[ <a target="_parent" href="http://fr.wikipedia.org/wiki/Base64">BASE64</a> ]</b><br>
<textarea cols=35 rows=10 wrap="virtual" name="b64" class="ff"><?php
if($b64 != "") echo $b64;
else if($ascii != "") {
echo base64_encode($ascii);
}
?>
</textarea>
<br>
<input type="submit" class="btn" value="DÉCODE">
</td></form>

<td align=center valign=center width="40em">
Le code-source originel de cette page est disponible auprès de Paul Schou à 
<a target="_parent" href="http://home.paulschou.net/tools/xlate/source.php">cette adresse</a>.<br>
L'idée initiale est due à Nick Ciske, voir <a target="_parent" href="http://nickciske.com/binary">http://nickciske.com/binary</a>.
</td></tr></table>
</body>
</html>
