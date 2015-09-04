/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2014.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.langages.R;

import java.util.Iterator;
import org.javascool.proglets.plurialgo.divers.Divers;
import org.javascool.proglets.plurialgo.langages.modele.*;

/**
 * Cette classe permet de traduire (approximativement) en R une instruction
 * de lecture (select ...) ou d'écriture (insert into...) dans une base de données.
*/
public class Sql {
	
	Instruction instr_pere;
	Argument arg_sql;
	
	public Sql(Instruction pere) {
		this.instr_pere = pere;
		arg_sql = (Argument) pere.getFichier();
	}
	
	public void lireSql(Programme prog, StringBuffer buf, int indent) {
		for (Iterator<ModeleArgument> iter=instr_pere.arguments.iterator(); iter.hasNext();) {
			Argument arg = (Argument) iter.next();
			if (arg.isMatSimple()) {
				this.lireSqlAvecMat(prog, buf, indent);
				return;
			}
		}
		Divers.ecrire(buf, "library(RODBC)", indent);
		Divers.ecrire(buf, "channel = odbcConnect(" + arg_sql.nom + ")", indent);
		Divers.ecrire(buf, "f_in = sqlQuery(channel, 'select * from matable')", indent);
		Divers.ecrire(buf, "n_lig = length(rownames(f_in)) # nombre de lignes", indent);
		Divers.ecrire(buf, "n_col = length(colnames(f_in)) # nombre de colonnes", indent);
		Divers.ecrire(buf, "attach(f_in)", indent);
	}	
	
	private void lireSqlAvecMat(Programme prog, StringBuffer buf, int indent) {
		Divers.ecrire(buf, "library(RODBC)", indent);
		Divers.ecrire(buf, "channel = odbcConnect('mabase')", indent);
		Divers.ecrire(buf, "f_in = sqlQuery(channel, 'select * from matable')", indent);
		Divers.ecrire(buf, "n_lig=length(rownames(f_in)) # nombre de lignes", indent);
		Divers.ecrire(buf, "n_col=0 # numero de colonne", indent);
		for (Iterator<ModeleArgument> iter=instr_pere.arguments.iterator(); iter.hasNext();) {
			Argument arg = (Argument) iter.next();
			lireSql(prog, buf, indent, arg);
		}
		if (arg_sql.instructions.size()>0) {
			String n_lig = "length(rownames(f_in))";
			Divers.ecrire(buf,"for (n_lig in seq(1," + n_lig +")-1) {", indent);
			for (Iterator<ModeleInstruction> iter=arg_sql.instructions.iterator(); iter.hasNext();) {
				Instruction instr = (Instruction) iter.next();
				instr.ecrire(prog, buf, indent+1);
			}
			Divers.ecrire(buf, "}", indent);
			Divers.ecrire(buf, "n_lig=length(rownames(f_in))", indent);
		}
	}	
		
	public void ecrireSql(Programme prog, StringBuffer buf, int indent) {
		StringBuffer bufInto = new StringBuffer("(");
		StringBuffer bufVal = new StringBuffer(" values(");
		StringBuffer bufPrep = new StringBuffer(" values(");
		instr_pere.ecrireSql(prog, bufInto, bufVal, bufPrep);
		Divers.ecrire(bufInto,")");
		Divers.ecrire(bufVal,")"); 
		Divers.ecrire(bufPrep,")");
		//this.addVariable(new Variable("n_col","ENTIER"));
		Divers.ecrire(buf, "library(RODBC)", indent);
		Divers.ecrire(buf, "channel = odbcConnect('mabase')", indent);
		Divers.ecrire(buf, "sqlQuery(channel, ", indent);
		Divers.ecrire(buf, "\"insert into matable");
		Divers.ecrire(buf, bufInto.toString());
		Divers.ecrire(buf, bufVal.toString());
		Divers.ecrire(buf, "\") ");	
	}

// -------------------------------
// lecture d'arguments (sql)
// -------------------------------
	
	private void lireSql(Programme prog, StringBuffer buf, int indent, Argument arg) {
		if ( arg.isSimple() ) {
			lireSimpleSql(prog,buf,indent,arg);
		}
		if ( arg.isTabSimple()) {
			lireTabSql(prog,buf,indent,arg);
		}
		if ( arg.isTabClasse(prog)) {
			lireTabClasseSql(prog, buf, indent, arg);
		}
		if ( arg.isMatSimple() ) {
			lireMatSql(prog,buf,indent,arg);
		}
		if ( arg.isEnregistrement(prog) || arg.isClasse(prog) ) {
			lireClasseSql(prog, buf, indent, arg);
		}
	}
	
	private void lireSimpleSql(Programme prog, StringBuffer buf, int indent, Argument arg) {
		Divers.ecrire(buf, arg.nom + " = as.vector(f_in[,n_col])", indent);
		Divers.ecrire(buf, "n_col = n_col+1 ", indent);
	}
	
	private void lireTabSql(Programme prog, StringBuffer buf, int indent, Argument arg) {
		Divers.ecrire(buf, arg.nom + " = as.vector(f_in[,n_col])", indent);
		Divers.ecrire(buf, "n_col = n_col+1 ", indent);
	}
	
	private void lireMatSql(Programme prog, StringBuffer buf, int indent, Argument arg) {
		Divers.ecrire(buf, arg.nom + " = as.matrix(f_in[,seq(n_col,n_col+" + prog.getDim(2, arg) +")])", indent);
		Divers.ecrire(buf, "n_col = n_col+" + prog.getDim(2, arg), indent);
	}
	
	private void lireTabClasseSql(Programme prog, StringBuffer buf, int indent, Argument arg) {
	}
	
	private void lireClasseSql(Programme prog, StringBuffer buf, int indent, Argument arg) {
		Classe cl = (Classe) arg.getClasse(prog);
		for(Iterator<ModeleVariable> iter=cl.proprietes.iterator(); iter.hasNext(); ) {
			Variable prop = (Variable) iter.next();
			if (prop.isOut()) continue;
			Argument arg1 = new Argument(arg.nom+"."+prop.nom, prop.type, null);
			lireSql(prog, buf, indent, arg1);
		}	
	}
	
// -------------------------------
// ecriture d'arguments (sql)
// -------------------------------
	
	private void ecrireSql(Programme prog, StringBuffer buf, int indent, Argument arg) {
		if ( arg.isSimple() ) {
			ecrireSimpleSql(prog,buf,indent,arg);
		}
		if ( arg.isTabSimple()) {
			ecrireTabSql(prog,buf,indent,arg);
		}
		if ( arg.isTabClasse(prog)) {
			ecrireTabClasseSql(prog, buf, indent, arg);
		}
		if ( arg.isMatSimple() ) {
			ecrireMatSql(prog,buf,indent,arg);
		}
		if ( arg.isEnregistrement(prog) || arg.isClasse(prog) ) {
			ecrireClasseSql(prog, buf, indent, arg);
		}
	}	
	
	private void ecrireSimpleSql(Programme prog, StringBuffer buf, int indent, Argument arg) {
		if (arg.isEntier()) {
			Divers.ecrire(buf, "stmt.setInt(n_col+1, " + arg.nom + ");", indent);
			Divers.ecrire(buf, "n_col = n_col+1;", indent);
		}
		else if (arg.isReel()) {
			Divers.ecrire(buf, "stmt.setDouble(n_col+1, " + arg.nom + ");", indent);
			Divers.ecrire(buf, "n_col = n_col+1;", indent);	
		}
		else if (arg.isTexte()) {
			Divers.ecrire(buf, "stmt.setString(n_col+1, " + arg.nom + ");", indent);
			Divers.ecrire(buf, "n_col = n_col+1;", indent);
		}
		else if (arg.isBooleen()) {
			Divers.ecrire(buf, "stmt.setBoolean(n_col+1, " + arg.nom + ");", indent);
			Divers.ecrire(buf, "n_col = n_col+1;", indent);
		}
	}
	
	private void ecrireTabSql(Programme prog, StringBuffer buf, int indent, Argument arg) {
	}
	
	private void ecrireMatSql(Programme prog, StringBuffer buf, int indent, Argument arg) {
	}
	
	private void ecrireTabClasseSql(Programme prog, StringBuffer buf, int indent, Argument arg) {
	}
	
	private void ecrireClasseSql(Programme prog, StringBuffer buf, int indent, Argument arg) {
		//Divers.indenter(buf, indent);
		Classe cl = (Classe) arg.getClasse(prog);
		for(Iterator<ModeleVariable> iter=cl.proprietes.iterator(); iter.hasNext(); ) {
			Variable prop = (Variable) iter.next();
			if (prop.isOut()) continue;
			Argument arg1 = new Argument(arg.nom+"."+prop.nom, prop.type, null);
			ecrireSql(prog, buf, indent, arg1);
		}
	}
	
}
