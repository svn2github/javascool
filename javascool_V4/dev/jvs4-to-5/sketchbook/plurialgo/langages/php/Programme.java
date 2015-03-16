/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2013.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.langages.php;

import java.util.*;

import org.javascool.proglets.plurialgo.divers.*;

/**
 * Cette classe hérite de la classe homonyme du modèle.
*/
public class Programme extends org.javascool.proglets.plurialgo.langages.modele.Programme {
	Instruction instr_saisie;

	/*
	 * On regroupe les instructions de saisie du programme principal dans une unique
	 * instruction de saisie dans un FORMULAIRE
	 */	
	private void preTraitement() {
		instr_saisie = null;
		for (Iterator<org.javascool.proglets.plurialgo.langages.modele.Instruction> iter=instructions.iterator(); iter.hasNext();) {
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
					for (Iterator<org.javascool.proglets.plurialgo.langages.modele.Argument> iter_arg=instr.arguments.iterator(); iter_arg.hasNext();) {
						Argument arg = (Argument) iter_arg.next();
						instr_saisie.arguments.add(arg);
					}
					instr.nom="//";
					//instructions.remove(instr);
				}
			}
		}
	}

	/**
	 * Redéfinition obligatoire de cette méthode.
	 */
	public void ecrire() {
		this.preTraitement();
		StringBuffer buf = new StringBuffer();
		String nom_fich = nom + ".php";
		les_fichiers.put(nom_fich, buf);
		ecrire(buf);
		this.postTraitement();
	}
	
	private void ecrire(StringBuffer buf) {
		Divers.ecrire(buf, "<html>", 0);
		Divers.ecrire(buf, "<head>", 0);
		Divers.ecrire(buf, "<?php", 0);
		this.addTypes(buf, 1);
		this.addClasses(buf, 1);
		this.addSousProgs(buf, 1);
		Divers.ecrire(buf, "?>", 0);
		Divers.ecrire(buf, "</head>", 0);
		if (!this.avecFormulaire()) {
			this.addMain(buf, 1);
		}
		else {
			this.addMainFormulaire(buf, 1);
		}
		Divers.ecrire(buf, "</html>", 0);
	}
	
	private void addSousProgs(StringBuffer buf, int indent) {
		if ((operations.size()>0))
			this.commenter(buf, "sous programmes", indent);
		for (Iterator<org.javascool.proglets.plurialgo.langages.modele.Operation> iter=operations.iterator(); iter.hasNext();) {
			Operation oper = (Operation) iter.next();
			oper.ecrire(this, buf, indent);
		}
	}
	
	private void addClasses(StringBuffer buf, int indent) {
		if ((classes.size()>0))
			this.commenter(buf, "enregistrements ou classes", indent);
		for (Iterator<org.javascool.proglets.plurialgo.langages.modele.Classe> iter=classes.iterator(); iter.hasNext();) {
			Classe classe = (Classe) iter.next();
			if (classe.isEnregistrement()) {
				//classe.ecrireEnregistrement(this, buf, indent);
			}
			else {
				classe.ecrire(this, buf, indent);
			}
		}
	}
	
	private void addMain(StringBuffer buf, int indent) {
		Divers.ecrire(buf, "<body>", indent-1);
		Divers.ecrire(buf, "<?php", indent-1);
		for (Iterator<org.javascool.proglets.plurialgo.langages.modele.Variable> iter=variables.iterator(); iter.hasNext();) {
			Variable var = (Variable) iter.next();
			var.ecrire(this, buf, indent);
		}
		for (Iterator<org.javascool.proglets.plurialgo.langages.modele.Instruction> iter=instructions.iterator(); iter.hasNext();) {
			Instruction instr = (Instruction) iter.next();
			instr.ecrire(this, buf, indent);
		}
		Divers.ecrire(buf, "?>", indent-1);
		Divers.ecrire(buf, "</body>", indent-1);
	}
	
	// ------------------------------------------
	// utilitaires d'ecriture
	// ------------------------------------------ 
	
	public void commenter(StringBuffer fich, String texte, int indent) {
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
		//Divers.remplacer(buf, "!=", "!=");
		Divers.remplacer(buf, "vrai", "true");
		Divers.remplacer(buf, "VRAI", "true");
		Divers.remplacer(buf, "faux", "false");
		Divers.remplacer(buf, "FAUX", "false");
		//Divers.remplacer(buf, "][", "][");
		//Divers.remplacer(buf, "[", "[");
		//Divers.remplacer(buf, "[", "[");
		//Divers.remplacer(buf, "'", "\"");
		Divers.remplacer(buf, "this", "$this");
		Divers.remplacer(buf, "$$", "$");
		Divers.remplacer(buf, "[ii]", "[$ii]");
		Divers.remplacer(buf, "[i1]", "[$i1]");
	}
	
	private void remplacerPoint(StringBuffer buf) {
		for (Iterator<org.javascool.proglets.plurialgo.langages.modele.Classe> iter1=classes.iterator(); iter1.hasNext();) {
			Classe cl = (Classe) iter1.next();
			for (Iterator<org.javascool.proglets.plurialgo.langages.modele.Variable> iter=cl.proprietes.iterator(); iter.hasNext();) {
				Variable prop = (Variable) iter.next();
				if (cl.isClasse()) {
					this.remplacerPoint(buf, "." + prop.nom, "->" + prop.nom);
				}
				else {
					this.remplacerPoint(buf, "." + prop.nom, "['" + prop.nom + "']");
				}
			}
		}
	}
	
	private void remplacerPoint(StringBuffer buf, String ancien, String nouveau) {
		int lg_ancien = ancien.length();
		String ch;
		for (int i=buf.length()-lg_ancien - 2; i>=2; i--) {
			if (ancien.equals(buf.substring(i, i+lg_ancien))) {
				ch = buf.substring(i+lg_ancien, i+lg_ancien+1);
				if  ( (ch.compareTo("A")>=0) && (ch.compareTo("Z")<=0) ) continue;
				if  ( (ch.compareTo("a")>=0) && (ch.compareTo("z")<=0) ) continue;
				if  ( (ch.compareTo("0")>=0) && (ch.compareTo("9")<=0) ) continue;
				if  ( ch.equals("_") ) continue;
				if  ( ch.equals("\"") ) continue;
				if  ( ch.equals("'") ) continue;
				if  ( ch.equals(":") ) continue;
				ch = buf.substring(i+lg_ancien, i+lg_ancien+2);
				if  ( ch.equals(" :") ) continue;
				buf.delete(i, i+lg_ancien);
				buf.insert(i, nouveau);
			}
		}	
	}

	private void ajouterDollar(StringBuffer buf) {
		for (Iterator<org.javascool.proglets.plurialgo.langages.modele.Variable> iter=variables.iterator(); iter.hasNext();) {
			Variable var = (Variable) iter.next();
			ajouterDollar(buf, var.nom);
		}
		for (Iterator<org.javascool.proglets.plurialgo.langages.modele.Operation> iter1=operations.iterator(); iter1.hasNext();) {
			Operation oper = (Operation) iter1.next();
			for (Iterator<org.javascool.proglets.plurialgo.langages.modele.Variable> iter=oper.variables.iterator(); iter.hasNext();) {
				Variable var = (Variable) iter.next();
				ajouterDollar(buf, var.nom);
			}
			for (Iterator<org.javascool.proglets.plurialgo.langages.modele.Variable> iter=oper.retours.iterator(); iter.hasNext();) {
				Variable retour = (Variable) iter.next();
				ajouterDollar(buf, retour.nom);
			}
			for (Iterator<org.javascool.proglets.plurialgo.langages.modele.Parametre> iter=oper.parametres.iterator(); iter.hasNext();) {
				Parametre param = (Parametre) iter.next();
				ajouterDollar(buf, param.nom);
			}
		}
		for (Iterator<org.javascool.proglets.plurialgo.langages.modele.Classe> iter2=classes.iterator(); iter2.hasNext();) {
			Classe cl = (Classe) iter2.next();
			for (Iterator<org.javascool.proglets.plurialgo.langages.modele.Operation> iter1=cl.operations.iterator(); iter1.hasNext();) {
				Operation oper = (Operation) iter1.next();
				for (Iterator<org.javascool.proglets.plurialgo.langages.modele.Variable> iter=oper.variables.iterator(); iter.hasNext();) {
					Variable var = (Variable) iter.next();
					ajouterDollar(buf, var.nom);
				}
				for (Iterator<org.javascool.proglets.plurialgo.langages.modele.Variable> iter=oper.retours.iterator(); iter.hasNext();) {
					Variable retour = (Variable) iter.next();
					ajouterDollar(buf, retour.nom);
				}
				for (Iterator<org.javascool.proglets.plurialgo.langages.modele.Parametre> iter=oper.parametres.iterator(); iter.hasNext();) {
					Parametre param = (Parametre) iter.next();
					ajouterDollar(buf, param.nom);
				}
			}
			for (Iterator<org.javascool.proglets.plurialgo.langages.modele.Constructeur> iter1=cl.constructeurs.iterator(); iter1.hasNext();) {
				Constructeur constr = (Constructeur) iter1.next();
				for (Iterator<org.javascool.proglets.plurialgo.langages.modele.Variable> iter=constr.variables.iterator(); iter.hasNext();) {
					Variable var = (Variable) iter.next();
					ajouterDollar(buf, var.nom);
				}
				for (Iterator<org.javascool.proglets.plurialgo.langages.modele.Parametre> iter=constr.parametres.iterator(); iter.hasNext();) {
					Parametre param = (Parametre) iter.next();
					ajouterDollar(buf, param.nom);
				}
			}
		}
	}
	
	private void ajouterDollar(StringBuffer buf, String ancien) {
		int lg_ancien = ancien.length();
		String ch;
		for (int i=buf.length()-lg_ancien - 4; i>=4; i--) {
			if (ancien.equals(buf.substring(i, i+lg_ancien))) {
				ch = buf.substring(i-1, i);
				if  ( (ch.compareTo("A")>=0) && (ch.compareTo("Z")<=0) ) continue;
				if  ( (ch.compareTo("a")>=0) && (ch.compareTo("z")<=0) ) continue;
				if  ( (ch.compareTo("0")>=0) && (ch.compareTo("9")<=0) ) continue;
				if  ( ch.equals("_") ) continue;
				if  ( ch.equals("\"") ) continue;
				if  ( ch.equals("'") ) continue;
				if  ( ch.equals(":") ) continue;
				if  ( ch.equals("$") ) continue;
				ch = buf.substring(i-4, i);
				if  ( ch.equals("<td>") ) continue;
				ch = buf.substring(i+lg_ancien, i+lg_ancien+1);
				if  ( (ch.compareTo("A")>=0) && (ch.compareTo("Z")<=0) ) continue;
				if  ( (ch.compareTo("a")>=0) && (ch.compareTo("z")<=0) ) continue;
				if  ( (ch.compareTo("0")>=0) && (ch.compareTo("9")<=0) ) continue;
				if  ( ch.equals("_") ) continue;
				if  ( ch.equals("\"") ) continue;
				if  ( ch.equals("'") ) continue;
				if  ( ch.equals(":") ) continue;
				ch = buf.substring(i+lg_ancien, i+lg_ancien+2);
				if  ( ch.equals(" :") ) continue;
				buf.insert(i, "$");
			}
		}		
	}
	
	private void postTraitement() {
		Iterator<String> iter = this.les_fichiers.keySet().iterator();
		while (iter.hasNext()) {
			String nom_fich = iter.next();
			StringBuffer buf = this.les_fichiers.get(nom_fich);
			ajouterDollar(buf);
			remplacerPoint(buf);
			postTraitement(buf);
		}
	}
	
	// ------------------------------------------
	// types
	// ------------------------------------------
	
	private void addTypes(StringBuffer buf, int indent) {
		this.commenter(buf, "types standard utilises", indent);
		if (avecType("TAB_") || avecType("MAT_")) {
			Divers.ecrire(buf, "define(" + quote("MAX_TAB") + ", 5);", indent);
		}
	}
	
	// ------------------------------------------
	// formulaires
	// ------------------------------------------ 
	
	private void addMainFormulaire(StringBuffer buf, int indent) {
		Formulaire form = new Formulaire(instr_saisie);
		// construction
		StringBuffer buf1 = new StringBuffer();
		Argument arg_formu = (Argument) instr_saisie.getFormulaire();
		this.les_fichiers.put(arg_formu.type+".php", buf1);
		Divers.ecrire(buf1, "<html>", 0);
		Divers.ecrire(buf1, "<body>", 0);
		if (avecType("TAB_") || avecType("MAT_")) {
			Divers.ecrire(buf1, "<?php", 0);
			Divers.ecrire(buf1, "define(" + quote("MAX_TAB") + ", 5);", 1);
			addConstruction(buf1, form, 1);
			Divers.ecrire(buf1, "?>", 0);
		}
		else {
			StringBuffer buf2 = new StringBuffer();
			addConstruction(buf2, form, 1);
			Divers.remplacer(buf2, "echo \"", "");
			Divers.remplacer(buf2, "\";", "");
			Divers.ecrire(buf1, buf2.toString());
		}
		Divers.ecrire(buf1, "</body>", 0);
		Divers.ecrire(buf1, "</html>", 0);
		// action
		Divers.ecrire(buf, "<body>", indent-1);
		addAction(buf, form, indent-1);
		Divers.ecrire(buf, "</body>", indent-1);
	}
	
	private void addConstruction(StringBuffer buf, Formulaire form, int indent) {
		Divers.ecrire(buf, "echo " + this.quote("<center><form action = '" + this.nom + ".php'>") + "; ", indent);
		form.constrFormu(this, buf, indent);
		Divers.indenter(buf, indent);
		Divers.ecrire(buf, "echo " + this.quote("<p><input type='submit' value='calculer'></p>") + "; ");
		Divers.ecrire(buf,"echo " + this.quote("</form></center>") + "; ", indent);
	}
	
	private void addAction(StringBuffer buf, Formulaire form, int indent) {
		Instruction instr_saisie = form.instr_pere;
		Divers.ecrire(buf, "<?php ", indent);
		for (Iterator<org.javascool.proglets.plurialgo.langages.modele.Variable> iter=variables.iterator(); iter.hasNext();) {
			Variable var = (Variable) iter.next();
			var.ecrire(this, buf, indent+1);
		}
		form.lireFormu(this, buf, indent+1);
		for (Iterator<org.javascool.proglets.plurialgo.langages.modele.Instruction> iter=instructions.iterator(); iter.hasNext();) {
			Instruction instr = (Instruction) iter.next();
			if (instr == instr_saisie) continue;
				instr.ecrire(this, buf, indent+1);
		}
		Divers.ecrire(buf, "?> ", indent);
	}
	
}
