<?php
include('wiki_get_contents_load.php');

foreach (array("JavaScool:SyndicationWiki" , "JavaScool:EditorCompletion" , "JavaScool:DocCreationProgletExemple" , "JavaScool:ProgletProcessing" , "JavaScool:DocFormatHml" , "JavaScool:FaqDeveloppement" , "JavaScool:SpecJavaScoolBuilder" , "JavaScool:Developpement" , "JavaScool:DocCreationProglet", "JavaScool:UsingNetbeans", "JavaScool:DocJavaScoolBuilder", "JavaScool:DocumentsHml", "JavaScool:Cadrage", "JavaScool:AutresInitiatives", "JavaScool:InfoAuLycee", "JavaScool:Manifeste", "JavaScool:About", "JavaScool:Faq", "JavaScool:Ailleurs", "JavaScool:Actualite", "JavaScool:Accueil", "JavaScool:Screenshot", "JavaScool:Lancement", "JavaScool:Activites", "JavaScool:TPE-Exemples", "JavaScool:TPE-Methode", "JavaScool:ProgletsProcessing", "JavaScool:Ressources", "JavaScool:TPE-Interventions", "JavaScool:TPE-Accueil", "JavaScool:TPE-Demos", "JavaScool:Credits", "JavaScool:Proglet") as $page) {  
  file_put_contents('./cache/'.$page, wiki_get_contents_load($page));
}

?>