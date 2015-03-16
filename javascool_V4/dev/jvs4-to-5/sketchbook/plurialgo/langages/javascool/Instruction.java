/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2013.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.langages.javascool;

import java.util.*;

import org.javascool.proglets.plurialgo.divers.*;


/**
 * Cette classe hérite de la classe homonyme du modèle.
*/
public class Instruction extends org.javascool.proglets.plurialgo.langages.modele.Instruction {
	
	public Instruction() {
	}
	
	public void ecrire(Programme prog, StringBuffer buf, int indent) {
		if (isLectureStandard()) {
			for (Iterator<org.javascool.proglets.plurialgo.langages.modele.Argument> iter=arguments.iterator(); iter.hasNext();) {
				Argument arg = (Argument) iter.next();
				arg.ecrire(prog, buf, indent, this);
			}
		}
		else if (isLectureFichierTexte()) {
			FichierTexte f = new FichierTexte(this);
			f.lireFichierTexte(prog, buf, indent);
		}
		else if (isLectureSQL()) {
			Sql sql = new Sql(this);
			sql.lireSql(prog, buf, indent);
		}
		else if (isEcritureStandard()) {
			for (Iterator<org.javascool.proglets.plurialgo.langages.modele.Argument> iter=arguments.iterator(); iter.hasNext();) {
				Argument arg = (Argument) iter.next();
				arg.ecrire(prog, buf, indent, this);
			}
		}
		else if (isEcritureFichierTexte()) {
			FichierTexte f = new FichierTexte(this);
			f.ecrireFichierTexte(prog, buf, indent);
		}
		else if (isEcritureSQL()) {
			Sql sql = new Sql(this);
			sql.ecrireSql(prog, buf, indent);
		}
		else if (isAppel(prog)) {
			ecrireAppel(prog, buf, indent); 
		}
		else if (isSi()) {
			interpreterSi();
			if (isSelon()) {
				String var = this.getVariableSelon();
				Divers.ecrire(buf, "switch (" + var + ") {", indent);
				for (Iterator<org.javascool.proglets.plurialgo.langages.modele.Si> iter=sis.iterator(); iter.hasNext();) {
					Si si = (Si) iter.next();
					si.ecrire(prog, buf, indent + 1);
				}
				Divers.ecrire(buf, "}", indent);
			}
			else {	// si classique
				for (Iterator<org.javascool.proglets.plurialgo.langages.modele.Si> iter=sis.iterator(); iter.hasNext();) {
					Si si = (Si) iter.next();
					si.ecrire(prog, buf, indent);
				}
			}
		}
		else if (isPour()) {
			interpreterPour();	
			for (Iterator<org.javascool.proglets.plurialgo.langages.modele.Pour> iter=pours.iterator(); iter.hasNext();) {
				Pour pour = (Pour) iter.next();
				pour.ecrire(prog, buf, indent);
			}
		}
		else if (isTantQue()) {
			interpreterTantQue();	
			for (Iterator<org.javascool.proglets.plurialgo.langages.modele.TantQue> iter=tantques.iterator(); iter.hasNext();) {
				TantQue tq = (TantQue) iter.next();
				tq.ecrire(prog, buf, indent);
			}
		}
		else if (isAffectation()) {
			for (Iterator<org.javascool.proglets.plurialgo.langages.modele.Affectation> iter=affectations.iterator(); iter.hasNext();) {
				Affectation aff = (Affectation) iter.next();
				aff.ecrire(prog, buf, indent);
			}
		}
		else if (isCommentaire()) {
			prog.commenter(buf, nom.substring(2), indent);
		}
		else if (isPrimitive()) {
			Divers.ecrire(buf, this.getPrimitive() + ";", indent);
			String comm = this.getPrimitiveCommentaire();
			if (comm!=null) {
				if (comm.contains("algoDeMath")) {			
					Divers.ecrire(buf, " //proglet algoDeMath");
				}
				else {
					Divers.ecrire(buf, " // " + comm);			
				}
			}
		}
		else {
			Divers.ecrire(buf, nom, indent);
		}
	}
	
	private void ecrireAppel(Programme prog, StringBuffer buf, int indent)  {
		Argument retour = (Argument) getRetour();
		Argument objet = (Argument) getObjet();
		Divers.indenter(buf, indent);
		if (isAppelFonction(prog)) {
			retour.ecrire(prog, buf);
			Divers.ecrire(buf, " = ");
		}
		if (objet==null)
			Divers.ecrire(buf, nom);
		else
			Divers.ecrire(buf, objet.nom + "." + nom);	
		Divers.ecrire(buf, "(");
		for (Iterator<org.javascool.proglets.plurialgo.langages.modele.Argument> iter=arguments.iterator(); iter.hasNext();) {
			Argument arg = (Argument) iter.next();
			arg.ecrire(prog, buf);
			if (iter.hasNext()) Divers.ecrire(buf, ", ");
		}
		Divers.ecrire(buf, ")");	
		Divers.ecrire(buf, ";");
	}
	
}
