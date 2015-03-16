/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2013.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.langages.javascript;

import org.javascool.proglets.plurialgo.divers.*;

/**
 * Cette classe hérite de la classe homonyme du modèle.
*/
public class Variable extends org.javascool.proglets.plurialgo.langages.modele.Variable {
	
	public Variable() {
	}
	
	public Variable(String nom, String type) {
		this.nom = nom;
		this.type = type;
		this.mode = null;
	}
	
	public void ecrire(Programme prog, StringBuffer buf, int indent) {
		String MAX_TAB = prog.getMaxTab();
		if (this.isTabSimple() || this.isTabClasse(prog)){
			Divers.ecrire(buf, nom, indent);
			Divers.ecrire(buf, " = Array(MAX_TAB);");
		}
		if (this.isMatSimple() || this.isMatClasse(prog)){
			Divers.ecrire(buf, nom, indent);
			Divers.ecrire(buf, " = Array(MAX_TAB);");
			Divers.ecrire(buf, "for(i1=0; i1<" + MAX_TAB + "; i1++) {", indent);
			Divers.ecrire(buf, this.nom + "[i1]" + " = Array(MAX_TAB); ", indent+1);
			Divers.ecrire(buf, "}", indent);
		}
		if (this.isEnregistrement(prog) || this.isClasse(prog)) {
			Divers.ecrire(buf, nom, indent);
			Divers.ecrire(buf, " = new ");
			Divers.ecrire(buf, this.getClasse(prog).nom);
			Divers.ecrire(buf, "();");
		}
		if (this.isTabClasse(prog)){
			Classe cl = (Classe) this.getClasseOfTab(prog);
			Divers.ecrire(buf, "for(i1=0; i1<" + MAX_TAB + "; i1++) {", indent);
			Divers.ecrire(buf, this.nom + "[i1]" + " = new " + cl.nom + "();", indent+1);
			Divers.ecrire(buf, "}", indent);
		}
	}
	
	public void ecrirePropriete(Programme prog, StringBuffer buf, int indent) {

		String MAX_TAB = prog.getMaxTab();
		if (this.isSimple()){
			Divers.ecrire(buf, "// this." + nom + ";", indent);
		}
		if (this.isTabSimple() || this.isTabClasse(prog)){
			Divers.ecrire(buf, "this." + nom, indent);
			Divers.ecrire(buf, " = Array(MAX_TAB);");
		}
		if (this.isMatSimple() || this.isMatClasse(prog)){
			Divers.ecrire(buf, "this." + nom, indent);
			Divers.ecrire(buf, " = Array(MAX_TAB);");
			Divers.ecrire(buf, "for(i1=0; i1<" + MAX_TAB + "; i1++) {", indent);
			Divers.ecrire(buf, "this." + nom + "[i1]" + " = Array(MAX_TAB); ", indent+1);
			Divers.ecrire(buf, "}", indent);
		}
	}
	
}
