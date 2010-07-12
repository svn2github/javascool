<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> 
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="fr" lang="fr"> 
<head> 
	<title>Java's Cool</title> 
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /> 
	<meta http-equiv="Pragma" content="no-cache" /> 
	<meta http-equiv="Content-Style-Type" content="text/css" /> 
	<meta http-equiv="Content-Script-Type" content="text/javascript" /> 
	<meta http-equiv="Content-Language" content="fr" /> 
	<meta name="robots" content="index,follow" /> 
 
	<link href="styles/style.css" type="text/css" rel="stylesheet" /> 
</head> 
<body>
	
	<?php include("header.html"); ?>
	<?php include("menu.html"); ?>
		
	<table>
		<tr>
			<TR> 
			<TD valign="top" width="100%"> 
				
					<!-- le corps de la page-->
				<DIV class="greybox_right">
				<diV class="greybox_left">

				<DIV class="greybox_top">
				<diV class="greybox_top_l">
				<diV class="greybox_top_r">
				<div class="greybox_bottom">
				<diV class="greybox_bottom_l">
				<diV class="greybox_bottom_r">
					<DIV class="content"> 
						<?php 
							if(isset($_GET['dest'])){
								$dest = $_GET['dest'];
							}else{
								$dest = "accueil";
							}
							switch($dest){
								case "accueil" : 
								
									include("news.html");
									include("accueil.html");
								break;
								case "download" : 
									include("download.html");
								break;
								case "manuels" : 
									include("manuels.html");
								break;
								case "forge" : 
									include("forge.html");
								break;
								case "contacts": 
									include("contacts.html");
								break;
								case "news" :
								
									include("news.html");
									break;
								case "faq";
									include("faq.html");
									break;
								case "screenshot" :
									include("screenshot.html");
									break;
								case "licence" :
									include("licence.html");
									break;
								case "rapport" :
									include("rapport.html");
									break;
							}
						?>
						<br/><br/>
						
					</DIV> 
							
				</DIV> 
				</div>
				</div>
				</DIV> 
				</div>
				</div>
				</div>
				</div>
				 
			</TD> 
			<TD valign="top"> 
				<?php include("menu_right.html"); ?>
			</td>
		
		</tr>
		
		
	</table>
		
	<?php include("foot.html"); ?>
</body>
</html>