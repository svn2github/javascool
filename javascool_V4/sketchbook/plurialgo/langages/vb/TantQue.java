/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2013.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.langages.vb;

import java.util.Iterator;
import org.javascool.proglets.plurialgo.divers.Divers;
import org.javascool.proglets.plurialgo.langages.modele.*;

/**
 * Cette classe hérite de la classe homonyme du modèle.
*/
public class TantQue extends ModeleTantQue {

	public void ecrire(Programme prog, StringBuffer buf, int indent) {
		Divers.ecrire(buf, "while " + this.getCondition() + " ", indent);
		for (Iterator<ModeleInstruction> iter=instructions.iterator(); iter.hasNext();) {
			Object obj = iter.next();
			Instruction instr = (Instruction)obj ;
			instr.ecrire(prog, buf, indent+1);
		}
		Divers.ecrire(buf, "wend", indent);
	}
}
