/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2013.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.interaction1;

import javax.swing.*;


/**
 * Cette classe permet de tester l'application dans une fenêtre swing, sans 
 * créer la proglet.
*/
public class FenetreTest extends JFrame {
	private static final long serialVersionUID = 1L;

	PanelInteraction panelInteraction;
	
	public FenetreTest() {
		super("Patrick.Raffinat@univ-pau.fr");
		panelInteraction = new PanelInteraction(); 
		this.setContentPane(panelInteraction);
		setPreferredSize(new java.awt.Dimension(680, 480));
		pack();
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	public static void main(String[] args) {
		FenetreTest fen = new FenetreTest();
		fen.setVisible(true);
	}
}
