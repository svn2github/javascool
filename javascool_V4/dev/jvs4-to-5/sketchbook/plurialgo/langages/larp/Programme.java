/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2013.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.langages.larp;

import java.util.*;

import org.javascool.proglets.plurialgo.divers.*;


/**
 * Cette classe hérite de la classe homonyme du modèle.
*/
public class Programme extends org.javascool.proglets.plurialgo.langages.modele.Programme {
	
	public void ecrire() {
		StringBuffer buf = new StringBuffer();
		String nom_fich = nom + ".txt";
		les_fichiers.put(nom_fich, buf);
		ecrire(buf);
	}
	
	private void ecrire(StringBuffer buf) {
		this.commenter(buf, "Module principal", 0);
		Divers.ecrire(buf, "DEBUT", 0);
		this.addClasses(buf, 0);
		this.addSousProgs(buf, 0);
		this.addMain(buf, 1);
		Divers.ecrire(buf, "FIN", 0);
		this.postTraitement();
	}
	
	private void addSousProgs(StringBuffer buf, int indent) {
		for (Iterator<org.javascool.proglets.plurialgo.langages.modele.Operation> iter=operations.iterator(); iter.hasNext();) {
			Operation oper = (Operation) iter.next();
			StringBuffer buf1 = new StringBuffer();
			this.les_fichiers.put(oper.nom+".txt", buf1);
			oper.ecrire(this, buf1, indent);
		}
	}
	
	private void addClasses(StringBuffer buf, int indent) {
		if ((classes.size()>0))
			this.ecrireWarning("Pas d'objets ou d'enregistrements avec ce langage");
	}
	
	private void addMain(StringBuffer buf, int indent) {
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
		Divers.ecrire(fich, "\\\\ " + texte, indent);
	}
	
	public String quote(String s) {
		return "\"" + s + "\"";
	}
	
	public String getMaxTab() {
		return "5";
	}
	
	private void postTraitement(StringBuffer buf) {
		Divers.remplacer(buf, "==", "=");
		Divers.remplacer(buf, " et ", " ET ");
		//Divers.remplacer(buf, " ET ", "&&");
		Divers.remplacer(buf, " ou ", " OU ");
		//Divers.remplacer(buf, " OU ", "||");
		Divers.remplacer(buf, "true", "1");
		Divers.remplacer(buf, "vrai", "1");
		Divers.remplacer(buf, "VRAI", "1");
		Divers.remplacer(buf, "false", "0");
		Divers.remplacer(buf, "faux", "0");
		Divers.remplacer(buf, "FAUX", "0");
		//Divers.remplacer(buf, "][", "][");
		//Divers.remplacer(buf, "[", "[");
		//Divers.remplacer(buf, "[", "[");
		//Divers.remplacer(buf, "'", "\"");
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
