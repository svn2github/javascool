/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2013.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.langages.xml;


import java.util.*;

import org.javascool.proglets.plurialgo.langages.modele.InfoTypee;


/**
 * Cette classe permet de reformuler un programme (bouton Reformuler de l'onglet Principal).
*/
public class ProgrammeDerive extends Programme {

	private Programme prog;	// le programme à reformuler	
	private Intermediaire inter; // la reformulation à appliquer
	private ArrayList<InfoTypee> infos; // liste de triplets (nom, type, mode) 
	private Classe classe; // la classe créée (si reformulation avec regroupement)

	/**
	 * 	Construit un programme par reformulation
	 *    @param prog le programme à reformuler
	 *    @param inter la reformulation à appliquer
	*/	
	public ProgrammeDerive(Programme prog, Intermediaire inter) {
		this.prog = prog;
		this.nom = prog.nom; 
		this.inter = inter;
		// récupération des classes et des operations
		for(Iterator<org.javascool.proglets.plurialgo.langages.modele.Classe> iter=prog.classes.iterator(); iter.hasNext(); ) {
			Classe cl = (Classe) iter.next();
			this.classes.add(cl);
		}
		for(Iterator<org.javascool.proglets.plurialgo.langages.modele.Operation> iter=prog.operations.iterator(); iter.hasNext(); ) {
			Operation oper = (Operation) iter.next();
			this.operations.add(oper);
		}
		// transformations
		this.initInfos();
		if (inter.sansRegroupement()) {
			traiterElementaire();
		}
		if (inter.avecEnregistrement()) {
			traiterEnregistrement();
		}
		if (inter.avecClasse()) {
			traiterClasse();
		}
		// divers
		if (instructions.size()==0) instructions.add(new Instruction("// ajouter des instructions"));
		traiterMaxTab();
	}

	/**
	 * 	Construit un programme par reformulation
	 *    @param prog le programme à reformuler (reformulation précisée dans la propriété options du programme)
	*/		
	public ProgrammeDerive(Programme prog) {
		this(prog, new Intermediaire());
	}
	
	private void initInfos() {
		InfoTypeeList infosList = new InfoTypeeList();
		String mode;
		if ((mode=prog.getOptionMode("enregistrement"))!=null) {
			inter.setOption("enregistrement", mode);	
		}
		else if ((mode=prog.getOptionMode("classe"))!=null) {
			inter.setOption("classe", mode);
		}
		if ((mode=prog.getOptionMode("calcul"))!=null) {
			inter.setOption("calcul", mode);
		}
		if ((mode=prog.getOptionMode("saisie"))!=null) {
			inter.setOption("saisie", mode);
		}
		if ((mode=prog.getOptionMode("affichage"))!=null) {
			inter.setOption("affichage", mode);
		}
		// les donnees
		if ((mode=prog.getOptionMode("donnees"))!=null) {
			inter.setOption("donnees", mode);
		}
		else {
			// recherche des instructions de lecture (perimé ?)
			for(Iterator<org.javascool.proglets.plurialgo.langages.modele.Instruction> iter=prog.instructions.iterator(); iter.hasNext(); ) {
				Instruction instr = (Instruction) iter.next();
				if (instr.isLectureStandard() || instr.isLectureFormulaire()) {
					for(Iterator<org.javascool.proglets.plurialgo.langages.modele.Argument> iter1 = instr.arguments.iterator(); iter1.hasNext(); ) {
						Argument arg = (Argument) iter1.next();
						System.out.println(arg.nom);
						infosList.addModes(arg.nom, "IN");
						infosList.addTypes(arg.nom, arg.type);
						infosList.addDim(arg.nom, arg.mode);
					}
					Argument arg = (Argument) instr.getObjet();
					if (arg!=null) {
						System.out.println(arg.nom);
						infosList.addModes(arg.nom, "IN");
						infosList.addTypes(arg.nom, arg.type);
						infosList.addDim(arg.nom, arg.mode);
					}
				}
			}
		}
		// les resultats
		if ((mode=prog.getOptionMode("resultats"))!=null) {
			inter.setOption("resultats", mode);
		}
		else {
			// recherche des instructions d'ecriture (perimé ?)
			for(Iterator<org.javascool.proglets.plurialgo.langages.modele.Instruction> iter=prog.instructions.iterator(); iter.hasNext(); ) {
				Instruction instr = (Instruction) iter.next();
				if (instr.isEcritureStandard()) {
					for(Iterator<org.javascool.proglets.plurialgo.langages.modele.Argument> iter1 = instr.arguments.iterator(); iter1.hasNext(); ) {
						Argument arg = (Argument) iter1.next();
						System.out.println(arg.nom);
						infosList.addModes(arg.nom, "OUT");
						infosList.addTypes(arg.nom, arg.type);
						infosList.addDim(arg.nom, arg.mode);
					}
					Argument arg = (Argument) instr.getObjet();
					if (arg!=null) {
						System.out.println(arg.nom);
						infosList.addModes(arg.nom, "OUT");
						infosList.addTypes(arg.nom, arg.type);
						infosList.addDim(arg.nom, arg.mode);
					}
				}
			}
		}
		// ajout des variables auxiliaires
		for(Iterator<org.javascool.proglets.plurialgo.langages.modele.Variable> iter=prog.variables.iterator(); iter.hasNext(); ) {
			Variable var = (Variable) iter.next();
			infosList.addTypes(var.nom, var.type);
			if (var.isIn()) {
				infosList.addModes(var.nom, "IN");
			}
			if (var.isOut()) {
				infosList.addModes(var.nom, "OUT");
			}
		}
		// interpretation des données, des resultats...
		infosList.addIntermediaire(inter);
		this.infos = infosList.getInfos();
	}
	
	// ----------------------------
	// regroupement élémentaire
	// ----------------------------
	
	private void traiterElementaire(){
		for(Iterator<InfoTypee> iter=infos.iterator(); iter.hasNext(); ) {
			InfoTypee info = iter.next();
			if (info.isAutre()) {
				if (inter!=null) {
					if (inter.avecProcedureCalcul() || inter.avecFonctionsCalcul()) {
						continue;
					}
				}
			}
			Variable var = new Variable(info.nom, info.type, null);
			variables.add(var);
		}
		traiterElementaireSaisie();
		traiterElementaireCalcul();
		traiterElementaireAffichage();
	}
	
	private void traiterElementaireInstructions(ArrayList<org.javascool.proglets.plurialgo.langages.modele.Instruction> liste1, ArrayList<org.javascool.proglets.plurialgo.langages.modele.Instruction> liste2){
		for(Iterator<org.javascool.proglets.plurialgo.langages.modele.Instruction> iter=liste1.iterator(); iter.hasNext(); ) {
			Instruction instr = (Instruction) iter.next();
			Instruction instr_copie = traiterElementaireInstruction(instr);
			if (instr_copie!=null) liste2.add(instr_copie);
		}
	}
	
	private Instruction traiterElementaireInstruction(Instruction instr){
		Instruction copie = new Instruction(instr.nom); 
		if (instr.isSi()) {
			for (Iterator<org.javascool.proglets.plurialgo.langages.modele.Si> iter=instr.sis.iterator(); iter.hasNext();) {
				Si si = (Si) iter.next();
				Si si_copie = this.traiterElementaireInstructionSi(si);
				if (si_copie!=null) copie.sis.add(si_copie);
			}
			if (copie.sis.size()==0) return null;
		}
		else if (instr.isPour()) {
			for (Iterator<org.javascool.proglets.plurialgo.langages.modele.Pour> iter=instr.pours.iterator(); iter.hasNext();) {
				Pour pour = (Pour) iter.next();
				Pour pour_copie = this.traiterElementaireInstructionPour(pour);
				if (pour_copie!=null) copie.pours.add(pour_copie);
			}
			if (copie.pours.size()==0) return null;
		}
		else if (instr.isTantQue()) {
			for (Iterator<org.javascool.proglets.plurialgo.langages.modele.TantQue> iter=instr.tantques.iterator(); iter.hasNext();) {
				TantQue tq = (TantQue) iter.next();
				TantQue tq_copie = this.traiterElementaireInstructionTantQue(tq);
				if (tq_copie!=null) copie.tantques.add(tq_copie);
			}
			if (copie.tantques.size()==0) return null;
		}
		else if (instr.isLecture() || instr.isEcriture()) {
			return null;
		}
		else {
			return instr;
		}
		return copie;
	}
	
	private Si traiterElementaireInstructionSi(Si si){
		Si copie = new Si();
		copie.condition = si.condition;
		copie.schema = si.schema;
		traiterElementaireInstructions(si.instructions, copie.instructions);
		if (copie.instructions.size()==0) return null;
		return (copie);
	}
	
	private Pour traiterElementaireInstructionPour(Pour pour){
		Pour copie = new Pour();
		copie.var = pour.var; copie.debut = pour.debut;
		copie.fin = pour.fin; copie.pas = pour.pas;
		copie.schema = pour.schema;
		traiterElementaireInstructions(pour.instructions, copie.instructions);
		if (copie.instructions.size()==0) return null;
		return (copie);
	}
	
	private TantQue traiterElementaireInstructionTantQue(TantQue tq){
		TantQue copie = new TantQue();
		copie.condition = tq.condition;
		copie.schema = tq.schema;
		traiterElementaireInstructions(tq.instructions, copie.instructions);
		if (copie.instructions.size()==0) return null;
		return (copie);
	}
	
	private void traiterElementaireSaisie(){
		Instruction instr;
		Operation oper;
		Argument arg;
		if (inter.avecSaisieElementaire() || inter.avecSaisieFichierTexte() || inter.avecSaisieFormulaire()  || inter.avecSaisieSql()) {
			instr = new Instruction("lire");
			if (inter.avecSaisieFichierTexte()) {
				arg = new Argument("fich", "FICHIER_TEXTE", null);
				arg.instructions.add( new Instruction("// ajouter des instructions"));
				instr.setFichier(arg);
			}
			if (inter.avecSaisieFormulaire()) {
				arg = new Argument("formu", "FORM", null);
				instr.setFormulaire(arg);
			}
			if (inter.avecSaisieSql()) {
				arg = new Argument("bd", "SQL", null);
				arg.instructions.add( new Instruction("// ajouter des instructions"));
				instr.setFichier(arg);
			}
			for(Iterator<InfoTypee> iter=infos.iterator(); iter.hasNext(); ) {
				InfoTypee info = iter.next();
				if (info.isIn() || info.isInOut()) {
					arg = new Argument(info.nom, info.type, null);
					arg.dim = info.dim;
					instr.arguments.add(arg);
				}
			}
			if (instr.arguments.size()>0) {
				instructions.add(instr);
			}
		}
		if (inter.avecFonctionsSaisie()) {
			for(Iterator<InfoTypee> iter=infos.iterator(); iter.hasNext(); ) {
				InfoTypee info = iter.next();
				if (info.isIn() || info.isInOut()) {
					instr = new Instruction("saisir_"+info.nom);
					oper = new Operation("saisir_"+info.nom);
					oper.traiterElementaireSaisieFonction(info, instr);
					instructions.add(instr);
					operations.add(oper);
				}
			}
		}
		if (inter.avecProcedureSaisie()) {
			instr = new Instruction("saisir_"+this.nom);
			oper = new Operation("saisir_"+this.nom);
			oper.traiterElementaireSaisieProcedure(infos, instr);
			if (instr.arguments.size()>0) {
				instructions.add(instr);
				operations.add(oper);
			}
		}
	}
	
	private void traiterElementaireAffichage(){
		Instruction instr;
		Operation oper;
		Argument arg;
		if ( inter.avecAffichageElementaire() || inter.avecAffichageFichierTexte() || inter.avecAffichageSql()) {
			instr = new Instruction("ecrire");
			if (inter.avecAffichageFichierTexte()) {
				arg = new Argument("fich", "FICHIER_TEXTE", null);
				instr.setFichier(arg);
			}
			if (inter.avecAffichageSql()) {
				arg = new Argument("bd", "SQL", null);
				instr.setFichier(arg);
			}
			for(Iterator<InfoTypee> iter=infos.iterator(); iter.hasNext(); ) {
				InfoTypee info = iter.next();
				if (info.isOut() || info.isInOut()) {
					arg = new Argument(info.nom, info.type, null);
					arg.dim = info.dim;
					instr.arguments.add(arg);
				}
			}
			if (instr.arguments.size()>0) {
				instructions.add(instr);
			}
		}
		if (inter.avecProcedureAffichage()) {
			instr = new Instruction("afficher_"+this.nom);
			oper = new Operation("afficher_"+this.nom);
			oper.traiterElementaireAffichageProcedure(infos, instr);
			if (instr.arguments.size()>0) {
				instructions.add(instr);
				operations.add(oper);
			}
		}
	}
	
	private void traiterElementaireCalcul(){
		Instruction instr;
		Operation oper;
		if (inter.avecFonctionsCalcul()) {
			ArrayList<org.javascool.proglets.plurialgo.langages.modele.Argument> arg_donnees = new ArrayList<org.javascool.proglets.plurialgo.langages.modele.Argument>();
			ArrayList<org.javascool.proglets.plurialgo.langages.modele.Parametre> param_donnees = new ArrayList<org.javascool.proglets.plurialgo.langages.modele.Parametre>();
			for(Iterator<InfoTypee> iter=infos.iterator(); iter.hasNext(); ) {
				InfoTypee info = iter.next();
				if (info.isIn()) {
					arg_donnees.add (new Argument(info.nom, info.type, info.mode));
					param_donnees.add (new Parametre(info.nom, info.type, info.mode));
				}
			}
			for(Iterator<InfoTypee> iter=infos.iterator(); iter.hasNext(); ) {
				InfoTypee info = iter.next();
				if (info.isOut()) {
					instr = new Instruction("calculer_"+info.nom);
					oper = new Operation("calculer_"+info.nom);
					oper.traiterElementaireCalculFonction(info, instr, param_donnees, arg_donnees);
					this.traiterElementaireInstructions(prog.instructions, oper.instructions);
					oper.traiterElementaireVariables(infos);
					instructions.add(instr);
					operations.add(oper);
				}
			}
		}
		else if (inter.avecProcedureCalcul()) {
			instr = new Instruction("calculer_"+this.nom);
			oper = new Operation("calculer_"+this.nom);
			oper.traiterElementaireCalculProcedure(infos, instr);
			this.traiterElementaireInstructions(prog.instructions, oper.instructions);
			oper.traiterElementaireVariables(infos);
			InfoTypeeList liste = new InfoTypeeList();
			liste.addInfos(infos);
			if (liste.compterMode("INOUT")>0 || liste.compterMode("OUT")>0) {
				instructions.add(instr);
				operations.add(oper);
			}
		}
		else {
			this.traiterElementaireInstructions(prog.instructions, instructions);
		}
	}
	
	// ----------------------------
	// regroupement enregistrement
	// ----------------------------
	
	private void traiterEnregistrement(){
		String nom_cl = inter.getNomGroupe();
		Classe enreg = new Classe(nom_cl, true);
		classes.add(enreg);
		this.classe = enreg;
		infos = enreg.creerProprietes(infos, inter.getProprietesGroupe());
		for(Iterator<InfoTypee> iter=infos.iterator(); iter.hasNext(); ) {
			InfoTypee info = iter.next();
			if (info.isAutre()) {
				if (inter!=null) {
					if (inter.avecProcedureCalcul() || inter.avecFonctionsCalcul()) {
						continue;
					}
				}
			}
			Variable var = new Variable(info.nom, info.type, null);
			variables.add(var);
		}
		traiterEnregistrementSaisie();
		traiterEnregistrementCalcul();
		traiterEnregistrementAffichage();
	}
	
	private void traiterEnregistrementInstructions(ArrayList<org.javascool.proglets.plurialgo.langages.modele.Instruction> liste1, ArrayList<org.javascool.proglets.plurialgo.langages.modele.Instruction> liste2, String nom_obj){
		for(Iterator<org.javascool.proglets.plurialgo.langages.modele.Instruction> iter=liste1.iterator(); iter.hasNext(); ) {
			Instruction instr = (Instruction) iter.next();
			Instruction instr_copie = traiterEnregistrementInstruction(instr, nom_obj);
			if (instr_copie!=null) liste2.add(instr_copie);
		}
	}
	
	private Instruction traiterEnregistrementInstruction(Instruction instr, String nom_obj){
		Instruction copie = new Instruction(instr.nom); 
		if (instr.isSi()) {
			for (Iterator<org.javascool.proglets.plurialgo.langages.modele.Si> iter=instr.sis.iterator(); iter.hasNext();) {
				Si si = (Si) iter.next();
				Si si_copie = this.traiterEnregistrementInstructionSi(si, nom_obj);
				if (si_copie!=null) copie.sis.add(si_copie);
			}
			if (copie.sis.size()==0) return null;
		}
		else if (instr.isPour()) {
			for (Iterator<org.javascool.proglets.plurialgo.langages.modele.Pour> iter=instr.pours.iterator(); iter.hasNext();) {
				Pour pour = (Pour) iter.next();
				Pour pour_copie = this.traiterEnregistrementInstructionPour(pour, nom_obj);
				if (pour_copie!=null) copie.pours.add(pour_copie);
			}
			if (copie.pours.size()==0) return null;
		}
		else if (instr.isTantQue()) {
			for (Iterator<org.javascool.proglets.plurialgo.langages.modele.TantQue> iter=instr.tantques.iterator(); iter.hasNext();) {
				TantQue tq = (TantQue) iter.next();
				TantQue tq_copie = this.traiterEnregistrementInstructionTantQue(tq, nom_obj);
				if (tq_copie!=null) copie.tantques.add(tq_copie);
			}
			if (copie.tantques.size()==0) return null;
		}
		else if (instr.isAffectation()) {
			for (Iterator<org.javascool.proglets.plurialgo.langages.modele.Affectation> iter=instr.affectations.iterator(); iter.hasNext();) {
				Affectation aff = (Affectation) iter.next();
				copie.affectations.add(this.traiterEnregistrementInstructionAffectation(aff, nom_obj));
			}
		}
		else if (instr.isLecture() || instr.isEcriture()) {
			return null;
		}
		else if (instr.isAppel(prog)) {
			copie = this.traiterEnregistrementArguments(instr, nom_obj);
		}
		return copie;
	}
	
	private Si traiterEnregistrementInstructionSi(Si si, String nom_obj){
		Si copie = new Si();
		copie.condition = traiterEnregistrementInstruction(si.condition, nom_obj);
		copie.schema = si.schema;
		traiterEnregistrementInstructions(si.instructions, copie.instructions, nom_obj);
		if (copie.instructions.size()==0) return null;
		return (copie);
	}
	
	private Pour traiterEnregistrementInstructionPour(Pour pour, String nom_obj){
		Pour copie = new Pour();
		copie.var = pour.var;
		copie.debut = traiterEnregistrementInstruction(pour.debut, nom_obj);
		copie.fin = traiterEnregistrementInstruction(pour.fin, nom_obj);
		copie.pas = traiterEnregistrementInstruction(pour.pas, nom_obj);
		copie.schema = pour.schema;
		traiterEnregistrementInstructions(pour.instructions, copie.instructions, nom_obj);
		if (copie.instructions.size()==0) return null;
		return (copie);
	}
	
	private TantQue traiterEnregistrementInstructionTantQue(TantQue tq, String nom_obj){
		TantQue copie = new TantQue();
		copie.condition = traiterEnregistrementInstruction(tq.condition, nom_obj);
		copie.schema = tq.schema;
		traiterEnregistrementInstructions(tq.instructions, copie.instructions, nom_obj);
		if (copie.instructions.size()==0) return null;
		return (copie);
	}
	
	private Affectation traiterEnregistrementInstructionAffectation(Affectation aff, String nom_obj){
		Affectation copie = new Affectation();
		copie.var = traiterEnregistrementInstruction(aff.var, nom_obj);
		copie.expression = traiterEnregistrementInstruction(aff.expression, nom_obj);
		return (copie);
	}
	
	private Instruction traiterEnregistrementArguments(Instruction instr, String nom_obj){
		Instruction copie = new Instruction(instr.nom);
		Argument arg, arg_copie;
		for (Iterator<org.javascool.proglets.plurialgo.langages.modele.Argument> iter=instr.arguments.iterator(); iter.hasNext();) {
			arg = (Argument) iter.next();
			arg_copie = new Argument(arg.nom, arg.type, arg.mode);
			copie.arguments.add(arg_copie);
			arg_copie.nom = traiterEnregistrementInstruction(arg.nom, nom_obj);
		}
		arg = (Argument) instr.getRetour();
		if (arg!=null) {
			arg_copie = new Argument(arg.nom, arg.type, arg.mode);
			copie.retours.add(arg_copie);
			arg_copie.nom = traiterEnregistrementInstruction(arg.nom, nom_obj);
		}
		arg = (Argument) instr.getObjet();
		if (arg!=null) {
			arg_copie = new Argument(arg.nom, arg.type, arg.mode);
			copie.objets.add(arg_copie);
			arg_copie.nom = traiterEnregistrementInstruction(arg.nom, nom_obj);
		}
		return (copie);
	}
	
	private String traiterEnregistrementInstruction(String txt, String nom_obj){
		if (txt==null) return "";
		System.out.println("avant remplacer : " + txt);
		StringBuffer buf = new StringBuffer(" " + txt + " ");
		for (Iterator<org.javascool.proglets.plurialgo.langages.modele.Variable> iter=this.classe.proprietes.iterator(); iter.hasNext();) {
			Variable prop = (Variable) iter.next();
			System.out.println("propriete : " + prop.nom);
			String ancien = prop.nom;
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
					if  ( (ch.compareTo("0")>=0) && (ch.compareTo("9")<=0) ) {
						if (nom_obj.equals("objet") && ch.equals("1")) {
							nouveau = "objet" + "." + prop.nom;
						}
						else if (nom_obj.equals("objet") && !ch.equals("1")) {
							nouveau = "objet" + ch + "." + prop.nom;
						}
						else {
							nouveau = nom_obj + ch + "." + prop.nom;
						}
						buf.delete(i, i+lg_ancien+1); buf.insert(i, nouveau);
					}
					else {
						nouveau = nom_obj + "." + prop.nom;
						buf.delete(i, i+lg_ancien); buf.insert(i, nouveau);
					}
				}
			}		
		}
		buf.delete(0, 1); buf.delete(buf.length()-1, buf.length());
		//System.out.println("apres remplacer : " + buf.toString());
		return buf.toString();
	}
	
	private void traiterEnregistrementSaisie(){
		Instruction instr, instr_oper, instr_obj;
		Operation oper=null;
		Argument arg, arg_oper;
		Parametre param_oper;
		if (inter.avecSaisieElementaire() || inter.avecSaisieFichierTexte() || inter.avecSaisieFormulaire()|| inter.avecSaisieSql()) {
			for(Iterator<InfoTypee> iter=infos.iterator(); iter.hasNext(); ) {
				InfoTypee info = iter.next();
				if (info.dim!=null && (info.isIn() || info.isInOut()) ) {
					info.dim = this.traiterEnregistrementInstruction(info.dim, info.nom);
				}
			}
			this.traiterElementaireSaisie();
		}
		if (inter.avecFonctionsSaisie() || inter.avecProcedureSaisie()) {
			// infos non enregistrements
			instr = new Instruction("lire");
			for(Iterator<InfoTypee> iter=infos.iterator(); iter.hasNext(); ) {
				InfoTypee info = iter.next();
				if ( (info.isIn() || info.isInOut()) && (!info.isEnregistrement(this)) ) {
					arg = new Argument(info.nom, info.type, null);
					instr.arguments.add(arg);
				}
			}
			if (instr.arguments.size()>0) {
				instructions.add(instr);
			}
			// infos enregistrements
			for(Iterator<InfoTypee> iter=infos.iterator(); iter.hasNext(); ) {
				InfoTypee info = iter.next();
				if ( (info.isIn() || info.isInOut()) && (info.isEnregistrement(this)) ) {
					if (oper==null) {
						oper = new Operation("saisir_"+inter.getNomGroupe());
						param_oper = new Parametre(classe.getSansIndice(info.nom), info.type, "OUT");
						oper.parametres.add(param_oper);
						instr_oper = new Instruction("lire");
						arg_oper = new Argument(classe.getSansIndice(info.nom), info.type, null);
						if (info.dim!=null) {
							arg_oper.dim = this.traiterEnregistrementInstruction(info.dim, arg_oper.nom);
						}
						instr_oper.arguments.add(arg_oper);
						oper.instructions.add(instr_oper);
						operations.add(oper);
					}
					instr_obj = new Instruction("saisir_"+inter.getNomGroupe());
					arg_oper = new Argument(info.nom, info.type, "OUT");
					instr_obj.arguments.add(arg_oper);
					instructions.add(instr_obj);
				}
			}
		}
	}
	
	private void traiterEnregistrementAffichage(){
		Instruction instr, instr_oper, instr_obj;
		Operation oper=null;
		Argument arg, arg_oper;
		Parametre param_oper;
		if ( inter.avecAffichageElementaire() || inter.avecAffichageFichierTexte() || inter.avecAffichageSql()) {
			for(Iterator<InfoTypee> iter=infos.iterator(); iter.hasNext(); ) {
				InfoTypee info = iter.next();
				if (info.dim!=null && (info.isOut() || info.isInOut()) ) {
					info.dim = this.traiterEnregistrementInstruction(info.dim, info.nom);
				}
			}
			this.traiterElementaireAffichage();
		}
		else if (inter.avecProcedureAffichage()) {
			// infos non enregistrements
			instr = new Instruction("ecrire");
			for(Iterator<InfoTypee> iter=infos.iterator(); iter.hasNext(); ) {
				InfoTypee info = iter.next();
				if ( (info.isOut() || info.isInOut()) && (!info.isEnregistrement(this)) ) {
					arg = new Argument(info.nom, info.type, null);
					instr.arguments.add(arg);
				}
			}
			if (instr.arguments.size()>0) {
				instructions.add(instr);
			}
			// infos enregistrements
			for(Iterator<InfoTypee> iter=infos.iterator(); iter.hasNext(); ) {
				InfoTypee info = iter.next();
				if ( (info.isOut() || info.isInOut()) && (info.isEnregistrement(this)) ) {
					if (oper==null) {
						oper = new Operation("afficher_"+inter.getNomGroupe());
						param_oper = new Parametre(classe.getSansIndice(info.nom), info.type, "IN");
						oper.parametres.add(param_oper);
						instr_oper = new Instruction("ecrire");
						arg_oper = new Argument(classe.getSansIndice(info.nom), info.type, null);
						if (info.dim!=null) {
							arg_oper.dim = this.traiterEnregistrementInstruction(info.dim, arg_oper.nom);
						}
						instr_oper.arguments.add(arg_oper);
						oper.instructions.add(instr_oper);
						operations.add(oper);
					}
					instr_obj = new Instruction("afficher_"+inter.getNomGroupe());
					arg_oper = new Argument(info.nom, info.type, "IN");
					instr_obj.arguments.add(arg_oper);
					instructions.add(instr_obj);
				}
			}
		}
	}
	
	private void traiterEnregistrementCalcul(){
		Instruction instr;
		Operation oper;
		if (inter.avecFonctionsCalcul()) {
			ArrayList<org.javascool.proglets.plurialgo.langages.modele.Argument> arg_donnees = new ArrayList<org.javascool.proglets.plurialgo.langages.modele.Argument>();
			ArrayList<org.javascool.proglets.plurialgo.langages.modele.Parametre> param_donnees = new ArrayList<org.javascool.proglets.plurialgo.langages.modele.Parametre>();
			for(Iterator<InfoTypee> iter=infos.iterator(); iter.hasNext(); ) {
				InfoTypee info = iter.next();
				if (info.isIn()) {
					arg_donnees.add (new Argument(info.nom, info.type, info.mode));
					param_donnees.add (new Parametre(info.nom, info.type, info.mode));
				}
			}
			for(Iterator<InfoTypee> iter=infos.iterator(); iter.hasNext(); ) {
				InfoTypee info = iter.next();
				if (info.isOut()) {
					instr = new Instruction("calculer_"+info.nom);
					oper = new Operation("calculer_"+info.nom);
					this.traiterEnregistrementInstructions(prog.instructions, oper.instructions, "objet");
					oper.traiterElementaireCalculFonction(info, instr, param_donnees, arg_donnees);
					oper.traiterElementaireVariables(infos);
					instructions.add(instr);
					operations.add(oper);
				}
			}
		}
		else if (inter.avecProcedureCalcul()) {
			instr = new Instruction("calculer");
			oper = new Operation("calculer");
			this.traiterEnregistrementInstructions(prog.instructions, oper.instructions, "objet");
			oper.traiterElementaireCalculProcedure(infos, instr);
			oper.traiterElementaireVariables(infos);
			instructions.add(instr);
			operations.add(oper);
		}
		else {
			this.traiterEnregistrementInstructions(prog.instructions, instructions, "objet");
		}
	}
	
	// ------------------------------------------
	// regroupement en classe
	// ------------------------------------------ 

	private void traiterClasse(){
		String nom_cl = inter.getNomGroupe();
		classe = new Classe(nom_cl, false);
		classes.add(classe);
		infos = classe.creerProprietes(infos, inter.getProprietesGroupe());
		for(Iterator<InfoTypee> iter=infos.iterator(); iter.hasNext(); ) {
			InfoTypee info = iter.next();
			if (info.isAutre()) {
				if (inter!=null) {
					if (inter.avecProcedureCalcul() || inter.avecFonctionsCalcul()) {
						continue;
					}
				}
			}
			Variable var = new Variable(info.nom, info.type, null);
			variables.add(var);
		}
		traiterClasseSaisie();
		traiterClasseCalcul();
		traiterClasseAffichage();
		// constructeur
		Constructeur constr = new Constructeur(classe.nom);
		constr.instructions.add( new Instruction("// ajouter des instructions"));
		classe.constructeurs.add(constr);
	}
	
	private void traiterClasseInstructions(ArrayList<org.javascool.proglets.plurialgo.langages.modele.Instruction> liste1, ArrayList<org.javascool.proglets.plurialgo.langages.modele.Instruction> liste2, String nom_obj){
		for(Iterator<org.javascool.proglets.plurialgo.langages.modele.Instruction> iter=liste1.iterator(); iter.hasNext(); ) {
			Instruction instr = (Instruction) iter.next();
			Instruction instr_copie = traiterClasseInstruction(instr, nom_obj);
			if (instr_copie!=null) liste2.add(instr_copie);
		}
	}
	
	private Instruction traiterClasseInstruction(Instruction instr, String nom_obj){
		Instruction copie = new Instruction(instr.nom); 
		if (instr.isSi()) {
			for (Iterator<org.javascool.proglets.plurialgo.langages.modele.Si> iter=instr.sis.iterator(); iter.hasNext();) {
				Si si = (Si) iter.next();
				Si si_copie = this.traiterClasseInstructionSi(si, nom_obj);
				if (si_copie!=null) copie.sis.add(si_copie);
			}
			if (copie.sis.size()==0) return null;
		}
		else if (instr.isPour()) {
			for (Iterator<org.javascool.proglets.plurialgo.langages.modele.Pour> iter=instr.pours.iterator(); iter.hasNext();) {
				Pour pour = (Pour) iter.next();
				Pour pour_copie = this.traiterClasseInstructionPour(pour, nom_obj);
				if (pour_copie!=null) copie.pours.add(pour_copie);
			}
			if (copie.pours.size()==0) return null;
		}
		else if (instr.isTantQue()) {
			for (Iterator<org.javascool.proglets.plurialgo.langages.modele.TantQue> iter=instr.tantques.iterator(); iter.hasNext();) {
				TantQue tq = (TantQue) iter.next();
				TantQue tq_copie = this.traiterClasseInstructionTantQue(tq, nom_obj);
				if (tq_copie!=null) copie.tantques.add(tq_copie);
			}
			if (copie.tantques.size()==0) return null;
		}
		else if (instr.isAffectation()) {
			for (Iterator<org.javascool.proglets.plurialgo.langages.modele.Affectation> iter=instr.affectations.iterator(); iter.hasNext();) {
				Affectation aff = (Affectation) iter.next();
				copie.affectations.add(this.traiterClasseInstructionAffectation(aff, nom_obj));
			}
		}
		else if (instr.isLecture() || instr.isEcriture()) {
			return null;
		}
		else if (instr.isAppel(prog)) {
			copie = this.traiterClasseArguments(instr, nom_obj);
		}
		return copie;
	}
	
	private Si traiterClasseInstructionSi(Si si, String nom_obj){
		Si copie = new Si();
		copie.condition = traiterClasseInstruction(si.condition, nom_obj);
		copie.schema = si.schema;
		traiterClasseInstructions(si.instructions, copie.instructions, nom_obj);
		if (copie.instructions.size()==0) return null;
		return (copie);
	}
	
	private Pour traiterClasseInstructionPour(Pour pour, String nom_obj){
		Pour copie = new Pour();
		copie.var = pour.var;
		copie.debut = traiterClasseInstruction(pour.debut, nom_obj);
		copie.fin = traiterClasseInstruction(pour.fin, nom_obj);
		copie.pas = traiterClasseInstruction(pour.pas, nom_obj);
		copie.schema = pour.schema;
		traiterClasseInstructions(pour.instructions, copie.instructions, nom_obj);
		if (copie.instructions.size()==0) return null;
		return (copie);
	}
	
	private TantQue traiterClasseInstructionTantQue(TantQue tq, String nom_obj){
		TantQue copie = new TantQue();
		copie.condition = traiterClasseInstruction(tq.condition, nom_obj);
		copie.schema = tq.schema;
		traiterClasseInstructions(tq.instructions, copie.instructions, nom_obj);
		if (copie.instructions.size()==0) return null;
		return (copie);
	}
	
	private Affectation traiterClasseInstructionAffectation(Affectation aff, String nom_obj){
		Affectation copie = new Affectation();
		copie.var = traiterClasseInstruction(aff.var, nom_obj);
		copie.expression = traiterClasseInstruction(aff.expression, nom_obj);
		return (copie);
	}
	
	private Instruction traiterClasseArguments(Instruction instr, String nom_obj){
		Instruction copie = new Instruction(instr.nom);
		Argument arg, arg_copie;
		for (Iterator<org.javascool.proglets.plurialgo.langages.modele.Argument> iter=instr.arguments.iterator(); iter.hasNext();) {
			arg = (Argument) iter.next();
			arg_copie = new Argument(arg.nom, arg.type, arg.mode);
			copie.arguments.add(arg_copie);
			arg_copie.nom = traiterEnregistrementInstruction(arg.nom, nom_obj);
		}
		arg = (Argument) instr.getRetour();
		if (arg!=null) {
			arg_copie = new Argument(arg.nom, arg.type, arg.mode);
			copie.retours.add(arg_copie);
			arg_copie.nom = traiterEnregistrementInstruction(arg.nom, nom_obj);
		}
		arg = (Argument) instr.getObjet();
		if (arg!=null) {
			arg_copie = new Argument(arg.nom, arg.type, arg.mode);
			copie.objets.add(arg_copie);
			arg_copie.nom = traiterEnregistrementInstruction(arg.nom, nom_obj);
		}
		return (copie);
	}
	
	private String traiterClasseInstruction(String txt, String nom_obj){
		if (txt==null) return "";
		System.out.println("avant remplacer : " + txt);
		StringBuffer buf = new StringBuffer(" " + txt + " ");
		for (Iterator<org.javascool.proglets.plurialgo.langages.modele.Variable> iter=this.classe.proprietes.iterator(); iter.hasNext();) {
			Variable prop = (Variable) iter.next();
			System.out.println("propriete : " + prop.nom);
			String ancien = prop.nom;
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
					if  ( (ch.compareTo("0")>=0) && (ch.compareTo("9")<=0) ) {
						if (nom_obj.equals("this") && ch.equals("1")) {
							nouveau = "this" + "." + prop.nom;
						}
						else if (nom_obj.equals("this") && !ch.equals("1")) {
							nouveau = "objet" + ch + "." + prop.nom;
						}
						else {
							nouveau = nom_obj + ch + "." + prop.nom;
						}
						buf.delete(i, i+lg_ancien+1); buf.insert(i, nouveau);
					}
					else { 
						nouveau = nom_obj + "." + prop.nom;
						buf.delete(i, i+lg_ancien); buf.insert(i, nouveau);
					}
				}
			}		
		}
		buf.delete(0, 1); buf.delete(buf.length()-1, buf.length());
		System.out.println("apres remplacer : " + buf.toString());
		return buf.toString();
	}
	
	private void traiterClasseSaisie(){
		Instruction instr, instr_oper, instr_obj;
		Operation oper=null;
		Argument arg, arg_oper;
		if (inter.avecSaisieElementaire() || inter.avecSaisieFichierTexte() || inter.avecSaisieFormulaire() || inter.avecSaisieSql()) {
			for(Iterator<InfoTypee> iter=infos.iterator(); iter.hasNext(); ) {
				InfoTypee info = iter.next();
				if (info.dim!=null && (info.isIn() || info.isInOut()) ) {
					info.dim = this.traiterClasseInstruction(info.dim, info.nom);
				}
			}	
			this.traiterElementaireSaisie();
		}
		if (inter.avecProcedureSaisie()  || inter.avecFonctionsSaisie()) {
			// infos non objets
			instr = new Instruction("lire");
			for(Iterator<InfoTypee> iter=infos.iterator(); iter.hasNext(); ) {
				InfoTypee info = iter.next();
				if ( (info.isIn() || info.isInOut()) && (!info.isClasse(this)) ) {
					arg = new Argument(info.nom, info.type, null);
					instr.arguments.add(arg);
				}
			}
			if (instr.arguments.size()>0) {
				instructions.add(instr);
			}
			// infos objets
			for(Iterator<InfoTypee> iter=infos.iterator(); iter.hasNext(); ) {
				InfoTypee info = iter.next();
				if ( (info.isIn() || info.isInOut()) && (info.isClasse(this)) ) {
					if (oper==null) {
						oper = new Operation("saisir");
						instr_oper = new Instruction("lire");
						arg_oper = new Argument("this", info.type, null);
						if (info.dim!=null) {
							arg_oper.dim = this.traiterClasseInstruction(info.dim, arg_oper.nom);
						}
						instr_oper.arguments.add(arg_oper);
						oper.instructions.add(instr_oper);
						classe.operations.add(oper);
					}
					instr_obj = new Instruction("saisir");
					arg = new Argument(info.nom, info.type, null);
					instr_obj.setObjet(arg);
					instructions.add(instr_obj);
				}
			}
		}
	}
	
	private void traiterClasseCalcul(){
		Instruction instr;
		Operation oper=null;
		Argument arg;
		Parametre param_oper;
		InfoTypee info_obj=null; Argument obj;
		for(Iterator<InfoTypee> iter=infos.iterator(); iter.hasNext(); ) {
			InfoTypee info = iter.next();
			if ( (info.isClasse(this)) ) {
				info_obj=info; break;
			}
		}
		if (info_obj==null) return;
		obj = new Argument(info_obj.nom, info_obj.type, null);
		if (inter.avecFonctionsCalcul()) {
			ArrayList<org.javascool.proglets.plurialgo.langages.modele.Argument> arg_donnees = new ArrayList<org.javascool.proglets.plurialgo.langages.modele.Argument>();
			ArrayList<org.javascool.proglets.plurialgo.langages.modele.Parametre> param_donnees = new ArrayList<org.javascool.proglets.plurialgo.langages.modele.Parametre>();
			for(Iterator<InfoTypee> iter=infos.iterator(); iter.hasNext(); ) {
				InfoTypee info = iter.next();
				if (info.isIn() && info!=info_obj) {
					arg_donnees.add (new Argument(info.nom, info.type, info.mode));
					param_donnees.add (new Parametre(info.nom, info.type, info.mode));
				}
			}
			for(Iterator<InfoTypee> iter=infos.iterator(); iter.hasNext(); ) {
				InfoTypee info = iter.next();
				if (info.isOut() && info!=info_obj) {
					instr = new Instruction("calculer_"+info.nom);
					arg = new Argument(info.nom, info.type, null);
					instr.setRetour(arg);
					instr.setObjet(obj);
					instr.arguments.addAll(arg_donnees);
					instructions.add(instr);
					oper = new Operation("calculer_"+info.nom);
					this.traiterClasseInstructions(prog.instructions, oper.instructions, "this");
					oper.setRetour( new Variable(info.nom, info.type, null) );
					oper.parametres.addAll(param_donnees);
					oper.traiterElementaireVariables(infos);
					classe.operations.add(oper);
				}
			}
		}
		else if (inter.avecProcedureCalcul()) {
			instr = new Instruction("calculer");
			oper = new Operation("calculer");
			this.traiterClasseInstructions(prog.instructions, oper.instructions, "this");
			for(Iterator<InfoTypee> iter=infos.iterator(); iter.hasNext(); ) {
				InfoTypee info = iter.next();
				if ( (info.isIn() || info.isInOut() || info.isOut()) && info!=info_obj ) {
					arg = new Argument(info.nom, info.type, info.mode);
					instr.arguments.add(arg);
					param_oper = new Parametre(info.nom, info.type, info.mode);
					oper.parametres.add(param_oper);
					oper.traiterElementaireVariables(infos);
				}
			}
			InfoTypeeList liste = new InfoTypeeList();
			liste.addInfos(infos);
			if (liste.compterMode("INOUT")>0 || liste.compterMode("OUT")>0) {
				instr.setObjet(obj);
				instructions.add(instr);
				classe.operations.add(oper);
			}
		}
		else {
			this.traiterClasseInstructions(prog.instructions, instructions, "objet");
		}
	}
	
	private void traiterClasseAffichage(){
		Instruction instr, instr_oper, instr_obj;
		Operation oper=null;
		Argument arg, arg_oper;
		if (inter.avecAffichageElementaire()  || inter.avecAffichageFichierTexte() || inter.avecAffichageSql()) {
			for(Iterator<InfoTypee> iter=infos.iterator(); iter.hasNext(); ) {
				InfoTypee info = iter.next();
				if (info.dim!=null && (info.isOut() || info.isInOut()) ) {
					info.dim = this.traiterClasseInstruction(info.dim, info.nom);
				}
			}	
			this.traiterElementaireAffichage();
		}
		if (inter.avecProcedureAffichage()) {
			// avec infos non objets
			instr = new Instruction("ecrire");
			for(Iterator<InfoTypee> iter=infos.iterator(); iter.hasNext(); ) {
				InfoTypee info = iter.next();
				if ( (info.isOut() || info.isInOut()) && (!info.isClasse(this)) ) {
					arg = new Argument(info.nom, info.type, null);
					instr.arguments.add(arg);
				}
			}
			if (instr.arguments.size()>0) {
				instructions.add(instr);
			}
			// avec infos objets
			for(Iterator<InfoTypee> iter=infos.iterator(); iter.hasNext(); ) {
				InfoTypee info = iter.next();
				if ( (info.isOut() || info.isInOut()) && (info.isClasse(this)) ) {
					if (oper==null) {
						oper = new Operation("afficher");
						instr_oper = new Instruction("ecrire");
						arg_oper = new Argument("this", info.type, null);
						if (info.dim!=null) {
							arg_oper.dim = this.traiterClasseInstruction(info.dim, arg_oper.nom);
						}
						instr_oper.arguments.add(arg_oper);
						oper.instructions.add(instr_oper);
						classe.operations.add(oper);
					}
					instr_obj = new Instruction("afficher");
					arg = new Argument(info.nom, info.type, null);
					instr_obj.setObjet(arg);
					instructions.add(instr_obj);
				}
			}
		}
	}
}
