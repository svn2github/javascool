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
public class Pour extends ModelePour {
	
	public void ecrire(Programme prog, StringBuffer buf, int indent) {
		if (!pas.equals("1")) {
			if (pas.startsWith("-")) {
				Divers.ecrire(buf, var + " PREND_LA_VALEUR " + debut, indent) ;
				Divers.ecrire(buf, "TANT_QUE (" + var + ">=" + fin + ") FAIRE", indent);
				Divers.ecrire(buf, "DEBUT_TANT_QUE", indent+1);
				for (Iterator<ModeleInstruction> iter=instructions.iterator(); iter.hasNext();) {
					Object obj = iter.next();
					Instruction instr = (Instruction)obj ;
					instr.ecrire(prog, buf, indent+1);
				}
				Divers.ecrire(buf, var + " PREND_LA_VALEUR " + var + pas, indent+1) ;
				Divers.ecrire(buf, "FIN_TANT_QUE", indent+1);
			}
			else {
				Divers.ecrire(buf, var + " PREND_LA_VALEUR " + debut + "; ", indent) ;
				Divers.ecrire(buf, "TANT_QUE (" + var + "<=" + fin + ") FAIRE", indent);
				Divers.ecrire(buf, "DEBUT_TANT_QUE", indent+1);
				for (Iterator<ModeleInstruction> iter=instructions.iterator(); iter.hasNext();) {
					Object obj = iter.next();
					Instruction instr = (Instruction)obj ;
					instr.ecrire(prog, buf, indent+1);
				}
				Divers.ecrire(buf, var + " PREND_LA_VALEUR " + var + "+" + pas, indent+1) ;
				Divers.ecrire(buf, "FIN_TANT_QUE", indent+1);
			}
		}
		else {
				Divers.ecrire(buf, "POUR " + var + " ALLANT_DE " + debut  
						+ " A " + fin , indent);
				Divers.ecrire(buf, "DEBUT_POUR", indent+1);
				for (Iterator<ModeleInstruction> iter=instructions.iterator(); iter.hasNext();) {
					Object obj = iter.next();
					Instruction instr = (Instruction)obj ;
					instr.ecrire(prog, buf, indent+1);
				}
				Divers.ecrire(buf, "FIN_POUR", indent+1);
		}
	}
	
}
