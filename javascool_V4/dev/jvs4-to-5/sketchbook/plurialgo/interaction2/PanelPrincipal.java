/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2013.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.interaction2;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Color;
import java.awt.event.*;
import java.util.Iterator;
import java.util.Map;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;

import org.fife.ui.rtextarea.RTextArea;
import org.javascool.gui.Desktop;
import org.javascool.proglets.plurialgo.langages.xml.AnalyseurAlgobox;
import org.javascool.proglets.plurialgo.langages.xml.AnalyseurJavascool;
import org.javascool.proglets.plurialgo.langages.xml.AnalyseurLarp;
import org.javascool.proglets.plurialgo.langages.xml.AnalyseurVb;
import org.javascool.proglets.plurialgo.langages.xml.Intermediaire;
import org.javascool.proglets.plurialgo.langages.xml.ProgrammeDerive;
import org.javascool.proglets.plurialgo.langages.xml.ProgrammeNouveau;
import org.javascool.proglets.plurialgo.langages.xml.iAnalyseur;
import org.javascool.widgets.Console;


/**
 * Cette classe correspond à l'onglet Principal de l'interface graphique.
*/  
@SuppressWarnings("unchecked")	// car les JList doivent être paramétrées avec Java7
public class PanelPrincipal extends JPanel implements ActionListener, ListSelectionListener {
	private static final long serialVersionUID = 1L;

	static String[] niv_groupement = { "elementaire", "enregistrement", "classe" };
	static String[] niv_calcul = { "elementaire", "procedure", "fonctions" };
	static String[] niv_affichage = { "elementaire", "procedure", "fichier_texte", "sql" };
	static String[] niv_saisie = { "elementaire", "procedure", "formulaire", "fichier_texte", "sql" };
	static int types_width=10;
	static int algo_width=10;
	static int donnees_width=15;
	static int groupe_width=20; 
	static int langList_count=3; 
	static int affichageList_count=3;
	static int groupementList_count=3;
	static int saisieList_count=3;
	static int calculList_count=3;
	
	PanelInteraction pInter;
	
	JTextField algoField;
	JTextField donneesField, resultatsField; //, autresField;
	JTextField reelsField, entiersField, textesField, booleensField;
	JTextField tab_reelsField, tab_entiersField, tab_textesField, tab_booleensField;
	JTextField mat_reelsField, mat_entiersField, mat_textesField, mat_booleensField;
	JTextField groupeField;
	JList langList, niv_affichageList, niv_groupementList, niv_saisieList, niv_calculList;

	private JButton creerButton;
	private JButton effacerButton;
	private JButton traduireButton, reformulerButton;
	private JButton compilerButton;
	
	public PanelPrincipal (PanelInteraction pInter) {
		this.setLayout( new BorderLayout() );
		this.pInter = pInter; pInter.pPrincipal = this;
		initEdition ();
	}

	private void initEdition() {
		Box vbox = Box.createVerticalBox();
		this.add(vbox);
		// panel algo
		JPanel pAlgo; pAlgo = new JPanel(); pAlgo.setBackground(null);
		pAlgo.add( new JLabel("algorithme") );
		algoField = new JTextField(algo_width);
		algoField.setText("exemple");
		pAlgo.add(algoField);
		pAlgo.add( new JLabel("langage") );
		langList = new JList(PanelInteraction.langList);
		langList.setSelectedIndex(0);
		langList.setVisibleRowCount(langList_count);
		pAlgo.add(new JScrollPane(langList));
		// panel entrees-sorties
		JPanel pInOut; pInOut = new JPanel(); pInOut.setBackground(null);
		pInOut.add( new JLabel("entrees") );
		donneesField = new JTextField(donnees_width);
		pInOut.add(donneesField);
		pInOut.add( new JLabel("sorties") );
		resultatsField = new JTextField(donnees_width);
		pInOut.add(resultatsField);
		// panel types élémentaires
		JPanel pTypes = new JPanel(new GridLayout(4,5));
		entiersField = new JTextField();
		reelsField = new JTextField();
		textesField = new JTextField();
		booleensField = new JTextField();
		tab_entiersField = new JTextField();
		tab_reelsField = new JTextField();
		tab_textesField = new JTextField();
		tab_booleensField = new JTextField();
		mat_entiersField = new JTextField();
		mat_reelsField = new JTextField();
		mat_textesField = new JTextField();
		mat_booleensField = new JTextField();
		pTypes.add( new JLabel("TYPES") );
		pTypes.add( new JLabel("Entiers") );
		pTypes.add( new JLabel("Reels") );
		pTypes.add( new JLabel("Textes") );
		pTypes.add( new JLabel("Booleens") );
		pTypes.add( new JLabel("Simples") );
		pTypes.add( entiersField );
		pTypes.add( reelsField );
		pTypes.add( textesField );
		pTypes.add( booleensField );
		pTypes.add( new JLabel("Tableaux") );
		pTypes.add( tab_entiersField );
		pTypes.add( tab_reelsField );
		pTypes.add( tab_textesField );
		pTypes.add( tab_booleensField );
		pTypes.add( new JLabel("Matrices") );
		pTypes.add( mat_entiersField );
		pTypes.add( mat_reelsField );
		pTypes.add( mat_textesField );
		pTypes.add( mat_booleensField );
		// Panel opérations
		JPanel pOper; pOper = new JPanel(); pOper.setBackground(null);
		pOper.add( new JLabel("saisie") );
		niv_saisieList = new JList(niv_saisie);
		niv_saisieList.addListSelectionListener(this);
		niv_saisieList.setSelectedIndex(0);
		niv_saisieList.setVisibleRowCount(saisieList_count);
		pOper.add(new JScrollPane(niv_saisieList));	 
		pOper.add( new JLabel("affichage") );
		niv_affichageList = new JList(niv_affichage);
		niv_affichageList.addListSelectionListener(this);
		niv_affichageList.setSelectedIndex(0);
		niv_affichageList.setVisibleRowCount(affichageList_count);
		pOper.add(new JScrollPane(niv_affichageList));	 
		pOper.add( new JLabel("calculs") );
		niv_calculList = new JList(niv_calcul);
		niv_calculList.addListSelectionListener(this);
		niv_calculList.setSelectedIndex(0);
		niv_calculList.setVisibleRowCount(calculList_count);
		pOper.add(new JScrollPane(niv_calculList));
		// panel groupes
		JPanel pGroup; pGroup = new JPanel(); pGroup.setBackground(null);
		pGroup.add( new JLabel("regroupement") );
		niv_groupementList = new JList(niv_groupement);
		niv_groupementList.addListSelectionListener(this);
		pGroup.add(new JScrollPane(niv_groupementList));
		niv_groupementList.setSelectedIndex(0);
		niv_groupementList.setVisibleRowCount(groupementList_count);
		pGroup.add( new JLabel(":") );
		groupeField = new JTextField(groupe_width);
		pGroup.add(groupeField);
		groupeField.setText("");
		// panel Boutons 
		Box hbox = Box.createHorizontalBox();
		creerButton = new JButton("Nouveau"); creerButton.addActionListener(this);
		creerButton.setActionCommand("nouveau");
		effacerButton = new JButton("Effacer");	effacerButton.addActionListener(this);
		effacerButton.setActionCommand("effacer");
		traduireButton = new JButton("Traduire"); traduireButton.addActionListener(this);
		traduireButton.setActionCommand("traduire");
		reformulerButton = new JButton("Reformuler"); reformulerButton.addActionListener(this);
		reformulerButton.setActionCommand("reformuler");
		compilerButton = new JButton("Compiler++"); compilerButton.addActionListener(this);
		compilerButton.setActionCommand("compiler");
        hbox.add(creerButton); hbox.add(Box.createHorizontalStrut(5));
        hbox.add(effacerButton); hbox.add(Box.createHorizontalStrut(5));
        hbox.add(traduireButton); hbox.add(Box.createHorizontalStrut(5));
        hbox.add(reformulerButton); hbox.add(Box.createHorizontalStrut(5));
        hbox.add(compilerButton); hbox.add(Box.createHorizontalStrut(5));
        // panel final
		vbox.add(Box.createGlue());
		vbox.add(pAlgo);
		vbox.add(Box.createGlue());
		vbox.add(pInOut); 
		vbox.add(Box.createGlue());
		vbox.add(pTypes); 
		vbox.add(Box.createGlue());
		vbox.add(pOper); 
		vbox.add(Box.createGlue());
		vbox.add(pGroup);
		vbox.add(hbox); 
		pTypes.setBackground(Color.pink);
		pTypes.setBackground(new Color(204,229,254));
		this.setBackground(Color.white);
	}
	
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		try {
			if (e.getSource() == this.creerButton || ("nouveau".equals(cmd))) {	
				this.nouveau();
			}
			else if (e.getSource() == this.effacerButton || ("effacer".equals(cmd))) {	
				this.effacer();	
			}
			else if (e.getSource() == this.traduireButton || ("traduire".equals(cmd))) {	
				this.traduire();	
			}
			else if (e.getSource() == this.reformulerButton || ("reformuler".equals(cmd))) {	
				this.reformuler();		
			}
			else if (e.getSource() == this.compilerButton || ("compiler".equals(cmd))) {	
				this.compilerPlus();	
				//org.javascool.gui.EditorWrapper.compilerPlus();
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	public void valueChanged(javax.swing.event.ListSelectionEvent e) {
		try {//langList, niv_affichageList, niv_groupementList, niv_saisieList, niv_calculList
			if (e.getSource()==langList) {
				if (langList.getSelectedIndex()>=0) 
					langList.ensureIndexIsVisible(langList.getSelectedIndex());
			}
			else if (e.getSource()==niv_affichageList) {
				if (niv_affichageList.getSelectedIndex()>=0) 
					niv_affichageList.ensureIndexIsVisible(niv_affichageList.getSelectedIndex());
			}
			else if (e.getSource()==niv_groupementList) {
				if (niv_groupementList.getSelectedIndex()>=0) 
					niv_groupementList.ensureIndexIsVisible(niv_groupementList.getSelectedIndex());
			}
			else if (e.getSource()==niv_saisieList) {
				if (niv_saisieList.getSelectedIndex()>=0) 
					niv_saisieList.ensureIndexIsVisible(niv_saisieList.getSelectedIndex());
			}
			else if (e.getSource()==niv_calculList) {
				if (niv_calculList.getSelectedIndex()>=0) 
					niv_calculList.ensureIndexIsVisible(niv_calculList.getSelectedIndex());
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	// ---------------------------------------------
	// Pour les autres Panels
	// ---------------------------------------------

	public String getNomAlgo() {
		return this.algoField.getText();
	}	

	public String getNomLangage() {
		return this.langList.getSelectedValue().toString();
	}			
	
	// ---------------------------------------------
	// Commandes
	// ---------------------------------------------
	
	private void nouveau() {
		Intermediaire inter = pInter.creerIntermediaire();
		org.javascool.proglets.plurialgo.langages.xml.Programme prog_xml;
		prog_xml = new org.javascool.proglets.plurialgo.langages.xml.Programme(new ProgrammeNouveau(inter));
		pInter.add_xml(prog_xml);
		pInter.traduireXml();
	}	
	
	private void effacer() {
		this.algoField.setText("exemple");
		this.donneesField.setText(""); 
		this.resultatsField.setText("");	
		//this.autresField.setText("");
		this.effacerTypes(); 
		this.niv_saisieList.setSelectedIndex(0);
		this.niv_affichageList.setSelectedIndex(0);
		this.niv_calculList.setSelectedIndex(0);
		this.niv_groupementList.setSelectedIndex(0);
		this.groupeField.setText("");
	}	
	
	private void effacerTypes() {
		this.entiersField.setText(""); 
		this.reelsField.setText(""); 
		this.textesField.setText("");
		this.booleensField.setText("");
		this.tab_entiersField.setText(""); 
		this.tab_reelsField.setText(""); 
		this.tab_textesField.setText("");
		this.tab_booleensField.setText("");
		this.mat_entiersField.setText(""); 
		this.mat_reelsField.setText(""); 
		this.mat_textesField.setText("");
		this.mat_booleensField.setText("");
	}
		
	private void traduire() {
		Intermediaire inter = null;
		iAnalyseur analyseur = null;
		pInter.clearConsole();
		// analyse du programme initial
		if (pInter.isVb()) {
			analyseur = new AnalyseurVb(pInter.getText(), false, false);
		}
		else if (pInter.isJavascool()) {
			analyseur = new AnalyseurJavascool(pInter.getText(), false, false);
		}
		else if (pInter.isAlgobox()) {
			analyseur = new AnalyseurAlgobox(pInter.getText(), false, false);
		}
		else if (pInter.isLarp()) {
			inter = pInter.creerIntermediaireLarp("traduire");
			analyseur = new AnalyseurLarp(pInter.getText(), false, false, inter);
		}
		else {
			pInter.clearConsole();
			pInter.writeConsole("---------- Avertissement ----------\n");
			pInter.writeConsole("le programme à traduire ne semble pas etre du javascool, du visual basic, du Larp ou de l'Algobox");
			return;
		}
		// ajout du resultat dans les onglets Complements et Resultats
		pInter.messageWarning(analyseur.getProgramme());
		pInter.pXml.setText(analyseur.getXml().toString());
		pInter.traduireXml();
	}
	
	private void reformuler() {
		Intermediaire inter = null;
		iAnalyseur analyseur = null;
		pInter.clearConsole();
		// analyse du programme initial
		if (pInter.isVb()) {
			analyseur = new AnalyseurVb(pInter.getText(), true, true);
		}
		else if (pInter.isJavascool()) {
			analyseur = new AnalyseurJavascool(pInter.getText(), true, true);
		}
		else if (pInter.isAlgobox()) {
			analyseur = new AnalyseurAlgobox(pInter.getText(), true, true);
		}
		else if (pInter.isLarp()) {
			inter = pInter.creerIntermediaireLarp("reformuler");
			analyseur = new AnalyseurLarp(pInter.getText(), true, true, inter);
		}
		else {
			pInter.clearConsole();
			pInter.writeConsole("---------- Avertissement ----------\n");
			pInter.writeConsole("le programme à traduire ne semble pas etre du javascool, du visual basic, du Larp ou de l'Algobox");
		}
		// construction du programme dérivé
		pInter.messageWarning(analyseur.getProgramme());
		inter = pInter.creerIntermediaire();
		ProgrammeDerive progDer = new ProgrammeDerive(analyseur.getProgramme(), inter);
		// ajout du resultat dans les onglets Complements et Resultats
		pInter.add_xml(new org.javascool.proglets.plurialgo.langages.xml.Programme(progDer));
		pInter.pPrincipal.algoField.setText(progDer.nom);
		pInter.traduireXml();
	}	
	
	private void compilerPlus() {
		Console.getInstance().clear();
		String contenu_courant = org.javascool.gui.EditorWrapper.getText();
		RTextArea area = org.javascool.gui.EditorWrapper.getRTextArea();
		if (!contenu_courant.contains(" main(")) {
			area.append("\n");
			area.append("void main() {");
			area.append("println(\"Pensez au 'void main()' !\");");
			area.append("}");
			Desktop.getInstance().saveCurrentFile();
			Desktop.getInstance().compileFile();
			org.javascool.gui.EditorWrapper.setText(contenu_courant);
			Desktop.getInstance().saveCurrentFile();
		}
		else if (contenu_courant.contains("//@import")) {
			int rep = javax.swing.JOptionPane.showConfirmDialog(null, "Actualiser les importations ?");
			if (rep==javax.swing.JOptionPane.NO_OPTION) {
				Desktop.getInstance().saveCurrentFile();
				Desktop.getInstance().compileFile();
			}
			else if (rep==javax.swing.JOptionPane.YES_OPTION) {
				org.javascool.gui.EditorWrapper.setText(contenu_courant.substring(0, contenu_courant.indexOf("//@import")));
				Map<String,String> others = org.javascool.gui.EditorWrapper.getOthers();
				Iterator<String> iter = others.keySet().iterator();
				while (iter.hasNext()) {
					String nom_fich = iter.next();
					String contenu = others.get(nom_fich);
					if (contenu.contains(" main(")) continue;
					area.append("\n");
					area.append("//@import : " + nom_fich + "\n");
					area.append(contenu);
					area.append("\n");
				}
				Desktop.getInstance().saveCurrentFile();
				Desktop.getInstance().compileFile();
			}
		}
		else {
			Map<String,String> others = org.javascool.gui.EditorWrapper.getOthers();
			Iterator<String> iter = others.keySet().iterator();
			while (iter.hasNext()) {
				String nom_fich = iter.next();
				String contenu = others.get(nom_fich);
				if (contenu.contains(" main(")) continue;
				area.append("\n");
				area.append("//@import : " + nom_fich + "\n");
				area.append(contenu);
				area.append("\n");
			}
			Desktop.getInstance().saveCurrentFile();
			Desktop.getInstance().compileFile();
		}
	}
	
}