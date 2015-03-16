/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2013.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.langages.python;

import java.util.Iterator;

import org.javascool.proglets.plurialgo.divers.Divers;


/**
 * Cette classe hérite de la classe homonyme du modèle.
*/
public class TantQue extends org.javascool.proglets.plurialgo.langages.modele.TantQue {

	public void ecrire(Programme prog, StringBuffer buf, int indent) {
			Divers.ecrire(buf, "while " + this.getCondition() + " :", indent);
			for (Iterator<org.javascool.proglets.plurialgo.langages.modele.Instruction> iter=instructions.iterator(); iter.hasNext();) {
				Object obj = iter.next();
				Instruction instr = (Instruction)obj ;
				instr.ecrire(prog, buf, indent+1);
			}
			Divers.ecrire(buf, "#end while", indent);
	}
	
}
