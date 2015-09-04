/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2014.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.langages.R;

import java.util.Iterator;
import org.javascool.proglets.plurialgo.divers.Divers;
import org.javascool.proglets.plurialgo.langages.modele.*;

/**
 * Cette classe permet de traduire en R une instruction
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
		for (Iterator<ModeleArgument> iter=instr_pere.arguments.iterator(); iter.hasNext();) {
			Argument arg = (Argument) iter.next();
			if (arg.isMatSimple()) {
				this.lireFichierTexteAvecMat(prog, buf, indent);
				return;
			}
		}
		Divers.indenter(buf, indent);
		Divers.ecrire(buf, "f_in = read.table(" + arg_fichier.nom + ", header=T, sep=" + prog.quote("\\t") + ")");
		Divers.ecrire(buf, "n_lig = length(rownames(f_in)) # nombre de lignes", indent);
		Divers.ecrire(buf, "n_col = length(colnames(f_in)) # nombre de colonnes", indent);
		Divers.ecrire(buf, "attach(f_in)", indent);
	}
	
	private void lireFichierTexteAvecMat(Programme prog, StringBuffer buf, int indent) {
		Divers.indenter(buf, indent);
		Divers.ecrire(buf, "f_in = read.table(" + arg_fichier.nom + ", header=F, sep=" + prog.quote("\\t") + ")");
		Divers.ecrire(buf, "n_lig=length(rownames(f_in)) # nombre de lignes", indent);
		Divers.ecrire(buf, "n_col=0 # numero de colonne", indent);
		for (Iterator<ModeleArgument> iter=instr_pere.arguments.iterator(); iter.hasNext();) {
			Argument arg = (Argument) iter.next();
			lireFichierTexte(prog, buf, indent, arg);
		}
		if (arg_fichier.instructions.size()>0) {
			String n_lig = "length(rownames(f_in))";
			Divers.ecrire(buf,"for (n_lig in seq(1," + n_lig +")-1) {", indent);
			for (Iterator<ModeleInstruction> iter=arg_fichier.instructions.iterator(); iter.hasNext();) {
				Instruction instr = (Instruction) iter.next();
				instr.ecrire(prog, buf, indent+1);
			}
			Divers.ecrire(buf, "}", indent);
			Divers.ecrire(buf, "n_lig=length(rownames(f_in))", indent);
		}
	}
	
	public void ecrireFichierTexte(Programme prog, StringBuffer buf, int indent) {
		Divers.ecrire(buf, "library(R2HTML)", indent);
		Divers.indenter(buf, indent);
		Divers.ecrire(buf, "HTMLInitFile(outdir=getwd(), filename=" + arg_fichier.nom + ", useLaTeX=FALSE, useGrid=FALSE)");
		Divers.ecrire(buf, "HTML.title(" + prog.quote("Exemple de rapport") + ", HR=1)", indent);
		for (Iterator<ModeleArgument> iter=instr_pere.arguments.iterator(); iter.hasNext();) {
			Argument arg = (Argument) iter.next();
			String titre = prog.quote("Etude de la variable " + arg.nom);
			Divers.ecrire(buf, "HTML.title(" + titre + ", HR=2)", indent);
			String msg = prog.quote(arg.nom+" : ");
			ecrireFichierTexte(prog, buf, indent, msg, arg);
		}
		Divers.ecrire(buf, "HTMLEndFile()",indent);
		
	}
	
// -------------------------------
// lecture d'arguments (fichiers textes)
// -------------------------------
	
	private void lireFichierTexte(Programme prog, StringBuffer buf, int indent, Argument arg) {
		if ( arg.isSimple() ) {
			lireSimpleFichierTexte(prog,buf,indent,arg);
		}
		if ( arg.isTabSimple()) {
			lireTabFichierTexte(prog,buf,indent,arg);
		}
		if ( arg.isTabClasse(prog)) {
			lireTabClasseFichierTexte(prog, buf, indent, arg);
		}
		if ( arg.isMatEntiers() || arg.isMatReels() || arg.isMatTextes() || arg.isMatBooleens()) {
			lireMatFichierTexte(prog,buf,indent,arg);
		}
		if ( arg.isEnregistrement(prog) || arg.isClasse(prog) ) {
			lireClasseFichierTexte(prog, buf, indent, arg);
		}
	}
	
	private void lireSimpleFichierTexte(Programme prog, StringBuffer buf, int indent, Argument arg) {
		Divers.ecrire(buf, arg.nom + " = as.vector(f_in[,n_col])", indent);
		Divers.ecrire(buf, "n_col = n_col+1 ", indent);
	}
	
	private void lireTabFichierTexte(Programme prog, StringBuffer buf, int indent, Argument arg) {
		Divers.ecrire(buf, arg.nom + " = as.vector(f_in[,n_col])", indent);
		Divers.ecrire(buf, "n_col = n_col+1 ", indent);
	}
	
	private void lireMatFichierTexte(Programme prog, StringBuffer buf, int indent, Argument arg) {
		Divers.ecrire(buf, arg.nom + " = as.matrix(f_in[,seq(n_col,n_col+" + prog.getDim(2, arg) +")])", indent);
		Divers.ecrire(buf, "n_col = n_col+" + prog.getDim(2, arg), indent);
	}
	
	private void lireTabClasseFichierTexte(Programme prog, StringBuffer buf, int indent, Argument arg) {
	}
	
	private void lireClasseFichierTexte(Programme prog, StringBuffer buf, int indent, Argument arg) {
		Classe cl = (Classe) arg.getClasse(prog);
		for(Iterator<ModeleVariable> iter=cl.proprietes.iterator(); iter.hasNext(); ) {
			Variable prop = (Variable) iter.next();
			if (prop.isOut()) continue;
			Argument arg1 = new Argument(arg.nom+"."+prop.nom, prop.type, null);
			lireFichierTexte(prog, buf, indent, arg1);
		}		
	}
	
// --------------------------------------
// ecriture d'arguments	(textes)
// --------------------------------------
	
	private void ecrireFichierTexte(Programme prog, StringBuffer buf, int indent, String msg, Argument arg) {
		if ( arg.isSimple() ) {
			ecrireSimpleFichierTexte(prog,buf,indent,msg,arg);
		}
		if ( arg.isTabSimple()) {
			ecrireTabFichierTexte(prog,buf,indent,msg,arg);
		}
		if ( arg.isTabClasse(prog)) {
			ecrireTabClasseFichierTexte(prog, buf, indent, msg,arg);
		}
		if ( arg.isMatSimple() ) {
			ecrireMatFichierTexte(prog,buf,indent,msg,arg);
		}
		if ( arg.isEnregistrement(prog) || arg.isClasse(prog)) {
			ecrireClasseFichierTexte(prog, buf, indent, msg,arg);
		}
	}
	
	private void ecrireSimpleFichierTexte(Programme prog, StringBuffer buf, int indent, String msg, Argument arg) {
		if (msg!=null) Divers.ecrire(buf, "HTML(" + msg + ") ", indent);
		Divers.ecrire(buf, "HTML(" + arg.nom + "); ", indent); 
	}
	
	private void ecrireTabFichierTexte(Programme prog, StringBuffer buf, int indent, String msg, Argument arg) {
		if (msg!=null) Divers.ecrire(buf, "HTML(" + msg + ") ", indent);
		Divers.ecrire(buf, "HTML(matrix(" + arg.nom + ", nrow=1)); ", indent);
	}
	
	private void ecrireMatFichierTexte(Programme prog, StringBuffer buf, int indent, String msg, Argument arg) {
		if (msg!=null) Divers.ecrire(buf, "HTML(" + msg + ") ", indent);
		Divers.ecrire(buf, "HTML(" + arg.nom + "); ", indent);
	}
	
	private void ecrireClasseFichierTexte(Programme prog, StringBuffer buf, int indent, String msg, Argument arg) {
		if (msg!=null) Divers.ecrire(buf, "HTML(" + msg + ") ", indent);
		Classe cl = (Classe) arg.getClasse(prog);
		for(Iterator<ModeleVariable> iter=cl.proprietes.iterator(); iter.hasNext(); ) {
			Variable prop = (Variable) iter.next();
			if (prop.isIn()) continue;
			Argument arg1 = new Argument(arg.nom+"."+prop.nom, prop.type, arg.mode);
			String msg1 = prog.quote(arg.nom + " : ");
			ecrireFichierTexte(prog, buf, indent, msg1, arg1);
		}
	}
	
	private void ecrireTabClasseFichierTexte(Programme prog, StringBuffer buf, int indent, String msg, Argument arg) {
	}	
	
}
