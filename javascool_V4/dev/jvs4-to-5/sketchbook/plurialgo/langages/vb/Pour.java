/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2013.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.langages.vb;

import java.util.Iterator;

import org.javascool.proglets.plurialgo.divers.Divers;



/**
 * Cette classe hérite de la classe homonyme du modèle.
*/
public class Pour extends org.javascool.proglets.plurialgo.langages.modele.Pour {
	
	public Pour() {
	}
	
	public void ecrire(Programme prog, StringBuffer buf, int indent) {
		if (isTantQue()) {
			if (pas.startsWith("-")) {
				Divers.ecrire(buf, var + "=" + debut, indent) ;
				Divers.ecrire(buf, "while(" + var + ">=" + fin + ") ", indent);
				for (Iterator<org.javascool.proglets.plurialgo.langages.modele.Instruction> iter=instructions.iterator(); iter.hasNext();) {
					Object obj = iter.next();
					Instruction instr = (Instruction)obj ;
					instr.ecrire(prog, buf, indent+1);
				}
				Divers.ecrire(buf, var + "=" + var + pas , indent+1) ;
				Divers.ecrire(buf, "wend", indent);
			}
			else {
				Divers.ecrire(buf, var + "=" + debut + "; ", indent) ;
				Divers.ecrire(buf, "while(" + var + "<=" + fin + ") ", indent);
				for (Iterator<org.javascool.proglets.plurialgo.langages.modele.Instruction> iter=instructions.iterator(); iter.hasNext();) {
					Object obj = iter.next();
					Instruction instr = (Instruction)obj ;
					instr.ecrire(prog, buf, indent+1);
				}
				Divers.ecrire(buf, var + "=" + var + "+" + pas , indent+1) ;
				Divers.ecrire(buf, "wend", indent);
			}
		}
		else {
			if (pas.startsWith("-")) {
				Divers.ecrire(buf, "for " + var + "=" + debut
						+ " to " + fin 
						+ " step " + pas , indent);
				for (Iterator<org.javascool.proglets.plurialgo.langages.modele.Instruction> iter=instructions.iterator(); iter.hasNext();) {
					Object obj = iter.next();
					Instruction instr = (Instruction)obj ;
					instr.ecrire(prog, buf, indent+1);
				}
				Divers.ecrire(buf, "next " + var, indent);
			}
			else {
				Divers.ecrire(buf, "for " + var + "=" + debut
						+ " to " + fin , indent);
				if (!"1".equals(pas)) 
					Divers.ecrire(buf, " step " + pas); 
				for (Iterator<org.javascool.proglets.plurialgo.langages.modele.Instruction> iter=instructions.iterator(); iter.hasNext();) {
					Object obj = iter.next();
					Instruction instr = (Instruction)obj ;
					instr.ecrire(prog, buf, indent+1);
				}
				Divers.ecrire(buf, "next " + var, indent);
			}
		}
	}
	
}
