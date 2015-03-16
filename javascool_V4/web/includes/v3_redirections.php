<?php // Mécanisme de redirection du site v3
include('includes/mailto.php');

// Effectue la redirection des liens et renvoie vraie si il y eu redirection et faux sinon
function v3_redirections($page) {
  global $v3_redirections_link_table, $v3_redirections_prefix_table, $v3_redirections_v4pages_table, $_SERVER;
  // Redirection des liens qui sont dans la table
  if (array_key_exists($page, $v3_redirections_link_table)) {
    header("Location: ".$v3_redirections_link_table[$page]);
    return true;
  }
  // Redirection sur les prefix des liens v3
  foreach($v3_redirections_prefix_table as $prefix => $redirect)
    if (strncmp($page, $prefix, strlen($prefix)) == 0) {
      header("Location: ".$redirect);
      return true;
    }
  // Envoie de mail si une page bizarre est demandée
  if (strlen($page) > 0 && !in_array($page, $v3_redirections_v4pages_table)) {
    echo "
    <h4>Uppss vous être en train de demander une page JavaScool qui n'existe pas (ou plus)</h4>
    <b>N'hésitez pas, si besoin, à nous <a href='mailto:thierry.vieville@inria.fr?subject=broken-link-on-javascool-web ($page)'>contacter</a>, nous allons vous dépanner.</b>
    <hr>";
    if (!strncmp("http:", $page, 5) != 0) {
      // mailto("mailto:thierry.vieville@inria.fr?subject=broken-link-on-javascool-web", "Spurious page=$page, request = ".$_SERVER["REQUEST_URI"]);
    }
    return true;
  }
  return false;
}

$v3_redirections_v4pages_table = array("api", "contact", "developers", "home", "proglets", "resources", "run");
		
$v3_redirections_prefix_table = array(
				    "api:" =>  "?page=api",
				    "doc%3Asketchbook" => "?page=resources&action=link-processing");
$v3_redirections_link_table = array(
			     "Accueil" => "?page=home",
			     "Menu" => "?page=home",
			     "Manifest" => "?page=home&action=manifest",
			     "Manifeste" => "?page=home&action=manifest",
			     "Faq" => "?page=home&action=faq",
			     "Contacts" => "?page=contact",
			     "Contact" => "?page=contact",
			     "contact%22" => "?page=contact",
			     "contactcontact%22" => "?page=contact",
			     "cr%c3%a9dits" => "?page=contact&action=credits",
			     "Cr%c3%a9dits" => "?page=contact&action=credits",
			     "Cr%25C3%25A9dits" => "?page=contact&action=credits",
			     "crédits" => "?page=contact&action=credits",
			     "Crédits" => "?page=contact&action=credits",
			     "Lancement" => "?page=run",
			     "Telechargement" => "?page=run",
			     "Licence"  => "?page=run&action=licence",
			     "licence"  => "?page=run&action=licence",
			     "Proglet" =>  "?page=proglets",
			     "Developpement" => "?page=developers",
			     "Ressources" => "?page=resources",
			     "glossaire" => "http://interstices.info/jcms/jalios_5358/glossaire-interstices",
			     "Activites" => "?page=proglets",
			     "Ailleurs" => "?page=home&action=faq-ailleurs",
			     "Ailleurs%22" => "?page=home&action=faq-ailleurs",
			     "AutresInitiatives" => "?page=home&action=faq-autres",
			     "AutresDeveloppements" => "?page=ressources",
			     "Cadrage" => "?page=home&action=faq-cadrage",
			     "Pepites" => "?page=resources&action=link-pepite",
			     "Perpites" => "?page=resources&action=link-pepite",
			     "doc%3A%2Fdocuments%2Fnos-ressources%2Fpepites.html" => "?page=resources&action=link-pepite",
			     "Revues" => "?page=resources&action=link-revues",
			     "doc%3A%2Fdocuments%2Fnos-ressources%2Frevues.html" => "?page=resources&action=link-revues",
			     "doc%3A%2Fdocuments%2Fquelques-t-p-e%2Findex.html" => "?page=resources&action=link-revues", 
			     "TPE-Quizz" => "?page=resources&action=link-quizz", 
			     "TPE-Quizz\"" => "?page=resources&action=link-quizz", 
			     "TPE-Quizz%22" => "?page=resources&action=link-quizz", 
			     "doc%3A%2Fdocuments%2Fspeed-dating-09%2Findex.html" => "?page=resources&action=link-quizz",
			     "TPE-Sujets" => "?page=resources&action=link-idees-tpe",
			     "TPE-Accueil" => "?page=resources&action=link-tpe-accueil",
			     "tpe-accueil" => "?page=resources&action=link-tpe-accueil",
			     "TPE-Demos" => "?page=resources&action=link-tpe-demos",
			     "TPE-Exemples" => "?page=resources&action=link-tpe-exemples",
			     "TPE-Interventions" => "?page=resources&action=link-interventions",
			     "Interventions" => "?page=resources&action=link-interventions",
			     "TPE-Methode" => "?page=resources&action=link-tpe-methode",
			     "doc%3Adocuments%2Fquelques-t-p-e%2Faffiche_finale_inria.pdf" => "doc/documents/quelques-t-p-e/affiche_finale_inria.pdf",
			     "doc:documents/quelques-t-p-e/affiche_finale_inria.pdf" => "doc/documents/quelques-t-p-e/affiche_finale_inria.pdf",
			     "doc%3Adocuments%2Fquelques-t-p-e%2Fquelques-t-p-e.pdf" => "doc/documents/quelques-t-p-e/quelques-t-p-e.pdf",
			     "doc:documents/quelques-t-p-e/quelques-t-p-e.pdf"=>  "doc/documents/quelques-t-p-e/quelques-t-p-e.pdf",
			     "doc%3A%2Fsketchbook%2Findex.html" => "?page=resources&action=link-processing",
			     "api:%3A%2Forg%2Fjavascool%2Fdoc-files%2Fabout-proglets.html" => "?page=resources&action=link-proglets",
			     "api%20documentation" => "?page=api",
			     // Il y a parfois des accès à des liens v4 sans prefix ?!?
			     "InfoAuLycee" => "?page=home&action=faq-infolycee",
			     "Screenshot" => "?page=run&action=screenshot",
			     "DocJavaScoolBuilder" => "?page=developers&action=doc-javascoolbuilder",
			     );
?>