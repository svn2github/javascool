/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2013.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.langages.algobox;

import java.util.Iterator;
import org.javascool.proglets.plurialgo.divers.Divers;
import org.javascool.proglets.plurialgo.langages.modele.*;


/**
 * Cette classe hérite de la classe homonyme du modèle.
*/
public class Si extends ModeleSi {
	
	public Si() {
	}

	public void ecrire(Programme prog, StringBuffer buf, int indent) {
		if (isSelon()) {
		}
		else {
			if (isSi()) {
				Divers.ecrire(buf, "SI " + this.getCondition() + " ALORS", indent);
				Divers.ecrire(buf, "DEBUT_SI", indent+1);
				for (Iterator<ModeleInstruction> iter=instructions.iterator(); iter.hasNext();) {
					Object obj = iter.next();
					Instruction instr = (Instruction)obj ;
					instr.ecrire(prog, buf, indent+1);
				}
				Divers.ecrire(buf, "FIN_SI", indent+1);
			}
			if (isSinonSi()) {
				Divers.ecrire(buf, "SINON", indent);
				Divers.ecrire(buf, "DEBUT_SINON", indent+1);
				Divers.ecrire(buf, "SI " + this.getCondition() + " ALORS", indent+1);
				Divers.ecrire(buf, "DEBUT_SI", indent+2);
				for (Iterator<ModeleInstruction> iter=instructions.iterator(); iter.hasNext();) {
					Object obj = iter.next();
					Instruction instr = (Instruction)obj ;
					instr.ecrire(prog, buf, indent+2);
				}
				Divers.ecrire(buf, "FIN_SI", indent+2);
			}
			if (isSinon()) {
				Divers.ecrire(buf, "SINON", indent);
				Divers.ecrire(buf, "DEBUT_SINON", indent+1);
				for (Iterator<ModeleInstruction> iter=instructions.iterator(); iter.hasNext();) {
					Object obj = iter.next();
					Instruction instr = (Instruction)obj ;
					instr.ecrire(prog, buf, indent+2);
				}
			}
		}
	}
	
}
