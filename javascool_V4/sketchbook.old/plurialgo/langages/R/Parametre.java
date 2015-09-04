/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2014.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.langages.R;

import org.javascool.proglets.plurialgo.divers.*;
import org.javascool.proglets.plurialgo.langages.modele.*;

/**
 * Cette classe hérite de la classe homonyme du modèle.
*/
public class Parametre extends ModeleParametre {
	
	public Parametre() {
	}
	
	void ecrire(Programme prog, StringBuffer buf) {
		Divers.ecrire(buf, nom);
	}
	
}
