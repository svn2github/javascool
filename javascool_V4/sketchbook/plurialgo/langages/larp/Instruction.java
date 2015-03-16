/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2013.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.langages.larp;

import java.util.*;
import org.javascool.proglets.plurialgo.divers.*;
import org.javascool.proglets.plurialgo.langages.modele.*;


/**
 * Cette classe hérite de la classe homonyme du modèle.
*/
public class Instruction extends ModeleInstruction {
	
	public Instruction() {
	}
	
	public void ecrire(Programme prog, StringBuffer buf, int indent) {
		if (isLectureStandard()) {
			for (Iterator<ModeleArgument> iter=arguments.iterator(); iter.hasNext();) {
				Argument arg = (Argument) iter.next();
				arg.ecrire(prog, buf, indent, this);
			}
		}
		else if (isLectureFichierTexte()) {
			FichierTexte f = new FichierTexte(this);
			f.lireFichierTexte(prog, buf, indent);
		}
		else if (isEcritureStandard()) {
			for (Iterator<ModeleArgument> iter=arguments.iterator(); iter.hasNext();) {
				Argument arg = (Argument) iter.next();
				arg.ecrire(prog, buf, indent, this);
			}
		}
		else if (isEcritureFichierTexte()) {
			FichierTexte f = new FichierTexte(this);
			f.ecrireFichierTexte(prog, buf, indent);
		}
		else if (isAppel(prog)) {
			ecrireAppel(prog, buf, indent); 
		}
		else if (isSi()) {
			int nb_si = interpreterSi();
			if (isSelon()) {
				String var = this.getVariableSelon();
				Divers.ecrire(buf, "SÉLECTIONNER " + var, indent);
				for (Iterator<ModeleSi> iter=sis.iterator(); iter.hasNext();) {
					Si si = (Si) iter.next();
					si.ecrire(prog, buf, indent + 1);
				}
				Divers.ecrire(buf, "FINSÉLECTIONNER", indent);
			}
			else {	// si classique
				for (Iterator<ModeleSi> iter=sis.iterator(); iter.hasNext();) {
					Si si = (Si) iter.next();
					si.ecrire(prog, buf, indent);
				}
				if (nb_si > 0) Divers.ecrire(buf, "FINSI", indent);
			}
		}
		else if (isPour()) {
			interpreterPour();	
			for (Iterator<ModelePour> iter=pours.iterator(); iter.hasNext();) {
				Pour pour = (Pour) iter.next();
				pour.ecrire(prog, buf, indent);
			}
		}
		else if (isTantQue()) {
			interpreterTantQue();	
			for (Iterator<ModeleTantQue> iter=tantques.iterator(); iter.hasNext();) {
				TantQue tq = (TantQue) iter.next();
				tq.ecrire(prog, buf, indent);
			}
		}
		else if (isAffectation()) {
			for (Iterator<ModeleAffectation> iter=affectations.iterator(); iter.hasNext();) {
				Affectation aff = (Affectation) iter.next();
				aff.ecrire(prog, buf, indent);
			}
		}
		else if (isCommentaire()) {
			prog.commenter(buf, nom.substring(2), indent);
		}
		else if (isPrimitive()) {
			Divers.ecrire(buf, "EXECUTER " + this.getPrimitive(), indent);
		}
		else {
			// Divers.ecrire(buf, nom, indent);
		}
	}
	
	private void ecrireAppel(Programme prog, StringBuffer buf, int indent)  {
		Argument retour = (Argument) getRetour();
		Divers.indenter(buf, indent);
		if (isAppelFonction(prog) && retour!=null) {
			retour.ecrire(prog, buf);
			Divers.ecrire(buf, " = ");
		}
		else if (isAppelProcedure(prog)) {
			Divers.ecrire(buf, "EXECUTER ");
		}
		Divers.ecrire(buf, nom);
		Divers.ecrire(buf, "(");
		for (Iterator<ModeleArgument> iter=arguments.iterator(); iter.hasNext();) {
			Argument arg = (Argument) iter.next();
			arg.ecrire(prog, buf);
			if (iter.hasNext()) Divers.ecrire(buf, ", ");
		}
		Divers.ecrire(buf, ")");
	}
	
	public void ecrire(ModeleProgramme prog, StringBuffer buf, int indent) {
		this.ecrire((org.javascool.proglets.plurialgo.langages.larp.Programme)prog, buf, indent);
	}
	
}
