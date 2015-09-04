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
 * Cette classe permet de transformer un code Carmetal (Javascript Rhino) en un objet de classe Programme.
 */
public class AnalyseurCarmetal implements iAnalyseur {
	
	private XmlProgramme prog_xml;
	private StringBuffer buf_jvs;
	private StringBuffer buf_xml;
	private String pile[] = new String[50];
	private int i_pile;

	/**
	      Transforme un code Carmetal en un objet de classe Programme.
	      @param txt le code Carmetal à analyser
	      @param ignorerLire ignore les instructions de lire si true
	      @param ignorerEcrire ignore les instructions d'écriture si true
	*/	
	public AnalyseurCarmetal(String txt, boolean ignorerLire, boolean ignorerEcrire, Intermediaire inter) {
		this.nettoyerJvs(txt);
		prog_xml = new XmlProgramme(); 
		XmlProgramme prog_nouveau = new ProgrammeNouveau(inter);
		prog_xml.nom = prog_nouveau.nom;
		prog_xml.variables.addAll(prog_nouveau.variables);
		this.jvsEnXml(ignorerLire, ignorerEcrire);
	}

	/**
	      Retourne l'objet de classe Programme obtenu après analyse du code Carmetal.
	*/		
	public XmlProgramme getProgramme() {
		if (buf_xml.toString().contains("[") && buf_xml.toString().contains("]")) {
			StringBuffer buf = new StringBuffer(buf_xml);
			Divers.remplacer(buf, "]", "+1]");
			Divers.remplacer(buf, "-1+1]", "]");
			return (XmlProgramme) ModeleProgramme.getProgramme(buf.toString(),"xml");
		}
		return prog_xml;	}

	/**
	      Retourne le code Xml obtenu après analyse du code Carmetal.
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
	
	private void nettoyerJvs(String txt) {
		buf_jvs = new StringBuffer();
		buf_jvs.append("\n");
		buf_jvs.append(txt);
		buf_jvs.append("\n");
		// on ote les commentaires (/* */)
		int i,j;
		i = buf_jvs.indexOf("/*");
		while (i>=0) {
			j = buf_jvs.indexOf("*/",i) + 2;
			if (j<0) j=i+2;
			buf_jvs.delete(i, j);
			i = buf_jvs.indexOf("/*");
		}
		// on ote les commentaires (//) et les tabulations de debut de ligne
		Divers.remplacer(buf_jvs, "\t", "");
		StringTokenizer tok = new StringTokenizer(buf_jvs.toString(),"\n\r",false);
		buf_jvs = new StringBuffer();
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
					buf_jvs.deleteCharAt(buf_jvs.length()-1);	// on ôte la fin de ligne
				}
			}
			if (ligne.endsWith(";")) ligne = ligne.substring(0, ligne.length()-1);
			if (ligne.isEmpty()) continue;
			buf_jvs.append(ligne+"\n");
			ligne_prec = ligne;
		}
		Divers.remplacer(buf_jvs, "\\\"", " ");
		Divers.remplacer(buf_jvs, "'", " ");
		Divers.remplacer(buf_jvs, "\"", "'");
	}
	
	private void jvsEnXml(boolean ignorerLire, boolean ignorerEcrire) {
		try {
			i_pile = 0; pile[0] = "";
			//this.initImport();
			this.initClasse();
			XmlOperation cur_oper = null;
			XmlClasse cur_class = null;
			XmlConstructeur cur_constr = null;
			Noeud cur_nd = prog_xml;	// le noeud où seront ajoutées les instructions
			StringTokenizer tok = new StringTokenizer(buf_jvs.toString(),"\n\r",false);
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
					ligne = Divers.remplacer(ligne, "{", "");
					ligne = ligne.trim();
					ligne = Divers.remplacer(ligne, "&&", " ET ");
					ligne = Divers.remplacer(ligne, "||", " OU ");
					si.condition = ligne.trim();				
				}
				else if (this.isSinonSi(ligne)) {
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
					ligne = Divers.remplacer(ligne, "{", "");
					ligne = ligne.trim();
					ligne = Divers.remplacer(ligne, "&&", " ET ");
					ligne = Divers.remplacer(ligne, "||", " OU ");
					tq.condition = ligne.trim();				
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
					int i0 = ligne.indexOf("(") + 1;
					int i1 = ligne.indexOf(";");
					if (i1<0) continue;
					int i2 = ligne.indexOf(";",i1+1);
					if (i2<0) continue;
					int i3 = ligne.lastIndexOf(")");
					if (i3<0) continue;
					// l'indice
					String partie = ligne.substring(i0, i1);
					int i = partie.indexOf("="); if (i<0) continue;
					pour.var = partie.substring(0, i).trim();
					if (pour.var.startsWith("int ")) {
						pour.var = pour.var.substring(3).trim();
					}
					// le debut et la fin
					pour.debut = partie.substring(i+1);
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
					int i = ligne.indexOf("(");
					int j = ligne.lastIndexOf(")");
					String parametres = ligne.substring(i+1, j);
					parametres = Divers.remplacer(parametres, " ", "");
					parametres = Divers.remplacer(parametres, "'+(", "'&");
					parametres = Divers.remplacer(parametres, "'+", "'&");
					parametres = Divers.remplacer(parametres, ")+'", "&'");
					parametres = Divers.remplacer(parametres, "+'", "&'");
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
					else if (!parametres.contains("&")) {
						XmlArgument arg = new XmlArgument();
						arg.nom = ligne.substring(i+1, j).trim();	
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
					cur_oper = null;
				}
				else if (this.isDim(ligne)) {
					XmlVariable var = new XmlVariable();
					int i = ligne.indexOf("=");
					String droite = null;
					if (i>0) {
						droite = ligne.substring(i+1, ligne.length());
						ligne = ligne.substring(0, i);
					}
					trouverType(var, cur_oper, cur_constr);
					var.nom = this.trouverNom(ligne);
					if (var.nom.equals("MAX_TAB")) continue;
					if (cur_oper!=null) {
						cur_oper.variables.add(var);
					}
					else if (cur_constr!=null) {
						cur_constr.variables.add(var);
					}
					else {
						prog_xml.variables.add(var);
					}
					if (droite!=null) {
						if (droite.trim().startsWith("new ")) continue;
						XmlInstruction instr = new XmlInstruction("affectation");
						XmlAffectation aff = new XmlAffectation(); 
						aff.var = var.nom;	aff.expression = droite.trim();
						instr.affectations.add(aff); aff.parent = instr;
						this.getInstructions(cur_nd).add(instr); instr.parent = cur_nd;
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
			prog_xml.ecrireWarning("probleme d'analyse du code Carmetal");
			ex.printStackTrace();
		}
		buf_xml = prog_xml.getXmlBuffer();
		Divers.remplacer(buf_xml, "<==", "<=");
		Divers.remplacer(buf_xml, ">==", ">=");
		Divers.remplacer(buf_xml, "!==", "!=");
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
				trouverType(param,null,null);
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
		StringTokenizer tok = new StringTokenizer(buf_jvs.toString(),"\n\r",false);
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
	}
	
	private String trouverNom(String txt) {
		// de variable (ou propriete), de fonction, de parametre : txt débute par un type
		// de procedure, de classe, d'appel
		int i = 0;
		int j = txt.length();
		String nom = "";
		if (txt.startsWith("function ")) {
			i = 9;
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
		if (!ligne.contains("else")) return false;
		if (!ligne.contains("if")) return false;
		return true;
	}
	
	private boolean isSinon(String ligne) {
		if (!ligne.contains("else")) return false;
		if (ligne.contains("if")) return false;
		return true;
	}	
	
	private boolean isFinSi(String ligne) {
		if (!ligne.equals("}")) return false;
		if (!pile[i_pile].equals("si")) return false;
		return true;
	}	
	
	private boolean isPour(String ligne) {
		if (!ligne.startsWith("for(")) return false;
		return true;
	}
	
	private boolean isFinPour(String ligne) {
		if (!ligne.equals("}")) return false;
		if (!pile[i_pile].equals("pour")) return false;
		return true;
	}	
	
	private boolean isTantque(String ligne) {
		if (ligne.startsWith("while ")) return true;
		if (ligne.startsWith("while(")) return true;
		return false;
	}
	
	private boolean isFinTantQue(String ligne) {
		if (!ligne.equals("}")) return false;
		if (!pile[i_pile].equals("tantque")) return false;
		return true;
	}	
	
	private boolean isLire(String ligne) {
		if (!ligne.contains("=")) return false;
		if (ligne.contains("Input(")) return true;
		return false;
	}	
	
	private boolean isEcrire(String ligne) {
		if (!ligne.contains(")")) return false;
		if (ligne.startsWith("Print(")) return true;
		if (ligne.startsWith("Println(")) return true;
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
		if (droite.startsWith("new ")) return false;
		if (droite.startsWith("{")) return false;
		return true;
	}	
	
	private boolean isProc(String ligne) {
		if (ligne.startsWith("function ")) return true;
		return false;
	}
	
	private boolean isFinProc(String ligne) {
		if (!ligne.equals("}")) return false;
		if (!pile[i_pile].equals("sub")) return false;
		return true;
	}	

	private boolean isRetour(String ligne) {
		if (ligne.startsWith("return ")) return true;
		return false;
	}	
	
	boolean isDim(String ligne) {
		return false;
	}	
	
	boolean isPropriete(String ligne) {
		return false;
	}	
	
	private boolean isClasse(String ligne) {
		if (!ligne.startsWith("class ")) return false;
		return true;
	}
	
	private boolean isFinClasse(String ligne) {
		if (!ligne.equals("}")) return false;
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
		if (!ligne.equals("}")) return false;
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
