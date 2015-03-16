/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2013.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.langages.python;

import java.util.Iterator;
import org.javascool.proglets.plurialgo.divers.*;
import org.javascool.proglets.plurialgo.langages.modele.*;


/**
 * Cette classe hérite de la classe homonyme du modèle.
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
		if (this.isTabEntiers()){
			Divers.ecrire(buf, nom + " = [0]*" + MAX_TAB, indent);
		}
		if (this.isTabReels()){
			Divers.ecrire(buf, nom + " = [0]*" + MAX_TAB, indent);
		}
		if (this.isTabTextes()){
			Divers.ecrire(buf, nom + " = ['']*" + MAX_TAB, indent);
		}
		if (this.isTabBooleens()){
			Divers.ecrire(buf, nom + " = [True]*" + MAX_TAB, indent);
		}
		if (this.isMatEntiers()){
			// [[0 for j1 in range(10)] for i1 in range(10)]
			Divers.ecrire(buf, nom + " = [[0 for j1 in range(" + MAX_TAB + ")] for i1 in range(" + MAX_TAB + ")]", indent);
		}
		if (this.isMatReels()){
			Divers.ecrire(buf, nom + " = [[0 for j1 in range(" + MAX_TAB + ")] for i1 in range(" + MAX_TAB + ")]", indent);
		}
		if (this.isMatTextes()){
			Divers.ecrire(buf, nom + " = [['' for j1 in range(" + MAX_TAB + ")] for i1 in range(" + MAX_TAB + ")]", indent);
		}
		if (this.isMatBooleens()){
			Divers.ecrire(buf, nom + " = [[True for j1 in range(" + MAX_TAB + ")] for i1 in range(" + MAX_TAB + ")]", indent);
		}
		if (this.isEnregistrement(prog)) {
			Divers.ecrire(buf, nom, indent);
			Divers.ecrire(buf, " = {}");
			Classe cl = (Classe) this.getClasse(prog);
			for (Iterator<ModeleVariable> iter=cl.proprietes.iterator(); iter.hasNext();) {
				Variable prop = (Variable) iter.next();
				Variable var = new Variable(this.nom+"."+prop.nom, prop.type);
				var.ecrire(prog, buf, indent);
			}
		}
		if (this.isClasse(prog)) {
			Divers.ecrire(buf, nom, indent);
			Divers.ecrire(buf, " = "+ this.getClasse(prog).nom + "()");
		}
		if (this.isTabClasse(prog)){
			Classe cl = (Classe) this.getClasseOfTab(prog);
			if (cl.isClasse()) {
				Divers.ecrire(buf, nom + " = [0]*" + MAX_TAB, indent);
				Divers.ecrire(buf, "for ii in range(0," + MAX_TAB + ") :", indent);
				Divers.ecrire(buf, this.nom + "[ii]" + " = " + cl.nom + "()", indent+1);
				Divers.ecrire(buf, "#end", indent);
			}
			else {
				Divers.ecrire(buf, nom + " = [0]*" + MAX_TAB, indent);
				Divers.ecrire(buf, "for ii in range(0," + MAX_TAB + ") :", indent);
				Variable var = new Variable(this.nom + "[ii]", cl.nom);
				var.ecrire(prog, buf, indent+1);
				Divers.ecrire(buf, "#end", indent);
			}
		}
	}
	
	public void ecrirePropriete(Programme prog, StringBuffer buf, int indent) {
		String MAX_TAB = prog.getMaxTab();
		if (this.isSimple()){
			Divers.ecrire(buf, "#self." + nom, indent);
		}
		if (this.isTabEntiers() || this.isTabReels()){
			Divers.ecrire(buf,  "self." + nom + " = [0]*" + MAX_TAB, indent);
		}
		if (this.isTabTextes()){
			Divers.ecrire(buf, "self." + nom + " = ['']*" + MAX_TAB, indent);
		}
		if (this.isTabBooleens()){
			Divers.ecrire(buf, "self." + nom + " = [True]*" + MAX_TAB, indent);
		}
		if (this.isMatEntiers() || this.isMatReels()){
			Divers.ecrire(buf, "self." + nom + " = []", indent);
			Divers.ecrire(buf, "for i1 in range(0," + MAX_TAB + ") :", indent);
			Divers.ecrire(buf, this.nom + ".append(" + "[0]*" + MAX_TAB + ")", indent+1);
			Divers.ecrire(buf, "#end", indent);
		}
		if (this.isMatTextes()){
			Divers.ecrire(buf, "self." + nom + " = []", indent);
			Divers.ecrire(buf, "for i1 in range(0," + MAX_TAB + ") :", indent);
			Divers.ecrire(buf, this.nom + ".append(" + "['']*" + MAX_TAB + ")", indent+1);
			Divers.ecrire(buf, "#end", indent);
		}
		if (this.isMatBooleens()){
			Divers.ecrire(buf, "self." + nom + " = []", indent);
			Divers.ecrire(buf, "for i1 in range(0," + MAX_TAB + ") :", indent);
			Divers.ecrire(buf, this.nom + ".append(" + "[True]*" + MAX_TAB + ")", indent+1);
			Divers.ecrire(buf, "#end", indent);
		}
		if (this.isClasse(prog)) {
			Divers.ecrire(buf, "self." + nom, indent);
			Divers.ecrire(buf, " = "+ this.getClasse(prog).nom + "()");
		}
		if (this.isEnregistrement(prog)) {
			Divers.ecrire(buf, "self." + nom, indent);
			Divers.ecrire(buf, " = {}");
			Classe cl = (Classe) this.getClasse(prog);
			for (Iterator<ModeleVariable> iter=cl.proprietes.iterator(); iter.hasNext();) {
				Variable prop = (Variable) iter.next();
				Variable var = new Variable("self." + this.nom+"."+prop.nom, prop.type);
				var.ecrire(prog, buf, indent);
			}
		}
	}
	
}
