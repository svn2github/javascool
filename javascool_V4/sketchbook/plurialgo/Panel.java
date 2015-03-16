/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2013.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo;

/**
 * Definit le panneau graphique de la proglet
 * 
 * @see <a href="Panel.java.html">code source</a>
 * @serial exclude 
 */
public class Panel extends org.javascool.proglets.plurialgo.interaction2.PanelInteraction {
	private static final long serialVersionUID = 1L;
	
	/* langages disponibles (par ordre alphabetique) :
	 * 	- algobox
	 *  - carmetal (javascript rhino)
	 *	- java
	 *	- javascool
	 *	- javascript
	 *	- larp
	 *	- php
	 *	- python
	 *	- r
	 *	- visual basic
	 *	- xcas 
	 */
	
	private static String[][] mesLangages = {
		// {nom, (org.javascool.proglets.plurialgo.)package, debut_tableau}
		{"Javascool", "javascool", "0"},
		{"Algobox", "algobox", "1"},
		{"Larp", "larp", "1"},
		{"Python", "python", "0"},
		{"Javascript", "javascript", "0"},
		{"Java", "java", "0"},
		{"Php", "php", "0"},
		{"Visual Basic", "vb", "1"},
		{"CarMetal", "carmetal", "1"},
		{"Xcas", "xcas", "0"},
		{"R", "R", "1"}
	};

	// @bean
	public Panel() {
		super(mesLangages,true);
		// ---------------------------------------------------------------------------------
		// pour forcer le chargement de classes : sinon Class.forName ne marche pas 
		// 		(voir methode creerObjet, classe AnalyseurXml du package langages.modele)
		// ---------------------------------------------------------------------------------
		new org.javascool.proglets.plurialgo.langages.algobox.Programme();
		new org.javascool.proglets.plurialgo.langages.carmetal.Programme();
		new org.javascool.proglets.plurialgo.langages.java.Programme();
		new org.javascool.proglets.plurialgo.langages.javascool.Programme();
		new org.javascool.proglets.plurialgo.langages.javascript.Programme();
		new org.javascool.proglets.plurialgo.langages.larp.Programme();
		new org.javascool.proglets.plurialgo.langages.php.Programme();
		new org.javascool.proglets.plurialgo.langages.python.Programme();
		new org.javascool.proglets.plurialgo.langages.vb.Programme();
		new org.javascool.proglets.plurialgo.langages.R.Programme();
		new org.javascool.proglets.plurialgo.langages.xcas.Programme();
	}

}
