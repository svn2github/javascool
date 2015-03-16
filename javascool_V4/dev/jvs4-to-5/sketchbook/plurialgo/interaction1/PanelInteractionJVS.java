/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2013.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.interaction1;

import javax.swing.JTabbedPane;

/**
 * Cette classe adapte à l'environnement Javascool
 * certains paramètres du conteneur par défaut de l'interface graphique.
*/
public class PanelInteractionJVS extends PanelInteraction {
	private static final long serialVersionUID = 1L;
	
	public PanelInteractionJVS() {
		super();
	}
		
	/*
	 * Redéfinition pour masquer l'onglet Complements.
	 */
	public void initOnglets() {
		// nord
		pConsole = new PanelConsole(null);
		this.add(pConsole,"North");
		// centre
		onglets = new JTabbedPane(JTabbedPane.TOP);
		onglets.setBackground(null);
		pPrincipal = new PanelPrincipal(this);	onglets.add("Principal", pPrincipal);
		pEdition = new PanelProgrammes(this); onglets.add("Résultats", pEdition);
		pHtml = new PanelHtml(this); onglets.add("Documentation", pHtml);
		pXml = new PanelXml(this);  // onglets.add("Compléments", pXml);  // caché pour des lycéens
		this.add(onglets, "Center");
	}

	/*
	 * Redéfinition pour changer la configuration (langages, l'url de documentation...).
	 */
	public void initConfig() {
		PanelInteraction.urlDoc = "/org/javascool/proglets/plurialgo/";
		String[] langages = { "javascool", "vb", "larp", "javascript", "java" };
		PanelInteraction.langList = langages;
	}

}
