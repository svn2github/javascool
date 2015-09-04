/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2013.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.interaction2;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Color;
import java.awt.event.*;
import java.util.Iterator;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;

import org.javascool.proglets.plurialgo.divers.Divers;
import org.javascool.proglets.plurialgo.langages.modele.*;
import org.javascool.proglets.plurialgo.langages.xml.*;


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
	static int langList_count=5; 
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
	private JList langList;
	JList niv_affichageList, niv_groupementList, niv_saisieList, niv_calculList;

	private JButton creerButton;
	private JButton effacerButton;
	private JButton traduireButton, reformulerButton;
	private JButton insererButton;
	
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
		langList = new JList(pInter.getLangagesNoms());
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
		insererButton = new JButton("Inserer"); insererButton.addActionListener(this);
		insererButton.setActionCommand("inserer"); insererButton.setVisible(true);
        hbox.add(creerButton); hbox.add(Box.createHorizontalStrut(5));
        hbox.add(insererButton); hbox.add(Box.createHorizontalStrut(5));
        hbox.add(effacerButton); hbox.add(Box.createHorizontalStrut(5));
        hbox.add(traduireButton); hbox.add(Box.createHorizontalStrut(5));
        hbox.add(reformulerButton); hbox.add(Box.createHorizontalStrut(5));
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
				if (this.reformulerSelection()) return;
				this.reformuler();		
			}
			else if (e.getSource() == this.insererButton || ("inserer".equals(cmd))) {
				this.inserer();
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
		return pInter.getLangagePackage(langList.getSelectedValue().toString());
	}	

	// ---------------------------------------------
	// Commandes
	// ---------------------------------------------
	
	private void nouveau() {
		Intermediaire inter = pInter.creerIntermediaire();
		org.javascool.proglets.plurialgo.langages.xml.XmlProgramme prog_xml;
		prog_xml = new org.javascool.proglets.plurialgo.langages.xml.XmlProgramme(new ProgrammeNouveau(inter));
		if (inter.avecSaisieFormulaire()) zoneListe(prog_xml);
		renommerOperations(prog_xml);
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
		else if (pInter.isPython()) {
			inter = pInter.creerIntermediaireLarp("traduire");
			analyseur = new AnalyseurPython(pInter.getText(), false, false, inter);
		}
		else if (pInter.isXcas()) {
			inter = pInter.creerIntermediaireLarp("traduire");
			analyseur = new AnalyseurXcas(pInter.getText(), false, false, inter);
		}
		else if (pInter.isCarmetal()) {
			inter = pInter.creerIntermediaireLarp("traduire");
			analyseur = new AnalyseurCarmetal(pInter.getText(), false, false, inter);
		}
		else {
			pInter.clearConsole();
			pInter.writeConsole("---------- Traduction impossible ----------\n");
			//pInter.writeConsole("l'algorithme initial ne semble pas etre du Javascool, du Larp, du Python, de l'Algobox, du Xcas ou du Visual Basic\n");
			return;
		}
		// ajout du resultat dans l' onglet Complements et dans l'editeur
		pInter.messageWarning(analyseur.getProgramme());
		pInter.setXml(analyseur.getXml().toString());
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
		else if (pInter.isPython()) {
			inter = pInter.creerIntermediaireLarp("reformuler");
			analyseur = new AnalyseurPython(pInter.getText(), true, true, inter);
		}
		else if (pInter.isXcas()) {
			inter = pInter.creerIntermediaireLarp("reformuler");
			analyseur = new AnalyseurXcas(pInter.getText(), true, true, inter);
		}
		else if (pInter.isCarmetal()) {
			inter = pInter.creerIntermediaireLarp("reformuler");
			analyseur = new AnalyseurCarmetal(pInter.getText(), true, true, inter);
		}
		else {
			pInter.clearConsole();
			pInter.writeConsole("---------- Reformulation impossible ----------\n");
			return;			
		}
		// construction du programme dérivé
		pInter.messageWarning(analyseur.getProgramme());
		inter = pInter.creerIntermediaire();
		ProgrammeDerive progDer = new ProgrammeDerive(analyseur.getProgramme(), inter);
		// ajout du resultat dans l' onglet Complements et dans l'editeur
		pInter.pPrincipal.algoField.setText(progDer.nom);
		org.javascool.proglets.plurialgo.langages.xml.XmlProgramme prog_xml;
		prog_xml = new org.javascool.proglets.plurialgo.langages.xml.XmlProgramme(progDer);
		if (inter.avecSaisieFormulaire()) zoneListe(prog_xml);
		if (analyseur.getProgramme().operations.size()==0) renommerOperations(prog_xml);
		pInter.add_xml(prog_xml);
		pInter.traduireXml();
	}	
	
	boolean reformulerSelection() {
		pInter.clearConsole();
		// recherche zone de sélection
		JTextArea editArea = pInter.getTextArea();
		int i_start = editArea.getSelectionStart();
		int i_end = editArea.getSelectionEnd();
		int indent = 0;
		if (i_end - i_start<5) return false;	// trop petit (donc sélection involontaire ?)
		String lang = pInter.pPrincipal.getNomLangage();
		if (lang.equals("algobox") ) return false;	
		String txt_select = "";
		try {
			int lig_start = editArea.getLineOfOffset(i_start);
			i_start = editArea.getLineStartOffset(lig_start);
			int lig_end = editArea.getLineOfOffset(i_end);
			i_end = editArea.getLineEndOffset(lig_end) - 1;
			txt_select = editArea.getText(i_start, i_end-i_start);
			while (txt_select.substring(indent, indent+1).equals("\t")) {
				indent = indent+1;
			}
			// normalisation avec indentation 1 pour inserer dans les ss-programmes
			if (indent==0) {
				txt_select = Divers.remplacer(txt_select, "\n", "\n\t");
				txt_select = "\t" + txt_select;
			}
			else if (indent>1) {
				for(int i=2; i<=indent; i=i+1) {
					txt_select = Divers.remplacer(txt_select, "\n\t", "\n");
				}
				txt_select = txt_select.substring(indent-1);
			}
		}
		catch(Exception ex) {
			return false;
		}
		// verification conditions d'application
		Intermediaire inter = pInter.creerIntermediaire();
		if (!inter.avecFonctionsCalcul() && !inter.avecProcedureCalcul()) return false;
		// creation sous-programme
		inter.niv_saisie = "";
		inter.niv_affichage = "";
		inter.niv_groupement = "elementaire";
		org.javascool.proglets.plurialgo.langages.xml.XmlProgramme prog_xml;
		prog_xml = new org.javascool.proglets.plurialgo.langages.xml.XmlProgramme(new ProgrammeNouveau(inter));
		prog_xml.nom = pInter.pPrincipal.getNomAlgo();
		System.out.println("operations:"+prog_xml.operations.size());
		if (prog_xml.operations.size()==0) return false;
		for (Iterator<ModeleOperation> iter=prog_xml.operations.iterator(); iter.hasNext();) {
			ModeleOperation oper = iter.next();
			if (oper.instructions.size()==1) {
				if (oper.instructions.get(0).isCommentaire()) {
					oper.instructions.remove(0);
				}
			}
			oper.instructions.add(0, new ModeleInstruction("//reformulationOperation"));
		}
		renommerOperations(prog_xml);
		pInter.add_xml(prog_xml);
		// conversion du programme dans le langage courant
		String txt = pInter.getXml();
		ModeleProgramme prog = ModeleProgramme.getProgramme(txt,lang); 
		// texte des sous-programmes
		StringBuffer buf_oper = new StringBuffer();
		for (Iterator<ModeleOperation> iter=prog.operations.iterator(); iter.hasNext();) {
			ModeleOperation oper = iter.next();
			oper.ecrire(prog, buf_oper, 0);
		}
		int i_reformu = buf_oper.indexOf("reformulationOperation");
		int i_tab = buf_oper.substring(0, i_reformu).lastIndexOf("\t");
		buf_oper.delete(i_tab, i_reformu + "reformulationOperation".length());
		buf_oper.insert(i_tab, txt_select);
		StringBuffer buf_appel = new StringBuffer();
		for (Iterator<ModeleInstruction> iter=prog.instructions.iterator(); iter.hasNext();) {
			ModeleInstruction instr = iter.next();
			instr.ecrire(prog, buf_appel, indent);
		}
		// on modifie l'editeur
		buf_appel.delete(0, 1);
		editArea.replaceRange(buf_appel.toString(), i_start, i_end);
		editArea.append(buf_oper.toString());
		return true;
	}
	
	private void inserer() {
		Intermediaire inter = pInter.creerIntermediaire();
		org.javascool.proglets.plurialgo.langages.xml.XmlProgramme prog_xml;
		prog_xml = new org.javascool.proglets.plurialgo.langages.xml.XmlProgramme(new ProgrammeNouveau(inter));
		pInter.add_xml(prog_xml);
		// conversion du programme dans le langage courant
		String lang = pInter.pPrincipal.getNomLangage();
		String txt = pInter.getXml();
		ModeleProgramme prog = ModeleProgramme.getProgramme(txt,lang); 
		// ajout des sous-programmes (ou des variables ou des entrées-sorties)
		StringBuffer buf = new StringBuffer();
		int indent = Divers.getIndent(pInter.getTextArea());
		if (prog.operations.size()>0) {	// ajout de sous-programmes
			for (Iterator<ModeleOperation> iter=prog.operations.iterator(); iter.hasNext();) {
				ModeleOperation oper = iter.next();
				oper.ecrire(prog, buf, indent);
			}					
		}
		if (prog.instructions.size()>0 && buf.length()==0 ) { // ajout de saisies ou d'affichages 
			for (Iterator<ModeleInstruction> iter=prog.instructions.iterator(); iter.hasNext();) {
				ModeleInstruction instr = iter.next();
				if (instr.isCommentaire()) continue;
				if (instr.isEcriture()) {
					boolean isExpression = false;
					String expression = "";
					for (Iterator<ModeleArgument> iter_arg=instr.arguments.iterator(); iter_arg.hasNext();) {
						ModeleArgument arg = iter_arg.next();
						if (!Divers.isIdent(arg.nom))  isExpression = true;
						expression = expression + " " + arg.nom;
					}
					if (isExpression) {
						ModeleArgument arg_expr = null;
						for (Iterator<ModeleArgument> iter_arg=instr.arguments.iterator(); iter_arg.hasNext();) {
							ModeleArgument arg = iter_arg.next();
							arg.nom = expression.trim();
							arg.type = "EXPR";
							arg_expr = arg;
						}
						instr.arguments.clear(); instr.arguments.add(arg_expr);
					}
				}
				instr.ecrire(prog, buf, indent);
				prog.postTraitement(buf);
			}		
		}
		if (prog.variables.size()>0 && buf.length()==0) {	// ajout de variables
			for (Iterator<ModeleVariable> iter=prog.variables.iterator(); iter.hasNext();) {
				ModeleVariable var = iter.next();
				var.ecrire(prog, buf, indent);
			}								
		}
		if (buf.length()>0 ) {
			Divers.inserer(pInter.getTextArea(), buf.toString());
		}	
	}	
	
	private void zoneListe(org.javascool.proglets.plurialgo.langages.xml.XmlProgramme prog_xml) {
		ModeleInstruction instr = null;
		boolean trouveFormulaire = false;
		for (Iterator<ModeleInstruction> iter=prog_xml.instructions.iterator(); iter.hasNext();) {
			instr = iter.next();
			if (instr.isLectureFormulaire()) {
				trouveFormulaire = true;
				break;
			}
		}		
		if (!trouveFormulaire) return;
		for (Iterator<ModeleArgument> iter=instr.arguments.iterator(); iter.hasNext();) {
			ModeleArgument arg = iter.next();
			if (arg.isTexte()) {
				String message = "pour la variable " + arg.nom + " : ";
				message = message + "\n ---- zone de texte : cliquez sur Annuler";
				message = message + "\n ---- zone de liste : entrez les choix (separes par des espaces), puis validez";
				message = message + "\n ---- boutons d'option : entrez les choix (separes par des virgules), puis validez";
				String reponse = JOptionPane.showInputDialog(message);
				if (reponse==null) continue;
				reponse = reponse.trim();
				if (reponse.isEmpty()) continue;
				if (reponse.contains(",")) {
					reponse = Divers.remplacer(reponse, ",", " ");
					reponse = reponse + " RADIO";
				}
				arg.type = arg.type + ":" + reponse;
			}
		}								
	}	
	
	private void renommerOperations(org.javascool.proglets.plurialgo.langages.xml.XmlProgramme prog_xml) {
		for (Iterator<ModeleOperation> iter=prog_xml.operations.iterator(); iter.hasNext();) {
			ModeleOperation oper = iter.next();
			String message = "changez eventuellement le nom du sous-programme : ";
			String reponse = JOptionPane.showInputDialog(message, oper.nom);
			if (reponse==null) continue;
			reponse = reponse.trim();
			if (reponse.isEmpty()) continue;
			if (reponse.equals(oper.nom)) continue;
			for (Iterator<ModeleInstruction> iter_instr=prog_xml.instructions.iterator(); iter_instr.hasNext();) {
				ModeleInstruction instr = iter_instr.next();
				if(oper.nom.equals(instr.nom)) {
					instr.nom = reponse;
					continue;
				}
			}
			if (oper.nom.equals("lire")) continue;
			if (oper.nom.equals("ecrire")) continue;
			oper.nom=reponse;
		}
	}

}
