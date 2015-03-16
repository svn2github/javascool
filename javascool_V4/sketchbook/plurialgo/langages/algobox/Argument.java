/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2014.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.langages.algobox;


import org.javascool.proglets.plurialgo.divers.*;
import org.javascool.proglets.plurialgo.langages.modele.*;

/**
 * Cette classe hérite de la classe homonyme du modèle.
*/
public class Argument extends ModeleArgument {
	
	public Argument() {
	}
	
	public Argument(String nom, String type, String mode) {
		this.nom = nom;
		this.type = type;
		this.mode = mode;
	}
	
	public void ecrire(Programme prog, StringBuffer buf, int indent, Instruction pere) {
		if (pere==null) return;
		if (pere.isLectureStandard()) {
			String msg = prog.quote(nom+" : ");
			this.lireStandard(prog, buf, indent, msg);
		}
		else if (pere.isEcritureStandard()) {
			String msg = prog.quote(nom+" : ");
			this.ecrireStandard(prog, buf, indent, msg);
		}
	}
	
// -------------------------------
// lecture d'arguments (standard)
// -------------------------------
	
	private void lireStandard(Programme prog, StringBuffer buf, int indent, String msg) { 
		if ( isSimple() ) {
			lireSimpleStandard(prog,buf,indent,msg);
		}
		if ( isTabSimple()) {
			lireTabStandard(prog,buf,indent,msg);
		}
	}
	
	private void lireSimpleStandard(Programme prog, StringBuffer buf, int indent, String msg) {
		//Divers.ecrire(buf, "AFFICHER " + transformerMsg(msg), indent);
		Divers.ecrire(buf, "LIRE " + this.nom, indent);
	}
	
	private void lireTabStandard(Programme prog, StringBuffer buf, int indent, String msg) {
		this.addVariable(new Variable("i1","ENTIER"));
		if (msg!=null) Divers.ecrire(buf, "AFFICHER " + msg , indent); 
		Divers.ecrire(buf, "POUR i1 ALLANT_DE 0 A " + prog.getDim(1, this) + "-1", indent);
		Divers.ecrire(buf, "DEBUT_POUR", indent+1);
		Argument arg = new Argument(this.nom+"[i1]", this.getTypeOfTab(), null);
		String msg1 = prog.quote(arg.nom + " : ");
		arg.lireStandard(prog, buf, indent+1, msg1);
		Divers.ecrire(buf, "FIN_POUR", indent);
	}
	
//	 --------------------------------
//	 écriture d'arguments	(standard)
//	 --------------------------------
	
	private void ecrireStandard(Programme prog, StringBuffer buf, int indent, String msg) {
		if ( isSimple() ) {
			ecrireSimpleStandard(prog,buf,indent,msg);
		}
		if ( isTabSimple()) {
			ecrireTabStandard(prog,buf,indent,msg);
		}
		if ( isExpression() ) {
			ecrireExpression(prog,buf,indent);
		}
	}
	
	private void ecrireExpression(Programme prog, StringBuffer buf, int indent) {
		Divers.indenter(buf, indent);
		Divers.ecrire(buf, "AFFICHER " + this.nom);
	}
	
	private void ecrireSimpleStandard(Programme prog, StringBuffer buf, int indent, String msg) {
		if (msg!=null) {
			Divers.ecrire(buf, "AFFICHER " + msg, indent);
		}
		Divers.ecrire(buf, "AFFICHER " + this.nom, indent);
	}
	
	private void ecrireTabStandard(Programme prog, StringBuffer buf, int indent, String msg) {
		if (msg!=null) Divers.ecrire(buf, "AFFICHER " + msg , indent); 
		Divers.ecrire(buf, "POUR i1 ALLANT_DE 0 A " + prog.getDim(1, this) + "-1", indent);
		Divers.ecrire(buf, "DEBUT_POUR", indent+1);
		Divers.ecrire(buf, "AFFICHER " + this.nom +"[i1]", indent+1);
		Divers.ecrire(buf, "FIN_POUR", indent+1);
	}
	
}
