/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2013.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.langages.xml;

import java.util.ArrayList;
import java.util.Iterator;

import org.javascool.proglets.plurialgo.langages.modele.InfoTypee;

/**
 * Cette classe hérite de la classe homonyme du modèle.
*/
public class Operation extends org.javascool.proglets.plurialgo.langages.modele.Operation {
	
	public Operation() {
	}
	
	public Operation(String nom) {
		this.nom = nom;
	}
	
	void traiterElementaireSaisieFonction(InfoTypee info, Instruction instr) {
		Operation oper = this;
		Argument arg = new Argument(info.nom, info.type, "OUT");
		instr.setRetour(arg);
		oper.setRetour( new Variable(info.nom, info.type, "OUT") );
		Instruction instr_oper = new Instruction("lire");
		Argument arg_oper = new Argument(info.nom, info.type, null);
		instr_oper.arguments.add(arg_oper);
		oper.instructions.add(instr_oper);
	}
	
	void traiterElementaireSaisieProcedure(ArrayList<InfoTypee> infos, Instruction instr) {
		Operation oper = this;
		Instruction instr_oper = new Instruction("lire");
		oper.instructions.add(instr_oper);
		for(Iterator<InfoTypee> iter=infos.iterator(); iter.hasNext(); ) {
			InfoTypee info = iter.next();
			if (info.isIn() || info.isInOut()) {
				Argument arg = new Argument(info.nom, info.type, "OUT");
				instr.arguments.add(arg);
				Argument arg_oper = new Argument(info.nom, info.type, null);
				arg_oper.dim = info.dim;
				instr_oper.arguments.add(arg_oper);
				Parametre param_oper = new Parametre(info.nom, info.type, "OUT");
				oper.parametres.add(param_oper);
			}
		}
	}
	
	void traiterElementaireAffichageProcedure(ArrayList<InfoTypee> infos, Instruction instr) {
		Operation oper = this;
		Instruction instr_oper = new Instruction("ecrire");
		oper.instructions.add(instr_oper);
		for(Iterator<InfoTypee> iter=infos.iterator(); iter.hasNext(); ) {
			InfoTypee info = iter.next();
			if (info.isOut() || info.isInOut()) {
				Argument arg = new Argument(info.nom, info.type, "IN");
				arg.dim = info.dim;
				instr.arguments.add(arg);
				Argument arg_oper = new Argument(info.nom, info.type, null);
				arg_oper.dim = info.dim;
				instr_oper.arguments.add(arg_oper);
				Parametre param_oper = new Parametre(info.nom, info.type, "IN");
				oper.parametres.add(param_oper);
			}
		}
	}
	
	void traiterElementaireCalculFonction(InfoTypee info, Instruction instr, 
				ArrayList<org.javascool.proglets.plurialgo.langages.modele.Parametre> param_donnees, 
				ArrayList<org.javascool.proglets.plurialgo.langages.modele.Argument> arg_donnees) {
		Operation oper = this;
		Argument arg = new Argument(info.nom, info.type, "OUT");
		instr.setRetour(arg);
		instr.arguments.addAll(arg_donnees);
		oper.setRetour( new Variable(info.nom, info.type, "OUT") );
		oper.parametres.addAll(param_donnees);
	}
	
	void traiterElementaireCalculProcedure(ArrayList<InfoTypee> infos, Instruction instr) {
		Operation oper = this;
		for(Iterator<InfoTypee> iter=infos.iterator(); iter.hasNext(); ) {
			InfoTypee info = iter.next();
			if (info.isIn() || info.isInOut() || info.isOut()) {
				Argument arg = new Argument(info.nom, info.type, info.mode);
				instr.arguments.add(arg);
				Parametre param_oper = new Parametre(info.nom, info.type, info.mode);
				oper.parametres.add(param_oper);
			}
		}
	}
	
	void traiterElementaireVariables(ArrayList<InfoTypee> infos) {
		InfoTypeeList liste = new InfoTypeeList();
		liste.addParametres(this.parametres);
		for(Iterator<InfoTypee> iter=infos.iterator(); iter.hasNext(); ) {
			InfoTypee info = iter.next();
			if (liste.getInfo(info.nom)!=null) continue;
			if (!info.isSimple() && !info.isTabSimple() && !info.isMatSimple()) continue;
			if (this.isFonction()) {
				if (info.nom.equals(this.getRetour().nom)) continue;
			}
			this.variables.add(new Variable(info.nom, info.type, null));
		}	
	}
	
}
