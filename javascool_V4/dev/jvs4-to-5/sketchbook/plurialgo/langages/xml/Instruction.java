/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2013.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.langages.xml;

/**
 * Cette classe hérite de la classe homonyme du modèle.
*/
public class Instruction extends org.javascool.proglets.plurialgo.langages.modele.Instruction {
	
	public Instruction() {
	}
	
	public Instruction(String nom) {
		this.nom = nom;
	}
	
	public static Instruction creerInstructionPour (String var, String debut, String fin, String pas) {
		Instruction instr= new Instruction("pour");
		Pour pour = new Pour(); 
		pour.var = var; pour.debut=debut; pour.fin = fin; pour.pas = pas;
		pour.instructions.add(new Instruction("// ajouter des instructions"));
		instr.pours.add(pour);
		return instr;
	}

	public static Instruction creerInstructionSi (int nbsi) {
		Instruction instr= new Instruction("si");
		for(int i=1; i<=nbsi;i++){
			Si si = new Si(); si.condition = "condition" + i;
			si.instructions.add(new Instruction("// ajouter des instructions"));
			instr.sis.add(si);
		}
		return instr;
	}
	
	public static Instruction creerInstructionAffectation (String var, String expression) {
		Instruction instr= new Instruction("affectation");
		Affectation aff = new Affectation(); aff.var = var; aff.expression = expression;
		instr.affectations.add(aff);
		return instr;
	}
	
	public static Instruction creerInstructionTantQue (String condition) {
		Instruction instr= new Instruction("tantque");
		TantQue tq = new TantQue(); tq.condition = condition;
		tq.instructions.add(new Instruction("// ajouter des instructions"));
		instr.tantques.add(tq);
		return instr;
	}	
		
}
