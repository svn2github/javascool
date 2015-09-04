/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2013.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.langages.xml;


import java.util.*;
import org.javascool.proglets.plurialgo.divers.Divers;
import org.javascool.proglets.plurialgo.langages.modele.*;


/**
 * Cette classe implémente le mécanisme de transformation 1-n 
 * (bouton Transformer 1-n de l'onglet Résultats).
*/
public class ProgrammeVectorise extends XmlProgramme {

	private XmlProgramme prog; // le programme à vectoriser
	private ArrayList<InfoTypee> infos; // les variables du programme principal
	private ArrayList<InfoTypee> infosVect;  // les variables à vectoriser
	
	private String ii,nn;	
	private String debut,pas;
	private String sommeVar, sommeArg;
	private String miniVar, miniArg;
	private String maxiVar, maxiArg;
	private String compterVar, compterArg;
	private String chercherVar, chercherArg;
	private boolean avecTantque;

	/**
	 * 	Construit un programme par transformation 1-n
	 *    @param prog le programme à transformer (transformation précisée dans la propriété options du programme)
	*/	
	public ProgrammeVectorise(XmlProgramme prog) {
		this.prog = prog;
		this.nom = prog.nom;
		// récupération des classes et des operations
		for(Iterator<ModeleClasse> iter=prog.classes.iterator(); iter.hasNext(); ) {
			XmlClasse cl = (XmlClasse) iter.next();
			this.classes.add(cl);
		}
		for(Iterator<ModeleOperation> iter=prog.operations.iterator(); iter.hasNext(); ) {
			XmlOperation oper = (XmlOperation) iter.next();
			this.operations.add(oper);
		}
		// vectorisation
		this.initInfos();
		String mode;
		avecTantque = (prog.getOptionMode("tantque")!=null);
		ii="ii"; 
		if ((mode=prog.getOptionMode("pour_var"))!=null) {
			ii = mode.trim();
		}
		nn="nn"; 
		if ((mode=prog.getOptionMode("pour_fin"))!=null) {
			nn = mode.trim();
		}
		debut="1"; 
		if ((mode=prog.getOptionMode("pour_debut"))!=null) {
			debut = mode.trim();
		}
		pas="1"; 
		if ((mode=prog.getOptionMode("pour_pas"))!=null) {
			pas = mode.trim();
		}
		traiterStandard();
		// divers
		if (instructions.size()==0) instructions.add(new XmlInstruction("// ajouter des instructions"));
	}
	
	private void initInfos() {
		infos = new ArrayList<InfoTypee>();
		for(Iterator<ModeleVariable> iter=prog.variables.iterator(); iter.hasNext(); ) {
			XmlVariable var = (XmlVariable) iter.next();
			infos.add(new InfoTypee(var.nom, var.type, null));
		}
		infosVect = new ArrayList<InfoTypee>();
		String mode;
		if ((mode=prog.getOptionMode("vectorisation"))!=null) {
			StringTokenizer tok= new StringTokenizer(mode, " ,");
			while (tok.hasMoreTokens()) {
				String nom = tok.nextToken();
				InfoTypee info = InfoTypeeList.getInfo(nom, infos);
				if (info!=null) {
					info.nom = nom + "s";
					if (info.isSimple()) {
						info.type = "TAB_" + info.type;
					}
					else if (info.isTabSimple()) {
						info.type = "MAT_" + info.type.substring(4);
					}
					else if (info.isMatSimple()) {
					}
					else {
						info.type = "TAB_" + info.type;
					}
					InfoTypee infoVect = new InfoTypee(nom);
					infoVect.type = info.type;
					infosVect.add(infoVect);
				}
				else {
					info = new InfoTypee(nom+"s");
					info.type = "TAB_REEL";
					infos.add(info);	
					InfoTypee infoVect = new InfoTypee(nom);
					infoVect.type = info.type;
					infosVect.add(infoVect);	
				}
			}
		}
	}

	// -----------------------------------------------------------
	// vectorisation 
	// -----------------------------------------------------------

	private void traiterStandard(){
		// variables
		XmlVariable var;
		for(Iterator<InfoTypee> iter=infos.iterator(); iter.hasNext(); ) {
			InfoTypee info = iter.next();
			var = new XmlVariable(info.nom, info.type, null);
			variables.add(var);
		}
		InfoTypeeList liste = new InfoTypeeList();
		liste.addVariables(variables);
		if (Divers.isIdent(ii)) {
			var = new XmlVariable(ii, "ENTIER", null);
			if (liste.getInfo(ii)==null) {
				variables.add(0, var); liste.addInfo(var);
			}
		}
		if (Divers.isIdent(debut)) {
			var = new XmlVariable(debut, "ENTIER", null);
			if (liste.getInfo(debut)==null) {
				variables.add(0, var); liste.addInfo(var);
			}
		}
		if (Divers.isIdent(nn)) {
			var = new XmlVariable(nn, "ENTIER", null);
			if (liste.getInfo(nn)==null) {
				variables.add(0, var); liste.addInfo(var);
			}
		}
		if (Divers.isIdent(pas)) {
			var = new XmlVariable(pas, "ENTIER", null);
			if (liste.getInfo(pas)==null) {
				variables.add(0, var); liste.addInfo(var);
			}
		}
		for(Iterator<ModeleArgument> iter=prog.options.iterator(); iter.hasNext(); ) {
			XmlArgument option = (XmlArgument) iter.next();
			initOption(option);
			if (sommeVar!=null) {
				var = new XmlVariable(sommeVar, "REEL", null);
				if (liste.getInfo(sommeVar)==null) {
					variables.add(var); liste.addInfo(var);
				}
			}
			if (compterVar!=null) {
				var = new XmlVariable(compterVar, "ENTIER", null);
				if (liste.getInfo(compterVar)==null) {
					variables.add(var); liste.addInfo(var);
				}
			}
			if (miniVar!=null) {
				var = new XmlVariable(miniVar, "REEL", null);
				if (liste.getInfo(miniVar)==null) {
					variables.add(var); liste.addInfo(var);
				}
			}
			if (maxiVar!=null) {
				var = new XmlVariable(maxiVar, "REEL", null);
				if (liste.getInfo(maxiVar)==null) {
					variables.add(var); liste.addInfo(var);
				}
			}
			if (chercherVar!=null) {
				var = new XmlVariable(chercherVar, "BOOLEEN", null);
				if (liste.getInfo(chercherVar)==null) {
					variables.add(var); liste.addInfo(var);
				}
			}
		}
		// saisie
		traiterStandardSaisie();
		// calculs
		traiterStandardCalcul();
		// affichage
		traiterStandardAffichage();
	}

	private void initOption(XmlArgument option) {
		String mode = option.mode;
		if ((option.nom.equals("sommation")) && (mode!=null)) {
			sommeVar = null;
			int i = mode.indexOf(":");
			if (i>=0) {
				sommeVar = mode.substring(0, i).trim();
				sommeArg = mode.substring(i+1, mode.length()).trim();
			}
		}
		if ((option.nom.equals("comptage")) && (mode!=null)) {
			compterVar = null;
			int i = mode.indexOf(":");
			if (i>=0) {
				compterVar = mode.substring(0, i).trim();
				compterArg = mode.substring(i+1, mode.length()).trim();
			}
		}
		if ((option.nom.equals("minimum")) && (mode!=null)) {
			miniVar = null;
			int i = mode.indexOf(":");
			if (i>=0) {
				miniVar = mode.substring(0, i).trim();
				miniArg = mode.substring(i+1, mode.length()).trim();
			}
		}
		if ((option.nom.equals("maximum")) && (mode!=null)) {
			maxiVar = null;
			int i = mode.indexOf(":");
			if (i>=0) {
				maxiVar = mode.substring(0, i).trim();
				maxiArg = mode.substring(i+1, mode.length()).trim();
			}
		}
		if ((option.nom.equals("recherche")) && (mode!=null)) {
			chercherVar = "stopper";
			//if (!ii.isEmpty()) chercherVar = chercherVar + "_" + ii;
			chercherArg = mode;
		}
	}
			
	private void traiterStandardSaisie() {
		XmlInstruction instr;
		XmlArgument arg;
		instr = new XmlInstruction("lire");
		if (Divers.isIdent(nn)) {
			arg = new XmlArgument(nn, "ENTIER", null);
			instr.arguments.add(arg);
		}
		if (Divers.isIdent(debut)) {
			arg = new XmlArgument(debut, "ENTIER", null);
			instr.arguments.add(arg);
		}
		if (Divers.isIdent(pas)) {
			arg = new XmlArgument(pas, "ENTIER", null);
			instr.arguments.add(arg);
		}
		if (instr.arguments.size()>0) {
			instructions.add(instr);
		}
	}
	
	private void traiterStandardAffichage() {
		XmlInstruction instr;
		XmlArgument arg;
		instr = new XmlInstruction("ecrire");
		for(Iterator<ModeleArgument> iter=prog.options.iterator(); iter.hasNext(); ) {
			XmlArgument option = (XmlArgument) iter.next();
			initOption(option);
			if (option.nom.equals("sommation") && sommeVar!=null) {
				arg = new XmlArgument(sommeVar, "REEL", null);
				instr.arguments.add(arg);
			}
			if (option.nom.equals("comptage") && compterVar!=null) {
				arg = new XmlArgument(compterVar, "ENTIER", null);
				instr.arguments.add(arg);
			}
			if (option.nom.equals("minimum") && miniVar!=null) {
				arg = new XmlArgument(miniVar, "REEL", null);
				instr.arguments.add(arg);
			}
			if (option.nom.equals("maximum") && maxiVar!=null) {
				arg = new XmlArgument(maxiVar, "REEL", null);
				instr.arguments.add(arg);
			}
			if (option.nom.equals("recherche") && chercherVar!=null) {
				arg = new XmlArgument(chercherVar, "BOOLEEN", null);
				instr.arguments.add(arg);
			}
		}
		if (instr.arguments.size()>0) {
			instructions.add(instr);
		}	
	}
	
	private void traiterStandardCalcul() {
		// avant la boucle
		for(Iterator<ModeleArgument> iter=prog.options.iterator(); iter.hasNext(); ) {
			XmlArgument option = (XmlArgument) iter.next();
			initOption(option);
			if (option.nom.equals("sommation") && sommeVar!=null) {
				XmlInstruction instr = XmlInstruction.creerInstructionAffectation(sommeVar, "0");
				instructions.add(instr);
			}
			if (option.nom.equals("comptage") && compterVar!=null) {
				XmlInstruction instr = XmlInstruction.creerInstructionAffectation(compterVar, "0");
				instructions.add(instr);
			}
			if (option.nom.equals("minimum") && miniVar!=null) {
				XmlInstruction instr = XmlInstruction.creerInstructionAffectation(miniVar, "1000");
				instructions.add(instr);
			}
			if (option.nom.equals("maximum") && maxiVar!=null) {
				XmlInstruction instr = XmlInstruction.creerInstructionAffectation(maxiVar, "-1000");
				instructions.add(instr);
			}
			if (option.nom.equals("recherche") && chercherVar!=null) {
				XmlInstruction instr = XmlInstruction.creerInstructionAffectation(chercherVar, "FAUX");
				instructions.add(instr);
			}
		}
		// Pour ou tantque ?
		XmlPour pour = null;
		XmlTantQue tq = null;
		XmlInstruction instr_iter = null;
		if (nn.isEmpty()) this.avecTantque = true;
		if (ii.isEmpty()) this.avecTantque = true;
		if (debut.isEmpty()) this.avecTantque = true;
		if (pas.isEmpty()) this.avecTantque = true;
		if (chercherVar!=null) this.avecTantque = true;
		if (this.avecTantque) {
			instr_iter = new XmlInstruction("tantque");
			tq = new XmlTantQue();
			tq.condition = "";
			if (!nn.isEmpty() && !ii.isEmpty() && !debut.isEmpty() && !pas.isEmpty()) {
				if (pas.startsWith("-")) {
					tq.condition = "(" + ii + ">=" + nn + ")";
				}
				else {
					tq.condition = "(" + ii + "<=" + nn + ")";
				}
			}
			if (chercherVar!=null) {
				if (!tq.condition.isEmpty()) tq.condition = tq.condition + " ET ";
				tq.condition = tq.condition + "(" + chercherVar + "==" + "FAUX" + ")";
			}
			String mode_tq;
			if ((mode_tq=prog.getOptionMode("tantque"))!=null) {
				if (!mode_tq.isEmpty()) {
					if (!tq.condition.isEmpty()) tq.condition = tq.condition + " ET ";
					tq.condition = tq.condition + mode_tq;	
				}
			}
			if (tq.condition.contains(" ET ") ) tq.condition = "(" + tq.condition + ")";
			instr_iter.tantques.add(tq);
			if (!ii.isEmpty() && !debut.isEmpty()) {
				instructions.add( XmlInstruction.creerInstructionAffectation(ii, debut) );
			}
			instructions.add(instr_iter);
		}
		else {
			instr_iter = new XmlInstruction("pour");
			pour = new XmlPour(); 
			pour.var = ii; pour.debut = debut; pour.fin = nn; pour.pas = pas;
			instr_iter.pours.add(pour);
			instructions.add(instr_iter);
		}
		// la boucle
		for(Iterator<ModeleInstruction> iter=prog.instructions.iterator(); iter.hasNext(); ) {
			XmlInstruction instr = (XmlInstruction) iter.next();
			if (instr.isLecture() && prog.getOption("donnees")!=null) continue;
			if (instr.isEcriture()&& prog.getOption("resultats")!=null) continue;
			instr = this.vectoriser(instr);
			if (pour!=null) pour.instructions.add(instr);
			if (tq!=null) tq.instructions.add(instr);
		}
		for(Iterator<ModeleArgument> iter=prog.options.iterator(); iter.hasNext(); ) {
			XmlArgument option = (XmlArgument) iter.next();
			initOption(option);
			if (option.nom.equals("sommation") && sommeVar!=null) {
				XmlInstruction instr = XmlInstruction.creerInstructionAffectation(sommeVar, vectoriser(sommeVar + " + " + sommeArg));
				if (pour!=null) pour.instructions.add(instr);
				if (tq!=null) tq.instructions.add(instr);
			}
			if (option.nom.equals("comptage") && compterVar!=null) {
				XmlInstruction instr = new XmlInstruction("si");
				XmlSi si = new XmlSi(); 
				si.condition = vectoriser(compterArg);
				si.instructions.add( XmlInstruction.creerInstructionAffectation(compterVar, compterVar + "+1") );
				instr.sis.add(si);
				if (pour!=null) pour.instructions.add(instr);
				if (tq!=null) tq.instructions.add(instr);
			}
			if (option.nom.equals("minimum") && miniVar!=null) {
				XmlInstruction instr = new XmlInstruction("si");
				XmlSi si = new XmlSi(); 
				si.condition = vectoriser(miniArg + " < " + miniVar);
				si.instructions.add( XmlInstruction.creerInstructionAffectation(miniVar, vectoriser(miniArg)) );
				instr.sis.add(si);
				if (pour!=null) pour.instructions.add(instr);
				if (tq!=null) tq.instructions.add(instr);
			}
			if (option.nom.equals("maximum") && maxiVar!=null) {
				XmlInstruction instr = new XmlInstruction("si");
				XmlSi si = new XmlSi(); 
				si.condition = vectoriser(maxiArg + " > " + maxiVar);
				si.instructions.add( XmlInstruction.creerInstructionAffectation(maxiVar, vectoriser(maxiArg)) );
				instr.sis.add(si);
				if (pour!=null) pour.instructions.add(instr);
				if (tq!=null) tq.instructions.add(instr);
			}
			if (option.nom.equals("recherche") && chercherVar!=null) {
				XmlInstruction instr = new XmlInstruction("si");
				XmlSi si = new XmlSi(); 
				si.condition = vectoriser(chercherArg);
				si.instructions.add( XmlInstruction.creerInstructionAffectation(chercherVar, "VRAI") );
				instr.sis.add(si);
				if (pour!=null) pour.instructions.add(instr);
				if (tq!=null) tq.instructions.add(instr);
			}
		}
		if (pour!=null) {
			if (pour.instructions.size()==0) {
				pour.instructions.add(new XmlInstruction("// ajouter des instructions"));
			}
		}
		if (tq!=null) {
			if (!ii.isEmpty() && !pas.isEmpty()) {
				if (pas.startsWith("-")) {
					tq.instructions.add( XmlInstruction.creerInstructionAffectation(ii, ii + pas) );
				}
				else {
					tq.instructions.add( XmlInstruction.creerInstructionAffectation(ii, ii + "+" + pas) );
				}
			}
			else if (tq.instructions.size()==0) {
				tq.instructions.add(new XmlInstruction("// ajouter des instructions"));
			}
		}
	}
	
	private XmlInstruction vectoriser(XmlInstruction instr){
		XmlInstruction copie = new XmlInstruction(instr.nom); 
		if (instr.isSi()) {
			for (Iterator<ModeleSi> iter=instr.sis.iterator(); iter.hasNext();) {
				XmlSi si = (XmlSi) iter.next();
				copie.sis.add(this.vectoriser(si));
			}
		}
		else if (instr.isPour()) {
			for (Iterator<ModelePour> iter=instr.pours.iterator(); iter.hasNext();) {
				XmlPour pour = (XmlPour) iter.next();
				copie.pours.add(this.vectoriser(pour));
			}
		}
		else if (instr.isTantQue()) {
			for (Iterator<ModeleTantQue> iter=instr.tantques.iterator(); iter.hasNext();) {
				XmlTantQue tq = (XmlTantQue) iter.next();
				copie.tantques.add(this.vectoriser(tq));
			}
		}
		else if (instr.isAffectation()) {
			for (Iterator<ModeleAffectation> iter=instr.affectations.iterator(); iter.hasNext();) {
				XmlAffectation aff = (XmlAffectation) iter.next();
				copie.affectations.add(this.vectoriser(aff));
			}
		}
		else {
			copie = this.vectoriserArguments(instr);
		}
		return copie;
	}
	
	private XmlSi vectoriser(XmlSi si){
		XmlSi copie = new XmlSi();
		copie.condition = vectoriser(si.condition);
		copie.schema = si.schema;
		for (Iterator<ModeleInstruction> iter=si.instructions.iterator(); iter.hasNext();) {
			XmlInstruction instr = (XmlInstruction) iter.next();
			copie.instructions.add(this.vectoriser(instr));
		}
		return (copie);
	}
	
	private XmlPour vectoriser(XmlPour pour){
		XmlPour copie = new XmlPour();
		copie.var = pour.var;
		copie.debut = pour.debut;
		copie.fin = vectoriser(pour.fin);
		copie.pas = pour.pas;
		copie.schema = pour.schema;
		for (Iterator<ModeleInstruction> iter=pour.instructions.iterator(); iter.hasNext();) {
			XmlInstruction instr = (XmlInstruction) iter.next();
			copie.instructions.add(this.vectoriser(instr));
		}
		return (copie);
	}
	
	private XmlTantQue vectoriser(XmlTantQue tq){
		XmlTantQue copie = new XmlTantQue();
		copie.condition = vectoriser(tq.condition);
		copie.schema = tq.schema;
		for (Iterator<ModeleInstruction> iter=tq.instructions.iterator(); iter.hasNext();) {
			XmlInstruction instr = (XmlInstruction) iter.next();
			copie.instructions.add(this.vectoriser(instr));
		}
		return (copie);
	}
	
	private XmlAffectation vectoriser(XmlAffectation aff){
		XmlAffectation copie = new XmlAffectation();
		copie.var = vectoriser(aff.var);
		copie.expression = vectoriser(aff.expression);
		return (copie);
	}
	
	private XmlInstruction vectoriserArguments(XmlInstruction instr) {
		XmlArgument arg, arg_copie;
		XmlInstruction copie = new XmlInstruction(instr.nom);
		for (Iterator<ModeleArgument> iter=instr.arguments.iterator(); iter.hasNext();) {
			arg = (XmlArgument) iter.next();
			arg_copie = new XmlArgument(arg.nom, arg.type, arg.mode);
			copie.arguments.add(arg_copie);
			arg_copie.nom = vectoriser(arg.nom);
		}
		arg = (XmlArgument) instr.getRetour();
		if (arg!=null) {
			arg_copie = new XmlArgument(arg.nom, arg.type, arg.mode);
			copie.retours.add(arg_copie);
			arg_copie.nom = vectoriser(arg.nom);
		}
		arg = (XmlArgument) instr.getObjet();
		if (arg!=null) {
			arg_copie = new XmlArgument(arg.nom, arg.type, arg.mode);
			copie.objets.add(arg_copie);
			arg_copie.nom = vectoriser(arg.nom);
		}
		return copie;
	}
	
	private String vectoriser(String txt){
		if (txt==null) return "";
		StringBuffer buf = new StringBuffer(" " + txt + " ");
		for (Iterator<InfoTypee> iter=this.infosVect.iterator(); iter.hasNext();) {
			InfoTypee info = iter.next();
			String ancien = info.nom;
			int lg_ancien = ancien.length();
			String ch;
			String nouveau;
			for (int i=buf.length()-lg_ancien - 1; i>=1; i--) {
				if (ancien.equals(buf.substring(i, i+lg_ancien))) {
					ch = buf.substring(i-1, i);
					if  ( (ch.compareTo("A")>=0) && (ch.compareTo("Z")<=0) ) continue;
					if  ( (ch.compareTo("a")>=0) && (ch.compareTo("z")<=0) ) continue;
					if  ( ch.equals("_") ) continue;
					if  ( ch.equals("'") ) continue;
					if  ( ch.equals(".") ) continue;
					ch = buf.substring(i+lg_ancien, i+lg_ancien+1);
					if  ( (ch.compareTo("A")>=0) && (ch.compareTo("Z")<=0) ) continue;
					if  ( (ch.compareTo("a")>=0) && (ch.compareTo("z")<=0) ) continue;
					if  ( ch.equals("_") ) continue;
					if  ( ch.equals("'") ) continue;
					if  ( (ch.compareTo("0")>=0) && (ch.compareTo("9")<=0) ) continue;
					nouveau = ancien + "s[" + ii +"]";
					System.out.println("nouveau:"+nouveau);
					buf.delete(i, i+lg_ancien);
					buf.insert(i, nouveau);
				}
			}		
		}
		buf.delete(0, 1); buf.delete(buf.length()-1, buf.length());
		return buf.toString();
	}
}
