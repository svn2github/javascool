/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2013.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.langages.xml;


import java.util.*;

import org.javascool.proglets.plurialgo.langages.modele.InfoTypee;


/**
 * Cette classe permet de créer des squelettes de programmes (bouton Nouveau de l'onglet Principal).
*/
public class ProgrammeNouveau extends Programme {
	
	private Intermediaire inter; // inter les caractéristiques de l'onglet Principal à prendre en compte
	private ArrayList<InfoTypee> infos; // liste de triplets (nom, type, mode)
	private Classe classe;	 // la classe créée (si création avec regroupement)

	/**
	 * Construit un squelette de programme.
	      @param inter les caractéristiques de l'onglet Principal à prendre en compte
	*/		
	public ProgrammeNouveau(Intermediaire inter) {
		this.nom = inter.getNomAlgo();
		this.inter = inter;
		InfoTypeeList infosList = new InfoTypeeList();
		infosList.addIntermediaire(inter);
		this.infos = infosList.getInfos();
		if (inter.sansRegroupement()) {
			traiterElementaire();
		}
		if (inter.avecEnregistrement()) {
			traiterEnregistrement();
		}
		if (inter.avecClasse()) {
			traiterClasse();
		}
		if (instructions.size()==0) instructions.add(new Instruction("// ajouter des instructions"));
		traiterMaxTab();
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
	
	private void traiterElementaireSaisie(){
		Instruction instr;
		Operation oper;
		Argument arg;
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
		else if (inter.avecProcedureSaisie()) {
			instr = new Instruction("saisir");
			oper = new Operation("saisir");
			oper.traiterElementaireSaisieProcedure(infos, instr);
			if (instr.arguments.size()>0) {
				instructions.add(instr);
				operations.add(oper);
			}
		}
		else if (!inter.sansSaisie()){
			instr = new Instruction("lire");
			if (inter.avecSaisieFichierTexte()) {
				arg = new Argument("'fich'", "FICHIER_TEXTE", null);
				arg.instructions.add( new Instruction("// ajouter des instructions"));
				instr.setFichier(arg);
			}
			else if (inter.avecSaisieFormulaire()) {
				arg = new Argument("formu", "FORM", null);
				instr.setFormulaire(arg);
			}
			else if (inter.avecSaisieSql()) {
				arg = new Argument("bd", "SQL", null);
				arg.instructions.add( new Instruction("// ajouter des instructions"));
				instr.setFichier(arg);
			}
			for(Iterator<InfoTypee> iter=infos.iterator(); iter.hasNext(); ) {
				InfoTypee info = iter.next();
				if (info.isIn() || info.isInOut()) {
					arg = new Argument(info.nom, info.type, null);
					instr.arguments.add(arg);
				}
			}
			if (instr.arguments.size()>0) {
				instructions.add(instr);
			}
		}
	}
	
	private void traiterElementaireAffichage(){
		Instruction instr;
		Operation oper;
		Argument arg;
		if (inter.avecProcedureAffichage()) {
			instr = new Instruction("afficher");
			oper = new Operation("afficher");
			oper.traiterElementaireAffichageProcedure(infos, instr);
			if (instr.arguments.size()>0) {
				instructions.add(instr);
				operations.add(oper);
			}
		}
		else if (!inter.sansAffichage()){
			instr = new Instruction("ecrire");
			if (inter.avecAffichageFichierTexte()) {
				arg = new Argument("'fich'", "FICHIER_TEXTE", null);
				instr.setFichier(arg);
			}
			else if (inter.avecAffichageSql()) {
				arg = new Argument("bd", "SQL", null);
				instr.setFichier(arg);
			}
			for(Iterator<InfoTypee> iter=infos.iterator(); iter.hasNext(); ) {
				InfoTypee info = iter.next();
				if (info.isOut() || info.isInOut()) {
					arg = new Argument(info.nom, info.type, null);
					instr.arguments.add(arg);
				}
			}
			if (instr.arguments.size()>0) {
				instructions.add(instr);
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
					traiterLocales(oper.variables);
					instructions.add(instr);
					operations.add(oper);
				}
			}
		}
		else if (inter.avecProcedureCalcul()) {
			instr = new Instruction("calculer");
			oper = new Operation("calculer");
			oper.traiterElementaireCalculProcedure(infos, instr);
			traiterLocales(oper.variables);
			instructions.add(instr);
			operations.add(oper);
		}
	}
	
	private void traiterLocales(ArrayList<org.javascool.proglets.plurialgo.langages.modele.Variable> locales){
		if (locales==null) return;
		for(Iterator<InfoTypee> iter=infos.iterator(); iter.hasNext(); ) {
			InfoTypee info = iter.next();
			if (info.isAutre()) {
				if (inter!=null) {
					if (inter.avecProcedureCalcul() || inter.avecFonctionsCalcul()) {
						Variable var = new Variable(info.nom, info.type, null);
						locales.add(var);
					}
				}
			}
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
	
	private void traiterEnregistrementSaisie(){
		Instruction instr, instr_oper, instr_obj;
		Operation oper=null;
		Argument arg, arg_oper;
		Parametre param_oper;
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
						oper = new Operation("saisir");
						param_oper = new Parametre(classe.getSansIndice(info.nom), info.type, "OUT");
						oper.parametres.add(param_oper);
						instr_oper = new Instruction("lire");
						arg_oper = new Argument(classe.getSansIndice(info.nom), info.type, null);
						instr_oper.arguments.add(arg_oper);
						oper.instructions.add(instr_oper);
						operations.add(oper);
					}
					instr_obj = new Instruction("saisir");
					arg_oper = new Argument(info.nom, info.type, "OUT");
					instr_obj.arguments.add(arg_oper);
					instructions.add(instr_obj);
				}
			}
		}
		else {
			this.traiterElementaireSaisie();
		}
	}
	
	private void traiterEnregistrementAffichage(){
		Instruction instr, instr_oper, instr_obj;
		Operation oper=null;
		Argument arg, arg_oper;
		Parametre param_oper;
		if (inter.avecProcedureAffichage()) {
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
						oper = new Operation("afficher");
						param_oper = new Parametre(classe.getSansIndice(info.nom), info.type, "IN");
						oper.parametres.add(param_oper);
						instr_oper = new Instruction("ecrire");
						arg_oper = new Argument(classe.getSansIndice(info.nom), info.type, null);
						instr_oper.arguments.add(arg_oper);
						oper.instructions.add(instr_oper);
						operations.add(oper);
					}
					instr_obj = new Instruction("afficher");
					arg_oper = new Argument(info.nom, info.type, "IN");
					instr_obj.arguments.add(arg_oper);
					instructions.add(instr_obj);
				}
			}
		}
		else {
			this.traiterElementaireAffichage();
		}
	}
	
	private void traiterEnregistrementCalcul(){
		this.traiterElementaireCalcul();
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
	
	private void traiterClasseSaisie(){
		Instruction instr, instr_oper, instr_obj;
		Operation oper=null;
		Argument arg, arg_oper;
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
		else {
			this.traiterElementaireSaisie();
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
					oper.setRetour( new Variable(info.nom, info.type, null) );
					oper.parametres.addAll(param_donnees);
					classe.operations.add(oper);
				}
			}
		}
		if (inter.avecProcedureCalcul()) {
			instr = new Instruction("calculer");
			oper = new Operation("calculer");
			for(Iterator<InfoTypee> iter=infos.iterator(); iter.hasNext(); ) {
				InfoTypee info = iter.next();
				if ( (info.isIn() || info.isInOut() || info.isOut()) && info!=info_obj ) {
					arg = new Argument(info.nom, info.type, info.mode);
					instr.arguments.add(arg);
					param_oper = new Parametre(info.nom, info.type, info.mode);
					oper.parametres.add(param_oper);
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
	}
	
	private void traiterClasseAffichage(){
		Instruction instr, instr_oper, instr_obj;
		Operation oper=null;
		Argument arg, arg_oper;
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
		else {
			this.traiterElementaireAffichage();
		}
	}
}
