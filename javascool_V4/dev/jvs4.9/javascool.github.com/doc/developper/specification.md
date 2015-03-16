---
layout: default
title: Développer autour de Java'sCool 4.9
---

## Spécification d'une «proglet» 

### Identificateur de la proglet 

* Le nom doit avoir au plus seize lettres minuscules ou majuscules; pas de chiffre ou de symbole. 
* Il ne doit contenir que des lettres faire au moins quatre caractères et au pluse seize et démarrer par une minuscule. 
* Il doit être de la forme «algoDeMaths» pour «algo de maths» c'est à dire, sans espace, commencer par une minucule et les autres mots par une majuscule. 
* L'identificateur de chaque proglet doit être unique. Regarder sur la [table des proglets](../../wpages/proglets.html) les noms existants. 

### Dossier d'une proglet

* Tous les fichiers nécessaires au fonctionnement de la proglet sont placés dans un dossier. 
* Le nom de ce dossier doit avoir le nom de la proglet.
* Il contient tous les fichiers de la proglet

* Fichiers de description (obligatoires)
  * *[proglet.json](./proglet-json.html)* : le descripteur de la proglet et son icône.
  * *[help.html](./help-html.html)* : le fichier de documentation de la proglet.

* Fichiers d'implémentation (optionnels)
  * *[Functions.java](./functions-java.html)*: définit les fonctions proposées à l'élève, si il y a des fonctions spécifiques à la proglet.
  * *[Panel.java](./functions-java.html)* : implémente le panneau graphique de la proglet, si l'activité ne se fait pas à la console.

* Fichiers supplémentaires (optionnels)
  * *[completion.json](./completion-json.html)* : qui définit les complétions automatiques proposées dans l'éditeur.
  * *[Translator.java](./translator-java.html)* : qui implémente la traduction de Jvs en Java spécifique de cette proglet. 

* Autres fichiers de ressources (ne pas créer de sous-répertoire)
  * Tous les fichiers de documentation, code ou ressources nécessaires à l'utilisation de la proglet.
  * Des fichiers Java'sCool  d'extension *.jvs* qui servent d'exemple à l'élève.

* Fichiers contenant des librairies logicielles eternes
  * Les fichiers JAR contenant des classes Java externes (des extensions) qui sont utilisés par la proglet sont simplement ajoutés dans le repertoire.
  * Les librairies dynamiques à installer sont ajoutés dans les sous répertoires :
    * *./win32/*.dll* : pour les DLL windows
    * *./i686/*.so* ou *./amd64/*.so* : pour les librairies dynamiques 32bits ou 64 bits Linux ou MacOS

### Documents de la proglet

* Tous les documents qui ne sont pas à utiliser en local
  * sont à faire déposer sur la [forge de publication](http://javascool.gforge.inria.fr/documents/sketchbook).
  * http://javascool.gforge.inria.fr/documents/sketchbook/«nom-de-la-proglet»


