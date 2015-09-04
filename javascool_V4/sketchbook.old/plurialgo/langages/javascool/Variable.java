/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2013.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.langages.javascool;

import org.javascool.proglets.plurialgo.divers.*;
import org.javascool.proglets.plurialgo.langages.modele.*;

/**
 * Cette classe hérite de la classe homonyme du modèle.
 * 
 * @author Raffinat Patrick
*/
public class Variable extends ModeleVariable {
	
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
		prog.ecrireType(buf, this);
		Divers.ecrire(buf, " ");
		Divers.ecrire(buf, nom);
		if (this.isTabEntiers()){
			Divers.ecrire(buf, " = new int[" + MAX_TAB + "]");
		}
		if (this.isTabReels()){
			Divers.ecrire(buf, " = new double[" + MAX_TAB + "]");
		}
		if (this.isTabTextes()){
			Divers.ecrire(buf, " = new String[" + MAX_TAB + "]");
		}
		if (this.isTabBooleens()){
			Divers.ecrire(buf, " = new boolean[" + MAX_TAB + "]");
		}
		if (this.isTabClasse(prog)){
			Classe cl = (Classe) this.getClasseOfTab(prog);
			Divers.ecrire(buf, " = new " + cl.nom + "[" + MAX_TAB + "]");
		}
		if (this.isMatEntiers()){
			Divers.ecrire(buf, " = new int[" + MAX_TAB + "][" + MAX_TAB + "]");
		}
		if (this.isMatReels()){
			Divers.ecrire(buf, " = new double[" + MAX_TAB + "][" + MAX_TAB + "]");
		}
		if (this.isMatTextes()){
			Divers.ecrire(buf, " = new String[" + MAX_TAB + "][" + MAX_TAB + "]");
		}
		if (this.isEnregistrement(prog) || this.isClasse(prog)){
			Divers.ecrire(buf, " = new ");
			Divers.ecrire(buf, this.getClasse(prog).nom);
			Divers.ecrire(buf, "()");
		}
		Divers.ecrire(buf, "; ");
		if (this.isTabClasse(prog)){
			Classe cl = (Classe) this.getClasseOfTab(prog);
			this.addVariable(new Variable("ii","ENTIER"));
			Divers.ecrire(buf, "for(ii=0; ii<" + MAX_TAB + "; ii++) {", indent);
			Divers.ecrire(buf, this.nom + "[ii]" + " = new " + cl.nom + "();", indent+1);
			Divers.ecrire(buf, "}", indent);
		}
	}
	
	public void ecrire(ModeleProgramme prog, StringBuffer buf, int indent) {
		this.ecrire((org.javascool.proglets.plurialgo.langages.javascool.Programme)prog, buf, indent);
	}
	
}
