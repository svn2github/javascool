/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2013.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.langages.java;

import java.util.*;
import org.javascool.proglets.plurialgo.divers.*;
import org.javascool.proglets.plurialgo.langages.modele.*;


/**
 * Cette classe hérite de la classe homonyme du modèle.
*/
public class Programme extends ModeleProgramme {
	
	boolean avecScanner = false;

	/**
	 * Redéfinition obligatoire de cette méthode.
	 */	
	public void ecrire() {
		StringBuffer buf = new StringBuffer();
		String nom_fich = nom + ".java";
		les_fichiers.put(nom_fich, buf);
		ecrire(buf);
		this.postTraitement();
	}
	
	private void ecrire(StringBuffer buf) {
		this.addDebut(buf);
		this.addTypes(buf, 1);
		this.addClasses(buf, 1);
		this.addSousProgs(buf, 1);
		if (!this.avecFormulaire()) {
			this.addMain(buf, 1);
		}
		else {
			this.addMainFormulaire(buf, 1);
		}
		Divers.ecrire(buf, "} ", 0);
	}
	
	private void addDebut(StringBuffer buf) {
		Divers.ecrire(buf, "import java.io.*;", 0);
		if (this.avecSql()) {
			Divers.ecrire(buf, "import java.sql.*;", 0);
		}
		Divers.ecrire(buf, "public class "+this.nom+" {", 0);
		if (avecScanner) {
			Divers.ecrire(buf, "static java.util.Scanner scanner = new java.util.Scanner(System.in) ;", 1);
		}
	}
	
	private void addSousProgs(StringBuffer buf, int indent) {
		//if ((operations.size()>0)) this.commenter(buf, "sous programmes", indent);
		for (Iterator<ModeleOperation> iter=operations.iterator(); iter.hasNext();) {
			Operation oper = (Operation) iter.next();
			oper.ecrire(this, buf, indent);
		}
	}
	
	private void addClasses(StringBuffer buf, int indent) {
		//if ((classes.size()>0))	this.commenter(buf, "enregistrements", indent);
		for (Iterator<ModeleClasse> iter=classes.iterator(); iter.hasNext();) {
			Classe classe = (Classe) iter.next();
			if (classe.isEnregistrement()) {
				StringBuffer buf1 = new StringBuffer();
				this.les_fichiers.put(classe.nom+".java", buf1);
				classe.ecrire(this, buf1, 0);
			}
			else {
				StringBuffer buf1 = new StringBuffer();
				this.les_fichiers.put(classe.nom+".java", buf1);
				classe.ecrire(this, buf1, 0);
			}
		}
	}
	
	private void addMain(StringBuffer buf, int indent) {
		//this.commenter(buf, "programme principal", indent);
		Divers.ecrire(buf, "public static void main(String [] args) {", indent);
		for (Iterator<ModeleVariable> iter=variables.iterator(); iter.hasNext();) {
			Variable var = (Variable) iter.next();
			var.ecrire(this, buf, indent+1);
		}
		for (Iterator<ModeleInstruction> iter=instructions.iterator(); iter.hasNext();) {
			Instruction instr = (Instruction) iter.next();
			instr.ecrire(this, buf, indent+1);
		}
		Divers.ecrire(buf, "} ", indent);
	}
	
	// ------------------------------------------
	// utilitaires d'ecriture
	// ------------------------------------------
	
	public void commenter(StringBuffer fich, String texte, int indent) {
		Divers.ecrire(fich, "// " + texte, indent);
	}
	
	public String quote(String s) {
		return "\"" + s + "\"";
	}
	
	public String getScanner() {
		this.avecScanner = true;
		return this.nom + ".scanner";
	}
	
	public void postTraitement(StringBuffer buf) {
		//Divers.remplacer(buf, "==", "==");
		Divers.remplacer(buf, " et ", " && ");
		Divers.remplacer(buf, " ET ", " && ");
		Divers.remplacer(buf, " ou ", " || ");
		Divers.remplacer(buf, " OU ", " || ");
		Divers.remplacer(buf, " and ", " && ");
		Divers.remplacer(buf, " AND ", " && ");
		Divers.remplacer(buf, " or ", " || ");
		Divers.remplacer(buf, " OR ", " || ");
		//Divers.remplacer(buf, "!=", "!=");
		Divers.remplacer(buf, "vrai", "true");
		Divers.remplacer(buf, "VRAI", "true");
		Divers.remplacer(buf, "faux", "false");
		Divers.remplacer(buf, "FAUX", "false");
		//Divers.remplacer(buf, "][", "][");
		//Divers.remplacer(buf, "[", "[");
		//Divers.remplacer(buf, "[", "[");
		Divers.remplacer(buf, "'", "\"");
		Divers.remplacer(buf, "@", "'");	// insert into
		Divers.remplacerPredef(buf, "racine", "Math.sqrt");
	}
	
	private void postTraitement() {
		Iterator<String> iter = this.les_fichiers.keySet().iterator();
		while (iter.hasNext()) {
			String nom_fich = iter.next();
			StringBuffer buf = this.les_fichiers.get(nom_fich);
			postTraitement(buf);
		}
	}
	
	// ------------------------------------------
	// types
	// ------------------------------------------ 
	
	public void ecrireType(StringBuffer buf, org.javascool.proglets.plurialgo.langages.modele.InfoTypee info) {
		if (info.isEntier()) Divers.ecrire(buf, "int");
		else if (info.isReel()) Divers.ecrire(buf, "double");
		else if (info.isTexte()) Divers.ecrire(buf, "String");
		else if (info.isBooleen()) Divers.ecrire(buf, "boolean");
		else if (info.isTabEntiers()) Divers.ecrire(buf, "int []");
		else if (info.isTabReels()) Divers.ecrire(buf, "double []");
		else if (info.isTabTextes()) Divers.ecrire(buf, "String []");
		else if (info.isTabBooleens()) Divers.ecrire(buf, "boolean []");
		else if (info.isTabClasse(this)) {
			Classe cl = (Classe) info.getClasseOfTab(this);
			Divers.ecrire(buf, cl.nom + " []");
		}
		else if (info.isMatEntiers()) Divers.ecrire(buf, "int [][]");
		else if (info.isMatReels()) Divers.ecrire(buf, "double [][]");
		else if (info.isMatTextes()) Divers.ecrire(buf, "String [][]");
		else if (info.isMatBooleens()) Divers.ecrire(buf, "boolean [][]");
		else if (info.isMatClasse(this)) {
			Classe cl = (Classe) info.getClasseOfTab(this);
			Divers.ecrire(buf, cl.nom + " [][]");
		}
		else Divers.ecrire(buf, info.type);
	}
	
	private void addTypes(StringBuffer buf, int indent) {
		if (avecType("TAB_") || avecType("MAT_")) {
			Divers.ecrire(buf, "public static int MAX_TAB = 10; // taille maximale des tableaux", indent);
		}
	}
	
	// ------------------------------------------
	// formulaires
	// ------------------------------------------ 
	
	private void addMainFormulaire(StringBuffer buf, int indent) {
		this.commenter(buf, "programme principal", indent);
		Divers.ecrire(buf, "public static void main(String [] args) {", indent);
		for (Iterator<ModeleInstruction> iter=instructions.iterator(); iter.hasNext();) {
			Instruction instr = (Instruction) iter.next();
			Argument formu = (Argument) instr.getFormulaire();
			if (formu!=null) {
				Divers.indenter(buf, indent+1);
				Divers.ecrire(buf, formu.type + " " + formu.nom);
				Divers.ecrire(buf, " = new " + formu.type + "();");
				StringBuffer buf1 = new StringBuffer();
				this.les_fichiers.put(formu.type+".java", buf1);
				addFormulaire(buf1, instr);
			}
		}
		Divers.ecrire(buf, "} ", indent);
	}
	
	private void addFormulaire(StringBuffer buf, Instruction instr_saisie) {
		Formulaire form = new Formulaire(instr_saisie);
		Divers.ecrire(buf, "import java.awt.*;", 0);
		Divers.ecrire(buf, "import javax.swing.*;", 0);
		Divers.ecrire(buf, "import java.awt.event.*;", 0);
		Divers.ecrire(buf, "public class " + form.arg_form.type + " implements ActionListener {", 0);
		this.addConstructionFormulaire(buf, form, 1);
		this.addActionFormulaire(buf, form, 1);
		Divers.ecrire(buf, "}", 0);
	}
	
	private void addConstructionFormulaire(StringBuffer buf, Formulaire form, int indent) {
		Instruction instr_saisie = form.instr_pere;
		form.declFormu(this, buf, indent);
		Divers.ecrire(buf, "JButton bouton_ok, bouton_quitter;", indent);
		Divers.ecrire(buf, "JFrame fen;", indent);
		Divers.ecrire(buf, "JPanel p;", indent);
		int nb_lig = instr_saisie.arguments.size() + 1;
		Argument formu = (Argument) instr_saisie.getFormulaire();
		Divers.ecrire(buf, "public " +  formu.type + "() {", indent);
		Divers.ecrire(buf, "fen = new JFrame(" + this.quote("Formulaire de saisie") + ");", indent+1);
		Divers.ecrire(buf, "p=new JPanel(new GridLayout(" + nb_lig + ",2));", indent+1);
		Divers.ecrire(buf, "fen.add(p);", indent+1);
		form.constrFormu(this, buf, indent);
		Divers.indenter(buf, indent+1);
		Divers.ecrire(buf, "bouton_ok=new JButton(" + this.quote("calculer") + "); ");
		Divers.ecrire(buf, "p.add(bouton_ok);");
		Divers.ecrire(buf, "bouton_ok.addActionListener(this);", indent+1);
		Divers.indenter(buf, indent+1);
		Divers.ecrire(buf, "bouton_quitter=new JButton(" + this.quote("quitter") + "); ");
		Divers.ecrire(buf, "p.add(bouton_quitter);");
		Divers.ecrire(buf, "bouton_quitter.addActionListener(this);", indent+1);
		Divers.ecrire(buf, "fen.pack();", indent+1);
		Divers.ecrire(buf, "fen.setVisible(true);", indent+1);
		Divers.ecrire(buf, "}", indent);
	}
	
	private void addActionFormulaire(StringBuffer buf, Formulaire form, int indent) {
		Instruction instr_saisie = form.instr_pere;
		Divers.ecrire(buf, "public void actionPerformed(ActionEvent e) {", indent);
		for (Iterator<ModeleVariable> iter=variables.iterator(); iter.hasNext();) {
			Variable var = (Variable) iter.next();
			var.ecrire(this, buf, indent+1);
		}
		Divers.ecrire(buf, "if (e.getSource()==bouton_ok) {", indent+1);
		form.lireFormu(this, buf, indent);
		for (Iterator<ModeleInstruction> iter=instructions.iterator(); iter.hasNext();) {
			Instruction instr = (Instruction) iter.next();
			if (instr == instr_saisie) continue;
			instr.ecrire(this, buf, indent+2);
		}
		Divers.ecrire(buf, "}", indent+1);
		Divers.ecrire(buf, "if (e.getSource()==bouton_quitter) {", indent+1);
		Divers.ecrire(buf, "fen.dispose();", indent+2);
		Divers.ecrire(buf, "}", indent+1);
		Divers.ecrire(buf, "}", indent);
	}
	
}
