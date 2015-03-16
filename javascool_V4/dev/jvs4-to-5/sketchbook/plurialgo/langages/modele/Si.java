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
public class Si extends Noeud {
	
	// variables utilisées par BindingCastor
	/**
	 * La condition de l'alternative.
	 */
	public String condition;
	/**
	 * Instructions internes à l'alternative.
	 */
	public ArrayList<Instruction> instructions;
	// autres variables
	public String schema;	// si, sinonsi, sinon
	
	public Si() {
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
	
	public final boolean isSi() {
		if (schema==null) return false;
		if (schema.equals("si")) return true;
		if (schema.equals("si.si")) return true;
		if (schema.equals("si.selon.si")) return true;
		return false;
	}
	
	public final boolean isSinonSi() {
		if (schema==null) return false;
		if (schema.equals("sinonsi")) return true;
		if (schema.equals("si.sinonsi")) return true;
		if (schema.equals("si.selon.sinonsi")) return true;
		return false;
	}
	
	public final boolean isSinon() {
		if (schema==null) return false;
		if (schema.equals("sinon")) return true;
		if (schema.equals("si.sinon")) return true;
		if (schema.equals("si.selon.sinon")) return true;
		return false;
	}
	
	public final boolean isSelon() {
		if (schema==null) return false;
		if (schema.contains(".selon.")) return true;
		return false;
	}
	
	public final boolean isSelonSinon() {
		if (schema==null) return false;
		if (schema.equals("si.selon.sinon")) return true;
		return false;
	}
	
	// ---------------------------------------------
	// Fonctions get
	// ---------------------------------------------
	
	public final String getVariableSelon() {
		if (condition==null) return null;
		int i = condition.indexOf("=");
		if (i<0) return null;
		return condition.substring(0, i);
	}
	
	public final String getValeurSelon() {
		if (condition==null) return "";
		int i = condition.lastIndexOf("=");
		if (i<0) return condition;
		return condition.substring(i+1);	
	}
	
	public final String getCondition() {
		if (condition==null) return "";
		if (!condition.trim().startsWith("(") || !condition.trim().endsWith(")")) return "("+condition+")"; 
		return condition;
	}
	
	// ---------------------------------------------
	// Xml
	// ---------------------------------------------
	
	void ecrireXml(StringBuffer buf, int indent) {
		Divers.ecrire(buf, "<si", indent);
		Divers.ecrireAttrXml(buf, "condition", condition);
		Divers.ecrire(buf, ">");
		for (Iterator<Instruction> iter=instructions.iterator(); iter.hasNext();) {
			Instruction instr = iter.next();
			instr.ecrireXml(buf, indent+1);
		}
		Divers.ecrire(buf, "</si>", indent);
	}
	
}
