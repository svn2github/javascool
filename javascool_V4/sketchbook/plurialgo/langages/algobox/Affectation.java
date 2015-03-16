/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2014.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.langages.algobox;

import java.util.StringTokenizer;
import org.javascool.proglets.plurialgo.divers.Divers;
import org.javascool.proglets.plurialgo.langages.modele.*;

/**
 * Cette classe hérite de la classe homonyme du modèle.
*/
public class Affectation extends ModeleAffectation {

	public Affectation() {
	}
	
	public void ecrire(Programme prog, StringBuffer buf, int indent) {
		if (this.isAffTabSimple()) {
			this.ecrireTabSimple(prog, buf, indent);
		}
		else if (this.isAffMatSimple()) {			
		}
		else {
			Divers.ecrire(buf, var + " PREND_LA_VALEUR " + expression, indent);
		}
	}	
	
	private void ecrireTabSimple(Programme prog, StringBuffer buf, int indent) {
		String txt = expression;
		int debut = txt.indexOf("{");
		int fin = txt.lastIndexOf("}");
		txt = txt.substring(debut+1, fin);
		StringTokenizer tok = new StringTokenizer(txt,",");
		int i = 1;
		while (tok.hasMoreTokens()) {
			String elem = tok.nextToken().trim();
			Divers.ecrire(buf, var + "[" + i + "]" + " PREND_LA_VALEUR " + elem, indent);
			i = i+1;
		}
	}
	
}
