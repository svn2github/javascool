/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2013.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.interaction2;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.*;
import java.util.Iterator;
import javax.swing.*;
import javax.swing.event.ListSelectionListener;

import org.javascool.proglets.plurialgo.divers.Divers;
import org.javascool.proglets.plurialgo.langages.modele.*;
import org.javascool.proglets.plurialgo.langages.xml.*;



/**
 * Cette classe correspond à l'onglet Boucles de l'interface graphique.
*/
@SuppressWarnings("unchecked")	// car les JList doivent être paramétrées avec Java7
public class PanelBoucles extends JPanel implements ActionListener, ListSelectionListener {
	private static final long serialVersionUID = 1L;

	private PanelInteraction pInter;	
	
	private JPanel pVect;	
	private JPanel pPour, pTq;
	private PanelBrancheSi pCond;
	private JList boucleList;
	private JComboBox pourOptionList;
	private JTextField pourVarField, pourFinField;
	private JTextField pourDebutField, pourPasField;
	private JTextField sommeVarField, sommeArgField; private JCheckBox sommeCheck;
	private JTextField compterVarField; private PanelBrancheSi pCompterCond; private JCheckBox compterCheck;
	private JTextField miniVarField, miniArgField; private JCheckBox miniCheck;
	private JTextField maxiVarField, maxiArgField; private JCheckBox maxiCheck;
	private JButton vectoriserButton, creerButton, insererButton, effacerButton;

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
		pPour = new JPanel();
		pPour.add( new JLabel(" : ") );
		pourVarField = new JTextField(4);
		pourVarField.setText("k");
		pPour.add(pourVarField);
		pPour.add( new JLabel(" de ") );
		pourDebutField = new JTextField(2);
		pourDebutField.setText("1");
		pPour.add(pourDebutField);
		pPour.add( new JLabel(" a ") );
		pourFinField = new JTextField(6);
		pourFinField.setText("n");
		pPour.add(pourFinField);
		pPour.add( new JLabel(" pas ") );
		pourPasField = new JTextField(2);
		pourPasField.setText("1");
		pPour.add(pourPasField);
		String [] choix_pour ={"...","tantque","jusque"};
		pourOptionList = new JComboBox(choix_pour);
		pourOptionList.setSelectedIndex(0);
		pourOptionList.addActionListener(this);
		pPour.add(new JScrollPane(pourOptionList));
		// tantque
		pTq = new JPanel();
		pTq.add( new JLabel(" : ") );
		pCond = new PanelBrancheSi();
		pCond.masquerSi();
		pTq.add(pCond);
		// boucle
		p = new JPanel(); 
		boucleList = new JList();
		String [] choix_boucle ={"pour","tantque","jusque"};
		boucleList.setListData(choix_boucle); 
		boucleList.setSelectedIndex(0);
		p.add(boucleList);
		boucleList.addListSelectionListener(this);
		Box boxPourTq = Box.createVerticalBox();
		boxPourTq.add(pPour);
		boxPourTq.add(pTq); pTq.setVisible(false);
		p.add(boxPourTq);
		vbox.add(p);
		// boutons
		vbox.add(Box.createVerticalStrut(20));
		p = new JPanel(); 
		creerButton = new JButton("Creer"); p.add(creerButton);
		creerButton.addActionListener(this);
		creerButton.setActionCommand("creer");
		creerButton.setVisible(true);
		insererButton = new JButton("Inserer"); p.add(insererButton);
		insererButton.addActionListener(this);
		insererButton.setActionCommand("inserer");
		insererButton.setVisible(true);
		effacerButton = new JButton("Effacer"); p.add(effacerButton);
		effacerButton.addActionListener(this);
		effacerButton.setActionCommand("effacer");
		effacerButton.setVisible(true);
		vectoriserButton = new JButton("Transformer"); p.add(vectoriserButton);
		vectoriserButton.addActionListener(this);
		vectoriserButton.setActionCommand("vectoriser");
		vectoriserButton.setVisible(true);
		vbox.add(p);
		vbox.add(Box.createVerticalStrut(20));
		// sommation
		p = new JPanel(); 
		sommeCheck = new JCheckBox(); p.add(sommeCheck);
		p.add( new JLabel("somme de ") );
		sommeArgField = new JTextField(10);	// sommeArgField.setText("increment");
		p.add(sommeArgField);
		p.add( new JLabel(" ---> ") );
		sommeVarField = new JTextField(6); sommeVarField.setText("som");
		p.add(sommeVarField);
		vbox.add(p);
		// minimum
		p = new JPanel(); 
		miniCheck = new JCheckBox(); p.add(miniCheck);
		p.add( new JLabel("minimum de ") );
		miniArgField = new JTextField(10);	// miniArgField.setText("expression");
		p.add(miniArgField);
		p.add( new JLabel(" ---> ") );
		miniVarField = new JTextField(6); miniVarField.setText("mini");
		p.add(miniVarField);
		vbox.add(p);
		// maximum
		p = new JPanel(); 
		maxiCheck = new JCheckBox(); p.add(maxiCheck);
		p.add( new JLabel("maximum de ") );
		maxiArgField = new JTextField(10);	// maxiArgField.setText("expression");
		p.add(maxiArgField);
		p.add( new JLabel(" ---> ") );
		maxiVarField = new JTextField(6); maxiVarField.setText("maxi");
		p.add(maxiVarField);
		vbox.add(p);
		// comptage
		p = new JPanel(); 
		compterCheck = new JCheckBox();	p.add(compterCheck);
		p.add( new JLabel("comptage de ") );
		pCompterCond = new PanelBrancheSi();
		pCompterCond.masquerSi();
		p.add(pCompterCond);
		p.add( new JLabel(" ---> ") );
		compterVarField = new JTextField(6); compterVarField.setText("effectif");
		p.add(compterVarField);
		vbox.add(p);
	}
		
	public void actionPerformed(ActionEvent e) {
        try {
			String cmd = e.getActionCommand();
			if (e.getSource() == this.vectoriserButton || ("vectoriser".equals(cmd))) {	
				if (this.vectoriserSelection()) return;
				this.vectoriser();	
			}
			else if (e.getSource() == this.creerButton || ("creer".equals(cmd))) {	
				this.creer();	
			}
			else if (e.getSource() == this.effacerButton || ("effacer".equals(cmd))) {	
				this.effacer();	
			}
			else if (e.getSource() == this.insererButton || ("inserer".equals(cmd))) {	
				this.inserer();	
			}
			// visibilite panel pTq
			else if (e.getSource()==pourOptionList) {
				if (pourOptionList.getSelectedIndex()==0) {	// pour standard
					pTq.setVisible(false);
				}
				else  {	// pour_tantque ou pour_jusque
					pTq.setVisible(true);
				}
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}	
	
	public void valueChanged(javax.swing.event.ListSelectionEvent e) {
		try {
			if (e.getSource()==boucleList) {
				if (boucleList.getSelectedIndex()==0) {	// pour
					pPour.setVisible(true);
					if (pourOptionList.getSelectedIndex()>0) {	
						pTq.setVisible(true);
					}
					else {
						pTq.setVisible(false);
					}
				}
				else if (boucleList.getSelectedIndex()==1) {	// tantque
					pPour.setVisible(false); pTq.setVisible(true);
				}
				else if (boucleList.getSelectedIndex()==2) {	// jusque
					pPour.setVisible(false); pTq.setVisible(true);
				}
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
		else if (pInter.isPython()) {
			inter = pInter.creerIntermediaireLarp("vectoriser");
			analyseur = new AnalyseurPython(pInter.getText(), false, false, inter);
		}
		else if (pInter.isXcas()) {
			inter = pInter.creerIntermediaireLarp("vectoriser");
			analyseur = new AnalyseurXcas(pInter.getText(), false, false, inter);
		}
		else if (pInter.isCarmetal()) {
			inter = pInter.creerIntermediaireLarp("vectoriser");
			analyseur = new AnalyseurCarmetal(pInter.getText(), false, false, inter);
		}
		else {
			pInter.clearConsole();
			pInter.writeConsole("---------- Transformation impossible ----------\n");
			return;
		}
		// vectorisation
		pInter.messageWarning(analyseur.getProgramme());
		org.javascool.proglets.plurialgo.langages.xml.XmlProgramme prog = analyseur.getProgramme();
		vectoriser(prog, true);
		ProgrammeVectorise progVect = new ProgrammeVectorise(prog);
		// ajout du resultat dans l' onglet Complements et dans l'editeur
		pInter.add_xml(new org.javascool.proglets.plurialgo.langages.xml.XmlProgramme(progVect));
		pInter.traduireXml();
	}
	
	private void vectoriser(org.javascool.proglets.plurialgo.langages.xml.XmlProgramme prog, boolean avecVectorisation) {
		// tantque ou jusque
		if ( (boucleList.getSelectedIndex()==1) || ( (boucleList.getSelectedIndex()==0) && (pourOptionList.getSelectedIndex()==1) ) ) {
			prog.options.add( new XmlArgument("tantque", null, pCond.getCondition().trim()) );
		}
		if ( (boucleList.getSelectedIndex()==2) || ( (boucleList.getSelectedIndex()==0) && (pourOptionList.getSelectedIndex()==2) ) ) {
			prog.options.add( new XmlArgument("recherche", null, pCond.getCondition().trim()) );
		}
		// les options
		if (sommeCheck.isSelected()) {
			prog.options.add( new XmlArgument("sommation", null, sommeVarField.getText()+":"+sommeArgField.getText()) );
		}
		if (compterCheck.isSelected()) {
			prog.options.add( new XmlArgument("comptage", null, compterVarField.getText()+":"+pCompterCond.getCondition().trim()) );
		}
		if (miniCheck.isSelected()) {
			prog.options.add( new XmlArgument("minimum", null, miniVarField.getText()+":"+miniArgField.getText()) );
		}
		if (maxiCheck.isSelected()) {
			prog.options.add( new XmlArgument("maximum", null, maxiVarField.getText()+":"+maxiArgField.getText()) );
		}
		// pour
		if (boucleList.getSelectedIndex()==0) {
			prog.options.add( new XmlArgument("pour_var", null, pourVarField.getText()) );
			prog.options.add( new XmlArgument("pour_fin", null, pourFinField.getText()) );
			prog.options.add( new XmlArgument("pour_debut", null, pourDebutField.getText()) );
			prog.options.add( new XmlArgument("pour_pas", null, pourPasField.getText()) );		
		}
		else {
			prog.options.add( new XmlArgument("pour_var", null, "") );
			prog.options.add( new XmlArgument("pour_fin", null, "") );
			prog.options.add( new XmlArgument("pour_debut", null, "") );
			prog.options.add( new XmlArgument("pour_pas", null, "") );	
		}
		// vectorisation
		String vect = "";
		if (avecVectorisation && !pourVarField.getText().trim().isEmpty()) {
			for (Iterator<ModeleVariable> iter=prog.variables.iterator(); iter.hasNext();) {
				ModeleVariable var = iter.next();
				if (var.nom.equals(pourVarField.getText().trim())) continue;
				if (var.nom.equals("i")) continue;
				if (var.nom.equals("i1")) continue;
				if (var.nom.equals("j")) continue;
				if (var.nom.equals("j1")) continue;
				if (var.nom.equals("k")) continue;
				if (var.isSimple()) {
					vect = vect + " " + var.nom;
				}
			}
			vect = vect.trim();
			if (!vect.isEmpty()) {
				String message = "2 options sont applicables au programme principal : ";
				message = message + "\n ---- sans introduction de tableaux : cliquez sur Annuler";
				message = message + "\n ---- avec introduction de tableaux : ";
				message = message + "\n        - indiquez les variables concernees";
				message = message + "\n        - cliquez sur OK";
				String reponse = JOptionPane.showInputDialog(message, vect);
				if (reponse==null) {
					vect = "";
				}
				else {
					vect = reponse.trim();
				}
			}
		}
		prog.options.add( new XmlArgument("vectorisation", null, vect) );
	}	
	
	private void creer() {
		pInter.clearConsole();
		// vectorisation
		org.javascool.proglets.plurialgo.langages.xml.XmlProgramme prog;
		prog = new org.javascool.proglets.plurialgo.langages.xml.XmlProgramme();
		prog.nom = pInter.pPrincipal.getNomAlgo();
		vectoriser(prog, false);
		ProgrammeVectorise progVect = new ProgrammeVectorise(prog);
		// ajout du resultat dans les onglets Complements et Resultats
		pInter.add_xml(new org.javascool.proglets.plurialgo.langages.xml.XmlProgramme(progVect));
		pInter.traduireXml();
	}
	
	private void inserer() {
		pInter.clearConsole();
		// vectorisation
		org.javascool.proglets.plurialgo.langages.xml.XmlProgramme prog_xml;
		prog_xml = new org.javascool.proglets.plurialgo.langages.xml.XmlProgramme();
		prog_xml.nom = pInter.pPrincipal.getNomAlgo();
		vectoriser(prog_xml, false);
		ProgrammeVectorise progVect = new ProgrammeVectorise(prog_xml);
		// conversion du programme en Xml
		prog_xml = new org.javascool.proglets.plurialgo.langages.xml.XmlProgramme(progVect);
		pInter.add_xml(prog_xml);
		// conversion du programme dans le langage courant
		String lang = pInter.pPrincipal.getNomLangage();
		String txt = pInter.getXml();
		ModeleProgramme prog = ModeleProgramme.getProgramme(txt,lang); 
		// ajout de la boucle
		StringBuffer buf = new StringBuffer();
		int indent = Divers.getIndent(pInter.getTextArea());
		for (Iterator<ModeleInstruction> iter=prog.instructions.iterator(); iter.hasNext();) {
			ModeleInstruction instr = iter.next();
			if (instr.isLecture()) continue;
			if (instr.isEcriture()) continue;
			instr.ecrire(prog, buf, indent);
		}
		if (buf.length()>0 ) {
			prog.postTraitement(buf);
			Divers.inserer(pInter.getTextArea(), buf.toString());
		}
	}
	
	boolean vectoriserSelection() {
		pInter.clearConsole();
		// recherche zone de sélection
		JTextArea editArea = pInter.getTextArea();
		int i_start = editArea.getSelectionStart();
		int i_end = editArea.getSelectionEnd();
		int indent = 0;
		if (i_end - i_start<5) return false;	// trop petit (donc sélection involontaire ?)
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
			txt_select = "\t" + Divers.remplacer(txt_select, "\n", "\n\t");
		}
		catch(Exception ex) {
			return false;
		}
		// vectorisation
		org.javascool.proglets.plurialgo.langages.xml.XmlProgramme prog_xml;
		prog_xml = new org.javascool.proglets.plurialgo.langages.xml.XmlProgramme();
		prog_xml.nom = pInter.pPrincipal.getNomAlgo();
		vectoriser(prog_xml, false);
		ProgrammeVectorise progVect = new ProgrammeVectorise(prog_xml);
		// ajout du commentaire transformer1n dans le Pour le programme vectorisé
		for (Iterator<ModeleInstruction> iter=progVect.instructions.iterator(); iter.hasNext();) {
			ModeleInstruction instr = iter.next();
			if (instr.isPour()) {
				ModelePour pour = instr.pours.get(0);
				if (pour==null) continue;
				if (pour.instructions.size()==1) {
					if (pour.instructions.get(0).isCommentaire()) {
						pour.instructions.remove(0);
					}
				}
				pour.instructions.add(0, new ModeleInstruction("//transformer1n"));
				break;
			}
			if (instr.isTantQue()) {
				ModeleTantQue tq = instr.tantques.get(0);
				if (tq==null) continue;
				if (tq.instructions.size()==1) {
					if (tq.instructions.get(0).isCommentaire()) {
						tq.instructions.remove(0);
					}
				}
				tq.instructions.add(0, new ModeleInstruction("//transformer1n"));
				break;
			}
		}
		// conversion du programme en Xml
		prog_xml = new org.javascool.proglets.plurialgo.langages.xml.XmlProgramme(progVect);
		pInter.add_xml(prog_xml);
		// conversion du programme dans le langage courant
		String lang = pInter.pPrincipal.getNomLangage();
		String txt = pInter.getXml();
		ModeleProgramme prog = ModeleProgramme.getProgramme(txt,lang); 
		// texte de la boucle (avec transformation1n)
		StringBuffer buf = new StringBuffer();
		for (Iterator<ModeleInstruction> iter=prog.instructions.iterator(); iter.hasNext();) {
			ModeleInstruction instr = iter.next();
			if (instr.isLecture()) continue;
			if (instr.isEcriture()) continue;
			instr.ecrire(prog, buf, indent);
		}
		// on remplace la ligne contenant transformation1n par txt_select
		int k = buf.indexOf("transformer1n");
		if (k<0) return false;
		int k_debut = buf.substring(0, k).lastIndexOf("\n")+1;
		int k_fin = buf.indexOf("\n", k);
		buf.delete(k_debut, k_fin);
		buf.insert(k_debut, txt_select);
		prog.postTraitement(buf);
		buf.delete(0, 1);
		editArea.replaceRange(buf.toString(), i_start, i_end);
		return true;
	}
	
	
	// ---------------------------------------------
	// Pour l'onglet Html
	// ---------------------------------------------
	
	void setSomme(boolean cocher, String mode) {
		sommeCheck.setSelected(cocher);
		int i = mode.indexOf(":");
		if (i<0) {
			sommeVarField.setText("");
			sommeArgField.setText("");
		}
		else {
			sommeVarField.setText(mode.substring(0, i));
			sommeArgField.setText(mode.substring(i+1));			
		}
	}
	
	void setMinimum(boolean cocher, String mode) {
		miniCheck.setSelected(cocher);
		int i = mode.indexOf(":");
		if (i<0) {
			miniVarField.setText("");
			miniArgField.setText("");
		}
		else {
			miniVarField.setText(mode.substring(0, i));
			miniArgField.setText(mode.substring(i+1));			
		}
	}
	
	void setMaximum(boolean cocher, String mode) {
		maxiCheck.setSelected(cocher);
		int i = mode.indexOf(":");
		if (i<0) {
			maxiVarField.setText("");
			maxiArgField.setText("");
		}
		else {
			maxiVarField.setText(mode.substring(0, i));
			maxiArgField.setText(mode.substring(i+1));			
		}
	}
	
	void effacer() {
		setPour("k", "1", "n", "1");
		setBoucleCondition("");
		setSomme(false, "som:");
		setCompterVar(false, "effectif"); setCompterCondition("");
		setMaximum(false, "maxi:");
		setMinimum(false, "mini:");
	}	
	
	void setPour(String var, String debut, String fin, String pas) {
		boucleList.setSelectedIndex(0);
		pourOptionList.setSelectedIndex(0);
		pourVarField.setText(var);
		pourDebutField.setText(debut);
		pourFinField.setText(fin);
		pourPasField.setText(pas);
	}
	
	void setPourOption(String boucle) {
		pourOptionList.setSelectedItem(boucle);
	}
	
	void setBoucle(String boucle) {
		boucleList.setSelectedValue(boucle, false);
	}
	
	void setBoucleCondition(String var1, String oper1, String expr1, String alors1, String var2, String oper2, String expr2) {
		pCond.setSi(0, var1, oper1, expr1, alors1, var2, oper2, expr2);
	}
	
	void setBoucleCondition(String var1, String oper1, String expr1) {
		pCond.setSi(0, var1, oper1, expr1);
	}
	
	void setBoucleCondition(String var1) {
		pCond.setSi(0, var1);	
	}
	
	void setCompterVar(boolean cocher, String var) {
		compterCheck.setSelected(cocher);
		compterVarField.setText(var);
	}
	
	void setCompterCondition(String var1, String oper1, String expr1, String alors1, String var2, String oper2, String expr2) {
		pCompterCond.setSi(0, var1, oper1, expr1, alors1, var2, oper2, expr2);
	}
	
	void setCompterCondition(String var1, String oper1, String expr1) {
		pCompterCond.setSi(0, var1, oper1, expr1);
	}
	
	void setCompterCondition(String var1) {
		pCompterCond.setSi(0, var1);	
	}

}
