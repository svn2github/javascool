/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2013.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.langages.python;

import java.util.*;

import org.javascool.proglets.plurialgo.divers.*;


/**
 * Cette classe hérite de la classe homonyme du modèle.
*/
public class Programme extends org.javascool.proglets.plurialgo.langages.modele.Programme {

	/**
	 * Redéfinition obligatoire de cette méthode.
	 */	
	public void ecrire() {
		StringBuffer buf = new StringBuffer();
		String nom_fich = nom + ".py";
		les_fichiers.put(nom_fich, buf);
		ecrire(buf);
		this.postTraitement();
	}
	
	private void ecrire(StringBuffer buf) {
		this.addDebut(buf);
		this.addTypes(buf, 0);
		this.addClasses(buf, 0);
		this.addSousProgs(buf, 0);
		if (!this.avecFormulaire()) {
			this.addMain(buf, 0);
		}
		else {
			this.addMainFormulaire(buf, 0);
		}
		Divers.ecrire(buf, "\n\n");
	}
	
	private void addDebut(StringBuffer buf) {
		Divers.ecrire(buf, "from string import *", 0);
		Divers.ecrire(buf, "from Tkinter import *", 0);
	}
	
	private void addSousProgs(StringBuffer buf, int indent) {
		if ((operations.size()>0))
			this.commenter(buf, "sous programmes", indent);
		for (Iterator<org.javascool.proglets.plurialgo.langages.modele.Operation> iter=operations.iterator(); iter.hasNext();) {
			Operation oper = (Operation) iter.next();
			oper.ecrire(this, buf, indent);
		}
	}
	
	private void addClasses(StringBuffer buf, int indent) {
		if ((classes.size()>0))
			this.commenter(buf, "classes", indent);
		for (Iterator<org.javascool.proglets.plurialgo.langages.modele.Classe> iter=classes.iterator(); iter.hasNext();) {
			Classe classe = (Classe) iter.next();
			if (classe.isEnregistrement()) {
				//classe.ecrireEnregistrement(this, buf, indent);
			}
			else {
				classe.ecrire(this, buf, 0);
			}
		}
	}
	
	private void addMain(StringBuffer buf, int indent) {
		this.commenter(buf, "programme principal", indent);
		for (Iterator<org.javascool.proglets.plurialgo.langages.modele.Variable> iter=variables.iterator(); iter.hasNext();) {
			Variable var = (Variable) iter.next();
			var.ecrire(this, buf, indent);
		}
		for (Iterator<org.javascool.proglets.plurialgo.langages.modele.Instruction> iter=instructions.iterator(); iter.hasNext();) {
			Instruction instr = (Instruction) iter.next();
			instr.ecrire(this, buf, indent);
		}
	}
	
	// ------------------------------------------
	// utilitaires d'ecriture
	// ------------------------------------------ 
	
	public void commenter(StringBuffer fich, String texte, int indent) {
		Divers.ecrire(fich, "# " + texte, indent);
	}
	
	public String quote(String s) {
		return "'" +s + "'";
	}
	
	private void postTraitement(StringBuffer buf) {
		//Divers.remplacer(buf, "==", "==");
		Divers.remplacer(buf, " et ", " and ");
		Divers.remplacer(buf, " ET ", " and ");
		Divers.remplacer(buf, " ou ", " or ");
		Divers.remplacer(buf, " OU ", " or ");
		//Divers.remplacer(buf, "!=", "!=");
		Divers.remplacer(buf, "vrai", "True");
		Divers.remplacer(buf, "VRAI", "True");
		Divers.remplacer(buf, "faux", "False");
		Divers.remplacer(buf, "FAUX", "False");
		//Divers.remplacer(buf, "][", "*" + this.getMaxTab() + "+");
		//Divers.remplacer(buf, "[", "[");
		//Divers.remplacer(buf, "[", "[");
		//Divers.remplacer(buf, "'", "\"");
		Divers.remplacer(buf, "this", "self");
	}
	
	private void remplacerPoint(StringBuffer buf) {
		for (Iterator<org.javascool.proglets.plurialgo.langages.modele.Classe> iter1=classes.iterator(); iter1.hasNext();) {
			Classe cl = (Classe) iter1.next();
			if (cl.isClasse()) continue;
			for (Iterator<org.javascool.proglets.plurialgo.langages.modele.Variable> iter=cl.proprietes.iterator(); iter.hasNext();) {
				Variable prop = (Variable) iter.next();
				this.remplacerPoint(buf, "." + prop.nom, "['" + prop.nom + "']");
			}
		}
	}
	
	private void remplacerPoint(StringBuffer buf, String ancien, String nouveau) {
		int lg_ancien = ancien.length();
		String ch;
		for (int i=buf.length()-lg_ancien - 2; i>=2; i--) {
			if (ancien.equals(buf.substring(i, i+lg_ancien))) {
				ch = buf.substring(i+lg_ancien, i+lg_ancien+1);
				if  ( (ch.compareTo("A")>=0) && (ch.compareTo("Z")<=0) ) continue;
				if  ( (ch.compareTo("a")>=0) && (ch.compareTo("z")<=0) ) continue;
				if  ( (ch.compareTo("0")>=0) && (ch.compareTo("9")<=0) ) continue;
				if  ( ch.equals("_") ) continue;
				if  ( ch.equals("\"") ) continue;
				if  ( ch.equals("'") ) continue;
				if  ( ch.equals(":") ) continue;
				ch = buf.substring(i+lg_ancien, i+lg_ancien+2);
				if  ( ch.equals(" :") ) continue;
				buf.delete(i, i+lg_ancien);
				buf.insert(i, nouveau);
			}
		}	
	}
	
	private void postTraitement() {
		Iterator<String> iter = this.les_fichiers.keySet().iterator();
		while (iter.hasNext()) {
			String nom_fich = iter.next();
			StringBuffer buf = this.les_fichiers.get(nom_fich);
			remplacerPoint(buf);
			postTraitement(buf);
		}
	}
	
	// ------------------------------------------
	// types
	// ------------------------------------------ 
	
	private void addTypes(StringBuffer buf, int indent) {
		this.commenter(buf, "types standard utilises", indent);
		if (avecType("TAB_") || avecType("MAT_")) {
			Divers.ecrire(buf, "MAX_TAB = 5", indent);
		}
	}
	// ------------------------------------------
	// formulaires
	// ------------------------------------------ 
	
	private void addMainFormulaire(StringBuffer buf, int indent) {
		this.commenter(buf, "programme principal", indent);
		for (Iterator<org.javascool.proglets.plurialgo.langages.modele.Instruction> iter=instructions.iterator(); iter.hasNext();) {
			Instruction instr = (Instruction) iter.next();
			Argument arg_formu = (Argument) instr.getFormulaire();
			if (arg_formu!=null) {
				Formulaire form = new Formulaire(instr);
				addAction(buf, form, indent);
			}
		}
		this.commenter(buf, "formulaire", indent);
		for (Iterator<org.javascool.proglets.plurialgo.langages.modele.Instruction> iter=instructions.iterator(); iter.hasNext();) {
			Instruction instr = (Instruction) iter.next();
			Argument arg_formu = (Argument) instr.getFormulaire();
			if (arg_formu!=null) {
				Formulaire form = new Formulaire(instr);
				addConstruction(buf, form, indent);
			}
		}
	}
	
	private void addConstruction(StringBuffer buf, Formulaire form, int indent) {
		Instruction instr_saisie = form.instr_pere;
		Argument arg_formu = (Argument) instr_saisie.getFormulaire();
		Divers.ecrire(buf, arg_formu.nom + " = Tk()", indent);
		form.constrFormu(this, buf, indent);
		Divers.indenter(buf, indent);
		Divers.ecrire(buf, "bouton_ok = Button(" + arg_formu.nom + ", ");
		Divers.ecrire(buf, "text='calculer', command=" + "main_" + arg_formu.nom +")");
		Divers.ecrire(buf, "bouton_ok.grid(row=n_lig, column=0)", indent);
		Divers.ecrire(buf, arg_formu.nom+".mainloop()", indent);
	}
	
	private void addAction(StringBuffer buf, Formulaire form, int indent) {
		Instruction instr_saisie = form.instr_pere;
		Argument arg_formu = (Argument) instr_saisie.getFormulaire();
		Divers.ecrire(buf, "def main_" + arg_formu.nom + "() :", indent);
		for (Iterator<org.javascool.proglets.plurialgo.langages.modele.Variable> iter=variables.iterator(); iter.hasNext();) {
			Variable var = (Variable) iter.next();
			var.ecrire(this, buf, indent+1);
		}
		form.lireFormu(this, buf, indent+1);
		for (Iterator<org.javascool.proglets.plurialgo.langages.modele.Instruction> iter=instructions.iterator(); iter.hasNext();) {
			Instruction instr = (Instruction) iter.next();
			if (instr == instr_saisie) continue;
			instr.ecrire(this, buf, indent+1);
		}
	}
	
}
