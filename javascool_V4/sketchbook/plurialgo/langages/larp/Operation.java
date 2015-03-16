package org.javascool.proglets.plurialgo.langages.larp;

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
		Variable retour = (Variable) getRetour();
		prog.commenter(buf, "Module auxiliaire " + this.nom, indent);
		Divers.ecrire(buf, "ENTRER ", indent);
		for (Iterator<ModeleParametre> iter=parametres.iterator(); iter.hasNext();) {
			Parametre param = (Parametre) iter.next();
			param.ecrire(prog, buf);
			if (iter.hasNext()) Divers.ecrire(buf, ", ");
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
			instr.ecrire(prog, buf, indent+1);
		}
		Divers.ecrire(buf, "RETOURNER ", indent);
		if (isFonction()) {
			Divers.ecrire(buf, retour.nom);
		}
	}	
	
	public void ecrire(ModeleProgramme prog, StringBuffer buf, int indent) {
		this.ecrire((org.javascool.proglets.plurialgo.langages.larp.Programme)prog, buf, indent);
	}

	
}
