/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2014.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.langages.R;

import org.javascool.proglets.plurialgo.divers.Divers;
import org.javascool.proglets.plurialgo.langages.modele.*;

/**
 * Cette classe hérite de la classe homonyme du modèle.
*/
public class Affectation extends ModeleAffectation {

	public void ecrire(Programme prog, StringBuffer buf, int indent) {
		if (this.isAffTabSimple()) {
			this.ecrireTabSimple(prog, buf, indent);
		}
		else if (this.isAffMatSimple()) {
			this.ecrireMatSimple(prog, buf, indent);
		}
		else {
			Divers.ecrire(buf, var + "=" + expression + " ", indent);
		}
	}
	
	private void ecrireTabSimple(Programme prog, StringBuffer buf, int indent) {
		String txt = expression;
		int debut = txt.indexOf("{");
		int fin = txt.lastIndexOf("}");
		txt = txt.substring(debut+1, fin);
		txt = "c(" + txt + ")";
		Divers.ecrire(buf, var + "=" + txt + " ", indent);
	}
	
	private void ecrireMatSimple(Programme prog, StringBuffer buf, int indent) {
		String txt = expression;
		int debut = txt.indexOf("[");
		int fin = txt.lastIndexOf("]");
		txt = txt.substring(debut+1, fin);
		txt = Divers.remplacer(txt, "[", "c(");
		txt = Divers.remplacer(txt, "]", ")");
		txt = "rbind(" + txt + ")";
		Divers.ecrire(buf, var + "=" + txt + " ", indent);
	}
	
}
