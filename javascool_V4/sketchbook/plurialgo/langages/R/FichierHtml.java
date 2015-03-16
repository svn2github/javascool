/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2014.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.langages.R;

import java.util.Iterator;
import org.javascool.proglets.plurialgo.divers.Divers;
import org.javascool.proglets.plurialgo.langages.modele.*;

/**
 * Cette classe permet de traduire en R une instruction
 * d'ecriture dans un fichier html (librairie R2HTML).
*/
public class FichierHtml {
	
	Instruction instr_pere;
	Argument arg_fichier;
	
	public FichierHtml(Instruction pere) {
		this.instr_pere = pere;
		arg_fichier = (Argument) pere.getFichier();
	}
	
	public void ecrireFichierTexte(Programme prog, StringBuffer buf, int indent) {
		Divers.ecrire(buf, "\n");
		prog.commenter(buf,"----- construction du fichier html",indent);
		Divers.ecrire(buf, "library(R2HTML)", indent);
		Divers.indenter(buf, indent);
		Divers.ecrire(buf, "HTMLInitFile(outdir=getwd(), filename=" + arg_fichier.nom + ", useLaTeX=FALSE, useGrid=FALSE)");
		Divers.ecrire(buf, "HTML.title(" + prog.quote("Exemple de rapport") + ", HR=1)", indent);
		for (Iterator<ModeleArgument> iter=instr_pere.arguments.iterator(); iter.hasNext();) {
			Argument arg = (Argument) iter.next();
			String titre = prog.quote("Affichage de la variable " + arg.nom);
			Divers.ecrire(buf, "HTML.title(" + titre + ", HR=2)", indent);
			String msg = prog.quote(arg.nom+" : ");
			ecrireFichierTexte(prog, buf, indent, msg, arg);
		}
		Divers.ecrire(buf, "HTMLEndFile()",indent);
		Divers.ecrire(buf, "\n");
		
	}
	
	public void ecrireInstruction(Programme prog, StringBuffer buf, int indent, Instruction instr) {
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
		Divers.ecrire(buf, "HTML(" + arg.nom + ") ", indent); 
	}
	
	private void ecrireTabFichierTexte(Programme prog, StringBuffer buf, int indent, String msg, Argument arg) {
		if (msg!=null) Divers.ecrire(buf, "HTML(" + msg + ") ", indent);
		Divers.ecrire(buf, "HTML(matrix(" + arg.nom + ", nrow=1)) ", indent);
	}
	
	private void ecrireMatFichierTexte(Programme prog, StringBuffer buf, int indent, String msg, Argument arg) {
		if (msg!=null) Divers.ecrire(buf, "HTML(" + msg + ") ", indent);
		Divers.ecrire(buf, "HTML(" + arg.nom + ") ", indent);
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
