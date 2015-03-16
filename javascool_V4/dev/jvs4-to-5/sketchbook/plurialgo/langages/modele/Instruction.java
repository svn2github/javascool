/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2013.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.langages.modele;

import java.util.*;

import org.javascool.proglets.plurialgo.divers.Divers;


/**
 * Cette classe doit être étendue pour chaque langage de programmation implanté.
*/
public class Instruction extends Noeud {
	
	// variables utilisées par BindingCastor
	/*
	 * Le nom de l'instruction : lire, ecrire, si, pour, tantque, affectation,
	 * appel de sous-programme, // (commentaire)...
	 */
	public String nom;
	/**
	 * Les arguments de l'instruction.
	 */
	public ArrayList<Argument> arguments;
	/**
	 * L'argument de retour (si fonction).
	 */
	public ArrayList<Argument> retours;	// 0 ou 1 Argument
	/**
	 * L'objet d'appel (si méthode de classe).
	 */
	public ArrayList<Argument> objets;	// 0 ou 1 Argument
	/**
	 * Option de lecture ou d'écriture (FORM_FICHIER_TEXTE, SQL...)
	 */
	public ArrayList<Argument> options;	// 0 ou 1 Argument
	/**
	 * Les alternatives d'une instruction conditionnelle (quand le nom de l'instruction vaut "si").
	 */
	public ArrayList<Si> sis;	
	/**
	 * Boucle Pour (quand le nom de l'instruction vaut "pour").
	 */
	public ArrayList<Pour> pours;	// 0 ou 1 Pour
	/**
	 * Boucle TantQue (quand le nom de l'instruction vaut "tantque").
	 */
	public ArrayList<TantQue> tantques;		// 0 ou 1 Tantque
	/**
	 * Affectation (quand le nom de l'instruction vaut "affectation").
	 */
	public ArrayList<Affectation> affectations;	// 0 ou 1 Affectation
	/**
	 * Bientôt obsolète ?
	 */
	public ArrayList<Instruction> instructions;	
	
	public Instruction() {
		arguments = new ArrayList<Argument>();
		retours = new ArrayList<Argument>();
		objets = new ArrayList<Argument>();
		options = new ArrayList<Argument>();
		sis = new ArrayList<Si>();
		pours = new ArrayList<Pour>();
		tantques = new ArrayList<TantQue>();
		affectations = new ArrayList<Affectation>();
		instructions = new ArrayList<Instruction>();
	}	
	
	public Instruction (String nom) {
		this();
		this.nom = nom;
	}
	
	// ---------------------------------------------
	// Noeuds
	// ---------------------------------------------

	public final void parcoursEnfants() {
		for(Iterator<Argument> iter=arguments.iterator(); iter.hasNext(); ) {
			Noeud nd = (Noeud) iter.next();
			nd.parent = this;
			nd.parcoursEnfants();
		}
		for(Iterator<Si> iter=sis.iterator(); iter.hasNext(); ) {
			Noeud nd = (Noeud) iter.next();
			nd.parent = this;
			nd.parcoursEnfants();
		}
		for(Iterator<Pour> iter=pours.iterator(); iter.hasNext(); ) {
			Noeud nd = (Noeud) iter.next();
			nd.parent = this;
			nd.parcoursEnfants();
		}
		for(Iterator<TantQue> iter=tantques.iterator(); iter.hasNext(); ) {
			Noeud nd = (Noeud) iter.next();
			nd.parent = this;
			nd.parcoursEnfants();
		}
		for(Iterator<Affectation> iter=affectations.iterator(); iter.hasNext(); ) {
			Noeud nd = (Noeud) iter.next();
			nd.parent = this;
			nd.parcoursEnfants();
		}
	}

	// ---------------------------------------------
	// get et set
	// ---------------------------------------------
	
	public final Argument getRetour() {
		if (retours.size()==0) return null;
		return retours.get(0);
	}
		
	public final void setRetour(Argument arg) {
		retours.clear();
		retours.add(arg);
	}
	
	public final Argument getOption() {
		if (options.size()==0) return null;
		return options.get(0);
	}
	
	public final void setOption(Argument arg) {
		options.clear();
		options.add(arg);
	}
	
	public final Argument getFichier() {
		return getOption();
	}
	
	public final void setFichier(Argument arg) {
		setOption(arg);
	}
	
	public final Argument getObjet() {
		if (objets.size()==0) return null;
		return objets.get(0);
	}
	
	public void setObjet(Argument arg) {
		objets.clear();
		objets.add(arg);
	}
	
	public Argument getFormulaire() {
		Argument option = getOption();
		if (option == null) return null;
		if (option.isFormulaire()) return option;
		return null;
	}
	
	public void setFormulaire(Argument arg) {
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
		Argument option = getOption();
		if (option == null) return false;
		return option.isFormulaire();
	}
	
	public boolean isLectureFichierTexte() {
		if (!isLecture()) return false;
		Argument option = getFichier();
		if (option == null) return false;
		return option.isFichierTexte();
	}
	
	public boolean isLectureSQL() {
		if (!isLecture()) return false;
		Argument option = getOption();
		if (option == null) return false;
		return option.isSQL();
	}
	
	public boolean isLectureFichierClasse(Programme prog) {
		if (! isLecture()) return false;
		Argument fichier = getOption();
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
		Argument option = getOption();
		if (option == null) return false;
		return option.isFormulaire();
	}
	
	public boolean isEcritureFichierTexte() {
		if (! isEcriture()) return false;
		Argument option = getOption();
		if (option == null) return false;
		return option.isFichierTexte();
	}
	
	public boolean isEcritureSQL() {
		if (! isEcriture()) return false;
		Argument option = getOption();
		if (option == null) return false;
		return option.isSQL();
	}
	
	public boolean isEcritureFichierClasse(Programme prog) {
		if (! isEcriture()) return false;
		Argument fichier = getFichier();
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
		for (Iterator<Si> iter=sis.iterator(); iter.hasNext();) {
			Si si = iter.next();
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
		for (Iterator<Si> iter=sis.iterator(); iter.hasNext();) {
			Si si = iter.next();
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
		for (Iterator<Pour> iter=pours.iterator(); iter.hasNext();) {
			Pour pour = iter.next();
			pour.schema = this.nom;
			i++; 
		}
		return i;
	}
	
	public int interpreterTantQue() {
		int i=0;
		for (Iterator<TantQue> iter=tantques.iterator(); iter.hasNext();) {
			TantQue tq = iter.next();
			tq.schema = this.nom;
			i++; 
		}
		return i;
	}

	// ---------------------------------------------
	// appel
	// ---------------------------------------------

	public Operation getAppel(Programme prog) {
		if (prog == null) return null;
		for (Iterator<Operation> iter=prog.operations.iterator(); iter.hasNext();) {
			Operation oper = iter.next();
			if (oper.nom.equalsIgnoreCase(this.nom)) return oper;
		}
		for (Iterator<Classe> iter1=prog.classes.iterator(); iter1.hasNext();) {
			Classe cl = iter1.next();
			for (Iterator<Operation> iter=cl.operations.iterator(); iter.hasNext();) {
				Operation oper = iter.next();
				if (oper.nom.equalsIgnoreCase(this.nom)) return oper;
			}
		}
		return null;
	}
	
	public boolean isAppel(Programme prog) {
		return getAppel(prog)!=null;
	}
	
	public boolean isAppelProcedure(Programme prog) {
		Operation oper = getAppel(prog);
		if (oper==null) return false;
		return oper.isProcedure();
	}
	
	public boolean isAppelFonction(Programme prog) {
		Operation oper = getAppel(prog);
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
		if (i<2) return false;	// au moins 1 caractère pour le nom
		int j = nom.lastIndexOf(")"); 
		if (j<i) return false;
		return true;
	}
	
	public String getPrimitive() {
		int i = nom.indexOf("////");
		if (i>0) {
			return nom.substring(0, i).trim();
		}
		i = nom.lastIndexOf(")"); 
		return nom.substring(0, i+1).trim();
	}
	
	public String getPrimitiveCommentaire() {
		int i = nom.indexOf("////");
		if (i>0) {
			return nom.substring(i+4, nom.length()).trim();
		}
		return null;
	}
	
	public void ecrireSql(Programme prog, StringBuffer bufInto, StringBuffer bufVal, StringBuffer bufPrep) {
		for (Iterator<Argument> iter=arguments.iterator(); iter.hasNext();) {
			Argument arg = iter.next();
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
			for (Iterator<Affectation> iter=affectations.iterator(); iter.hasNext();) {
				Affectation aff = iter.next();
				aff.ecrireXml(buf, indent);
			}
		}
		else if (isPour()) {
			for (Iterator<Pour> iter=pours.iterator(); iter.hasNext();) {
				Pour pour = iter.next();
				pour.ecrireXml(buf, indent);
			}
		}
		else if (isTantQue()) {	
			for (Iterator<TantQue> iter=tantques.iterator(); iter.hasNext();) {
				TantQue tq = iter.next();
				tq.ecrireXml(buf, indent);
			}
		}
		else if (isSi()) {	
			Divers.ecrire(buf, "<instruction", indent);
			Divers.ecrireAttrXml(buf, "nom", nom);
			Divers.ecrire(buf, ">");
			for (Iterator<Si> iter=sis.iterator(); iter.hasNext();) {
				Si si = iter.next();
				si.ecrireXml(buf, indent+1);
			}
			Divers.ecrire(buf, "</instruction>", indent);
		}
		else {
			Divers.ecrire(buf, "<instruction", indent);
			Divers.ecrireAttrXml(buf, "nom", nom);
			Divers.ecrire(buf, ">");
			for (Iterator<Argument> iter=arguments.iterator(); iter.hasNext();) {
				Argument arg = iter.next();
				arg.ecrireXml(buf, indent+1);
			}
			for (Iterator<Argument> iter=retours.iterator(); iter.hasNext();) {
				Argument retour = iter.next();
				retour.ecrireRetourXml(buf, indent+1);
			}
			for (Iterator<Argument> iter=objets.iterator(); iter.hasNext();) {
				Argument objet = iter.next();
				objet.ecrireObjetXml(buf, indent+1);
			}
			for (Iterator<Argument> iter=options.iterator(); iter.hasNext();) {
				Argument option = iter.next();
				option.ecrireOptionXml(buf, indent+1);
			}
			Divers.ecrire(buf, "</instruction>", indent);
		}
	}
	
}
