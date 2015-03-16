/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2013.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.langages.vb;

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
		Divers.indenter(buf, indent);
		Divers.ecrire(buf, "Dim ");
		Divers.ecrire(buf, nom);
		Divers.ecrire(buf, " ");
		prog.ecrireType(buf, this);
		if (this.isClasse(prog)){
			Divers.indenter(buf, indent);
			Divers.ecrire(buf, "Set ");
			Divers.ecrire(buf, nom);
			Divers.ecrire(buf, " = new ");
			Divers.ecrire(buf, this.getClasse(prog).nom);
		}
		if (this.isTabClasse(prog)){
			Classe cl = (Classe) this.getClasseOfTab(prog);
			if (cl.isClasse()) {
				this.addVariable(new Variable("i1", "ENTIER"));
				Divers.ecrire(buf, "for i1=0 to " + MAX_TAB + "-1", indent);
				Divers.ecrire(buf, "Set " + this.nom + "[i1]" + " = new " + cl.nom, indent+1);
				Divers.ecrire(buf, "next i1", indent);
			}
		}
	}
	
	public void ecrirePropClasse(Programme prog, StringBuffer buf, int indent) {
		Divers.indenter(buf, indent);
		Divers.ecrire(buf, "public ");
		Divers.ecrire(buf, nom);
		Divers.ecrire(buf, " ");
		prog.ecrireType(buf, this);
	}
	
	public void ecrirePropEnregistrement(Programme prog, StringBuffer buf, int indent) {
		Divers.indenter(buf, indent);
		Divers.ecrire(buf, nom);
		Divers.ecrire(buf, " ");
		prog.ecrireType(buf, this);
	}
	
}
