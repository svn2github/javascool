<?php // Description du plan du site pour son affichage

// array("Name", "Link", "Info sur le contenu PHP de la page")
$sitemap_data = array(
 array("Java's Cool","index.php", "renvoie à Accueil"),
 array("Accueil",
   array("Accueil","?page=home", "inclut wiki::JavaScool:Accueil et wiki::JavaScool:Actualité"), 
   array("Manifeste","?page=home&action=manifest", "inclut wiki::JavaScool:Manifeste"), 
   array("FAQ","?page=home&action=faq", "inclut wiki::JavaScool:Faq")
 ),
 array("Lancement",
  array("Lancement","?page=run", "texte local plus compléments sur wiki::JavaScool:Lancement"),
  array("Licence","?page=run&action=licence", "nowiki")
 ),
 array("Proglets", "", "nowwiki"),
 array("Ressources",
   array("Ressources","?page=resources", "inclut wiki::JavaScool:Ressources et le reste des contenus sont le «SIL:O!» directement")
 ),
 array("Développeurs",
   array("Développeurs","?page=developers", "inclut wiki::JavaScool:Développement etc...")
       // Et 7 autres pages
 ),
 array("Contact", 
   array("Contact", "?page=contact", "nowiki"),
   array("Crédits". "?page=contact&action=credits", "inclut wiki::JavaScool:Crédits")
 )
);
?>
