/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2013.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.langages.vb;

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
		String nom_fich = nom + ".bas";
		les_fichiers.put(nom_fich, buf);
		ecrire(buf);
		this.postTraitement();
	}
	
	private void ecrire(StringBuffer buf) {
		this.addDebut(buf);
		this.addTypes(buf, 0);
		this.addClasses(buf, 0);
		this.addSousProgs(buf, 0);
		this.addMain(buf, 0);
	}
	
	private void addDebut(StringBuffer buf) {
		Divers.ecrire(buf, "Option Explicit", 0);
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
			this.commenter(buf, "enregistrements ou classes", indent);
		for (Iterator<org.javascool.proglets.plurialgo.langages.modele.Classe> iter=classes.iterator(); iter.hasNext();) {
			Classe classe = (Classe) iter.next();
			if (classe.isEnregistrement()) {
				classe.ecrireEnregistrement(this, buf, indent);
			}
			else {
				StringBuffer buf1 = new StringBuffer();
				this.les_fichiers.put(classe.nom+".bas", buf1);
				classe.ecrire(this, buf1, 0);
			}
		}
	}
	
	private void addMain(StringBuffer buf, int indent) {
		this.commenter(buf, "programme principal", indent);
		Divers.ecrire(buf, "sub main()", indent);
		for (Iterator<org.javascool.proglets.plurialgo.langages.modele.Variable> iter=variables.iterator(); iter.hasNext();) {
			Variable var = (Variable) iter.next();
			var.ecrire(this, buf, indent+1);
		}
		for (Iterator<org.javascool.proglets.plurialgo.langages.modele.Instruction> iter=instructions.iterator(); iter.hasNext();) {
			Instruction instr = (Instruction) iter.next();
			instr.ecrire(this, buf, indent+1);
		}
		Divers.ecrire(buf, "end sub", indent);
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

	private void postTraitement(StringBuffer buf) {
		Divers.remplacer(buf, "this.", "");
		Divers.remplacer(buf, this.quote("\\t"), "vbTab");
		Divers.remplacer(buf, "==", "=");
		Divers.remplacer(buf, " et ", " and ");
		Divers.remplacer(buf, " ET ", " and ");
		Divers.remplacer(buf, " ou ", " or ");
		Divers.remplacer(buf, " OU ", " or ");
		Divers.remplacer(buf, "!=", "<>");
		Divers.remplacer(buf, "vrai", "true");
		Divers.remplacer(buf, "VRAI", "true");
		Divers.remplacer(buf, "faux", "false");
		Divers.remplacer(buf, "FAUX", "false");
		Divers.remplacer(buf, "][", ",");
		Divers.remplacer(buf, "[", "(");
		Divers.remplacer(buf, "]", ")");
		Divers.remplacer(buf, "'", "\"");
		Divers.remplacer(buf, "//", "'");
		Divers.remplacerPredef(buf, "racine", "Sqr");
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
		if (info.isEntier()) Divers.ecrire(buf, "as integer");
		else if (info.isReel()) Divers.ecrire(buf, "as double");
		else if (info.isTexte()) Divers.ecrire(buf, "as string");
		else if (info.isBooleen()) Divers.ecrire(buf, "as boolean");
		else if (info.isTabEntiers()) Divers.ecrire(buf, "(1 to MAX_TAB) as integer");
		else if (info.isTabReels()) Divers.ecrire(buf, "(1 to MAX_TAB) as double");
		else if (info.isTabTextes()) Divers.ecrire(buf, "(1 to MAX_TAB) as string");
		else if (info.isTabBooleens()) Divers.ecrire(buf, "(1 to MAX_TAB) as boolean");
		else if (info.isTabClasse(this)) {
			Classe cl = (Classe) info.getClasseOfTab(this);
			Divers.ecrire(buf, "(1 to MAX_TAB) as " + cl.nom );
		}
		else if (info.isMatEntiers()) Divers.ecrire(buf, "(1 to MAX_TAB, 1 to MAX_TAB) as integer");
		else if (info.isMatReels()) Divers.ecrire(buf, "(1 to MAX_TAB, 1 to MAX_TAB) as double");
		else if (info.isMatTextes()) Divers.ecrire(buf, "(1 to MAX_TAB, 1 to MAX_TAB) as string");
		else if (info.isMatBooleens()) Divers.ecrire(buf, "(1 to MAX_TAB, 1 to MAX_TAB) as boolean");
		else if (info.isMatClasse(this)) {
			Classe cl = (Classe) info.getClasseOfTab(this);
			Divers.ecrire(buf, "(1 to MAX_TAB, 1 to MAX_TAB) as " + cl.nom);
		}
		else Divers.ecrire(buf, "as " + info.type);
	}
	
	private void addTypes(StringBuffer buf, int indent) {
		//this.commenter(buf, "types standard utilises", indent);
		if (avecType("TAB_") || avecType("MAT_")) {
			Divers.ecrire(buf, "const MAX_TAB as integer = 5", indent);
		}
	}
	
}

	