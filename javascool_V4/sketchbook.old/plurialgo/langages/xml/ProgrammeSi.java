/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2013.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.langages.xml;



/**
 * Cette classe permet de créer des instructions conditionnelles (onglet "Si").
*/
public class ProgrammeSi extends XmlProgramme {
	
	private XmlInstruction instr_si, instr_si_1;
	private XmlSi cur_si;
	
	public ProgrammeSi() {
		this.nom = "exemple";
		instr_si = new XmlInstruction("si");
		instr_si_1 = null;
		cur_si = null; 
		this.instructions.add(instr_si);
	}

	public void ajouterBranche(int niv_si, String cond) {
		//System.out.println("ajouter " + niv_si + " " + cond);
		XmlSi si = new XmlSi(); si.condition = cond;
		si.instructions.add(new XmlInstruction("// ajouter des instructions"));
		if (niv_si==0) {
			instr_si.sis.add(si);
			instr_si_1 = null;
			cur_si = si;
		}
		else if (niv_si==1) {
			if (instr_si_1==null) {
				instr_si_1 = new XmlInstruction("si");
				cur_si.instructions.add(instr_si_1);
			}
			instr_si_1.sis.add(si);
		}
	}
}