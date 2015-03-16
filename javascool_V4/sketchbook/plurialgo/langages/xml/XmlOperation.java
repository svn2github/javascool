/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2013.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.langages.xml;

import java.util.ArrayList;
import java.util.Iterator;
import org.javascool.proglets.plurialgo.langages.modele.*;

/**
 * Cette classe hérite de la classe homonyme du modèle.
*/
public class XmlOperation extends ModeleOperation {
	
	public XmlOperation() {
	}
	
	public XmlOperation(String nom) {
		this.nom = nom;
	}
	
	void traiterElementaireSaisieFonction(InfoTypee info, XmlInstruction instr) {
		XmlOperation oper = this;
		XmlArgument arg = new XmlArgument(info.nom, info.type, "OUT");
		instr.setRetour(arg);
		oper.setRetour( new XmlVariable(info.nom, info.type, "OUT") );
		XmlInstruction instr_oper = new XmlInstruction("lire");
		XmlArgument arg_oper = new XmlArgument(info.nom, info.type, null);
		instr_oper.arguments.add(arg_oper);
		oper.instructions.add(instr_oper);
	}
	
	void traiterElementaireSaisieProcedure(ArrayList<InfoTypee> infos, XmlInstruction instr) {
		XmlOperation oper = this;
		XmlInstruction instr_oper = new XmlInstruction("lire");
		oper.instructions.add(instr_oper);
		for(Iterator<InfoTypee> iter=infos.iterator(); iter.hasNext(); ) {
			InfoTypee info = iter.next();
			if (info.isIn() || info.isInOut()) {
				XmlArgument arg = new XmlArgument(info.nom, info.type, "OUT");
				instr.arguments.add(arg);
				XmlArgument arg_oper = new XmlArgument(info.nom, info.type, null);
				arg_oper.dim = info.dim;
				instr_oper.arguments.add(arg_oper);
				XmlParametre param_oper = new XmlParametre(info.nom, info.type, "OUT");
				oper.parametres.add(param_oper);
			}
		}
	}
	
	void traiterElementaireAffichageProcedure(ArrayList<InfoTypee> infos, XmlInstruction instr) {
		XmlOperation oper = this;
		XmlInstruction instr_oper = new XmlInstruction("ecrire");
		oper.instructions.add(instr_oper);
		for(Iterator<InfoTypee> iter=infos.iterator(); iter.hasNext(); ) {
			InfoTypee info = iter.next();
			if (info.isOut() || info.isInOut()) {
				XmlArgument arg = new XmlArgument(info.nom, info.type, "IN");
				arg.dim = info.dim;
				instr.arguments.add(arg);
				XmlArgument arg_oper = new XmlArgument(info.nom, info.type, null);
				arg_oper.dim = info.dim;
				instr_oper.arguments.add(arg_oper);
				XmlParametre param_oper = new XmlParametre(info.nom, info.type, "IN");
				oper.parametres.add(param_oper);
			}
		}
	}
	
	void traiterElementaireCalculFonction(InfoTypee info, XmlInstruction instr, 
				ArrayList<ModeleParametre> param_donnees, 
				ArrayList<ModeleArgument> arg_donnees) {
		XmlOperation oper = this;
		XmlArgument arg = new XmlArgument(info.nom, info.type, "OUT");
		instr.setRetour(arg);
		instr.arguments.addAll(arg_donnees);
		oper.setRetour( new XmlVariable(info.nom, info.type, "OUT") );
		oper.parametres.addAll(param_donnees);
	}
	
	void traiterElementaireCalculProcedure(ArrayList<InfoTypee> infos, XmlInstruction instr) {
		XmlOperation oper = this;
		for(Iterator<InfoTypee> iter=infos.iterator(); iter.hasNext(); ) {
			InfoTypee info = iter.next();
			if (info.isIn() || info.isInOut() || info.isOut()) {
				XmlArgument arg = new XmlArgument(info.nom, info.type, info.mode);
				instr.arguments.add(arg);
				XmlParametre param_oper = new XmlParametre(info.nom, info.type, info.mode);
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
			this.variables.add(new XmlVariable(info.nom, info.type, null));
		}	
	}
	
}
