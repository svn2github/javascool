/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2014.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.langages.algobox;

import java.util.Iterator;
import org.javascool.proglets.plurialgo.divers.Divers;
import org.javascool.proglets.plurialgo.langages.modele.*;


/**
 * Cette classe hérite de la classe homonyme du modèle.
*/
public class TantQue extends ModeleTantQue {

	public void ecrire(Programme prog, StringBuffer buf, int indent) {
		Divers.ecrire(buf, "TANT_QUE " + this.getCondition() + " FAIRE", indent);
		Divers.ecrire(buf, "DEBUT_TANT_QUE", indent+1);
		for (Iterator<ModeleInstruction> iter=instructions.iterator(); iter.hasNext();) {
			Object obj = iter.next();
			Instruction instr = (Instruction)obj ;
			instr.ecrire(prog, buf, indent+1);
		}
		Divers.ecrire(buf, "FIN_TANT_QUE", indent);
	}
}
