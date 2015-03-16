/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2014.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.langages.xml;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;
import org.javascool.proglets.plurialgo.divers.*;
import org.javascool.proglets.plurialgo.langages.modele.*;


/**
 * Cette classe permet de transformer un code Xcas en un objet de classe Programme.
 */
public class AnalyseurXcas implements iAnalyseur {
	
	private XmlProgramme prog_xml;
	private StringBuffer buf_xcas;
	private StringBuffer buf_xml;
	private String pile[] = new String[50];
	private int i_pile;

	/**
	      Transforme un code Xcas en un objet de classe Programme.
	      @param txt le code Xcas à analyser
	      @param ignorerLire ignore les instructions de lire si true
	      @param ignorerEcrire ignore les instructions d'écriture si true
	      @param inter pour récupérer le type des variables du code Xcas
	*/	
	public AnalyseurXcas(String txt, boolean ignorerLire, boolean ignorerEcrire, Intermediaire inter) {
		this.nettoyerXcas(txt);
		prog_xml = new XmlProgramme(); 
		XmlProgramme prog_nouveau = new ProgrammeNouveau(inter);
		prog_xml.nom = prog_nouveau.nom;
		prog_xml.variables.addAll(prog_nouveau.variables);
		this.xcasEnXml(ignorerLire, ignorerEcrire);
	}

	/**
	      Retourne l'objet de classe Programme obtenu après analyse du code Javascool.
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
	
	private void nettoyerXcas(String txt) {
		buf_xcas = new StringBuffer();
		buf_xcas.append("\n");
		buf_xcas.append(txt);
		buf_xcas.append("\n");
		// on ote les commentaires (/* */)
		int i,j;
		i = buf_xcas.indexOf("/*");
		while (i>=0) {
			j = buf_xcas.indexOf("*/",i) + 2;
			if (j<0) j=i+2;
			buf_xcas.delete(i, j);
			i = buf_xcas.indexOf("/*");
		}
		// on ote les commentaires (//) et les tabulations de debut de ligne
		Divers.remplacer(buf_xcas, "\t", "");
		StringTokenizer tok = new StringTokenizer(buf_xcas.toString(),"\n\r",false);
		buf_xcas = new StringBuffer();
		String ligne = "";
		String ligne_prec = "";
		while(tok.hasMoreTokens()) {
			ligne = tok.nextToken();
			if (ligne.isEmpty()) continue;
			i = ligne.indexOf("//");
			if (i==0) continue;
			if (i>=1) ligne = ligne.substring(0,i-1);
			ligne = ligne.trim();
			if (ligne.startsWith("else")) {
				if (ligne_prec.equals("}")) {
					buf_xcas.deleteCharAt(buf_xcas.length()-1);	// on ôte la fin de ligne
				}
			}
			if (ligne.endsWith(";")) ligne = ligne.substring(0, ligne.length()-1);
			if (ligne.isEmpty()) continue;
			buf_xcas.append(ligne+"\n");
			ligne_prec = ligne;
		}
		Divers.remplacer(buf_xcas, "\\\"", " ");
		Divers.remplacer(buf_xcas, "'", " ");
		Divers.remplacer(buf_xcas, "\"", "'");
		Divers.remplacer(buf_xcas, "}:", "}");
	}
	
	private void xcasEnXml(boolean ignorerLire, boolean ignorerEcrire) {
		try {
			i_pile = 0; pile[0] = "";
			this.initOperation();
			XmlOperation cur_oper = null;
			XmlConstructeur cur_constr = null;
			Noeud cur_nd = prog_xml;	// le noeud où seront ajoutées les instructions
			StringTokenizer tok = new StringTokenizer(buf_xcas.toString(),"\n\r",false);
			while(tok.hasMoreTokens()) {
				String ligne = tok.nextToken();
				//System.out.println("ligne@ "+ligne);
				if (this.isAffectation(ligne)) {
					XmlInstruction instr = new XmlInstruction("affectation");
					XmlAffectation aff = new XmlAffectation(); aff.var = ""; aff.expression = "";
					int i = ligne.indexOf(":=");
					String gauche = ligne.substring(0, i);
					if (gauche!=null) aff.var = gauche.trim();
					String droite = ligne.substring(i+2, ligne.length());
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
					//System.out.println("isSi:"+ligne);
					if (ligne.endsWith("{")) {
						i_pile++; pile[i_pile] = "si"; 
					}
					XmlInstruction instr = new XmlInstruction("si");
					XmlSi si = new XmlSi(); si.condition = "";
					instr.sis.add(si); si.parent = instr;
					this.getInstructions(cur_nd).add(instr); instr.parent = cur_nd;
					cur_nd = si;
					ligne = ligne.substring(2, ligne.length());	// if et si ignores
					ligne = Divers.remplacer(ligne, "{", "");
					ligne = Divers.remplacer(ligne, "alors", "");
					ligne = ligne.trim();
					ligne = Divers.remplacer(ligne, "&&", " ET ");
					ligne = Divers.remplacer(ligne, "||", " OU ");
					si.condition = ligne.trim();				
				}
				else if (this.isSinonSi(ligne)) {
					//System.out.println("isSinonSi:"+ligne);
					this.ajouterCommentaires(cur_nd);
					XmlInstruction instr = (XmlInstruction) cur_nd.parent;
					XmlSi sinonsi = new XmlSi(); sinonsi.condition = "";
					instr.sis.add(sinonsi); sinonsi.parent = instr;
					cur_nd = sinonsi;
					int i = ligne.indexOf("if");
					ligne = ligne.substring(i+2, ligne.length());
					ligne = Divers.remplacer(ligne, "{", "");
					ligne = ligne.trim();
					ligne = Divers.remplacer(ligne, "&&", " ET ");
					ligne = Divers.remplacer(ligne, "||", " OU ");
					sinonsi.condition = ligne.trim();
				}
				else if (this.isSinon(ligne)) {
					//System.out.println("isSinon:"+ligne);
					this.ajouterCommentaires(cur_nd);
					XmlInstruction instr = (XmlInstruction) cur_nd.parent;
					XmlSi sinon = new XmlSi(); sinon.condition = "";
					instr.sis.add(sinon); sinon.parent = instr;
					cur_nd = sinon;
				}
				else if (this.isFinSi(ligne)) {
					//System.out.println("isFinSi:"+ligne);
					if (ligne.endsWith("}")) {
						if (i_pile>0) i_pile--;
					}
					this.ajouterCommentaires(cur_nd);
					cur_nd = cur_nd.parent; // de type Instruction
					cur_nd = cur_nd.parent;
				}
				else if (this.isTantque(ligne)) {
					if (ligne.endsWith("{")) {
						i_pile++; pile[i_pile] = "tantque"; 
					}
					XmlInstruction instr = new XmlInstruction("tantque");
					XmlTantQue tq = new XmlTantQue(); tq.condition = "";
					instr.tantques.add(tq); tq.parent = instr;
					this.getInstructions(cur_nd).add(instr); instr.parent = cur_nd;
					cur_nd = tq;
					if (ligne.startsWith("while")) {
						ligne = ligne.substring(5, ligne.length());
						ligne = Divers.remplacer(ligne, "{", "");
					}
					else if (ligne.startsWith("tantque")) {
						ligne = ligne.substring(7, ligne.length());
						ligne = Divers.remplacer(ligne, " faire", "");
					}
					ligne = ligne.trim();
					ligne = Divers.remplacer(ligne, "&&", " ET ");
					ligne = Divers.remplacer(ligne, "||", " OU ");
					tq.condition = ligne.trim();				
				}
				else if (this.isFinTantQue(ligne)) {
					if (ligne.endsWith("}")) {
						if (i_pile>0) i_pile--;
					}
					this.ajouterCommentaires(cur_nd);
					cur_nd = cur_nd.parent; // de type Instruction
					cur_nd = cur_nd.parent;
				}
				else if (this.isPour(ligne)) {
					if (ligne.endsWith("{")) {
						i_pile++; pile[i_pile] = "pour"; 
					}
					XmlInstruction instr = new XmlInstruction("pour");
					XmlPour pour = new XmlPour(); 
					pour.var = ""; pour.debut = ""; pour.fin = ""; pour.pas = "1";
					instr.pours.add(pour); pour.parent = instr;
					this.getInstructions(cur_nd).add(instr); instr.parent = cur_nd;
					cur_nd = pour;
					if (ligne.startsWith("for")) {
						int i0 = ligne.indexOf("(") + 1;
						int i1 = ligne.indexOf(";"); if (i1<0) continue;
						int i2 = ligne.indexOf(";",i1+1); if (i2<0) continue;
						int i3 = ligne.lastIndexOf(")"); if (i3<0) continue;
						// l'indice
						String partie = ligne.substring(i0, i1);
						int i = partie.indexOf(":="); if (i<0) continue;
						pour.var = partie.substring(0, i).trim();
						// le debut et la fin
						pour.debut = partie.substring(i+2);
						partie = ligne.substring(i1, i2);
						if (partie.contains("<=")) {
							i = partie.indexOf("<=") + 2;
							pour.fin = partie.substring(i);
						}
						else if (partie.contains("<")) {
							i = partie.indexOf("<") + 1;
							pour.fin = partie.substring(i) + "-1";
						}
						else if (partie.contains(">=")) {
							i = partie.indexOf(">=") + 2;
							pour.fin = partie.substring(i);
							pour.pas = "-1";
						}
						else if (partie.contains(">")) {
							i = partie.indexOf(">") + 1;
							pour.fin = partie.substring(i) + "+1";
							pour.pas = "-1";
						}
						else {
							continue;
						}
						// le pas
						partie = Divers.remplacer(ligne.substring(i2, i3)," ","");
						if (partie.contains("+=")) {
							i = partie.indexOf("+=") + 2;
							pour.pas = partie.substring(i);
						}
						else if (partie.contains("-=")) {
							i = partie.indexOf("-=") + 2;
							pour.pas = "-"+partie.substring(i);
						}
						else if (partie.contains("="+pour.var+"+")) {
							i = partie.indexOf("="+pour.var+"+") + pour.var.length() + 2;
							pour.pas = partie.substring(i);
						}
						else if (partie.contains("="+pour.var+"-")) {
							i = partie.indexOf("="+pour.var+"-") + pour.var.length() + 1;
							pour.pas = partie.substring(i);
						}
					}
					else if (ligne.startsWith("pour")) {
						ligne = Divers.remplacer(ligne, "pour ", "");
						ligne = Divers.remplacer(ligne, " faire", "");
						int i = ligne.indexOf(" de ");	if (i<0) continue;
						String var = ligne.substring(0, i);
						if (var!=null) pour.var = var.trim();
						ligne = ligne.substring(i+4, ligne.length()); if (ligne==null) continue;
						i = ligne.indexOf(" jusque "); if (i<0) continue;
						String debut = ligne.substring(0, i);
						if (debut!=null) pour.debut = debut.trim();
						ligne = ligne.substring(i+8, ligne.length()); if (ligne==null) continue;
						i = ligne.indexOf(" pas "); 
						if (i<0) {
							pour.fin = ligne.trim();
						}
						else {
							String fin = ligne.substring(0, i);
							if (fin!=null) pour.fin = fin.trim();
							ligne = ligne.substring(i+5, ligne.length()); if (ligne==null) continue;
							pour.pas = ligne.trim();
						}						
					}
				}
				else if (this.isFinPour(ligne)) {
					if (ligne.endsWith("}")) {
						if (i_pile>0) i_pile--;
					}
					this.ajouterCommentaires(cur_nd);
					cur_nd = cur_nd.parent; // de type Instruction
					cur_nd = cur_nd.parent;
				}
				else if (this.isLire(ligne)) {
					if (ignorerLire && (cur_oper==null)) continue;
					XmlInstruction instr = new XmlInstruction("lire");
					XmlArgument arg = new XmlArgument();
					int i = ligne.lastIndexOf(",")+1;
					int j = ligne.lastIndexOf(")");
					if (j<0) continue;
					arg.nom = ligne.substring(i, j).trim();
					if (arg.nom.contains("]") && !arg.nom.contains("[") && i>0) {
						i = ligne.substring(0, i-1).lastIndexOf(",")+1;
						arg.nom = ligne.substring(i, j).trim();
					}
					arg.type="REEL"; trouverType(arg, cur_oper, cur_constr);
					instr.arguments.add(arg); arg.parent = instr;
					this.getInstructions(cur_nd).add(instr); instr.parent = cur_nd; 
				}
				else if (this.isEcrire(ligne)) {
					if (ignorerEcrire && (cur_oper==null)) continue;
					XmlInstruction instr = new XmlInstruction("ecrire");
					int i = ligne.indexOf("(")+1;
					int j = ligne.lastIndexOf(")");
					String parametres = ligne.substring(i, j).trim();
					XmlArgument arg = new XmlArgument();
					arg.nom = parametres.trim();	
					arg.type = "EXPR"; trouverType(arg, cur_oper, cur_constr);
					instr.arguments.add(arg); arg.parent = instr;
					if (instr.arguments.size()>0) {
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
					cur_oper = null;
				}
				else if (this.isDim(ligne)) {
					ligne = ligne.substring("local ".length());
					StringTokenizer tok1 = new StringTokenizer(ligne,",",false);
					while(tok1.hasMoreTokens()) {
						String mot = tok1.nextToken().trim();
						XmlVariable var = new XmlVariable();
						int i = mot.indexOf(":=");
						if (i>0) mot = mot.substring(0, i);
						if (!Divers.isIdent(mot)) continue;
						var.nom = this.trouverNom(mot);
						trouverType(var, cur_oper, cur_constr);
						if (cur_oper!=null) {
							cur_oper.variables.add(var);
						}
						else {
							prog_xml.variables.add(var);
						}
					}
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
			prog_xml.ecrireWarning("probleme d'analyse du code Xcas");
			ex.printStackTrace();
		}
		buf_xml = prog_xml.getXmlBuffer();
		Divers.remplacer(buf_xml, "<==", "<=");
		Divers.remplacer(buf_xml, ">==", ">=");
	}	
	
	private void initOperation() {
		StringTokenizer tok = new StringTokenizer(buf_xcas.toString(),"\n\r",false);
		XmlOperation oper = null;
		while(tok.hasMoreTokens()) {
			String ligne = tok.nextToken();
			if (this.isProc(ligne)) {
				oper = new XmlOperation();
				oper.nom = trouverNom(ligne);
				if (oper.nom==null) continue;
				int i = ligne.indexOf("(");
				if (i>=0) {
					String parametres = ligne.substring(i+1, ligne.lastIndexOf(")"));
					StringTokenizer tok1 = new StringTokenizer(parametres,",",false);
					while(tok1.hasMoreTokens()) {
						String parametre = tok1.nextToken();
						if (parametre==null) continue;
						XmlParametre param = new XmlParametre();
						param.mode = "IN";
						param.nom = trouverNom(parametre);
						trouverType(param,null,null);
						if (param.nom!=null) {
							oper.parametres.add(param);
						}
					}
				}
				prog_xml.operations.add(oper);	
				oper.parent = prog_xml;
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
	}
	
	private String trouverNom(String txt) {
		// de variable, de fonction, de parametre 
		// de procedure, d'appel
		int i = 0;
		int j = txt.length();
		String nom = "";
		if (txt.contains("(")) {
			j = txt.indexOf("(");
		}
		if (txt.contains(":=")) {
			if (txt.indexOf(":=")<txt.indexOf("(")) {
				j = txt.indexOf(":=");
			}
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
		if (ligne.startsWith("si ")) return true;
		if (ligne.startsWith("si(")) return true;
		return false;
	}
	
	private boolean isSinonSi(String ligne) {
		if (!ligne.contains("else") && !ligne.contains("sinon")) return false;
		if (ligne.contains("if") || ligne.contains("si")) return false;
		if (!ligne.contains("if") && !ligne.contains("si")) return false;
		return true;
	}
	
	private boolean isSinon(String ligne) {
		if (!ligne.contains("else") && !ligne.contains("sinon")) return false;
		if (!ligne.contains("if") && !ligne.contains("si")) return true;
		return true;
	}	
	
	private boolean isFinSi(String ligne) {
		if (ligne.equals("fsi")) return true;
		if (!ligne.equals("}")) return false;
		if (!pile[i_pile].equals("si")) return false;
		return true;
	}	
	
	private boolean isPour(String ligne) {
		if (ligne.startsWith("pour ")) return true;
		if (ligne.startsWith("for ")) return true;
		if (ligne.startsWith("for(")) return true;
		return false;
	}
	
	private boolean isFinPour(String ligne) {
		if (ligne.equals("fpour")) return true;
		if (!ligne.equals("}")) return false;
		if (!pile[i_pile].equals("pour")) return false;
		return true;
	}	
	
	private boolean isTantque(String ligne) { 
		if (ligne.startsWith("tantque ")) return true;
		if (ligne.startsWith("tantque(")) return true;
		if (ligne.startsWith("while ")) return true;
		if (ligne.startsWith("while(")) return true;
		return false;
	}
	
	private boolean isFinTantQue(String ligne) {
		if (ligne.equals("ftantque")) return true;
		if (!ligne.equals("}")) return false;
		if (!pile[i_pile].equals("tantque")) return false;
		return true;
	}	
	
	private boolean isLire(String ligne) {
		if (!ligne.endsWith(")")) return false;
		if (!ligne.contains(",")) return false;
		if (ligne.startsWith("saisir(")) return true;
		if (ligne.startsWith("saisir_chaine(")) return true;
		if (ligne.startsWith("input(")) return true;
		if (ligne.contains("textinput(")) return true;
		return false;
	}	
	
	private boolean isEcrire(String ligne) {
		if (!ligne.contains(")")) return false;
		if (ligne.startsWith("print(")) return true;
		if (ligne.startsWith("afficher(")) return true;
		return false;
	}	
	
	private boolean isAffectation(String ligne) {
		if (!ligne.contains(":=")) return false;
		if (ligne.contains("{")) return false;
		if (ligne.startsWith("local ")) return false;
		String droite = ligne.substring(ligne.indexOf(":=")+2).trim();
		if (droite.startsWith("matrix(")) return false;
		if (droite.equals("[]")) return false;
		if (droite.equals("[ ]")) return false;
		return true;
	}	
	
	private boolean isProc(String ligne) {
		if (!ligne.contains(":=")) return false;
		if (!ligne.contains("{")) return false;
		int i = ligne.indexOf("(");
		if (i<0) return false;
		String nom_prim = ligne.substring(0, i).trim();
		if (nom_prim.equals("for")) return false;
		if (!Divers.isIdent(nom_prim)) return false;
		return true;
	}
	
	private boolean isFinProc(String ligne) {
		if (!ligne.equals("}")) return false;
		if (!pile[i_pile].equals("sub")) return false;
		return true;
	}	

	private boolean isRetour(String ligne) {
		if (ligne.startsWith("return ")) return true;
		if (ligne.startsWith("retourne ")) return true;
		return false;
	}	
	
	private boolean isDim(String ligne) {
		if (!ligne.startsWith("local ")) return false;
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
		int j = ligne.lastIndexOf(")"); 
		if (j<i) return false;
		String nom_prim = ligne.substring(0, i).trim();
		if (!Divers.isIdent(nom_prim)) return false;
		return true;	// isPrimitive à tester en dernier pour eliminer si, tantque...
	}

}
