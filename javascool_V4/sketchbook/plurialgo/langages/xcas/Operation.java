package org.javascool.proglets.plurialgo.langages.xcas;

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
		StringBuffer gauche_local = new StringBuffer();
		StringBuffer gauche_retour = new StringBuffer();
		Variable retour = (Variable) getRetour();
		Divers.ecrire(buf, this.nom, indent);
		Divers.ecrire(buf, "(");
		int nbparam=0;
		for (Iterator<ModeleParametre> iter=parametres.iterator(); iter.hasNext();) {
			Parametre param = (Parametre) iter.next();
			if (param.isOut()) {	// procedure transformee en fonction
				if (gauche_retour.length()>0) {
					gauche_retour.append(",");
					gauche_local.append(",");
				}
				gauche_retour.append(param.nom);
				gauche_local.append(param.nom);
				if (param.isTabSimple()) {
					gauche_local.append(":=[]");
				}
				if (param.isMatSimple()) {
					gauche_local.append(":=matrix(5,5)");
				}
				continue;
			}
			nbparam=nbparam+1;
			if (nbparam>1) Divers.ecrire(buf, ", ");
			param.ecrire(prog, buf);
		}
		Divers.ecrire(buf, "):={");
		if (variables.size()>0 || isFonction() || gauche_local.length()>0) {
			Divers.ecrire(buf, "local ", indent+1);
		}
		for (Iterator<ModeleVariable> iter=variables.iterator(); iter.hasNext();) {
			Variable var = (Variable) iter.next();
			var.ecrire(prog, buf,-1);
			if (iter.hasNext() || isFonction() || gauche_local.length()>0) {
				Divers.ecrire(buf, ",");
			}
		}
		if (isFonction()) {
			retour.ecrire(prog, buf, -1);
		}
		else if (gauche_local.length()>0) {
			Divers.ecrire(buf, gauche_local.toString());			
		}
		if (variables.size()>0 || isFonction() || gauche_local.length()>0) {
			Divers.ecrire(buf, ";");
		}
		for (Iterator<ModeleInstruction> iter=instructions.iterator(); iter.hasNext();) {
			Instruction instr = (Instruction) iter.next();
			instr.ecrire(prog, buf, indent+1);
		}
		if (isFonction()) {
			Divers.ecrire(buf, "retourne ", indent+1);
			Divers.ecrire(buf, retour.nom);
			Divers.ecrire(buf, ";");
		}
		else if (gauche_retour.length()>0) {
			Divers.ecrire(buf, "retourne ", indent+1);
			Divers.ecrire(buf, gauche_retour.toString());
			Divers.ecrire(buf, ";");			
		}
		Divers.ecrire(buf, "};", indent);
	}
	
	public void ecrire(ModeleProgramme prog, StringBuffer buf, int indent) {
		this.ecrire((org.javascool.proglets.plurialgo.langages.xcas.Programme)prog, buf, indent);
	}
	
}
