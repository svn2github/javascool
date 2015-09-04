/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2013.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.langages.xml;

import java.util.*;
import org.javascool.proglets.plurialgo.langages.modele.InfoTypee;
import org.javascool.proglets.plurialgo.langages.modele.*;


/**
 * Cette classe permet de créer des squelettes de programmes (bouton Nouveau de l'onglet Principal).
*/
public class ProgrammeNouveau extends XmlProgramme {
	
	private Intermediaire inter; // inter les caractéristiques de l'onglet Principal à prendre en compte
	private ArrayList<InfoTypee> infos; // liste de triplets (nom, type, mode)
	private XmlClasse classe;	 // la classe créée (si création avec regroupement)

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
		if (instructions.size()==0) instructions.add(new XmlInstruction("// ajouter des instructions"));
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
			XmlVariable var = new XmlVariable(info.nom, info.type, null);
			variables.add(var);
		}
		traiterElementaireSaisie();
		traiterElementaireCalcul();
		traiterElementaireAffichage();
	}
	
	private void traiterElementaireSaisie(){
		XmlInstruction instr;
		XmlOperation oper;
		XmlArgument arg;
		if (inter.avecFonctionsSaisie()) {
			for(Iterator<InfoTypee> iter=infos.iterator(); iter.hasNext(); ) {
				InfoTypee info = iter.next();
				if (info.isIn() || info.isInOut()) {
					instr = new XmlInstruction("saisir_"+info.nom);
					oper = new XmlOperation("saisir_"+info.nom);
					oper.traiterElementaireSaisieFonction(info, instr);
					instructions.add(instr);
					operations.add(oper);
				}
			}
		}
		else if (inter.avecProcedureSaisie()) {
			instr = new XmlInstruction("saisir");
			oper = new XmlOperation("saisir");
			oper.traiterElementaireSaisieProcedure(infos, instr);
			if (instr.arguments.size()>0) {
				instructions.add(instr);
				operations.add(oper);
			}
		}
		else if (!inter.sansSaisie()){
			instr = new XmlInstruction("lire");
			if (inter.avecSaisieFichierTexte()) {
				arg = new XmlArgument("'fich'", "FICHIER_TEXTE", null);
				arg.instructions.add( new XmlInstruction("// ajouter des instructions"));
				instr.setFichier(arg);
			}
			else if (inter.avecSaisieFormulaire()) {
				arg = new XmlArgument("formu", "FORM", null);
				instr.setFormulaire(arg);
			}
			else if (inter.avecSaisieSql()) {
				arg = new XmlArgument("bd", "SQL", null);
				arg.instructions.add( new XmlInstruction("// ajouter des instructions"));
				instr.setFichier(arg);
			}
			for(Iterator<InfoTypee> iter=infos.iterator(); iter.hasNext(); ) {
				InfoTypee info = iter.next();
				if (info.isIn() || info.isInOut()) {
					arg = new XmlArgument(info.nom, info.type, null);
					instr.arguments.add(arg);
				}
			}
			if (instr.arguments.size()>0) {
				instructions.add(instr);
			}
		}
	}
	
	private void traiterElementaireAffichage(){
		XmlInstruction instr;
		XmlOperation oper;
		XmlArgument arg;
		if (inter.avecProcedureAffichage()) {
			instr = new XmlInstruction("afficher");
			oper = new XmlOperation("afficher");
			oper.traiterElementaireAffichageProcedure(infos, instr);
			if (instr.arguments.size()>0) {
				instructions.add(instr);
				operations.add(oper);
			}
		}
		else if (!inter.sansAffichage()){
			instr = new XmlInstruction("ecrire");
			if (inter.avecAffichageFichierTexte()) {
				arg = new XmlArgument("'fich'", "FICHIER_TEXTE", null);
				instr.setFichier(arg);
			}
			else if (inter.avecAffichageSql()) {
				arg = new XmlArgument("bd", "SQL", null);
				instr.setFichier(arg);
			}
			for(Iterator<InfoTypee> iter=infos.iterator(); iter.hasNext(); ) {
				InfoTypee info = iter.next();
				if (info.isOut() || info.isInOut()) {
					arg = new XmlArgument(info.nom, info.type, null);
					instr.arguments.add(arg);
				}
			}
			if (instr.arguments.size()>0) {
				instructions.add(instr);
			}
		}
	}
	
	private void traiterElementaireCalcul(){
		XmlInstruction instr;
		XmlOperation oper;
		if (inter.avecFonctionsCalcul()) {
			ArrayList<ModeleArgument> arg_donnees = new ArrayList<ModeleArgument>();
			ArrayList<ModeleParametre> param_donnees = new ArrayList<ModeleParametre>();
			for(Iterator<InfoTypee> iter=infos.iterator(); iter.hasNext(); ) {
				InfoTypee info = iter.next();
				if (info.isIn()) {
					arg_donnees.add (new XmlArgument(info.nom, info.type, info.mode));
					param_donnees.add (new XmlParametre(info.nom, info.type, info.mode));
				}
			}
			for(Iterator<InfoTypee> iter=infos.iterator(); iter.hasNext(); ) {
				InfoTypee info = iter.next();
				if (info.isOut()) {
					instr = new XmlInstruction("calculer_"+info.nom);
					oper = new XmlOperation("calculer_"+info.nom);
					oper.traiterElementaireCalculFonction(info, instr, param_donnees, arg_donnees);
					traiterLocales(oper.variables);
					instructions.add(instr);
					operations.add(oper);
				}
			}
		}
		else if (inter.avecProcedureCalcul()) {
			instr = new XmlInstruction("calculer");
			oper = new XmlOperation("calculer");
			oper.traiterElementaireCalculProcedure(infos, instr);
			traiterLocales(oper.variables);
			instructions.add(instr);
			operations.add(oper);
		}
	}
	
	private void traiterLocales(ArrayList<ModeleVariable> locales){
		if (locales==null) return;
		for(Iterator<InfoTypee> iter=infos.iterator(); iter.hasNext(); ) {
			InfoTypee info = iter.next();
			if (info.isAutre()) {
				if (inter!=null) {
					if (inter.avecProcedureCalcul() || inter.avecFonctionsCalcul()) {
						XmlVariable var = new XmlVariable(info.nom, info.type, null);
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
		XmlClasse enreg = new XmlClasse(nom_cl, true);
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
			XmlVariable var = new XmlVariable(info.nom, info.type, null);
			variables.add(var);
		}
		traiterEnregistrementSaisie();
		traiterEnregistrementCalcul();
		traiterEnregistrementAffichage();
	}
	
	private void traiterEnregistrementSaisie(){
		XmlInstruction instr, instr_oper, instr_obj;
		XmlOperation oper=null;
		XmlArgument arg, arg_oper;
		XmlParametre param_oper;
		if (inter.avecFonctionsSaisie() || inter.avecProcedureSaisie()) {
			// infos non enregistrements
			instr = new XmlInstruction("lire");
			for(Iterator<InfoTypee> iter=infos.iterator(); iter.hasNext(); ) {
				InfoTypee info = iter.next();
				if ( (info.isIn() || info.isInOut()) && (!info.isEnregistrement(this)) ) {
					arg = new XmlArgument(info.nom, info.type, null);
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
						oper = new XmlOperation("saisir");
						param_oper = new XmlParametre(classe.getSansIndice(info.nom), info.type, "OUT");
						oper.parametres.add(param_oper);
						instr_oper = new XmlInstruction("lire");
						arg_oper = new XmlArgument(classe.getSansIndice(info.nom), info.type, null);
						instr_oper.arguments.add(arg_oper);
						oper.instructions.add(instr_oper);
						operations.add(oper);
					}
					instr_obj = new XmlInstruction("saisir");
					arg_oper = new XmlArgument(info.nom, info.type, "OUT");
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
		XmlInstruction instr, instr_oper, instr_obj;
		XmlOperation oper=null;
		XmlArgument arg, arg_oper;
		XmlParametre param_oper;
		if (inter.avecProcedureAffichage()) {
			// infos non enregistrements
			instr = new XmlInstruction("ecrire");
			for(Iterator<InfoTypee> iter=infos.iterator(); iter.hasNext(); ) {
				InfoTypee info = iter.next();
				if ( (info.isOut() || info.isInOut()) && (!info.isEnregistrement(this)) ) {
					arg = new XmlArgument(info.nom, info.type, null);
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
						oper = new XmlOperation("afficher");
						param_oper = new XmlParametre(classe.getSansIndice(info.nom), info.type, "IN");
						oper.parametres.add(param_oper);
						instr_oper = new XmlInstruction("ecrire");
						arg_oper = new XmlArgument(classe.getSansIndice(info.nom), info.type, null);
						instr_oper.arguments.add(arg_oper);
						oper.instructions.add(instr_oper);
						operations.add(oper);
					}
					instr_obj = new XmlInstruction("afficher");
					arg_oper = new XmlArgument(info.nom, info.type, "IN");
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
		classe = new XmlClasse(nom_cl, false);
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
			XmlVariable var = new XmlVariable(info.nom, info.type, null);
			variables.add(var);
		}
		traiterClasseSaisie();
		traiterClasseCalcul();
		traiterClasseAffichage();
		// constructeur
		XmlConstructeur constr = new XmlConstructeur(classe.nom);
		constr.instructions.add( new XmlInstruction("// ajouter des instructions"));
		classe.constructeurs.add(constr);
	}
	
	private void traiterClasseSaisie(){
		XmlInstruction instr, instr_oper, instr_obj;
		XmlOperation oper=null;
		XmlArgument arg, arg_oper;
		if (inter.avecProcedureSaisie()  || inter.avecFonctionsSaisie()) {
			// infos non objets
			instr = new XmlInstruction("lire");
			for(Iterator<InfoTypee> iter=infos.iterator(); iter.hasNext(); ) {
				InfoTypee info = iter.next();
				if ( (info.isIn() || info.isInOut()) && (!info.isClasse(this)) ) {
					arg = new XmlArgument(info.nom, info.type, null);
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
						oper = new XmlOperation("saisir");
						instr_oper = new XmlInstruction("lire");
						arg_oper = new XmlArgument("this", info.type, null);
						instr_oper.arguments.add(arg_oper);
						oper.instructions.add(instr_oper);
						classe.operations.add(oper);
					}
					instr_obj = new XmlInstruction("saisir");
					arg = new XmlArgument(info.nom, info.type, null);
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
		XmlInstruction instr;
		XmlOperation oper=null;
		XmlArgument arg;
		XmlParametre param_oper;
		InfoTypee info_obj=null; XmlArgument obj;
		for(Iterator<InfoTypee> iter=infos.iterator(); iter.hasNext(); ) {
			InfoTypee info = iter.next();
			if ( (info.isClasse(this)) ) {
				info_obj=info; break;
			}
		}
		if (info_obj==null) return;
		obj = new XmlArgument(info_obj.nom, info_obj.type, null);
		if (inter.avecFonctionsCalcul()) {
			ArrayList<ModeleArgument> arg_donnees = new ArrayList<ModeleArgument>();
			ArrayList<ModeleParametre> param_donnees = new ArrayList<ModeleParametre>();
			for(Iterator<InfoTypee> iter=infos.iterator(); iter.hasNext(); ) {
				InfoTypee info = iter.next();
				if (info.isIn() && info!=info_obj) {
					arg_donnees.add (new XmlArgument(info.nom, info.type, info.mode));
					param_donnees.add (new XmlParametre(info.nom, info.type, info.mode));
				}
			}
			for(Iterator<InfoTypee> iter=infos.iterator(); iter.hasNext(); ) {
				InfoTypee info = iter.next();
				if (info.isOut() && info!=info_obj) {
					instr = new XmlInstruction("calculer_"+info.nom);
					arg = new XmlArgument(info.nom, info.type, null);
					instr.setRetour(arg);
					instr.setObjet(obj);
					instr.arguments.addAll(arg_donnees);
					instructions.add(instr);
					oper = new XmlOperation("calculer_"+info.nom);
					oper.setRetour( new XmlVariable(info.nom, info.type, null) );
					oper.parametres.addAll(param_donnees);
					classe.operations.add(oper);
				}
			}
		}
		if (inter.avecProcedureCalcul()) {
			instr = new XmlInstruction("calculer");
			oper = new XmlOperation("calculer");
			for(Iterator<InfoTypee> iter=infos.iterator(); iter.hasNext(); ) {
				InfoTypee info = iter.next();
				if ( (info.isIn() || info.isInOut() || info.isOut()) && info!=info_obj ) {
					arg = new XmlArgument(info.nom, info.type, info.mode);
					instr.arguments.add(arg);
					param_oper = new XmlParametre(info.nom, info.type, info.mode);
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
		XmlInstruction instr, instr_oper, instr_obj;
		XmlOperation oper=null;
		XmlArgument arg, arg_oper;
		if (inter.avecProcedureAffichage()) {
			// avec infos non objets
			instr = new XmlInstruction("ecrire");
			for(Iterator<InfoTypee> iter=infos.iterator(); iter.hasNext(); ) {
				InfoTypee info = iter.next();
				if ( (info.isOut() || info.isInOut()) && (!info.isClasse(this)) ) {
					arg = new XmlArgument(info.nom, info.type, null);
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
						oper = new XmlOperation("afficher");
						instr_oper = new XmlInstruction("ecrire");
						arg_oper = new XmlArgument("this", info.type, null);
						instr_oper.arguments.add(arg_oper);
						oper.instructions.add(instr_oper);
						classe.operations.add(oper);
					}
					instr_obj = new XmlInstruction("afficher");
					arg = new XmlArgument(info.nom, info.type, null);
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
