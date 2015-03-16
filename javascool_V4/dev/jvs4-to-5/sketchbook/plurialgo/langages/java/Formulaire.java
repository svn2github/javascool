/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2013.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.langages.java;

import java.util.Iterator;

import org.javascool.proglets.plurialgo.divers.Divers;

/**
 * Cette classe permet de traduire en Java une instruction
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
	 * Déclaration des composants du formulaire.
	 * @param prog
	 * @param buf
	 * @param indent
	 */	
	public void declFormu(Programme prog, StringBuffer buf, int indent) {
		for (Iterator<org.javascool.proglets.plurialgo.langages.modele.Argument> iter=instr_pere.arguments.iterator(); iter.hasNext();) {
			Argument arg = (Argument) iter.next();
			declFormu(prog, buf, indent, arg);
		}
	}
	
	/**
	 * Initialisation des composants du formulaire.
	 * @param prog
	 * @param buf
	 * @param indent
	 */
	public void constrFormu(Programme prog, StringBuffer buf, int indent) {
		for (Iterator<org.javascool.proglets.plurialgo.langages.modele.Argument> iter=instr_pere.arguments.iterator(); iter.hasNext();) {
			Argument arg = (Argument) iter.next();
			String msg = prog.quote(arg.nom);
			constrFormu(prog, buf, indent+1, msg, "p", arg);
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
			lireFormu(prog, buf, indent+2, arg);
		}	
	}
	
// -------------------------------
// Déclaration des composants du formulaire
// -------------------------------
	
	private void declFormu(Programme prog, StringBuffer buf, int indent, Argument arg) {
		if ( arg.isSimple() ) {
			declSimpleFormu(prog, buf, indent, arg);
		}
		if ( arg.isTabSimple()) {
			declTabFormu(prog, buf, indent, arg);
		}
		if ( arg.isMatSimple()) {
			declMatFormu(prog, buf, indent, arg);
		}
		if ( arg.isEnregistrement(prog) || arg.isClasse(prog) ) {
			declClasseFormu(prog, buf, indent, arg);
		}
		if (arg.isTabClasse(prog)) {
			this.declTabClasseFormu(prog, buf, indent, arg);
		}
	}
	
	private void declSimpleFormu(Programme prog, StringBuffer buf, int indent, Argument arg) {
		String zone = "zone_" + arg.nom; zone = Divers.remplacer(zone, ".", "_");
		if (arg.isEntier() || arg.isReel()) {
			Divers.indenter(buf, indent);
			Divers.ecrire(buf, "JTextField " + zone + "; ");			
		}
		else if (arg.isTexte()) {
			this.declTexteFormu(prog, buf, indent, arg);
		}
		else if (arg.isBooleen()) {
			Divers.indenter(buf, indent);
			Divers.ecrire(buf, "Checkbox " + zone + "; ");
		}
	}
	
	private void declTexteFormu(Programme prog, StringBuffer buf, int indent, Argument arg) {
		if (arg.avecTexteRadio()) {
			String zone = "zone_" + arg.nom; zone = Divers.remplacer(zone, ".", "_");
			String group = "group_" + arg.nom; group = Divers.remplacer(group, ".", "_");
			Divers.indenter(buf, indent);
			Divers.ecrire(buf, "ButtonGroup " + group + "; ");
			String p_group = "p_" + group;
			Divers.indenter(buf, indent);
			Divers.ecrire(buf, "JPanel " + p_group + "; ");
			String[] liste = arg.getTexteRadio();
			int n = liste.length;
			for(int i=0; i<n; i++) {
				Divers.indenter(buf, indent);
				String zone_bouton = "zone_" + liste[i] + "_" + arg.nom; 
				zone_bouton = Divers.remplacer(zone_bouton, ".", "_");
				Divers.ecrire(buf, "JRadioButton " + zone_bouton + "; ");
			}
		}
		else if (arg.avecTexteListe()) {
			String zone = "zone_" + arg.nom; zone = Divers.remplacer(zone, ".", "_");
			String model = "model_" + arg.nom; model = Divers.remplacer(model, ".", "_");
			Divers.indenter(buf, indent);
			Divers.ecrire(buf, "JList " + zone + "; ");
			Divers.indenter(buf, indent);
			Divers.ecrire(buf, "DefaultListModel " + model + "; ");
		}
		else {
			String zone = "zone_" + arg.nom; zone = Divers.remplacer(zone, ".", "_");
			Divers.indenter(buf, indent);
			Divers.ecrire(buf, "JTextField " + zone + "; ");
		}
	}
	
	private void declTabFormu(Programme prog, StringBuffer buf, int indent, Argument arg) {
		String zone = "zone_" + arg.nom; zone = Divers.remplacer(zone, ".", "_");
		String p_zone = "p_" + arg.nom; p_zone = Divers.remplacer(p_zone, ".", "_");
		if (arg.isTabBooleens()) {
			Divers.indenter(buf, indent);
			Divers.ecrire(buf, "Checkbox[] " + zone + "; ");			
		}
		else if (arg.isTabTextes() && arg.avecTexteRadio()) {
			String group = "group_" + arg.nom; group = Divers.remplacer(group, ".", "_");
			Divers.indenter(buf, indent);
			Divers.ecrire(buf, "ButtonGroup [] " + group + "; ");
			String p_group = "p_" + group;
			Divers.indenter(buf, indent);
			Divers.ecrire(buf, "JPanel [] " + p_group + "; ");
			String[] liste = arg.getTexteRadio();
			int n = liste.length;
			for(int i=0; i<n; i++) {
				Divers.indenter(buf, indent);
				String zone_bouton = "zone_" + liste[i] + "_" + arg.nom; 
				zone_bouton = Divers.remplacer(zone_bouton, ".", "_");
				Divers.ecrire(buf, "JRadioButton [] " + zone_bouton + "; ");
			}
		}
		else if (arg.isTabTextes() && arg.avecTexteListe()) {
			String model = "model_" + arg.nom; model = Divers.remplacer(model, ".", "_");
			Divers.indenter(buf, indent);
			Divers.ecrire(buf, "JList[] " + zone + "; ");	
			Divers.indenter(buf, indent);
			Divers.ecrire(buf, "DefaultListModel[] " + model + "; ");		
		}
		else {
			Divers.indenter(buf, indent);
			Divers.ecrire(buf, "JTextField[] " + zone + "; ");
		}
		Divers.ecrire(buf, "JPanel " + p_zone + "; ");
	}
	
	private void declMatFormu(Programme prog, StringBuffer buf, int indent, Argument arg) {
		String zone = "zone_" + arg.nom; zone = Divers.remplacer(zone, ".", "_");
		String p_zone = "p_" + arg.nom; p_zone = Divers.remplacer(p_zone, ".", "_");
		if (arg.isMatBooleens()) {
			Divers.indenter(buf, indent);
			Divers.ecrire(buf, "Checkbox[][] " + zone + "; ");			
		}
		else if (arg.isMatTextes() && arg.avecTexteRadio()) {
			String group = "group_" + arg.nom; group = Divers.remplacer(group, ".", "_");
			Divers.indenter(buf, indent);
			Divers.ecrire(buf, "ButtonGroup [][] " + group + "; ");
			String p_group = "p_" + group;
			Divers.indenter(buf, indent);
			Divers.ecrire(buf, "JPanel [][] " + p_group + "; ");
			String[] liste = arg.getTexteRadio();
			int n = liste.length;
			for(int i=0; i<n; i++) {
				Divers.indenter(buf, indent);
				String zone_bouton = "zone_" + liste[i] + "_" + arg.nom; 
				zone_bouton = Divers.remplacer(zone_bouton, ".", "_");
				Divers.ecrire(buf, "JRadioButton [][] " + zone_bouton + "; ");
			}			
		}
		else if (arg.isMatTextes() && arg.avecTexteListe()) {
			String model = "model_" + arg.nom; model = Divers.remplacer(zone, ".", "_");
			Divers.indenter(buf, indent);
			Divers.ecrire(buf, "JList[][] " + zone + "; ");	
			Divers.indenter(buf, indent);
			Divers.ecrire(buf, "DefaultListModel[][] " + model + "; ");	
		}
		else {
			Divers.indenter(buf, indent);
			Divers.ecrire(buf, "JTextField[][] " + zone + "; ");
		}
		Divers.ecrire(buf, "JPanel " + p_zone + "; ");
	}
	
	private void declClasseFormu(Programme prog, StringBuffer buf, int indent, Argument arg) {
		String p_zone = "p_" + arg.nom; p_zone = Divers.remplacer(p_zone, ".", "_");
		Divers.indenter(buf, indent);
		Divers.ecrire(buf, "JPanel " + p_zone + "; ");
		Classe cl = (Classe) arg.getClasse(prog);
		for(Iterator<org.javascool.proglets.plurialgo.langages.modele.Variable> iter=cl.proprietes.iterator(); iter.hasNext(); ) {
			Variable prop = (Variable) iter.next();
			if (prop.isOut()) continue;
			Argument arg1 = new Argument(arg.nom+"."+prop.nom, prop.type, arg.mode);
			declFormu(prog, buf, indent, arg1);
		}
	}
	
	private void declTabClasseFormu(Programme prog, StringBuffer buf, int indent, Argument arg) {
		String zone = "zone_" + arg.nom; zone = Divers.remplacer(zone, ".", "_");
		String p_zone = "p_" + arg.nom; p_zone = Divers.remplacer(p_zone, ".", "_");
		Divers.indenter(buf, indent);
		Divers.ecrire(buf, "JPanel " + p_zone + "; ");
		Classe cl = (Classe) arg.getClasseOfTab(prog);
		for(Iterator<org.javascool.proglets.plurialgo.langages.modele.Variable> iter=cl.proprietes.iterator(); iter.hasNext(); ) {
			Variable prop = (Variable) iter.next();
			if (prop.isOut()) continue;
			if ( prop.isSimple()) {
				Argument arg1 = new Argument(arg.nom+"."+prop.nom, "TAB_"+prop.type, arg.mode);
				declFormu(prog, buf, indent, arg1);
			}
			if ( prop.isTabSimple()) {
				Argument arg1 = new Argument(arg.nom+"."+prop.nom, "MAT_"+prop.getTypeOfTab(), arg.mode);
				declFormu(prog, buf, indent, arg1);
			}
		}
	}
	
// -------------------------------
// Initialisation des composants du formulaire
// -------------------------------
	
	private void constrFormu(Programme prog, StringBuffer buf, int indent, String msg, String p_pere, Argument arg) {
		if ( arg.isSimple() ) {
			this.constrSimpleFormu(prog, buf, indent, msg, p_pere, arg);
		}
		if ( arg.isTabSimple()) {
			this.constrTabFormu(prog, buf, indent, msg, p_pere, arg, "i1");
		}
		if ( arg.isMatSimple() ) {
			this.constrMatFormu(prog, buf, indent, msg, p_pere, arg);
		}
		if ( arg.isEnregistrement(prog) || arg.isClasse(prog) ) {
			constrClasseFormu(prog, buf, indent, msg, p_pere, arg);
		}
		if (arg.isTabClasse(prog)) {
			constrTabClasseFormu(prog, buf, indent, msg, p_pere, arg);
		}
	}
	
	private void constrLabelFormu(Programme prog, StringBuffer buf, int indent, String msg, String p_pere, Argument arg) {
		if (msg!=null) {
			Divers.indenter(buf, indent);
			String label = "new JLabel(" + msg + ")";
			Divers.ecrire(buf, p_pere + ".add(" + label + ");");
		}
	}
	
	private void constrSimpleFormu(Programme prog, StringBuffer buf, int indent, String msg, String p_pere, Argument arg) {
		String zone = "zone_" + arg.nom; zone = Divers.remplacer(zone, ".", "_");
		if (arg.isEntier() || arg.isReel()) {
			this.constrLabelFormu(prog, buf, indent, msg, p_pere, arg);	
			Divers.indenter(buf, indent);
			Divers.ecrire(buf, zone + "=new JTextField(); ");
			Divers.ecrire(buf, p_pere + ".add(" + zone + ");");	
		}
		else if (arg.isTexte()) {
			this.constrTexteFormu(prog, buf, indent, msg, p_pere, arg);
		}
		else if (arg.isBooleen()) {
			this.constrLabelFormu(prog, buf, indent, msg, p_pere, arg);
			Divers.indenter(buf, indent);
			Divers.ecrire(buf, zone + "=new Checkbox(); ");
			Divers.ecrire(buf, p_pere + ".add(" + zone + ");");
		}
	}
	
	private void constrTexteFormu(Programme prog, StringBuffer buf, int indent, String msg, String p_pere, Argument arg) {
		String zone = "zone_" + arg.nom; zone = Divers.remplacer(zone, ".", "_");
		if (arg.avecTexteRadio()) {
			String group = "group_" + arg.nom; group = Divers.remplacer(group, ".", "_");
			this.constrLabelFormu(prog, buf, indent, msg, p_pere, arg);
			Divers.indenter(buf, indent);
			Divers.ecrire(buf, group + "=new ButtonGroup(); ");
			String p_group = "p_" + group;
			Divers.indenter(buf, indent);
			Divers.ecrire(buf, p_group + "=new JPanel(); ");
			Divers.ecrire(buf, p_pere + ".add(" + p_group + "); ");
			String[] liste = arg.getTexteRadio();
			int n = liste.length;
			for(int i=0; i<n; i++) {
				String zone_bouton = "zone_" + liste[i] + "_" + arg.nom; 
				zone_bouton = Divers.remplacer(zone_bouton, ".", "_");
				Divers.indenter(buf, indent);
				Divers.ecrire(buf, zone_bouton + " = new JRadioButton(" + prog.quote(liste[i])+ "); ");
				Divers.indenter(buf, indent);
				Divers.ecrire(buf, group + ".add(" + zone_bouton + "); ");
				Divers.indenter(buf, indent);
				Divers.ecrire(buf, p_group + ".add(" + zone_bouton + "); ");
			}
		}
		else if (arg.avecTexteListe()) {
			String model = "model_" + arg.nom; model = Divers.remplacer(model, ".", "_");
			this.constrLabelFormu(prog, buf, indent, msg, p_pere, arg);
			Divers.indenter(buf, indent);
			Divers.ecrire(buf, model + "=new DefaultListModel(); ");
			String[] liste = arg.getTexteListe();
			int n = liste.length;
			for(int i=0; i<n; i++) {
				Divers.ecrire(buf, model + ".addElement(" + prog.quote(liste[i])+ ");", indent);
			}
			Divers.indenter(buf, indent);
			Divers.ecrire(buf, zone + "=new JList(" + model + "); ");
			Divers.indenter(buf, indent);
			Divers.ecrire(buf, zone + ".setVisibleRowCount(2); ");
			Divers.indenter(buf, indent);
			Divers.ecrire(buf, p_pere + ".add(new JScrollPane(" + zone + ")); ");
		}
		else {
			this.constrLabelFormu(prog, buf, indent, msg, p_pere, arg);	
			Divers.indenter(buf, indent);
			Divers.ecrire(buf, zone + "=new JTextField(); ");
			Divers.ecrire(buf, p_pere + ".add(" + zone + ");");	
		}
	}
	
	private void constrTabFormu(Programme prog, StringBuffer buf, int indent, String msg, String p_pere, Argument arg, String indice) {
		this.constrLabelFormu(prog, buf, indent, msg, p_pere, arg);	
		String p_zone = "p_" + arg.nom; p_zone = Divers.remplacer(p_zone, ".", "_");
		String zone = "zone_" + arg.nom; zone = Divers.remplacer(zone, ".", "_");
		Divers.indenter(buf, indent);
		Divers.ecrire(buf, p_zone + "=new JPanel(new GridLayout(1, " + prog.getMaxTab() +")); ");
		Divers.ecrire(buf, p_pere + ".add(" + p_zone + "); ");
		if (arg.isTabBooleens()) {
			Divers.indenter(buf, indent);
			Divers.ecrire(buf, zone + "=new Checkbox [" + prog.getMaxTab() + "]; ");			
		}
		else if (arg.isTabTextes() && arg.avecTexteRadio()) {
			String group = "group_" + arg.nom; group = Divers.remplacer(group, ".", "_");
			Divers.indenter(buf, indent);
			Divers.ecrire(buf, group + "=new ButtonGroup[" + prog.getMaxTab() + "]; ");
			String p_group = "p_" + group;
			Divers.indenter(buf, indent);
			Divers.ecrire(buf, p_group + "=new JPanel[ " + prog.getMaxTab() + "]; ");
			String[] liste = arg.getTexteRadio();
			int n = liste.length;
			for(int i=0; i<n; i++) {
				String zone_bouton = "zone_" + liste[i] + "_" + arg.nom; 
				zone_bouton = Divers.remplacer(zone_bouton, ".", "_");
				Divers.indenter(buf, indent);
				Divers.ecrire(buf, zone_bouton + "=new JRadioButton[ " + prog.getMaxTab() + "]; ");
			}			
		}
		else if (arg.isTabTextes() && arg.avecTexteListe()) {
			String model = "model_" + arg.nom; model = Divers.remplacer(model, ".", "_");
			Divers.indenter(buf, indent);
			Divers.ecrire(buf, zone + "=new JList [" + prog.getMaxTab() + "]; ");	
			Divers.indenter(buf, indent);
			Divers.ecrire(buf, model + "=new DefaultListModel [" + prog.getMaxTab() + "]; ");	
		}
		else {
			Divers.indenter(buf, indent);
			Divers.ecrire(buf, zone + "=new JTextField [" + prog.getMaxTab() + "]; ");
		}
		// la boucle
		instr_pere.addVariable(new Variable(indice,"ENTIER"));
		Divers.ecrire(buf, "for(int " + indice + "=0; " + indice + "<" + prog.getMaxTab() + "; " + indice + "++) {", indent);
		String msg1 = null;
		Argument arg1 = new Argument(arg.nom+"[" + indice + "]", arg.getTypeOfTab(), arg.mode);
		constrFormu(prog, buf, indent+1, msg1, p_zone, arg1);
		Divers.ecrire(buf, "}", indent);
	}
	
	private void constrMatFormu(Programme prog, StringBuffer buf, int indent, String msg, String p_pere, Argument arg) {
		this.constrLabelFormu(prog, buf, indent, msg, p_pere, arg);	
		Divers.indenter(buf, indent);
		String p_zone = "p_" + arg.nom; p_zone = Divers.remplacer(p_zone, ".", "_");
		Divers.ecrire(buf, p_zone + "=new JPanel(new GridLayout(" + prog.getMaxTab() + "," + prog.getMaxTab() + ")); ");
		Divers.ecrire(buf, p_pere + ".add(" + p_zone + "); ");
		String zone = "zone_" + arg.nom; zone = Divers.remplacer(zone, ".", "_");
		if (arg.isMatBooleens()) {
			Divers.indenter(buf, indent);
			Divers.ecrire(buf, zone + "=new Checkbox [" + prog.getMaxTab() + "]["  + prog.getMaxTab() + "]; ");			
		}
		else if (arg.isMatTextes() && arg.avecTexteRadio()) {
			String group = "group_" + arg.nom; group = Divers.remplacer(group, ".", "_");
			Divers.indenter(buf, indent);
			Divers.ecrire(buf, group + "=new ButtonGroup[" + prog.getMaxTab() + "]["  + prog.getMaxTab() + "]; ");
			String p_group = "p_" + group;
			Divers.indenter(buf, indent);
			Divers.ecrire(buf, p_group + "=new JPanel[ " + prog.getMaxTab()+ "]["  + prog.getMaxTab() + "]; ");
			String[] liste = arg.getTexteRadio();
			int n = liste.length;
			for(int i=0; i<n; i++) {
				String zone_bouton = "zone_" + liste[i] + "_" + arg.nom; 
				zone_bouton = Divers.remplacer(zone_bouton, ".", "_");
				Divers.indenter(buf, indent);
				Divers.ecrire(buf, zone_bouton + "=new JRadioButton[ " + prog.getMaxTab()+ "][" + prog.getMaxTab() + "]; ");
			}			
		}
		else if (arg.isMatTextes() && arg.avecTexteListe()) {
			String model = "model_" + arg.nom; model = Divers.remplacer(model, ".", "_");
			Divers.indenter(buf, indent);
			Divers.ecrire(buf, zone + "=new JList [" + prog.getMaxTab() + "]["  + prog.getMaxTab() + "]; ");	
			Divers.indenter(buf, indent);
			Divers.ecrire(buf, model + "=new DefaultListModel [" + prog.getMaxTab() + "]["  + prog.getMaxTab() + "]; ");			
		}
		else {
			Divers.indenter(buf, indent);
			Divers.ecrire(buf, zone + "=new JTextField [" + prog.getMaxTab() + "]["  + prog.getMaxTab() + "]; ");
		}
		// les boucles
		arg.addVariable(new Variable("i1","ENTIER"));
		arg.addVariable(new Variable("j1","ENTIER"));
		Divers.ecrire(buf, "for(int i1=0; i1<" + prog.getMaxTab() + "; i1++) {", indent);
		Divers.ecrire(buf, "for(int j1=0; j1<" + prog.getMaxTab() + "; j1++) {", indent+1);
		String msg1 = null;
		Argument arg1 = new Argument(arg.nom+"[i1][j1]", arg.getTypeOfMat(), arg.mode);
		constrFormu(prog, buf, indent+2, msg1, p_zone, arg1);
		Divers.ecrire(buf, "}", indent+1);
		Divers.ecrire(buf, "}", indent);
	}
	
	private void constrClasseFormu(Programme prog, StringBuffer buf, int indent, String msg, String p_pere, Argument arg) {
		this.constrLabelFormu(prog, buf, indent, msg, p_pere, arg);	
		Classe cl = (Classe) arg.getClasse(prog);
		String p_zone = "p_" + arg.nom; p_zone = Divers.remplacer(p_zone, ".", "_");
		int nb_prop = cl.proprietes.size() - cl.compterMode("OUT");
		Divers.ecrire(buf, p_zone + "=new JPanel(new GridLayout(" + (2*nb_prop) + "," + 1 + ")); ");
		Divers.ecrire(buf, p_pere + ".add(" + p_zone + ");");
		for(Iterator<org.javascool.proglets.plurialgo.langages.modele.Variable> iter=cl.proprietes.iterator(); iter.hasNext(); ) {
			Variable prop = (Variable) iter.next();
			if (prop.isOut()) continue;
			String msg1 = prog.quote(prop.nom);
			Argument arg1 = new Argument(arg.nom+"."+prop.nom, prop.type, arg.mode);
			constrFormu(prog, buf, indent, msg1, p_zone, arg1);
		}
	}
	
	private void constrTabClasseFormu(Programme prog, StringBuffer buf, int indent, String msg, String p_pere, Argument arg) {
		this.constrLabelFormu(prog, buf, indent, msg, p_pere, arg);	
		Classe cl = (Classe) arg.getClasseOfTab(prog);
		Divers.indenter(buf, indent);
		String p_zone = "p_" + arg.nom; p_zone = Divers.remplacer(p_zone, ".", "_");
		String zone = "zone_" + arg.nom; zone = Divers.remplacer(zone, ".", "_");
		int nb_prop = cl.proprietes.size() - cl.compterMode("OUT");
		Divers.ecrire(buf, p_zone + "=new JPanel(new GridLayout(" + 2 + "," + nb_prop + ")); ");
		Divers.ecrire(buf, p_pere + ".add(" + p_zone + ");");
		for(Iterator<org.javascool.proglets.plurialgo.langages.modele.Variable> iter=cl.proprietes.iterator(); iter.hasNext(); ) {
			Variable prop = (Variable) iter.next();
			if (prop.isOut()) continue;
			String msg1 = prog.quote(prop.nom);
			String label1 = "new JLabel(" + msg1 + ")";
			Divers.indenter(buf, indent);
			Divers.ecrire(buf, p_zone + ".add(" + label1 + ");");
		}
		for(Iterator<org.javascool.proglets.plurialgo.langages.modele.Variable> iter=cl.proprietes.iterator(); iter.hasNext(); ) {
			Variable prop = (Variable) iter.next();
			if (prop.isOut()) continue;
			instr_pere.addVariable(new Variable("ii","ENTIER"));
			if (prop.isSimple()) {
				Argument arg1 = new Argument(arg.nom+"."+prop.nom, "TAB_"+prop.type, arg.mode);
				String msg1 = null;
				constrTabFormu(prog, buf, indent, msg1, p_zone, arg1, "ii");
			}
			if (prop.isTabSimple()) {
				Argument arg1 = new Argument(arg.nom+"."+prop.nom, "MAT_"+prop.getTypeOfTab(), arg.mode);
				String msg1 = null;
				constrMatFormu(prog, buf, indent, msg1, p_zone, arg1);
			}
		}
	}
	
// -------------------------------
// lecture des composants du formulaire
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
	
	public void lireSimpleFormu(Programme prog, StringBuffer buf, int indent, Argument arg) {
		String zone = "zone_" + arg.nom; zone = Divers.remplacer(zone, ".", "_");
		if (arg.isEntier()) {
			Divers.indenter(buf, indent);
			Divers.ecrire(buf, arg.nom + " = Integer.parseInt( ");
			Divers.ecrire(buf, zone + ".getText() ); "); 
		}
		else if (arg.isReel()) {
			Divers.indenter(buf, indent);
			Divers.ecrire(buf, arg.nom + " = Double.parseDouble( ");
			Divers.ecrire(buf, zone + ".getText() ); "); 
		}
		else if (arg.isTexte()) {
			this.lireTexteFormu(prog, buf, indent, arg);
		}
		else if (arg.isBooleen()) {
			Divers.indenter(buf, indent);
			Divers.ecrire(buf, arg.nom + " = ");
			Divers.ecrire(buf, zone + ".getState(); "); 
		}
	}
	
	private void lireTexteFormu(Programme prog, StringBuffer buf, int indent, Argument arg) {
		String zone = "zone_" + arg.nom; zone = Divers.remplacer(zone, ".", "_");
		Divers.indenter(buf, indent);
		Divers.ecrire(buf, arg.nom + " = ");
		if (arg.avecTexteRadio()) {
			Divers.ecrire(buf, "null; "); 	
			String[] liste = arg.getTexteRadio();
			int n = liste.length;
			for(int i=0; i<n; i++) {
				String zone_bouton = "zone_" + liste[i] + "_" + arg.nom; 
				zone_bouton = Divers.remplacer(zone_bouton, ".", "_");
				Divers.indenter(buf, indent);
				Divers.ecrire(buf, "if (" + zone_bouton + ".isSelected()) ");
				Divers.ecrire(buf, arg.nom + "=" + prog.quote(liste[i])+ "; ");
			}		
		}
		else if (arg.avecTexteListe()) {
			Divers.ecrire(buf, "(String) " + zone + ".getSelectedValue(); "); 
		}
		else {
			Divers.ecrire(buf, zone + ".getText(); "); 			
		}

	}
	
	private void lireTabFormu(Programme prog, StringBuffer buf, int indent, Argument arg) {
		String zone = "zone_" + arg.nom; zone = Divers.remplacer(zone, ".", "_");
		// la boucle
		instr_pere.addVariable(new Variable("i1","ENTIER"));
		Divers.ecrire(buf, "for(i1=0; i1<" + prog.getDim(1, arg) + "; i1++) {", indent);
		Argument arg1 = new Argument(arg.nom+"[i1]", arg.getTypeOfTab(), arg.mode);
		lireFormu(prog, buf, indent+1, arg1);
		Divers.ecrire(buf, "}", indent);
	}
	
	private void lireMatFormu(Programme prog, StringBuffer buf, int indent, Argument arg, String indice1, String indice2) {
		String zone = "zone_" + arg.nom; zone = Divers.remplacer(zone, ".", "_");
		// les boucles
		instr_pere.addVariable(new Variable(indice1,"ENTIER"));
		instr_pere.addVariable(new Variable(indice2,"ENTIER"));
		Divers.ecrire(buf, "for(int " + indice1 + "=0; " + indice1 + "<" + prog.getDim(1, arg) + "; " + indice1 + "++) {", indent);
		Divers.ecrire(buf, "for(int " + indice2 + "=0; " + indice2 + "<" + prog.getDim(2, arg) + "; " + indice2 + "++) {", indent+1);
		Argument arg1 = new Argument(arg.nom+"[" + indice1 + "][" + indice2 + "]", arg.getTypeOfMat(), arg.mode);
		lireFormu(prog, buf, indent+2, arg1);
		Divers.ecrire(buf, "}", indent+1);
		Divers.ecrire(buf, "}", indent);
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
			if ( prop.isSimple() ) {
				instr_pere.addVariable(new Variable("i1","ENTIER"));
				Argument arg1 = new Argument(arg.nom+"."+prop.nom, "TAB_"+prop.type, arg.mode);
				lireFormu(prog, buf, indent, arg1);
				Divers.remplacer(buf, arg.nom+"."+prop.nom+"[i1]", arg.nom+"[i1]"+"."+prop.nom);
			}
			if ( prop.isTabSimple() ) {
				instr_pere.addVariable(new Variable("i1","ENTIER"));
				instr_pere.addVariable(new Variable("ii","ENTIER"));
				Argument arg1 = new Argument(arg.nom+"."+prop.nom, "MAT_"+prop.getTypeOfTab(), arg.mode);
				lireMatFormu(prog, buf, indent,arg1, "i1", "ii");
				Divers.remplacer(buf, arg.nom+"."+prop.nom+"[ii][i1]", arg.nom+"[ii]"+"."+prop.nom+"[i1]");
			}
		}
	}
	
}

