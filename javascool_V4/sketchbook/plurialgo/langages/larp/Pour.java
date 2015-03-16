/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2013.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.langages.larp;

import java.util.Iterator;
import org.javascool.proglets.plurialgo.divers.Divers;
import org.javascool.proglets.plurialgo.langages.modele.*;


/**
 * Cette classe hérite de la classe homonyme du modèle.
*/
public class Pour extends ModelePour {
	
	public void ecrire(Programme prog, StringBuffer buf, int indent) {
		if (isTantQue()) {
			if (pas.startsWith("-")) {
				Divers.ecrire(buf, var + "=" + debut, indent) ;
				Divers.ecrire(buf, "TANTQUE (" + var + ">=" + fin + ") ", indent);
				for (Iterator<ModeleInstruction> iter=instructions.iterator(); iter.hasNext();) {
					Object obj = iter.next();
					Instruction instr = (Instruction)obj ;
					instr.ecrire(prog, buf, indent+1);
				}
				Divers.ecrire(buf, var + "=" + var + pas, indent+1) ;
				Divers.ecrire(buf, "FINTANTQUE", indent);
			}
			else {
				Divers.ecrire(buf, var + "=" + debut + "; ", indent) ;
				Divers.ecrire(buf, "TANTQUE (" + var + "<=" + fin + ") FAIRE", indent);
				for (Iterator<ModeleInstruction> iter=instructions.iterator(); iter.hasNext();) {
					Object obj = iter.next();
					Instruction instr = (Instruction)obj ;
					instr.ecrire(prog, buf, indent+1);
				}
				Divers.ecrire(buf, var + "=" + var + "+" + pas, indent+1) ;
				Divers.ecrire(buf, "FINTANTQUE", indent);
			}
		}
		else {
				Divers.ecrire(buf, "POUR " + var + "=" + debut  
						+ " JUSQU'A " + fin 
						+ " INCREMENT " + pas + " FAIRE ", indent);
				for (Iterator<ModeleInstruction> iter=instructions.iterator(); iter.hasNext();) {
					Object obj = iter.next();
					Instruction instr = (Instruction)obj ;
					instr.ecrire(prog, buf, indent+1);
				}
				Divers.ecrire(buf, "FINPOUR", indent);
		}
	}
	
}
