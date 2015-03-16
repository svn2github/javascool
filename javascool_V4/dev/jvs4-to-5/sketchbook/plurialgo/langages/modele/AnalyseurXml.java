/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2013.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.langages.modele;

import java.util.ArrayList;
import java.util.StringTokenizer;

import org.javascool.proglets.plurialgo.divers.*;
import org.javascool.proglets.plurialgo.langages.modele.Noeud;


/**
 * Cette classe permet de transformer un code Javascool en un objet de classe Programme.
 */
public class AnalyseurXml {
	
	private String nom_lang;
	private Programme prog_xml;
	private StringBuffer buf_xml;
	private String pile[] = new String[50];
	private int i_pile;

	/**
	      Transforme un code Xml en un objet de classe Programme.
	      @param txt_xml le code Xml à analyser
	      @param nom_lang le langage de traduction
	*/	
	public AnalyseurXml(String txt_xml, String nom_lang) {
		this.nom_lang = nom_lang;
		this.nettoyer(txt_xml);
		this.analyser();
	}
	
	public AnalyseurXml(String txt_xml, Programme prog) {
		this.prog_xml = prog;
		this.nettoyer(txt_xml);
		this.analyser();
	}
	/**
	      Retourne l'objet de classe Programme obtenu après analyse du code Xml.
	*/		
	public Programme getProgramme() {
		return prog_xml;
	}

	// marche uniquement quand on teste dans une fenetre Swing indépendante de l'environnement proglet
//	private Object creerObjet(String nom_classe) {
//		Object obj = null;
//		try {
//			String nom_paq = this.getClass().getPackage().getName();
//			nom_paq = nom_paq.substring(0, nom_paq.indexOf(".modele"));
//			String nom_cl = nom_paq + "." + nom_lang + "." + nom_classe;
//			//obj = Class.forName(nom_cl).newInstance();
//			//obj = Class.forName(nom_cl,true,this.getClass().getClassLoader()).newInstance();
//			obj = Thread.currentThread().getContextClassLoader().loadClass(nom_cl).newInstance();
//		}
//		catch(Exception ex) {
//			System.out.println("echec de loadClass()");
//			System.out.println(ex.getClass());
//			System.out.println(ex.getMessage());
//			System.out.println("fin echec de loadClass()");
//		}
//		return obj;
//	}

	private Object creerObjet(String nom_classe) {
		Object obj = null;
		try {
			if (nom_lang.equals("xml")) {
				if (nom_classe.equals("Affectation")) {
					obj = new org.javascool.proglets.plurialgo.langages.xml.Affectation();
				}
				else if (nom_classe.equals("Argument")) {
					obj = new org.javascool.proglets.plurialgo.langages.xml.Argument();
				}
				else if (nom_classe.equals("Classe")) {
					obj = new org.javascool.proglets.plurialgo.langages.xml.Classe();
				}
				else if (nom_classe.equals("Constructeur")) {
					obj = new org.javascool.proglets.plurialgo.langages.xml.Constructeur();
				}
				else if (nom_classe.equals("Instruction")) {
					obj = new org.javascool.proglets.plurialgo.langages.xml.Instruction();
				}
				else if (nom_classe.equals("Operation")) {
					obj = new org.javascool.proglets.plurialgo.langages.xml.Operation();
				}
				else if (nom_classe.equals("Parametre")) {
					obj = new org.javascool.proglets.plurialgo.langages.xml.Parametre();
				}
				else if (nom_classe.equals("Pour")) {
					obj = new org.javascool.proglets.plurialgo.langages.xml.Pour();
				}
				else if (nom_classe.equals("Programme")) {
					obj = new org.javascool.proglets.plurialgo.langages.xml.Programme();
				}
				else if (nom_classe.equals("Si")) {
					obj = new org.javascool.proglets.plurialgo.langages.xml.Si();
				}
				else if (nom_classe.equals("TantQue")) {
					obj = new org.javascool.proglets.plurialgo.langages.xml.TantQue();
				}
				else if (nom_classe.equals("Variable")) {
					obj = new org.javascool.proglets.plurialgo.langages.xml.Variable();
				}
			}
			else if (nom_lang.equals("java")) {
				if (nom_classe.equals("Affectation")) {
					obj = new org.javascool.proglets.plurialgo.langages.java.Affectation();
				}
				else if (nom_classe.equals("Argument")) {
					obj = new org.javascool.proglets.plurialgo.langages.java.Argument();
				}
				else if (nom_classe.equals("Classe")) {
					obj = new org.javascool.proglets.plurialgo.langages.java.Classe();
				}
				else if (nom_classe.equals("Constructeur")) {
					obj = new org.javascool.proglets.plurialgo.langages.java.Constructeur();
				}
				else if (nom_classe.equals("Instruction")) {
					obj = new org.javascool.proglets.plurialgo.langages.java.Instruction();
				}
				else if (nom_classe.equals("Operation")) {
					obj = new org.javascool.proglets.plurialgo.langages.java.Operation();
				}
				else if (nom_classe.equals("Parametre")) {
					obj = new org.javascool.proglets.plurialgo.langages.java.Parametre();
				}
				else if (nom_classe.equals("Pour")) {
					obj = new org.javascool.proglets.plurialgo.langages.java.Pour();
				}
				else if (nom_classe.equals("Programme")) {
					obj = new org.javascool.proglets.plurialgo.langages.java.Programme();
				}
				else if (nom_classe.equals("Si")) {
					obj = new org.javascool.proglets.plurialgo.langages.java.Si();
				}
				else if (nom_classe.equals("TantQue")) {
					obj = new org.javascool.proglets.plurialgo.langages.java.TantQue();
				}
				else if (nom_classe.equals("Variable")) {
					obj = new org.javascool.proglets.plurialgo.langages.java.Variable();
				}
			}
			else if (nom_lang.equals("javascript")) {
				if (nom_classe.equals("Affectation")) {
					obj = new org.javascool.proglets.plurialgo.langages.javascript.Affectation();
				}
				else if (nom_classe.equals("Argument")) {
					obj = new org.javascool.proglets.plurialgo.langages.javascript.Argument();
				}
				else if (nom_classe.equals("Classe")) {
					obj = new org.javascool.proglets.plurialgo.langages.javascript.Classe();
				}
				else if (nom_classe.equals("Constructeur")) {
					obj = new org.javascool.proglets.plurialgo.langages.javascript.Constructeur();
				}
				else if (nom_classe.equals("Instruction")) {
					obj = new org.javascool.proglets.plurialgo.langages.javascript.Instruction();
				}
				else if (nom_classe.equals("Operation")) {
					obj = new org.javascool.proglets.plurialgo.langages.javascript.Operation();
				}
				else if (nom_classe.equals("Parametre")) {
					obj = new org.javascool.proglets.plurialgo.langages.javascript.Parametre();
				}
				else if (nom_classe.equals("Pour")) {
					obj = new org.javascool.proglets.plurialgo.langages.javascript.Pour();
				}
				else if (nom_classe.equals("Programme")) {
					obj = new org.javascool.proglets.plurialgo.langages.javascript.Programme();
				}
				else if (nom_classe.equals("Si")) {
					obj = new org.javascool.proglets.plurialgo.langages.javascript.Si();
				}
				else if (nom_classe.equals("TantQue")) {
					obj = new org.javascool.proglets.plurialgo.langages.javascript.TantQue();
				}
				else if (nom_classe.equals("Variable")) {
					obj = new org.javascool.proglets.plurialgo.langages.javascript.Variable();
				}
			}
			else if (nom_lang.equals("larp")) {
				if (nom_classe.equals("Affectation")) {
					obj = new org.javascool.proglets.plurialgo.langages.larp.Affectation();
				}
				else if (nom_classe.equals("Argument")) {
					obj = new org.javascool.proglets.plurialgo.langages.larp.Argument();
				}
				else if (nom_classe.equals("Classe")) {
					obj = new org.javascool.proglets.plurialgo.langages.larp.Classe();
				}
				else if (nom_classe.equals("Constructeur")) {
					obj = new org.javascool.proglets.plurialgo.langages.larp.Constructeur();
				}
				else if (nom_classe.equals("Instruction")) {
					obj = new org.javascool.proglets.plurialgo.langages.larp.Instruction();
				}
				else if (nom_classe.equals("Operation")) {
					obj = new org.javascool.proglets.plurialgo.langages.larp.Operation();
				}
				else if (nom_classe.equals("Parametre")) {
					obj = new org.javascool.proglets.plurialgo.langages.larp.Parametre();
				}
				else if (nom_classe.equals("Pour")) {
					obj = new org.javascool.proglets.plurialgo.langages.larp.Pour();
				}
				else if (nom_classe.equals("Programme")) {
					obj = new org.javascool.proglets.plurialgo.langages.larp.Programme();
				}
				else if (nom_classe.equals("Si")) {
					obj = new org.javascool.proglets.plurialgo.langages.larp.Si();
				}
				else if (nom_classe.equals("TantQue")) {
					obj = new org.javascool.proglets.plurialgo.langages.larp.TantQue();
				}
				else if (nom_classe.equals("Variable")) {
					obj = new org.javascool.proglets.plurialgo.langages.larp.Variable();
				}
			}
			else if (nom_lang.equals("vb")) {
				if (nom_classe.equals("Affectation")) {
					obj = new org.javascool.proglets.plurialgo.langages.vb.Affectation();
				}
				else if (nom_classe.equals("Argument")) {
					obj = new org.javascool.proglets.plurialgo.langages.vb.Argument();
				}
				else if (nom_classe.equals("Classe")) {
					obj = new org.javascool.proglets.plurialgo.langages.vb.Classe();
				}
				else if (nom_classe.equals("Constructeur")) {
					obj = new org.javascool.proglets.plurialgo.langages.vb.Constructeur();
				}
				else if (nom_classe.equals("Instruction")) {
					obj = new org.javascool.proglets.plurialgo.langages.vb.Instruction();
				}
				else if (nom_classe.equals("Operation")) {
					obj = new org.javascool.proglets.plurialgo.langages.vb.Operation();
				}
				else if (nom_classe.equals("Parametre")) {
					obj = new org.javascool.proglets.plurialgo.langages.vb.Parametre();
				}
				else if (nom_classe.equals("Pour")) {
					obj = new org.javascool.proglets.plurialgo.langages.vb.Pour();
				}
				else if (nom_classe.equals("Programme")) {
					obj = new org.javascool.proglets.plurialgo.langages.vb.Programme();
				}
				else if (nom_classe.equals("Si")) {
					obj = new org.javascool.proglets.plurialgo.langages.vb.Si();
				}
				else if (nom_classe.equals("TantQue")) {
					obj = new org.javascool.proglets.plurialgo.langages.vb.TantQue();
				}
				else if (nom_classe.equals("Variable")) {
					obj = new org.javascool.proglets.plurialgo.langages.vb.Variable();
				}
			}
			else if (nom_lang.equals("php")) {
				if (nom_classe.equals("Affectation")) {
					obj = new org.javascool.proglets.plurialgo.langages.php.Affectation();
				}
				else if (nom_classe.equals("Argument")) {
					obj = new org.javascool.proglets.plurialgo.langages.php.Argument();
				}
				else if (nom_classe.equals("Classe")) {
					obj = new org.javascool.proglets.plurialgo.langages.php.Classe();
				}
				else if (nom_classe.equals("Constructeur")) {
					obj = new org.javascool.proglets.plurialgo.langages.php.Constructeur();
				}
				else if (nom_classe.equals("Instruction")) {
					obj = new org.javascool.proglets.plurialgo.langages.php.Instruction();
				}
				else if (nom_classe.equals("Operation")) {
					obj = new org.javascool.proglets.plurialgo.langages.php.Operation();
				}
				else if (nom_classe.equals("Parametre")) {
					obj = new org.javascool.proglets.plurialgo.langages.php.Parametre();
				}
				else if (nom_classe.equals("Pour")) {
					obj = new org.javascool.proglets.plurialgo.langages.php.Pour();
				}
				else if (nom_classe.equals("Programme")) {
					obj = new org.javascool.proglets.plurialgo.langages.php.Programme();
				}
				else if (nom_classe.equals("Si")) {
					obj = new org.javascool.proglets.plurialgo.langages.php.Si();
				}
				else if (nom_classe.equals("TantQue")) {
					obj = new org.javascool.proglets.plurialgo.langages.php.TantQue();
				}
				else if (nom_classe.equals("Variable")) {
					obj = new org.javascool.proglets.plurialgo.langages.php.Variable();
				}
			}
			else if (nom_lang.equals("python")) {
				if (nom_classe.equals("Affectation")) {
					obj = new org.javascool.proglets.plurialgo.langages.python.Affectation();
				}
				else if (nom_classe.equals("Argument")) {
					obj = new org.javascool.proglets.plurialgo.langages.python.Argument();
				}
				else if (nom_classe.equals("Classe")) {
					obj = new org.javascool.proglets.plurialgo.langages.python.Classe();
				}
				else if (nom_classe.equals("Constructeur")) {
					obj = new org.javascool.proglets.plurialgo.langages.python.Constructeur();
				}
				else if (nom_classe.equals("Instruction")) {
					obj = new org.javascool.proglets.plurialgo.langages.python.Instruction();
				}
				else if (nom_classe.equals("Operation")) {
					obj = new org.javascool.proglets.plurialgo.langages.python.Operation();
				}
				else if (nom_classe.equals("Parametre")) {
					obj = new org.javascool.proglets.plurialgo.langages.python.Parametre();
				}
				else if (nom_classe.equals("Pour")) {
					obj = new org.javascool.proglets.plurialgo.langages.python.Pour();
				}
				else if (nom_classe.equals("Programme")) {
					obj = new org.javascool.proglets.plurialgo.langages.python.Programme();
				}
				else if (nom_classe.equals("Si")) {
					obj = new org.javascool.proglets.plurialgo.langages.python.Si();
				}
				else if (nom_classe.equals("TantQue")) {
					obj = new org.javascool.proglets.plurialgo.langages.python.TantQue();
				}
				else if (nom_classe.equals("Variable")) {
					obj = new org.javascool.proglets.plurialgo.langages.python.Variable();
				}
			}
			else {
				if (nom_classe.equals("Affectation")) {
					obj = new org.javascool.proglets.plurialgo.langages.javascool.Affectation();
				}
				else if (nom_classe.equals("Argument")) {
					obj = new org.javascool.proglets.plurialgo.langages.javascool.Argument();
				}
				else if (nom_classe.equals("Classe")) {
					obj = new org.javascool.proglets.plurialgo.langages.javascool.Classe();
				}
				else if (nom_classe.equals("Constructeur")) {
					obj = new org.javascool.proglets.plurialgo.langages.javascool.Constructeur();
				}
				else if (nom_classe.equals("Instruction")) {
					obj = new org.javascool.proglets.plurialgo.langages.javascool.Instruction();
				}
				else if (nom_classe.equals("Operation")) {
					obj = new org.javascool.proglets.plurialgo.langages.javascool.Operation();
				}
				else if (nom_classe.equals("Parametre")) {
					obj = new org.javascool.proglets.plurialgo.langages.javascool.Parametre();
				}
				else if (nom_classe.equals("Pour")) {
					obj = new org.javascool.proglets.plurialgo.langages.javascool.Pour();
				}
				else if (nom_classe.equals("Programme")) {
					obj = new org.javascool.proglets.plurialgo.langages.javascool.Programme();
				}
				else if (nom_classe.equals("Si")) {
					obj = new org.javascool.proglets.plurialgo.langages.javascool.Si();
				}
				else if (nom_classe.equals("TantQue")) {
					obj = new org.javascool.proglets.plurialgo.langages.javascool.TantQue();
				}
				else if (nom_classe.equals("Variable")) {
					obj = new org.javascool.proglets.plurialgo.langages.javascool.Variable();
				}
			}
		}
		catch(Exception ex) {
			System.out.println("echec de creerObjetBis " + nom_classe);
		}
		return obj;
	}
	
	private String lireAttribut(String ligne, String nom_attr) {
		int i = ligne.indexOf(" " + nom_attr + "=\"");
		if (i<0) return null;
		i = ligne.indexOf("\"", i);
		int j = ligne.indexOf("\"", i+1);
		if (j<0) return null;
		return ligne.substring(i+1, j);
	}
		
	private void nettoyer(String txt_xml) {
		buf_xml = new StringBuffer();
		buf_xml.append(txt_xml);
		Divers.remplacer(buf_xml, "\t", "");
		StringTokenizer tok = new StringTokenizer(buf_xml.toString(),"\n\r",false);
		buf_xml = new StringBuffer();
		while(tok.hasMoreTokens()) {
			String ligne = tok.nextToken();
			if (ligne.isEmpty()) continue;
			buf_xml.append(ligne+"\n");
		}
	}
	
	private void analyser() {
		prog_xml = (Programme) creerObjet("Programme");
		try {
			i_pile = 0; pile[0] = "";
			//this.initImport();
			this.initOperClasse();
			Operation cur_oper = null;
			Classe cur_class = null;
			Constructeur cur_constr = null;
			Noeud cur_nd = null;	// le noeud où seront ajoutées les instructions
			StringTokenizer tok = new StringTokenizer(buf_xml.toString(),"\n\r",false);
			String attr = null;
			while(tok.hasMoreTokens()) {
				String ligne = tok.nextToken();
				//System.out.println("ligne:"+ligne);
				if (this.isProgramme(ligne)) {
					attr = lireAttribut(ligne, "nom");
					if (attr!=null) prog_xml.nom = attr;
					cur_nd = prog_xml;
				}
				else if (this.isFinProgramme(ligne)) {					
				}
				else if (this.isClasse(ligne)) {
					attr = lireAttribut(ligne, "nom");
					cur_class = prog_xml.getClasse(attr);
					if (cur_class==null) continue;
				}
				else if (this.isFinClasse(ligne)) {
					cur_class = null;
				}
				else if (this.isConstructeur(ligne)) {
					cur_constr = (Constructeur) creerObjet("Constructeur");
					attr = lireAttribut(ligne, "nom");
					if (attr!=null) cur_constr.nom = attr;
					if (cur_class!=null) {
						cur_class.constructeurs.add(cur_constr);
						cur_nd = cur_constr;
						cur_nd.parent = prog_xml;
					}
				}
				else if (this.isFinConstructeur(ligne)) {
					cur_nd = cur_nd.parent;
					cur_constr = null;
				}
				else if (this.isAffectation(ligne)) {
					Instruction instr = (Instruction) creerObjet("Instruction");
					instr.nom = "affectation";
					Affectation aff = (Affectation) creerObjet("Affectation");
					aff.var = ""; aff.expression = "";
					attr = lireAttribut(ligne, "var");
					if (attr!=null) aff.var = attr;
					attr = lireAttribut(ligne, "expression");
					if (attr!=null) aff.expression = attr;
					instr.affectations.add(aff); aff.parent = instr;
					this.getInstructions(cur_nd).add(instr); instr.parent = cur_nd;
				}
				else if (this.isInstructionSi(ligne)) {
					i_pile++; pile[i_pile] = "si"; 
					Instruction instr = (Instruction) creerObjet("Instruction");
					instr.nom = "si";
					this.getInstructions(cur_nd).add(instr); instr.parent = cur_nd;
					cur_nd = instr;
				}
				else if (this.isFinInstructionSi(ligne)) {
					if (i_pile>0) i_pile--;
					cur_nd = cur_nd.parent;
				}
				else if (this.isSi(ligne)) { 
					Instruction instr = (Instruction)cur_nd;
					Si si = (Si) creerObjet("Si");
					si.condition = "";
					attr = lireAttribut(ligne, "condition");
					if (attr!=null) si.condition = attr;
					instr.sis.add(si); si.parent = instr;
					cur_nd = si;				
				}
				else if (this.isFinSi(ligne)) {
					//this.ajouterCommentaires(cur_nd);
					cur_nd = cur_nd.parent; // de type Instruction
				}
				else if (this.isTantque(ligne)) {
					Instruction instr = (Instruction) creerObjet("Instruction");
					instr.nom = "tantque";
					TantQue tq = (TantQue) this.creerObjet("TantQue");
					tq.condition = "";
					attr = lireAttribut(ligne, "condition");
					if (attr!=null) tq.condition = attr;
					instr.tantques.add(tq); tq.parent = instr;
					this.getInstructions(cur_nd).add(instr); instr.parent = cur_nd;
					cur_nd = tq;				
				}
				else if (this.isFinTantQue(ligne)) {
					//this.ajouterCommentaires(cur_nd);
					cur_nd = cur_nd.parent; // de type Instruction
					cur_nd = cur_nd.parent;
				}
				else if (this.isPour(ligne)) {
					Instruction instr = (Instruction) creerObjet("Instruction");
					instr.nom = "pour";
					Pour pour = (Pour) this.creerObjet("Pour"); 
					pour.var = ""; pour.debut = ""; pour.fin = ""; pour.pas = "1";
					attr = lireAttribut(ligne, "var");
					if (attr!=null) pour.var = attr;
					attr = lireAttribut(ligne, "debut");
					if (attr!=null) pour.debut = attr;
					attr = lireAttribut(ligne, "fin");
					if (attr!=null) pour.fin = attr;
					attr = lireAttribut(ligne, "pas");
					if (attr!=null) pour.pas = attr;
					instr.pours.add(pour); pour.parent = instr;
					this.getInstructions(cur_nd).add(instr); instr.parent = cur_nd;
					cur_nd = pour;
				}
				else if (this.isFinPour(ligne)) {
					//this.ajouterCommentaires(cur_nd);
					cur_nd = cur_nd.parent; // de type Instruction
					cur_nd = cur_nd.parent;
				}
				else if (this.isLire(ligne)) {
					i_pile++; pile[i_pile] = "lire"; 
					Instruction instr = (Instruction) creerObjet("Instruction");
					instr.nom = "lire";
					this.getInstructions(cur_nd).add(instr); instr.parent = cur_nd;
					cur_nd = instr;
				}
				else if (this.isFinLire(ligne)) {
					if (i_pile>0) i_pile--;
					cur_nd = cur_nd.parent;
				}
				else if (this.isEcrire(ligne)) {
					i_pile++; pile[i_pile] = "ecrire"; 
					Instruction instr = (Instruction) creerObjet("Instruction");
					instr.nom = "ecrire";
					this.getInstructions(cur_nd).add(instr); instr.parent = cur_nd;
					cur_nd = instr;
				}
				else if (this.isFinEcrire(ligne)) {
					if (i_pile>0) i_pile--;
					cur_nd = cur_nd.parent;
				}
				else if (this.isCommentaire(ligne)) {
					i_pile++; pile[i_pile] = "commentaire"; 
					Instruction instr = (Instruction) creerObjet("Instruction");
					attr = lireAttribut(ligne, "nom");
					instr.nom = attr;
					this.getInstructions(cur_nd).add(instr); instr.parent = cur_nd;
				}
				else if (this.isFinCommentaire(ligne)) {
					if (i_pile>0) i_pile--;
				}
				else if (this.isArgument(ligne)) { 
					Instruction instr = (Instruction)cur_nd;
					Argument arg = (Argument) creerObjet("Argument");
					arg.nom = ""; arg.type=""; arg.mode="";
					attr = lireAttribut(ligne, "nom");
					if (attr!=null) arg.nom = attr;
					attr = lireAttribut(ligne, "type");
					if (attr!=null) arg.type = attr;
					attr = lireAttribut(ligne, "mode");
					if (attr!=null) arg.mode = attr;
					instr.arguments.add(arg); arg.parent = instr;
				}
				else if (this.isOper(ligne)) {
					attr = lireAttribut(ligne, "nom");
					cur_oper = prog_xml.getOperation(attr);
					if (cur_oper==null) continue;
					cur_nd = cur_oper;
				}
				else if (this.isFinOper(ligne)) {
					cur_nd = cur_nd.parent;
					cur_oper = null;
				}
				else if (this.isDim(ligne)) {
					Variable var = (Variable) creerObjet("Variable");
					attr = this.lireAttribut(ligne, "type");
					if (attr!=null) var.type = attr;
					attr = this.lireAttribut(ligne, "nom");
					if (attr!=null) var.nom = attr;
					if (cur_oper!=null) {
						cur_oper.variables.add(var);
					}
					else if (cur_constr!=null) {
						cur_constr.variables.add(var);
					}
					else {
						prog_xml.variables.add(var);
					}
				}
				else if (this.isAppel(ligne)) {
					i_pile++; pile[i_pile] = "appel"; 
					Instruction instr = (Instruction) creerObjet("Instruction");
					attr = this.lireAttribut(ligne, "nom");
					if (attr!=null) instr.nom = attr;
					this.getInstructions(cur_nd).add(instr); instr.parent = cur_nd;
					cur_nd = instr;
				}
				else if (this.isFinAppel(ligne)) {
					if (i_pile>0) i_pile--;
					cur_nd = cur_nd.parent;
				}
				else if (this.isRetour(ligne)) {
					if (cur_nd instanceof Instruction) {
						Argument retour = (Argument) creerObjet("Argument");
						retour.nom = ""; retour.type = ""; retour.mode="OUT";
						attr = lireAttribut(ligne, "nom");
						if (attr!=null) retour.nom = attr;
						attr = lireAttribut(ligne, "type");
						if (attr!=null) retour.type = attr;
						Instruction instr = (Instruction)cur_nd;
						instr.retours.add(retour); retour.parent = instr;
					}
					else if (cur_oper!=null) {
						Variable retour = (Variable) creerObjet("Variable");
						retour.nom = ""; retour.type = ""; retour.mode="OUT";
						attr = lireAttribut(ligne, "nom");
						if (attr!=null) retour.nom = attr;
						attr = lireAttribut(ligne, "type");
						if (attr!=null) retour.type = attr;
						cur_oper.retours.add(retour); retour.parent = cur_oper;
					}
				}
				else if (this.isObjet(ligne)) {
					if (cur_nd instanceof Instruction) {
						Argument objet = (Argument) creerObjet("Argument");
						objet.nom = ""; objet.type = ""; objet.mode="";
						attr = lireAttribut(ligne, "nom");
						if (attr!=null) objet.nom = attr;
						attr = lireAttribut(ligne, "type");
						if (attr!=null) objet.type = attr;
						Instruction instr = (Instruction)cur_nd;
						instr.objets.add(objet); objet.parent = instr;
					}
				}
				else if (this.isOption(ligne)) {
					if (cur_nd instanceof Instruction) {
						Argument option = (Argument) creerObjet("Argument");
						option.nom = ""; option.type = ""; option.mode="";
						attr = lireAttribut(ligne, "nom");
						if (attr!=null) option.nom = attr;
						attr = lireAttribut(ligne, "type");
						if (attr!=null) option.type = attr;
						Instruction instr = (Instruction)cur_nd;
						instr.options.add(option); option.parent = instr;
					}
				}
				else if (this.isPrimitive(ligne)) {
					i_pile++; pile[i_pile] = "primitive"; 
					Instruction instr = (Instruction) creerObjet("Instruction");
					attr = lireAttribut(ligne, "nom");
					instr.nom = attr;
					this.getInstructions(cur_nd).add(instr); instr.parent = cur_nd;
				}
				else if (this.isFinPrimitive(ligne)) {
					if (i_pile>0) i_pile--;
				}
			}
		}
		catch (Exception ex) {
			prog_xml.ecrireWarning("probleme d'analyse du code Xml");
			ex.printStackTrace();
		}
	}
	
	private void initOperClasse() {
		try {
			i_pile = 0; pile[0] = "";
			Operation cur_oper = null;
			Classe cur_class = null;
			StringTokenizer tok = new StringTokenizer(buf_xml.toString(),"\n\r",false);
			String attr = null;
			while(tok.hasMoreTokens()) {
				String ligne = tok.nextToken();
				if (this.isClasse(ligne)) {
					cur_class = (Classe) creerObjet("Classe");
					attr = lireAttribut(ligne, "nom");
					if (attr!=null) cur_class.nom = attr;
					prog_xml.classes.add(cur_class);
					cur_class.parent = prog_xml;
				}
				else if (this.isFinClasse(ligne)) {	
					cur_class = null;
				}
				else if (this.isPropriete(ligne)) {
					if (cur_class==null) continue;
					Variable var = (Variable) creerObjet("Variable");
					attr = this.lireAttribut(ligne, "type");
					if (attr!=null) var.type = attr;
					attr = this.lireAttribut(ligne, "nom");
					if (attr!=null) var.nom = attr;
					cur_class.proprietes.add(var);
				}
				else if (this.isOper(ligne)) {
					cur_oper = (Operation) creerObjet("Operation");
					attr = lireAttribut(ligne, "nom");
					if (attr!=null) cur_oper.nom = attr;
					if (cur_class==null) {
						prog_xml.operations.add(cur_oper);	
						cur_oper.parent = prog_xml;
					}
					else {
						cur_class.operations.add(cur_oper);	
						cur_oper.parent = cur_class;
						
					}
				}
				else if (this.isFinOper(ligne)) {
					cur_oper = null;
				}
				else if (this.isParametre(ligne)) { 
					Parametre arg = (Parametre) creerObjet("Parametre");
					arg.nom = ""; arg.type=""; arg.mode="";
					attr = lireAttribut(ligne, "nom");
					if (attr!=null) arg.nom = attr;
					attr = lireAttribut(ligne, "type");
					if (attr!=null) arg.type = attr;
					attr = lireAttribut(ligne, "mode");
					if (attr!=null) arg.mode = attr;
					if (cur_oper!=null) {
						cur_oper.parametres.add(arg);	
						arg.parent = cur_oper;
					}
				}
			}
		}
		catch (Exception ex) {
			prog_xml.ecrireWarning("probleme d'analyse du code Xml");
			ex.printStackTrace();
		}
	}

//	private void ajouterCommentaires(Noeud cur_nd) {
//		// pour eviter les listes d'instructions vides
//		if (this.getInstructions(cur_nd).size()>0) return;
//		Instruction instr = new Instruction("// ajouter des instructions");
//		this.getInstructions(cur_nd).add(instr); instr.parent = cur_nd;
//	}
	
	private ArrayList<Instruction> getInstructions(Noeud nd) {
		if (nd instanceof Programme) return ((Programme) nd).instructions;
		if (nd instanceof Constructeur) return ((Constructeur) nd).instructions;
		if (nd instanceof Operation) return ((Operation) nd).instructions;
		if (nd instanceof Si) return ((Si) nd).instructions;
		if (nd instanceof Pour) return ((Pour) nd).instructions;
		if (nd instanceof TantQue) return ((TantQue) nd).instructions;
		if (nd==null) return prog_xml.instructions;
		return getInstructions(nd.parent);
	}
	
	private boolean isProgramme(String ligne) { 
		if (!ligne.startsWith("<programme ")) return false;
		return true;
	}
	
	private boolean isFinProgramme(String ligne) { 
		if (!ligne.startsWith("</programme>")) return false;
		return true;
	}
	
	private boolean isInstruction(String ligne) { 
		if (!ligne.startsWith("<instruction ")) return false;
		return true;
	}
	
	private boolean isFinInstruction(String ligne) { 
		if (!ligne.startsWith("</instruction>")) return false;
		return true;
	}
	
	private boolean isInstructionSi(String ligne) { 
		if (!this.isInstruction(ligne)) return false;
		String attr = this.lireAttribut(ligne, "nom");
		if ("si".equals(attr)) return true;
		return false;
	}
	
	private boolean isFinInstructionSi(String ligne) { 
		if (!this.isFinInstruction(ligne)) return false;
		if (!pile[i_pile].equals("si")) return false;
		return true;
	}
				
	private boolean isSi(String ligne) { 
		if (!ligne.startsWith("<si ")) return false;
		return true;
	}
	
	private boolean isFinSi(String ligne) {
		if (!ligne.startsWith("</si>")) return false;
		return true;
	}	
	
	private boolean isPour(String ligne) { 
		if (!ligne.startsWith("<pour ")) return false;
		return true;
	}

	private boolean isFinPour(String ligne) {
		if (!ligne.startsWith("</pour>")) return false;
		return true;
	}	
	
	private boolean isTantque(String ligne) {
		if (!ligne.startsWith("<tantque ")) return false;
		return true;
	}
	
	private boolean isFinTantQue(String ligne) {
		if (!ligne.startsWith("</tantque>")) return false;
		return true;
	}	
	
	private boolean isLire(String ligne) {
		if (!this.isInstruction(ligne)) return false;
		String attr = this.lireAttribut(ligne, "nom");
		if ("lire".equals(attr)) return true;
		return false;
	}	
	
	private boolean isFinLire(String ligne) { 
		if (!this.isFinInstruction(ligne)) return false;
		if (!pile[i_pile].equals("lire")) return false;
		return true;
	}
	
	private boolean isEcrire(String ligne) {
		if (!this.isInstruction(ligne)) return false;
		String attr = this.lireAttribut(ligne, "nom");
		if ("ecrire".equals(attr)) return true;
		return false;
	}	
	
	private boolean isFinEcrire(String ligne) { 
		if (!this.isFinInstruction(ligne)) return false;
		if (!pile[i_pile].equals("ecrire")) return false;
		return true;
	}
	
	private boolean isCommentaire(String ligne) {
		if (!this.isInstruction(ligne)) return false;
		String attr = this.lireAttribut(ligne, "nom");
		if (attr==null) return false;
		if (attr.startsWith("//")) return true;
		return false;
	}	
	
	private boolean isFinCommentaire(String ligne) { 
		if (!this.isFinInstruction(ligne)) return false;
		if (!pile[i_pile].equals("commentaire")) return false;
		return true;
	}
	
	private boolean isPrimitive(String ligne) {
		if (!this.isInstruction(ligne)) return false;
		String attr = this.lireAttribut(ligne, "nom");
		if (attr==null) return false;
		if (attr.startsWith("//")) return false;
		if (attr.contains("////")) return true;
		int i = attr.indexOf("("); 
		if (i<2) return false;	// au moins 1 caractère pour le nom
		int j = attr.lastIndexOf(")"); 
		if (j<i) return false;
		return true;
	}	
	
	private boolean isFinPrimitive(String ligne) { 
		if (!this.isFinInstruction(ligne)) return false;
		if (!pile[i_pile].equals("primitive")) return false;
		return true;
	}
	
	private boolean isArgument(String ligne) {
		if (!ligne.startsWith("<argument ")) return false;
		return true;
	}	
	
	private boolean isParametre(String ligne) {
		if (!ligne.startsWith("<parametre ")) return false;
		return true;
	}	
		
	private boolean isAffectation(String ligne) {
		if (!ligne.startsWith("<affectation ")) return false;
		return true;
	}	
	
	private boolean isOper(String ligne) {
		if (!ligne.startsWith("<operation ")) return false;
		return true;
	}
	
	private boolean isFinOper(String ligne) {
		if (!ligne.startsWith("</operation>")) return false;
		return true;
	}	

	private boolean isRetour(String ligne) {
		if (ligne.startsWith("<return ")) return true;
		return false;
	}	
	
	private boolean isDim(String ligne) {
		if (!ligne.startsWith("<variable ")) return false;
		return true;
	}	
	
	private boolean isObjet(String ligne) {
		if (!ligne.startsWith("<objet ")) return false;
		return true;
	}	
	
	private boolean isOption(String ligne) {
		if (!ligne.startsWith("<option ")) return false;
		return true;
	}	
	
	boolean isPropriete(String ligne) {
		if (!ligne.startsWith("<propriete ")) return false;
		return true;
	}	
	
	boolean isClasse(String ligne) {
		if (ligne.startsWith("<classe ")) return true;
		return false;
	}
	
	boolean isFinClasse(String ligne) {
		if (ligne.startsWith("</classe>")) return true;
		return false;
	}	
	
	boolean isConstructeur(String ligne) {
		if (ligne.startsWith("<constructeur ")) return true;
		return false;
	}
	
	boolean isFinConstructeur(String ligne) {
		if (ligne.startsWith("</constructeur>")) return true;
		return false;
	}	
	
	boolean isAppel(String ligne) {
		if (!this.isInstruction(ligne)) return false;
		String attr = this.lireAttribut(ligne, "nom");
		String nom = attr;
		if (prog_xml.getOperation(nom)!=null) return true;
		return false;
	}
	
	private boolean isFinAppel(String ligne) { 
		if (!this.isFinInstruction(ligne)) return false;
		if (!pile[i_pile].equals("appel")) return false;
		return true;
	}
	
}
