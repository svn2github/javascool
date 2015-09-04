/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2013.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.langages.java;

import java.util.Iterator;
import org.javascool.proglets.plurialgo.divers.Divers;
import org.javascool.proglets.plurialgo.langages.modele.*;

/**
 * Cette classe permet de traduire en Java une instruction
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
		Divers.ecrire(buf, "n_lig=0; // numero de ligne", indent);
		Divers.ecrire(buf, "try {", indent);
		Divers.indenter(buf, indent+1);
		//instr_pere.addVariable(new Variable("f_in","BufferedReader"));
		Divers.ecrire(buf, "BufferedReader " + "f_in" + " = new BufferedReader( ");
		Divers.ecrire(buf, "new FileReader(" + arg_fichier.nom +") );");
		Divers.ecrire(buf, "String ligne; ", indent+1);
		Divers.ecrire(buf, "while ( (ligne=" + "f_in" + ".readLine()) != null) {", indent+1);
		Divers.ecrire(buf, "// analyse de la ligne lue", indent+2);
		Divers.ecrire(buf, "java.util.StringTokenizer tok=new java.util.StringTokenizer(ligne," + prog.quote(" \\t") + ");", indent+2);
		for (Iterator<ModeleArgument> iter=instr_pere.arguments.iterator(); iter.hasNext();) {
			Argument arg = (Argument) iter.next();
			lireFichierTexte(prog, buf, indent+2, arg);
		}
		for (Iterator<ModeleInstruction> iter=arg_fichier.instructions.iterator(); iter.hasNext();) {
			Instruction instr = (Instruction) iter.next();
			instr.ecrire(prog, buf, indent+2);
		}
		Divers.ecrire(buf, "// incrementation du compteur de lignes", indent+2);
		Divers.ecrire(buf, "n_lig=n_lig+1;", indent+2);
		Divers.ecrire(buf, "}", indent+1);
		Divers.ecrire(buf, "f_in.close();", indent+1);
		Divers.ecrire(buf, "} catch (Exception ex) { System.out.println(ex.toString()); }", indent);
	}
	
	public void ecrireFichierTexte(Programme prog, StringBuffer buf, int indent) {
		Divers.ecrire(buf, "try {", indent);
		Divers.indenter(buf, indent+1);
		//this.addVariable(new Variable("f_out","PrintStream"));
		Divers.ecrire(buf, "PrintStream " + "f_out" + " = new PrintStream( ");
		Divers.ecrire(buf, "new FileOutputStream(" + arg_fichier.nom +") );");
		for (Iterator<ModeleArgument> iter=instr_pere.arguments.iterator(); iter.hasNext();) {
			Argument arg = (Argument) iter.next();
			String msg = prog.quote(arg.nom+" : ");
			ecrireFichierTexte(prog, buf, indent+1, msg, arg);
		}
		Divers.ecrire(buf, "f_out.close();", indent+1);
		Divers.ecrire(buf, "} catch (Exception ex) { System.out.println(ex.toString()); }", indent);
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
		if (arg.isEntier()) {
			Divers.indenter(buf, indent);
			Divers.ecrire(buf, arg.nom + " = Integer.parseInt( ");
			Divers.ecrire(buf, "tok.nextToken()" + " ); ");
		}
		else if (arg.isReel()) {
			Divers.indenter(buf, indent);
			Divers.ecrire(buf, arg.nom + " = Double.parseDouble( ");
			Divers.ecrire(buf, "tok.nextToken()" + " ); ");			
		}
		else if (arg.isTexte()) {
			Divers.indenter(buf, indent);
			Divers.ecrire(buf, arg.nom + " = ");
			Divers.ecrire(buf, "tok.nextToken()" + "; ");
		}
		else if (arg.isBooleen()) {
			Divers.indenter(buf, indent);
			Divers.ecrire(buf, arg.nom + " = Boolean.parseBoolean( ");
			Divers.ecrire(buf, "tok.nextToken()" + " ); ");
		}
	}
	
	private void lireTabFichierTexte(Programme prog, StringBuffer buf, int indent, Argument arg) {
		Argument arg1 = new Argument(arg.nom+"[n_lig]", arg.getTypeOfTab(), null);
		lireFichierTexte(prog, buf, indent, arg1);
	}
	
	private void lireMatFichierTexte(Programme prog, StringBuffer buf, int indent, Argument arg) {
		instr_pere.addVariable(new Variable("j1","ENTIER"));
		Divers.ecrire(buf, "for(j1=0; j1<" + prog.getDim(2, arg) + "; j1++) {", indent);
		Argument arg1 = new Argument(arg.nom+"[n_lig][j1]", arg.getTypeOfMat(), null);
		lireFichierTexte(prog, buf, indent+1, arg1);
		Divers.ecrire(buf, "}", indent);
	}
	
	private void lireTabClasseFichierTexte(Programme prog, StringBuffer buf, int indent, Argument arg) {
		Classe cl = (Classe) arg.getClasseOfTab(prog);
		for(Iterator<ModeleVariable> iter=cl.proprietes.iterator(); iter.hasNext(); ) {
			Variable prop = (Variable) iter.next();
			if (prop.isOut()) continue;
			if ( prop.isSimple()) {
				Argument arg1 = new Argument(arg.nom+"."+prop.nom, "TAB_"+prop.type, arg.mode);
				lireFichierTexte(prog, buf, indent, arg1);
				Divers.remplacer(buf, arg.nom+"."+prop.nom+"[n_lig]", arg.nom+"[n_lig]"+"."+prop.nom);
			}
			if ( prop.isTabSimple()) {
				Argument arg1 = new Argument(arg.nom+"."+prop.nom, "MAT_"+prop.getTypeOfTab(), arg.mode);
				lireFichierTexte(prog, buf, indent, arg1);
				Divers.remplacer(buf, arg.nom+"."+prop.nom+"[n_lig][j1]", arg.nom+"[n_lig]"+"."+prop.nom+"[j1]");
			}
		}
	}
	
	private void lireClasseFichierTexte(Programme prog, StringBuffer buf, int indent, Argument arg) {
		Classe cl = (Classe) arg.getClasse(prog);
		for(Iterator<ModeleVariable> iter=cl.proprietes.iterator(); iter.hasNext(); ) {
			Variable prop = (Variable) iter.next();
			if (prop.isOut()) continue;
			if ( prop.isTabSimple()) {
				instr_pere.addVariable(new Variable("j1","ENTIER"));
				Divers.ecrire(buf, "for(j1=0; j1<" + prog.getDim(1, arg) + "; j1++) {", indent);
				Argument arg1 = new Argument(arg.nom+"."+prop.nom+"[j1]", prop.getTypeOfTab(), null);
				lireFichierTexte(prog, buf, indent+1, arg1);
				Divers.ecrire(buf, "}", indent);
			}
			else {
				Argument arg1 = new Argument(arg.nom+"."+prop.nom, prop.type, null);
				lireFichierTexte(prog, buf, indent, arg1);
			}
		}
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
		Divers.indenter(buf, indent);
		if (msg!=null) Divers.ecrire(buf, "f_out.print(" + msg + "); ");
		Divers.ecrire(buf, "f_out.print(" + arg.nom + "); ");
		if (msg!=null && !(prog.quote("\\t").equals(msg))) Divers.ecrire(buf, "f_out.println(); "); 
	}
	
	private void ecrireTabFichierTexte(Programme prog, StringBuffer buf, int indent, String msg, Argument arg) {
		if (msg!=null) Divers.ecrire(buf, "f_out.println(" + msg + ");", indent);
		instr_pere.addVariable(new Variable("i1","ENTIER"));
		Divers.ecrire(buf, "for(i1=0; i1<" + prog.getDim(1, arg) + "; i1++) {", indent);
		String msg1 = prog.quote("\\t");
		Argument arg1 = new Argument(arg.nom+"[i1]", arg.getTypeOfTab(), null);
		ecrireFichierTexte(prog, buf, indent+1, msg1, arg1);
		Divers.ecrire(buf, "}", indent);
		Divers.ecrire(buf, "f_out.println();", indent);
	}
	
	private void ecrireMatFichierTexte(Programme prog, StringBuffer buf, int indent, String msg, Argument arg) {
		if (msg!=null) Divers.ecrire(buf, "f_out.println(" + msg + ");", indent); 
		instr_pere.addVariable(new Variable("i1","ENTIER"));
		instr_pere.addVariable(new Variable("j1","ENTIER"));
		Divers.ecrire(buf, "for(i1=0; i1<" + prog.getDim(1, arg) + "; i1++) {", indent);
		Divers.ecrire(buf, "for(j1=0; j1<" + prog.getDim(2, arg) + "; j1++) {", indent+1);
		String msg1 = prog.quote("\\t");
		Argument arg1 = new Argument(arg.nom+"[i1][j1]", arg.getTypeOfMat(), null);
		ecrireFichierTexte(prog, buf, indent+2, msg1, arg1);
		Divers.ecrire(buf, "}", indent+1);
		Divers.ecrire(buf, "f_out.println();", indent+1);
		Divers.ecrire(buf, "}", indent);
		Divers.ecrire(buf, "f_out.println();", indent);
	}
	
	private void ecrireClasseFichierTexte(Programme prog, StringBuffer buf, int indent, String msg, Argument arg) {
		if (msg!=null) Divers.ecrire(buf, "f_out.println(" + msg + ");", indent); 
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
		if (msg!=null) Divers.ecrire(buf, "f_out.println(" + msg + ");", indent); 
		Classe cl = (Classe) arg.getClasseOfTab(prog);
		instr_pere.addVariable(new Variable("ii","ENTIER"));
		Divers.ecrire(buf, "for(ii=0; ii<" + prog.getDim(1, arg) + "; ii++) {", indent);
		String msg1 = prog.quote("rang ") + " + ii + " + prog.quote(" de " + arg.nom + " : ");
		Divers.ecrire(buf, "f_out.println(" + msg1 + "); ", indent+1); 
		for(Iterator<ModeleVariable> iter=cl.proprietes.iterator(); iter.hasNext(); ) {
			Variable prop = (Variable) iter.next();
			if (prop.isIn()) continue;
			Argument arg1 = new Argument(arg.nom+"[ii]"+"."+prop.nom, prop.type, arg.oteDim(1));
			msg1=prog.quote(prop.nom + " : ");
			ecrireFichierTexte(prog, buf, indent+1, msg1, arg1);
		}
		Divers.ecrire(buf, "}", indent);
		Divers.ecrire(buf, "f_out.println();", indent);
	}
	
}
