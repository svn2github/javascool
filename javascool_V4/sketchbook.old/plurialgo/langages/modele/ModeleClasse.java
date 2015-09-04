/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2013.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.langages.modele;

import java.util.*;

import org.javascool.proglets.plurialgo.divers.Divers;


/**
 * Cette classe doit être étendue pour chaque langage de programmation implanté.
*/
public class ModeleClasse extends Noeud {	
	
	// variables utilisées par BindingCastor
	/**
	 * Le nom de l'enregistrement ou de la classe.
	 */
	public String nom;
	/**
	 * Les constructeurs de la classe.
	 */
	public ArrayList<ModeleConstructeur> constructeurs;
	/**
	 * Les méthodes de la classe.
	 */
	public ArrayList<ModeleOperation> operations;
	/**
	 * Les champs de la classe.
	 */
	public ArrayList<ModeleVariable> proprietes;
	// autres variables 
	public boolean isRecord;
	
	public ModeleClasse() {
		// variables utilisées par BindingCastor
		constructeurs = new ArrayList<ModeleConstructeur>();
		operations = new ArrayList<ModeleOperation>();
		proprietes = new ArrayList<ModeleVariable>();
		// autres variables 
		isRecord = true;
	}
	
	// ---------------------------------------------
	// Noeuds
	// ---------------------------------------------

	public final void parcoursEnfants() {
		for(Iterator<ModeleOperation> iter=operations.iterator(); iter.hasNext(); ) {
			Noeud nd = (Noeud) iter.next();
			nd.parent = this;
			nd.parcoursEnfants();
		}
		for(Iterator<ModeleConstructeur> iter=constructeurs.iterator(); iter.hasNext(); ) {
			Noeud nd = (Noeud) iter.next();
			nd.parent = this;
			nd.parcoursEnfants();
		}
		for(Iterator<ModeleVariable> iter=proprietes.iterator(); iter.hasNext(); ) {
			Noeud nd = (Noeud) iter.next();
			nd.parent = this;
			nd.parcoursEnfants();
		}
	}

	// ---------------------------------------------
	// fonctions booléennes
	// ---------------------------------------------
	
	public final boolean isClasse() {
		return constructeurs.size()>0 || operations.size()>0 || !isRecord;
	}
	
	public final boolean isEnregistrement() {
		return constructeurs.size()==0 && operations.size()==0 && isRecord;
	}

	// ---------------------------------------------
	// fonctions get
	// ---------------------------------------------

	public final ModeleOperation getOperation(String nom_oper) {
		for (Iterator<ModeleOperation> iter1=this.operations.iterator(); iter1.hasNext();) {
			ModeleOperation oper = iter1.next();
			if (oper.nom.equals(nom_oper)) return oper;
		}
		return null;
	}

	public final ModeleVariable getPropriete(String nom_prop) {
		for (Iterator<ModeleVariable> iter1=this.proprietes.iterator(); iter1.hasNext();) {
			ModeleVariable prop = iter1.next();
			if (prop.nom.equals(nom_prop)) return prop;
		}
		return null;
	}

	// ---------------------------------------------
	// fonctions de comptage
	// ---------------------------------------------
	
	public final int compterMode(String mode) {
		int nb=0;
		for(Iterator<ModeleVariable> iter=proprietes.iterator(); iter.hasNext(); ) {
			ModeleVariable prop = iter.next();
			if (mode.equalsIgnoreCase(prop.mode)) nb++;
		}
		return nb;
	}

	// ---------------------------------------------
	// Xml
	// ---------------------------------------------
	
	void ecrireXml(StringBuffer buf, int indent) {
		Divers.ecrire(buf, "<classe", indent);
		Divers.ecrireAttrXml(buf, "nom", nom);
		Divers.ecrire(buf, ">");
		for (Iterator<ModeleVariable> iter=proprietes.iterator(); iter.hasNext();) {
			ModeleVariable prop = iter.next();
			prop.ecrireProprieteXml(buf, indent+1);
		}
		for (Iterator<ModeleConstructeur> iter=constructeurs.iterator(); iter.hasNext();) {
			ModeleConstructeur constr = iter.next();
			constr.ecrireXml(buf, indent+1);
		}
		for (Iterator<ModeleOperation> iter=operations.iterator(); iter.hasNext();) {
			ModeleOperation oper = iter.next();
			oper.ecrireXml(buf, indent+1);
		}
		Divers.ecrire(buf, "</classe>", indent);
	}	
}
