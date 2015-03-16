/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2013.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.langages.php;


import java.util.*;
import org.javascool.proglets.plurialgo.divers.*;

/**
 * Cette classe hérite de la classe homonyme du modèle.
*/
public class Argument extends org.javascool.proglets.plurialgo.langages.modele.Argument {
	
	public Argument() {
	}
	
	public Argument(String nom, String type, String mode) {
		this.nom = nom;
		this.type = type;
		this.mode = mode;
	}
	
	public void ecrire(Programme prog, StringBuffer buf, int indent) {
		if (Divers.isChiffre(nom.substring(0, 1))) {
			Divers.ecrire(buf, "" + nom, indent);
		}
		else {
			Divers.ecrire(buf, "$" + nom, indent);			
		}
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
		Divers.ecrire(buf, "// tache non realisee : lecture standard de " + this.nom , indent);
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
		if ( isTabClasse(prog)) {
			ecrireTabClasseStandard(prog, buf, indent, msg);
		}
		if ( isMatSimple() ) {
			ecrireMatStandard(prog,buf,indent,msg);
		}
		if ( isEnregistrement(prog) || isClasse(prog) ) {
			ecrireClasseStandard(prog, buf, indent, msg);
		}
	}
	
	private void ecrireSimpleStandard(Programme prog, StringBuffer buf, int indent, String msg) {
		if (isBooleen()) {
			Divers.indenter(buf, indent);
			if (msg!=null) Divers.ecrire(buf, "echo " + msg + "; ");
			Divers.ecrire(buf, "echo " + "$" + this.nom + "; ");
			if (msg!=null && !(prog.quote("\\t").equals(msg))) Divers.ecrire(buf, "echo " + prog.quote("<br/>") + "; ");			
		}
		else {
			Divers.indenter(buf, indent);
			if (msg!=null) Divers.ecrire(buf, "echo " + msg + "; ");
			Divers.ecrire(buf, "echo " + "$" + this.nom + "; ");
			if (msg!=null && !(prog.quote("\\t").equals(msg))) Divers.ecrire(buf, "echo " + prog.quote("<br/>") + "; "); 
		}
	}
	
	private void ecrireTabStandard(Programme prog, StringBuffer buf, int indent, String msg) {
		if (msg!=null) Divers.ecrire(buf, "echo " + msg + ", " + prog.quote("<br/>") + "; ", indent);
		Divers.ecrire(buf,"echo " + prog.quote("<table border='1'>") + "; ", indent);
		Divers.ecrire(buf, "echo " + prog.quote("<tr>") + "; ", indent); 
		// this.addVariable(new Variable("i1","ENTIER"));
		Divers.ecrire(buf, "for($i1=0; $i1<" + prog.getDim(1, this) + "; $i1++) {", indent);
		Argument arg = new Argument(this.nom+"[$i1]", this.getTypeOfTab(), null);
		Divers.ecrire(buf, "echo " + prog.quote("<td>") + "," + arg.nom + "," + prog.quote("</td>") + ";", indent+1);
		Divers.ecrire(buf, "}", indent);
		Divers.ecrire(buf, "echo " + prog.quote("</tr>") + "; ", indent); 
		Divers.ecrire(buf, "echo " + prog.quote("</table>") + "; ", indent); 
	}
	
	private void ecrireMatStandard(Programme prog, StringBuffer buf, int indent, String msg) {
		if (msg!=null) Divers.ecrire(buf, "echo " + msg + ", " + prog.quote("<br/>") + "; ", indent);
		// this.addVariable(new Variable("i1","ENTIER"));
		Divers.ecrire(buf,"echo " + prog.quote("<table border='1'>") + "; ", indent);
		Divers.ecrire(buf, "for($i1=0; $i1<" + prog.getDim(1, this) + "; $i1++) {", indent);
		Divers.ecrire(buf, "echo " + prog.quote("<tr>") + "; ", indent+1); 
		Divers.ecrire(buf, "for($j1=0; $j1<" + prog.getDim(2, this) + "; $j1++) {", indent+1);
		Argument arg = new Argument(this.nom+"[$i1][$j1]", this.getTypeOfMat(), null);
		Divers.ecrire(buf, "echo " + prog.quote("<td>") + "," + arg.nom + "," + prog.quote("</td>") + ";", indent+2);
		Divers.ecrire(buf, "}", indent+1);
		Divers.ecrire(buf, "echo " + prog.quote("<tr>") + "; ", indent+1); 
		Divers.ecrire(buf, "}", indent);
		Divers.ecrire(buf, "echo " + prog.quote("</table>") + "; ", indent); 
	}
	
	private void ecrireClasseStandard(Programme prog, StringBuffer buf, int indent, String msg) {
		if (msg!=null) Divers.ecrire(buf, "echo " + msg + ", " + prog.quote("<br/>") + "; ", indent);
		Classe cl = (Classe) getClasse(prog);
		for(Iterator<org.javascool.proglets.plurialgo.langages.modele.Variable> iter=cl.proprietes.iterator(); iter.hasNext(); ) {
			Variable prop = (Variable) iter.next();
			if (prop.isIn()) continue;
			Argument arg = new Argument(nom+"."+prop.nom, prop.type, this.mode);
			String msg1 = prog.quote(arg.nom + " : ");
			arg.ecrireStandard(prog, buf, indent, msg1);
		}
	}
	
	private void ecrireTabClasseStandard(Programme prog, StringBuffer buf, int indent, String msg) {
		if (msg!=null) Divers.ecrire(buf, "echo " + msg + ", " + prog.quote("<br/>") + "; ", indent);
		Classe cl = (Classe) getClasseOfTab(prog);
		// this.addVariable(new Variable("ii","ENTIER"));
		Divers.ecrire(buf, "for($ii=0; $ii<" + prog.getDim(1, this) + "; $ii++) {", indent);
		String msg1 = prog.quote("rang ") + " , $ii , " + prog.quote(" de " + this.nom + " : ");
		if (msg1!=null) Divers.ecrire(buf, "echo " + msg1 + ", " + prog.quote("<br/>") + "; ", indent+1);
		for(Iterator<org.javascool.proglets.plurialgo.langages.modele.Variable> iter=cl.proprietes.iterator(); iter.hasNext(); ) {
			Variable prop = (Variable) iter.next();
			if (prop.isIn()) continue;
			Argument arg = new Argument(this.nom+"[$ii]"+"."+prop.nom, prop.type, oteDim(1));
			msg1=prog.quote(prop.nom + " : ");
			arg.ecrireStandard(prog, buf, indent+1, msg1);
		}
		Divers.ecrire(buf, "}", indent);
		if (msg!=null) Divers.ecrire(buf, "echo " + prog.quote("<br/>") + "; ", indent);
	}
	
}
