/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2013.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.langages.larp;

import java.util.Iterator;
import org.javascool.proglets.plurialgo.divers.Divers;
import org.javascool.proglets.plurialgo.langages.modele.*;

/**
 * Cette classe permet de traduire en Larp une instruction
 * de lecture ou d'écriture dans un fichier texte.
*/
public class FichierTexte {
	
	Instruction instr_pere;
	Argument arg_fichier;
	
	public FichierTexte(Instruction pere) {
		this.instr_pere = pere;
		arg_fichier = (Argument) pere.getFichier();
	}
	
	public void lireFichierTexte(Programme prog, StringBuffer buf, int indent) {
		instr_pere.addVariable(new Variable("n_lig","ENTIER"));
		Divers.ecrire(buf, "n_lig=0 \\\\ numero de ligne", indent);
		Divers.ecrire(buf, "f_in=3 \\\\ numero de canal (arbitraire)", indent);
		Divers.ecrire(buf, "OUVRIR FICHIER " + arg_fichier.nom + " SUR f_in EN LECTURE ", indent);
		Divers.ecrire(buf, "TANTQUE ( !FINDECONTENU(f_in) ) FAIRE", indent);
		Divers.ecrire(buf, "SEPARATEUR \"@\"", indent+1);
		Divers.ecrire(buf, "LIRE ligne DE f_in", indent+1);
		Divers.ecrire(buf, "SEPARATEUR \" \"", indent+1);
		Divers.ecrire(buf, "n_lig = n_lig+1", indent+1);
		for (Iterator<ModeleArgument> iter=instr_pere.arguments.iterator(); iter.hasNext();) {
			Argument arg = (Argument) iter.next();
			lireFichierTexte(prog, buf, indent+1, arg);
		}
		for (Iterator<ModeleInstruction> iter=arg_fichier.instructions.iterator(); iter.hasNext();) {
			Instruction instr = (Instruction) iter.next();
			instr.ecrire(prog, buf, indent+1);
		}
		Divers.ecrire(buf, "FINTANTQUE", indent);
		Divers.ecrire(buf, "FERMER f_in", indent);
	}
	
	public void ecrireFichierTexte(Programme prog, StringBuffer buf, int indent) {
		Divers.ecrire(buf, "f_out=4 \\\\ numero de canal (arbitraire)", indent);
		Divers.ecrire(buf, "OUVRIR FICHIER " + arg_fichier.nom + " SUR f_out EN ECRITURE  ", indent);
		for (Iterator<ModeleArgument> iter=instr_pere.arguments.iterator(); iter.hasNext();) {
			Argument arg = (Argument) iter.next();
			String msg = prog.quote(arg.nom+" : ");
			ecrireFichierTexte(prog, buf, indent+1, msg, arg);
		}
		Divers.ecrire(buf, "FERMER f_out", indent);
	}
	
// -------------------------------
// lecture d'arguments 
// -------------------------------
	
	private void lireFichierTexte(Programme prog, StringBuffer buf, int indent, Argument arg) {
		if ( arg.isSimple() ) {
			lireSimpleFichierTexte(prog,buf,indent,arg);
		}
		if ( arg.isTabSimple()) {
			lireTabFichierTexte(prog,buf,indent,arg);
		}
		if ( arg.isMatEntiers() || arg.isMatReels() || arg.isMatTextes() || arg.isMatBooleens()) {
			lireMatFichierTexte(prog,buf,indent,arg);
		}
	}	
	
	private void lireSimpleFichierTexte(Programme prog, StringBuffer buf, int indent, Argument arg) {
		Divers.ecrire(buf, arg.nom + " = ligne", indent);
		Divers.ecrire(buf, "SI (POSITION(\" \",ligne)>0) ALORS", indent);
		Divers.ecrire(buf, arg.nom + " = " + "SOUSENSEMBLE(ligne,1,POSITION(\" \",ligne)-1)", indent+1);
		Divers.ecrire(buf, "ligne = SOUSENSEMBLE(ligne,POSITION(\" \",ligne)+1,LONGUEUR(ligne))", indent+1);
		Divers.ecrire(buf, "FINSI", indent);
	}
	
	private void lireTabFichierTexte(Programme prog, StringBuffer buf, int indent, Argument arg) {
		Argument arg1 = new Argument(arg.nom+"[n_lig]", arg.getTypeOfTab(), null);
		lireFichierTexte(prog, buf, indent, arg1);
	}
	
	private void lireMatFichierTexte(Programme prog, StringBuffer buf, int indent, Argument arg) {
		Divers.ecrire(buf, "POUR j1=1 JUSQU'A " + prog.getDim(2, arg) + " INCREMENT 1 FAIRE", indent);
		Argument arg1 = new Argument(arg.nom+"[n_lig][j1]", arg.getTypeOfMat(), null);
		lireFichierTexte(prog, buf, indent+1, arg1);
		Divers.ecrire(buf, "FINPOUR", indent);
	}
	
// --------------------------------------
// écriture d'arguments
// --------------------------------------
	
	private void ecrireFichierTexte(Programme prog, StringBuffer buf, int indent, String msg, Argument arg) {
		if ( arg.isSimple() ) {
			ecrireSimpleFichierTexte(prog,buf,indent,msg,arg);
		}
		if ( arg.isTabSimple()) {
			ecrireTabFichierTexte(prog,buf,indent,msg,arg);
		}
		if ( arg.isMatSimple() ) {
			ecrireMatFichierTexte(prog,buf,indent,msg,arg);
		}
	}
	
	private void ecrireSimpleFichierTexte(Programme prog, StringBuffer buf, int indent, String msg, Argument arg) {
		if (msg!=null) Divers.ecrire(buf, "ECRIRE " + msg + " DANS f_out ", indent);
		Divers.ecrire(buf, "ECRIRE " + arg.nom + " DANS f_out ", indent);
	}
	
	private void ecrireTabFichierTexte(Programme prog, StringBuffer buf, int indent, String msg, Argument arg) {
		if (msg!=null) {
			Divers.ecrire(buf, "ECRIRE " + msg + " DANS f_out ", indent);
		}
		// this.addVariable(new Variable("i1","ENTIER"));
		Divers.ecrire(buf, "POUR i1=1 JUSQU'A " + prog.getDim(1, arg) + " INCREMENT 1 FAIRE", indent);
		String msg1 = null; //prog.quote("\\t");
		Argument arg1 = new Argument(arg.nom+"[i1]", arg.getTypeOfTab(), null);
		ecrireFichierTexte(prog, buf, indent+1, msg1, arg1);
		Divers.ecrire(buf, "FIN POUR", indent);	
	}
	
	private void ecrireMatFichierTexte(Programme prog, StringBuffer buf, int indent, String msg, Argument arg) {
		if (msg!=null) {
			Divers.ecrire(buf, "ECRIRE " + msg + " DANS f_out ", indent);
		}
		Divers.ecrire(buf, "POUR i1=1 JUSQU'A " + prog.getDim(1, arg) + " INCREMENT 1 FAIRE", indent);
		Divers.ecrire(buf, "POUR j1=1 JUSQU'A " + prog.getDim(2, arg) + " INCREMENT 1 FAIRE", indent+1);
		String msg1 = null; // prog.quote("\\t");
		Argument arg1 = new Argument(arg.nom+"[i1][j1]", arg.getTypeOfMat(), null);
		ecrireFichierTexte(prog, buf, indent+2, msg1, arg1);
		Divers.ecrire(buf, "FINPOUR", indent+1);
		Divers.ecrire(buf, "FINPOUR", indent);	
	}

}
