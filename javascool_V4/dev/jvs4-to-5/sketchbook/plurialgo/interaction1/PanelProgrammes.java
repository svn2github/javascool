/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2013.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.interaction1;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.*;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.javascool.proglets.plurialgo.divers.Divers;
import org.javascool.proglets.plurialgo.langages.xml.AnalyseurAlgobox;
import org.javascool.proglets.plurialgo.langages.xml.AnalyseurJavascool;
import org.javascool.proglets.plurialgo.langages.xml.AnalyseurLarp;
import org.javascool.proglets.plurialgo.langages.xml.AnalyseurVb;
import org.javascool.proglets.plurialgo.langages.xml.Argument;
import org.javascool.proglets.plurialgo.langages.xml.Intermediaire;
import org.javascool.proglets.plurialgo.langages.xml.ProgrammeDerive;
import org.javascool.proglets.plurialgo.langages.xml.ProgrammeVectorise;
import org.javascool.proglets.plurialgo.langages.xml.iAnalyseur;



/**
 * Cette classe correspond à l'onglet Résultats de l'interface graphique.
 * 
 * <p>
 * L'éditeur syntaxique est réalisé à partir de la librairie
 * <a href="http://fifesoft.com/rsyntaxtextarea/" target="_blank">rsyntaxtextarea</a>.
 * </p>
*/
@SuppressWarnings("unchecked")	// car les JList doivent être paramétrées avec Java7
public class PanelProgrammes extends JPanel implements ActionListener, ListSelectionListener {
	private static final long serialVersionUID = 1L;

	private PanelInteraction pInter;	
	
	private JPanel pVect;	
	private JList boucleList;
	private JTextField pourVarField, pourFinField, vectModeField;
	private JTextField sommeModeField; private JCheckBox sommeCheck;
	private JTextField compterModeField; private JCheckBox compterCheck;
	private JTextField miniModeField; private JCheckBox miniCheck;
	private JTextField maxiModeField; private JCheckBox maxiCheck;
	private JTextField chercherModeField; private JCheckBox chercherCheck;
	private JButton vectoriserButton;
	
	private JPanel pEdit;
	private RSyntaxTextArea editArea;	
	private JList fichList;
	private Map<String,StringBuffer> les_fichiers;
	private String le_fichier; 
	private JPopupMenu popup;	
	
	private JSplitPane splitPane;

	public PanelProgrammes (PanelInteraction pInter) {
		super(new BorderLayout());
		this.pInter = pInter;
		initEdition();
		initPopupMenus();
		initVect();
		splitPane= new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, pVect, pEdit);
		splitPane.setOneTouchExpandable(true);
		this.add(splitPane);
        setVisible(true);
	}
	
	private void initEdition() {
		// editeur de texte
        editArea = new RSyntaxTextArea(20,20);
		editArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_NONE);
		editArea.setCodeFoldingEnabled(true);
		editArea.setAntiAliasingEnabled(true);
		RTextScrollPane paneScrollPane = new RTextScrollPane(editArea);
        paneScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		editArea.setVisible(true);
		// selecteur de fichiers
		les_fichiers = new TreeMap<String,StringBuffer>();
		le_fichier = null;
		fichList=new JList();
		fichList.addListSelectionListener(this);
		String [] choix ={"exemple.jvs"};
		fichList.setListData(choix); 
		les_fichiers.put(choix[0], new StringBuffer());
		le_fichier = choix[0];
		fichList.setVisible(false);
		// positionnement des composants
		pEdit = new JPanel(new BorderLayout());
		pEdit.add(paneScrollPane,"Center");
		pEdit.add(fichList,"East");
		pEdit.setVisible(true);
	}
	
	private void initPopupMenus() {
		popup = editArea.getPopupMenu();
	    popup.addSeparator();
		JMenuItem menu;
		menu = new JMenuItem("Si"); menu.addActionListener(this); menu.setActionCommand("si");
		popup.add(menu);
		menu = new JMenuItem("Pour"); menu.addActionListener(this); menu.setActionCommand("pour");
		popup.add(menu);
		menu = new JMenuItem("Tantque"); menu.addActionListener(this); menu.setActionCommand("tantque");
		popup.add(menu);
	}
	
	private void initVect() {
		JPanel p;
		pVect = new JPanel();
		pVect.setLayout( new FlowLayout() );
		this.add(pVect,"West");
		Box vbox = Box.createVerticalBox();
		pVect.add(vbox); 
		vbox.add(Box.createVerticalStrut(20));
		// titre
		p = new JPanel(); 
		p.add( new JLabel("Transformation 1-n") );
		vbox.add(p);
		vbox.add(Box.createVerticalStrut(10)); 
		// pour
		p = new JPanel(); 
		boucleList = new JList();
		String [] choix_boucle ={"pour","tantque"};
		boucleList.setListData(choix_boucle); 
		boucleList.setSelectedIndex(0);
		p.add(boucleList);
		p.add( new JLabel(" : ") );
		pourVarField = new JTextField(4);
		pourVarField.setText("k");
		p.add(pourVarField);
		p.add( new JLabel(" de 1 à ") );
		pourFinField = new JTextField(4);
		pourFinField.setText("n");
		p.add(pourFinField);
		vbox.add(p);
		// sommation
		p = new JPanel(); 
		sommeCheck = new JCheckBox(); p.add(sommeCheck);
		p.add( new JLabel("sommation : ") );
		sommeModeField = new JTextField(10);
		sommeModeField.setText("somme:increment");
		p.add(sommeModeField);
		vbox.add(p);
		// comptage
		p = new JPanel(); 
		compterCheck = new JCheckBox();	p.add(compterCheck);
		p.add( new JLabel("comptage : ") );
		compterModeField = new JTextField(10);
		compterModeField.setText("effectif:condition");
		p.add(compterModeField);
		vbox.add(p);
		// minimum
		p = new JPanel(); 
		miniCheck = new JCheckBox(); p.add(miniCheck);
		p.add( new JLabel("minimum : ") );
		miniModeField = new JTextField(10);
		miniModeField.setText("mini:expression");
		p.add(miniModeField);
		vbox.add(p);
		// maximum
		p = new JPanel(); 
		maxiCheck = new JCheckBox(); p.add(maxiCheck);
		p.add( new JLabel("maximum : ") );
		maxiModeField = new JTextField(10);
		maxiModeField.setText("maxi:expression");
		p.add(maxiModeField);
		vbox.add(p);
		// chercher
		p = new JPanel(); 
		chercherCheck = new JCheckBox(); p.add(chercherCheck);
		p.add( new JLabel("chercher (un) : ") );
		chercherModeField = new JTextField(10);
		chercherModeField.setText("condition");
		p.add(chercherModeField);
		vbox.add(p);
		// bouton
		p = new JPanel(); 
		vectoriserButton = new JButton("Transformer (1-n)"); p.add(vectoriserButton);
		vectoriserButton.addActionListener(this);
		vectoriserButton.setActionCommand("vectoriser");
		vectoriserButton.setVisible(true);
		vbox.add(p);
		// ajout de dimension
		p = new JPanel(); 
		p.add( new JLabel("vectorisation : ") );
		vectModeField = new JTextField(10);
		vectModeField.setText("");
		p.add(vectModeField);
		vbox.add(p);
	}
		
	public void actionPerformed(ActionEvent e) {
        try {
			String cmd = e.getActionCommand();
			if (e.getSource() == this.vectoriserButton || ("vectoriser".equals(cmd))) {	
				this.vectoriser();	
			}
			else if ("traduire".equals(cmd)) {	
				this.traduire();	
			}
			else if ("reformuler".equals(cmd)) {	
				this.reformuler();	
			}
			// menu Instructions
			else if ("si".equals(cmd)) {
				this.addSi();	
			}
			else if ("pour".equals(cmd)) {
				this.addPour();	
			}
			else if ("tantque".equals(cmd)) {
				this.addTantQue();	
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	public void valueChanged(javax.swing.event.ListSelectionEvent e) {
		try {
			if (e.getSource()==this.fichList) {
				this.updateEditeur();
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	// ---------------------------------------------
	// Pour les autres Panels
	// ---------------------------------------------
	
	public String getText() {
		//return org.javascool.gui.EditorWrapper.getText();
		return this.editArea.getText();
	}	
	
	public void setText(String txt) {
		//org.javascool.gui.EditorWrapper.setText(txt);
		this.editArea.setText(txt);
	}	
	
	public void add_editeur(org.javascool.proglets.plurialgo.langages.modele.Programme prog) {
		les_fichiers.clear(); le_fichier=null;	 
		les_fichiers.putAll(prog.les_fichiers);
		fichList.setListData(prog.les_fichiers.keySet().toArray());
		fichList.setSelectedIndex(0); 
		if (prog.les_fichiers.size()>1) {
			fichList.setVisible(true);
		}
		else {
			fichList.setVisible(false);
		}
	}
	
	// ---------------------------------------------
	// Mise à jour éditeur
	// ---------------------------------------------
	
	private void updateEditeur(){
		if (this.fichList.getSelectedValue()!=null) {
			if (le_fichier!=null) {
				les_fichiers.put(le_fichier, new StringBuffer(editArea.getText()));
			}
			le_fichier = this.fichList.getSelectedValue().toString();
			colorier(le_fichier);
			pInter.pEdition.setText(les_fichiers.get(le_fichier).toString());
		}
	}
	
    public void colorier(String le_fichier) {
    	if (le_fichier.endsWith(".html")) {
    		//editArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);
    		editArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
    	}
    	else if (le_fichier.endsWith(".larp") || le_fichier.endsWith(".txt") || le_fichier.equals("larp")) {
    		//editArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_NONE);
    		editArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_DELPHI);
    	}
    	else if (le_fichier.endsWith(".bas") || le_fichier.equals("vb")) {
    		//editArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_DELPHI);
    		editArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
    	}
    	else if (le_fichier.endsWith(".jvs") || le_fichier.equals("javascool")) {
    		//editArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_SCALA);
    		editArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
    	}
    	else if (le_fichier.endsWith(".scala") || le_fichier.equals("scala")) {
    		//editArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_SCALA);
    		editArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
    	}
    	else if (le_fichier.endsWith(".java")) {
    		editArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
    	}
    	else if (le_fichier.endsWith(".cpp")) {
    		editArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_CPLUSPLUS);
    	}
    	else if (le_fichier.endsWith(".adb") || le_fichier.endsWith(".ads")) {
    		editArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_DELPHI);
    	}
    	else if (le_fichier.endsWith(".pm") || le_fichier.endsWith(".pl")) {
    		editArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_PERL);
    	}
    	else if (le_fichier.endsWith(".scm")) {
    		editArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_LISP);
    	}
    	else if (le_fichier.endsWith(".py")) {
    		editArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_PYTHON);
    	}
    	else if (le_fichier.endsWith(".R")) {
    		editArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
    	}
    	else if (le_fichier.endsWith(".php")) {
    		//editArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_PHP);
    		editArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
    	}
    	else if (le_fichier.endsWith(".xml")) {
    		editArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_XML);
    	}
    	else {
    		editArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_NONE);
    	}
    }
	
	// ---------------------------------------------
	// utilitaires pour vectorisation, reformulation et traduction
	// ---------------------------------------------
	
	private boolean isJavascool() {
		String txt=getText();
		if (txt.contains("void ") && txt.contains(" main()")) return true;
		if (txt.contains("void ") && txt.contains(" main( )")) return true;
		return false;
	}	
	
	private boolean isVb() {
		String txt=getText().toLowerCase();
		if (txt.contains("sub ")) return true;
		if (txt.trim().length()==0) return true;
		return false;
	}	
	
	private boolean isLarp() {
		String txt=getText().toLowerCase();
		if (isAlgobox()) return false;
		if ((txt.contains("debut")||txt.contains("début"))&&txt.contains("fin")) return true;
		if (txt.contains("entrer")&&txt.contains("retourner")) return true;
		return false;
	}	
	
	private boolean isAlgobox() {
		String txt=getText().toLowerCase();
		if (txt.contains("debut_algorithme")&&txt.contains("fin_algorithme")) return true;
		return false;
	}	
		
	// ---------------------------------------------
	// Vectorisation
	// ---------------------------------------------
	
	public void vectoriser() {
		Intermediaire inter = null;
		iAnalyseur analyseur = null;
		pInter.clearConsole();
		// analyse du programme initial
		if (isVb()) {
			analyseur = new AnalyseurVb(editArea.getText(), false, false);
		}
		else if (isJavascool()) {
			analyseur = new AnalyseurJavascool(editArea.getText(), false, false);
		}
		else if (isLarp()) {
			inter = pInter.creerIntermediaireLarp("vectoriser");
			analyseur = new AnalyseurLarp(editArea.getText(), false, false, inter);
		}
		else {
			analyseur = new AnalyseurVb("", false, false);
		}
		// vectorisation
		pInter.messageWarning(analyseur.getProgramme());
		org.javascool.proglets.plurialgo.langages.xml.Programme prog = analyseur.getProgramme();
		vectoriser(prog);
		ProgrammeVectorise progVect = new ProgrammeVectorise(prog);
		// ajout du resultat dans les onglets Complements et Resultats
		pInter.add_xml(new org.javascool.proglets.plurialgo.langages.xml.Programme(progVect));
		pInter.traduireXml();
	}
	
	private void vectoriser(org.javascool.proglets.plurialgo.langages.xml.Programme prog) {
		prog.options.add( new Argument("vectorisation", null, vectModeField.getText()) );
		prog.options.add( new Argument("pour_var", null, pourVarField.getText()) );
		prog.options.add( new Argument("pour_fin", null, pourFinField.getText()) );
		if ("tantque".equals(boucleList.getSelectedValue())) {
			prog.options.add( new Argument("tantque", null, "tantque") );
		}
		if (sommeCheck.isSelected()) {
			prog.options.add( new Argument("sommation", null, sommeModeField.getText()) );
		}
		if (compterCheck.isSelected()) {
			prog.options.add( new Argument("comptage", null, compterModeField.getText()) );
		}
		if (miniCheck.isSelected()) {
			prog.options.add( new Argument("minimum", null, miniModeField.getText()) );
		}
		if (maxiCheck.isSelected()) {
			prog.options.add( new Argument("maximum", null, maxiModeField.getText()) );
		}
		if (chercherCheck.isSelected()) {
			prog.options.add( new Argument("recherche", null, chercherModeField.getText()) );
		}
	}	

	// ---------------------------------------------
	// Traduction
	// ---------------------------------------------
	
	public void traduire() {
		Intermediaire inter = null;
		iAnalyseur analyseur = null;
		pInter.clearConsole();
		// analyse du programme initial
		if (isVb()) {
			analyseur = new AnalyseurVb(editArea.getText(), false, false);
		}
		else if (isJavascool()) {
			analyseur = new AnalyseurJavascool(editArea.getText(), false, false);
		}
		else if (isAlgobox()) {
			analyseur = new AnalyseurAlgobox(editArea.getText(), false, false);
		}
		else if (isLarp()) {
			inter = pInter.creerIntermediaireLarp("traduire");
			analyseur = new AnalyseurLarp(editArea.getText(), false, false, inter);
		}
		else {
			pInter.clearConsole();
			pInter.writeConsole("---------- Avertissement ----------\n");
			pInter.writeConsole("le programme à traduire ne semble pas etre du javascool, du visual basic, du Larp ou de l'algobox");
			return;
		}
		// ajout du resultat dans les onglets Complements et Resultats
		pInter.messageWarning(analyseur.getProgramme());
		pInter.pXml.setText(analyseur.getXml().toString());
		pInter.traduireXml();
	}			
		
	// ---------------------------------------------
	// Reformulation
	// ---------------------------------------------
	
	public void reformuler() {
		Intermediaire inter = null;
		iAnalyseur analyseur = null;
		pInter.clearConsole();
		// analyse du programme initial
		if (isVb()) {
			analyseur = new AnalyseurVb(editArea.getText(), true, true);
		}
		else if (isJavascool()) {
			analyseur = new AnalyseurJavascool(editArea.getText(), true, true);
		}
		else if (isLarp()) {
			inter = pInter.creerIntermediaireLarp("reformuler");
			analyseur = new AnalyseurLarp(editArea.getText(), true, true, inter);
		}
		else {
			pInter.clearConsole();
			pInter.writeConsole("---------- Avertissement ----------\n");
			pInter.writeConsole("le programme à reformuler ne semble pas etre du javascool ou du visual basic");
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
		
	// ---------------------------------------------
	// Commandes algorithmiques : Si, Pour, Tantque
	// ---------------------------------------------
			
		private String tabuler(int indent) {
			String txt_indent = "\n";
			for(int i=0; i<indent; i++) {
			txt_indent = txt_indent + "\t";
			}
			return txt_indent;
		}    
		
		private void insertText(String txt) {
	    	int start = editArea.getSelectionStart();
	    	editArea.setSelectionEnd(start);
	    	editArea.replaceSelection(txt);
		}  

		private void addSi() {
			int indent = Divers.getIndent(this.editArea);
			String tabus = tabuler(indent);
			String txt = ""; 
			if (isVb()) {
				txt += tabus + "if (condition) then";
				txt += tabus + "\tinstructions";
				txt += tabus + "elseif (condition) Then";
				txt += tabus + "\tinstructions";
				txt += tabus + "else";
				txt += tabus + "\tinstructions";
				txt += tabus + "end if";
				this.insertText(txt);
			}
			else if (isJavascool()) {
				txt += tabus + "if (condition) {";
				txt += tabus + "\tinstructions";
				txt += tabus + "}";
				txt += tabus + "elseif (condition) {";
				txt += tabus + "\tinstructions";
				txt += tabus + "}";
				txt += tabus + "else {";
				txt += tabus + "\tinstructions";
				txt += tabus + "}";
				this.insertText(txt);
			}
			else if (isLarp()) {
				txt += tabus + "SI (condition) ALORS";
				txt += tabus + "\tinstructions";
				txt += tabus + "SINON SI (condition) ALORS";
				txt += tabus + "\tinstructions";
				txt += tabus + "SINON";
				txt += tabus + "\tinstructions";
				txt += tabus + "FINSI";
				this.insertText(txt);
			}
		}
		
		private void addPour() {
			int indent = Divers.getIndent(this.editArea);
			String tabus = tabuler(indent); 
			String txt = ""; 
			if (isVb()) {
				txt += tabus + "for variable=debut to fin";
				txt += tabus + "\tinstructions";
				txt += tabus + "next variable";
				this.insertText(txt);
			}
			else if (isJavascool()) {
				txt += tabus + "while (condition) {";
				txt += tabus + "\tinstructions";
				txt += tabus + "}";
				this.insertText(txt); 
			}
			else if (isLarp()) {
				txt += tabus + "POUR variable=debut JUSQU'A fin FAIRE";
				txt += tabus + "\tinstructions";
				txt += tabus + "FINPOUR";
				this.insertText(txt);
			}
		}
		
		private void addTantQue() {
			int indent = Divers.getIndent(this.editArea);
			String tabus = tabuler(indent);
			String txt = ""; 
			if (isVb()) {
				txt += tabus + "while (condition)";
				txt += tabus + "\tinstructions";
				txt += tabus + "wend";
				this.insertText(txt); 
			}
			else if (isJavascool()) {
				txt += tabus + "while (condition) {";
				txt += tabus + "\tinstructions";
				txt += tabus + "}";
				this.insertText(txt); 
			}
			else if (isLarp()) {
				txt += tabus + "TANTQUE (condition) ";
				txt += tabus + "\tinstructions";
				txt += tabus + "FINTANTQUE";
				this.insertText(txt); 
			}
		}			
	
}
