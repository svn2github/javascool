/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2013.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.langages.vb;

import java.util.Iterator;

import org.javascool.proglets.plurialgo.divers.Divers;

/**
 * Cette classe permet de traduire en Visual Basic une instruction
 * de lecture dans une feuille de calcul.
 * 
 * @author Raffinat Patrick
*/
public class Formulaire {
	
	Instruction instr_pere;
	Argument arg_form;
	
	public Formulaire(Instruction pere) {
		this.instr_pere = pere;
		arg_form = (Argument) pere.getFormulaire();
	}
	
	public void lireFormu(Programme prog, StringBuffer buf, int indent) {
		instr_pere.addVariable(new Variable("formu","Worksheet"));
		instr_pere.addVariable(new Variable("n_lig","ENTIER"));
		instr_pere.addVariable(new Variable("n_col","ENTIER"));
		Divers.ecrire(buf, "Set formu = Worksheets(1) ", indent);
		Divers.ecrire(buf, "n_lig=1", indent);
		for (Iterator<org.javascool.proglets.plurialgo.langages.modele.Argument> iter=instr_pere.arguments.iterator(); iter.hasNext();) {
			Argument arg = (Argument) iter.next();
			String msg = prog.quote(arg.nom + " : ");
			Divers.ecrire(buf, "n_col=1", indent);
			lireFormu(prog, buf, indent, msg, arg);
			Divers.ecrire(buf, "n_lig=n_lig+1", indent);
		}		
	}

// -------------------------------
// lecture d'arguments
// -------------------------------
	
	private void lireFormu(Programme prog, StringBuffer buf, int indent, String msg, Argument arg) {
		if ( arg.isSimple() ) {
			this.constrSimpleFormu(prog, buf, indent, msg, arg);
		}
		if ( arg.isTabSimple()) {
			this.constrTabFormu(prog, buf, indent, msg, arg, "i1");
		}
		if ( arg.isMatSimple() ) {
			this.constrMatFormu(prog, buf, indent, msg, arg);
		}
		if ( arg.isEnregistrement(prog) || arg.isClasse(prog) ) {
			constrClasseFormu(prog, buf, indent, msg, arg);
		}
		if (arg.isTabClasse(prog)) {
			constrTabClasseFormu(prog, buf, indent, msg, arg);
		}
	}
	
	private void constrLabelFormu(Programme prog, StringBuffer buf, int indent, String msg, Argument arg) {
		if (msg!=null) {
			Divers.ecrire(buf, "formu.cells(n_lig,n_col).value = " + msg, indent);
			Divers.ecrire(buf, "n_col=n_col+1", indent);
		}
	}
	
	private void constrSimpleFormu(Programme prog, StringBuffer buf, int indent, String msg, Argument arg) {
		this.constrLabelFormu(prog, buf, indent, msg, arg);
		Divers.ecrire(buf, "formu.cells(n_lig,n_col).Interior.ColorIndex = 6" , indent);
		Divers.ecrire(buf, arg.nom + " = formu.cells(n_lig,n_col).value ", indent);
	}
	
	private void constrTabFormu(Programme prog, StringBuffer buf, int indent, String msg, Argument arg, String indice) {
		this.constrLabelFormu(prog, buf, indent, msg, arg);	
		// la boucle
		instr_pere.addVariable(new Variable(indice,"ENTIER"));
		Divers.ecrire(buf, "for " + indice + "=1 to " + prog.getMaxTab() + "", indent);
		String msg1 = null;
		Argument arg1 = new Argument(arg.nom+"["+indice+"]", arg.getTypeOfTab(), arg.mode);
		lireFormu(prog, buf, indent+1, msg1, arg1);
		if (indice.equals("ii")) {
				Divers.ecrire(buf, "n_lig=n_lig+1", indent+1);
		}
		else {
				Divers.ecrire(buf, "n_col=n_col+1", indent+1);
		}
		Divers.ecrire(buf, "next "+indice, indent);
	}
	
	private void constrMatFormu(Programme prog, StringBuffer buf, int indent, String msg, Argument arg) {
		this.constrLabelFormu(prog, buf, indent, msg, arg);	
		// les boucles
		instr_pere.addVariable(new Variable("i1","ENTIER"));
		instr_pere.addVariable(new Variable("j1","ENTIER"));
		Divers.ecrire(buf, "for i1=1 to " + prog.getMaxTab() + "", indent);
		Divers.ecrire(buf, "for j1=1 to " + prog.getMaxTab() + "", indent+1);
		String msg1 = prog.quote("rang ") + " + str(i1) + " + prog.quote(", ") + " + str(j1) + " + prog.quote(" : ");
		msg1 = null;
		Argument arg1 = new Argument(arg.nom+"[i1][j1]", arg.getTypeOfMat(), arg.mode);
		lireFormu(prog, buf, indent+2, msg1, arg1);
		/* ajout */ Divers.ecrire(buf, "n_col=n_col+1", indent+2);
		Divers.ecrire(buf, "next j1", indent+1);
		Divers.ecrire(buf, "n_lig=n_lig+1", indent+1);
		Divers.ecrire(buf, "n_col=n_col-" + prog.getMaxTab(), indent+1);
		Divers.ecrire(buf, "next i1", indent);
	}
	
	private void constrClasseFormu(Programme prog, StringBuffer buf, int indent, String msg, Argument arg) {
		Classe cl = (Classe) arg.getClasse(prog);
		this.constrLabelFormu(prog, buf, indent, msg, arg);	
		for(Iterator<org.javascool.proglets.plurialgo.langages.modele.Variable> iter=cl.proprietes.iterator(); iter.hasNext(); ) {
			Variable prop = (Variable) iter.next();
			if (prop.isOut()) continue;
			String msg1 = prog.quote(prop.nom);
			Divers.ecrire(buf, "n_lig=n_lig+1", indent);
			Divers.ecrire(buf, "n_col=1", indent);
			Argument arg1 = new Argument(arg.nom+"."+prop.nom, prop.type, arg.mode);
			lireFormu(prog, buf, indent, msg1, arg1);
		}
	}
	
	private void constrTabClasseFormu(Programme prog, StringBuffer buf, int indent, String msg, Argument arg) {
		Classe cl = (Classe) arg.getClasseOfTab(prog);
		if (msg!=null) {
			Divers.ecrire(buf, "formu.cells(n_lig,n_col).value = " + msg, indent);
			Divers.ecrire(buf, "n_lig=n_lig+1", indent);
			Divers.ecrire(buf, "n_col=1", indent);
		}
		for(Iterator<org.javascool.proglets.plurialgo.langages.modele.Variable> iter=cl.proprietes.iterator(); iter.hasNext(); ) {
			Variable prop = (Variable) iter.next();
			if (prop.isOut()) continue;
			String msg1 = prog.quote(prop.nom);
			Divers.ecrire(buf, "formu.cells(n_lig,n_col).value = " + msg1, indent);
			if ( prop.isSimple()) {
				Divers.ecrire(buf, "n_col=n_col+1", indent);
			}
			if ( prop.isTabSimple()) {
				Divers.ecrire(buf, "n_col=n_col+" + prog.getMaxTab(), indent);
			}
		}
		Divers.ecrire(buf, "n_lig=n_lig+1", indent);
		Divers.ecrire(buf, "n_col=1", indent);
		for(Iterator<org.javascool.proglets.plurialgo.langages.modele.Variable> iter=cl.proprietes.iterator(); iter.hasNext(); ) {
			Variable prop = (Variable) iter.next();
			if (prop.isOut()) continue;
			if ( prop.isSimple()) {
				instr_pere.addVariable(new Variable("ii","ENTIER"));
				Argument arg1 = new Argument(arg.nom+"."+prop.nom, "TAB_"+prop.type, arg.mode);
				String msg1 = null; 
				constrTabFormu(prog, buf, indent, msg1, arg1, "ii");
				Divers.remplacer(buf, arg.nom+"."+prop.nom+"[ii]", arg.nom+"[ii]"+"."+prop.nom);
				Divers.ecrire(buf, "n_lig=n_lig-" + prog.getMaxTab(), indent);
				Divers.ecrire(buf, "n_col=n_col+1", indent);
			}
			if ( prop.isTabSimple()) {
				instr_pere.addVariable(new Variable("i1","ENTIER"));
				instr_pere.addVariable(new Variable("j1","ENTIER"));
				Argument arg1 = new Argument(arg.nom+"."+prop.nom, "MAT_"+prop.getTypeOfTab(), arg.mode);
				String msg1 = null;
				lireFormu(prog, buf, indent, msg1, arg1);
				Divers.remplacer(buf, arg.nom+"."+prop.nom+"[i1][j1]", arg.nom+"[i1]"+"."+prop.nom+"[j1]");
				Divers.ecrire(buf, "n_lig=n_lig-" + prog.getMaxTab(), indent);
				Divers.ecrire(buf, "n_col=n_col+" + prog.getMaxTab(), indent);
			}
		}
		Divers.ecrire(buf, "n_lig=n_lig+" + prog.getMaxTab(), indent);
		Divers.ecrire(buf, "n_col=1", indent);
	}
	
}


