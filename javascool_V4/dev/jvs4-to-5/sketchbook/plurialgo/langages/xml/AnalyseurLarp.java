/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2013.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.langages.xml;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.javascool.proglets.plurialgo.divers.*;
import org.javascool.proglets.plurialgo.langages.modele.InfoTypee;
import org.javascool.proglets.plurialgo.langages.modele.Noeud;


/**
 * Cette classe permet de transformer un code Larp en un objet de classe Programme.
 */
public class AnalyseurLarp implements iAnalyseur {
	
	private Programme prog_xml;
	private StringBuffer buf_larp;
	private StringBuffer buf_xml;

	/**
	      Transforme un code Larp en un objet de classe Programme.
	      @param txt le code Larp à analyser
	      @param ignorerLire ignore les instructions de lire si true
	      @param ignorerEcrire ignore les instructions d'écriture si true
	      @param inter pour récupérer le type des variables du code Larp
	*/		
	public AnalyseurLarp(String txt, boolean ignorerLire, boolean ignorerEcrire, Intermediaire inter) {
		this.nettoyerLarp(txt);
		prog_xml = new Programme(); 
		Programme prog_nouveau = new ProgrammeNouveau(inter);
		prog_xml.nom = prog_nouveau.nom;
		prog_xml.variables.addAll(prog_nouveau.variables);
		this.larpEnXml(ignorerLire, ignorerEcrire);
	}

	/**
	      Transforme un code Larp en un objet de classe Programme.
	      @param txt le code Larp à analyser
	      @param ignorerLire ignore les instructions de lire si true
	      @param ignorerEcrire ignore les instructions d'écriture si true
	*/		
	public AnalyseurLarp(String txt, boolean ignorerLire, boolean ignorerEcrire) {
		this.nettoyerLarp(txt);
		prog_xml = new Programme(); 
		prog_xml.nom = "exemple";
		this.larpEnXml(ignorerLire, ignorerEcrire);
	}

	/**
	      Retourne l'objet de classe Programme obtenu après analyse du code Larp.
	*/		
	public Programme getProgramme() {
		return prog_xml;
	}

	/**
	      Retourne le code Xml obtenu après analyse du code Larp.
	*/	
	public StringBuffer getXml() {
		return buf_xml;
	}
	
	private void nettoyerLarp(String txt) {
		buf_larp = new StringBuffer();
		buf_larp.append("\n");
		buf_larp.append(txt);
		buf_larp.append("\n");
		Divers.remplacer(buf_larp, "\"", "'");
		StringTokenizer tok = new StringTokenizer(buf_larp.toString()," \t\n\r\\(),",true);
		buf_larp = new StringBuffer();
		boolean comment = false;
		while(tok.hasMoreTokens()) {
			String mot = tok.nextToken();
			if (mot.equals("\n") || mot.equals("\r")) {
				buf_larp.append("\n");
				comment = false;
				continue;
			}
			if (mot.equals("\t")) {
				continue;
			}
			if (mot.startsWith("\\")) {
				comment = true;
				continue;
			}
			if (comment) {
				continue;
			}
			String mot_maj = mot.toUpperCase();
			if (mot_maj.equals("SI")) {
				buf_larp.append(mot_maj); continue;
			}
			if (mot_maj.equals("ALORS")) {
				buf_larp.append(mot_maj); continue;
			}
			if (mot_maj.equals("SINON")) {
				buf_larp.append(mot_maj); continue;
			}
			if (mot_maj.equals("FINSI")) {
				buf_larp.append(mot_maj); continue;
			}
			if (mot_maj.equals("POUR")) {
				buf_larp.append(mot_maj); continue;
			}
			//if (mot_maj.equals("JUSQU'À") || mot_maj.equals("JUSQU'A")) {
			if (mot_maj.length()==7 && mot_maj.startsWith("JUSQU'")) {
				buf_larp.append("JUSQU'A"); continue;
			}
			//if (mot_maj.equals("INCRÉMENT") || mot_maj.equals("INCREMENT")) {
			if (mot_maj.length()==9 && mot_maj.startsWith("INCR") && mot_maj.endsWith("MENT")) {
				buf_larp.append("INCREMENT"); continue;
			}
			if (mot_maj.equals("FAIRE")) {
				buf_larp.append(mot_maj); continue;
			}
			if (mot_maj.equals("FINPOUR")) {
				buf_larp.append(mot_maj); continue;
			}
			if (mot_maj.equals("TANTQUE")) {
				buf_larp.append(mot_maj); continue;
			}
			if (mot_maj.equals("FINTANTQUE")) {
				buf_larp.append(mot_maj); continue;
			}
			if (mot_maj.equals("LIRE")) {
				buf_larp.append(mot_maj); continue;
			}
			//if (mot_maj.equals("ÉCRIRE") || mot_maj.equals("ECRIRE")) {
			if (mot_maj.length()==6 && mot_maj.endsWith("CRIRE")) {
				buf_larp.append("ECRIRE"); continue;
			}
			//if (mot_maj.equals("REQUÊTE") || mot_maj.equals("REQUETE")) {
			if (mot_maj.length()==7 && mot_maj.startsWith("REQU") && mot_maj.endsWith("TE")) {
				buf_larp.append("REQUETE"); continue;
			}
			//if (mot_maj.equals("DEBUT") || mot_maj.equals("DÉBUT")) {
			if (mot_maj.length()==5 && mot_maj.startsWith("D") && mot_maj.endsWith("BUT")) {
				buf_larp.append("DEBUT"); continue;
			}
			if (mot_maj.equals("FIN")) {
				buf_larp.append(mot_maj); continue;
			}
			//if (mot_maj.equals("EXECUTER") || mot_maj.equals("EXÉCUTER")) {
			if (mot_maj.length()==8 && mot_maj.startsWith("EX") && mot_maj.endsWith("CUTER")) {
				buf_larp.append("EXECUTER"); continue;
			}
			if (mot_maj.equals("ENTRER")) {
				buf_larp.append(mot_maj); continue;
			}
			if (mot_maj.equals("RETOURNER")) {
				buf_larp.append(mot_maj); continue;
			}
			if (mot_maj.equals("REFERENCE")) {
				buf_larp.append(mot_maj); continue;
			}
			if (mot_maj.equals("ET")) {
				buf_larp.append(mot_maj); continue;
			}
			if (mot_maj.equals("OU")) {
				buf_larp.append(mot_maj); continue;
			}
			buf_larp.append(mot);
		}
		for(int i=0;i<60;i++) {
			Divers.remplacer(buf_larp,"\n ", "\n");
			Divers.remplacer(buf_larp," \n", "\n");
			Divers.remplacer(buf_larp,"\n\n", "\n");
		}		
	}
	
	private void larpEnXml(boolean ignorerLire, boolean ignorerEcrire) {
		try {
			//this.initImport();
			this.initOperation();
			Operation cur_oper = null;
			Noeud cur_nd = prog_xml;
			StringTokenizer tok = new StringTokenizer(buf_larp.toString(),"\n\r",false);
			while(tok.hasMoreTokens()) {
				String ligne = tok.nextToken();
				if (this.isImport(ligne)) {
				}
				else if (this.isAffectation(ligne)) {
					Instruction instr = new Instruction("affectation");
					Affectation aff = new Affectation(); aff.var = ""; aff.expression = "";
					instr.affectations.add(aff); aff.parent = instr;
					this.getInstructions(cur_nd).add(instr); instr.parent = cur_nd;
					int i = ligne.indexOf("=");
					String gauche = ligne.substring(0, i);
					if (gauche!=null) aff.var = gauche.trim();
					String droite = ligne.substring(i+1, ligne.length());
					if (droite!=null) aff.expression = droite.trim();
				}
				else if (this.isSi(ligne)) {
					Instruction instr = new Instruction("si");
					Si si = new Si(); si.condition = "";
					instr.sis.add(si); si.parent = instr;
					this.getInstructions(cur_nd).add(instr); instr.parent = cur_nd;
					cur_nd = si;
					ligne = Divers.remplacer(ligne, "SI", "");
					ligne = Divers.remplacer(ligne, "ALORS", "");
					ligne = Divers.remplacer(ligne, "=", "==");
					ligne = Divers.remplacer(ligne, ">==", ">=");
					ligne = Divers.remplacer(ligne, "<==", "<=");
					si.condition = ligne;				
				}
				else if (this.isSinonSi(ligne)) {
					this.ajouterCommentaires(cur_nd);
					Instruction instr = (Instruction) cur_nd.parent;
					Si sinonsi = new Si(); sinonsi.condition = "";
					instr.sis.add(sinonsi); sinonsi.parent = instr;
					cur_nd = sinonsi;
					ligne = Divers.remplacer(ligne, "SINON", "");
					ligne = Divers.remplacer(ligne, "SI", "");
					ligne = Divers.remplacer(ligne, "ALORS", "");
					ligne = Divers.remplacer(ligne, "=", "==");
					ligne = Divers.remplacer(ligne, ">==", ">=");
					ligne = Divers.remplacer(ligne, "<==", "<=");
					sinonsi.condition = ligne;
				}
				else if (this.isSinon(ligne)) {
					this.ajouterCommentaires(cur_nd);
					Instruction instr = (Instruction) cur_nd.parent;
					Si sinon = new Si(); sinon.condition = "";
					instr.sis.add(sinon); sinon.parent = instr;
					cur_nd = sinon;
				}
				else if (this.isFinSi(ligne)) {
					this.ajouterCommentaires(cur_nd);
					cur_nd = cur_nd.parent; // de type Instruction
					cur_nd = cur_nd.parent;
				}
				else if (this.isTantque(ligne)) {
					Instruction instr = new Instruction("tantque");
					TantQue tq = new TantQue(); tq.condition = "";
					instr.tantques.add(tq); tq.parent = instr;
					this.getInstructions(cur_nd).add(instr); instr.parent = cur_nd;
					cur_nd = tq;
					ligne = Divers.remplacer(ligne, "TANTQUE", "");
					ligne = Divers.remplacer(ligne, "FAIRE", "");
					ligne = Divers.remplacer(ligne, "=", "==");
					ligne = Divers.remplacer(ligne, ">==", ">=");
					ligne = Divers.remplacer(ligne, "<==", "<=");
					tq.condition = ligne;				
				}
				else if (this.isFinTantQue(ligne)) {
					this.ajouterCommentaires(cur_nd);
					cur_nd = cur_nd.parent; // de type Instruction
					cur_nd = cur_nd.parent;
				}
				else if (this.isPour(ligne)) {
					Instruction instr = new Instruction("pour");
					Pour pour = new Pour(); 
					pour.var = ""; pour.debut = ""; pour.fin = ""; pour.pas = "1";
					instr.pours.add(pour); pour.parent = instr;
					this.getInstructions(cur_nd).add(instr); instr.parent = cur_nd;
					cur_nd = pour;
					ligne = Divers.remplacer(ligne, "POUR", "");
					ligne = Divers.remplacer(ligne, "FAIRE", "");
					int i = ligne.indexOf("=");	if (i<0) continue;
					String var = ligne.substring(0, i);
					if (var!=null) pour.var = var.trim();
					ligne = ligne.substring(i+1, ligne.length()); if (ligne==null) continue;
					i = ligne.indexOf("JUSQU'A"); if (i<0) continue;
					String debut = ligne.substring(0, i);
					if (debut!=null) pour.debut = debut.trim();
					ligne = ligne.substring(i+1+7, ligne.length()); if (ligne==null) continue;
					i = ligne.indexOf("INCREMENT"); 
					if (i<0) {
						pour.fin = ligne.trim();
					}
					else {
						String fin = ligne.substring(0, i);
						if (fin!=null) pour.fin = fin.trim();
						ligne = ligne.substring(i+1+9, ligne.length()); if (ligne==null) continue;
						pour.pas = ligne.trim();
					}
				}
				else if (this.isFinPour(ligne)) {
					this.ajouterCommentaires(cur_nd);
					cur_nd = cur_nd.parent; // de type Instruction
					cur_nd = cur_nd.parent;
				}
				else if (this.isLire(ligne)) {
					if (ignorerLire && (cur_oper==null)) continue;
					String parametres = Divers.remplacer(ligne, "LIRE", "");
					if (ligne.startsWith("REQUETE")) {
						int i = ligne.lastIndexOf("'");	// dernier guillemet (s'il existe)
						int j = ligne.indexOf(",", i+1);
						if (j<0) continue;
						parametres = ligne.substring(j+1);
					}
					StringTokenizer tok1 = new StringTokenizer(parametres,",",false);
					while(tok1.hasMoreTokens()) {
						String parametre = tok1.nextToken();
						if (parametre==null) continue;
						Instruction instr = new Instruction("lire");
						Argument arg = new Argument();
						arg.nom = parametre.trim();	
						arg.type = "REEL"; trouverType(arg);
						instr.arguments.add(arg); arg.parent = instr;
						this.getInstructions(cur_nd).add(instr); instr.parent = cur_nd;
					}
				}
				else if (this.isEcrire(ligne)) {
					if (ignorerEcrire && (cur_oper==null)) continue;
					Instruction instr = new Instruction("ecrire");
					String parametres = Divers.remplacer(ligne, "ECRIRE", "");
					StringTokenizer tok1 = new StringTokenizer(parametres,",",false);
					while(tok1.hasMoreTokens()) {
						String parametre = tok1.nextToken();
						if (parametre==null) continue;
						if (parametre.contains("\"")) continue;
						if (parametre.contains("'")) continue;
						Argument arg = new Argument();
						arg.nom = parametre.trim();	
						arg.type = "REEL"; trouverType(arg);
						instr.arguments.add(arg); arg.parent = instr;
					}
					if (instr.arguments.size()>0) {
						this.getInstructions(cur_nd).add(instr); instr.parent = cur_nd;
					}
				}
				else if (this.isOper(ligne)) {
					cur_oper = (Operation) prog_xml.getOperation(this.trouverNom(ligne));
					if (cur_oper==null) continue;
					cur_nd = cur_oper;
				}
				else if (this.isFinOper(ligne)) {
					if (cur_oper==null) {
						cur_nd = prog_xml;
						continue;
					}
					if (!cur_oper.isFonction()) {
						cur_nd = prog_xml;
						cur_oper = null;
						continue;
					}
					Instruction instr = new Instruction("affectation");
					Affectation aff = new Affectation(); 
					aff.var = cur_oper.getRetour().nom;
					aff.expression = trouverNom(ligne);
					instr.affectations.add(aff); aff.parent = instr;
					this.getInstructions(cur_nd).add(instr); instr.parent = cur_nd;
					cur_nd = prog_xml;
					cur_oper = null;
				}
				else if (this.isAppel(ligne)) {
					String parametre = null;
					String parametres = null;
					String nom = trouverNom(ligne);
					Operation oper = (Operation) prog_xml.getOperation(nom);
					if (oper==null) {
						ligne = Divers.remplacer(ligne,"EXECUTER ", "" );
						Instruction instr = new Instruction(ligne.trim());
						this.getInstructions(cur_nd).add(instr); instr.parent = cur_nd;
						continue;
					}
					int i = ligne.indexOf("(");
					if (i>=0) {
						parametres = ligne.substring(i+1, ligne.lastIndexOf(")"));
					}
					Instruction instr = new Instruction(oper.nom);
					this.getInstructions(cur_nd).add(instr); instr.parent = cur_nd;
					for (Iterator<org.javascool.proglets.plurialgo.langages.modele.Parametre> iter=oper.parametres.iterator(); iter.hasNext();) {
						Parametre param = (Parametre) iter.next();
						Argument arg = new Argument(param.nom, param.type, param.mode);
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
			prog_xml.ecrireWarning("probleme d'analyse du code Larp");
		}
		buf_xml = prog_xml.getXmlBuffer();
		Divers.remplacer(buf_xml, "<==", "<=");
		Divers.remplacer(buf_xml, ">==", ">=");
	}

	private void initOperation() {
		StringTokenizer tok = new StringTokenizer(buf_larp.toString(),"\n\r",false);
		Operation oper = null;
		while(tok.hasMoreTokens()) {
			String ligne = tok.nextToken();
			if (this.isOper(ligne)) {
				oper = new Operation();
				oper.nom = trouverNom(ligne);
				if (oper.nom==null) continue;
				int i = ligne.indexOf("ENTRER");
				String parametres = ligne.substring(i+6, ligne.length());
				StringTokenizer tok1 = new StringTokenizer(parametres,",",false);
				while(tok1.hasMoreTokens()) {
					String parametre = tok1.nextToken();
					if (parametre==null) continue;
					Parametre param = new Parametre();
					param.mode = "IN";
					param.type = "REEL";
					param.nom = trouverNom(parametre);
					trouverType(param);
					if (param.nom!=null) {
						oper.parametres.add(param);
					}
					if (parametre.contains("REFERENCE")) {
						param.mode = "OUT";			
					}
				}
				prog_xml.operations.add(oper);	
				oper.parent = prog_xml;
			}
			if (this.isFinOper(ligne)) {
				if (trouverNom(ligne)!=null) {
					Variable retour = new Variable();
					retour.mode = "OUT";
					retour.type = "REEL";
					retour.nom = "retour";
					oper.retours.add(retour);
				}
			}
		}
	}	

	private void trouverType(InfoTypee arg) {
		InfoTypee info;
		InfoTypeeList liste = new InfoTypeeList();
		liste.addVariables(prog_xml.variables);
		String nom;
		if (arg.nom.contains("[")) {
			nom = arg.nom.substring(0, arg.nom.indexOf("["));
			info = liste.getInfo(nom);
			if (info!=null) {
				arg.type = info.type.substring(4, info.type.length());
			}
		}
		else {
			info = liste.getInfo(arg.nom);
			if (info!=null) {
				arg.type = info.type;
			}
		}
	}

	private String trouverNom(String ligne) {
		if (ligne.startsWith("ENTRER")) return "calculer";
		StringTokenizer tok = new StringTokenizer(ligne," (",true);
		while(tok.hasMoreTokens()) {
			String mot = tok.nextToken();	
			if (mot.equals(" ")) continue;	
			if (mot.equals("ENTRER")) continue;	
			if (mot.equals("REFERENCE")) continue;	
			if (mot.equals("EXECUTER")) continue;
			if (mot.equals("RETOURNER")) continue;
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
		Instruction instr = new Instruction("// ajouter des instructions");
		this.getInstructions(cur_nd).add(instr); instr.parent = cur_nd;
	}
	
	private ArrayList<org.javascool.proglets.plurialgo.langages.modele.Instruction> getInstructions(Noeud nd) {
		if (nd instanceof Programme) return ((Programme) nd).instructions;
		if (nd instanceof Operation) return ((Operation) nd).instructions;
		if (nd instanceof Si) return ((Si) nd).instructions;
		if (nd instanceof Pour) return ((Pour) nd).instructions;
		if (nd instanceof TantQue) return ((TantQue) nd).instructions;
		if (nd==null) return prog_xml.instructions;
		return getInstructions(nd.parent);
	}
	
	private boolean isSi(String ligne) {
		if (ligne.contains("SINON")) return false;
		if (!ligne.startsWith("SI")) return false;
		if (!ligne.contains("ALORS")) return false;
		return true;
	}
	
	private boolean isSinonSi(String ligne) {
		if (!ligne.startsWith("SINON")) return false;
		if (!ligne.contains("ALORS")) return false;
		return true;
	}
	
	private boolean isSinon(String ligne) {
		if (!ligne.startsWith("SINON")) return false;
		if (ligne.contains("ALORS")) return false;
		return true;
	}	
	
	private boolean isFinSi(String ligne) {
		if (ligne.startsWith("FINSI")) return true;
		if (ligne.startsWith("FIN SI")) return true;
		return false;
	}	
	
	private boolean isPour(String ligne) {
		if (!ligne.startsWith("POUR")) return false;
		if (!ligne.contains("JUSQU'A")) return false;
		return true;
	}
	
	private boolean isFinPour(String ligne) {
		if (ligne.startsWith("FINPOUR")) return true;
		if (ligne.startsWith("FIN POUR")) return true;
		return false;
	}	
	
	private boolean isTantque(String ligne) {
		if (!ligne.startsWith("TANTQUE")) return false;
		return true;
	}
	
	private boolean isFinTantQue(String ligne) {
		if (ligne.startsWith("FINTANTQUE")) return true;
		if (ligne.startsWith("FIN TANTQUE")) return true;
		return false;
	}	
	
	private boolean isLire(String ligne) {
		if (ligne.startsWith("LIRE")) return true;
		if (ligne.startsWith("REQUETE")) return true;
		return false;
	}	
	
	private boolean isEcrire(String ligne) {
		if (!ligne.startsWith("ECRIRE")) return false;
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
		return true;
	}	
	
	private boolean isOper(String ligne) {
		if (!ligne.contains("ENTRER")) return false;
		return true;
	}
	
	private boolean isFinOper(String ligne) {
		if (!ligne.startsWith("RETOURNER")) return false;
		return true;
	}
	
	private boolean isAppel(String ligne) {
		if (ligne.startsWith("EXECUTER ")) return true;
		return false;
	}
	
	private boolean isImport(String ligne) {
		if (ligne.contains("@import")) return true;
		return false;
	}
}
