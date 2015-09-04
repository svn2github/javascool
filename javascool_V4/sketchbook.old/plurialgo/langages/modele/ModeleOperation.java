/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2013.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.langages.modele;

import java.util.*;

import org.javascool.proglets.plurialgo.divers.Divers;



/**
 * Cette classe doit être étendue pour chaque langage de programmation implanté.
*/
public class ModeleOperation extends Noeud {
	
	// variables utilisées par BindingCastor	
	/*
	 * Le nom du sous-programme.
	 */
	public String nom;
	/**
	 * Les paramètres du sous-programme.
	 */
	public ArrayList<ModeleParametre> parametres;
	/**
	 * Les instructions du sous-programme.
	 */
	public ArrayList<ModeleInstruction> instructions;
	/**
	 * Les variables locales du sous-programme.
	 */
	public ArrayList<ModeleVariable> variables;
	/**
	 * Variable de retour (si fonction).
	 */
	public ArrayList<ModeleVariable> retours;	// 0 ou 1 Variable
	// autres variables
	public ModeleClasse classe;
	
	public ModeleOperation() {
		parametres = new ArrayList<ModeleParametre>();
		instructions = new ArrayList<ModeleInstruction>();
		variables = new ArrayList<ModeleVariable>();
		retours = new ArrayList<ModeleVariable>();
	}
	
	public void ecrire(ModeleProgramme prog, StringBuffer buf, int indent) {
		// à redéfinir pour chaque langage
	}
	
	// ---------------------------------------------
	// Noeuds
	// ---------------------------------------------

	public void parcoursEnfants() {
		for(Iterator<ModeleInstruction> iter=instructions.iterator(); iter.hasNext(); ) {
			Noeud nd = (Noeud) iter.next();
			nd.parent = this;
			nd.parcoursEnfants();
		}
	}
	
	// ---------------------------------------------
	// variable de retour
	// ---------------------------------------------
	
	public final ModeleVariable getRetour() {
		if (retours.size()==0) return null;
		return retours.get(0);
	}
	
	public final void setRetour(ModeleVariable var) {
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
		for (Iterator<ModeleParametre> iter=parametres.iterator(); iter.hasNext();) {
			ModeleParametre param = iter.next();
			param.ecrireXml(buf, indent+1);
		}
		for (Iterator<ModeleVariable> iter=retours.iterator(); iter.hasNext();) {
			ModeleVariable retour = iter.next();
			retour.ecrireRetourXml(buf, indent+1);
		}
		for (Iterator<ModeleVariable> iter=variables.iterator(); iter.hasNext();) {
			ModeleVariable var = iter.next();
			var.ecrireXml(buf, indent+1);
		}
		for (Iterator<ModeleInstruction> iter=instructions.iterator(); iter.hasNext();) {
			ModeleInstruction instr = iter.next();
			instr.ecrireXml(buf, indent+1);
		}
		Divers.ecrire(buf, "</operation>", indent);
	}
	
}
