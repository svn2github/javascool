/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2013.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.interaction2;

import java.awt.FlowLayout;
import java.awt.Color;
import java.awt.event.*;
import java.util.Iterator;
import javax.swing.*;

import org.javascool.proglets.plurialgo.divers.Divers;
import org.javascool.proglets.plurialgo.langages.modele.*;
import org.javascool.proglets.plurialgo.langages.xml.*;


/**
 * Cette classe correspond à l'onglet Si de l'interface graphique.
*/
@SuppressWarnings("unchecked")	// car les JList doivent être paramétrées avec Java7
public class PanelSi extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	static int nb_max = 10;
	JCheckBox t_check[];
	PanelBrancheSi t_branche[];

	PanelInteraction pInter;
	JButton creerButton;
	JButton effacerButton;
	JButton insererButton;
	
	public PanelSi (PanelInteraction pInter) {
		this.setLayout( new FlowLayout(FlowLayout.LEFT) );
		this.pInter = pInter; 
		initBox ();
	}

	private void initBox() {
		Box vBox = Box.createVerticalBox();
		// panel Boutons 
		Box hbox = Box.createHorizontalBox();
		creerButton = new JButton("Creer"); creerButton.addActionListener(this);
		creerButton.setActionCommand("creer"); creerButton.setVisible(false);
		insererButton = new JButton("Inserer"); insererButton.addActionListener(this);
		insererButton.setActionCommand("inserer");
		effacerButton = new JButton("Effacer"); effacerButton.addActionListener(this);
		effacerButton.setActionCommand("effacer");
		hbox.add(Box.createHorizontalStrut(5));
        hbox.add(creerButton); hbox.add(Box.createHorizontalStrut(5));
        hbox.add(insererButton); hbox.add(Box.createHorizontalStrut(5));
        hbox.add(effacerButton); hbox.add(Box.createHorizontalStrut(5));
        vBox.add(hbox);
        // création des alternatives
		t_check = new JCheckBox[nb_max];
		t_branche = new PanelBrancheSi[nb_max];
		for(int i=0; i<nb_max; i=i+1) {
			t_check[i]=new JCheckBox();
			t_check[i].addActionListener(this);
			t_branche[i]=new PanelBrancheSi();
			t_branche[i].setVisible(false);
			JPanel p = new JPanel();
			p.setLayout( new FlowLayout(FlowLayout.LEFT) );
			p.add(t_check[i]);
			p.add(t_branche[i]);
			vBox.add(p);
		}
		for(int i=0; i<2; i=i+1) {
			t_check[i].setSelected(true);
			t_branche[i].setVisible(true);
		}
        // fin
		this.add(vBox);
		this.setBackground(Color.white);
	}

	public void actionPerformed(ActionEvent e) {
		try {
			for(int i=0; i<nb_max; i=i+1) {
				if (e.getSource()==t_check[i]) {
					if (t_check[i].isSelected()) {
						for(int i1=0; i1<=i; i1=i1+1) {
							t_check[i1].setSelected(true);
							t_branche[i1].setVisible(true);
						}
						for(int i1=i+1; i1<nb_max; i1=i1+1) {
							t_check[i1].setSelected(false);
							t_branche[i1].setVisible(false);
						}
					}
					else {
						for(int i1=0; i1<i; i1=i1+1) {
							t_check[i1].setSelected(true);
							t_branche[i1].setVisible(true);
						}
						for(int i1=i; i1<nb_max; i1=i1+1) {
							t_check[i1].setSelected(false);
							t_branche[i1].setVisible(false);
						}
						
					}
				}
			}
			if (e.getSource() == this.creerButton) {	
				this.nouveau();
			}
			if (e.getSource() == this.insererButton) {	
				this.inserer();
			}
			if (e.getSource() == this.effacerButton) {	
				this.effacer();
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	// ---------------------------------------------
	// Boutons
	// ---------------------------------------------
	
	void nouveau() {
		org.javascool.proglets.plurialgo.langages.xml.XmlProgramme prog_xml;
		ProgrammeSi prog_si = new ProgrammeSi();
		for(int i=0; i<nb_max; i=i+1) {
			if (t_check[i].isSelected()) {
				prog_si.ajouterBranche(t_branche[i].getNiveau(), t_branche[i].getCondition());
			}
		}
		prog_xml = new org.javascool.proglets.plurialgo.langages.xml.XmlProgramme(prog_si);
		pInter.add_xml(prog_xml);
		pInter.traduireXml();
	}
	
	void inserer() {
		org.javascool.proglets.plurialgo.langages.xml.XmlProgramme prog_xml;
		ProgrammeSi prog_si = new ProgrammeSi();
		for(int i=0; i<nb_max; i=i+1) {
			if (t_check[i].isSelected()) {
				prog_si.ajouterBranche(t_branche[i].getNiveau(), t_branche[i].getCondition());
			}
		}
		// conversion du programme en Xml
		prog_xml = new org.javascool.proglets.plurialgo.langages.xml.XmlProgramme(prog_si);
		pInter.add_xml(prog_xml);
		// conversion du programme dans le langage courant
		String lang = pInter.pPrincipal.getNomLangage();
		String txt = pInter.getXml();
		ModeleProgramme prog = ModeleProgramme.getProgramme(txt,lang); 
		// ajout de l'instruction conditionnelle
		StringBuffer buf = new StringBuffer();
		int indent = Divers.getIndent(pInter.getTextArea());
		for (Iterator<ModeleInstruction> iter=prog.instructions.iterator(); iter.hasNext();) {
			ModeleInstruction instr = iter.next();
			instr.ecrire(prog, buf, indent);
		}
		if (buf.length()>0 ) {
			prog.postTraitement(buf);
			Divers.inserer(pInter.getTextArea(), buf.toString());
		}
	}
	
	void effacer() {
		for(int i=0; i<nb_max; i=i+1) {
			t_branche[i].setSi(0, "");
		}
		for(int i=0; i<2; i=i+1) {
			t_check[i].setSelected(true);
			t_branche[i].setVisible(true);
		}
		for(int i=2; i<nb_max; i=i+1) {
			t_check[i].setSelected(false);
			t_branche[i].setVisible(false);
		}			
	}
	
	// ---------------------------------------------
	// Pour l'onglet Html
	// ---------------------------------------------
	
	void setSi(int i, int indent, String var1, String oper1, String expr1, String alors1, String var2, String oper2, String expr2) {
		t_check[i].setSelected(true);
		t_branche[i].setVisible(true);
		t_branche[i].setSi(indent, var1, oper1, expr1, alors1, var2, oper2, expr2);
	}
	
	void setSi(int i, int indent, String var1, String oper1, String expr1) {
		t_check[i].setSelected(true);
		t_branche[i].setVisible(true);
		t_branche[i].setSi(indent, var1, oper1, expr1);
	}
	
	void setSi(int i, int indent, String var1) {
		t_check[i].setSelected(true);
		t_branche[i].setVisible(true);
		t_branche[i].setSi(indent, var1);
	}
		
}
