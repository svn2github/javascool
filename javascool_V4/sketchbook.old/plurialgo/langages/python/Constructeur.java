/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2013.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.langages.python;

import java.util.*;
import org.javascool.proglets.plurialgo.divers.*;
import org.javascool.proglets.plurialgo.langages.modele.*;



/**
 * Cette classe hérite de la classe homonyme du modèle.
*/
public class Constructeur extends ModeleConstructeur {

	public Constructeur() {
	}
	
	public void ecrire(Programme prog, StringBuffer buf, int indent) {
		Classe cl = null;
		for (Iterator<ModeleClasse> iter=prog.classes.iterator(); iter.hasNext();) {
			cl = (Classe) iter.next();
			if (cl.nom.equals(this.nom)) break;
		}
		Divers.indenter(buf, indent);
		Divers.ecrire(buf, "def __init__");
		Divers.ecrire(buf, "(");
		Divers.ecrire(buf, "self");
		if (parametres.size()>0)	Divers.ecrire(buf, ", ");
		for (Iterator<ModeleParametre> iter=parametres.iterator(); iter.hasNext();) {
			Parametre param = (Parametre) iter.next();
			param.ecrire(prog, buf);
			if (iter.hasNext()) Divers.ecrire(buf, ", ");
		}
		Divers.ecrire(buf, ")");	
		Divers.ecrire(buf, " : ");
		Divers.ecrire(buf, prog.quote("proprietes"), indent+1);
		for (Iterator<ModeleVariable> iter=cl.proprietes.iterator(); iter.hasNext();) {
			Variable prop = (Variable) iter.next();
			prop.ecrirePropriete(prog, buf, indent+1);
		}
		for (Iterator<ModeleVariable> iter=variables.iterator(); iter.hasNext();) {
			Variable var = (Variable) iter.next();
			var.ecrire(prog, buf, indent+1);
		}
		for (Iterator<ModeleInstruction> iter=instructions.iterator(); iter.hasNext();) {
			Instruction instr = (Instruction) iter.next();
			instr.ecrire(prog, buf, indent+1);
		}
		Divers.ecrire(buf, "#end", indent);
	}
	
}
