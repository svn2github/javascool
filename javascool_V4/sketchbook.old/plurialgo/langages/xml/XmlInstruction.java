/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2013.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.langages.xml;

import org.javascool.proglets.plurialgo.langages.modele.*;

/**
 * Cette classe hérite de la classe homonyme du modèle.
*/
public class XmlInstruction extends ModeleInstruction {
	
	public XmlInstruction() {
	}
	
	public XmlInstruction(String nom) {
		this.nom = nom;
	}
	
	public static XmlInstruction creerInstructionPour (String var, String debut, String fin, String pas) {
		XmlInstruction instr= new XmlInstruction("pour");
		XmlPour pour = new XmlPour(); 
		pour.var = var; pour.debut=debut; pour.fin = fin; pour.pas = pas;
		pour.instructions.add(new XmlInstruction("// ajouter des instructions"));
		instr.pours.add(pour);
		return instr;
	}

	public static XmlInstruction creerInstructionSi (int nbsi) {
		XmlInstruction instr= new XmlInstruction("si");
		for(int i=1; i<=nbsi;i++){
			XmlSi si = new XmlSi(); si.condition = "condition" + i;
			si.instructions.add(new XmlInstruction("// ajouter des instructions"));
			instr.sis.add(si);
		}
		return instr;
	}
	
	public static XmlInstruction creerInstructionAffectation (String var, String expression) {
		XmlInstruction instr= new XmlInstruction("affectation");
		XmlAffectation aff = new XmlAffectation(); aff.var = var; aff.expression = expression;
		instr.affectations.add(aff);
		return instr;
	}
	
	public static XmlInstruction creerInstructionTantQue (String condition) {
		XmlInstruction instr= new XmlInstruction("tantque");
		XmlTantQue tq = new XmlTantQue(); tq.condition = condition;
		tq.instructions.add(new XmlInstruction("// ajouter des instructions"));
		instr.tantques.add(tq);
		return instr;
	}	
		
}
