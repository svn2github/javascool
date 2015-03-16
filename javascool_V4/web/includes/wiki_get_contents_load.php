<?php

function wiki_get_contents_load($name) {
  global $wiki_get_contents_redirections;
  // Recuperation de la page sur le wiki
  $text = file_get_contents('https://wiki.inria.fr/sciencinfolycee/'.$name.'?printable=yes&action=render');  
  // Remplace tous les liens entre pages par des pages vues du site
  foreach($wiki_get_contents_redirections as $wiki => $php) 
    $text = ereg_replace("href=\"http[s]?://wiki.inria.fr/sciencinfolycee/$wiki\"", "class=\"internal\" href=\"$php\"", $text);
  // Remplace tous les liens wikis locaux pas des liens distants
  $text = ereg_replace('src="(/wikis)?/sciencinfolycee', 'src="http://wiki.inria.fr/wikis/sciencinfolycee', $text);
  // Qualifie proprement les liens internes issus du wiki
  $text = ereg_replace("href=\"http://javascool.gforge.inria.fr(/v4)*([^\"]*)\"( *class=\"external text\")?", "href=\"/v4/\\2\" class=\"internal\"", $text);
  // Elimine la table de méta-donnée
  $text = ereg_replace('<table class="wikitable">.*</table>', '', $text);
  // Détecte les erreurs
  if (ereg("<title>(Erreur|Connexion nécessaire)", $text))
    $text = "Erreur: la page wiki $name est en erreur.\n";
  return $text;
}

// Redirections des pages du wiki
$wiki_get_contents_redirections = array(
					"JavaScool:Accueil" => "?page=home",
					"JavaScool:Actualité" => "?page=home",
					"JavaScool:Ailleurs" => "?page=home&action=faq-ailleurs",
					"JavaScool:About" => "?page=home&action=about",
					"JavaScool:Faq" => "?page=home&action=faq",
					"JavaScool:Ailleurs" => "?page=home&action=faq-ailleurs",
					"JavaScool:AutresInitiatives" => "?page=home&action=faq-autres",
					"JavaScool:Cadrage" => "?page=home&action=faq-cadrage",
					"JavaScool:InfoAuLycee" => "?page=home&action=faq-infolycee",
					"JavaScool:Lancement" => "?page=run",
					"JavaScool:Licence" => "?page=run&action=licence",
					"JavaScool:Screenshot" => "?page=run&action=screenshot",
					"JavaScool:Cr%C3%A9dits" => "?page=contact&action=credits",
					"JavaScool:Proglet" => "?page=proglets&action=info",
					"JavaScool:Développement" => "?page=developers", 
					"JavaScool:DocCreationProglet" => "?page=developers&action=spec-proglets",
					"JavaScool:DocCreationProgletExemple" => "?page=developers&action=doc-proglets",
					"JavaScool:ProgletProcessing" => "?page=developers&action=doc-processing",
					"JavaScool:UsingNetbeans" => "?page=developers&action=using-netbeans",
					"JavaScool:DocFormatHml" => "?page=developers&action=doc-hml",
					"JavaScool:DocumentsHml" => "?page=developers&action=spec-hml",
					"JavaScool:EditorCompletion" => "?page=developers&action=doc-completion",
					"JavaScool:DocJavaScoolBuilder" => "?page=developers&action=doc-javascoolbuilder",
					"JavaScool:SpecJavaScoolBuilder" => "?page=developers&action=spec-javascoolbuilder",
                                        "JavaScool:SyndicationWiki" => "?page=developers&action=syndication-wiki",
					"JavaScool:FaqD%C3%A9veloppement" => "?page=developers&action=faq-developers",
					"JavaScool:Ressources" => "?page=resources",
					"JavaScool:ProgletsProcessing" => "?page=resources&action=link-processing",
					"JavaScool:Activites" => "?page=resources&action=link-proglets",
					"JavaScool:TPE-Accueil" => "?page=resources&action=link-tpe-accueil",
					"JavaScool:Pepites" =>  "?page=resources&action=link-pepites",
					"JavaScool:Revues" =>  "?page=resources&action=link-revues",
					"JavaScool:TPE-Quizz" =>  "?page=resources&action=link-quizz",
					"JavaScool:TPE-Sujets" =>  "?page=resources&action=link-idees-tpe",
                                        "JavaScool:TPE-Demos" =>  "?page=resources&action=link-tpe-demos",
                                        "JavaScool:TPE-Exemples" =>  "?page=resources&action=link-tpe-exemples",
                                        "JavaScool:TPE-Interventions" =>  "?page=resources&action=link-interventions",
                                        "JavaScool:TPE-Methode" =>  "?page=resources&action=link-tpe-methode",
					);
?>