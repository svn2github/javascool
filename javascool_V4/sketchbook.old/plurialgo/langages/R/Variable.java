/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2014.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.langages.R;

import org.javascool.proglets.plurialgo.divers.*;
import java.util.*;
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
		if (this.isTabEntiers() || this.isTabReels()){
			Divers.ecrire(buf, nom + " = numeric(0)", indent);
		}
		if (this.isTabTextes()){
			Divers.ecrire(buf, nom + " = character(0)", indent);
		}
		if (this.isTabBooleens()){
			Divers.ecrire(buf, nom + " = logical(0)", indent);
		}
		if (this.isMatEntiers() || this.isMatReels()){
			Divers.ecrire(buf, nom + " = matrix(0, nrow=" + MAX_TAB + ", ncol=" + MAX_TAB +")", indent);
		}
		if (this.isMatTextes()){
			Divers.ecrire(buf, nom + " = matrix('', nrow=" + MAX_TAB + ", ncol=" + MAX_TAB +")", indent);
		}
		if (this.isMatBooleens()){
			Divers.ecrire(buf, nom + " = matrix(F, nrow=" + MAX_TAB + ", ncol=" + MAX_TAB +")", indent);
		}
		if (this.isEnregistrement(prog)) {
			Classe cl = (Classe) this.getClasse(prog);
			Divers.ecrire(buf, nom, indent);
			Divers.ecrire(buf, " = list(");
			for (Iterator<ModeleVariable> iter=cl.proprietes.iterator(); iter.hasNext();) {
				Variable prop = (Variable) iter.next();
				prop.ecrireProprieteEnreg(prog, buf, -1);
				if (iter.hasNext()) Divers.ecrire(buf,", ");
			}
			Divers.ecrire(buf, ")");
		}
		if (this.isClasse(prog)) {
			Classe cl = (Classe) this.getClasse(prog);
			Divers.ecrire(buf, nom, indent);
			Divers.ecrire(buf, " = new(");
			Divers.ecrire(buf, prog.quote(cl.nom));
			for (Iterator<ModeleVariable> iter=cl.proprietes.iterator(); iter.hasNext();) {
				Variable prop = (Variable) iter.next();
				Divers.ecrire(buf,", ");
				prop.ecrireProprieteEnreg(prog, buf, -1);
			}
			Divers.ecrire(buf, ")");
		}
		if (this.isTabClasse(prog)){
		}
	}
	
	public void ecrire(ModeleProgramme prog, StringBuffer buf, int indent) {
		this.ecrire((org.javascool.proglets.plurialgo.langages.R.Programme)prog, buf, indent);
	}
	
	private void ecrireProprieteEnreg(Programme prog, StringBuffer buf, int indent) {
		if (this.isEntier() || this.isReel()){
			Divers.ecrire(buf, nom + " = 0", indent);
		}
		else if (this.isTexte()){
			Divers.ecrire(buf, nom + " = ''", indent);
		}
		else if (this.isBooleen()){
			Divers.ecrire(buf, nom + " = F", indent);
		}
		else {
			this.ecrire(prog, buf, indent);
		}
	}
	
	void ecrireRepresentation(Programme prog, StringBuffer buf, int indent) {
		if (this.isEntier() || this.isReel()){
			Divers.ecrire(buf, nom + " = " + prog.quote("numeric"), indent);
		}
		else if (this.isTexte()){
			Divers.ecrire(buf, nom + " = " + prog.quote("character"), indent);
		}
		else if (this.isBooleen()){
			Divers.ecrire(buf, nom + " = " + prog.quote("logical"), indent);
		}
		else if (this.isTabEntiers() || this.isTabReels()){
			Divers.ecrire(buf, nom + " = " + prog.quote("numeric"), indent);
		}
		else if (this.isTabTextes()){
			Divers.ecrire(buf, nom + " = " + prog.quote("character"), indent);
		}
		else if (this.isTabBooleens()){
			Divers.ecrire(buf, nom + " = " + prog.quote("logical"), indent);
		}
		else if (this.isMatEntiers() || this.isMatReels()){
			Divers.ecrire(buf, nom + " = " + prog.quote("matrix"), indent);
		}
		else if (this.isMatTextes()){
			Divers.ecrire(buf, nom + " = " + prog.quote("matrix"), indent);
		}
		else if (this.isMatBooleens()){
			Divers.ecrire(buf, nom + " = " + prog.quote("matrix"), indent);
		}
		else if (this.isEnregistrement(prog)) {
			Divers.ecrire(buf, nom + " = " + prog.quote("list"), indent);
		}
	}
	
}
