/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2013.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.langages.modele;

import java.util.*;

import org.javascool.proglets.plurialgo.divers.Divers;


/**
 * Cette classe doit être étendue pour chaque langage de programmation implanté.
*/
public class Classe extends Noeud {	
	
	// variables utilisées par BindingCastor
	/**
	 * Le nom de l'enregistrement ou de la classe.
	 */
	public String nom;
	/**
	 * Les constructeurs de la classe.
	 */
	public ArrayList<Constructeur> constructeurs;
	/**
	 * Les méthodes de la classe.
	 */
	public ArrayList<Operation> operations;
	/**
	 * Les champs de la classe.
	 */
	public ArrayList<Variable> proprietes;
	// autres variables 
	public boolean isRecord;
	
	public Classe() {
		// variables utilisées par BindingCastor
		constructeurs = new ArrayList<Constructeur>();
		operations = new ArrayList<Operation>();
		proprietes = new ArrayList<Variable>();
		// autres variables 
		isRecord = true;
	}
	
	// ---------------------------------------------
	// Noeuds
	// ---------------------------------------------

	public final void parcoursEnfants() {
		for(Iterator<Operation> iter=operations.iterator(); iter.hasNext(); ) {
			Noeud nd = (Noeud) iter.next();
			nd.parent = this;
			nd.parcoursEnfants();
		}
		for(Iterator<Constructeur> iter=constructeurs.iterator(); iter.hasNext(); ) {
			Noeud nd = (Noeud) iter.next();
			nd.parent = this;
			nd.parcoursEnfants();
		}
		for(Iterator<Variable> iter=proprietes.iterator(); iter.hasNext(); ) {
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

	public final Operation getOperation(String nom_oper) {
		for (Iterator<Operation> iter1=this.operations.iterator(); iter1.hasNext();) {
			Operation oper = iter1.next();
			if (oper.nom.equals(nom_oper)) return oper;
		}
		return null;
	}

	public final Variable getPropriete(String nom_prop) {
		for (Iterator<Variable> iter1=this.proprietes.iterator(); iter1.hasNext();) {
			Variable prop = iter1.next();
			if (prop.nom.equals(nom_prop)) return prop;
		}
		return null;
	}

	// ---------------------------------------------
	// fonctions de comptage
	// ---------------------------------------------
	
	public final int compterMode(String mode) {
		int nb=0;
		for(Iterator<Variable> iter=proprietes.iterator(); iter.hasNext(); ) {
			Variable prop = iter.next();
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
		for (Iterator<Variable> iter=proprietes.iterator(); iter.hasNext();) {
			Variable prop = iter.next();
			prop.ecrireProprieteXml(buf, indent+1);
		}
		for (Iterator<Constructeur> iter=constructeurs.iterator(); iter.hasNext();) {
			Constructeur constr = iter.next();
			constr.ecrireXml(buf, indent+1);
		}
		for (Iterator<Operation> iter=operations.iterator(); iter.hasNext();) {
			Operation oper = iter.next();
			oper.ecrireXml(buf, indent+1);
		}
		Divers.ecrire(buf, "</classe>", indent);
	}	
}
