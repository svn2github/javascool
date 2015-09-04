/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2013.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.langages.modele;

import java.util.*;

/**
 * Cette classe permet de gérer les liens bidirectionnels dans l'arborescence d'un programme.
*/
public abstract class Noeud {
	
	/**
	 * Le parent du noeud.
	 */
	public Noeud parent;
	
	/**
	 * Ajoute les liens montants pour tous les descendants du noeud.
	 */
	public abstract void parcoursEnfants();	

	/**
	 * Ajoute une variable dans le plus proche parent le permettant 
	 * (Operation, Constructeur ou Programme).
	 * @param var : la variable à ajouter
	 */
	public final void addVariable(ModeleVariable var) {
		Noeud nd = this;
		ArrayList<ModeleVariable> vars = null;
		while (nd!=null) {
			if (nd instanceof ModeleProgramme) {
				vars = ((ModeleProgramme)nd).variables;
				break;
			}
			if (nd instanceof ModeleOperation) {
				vars = ((ModeleOperation)nd).variables;
				break;
			}
			if (nd instanceof ModeleConstructeur) {
				vars = ((ModeleConstructeur)nd).variables;
				break;
			}
			nd = nd.parent;
		}
		if (nd==null) return;
		for(Iterator<ModeleVariable> iter=vars.iterator(); iter.hasNext(); ) {
			ModeleVariable var1 = iter.next();
			if (var.nom.equalsIgnoreCase(var1.nom)) return;
		}
		vars.add(0,var);
	}
}
