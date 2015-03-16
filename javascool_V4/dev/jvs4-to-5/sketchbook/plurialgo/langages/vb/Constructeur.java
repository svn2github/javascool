/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2013.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.langages.vb;

import java.util.*;

import org.javascool.proglets.plurialgo.divers.*;



/**
 * Cette classe hérite de la classe homonyme du modèle.
*/
public class Constructeur extends org.javascool.proglets.plurialgo.langages.modele.Constructeur {

	public Constructeur() {
	}
	
	public void ecrire(Programme prog, StringBuffer buf, int indent) {
		if (this.parametres.size()>0) return;
		Divers.indenter(buf, indent);
		Divers.ecrire(buf, "Private Sub Class_Initialize()");
		for (Iterator<org.javascool.proglets.plurialgo.langages.modele.Variable> iter=variables.iterator(); iter.hasNext();) {
			Variable var = (Variable) iter.next();
			var.ecrire(prog, buf, indent+1);
		}
		for (Iterator<org.javascool.proglets.plurialgo.langages.modele.Instruction> iter=instructions.iterator(); iter.hasNext();) {
			Instruction instr = (Instruction) iter.next();
			instr.ecrire(prog, buf, indent+1);
		}
		Divers.ecrire(buf, "End Sub ", indent);
	}
}
