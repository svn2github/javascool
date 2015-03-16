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
public class ModeleTantQue extends Noeud {
	
	// variables utilisées par BindingCastor
	/**
	 * La condition de la boucle.
	 */
	public String condition;
	/**
	 * Instructions internes à la boucle.
	 */
	public ArrayList<ModeleInstruction> instructions;
	// autres variables
	/**
	 * Bientôt obsolète ?
	 */
	public String schema;	
	
	public ModeleTantQue() {
		instructions = new ArrayList<ModeleInstruction>();
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
	// Autres fonctions
	// ---------------------------------------------

	public final boolean isTantQue() {
		return "tantque".equals(schema);
	}
	
	public String getCondition() {
		if (condition==null) return "";
		if (!condition.trim().startsWith("(") && !condition.trim().endsWith(")")) return "("+condition+")"; 
		return condition;
	}
	
	// ---------------------------------------------
	// Xml
	// ---------------------------------------------
	
	void ecrireXml(StringBuffer buf, int indent) {
		Divers.ecrire(buf, "<tantque", indent);
		Divers.ecrireAttrXml(buf, "condition", condition);
		Divers.ecrire(buf, ">");
		for (Iterator<ModeleInstruction> iter=instructions.iterator(); iter.hasNext();) {
			ModeleInstruction instr = iter.next();
			instr.ecrireXml(buf, indent+1);
		}
		Divers.ecrire(buf, "</tantque>", indent);
	}

}
