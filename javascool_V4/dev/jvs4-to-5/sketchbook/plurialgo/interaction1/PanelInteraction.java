/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2013.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.interaction1;

import java.awt.*;
import javax.swing.*;

import org.javascool.proglets.plurialgo.divers.Divers;
import org.javascool.proglets.plurialgo.langages.modele.Programme;
import org.javascool.proglets.plurialgo.langages.xml.Intermediaire;


/**
 * Cette classe correspond au conteneur de l'interface graphique, à intégrer
 * ensuite dans une fenêtre swing, une applette ou l'environnement Javascool.
*/
public class PanelInteraction extends JPanel {	
	private static final long serialVersionUID = 1L;
	
	public PanelConsole pConsole;
	public JTabbedPane onglets;
	public PanelProgrammes pEdition;
	public PanelHtml pHtml;
	public PanelPrincipal pPrincipal;
	public PanelXml pXml;
	
	public static String[] langList = { "javascool", "vb", "larp", "javascript", "php", "python", "java" };
	public static String dirTravail = null;
	public static String urlDoc = null;

	public PanelInteraction() {
		super(new BorderLayout());
		this.initConfig();
		this.initOnglets();
	}
	
	/**
	 * L'initialisation des onglets (notamment le masquage de l'onglet Compléments)
	 * peut être redéfini dans une classe dérivée.
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
		pXml = new PanelXml(this);  onglets.add("Compléments", pXml);
		this.add(onglets, "Center");
	}
	
	/**
	 * L'initialisation de la configuration (liste des langages, url de la documentation...) 
	 * peut être redéfinie dans une classe dérivée.
	 */
	public void initConfig() {
		// urlDoc
		PanelInteraction.urlDoc = "http://web.univ-pau.fr/~raffinat/plurialgo/jvs/aideJVS/";
	}
		
	public void selectPanel(Component pn) {
		if (pn==pEdition) {
			onglets.setSelectedComponent(pn);
		}
		else if (pn==pPrincipal) {
			onglets.setSelectedComponent(pn);
		}
		else if (pn==pHtml) {
			onglets.setSelectedComponent(pn);
		}
	}

	// ---------------------------------------------
	// Console
	// ---------------------------------------------
	
	public void clearConsole() {
		pConsole.clearConsole();
	}	
	
	public void writeConsole(String txt) {
		pConsole.writeConsole(txt);
	}	
	
	public void writelnConsole() {
		pConsole.writelnConsole();
	}
	
	public boolean messageWarning(org.javascool.proglets.plurialgo.langages.modele.Programme prog) {
		if (prog.buf_warning.length()>0) {
			this.clearConsole();
			this.writeConsole("---------- Avertissement ----------\n");
			this.writeConsole(prog.buf_warning.toString());
			return true;
		}
		return false;
	}
	
	public boolean messageErreur(org.javascool.proglets.plurialgo.langages.modele.Programme prog) {
		if (prog.buf_error.length()>0) {
			this.clearConsole();
			this.writeConsole("---------- Erreur ----------\n");
			this.writeConsole(prog.buf_error.toString());
			return true;
		}
		return false;
	}
	
	// ---------------------------------------------
	// Xml
	// ---------------------------------------------

	public boolean add_xml(Programme prog) {
		StringBuffer buf_xml = prog.getXmlBuffer();
		if (this.messageErreur(prog)) {
			return false;
		}
		else if (this.messageWarning(prog)) {
			Divers.remplacerSpeciaux(buf_xml);
			pXml.setText(buf_xml.toString());
			return true;
		}
		else {
			Divers.remplacerSpeciaux(buf_xml);
			pXml.setText(buf_xml.toString());
			return true;
		}
	}
	
	public Programme getProgramme(String txt_xml, String nom_lang) {
		//return BindingCastor.getProgramme(txt_algo, nom_lang);
		return org.javascool.proglets.plurialgo.langages.modele.Programme.getProgramme(txt_xml, nom_lang);
	}	

	public void traduireXml() {
		String lang = pPrincipal.getNomLangage();
		String txt = pXml.getText();
		if (lang.equals("cplus") || lang.equals("java") || lang.equals("javascript") 
				|| lang.equals("perl") || lang.equals("php") || lang.equals("python")
				|| lang.equals("scala") || lang.equals("javascool")) {
			StringBuffer buf = new StringBuffer(pXml.getText());
			Divers.remplacer(buf, "]", "-1]");
			Divers.remplacer(buf, "+1-1]", "]");
			pXml.setText(buf.toString());
		}
		traduireXml(pPrincipal.getNomLangage());
		if (lang.equals("cplus") || lang.equals("java") || lang.equals("javascript") 
				|| lang.equals("perl") || lang.equals("php") || lang.equals("python") 
				|| lang.equals("scala") || lang.equals("javascool")) {
			pXml.setText(txt);
		}
	}
	
	private boolean traduireXml(String nom_lang) {
		// récupération du programme
		Programme prog = getProgramme(pXml.getText(),nom_lang); 
		if (this.messageErreur(prog)) {
			System.out.println("erreur:"+prog.getXmlBuffer().toString());
			return false;
		}
		prog.ecrire2();
		// edition du programme dans l'onglet Edition
		this.add_editeur(prog);
		this.selectPanel(this.pEdition);
		return true;
	}
	
	// ---------------------------------------------
	// Editeur
	// ---------------------------------------------
		
	public void add_editeur(org.javascool.proglets.plurialgo.langages.modele.Programme prog) {
		this.pEdition.add_editeur(prog);
	}	
	
    public void colorier(String le_fichier) {
    	this.pEdition.colorier(le_fichier);
    }
	
	// ---------------------------------------------	
	// méthodes de création d'intermediaire	
	// ---------------------------------------------
	
	public Intermediaire creerIntermediaire() {
		Intermediaire inter = new Intermediaire();
		PanelPrincipal fen = this.pPrincipal;
		inter.nom_algo = fen.algoField.getText();
		if (inter.nom_algo.equalsIgnoreCase("patrick")) inter.nom_algo = "raffinat";
		inter.nom_langage = fen.langList.getSelectedValue().toString(); 
		inter.donnees = fen.donneesField.getText();
		inter.resultats = fen.resultatsField.getText();
		inter.reels = fen.reelsField.getText();
		inter.entiers = fen.entiersField.getText();
		inter.textes = fen.textesField.getText();
		inter.booleens = fen.booleensField.getText();
		inter.tab_reels = fen.tab_reelsField.getText();
		inter.tab_entiers = fen.tab_entiersField.getText();
		inter.tab_textes = fen.tab_textesField.getText();
		inter.tab_booleens = fen.tab_booleensField.getText();
		inter.mat_reels = fen.mat_reelsField.getText();
		inter.mat_entiers = fen.mat_entiersField.getText();
		inter.mat_textes = fen.mat_textesField.getText();
		inter.mat_booleens = fen.mat_booleensField.getText();
		inter.niv_saisie = fen.niv_saisieList.getSelectedValue().toString(); 
		inter.niv_calcul = fen.niv_calculList.getSelectedValue().toString(); 
		inter.niv_affichage = fen.niv_affichageList.getSelectedValue().toString(); 
		inter.niv_groupement = fen.niv_groupementList.getSelectedValue().toString(); 
		inter.nom_groupe = fen.groupeField.getText();
		return inter;
	}
	
	public Intermediaire creerIntermediaireLarp(String option) {
		Intermediaire inter = this.creerIntermediaire();
		if ("traduire".equals(option)) {
			inter.donnees="";
			inter.resultats="";
			inter.niv_saisie="elementaire";
			inter.niv_affichage="elementaire";
			inter.niv_calcul="elementaire";
			inter.niv_groupement="elementaire";			
		}
		else if ("reformuler".equals(option)) {
			inter.niv_saisie="elementaire";
			inter.niv_affichage="elementaire";
			inter.niv_calcul="elementaire";
			inter.niv_groupement="elementaire";
		}
		else if ("vectoriser".equals(option)) {
			inter.donnees="";
			inter.resultats="";
			inter.niv_saisie="elementaire";
			inter.niv_affichage="elementaire";
			inter.niv_calcul="elementaire";
			inter.niv_groupement="elementaire";				
		}
		return inter;
	}

}
