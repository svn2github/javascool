/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2013.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.langages.larp;


import org.javascool.proglets.plurialgo.divers.*;

/**
 * Cette classe hérite de la classe homonyme du modèle.
*/
public class Argument extends org.javascool.proglets.plurialgo.langages.modele.Argument {
	
	public Argument() {
	}
	
	public Argument(String nom, String type, String mode) {
		this.nom = nom;
		this.type = type;
		this.mode = mode;
	}
	
	public void ecrire(Programme prog, StringBuffer buf, int indent, Instruction pere) {
		if (pere==null) return;
		if (pere.isLectureStandard()) {
			String msg = prog.quote(nom+" : ");
			this.lireStandard(prog, buf, indent, msg);
		}
		else if (pere.isEcritureStandard()) {
			String msg = prog.quote(nom+" : ");
			this.ecrireStandard(prog, buf, indent, msg);
		}
	}
	
	private String transformerMsg(String msg) {
		if (msg==null) return "";
		String txt = msg;
		txt=Divers.remplacer(txt, "[", "[\", ");
		txt=Divers.remplacer(txt, "]", ", \"]");
		return txt;
	}
	
// -------------------------------
// lecture d'arguments (standard)
// -------------------------------
	
	private void lireStandard(Programme prog, StringBuffer buf, int indent, String msg) { 
		if ( isSimple() ) {
			lireSimpleStandard(prog,buf,indent,msg);
		}
		if ( isTabSimple()) {
			lireTabStandard(prog,buf,indent,msg);
		}
		if ( isMatSimple() ) {
			lireMatStandard(prog,buf,indent,msg);
		}
	}
	
	private void lireSimpleStandard(Programme prog, StringBuffer buf, int indent, String msg) {
		Divers.ecrire(buf, "ECRIRE " + transformerMsg(msg), indent);
		Divers.ecrire(buf, "LIRE " + this.nom, indent);
	}
	
	private void lireTabStandard(Programme prog, StringBuffer buf, int indent, String msg) {
		Divers.ecrire(buf, "POUR i1=1 JUSQU'A " + prog.getDim(1, this) + " FAIRE", indent);
		Argument arg = new Argument(this.nom+"[i1]", this.getTypeOfTab(), null);
		String msg1 = prog.quote(arg.nom + " : ");
		arg.lireStandard(prog, buf, indent+1, msg1);
		Divers.ecrire(buf, "FINPOUR", indent);
	}
	
	private void lireMatStandard(Programme prog, StringBuffer buf, int indent, String msg) {
		Divers.ecrire(buf, "POUR i1=1 JUSQU'A " + prog.getDim(1, this) + " FAIRE", indent);
		Divers.ecrire(buf, "POUR j1=1 JUSQU'A " + prog.getDim(2, this) + " FAIRE", indent+1);
		Argument arg = new Argument(this.nom+"[i1][j1]", this.getTypeOfMat(), null);
		String msg1 = prog.quote(arg.nom + " : ");
		arg.lireStandard(prog, buf, indent+2, msg1);
		Divers.ecrire(buf, "FINPOUR ", indent+1);
		Divers.ecrire(buf, "FINPOUR ", indent);
	}
	
//	 --------------------------------
//	 écriture d'arguments	(standard)
//	 --------------------------------
	
	private void ecrireStandard(Programme prog, StringBuffer buf, int indent, String msg) {
		if ( isSimple() ) {
			ecrireSimpleStandard(prog,buf,indent,msg);
		}
		if ( isTabSimple()) {
			ecrireTabStandard(prog,buf,indent,msg);
		}
		if ( isMatSimple() ) {
			ecrireMatStandard(prog,buf,indent,msg);
		}
	}
	
	private void ecrireSimpleStandard(Programme prog, StringBuffer buf, int indent, String msg) {
		Divers.indenter(buf, indent);
		if (msg!=null) Divers.ecrire(buf, "ECRIRE " + msg + ", " + this.nom);
		else Divers.ecrire(buf, "ECRIRE " + this.nom);
	}
	
	private void ecrireTabStandard(Programme prog, StringBuffer buf, int indent, String msg) {
		this.ecrireSimpleStandard(prog, buf, indent, msg);
	}
	
	private void ecrireMatStandard(Programme prog, StringBuffer buf, int indent, String msg) {
		if (msg!=null) Divers.ecrire(buf, "ECRIRE " + msg , indent); 
		Divers.ecrire(buf, "POUR i1=1 JUSQU'A " + prog.getDim(1, this) + " FAIRE", indent);
		Divers.ecrire(buf, "ECRIRE " + this.nom +"[i1]", indent+1);
		Divers.ecrire(buf, "FINPOUR", indent);
	}
	
}
