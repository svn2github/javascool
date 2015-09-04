/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2013.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.langages.vb;

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
		else if (isLectureFormulaire()) {
			Formulaire form = new Formulaire(this);
			form.lireFormu(prog, buf, indent);
		}
		else if (isLectureFichierTexte()) {
			FichierTexte f = new FichierTexte(this);
			f.lireFichierTexte(prog, buf, indent);
		}
		else if (isLectureFichierClasse(prog)) {
			Argument fichier = (Argument) getFichier();
			Divers.ecrire(buf, "// tache non realisee : lecture du fichier objet " + fichier.nom , indent);
			prog.ecrireErreur("// tache non realisee : lecture du fichier objet " + fichier.nom);
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
		else if (isEcritureFichierClasse(prog)) {
			Argument fichier = (Argument) getFichier();
			Divers.ecrire(buf, "// tache non realisee : ecriture dans le fichier objet " + fichier.nom , indent);
			prog.ecrireWarning("// tache non realisee : ecriture dans le fichier objet " + fichier.nom);
		}
		else if (isAppel(prog)) {
			ecrireAppel(prog, buf, indent);
		}
		else if (isSi()) {
			int nb_si = interpreterSi();	
			if (isSelon()) {
				String var = this.getVariableSelon();
				Divers.ecrire(buf, "select case " + var, indent);
				for (Iterator<ModeleSi> iter=sis.iterator(); iter.hasNext();) {
					Si si = (Si) iter.next();
					si.ecrire(prog, buf, indent+1);
				}
				Divers.ecrire(buf, "end select", indent);
			}
			else {
				for (Iterator<ModeleSi> iter=sis.iterator(); iter.hasNext();) {
					Si si = (Si) iter.next();
					si.ecrire(prog, buf, indent);
				}
				if (nb_si>0) {
					Divers.ecrire(buf, "end if", indent);
				}
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
			Divers.ecrire(buf, "Call " + this.getPrimitive(), indent);
		}
		else {
			// Divers.ecrire(buf, nom, indent);
		}
	}
	
	private void ecrireAppel(Programme prog, StringBuffer buf, int indent)  {
		Argument retour = (Argument) getRetour();
		Argument objet = (Argument) getObjet();
		Divers.indenter(buf, indent);
		if (isAppelFonction(prog) && retour!=null) {
			retour.ecrire(prog, buf);
			Divers.ecrire(buf, " = ");
		}
		if (isAppelProcedure(prog)) {
			Divers.ecrire(buf, "Call ");
		}
		if (objet==null)
			Divers.ecrire(buf, nom);
		else
			Divers.ecrire(buf, objet.nom + "." + nom);	
		Divers.ecrire(buf, "(");
		for (Iterator<ModeleArgument> iter=arguments.iterator(); iter.hasNext();) {
			Argument arg = (Argument) iter.next();
			arg.ecrire(prog, buf);
			if (iter.hasNext()) Divers.ecrire(buf, ", ");
		}
		Divers.ecrire(buf, ")");	
	}	
	
	public void ecrire(ModeleProgramme prog, StringBuffer buf, int indent) {
		this.ecrire((org.javascool.proglets.plurialgo.langages.vb.Programme)prog, buf, indent);
	}

}
