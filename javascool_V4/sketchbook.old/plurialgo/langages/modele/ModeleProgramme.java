/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2013.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.langages.modele;

import java.util.*;

import org.javascool.proglets.plurialgo.divers.*;

/**
 * Cette classe doit être étendue pour chaque langage de programmation implanté.
*/
public class ModeleProgramme extends Noeud {
	
	// variables utilisées par BindingCastor
	/**
	 * Le nom du programme.
	 */
	public String nom;
	/**
	 * Les sous-programmes (fonctions ou procédures).
	 */
	public ArrayList<ModeleOperation> operations;
	/**
	 * Les variables du programme principal.
	 */
	public ArrayList<ModeleVariable> variables;
	/**
	 * Les instructions du programme principal.
	 */
	public ArrayList<ModeleInstruction> instructions;
	/**
	 * Les regroupements (enregistrements ou classes).
	 */
	public ArrayList<ModeleClasse> classes;
	/**
	 * Les programmes (ou bibliothèques) importés.
	 */
	public ArrayList<ModeleProgramme> programmes;
	/**
	 * Les options du programme (utilisées par le paquetage raffinat.inout.langages.xml).
	 */
	public ArrayList<ModeleArgument> options;
	// autres variables
	/**
	 * Le(s) fichier(s) de traduction du programme.
	 */
	public Map<String,StringBuffer> les_fichiers;
	/**
	 * Les messages d'erreur.
	 */
	public StringBuffer buf_error;
	/**
	 * Les messages d'avertissement.
	 */
	public StringBuffer buf_warning;
	
	public ModeleProgramme() {
		// variables utilisées par BindingCastor
		operations = new ArrayList<ModeleOperation>();
		variables = new ArrayList<ModeleVariable>();
		instructions = new ArrayList<ModeleInstruction>();
		classes = new ArrayList<ModeleClasse>();
		programmes = new ArrayList<ModeleProgramme>();
		options = new ArrayList<ModeleArgument>();
		// variables non utilisées par BindingCastor
		les_fichiers = new TreeMap<String,StringBuffer>();
		buf_error = new StringBuffer(); 
		buf_warning = new StringBuffer();
	}

	// ---------------------------------------------
	// Noeuds
	// ---------------------------------------------
	
	public final void parcoursEnfants() {
		for(Iterator<ModeleOperation> iter=operations.iterator(); iter.hasNext(); ) {
			Noeud nd = (Noeud) iter.next();
			nd.parent = this;
			nd.parcoursEnfants();
		}
		for(Iterator<ModeleInstruction> iter=instructions.iterator(); iter.hasNext(); ) {
			Noeud nd = (Noeud) iter.next();
			nd.parent = this;
			nd.parcoursEnfants();
		}
		for(Iterator<ModeleClasse> iter=classes.iterator(); iter.hasNext(); ) {
			Noeud nd = (Noeud) iter.next();
			nd.parent = this;
			nd.parcoursEnfants();
		}
	}

	// ---------------------------------------------
	// Ecriture
	// ---------------------------------------------
	
	/**
	 * A redéfinir pour chaque langage implanté.
	 */
	public void ecrire() {		
	}
	
	public void postTraitement(StringBuffer buf) {		
	}
	
	public final void ecrire2() {
		this.parcoursEnfants(); 
		this.ecrire(); 
		this.buf_error.delete(0, this.buf_error.length()); 
		this.buf_warning.delete(0, this.buf_warning.length()); 
		this.ecrire();
	}
	
	public final void ecrireErreur(String msg) {
		this.buf_error.append(msg);
		this.buf_error.append("\n");
		
	}
	
	public final void ecrireWarning(String msg) {
		this.buf_warning.append(msg);
		this.buf_warning.append("\n");	
	}

	// ---------------------------------------------
	// fonctions booléennes
	// ---------------------------------------------
	
	public final boolean avecFormulaire() {
		for (Iterator<ModeleInstruction> iter=instructions.iterator(); iter.hasNext();) {
			ModeleInstruction instr = iter.next();
			if (instr.getFormulaire()!=null) return true;
		}
		return false;
	}
	
	public final boolean avecSql() {
		for (Iterator<ModeleInstruction> iter=instructions.iterator(); iter.hasNext();) {
			ModeleInstruction instr = iter.next();
			if (instr.isLectureSQL() || instr.isEcritureSQL()) return true;
		}
		return false;
	}
	
	public final boolean avecType(String nom_type) {
		for (Iterator<ModeleVariable> iter=variables.iterator(); iter.hasNext();) {
			ModeleVariable var = iter.next();
			if (var.isType(nom_type)) return true;
		}
		for (Iterator<ModeleClasse> iter=classes.iterator(); iter.hasNext();) {
			ModeleClasse cl = iter.next();
			for (Iterator<ModeleVariable> iter1=cl.proprietes.iterator(); iter1.hasNext();) {
				ModeleVariable var = iter1.next();
				if (var.isType(nom_type)) return true;
			}
		}
		return false;
	}

	// ---------------------------------------------
	// fonctions get
	// ---------------------------------------------
	
	public final ModeleClasse getClasse(String nom_cl) {
		for (Iterator<ModeleClasse> iter=this.classes.iterator(); iter.hasNext();) {
			ModeleClasse cl = iter.next();
			if (cl.nom.equals(nom_cl)) return cl;
		}
		return null;
	}
	
	public final ModeleClasse getClasseOfProp(String nom_prop) {
		for (Iterator<ModeleClasse> iter=this.classes.iterator(); iter.hasNext();) {
			ModeleClasse cl = iter.next();
			for (Iterator<ModeleVariable> iter1=cl.proprietes.iterator(); iter1.hasNext();) {
				ModeleVariable prop = iter1.next();
				if (prop.nom.equals(nom_prop)) return cl;
			}
		}
		return null;
	}
	
	public final ModeleOperation getOperation(String nom_oper) {
		if (nom_oper.contains(".")) {
			int pos = nom_oper.indexOf(".");
			nom_oper = nom_oper.substring(pos+1);
		}
		for (Iterator<ModeleClasse> iter=this.classes.iterator(); iter.hasNext();) {
			ModeleClasse cl = iter.next();
			for (Iterator<ModeleOperation> iter1=cl.operations.iterator(); iter1.hasNext();) {
				ModeleOperation oper = iter1.next();
				if (oper.nom.equals(nom_oper)) {
					if (oper.classe == null) oper.classe = cl;	// ne devrait pas arriver ?
					return oper;
				}
			}
		}
		for (Iterator<ModeleOperation> iter1=this.operations.iterator(); iter1.hasNext();) {
			ModeleOperation oper = iter1.next();
			if (oper.nom.equals(nom_oper)) return oper;
		}
		return null;
	}

	public final ModeleArgument getOption(String nom_opt) {
		for (Iterator<ModeleArgument> iter=this.options.iterator(); iter.hasNext();) {
			ModeleArgument option = iter.next();
			if (option.nom.equalsIgnoreCase(nom_opt)) {
				return option;
			}
		}
		return null;
	}
	
	public final String getOptionMode(String nom_opt) {
		for (Iterator<ModeleArgument> iter=this.options.iterator(); iter.hasNext();) {
			ModeleArgument option = iter.next();
			if (option.nom.equalsIgnoreCase(nom_opt)) {
				if (option.mode!=null) return option.mode;
			}
		}
		return null;
	}

	// ---------------------------------------------
	// fonctions de tableaux
	// ---------------------------------------------
		
	public String getMaxTab() {
		return "MAX_TAB";
	}
	
	public String getDim(int dim, ModeleArgument arg) {
		String res = arg.getDim(dim);
		if (res==null) res=getMaxTab();
		if (res.equals("MAX_TAB")) res=getMaxTab();
		return res;
	}

	// ---------------------------------------------
	// BindingCastor ou solution alternative
	// ---------------------------------------------
	
	public final StringBuffer getXmlBuffer() {
//		return BindingCastor.saveXml(this);
		StringBuffer buf = new StringBuffer();
		ecrireXml(buf, 0);
		return buf;
	}
	
	public final ModeleProgramme getClone(String nom_lang) {
//		return BindingCastor.getProgramme(getXmlBuffer().toString(), nom_lang); 
		return null;
	}	
	
	public final static ModeleProgramme getProgramme(String txt_xml, String nom_lang) {
//		return BindingCastor.getProgramme(txt_xml, nom_lang); 	
		AnalyseurXml analyseur = new AnalyseurXml(txt_xml, nom_lang);
		return analyseur.getProgramme();
	}
	
	void ecrireXml(StringBuffer buf, int indent) {
		Divers.ecrire(buf, "<programme", indent);
		Divers.ecrireAttrXml(buf, "nom", nom);
		Divers.ecrire(buf, ">");
		for (Iterator<ModeleClasse> iter=classes.iterator(); iter.hasNext();) {
			ModeleClasse cl = iter.next();
			cl.ecrireXml(buf, indent+1);
		}
		for (Iterator<ModeleOperation> iter=operations.iterator(); iter.hasNext();) {
			ModeleOperation oper = iter.next();
			oper.ecrireXml(buf, indent+1);
		}
		for (Iterator<ModeleVariable> iter=variables.iterator(); iter.hasNext();) {
			ModeleVariable var =  iter.next();
			var.ecrireXml(buf, indent+1);
		}
		for (Iterator<ModeleInstruction> iter=instructions.iterator(); iter.hasNext();) {
			ModeleInstruction instr = iter.next();
			instr.ecrireXml(buf, indent+1);
		}
		Divers.ecrire(buf, "</programme>", indent);
	}
	
}
