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
	private ModeleProgramme prog_xml;
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
	
	public AnalyseurXml(String txt_xml, ModeleProgramme prog) {
		this.prog_xml = prog;
		this.nettoyer(txt_xml);
		this.analyser();
	}
	/**
	      Retourne l'objet de classe Programme obtenu après analyse du code Xml.
	*/		
	public ModeleProgramme getProgramme() {
		return prog_xml;
	}

	private Object creerObjet(String nom_classe) {
		Object obj = null;
		try {
			String nom_paq = this.getClass().getPackage().getName();
			nom_paq = nom_paq.substring(0, nom_paq.indexOf(".modele"));
			String nom_cl = nom_paq + "." + nom_lang + "." + nom_classe;
			if (nom_lang.equals("xml")) {
				nom_cl = Divers.remplacer(nom_cl, ".xml.", ".xml.Xml");
			}
			//obj = Class.forName(nom_cl).newInstance();
			obj = Class.forName(nom_cl,true,this.getClass().getClassLoader()).newInstance();
			//obj = Thread.currentThread().getContextClassLoader().loadClass(nom_cl).newInstance();
		}
		catch(Exception ex) {
			System.out.println("echec de loadClass()");
			System.out.println(ex.getClass());
			System.out.println(ex.getMessage());
			System.out.println("fin echec de loadClass()");
			//prog_xml.buf_error.append(ex.getClass() + " : " + ex.getMessage() + "\n");
			//return creerObjetSecours(nom_classe);	
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
		prog_xml = (ModeleProgramme) creerObjet("Programme");
		try {
			i_pile = 0; pile[0] = "";
			//this.initImport();
			this.initOperClasse();
			ModeleOperation cur_oper = null;
			ModeleClasse cur_class = null;
			ModeleConstructeur cur_constr = null;
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
					cur_constr = (ModeleConstructeur) creerObjet("Constructeur");
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
					ModeleInstruction instr = (ModeleInstruction) creerObjet("Instruction");
					instr.nom = "affectation";
					ModeleAffectation aff = (ModeleAffectation) creerObjet("Affectation");
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
					ModeleInstruction instr = (ModeleInstruction) creerObjet("Instruction");
					instr.nom = "si";
					this.getInstructions(cur_nd).add(instr); instr.parent = cur_nd;
					cur_nd = instr;
				}
				else if (this.isFinInstructionSi(ligne)) {
					if (i_pile>0) i_pile--;
					cur_nd = cur_nd.parent;
				}
				else if (this.isSi(ligne)) { 
					ModeleInstruction instr = (ModeleInstruction)cur_nd;
					ModeleSi si = (ModeleSi) creerObjet("Si");
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
					ModeleInstruction instr = (ModeleInstruction) creerObjet("Instruction");
					instr.nom = "tantque";
					ModeleTantQue tq = (ModeleTantQue) this.creerObjet("TantQue");
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
					ModeleInstruction instr = (ModeleInstruction) creerObjet("Instruction");
					instr.nom = "pour";
					ModelePour pour = (ModelePour) this.creerObjet("Pour"); 
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
					ModeleInstruction instr = (ModeleInstruction) creerObjet("Instruction");
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
					ModeleInstruction instr = (ModeleInstruction) creerObjet("Instruction");
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
					ModeleInstruction instr = (ModeleInstruction) creerObjet("Instruction");
					attr = lireAttribut(ligne, "nom");
					instr.nom = attr;
					this.getInstructions(cur_nd).add(instr); instr.parent = cur_nd;
				}
				else if (this.isFinCommentaire(ligne)) {
					if (i_pile>0) i_pile--;
				}
				else if (this.isArgument(ligne)) { 
					ModeleInstruction instr = (ModeleInstruction)cur_nd;
					ModeleArgument arg = (ModeleArgument) creerObjet("Argument");
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
					ModeleVariable var = (ModeleVariable) creerObjet("Variable");
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
					ModeleInstruction instr = (ModeleInstruction) creerObjet("Instruction");
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
					if (cur_nd instanceof ModeleInstruction) {
						ModeleArgument retour = (ModeleArgument) creerObjet("Argument");
						retour.nom = ""; retour.type = ""; retour.mode="OUT";
						attr = lireAttribut(ligne, "nom");
						if (attr!=null) retour.nom = attr;
						attr = lireAttribut(ligne, "type");
						if (attr!=null) retour.type = attr;
						ModeleInstruction instr = (ModeleInstruction)cur_nd;
						instr.retours.add(retour); retour.parent = instr;
					}
					else if (cur_oper!=null) {
						ModeleVariable retour = (ModeleVariable) creerObjet("Variable");
						retour.nom = ""; retour.type = ""; retour.mode="OUT";
						attr = lireAttribut(ligne, "nom");
						if (attr!=null) retour.nom = attr;
						attr = lireAttribut(ligne, "type");
						if (attr!=null) retour.type = attr;
						cur_oper.retours.add(retour); retour.parent = cur_oper;
					}
				}
				else if (this.isObjet(ligne)) {
					if (cur_nd instanceof ModeleInstruction) {
						ModeleArgument objet = (ModeleArgument) creerObjet("Argument");
						objet.nom = ""; objet.type = ""; objet.mode="";
						attr = lireAttribut(ligne, "nom");
						if (attr!=null) objet.nom = attr;
						attr = lireAttribut(ligne, "type");
						if (attr!=null) objet.type = attr;
						ModeleInstruction instr = (ModeleInstruction)cur_nd;
						instr.objets.add(objet); objet.parent = instr;
					}
				}
				else if (this.isOption(ligne)) {
					if (cur_nd instanceof ModeleInstruction) {
						ModeleArgument option = (ModeleArgument) creerObjet("Argument");
						option.nom = ""; option.type = ""; option.mode="";
						attr = lireAttribut(ligne, "nom");
						if (attr!=null) option.nom = attr;
						attr = lireAttribut(ligne, "type");
						if (attr!=null) option.type = attr;
						ModeleInstruction instr = (ModeleInstruction)cur_nd;
						instr.options.add(option); option.parent = instr;
					}
				}
				else if (this.isPrimitive(ligne)) {
					i_pile++; pile[i_pile] = "primitive"; 
					ModeleInstruction instr = (ModeleInstruction) creerObjet("Instruction");
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
			ModeleOperation cur_oper = null;
			ModeleClasse cur_class = null;
			StringTokenizer tok = new StringTokenizer(buf_xml.toString(),"\n\r",false);
			String attr = null;
			while(tok.hasMoreTokens()) {
				String ligne = tok.nextToken();
				if (this.isClasse(ligne)) {
					cur_class = (ModeleClasse) creerObjet("Classe");
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
					ModeleVariable var = (ModeleVariable) creerObjet("Variable");
					attr = this.lireAttribut(ligne, "type");
					if (attr!=null) var.type = attr;
					attr = this.lireAttribut(ligne, "nom");
					if (attr!=null) var.nom = attr;
					cur_class.proprietes.add(var);
				}
				else if (this.isOper(ligne)) {
					cur_oper = (ModeleOperation) creerObjet("Operation");
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
					ModeleParametre arg = (ModeleParametre) creerObjet("Parametre");
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
	
	private ArrayList<ModeleInstruction> getInstructions(Noeud nd) {
		if (nd instanceof ModeleProgramme) return ((ModeleProgramme) nd).instructions;
		if (nd instanceof ModeleConstructeur) return ((ModeleConstructeur) nd).instructions;
		if (nd instanceof ModeleOperation) return ((ModeleOperation) nd).instructions;
		if (nd instanceof ModeleSi) return ((ModeleSi) nd).instructions;
		if (nd instanceof ModelePour) return ((ModelePour) nd).instructions;
		if (nd instanceof ModeleTantQue) return ((ModeleTantQue) nd).instructions;
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
