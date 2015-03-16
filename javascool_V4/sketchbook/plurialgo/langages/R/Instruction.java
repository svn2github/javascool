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
public class Instruction extends ModeleInstruction {
	boolean deplacerHtml = false;
	boolean deplacerWriter = false;
	
	public Instruction() {
	}
	
	public void ecrire(Programme prog, StringBuffer buf, int indent) {
		if (isLectureStandard()) {
			this.lireStandard(prog, buf, indent);
		}
		else if (isLectureFichierTexte()) {
			FichierTexte f = new FichierTexte(this);
			f.lireFichierTexte(prog, buf, indent);
		}
		else if (isLectureSQL()) {
			Sql sql = new Sql(this);
			sql.lireSql(prog, buf, indent);
		}
		else if (isLecture()) {	// option erronee = standard
			this.setOption(null);
			this.lireStandard(prog, buf, indent);
		}
		else if (isEcritureStandard()) {
			this.ecrireStandard(prog, buf, indent);
		}
		else if (isEcritureFichierTexte()) {
			FichierTexte f = new FichierTexte(this);
			f.ecrireFichierTexte(prog, buf, indent);
		}
		else if (isEcritureFichierHtml()) {
			FichierHtml f = new FichierHtml(this);
			f.ecrireFichierTexte(prog, buf, indent);
		}
		else if (isEcritureFichierWriter()) {
			FichierWriter f = new FichierWriter(this);
			// fichier writer
			StringBuffer buf1 = new StringBuffer();
			String nom_fich = prog.nom + ".odt";
			prog.les_fichiers.put(nom_fich, buf1);
			f.ecrireFichierTexte(prog, buf1, 0);
			// fichier R
			Divers.ecrire(buf, "\n");
			Divers.ecrire(buf,"# ----- transformation du fichier Writer",indent);
			Divers.ecrire(buf,"library(odfWeave)",indent);
			Divers.ecrire(buf,"odfWeave(" + f.arg_fichier.nom + ",'resultat.odt')",indent);
			Divers.ecrire(buf, "\n");
		}
		else if (isEcritureSQL()) {
			Sql sql = new Sql(this);
			sql.ecrireSql(prog, buf, indent);
		}
		else if (isEcriture()) {	// option erronee = standard
			this.setOption(null);
			this.ecrireStandard(prog, buf, indent);
		}
		else if (isAppel(prog)) {
			ecrireAppel(prog, buf, indent);
		}
		else if (isSi()) {
			interpreterSi();
			for (Iterator<ModeleSi> iter=sis.iterator(); iter.hasNext();) {
				Si si = (Si) iter.next();
				si.ecrire(prog, buf, indent);
			}
		}
		else if (isPour()) {
			interpreterPour();	// en pour, tantque...
			for (Iterator<ModelePour> iter=pours.iterator(); iter.hasNext();) {
				Pour pour = (Pour) iter.next();
				pour.ecrire(prog, buf, indent);
			}
		}
		else if (isTantQue()) {
			interpreterTantQue();	// en tantque, repeter...
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
			Divers.ecrire(buf, this.getPrimitive() + ";", indent);
		}
		else {
			// Divers.ecrire(buf, nom, indent);
		}
	}
	
	private void lireStandard(Programme prog, StringBuffer buf, int indent) {
		for (Iterator<ModeleArgument> iter=arguments.iterator(); iter.hasNext();) {
			Argument arg = (Argument) iter.next();
			arg.ecrire(prog, buf, indent, this);
		}	
	}
	
	private void ecrireStandard(Programme prog, StringBuffer buf, int indent) {
		for (Iterator<ModeleArgument> iter=arguments.iterator(); iter.hasNext();) {
			Argument arg = (Argument) iter.next();
			arg.ecrire(prog, buf, indent, this);
		}		
	}
	
	private boolean isEcritureFichierHtml() {
		if (! isEcriture()) return false;
		Argument option = (Argument) getOption();
		if (option == null) return false;
		if (option.type == null) return false;
		return option.type.equalsIgnoreCase("FICHIER_HTML");		
	}
	
	private boolean isEcritureFichierWriter() {
		if (! isEcriture()) return false;
		Argument option = (Argument) getOption();
		if (option == null) return false;
		if (option.type == null) return false;
		return option.type.equalsIgnoreCase("FICHIER_WRITER");		
	}
	
	private void ecrireAppel(Programme prog, StringBuffer buf, int indent)  {
		Argument retour = (Argument) getRetour();
		Argument objet = (Argument) getObjet();
		if (objet==null) {
			Divers.indenter(buf, indent);
			if (isAppelFonction(prog) && retour!=null) {
				if (!"".equals(retour.nom)) {
					Divers.ecrire(buf, retour.nom + " = ");
				}
			}
			Divers.ecrire(buf, nom);
			Divers.ecrire(buf, "(");	
			for (Iterator<ModeleArgument> iter=arguments.iterator(); iter.hasNext();) {
				Argument arg = (Argument) iter.next();
				if (arg.isOut()) {
					Divers.ecrire(buf, prog.quote(arg.nom));
				}
				else {
					Divers.ecrire(buf, arg.nom);
				}
				if (iter.hasNext()) Divers.ecrire(buf, ", ");
			}
			Divers.ecrire(buf, ")");
		}
		else {
			Divers.indenter(buf, indent);
			if (isAppelFonction(prog) && retour!=null) {
				if (!"".equals(retour.nom)) {
					Divers.ecrire(buf, retour.nom + " = ");
				}
			}
			Divers.ecrire(buf, nom);
			Divers.ecrire(buf, "(" + objet.nom);	
			for (Iterator<ModeleArgument> iter=arguments.iterator(); iter.hasNext();) {
				Divers.ecrire(buf, ", ");
				Argument arg = (Argument) iter.next();
				Divers.ecrire(buf, arg.nom);
			}
			Divers.ecrire(buf, ")");
		}
	}
	
	public void ecrire(ModeleProgramme prog, StringBuffer buf, int indent) {
		this.ecrire((org.javascool.proglets.plurialgo.langages.R.Programme)prog, buf, indent);
	}
		
}
