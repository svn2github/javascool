/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2013.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.langages.python;

import org.javascool.proglets.plurialgo.divers.Divers;

/**
 * Cette classe hérite de la classe homonyme du modèle.
*/
public class Affectation extends org.javascool.proglets.plurialgo.langages.modele.Affectation {

	public void ecrire(Programme prog, StringBuffer buf, int indent) {
		Divers.ecrire(buf, var + "=" + expression + " ", indent);
	}
	
}
