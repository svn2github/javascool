/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2014.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.langages.R;

import java.util.Iterator;
import org.javascool.proglets.plurialgo.divers.Divers;
import org.javascool.proglets.plurialgo.langages.modele.*;

/**
 * Cette classe permet de traduire en R une instruction
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
	 * Initialisation des composants du formulaire.
	 * @param prog
	 * @param buf
	 * @param indent
	 */	
	public void constrFormu(Programme prog, StringBuffer buf, int indent) {
		Divers.ecrire(buf, "n_lig=0", indent);
		for (Iterator<ModeleArgument> iter=instr_pere.arguments.iterator(); iter.hasNext();) {
			Argument arg = (Argument) iter.next();
			String msg = prog.quote(arg.nom + " : ");
			Divers.ecrire(buf, "n_col=0", indent);
			constrFormu(prog, buf, indent+1, msg, arg);
			Divers.ecrire(buf, "n_lig=n_lig+1", indent);
		}		
	}
	
	/**
	 * Lecture des composants du formulaire.
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

// -------------------------------
// construction d'arguments (Formulaire)
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
		if (msg!=null) {
			Divers.ecrire(buf, "tkgrid( tklabel(formu, text=" + msg + "), row=n_lig, column=n_col)", indent);
			Divers.ecrire(buf, "n_col=n_col+1", indent);
		}
	}
	
	private void constrSimpleFormu(Programme prog, StringBuffer buf, int indent, String msg, Argument arg) {
		String zone = "zone_" + arg.nom; zone = Divers.remplacer(zone, ".", "_");
		zone = Divers.remplacer(zone, "][", "*" + prog.getMaxTab() + "+");
		zone = Divers.remplacer(zone, "[", "{["); zone = Divers.remplacer(zone, "]", "]}");
		if (arg.isEntier() || arg.isReel()) {
			this.constrLabelFormu(prog, buf, indent, msg, arg);	
			Divers.ecrire(buf, zone + " = tkentry(formu, width=8)", indent);
			Divers.ecrire(buf, "tkgrid(" + zone + ", row=n_lig, column=n_col)", indent);
		}
		else if (arg.isTexte()) {
			this.constrTexteFormu(prog, buf, indent, msg, arg);
		}
		else if (arg.isBooleen()) {
			this.constrLabelFormu(prog, buf, indent, msg, arg);
			String zone_value = "value_" + zone;
			Divers.ecrire(buf, zone_value + " = tclVar(0)", indent);
			Divers.ecrire(buf, zone + " = tkcheckbutton(formu)", indent);
			Divers.ecrire(buf, "tkconfigure(" + zone +", variable=" + zone_value + ")", indent);
			Divers.ecrire(buf, "tkgrid(" + zone + ", row=n_lig, column=n_col)", indent);
		}
	}
	
	private void constrTexteFormu(Programme prog, StringBuffer buf, int indent, String msg, Argument arg) {
		String zone = "zone_" + arg.nom; zone = Divers.remplacer(zone, ".", "_");
		zone = Divers.remplacer(zone, "][", "*" + prog.getMaxTab() + "+");
		zone = Divers.remplacer(zone, "[", "{["); zone = Divers.remplacer(zone, "]", "]}");
		String zone_value = "value_" + zone;
		if (arg.avecTexteRadio()) {
			this.constrLabelFormu(prog, buf, indent, msg, arg);
			Divers.ecrire(buf, zone_value + " = tclVar(0)", indent);
			String[] liste = arg.getTexteRadio();
			int n = liste.length;
			for(int i=0; i<n; i++) {
				String zone_option = liste[i] + "_" + zone;
				Divers.ecrire(buf, zone_option + " = tkradiobutton(formu)", indent);
				Divers.ecrire(buf, "tkconfigure(" + zone_option +", variable=" + zone_value + ", value=" 
						+ prog.quote(liste[i])+ ")", indent);
				Divers.ecrire(buf, "tkgrid( tklabel(formu, text=" + prog.quote(liste[i]) + "), row=n_lig, column=n_col)", indent);
				Divers.ecrire(buf, "n_col=n_col+1", indent);
				Divers.ecrire(buf, "tkgrid(" + zone_option + ", row=n_lig, column=n_col)", indent);
				if (i<n-1) Divers.ecrire(buf, "n_col=n_col+1", indent);
			}
		}
		else if (arg.avecTexteListe()) {
			this.constrLabelFormu(prog, buf, indent, msg, arg);
			Divers.ecrire(buf, zone + " = tklistbox(formu, height=2, width=8, exportselection=0)", indent);
			String[] liste = arg.getTexteListe();
			int n = liste.length;
			for(int i=0; i<n; i++) {
				Divers.ecrire(buf, "tkinsert(" + zone + "," + prog.quote("end") + "," 
						+ prog.quote(liste[i])+ ")", indent);
			}
			Divers.ecrire(buf, "tkgrid(" + zone + ", row=n_lig, column=n_col)", indent);
		}
		else {
			this.constrLabelFormu(prog, buf, indent, msg, arg);
			Divers.ecrire(buf, zone + " = tkentry(formu, width=8)", indent);
			Divers.ecrire(buf, "tkgrid(" + zone + ", row=n_lig, column=n_col)", indent);	
		}
	}
	
	private void constrTabFormu(Programme prog, StringBuffer buf, int indent, String msg, Argument arg, String indice) {
		String zone = "zone_" + arg.nom; zone = Divers.remplacer(zone, ".", "_");
		zone = Divers.remplacer(zone, "][", "*" + prog.getMaxTab() + "+");
		zone = Divers.remplacer(zone, "[", "{["); zone = Divers.remplacer(zone, "]", "]}");
		this.constrLabelFormu(prog, buf, indent, msg, arg);	
		Divers.ecrire(buf, zone + "=list()", indent);
		if (arg.isTabTextes()) {
			Argument arg1 = new Argument(arg.nom+"[i1]", arg.getTypeOfTab(), arg.mode);
			if (arg1.avecTexteRadio()) {
				String zone_value = "value_" + zone;
				Divers.ecrire(buf, zone_value + "=list()", indent);
				String[] liste = arg.getTexteRadio();
				int n = liste.length;
				for(int i=0; i<n; i++) {
					String zone_option = liste[i] + "_" + zone;
					Divers.ecrire(buf, zone_option + "=list()", indent);
				}
			}
		}
		else if (arg.isTabBooleens()) {
			String zone_value = "value_" + zone;
			Divers.ecrire(buf, zone_value + "=list()", indent);
		}
		// la boucle
		Divers.ecrire(buf, "for (i1 in seq(1, " + prog.getMaxTab() + ")) {", indent);
		String msg1 = null;
		Argument arg1 = new Argument(arg.nom+"[i1]", arg.getTypeOfTab(), arg.mode);
		constrFormu(prog, buf, indent+1, msg1, arg1);
		/* ajout */ Divers.ecrire(buf, "n_col=n_col+1", indent+1);
		Divers.ecrire(buf, "}", indent);
	}
	
	private void constrMatFormu(Programme prog, StringBuffer buf, int indent, String msg, Argument arg) {
		String zone = "zone_" + arg.nom; zone = Divers.remplacer(zone, ".", "_");
		zone = Divers.remplacer(zone, "][", "*" + prog.getMaxTab() + "+");
		zone = Divers.remplacer(zone, "[", "{["); zone = Divers.remplacer(zone, "]", "]}");
		this.constrLabelFormu(prog, buf, indent, msg, arg);	
		Divers.ecrire(buf, zone + "=list()", indent);
		// les boucles
		Divers.ecrire(buf, "for (i1 in seq(1, " + prog.getMaxTab() + ")) {", indent);
		Divers.ecrire(buf, "for (j1 in seq(1, " + prog.getMaxTab() + ")) {", indent+1);
		String msg1 = prog.quote("rang ") + " + str(i1) + " + prog.quote(", ") + " + str(j1) + " + prog.quote(" : ");
		msg1 = null;
		Argument arg1 = new Argument(arg.nom+"[i1][j1]", arg.getTypeOfMat(), arg.mode);
		constrFormu(prog, buf, indent+2, msg1, arg1);
		/* ajout */ Divers.ecrire(buf, "n_col=n_col+1", indent+2);
		Divers.ecrire(buf, "}", indent+1);
		Divers.ecrire(buf, "n_lig=n_lig+1", indent+1);
		Divers.ecrire(buf, "n_col=n_col-" + prog.getMaxTab(), indent+1);
		Divers.ecrire(buf, "}", indent);
	}
	
	private void constrClasseFormu(Programme prog, StringBuffer buf, int indent, String msg, Argument arg) {
		Classe cl = (Classe) arg.getClasse(prog);
		String zone = "zone_" + arg.nom; zone = Divers.remplacer(zone, ".", "_");
		zone = Divers.remplacer(zone, "][", "*" + prog.getMaxTab() + "+");
		zone = Divers.remplacer(zone, "[", "{["); zone = Divers.remplacer(zone, "]", "]}");
		this.constrLabelFormu(prog, buf, indent, msg, arg);	
		for(Iterator<ModeleVariable> iter=cl.proprietes.iterator(); iter.hasNext(); ) {
			Variable prop = (Variable) iter.next();
			if (prop.isOut()) continue;
			String msg1 = prog.quote(prop.nom);
			Divers.ecrire(buf, "n_lig=n_lig+1", indent);
			Divers.ecrire(buf, "n_col=0", indent);
			Argument arg1 = new Argument(arg.nom+"."+prop.nom, prop.type, arg.mode);
			constrFormu(prog, buf, indent, msg1, arg1);
		}		
	}
	
	private void constrTabClasseFormu(Programme prog, StringBuffer buf, int indent, String msg, Argument arg) {
	}
	
// -------------------------------
// lecture d'arguments (Formulaire)
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
		String zone = "zone_" + arg.nom; zone = Divers.remplacer(zone, ".", "_");
		zone = Divers.remplacer(zone, "][", "*" + prog.getMaxTab() + "+");
		zone = Divers.remplacer(zone, "[", "{["); zone = Divers.remplacer(zone, "]", "]}");
		if (arg.isEntier()) {
			Divers.ecrire(buf, arg.nom + " = as.numeric( tclvalue(tkget(" + zone + ")) ) ", indent);
		}
		else if (arg.isReel()) {
			Divers.ecrire(buf, arg.nom + " = as.numeric( tclvalue(tkget(" + zone + ")) ) ", indent);
		}
		else if (arg.isTexte()) {
			this.lireTexteFormu(prog, buf, indent, arg);
		}
		else if (arg.isBooleen()) {
			String zone_value = "value_" + zone;
			Divers.ecrire(buf, arg.nom + " = as.logical(as.numeric(tclvalue(" + zone_value + "))) ", indent);
		}
	}
	
	private void lireTexteFormu(Programme prog, StringBuffer buf, int indent, Argument arg) {
		String zone = "zone_" + arg.nom; zone = Divers.remplacer(zone, ".", "_");
		zone = Divers.remplacer(zone, "][", "*" + prog.getMaxTab() + "+");
		zone = Divers.remplacer(zone, "[", "{["); zone = Divers.remplacer(zone, "]", "]}");
		if (arg.avecTexteRadio()) {
			String zone_value = "value_" + zone;
			Divers.ecrire(buf, arg.nom + " = as.character(tclvalue(" + zone_value + ")) ", indent);
		}
		else if (arg.avecTexteListe()) {
			Divers.indenter(buf, indent);
			Divers.ecrire(buf, arg.nom + " = tclvalue(tkget(" + zone + ",");
			Divers.ecrire(buf, "tkcurselection(" + zone + ")))");
		}
		else {
			Divers.ecrire(buf, arg.nom + " = as.character( tclvalue(tkget(" + zone + ")) ) ", indent);
		}		
	}
	
	private void lireTabFormu(Programme prog, StringBuffer buf, int indent, Argument arg) {
		String zone = "zone_" + arg.nom; zone = Divers.remplacer(zone, ".", "_");
		zone = Divers.remplacer(zone, "][", "*" + prog.getMaxTab() + "+");
		zone = Divers.remplacer(zone, "[", "{["); zone = Divers.remplacer(zone, "]", "]}");
		// la boucle
		Divers.ecrire(buf, "for (i1 in seq(1, " + prog.getDim(1, arg) + ")) {", indent);
		Argument arg1 = new Argument(arg.nom+"[i1]", arg.getTypeOfTab(), arg.mode);
		lireFormu(prog, buf, indent+1, arg1);
		Divers.ecrire(buf, "}", indent);
	}
	
	private void lireMatFormu(Programme prog, StringBuffer buf, int indent, Argument arg, String indice1, String indice2) {
		String zone = "zone_" + arg.nom; zone = Divers.remplacer(zone, ".", "_");
		zone = Divers.remplacer(zone, "][", "*" + prog.getMaxTab() + "+");
		zone = Divers.remplacer(zone, "[", "{["); zone = Divers.remplacer(zone, "]", "]}");
		// les boucles
		//instr_pere.addVariable(new Variable(indice1,"ENTIER"));
		//instr_pere.addVariable(new Variable(indice2,"ENTIER"));
		Divers.ecrire(buf, "for (i1 in seq(1, " + prog.getDim(1, arg) + ")) {", indent);
		Divers.ecrire(buf, "for (j1 in seq(1, " + prog.getDim(2, arg) + ")) {", indent+1);
		Argument arg1 = new Argument(arg.nom+"[i1][j1]", arg.getTypeOfMat(), arg.mode);
		lireFormu(prog, buf, indent+2, arg1);
		Divers.ecrire(buf, "}", indent+1);
		Divers.ecrire(buf, "}", indent);
		String dim1 = prog.getDim(1, arg);
		String dim2 = prog.getDim(2, arg);
		if ( !dim1.equals(prog.getMaxTab()) || !dim2.equals(prog.getMaxTab()) ) {
			Divers.ecrire(buf, arg.nom + "=" + arg.nom + "[1:" + dim1 + ",1:" + dim2 + "]", indent);
		}
	}
	
	private void lireClasseFormu(Programme prog, StringBuffer buf, int indent, Argument arg) {
		String zone = "zone_" + arg.nom; zone = Divers.remplacer(zone, ".", "_");
		zone = Divers.remplacer(zone, "][", "*" + prog.getMaxTab() + "+");
		zone = Divers.remplacer(zone, "[", "{["); zone = Divers.remplacer(zone, "]", "]}");
		Classe cl = (Classe) arg.getClasse(prog);
		for(Iterator<ModeleVariable> iter=cl.proprietes.iterator(); iter.hasNext(); ) {
			Variable prop = (Variable) iter.next();
			if (prop.isOut()) continue;
			Argument arg1 = new Argument(arg.nom+"."+prop.nom, prop.type, arg.mode);
			lireFormu(prog, buf, indent, arg1);
		}
		Divers.ecrire(buf, "try(assign('" + arg.nom + "',data.frame(" + arg.nom + ")),silent=True)", indent);
	}
	
	private void lireTabClasseFormu(Programme prog, StringBuffer buf, int indent, Argument arg) {
	}
}




