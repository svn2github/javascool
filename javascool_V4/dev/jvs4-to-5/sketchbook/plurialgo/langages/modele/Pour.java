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
public class Pour extends Noeud {
	
	// variables utilisées par BindingCastor
	/**
	 * Valeur de début de la boucle.
	 */
	public String debut; 
	/**
	 * Valeur de fin de la boucle.
	 */
	public String fin;
	/**
	 * Pas de la boucle.
	 */
	public String pas; 
	/**
	 * Indice de la boucle Pour.
	 */
	public String var;
	/**
	 * Instructions internes à la boucle.
	 */
	public ArrayList<Instruction> instructions;
	// autres variables
	/**
	 * Bientôt obsolète ?
	 */
	public String schema;	// pour, pour.tantque
	
	public Pour() {
		instructions = new ArrayList<Instruction>();
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
	// Fonctions booléennes
	// ---------------------------------------------
	
	public final boolean isPour() {
		if (schema==null) return true;
		if (schema.equals("pour")) return true;
		return false;
	}
	
	public final boolean isTantQue() {
		if (schema==null) return false;
		if (schema.equals("tantque")) return true;
		if (schema.endsWith(".tantque")) return true;
		return false;
	}
	
	// ---------------------------------------------
	// Xml
	// ---------------------------------------------
	
	void ecrireXml(StringBuffer buf, int indent) {
		Divers.ecrire(buf, "<pour", indent);
		Divers.ecrireAttrXml(buf, "var", var);
		Divers.ecrireAttrXml(buf, "debut", debut);
		Divers.ecrireAttrXml(buf, "fin", fin);
		Divers.ecrireAttrXml(buf, "pas", pas);
		Divers.ecrire(buf, ">");
		for (Iterator<Instruction> iter=instructions.iterator(); iter.hasNext();) {
			Instruction instr = iter.next();
			instr.ecrireXml(buf, indent+1);
		}
		Divers.ecrire(buf, "</pour>", indent);
	}
	
}
