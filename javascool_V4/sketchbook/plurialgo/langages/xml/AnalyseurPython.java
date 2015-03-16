/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2013.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.langages.xml;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.javascool.proglets.plurialgo.divers.*;
import org.javascool.proglets.plurialgo.langages.modele.*;


/**
 * Cette classe permet de transformer un code Python en un objet de classe Programme.
 */
public class AnalyseurPython implements iAnalyseur {
	
	private XmlProgramme prog_xml;
	private StringBuffer buf_pyt;
	private StringBuffer buf_xml;
	private String pile[] = new String[50];
	private int i_pile;

	/**
	      Transforme un code Python en un objet de classe Programme.
	      @param txt le code Python à analyser
	      @param ignorerLire ignore les instructions de lire si true
	      @param ignorerEcrire ignore les instructions d'écriture si true
	      @param inter pour récupérer le type des variables du code Python
	*/	
	public AnalyseurPython(String txt, boolean ignorerLire, boolean ignorerEcrire, Intermediaire inter) {
		this.nettoyerPython(txt);
		prog_xml = new XmlProgramme(); 
		XmlProgramme prog_nouveau = new ProgrammeNouveau(inter);
		prog_xml.nom = prog_nouveau.nom;
		prog_xml.variables.addAll(prog_nouveau.variables);
		this.pythonEnXml(ignorerLire, ignorerEcrire);
	}

	/**
	      Retourne l'objet de classe Programme obtenu après analyse du code Python.
	*/		
	public XmlProgramme getProgramme() {
		if (buf_xml.toString().contains("[") && buf_xml.toString().contains("]")) {
			StringBuffer buf = new StringBuffer(buf_xml);
			Divers.remplacer(buf, "]", "+1]");
			Divers.remplacer(buf, "-1+1]", "]");
			return (XmlProgramme) ModeleProgramme.getProgramme(buf.toString(),"xml");
		}
		return prog_xml;
	}

	/**
	      Retourne le code Xml obtenu après analyse du code Javascool.
	*/		
	public StringBuffer getXml() {
		if (buf_xml.toString().contains("[") && buf_xml.toString().contains("]")) {
			StringBuffer buf = new StringBuffer(buf_xml);
			Divers.remplacer(buf, "]", "+1]");
			Divers.remplacer(buf, "-1+1]", "]");
			return buf;
		}
		return buf_xml;
	}
	
	private void nettoyerPython(String txt) {
		buf_pyt = new StringBuffer();
		buf_pyt.append("\n");
		buf_pyt.append(txt);
		buf_pyt.append("\n");
		int i;
		// on ote les commentaires (#) et les tabulations de debut de ligne
		Divers.remplacer(buf_pyt, "\t", "");
		StringTokenizer tok = new StringTokenizer(buf_pyt.toString(),"\n\r",false);
		buf_pyt = new StringBuffer();
		String ligne = "";
		while(tok.hasMoreTokens()) {
			ligne = tok.nextToken();
			if (ligne.isEmpty()) continue;
			i = ligne.indexOf("#");
			if (ligne.startsWith("#end")) {
			}
			else if (i>=1){
				ligne = ligne.substring(0,i-1);
				ligne = ligne.trim();
				if (ligne.isEmpty()) continue;
			}
			buf_pyt.append(ligne+"\n");
		}
		// Divers.remplacer(buf_pyt, "'", " ");	// dangereux car effets de bord
		Divers.remplacer(buf_pyt, "\"", "'");
		Divers.remplacer(buf_pyt, "self.", "this.");
		Divers.remplacer(buf_pyt, "(self ,", "(");
		Divers.remplacer(buf_pyt, "(self,", "(");
		Divers.remplacer(buf_pyt, "(self)", "()");
	}
	
	private void pythonEnXml(boolean ignorerLire, boolean ignorerEcrire) {
		try {
			i_pile = 0; pile[0] = "";
			//this.initImport();
			this.initClasse();
			XmlOperation cur_oper = null;
			XmlClasse cur_class = null;
			XmlConstructeur cur_constr = null;
			Noeud cur_nd = prog_xml;	// le noeud où seront ajoutées les instructions
			StringTokenizer tok = new StringTokenizer(buf_pyt.toString(),"\n\r",false);
			while(tok.hasMoreTokens()) {
				String ligne = tok.nextToken();
				//System.out.println("ligne:"+ligne);
				if (this.isClasse(ligne)) {
					i_pile++; pile[i_pile] = "class"; 
					cur_class = (XmlClasse) prog_xml.getClasse(trouverNom(ligne));
				}
				else if (this.isFinClasse(ligne)) {
					if (i_pile>0) i_pile--;
					cur_class = null;
				}
				else if (this.isConstructeur(ligne)) {
					i_pile++; pile[i_pile] = "constructeur"; 
					cur_constr = new XmlConstructeur(cur_class.nom);
					cur_class.constructeurs.add(cur_constr);
					cur_nd = cur_constr;
					cur_nd.parent = prog_xml;
				}
				else if (this.isFinConstructeur(ligne)) {
					if (i_pile>0) i_pile--;
					cur_nd = cur_nd.parent;
					cur_constr = null;
				}
				else if (this.isAffectation(ligne)) {
					XmlInstruction instr = new XmlInstruction("affectation");
					XmlAffectation aff = new XmlAffectation(); aff.var = ""; aff.expression = "";
					int i = ligne.indexOf("=");
					String gauche = ligne.substring(0, i);
					if (gauche!=null) aff.var = gauche.trim();
					String droite = ligne.substring(i+1, ligne.length());
					if (droite!=null) aff.expression = droite.trim();
					if (aff.isAffTabSimple() || aff.isAffMatSimple()) {
						aff.expression = Divers.remplacer(aff.expression, "[", "{");
						aff.expression = Divers.remplacer(aff.expression, "]", "}");
					}
					instr.affectations.add(aff); aff.parent = instr;
					this.getInstructions(cur_nd).add(instr); instr.parent = cur_nd;
				}
				else if (this.isRetour(ligne)) {
					if (cur_oper==null) continue;
					if (cur_oper.isProcedure()) {
						XmlVariable retour = new XmlVariable();
						retour.mode = "OUT";
						retour.nom = "retour";
						trouverType(retour, cur_oper, cur_constr);
						cur_oper.retours.add(retour);						
					}
					XmlInstruction instr = new XmlInstruction("affectation");
					XmlAffectation aff = new XmlAffectation(); 
					aff.var = cur_oper.getRetour().nom;
					int i = ligne.indexOf(" "); 
					aff.expression = "";
					aff.expression = ligne.substring(i, ligne.length()).trim();
					if (aff.var.equals(aff.expression)) continue;
					InfoTypee info;
					InfoTypeeList liste = new InfoTypeeList();
					liste.addVariables(cur_oper.variables);
					if ((info=liste.getInfo(aff.expression))!=null) {
							cur_oper.getRetour().nom = aff.expression;
							cur_oper.variables.remove(info);
							continue;
					}
					instr.affectations.add(aff); aff.parent = instr;
					this.getInstructions(cur_nd).add(instr); instr.parent = cur_nd;
				}
				else if (this.isSi(ligne)) {
					i_pile++; pile[i_pile] = "si"; 
					XmlInstruction instr = new XmlInstruction("si");
					XmlSi si = new XmlSi(); si.condition = "";
					instr.sis.add(si); si.parent = instr;
					this.getInstructions(cur_nd).add(instr); instr.parent = cur_nd;
					cur_nd = si;
					ligne = ligne.substring(2, ligne.length());
					ligne = Divers.remplacer(ligne, ":", "");
					ligne = ligne.trim();
					ligne = Divers.remplacer(ligne, " and ", " ET ");
					ligne = Divers.remplacer(ligne, " or ", " OU ");
					si.condition = ligne;				
				}
				else if (this.isSinonSi(ligne)) {
					this.ajouterCommentaires(cur_nd);
					XmlInstruction instr = (XmlInstruction) cur_nd.parent;
					XmlSi sinonsi = new XmlSi(); sinonsi.condition = "";
					instr.sis.add(sinonsi); sinonsi.parent = instr;
					cur_nd = sinonsi;
					int i = ligne.indexOf("if");
					ligne = ligne.substring(i+2, ligne.length());
					ligne = Divers.remplacer(ligne, ":", "");
					ligne = ligne.trim();
					ligne = Divers.remplacer(ligne, " and ", " ET ");
					ligne = Divers.remplacer(ligne, " or ", " OU ");
					sinonsi.condition = ligne;
				}
				else if (this.isSinon(ligne)) {
					this.ajouterCommentaires(cur_nd);
					XmlInstruction instr = (XmlInstruction) cur_nd.parent;
					XmlSi sinon = new XmlSi(); sinon.condition = "";
					instr.sis.add(sinon); sinon.parent = instr;
					cur_nd = sinon;
				}
				else if (this.isFinSi(ligne)) {
					if (i_pile>0) i_pile--;
					this.ajouterCommentaires(cur_nd);
					cur_nd = cur_nd.parent; // de type Instruction
					cur_nd = cur_nd.parent;
				}
				else if (this.isTantque(ligne)) {
					i_pile++; pile[i_pile] = "tantque"; 
					XmlInstruction instr = new XmlInstruction("tantque");
					XmlTantQue tq = new XmlTantQue(); tq.condition = "";
					instr.tantques.add(tq); tq.parent = instr;
					this.getInstructions(cur_nd).add(instr); instr.parent = cur_nd;
					cur_nd = tq;
					ligne = ligne.substring(5, ligne.length());
					ligne = Divers.remplacer(ligne, ":", "");
					ligne = ligne.trim();
					ligne = Divers.remplacer(ligne, " and ", " ET ");
					ligne = Divers.remplacer(ligne, " or ", " OU ");
					tq.condition = ligne;				
				}
				else if (this.isFinTantQue(ligne)) {
					if (i_pile>0) i_pile--;
					this.ajouterCommentaires(cur_nd);
					cur_nd = cur_nd.parent; // de type Instruction
					cur_nd = cur_nd.parent;
				}
				else if (this.isPour(ligne)) {
					i_pile++; pile[i_pile] = "pour"; 
					XmlInstruction instr = new XmlInstruction("pour");
					XmlPour pour = new XmlPour(); 
					pour.var = ""; pour.debut = ""; pour.fin = ""; pour.pas = "1";
					instr.pours.add(pour); pour.parent = instr;
					this.getInstructions(cur_nd).add(instr); instr.parent = cur_nd;
					cur_nd = pour;
					int i = ligne.indexOf("in"); if (i<0) continue;
					pour.var = ligne.substring(4, i).trim();
					int i0 = ligne.indexOf("(") + 1;
					int i1 = ligne.indexOf(",");
					int i2 = ligne.lastIndexOf(",");
					int i3 = ligne.lastIndexOf(")");
					if(i1<0){
						pour.debut="0";
						pour.fin=ligne.substring(i0,i3).trim();
					}
					else if(i1==i2){
						pour.debut=ligne.substring(i0,i1).trim();
						pour.fin=ligne.substring(i1+1,i3).trim();
					}
					else {
						pour.debut=ligne.substring(i0,i1).trim();
						pour.fin=ligne.substring(i1+1,i2).trim();
						pour.pas=ligne.substring(i2+1,i3).trim();
					}
					pour.fin = pour.fin + "-1";
				}
				else if (this.isFinPour(ligne)) {
					if (i_pile>0) i_pile--;
					this.ajouterCommentaires(cur_nd);
					cur_nd = cur_nd.parent; // de type Instruction
					cur_nd = cur_nd.parent;
				}
				else if (this.isLire(ligne)) {
					if (ignorerLire && (cur_oper==null)) continue;
					XmlInstruction instr = new XmlInstruction("lire");
					XmlArgument arg = new XmlArgument();
					int i = ligne.indexOf("=");
					arg.nom = ligne.substring(0, i).trim();
					arg.type="REEL"; trouverType(arg, cur_oper, cur_constr);
					instr.arguments.add(arg); arg.parent = instr;
					this.getInstructions(cur_nd).add(instr); instr.parent = cur_nd; 
				}
				else if (this.isEcrire(ligne)) {
					if (ignorerEcrire && (cur_oper==null)) continue;
					XmlInstruction instr = new XmlInstruction("ecrire");
					if (ligne.trim().endsWith(",")) ligne = ligne.substring(0, ligne.lastIndexOf(","));
					int i = ligne.indexOf("print") + 6;
					int j = ligne.length();
					if (ligne.startsWith("print(")) {
						j = ligne.lastIndexOf(")");
					}
					String parametres = ligne.substring(i, j);
					parametres = Divers.remplacer(parametres, " ", "");
					parametres = Divers.remplacer(parametres, "',(", "'&");
					parametres = Divers.remplacer(parametres, "',", "'&");
					parametres = Divers.remplacer(parametres, "),'", "&'");
					parametres = Divers.remplacer(parametres, ",'", "&'");
					if (parametres.trim().isEmpty()) continue;
					StringTokenizer tok1 = new StringTokenizer(parametres,"&",false);
					while(tok1.hasMoreTokens()) {
						String parametre = tok1.nextToken();
						if (parametre==null) continue;
						if (parametre.contains("'")) continue;
						XmlArgument arg = new XmlArgument();
						arg.nom = parametre.trim();	
						arg.type = "EXPR"; trouverType(arg, cur_oper, cur_constr);
						instr.arguments.add(arg); arg.parent = instr;
					}
					if (instr.arguments.size()>0) {
						this.getInstructions(cur_nd).add(instr); instr.parent = cur_nd;
					}
					else if (!parametres.contains("&") && !parametres.contains(":")) {
						XmlArgument arg = new XmlArgument();
						arg.nom = ligne.substring(i, j).trim();	
						arg.type = "EXPR";
						instr.arguments.add(arg); arg.parent = instr;
						this.getInstructions(cur_nd).add(instr); instr.parent = cur_nd;
					}
				}
				else if (this.isProc(ligne)) {
					i_pile++; pile[i_pile] = "sub"; 
					cur_oper = (XmlOperation) prog_xml.getOperation(this.trouverNom(ligne));
					if (cur_oper==null) continue;
					cur_nd = cur_oper;
				}
				else if (this.isFinProc(ligne)) {
					if (i_pile>0) i_pile--;
					cur_nd = cur_nd.parent;
					if (cur_oper!=null  & cur_oper.isFonction()) {
						InfoTypee info;
						InfoTypeeList liste = new InfoTypeeList();
						liste.addVariables(cur_oper.variables);
						if ((info=liste.getInfo(cur_oper.getRetour().nom))!=null) {
								cur_oper.variables.remove(info);
						}
					}
					cur_oper = null;
				}
				else if (this.isAppel(ligne)) {
					String parametre = null;
					String parametres = null;
					String nom = trouverNom(ligne);
					String objet = null;
					if (nom.contains(".")) {
						int i_pt = nom.indexOf(".");
						objet = nom.substring(0, i_pt);
						nom = nom.substring(i_pt+1);
					}
					XmlOperation oper = (XmlOperation) prog_xml.getOperation(nom);
					int i = ligne.indexOf("(");
					if (i>=0) {
						parametres = ligne.substring(i+1, ligne.lastIndexOf(")"));
					}
					XmlInstruction instr = new XmlInstruction(oper.nom);
					if (objet!=null) {
						XmlArgument arg = new XmlArgument(objet, "REEL", null);
						trouverType(arg, cur_oper, cur_constr);
						instr.setObjet(arg);
					}
					this.getInstructions(cur_nd).add(instr); instr.parent = cur_nd;
					for (Iterator<ModeleParametre> iter=oper.parametres.iterator(); iter.hasNext();) {
						XmlParametre param = (XmlParametre) iter.next();
						XmlArgument arg = new XmlArgument(param.nom, param.type, param.mode);
						instr.arguments.add(arg);
						if (parametres==null) continue;
						if (parametres.trim().length()==0) continue;
						while (true) {
							i = parametres.indexOf(",");
							if (i>=0) {
								if (parametre==null) {
									parametre = parametres.substring(0, i).trim();
								}
								else {
									parametre = parametre + "," + parametres.substring(0, i).trim();
								}
								parametres = parametres.substring(i+1, parametres.length());
							}
							else {
								if (parametre==null) {
									parametre = parametres.trim();
								}
								else {
									parametre = parametre + "," + parametres.trim();
								}
								parametres = null;
							}
							if (this.egalOuvrFerm(parametre)) {
								arg.nom = parametre;
								parametre = null;
								break;
							}
							else if (parametres==null) {
								break;
							}
						}
					}
				}
				else if (this.isPrimitive(ligne)) {
					int j = ligne.lastIndexOf(")"); 
					XmlInstruction instr = new XmlInstruction(ligne.substring(0, j+1));
					this.getInstructions(cur_nd).add(instr); instr.parent = cur_nd;
				}
			}
		}
		catch (Exception ex) {
			prog_xml.ecrireWarning("probleme d'analyse du code Python");
			ex.printStackTrace();
		}
		buf_xml = prog_xml.getXmlBuffer();
		Divers.remplacer(buf_xml, "<==", "<=");
		Divers.remplacer(buf_xml, ">==", ">=");
		Divers.remplacer(buf_xml, "!==", "!=");
		Divers.remplacer(buf_xml, "-1+1\"", "\"");
		Divers.remplacer(buf_xml, "-1+1]", "]");
		Divers.remplacer(buf_xml, "-1+1-", "-");
		Divers.remplacer(buf_xml, "+1-1\"", "\"");
		Divers.remplacer(buf_xml, "+1-1]", "]");
		Divers.remplacer(buf_xml, "+1-1+", "+");
	}	

	private void initOperation(String ligne, XmlClasse cur_classe) {
		XmlOperation oper = new XmlOperation();
		oper.nom = trouverNom(ligne);
		int i = ligne.indexOf("(");
		if (i>=0) {
			String parametres = ligne.substring(i+1, ligne.lastIndexOf(")"));
			StringTokenizer tok1 = new StringTokenizer(parametres,",",false);
			while(tok1.hasMoreTokens()) {
				String parametre = tok1.nextToken().trim();
				if (parametre==null) continue;
				XmlParametre param = new XmlParametre();
				param.mode = "IN";
				param.nom = trouverNom(parametre);
				trouverType(param, null, null);
				if (param.nom!=null) {
					oper.parametres.add(param);
				}
			}
		}
		if (cur_classe==null) {
			prog_xml.operations.add(oper);	
			oper.parent = prog_xml;
		}
		else {
			cur_classe.operations.add(oper);	
			oper.parent = cur_classe;
			
		}
	}
	
	private void initClasse() {
		StringTokenizer tok = new StringTokenizer(buf_pyt.toString(),"\n\r",false);
		XmlClasse cur_class = null;
		i_pile = 0; pile[0] = "";
		while(tok.hasMoreTokens()) {
			String ligne = tok.nextToken();
			if (this.isClasse(ligne)) {
				i_pile++; pile[i_pile] = "class"; 
				cur_class = new XmlClasse();
				cur_class.nom = trouverNom(ligne);
				prog_xml.classes.add(cur_class);
				cur_class.parent = prog_xml;
			}
			else if (this.isFinClasse(ligne)) {	
				if (i_pile>0) i_pile--;
				cur_class = null;
			}
			else if (this.isSi(ligne)) {
				i_pile++; pile[i_pile] = "si"; 	
			}
			else if (this.isFinSi(ligne)) {
				if (i_pile>0) i_pile--;
			}
			else if (this.isTantque(ligne)) {
				i_pile++; pile[i_pile] = "tantque"; 
			}
			else if (this.isFinTantQue(ligne)) {
				if (i_pile>0) i_pile--;
			}
			else if (this.isPour(ligne)) {
				i_pile++; pile[i_pile] = "pour"; 
			}
			else if (this.isFinPour(ligne)) {
				if (i_pile>0) i_pile--;
			}
			else if (this.isProc(ligne)) {
				i_pile++; pile[i_pile] = "sub"; 
				initOperation(ligne, cur_class);
			}
			else if (this.isFinProc(ligne)) {
				if (i_pile>0) i_pile--;
			}
			else if (this.isConstructeur(ligne)) {
				i_pile++; pile[i_pile] = "constructeur"; 
			}
			else if (this.isFinConstructeur(ligne)) {
				if (i_pile>0) i_pile--;
			}
		}
	}
	
	private void trouverType(InfoTypee arg, XmlOperation cur_oper, XmlConstructeur cur_constr) { 
		// utilisé pour lire et ecrire
		InfoTypeeList liste = new InfoTypeeList();
		if (cur_oper!=null) {
			liste.addVariables(cur_oper.variables);
		}
		if (cur_constr!=null) {
			liste.addVariables(cur_oper.variables);
		}
		liste.addVariables(prog_xml.variables);
		InfoTypee info;
		String nom = arg.nom;
		if (!nom.contains(".")) {
			if (nom.contains("[")) {
				nom = nom.substring(0, nom.indexOf("["));
				info = liste.getInfo(nom);
				if (info!=null) {
					arg.type = info.type.substring(4, info.type.length());
				}
			}
			else {
				info = liste.getInfo(nom);
				if (info!=null) {
					arg.type = info.type;
				}
			}
		}
		else {
			int i = arg.nom.lastIndexOf(".");
			nom = nom.substring(i+1, nom.length());
			liste = new InfoTypeeList();
			for (Iterator<ModeleClasse> iter1=prog_xml.classes.iterator(); iter1.hasNext();) {
				XmlClasse cl = (XmlClasse) iter1.next();
				liste.addVariables(cl.proprietes);
			}
			if (nom.contains("[")) {
				nom = nom.substring(0, nom.indexOf("["));
				info = liste.getInfo(nom);
				if (info!=null) {
					arg.type = info.type.substring(4, info.type.length());
				}
			}
			else {
				info = liste.getInfo(nom);
				if (info!=null) {
					arg.type = info.type;
				}
			}
		}
	}
	
	private String trouverNom(String txt) {
		// de variable (ou propriete), de fonction, de parametre
		// de procedure, de classe, d'appel
		int i = 0;
		int j = txt.length();
		String nom = "";
		if (txt.startsWith("def ")) {
			i = 4;
			j = txt.indexOf(":");
		}
		else if (txt.startsWith("class ")) {
			i = 6;
			j = txt.indexOf(":");
		}
		if (txt.contains("(")) {
			j = txt.indexOf("(");
		}
		nom = txt.substring(i,j).trim();
		return nom;
	}
	
	private boolean egalOuvrFerm(String txt) {
		int nbPar = 0;
		for(int i=0; i<txt.length(); i++) {
			if (txt.substring(i, i+1).equals("(")) {
				nbPar++;
			}
			else if (txt.substring(i, i+1).equals(")")) {
				nbPar--;
			}
		}
		return (nbPar==0);
	}
	
	private void ajouterCommentaires(Noeud cur_nd) {
		// pour eviter les listes d'instructions vides
		if (this.getInstructions(cur_nd).size()>0) return;
		XmlInstruction instr = new XmlInstruction("// ajouter des instructions");
		this.getInstructions(cur_nd).add(instr); instr.parent = cur_nd;
	}
	
	private ArrayList<ModeleInstruction> getInstructions(Noeud nd) {
		if (nd instanceof XmlProgramme) return ((XmlProgramme) nd).instructions;
		if (nd instanceof XmlConstructeur) return ((XmlConstructeur) nd).instructions;
		if (nd instanceof XmlOperation) return ((XmlOperation) nd).instructions;
		if (nd instanceof XmlSi) return ((XmlSi) nd).instructions;
		if (nd instanceof XmlPour) return ((XmlPour) nd).instructions;
		if (nd instanceof XmlTantQue) return ((XmlTantQue) nd).instructions;
		if (nd==null) return prog_xml.instructions;
		return getInstructions(nd.parent);
	}
	
	private boolean isSi(String ligne) {  
		if (ligne.startsWith("if ")) return true;
		if (ligne.startsWith("if(")) return true;
		return false;
	}
	
	private boolean isSinonSi(String ligne) {
		if (!ligne.startsWith("elif")) return false;
		return true;
	}
	
	private boolean isSinon(String ligne) {
		if (!ligne.startsWith("else")) return false;
		return true;
	}	
	
	private boolean isFinSi(String ligne) {
		if (!ligne.startsWith("#end")) return false;
		if (!pile[i_pile].equals("si")) return false;
		return true;
	}	
	
	private boolean isPour(String ligne) {
		if (!ligne.startsWith("for ")) return false;
		return true;
	}
	
	private boolean isFinPour(String ligne) {
		if (!ligne.startsWith("#end")) return false;
		if (!pile[i_pile].equals("pour")) return false;
		return true;
	}	
	
	private boolean isTantque(String ligne) { 
		if (ligne.startsWith("while ")) return true;
		if (ligne.startsWith("while(")) return true;
		return false;
	}
	
	private boolean isFinTantQue(String ligne) {
		if (!ligne.startsWith("#end")) return false;
		if (!pile[i_pile].equals("tantque")) return false;
		return true;
	}	
	
	private boolean isLire(String ligne) {
		if (!ligne.contains("=")) return false;
		if (ligne.contains("input(")) return true;
		return false;
	}	
	
	private boolean isEcrire(String ligne) {
		if (ligne.startsWith("print ")) return true;
		if (ligne.startsWith("print(")) return true;
		return false;
	}	
	
	private boolean isAffectation(String ligne) {
		if (!ligne.contains("=")) return false;
		if (this.isSi(ligne)) return false;
		if (this.isSinonSi(ligne)) return false;
		if (this.isSinon(ligne)) return false;
		if (this.isPour(ligne)) return false;
		if (this.isTantque(ligne)) return false;
		if (this.isLire(ligne)) return false;
		if (this.isEcrire(ligne)) return false;
		String droite = ligne.substring(ligne.indexOf("=")+1).trim();
		if (droite.startsWith("[") && !droite.endsWith("]")) return false;
		if (droite.startsWith("[") && droite.contains(" for ")) return false;
		return true;
	}	
	
	private boolean isProc(String ligne) {
		if (!ligne.startsWith("def ")) return false;
		return true;
	}
	
	private boolean isFinProc(String ligne) {
		if (!ligne.startsWith("#end")) return false;
		if (!pile[i_pile].equals("sub")) return false;
		//System.out.println("isFinProc:"+ligne);
		return true;
	}	

	private boolean isRetour(String ligne) {
		if (ligne.startsWith("return ")) return true;
		return false;
	}	
	
	private boolean isClasse(String ligne) {
		if (!ligne.startsWith("class ")) return false;
		return true;
	}
	
	private boolean isFinClasse(String ligne) {
		if (!ligne.startsWith("#end")) return false;
		if (!pile[i_pile].equals("class")) return false;
		return true;
	}	
	
	private boolean isConstructeur(String ligne) {
		int i = ligne.indexOf("(");
		if (i<0) return false;
		String nom_cl = ligne.substring(0,i).trim();
		return (prog_xml.getClasse(nom_cl)!=null);
	}
	
	private boolean isFinConstructeur(String ligne) {
		if (!ligne.startsWith("#end")) return false;
		if (!pile[i_pile].equals("constructeur")) return false;
		return true;
	}	
	
	private boolean isAppel(String ligne) {
		if (!ligne.contains("(")) return false;
		if (!ligne.contains(")")) return false;
		String nom = trouverNom(ligne).trim();
		if (prog_xml.getOperation(nom)!=null) return true;
		if (!nom.contains(".")) return false;
		nom = nom.substring(nom.indexOf(".")+1);
		if (prog_xml.getOperation(nom)!=null) return true;
		return false;
	}
	
	private boolean isPrimitive(String ligne) {
		int i = ligne.indexOf("("); 
		if (i<0) return false;
		if (!ligne.trim().endsWith(")")) return false;
		String nom_prim = ligne.substring(0, i).trim();
		if (!Divers.isIdent(nom_prim)) return false;
		return true;	// isPrimitive à tester en dernier pour eliminer si, tantque...
	}

}
