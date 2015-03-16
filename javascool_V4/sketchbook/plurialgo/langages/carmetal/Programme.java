/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2013.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.langages.carmetal;

import java.util.*;

import org.javascool.proglets.plurialgo.divers.*;
import org.javascool.proglets.plurialgo.langages.modele.*;


/**
 * Cette classe hérite de la classe homonyme du modèle.
*/
public class Programme extends ModeleProgramme {
		
	public void ecrire() {
		StringBuffer buf = new StringBuffer();
		String nom_fich = nom + ".html";
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
	}
	
	private void addSousProgs(StringBuffer buf, int indent) {
		//if ((operations.size()>0)) this.commenter(buf, "sous programmes", indent);
		for (Iterator<ModeleOperation> iter=operations.iterator(); iter.hasNext();) {
			Operation oper = (Operation) iter.next();
			oper.ecrire(this, buf, indent);
		}
	}
	
	private void addClasses(StringBuffer buf, int indent) {
		//if ((classes.size()>0))	this.commenter(buf, "enregistrements ou classes", indent);
		for (Iterator<ModeleClasse> iter=classes.iterator(); iter.hasNext();) {
			Classe classe = (Classe) iter.next();
			if (classe.isEnregistrement()) {
				classe.ecrireEnregistrement(this, buf, indent);
			}
			else {
				classe.ecrire(this, buf, indent);
			}
		}
	}
	
	private void addMain(StringBuffer buf, int indent) {
		//this.commenter(buf, "programme principal", indent);
		for (Iterator<ModeleVariable> iter=variables.iterator(); iter.hasNext();) {
			Variable var = (Variable) iter.next();
			var.ecrire(this, buf, indent);
		}
		for (Iterator<ModeleInstruction> iter=instructions.iterator(); iter.hasNext();) {
			Instruction instr = (Instruction) iter.next();
			instr.ecrire(this, buf, indent);
		}
	}
	
	// ------------------------------------------
	// utilitaires d'ecriture
	// ------------------------------------------ 
	
	public void commenter(StringBuffer fich, String texte, int indent) {
		// Divers.ecrire(fich, "<!-- " + texte + " -->", indent);
		Divers.ecrire(fich, "// " + texte, indent);
	}
	
	public String quote(String s) {
		return "\"" + s + "\"";
	}
	
	public void postTraitement(StringBuffer buf) {
		Divers.remplacer(buf, " et ", " && ");
		Divers.remplacer(buf, " ET ", " && ");
		Divers.remplacer(buf, " ou ", " || ");
		Divers.remplacer(buf, " OU ", " || ");
		Divers.remplacer(buf, " and ", " && ");
		Divers.remplacer(buf, " AND ", " && ");
		Divers.remplacer(buf, " or ", " || ");
		Divers.remplacer(buf, " OR ", " || ");
		Divers.remplacer(buf, "vrai", "true");
		Divers.remplacer(buf, "VRAI", "true");
		Divers.remplacer(buf, "faux", "false");
		Divers.remplacer(buf, "FAUX", "false");
		Divers.remplacer(buf, "MAX_TAB", "10");
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
	
	private void addTypes(StringBuffer buf, int indent) {
		if (avecType("TAB_") || avecType("MAT_")) {
			// Divers.ecrire(buf, "var MAX_TAB = 10; // taille maximale des tableaux", indent);
		}
	}

}
