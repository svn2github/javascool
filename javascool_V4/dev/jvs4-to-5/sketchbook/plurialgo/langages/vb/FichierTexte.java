/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2013.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.langages.vb;

import java.util.Iterator;

import org.javascool.proglets.plurialgo.divers.Divers;

/**
 * Cette classe permet de traduire en Visual Basic une instruction
 * de lecture ou d'écriture dans un fichier texte.
*/
public class FichierTexte {
	
	Instruction instr_pere;
	Argument arg_fichier;
	
	public FichierTexte(Instruction pere) {
		this.instr_pere = pere;
		arg_fichier = (Argument) pere.getFichier();
	}
	
	public void lireFichierTexte(Programme prog, StringBuffer buf, int indent) {
		instr_pere.addVariable(new Variable("n_lig","ENTIER"));
		Divers.ecrire(buf, "n_lig=0 // numero de ligne", indent);
		instr_pere.addVariable(new Variable("f_in","integer"));
		Divers.ecrire(buf, "f_in = FreeFile() ", indent);
		Divers.ecrire(buf, "Open " + arg_fichier.nom + " For Input As #f_in", indent);
		Divers.ecrire(buf, "while ( Not EOF(f_in) ) ", indent);
		for (Iterator<org.javascool.proglets.plurialgo.langages.modele.Argument> iter=instr_pere.arguments.iterator(); iter.hasNext();) {
			Argument arg = (Argument) iter.next();
			lireFichierTexte(prog, buf, indent+1, arg);
		}
		for (Iterator<org.javascool.proglets.plurialgo.langages.modele.Instruction> iter=arg_fichier.instructions.iterator(); iter.hasNext();) {
			Instruction instr = (Instruction) iter.next();
			instr.ecrire(prog, buf, indent+1);
		}
		prog.commenter(buf, "incrementation du compteur de lignes", indent+1);
		Divers.ecrire(buf, "n_lig=n_lig+1", indent+1);
		Divers.ecrire(buf, "wend", indent);
		Divers.ecrire(buf, "Close #f_in", indent);
	}
	
	public void ecrireFichierTexte(Programme prog, StringBuffer buf, int indent) {
		instr_pere.addVariable(new Variable("f_out","integer"));
		Divers.ecrire(buf, "f_out = FreeFile() ", indent);
		Divers.ecrire(buf, "Open " + arg_fichier.nom + " For Output As #f_out", indent);
		for (Iterator<org.javascool.proglets.plurialgo.langages.modele.Argument> iter=instr_pere.arguments.iterator(); iter.hasNext();) {
			Argument arg = (Argument) iter.next();
			String msg = prog.quote(arg.nom+" : ");
			ecrireFichierTexte(prog, buf, indent, msg, arg);
		}
		Divers.ecrire(buf, "Close #f_out", indent);
	}
	
// -------------------------------
// lecture d'arguments
// -------------------------------
	
	private void lireFichierTexte(Programme prog, StringBuffer buf, int indent, Argument arg) {
		if ( arg.isSimple() ) {
			lireSimpleFichierTexte(prog,buf,indent,arg);
		}
		if ( arg.isTabSimple()) {
			lireTabFichierTexte(prog,buf,indent,arg);
		}
		if ( arg.isTabClasse(prog)) {
			lireTabClasseFichierTexte(prog, buf, indent, arg);
		}
		if ( arg.isMatEntiers() || arg.isMatReels() || arg.isMatTextes() || arg.isMatBooleens()) {
			lireMatFichierTexte(prog,buf,indent,arg);
		}
		if ( arg.isEnregistrement(prog) || arg.isClasse(prog) ) {
			lireClasseFichierTexte(prog, buf, indent, arg);
		}
	}	
	
	private void lireSimpleFichierTexte(Programme prog, StringBuffer buf, int indent, Argument arg) {
		Divers.indenter(buf, indent);
		Divers.ecrire(buf, "Input #f_in, " + arg.nom);
	}
	
	private void lireTabFichierTexte(Programme prog, StringBuffer buf, int indent, Argument arg) {
		Argument arg1 = new Argument(arg.nom+"[n_lig]", arg.getTypeOfTab(), null);
		lireFichierTexte(prog, buf, indent, arg1);
	}
	
	private void lireMatFichierTexte(Programme prog, StringBuffer buf, int indent, Argument arg) {
		instr_pere.addVariable(new Variable("j1","ENTIER"));
		Divers.ecrire(buf, "for j1=0 to " + prog.getDim(2, arg) + "-1 ", indent);
		Argument arg1 = new Argument(arg.nom+"[n_lig][j1]", arg.getTypeOfMat(), null);
		lireFichierTexte(prog, buf, indent+1, arg1);
		Divers.ecrire(buf, "next j1", indent);
	}
	
	private void lireTabClasseFichierTexte(Programme prog, StringBuffer buf, int indent, Argument arg) {
		Classe cl = (Classe) arg.getClasseOfTab(prog);
		for(Iterator<org.javascool.proglets.plurialgo.langages.modele.Variable> iter=cl.proprietes.iterator(); iter.hasNext(); ) {
			Variable prop = (Variable) iter.next();
			if (prop.isOut()) continue;
			if ( prop.isSimple()) {
				Argument arg1 = new Argument(arg.nom+"."+prop.nom, "TAB_"+prop.type, arg.mode);
				lireFichierTexte(prog, buf, indent, arg1);
				Divers.remplacer(buf, arg.nom+"."+prop.nom+"[n_lig]", arg.nom+"[n_lig]"+"."+prop.nom);
			}
			if ( prop.isTabSimple()) {
				Argument arg1 = new Argument(arg.nom+"."+prop.nom, "MAT_"+prop.getTypeOfTab(), arg.mode);
				lireFichierTexte(prog, buf, indent, arg1);
				Divers.remplacer(buf, arg.nom+"."+prop.nom+"[n_lig][j1]", arg.nom+"[n_lig]"+"."+prop.nom+"[j1]");
			}
		}
	}
	
	private void lireClasseFichierTexte(Programme prog, StringBuffer buf, int indent, Argument arg) {
		Classe cl = (Classe) arg.getClasse(prog);
		for(Iterator<org.javascool.proglets.plurialgo.langages.modele.Variable> iter=cl.proprietes.iterator(); iter.hasNext(); ) {
			Variable prop = (Variable) iter.next();
			if (prop.isOut()) continue;
			Argument arg1 = new Argument(arg.nom+"."+prop.nom, prop.type, null);
			lireFichierTexte(prog, buf, indent, arg1);
		}
	}
	
// --------------------------------------
// écriture d'arguments	
// --------------------------------------
	
	private void ecrireFichierTexte(Programme prog, StringBuffer buf, int indent, String msg, Argument arg) {
		if ( arg.isSimple() ) {
			ecrireSimpleFichierTexte(prog,buf,indent,msg,arg);
		}
		if ( arg.isTabSimple()) {
			ecrireTabFichierTexte(prog,buf,indent,msg,arg);
		}
		if ( arg.isTabClasse(prog)) {
			ecrireTabClasseFichierTexte(prog, buf, indent, msg,arg);
		}
		if ( arg.isMatSimple() ) {
			ecrireMatFichierTexte(prog,buf,indent,msg,arg);
		}
		if ( arg.isEnregistrement(prog) || arg.isClasse(prog)) {
			ecrireClasseFichierTexte(prog, buf, indent, msg,arg);
		}
	}
	
	private void ecrireSimpleFichierTexte(Programme prog, StringBuffer buf, int indent, String msg, Argument arg) {
		if (msg!=null) Divers.ecrire(buf, "Print #f_out, " + msg + ", ", indent);
		Divers.ecrire(buf, "Print #f_out, " + arg.nom + ", ", indent);
		if (msg!=null && !(prog.quote("\\t").equals(msg))) Divers.ecrire(buf, "Print #f_out, ", indent); 
	}
	
	private void ecrireTabFichierTexte(Programme prog, StringBuffer buf, int indent, String msg, Argument arg) {
		if (msg!=null) Divers.ecrire(buf, "Print #f_out, " + msg, indent);
		instr_pere.addVariable(new Variable("i1","ENTIER"));
		Divers.ecrire(buf, "for i1=0 to " + prog.getDim(1, arg) + "-1 ", indent);
		String msg1 = prog.quote("\\t");
		Argument arg1 = new Argument(arg.nom+"[i1]", arg.getTypeOfTab(), null);
		ecrireFichierTexte(prog, buf, indent+1, msg1, arg1);
		Divers.ecrire(buf, "next i1", indent);
		Divers.ecrire(buf, "Print #f_out, ", indent); 
	}
	
	private void ecrireMatFichierTexte(Programme prog, StringBuffer buf, int indent, String msg, Argument arg) {
		if (msg!=null) Divers.ecrire(buf, "Print #f_out, " + msg, indent);
		instr_pere.addVariable(new Variable("i1","ENTIER"));
		instr_pere.addVariable(new Variable("j1","ENTIER"));
		Divers.ecrire(buf, "for i1=0 to " + prog.getDim(1, arg) + "-1 ", indent);
		Divers.ecrire(buf, "for j1=0 to " + prog.getDim(2, arg) + "-1 ", indent+1);
		String msg1 = prog.quote("\\t");;
		Argument arg1 = new Argument(arg.nom+"[i1][j1]", arg.getTypeOfMat(), null);
		ecrireFichierTexte(prog, buf, indent+2, msg1, arg1);
		Divers.ecrire(buf, "next j1", indent+1);
		Divers.ecrire(buf, "Print #f_out, ", indent+1); 
		Divers.ecrire(buf, "next i1", indent);
		Divers.ecrire(buf, "Print #f_out, ", indent); 	
	}
	
	private void ecrireClasseFichierTexte(Programme prog, StringBuffer buf, int indent, String msg, Argument arg) {
		if (msg!=null) Divers.ecrire(buf, "Print #f_out," + msg, indent);
		Classe cl = (Classe) arg.getClasse(prog);
		for(Iterator<org.javascool.proglets.plurialgo.langages.modele.Variable> iter=cl.proprietes.iterator(); iter.hasNext(); ) {
			Variable prop = (Variable) iter.next();
			if (prop.isIn()) continue;
			Argument arg1 = new Argument(arg.nom+"."+prop.nom, prop.type, arg.mode);
			String msg1 = prog.quote(arg.nom + " : ");
			ecrireFichierTexte(prog, buf, indent, msg1, arg1);
		}
	}
	
	private void ecrireTabClasseFichierTexte(Programme prog, StringBuffer buf, int indent, String msg, Argument arg) {
		if (msg!=null) Divers.ecrire(buf, "Print #f_out," + msg, indent);
		Classe cl = (Classe) arg.getClasseOfTab(prog);
		instr_pere.addVariable(new Variable("ii","ENTIER"));
		Divers.ecrire(buf, "for ii=0 to " + prog.getDim(1, arg) + "-1 ", indent);
		String msg1 = prog.quote("rang ") + " & ii & " + prog.quote(" de " + arg.nom + " : ");
		Divers.ecrire(buf, "Print #f_out, " + msg1 , indent+1); 
		for(Iterator<org.javascool.proglets.plurialgo.langages.modele.Variable> iter=cl.proprietes.iterator(); iter.hasNext(); ) {
			Variable prop = (Variable) iter.next();
			if (prop.isIn()) continue;
			Argument arg1 = new Argument(arg.nom+"[ii]"+"."+prop.nom, prop.type, arg.oteDim(1));
			msg1=prog.quote(prop.nom + " : ");
			ecrireFichierTexte(prog, buf, indent+1, msg1, arg1);
		}
		Divers.ecrire(buf, "next ii", indent);
		Divers.ecrire(buf, "Print #f_out, ", indent);
	}
	
}
