/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2013.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.langages.xml;

import java.util.*;
import org.javascool.proglets.plurialgo.langages.modele.*;

/**
 * Cette classe permet de reformuler un programme (bouton Reformuler de l'onglet Principal).
*/
public class ProgrammeDerive extends XmlProgramme {

	private XmlProgramme prog;	// le programme à reformuler	
	private Intermediaire inter; // la reformulation à appliquer
	private ArrayList<InfoTypee> infos; // liste de triplets (nom, type, mode) 
	private XmlClasse classe; // la classe créée (si reformulation avec regroupement)
	private int nb_obj;

	/**
	 * 	Construit un programme par reformulation
	 *    @param prog le programme à reformuler
	 *    @param inter la reformulation à appliquer
	*/	
	public ProgrammeDerive(XmlProgramme prog, Intermediaire inter) {
		this.prog = prog;
		this.nom = prog.nom; 
		this.inter = inter;
		// récupération des classes et des operations
		for(Iterator<ModeleClasse> iter=prog.classes.iterator(); iter.hasNext(); ) {
			XmlClasse cl = (XmlClasse) iter.next();
			this.classes.add(cl);
		}
		for(Iterator<ModeleOperation> iter=prog.operations.iterator(); iter.hasNext(); ) {
			XmlOperation oper = (XmlOperation) iter.next();
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
		if (instructions.size()==0) instructions.add(new XmlInstruction("// ajouter des instructions"));
		traiterMaxTab();
	}

	/**
	 * 	Construit un programme par reformulation
	 *    @param prog le programme à reformuler (reformulation précisée dans la propriété options du programme)
	*/		
	public ProgrammeDerive(XmlProgramme prog) {
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
			for(Iterator<ModeleInstruction> iter=prog.instructions.iterator(); iter.hasNext(); ) {
				XmlInstruction instr = (XmlInstruction) iter.next();
				if (instr.isLectureStandard() || instr.isLectureFormulaire()) {
					for(Iterator<ModeleArgument> iter1 = instr.arguments.iterator(); iter1.hasNext(); ) {
						XmlArgument arg = (XmlArgument) iter1.next();
						System.out.println(arg.nom);
						infosList.addModes(arg.nom, "IN");
						infosList.addTypes(arg.nom, arg.type);
						infosList.addDim(arg.nom, arg.mode);
					}
					XmlArgument arg = (XmlArgument) instr.getObjet();
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
			for(Iterator<ModeleInstruction> iter=prog.instructions.iterator(); iter.hasNext(); ) {
				XmlInstruction instr = (XmlInstruction) iter.next();
				if (instr.isEcritureStandard()) {
					for(Iterator<ModeleArgument> iter1 = instr.arguments.iterator(); iter1.hasNext(); ) {
						XmlArgument arg = (XmlArgument) iter1.next();
						System.out.println(arg.nom);
						infosList.addModes(arg.nom, "OUT");
						infosList.addTypes(arg.nom, arg.type);
						infosList.addDim(arg.nom, arg.mode);
					}
					XmlArgument arg = (XmlArgument) instr.getObjet();
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
		for(Iterator<ModeleVariable> iter=prog.variables.iterator(); iter.hasNext(); ) {
			XmlVariable var = (XmlVariable) iter.next();
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
			XmlVariable var = new XmlVariable(info.nom, info.type, null);
			variables.add(var);
		}
		traiterElementaireSaisie();
		traiterElementaireCalcul();
		traiterElementaireAffichage();
	}
	
	private void traiterElementaireInstructions(ArrayList<ModeleInstruction> liste1, ArrayList<ModeleInstruction> liste2){
		for(Iterator<ModeleInstruction> iter=liste1.iterator(); iter.hasNext(); ) {
			XmlInstruction instr = (XmlInstruction) iter.next();
			XmlInstruction instr_copie = traiterElementaireInstruction(instr);
			if (instr_copie!=null) liste2.add(instr_copie);
		}
	}
	
	private XmlInstruction traiterElementaireInstruction(XmlInstruction instr){
		XmlInstruction copie = new XmlInstruction(instr.nom); 
		if (instr.isSi()) {
			for (Iterator<ModeleSi> iter=instr.sis.iterator(); iter.hasNext();) {
				XmlSi si = (XmlSi) iter.next();
				XmlSi si_copie = this.traiterElementaireInstructionSi(si);
				if (si_copie!=null) copie.sis.add(si_copie);
			}
			if (copie.sis.size()==0) return null;
		}
		else if (instr.isPour()) {
			for (Iterator<ModelePour> iter=instr.pours.iterator(); iter.hasNext();) {
				XmlPour pour = (XmlPour) iter.next();
				XmlPour pour_copie = this.traiterElementaireInstructionPour(pour);
				if (pour_copie!=null) copie.pours.add(pour_copie);
			}
			if (copie.pours.size()==0) return null;
		}
		else if (instr.isTantQue()) {
			for (Iterator<ModeleTantQue> iter=instr.tantques.iterator(); iter.hasNext();) {
				XmlTantQue tq = (XmlTantQue) iter.next();
				XmlTantQue tq_copie = this.traiterElementaireInstructionTantQue(tq);
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
	
	private XmlSi traiterElementaireInstructionSi(XmlSi si){
		XmlSi copie = new XmlSi();
		copie.condition = si.condition;
		copie.schema = si.schema;
		traiterElementaireInstructions(si.instructions, copie.instructions);
		if (copie.instructions.size()==0) return null;
		return (copie);
	}
	
	private XmlPour traiterElementaireInstructionPour(XmlPour pour){
		XmlPour copie = new XmlPour();
		copie.var = pour.var; copie.debut = pour.debut;
		copie.fin = pour.fin; copie.pas = pour.pas;
		copie.schema = pour.schema;
		traiterElementaireInstructions(pour.instructions, copie.instructions);
		if (copie.instructions.size()==0) return null;
		return (copie);
	}
	
	private XmlTantQue traiterElementaireInstructionTantQue(XmlTantQue tq){
		XmlTantQue copie = new XmlTantQue();
		copie.condition = tq.condition;
		copie.schema = tq.schema;
		traiterElementaireInstructions(tq.instructions, copie.instructions);
		if (copie.instructions.size()==0) return null;
		return (copie);
	}
	
	private void traiterElementaireSaisie(){
		XmlInstruction instr;
		XmlOperation oper;
		XmlArgument arg;
		if (inter.avecSaisieElementaire() || inter.avecSaisieFichierTexte() || inter.avecSaisieFormulaire()  || inter.avecSaisieSql()) {
			instr = new XmlInstruction("lire");
			if (inter.avecSaisieFichierTexte()) {
				arg = new XmlArgument("fich", "FICHIER_TEXTE", null);
				arg.instructions.add( new XmlInstruction("// ajouter des instructions"));
				instr.setFichier(arg);
			}
			if (inter.avecSaisieFormulaire()) {
				arg = new XmlArgument("formu", "FORM", null);
				instr.setFormulaire(arg);
			}
			if (inter.avecSaisieSql()) {
				arg = new XmlArgument("bd", "SQL", null);
				arg.instructions.add( new XmlInstruction("// ajouter des instructions"));
				instr.setFichier(arg);
			}
			for(Iterator<InfoTypee> iter=infos.iterator(); iter.hasNext(); ) {
				InfoTypee info = iter.next();
				if (info.isIn() || info.isInOut()) {
					arg = new XmlArgument(info.nom, info.type, null);
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
					instr = new XmlInstruction("saisir_"+info.nom);
					oper = new XmlOperation("saisir_"+info.nom);
					oper.traiterElementaireSaisieFonction(info, instr);
					instructions.add(instr);
					operations.add(oper);
				}
			}
		}
		if (inter.avecProcedureSaisie()) {
			instr = new XmlInstruction("saisir_"+this.nom);
			oper = new XmlOperation("saisir_"+this.nom);
			oper.traiterElementaireSaisieProcedure(infos, instr);
			if (instr.arguments.size()>0) {
				instructions.add(instr);
				operations.add(oper);
			}
		}
	}
	
	private void traiterElementaireAffichage(){
		XmlInstruction instr;
		XmlOperation oper;
		XmlArgument arg;
		if ( inter.avecAffichageElementaire() || inter.avecAffichageFichierTexte() || inter.avecAffichageSql()) {
			instr = new XmlInstruction("ecrire");
			if (inter.avecAffichageFichierTexte()) {
				arg = new XmlArgument("fich", "FICHIER_TEXTE", null);
				instr.setFichier(arg);
			}
			if (inter.avecAffichageSql()) {
				arg = new XmlArgument("bd", "SQL", null);
				instr.setFichier(arg);
			}
			for(Iterator<InfoTypee> iter=infos.iterator(); iter.hasNext(); ) {
				InfoTypee info = iter.next();
				if (info.isOut() || info.isInOut()) {
					arg = new XmlArgument(info.nom, info.type, null);
					arg.dim = info.dim;
					instr.arguments.add(arg);
				}
			}
			if (instr.arguments.size()>0) {
				instructions.add(instr);
			}
		}
		if (inter.avecProcedureAffichage()) {
			instr = new XmlInstruction("afficher_"+this.nom);
			oper = new XmlOperation("afficher_"+this.nom);
			oper.traiterElementaireAffichageProcedure(infos, instr);
			if (instr.arguments.size()>0) {
				instructions.add(instr);
				operations.add(oper);
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
					this.traiterElementaireInstructions(prog.instructions, oper.instructions);
					oper.traiterElementaireVariables(infos);
					instructions.add(instr);
					operations.add(oper);
				}
			}
		}
		else if (inter.avecProcedureCalcul()) {
			instr = new XmlInstruction("calculer_"+this.nom);
			oper = new XmlOperation("calculer_"+this.nom);
			oper.traiterElementaireCalculProcedure(infos, instr);
			this.traiterElementaireInstructions(prog.instructions, oper.instructions);
			oper.traiterElementaireVariables(infos);
			InfoTypeeList liste = new InfoTypeeList();
			liste.addInfos(infos);
			//if (liste.compterMode("INOUT")>0 || liste.compterMode("OUT")>0) {
				instructions.add(instr);
				operations.add(oper);
			//}
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
		XmlClasse enreg = new XmlClasse(nom_cl, true);
		classes.add(enreg);
		this.classe = enreg;
		infos = enreg.creerProprietes(infos, inter.getProprietesGroupe());
		nb_obj = 0;
		for(Iterator<InfoTypee> iter=infos.iterator(); iter.hasNext(); ) {
			InfoTypee info = iter.next();
			if (info.type.equals(nom_cl)) nb_obj = nb_obj+1;
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
	
	private void traiterEnregistrementInstructions(ArrayList<ModeleInstruction> liste1, ArrayList<ModeleInstruction> liste2, String nom_obj){
		for(Iterator<ModeleInstruction> iter=liste1.iterator(); iter.hasNext(); ) {
			XmlInstruction instr = (XmlInstruction) iter.next();
			XmlInstruction instr_copie = traiterEnregistrementInstruction(instr, nom_obj);
			if (instr_copie!=null) liste2.add(instr_copie);
		}
	}
	
	private XmlInstruction traiterEnregistrementInstruction(XmlInstruction instr, String nom_obj){
		XmlInstruction copie = new XmlInstruction(instr.nom); 
		if (instr.isSi()) {
			for (Iterator<ModeleSi> iter=instr.sis.iterator(); iter.hasNext();) {
				XmlSi si = (XmlSi) iter.next();
				XmlSi si_copie = this.traiterEnregistrementInstructionSi(si, nom_obj);
				if (si_copie!=null) copie.sis.add(si_copie);
			}
			if (copie.sis.size()==0) return null;
		}
		else if (instr.isPour()) {
			for (Iterator<ModelePour> iter=instr.pours.iterator(); iter.hasNext();) {
				XmlPour pour = (XmlPour) iter.next();
				XmlPour pour_copie = this.traiterEnregistrementInstructionPour(pour, nom_obj);
				if (pour_copie!=null) copie.pours.add(pour_copie);
			}
			if (copie.pours.size()==0) return null;
		}
		else if (instr.isTantQue()) {
			for (Iterator<ModeleTantQue> iter=instr.tantques.iterator(); iter.hasNext();) {
				XmlTantQue tq = (XmlTantQue) iter.next();
				XmlTantQue tq_copie = this.traiterEnregistrementInstructionTantQue(tq, nom_obj);
				if (tq_copie!=null) copie.tantques.add(tq_copie);
			}
			if (copie.tantques.size()==0) return null;
		}
		else if (instr.isAffectation()) {
			for (Iterator<ModeleAffectation> iter=instr.affectations.iterator(); iter.hasNext();) {
				XmlAffectation aff = (XmlAffectation) iter.next();
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
	
	private XmlSi traiterEnregistrementInstructionSi(XmlSi si, String nom_obj){
		XmlSi copie = new XmlSi();
		copie.condition = traiterEnregistrementInstruction(si.condition, nom_obj);
		copie.schema = si.schema;
		traiterEnregistrementInstructions(si.instructions, copie.instructions, nom_obj);
		if (copie.instructions.size()==0) return null;
		return (copie);
	}
	
	private XmlPour traiterEnregistrementInstructionPour(XmlPour pour, String nom_obj){
		XmlPour copie = new XmlPour();
		copie.var = pour.var;
		copie.debut = traiterEnregistrementInstruction(pour.debut, nom_obj);
		copie.fin = traiterEnregistrementInstruction(pour.fin, nom_obj);
		copie.pas = traiterEnregistrementInstruction(pour.pas, nom_obj);
		copie.schema = pour.schema;
		traiterEnregistrementInstructions(pour.instructions, copie.instructions, nom_obj);
		if (copie.instructions.size()==0) return null;
		return (copie);
	}
	
	private XmlTantQue traiterEnregistrementInstructionTantQue(XmlTantQue tq, String nom_obj){
		XmlTantQue copie = new XmlTantQue();
		copie.condition = traiterEnregistrementInstruction(tq.condition, nom_obj);
		copie.schema = tq.schema;
		traiterEnregistrementInstructions(tq.instructions, copie.instructions, nom_obj);
		if (copie.instructions.size()==0) return null;
		return (copie);
	}
	
	private XmlAffectation traiterEnregistrementInstructionAffectation(XmlAffectation aff, String nom_obj){
		XmlAffectation copie = new XmlAffectation();
		copie.var = traiterEnregistrementInstruction(aff.var, nom_obj);
		copie.expression = traiterEnregistrementInstruction(aff.expression, nom_obj);
		return (copie);
	}
	
	private XmlInstruction traiterEnregistrementArguments(XmlInstruction instr, String nom_obj){
		XmlInstruction copie = new XmlInstruction(instr.nom);
		XmlArgument arg, arg_copie;
		for (Iterator<ModeleArgument> iter=instr.arguments.iterator(); iter.hasNext();) {
			arg = (XmlArgument) iter.next();
			arg_copie = new XmlArgument(arg.nom, arg.type, arg.mode);
			copie.arguments.add(arg_copie);
			arg_copie.nom = traiterEnregistrementInstruction(arg.nom, nom_obj);
		}
		arg = (XmlArgument) instr.getRetour();
		if (arg!=null) {
			arg_copie = new XmlArgument(arg.nom, arg.type, arg.mode);
			copie.retours.add(arg_copie);
			arg_copie.nom = traiterEnregistrementInstruction(arg.nom, nom_obj);
		}
		arg = (XmlArgument) instr.getObjet();
		if (arg!=null) {
			arg_copie = new XmlArgument(arg.nom, arg.type, arg.mode);
			copie.objets.add(arg_copie);
			arg_copie.nom = traiterEnregistrementInstruction(arg.nom, nom_obj);
		}
		return (copie);
	}
	
	private String traiterEnregistrementInstruction(String txt, String nom_obj){
		if (txt==null) return "";
		//System.out.println("avant remplacer : " + txt);
		StringBuffer buf = new StringBuffer(" " + txt + " ");
		for (Iterator<ModeleVariable> iter=this.classe.proprietes.iterator(); iter.hasNext();) {
			XmlVariable prop = (XmlVariable) iter.next();
			//System.out.println("propriete : " + prop.nom);
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
						if (nom_obj.equals("objet") && ch.equals("1") && (nb_obj==1)) {
							nouveau = "objet" + "." + prop.nom;
						}
						else if (nom_obj.equals("objet")) {
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
		XmlInstruction instr, instr_oper, instr_obj;
		XmlOperation oper=null;
		XmlArgument arg, arg_oper;
		XmlParametre param_oper;
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
						oper = new XmlOperation("saisir_"+inter.getNomGroupe());
						param_oper = new XmlParametre(classe.getSansIndice(info.nom), info.type, "OUT");
						oper.parametres.add(param_oper);
						instr_oper = new XmlInstruction("lire");
						arg_oper = new XmlArgument(classe.getSansIndice(info.nom), info.type, null);
						if (info.dim!=null) {
							arg_oper.dim = this.traiterEnregistrementInstruction(info.dim, arg_oper.nom);
						}
						instr_oper.arguments.add(arg_oper);
						oper.instructions.add(instr_oper);
						operations.add(oper);
					}
					instr_obj = new XmlInstruction("saisir_"+inter.getNomGroupe());
					arg_oper = new XmlArgument(info.nom, info.type, "OUT");
					instr_obj.arguments.add(arg_oper);
					instructions.add(instr_obj);
				}
			}
		}
	}
	
	private void traiterEnregistrementAffichage(){
		XmlInstruction instr, instr_oper, instr_obj;
		XmlOperation oper=null;
		XmlArgument arg, arg_oper;
		XmlParametre param_oper;
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
						oper = new XmlOperation("afficher_"+inter.getNomGroupe());
						param_oper = new XmlParametre(classe.getSansIndice(info.nom), info.type, "IN");
						oper.parametres.add(param_oper);
						instr_oper = new XmlInstruction("ecrire");
						arg_oper = new XmlArgument(classe.getSansIndice(info.nom), info.type, null);
						if (info.dim!=null) {
							arg_oper.dim = this.traiterEnregistrementInstruction(info.dim, arg_oper.nom);
						}
						instr_oper.arguments.add(arg_oper);
						oper.instructions.add(instr_oper);
						operations.add(oper);
					}
					instr_obj = new XmlInstruction("afficher_"+inter.getNomGroupe());
					arg_oper = new XmlArgument(info.nom, info.type, "IN");
					instr_obj.arguments.add(arg_oper);
					instructions.add(instr_obj);
				}
			}
		}
	}
	
	private void traiterEnregistrementCalcul(){
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
					this.traiterEnregistrementInstructions(prog.instructions, oper.instructions, "objet");
					oper.traiterElementaireCalculFonction(info, instr, param_donnees, arg_donnees);
					oper.traiterElementaireVariables(infos);
					instructions.add(instr);
					operations.add(oper);
				}
			}
		}
		else if (inter.avecProcedureCalcul()) {
			instr = new XmlInstruction("calculer");
			oper = new XmlOperation("calculer");
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
	
	private void traiterClasseInstructions(ArrayList<ModeleInstruction> liste1, ArrayList<ModeleInstruction> liste2, String nom_obj){
		for(Iterator<ModeleInstruction> iter=liste1.iterator(); iter.hasNext(); ) {
			XmlInstruction instr = (XmlInstruction) iter.next();
			XmlInstruction instr_copie = traiterClasseInstruction(instr, nom_obj);
			if (instr_copie!=null) liste2.add(instr_copie);
		}
	}
	
	private XmlInstruction traiterClasseInstruction(XmlInstruction instr, String nom_obj){
		XmlInstruction copie = new XmlInstruction(instr.nom); 
		if (instr.isSi()) {
			for (Iterator<ModeleSi> iter=instr.sis.iterator(); iter.hasNext();) {
				XmlSi si = (XmlSi) iter.next();
				XmlSi si_copie = this.traiterClasseInstructionSi(si, nom_obj);
				if (si_copie!=null) copie.sis.add(si_copie);
			}
			if (copie.sis.size()==0) return null;
		}
		else if (instr.isPour()) {
			for (Iterator<ModelePour> iter=instr.pours.iterator(); iter.hasNext();) {
				XmlPour pour = (XmlPour) iter.next();
				XmlPour pour_copie = this.traiterClasseInstructionPour(pour, nom_obj);
				if (pour_copie!=null) copie.pours.add(pour_copie);
			}
			if (copie.pours.size()==0) return null;
		}
		else if (instr.isTantQue()) {
			for (Iterator<ModeleTantQue> iter=instr.tantques.iterator(); iter.hasNext();) {
				XmlTantQue tq = (XmlTantQue) iter.next();
				XmlTantQue tq_copie = this.traiterClasseInstructionTantQue(tq, nom_obj);
				if (tq_copie!=null) copie.tantques.add(tq_copie);
			}
			if (copie.tantques.size()==0) return null;
		}
		else if (instr.isAffectation()) {
			for (Iterator<ModeleAffectation> iter=instr.affectations.iterator(); iter.hasNext();) {
				XmlAffectation aff = (XmlAffectation) iter.next();
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
	
	private XmlSi traiterClasseInstructionSi(XmlSi si, String nom_obj){
		XmlSi copie = new XmlSi();
		copie.condition = traiterClasseInstruction(si.condition, nom_obj);
		copie.schema = si.schema;
		traiterClasseInstructions(si.instructions, copie.instructions, nom_obj);
		if (copie.instructions.size()==0) return null;
		return (copie);
	}
	
	private XmlPour traiterClasseInstructionPour(XmlPour pour, String nom_obj){
		XmlPour copie = new XmlPour();
		copie.var = pour.var;
		copie.debut = traiterClasseInstruction(pour.debut, nom_obj);
		copie.fin = traiterClasseInstruction(pour.fin, nom_obj);
		copie.pas = traiterClasseInstruction(pour.pas, nom_obj);
		copie.schema = pour.schema;
		traiterClasseInstructions(pour.instructions, copie.instructions, nom_obj);
		if (copie.instructions.size()==0) return null;
		return (copie);
	}
	
	private XmlTantQue traiterClasseInstructionTantQue(XmlTantQue tq, String nom_obj){
		XmlTantQue copie = new XmlTantQue();
		copie.condition = traiterClasseInstruction(tq.condition, nom_obj);
		copie.schema = tq.schema;
		traiterClasseInstructions(tq.instructions, copie.instructions, nom_obj);
		if (copie.instructions.size()==0) return null;
		return (copie);
	}
	
	private XmlAffectation traiterClasseInstructionAffectation(XmlAffectation aff, String nom_obj){
		XmlAffectation copie = new XmlAffectation();
		copie.var = traiterClasseInstruction(aff.var, nom_obj);
		copie.expression = traiterClasseInstruction(aff.expression, nom_obj);
		return (copie);
	}
	
	private XmlInstruction traiterClasseArguments(XmlInstruction instr, String nom_obj){
		XmlInstruction copie = new XmlInstruction(instr.nom);
		XmlArgument arg, arg_copie;
		for (Iterator<ModeleArgument> iter=instr.arguments.iterator(); iter.hasNext();) {
			arg = (XmlArgument) iter.next();
			arg_copie = new XmlArgument(arg.nom, arg.type, arg.mode);
			copie.arguments.add(arg_copie);
			arg_copie.nom = traiterEnregistrementInstruction(arg.nom, nom_obj);
		}
		arg = (XmlArgument) instr.getRetour();
		if (arg!=null) {
			arg_copie = new XmlArgument(arg.nom, arg.type, arg.mode);
			copie.retours.add(arg_copie);
			arg_copie.nom = traiterEnregistrementInstruction(arg.nom, nom_obj);
		}
		arg = (XmlArgument) instr.getObjet();
		if (arg!=null) {
			arg_copie = new XmlArgument(arg.nom, arg.type, arg.mode);
			copie.objets.add(arg_copie);
			arg_copie.nom = traiterEnregistrementInstruction(arg.nom, nom_obj);
		}
		return (copie);
	}
	
	private String traiterClasseInstruction(String txt, String nom_obj){
		if (txt==null) return "";
		//System.out.println("avant remplacer : " + txt);
		StringBuffer buf = new StringBuffer(" " + txt + " ");
		for (Iterator<ModeleVariable> iter=this.classe.proprietes.iterator(); iter.hasNext();) {
			XmlVariable prop = (XmlVariable) iter.next();
			//System.out.println("propriete : " + prop.nom);
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
		//System.out.println("apres remplacer : " + buf.toString());
		return buf.toString();
	}
	
	private void traiterClasseSaisie(){
		XmlInstruction instr, instr_oper, instr_obj;
		XmlOperation oper=null;
		XmlArgument arg, arg_oper;
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
						if (info.dim!=null) {
							arg_oper.dim = this.traiterClasseInstruction(info.dim, arg_oper.nom);
						}
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
					this.traiterClasseInstructions(prog.instructions, oper.instructions, "this");
					oper.setRetour( new XmlVariable(info.nom, info.type, null) );
					oper.parametres.addAll(param_donnees);
					oper.traiterElementaireVariables(infos);
					classe.operations.add(oper);
				}
			}
		}
		else if (inter.avecProcedureCalcul()) {
			instr = new XmlInstruction("calculer");
			oper = new XmlOperation("calculer");
			this.traiterClasseInstructions(prog.instructions, oper.instructions, "this");
			for(Iterator<InfoTypee> iter=infos.iterator(); iter.hasNext(); ) {
				InfoTypee info = iter.next();
				if ( (info.isIn() || info.isInOut() || info.isOut()) && info!=info_obj ) {
					arg = new XmlArgument(info.nom, info.type, info.mode);
					instr.arguments.add(arg);
					param_oper = new XmlParametre(info.nom, info.type, info.mode);
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
		XmlInstruction instr, instr_oper, instr_obj;
		XmlOperation oper=null;
		XmlArgument arg, arg_oper;
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
						if (info.dim!=null) {
							arg_oper.dim = this.traiterClasseInstruction(info.dim, arg_oper.nom);
						}
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
	}
}
