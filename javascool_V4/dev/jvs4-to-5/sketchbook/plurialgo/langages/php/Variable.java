/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2013.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.langages.php;

import java.util.Iterator;
import org.javascool.proglets.plurialgo.divers.*;


/**
 * Cette classe hérite de la classe homonyme du modèle.
*/
public class Variable extends org.javascool.proglets.plurialgo.langages.modele.Variable {
	
	public Variable() {
	}
	
	public Variable(String nom, String type) {
		this.nom = nom;
		this.type = type;
		this.mode = null;
	}
	
	public void ecrire(Programme prog, StringBuffer buf, int indent) {
		String MAX_TAB = prog.getMaxTab();
		if (this.isTabSimple()){
			Divers.ecrire(buf, "$" + nom, indent);
			Divers.ecrire(buf, " = array();");
		}
		if (this.isMatSimple()){
			Divers.ecrire(buf, "$" + nom, indent);
			Divers.ecrire(buf, " = array();");
		}
		if (this.isClasse(prog)) {
			Divers.ecrire(buf, "$" + nom, indent);
			Divers.ecrire(buf, " = new ");
			Divers.ecrire(buf, this.getClasse(prog).nom);
			Divers.ecrire(buf, "();");
		}
		if (this.isEnregistrement(prog)) {
			Divers.ecrire(buf, "$" + nom, indent);
			Divers.ecrire(buf, " = array(); ");
			Classe cl = (Classe) this.getClasse(prog);
			for (Iterator<org.javascool.proglets.plurialgo.langages.modele.Variable> iter=cl.proprietes.iterator(); iter.hasNext();) {
				Variable prop = (Variable) iter.next();
				Variable var = new Variable(this.nom+"."+prop.nom, prop.type);
				var.ecrire(prog, buf, indent);
			}
		}
		if (this.isTabClasse(prog)){
			Classe cl = (Classe) this.getClasseOfTab(prog);
			if (cl.isClasse()) {
				Divers.ecrire(buf, "$" + nom + " = array();", indent);
				Divers.ecrire(buf, "for($i1=0; $i1<" + MAX_TAB + "; $i1++) {", indent);
				Divers.ecrire(buf, this.nom + "[$i1]" + " = new " + cl.nom + "();", indent+1);
				Divers.ecrire(buf, "}", indent);
			}
			else {
				Divers.ecrire(buf, "$" + nom + " = array();", indent);
				Divers.ecrire(buf, "for($i1=0; $i1<" + MAX_TAB + "; $i1++) {", indent);
				Variable var = new Variable(this.nom + "[$i1]", cl.nom);
				var.ecrire(prog, buf, indent+1);
				Divers.ecrire(buf, "}", indent);
			}
		}
	}
	
	public void ecrirePropriete(Programme prog, StringBuffer buf, int indent) {
		if (this.isSimple()){
			Divers.ecrire(buf, "var " + "$" + nom, indent);
			Divers.ecrire(buf, "; ");
		}
		if (this.isTabSimple()){
			Divers.ecrire(buf, "var " + "$" + nom, indent);
			Divers.ecrire(buf, " = array();");
		}
		if (this.isMatSimple()){
			Divers.ecrire(buf, "var " + "$" + nom, indent);
			Divers.ecrire(buf, " = array();");
		}
		if (this.isClasse(prog)) {
			Divers.ecrire(buf, "var " + "$" + nom, indent);
			Divers.ecrire(buf, " = new ");
			Divers.ecrire(buf, this.getClasse(prog).nom);
			Divers.ecrire(buf, "();");
		}
	}
	
}
