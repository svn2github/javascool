/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2014.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.langages.R;

import java.util.Iterator;
import org.javascool.proglets.plurialgo.divers.Divers;
import org.javascool.proglets.plurialgo.langages.modele.*;

/**
 * Cette classe permet de traduire en R une instruction
 * d'ecriture dans un fichier odt (librairie odfWeave).
*/
public class FichierWriter {
	
	Instruction instr_pere;
	Argument arg_fichier;
	
	public FichierWriter(Instruction pere) {
		this.instr_pere = pere;
		arg_fichier = (Argument) pere.getFichier();
	}
	
	public void ecrireFichierTexte(Programme prog, StringBuffer buf, int indent) {
		Divers.ecrire(buf, "Exemple de rapport", indent);
		Divers.ecrire(buf, "\n");
		Divers.ecrire(buf, "<<echo=FALSE,results=hide>>=", indent);
		Divers.ecrire(buf, "# chunk d'initialisation (A completer)", indent+1);
		Divers.ecrire(buf, "@", indent);
		Divers.ecrire(buf, "\n");
		Divers.ecrire(buf, "Affichage brut (comme dans la console R)", indent);
		Divers.ecrire(buf, "\n");
		Divers.ecrire(buf, "<<echo=TRUE,results=verbatim>>=", indent);
		for (Iterator<ModeleArgument>  iter=instr_pere.arguments.iterator(); iter.hasNext();) {
			Argument arg = (Argument) iter.next();
			String msg = prog.quote(arg.nom+" : ");
			arg.ecrireStandard(prog, buf, indent+1, msg);
		}
		Divers.ecrire(buf, "@", indent);
		Divers.ecrire(buf, "\n");
		Divers.ecrire(buf, "Affichage ameliore (Sexpr ou odfTable)", indent);
		Divers.ecrire(buf, "\n");
		for (Iterator<ModeleArgument>  iter=instr_pere.arguments.iterator(); iter.hasNext();) {
			Argument arg = (Argument) iter.next();
			if (arg.isSimple()) {
				String msg = arg.nom+" : ";
				Divers.ecrire(buf, msg + "\\\\Sexpr{" + arg.nom + "}", indent);
				Divers.ecrire(buf, "\n");
			}
			if (arg.isTabSimple()) {
				String msg = arg.nom+" : ";
				Divers.ecrire(buf, msg, indent);
				Divers.ecrire(buf, "<<echo=FALSE,results=xml>>=", indent);
				String odf_table = "odf_" + arg.nom;
				Divers.ecrire(buf, odf_table + " = matrix(" + arg.nom + ", nrow=1)", indent);
				Divers.ecrire(buf, "colnames(" + odf_table + ") = names(" + arg.nom + ")", indent);	
				Divers.ecrire(buf, "odfTable(" + odf_table + ", useRowNames=T)", indent);				
				Divers.ecrire(buf, "@", indent);	
				Divers.ecrire(buf, "\n");			
			}
			if (arg.isMatSimple()) {
				String msg = arg.nom+" : ";
				Divers.ecrire(buf, msg, indent);
				Divers.ecrire(buf, "<<echo=FALSE,results=xml>>=", indent);
				String odf_table = arg.nom;
				Divers.ecrire(buf, "odfTable(" + odf_table + ", useRowNames=T)", indent);				
				Divers.ecrire(buf, "@", indent);	
				Divers.ecrire(buf, "\n");					
			}
		}
		Divers.ecrire(buf, "\n");		
	}
	
}
