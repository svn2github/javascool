/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2014.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.langages.R;

import java.util.*;
import org.javascool.proglets.plurialgo.divers.*;
import org.javascool.proglets.plurialgo.langages.modele.*;

/**
 * Cette classe hérite de la classe homonyme du modèle.
*/
public class Operation extends ModeleOperation {
	
	public Operation() {
	}
	
	public void ecrire(Programme prog, StringBuffer buf, int indent) {
		Divers.indenter(buf, indent);
		Divers.ecrire(buf, nom);
		Divers.ecrire(buf, " = ");
		this.ecrireLambda(prog, buf, indent);
	}

	void ecrireSpec(Programme prog, StringBuffer buf, int indent) {
		Divers.indenter(buf, indent);
		Divers.ecrire(buf, nom);
		Divers.ecrire(buf, " = function(");
		if (this.classe!=null) {
			Divers.ecrire(buf, "this");
			if (parametres.size()>0)	Divers.ecrire(buf, ", ");
		} 
		for (Iterator<ModeleParametre> iter=parametres.iterator(); iter.hasNext();) {
			Parametre param = (Parametre) iter.next();
			param.ecrire(prog, buf);
			if (iter.hasNext()) Divers.ecrire(buf, ", ");
		}
		Divers.ecrire(buf, ")");	
		Divers.ecrire(buf, " { }");
	}	
	
	void ecrireLambda(Programme prog, StringBuffer buf, int indent) {
		Variable retour = (Variable) getRetour();
		Divers.ecrire(buf, "function(");
		if (this.classe!=null) {
			Divers.ecrire(buf, "this");
			if (parametres.size()>0)	Divers.ecrire(buf, ", ");
		} 
		for (Iterator<ModeleParametre> iter=parametres.iterator(); iter.hasNext();) {
			Parametre param = (Parametre) iter.next();
			param.ecrire(prog, buf);
			if (iter.hasNext()) Divers.ecrire(buf, ", ");
		}
		Divers.ecrire(buf, ")");	
		Divers.ecrire(buf, " { ");
		for (Iterator<ModeleParametre> iter=parametres.iterator(); iter.hasNext();) {
			Parametre param = (Parametre) iter.next();
			if (param.isOut()) {
				Divers.ecrire(buf, "out_" + param.nom + " = " + param.nom, indent+1);
			}
		}
		for (Iterator<ModeleVariable> iter=variables.iterator(); iter.hasNext();) {
			Variable var = (Variable) iter.next();
			var.ecrire(prog, buf, indent+1);
		}
		if (isFonction()) {
			retour.ecrire(prog, buf, indent+1);
		}
		for (Iterator<ModeleInstruction> iter=instructions.iterator(); iter.hasNext();) {
			Instruction instr = (Instruction) iter.next();
			if (instr.deplacerHtml) continue;
			if (instr.deplacerWriter) continue;
			instr.ecrire(prog, buf, indent+1);
		}
		if (isFonction()) {
			Divers.ecrire(buf, "return(" + retour.nom + ")", indent+1);
		}
		if (isProcedure()) {
			for (Iterator<ModeleParametre> iter=parametres.iterator(); iter.hasNext();) {
				Parametre param = (Parametre) iter.next();
				if (param.isOut()) {
					Divers.ecrire(buf, "assign(out_" + param.nom + ", " + param.nom + ", pos=1)", indent+1);
				}
			}
		}
		if (this.classe!=null) {
			Divers.ecrire(buf, "this", indent+1);
		}
		Divers.ecrire(buf, "}", indent);
	}
	
	public void ecrire(ModeleProgramme prog, StringBuffer buf, int indent) {
		this.ecrire((org.javascool.proglets.plurialgo.langages.R.Programme)prog, buf, indent);
	}
		
}
