/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2013.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.langages.modele;

import java.util.*;

import org.javascool.proglets.plurialgo.divers.Divers;


/**
 * Cette classe doit être étendue pour chaque langage de programmation implanté.
*/
public class ModeleInstruction extends Noeud {
	
	// variables utilisées par BindingCastor
	/*
	 * Le nom de l'instruction : lire, ecrire, si, pour, tantque, affectation,
	 * appel de sous-programme, // (commentaire)...
	 */
	public String nom;
	/**
	 * Les arguments de l'instruction.
	 */
	public ArrayList<ModeleArgument> arguments;
	/**
	 * L'argument de retour (si fonction).
	 */
	public ArrayList<ModeleArgument> retours;	// 0 ou 1 Argument
	/**
	 * L'objet d'appel (si méthode de classe).
	 */
	public ArrayList<ModeleArgument> objets;	// 0 ou 1 Argument
	/**
	 * Option de lecture ou d'écriture (FORM_FICHIER_TEXTE, SQL...)
	 */
	public ArrayList<ModeleArgument> options;	// 0 ou 1 Argument
	/**
	 * Les alternatives d'une instruction conditionnelle (quand le nom de l'instruction vaut "si").
	 */
	public ArrayList<ModeleSi> sis;	
	/**
	 * Boucle Pour (quand le nom de l'instruction vaut "pour").
	 */
	public ArrayList<ModelePour> pours;	// 0 ou 1 Pour
	/**
	 * Boucle TantQue (quand le nom de l'instruction vaut "tantque").
	 */
	public ArrayList<ModeleTantQue> tantques;		// 0 ou 1 Tantque
	/**
	 * Affectation (quand le nom de l'instruction vaut "affectation").
	 */
	public ArrayList<ModeleAffectation> affectations;	// 0 ou 1 Affectation
	
	public ModeleInstruction() {
		arguments = new ArrayList<ModeleArgument>();
		retours = new ArrayList<ModeleArgument>();
		objets = new ArrayList<ModeleArgument>();
		options = new ArrayList<ModeleArgument>();
		sis = new ArrayList<ModeleSi>();
		pours = new ArrayList<ModelePour>();
		tantques = new ArrayList<ModeleTantQue>();
		affectations = new ArrayList<ModeleAffectation>();
	}	
	
	public ModeleInstruction (String nom) {
		this();
		this.nom = nom;
	}
	
	public void ecrire(ModeleProgramme prog, StringBuffer buf, int indent) {
		// à redéfinir pour chaque langage
	}
	
	// ---------------------------------------------
	// Noeuds
	// ---------------------------------------------

	public final void parcoursEnfants() {
		for(Iterator<ModeleArgument> iter=arguments.iterator(); iter.hasNext(); ) {
			Noeud nd = (Noeud) iter.next();
			nd.parent = this;
			nd.parcoursEnfants();
		}
		for(Iterator<ModeleSi> iter=sis.iterator(); iter.hasNext(); ) {
			Noeud nd = (Noeud) iter.next();
			nd.parent = this;
			nd.parcoursEnfants();
		}
		for(Iterator<ModelePour> iter=pours.iterator(); iter.hasNext(); ) {
			Noeud nd = (Noeud) iter.next();
			nd.parent = this;
			nd.parcoursEnfants();
		}
		for(Iterator<ModeleTantQue> iter=tantques.iterator(); iter.hasNext(); ) {
			Noeud nd = (Noeud) iter.next();
			nd.parent = this;
			nd.parcoursEnfants();
		}
		for(Iterator<ModeleAffectation> iter=affectations.iterator(); iter.hasNext(); ) {
			Noeud nd = (Noeud) iter.next();
			nd.parent = this;
			nd.parcoursEnfants();
		}
	}

	// ---------------------------------------------
	// get et set
	// ---------------------------------------------
	
	public final ModeleArgument getRetour() {
		if (retours.size()==0) return null;
		return retours.get(0);
	}
		
	public final void setRetour(ModeleArgument arg) {
		retours.clear();
		retours.add(arg);
	}
	
	public final ModeleArgument getOption() {
		if (options.size()==0) return null;
		return options.get(0);
	}
	
	public final void setOption(ModeleArgument arg) {
		options.clear();
		options.add(arg);
	}
	
	public final ModeleArgument getFichier() {
		return getOption();
	}
	
	public final void setFichier(ModeleArgument arg) {
		setOption(arg);
	}
	
	public final ModeleArgument getObjet() {
		if (objets.size()==0) return null;
		return objets.get(0);
	}
	
	public void setObjet(ModeleArgument arg) {
		objets.clear();
		objets.add(arg);
	}
	
	public ModeleArgument getFormulaire() {
		ModeleArgument option = getOption();
		if (option == null) return null;
		if (option.isFormulaire()) return option;
		return null;
	}
	
	public void setFormulaire(ModeleArgument arg) {
		setOption(arg);
	}

	// ---------------------------------------------
	// fonctions booleennes
	// ---------------------------------------------
	
	public boolean isLecture() {
		return "lire".equalsIgnoreCase(nom);
	}
	
	public boolean isLectureStandard() {
		return isLecture() && (getOption()==null);
	}
	
	public boolean isLectureFormulaire() {
		if (!isLecture()) return false;
		ModeleArgument option = getOption();
		if (option == null) return false;
		return option.isFormulaire();
	}
	
	public boolean isLectureFichierTexte() {
		if (!isLecture()) return false;
		ModeleArgument option = getFichier();
		if (option == null) return false;
		return option.isFichierTexte();
	}
	
	public boolean isLectureSQL() {
		if (!isLecture()) return false;
		ModeleArgument option = getOption();
		if (option == null) return false;
		return option.isSQL();
	}
	
	public boolean isLectureFichierClasse(ModeleProgramme prog) {
		if (! isLecture()) return false;
		ModeleArgument fichier = getOption();
		if (fichier==null) return false;
		return fichier.isFichierClasse(prog);
	}
	
	public boolean isEcriture() {
		return "ecrire".equalsIgnoreCase(nom);
	}
	
	public boolean isEcritureStandard() {
		return isEcriture() && getOption()==null;
	}
	
	public boolean isEcritureFormulaire() {
		if (! isEcriture()) return false;
		ModeleArgument option = getOption();
		if (option == null) return false;
		return option.isFormulaire();
	}
	
	public boolean isEcritureFichierTexte() {
		if (! isEcriture()) return false;
		ModeleArgument option = getOption();
		if (option == null) return false;
		return option.isFichierTexte();
	}
	
	public boolean isEcritureSQL() {
		if (! isEcriture()) return false;
		ModeleArgument option = getOption();
		if (option == null) return false;
		return option.isSQL();
	}
	
	public boolean isEcritureFichierClasse(ModeleProgramme prog) {
		if (! isEcriture()) return false;
		ModeleArgument fichier = getFichier();
		if (fichier==null) return false;
		return fichier.isFichierClasse(prog);
	}

	// ---------------------------------------------
	// instructions conditionnelles
	// ---------------------------------------------
	
	public boolean isSi() {
		if (nom==null) return false;
		if (nom.equalsIgnoreCase("_if_")) return true;
		if (nom.equalsIgnoreCase("_select_")) return true;
		if (nom.equalsIgnoreCase("si")) return true;
		if (nom.startsWith("si.")) return true;
		return false;
	}
	
	public boolean isSelon() {
		if (!isSi()) return false;
		if (nom.equalsIgnoreCase("_select_")) return true;
		if (nom.equalsIgnoreCase("si.selon")) return true;
		return false;
	}

	public int interpreterSi() {
		int i=0; String schema=nom+".si";
		for (Iterator<ModeleSi> iter=sis.iterator(); iter.hasNext();) {
			ModeleSi si = iter.next();
			if ( (!iter.hasNext()) && (i>0) && (si.condition.trim().isEmpty()) ){
				schema = nom+".sinon";
				// if (si.condition.length()>0) schema=nom+".sinonsi";
			}
			si.schema = schema;
			i++; schema = nom+".sinonsi";
		}
		return i;
	}
	
	public String getVariableSelon() {
		String var;
		for (Iterator<ModeleSi> iter=sis.iterator(); iter.hasNext();) {
			ModeleSi si = iter.next();
			var = si.getVariableSelon();
			if ( var!=null )return var;
		}
		return "var";
	}

	// ---------------------------------------------
	// boucles
	// ---------------------------------------------
	
	public boolean isPour() {
		if (nom==null) return false;
		if (nom.equalsIgnoreCase("pour")) return true;
		if (nom.startsWith("pour.")) return true;
		return false;
	}
	
	public boolean isTantQue() {
		if (nom==null) return false;
		if (nom.equalsIgnoreCase("tantque")) return true;
		if (nom.startsWith("tantque.")) return true;
		return false;
	}

	public int interpreterPour() {
		int i=0;
		for (Iterator<ModelePour> iter=pours.iterator(); iter.hasNext();) {
			ModelePour pour = iter.next();
			pour.schema = this.nom;
			i++; 
		}
		return i;
	}
	
	public int interpreterTantQue() {
		int i=0;
		for (Iterator<ModeleTantQue> iter=tantques.iterator(); iter.hasNext();) {
			ModeleTantQue tq = iter.next();
			tq.schema = this.nom;
			i++; 
		}
		return i;
	}

	// ---------------------------------------------
	// appel
	// ---------------------------------------------

	public ModeleOperation getAppel(ModeleProgramme prog) {
		if (prog == null) return null;
		for (Iterator<ModeleOperation> iter=prog.operations.iterator(); iter.hasNext();) {
			ModeleOperation oper = iter.next();
			if (oper.nom.equalsIgnoreCase(this.nom)) return oper;
		}
		for (Iterator<ModeleClasse> iter1=prog.classes.iterator(); iter1.hasNext();) {
			ModeleClasse cl = iter1.next();
			for (Iterator<ModeleOperation> iter=cl.operations.iterator(); iter.hasNext();) {
				ModeleOperation oper = iter.next();
				if (oper.nom.equalsIgnoreCase(this.nom)) return oper;
			}
		}
		return null;
	}
	
	public boolean isAppel(ModeleProgramme prog) {
		return getAppel(prog)!=null;
	}
	
	public boolean isAppelProcedure(ModeleProgramme prog) {
		ModeleOperation oper = getAppel(prog);
		if (oper==null) return false;
		return oper.isProcedure();
	}
	
	public boolean isAppelFonction(ModeleProgramme prog) {
		ModeleOperation oper = getAppel(prog);
		if (oper==null) return false;
		return oper.isFonction();
	}

	// ---------------------------------------------
	// divers
	// ---------------------------------------------

	public boolean isAffectation() {
		if (nom==null) return false;
		if (nom.equalsIgnoreCase("affectation")) return true;
		return false;
	}
	
	public boolean isCommentaire() {
		if (nom==null) return false;
		return nom.startsWith("//");
	}
	
	public boolean isPrimitive() {
		if (nom==null) return false;
		if (nom.startsWith("//")) return false;
		if (nom.contains("////")) return true;
		int i = nom.indexOf("("); 
		if (i<1) return false;	// au moins 1 caractère pour le nom
		int j = nom.lastIndexOf(")"); 
		if (j<i) return false;
		String nom_prim = nom.substring(0, i).trim();
		if (!Divers.isIdent(nom_prim)) return false;
		return true;
	}
	
	public String getPrimitive() {
		int i = nom.lastIndexOf(")"); 
		return nom.substring(0, i+1).trim();
	}
	

	
	public void ecrireSql(ModeleProgramme prog, StringBuffer bufInto, StringBuffer bufVal, StringBuffer bufPrep) {
		for (Iterator<ModeleArgument> iter=arguments.iterator(); iter.hasNext();) {
			ModeleArgument arg = iter.next();
			arg.ecrireSql(prog, bufInto, bufVal, bufPrep);
		}
		if (bufInto.lastIndexOf(",")>=0) bufInto.deleteCharAt(bufInto.lastIndexOf(","));
		if (bufVal.lastIndexOf(",")>=0) bufVal.deleteCharAt(bufVal.lastIndexOf(","));
		if (bufPrep.lastIndexOf(",")>=0) bufPrep.deleteCharAt(bufPrep.lastIndexOf(","));
	}

	// ---------------------------------------------
	// Xml
	// ---------------------------------------------
	
	void ecrireXml(StringBuffer buf, int indent) {
		if (isAffectation()) {
			for (Iterator<ModeleAffectation> iter=affectations.iterator(); iter.hasNext();) {
				ModeleAffectation aff = iter.next();
				aff.ecrireXml(buf, indent);
			}
		}
		else if (isPour()) {
			for (Iterator<ModelePour> iter=pours.iterator(); iter.hasNext();) {
				ModelePour pour = iter.next();
				pour.ecrireXml(buf, indent);
			}
		}
		else if (isTantQue()) {	
			for (Iterator<ModeleTantQue> iter=tantques.iterator(); iter.hasNext();) {
				ModeleTantQue tq = iter.next();
				tq.ecrireXml(buf, indent);
			}
		}
		else if (isSi()) {	
			Divers.ecrire(buf, "<instruction", indent);
			Divers.ecrireAttrXml(buf, "nom", nom);
			Divers.ecrire(buf, ">");
			for (Iterator<ModeleSi> iter=sis.iterator(); iter.hasNext();) {
				ModeleSi si = iter.next();
				si.ecrireXml(buf, indent+1);
			}
			Divers.ecrire(buf, "</instruction>", indent);
		}
		else {
			Divers.ecrire(buf, "<instruction", indent);
			Divers.ecrireAttrXml(buf, "nom", nom);
			Divers.ecrire(buf, ">");
			for (Iterator<ModeleArgument> iter=arguments.iterator(); iter.hasNext();) {
				ModeleArgument arg = iter.next();
				arg.ecrireXml(buf, indent+1);
			}
			for (Iterator<ModeleArgument> iter=retours.iterator(); iter.hasNext();) {
				ModeleArgument retour = iter.next();
				retour.ecrireRetourXml(buf, indent+1);
			}
			for (Iterator<ModeleArgument> iter=objets.iterator(); iter.hasNext();) {
				ModeleArgument objet = iter.next();
				objet.ecrireObjetXml(buf, indent+1);
			}
			for (Iterator<ModeleArgument> iter=options.iterator(); iter.hasNext();) {
				ModeleArgument option = iter.next();
				option.ecrireOptionXml(buf, indent+1);
			}
			Divers.ecrire(buf, "</instruction>", indent);
		}
	}
	
}
