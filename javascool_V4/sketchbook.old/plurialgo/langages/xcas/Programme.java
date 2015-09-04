/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2013.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.langages.xcas;

import java.util.*;
import org.javascool.proglets.plurialgo.divers.*;
import org.javascool.proglets.plurialgo.langages.modele.*;


/**
 * Cette classe hérite de la classe homonyme du modèle.
*/
public class Programme extends ModeleProgramme {
	
	public void ecrire() {
		StringBuffer buf = new StringBuffer();
		String nom_fich = nom + ".txt";
		les_fichiers.put(nom_fich, buf);
		ecrire(buf);
	}
	
	private void ecrire(StringBuffer buf) {
		this.addClasses(buf, 0);
		this.addSousProgs(buf, 0);
		this.addMain(buf, 0);
		this.postTraitement();
	}
	
	private void addSousProgs(StringBuffer buf, int indent) {
		for (Iterator<ModeleOperation> iter=operations.iterator(); iter.hasNext();) {
			Operation oper = (Operation) iter.next();
			oper.ecrire(this, buf, indent);
		}
	}
	
	private void addClasses(StringBuffer buf, int indent) {
		if ((classes.size()>0))
			this.ecrireWarning("Pas d'objets ou d'enregistrements avec ce langage");
	}
	
	private void addMain(StringBuffer buf, int indent) {
		for (Iterator<ModeleVariable> iter=variables.iterator(); iter.hasNext();) {
			Variable var = (Variable) iter.next();
			if (var.isTabSimple()) {
				Divers.ecrire(buf, var.nom + ":=[];", indent);
			}
			if (var.isMatSimple()) {
				String MAX_TAB = getMaxTab();
				Divers.ecrire(buf, var.nom + ":=matrix("+MAX_TAB+","+MAX_TAB+")", indent);
			}
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
		Divers.ecrire(fich, "// " + texte, indent);
	}
	
	public String quote(String s) {
		return "\"" + s + "\"";
	}
	
	public String getMaxTab() {
		return "10";
	}
	
	public void postTraitement(StringBuffer buf) {
		Divers.remplacer(buf, " et ", " et ");
		Divers.remplacer(buf, " ET ", " et ");
		Divers.remplacer(buf, " ou ", " ou ");
		Divers.remplacer(buf, " OU ", " ou ");
		Divers.remplacer(buf, " and ", " et ");
		Divers.remplacer(buf, " AND ", " et ");
		Divers.remplacer(buf, " or ", " ou ");
		Divers.remplacer(buf, " OR ", " ou ");
		Divers.remplacer(buf, "vrai", "true");
		Divers.remplacer(buf, "VRAI", "true");
		Divers.remplacer(buf, "faux", "false");
		Divers.remplacer(buf, "FAUX", "false");
		Divers.remplacer(buf, "'", "\"");
		Divers.remplacer(buf, "][", ",");
	}
	
	private void postTraitement() {
		Iterator<String> iter = this.les_fichiers.keySet().iterator();
		while (iter.hasNext()) {
			String nom_fich = iter.next();
			StringBuffer buf = this.les_fichiers.get(nom_fich);
			postTraitement(buf);
		}
	}

}
