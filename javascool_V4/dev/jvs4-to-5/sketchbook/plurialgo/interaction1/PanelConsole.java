/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2013.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.interaction1;

import java.awt.*;
import javax.swing.*;

/**
 * Cette classe correspond à la console de l'interface graphique.
*/
public class PanelConsole extends JPanel {
	private static final long serialVersionUID = 1L;
	
	public PanelInteraction pInter;
	private JTextArea sortieArea;
	
	public PanelConsole(PanelInteraction pInter) {
		super(new BorderLayout());
		this.pInter = pInter; 	
		sortieArea = new JTextArea(4,20);
		sortieArea.setEditable(true);
		this.add(new JScrollPane(sortieArea),"North");	
	}	
		
	public void clearConsole() {
		try {
			this.sortieArea.setText("");
		}
		catch (Exception ex) {
			System.err.println(ex);
		}
	}
	
	public void writeConsole(String txt) {
		try {
			this.sortieArea.append(txt);
			this.sortieArea.setCaretPosition(0);
		}
		catch (Exception ex) {
			System.err.println(ex);
		}
	}	
	
	public void writelnConsole() {
		try {
			this.sortieArea.append("\n");
			this.sortieArea.setCaretPosition(0);
		}
		catch (Exception ex) {
			System.err.println(ex);
		}
	}

}
