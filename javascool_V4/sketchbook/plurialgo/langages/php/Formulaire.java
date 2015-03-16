/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2013.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.langages.php;

import java.util.Iterator;
import org.javascool.proglets.plurialgo.divers.Divers;
import org.javascool.proglets.plurialgo.langages.modele.*;

/**
 * Cette classe permet de traduire en Php une instruction
 * de lecture dans un formulaire.
*/
public class Formulaire {
	
	Instruction instr_pere;
	Argument arg_form;
	
	public Formulaire(Instruction pere) {
		this.instr_pere = pere;
		arg_form = (Argument) pere.getFormulaire();
	}
	
	/**
	 * Construction du formulaire.
	 * @param prog
	 * @param buf
	 * @param indent
	 */
	public void constrFormu(Programme prog, StringBuffer buf, int indent) {
		Divers.ecrire(buf,"echo " + prog.quote("<table border='1'>") + "; ", indent);	
		for (Iterator<ModeleArgument> iter=instr_pere.arguments.iterator(); iter.hasNext();) {
			Argument arg = (Argument) iter.next();
			String msg = arg.nom + " : "; 
			Divers.ecrire(buf,"echo " + prog.quote("<tr>") + "; ", indent);
			constrFormu(prog, buf, indent, msg, arg);
			Divers.ecrire(buf,"echo " + prog.quote("</tr>") + "; ", indent);
		}
		Divers.ecrire(buf,"echo " + prog.quote("</table>") + "; ", indent);		
	}

	/**
	 * Lecture du formulaire.
	 * @param prog
	 * @param buf
	 * @param indent
	 */	
	public void lireFormu(Programme prog, StringBuffer buf, int indent) {
		for (Iterator<ModeleArgument> iter=instr_pere.arguments.iterator(); iter.hasNext();) {
			Argument arg = (Argument) iter.next();
			lireFormu(prog, buf, indent, arg);
		}
	}
	
	private String transformerMsg(String msg) {
		if (msg==null) return "";
		String txt = msg; txt = Divers.remplacer(txt, ".", "_");
		txt=Divers.remplacer(txt, "][", ").\"_\".(");
		txt=Divers.remplacer(txt, "[", "_\".(");
		txt=Divers.remplacer(txt, "]", ").\"");
		return txt;
	}

// -------------------------------
// construction du formulaire
// -------------------------------
	
	private void constrFormu(Programme prog, StringBuffer buf, int indent, String msg, Argument arg) {
		if ( arg.isSimple() ) {
			this.constrSimpleFormu(prog, buf, indent, msg, arg);
		}
		if ( arg.isTabSimple()) {
			this.constrTabFormu(prog, buf, indent, msg, arg, "i1");
		}
		if ( arg.isMatSimple() ) {
			this.constrMatFormu(prog, buf, indent, msg, arg);
		}
		if ( arg.isEnregistrement(prog) || arg.isClasse(prog) ) {
			constrClasseFormu(prog, buf, indent, msg, arg);
		}
		if (arg.isTabClasse(prog)) {
			constrTabClasseFormu(prog, buf, indent, msg, arg);
		}
	}
	
	private void constrLabelFormu(Programme prog, StringBuffer buf, int indent, String msg, Argument arg) {
		Divers.indenter(buf, indent);
		if (msg!=null) {
			Divers.ecrire(buf,"echo " + prog.quote("<td>" + msg + "</td>") + "; ");
		}
	}
	
	private void constrSimpleFormu(Programme prog, StringBuffer buf, int indent, String msg, Argument arg) {
		String zone = transformerMsg("zone_" + arg.nom);
		if (arg.isEntier() || arg.isReel()) {
			this.constrLabelFormu(prog, buf, indent, msg, arg);
			String txt = "<input type='text' size='8' " + "name='" + zone + "'></input>";
			Divers.ecrire(buf,"echo " + prog.quote("<td>" + txt + "</td>") +  "; ");
		}
		else if (arg.isTexte()) {
			this.constrTexteFormu(prog, buf, indent, msg, arg);
		}
		else if (arg.isBooleen()) {
			this.constrLabelFormu(prog, buf, indent, msg, arg);
			String txt = "<input type='checkbox' " + "name='" + zone + "'></input>";
			Divers.ecrire(buf,"echo " + prog.quote("<td>" + txt + "</td>") +  "; ");
		}
	}
	
	private void constrTexteFormu(Programme prog, StringBuffer buf, int indent, String msg, Argument arg) {
		String zone = transformerMsg("zone_" + arg.nom);
		if (arg.avecTexteRadio()) {
			this.constrLabelFormu(prog, buf, indent, msg, arg);
			String[] liste = arg.getTexteRadio();
			int n = liste.length;
			String txt;
			Divers.ecrire(buf,"echo " + prog.quote("<td>") +  "; ", indent);
			for(int i=0; i<n; i++) {
				txt = "<input type='radio' name='" + zone + "' value='" + liste[i] + "'>" + liste[i] + "</input>";
				Divers.ecrire(buf,"echo " + prog.quote(txt) +  "; ", indent);
				if (i<n-1) Divers.ecrire(buf,"echo " + prog.quote("<br>") +  "; ", indent);
			}
			Divers.ecrire(buf,"echo " + prog.quote("</td>") +  "; ", indent);
		}
		else if (arg.avecTexteListe()) {
			this.constrLabelFormu(prog, buf, indent, msg, arg);
			String[] liste = arg.getTexteListe();
			int n = liste.length;
			String txt;
			txt= "<select size='3' " + "name='" + zone + "'>";
			Divers.ecrire(buf,"echo " + prog.quote("<td>" + txt) +  "; ", indent);
			for(int i=0; i<n; i++) {
				txt = "<option value='" + liste[i] + "'>" + liste[i] + "</option>";
				Divers.ecrire(buf,"echo " + prog.quote(txt) +  "; ", indent);
			}
			Divers.ecrire(buf,"echo " + prog.quote("</select></td>") +  "; ", indent);
		}
		else {
			this.constrLabelFormu(prog, buf, indent, msg, arg);	
			String txt = "<input type='text' size='8' " + "name='" + zone + "'></input>";
			Divers.ecrire(buf,"echo " + prog.quote("<td>" + txt + "</td>") +  "; ");
		}
	}
	
	private void constrTabFormu(Programme prog, StringBuffer buf, int indent, String msg, Argument arg, String indice) {
		this.constrLabelFormu(prog, buf, indent, msg, arg);	
		// la boucle
		Divers.ecrire(buf,"echo " + prog.quote("<td><table>") + "; ", indent);
		Divers.ecrire(buf,"echo " + prog.quote("<tr>") + "; ", indent);
		Divers.ecrire(buf, "for($i1=0; $i1<" + prog.getMaxTab() + "; $i1++) {", indent);
		String msg1 = null;
		Argument arg1 = new Argument(arg.nom+"[$i1]", arg.getTypeOfTab(), arg.mode);
		constrFormu(prog, buf, indent+1, msg1, arg1);
		Divers.ecrire(buf, "}", indent);
		Divers.ecrire(buf,"echo " + prog.quote("</tr>") + "; ", indent);
		Divers.ecrire(buf,"echo " + prog.quote("</table></td>") + "; ", indent);
	}
	
	private void constrMatFormu(Programme prog, StringBuffer buf, int indent, String msg, Argument arg) {
		this.constrLabelFormu(prog, buf, indent, msg, arg);	
		// les boucles
		Divers.ecrire(buf,"echo " + prog.quote("<td><table>") + "; ", indent);
		Divers.ecrire(buf, "for($i1=0; $i1<" + prog.getMaxTab() + "; $i1++) {", indent);
		Divers.ecrire(buf,"echo " + prog.quote("<tr>") + "; ", indent+1);
		Divers.ecrire(buf, "for($j1=0; $j1<" + prog.getMaxTab() + "; $j1++) {", indent+1);
		String msg1 = null;
		Argument arg1 = new Argument(arg.nom+"[$i1][$j1]", arg.getTypeOfMat(), arg.mode);
		constrFormu(prog, buf, indent+2, msg1, arg1);
		Divers.ecrire(buf, "}", indent+1);
		Divers.ecrire(buf,"echo " + prog.quote("</tr>") + "; ", indent+1);
		Divers.ecrire(buf, "}", indent);
		Divers.ecrire(buf,"echo " + prog.quote("</table></td>") + "; ", indent);
	}
	
	private void constrClasseFormu(Programme prog, StringBuffer buf, int indent, String msg, Argument arg) {
		Classe cl = (Classe) arg.getClasse(prog);
		this.constrLabelFormu(prog, buf, indent, msg, arg);	
		// la boucle
		Divers.ecrire(buf,"echo " + prog.quote("<td><table>") + "; ", indent);
		for(Iterator<ModeleVariable> iter=cl.proprietes.iterator(); iter.hasNext(); ) {
			Variable prop = (Variable) iter.next();
			if (prop.isOut()) continue;
			String msg1 = (prop.nom + " : ");
			Argument arg1 = new Argument(arg.nom+"."+prop.nom, prop.type, arg.mode);
			Divers.ecrire(buf,"echo " + prog.quote("<tr>") + "; ", indent);
			constrFormu(prog, buf, indent, msg1, arg1);
			Divers.ecrire(buf,"echo " + prog.quote("</tr>") + "; ", indent);
		}
		Divers.ecrire(buf,"echo " + prog.quote("</table></td>") + "; ", indent);
	}
	
	private void constrTabClasseFormu(Programme prog, StringBuffer buf, int indent, String msg, Argument arg) {
		this.constrLabelFormu(prog, buf, indent, msg, arg);
		// la boucle
		Divers.ecrire(buf,"echo " + prog.quote("<td><table border='1'>") + "; ", indent);
		Divers.ecrire(buf, "for($ii=0; $ii<" + prog.getMaxTab() + "; $ii++) {", indent);
		String msg1 = null;
		Argument arg1 = new Argument(arg.nom+"[$ii]", arg.getTypeOfTab(), arg.mode);
		Divers.ecrire(buf,"echo " + prog.quote("<tr>") + "; ", indent+1);
		constrFormu(prog, buf, indent+1, msg1, arg1);
		Divers.ecrire(buf,"echo " + prog.quote("</tr>") + "; ", indent+1);
		Divers.ecrire(buf, "}", indent);
		Divers.ecrire(buf,"echo " + prog.quote("</table></td>") + "; ", indent);
	}
	
// -------------------------------
// lecture du formulaire
// -------------------------------
	
	private void lireFormu(Programme prog, StringBuffer buf, int indent, Argument arg) {
		if (arg.isSimple()) {
			this.lireSimpleFormu(prog, buf, indent, arg);
		}
		if ( arg.isTabSimple()) {
			lireTabFormu(prog, buf, indent, arg);
		}
		if ( arg.isMatSimple() ) {
			lireMatFormu(prog, buf, indent, arg, "i1", "j1");
		}
		if ( arg.isEnregistrement(prog) || arg.isClasse(prog) ) {
			lireClasseFormu(prog, buf, indent, arg);
		}
		if (arg.isTabClasse(prog)) {
			this.lireTabClasseFormu(prog, buf, indent, arg);
		}
	}
	
	private void lireSimpleFormu(Programme prog, StringBuffer buf, int indent, Argument arg) {
		String zone = transformerMsg("zone_" + arg.nom);
		if (arg.isBooleen()) {
			Divers.ecrire(buf, "$" + arg.nom + " = (int)array_key_exists(" + prog.quote(zone) + ",$_REQUEST); ", indent); 
		}
		else {
			Divers.ecrire(buf, "$" + arg.nom + " = $_REQUEST[" + prog.quote(zone) + "]; ", indent); 
		}
	}
	
	private void lireTabFormu(Programme prog, StringBuffer buf, int indent, Argument arg) {
		Divers.ecrire(buf, "for($i1=0; $i1<" + prog.getDim(1, arg) + "; $i1++) {", indent);
		Argument arg1 = new Argument(arg.nom+"[$i1]", arg.getTypeOfTab(), arg.mode);
		lireFormu(prog, buf, indent+1, arg1);
		Divers.ecrire(buf, "}", indent);
	}
	
	private void lireMatFormu(Programme prog, StringBuffer buf, int indent, Argument arg, String indice1, String indice2) {
		Divers.ecrire(buf, "for($i1=0; $i1<" + prog.getDim(1, arg) + "; $i1++) {", indent);
		Divers.ecrire(buf, "for($j1=0; $j1<" + prog.getDim(2, arg) + "; $j1++) {", indent+1);
		Argument arg1 = new Argument(arg.nom+"[$i1][$j1]", arg.getTypeOfMat(), arg.mode);
		lireFormu(prog, buf, indent+2, arg1);
		Divers.ecrire(buf, "}", indent+1);
		Divers.ecrire(buf, "}", indent);
	}
	
	private void lireClasseFormu(Programme prog, StringBuffer buf, int indent, Argument arg) {
		Classe cl = (Classe) arg.getClasse(prog);
		for(Iterator<ModeleVariable> iter=cl.proprietes.iterator(); iter.hasNext(); ) {
			Variable prop = (Variable) iter.next();
			if (prop.isOut()) continue;
			Argument arg1 = new Argument(arg.nom+"."+prop.nom, prop.type, arg.mode);
			lireFormu(prog, buf, indent, arg1);
		}	
	}
	
	private void lireTabClasseFormu(Programme prog, StringBuffer buf, int indent, Argument arg) {
		Divers.ecrire(buf, "for($ii=0; $ii<" + prog.getDim(1, arg) + "; $ii++) {", indent);
		Argument arg1 = new Argument(arg.nom+"[$ii]", arg.getTypeOfTab(), prog.getDim(2, arg));
		lireFormu(prog, buf, indent+1, arg1);
		Divers.ecrire(buf, "}", indent);
	}
	
}


