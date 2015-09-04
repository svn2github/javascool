/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2014.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.langages.R;

import org.javascool.proglets.plurialgo.divers.*;
import java.util.*;
import org.javascool.proglets.plurialgo.langages.modele.*;

/**
 * Cette classe hérite de la classe homonyme du modèle.
*/
public class Argument extends ModeleArgument {
	
	public Argument() {
	}
	
	public Argument(String nom, String type, String mode) {
		this.nom = nom;
		this.type = type;
		this.mode = mode;
	}
	
//	public void ecrire(Programme prog, StringBuffer buf, int indent) {
//		Divers.ecrire(buf, "argument " + nom + " : "  + type, indent);
//	}
	
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
		if (txt.contains("[")) {
			txt = "paste( " + txt + " )";
			txt = Divers.remplacer(txt, "[", "[\" , ");
			txt = Divers.remplacer(txt, "]", " , \"]");
		}
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
		if ( isTabClasse(prog)) {
			lireTabClasseStandard(prog, buf, indent, msg);
		}
		if ( isMatSimple() ) {
			lireMatStandard(prog,buf,indent,msg);
		}
		if ( isEnregistrement(prog) || isClasse(prog) ) {
			lireClasseStandard(prog, buf, indent, msg);
		}
	}
	
	private void lireSimpleStandard(Programme prog, StringBuffer buf, int indent, String msg) {
		if (this.isTexte()) {
			Divers.indenter(buf, indent);
			Divers.ecrire(buf, this.nom + " = as.character(dlgInput( " + transformerMsg(msg) + " )$res) "); 
		}
		else {
			Divers.indenter(buf, indent);	
			Divers.ecrire(buf, this.nom + " = as.numeric(dlgInput( " + transformerMsg(msg) + " )$res) "); 					
		}
	}
	
	private void lireTabStandard(Programme prog, StringBuffer buf, int indent, String msg) {
		this.addVariable(new Variable("i1","ENTIER"));
		Divers.ecrire(buf, "for (i1 in seq(1," + prog.getDim(1, this) + ")) { ", indent);
		Argument arg = new Argument(this.nom+"[i1]", this.getTypeOfTab(), null);
		String msg1 =  prog.quote(arg.nom + " : ");
		arg.lireStandard(prog, buf, indent+1, msg1);
		Divers.ecrire(buf, "}", indent);
	}
	
	private void lireMatStandard(Programme prog, StringBuffer buf, int indent, String msg) {
		this.addVariable(new Variable("i1","ENTIER"));
		this.addVariable(new Variable("j1","ENTIER"));
		Divers.ecrire(buf, "for (i1 in seq(1," + prog.getDim(1, this) + ")) { ", indent);
		Divers.ecrire(buf, "for (j1 in seq(1," + prog.getDim(2, this) + ")) { ", indent+1);
		Argument arg = new Argument(this.nom+"[i1][j1]", this.getTypeOfMat(), null);
		String msg1 =  prog.quote(arg.nom + " : ");
		arg.lireStandard(prog, buf, indent+2, msg1);
		Divers.ecrire(buf, "}", indent+1);
		Divers.ecrire(buf, "}", indent);
		String dim1 = prog.getDim(1, this);
		String dim2 = prog.getDim(2, this);
		if ( !dim1.equals(prog.getMaxTab()) || !dim2.equals(prog.getMaxTab()) ) {
			Divers.ecrire(buf, this.nom + "=" + this.nom + "[1:" + dim1 + ",1:" + dim2 + "]", indent);
		}
	}
	
	private void lireClasseStandard(Programme prog, StringBuffer buf, int indent, String msg) {
		Classe cl = (Classe) getClasse(prog);
		for(Iterator<ModeleVariable> iter=cl.proprietes.iterator(); iter.hasNext(); ) {
			Variable prop = (Variable) iter.next();
			if (prop.isOut()) continue;
			Argument arg = new Argument(this.nom+"."+prop.nom, prop.type, this.mode);
			arg.parent = this.parent;
			String msg1 = prog.quote(arg.nom);
			arg.lireStandard(prog, buf, indent, msg1);
		}
		if (cl.isEnregistrement()) {
			//Divers.ecrire(buf, "try(assign('" + nom + "',data.frame(" + nom + ")),silent=TRUE)", indent);
		}
	}
	
	public void lireTabClasseStandard(Programme prog, StringBuffer buf, int indent, String msg) {
	}
	
//	 --------------------------------
//	 ecriture d'arguments	(standard)
//	 --------------------------------
	void ecrireStandard(Programme prog, StringBuffer buf, int indent, String msg) {
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
		if ( isEnregistrement(prog) ) {
			ecrireEnregistrementStandard(prog, buf, indent, msg);
		}
		if ( isClasse(prog) ) {
			//ecrireEnregistrementStandard(prog, buf, indent, msg);
			ecrireClasseStandard(prog, buf, indent, msg);
		}
	}
	
	private void ecrireSimpleStandard(Programme prog, StringBuffer buf, int indent, String msg) {
		if (msg!=null) {
			Divers.ecrire(buf, "cat(" + msg , indent); 
			Divers.ecrire(buf, ", " + this.nom + ", \"\\n\")");
		}
		else {
			Divers.ecrire(buf, "cat(" + this.nom + ", \"\\n\")", indent);
		}
	}
	
	private void ecrireTabStandard(Programme prog, StringBuffer buf, int indent, String msg) {
		if (msg!=null) {
			Divers.ecrire(buf, "cat(" + msg , indent); 
			Divers.ecrire(buf, ", " + this.nom + ", \"\\n\")");
		}
		else {
			Divers.ecrire(buf, "cat(" + this.nom + ", \"\\n\")", indent);
		}
	}
	
	private void ecrireMatStandard(Programme prog, StringBuffer buf, int indent, String msg) {
		if (msg!=null) Divers.ecrire(buf, "print(" + msg + ")", indent);
		Divers.ecrire(buf, "print(" + nom + ")", indent);
	}
	
	private void ecrireEnregistrementStandard(Programme prog, StringBuffer buf, int indent, String msg) {
		if (msg!=null) Divers.ecrire(buf, "print(" + msg + ")", indent);
		Divers.ecrire(buf, "print(" + nom + ")", indent);
	}
	
	private void ecrireClasseStandard(Programme prog, StringBuffer buf, int indent, String msg) {
		if (msg!=null) Divers.ecrire(buf, "print(" + msg + ")", indent);
		Classe cl = (Classe) getClasse(prog);
		for(Iterator<ModeleVariable> iter=cl.proprietes.iterator(); iter.hasNext(); ) {
			Variable prop = (Variable) iter.next();
			if (prop.isIn()) continue;
			Argument arg = new Argument(nom+"@"+prop.nom, prop.type, this.mode);
			String msg1 = prog.quote(arg.nom + " : ");
			arg.ecrireStandard(prog, buf, indent, msg1);
		}
	}
	
	private void ecrireTabClasseStandard(Programme prog, StringBuffer buf, int indent, String msg) {
	}
	
}
