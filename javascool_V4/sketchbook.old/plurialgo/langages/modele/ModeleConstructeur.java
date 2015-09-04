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
public class ModeleConstructeur extends Noeud {
	
	// variables utilisées par BindingCastor
	/**
	 * Le nom du constructeur.
	 */
	public String nom;
	/**
	 * Les paramètres du constructeur.
	 */
	public ArrayList<ModeleParametre> parametres;
	/**
	 * Les instructions du constructeur.
	 */
	public ArrayList<ModeleInstruction> instructions;
	/**
	 * Les variables locales du constructeur.
	 */
	public ArrayList<ModeleVariable> variables;
	
	public ModeleConstructeur() {
		parametres = new ArrayList<ModeleParametre>();
		instructions = new ArrayList<ModeleInstruction>();
		variables = new ArrayList<ModeleVariable>();
	}
	
	// ---------------------------------------------
	// Noeuds
	// ---------------------------------------------

	public final void parcoursEnfants() {
		for(Iterator<ModeleInstruction> iter=instructions.iterator(); iter.hasNext(); ) {
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
		for (Iterator<ModeleParametre> iter=parametres.iterator(); iter.hasNext();) {
			ModeleParametre param = iter.next();
			param.ecrireXml(buf, indent+1);
		}
		for (Iterator<ModeleVariable> iter=variables.iterator(); iter.hasNext();) {
			ModeleVariable var = iter.next();
			var.ecrireXml(buf, indent+1);
		}
		for (Iterator<ModeleInstruction> iter=instructions.iterator(); iter.hasNext();) {
			ModeleInstruction instr = iter.next();
			instr.ecrireXml(buf, indent+1);
		}
		Divers.ecrire(buf, "</constructeur>", indent);
	}
		
}

