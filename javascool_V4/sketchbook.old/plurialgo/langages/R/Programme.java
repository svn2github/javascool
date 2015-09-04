/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2014.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.langages.R;

import java.util.*;

import org.javascool.proglets.plurialgo.divers.*;
import org.javascool.proglets.plurialgo.langages.modele.*;

/**
 * Cette classe hérite de la classe homonyme du modèle.
*/
public class Programme extends ModeleProgramme {
	
	/**
	 * Redéfinition obligatoire de cette méthode.
	 */
	public void ecrire() {
		StringBuffer buf = new StringBuffer();
		String nom_fich = nom + ".R";
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
		Divers.ecrire(buf, "\n");
	}
	
	private void addDebut(StringBuffer buf) {
		if (this.avecFormulaire()) {
			Divers.ecrire(buf, "library(tcltk)", 0);
		}
		Divers.ecrire(buf, "library(svDialogs)", 0);
	}
	
	private void addSousProgs(StringBuffer buf, int indent) {
		// if ((operations.size()>0)) this.commenter(buf, "sous programmes", indent);
		for (Iterator<ModeleOperation> iter=operations.iterator(); iter.hasNext();) {
			Operation oper = (Operation) iter.next();
			oper.ecrire(this, buf, indent);
		}
	}
	
	private void addClasses(StringBuffer buf, int indent) {
		// if ((classes.size()>0))	this.commenter(buf, "classes", indent);
		for (Iterator<ModeleClasse> iter=classes.iterator(); iter.hasNext();) {
			Classe classe = (Classe) iter.next();
			if (classe.isEnregistrement()) {
				//classe.ecrireEnregistrement(this, buf, indent);
			}
			else {
				classe.ecrire(this, buf, indent);
			}
		}
	}
	
	private void addMain(StringBuffer buf, int indent) {
		// this.commenter(buf, "programme principal", indent);
		for (Iterator<ModeleVariable> iter=variables.iterator(); iter.hasNext();) {
			Variable var = (Variable) iter.next();
			var.ecrire(this, buf, indent);
		}
		for (Iterator<ModeleInstruction> iter=instructions.iterator(); iter.hasNext();) {
			Instruction instr = (Instruction) iter.next();
			if (instr.deplacerHtml) continue;
			if (instr.deplacerWriter) continue;
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
		return "\"" + s + "\"";
		//return "'" +s + "'";
	}
	
	public String getMaxTab() {
		String MAX_TAB = "MAX_TAB";
		return MAX_TAB;
	}
	
	public String getDim(int dim, Argument arg) {
		String res = arg.getDim(dim);
		if (res==null) res=getMaxTab();
		return res;
	}
	
	public void postTraitement(StringBuffer buf) {
		Divers.remplacer(buf, " et ", " & ");
		Divers.remplacer(buf, " ET ", " & ");
		Divers.remplacer(buf, " ou ", " | ");
		Divers.remplacer(buf, " OU ", " | ");
		Divers.remplacer(buf, " and ", " & ");
		Divers.remplacer(buf, " AND ", " & ");
		Divers.remplacer(buf, " or ", " | ");
		Divers.remplacer(buf, " OR ", " | ");
		Divers.remplacer(buf, "vrai", "T");
		Divers.remplacer(buf, "VRAI", "T");
		Divers.remplacer(buf, "faux", "F");
		Divers.remplacer(buf, "FAUX", "F");
		Divers.remplacer(buf, "true", "T");
		Divers.remplacer(buf, "false", "F");
		Divers.remplacer(buf, "][", ",");
		Divers.remplacer(buf, "{[", "[[");
		Divers.remplacer(buf, "]}", "]]");
		Divers.remplacer(buf, ", )", ")");	// appels de fonctions ???
		Divers.remplacer(buf, "MAX_TAB", "10");
	}
	
	private void remplacerPoint(StringBuffer buf) {
		for (Iterator<ModeleClasse> iter1=classes.iterator(); iter1.hasNext();) {
			Classe cl = (Classe) iter1.next();
			if (cl.isEnregistrement()) {
				for (Iterator<ModeleVariable> iter=cl.proprietes.iterator(); iter.hasNext();) {
					Variable prop = (Variable) iter.next();
					Divers.remplacer(buf, "." + prop.nom, "$" + prop.nom);
				}
			}
			else {
				for (Iterator<ModeleVariable> iter=cl.proprietes.iterator(); iter.hasNext();) {
					Variable prop = (Variable) iter.next();
					Divers.remplacer(buf, "." + prop.nom, "@" + prop.nom);
				}
				for (Iterator<ModeleOperation> iter=cl.operations.iterator(); iter.hasNext();) {
					Operation oper = (Operation) iter.next();
					Divers.remplacer(buf, "." + oper.nom, "@" + oper.nom);
				}
			}
		}
		Divers.remplacer(buf, "this.", "this@");
	}
	
	private void doublerCrochets(StringBuffer buf) {
		String ch;
		int j;
		for (int i=buf.length()-2; i>=2; i--) {
			ch = buf.substring(i, i+2);
			if (ch.equals("].") || ch.equals("]$")  || ch.equals("]@")) {
				ch = buf.substring(i-1, i);
				if  ( ch.equals("]") ) continue;
				buf.insert(i+1, "}");
				j=i-1;
				while ( (!buf.substring(j-1, j).equals("[")) && (j>1) ) j--;
				if (buf.substring(j-1, j).equals("[")) buf.insert(j-1, "{");
				i=j;
			}
		}		
	}
	
	private void postTraitement() {
		Iterator<String> iter = this.les_fichiers.keySet().iterator();
		while (iter.hasNext()) {
			String nom_fich = iter.next();
			StringBuffer buf = this.les_fichiers.get(nom_fich);
			doublerCrochets(buf);
			remplacerPoint(buf);
			postTraitement(buf);
		}
	}
	
	// ------------------------------------------
	// types
	// ------------------------------------------ 
	
	public void ecrireType(StringBuffer buf, org.javascool.proglets.plurialgo.langages.modele.InfoTypee info) {
		Divers.ecrire(buf, info.type);
	}
	
	private void addTypes(StringBuffer buf, int indent) {
		if (avecType("TAB_") || avecType("MAT_")) {
			//Divers.ecrire(buf, "MAX_TAB = 10", indent);
		}
	}
	
	// ------------------------------------------
	// formulaires
	// ------------------------------------------ 
	
	private void addMainFormulaire(StringBuffer buf, int indent) {
		this.commenter(buf, "programme principal", indent);
		for (Iterator<ModeleInstruction> iter=instructions.iterator(); iter.hasNext();) {
			Instruction instr = (Instruction) iter.next();
			Argument arg_formu = (Argument) instr.getFormulaire();
			if (arg_formu!=null) {
				Formulaire form = new Formulaire(instr);
				addAction(buf, form, indent);
			}
		}
		Divers.ecrire(buf, "\n");
		this.commenter(buf, "formulaire", indent);
		for (Iterator<ModeleInstruction> iter=instructions.iterator(); iter.hasNext();) {
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
		Divers.ecrire(buf, arg_formu.nom + " = tktoplevel()", indent);
		Divers.ecrire(buf, "tktitle(" + arg_formu.nom + ") = " + this.quote("..."), indent);
		form.constrFormu(this, buf, indent);
		Divers.indenter(buf, indent);
		Divers.ecrire(buf, "bouton_ok = tkbutton(" + arg_formu.nom + ", ");
		Divers.ecrire(buf, "text='calculer', command=" + "main_" + arg_formu.nom +")");
		Divers.ecrire(buf, "tkgrid(bouton_ok,row=n_lig, column=0)", indent);
		Divers.ecrire(buf, "tkfocus(" + arg_formu.nom + ")", indent);
	}
	
	private void addAction(StringBuffer buf, Formulaire form, int indent) {
		Instruction instr_saisie = form.instr_pere;
		Argument formu = (Argument) instr_saisie.getFormulaire();
		Divers.ecrire(buf, "main_" + formu.nom + " = function() {", indent);
		for (Iterator<ModeleVariable> iter=variables.iterator(); iter.hasNext();) {
			Variable var = (Variable) iter.next();
			var.ecrire(this, buf, indent+1);
		}
		form.lireFormu(this, buf, indent+1);
		for (Iterator<ModeleInstruction> iter=instructions.iterator(); iter.hasNext();) {
			Instruction instr = (Instruction) iter.next();
			if (instr == instr_saisie) continue;
			if (instr.deplacerHtml) continue;
			if (instr.deplacerWriter) continue;
			instr.ecrire(this, buf, indent+1);
		}
		Divers.ecrire(buf, "}", indent);
	}
	
}
