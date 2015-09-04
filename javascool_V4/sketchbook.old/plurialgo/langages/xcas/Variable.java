/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2014.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.langages.xcas;

import org.javascool.proglets.plurialgo.divers.Divers;
import org.javascool.proglets.plurialgo.langages.modele.*;


/**
 * Cette classe hérite de la classe homonyme du modèle.
*/
public class Variable extends ModeleVariable {
	
	public Variable() {
	}
	
	public Variable(String nom, String type) {
		this.nom = nom;
		this.type = type;
		this.mode = null;
	}
	
	public void ecrire(Programme prog, StringBuffer buf, int indent) {
		String MAX_TAB = prog.getMaxTab();
		Divers.ecrire(buf,this.nom);
		if (this.isTabSimple()) {
			Divers.ecrire(buf, ":=[]");
		}
		if (this.isMatSimple()) {
			Divers.ecrire(buf, ":=matrix("+MAX_TAB+","+MAX_TAB+")");
		}
	}

}
