/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2013.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.langages.algobox;

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
		if (this.isTexte()) {
			Divers.ecrire(buf, nom + " EST_DU_TYPE CHAINE", indent);
		}
		else if (this.isSimple()) {
			Divers.ecrire(buf, nom + " EST_DU_TYPE NOMBRE", indent);
		}
		else if (this.isTabSimple()) {
			Divers.ecrire(buf, nom + " EST_DU_TYPE LISTE", indent);
		}
	}
	
	public void ecrire(ModeleProgramme prog, StringBuffer buf, int indent) {
		this.ecrire((org.javascool.proglets.plurialgo.langages.algobox.Programme)prog, buf, indent);
	}

}
