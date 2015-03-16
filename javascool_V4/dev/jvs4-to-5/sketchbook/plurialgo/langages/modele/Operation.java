/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2013.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.langages.modele;

import java.util.*;

import org.javascool.proglets.plurialgo.divers.Divers;



/**
 * Cette classe doit être étendue pour chaque langage de programmation implanté.
*/
public class Operation extends Noeud {
	
	// variables utilisées par BindingCastor	
	/*
	 * Le nom du sous-programme.
	 */
	public String nom;
	/**
	 * Les paramètres du sous-programme.
	 */
	public ArrayList<Parametre> parametres;
	/**
	 * Les instructions du sous-programme.
	 */
	public ArrayList<Instruction> instructions;
	/**
	 * Les variables locales du sous-programme.
	 */
	public ArrayList<Variable> variables;
	/**
	 * Variable de retour (si fonction).
	 */
	public ArrayList<Variable> retours;	// 0 ou 1 Variable
	// autres variables
	public Classe classe;
	
	public Operation() {
		parametres = new ArrayList<Parametre>();
		instructions = new ArrayList<Instruction>();
		variables = new ArrayList<Variable>();
		retours = new ArrayList<Variable>();
	}
	
	// ---------------------------------------------
	// Noeuds
	// ---------------------------------------------

	public void parcoursEnfants() {
		for(Iterator<Instruction> iter=instructions.iterator(); iter.hasNext(); ) {
			Noeud nd = (Noeud) iter.next();
			nd.parent = this;
			nd.parcoursEnfants();
		}
	}
	
	// ---------------------------------------------
	// variable de retour
	// ---------------------------------------------
	
	public final Variable getRetour() {
		if (retours.size()==0) return null;
		return retours.get(0);
	}
	
	public final void setRetour(Variable var) {
		retours.clear();
		retours.add(var);
	}
	
	// ---------------------------------------------
	// fonctions boolennes
	// ---------------------------------------------

	public final boolean isFonction() {
		return getRetour()!=null;
	}
	
	public final boolean isProcedure() {
		return getRetour()==null;
	}
	
	// ---------------------------------------------
	// Xml
	// ---------------------------------------------
	
	void ecrireXml(StringBuffer buf, int indent) {
		Divers.ecrire(buf, "<operation", indent);
		Divers.ecrireAttrXml(buf, "nom", nom);
		Divers.ecrire(buf, ">");
		for (Iterator<Parametre> iter=parametres.iterator(); iter.hasNext();) {
			Parametre param = iter.next();
			param.ecrireXml(buf, indent+1);
		}
		for (Iterator<Variable> iter=retours.iterator(); iter.hasNext();) {
			Variable retour = iter.next();
			retour.ecrireRetourXml(buf, indent+1);
		}
		for (Iterator<Variable> iter=variables.iterator(); iter.hasNext();) {
			Variable var = iter.next();
			var.ecrireXml(buf, indent+1);
		}
		for (Iterator<Instruction> iter=instructions.iterator(); iter.hasNext();) {
			Instruction instr = iter.next();
			instr.ecrireXml(buf, indent+1);
		}
		Divers.ecrire(buf, "</operation>", indent);
	}
	
}
