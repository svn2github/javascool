/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2013.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.langages.xml;

import java.util.*;
import org.javascool.proglets.plurialgo.langages.modele.*;


/**
 * Cette classe hérite de la classe homonyme du modèle.
*/
public class XmlProgramme extends ModeleProgramme {
	
	public XmlProgramme() {
	}
	
	public XmlProgramme(XmlProgramme progDer) {
		nom = progDer.nom;
		operations = progDer.operations;
		variables = progDer.variables;
		instructions = progDer.instructions;
		classes = progDer.classes;
		les_fichiers = progDer.les_fichiers;
		buf_error = progDer.buf_error; 
		buf_warning = progDer.buf_warning;
	}

	
	// ----------------------------
	// MAX_TAB
	// ----------------------------
	
	protected void traiterMaxTab() {
		for (Iterator<ModeleInstruction> iter=instructions.iterator(); iter.hasNext();) {
			XmlInstruction instr = (XmlInstruction) iter.next();
			traiterMaxTab(instr);
		}
		for (Iterator<ModeleOperation> iter1=operations.iterator(); iter1.hasNext();) {
			XmlOperation oper = (XmlOperation) iter1.next();
			for (Iterator<ModeleInstruction> iter=oper.instructions.iterator(); iter.hasNext();) {
				XmlInstruction instr = (XmlInstruction) iter.next();
				traiterMaxTab(instr);
			}
		}
		for (Iterator<ModeleClasse> iter2=classes.iterator(); iter2.hasNext();) {
			XmlClasse cl = (XmlClasse) iter2.next();
			for (Iterator<ModeleOperation> iter1=cl.operations.iterator(); iter1.hasNext();) {
				XmlOperation oper = (XmlOperation) iter1.next();
				for (Iterator<ModeleInstruction> iter=oper.instructions.iterator(); iter.hasNext();) {
					XmlInstruction instr = (XmlInstruction) iter.next();
					traiterMaxTab(instr);
				}
			}
			for (Iterator<ModeleConstructeur> iter1=cl.constructeurs.iterator(); iter1.hasNext();) {
				XmlConstructeur constr = (XmlConstructeur) iter1.next();
				for (Iterator<ModeleInstruction> iter=constr.instructions.iterator(); iter.hasNext();) {
					XmlInstruction instr = (XmlInstruction) iter.next();
					traiterMaxTab(instr);
				}
			}
		}
	}
	
	private void traiterMaxTab(XmlInstruction instr) {
		if (instr.isEcriture() || instr.isLecture()) {
			if (instr.arguments.size()==0) return;
			XmlArgument arg1 = (XmlArgument) instr.arguments.get(0);
			XmlArgument arg2 = arg1;
			if (instr.arguments.size()>=2) {
				arg2 = (XmlArgument) instr.arguments.get(1);
			}
			for (Iterator<ModeleArgument> iter=instr.arguments.iterator(); iter.hasNext();) {
				XmlArgument arg = (XmlArgument) iter.next();
				traiterMaxTab(arg,arg1,arg2);
			}
		}
	}
	
	private void traiterMaxTab(XmlArgument arg, XmlArgument arg1, XmlArgument arg2) {
		String mode1 = "MAX_TAB";
		String mode2 = "MAX_TAB";
		String mode3 = "MAX_TAB";
		if (arg.isTabSimple()) {
			if (arg1.isEntier()) {
				mode1 = arg1.nom;
			}
			arg.mode = mode1;
		}
		if (arg.isMatSimple()) {
			if (arg1.isEntier()) {
				mode1 = arg1.nom;
			}
			if (arg2.isEntier()) {
				mode2 = arg2.nom;
			}
			else {
				mode2 = mode1;
			}
			arg.mode = mode1 + "," + mode2;
		}
		if (arg.isClasse(this) || arg.isEnregistrement(this)) {
			arg.mode = null;
			XmlClasse cl = (XmlClasse) arg.getClasse(this);
			if (cl.proprietes.size()==0) return;
			XmlVariable prop1 = (XmlVariable) cl.proprietes.get(0);
			XmlVariable prop2 = prop1;
			if (cl.proprietes.size()>=2) {
				prop2 = (XmlVariable) cl.proprietes.get(1);
			}
			for (Iterator<ModeleVariable> iter=cl.proprietes.iterator(); iter.hasNext();) {
				XmlVariable prop = (XmlVariable) iter.next();
				if (prop.isTabSimple()) {
					if (prop1.isEntier()) {
						mode1 = arg.nom + "." + prop1.nom;
					}
					arg.mode = mode1;
				}
				if (prop.isMatSimple()) {
					if (prop1.isEntier()) {
						mode1 = arg.nom + "." + prop1.nom;
					}
					if (prop2.isEntier()) {
						mode2 = arg.nom + "." + prop2.nom;
					}
					else {
						mode2 = mode1;
					}
					arg.mode = mode1 + "," + mode2;
				}
			}
		}
		if (arg.isTabClasse(this)) {
			if (arg1.isEntier()) {
				mode1 = arg1.nom;
			}
			arg.mode = mode1;
			XmlClasse cl = (XmlClasse) arg.getClasseOfTab(this);
			if (cl.proprietes.size()==0) return;
			XmlVariable prop1 = (XmlVariable) cl.proprietes.get(0);
			XmlVariable prop2 = prop1;
			if (cl.proprietes.size()>=2) {
				prop2 = (XmlVariable) cl.proprietes.get(1);
			}
			for (Iterator<ModeleVariable> iter=cl.proprietes.iterator(); iter.hasNext();) {
				XmlVariable prop = (XmlVariable) iter.next();
				if (prop.isTabSimple()) {
					if (prop1.isEntier()) {
						mode2 = arg.nom + "." + prop1.nom;
					}
					arg.mode = mode1 + "," + mode2;
				}
				if (prop.isMatSimple()) {
					if (prop1.isEntier()) {
						mode2 = arg.nom + "." + prop1.nom;
					}
					if (prop2.isEntier()) {
						mode3 = arg.nom + "." + prop1.nom;
					}
					else {
						mode3 = mode2;
					}
					arg.mode = mode1 + "," + mode2 + "," + mode3;
				}
			}
		}
	}
}
