/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2013.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.langages.xml;

import java.util.ArrayList;
import java.util.StringTokenizer;

import org.javascool.proglets.plurialgo.divers.*;
import org.javascool.proglets.plurialgo.langages.modele.InfoTypee;
import org.javascool.proglets.plurialgo.langages.modele.Noeud;


/**
 * Cette classe permet de transformer un code Algobox en un objet de classe Programme.
 */
public class AnalyseurAlgobox implements iAnalyseur {
	
	private Programme prog_xml;
	private StringBuffer buf_algo;
	private StringBuffer buf_xml;
	private boolean isXml;

	/**
	      Transforme un code Algobox en un objet de classe Programme.
	      @param txt le code Algobox à analyser
	      @param ignorerLire ignore les instructions de lire si true
	      @param ignorerEcrire ignore les instructions d'écriture si true
	*/		
	public AnalyseurAlgobox(String txt, boolean ignorerLire, boolean ignorerEcrire) {
		this.nettoyerAlgobox(txt);
		this.algoboxEnXml(ignorerLire, ignorerEcrire);
	}

	/**
	      Retourne l'objet de classe Programme obtenu après analyse du code Algobox.
	*/		
	public Programme getProgramme() {
		return prog_xml;
	}

	/**
	      Retourne le code Xml obtenu après analyse du code Algobox.
	*/	
	public StringBuffer getXml() {
		return buf_xml;
	}
	
	private void nettoyerAlgobox(String txt) {
		isXml = txt.contains("</item>");
		buf_algo = new StringBuffer();
		if (isXml) {
			StringTokenizer tok = new StringTokenizer(txt,"\n\r",false);
			while(tok.hasMoreTokens()) {
				String mot = tok.nextToken();
				if (this.isOperF1(mot) || this.isOperF2(mot)|| this.isRepere(mot)) {
					buf_algo.append("// "+mot+"\n");
					continue;
				}
				int i = mot.indexOf("algoitem=\"");
				int j = mot.indexOf("\"",i+10);
				int k = mot.indexOf("<item ");
				if (i>=0 && j>i+10) {
					for(int l=0;l<k/4-1;l++) buf_algo.append("\t");
					mot = mot.substring(i+10, j);
					buf_algo.append(mot+"\n");
				}
			}
			Divers.remplacer(buf_algo,"&quot;", "\"");
			Divers.remplacer(buf_algo,"&lt;", "<");
			Divers.remplacer(buf_algo,"&gt;", ">");
		}
		else {
			buf_algo.append(txt);
		}
		Divers.remplacer(buf_algo, ")->(", ",");	// TRACER_SEGMENT
		Divers.remplacer(buf_algo,"\t", "");	
		Divers.remplacer(buf_algo,"\r", "");
		for(int i=0;i<60;i++) {
			Divers.remplacer(buf_algo,"\n ", "\n");
			Divers.remplacer(buf_algo," \n", "\n");
			Divers.remplacer(buf_algo,"\n\n", "\n");
		}	
		Divers.remplacer(buf_algo,"FIN_SI\nSINON", "SINON");	
		Divers.remplacer(buf_algo,"AFFICHER*", "AFFICHER");	
	}
	
	private void algoboxEnXml(boolean ignorerLire, boolean ignorerEcrire) {
		prog_xml = new Programme(); 
		prog_xml.nom = "exemple";
		try {
			Operation cur_oper = null;
			Noeud cur_nd = prog_xml;
			StringTokenizer tok = new StringTokenizer(buf_algo.toString(),"\n\r",false);
			while(tok.hasMoreTokens()) {
				String ligne = tok.nextToken();
				if (this.isDim(ligne)) {
					Variable var = new Variable();
					int i = ligne.indexOf(" EST_DU_TYPE ");
					var.nom = ligne.substring(0, i).trim();
					var.type = this.trouverType(ligne);
					prog_xml.variables.add(var);
				}
				else if (this.isAffectation(ligne)) {
					Instruction instr = new Instruction("affectation");
					Affectation aff = new Affectation(); aff.var = ""; aff.expression = "";
					instr.affectations.add(aff); aff.parent = instr;
					this.getInstructions(cur_nd).add(instr); instr.parent = cur_nd;
					int i = ligne.indexOf("PREND_LA_VALEUR");
					String gauche = ligne.substring(0, i);
					if (gauche!=null) aff.var = gauche.trim();
					String droite = ligne.substring(i+15, ligne.length());
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
					ligne = Divers.remplacer(ligne, "TANT_QUE", "");
					ligne = Divers.remplacer(ligne, "FAIRE", "");
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
					ligne = Divers.remplacer(ligne, "POUR ", "");
					int i = ligne.indexOf("ALLANT_DE");	if (i<0) continue;
					String var = ligne.substring(0, i);
					if (var!=null) {
						pour.var = var.trim();
						InfoTypee info;
						InfoTypeeList liste = new InfoTypeeList();
						liste.addVariables(prog_xml.variables);
						if ((info=liste.getInfo(pour.var))!=null) {
								info.type = "ENTIER";
						}
					}
					ligne = ligne.substring(i+9, ligne.length()); if (ligne==null) continue;
					i = ligne.indexOf(" A "); if (i<0) continue;
					String debut = ligne.substring(0, i);
					if (debut!=null) pour.debut = debut.trim();
					ligne = ligne.substring(i+3, ligne.length()); if (ligne==null) continue;
					pour.fin = ligne.trim();
				}
				else if (this.isFinPour(ligne)) {
					this.ajouterCommentaires(cur_nd);
					cur_nd = cur_nd.parent; // de type Instruction
					cur_nd = cur_nd.parent;
				}
				else if (this.isLire(ligne)) {
					if (ignorerLire && (cur_oper==null)) continue;
					String parametres = Divers.remplacer(ligne, "LIRE", "");
					Instruction instr = new Instruction("lire");
					Argument arg = new Argument();
					arg.nom = parametres.trim();	
					arg.type = "REEL"; trouverType(arg);
					instr.arguments.add(arg); arg.parent = instr;
					if (instr.arguments.size()>0) {
						this.getInstructions(cur_nd).add(instr); instr.parent = cur_nd;
					}
				}
				else if (this.isEcrire(ligne)) {
					if (ignorerEcrire && (cur_oper==null)) continue;
					Instruction instr = new Instruction("ecrire");
					String parametres = Divers.remplacer(ligne, "AFFICHER", "");
					if (parametres.contains("&quot;")) continue;
					if (parametres.contains("\"")) continue;
					Argument arg = new Argument();
					arg.nom = parametres.trim();	
					arg.type = "REEL"; trouverType(arg);
					instr.arguments.add(arg); arg.parent = instr;
					if (instr.arguments.size()>0) {
						this.getInstructions(cur_nd).add(instr); instr.parent = cur_nd;
					}
				}
				else if (this.isOperF1(ligne)) {
					traiterOperF1(ligne);
				}
				else if (this.isOperF2(ligne)) {
					traiterOperF2(ligne);
				}
				else if (this.isPrimitive(ligne)) {
					int i = ligne.indexOf("("); if (i<0) continue;
					int j = ligne.lastIndexOf(")"); if (j<i) continue;
					String prim = ligne.substring(0, i).trim();
					if (ligne.contains("TRACER_SEGMENT")) {
						prim = "addLine";
					}
					else if (ligne.contains("TRACER_POINT")) {
						prim = "setPoint";
					}
					Instruction instr = new Instruction(prim + ligne.substring(i, j+1) + " ////proglet algoDeMaths");
					this.getInstructions(cur_nd).add(instr); instr.parent = cur_nd;
				}
				else if (this.isRepere(ligne)) {
					int i = ligne.indexOf("repcode=\"");
					int j = ligne.indexOf("\"",i+9);
					String repcode = ligne.substring(i+9, j);
					int k;
					k = repcode.lastIndexOf("#"); if (k<0) continue;
					repcode = repcode.substring(0, k);	// on ote gradY
					k = repcode.lastIndexOf("#"); if (k<0) continue;
					repcode = repcode.substring(0, k);	// on ote gradX
					repcode=Divers.remplacer(repcode, "#", ",");
					Instruction instr = new Instruction("reset(" + repcode + ") ////proglet algoDeMaths");
					this.getInstructions(cur_nd).add(instr); instr.parent = cur_nd;
				}
			}
		}
		catch (Exception ex) {
			prog_xml.ecrireWarning("probleme d'analyse du code Algobox");
		}
		buf_xml = prog_xml.getXmlBuffer();
		Divers.remplacer(buf_xml, "<==", "<=");
		Divers.remplacer(buf_xml, ">==", ">=");
	}
	
	private void traiterOperF1(String ligne) {
		int i,j;
		i = ligne.indexOf("fctcode=\"");
		j = ligne.indexOf("\"",i+9);
		String fctcode = ligne.substring(i+9, j);
		Operation oper = new Operation();
		oper.nom = "F1";
		prog_xml.operations.add(oper); oper.parent = prog_xml;
		Parametre param = new Parametre();
		param.mode = "IN"; param.type = "REEL";	param.nom = "x";
		oper.parametres.add(param);
		Variable retour = new Variable();
		retour.mode = "OUT"; retour.type = "REEL"; retour.nom = "retour";
		oper.retours.add(retour);
		Instruction instr = new Instruction("affectation");
		Affectation aff = new Affectation(); aff.var = "retour"; aff.expression = fctcode;
		instr.affectations.add(aff); aff.parent = instr;
		oper.instructions.add(instr); 
	}	
	
	private void traiterOperF2(String ligne) {
		int i,j;
		i = ligne.indexOf("F2para=\"");	j = ligne.indexOf("\"",i+8);
		String f2para = ligne.substring(i+8, j);
		i = ligne.indexOf("F2lignes=\""); j = ligne.indexOf("\"",i+10);
		String f2lignes = ligne.substring(i+10, j);
		i = ligne.indexOf("F2defaut=\""); j = ligne.indexOf("\"",i+10);
		String f2defaut = ligne.substring(i+10, j);
		Operation oper = new Operation();
		oper.nom = "F2";
		prog_xml.operations.add(oper); oper.parent = prog_xml;
		// les parametres
		StringTokenizer tok1 = new StringTokenizer(f2para,",",false);
		while(tok1.hasMoreTokens()) {
			String parametre = tok1.nextToken();
			if (parametre==null) continue;
			Parametre param = new Parametre();
			param.mode = "IN"; param.type = "REEL"; param.nom = parametre.trim();
			trouverType(param);
			if (param.nom!=null) {
				oper.parametres.add(param);
			}
		}
		// le retour
		Variable retour = new Variable();
		retour.mode = "OUT"; retour.type = "REEL"; retour.nom = "retour";
		oper.retours.add(retour);
		// l'instruction conditionnelle
		Instruction instr = new Instruction("si");
		StringTokenizer tok2 = new StringTokenizer(f2lignes,"#",false);
		while(tok2.hasMoreTokens()) {
			String parametre = tok2.nextToken();
			if (parametre==null) continue;
			i = parametre.indexOf("@");
			if (i<0) continue;
			Si si = new Si(); si.condition = parametre.substring(0, i);
			instr.sis.add(si); si.parent = instr;
			Instruction instr_aff = new Instruction("affectation");
			si.instructions.add(instr_aff);
			Affectation aff = new Affectation(); 
			aff.var = retour.nom;; 
			aff.expression = parametre.substring(i+1, parametre.length());
			instr_aff.affectations.add(aff); 
		}
		if (f2defaut.trim().length()>0) {
			Instruction instr_aff = new Instruction("affectation");
			Affectation aff = new Affectation(); 
			aff.var = retour.nom;; 
			aff.expression = f2defaut;
			instr_aff.affectations.add(aff); 
			if (instr.sis.size()>0) {
				Si si = new Si(); si.condition = "";
				instr.sis.add(si); si.parent = instr;
				si.instructions.add(instr_aff);
			}
			else {
				oper.instructions.add(instr_aff); 
			}
		}
		if (instr.sis.size()>0) {
			oper.instructions.add(instr); 
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
	
	private String trouverType(String txt) {
		//utilisé pour typage (sauf lire et ecrire)
		String type = "REEL";
		int i = txt.indexOf(" EST_DU_TYPE ");
		if (i>=0) {
			type = txt.substring(i+13, txt.length()).trim();
			if (type.equals("NOMBRE")) {
				type = "REEL";
			}
			else if (type.equals("LISTE")) {
				type = "TAB_REEL";
			}
			else if (type.equals("CHAINE")) {
				type = "TEXTE";
			}
		}
		return type;
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
		if (!ligne.startsWith("SI ")) return false;
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
		if (ligne.startsWith("FIN_SI")) return true;
		return false;
	}	
	
	private boolean isPour(String ligne) {
		if (!ligne.startsWith("POUR ")) return false;
		return true;
	}
	
	private boolean isFinPour(String ligne) {
		if (ligne.startsWith("FIN_POUR")) return true;
		return false;
	}	
	
	private boolean isTantque(String ligne) {
		if (!ligne.startsWith("TANT_QUE ")) return false;
		return true;
	}
	
	private boolean isFinTantQue(String ligne) {
		if (ligne.startsWith("FIN_TANT_QUE")) return true;
		return false;
	}	
	
	private boolean isLire(String ligne) {
		if (!ligne.startsWith("LIRE")) return false;
		return true;
	}	
	
	private boolean isEcrire(String ligne) {
		if (!ligne.startsWith("AFFICHER")) return false;
		return true;
	}	
	
	private boolean isAffectation(String ligne) {
		if (!ligne.contains("PREND_LA_VALEUR")) return false;
		return true;
	}	
	
	private boolean isDim(String ligne) {
		if (!ligne.contains("EST_DU_TYPE")) return false;
		return true;
	}	
	
	private boolean isOperF1(String ligne) {
		if (!ligne.contains("fonction ")) return false;
		if (ligne.contains("inactif")) return false;
		int i = ligne.indexOf("fctcode=\"");
		int j = ligne.indexOf("\"",i+9);
		if (i>=0 && j>i+9) {
			//String fctcode = ligne.substring(i+9, j);
			return true;
		}
		return false;
	}
	
	private boolean isOperF2(String ligne) {
		if (!ligne.contains("F2 ")) return false;
		if (!ligne.contains("F2para")) return false;
		if (!ligne.contains("F2lignes")) return false;
		if (!ligne.contains("F2defaut")) return false;
		if (ligne.contains("inactif")) return false;
		return true;
	}
	
	private boolean isPrimitive(String ligne) {
		if (ligne.contains("TRACER_SEGMENT")) return true;
		if (ligne.contains("TRACER_POINT")) return true;
		return false;
	}
	
	private boolean isRepere(String ligne) {
		if (!ligne.contains("repere ")) return false;
		if (!ligne.contains("repcode")) return false;
		if (ligne.contains("inactif")) return false;
		return true;
	}

}
