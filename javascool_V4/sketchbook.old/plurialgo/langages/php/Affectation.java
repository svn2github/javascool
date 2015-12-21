/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2013.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.langages.php;

import org.javascool.proglets.plurialgo.divers.Divers;
import org.javascool.proglets.plurialgo.langages.modele.*;

/**
 * Cette classe hérite de la classe homonyme du modèle.
*/
public class Affectation extends ModeleAffectation {

	public void ecrire(Programme prog, StringBuffer buf, int indent) {
		if (this.isAffTabSimple() || this.isAffMatSimple()) {
			expression = Divers.remplacer(expression, "{", "array(");
			expression = Divers.remplacer(expression, "}", ")");
		}
		Divers.ecrire(buf, "$" + var + "=" + expression + ";", indent);
	}
	
}