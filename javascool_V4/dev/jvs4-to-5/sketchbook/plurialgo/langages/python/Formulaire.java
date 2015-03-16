/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2013.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.langages.python;

import java.util.Iterator;
import org.javascool.proglets.plurialgo.divers.Divers;

/**
 * Cette classe permet de traduire en Python une instruction
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
		for (Iterator<org.javascool.proglets.plurialgo.langages.modele.Argument> iter=instr_pere.arguments.iterator(); iter.hasNext();) {
			Argument arg = (Argument) iter.next();
			String msg = prog.quote(arg.nom+ " : ");
			Divers.ecrire(buf, "n_col=0", indent);
			constrFormu(prog, buf, indent, msg, arg);
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
		for (Iterator<org.javascool.proglets.plurialgo.langages.modele.Argument> iter=instr_pere.arguments.iterator(); iter.hasNext();) {
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
		String label = "label_" + arg.nom; label = Divers.remplacer(label, ".", "_");
		if (msg!=null) {
			Divers.ecrire(buf, label + " = Label(formu, text=" + msg + ")", indent);
			Divers.ecrire(buf, label + ".grid(row=n_lig, column=n_col)", indent);
			Divers.ecrire(buf, "n_col=n_col+1", indent);
		}
	}
	
	private void constrSimpleFormu(Programme prog, StringBuffer buf, int indent, String msg, Argument arg) {
		String zone = "zone_" + arg.nom; zone = Divers.remplacer(zone, ".", "_");
		if (arg.isEntier() || arg.isReel()) {
			this.constrLabelFormu(prog, buf, indent, msg, arg);
			Divers.ecrire(buf, zone + " = Entry(formu)", indent);
			Divers.ecrire(buf, zone + ".grid(row=n_lig, column=n_col)", indent);
		}
		else if (arg.isTexte()) {
			this.constrTexteFormu(prog, buf, indent, msg, arg);
		}
		else if (arg.isBooleen()) {
			this.constrLabelFormu(prog, buf, indent, msg, arg);
			String var_zone = "var_" + zone;
			Divers.ecrire(buf, var_zone + " = IntVar()", indent);
			Divers.ecrire(buf, zone + " = Checkbutton(formu, variable=" + var_zone + ")", indent);
			Divers.ecrire(buf, zone + ".grid(row=n_lig, column=n_col)", indent);
		}
	}
	
	private void constrTexteFormu(Programme prog, StringBuffer buf, int indent, String msg, Argument arg) {
		String zone = "zone_" + arg.nom; zone = Divers.remplacer(zone, ".", "_");
		if (arg.avecTexteRadio() || arg.avecTexteListe()) {
			this.constrLabelFormu(prog, buf, indent, msg, arg);
			Divers.ecrire(buf, zone + " = Listbox(formu, height=3, exportselection=0)", indent);
			String[] liste = arg.getTexteListe();
			int n = liste.length;
			for(int i=0; i<n; i++) {
				Divers.ecrire(buf, zone + ".insert(END, " + prog.quote(liste[i])+ ")", indent);
			}
			Divers.ecrire(buf, zone + ".grid(row=n_lig, column=n_col)", indent);
		}
		else {
			this.constrLabelFormu(prog, buf, indent, msg, arg);	
			Divers.ecrire(buf, zone + " = Entry(formu)", indent);
			Divers.ecrire(buf, zone + ".grid(row=n_lig, column=n_col)", indent);
		}
	}
	
	private void constrTabFormu(Programme prog, StringBuffer buf, int indent, String msg, Argument arg, String indice) {
		String zone = "zone_" + arg.nom; zone = Divers.remplacer(zone, ".", "_");
		this.constrLabelFormu(prog, buf, indent, msg, arg);
		Divers.ecrire(buf, zone + "=[0]*" + prog.getMaxTab(), indent);
		// la boucle
		Divers.ecrire(buf, "for " + indice + " in range(0, " + prog.getMaxTab() + ") :", indent);
		String msg1 = null;
		Argument arg1 = new Argument(arg.nom+"[" + indice + "]", arg.getTypeOfTab(), arg.mode);
		constrFormu(prog, buf, indent+1, msg1, arg1);
		/* ajout */ Divers.ecrire(buf, "n_col=n_col+1", indent+1);
		Divers.ecrire(buf, "#end for", indent);
	}
	
	private void constrMatFormu(Programme prog, StringBuffer buf, int indent, String msg, Argument arg) {
		String zone = "zone_" + arg.nom; zone = Divers.remplacer(zone, ".", "_");
		this.constrLabelFormu(prog, buf, indent, msg, arg);	
		String MAX_TAB = prog.getMaxTab();
		Divers.ecrire(buf, zone + " = []", indent);
		Divers.ecrire(buf, "for i1 in range(0," + MAX_TAB + ") :", indent);
		Divers.ecrire(buf, zone + ".append(" + "[0]*" + MAX_TAB + ")", indent+1);
		Divers.ecrire(buf, "#end for", indent);
		// les boucles
		Divers.ecrire(buf, "for i1 in range(0, " + prog.getMaxTab() + ") :", indent);
		Divers.ecrire(buf, "for j1 in range(0, " + prog.getMaxTab() + ") :", indent+1);
		String msg1 = null;
		Argument arg1 = new Argument(arg.nom+"[i1][j1]", arg.getTypeOfMat(), arg.mode);
		constrFormu(prog, buf, indent+2, msg1, arg1);
		/* ajout */ Divers.ecrire(buf, "n_col=n_col+1", indent+2);
		Divers.ecrire(buf, "#end for", indent+1);
		Divers.ecrire(buf, "n_lig=n_lig+1", indent+1);
		Divers.ecrire(buf, "n_col=n_col-" + prog.getMaxTab(), indent+1);
		Divers.ecrire(buf, "#end for", indent);
	}
	
	private void constrClasseFormu(Programme prog, StringBuffer buf, int indent, String msg, Argument arg) {
		Classe cl = (Classe) arg.getClasse(prog);
		this.constrLabelFormu(prog, buf, indent, msg, arg);	
		for(Iterator<org.javascool.proglets.plurialgo.langages.modele.Variable> iter=cl.proprietes.iterator(); iter.hasNext(); ) {
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
		Classe cl = (Classe) arg.getClasseOfTab(prog);
		String label = "label_" + arg.nom; label = Divers.remplacer(label, ".", "_");
		String zone = "zone_" + arg.nom; zone = Divers.remplacer(zone, ".", "_");
		if (msg!=null) {
			Divers.ecrire(buf, label + " = Label(formu, text=" + msg + ")", indent);
			Divers.ecrire(buf, label + ".grid(row=n_lig, column=n_col)", indent);
			Divers.ecrire(buf, "n_lig=n_lig+1", indent);
			Divers.ecrire(buf, "n_col=0", indent);
		}
		for(Iterator<org.javascool.proglets.plurialgo.langages.modele.Variable> iter=cl.proprietes.iterator(); iter.hasNext(); ) {
			Variable prop = (Variable) iter.next();
			if (prop.isOut()) continue;
			String msg1 = prog.quote(prop.nom);
			String label1 = "label_" + prop.nom;
			Divers.ecrire(buf, label1 + " = Label(formu, text=" + msg1 + ")", indent);
			Divers.ecrire(buf, label1 + ".grid(row=n_lig, column=n_col)", indent);
			if ( prop.isSimple()) {
				Divers.ecrire(buf, "n_col=n_col+1", indent);
			}
			if ( prop.isTabSimple()) {
				Divers.ecrire(buf, "n_col=n_col+" + prog.getMaxTab(), indent);
			}
		}
		Divers.ecrire(buf, "n_lig=n_lig+1", indent);
		Divers.ecrire(buf, "n_col=0", indent);
		for(Iterator<org.javascool.proglets.plurialgo.langages.modele.Variable> iter=cl.proprietes.iterator(); iter.hasNext(); ) {
			Variable prop = (Variable) iter.next();
			if (prop.isOut()) continue;
			if ( prop.isSimple()) {
				String msg1 = null; 
				constrTabFormu(prog, buf, indent, msg1, arg, "ii");
				Divers.ecrire(buf, "n_lig=n_lig-" + prog.getMaxTab(), indent);
				Divers.ecrire(buf, "n_col=n_col+1", indent);
			}
			if ( prop.isTabEntiers() || prop.isTabReels() || prop.isTabTextes() || prop.isTabBooleens()) {
				Argument arg1 = new Argument(arg.nom+"."+prop.nom, "MAT_"+prop.getTypeOfTab(), arg.mode);
				String msg1 = null;
				constrFormu(prog, buf, indent, msg1, arg1);
				Divers.ecrire(buf, "n_lig=n_lig-" + prog.getMaxTab(), indent);
				Divers.ecrire(buf, "n_col=n_col+" + prog.getMaxTab(), indent);
			}
		}
		Divers.ecrire(buf, "n_lig=n_lig+" + prog.getMaxTab(), indent);
		Divers.ecrire(buf, "n_col=0", indent);
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
		if (arg.isEntier() || arg.isReel()) {
			Divers.ecrire(buf, arg.nom + " = eval( " + zone + ".get() ) ", indent);
		}
		else if (arg.isTexte()) {
			this.lireTexteFormu(prog, buf, indent, arg);
		}
		else if (arg.isBooleen()) {
			String var_zone = "var_" + zone;
			Divers.ecrire(buf, arg.nom + " = " + var_zone + ".get() ", indent);
		}
	}
	
	private void lireTexteFormu(Programme prog, StringBuffer buf, int indent, Argument arg) {
		String zone = "zone_" + arg.nom; zone = Divers.remplacer(zone, ".", "_");
		if (arg.avecTexteListe()) {
			Divers.ecrire(buf, arg.nom + " = " + zone + ".get(" + zone + ".curselection() ) ", indent);
		}
		else {
			Divers.ecrire(buf, arg.nom + " = str( " + zone + ".get() ) ", indent);
		}
	}
	
	private void lireTabFormu(Programme prog, StringBuffer buf, int indent, Argument arg) {
		String zone = "zone_" + arg.nom; zone = Divers.remplacer(zone, ".", "_");
		// la boucle
		Divers.ecrire(buf, "for i1 in range(0, " + prog.getDim(1, arg) + ") :", indent);
		Argument arg1 = new Argument(arg.nom+"[i1]", arg.getTypeOfTab(), arg.mode);
		lireFormu(prog, buf, indent+1, arg1);
		Divers.ecrire(buf, "#end for", indent);
	}
	
	private void lireMatFormu(Programme prog, StringBuffer buf, int indent, Argument arg, String indice1, String indice2) {
		String zone = "zone_" + arg.nom; zone = Divers.remplacer(zone, ".", "_");
		// les boucles
		Divers.ecrire(buf, "for " + indice1 + " in range(0, " + prog.getDim(1, arg) + ") :", indent);
		Divers.ecrire(buf, "for " + indice2 + " in range(0, " + prog.getDim(2, arg) + ") :", indent+1);
		Argument arg1 = new Argument(arg.nom+"["+indice1+"]["+indice2+"]", arg.getTypeOfMat(), arg.mode);
		lireFormu(prog, buf, indent+2, arg1);
		Divers.ecrire(buf, "#end for", indent+1);
		Divers.ecrire(buf, "#end for", indent);	
	}
	
	private void lireClasseFormu(Programme prog, StringBuffer buf, int indent, Argument arg) {
		Classe cl = (Classe) arg.getClasse(prog);
		for(Iterator<org.javascool.proglets.plurialgo.langages.modele.Variable> iter=cl.proprietes.iterator(); iter.hasNext(); ) {
			Variable prop = (Variable) iter.next();
			if (prop.isOut()) continue;
			Argument arg1 = new Argument(arg.nom+"."+prop.nom, prop.type, arg.mode);
			lireFormu(prog, buf, indent, arg1);
		}
	}
	
	private void lireTabClasseFormu(Programme prog, StringBuffer buf, int indent, Argument arg) {
		Classe cl = (Classe) arg.getClasseOfTab(prog);
		String zone = "zone_" + arg.nom; zone = Divers.remplacer(zone, ".", "_");
		for(Iterator<org.javascool.proglets.plurialgo.langages.modele.Variable> iter=cl.proprietes.iterator(); iter.hasNext(); ) {
			Variable prop = (Variable) iter.next();
			if (prop.isOut()) continue;
			if ( prop.isSimple()) {
				Argument arg1 = new Argument(arg.nom+"."+prop.nom, "TAB_"+prop.type, arg.mode);
				lireFormu(prog, buf, indent, arg1);
				Divers.remplacer(buf, arg.nom+"."+prop.nom+"[i1]", arg.nom+"[i1]"+"."+prop.nom);
			}
			if ( prop.isTabSimple()) {
				Argument arg1 = new Argument(arg.nom+"."+prop.nom, "MAT_"+prop.getTypeOfTab(), arg.mode);
				lireMatFormu(prog, buf, indent, arg1, "ii", "i1");
				Divers.remplacer(buf, arg.nom+"."+prop.nom+"[ii][i1]", arg.nom+"[ii]"+"."+prop.nom+"[i1]");
			}
		}
	}
	
}


