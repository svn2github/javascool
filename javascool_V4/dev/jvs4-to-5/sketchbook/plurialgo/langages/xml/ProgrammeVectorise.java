/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2013.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.langages.xml;


import java.util.*;

import org.javascool.proglets.plurialgo.divers.Divers;
import org.javascool.proglets.plurialgo.langages.modele.InfoTypee;



/**
 * Cette classe implémente le mécanisme de transformation 1-n 
 * (bouton Transformer 1-n de l'onglet Résultats).
*/
public class ProgrammeVectorise extends Programme {

	private Programme prog; // le programme à vectoriser
	private ArrayList<InfoTypee> infos; // les variables du programme principal
	private ArrayList<InfoTypee> infosVect;  // les variables à vectoriser
	
	private String ii,nn;
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
	public ProgrammeVectorise(Programme prog) {
		this.prog = prog;
		this.nom = prog.nom;
		// récupération des classes et des operations
		for(Iterator<org.javascool.proglets.plurialgo.langages.modele.Classe> iter=prog.classes.iterator(); iter.hasNext(); ) {
			Classe cl = (Classe) iter.next();
			this.classes.add(cl);
		}
		for(Iterator<org.javascool.proglets.plurialgo.langages.modele.Operation> iter=prog.operations.iterator(); iter.hasNext(); ) {
			Operation oper = (Operation) iter.next();
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
		nn="nn"; //while (InfoTypee.getInfo(nn, prog.variables)!=null) nn=nn+"i";
		if ((mode=prog.getOptionMode("pour_fin"))!=null) {
			nn = mode.trim();
		}
		traiterStandard();
		// divers
		if (instructions.size()==0) instructions.add(new Instruction("// ajouter des instructions"));
	}
	
	private void initInfos() {
		infos = new ArrayList<InfoTypee>();
		for(Iterator<org.javascool.proglets.plurialgo.langages.modele.Variable> iter=prog.variables.iterator(); iter.hasNext(); ) {
			Variable var = (Variable) iter.next();
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
		Variable var;
		for(Iterator<InfoTypee> iter=infos.iterator(); iter.hasNext(); ) {
			InfoTypee info = iter.next();
			var = new Variable(info.nom, info.type, null);
			variables.add(var);
		}
		InfoTypeeList liste = new InfoTypeeList();
		liste.addVariables(variables);
		if (isIdent(nn)) {
			var = new Variable(nn, "ENTIER", null);
			if (liste.getInfo(nn)==null) {
				variables.add(0, var); liste.addInfo(var);
			}
		}
		var = new Variable(ii, "ENTIER", null);
		if (liste.getInfo(ii)==null && !ii.isEmpty()) {
			variables.add(var); liste.addInfo(var);
		}
		for(Iterator<org.javascool.proglets.plurialgo.langages.modele.Argument> iter=prog.options.iterator(); iter.hasNext(); ) {
			Argument option = (Argument) iter.next();
			initOption(option);
			if (sommeVar!=null) {
				var = new Variable(sommeVar, "REEL", null);
				if (liste.getInfo(sommeVar)==null) {
					variables.add(var); liste.addInfo(var);
				}
			}
			if (compterVar!=null) {
				var = new Variable(compterVar, "ENTIER", null);
				if (liste.getInfo(compterVar)==null) {
					variables.add(var); liste.addInfo(var);
				}
			}
			if (miniVar!=null) {
				var = new Variable(miniVar, "REEL", null);
				if (liste.getInfo(miniVar)==null) {
					variables.add(var); liste.addInfo(var);
				}
			}
			if (maxiVar!=null) {
				var = new Variable(maxiVar, "REEL", null);
				if (liste.getInfo(maxiVar)==null) {
					variables.add(var); liste.addInfo(var);
				}
			}
			if (chercherVar!=null) {
				var = new Variable(chercherVar, "BOOLEEN", null);
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
	
	private boolean isIdent(String txt) {
		if (txt.isEmpty()) return false;
		String ch = txt.substring(0,1);
		if (!Divers.isLettre(ch)) return false;
		for(int i=0;i<txt.length(); i++) {
			ch = txt.substring(i,i+1);
			if (!Divers.isChiffre(ch) && !Divers.isLettre(ch)) return false;
		}
		return true;
	}
	
	private void initOption(Argument option) {
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
			chercherVar = "trouve";
			//if (!ii.isEmpty()) chercherVar = chercherVar + "_" + ii;
			chercherArg = mode;
		}
	}
			
	private void traiterStandardSaisie() {
		Instruction instr;
		Argument arg;
		instr = new Instruction("lire");
		if (isIdent(nn)) {
			arg = new Argument(nn, "ENTIER", null);
			instr.arguments.add(arg);
			instructions.add(instr);
		}
	}
	
	private void traiterStandardAffichage() {
		Instruction instr;
		Argument arg;
		instr = new Instruction("ecrire");
		for(Iterator<org.javascool.proglets.plurialgo.langages.modele.Argument> iter=prog.options.iterator(); iter.hasNext(); ) {
			Argument option = (Argument) iter.next();
			initOption(option);
			if (option.nom.equals("sommation") && sommeVar!=null) {
				arg = new Argument(sommeVar, "REEL", null);
				instr.arguments.add(arg);
			}
			if (option.nom.equals("comptage") && compterVar!=null) {
				arg = new Argument(compterVar, "ENTIER", null);
				instr.arguments.add(arg);
			}
			if (option.nom.equals("minimum") && miniVar!=null) {
				arg = new Argument(miniVar, "REEL", null);
				instr.arguments.add(arg);
			}
			if (option.nom.equals("maximum") && maxiVar!=null) {
				arg = new Argument(maxiVar, "REEL", null);
				instr.arguments.add(arg);
			}
			if (option.nom.equals("recherche") && chercherVar!=null) {
				arg = new Argument(chercherVar, "BOOLEEN", null);
				instr.arguments.add(arg);
			}
		}
		if (instr.arguments.size()>0) {
			instructions.add(instr);
		}	
	}
	
	private void traiterStandardCalcul() {
		// avant la boucle
		for(Iterator<org.javascool.proglets.plurialgo.langages.modele.Argument> iter=prog.options.iterator(); iter.hasNext(); ) {
			Argument option = (Argument) iter.next();
			initOption(option);
			if (option.nom.equals("sommation") && sommeVar!=null) {
				Instruction instr = Instruction.creerInstructionAffectation(sommeVar, "0");
				instructions.add(instr);
			}
			if (option.nom.equals("comptage") && compterVar!=null) {
				Instruction instr = Instruction.creerInstructionAffectation(compterVar, "0");
				instructions.add(instr);
			}
			if (option.nom.equals("minimum") && miniVar!=null) {
				Instruction instr = Instruction.creerInstructionAffectation(miniVar, "1000");
				instructions.add(instr);
			}
			if (option.nom.equals("maximum") && maxiVar!=null) {
				Instruction instr = Instruction.creerInstructionAffectation(maxiVar, "-1000");
				instructions.add(instr);
			}
			if (option.nom.equals("recherche") && chercherVar!=null) {
				Instruction instr = Instruction.creerInstructionAffectation(chercherVar, "FAUX");
				instructions.add(instr);
			}
		}
		// Pour ou tantque ?
		Pour pour = null;
		TantQue tq = null;
		Instruction instr_iter = null;
		if (nn.isEmpty()) this.avecTantque = true;
		if (ii.isEmpty()) this.avecTantque = true;
		if (chercherVar!=null) this.avecTantque = true;
		if (this.avecTantque) {
			instr_iter = new Instruction("tantque");
			tq = new TantQue();
			tq.condition = "";
			if (!nn.isEmpty() && !ii.isEmpty()) tq.condition = "(" + ii + "<=" + nn + ")";
			if (chercherVar!=null) {
				if (!tq.condition.isEmpty()) tq.condition = tq.condition + " ET ";
				tq.condition = tq.condition + "(" + chercherVar + "==" + "FAUX" + ")";
			}
			if (tq.condition.contains(" ET ") ) tq.condition = "(" + tq.condition + ")";
			instr_iter.tantques.add(tq);
			if (!ii.isEmpty()) {
				instructions.add( Instruction.creerInstructionAffectation(ii, "1") );
			}
			instructions.add(instr_iter);
		}
		else {
			instr_iter = new Instruction("pour");
			pour = new Pour(); 
			pour.var = ii; pour.debut = "1"; pour.fin = nn; pour.pas = "1";
			instr_iter.pours.add(pour);
			instructions.add(instr_iter);
		}
		// la boucle
		for(Iterator<org.javascool.proglets.plurialgo.langages.modele.Instruction> iter=prog.instructions.iterator(); iter.hasNext(); ) {
			Instruction instr = (Instruction) iter.next();
			if (instr.isLecture() && prog.getOption("donnees")!=null) continue;
			if (instr.isEcriture()&& prog.getOption("resultats")!=null) continue;
			instr = this.vectoriser(instr);
			if (pour!=null) pour.instructions.add(instr);
			if (tq!=null) tq.instructions.add(instr);
		}
		for(Iterator<org.javascool.proglets.plurialgo.langages.modele.Argument> iter=prog.options.iterator(); iter.hasNext(); ) {
			Argument option = (Argument) iter.next();
			initOption(option);
			if (option.nom.equals("sommation") && sommeVar!=null) {
				Instruction instr = Instruction.creerInstructionAffectation(sommeVar, vectoriser(sommeVar + " + " + sommeArg));
				if (pour!=null) pour.instructions.add(instr);
				if (tq!=null) tq.instructions.add(instr);
			}
			if (option.nom.equals("comptage") && compterVar!=null) {
				Instruction instr = new Instruction("si");
				Si si = new Si(); 
				si.condition = vectoriser(compterArg);
				si.instructions.add( Instruction.creerInstructionAffectation(compterVar, compterVar + "+1") );
				instr.sis.add(si);
				if (pour!=null) pour.instructions.add(instr);
				if (tq!=null) tq.instructions.add(instr);
			}
			if (option.nom.equals("minimum") && miniVar!=null) {
				Instruction instr = new Instruction("si");
				Si si = new Si(); 
				si.condition = vectoriser(miniArg + " < " + miniVar);
				si.instructions.add( Instruction.creerInstructionAffectation(miniVar, vectoriser(miniArg)) );
				instr.sis.add(si);
				if (pour!=null) pour.instructions.add(instr);
				if (tq!=null) tq.instructions.add(instr);
			}
			if (option.nom.equals("maximum") && maxiVar!=null) {
				Instruction instr = new Instruction("si");
				Si si = new Si(); 
				si.condition = vectoriser(maxiArg + " > " + maxiVar);
				si.instructions.add( Instruction.creerInstructionAffectation(maxiVar, vectoriser(maxiArg)) );
				instr.sis.add(si);
				if (pour!=null) pour.instructions.add(instr);
				if (tq!=null) tq.instructions.add(instr);
			}
			if (option.nom.equals("recherche") && chercherVar!=null) {
				Instruction instr = new Instruction("si");
				Si si = new Si(); 
				si.condition = vectoriser(chercherArg);
				si.instructions.add( Instruction.creerInstructionAffectation(chercherVar, "VRAI") );
				instr.sis.add(si);
				if (pour!=null) pour.instructions.add(instr);
				if (tq!=null) tq.instructions.add(instr);
			}
		}
		if (pour!=null) {
			if (pour.instructions.size()==0) {
				pour.instructions.add(new Instruction("// ajouter des instructions"));
			}
		}
		if (tq!=null) {
			if (!ii.isEmpty()) {
				tq.instructions.add( Instruction.creerInstructionAffectation(ii, ii + "+1") );
			}
			else if (tq.instructions.size()==0) {
				tq.instructions.add(new Instruction("// ajouter des instructions"));
			}
		}
	}
	
	private Instruction vectoriser(Instruction instr){
		Instruction copie = new Instruction(instr.nom); 
		if (instr.isSi()) {
			for (Iterator<org.javascool.proglets.plurialgo.langages.modele.Si> iter=instr.sis.iterator(); iter.hasNext();) {
				Si si = (Si) iter.next();
				copie.sis.add(this.vectoriser(si));
			}
		}
		else if (instr.isPour()) {
			for (Iterator<org.javascool.proglets.plurialgo.langages.modele.Pour> iter=instr.pours.iterator(); iter.hasNext();) {
				Pour pour = (Pour) iter.next();
				copie.pours.add(this.vectoriser(pour));
			}
		}
		else if (instr.isTantQue()) {
			for (Iterator<org.javascool.proglets.plurialgo.langages.modele.TantQue> iter=instr.tantques.iterator(); iter.hasNext();) {
				TantQue tq = (TantQue) iter.next();
				copie.tantques.add(this.vectoriser(tq));
			}
		}
		else if (instr.isAffectation()) {
			for (Iterator<org.javascool.proglets.plurialgo.langages.modele.Affectation> iter=instr.affectations.iterator(); iter.hasNext();) {
				Affectation aff = (Affectation) iter.next();
				copie.affectations.add(this.vectoriser(aff));
			}
		}
		else {
			copie = this.vectoriserArguments(instr);
		}
		return copie;
	}
	
	private Si vectoriser(Si si){
		Si copie = new Si();
		copie.condition = vectoriser(si.condition);
		copie.schema = si.schema;
		for (Iterator<org.javascool.proglets.plurialgo.langages.modele.Instruction> iter=si.instructions.iterator(); iter.hasNext();) {
			Instruction instr = (Instruction) iter.next();
			copie.instructions.add(this.vectoriser(instr));
		}
		return (copie);
	}
	
	private Pour vectoriser(Pour pour){
		Pour copie = new Pour();
		copie.var = pour.var;
		copie.debut = pour.debut;
		copie.fin = vectoriser(pour.fin);
		copie.pas = pour.pas;
		copie.schema = pour.schema;
		for (Iterator<org.javascool.proglets.plurialgo.langages.modele.Instruction> iter=pour.instructions.iterator(); iter.hasNext();) {
			Instruction instr = (Instruction) iter.next();
			copie.instructions.add(this.vectoriser(instr));
		}
		return (copie);
	}
	
	private TantQue vectoriser(TantQue tq){
		TantQue copie = new TantQue();
		copie.condition = vectoriser(tq.condition);
		copie.schema = tq.schema;
		for (Iterator<org.javascool.proglets.plurialgo.langages.modele.Instruction> iter=tq.instructions.iterator(); iter.hasNext();) {
			Instruction instr = (Instruction) iter.next();
			copie.instructions.add(this.vectoriser(instr));
		}
		return (copie);
	}
	
	private Affectation vectoriser(Affectation aff){
		Affectation copie = new Affectation();
		copie.var = vectoriser(aff.var);
		copie.expression = vectoriser(aff.expression);
		return (copie);
	}
	
	private Instruction vectoriserArguments(Instruction instr) {
		Argument arg, arg_copie;
		Instruction copie = new Instruction(instr.nom);
		for (Iterator<org.javascool.proglets.plurialgo.langages.modele.Argument> iter=instr.arguments.iterator(); iter.hasNext();) {
			arg = (Argument) iter.next();
			arg_copie = new Argument(arg.nom, arg.type, arg.mode);
			copie.arguments.add(arg_copie);
			arg_copie.nom = vectoriser(arg.nom);
		}
		arg = (Argument) instr.getRetour();
		if (arg!=null) {
			arg_copie = new Argument(arg.nom, arg.type, arg.mode);
			copie.retours.add(arg_copie);
			arg_copie.nom = vectoriser(arg.nom);
		}
		arg = (Argument) instr.getObjet();
		if (arg!=null) {
			arg_copie = new Argument(arg.nom, arg.type, arg.mode);
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
