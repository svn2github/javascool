/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2013.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.langages.javascool;

import java.util.Iterator;

import org.javascool.proglets.plurialgo.divers.Divers;

/**
 * Cette classe permet de traduire (approximativement) en Javascool une instruction
 * de lecture (select ...) ou d'écriture (insert into...) dans une base de données.
*/
public class Sql {
	
	Instruction instr_pere;
	Argument arg_sql;
	String n_lig;
	
	public Sql(Instruction pere) {
		this.instr_pere = pere;
		arg_sql = (Argument) pere.getFichier();
	}
	
	/**
	 * Traduction d'une instruction de lecture.
	 * @param prog
	 * @param buf
	 * @param indent
	 */
	public void lireSql(Programme prog, StringBuffer buf, int indent) {
		instr_pere.addVariable(new Variable("n_lig","ENTIER"));
		instr_pere.addVariable(new Variable("n_col","ENTIER"));
		Divers.ecrire(buf, "n_lig=0;", indent);
		Divers.ecrire(buf, "try {", indent);
		Divers.ecrire(buf, "Class.forName('sun.jdbc.odbc.JdbcOdbcDriver'); ", indent+1);
		Divers.ecrire(buf, "Connection dbCon = DriverManager.getConnection('jdbc:odbc:mabase'); ", indent+1);
		Divers.ecrire(buf, "Statement stmt = dbCon.createStatement();", indent+1);
		Divers.ecrire(buf, "ResultSet rs = stmt.executeQuery('select * from mabase');", indent+1);
		Divers.ecrire(buf, "while (rs.next()) {", indent+1);
		Divers.ecrire(buf, "n_col=0;", indent+2);
		for (Iterator<org.javascool.proglets.plurialgo.langages.modele.Argument> iter=instr_pere.arguments.iterator(); iter.hasNext();) {
			Argument arg = (Argument) iter.next();
			lireSql(prog, buf, indent+2, arg);
		}
		for (Iterator<org.javascool.proglets.plurialgo.langages.modele.Instruction> iter=arg_sql.instructions.iterator(); iter.hasNext();) {
			Instruction instr = (Instruction) iter.next();
			instr.ecrire(prog, buf, indent+2);
		}
		Divers.ecrire(buf, "n_lig=n_lig+1;", indent+2);
		Divers.ecrire(buf, "}", indent+1);
		Divers.ecrire(buf, "rs.close();", indent+1);
		Divers.ecrire(buf, "stmt.close();", indent+1);
		Divers.ecrire(buf, "dbCon.close();", indent+1);
		Divers.ecrire(buf, "} catch (Exception ex) { System.out.println(ex.toString()); }", indent);
	}	
	
	/**
	 * Traduction d'une instruction d'écriture.
	 * @param prog
	 * @param buf
	 * @param indent
	 */	
	public void ecrireSql(Programme prog, StringBuffer buf, int indent) {
		// recherche du nombre de lignes
		n_lig = null;
		for (Iterator<org.javascool.proglets.plurialgo.langages.modele.Argument> iter=instr_pere.arguments.iterator(); iter.hasNext();) {
			Argument arg = (Argument) iter.next();
			if (arg.isTabSimple() || arg.isMatSimple() || arg.isTabClasse(prog)) {
				n_lig = prog.getDim(1, arg); break;
			}
			else if ( arg.isEnregistrement(prog) || arg.isClasse(prog) ) {
				Classe cl = (Classe) arg.getClasse(prog);
				for(Iterator<org.javascool.proglets.plurialgo.langages.modele.Variable> iter_prop=cl.proprietes.iterator(); iter.hasNext(); ) {
					Variable prop = (Variable) iter_prop.next();
					if (prop.isOut()) continue;
					if ( prop.isMatSimple()) {
						n_lig = prog.getDim(1, arg); break;
					}
				}
			}
		}
		// debut de l'ecriture
		Divers.ecrire(buf, "try {", indent);
		Divers.ecrire(buf, "Class.forName('sun.jdbc.odbc.JdbcOdbcDriver'); ", indent+1);
		Divers.ecrire(buf, "Connection dbCon = DriverManager.getConnection('jdbc:odbc:mabase'); ", indent+1);
		Divers.ecrire(buf, "Statement stmt = dbCon.createStatement();", indent+1);
		int indent_for = indent+1;
		if (n_lig!=null) { 
			instr_pere.addVariable(new Variable("n_lig","ENTIER"));
			Divers.ecrire(buf, "for(n_lig=0; n_lig<" + n_lig + "; n_lig++) {", indent+1);
			indent_for++;
		}
		Divers.ecrire(buf, "String req=" + prog.quote("insert into matable values(") + ";", indent_for);
		for (Iterator<org.javascool.proglets.plurialgo.langages.modele.Argument> iter=instr_pere.arguments.iterator(); iter.hasNext();) {
			Argument arg = (Argument) iter.next();
			ecrireSql(prog, buf, indent_for, arg);
		}
		Divers.ecrire(buf, "req=req.substring(0,req.length()-1);  // on ote la virgule finale", indent_for);
		Divers.ecrire(buf, "req=req" + "+" + prog.quote(")") + ";", indent_for);
		Divers.ecrire(buf, "stmt.executeUpdate(req);", indent_for);
		if (n_lig!=null) { 
			Divers.ecrire(buf, "}", indent+1);
		}
		Divers.ecrire(buf, "stmt.close();", indent+1);
		Divers.ecrire(buf, "dbCon.close();", indent+1);
		Divers.ecrire(buf, "} catch (Exception ex) { System.out.println(ex.toString()); }", indent);
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
		if (arg.isEntier()) {
			Divers.ecrire(buf, arg.nom + " = rs.getInt(n_col+1);", indent);
			Divers.ecrire(buf, "n_col = n_col+1;", indent);
		}
		else if (arg.isReel()) {
			Divers.ecrire(buf, arg.nom + " = rs.getDouble(n_col+1);", indent);
			Divers.ecrire(buf, "n_col = n_col+1;", indent);			
		}
		else if (arg.isTexte()) {
			Divers.ecrire(buf, arg.nom + " = rs.getString(n_col+1);", indent);
			Divers.ecrire(buf, "n_col = n_col+1;", indent);
		}
		else if (arg.isBooleen()) {
			Divers.ecrire(buf, arg.nom + " = rs.getBoolean(n_col+1);", indent);
			Divers.ecrire(buf, "n_col = n_col+1;", indent);
		}
	}
	
	private void lireTabSql(Programme prog, StringBuffer buf, int indent, Argument arg) {
		Argument arg1 = new Argument(arg.nom+"[n_lig]", arg.getTypeOfTab(), null);
		lireSql(prog, buf, indent, arg1);
	}
	
	private void lireMatSql(Programme prog, StringBuffer buf, int indent, Argument arg) {
		instr_pere.addVariable(new Variable("j1","ENTIER"));
		Divers.ecrire(buf, "for(j1=0; j1<" + prog.getDim(2, arg) + "; j1++) {", indent);
		Argument arg1 = new Argument(arg.nom+"[n_lig][j1]", arg.getTypeOfMat(), null);
		lireSql(prog, buf, indent+1, arg1);
		Divers.ecrire(buf, "}", indent);
	}
	
	private void lireTabClasseSql(Programme prog, StringBuffer buf, int indent, Argument arg) {
		Classe cl = (Classe) arg.getClasseOfTab(prog);
		for(Iterator<org.javascool.proglets.plurialgo.langages.modele.Variable> iter=cl.proprietes.iterator(); iter.hasNext(); ) {
			Variable prop = (Variable) iter.next();
			if (prop.isOut()) continue;
			if ( prop.isSimple()) {
				Argument arg1 = new Argument(arg.nom+"."+prop.nom, "TAB_"+prop.type, arg.mode);
				lireSql(prog, buf, indent, arg1);
				Divers.remplacer(buf, arg.nom+"."+prop.nom+"[n_lig]", arg.nom+"[n_lig]"+"."+prop.nom);
			}
			if ( prop.isTabSimple()) {
				Argument arg1 = new Argument(arg.nom+"."+prop.nom, "MAT_"+prop.getTypeOfTab(), arg.mode);
				lireSql(prog, buf, indent, arg1);
				Divers.remplacer(buf, arg.nom+"."+prop.nom+"[n_lig][j1]", arg.nom+"[n_lig]"+"."+prop.nom+"[j1]");
			}
		}
	}
	
	private void lireClasseSql(Programme prog, StringBuffer buf, int indent, Argument arg) {
		Classe cl = (Classe) arg.getClasse(prog);
		for(Iterator<org.javascool.proglets.plurialgo.langages.modele.Variable> iter=cl.proprietes.iterator(); iter.hasNext(); ) {
			Variable prop = (Variable) iter.next();
			if (prop.isOut()) continue;
			if ( prop.isTabSimple()) {
				instr_pere.addVariable(new Variable("j1","ENTIER"));
				Divers.ecrire(buf, "for(j1=0; j1<" + prog.getDim(1, arg) + "; j1++) {", indent);
				Argument arg1 = new Argument(arg.nom+"."+prop.nom+"[j1]", prop.getTypeOfTab(), null);
				lireSql(prog, buf, indent+1, arg1);
				Divers.ecrire(buf, "}", indent);
			}
			else {
				Argument arg1 = new Argument(arg.nom+"."+prop.nom, prop.type, null);
				lireSql(prog, buf, indent, arg1);
			}
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
		if (arg.isTexte()) {
			Divers.ecrire(buf, "req=req" + "+" + prog.quote("@") + "+"  + arg.nom + "+" + prog.quote("@") 
					+ "+" + prog.quote(",") + ";", indent);
		}
		else {
			Divers.ecrire(buf, "req=req" + "+" + arg.nom 
					 + "+" + prog.quote(",") + ";", indent);
		}
	}
	
	private void ecrireTabSql(Programme prog, StringBuffer buf, int indent, Argument arg) {
		Argument arg1 = new Argument(arg.nom+"[n_lig]", arg.getTypeOfTab(), null);
		ecrireSql(prog, buf, indent, arg1);
	}
	
	private void ecrireMatSql(Programme prog, StringBuffer buf, int indent, Argument arg) {
		instr_pere.addVariable(new Variable("j1","ENTIER"));
		Divers.ecrire(buf, "for(j1=0; j1<" + prog.getDim(2, arg) + "; j1++) {", indent);
		Argument arg1 = new Argument(arg.nom+"[n_lig][j1]", arg.getTypeOfMat(), null);
		ecrireSql(prog, buf, indent+1, arg1);
		Divers.ecrire(buf, "}", indent);
	}
	
	private void ecrireTabClasseSql(Programme prog, StringBuffer buf, int indent, Argument arg) {
		Classe cl = (Classe) arg.getClasseOfTab(prog);
		for(Iterator<org.javascool.proglets.plurialgo.langages.modele.Variable> iter=cl.proprietes.iterator(); iter.hasNext(); ) {
			Variable prop = (Variable) iter.next();
			if (prop.isOut()) continue;
			if ( prop.isSimple()) {
				Argument arg1 = new Argument(arg.nom+"."+prop.nom, "TAB_"+prop.type, arg.mode);
				ecrireSql(prog, buf, indent, arg1);
				Divers.remplacer(buf, arg.nom+"."+prop.nom+"[n_lig]", arg.nom+"[n_lig]"+"."+prop.nom);
			}
			if ( prop.isTabSimple()) {
				Argument arg1 = new Argument(arg.nom+"."+prop.nom, "MAT_"+prop.getTypeOfTab(), arg.mode);
				ecrireSql(prog, buf, indent, arg1);
				Divers.remplacer(buf, arg.nom+"."+prop.nom+"[n_lig][j1]", arg.nom+"[n_lig]"+"."+prop.nom+"[j1]");
			}
		}
	}
	
	private void ecrireClasseSql(Programme prog, StringBuffer buf, int indent, Argument arg) {
		Classe cl = (Classe) arg.getClasse(prog);
		for(Iterator<org.javascool.proglets.plurialgo.langages.modele.Variable> iter=cl.proprietes.iterator(); iter.hasNext(); ) {
			Variable prop = (Variable) iter.next();
			if (prop.isOut()) continue;
			if ( prop.isTabSimple()) {
				instr_pere.addVariable(new Variable("j1","ENTIER"));
				Divers.ecrire(buf, "for(j1=0; j1<" + prog.getDim(1, arg) + "; j1++) {", indent);
				Argument arg1 = new Argument(arg.nom+"."+prop.nom+"[j1]", prop.getTypeOfTab(), null);
				ecrireSql(prog, buf, indent+1, arg1);
				Divers.ecrire(buf, "}", indent);
			}
			else {
				Argument arg1 = new Argument(arg.nom+"."+prop.nom, prop.type, null);
				ecrireSql(prog, buf, indent, arg1);
			}
		}
	}
	
}
