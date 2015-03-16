/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2013.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.interaction2;

import java.awt.*;
import java.util.Iterator;

import javax.swing.*;

import org.javascool.proglets.plurialgo.divers.Divers;
import org.javascool.proglets.plurialgo.langages.modele.Programme;
import org.javascool.proglets.plurialgo.langages.xml.Intermediaire;
import org.javascool.widgets.Console;
import org.javascool.gui.Desktop;


/**
 * Cette classe correspond au conteneur de l'interface graphique.
*/
public class PanelInteraction extends JPanel {	
	private static final long serialVersionUID = 1L;
	
	public JTabbedPane onglets;
	public PanelBoucles pEdition;
	public PanelHtml pHtml;
	public PanelPrincipal pPrincipal;
	public PanelXml pXml;
	
	public static String[] langList = { "javascool", "vb", "larp", "javascript", "java", "php", "python" };
	public static String dirTravail = null;
	public static String urlDoc = "/org/javascool/proglets/plurialgo/";

	public PanelInteraction() {
		super(new BorderLayout());
		this.initOnglets();
	}

	private void initOnglets() {
		onglets = new JTabbedPane(JTabbedPane.TOP);
		onglets.setBackground(null);
		pPrincipal = new PanelPrincipal(this);	onglets.add("Principal", pPrincipal);
		pEdition = new PanelBoucles(this); onglets.add("Boucles", pEdition);
		pHtml = new PanelHtml(this); onglets.add("Documentation", pHtml);
		pXml = new PanelXml(this);  // onglets.add("Complements", pXml);
		this.add(onglets, "Center");
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
		Console.getInstance().clear();
	}	
	
	public void writeConsole(String txt) {
		Desktop.getInstance().focusOnConsolePanel();
		Console.getInstance().print(txt);
	}	
	
	public void writelnConsole() {
		Console.getInstance().print("\n");
	}
	
	public boolean messageWarning(Programme prog) {
		if (prog.buf_warning.length()>0) {
			this.clearConsole();
			this.writeConsole("---------- Avertissement ----------\n");
			this.writeConsole(prog.buf_warning.toString());
			return true;
		}
		return false;
	}
	
	public boolean messageErreur(Programme prog) {
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
				|| lang.equals("perl") || lang.equals("php") || lang.equals("python")) {
			pXml.setText(txt);
		}
	}
	
	private boolean traduireXml(String nom_lang) {
		// recuperation du programme
		Programme prog = Programme.getProgramme(pXml.getText(),nom_lang); 
		if (this.messageErreur(prog)) {
			System.out.println("erreur:"+prog.getXmlBuffer().toString());
			return false;
		}
		prog.ecrire2();
		// edition du programme dans l'onglet Edition
		this.add_editeur(prog);
		//this.selectPanel(this.pEdition);
		return true;
	}
	
	// ---------------------------------------------
	// Editeur
	// ---------------------------------------------
	
	public String getText() {
		//String txt = org.javascool.gui.EditorWrapper.getText();
		String txt = org.javascool.gui.EditorWrapper.getRTextArea().getText();
		return txt;
	}	
	
	public void setText(String txt) {
		Desktop.getInstance().openNewFile();
		org.javascool.gui.EditorWrapper.setText(txt);
	}	
	
	public void add_editeur(org.javascool.proglets.plurialgo.langages.modele.Programme prog) {
		Iterator<String> iter = prog.les_fichiers.keySet().iterator();
		while (iter.hasNext()) {
			String nom_fich = iter.next();
			StringBuffer buf = prog.les_fichiers.get(nom_fich);
			setText(buf.toString());
		}
	}	
	
	public boolean isJavascool() {
		String txt=this.getText();
		if (txt.contains("void ") && txt.contains(" main()")) return true;
		if (txt.contains("void ") && txt.contains(" main( )")) return true;
		return false;
	}	
	
	public boolean isVb() {
		String txt=this.getText().toLowerCase();
		if (txt.contains("sub ")) return true;
		if (txt.trim().length()==0) return true;
		return false;
	}	
	
	public boolean isLarp() {
		String txt=this.getText().toLowerCase();
		if (isAlgobox()) return false;
		if ((txt.contains("debut")||txt.contains("début")||txt.contains("but"))
				&&txt.contains("fin")) return true;
		if (txt.contains("entrer")&&txt.contains("retourner")) return true;
		//if (txt.trim().length()==0) return true;
		return false;
	}	
	
	public boolean isAlgobox() {
		String txt=getText().toLowerCase();
		if (txt.contains("debut_algorithme")&&txt.contains("fin_algorithme")) return true;
		return false;
	}	
	
	// ---------------------------------------------	
	// methodes de creation d'intermediaire	
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
