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
 * Cette classe permet de transformer un code Visual Basic en un objet de classe Programme.
 */
public class AnalyseurVb implements iAnalyseur {
	
	private XmlProgramme prog_xml;
	private StringBuffer buf_vb;
	private StringBuffer buf_xml;

	/**
	      Transforme un code Visual Basic en un objet de classe Programme.
	      @param txt le code Visual Basic à analyser
	      @param ignorerLire ignore les instructions de lire si true
	      @param ignorerEcrire ignore les instructions d'écriture si true
	*/		
	public AnalyseurVb(String txt, boolean ignorerLire, boolean ignorerEcrire) {
		this.nettoyerVb(txt);
		this.vbEnXml(ignorerLire, ignorerEcrire);
	}

	/**
	      Retourne l'objet de classe Programme obtenu après analyse du code Visual Basic.
	*/		
	public XmlProgramme getProgramme() {
		return prog_xml;
	}

	/**
	      Retourne le code Xml obtenu après analyse du code Visual Basic.
	*/	
	public StringBuffer getXml() {
		return buf_xml;
	}
	
	private void nettoyerVb(String txt) {
		buf_vb = new StringBuffer();
		buf_vb.append("\n");
		buf_vb.append(txt);
		buf_vb.append("\n");
		Divers.remplacer(buf_vb, "\\\\@import", "@import");
		Divers.remplacer(buf_vb, "'@import", "@import");
		StringTokenizer tok = new StringTokenizer(buf_vb.toString()," \t\n\r'(),\"",true);
		buf_vb = new StringBuffer();
		boolean comment = false;
		int nb_guill = 0;	// pour differencier "'" et commentaire
		while(tok.hasMoreTokens()) {
			String mot = tok.nextToken();
			if (mot.equals("\n") || mot.equals("\r")) {
				buf_vb.append("\n");
				comment = false;
				nb_guill = 0;
				continue;
			}
			if (mot.equals("\t")) {
				continue;
			}
			if (mot.equals("\"")) {
				nb_guill++;
			}
			if (mot.equals("'")) {
				if (nb_guill%2==1) {
					mot = " ";
				}
				else {
					comment = true;
					continue;
				}
			}
			if (comment) {
				continue;
			}
			String mot_maj = mot.toUpperCase();
			if (mot_maj.equals("IF")) {
				buf_vb.append(mot_maj); continue;
			}
			if (mot_maj.equals("THEN")) {
				buf_vb.append(mot_maj); continue;
			}
			if (mot_maj.equals("ELSE")) {
				buf_vb.append(mot_maj); continue;
			}
			if (mot_maj.equals("ELSEIF")) {
				buf_vb.append(mot_maj); continue;
			}
			if (mot_maj.equals("END")) {
				buf_vb.append(mot_maj); continue;
			}
			if (mot_maj.equals("FOR")) {
				buf_vb.append(mot_maj); continue;
			}
			if (mot_maj.equals("TO")) {
				buf_vb.append(mot_maj); continue;
			}
			if (mot_maj.equals("STEP")) {
				buf_vb.append(mot_maj); continue;
			}
			if (mot_maj.equals("NEXT")) {
				buf_vb.append(mot_maj); continue;
			}
			if (mot_maj.equals("WHILE")) {
				buf_vb.append(mot_maj); continue;
			}
			if (mot_maj.equals("WEND")) {
				buf_vb.append(mot_maj); continue;
			}
			if (mot_maj.equals("INPUTBOX")) {
				buf_vb.append(mot_maj); continue;
			}
			if (mot_maj.equals("MSGBOX")) {
				buf_vb.append(mot_maj); continue;
			}
			if (mot_maj.equals("CALL")) {
				buf_vb.append(mot_maj); continue;
			}
			if (mot_maj.equals("SUB")) {
				buf_vb.append(mot_maj); continue;
			}
			if (mot_maj.equals("FUNCTION")) {
				buf_vb.append(mot_maj); continue;
			}
			if (mot_maj.equals("BYREF")) {
				buf_vb.append(mot_maj); continue;
			}
			if (mot_maj.equals("BYVAL")) {
				buf_vb.append(mot_maj); continue;
			}
			if (mot_maj.equals("DIM")) {
				buf_vb.append(mot_maj); continue;
			}
			if (mot_maj.equals("AS")) {
				buf_vb.append(mot_maj); continue;
			}
			if (mot_maj.equals("INTEGER")) {
				buf_vb.append(mot_maj); continue;
			}
			if (mot_maj.equals("DOUBLE")) {
				buf_vb.append(mot_maj); continue;
			}
			if (mot_maj.equals("STRING")) {
				buf_vb.append(mot_maj); continue;
			}
			if (mot_maj.equals("BOOLEAN")) {
				buf_vb.append(mot_maj); continue;
			}
			if (mot_maj.equals("TYPE")) {
				buf_vb.append(mot_maj); continue;
			}
			if (mot_maj.equals("AND")) {
				buf_vb.append(mot_maj); continue;
			}
			if (mot_maj.equals("OR")) {
				buf_vb.append(mot_maj); continue;
			}
			if (mot_maj.equals("CONST")) {
				buf_vb.append(mot_maj); continue;
			}
			buf_vb.append(mot);
		}
		for(int i=0;i<60;i++) {
			Divers.remplacer(buf_vb,"\n ", "\n");
			Divers.remplacer(buf_vb," \n", "\n");
			Divers.remplacer(buf_vb,"\n\n", "\n");
		}
		Divers.remplacer(buf_vb, "\"", "'");
	}
	
	private void vbEnXml(boolean ignorerLire, boolean ignorerEcrire) {
		prog_xml = new XmlProgramme();
		prog_xml.nom = "exemple"; 
		try {
			this.initEnreg();
			this.initOperation();
			XmlOperation cur_oper = null;
			Noeud cur_nd = prog_xml;
			StringTokenizer tok = new StringTokenizer(buf_vb.toString(),"\n\r",false);
			while(tok.hasMoreTokens()) {
				String ligne = tok.nextToken();
				if (this.isImport(ligne)) {					
				}
				else if (this.isAffectation(ligne)) {
					ligne = ajouterCrochet(ligne);
					XmlInstruction instr = new XmlInstruction("affectation");
					XmlAffectation aff = new XmlAffectation(); aff.var = ""; aff.expression = "";
					int i = ligne.indexOf("=");
					String gauche = ligne.substring(0, i);
					if (gauche!=null) aff.var = gauche.trim();
					String droite = ligne.substring(i+1, ligne.length());
					if (droite!=null) aff.expression = droite.trim();
					boolean ajouterInstr = true;
					if (cur_oper!=null) {
						if (aff.var.equals(cur_oper.nom)) {
							InfoTypee info;
							InfoTypeeList liste = new InfoTypeeList();
							liste.addVariables(cur_oper.variables);
							if ((info=liste.getInfo(droite.trim()))!=null) {
								cur_oper.getRetour().nom = droite.trim();
								ajouterInstr = false;
								cur_oper.variables.remove(info);
							}
							else {
								aff.var = cur_oper.getRetour().nom;								
							}
						}
					}
					if (ajouterInstr) {
						instr.affectations.add(aff); aff.parent = instr;
						this.getInstructions(cur_nd).add(instr); instr.parent = cur_nd;
					}
				}
				else if (this.isSi(ligne)) {
					ligne = ajouterCrochet(ligne);
					XmlInstruction instr = new XmlInstruction("si");
					XmlSi si = new XmlSi(); si.condition = "";
					instr.sis.add(si); si.parent = instr;
					this.getInstructions(cur_nd).add(instr); instr.parent = cur_nd;
					cur_nd = si;
					ligne = Divers.remplacer(ligne, "IF", "");
					ligne = Divers.remplacer(ligne, "THEN", "");
					ligne = Divers.remplacer(ligne, "=", "==");
					ligne = Divers.remplacer(ligne, ">==", ">=");
					ligne = Divers.remplacer(ligne, "<==", "<=");
					ligne = Divers.remplacer(ligne, "!==", "!=");
					si.condition = ligne.trim();				
				}
				else if (this.isSinonSi(ligne)) {
					ligne = ajouterCrochet(ligne);
					this.ajouterCommentaires(cur_nd);
					XmlInstruction instr = (XmlInstruction) cur_nd.parent;
					XmlSi sinonsi = new XmlSi(); sinonsi.condition = "";
					instr.sis.add(sinonsi); sinonsi.parent = instr;
					cur_nd = sinonsi;
					ligne = Divers.remplacer(ligne, "ELSE", "");
					ligne = Divers.remplacer(ligne, "IF", "");
					ligne = Divers.remplacer(ligne, "THEN", "");
					ligne = Divers.remplacer(ligne, "=", "==");
					ligne = Divers.remplacer(ligne, ">==", ">=");
					ligne = Divers.remplacer(ligne, "<==", "<=");
					ligne = Divers.remplacer(ligne, "!==", "!=");
					sinonsi.condition = ligne.trim();
				}
				else if (this.isSinon(ligne)) {
					ligne = ajouterCrochet(ligne);
					this.ajouterCommentaires(cur_nd);
					XmlInstruction instr = (XmlInstruction) cur_nd.parent;
					XmlSi sinon = new XmlSi(); sinon.condition = "";
					instr.sis.add(sinon); sinon.parent = instr;
					cur_nd = sinon;
				}
				else if (this.isFinSi(ligne)) {
					this.ajouterCommentaires(cur_nd);
					cur_nd = cur_nd.parent; // de type Instruction
					cur_nd = cur_nd.parent;
				}
				else if (this.isTantque(ligne)) {
					ligne = ajouterCrochet(ligne);
					XmlInstruction instr = new XmlInstruction("tantque");
					XmlTantQue tq = new XmlTantQue(); tq.condition = "";
					instr.tantques.add(tq); tq.parent = instr;
					this.getInstructions(cur_nd).add(instr); instr.parent = cur_nd;
					cur_nd = tq;
					ligne = Divers.remplacer(ligne, "WHILE", "");
					ligne = Divers.remplacer(ligne, "=", "==");
					ligne = Divers.remplacer(ligne, ">==", ">=");
					ligne = Divers.remplacer(ligne, "<==", "<=");
					ligne = Divers.remplacer(ligne, "!==", "!=");
					tq.condition = ligne.trim();				
				}
				else if (this.isFinTantQue(ligne)) {
					this.ajouterCommentaires(cur_nd);
					cur_nd = cur_nd.parent; // de type Instruction
					cur_nd = cur_nd.parent;
				}
				else if (this.isPour(ligne)) {
					ligne = ajouterCrochet(ligne);
					XmlInstruction instr = new XmlInstruction("pour");
					XmlPour pour = new XmlPour(); 
					pour.var = ""; pour.debut = ""; pour.fin = ""; pour.pas = "1";
					instr.pours.add(pour); pour.parent = instr;
					this.getInstructions(cur_nd).add(instr); instr.parent = cur_nd;
					cur_nd = pour;
					ligne = Divers.remplacer(ligne, "FOR", "");
					int i = ligne.indexOf("=");	if (i<0) continue;
					String var = ligne.substring(0, i);
					if (var!=null) pour.var = var.trim();
					ligne = ligne.substring(i+1, ligne.length()); if (ligne==null) continue;
					i = ligne.indexOf("TO"); if (i<0) continue;
					String debut = ligne.substring(0, i);
					if (debut!=null) pour.debut = debut.trim();
					ligne = ligne.substring(i+1+2, ligne.length()); if (ligne==null) continue;
					i = ligne.indexOf("STEP"); 
					if (i<0) {
						pour.fin = ligne.trim();
					}
					else {
						String fin = ligne.substring(0, i);
						if (fin!=null) pour.fin = fin.trim();
						ligne = ligne.substring(i+1+4, ligne.length()); if (ligne==null) continue;
						pour.pas = ligne.trim();
					}
				}
				else if (this.isFinPour(ligne)) {
					this.ajouterCommentaires(cur_nd);
					cur_nd = cur_nd.parent; // de type Instruction
					cur_nd = cur_nd.parent;
				}
				else if (this.isLire(ligne)) {
					ligne = ajouterCrochet(ligne);
					if (ignorerLire && (cur_oper==null)) continue;
					XmlInstruction instr = new XmlInstruction("lire");
					XmlArgument arg = new XmlArgument();
					int i = ligne.indexOf("=");
					arg.nom = ligne.substring(0, i).trim();
					arg.type="REEL"; trouverType(arg, cur_oper);
					instr.arguments.add(arg); arg.parent = instr;
					this.getInstructions(cur_nd).add(instr); instr.parent = cur_nd; 
				}
				else if (this.isEcrire(ligne)) {
					ligne = ajouterCrochet(ligne);
					if (ignorerEcrire && (cur_oper==null)) continue;
					XmlInstruction instr = new XmlInstruction("ecrire");
					int i = ligne.indexOf("(");
					int j = ligne.lastIndexOf(")");
					String parametres = ligne.substring(i+1, j);
					if (parametres.trim().isEmpty()) continue;
					StringTokenizer tok1 = new StringTokenizer(parametres,"&",false);
					while(tok1.hasMoreTokens()) {
						String parametre = tok1.nextToken();
						if (parametre==null) continue;
						if (parametre.contains("'")) continue;
						XmlArgument arg = new XmlArgument();
						arg.nom = parametre.trim();	
						arg.type = "EXPR"; trouverType(arg, cur_oper);
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
					cur_oper = (XmlOperation) prog_xml.getOperation(this.trouverNom(ligne));
					if (cur_oper==null) continue;
					cur_nd = cur_oper;
				}
				else if (this.isFinProc(ligne)) {
					cur_nd = prog_xml;
					cur_oper = null;
				}
				else if (this.isFonct(ligne)) {
					cur_oper = (XmlOperation) prog_xml.getOperation(this.trouverNom(ligne));
					if (cur_oper==null) continue;
					cur_nd = cur_oper;
				}
				else if (this.isFinFonct(ligne)) {
					cur_nd = prog_xml;
					cur_oper = null;
				}
				else if (this.isDim(ligne)) {
					XmlVariable var = new XmlVariable();
					var.nom = this.trouverNom(ligne);
					var.type = this.trouverType(ligne);
					if (cur_oper==null) {
						prog_xml.variables.add(var);
					}
					else {
						cur_oper.variables.add(var);
					}
				}
				else if (this.isAppel(ligne)) {
					ligne = ajouterCrochet(ligne);
					String parametre = null;
					String parametres = null;
					String nom = trouverNom(ligne);
					XmlOperation oper = (XmlOperation) prog_xml.getOperation(nom);
					if (oper==null) {
						ligne = Divers.remplacer(ligne,"CALL ", "" );
						XmlInstruction instr = new XmlInstruction(ligne.trim());
						this.getInstructions(cur_nd).add(instr); instr.parent = cur_nd;
						continue;
					}
					int i = ligne.indexOf("(");
					if (i>=0) {
						parametres = ligne.substring(i+1, ligne.lastIndexOf(")"));
					}
					XmlInstruction instr = new XmlInstruction(oper.nom);
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
			}
		}
		catch (Exception ex) {
			prog_xml.ecrireWarning("probleme d'analyse du code Visual Basic");
		}
		buf_xml = prog_xml.getXmlBuffer();
		Divers.remplacer(buf_xml, "<==", "<=");
		Divers.remplacer(buf_xml, ">==", ">=");
	}
	
	private void initOperation() {
		StringTokenizer tok = new StringTokenizer(buf_vb.toString(),"\n\r",false);
		XmlOperation oper = null;
		while(tok.hasMoreTokens()) {
			String ligne = tok.nextToken();
			if (this.isProcPrinc(ligne)) continue;
			if (this.isProc(ligne) || this.isFonct(ligne)) {
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
						param.type = trouverType(parametre);
						param.nom = trouverNom(parametre);
						if (param.nom!=null) {
							oper.parametres.add(param);
						}
						if (parametre.contains("BYREF")) {
							param.mode = "OUT";			
						}
					}
				}
				prog_xml.operations.add(oper);	
				oper.parent = prog_xml;
			}
			if (this.isFonct(ligne)) {
				XmlVariable retour = new XmlVariable();
				retour.mode = "OUT";
				retour.type = "REEL";
				retour.nom = "retour";
				oper.retours.add(retour);
				if (ligne.lastIndexOf(" AS ") > ligne.lastIndexOf(")")) {
					String droite = ligne.substring(ligne.lastIndexOf(" AS "), ligne.length());
					retour.type = trouverType(droite);
				}
			}
		}
	}
	
	private void initEnreg() {
		StringTokenizer tok = new StringTokenizer(buf_vb.toString(),"\n\r",false);
		XmlClasse enreg = null;
		while(tok.hasMoreTokens()) {
			String ligne = tok.nextToken();
			if (this.isEnreg(ligne)) {
				enreg = new XmlClasse();
				enreg.nom = trouverNom(ligne);
				continue;
			}
			if (enreg==null) {
				continue;
			}
			if (this.isFinEnreg(ligne)) {
				prog_xml.classes.add(enreg);
				enreg = null;
				continue;
			}
			XmlVariable var = new XmlVariable();
			var.nom = trouverNom(ligne);
			var.type = trouverType(ligne);
			enreg.proprietes.add(var);
		}
	}
		
	private String trouverType(String txt) {
		//utilisé pour typage (sauf lire et ecrire)
		String type = "REEL";
		int i = txt.indexOf(" AS ");
		if (i>=0) {
			type = txt.substring(i+4, txt.length()).trim();
			if (type.equals("INTEGER")) {
				type = "ENTIER";
			}
			else if (type.equals("DOUBLE")) {
				type = "REEL";
			}
			else if (type.equals("STRING")) {
				type = "TEXTE";
			}
			else if (type.equals("BOOLEAN")) {
				type = "BOOLEEN";
			}
			else {
				type = type.trim();
			}	
		}
		if (txt.contains("(")) {
			if (txt.contains(",")) {
				type = "MAT_" + type;
			}
			else {
				type = "TAB_" + type;	
			}
		}
		return type;
	}
	
	private void trouverType(XmlArgument arg, XmlOperation cur_oper) { 
		// utilisé pour lire et ecrire
		InfoTypeeList liste = new InfoTypeeList();
		if (cur_oper!=null) {
			liste.addVariables(cur_oper.variables);
			liste.addParametres(cur_oper.parametres);
		}
		else {
			liste.addVariables(prog_xml.variables);
		}
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
	
	private String trouverNom(String ligne) {
		StringTokenizer tok = new StringTokenizer(ligne," (=",true);
		while(tok.hasMoreTokens()) {
			String mot = tok.nextToken();	
			if (mot.equals(" ")) continue;	
			if (mot.equals("SUB")) continue;	
			if (mot.equals("FUNCTION")) continue;	
			if (mot.equals("DIM")) continue;
			if (mot.equals("BYREF")) continue;
			if (mot.equals("BYVAL")) continue;
			if (mot.equals("CALL")) continue;
			if (mot.equals("TYPE")) continue;
			return mot;
		}
		return null;
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
		if (nd instanceof XmlOperation) return ((XmlOperation) nd).instructions;
		if (nd instanceof XmlSi) return ((XmlSi) nd).instructions;
		if (nd instanceof XmlPour) return ((XmlPour) nd).instructions;
		if (nd instanceof XmlTantQue) return ((XmlTantQue) nd).instructions;
		if (nd==null) return prog_xml.instructions;
		return getInstructions(nd.parent);
	}
	
	private boolean isSi(String ligne) {
		if (ligne.contains("ELSE")) return false;
		if (!ligne.startsWith("IF")) return false;
		if (!ligne.contains("THEN")) return false;
		return true;
	}
	
	private boolean isSinonSi(String ligne) {
		if (!ligne.startsWith("ELSE")) return false;
		if (!ligne.contains("IF")) return false;
		return true;
	}
	
	private boolean isSinon(String ligne) {
		if (!ligne.startsWith("ELSE")) return false;
		if (ligne.contains("IF")) return false;
		if (ligne.contains("THEN")) return false;
		return true;
	}	
	
	private boolean isFinSi(String ligne) {
		if (!ligne.startsWith("END")) return false;
		if (!ligne.contains("IF")) return false;
		return true;
	}	
	
	private boolean isPour(String ligne) {
		if (!ligne.startsWith("FOR")) return false;
		if (!ligne.contains("TO")) return false;
		return true;
	}
	
	private boolean isFinPour(String ligne) {
		if (ligne.startsWith("NEXT")) return true;
		return false;
	}	
	
	private boolean isTantque(String ligne) {
		if (!ligne.startsWith("WHILE")) return false;
		return true;
	}
	
	private boolean isFinTantQue(String ligne) {
		if (ligne.startsWith("WEND")) return true;
		return false;
	}	
	
	private boolean isLire(String ligne) {
		if (!ligne.contains("INPUTBOX")) return false;
		return true;
	}	
	
	private boolean isEcrire(String ligne) {
		if (!ligne.contains("MSGBOX")) return false;
		return true;
	}	
	
	private boolean isAffectation(String ligne) {
		if (!ligne.contains("=")) return false;
		if (this.isSi(ligne)) return false;
		if (this.isSinonSi(ligne)) return false;
		if (this.isPour(ligne)) return false;
		if (this.isTantque(ligne)) return false;
		if (this.isLire(ligne)) return false;
		if (this.isEcrire(ligne)) return false;
		if (this.isConst(ligne)) return false;
		return true;
	}	
	
	private boolean isProc(String ligne) {
		if (!ligne.startsWith("SUB ")) return false;
		return true;
	}
	
	private boolean isProcPrinc(String ligne) {
		if (!isProc(ligne)) return false;
		if (!trouverNom(ligne).equals("main")) return false;
		return true;
	}
	
	private boolean isFinProc(String ligne) {
		if (!ligne.startsWith("END")) return false;
		if (!ligne.contains("SUB")) return false;
		return true;
	}	
	
	private boolean isFonct(String ligne) {
		if (!ligne.startsWith("FUNCTION ")) return false;
		return true;
	}
	
	private boolean isFinFonct(String ligne) {
		if (!ligne.startsWith("END")) return false;
		if (!ligne.contains("FUNCTION")) return false;
		return true;
	}	
	
	private boolean isDim(String ligne) {
		if (!ligne.startsWith("DIM ")) return false;
		return true;
	}	
	
	private boolean isEnreg(String ligne) {
		if (!ligne.startsWith("TYPE ")) return false;
		return true;
	}
	
	private boolean isFinEnreg(String ligne) {
		if (!ligne.startsWith("END")) return false;
		if (!ligne.contains("TYPE")) return false;
		return true;
	}	
	
	private boolean isAppel(String ligne) {
		if (ligne.startsWith("CALL ")) return true;
		return false;
	}
	
	private boolean isConst(String ligne) {
		if (ligne.startsWith("CONST ")) return true;
		return false;
	}
	
	private boolean isImport(String ligne) {
		if (ligne.contains("@import")) return true;
		return false;
	}


	// ------------------------------------------	
	// les crochets des tableaux
	// ------------------------------------------ 

	private void ajouterCrochet(StringBuffer buf) {
		for (Iterator<ModeleVariable> iter=prog_xml.variables.iterator(); iter.hasNext();) {
			XmlVariable var = (XmlVariable) iter.next();
			ajouterCrochet(buf, var.nom);
		}
		for (Iterator<ModeleOperation> iter1=prog_xml.operations.iterator(); iter1.hasNext();) {
			XmlOperation oper = (XmlOperation) iter1.next();
			for (Iterator<ModeleVariable> iter=oper.variables.iterator(); iter.hasNext();) {
				XmlVariable var = (XmlVariable) iter.next();
				ajouterCrochet(buf, var.nom);
			}
			for (Iterator<ModeleVariable> iter=oper.retours.iterator(); iter.hasNext();) {
				XmlVariable retour = (XmlVariable) iter.next();
				ajouterCrochet(buf, retour.nom);
			}
			for (Iterator<ModeleParametre> iter=oper.parametres.iterator(); iter.hasNext();) {
				XmlParametre param = (XmlParametre) iter.next();
				ajouterCrochet(buf, param.nom);
			}
		}
		for (Iterator<ModeleClasse> iter1=prog_xml.classes.iterator(); iter1.hasNext();) {
			XmlClasse cl = (XmlClasse) iter1.next();
			for (Iterator<ModeleVariable> iter=cl.proprietes.iterator(); iter.hasNext();) {
				XmlVariable prop = (XmlVariable) iter.next();
				ajouterCrochet(buf, prop.nom);
			}
		}
	}
	
	private void ajouterCrochet(StringBuffer buf, String nom) {
		String ancien = nom + "(";
		int lg_ancien = ancien.length();
		String nouveau = nom + "[";
		String ch;
		buf.insert(0, " "); buf.append(" ");
		for (int i=buf.length()-lg_ancien - 1; i>=1; i--) {
			if (ancien.equals(buf.substring(i, i+lg_ancien))) {
				ch = buf.substring(i-1, i);
				if  ( (ch.compareTo("A")>=0) && (ch.compareTo("Z")<=0) ) continue;
				if  ( (ch.compareTo("a")>=0) && (ch.compareTo("z")<=0) ) continue;
				if  ( (ch.compareTo("0")>=0) && (ch.compareTo("9")<=0) ) continue;
				if  ( ch.equals("_") ) continue;
				if  ( ch.equals("\"") ) continue;
				if  ( ch.equals("'") ) continue;
				buf.delete(i, i+lg_ancien);
				buf.insert(i, nouveau);
			}
		}
		buf.delete(0, 1); buf.delete(buf.length()-1, buf.length());		
	}
	
	private String ajouterCrochet(String ligne) {
		if (!ligne.contains("(")) return ligne;
		String txt, ch;
		StringBuffer buf = new StringBuffer(ligne);
		ajouterCrochet(buf);
		txt = buf.toString();
		if (!txt.contains("[")) return txt;
		String pile = "";
		for (int i=0; i<buf.length(); i++) {
			ch = buf.substring(i, i+1);
			if (ch.equals("(")) {
				pile = pile + ")";
			}
			else if (ch.equals("[")) {
				pile = pile + "]";
			}
			else if (ch.equals(")")) {
				if (pile.endsWith("]")) {
					buf.delete(i, i+1);
					buf.insert(i, pile.substring(pile.length()-1, pile.length()));
				}
				if (pile.length()>0) {
					pile = pile.substring(0, pile.length()-1);
				}
			}
		}
		txt = buf.toString();
		return txt;
	}
}
