/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2013.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.langages.larp;

import java.util.Iterator;

import org.javascool.proglets.plurialgo.divers.Divers;


/**
 * Cette classe hérite de la classe homonyme du modèle.
*/
public class Si extends org.javascool.proglets.plurialgo.langages.modele.Si {
	
	public Si() {
	}

	public void ecrire(Programme prog, StringBuffer buf, int indent) {
		if (isSelon()) {
			if (isSinon()) {
				Divers.ecrire(buf, "SINON", indent);
				for (Iterator<org.javascool.proglets.plurialgo.langages.modele.Instruction> iter=instructions.iterator(); iter.hasNext();) {
					Object obj = iter.next();
					Instruction instr = (Instruction)obj ;
					instr.ecrire(prog, buf, indent+1);
				}
			}
			else if ( isSi() || isSinonSi() ) {
				String valeur = this.getValeurSelon();
				Divers.ecrire(buf, "" + valeur + " :", indent);
				for (Iterator<org.javascool.proglets.plurialgo.langages.modele.Instruction> iter=instructions.iterator(); iter.hasNext();) {
					Object obj = iter.next();
					Instruction instr = (Instruction)obj ;
					instr.ecrire(prog, buf, indent+1);
				}
			}
		}
		else {
			if (isSi()) {
				Divers.ecrire(buf, "SI " + this.getCondition() + " ALORS", indent);
				for (Iterator<org.javascool.proglets.plurialgo.langages.modele.Instruction> iter=instructions.iterator(); iter.hasNext();) {
					Object obj = iter.next();
					Instruction instr = (Instruction)obj ;
					instr.ecrire(prog, buf, indent+1);
				}
			}
			if (isSinonSi()) {
				Divers.ecrire(buf, "SINON SI " + this.getCondition() + " ALORS", indent);
				for (Iterator<org.javascool.proglets.plurialgo.langages.modele.Instruction> iter=instructions.iterator(); iter.hasNext();) {
					Object obj = iter.next();
					Instruction instr = (Instruction)obj ;
					instr.ecrire(prog, buf, indent+1);
				}
			}
			if (isSinon()) {
				Divers.ecrire(buf, "SINON", indent);
				for (Iterator<org.javascool.proglets.plurialgo.langages.modele.Instruction> iter=instructions.iterator(); iter.hasNext();) {
					Object obj = iter.next();
					Instruction instr = (Instruction)obj ;
					instr.ecrire(prog, buf, indent+1);
				}
			}
		}
	}
	
}
