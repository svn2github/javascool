/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2013.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.langages.python;


import java.util.*;

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
	
//	 -------------------------------
//	 lecture d'arguments (standard)
//	 -------------------------------
	
	private void lireStandard(Programme prog, StringBuffer buf, int indent, String msg) {
		if ( isSimple() ) {
			lireSimpleStandard(prog,buf,indent,msg);
		}
		if ( isTabSimple()) {
			lireTabStandard(prog,buf,indent,msg);
		}
		if ( isTabClasse(prog)) {
			lireTabClasseStandard(prog, buf, indent, msg);
		}
		if ( isMatSimple() ) {
			lireMatStandard(prog,buf,indent,msg);
		}
		if ( isEnregistrement(prog)|| isClasse(prog) ) {
			lireClasseStandard(prog, buf, indent, msg);
		}
	}
	
	private void lireSimpleStandard(Programme prog, StringBuffer buf, int indent, String msg) {
		if (isTexte()) {
			Divers.ecrire(buf, this.nom + " = raw_input(" + msg + ") ", indent); 
		}
		else {
			Divers.ecrire(buf, this.nom + " = input(" + msg + ") ", indent); 
		}
	}
	
	private void lireTabStandard(Programme prog, StringBuffer buf, int indent, String msg) {
		if (msg!=null) Divers.ecrire(buf, "print " + msg + " ", indent); 
		Divers.ecrire(buf, "for i1 in range(0," + prog.getDim(1, this) + ") : ", indent);
		String msg1 = prog.quote("rang ") + " + str(i1) + " + prog.quote(" : ");
		Argument arg = new Argument(this.nom+"[i1]", this.getTypeOfTab(), null);
		arg.lireStandard(prog, buf, indent+1, msg1);
		Divers.ecrire(buf, "#end for", indent);
	}
	
	private void lireMatStandard(Programme prog, StringBuffer buf, int indent, String msg) {
		if (msg!=null) Divers.ecrire(buf, "print " + msg + " ", indent); 
		Divers.ecrire(buf, "for i1 in range(0," + prog.getDim(1, this) + ") : ", indent);
		Divers.ecrire(buf, "for j1 in range(0," + prog.getDim(2, this) + ") : ", indent+1);
		String msg1 = prog.quote("rang ") + " + str(i1) + " + prog.quote(", ") + " + str(j1) + " + prog.quote(" : ");
		Argument arg = new Argument(this.nom+"[i1][j1]", this.getTypeOfMat(), null);
		arg.lireStandard(prog, buf, indent+2, msg1);
		Divers.ecrire(buf, "#end for", indent+1);
		Divers.ecrire(buf, "#end for", indent);
	}
	
	private void lireClasseStandard(Programme prog, StringBuffer buf, int indent, String msg) {
		Classe cl = (Classe) getClasse(prog);
		for(Iterator<org.javascool.proglets.plurialgo.langages.modele.Variable> iter=cl.proprietes.iterator(); iter.hasNext(); ) {
			Variable prop = (Variable) iter.next();
			if (prop.isOut()) continue;
			Argument arg = new Argument(this.nom+"."+prop.nom, prop.type, this.mode);
			String msg1 = prog.quote(arg.nom  + " : ");
			arg.lireStandard(prog, buf, indent, msg1);
		}
	}
	
	private void lireTabClasseStandard(Programme prog, StringBuffer buf, int indent, String msg) {
		Classe cl = (Classe) getClasseOfTab(prog);
		Divers.ecrire(buf, "for ii in range(0," + prog.getDim(1, this) + ") : ", indent);
		String msg1 = prog.quote("rang ") + " + str(ii) + " + prog.quote(" de "+this.nom+" : ");
		Divers.ecrire(buf, "print " + msg1 + " ", indent+1); 
		for(Iterator<org.javascool.proglets.plurialgo.langages.modele.Variable> iter=cl.proprietes.iterator(); iter.hasNext(); ) {
			Variable prop = (Variable) iter.next();
			Argument arg = new Argument(this.nom+"[ii]"+"."+prop.nom, prop.type, oteDim(1));
			if (prop.isOut()) continue;
			msg1=prog.quote(prop.nom + " : ");
			arg.lireStandard(prog, buf, indent+1, msg1);
		}
		Divers.ecrire(buf, "#end for", indent);
	}
	
//	 --------------------------------
//	 écriture d'arguments (standard)
//	 --------------------------------
	
	private void ecrireStandard(Programme prog, StringBuffer buf, int indent, String msg) {
		if ( isSimple() ) {
			ecrireSimpleStandard(prog,buf,indent,msg);
		}
		if ( isTabSimple()) {
			ecrireTabStandard(prog,buf,indent,msg);
		}
		if ( isTabClasse(prog)) {
			ecrireTabClasseStandard(prog, buf, indent, msg);
		}
		if ( isMatSimple() ) {
			ecrireMatStandard(prog,buf,indent,msg);
		}
		if ( isEnregistrement(prog) || isClasse(prog) ) {
			ecrireClasseStandard(prog, buf, indent, msg);
		}
	}
	
	private void ecrireSimpleStandard(Programme prog, StringBuffer buf, int indent, String msg) {
		if (msg!=null && !(prog.quote("\\t").equals(msg))) 
			Divers.ecrire(buf, "print " + msg + ", " + this.nom, indent); 
		else if (msg!=null && (prog.quote("\\t").equals(msg))) 
			Divers.ecrire(buf, "print " + msg + ", " + this.nom + ", ", indent); 
		else if (msg==null) 
			Divers.ecrire(buf, "print " + this.nom, indent);
	}
	
	private void ecrireTabStandard(Programme prog, StringBuffer buf, int indent, String msg) {
		if (msg!=null) Divers.ecrire(buf, "print " + msg + " ", indent);
		Divers.ecrire(buf, "for i1 in range(0," + prog.getDim(1, this) + ") : ", indent);
		String msg1 = prog.quote("\\t");
		Argument arg = new Argument(this.nom+"[i1]", this.getTypeOfTab(), null);
		arg.ecrireStandard(prog, buf, indent+1, msg1);
		Divers.ecrire(buf, "#end for", indent);
		Divers.ecrire(buf, "print", indent);
	}
	
	private void ecrireMatStandard(Programme prog, StringBuffer buf, int indent, String msg) {
		if (msg!=null) Divers.ecrire(buf, "print " + msg + " ", indent);
		Divers.ecrire(buf, "for i1 in range(0," + prog.getDim(1, this) + ") : ", indent);
		Divers.ecrire(buf, "for j1 in range(0," + prog.getDim(2, this) + ") : ", indent+1);
		String msg1 = prog.quote("\\t");
		Argument arg = new Argument(this.nom+"[i1][j1]", this.getTypeOfMat(), null);
		arg.ecrireStandard(prog, buf, indent+2, msg1);
		Divers.ecrire(buf, "#end for", indent+1);
		Divers.ecrire(buf, "print", indent+1);
		Divers.ecrire(buf, "#end for", indent);
		Divers.ecrire(buf, "print", indent);
	}
	
	private void ecrireClasseStandard(Programme prog, StringBuffer buf, int indent, String msg) {
		if (msg!=null) Divers.ecrire(buf, "print " + msg + " ", indent);
		Classe cl = (Classe) getClasse(prog);
		for(Iterator<org.javascool.proglets.plurialgo.langages.modele.Variable> iter=cl.proprietes.iterator(); iter.hasNext(); ) {
			Variable prop = (Variable) iter.next();
			if (prop.isIn()) continue;
			Argument arg = new Argument(this.nom+"."+prop.nom, prop.type, this.mode);
			String msg1 = prog.quote(arg.nom + " : ");
			arg.ecrireStandard(prog, buf, indent, msg1);
		}
	}
	
	private void ecrireTabClasseStandard(Programme prog, StringBuffer buf, int indent, String msg) {
		if (msg!=null) Divers.ecrire(buf, "print " + msg + " ", indent);
		Classe cl = (Classe) getClasseOfTab(prog);
		Divers.ecrire(buf, "for ii in range(0," + prog.getDim(1, this) + ") : ", indent);
		String msg1 = prog.quote("rang ") + " + str(ii) + " + prog.quote(" de " + this.nom + " : ");
		Divers.ecrire(buf, "print " + msg1 + " ", indent+1); 
		for(Iterator<org.javascool.proglets.plurialgo.langages.modele.Variable> iter=cl.proprietes.iterator(); iter.hasNext(); ) {
			Variable prop = (Variable) iter.next();
			if (prop.isIn()) continue;
			Argument arg = new Argument(this.nom+"[ii]"+"."+prop.nom, prop.type, oteDim(1));
			msg1=prog.quote(prop.nom + " : ");
			arg.ecrireStandard(prog, buf, indent+1, msg1);
		}
		Divers.ecrire(buf, "#end for", indent);
		Divers.ecrire(buf, "print ", indent);
	}
	
}
