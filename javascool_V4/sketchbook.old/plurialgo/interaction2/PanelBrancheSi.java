/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2013.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.interaction2;

import java.awt.FlowLayout;
import java.awt.Color;
import java.awt.event.*;
import java.util.StringTokenizer;
import javax.swing.*;


/**
 * Cette classe correspond à l'onglet Si de l'interface graphique.
*/
@SuppressWarnings("unchecked")	// car les JList doivent être paramétrées avec Java7
public class PanelBrancheSi extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;

	static String[] niv_oper = { ")", "==", "!=", "<=", ">=", "<", ">" };
	static String[] niv_alors = { "...", "ET", "OU" };
	static String[] niv_si = { "si", "--" };
	static int var_taille = 6;
	static int expr_taille = 6;

	JTextField var1Field, var2Field; 
	JTextField expr1Field, expr2Field;
	JLabel ferm1Label, ferm2Label;
	JComboBox oper1List, oper2List;
	JComboBox alors1List;
	JComboBox siList;
	JPanel pBloc1, pBloc2;	
	
	public PanelBrancheSi () {
		this.setLayout( new FlowLayout(FlowLayout.LEFT) );
		initBox ();
	}

	private void initBox() {
		Box p1 = Box.createHorizontalBox();
		// si
		JPanel pSi = new JPanel();
		siList = new JComboBox(niv_si);
		siList.setSelectedIndex(0);
		pSi.add(siList);
		p1.add(pSi);
		// bloc 1
		pBloc1 = new JPanel();
		pBloc1.add( new JLabel("(") );
		var1Field = new JTextField(var_taille);
		pBloc1.add( var1Field );
		oper1List = new JComboBox(niv_oper);
		oper1List.setSelectedIndex(0);
		oper1List.addActionListener(this);
		pBloc1.add(new JScrollPane(oper1List));
		expr1Field = new JTextField(expr_taille);
		pBloc1.add( expr1Field );
		ferm1Label = new JLabel(")");
		pBloc1.add( ferm1Label );
		ferm1Label.setVisible(false);
		expr1Field.setVisible(false);
		p1.add(pBloc1);
		// lien 1
		JPanel pAlors1 = new JPanel();
		alors1List = new JComboBox(niv_alors);
		alors1List.setSelectedIndex(0);
		alors1List.addActionListener(this);
		pAlors1.add(new JScrollPane(alors1List));
		p1.add(pAlors1);
		// bloc 2
		pBloc2 = new JPanel();
		pBloc2.add( new JLabel("(") );
		var2Field = new JTextField(var_taille);
		pBloc2.add( var2Field );
		oper2List = new JComboBox(niv_oper);
		oper2List.setSelectedIndex(0);
		oper2List.addActionListener(this);
		pBloc2.add(new JScrollPane(oper2List));
		expr2Field = new JTextField(expr_taille);
		pBloc2.add( expr2Field );
		ferm2Label = new JLabel(")");
		pBloc2.add( ferm2Label );
		ferm2Label.setVisible(false);
		expr2Field.setVisible(false);
		p1.add(pBloc2);
		pBloc2.setVisible(false);
		// fin
		this.add(p1);
		this.setBackground(Color.white);
	}

	public void actionPerformed(ActionEvent e) {
		try {
			if (e.getSource()==oper1List) {
				if (oper1List.getSelectedIndex()==0) {
					ferm1Label.setVisible(false);
					expr1Field.setVisible(false);
				}
				else if (oper1List.getSelectedIndex()>0) {
					ferm1Label.setVisible(true);
					expr1Field.setVisible(true);
				}
			}
			if (e.getSource()==oper2List) {
				if (oper2List.getSelectedIndex()==0) {
					ferm2Label.setVisible(false);
					expr2Field.setVisible(false);
				}
				else if (oper2List.getSelectedIndex()>0) {
					ferm2Label.setVisible(true);
					expr2Field.setVisible(true);
				}
			}
			if (e.getSource()==alors1List) {
				if (alors1List.getSelectedIndex()==0) {
					pBloc2.setVisible(false);
				}
				else if (alors1List.getSelectedIndex()>0) {
					pBloc2.setVisible(true);
				}
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	// ---------------------------------------------
	// Pour les autres Panels
	// ---------------------------------------------

	public String getBloc(String var, String oper, String expr) {
		if (oper.equals(")")) {
			if (var.trim().isEmpty()) return var;
			return "(" + var + ")";
		}
		StringTokenizer tok1 = new StringTokenizer(expr,";",false);
		int nb_token = tok1.countTokens();
		String bloc = null;
		String et_ou = " ET ";
		if (oper.equals("==")) {
			et_ou = " OU ";
		}
		while(tok1.hasMoreTokens()) {
			String expr1 = tok1.nextToken().trim();
			if (bloc==null) {
				bloc = "(" + var + oper + expr1 + ")";
			}
			else {
				bloc = bloc + et_ou + "(" + var + oper + expr1 + ")";
			}
		}
		if (nb_token>1) {
			bloc = "(" + bloc + ")";
		} 
		return bloc;
	}
	
	public String getCondition() {
		// bloc1
		String bloc1;
		String var1 = this.var1Field.getText();
		String oper1 = oper1List.getSelectedItem().toString();
		String expr1 = this.expr1Field.getText();
		bloc1 = this.getBloc(var1, oper1, expr1); 
		// lien 1
		if (alors1List.getSelectedIndex()==0) {
			return bloc1;
		}
		String alors1 = alors1List.getSelectedItem().toString().toUpperCase();
		// bloc2
		String bloc2;
		String var2 = this.var2Field.getText();
		String oper2 = oper2List.getSelectedItem().toString();
		String expr2 = this.expr2Field.getText();
		bloc2 = this.getBloc(var2, oper2, expr2); 
		// reponse
		String rep;
		rep = "( " + bloc1 + " " + alors1 + " " + bloc2 + " )";
		return(rep);
	}
	
	public int getNiveau() {
		return siList.getSelectedIndex();
	}
	
	// ---------------------------------------------
	// Pour l'onglet Html
	// ---------------------------------------------
	
	void setSi(int indent, String var1, String oper1, String expr1, String alors1, String var2, String oper2, String expr2) {
		siList.setSelectedIndex(indent);
		var1Field.setText(var1);
		oper1List.setSelectedItem(oper1);
		expr1Field.setText(expr1);
		alors1List.setSelectedItem(alors1);
		var2Field.setText(var2);
		oper2List.setSelectedItem(oper2);
		expr2Field.setText(expr2);
	}
	
	void setSi(int indent, String var1, String oper1, String expr1) {
		siList.setSelectedIndex(indent);
		var1Field.setText(var1);
		oper1List.setSelectedItem(oper1);
		expr1Field.setText(expr1);
		alors1List.setSelectedIndex(0);
		var2Field.setText("");
		oper2List.setSelectedIndex(0);
		expr2Field.setText("");		
	}
	
	void setSi(int indent, String var1) {
		siList.setSelectedIndex(indent);
		var1Field.setText(var1);
		oper1List.setSelectedIndex(0);
		expr1Field.setText("");
		alors1List.setSelectedIndex(0);
		var2Field.setText("");
		oper2List.setSelectedIndex(0);
		expr2Field.setText("");		
	}
	
	// ---------------------------------------------
	// Pour l'onglet Boucles
	// ---------------------------------------------
		
	public void masquerSi() {
		siList.setVisible(false);
	}

}
