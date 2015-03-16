---
layout: default
title: Développer autour de Java'sCool 4.9
---

## Les fichiers *Functions.java*, *Panel.java*, etc.

* Toutes les classes doivent être dans le package *org.javascool.proglets.*identificateur**.

* Tous les sources sont à metter dans le répertoire de la proglet, sans sous-répertoire.

### Le fichier *Functions.java*

* Ce fichier définit les fonctions directement accessibles depuis Java's Cool, et qui permettent donc d'interagir avec la proglet.
  * le nom *Functions.java* est fixé par les spécifications.

* Dans cet exemple, nous définissons la méthode *setMessage(String)* qui permet de changer le texte affiché et on note :
  * La proglet de nom *sampleCode* est définie dans un package de nom *org.javascool.proglets.sampleCode*.
  * La classe se nomme bien *Functions.java*.
  * Elle possède une méthode *getPane()* qui permet d'accéder à l'objet graphique
	
	package org.javascool.proglets.sampleCode;
	import static org.javascool.macros.Macros.*;
	
	public class Functions {
	  /** Renvoie l'instance de la proglet. */
	  private static Panel getPane() {
	    return getProgletPane();
	  }
	  /** Permet de changer le message affiché sur le panel de la proglet */
	  public static void setMessage(String text) {
	    getPane().label.setText(text);
	  }
	}
	

### Le fichier *Panel.java*

* C'est le fichier qui contient la description de l'objet graphique qui incarne la proglet. 
  * le nom *Panel.java* est fixé par les spécifications.

* Cette classe Java implémente le code à exécuterau lancement de la proglet (initialisation des variables, lancer des tâches, etc..).

* La classe Panel doit hériter d'un composant graphique tel que *javax.swing.JPanel* ou *java.awt.Component*

* Dans l'exemple qui suit on note que :
  * La proglet de nom *sampleCode* est définie dans un package de nom *org.javascool.proglets.sampleCode*.
  * La classe se nomme bien *Panel.java* et hérite de *javax.swing.JPanel*.
	
	package org.javascool.proglets.sampleCode;
	import javax.swing.JLabel;

	public class Panel extends javax.swing.JPanel {
	  // Construction de la proglet
	  public Panel() {
	    // On crée un label
	    label = new JLabel("");
	    // Et on l'ajoute au panel
	    add(label);
	  }
	  // Ce label sera utilisé par la routine Functions.setMessage()
	  JLabel label;
	}
	
----

* *Note*: La plateforme Java's Cool propose:
  * Des [widgets](../../javascool-core-api/org/javascool/widgets/package-summary.html) pour aider à créer des interfaces graphiques.
  * Des [macros](../../javascool-core-api/org/javascool/macros/package-summary.html) et des [tools](../../javascool-core-api/org/javascool/tools/package-summary.html) disposer de fonctions utilitaires facilitant l'implémentation.
  * Un mécanisme d'[assertion](../../javascool-core-api/org/javascool/macros/Macros.html#assertion%28boolean,%20java.lang.String,%20java.lang.Object%29) aide à débugger le code. 
