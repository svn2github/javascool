/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2013.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.langages.modele;

import java.util.ArrayList;
import java.util.Iterator;

import org.javascool.proglets.plurialgo.divers.Divers;


/**
 * Cette classe doit être étendue pour chaque langage de programmation implanté.
*/
public class Constructeur extends Noeud {
	
	// variables utilisées par BindingCastor
	/**
	 * Le nom du constructeur.
	 */
	public String nom;
	/**
	 * Les paramètres du constructeur.
	 */
	public ArrayList<Parametre> parametres;
	/**
	 * Les instructions du constructeur.
	 */
	public ArrayList<Instruction> instructions;
	/**
	 * Les variables locales du constructeur.
	 */
	public ArrayList<Variable> variables;
	
	public Constructeur() {
		parametres = new ArrayList<Parametre>();
		instructions = new ArrayList<Instruction>();
		variables = new ArrayList<Variable>();
	}
	
	// ---------------------------------------------
	// Noeuds
	// ---------------------------------------------

	public final void parcoursEnfants() {
		for(Iterator<Instruction> iter=instructions.iterator(); iter.hasNext(); ) {
			Noeud nd = (Noeud) iter.next();
			nd.parent = this;
			nd.parcoursEnfants();
		}
	}
	
	// ---------------------------------------------
	// Xml
	// ---------------------------------------------

	void ecrireXml(StringBuffer buf, int indent) {
		Divers.ecrire(buf, "<constructeur", indent);
		Divers.ecrireAttrXml(buf, "nom", nom);
		Divers.ecrire(buf, ">");
		for (Iterator<Parametre> iter=parametres.iterator(); iter.hasNext();) {
			Parametre param = iter.next();
			param.ecrireXml(buf, indent+1);
		}
		for (Iterator<Variable> iter=variables.iterator(); iter.hasNext();) {
			Variable var = iter.next();
			var.ecrireXml(buf, indent+1);
		}
		for (Iterator<Instruction> iter=instructions.iterator(); iter.hasNext();) {
			Instruction instr = iter.next();
			instr.ecrireXml(buf, indent+1);
		}
		Divers.ecrire(buf, "</constructeur>", indent);
	}
		
}

