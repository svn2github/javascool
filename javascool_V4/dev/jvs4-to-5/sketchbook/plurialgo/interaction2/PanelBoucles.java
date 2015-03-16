/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2013.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.interaction2;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.*;
import javax.swing.*;

import org.javascool.proglets.plurialgo.langages.xml.AnalyseurAlgobox;
import org.javascool.proglets.plurialgo.langages.xml.AnalyseurJavascool;
import org.javascool.proglets.plurialgo.langages.xml.AnalyseurLarp;
import org.javascool.proglets.plurialgo.langages.xml.AnalyseurVb;
import org.javascool.proglets.plurialgo.langages.xml.Argument;
import org.javascool.proglets.plurialgo.langages.xml.Intermediaire;
import org.javascool.proglets.plurialgo.langages.xml.ProgrammeVectorise;
import org.javascool.proglets.plurialgo.langages.xml.iAnalyseur;



/**
 * Cette classe correspond à l'onglet Boucles de l'interface graphique.
*/
@SuppressWarnings("unchecked")	// car les JList doivent être paramétrées avec Java7
public class PanelBoucles extends JPanel implements ActionListener {
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
	private JButton vectoriserButton, bouclerButton;

	public PanelBoucles (PanelInteraction pInter) {
		super(new BorderLayout());
		this.pInter = pInter;
		initVect();
		this.add(pVect);
        setVisible(true);
	}
	
	private void initVect() {
		JPanel p;
		pVect = new JPanel();
		pVect.setLayout( new FlowLayout() );
		this.add(pVect,"West");
		Box vbox = Box.createVerticalBox();
		pVect.add(vbox); 
		vbox.add(Box.createVerticalStrut(20));
		// pour
		p = new JPanel(); 
		boucleList = new JList();
		String [] choix_boucle ={"pour","tantque"};
		boucleList.setListData(choix_boucle); 
		boucleList.setSelectedIndex(0);
		p.add(boucleList);
		p.add( new JLabel(" : ") );
		pourVarField = new JTextField(8);
		pourVarField.setText("k");
		p.add(pourVarField);
		p.add( new JLabel(" de 1 a ") );
		pourFinField = new JTextField(8);
		pourFinField.setText("n");
		p.add(pourFinField);
		vbox.add(p);
		// sommation
		p = new JPanel(); 
		sommeCheck = new JCheckBox(); p.add(sommeCheck);
		p.add( new JLabel("sommation : ") );
		sommeModeField = new JTextField(20);
		sommeModeField.setText("somme:increment");
		p.add(sommeModeField);
		vbox.add(p);
		// comptage
		p = new JPanel(); 
		compterCheck = new JCheckBox();	p.add(compterCheck);
		p.add( new JLabel("comptage : ") );
		compterModeField = new JTextField(20);
		compterModeField.setText("effectif:condition");
		p.add(compterModeField);
		vbox.add(p);
		// minimum
		p = new JPanel(); 
		miniCheck = new JCheckBox(); p.add(miniCheck);
		p.add( new JLabel("minimum : ") );
		miniModeField = new JTextField(20);
		miniModeField.setText("mini:expression");
		p.add(miniModeField);
		vbox.add(p);
		// maximum
		p = new JPanel(); 
		maxiCheck = new JCheckBox(); p.add(maxiCheck);
		p.add( new JLabel("maximum : ") );
		maxiModeField = new JTextField(20);
		maxiModeField.setText("maxi:expression");
		p.add(maxiModeField);
		vbox.add(p);
		// chercher
		p = new JPanel(); 
		chercherCheck = new JCheckBox(); p.add(chercherCheck);
		p.add( new JLabel("chercher (un) : ") );
		chercherModeField = new JTextField(20);
		chercherModeField.setText("condition");
		p.add(chercherModeField);
		vbox.add(p);
		// bouton
		p = new JPanel(); 
		vectoriserButton = new JButton("Transformer (1-n)"); p.add(vectoriserButton);
		vectoriserButton.addActionListener(this);
		vectoriserButton.setActionCommand("vectoriser");
		vectoriserButton.setVisible(true);
		bouclerButton = new JButton("Boucler"); p.add(bouclerButton);
		bouclerButton.addActionListener(this);
		bouclerButton.setActionCommand("boucler");
		vectoriserButton.setVisible(true);
		vbox.add(p);
		// ajout de dimension
		p = new JPanel(); 
		p.add( new JLabel("vectorisation : ") );
		vectModeField = new JTextField(20);
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
			else if (e.getSource() == this.bouclerButton || ("boucler".equals(cmd))) {	
				this.boucler();	
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	// ---------------------------------------------
	// Vectorisation
	// ---------------------------------------------
	
	private void vectoriser() {
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
			inter = pInter.creerIntermediaireLarp("vectoriser");
			analyseur = new AnalyseurLarp(pInter.getText(), false, false, inter);
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
	
	private void boucler() {
		pInter.clearConsole();
		// vectorisation
		org.javascool.proglets.plurialgo.langages.xml.Programme prog;
		prog = new org.javascool.proglets.plurialgo.langages.xml.Programme();
		prog.nom = pInter.pPrincipal.getNomAlgo();
		vectoriser(prog);
		ProgrammeVectorise progVect = new ProgrammeVectorise(prog);
		// ajout du resultat dans les onglets Complements et Resultats
		pInter.add_xml(new org.javascool.proglets.plurialgo.langages.xml.Programme(progVect));
		pInter.traduireXml();
	}
		
}
