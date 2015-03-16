/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2013.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.langages.javascript;

import java.util.*;
import org.javascool.proglets.plurialgo.divers.*;
import org.javascool.proglets.plurialgo.langages.modele.*;


/**
 * Cette classe hérite de la classe homonyme du modèle.
*/
public class Programme extends ModeleProgramme {
	
	Instruction instr_saisie;	// saisie elementaire ou formulaire

	/*
	 * Plutôt que de faire la saisie ELEMENTAIRE avec des boîtes de dialogue ( alert ), on
	 * regroupe les instructions de saisie du programme principal dans une unique
	 * instruction de saisie dans un FORMULAIRE
	 */
	private void preTraitement() {	
		instr_saisie = null	;
		for (Iterator<ModeleInstruction> iter=instructions.iterator(); iter.hasNext();) {
			Instruction instr = (Instruction) iter.next();
			if (instr.isLectureStandard() || instr.isLectureFormulaire()) {
				if (instr_saisie==null) {
					if (instr.isLectureStandard()) {
						Argument arg = new Argument("formu", "FORM", null);
						instr.setFormulaire(arg);
					}
					instr_saisie = instr;
				}
				else {
					for (Iterator<ModeleArgument> iter_arg=instr.arguments.iterator(); iter_arg.hasNext();) {
						Argument arg = (Argument) iter_arg.next();
						instr_saisie.arguments.add(arg);
					}
					instr.nom="//";
					//instructions.remove(instr);
				}
			}
		}
	}
	
	public void ecrire() {
		StringBuffer buf = new StringBuffer();
		this.preTraitement();
		String nom_fich = nom + ".html";
		les_fichiers.put(nom_fich, buf);
		ecrire(buf);
		this.postTraitement();
	}
	
	private void ecrire(StringBuffer buf) {
		this.addDebut(buf);
		this.addTypes(buf, 1);
		this.addClasses(buf, 1);
		this.addSousProgs(buf, 1);
		if (!this.avecFormulaire()) {
			this.addMain(buf, 1);
		}
		else {
			this.addMainFormulaire(buf, 1);
		}
	}
	
	private void addDebut(StringBuffer buf) {
		Divers.ecrire(buf, "<html>", 0);
		Divers.ecrire(buf, "<head>", 0);
		Divers.ecrire(buf, "<script language='Javascript'>", 0);
	}
	
	private void addSousProgs(StringBuffer buf, int indent) {
		//if ((operations.size()>0)) this.commenter(buf, "sous programmes", indent);
		for (Iterator<ModeleOperation> iter=operations.iterator(); iter.hasNext();) {
			Operation oper = (Operation) iter.next();
			oper.ecrire(this, buf, indent);
		}
	}
	
	private void addClasses(StringBuffer buf, int indent) {
		//if ((classes.size()>0))	this.commenter(buf, "enregistrements ou classes", indent);
		for (Iterator<ModeleClasse> iter=classes.iterator(); iter.hasNext();) {
			Classe classe = (Classe) iter.next();
			if (classe.isEnregistrement()) {
				classe.ecrireEnregistrement(this, buf, indent);
			}
			else {
				classe.ecrire(this, buf, indent);
			}
		}
	}
	
	private void addMain(StringBuffer buf, int indent) {
		this.commenter(buf, "programme principal", indent);
		Divers.ecrire(buf, "function main() {", indent);
		for (Iterator<ModeleVariable> iter=variables.iterator(); iter.hasNext();) {
			Variable var = (Variable) iter.next();
			var.ecrire(this, buf, indent+1);
		}
		for (Iterator<ModeleInstruction> iter=instructions.iterator(); iter.hasNext();) {
			Instruction instr = (Instruction) iter.next();
			instr.ecrire(this, buf, indent+1);
		}
		Divers.ecrire(buf, "} ", indent);
		Divers.ecrire(buf, "</script>", indent-1);
		Divers.ecrire(buf, "</head>", indent-1);
		Divers.ecrire(buf, "<body>", indent-1);
		Divers.indenter(buf, indent);
		//Divers.ecrire(buf, "<script language='javascript'> main(); </script>");
		Divers.ecrire(buf, "<a href='javascript:main()'>executer</a>");
		Divers.ecrire(buf, "</body>", indent-1);
		Divers.ecrire(buf, "</html>", indent-1);
	}
	
	// ------------------------------------------
	// utilitaires d'ecriture
	// ------------------------------------------ 
	
	public void commenter(StringBuffer fich, String texte, int indent) {
		// Divers.ecrire(fich, "<!-- " + texte + " -->", indent);
		Divers.ecrire(fich, "// " + texte, indent);
	}
	
	public String quote(String s) {
		return "\"" + s + "\"";
	}
	
	public void postTraitement(StringBuffer buf) {
		//Divers.remplacer(buf, "==", "==");
		Divers.remplacer(buf, " et ", " && ");
		Divers.remplacer(buf, " ET ", " && ");
		Divers.remplacer(buf, " ou ", " || ");
		Divers.remplacer(buf, " OU ", " || ");
		Divers.remplacer(buf, " and ", " && ");
		Divers.remplacer(buf, " AND ", " && ");
		Divers.remplacer(buf, " or ", " || ");
		Divers.remplacer(buf, " OR ", " || ");
		Divers.remplacer(buf, "vrai", "true");
		Divers.remplacer(buf, "VRAI", "true");
		Divers.remplacer(buf, "faux", "false");
		Divers.remplacer(buf, "FAUX", "false");
	}
	
	private void postTraitement() {
		Iterator<String> iter = this.les_fichiers.keySet().iterator();
		while (iter.hasNext()) {
			String nom_fich = iter.next();
			StringBuffer buf = this.les_fichiers.get(nom_fich);
			postTraitement(buf);
		}
	}
	
	// ------------------------------------------
	// types
	// ------------------------------------------ 
	
	private void addTypes(StringBuffer buf, int indent) {
		if (avecType("TAB_") || avecType("MAT_")) {
			Divers.ecrire(buf, "var MAX_TAB = 10; // taille maximale des tableaux", indent);
		}
	}
	
	// ------------------------------------------
	// formulaires
	// ------------------------------------------ 
	
	private void addMainFormulaire(StringBuffer buf, int indent) {
		this.commenter(buf, "programme principal", indent);
		Formulaire form = new Formulaire(instr_saisie);
		addAction(buf, form, indent);
		Divers.ecrire(buf, "</script>", indent-1);
		Divers.ecrire(buf, "</head>", indent-1);
		Divers.ecrire(buf, "<body>", indent-1);
		if (avecType("TAB_") || avecType("MAT_")) {
			Divers.ecrire(buf, "<script language='Javascript'>", indent-1);
			addConstruction(buf, form, indent);
			Divers.ecrire(buf, "</script>", indent-1);
		}
		else {
			StringBuffer buf1 = new StringBuffer();
			addConstruction(buf1, form, indent);
			Divers.remplacer(buf1, "document.writeln(\"", "");
			Divers.remplacer(buf1, "\");", "");
			Divers.ecrire(buf, buf1.toString());
		}
		Divers.ecrire(buf, "</body>", indent-1);
		Divers.ecrire(buf, "</html>", indent-1);
	}
	
	private void addConstruction(StringBuffer buf, Formulaire form, int indent) {
		Instruction instr_saisie = form.instr_pere;
		Argument arg_formu = (Argument) instr_saisie.getFormulaire();
		Divers.ecrire(buf, "document.writeln(" + this.quote("<center><form name ='" + arg_formu.nom + "'>") + "); ", indent);
		form.constrFormu(this, buf, indent);
		Divers.indenter(buf, indent);
		String txt = "<p><input type='button' value='calculer' onClick='main(" + arg_formu.nom + ")'></input></p>";
		Divers.ecrire(buf, "document.writeln(" + this.quote(txt) + ");");
		Divers.ecrire(buf,"document.writeln(" + this.quote("</form></center>") + "); ", indent);
	}
	
	private void addAction(StringBuffer buf, Formulaire form, int indent) {
		Instruction instr_saisie = form.instr_pere;
		Divers.ecrire(buf, "function main (formu) {", indent);
		for (Iterator<ModeleVariable> iter=variables.iterator(); iter.hasNext();) {
			Variable var = (Variable) iter.next();
			var.ecrire(this, buf, indent+1);
		}
		form.lireFormu(this, buf, indent+1);
		for (Iterator<ModeleInstruction> iter=instructions.iterator(); iter.hasNext();) {
			Instruction instr = (Instruction) iter.next();
			if (instr == instr_saisie) continue;
				instr.ecrire(this, buf, indent+1);
		}
		Divers.ecrire(buf, "document.close(); ", indent+1);
		Divers.ecrire(buf, "} ", indent);
	}

}
