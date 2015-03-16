/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2013.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.interaction2;

import java.awt.BorderLayout;
import java.awt.event.*;
import javax.swing.*;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
//import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.javascool.proglets.plurialgo.langages.modele.Programme;
import org.javascool.proglets.plurialgo.langages.xml.Intermediaire;
import org.javascool.proglets.plurialgo.langages.xml.ProgrammeDerive;
import org.javascool.proglets.plurialgo.langages.xml.ProgrammeVectorise;



/**
 * Cette classe permet de visualiser le code Xml des programmes.
 * 
 * <p>
 * L'éditeur syntaxique est réalisé à partir de la librairie
 * <a href="http://fifesoft.com/rsyntaxtextarea/" target="_blank">rsyntaxtextarea</a>.
 * </p>
*/
public class PanelXml extends JInternalFrame implements ActionListener {
	private static final long serialVersionUID = 1L;

	private PanelInteraction pInter;
	private JPanel pFinal;
	private RSyntaxTextArea editArea;
	private JPopupMenu popup;
	
	public PanelXml (PanelInteraction pInter) {
		super();
		this.pInter = pInter;
		pFinal = new JPanel(new BorderLayout()); this.add(pFinal);
		initEdition ();
		initPopupMenus();
        setVisible(true);
	}

	private void initEdition() {
		// editeur de texte
        editArea = new RSyntaxTextArea(20,20);
		//editArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_XML);
		editArea.setCodeFoldingEnabled(true);
		editArea.setAntiAliasingEnabled(true);
		RTextScrollPane paneScrollPane = new RTextScrollPane(editArea);
        paneScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		editArea.setVisible(true);
		// placement
		pFinal.add(paneScrollPane,"Center");
	}
	
	private void initPopupMenus() {
		popup = editArea.getPopupMenu();
	    popup.addSeparator();
		JMenuItem menu;
		menu = new JMenuItem("Traduire"); menu.addActionListener(this); menu.setActionCommand("traduire");
		popup.add(menu);
		menu = new JMenuItem("Reformuler"); menu.addActionListener(this); menu.setActionCommand("reformuler");
		popup.add(menu);
		menu = new JMenuItem("Transformer 1-n"); menu.addActionListener(this); menu.setActionCommand("vectoriser");
		popup.add(menu);
	}
			    
	public void actionPerformed(ActionEvent e) {
        try {
			String cmd = e.getActionCommand();
			// boutons
			if ("traduire".equals(cmd)) {
				pInter.traduireXml();	
			}
			else if ("reformuler".equals(cmd)) {
				this.reformulerXml();	
			}
			else if ("vectoriser".equals(cmd)) {
				this.vectoriserXml();	
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	// ---------------------------------------------
	// Pour les autres Panels
	// ---------------------------------------------
	
	public String getText() {
		return this.editArea.getText();
	}	
	
	public void setText(String txt) {
		this.editArea.setText(txt);
	}	
	
	// ---------------------------------------------
	// boutons : reformuler, vectoriser...
	// ---------------------------------------------

	private boolean reformulerXml() {
		// récupération du programme Xml
		pInter.clearConsole();
		org.javascool.proglets.plurialgo.langages.xml.Programme prog;
		prog = (org.javascool.proglets.plurialgo.langages.xml.Programme) Programme.getProgramme(this.getText(),"xml"); 
		if (pInter.messageErreur(prog)) {
			return false;
		}
		// construction du programme dérivé
		Intermediaire inter = pInter.creerIntermediaire();
		inter.setOption("donnees", pInter.pPrincipal.donneesField.getText());
		inter.setOption("resultats", pInter.pPrincipal.resultatsField.getText());
		ProgrammeDerive progDer = new ProgrammeDerive(prog, inter);
		pInter.pPrincipal.algoField.setText(progDer.nom);
		// écriture du programme Xml dans l'onglet Xml
		pInter.add_xml(new org.javascool.proglets.plurialgo.langages.xml.Programme(progDer));
		pInter.pPrincipal.algoField.setText(progDer.nom);
		return(true);
	}
	
	private boolean vectoriserXml() {
		pInter.clearConsole();
		// récupération du programme Xml
		org.javascool.proglets.plurialgo.langages.xml.Programme prog;
		prog = (org.javascool.proglets.plurialgo.langages.xml.Programme) Programme.getProgramme(this.getText(),"xml");  
		if (pInter.messageErreur(prog)) {
			return false;
		}
		// construction du programme vectorisé
		ProgrammeVectorise progVect = new ProgrammeVectorise(prog);
		// écriture du programme Xml dans l'onglet Xml
		pInter.add_xml(new org.javascool.proglets.plurialgo.langages.xml.Programme(progVect));
		pInter.pPrincipal.algoField.setText(progVect.nom);
		return(true);
	}

}
