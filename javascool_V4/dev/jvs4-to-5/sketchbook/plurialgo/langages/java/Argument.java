/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2013.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.langages.java;


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
	
	private String transformerMsg(String msg) {
		if (msg==null) return "";
		String txt = msg;
		txt=Divers.remplacer(txt, "[", "[\" + (");
		txt=Divers.remplacer(txt, "]", ") + \"]");
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
		Divers.ecrire(buf, "System.out.println( " + transformerMsg(msg) + "); ", indent);
		if (this.isEntier()) {
			Divers.indenter(buf, indent);
			Divers.ecrire(buf, this.nom + " = " + prog.getScanner() + ".nextInt(); ");
		}
		if (this.isReel()) {
			Divers.indenter(buf, indent);
			Divers.ecrire(buf, this.nom + " = " + prog.getScanner() + ".nextDouble(); ");
		}
		if (this.isTexte()) {
			Divers.ecrire(buf, "//" + prog.getScanner() + ".nextLine(); ", indent);
			Divers.indenter(buf, indent);
			Divers.ecrire(buf, this.nom + " = " + prog.getScanner() + ".next(); ");
		}
		if (this.isBooleen()) {
			Divers.indenter(buf, indent);
			Divers.ecrire(buf, this.nom + " = " + prog.getScanner() + ".nextBoolean(); ");
		}
	}
	
	private void lireTabStandard(Programme prog, StringBuffer buf, int indent, String msg) {
		this.addVariable(new Variable("i1","ENTIER"));
		Divers.ecrire(buf, "for(i1=0; i1<" + prog.getDim(1, this) + "; i1++) {", indent);
		Argument arg = new Argument(this.nom+"[i1]", this.getTypeOfTab(), null);
		String msg1 =  prog.quote(arg.nom + " : ");
		arg.lireStandard(prog, buf, indent+1, msg1);
		Divers.ecrire(buf, "}", indent);
	}
	
	private void lireMatStandard(Programme prog, StringBuffer buf, int indent, String msg) {
		this.addVariable(new Variable("i1","ENTIER"));
		this.addVariable(new Variable("j1","ENTIER"));
		Divers.ecrire(buf, "for(i1=0; i1<" + prog.getDim(1, this)+ "; i1++) {", indent);
		Divers.ecrire(buf, "for(j1=0; j1<" + prog.getDim(2, this) + "; j1++) {", indent+1);
		Argument arg = new Argument(this.nom+"[i1][j1]", this.getTypeOfMat(), null);
		String msg1 =  prog.quote(arg.nom + " : ");
		arg.lireStandard(prog, buf, indent+2, msg1);
		Divers.ecrire(buf, "}", indent+1);
		Divers.ecrire(buf, "}", indent);
	}
	
	public void lireClasseStandard(Programme prog, StringBuffer buf, int indent, String msg) {
		Classe cl = (Classe) getClasse(prog);
		for(Iterator<org.javascool.proglets.plurialgo.langages.modele.Variable> iter=cl.proprietes.iterator(); iter.hasNext(); ) {
			Variable prop = (Variable) iter.next();
			if (prop.isOut()) continue;
			Argument arg = new Argument(this.nom+"."+prop.nom, prop.type, this.mode);
			arg.parent = this.parent;	// utile si arg tableau (déclaration indice pour)
			String msg1 =  prog.quote(arg.nom + " : ");
			arg.lireStandard(prog, buf, indent, msg1);
		}
	}
	
	private void lireTabClasseStandard(Programme prog, StringBuffer buf, int indent, String msg) {
		Classe cl = (Classe) getClasseOfTab(prog);
		this.addVariable(new Variable("ii","ENTIER"));
		Divers.ecrire(buf, "for(ii=0; ii<" + prog.getDim(1, this) + "; ii++) {", indent); 
		for(Iterator<org.javascool.proglets.plurialgo.langages.modele.Variable> iter=cl.proprietes.iterator(); iter.hasNext(); ) {
			Variable prop = (Variable) iter.next();
			Argument arg = new Argument(this.nom+"[ii]"+"."+prop.nom, prop.type, oteDim(1));
			arg.parent = this.parent;	// utile si arg tableau (déclaration indice pour)
			if (prop.isOut()) continue;
			String msg1 =  prog.quote(arg.nom + " : ");
			msg1=prog.quote(prop.nom + " : ");
			arg.lireStandard(prog, buf, indent+1, msg1);
		}
		Divers.ecrire(buf, "}", indent);
	}
	
// --------------------------------
// écriture d'arguments	(standard)
// --------------------------------
	
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
		Divers.indenter(buf, indent);
		if (msg!=null) Divers.ecrire(buf, "System.out.print(" + msg + "); ");
		Divers.ecrire(buf, "System.out.print(" + this.nom + "); ");
		if (msg!=null && !(prog.quote("\\t").equals(msg))) Divers.ecrire(buf, "System.out.println(); "); 
	}
	
	private void ecrireTabStandard(Programme prog, StringBuffer buf, int indent, String msg) {
		if (msg!=null) Divers.ecrire(buf, "System.out.println(" + msg + ");", indent);
		this.addVariable(new Variable("i1","ENTIER"));
		Divers.ecrire(buf, "for(i1=0; i1<" + prog.getDim(1, this) + "; i1++) {", indent);
		String msg1 = prog.quote("\\t");
		Argument arg = new Argument(this.nom+"[i1]", this.getTypeOfTab(), null);
		arg.ecrireStandard(prog, buf, indent+1, msg1);
		Divers.ecrire(buf, "}", indent);
		Divers.ecrire(buf, "System.out.println();", indent);
	}
	
	private void ecrireMatStandard(Programme prog, StringBuffer buf, int indent, String msg) {
		if (msg!=null) Divers.ecrire(buf, "System.out.println(" + msg + ");", indent); 
		this.addVariable(new Variable("i1","ENTIER"));
		this.addVariable(new Variable("j1","ENTIER"));
		Divers.ecrire(buf, "for(i1=0; i1<" + prog.getDim(1, this) + "; i1++) {", indent);
		Divers.ecrire(buf, "for(j1=0; j1<" + prog.getDim(2, this) + "; j1++) {", indent+1);
		String msg1 = prog.quote("\\t");
		Argument arg = new Argument(this.nom+"[i1][j1]", this.getTypeOfMat(), null);
		arg.ecrireStandard(prog, buf, indent+2, msg1);
		Divers.ecrire(buf, "}", indent+1);
		Divers.ecrire(buf, "System.out.println();", indent+1);
		Divers.ecrire(buf, "}", indent);
		Divers.ecrire(buf, "System.out.println();", indent);
	}
	
	private void ecrireClasseStandard(Programme prog, StringBuffer buf, int indent, String msg) {
		if (msg!=null) Divers.ecrire(buf, "System.out.println(" + msg + ");", indent); 
		Classe cl = (Classe) getClasse(prog);
		for(Iterator<org.javascool.proglets.plurialgo.langages.modele.Variable> iter=cl.proprietes.iterator(); iter.hasNext(); ) {
			Variable prop = (Variable) iter.next();
			if (prop.isIn()) continue;
			Argument arg = new Argument(this.nom+"."+prop.nom, prop.type, this.mode);	
			arg.parent = this.parent;	// utile si arg tableau (déclaration indice pour)
			String msg1 = prog.quote(arg.nom + " : ");
			arg.ecrireStandard(prog, buf, indent, msg1);
		}
	}
	
	private void ecrireTabClasseStandard(Programme prog, StringBuffer buf, int indent, String msg) {
		if (msg!=null) Divers.ecrire(buf, "System.out.println(" + msg + ");", indent); 
		Classe cl = (Classe) getClasseOfTab(prog);
		this.addVariable(new Variable("ii","ENTIER"));
		Divers.ecrire(buf, "for(ii=0; ii<" + prog.getDim(1, this) + "; ii++) {", indent);
		String msg1 = prog.quote("rang ") + " + ii + " + prog.quote(" de " + this.nom + " : ");
		Divers.ecrire(buf, "System.out.println(" + msg1 + "); ", indent+1); 
		for(Iterator<org.javascool.proglets.plurialgo.langages.modele.Variable> iter=cl.proprietes.iterator(); iter.hasNext(); ) {
			Variable prop = (Variable) iter.next();
			if (prop.isIn()) continue;
			Argument arg = new Argument(this.nom+"[ii]"+"."+prop.nom, prop.type, oteDim(1));
			arg.parent = this.parent;	// utile si arg tableau (déclaration indice pour)
			msg1=prog.quote(prop.nom + " : ");
			arg.ecrireStandard(prog, buf, indent+1, msg1);
		}
		Divers.ecrire(buf, "}", indent);
		Divers.ecrire(buf, "System.out.println();", indent);
	}
	
}
