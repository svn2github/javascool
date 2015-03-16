---
layout: default
title: Développer autour de Java'sCool 4.9
---

## Le fichier *completion.json* de complétion automatique au niveau de l'éditeur

* Pour définir la complétion automatique dans l'éditeur, il suffit de définir un fichier *completion.json* de la forme :
	
	[
	  {
		"name"  : "nom de la complétion" 
		"title" : "description en ligne"
		"code"  : "Texte source de la complétion"
		"doc"   : "Texte qui documente la fonction de l'on complète"
	  }
	  ../..
	]
	

* Le champ title permet, en autre, de distinguer deux complétions qui ont le même nom.
* Le texte qui documente la fonction est du texte ordinaire, sans HTML. 
* Les quatre champs sont obligatoires, et 
  * si le champ *doc* est omis c'est le champ *title* qui est pris en compte,
  * si le champ *code* est omis c'est le champ *name* qui est pris en compte.

* Exemple :
	
	[
	  {
		"name"  : "readString()" 
		"title" : "Lit une String dans la variable s"
		"code"  : "String s = readString();"
		"doc"   : "Crée une variable s qui va contenir une chaîne de caractères demandée à l'utilisateur."
	  }
	  {
		"name"  : "readString()" 
		"title" : "Demande à l'utilisateur une chaîne"
		"code"  : "readString()"
		"doc"   : "Lit une chaîne de caractère dans une fenêtre présentée à l'utilisateur."
	  }
	]
	
