/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2013.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.langages.modele;

import java.util.*;

import org.javascool.proglets.plurialgo.divers.*;

/**
 * Cette classe doit être étendue pour chaque langage de programmation implanté.
*/
public class Programme extends Noeud {
	
	// variables utilisées par BindingCastor
	/**
	 * Le nom du programme.
	 */
	public String nom;
	/**
	 * Les sous-programmes (fonctions ou procédures).
	 */
	public ArrayList<Operation> operations;
	/**
	 * Les variables du programme principal.
	 */
	public ArrayList<Variable> variables;
	/**
	 * Les instructions du programme principal.
	 */
	public ArrayList<Instruction> instructions;
	/**
	 * Les regroupements (enregistrements ou classes).
	 */
	public ArrayList<Classe> classes;
	/**
	 * Les programmes (ou bibliothèques) importés.
	 */
	public ArrayList<Programme> programmes;
	/**
	 * Les options du programme (utilisées par le paquetage raffinat.inout.langages.xml).
	 */
	public ArrayList<Argument> options;
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
	
	public Programme() {
		// variables utilisées par BindingCastor
		operations = new ArrayList<Operation>();
		variables = new ArrayList<Variable>();
		instructions = new ArrayList<Instruction>();
		classes = new ArrayList<Classe>();
		programmes = new ArrayList<Programme>();
		options = new ArrayList<Argument>();
		// variables non utilisées par BindingCastor
		les_fichiers = new TreeMap<String,StringBuffer>();
		buf_error = new StringBuffer(); 
		buf_warning = new StringBuffer();
	}

	// ---------------------------------------------
	// Noeuds
	// ---------------------------------------------
	
	public final void parcoursEnfants() {
		for(Iterator<Operation> iter=operations.iterator(); iter.hasNext(); ) {
			Noeud nd = (Noeud) iter.next();
			nd.parent = this;
			nd.parcoursEnfants();
		}
		for(Iterator<Instruction> iter=instructions.iterator(); iter.hasNext(); ) {
			Noeud nd = (Noeud) iter.next();
			nd.parent = this;
			nd.parcoursEnfants();
		}
		for(Iterator<Classe> iter=classes.iterator(); iter.hasNext(); ) {
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
	public void ecrire() {}
	
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
		for (Iterator<Instruction> iter=instructions.iterator(); iter.hasNext();) {
			Instruction instr = iter.next();
			if (instr.getFormulaire()!=null) return true;
		}
		return false;
	}
	
	public final boolean avecSql() {
		for (Iterator<Instruction> iter=instructions.iterator(); iter.hasNext();) {
			Instruction instr = iter.next();
			if (instr.isLectureSQL() || instr.isEcritureSQL()) return true;
		}
		return false;
	}
	
	public final boolean avecType(String nom_type) {
		for (Iterator<Variable> iter=variables.iterator(); iter.hasNext();) {
			Variable var = iter.next();
			if (var.isType(nom_type)) return true;
		}
		for (Iterator<Classe> iter=classes.iterator(); iter.hasNext();) {
			Classe cl = iter.next();
			for (Iterator<Variable> iter1=cl.proprietes.iterator(); iter1.hasNext();) {
				Variable var = iter1.next();
				if (var.isType(nom_type)) return true;
			}
		}
		return false;
	}

	// ---------------------------------------------
	// fonctions get
	// ---------------------------------------------
	
	public final Classe getClasse(String nom_cl) {
		for (Iterator<Classe> iter=this.classes.iterator(); iter.hasNext();) {
			Classe cl = iter.next();
			if (cl.nom.equals(nom_cl)) return cl;
		}
		return null;
	}
	
	public final Classe getClasseOfProp(String nom_prop) {
		for (Iterator<Classe> iter=this.classes.iterator(); iter.hasNext();) {
			Classe cl = iter.next();
			for (Iterator<Variable> iter1=cl.proprietes.iterator(); iter1.hasNext();) {
				Variable prop = iter1.next();
				if (prop.nom.equals(nom_prop)) return cl;
			}
		}
		return null;
	}
	
	public final Operation getOperation(String nom_oper) {
		if (nom_oper.contains(".")) {
			int pos = nom_oper.indexOf(".");
			nom_oper = nom_oper.substring(pos+1);
		}
		for (Iterator<Classe> iter=this.classes.iterator(); iter.hasNext();) {
			Classe cl = iter.next();
			for (Iterator<Operation> iter1=cl.operations.iterator(); iter1.hasNext();) {
				Operation oper = iter1.next();
				if (oper.nom.equals(nom_oper)) {
					if (oper.classe == null) oper.classe = cl;	// ne devrait pas arriver ?
					return oper;
				}
			}
		}
		for (Iterator<Operation> iter1=this.operations.iterator(); iter1.hasNext();) {
			Operation oper = iter1.next();
			if (oper.nom.equals(nom_oper)) return oper;
		}
		return null;
	}

	public final Argument getOption(String nom_opt) {
		for (Iterator<Argument> iter=this.options.iterator(); iter.hasNext();) {
			Argument option = iter.next();
			if (option.nom.equalsIgnoreCase(nom_opt)) {
				return option;
			}
		}
		return null;
	}
	
	public final String getOptionMode(String nom_opt) {
		for (Iterator<Argument> iter=this.options.iterator(); iter.hasNext();) {
			Argument option = iter.next();
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
	
	public String getDim(int dim, Argument arg) {
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
	
	public final Programme getClone(String nom_lang) {
//		return BindingCastor.getProgramme(getXmlBuffer().toString(), nom_lang); 
		return null;
	}	
	
	public final static Programme getProgramme(String txt_xml, String nom_lang) {
//		return BindingCastor.getProgramme(txt_xml, nom_lang); 	
		AnalyseurXml analyseur = new AnalyseurXml(txt_xml, nom_lang);
		return analyseur.getProgramme();
	}
	
	void ecrireXml(StringBuffer buf, int indent) {
		Divers.ecrire(buf, "<programme", indent);
		Divers.ecrireAttrXml(buf, "nom", nom);
		Divers.ecrire(buf, ">");
		for (Iterator<Classe> iter=classes.iterator(); iter.hasNext();) {
			Classe cl = iter.next();
			cl.ecrireXml(buf, indent+1);
		}
		for (Iterator<Operation> iter=operations.iterator(); iter.hasNext();) {
			Operation oper = iter.next();
			oper.ecrireXml(buf, indent+1);
		}
		for (Iterator<Variable> iter=variables.iterator(); iter.hasNext();) {
			Variable var =  iter.next();
			var.ecrireXml(buf, indent+1);
		}
		for (Iterator<Instruction> iter=instructions.iterator(); iter.hasNext();) {
			Instruction instr = iter.next();
			instr.ecrireXml(buf, indent+1);
		}
		Divers.ecrire(buf, "</programme>", indent);
	}
	
}
