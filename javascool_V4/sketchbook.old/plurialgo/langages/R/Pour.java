/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2014.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.langages.R;

import java.util.Iterator;
import org.javascool.proglets.plurialgo.divers.Divers;
import org.javascool.proglets.plurialgo.langages.modele.*;

/**
 * Cette classe hérite de la classe homonyme du modèle.
*/
public class Pour extends ModelePour {
	
	public Pour() {
	}
	
	public void ecrire(Programme prog, StringBuffer buf, int indent) {
		if (isTantQue()) {
			if (pas.startsWith("-")) {
				Divers.ecrire(buf, var + "=" + debut, indent) ;
				Divers.ecrire(buf, "while(" + var + ">=" + fin + ") { ", indent);
				for (Iterator<ModeleInstruction> iter=instructions.iterator(); iter.hasNext();) {
					Object obj = iter.next();
					Instruction instr = (Instruction)obj ;
					instr.ecrire(prog, buf, indent+1);
				}
				Divers.ecrire(buf, var + "=" + var + pas , indent+1) ;
				Divers.ecrire(buf, "}", indent);
			}
			else {
				Divers.ecrire(buf, var + "=" + debut , indent) ;
				Divers.ecrire(buf, "while(" + var + "<=" + fin + ") { ", indent);
				for (Iterator<ModeleInstruction> iter=instructions.iterator(); iter.hasNext();) {
					Object obj = iter.next();
					Instruction instr = (Instruction)obj ;
					instr.ecrire(prog, buf, indent+1);
				}
				Divers.ecrire(buf, var + "=" + var + "+" + pas, indent+1) ;
				Divers.ecrire(buf, "}", indent);
			}
		}
		else {
			if (pas.equals("1")) {
				Divers.ecrire(buf, "for (" + var + " in seq(" + debut + ", " 
						+ fin + ")) {", indent);
			}
			else {
				Divers.ecrire(buf, "for (" + var + " in seq(" + debut + ", " 
						+ fin + ", " + pas + ")) {", indent);
			}
			for (Iterator<ModeleInstruction> iter=instructions.iterator(); iter.hasNext();) {
				Object obj = iter.next();
				Instruction instr = (Instruction)obj ;
				instr.ecrire(prog, buf, indent+1);
			}
			Divers.ecrire(buf, "}", indent);
		}
	}
	
}
