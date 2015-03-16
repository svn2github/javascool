/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2013.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.langages.xml;

import java.util.ArrayList;
import java.util.StringTokenizer;

import org.javascool.proglets.plurialgo.divers.*;
import org.javascool.proglets.plurialgo.langages.modele.*;


/**
 * Cette classe permet de transformer un code Algobox en un objet de classe Programme.
 */
public class AnalyseurAlgobox implements iAnalyseur {
	
	private XmlProgramme prog_xml;
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
		
	public AnalyseurAlgobox(String txt) {
		buf_algo = new StringBuffer();
		buf_algo.append(txt);
	}

	/**
	      Retourne l'objet de classe Programme obtenu après analyse du code Algobox.
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
	      Retourne le code Xml obtenu après analyse du code Algobox.
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
		Divers.remplacer(buf_algo, "'", " ");
		Divers.remplacer(buf_algo,"\"", "'");
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
		prog_xml = new XmlProgramme(); 
		prog_xml.nom = "exemple";
		try {
			XmlOperation cur_oper = null;
			Noeud cur_nd = prog_xml;
			StringTokenizer tok = new StringTokenizer(buf_algo.toString(),"\n\r",false);
			while(tok.hasMoreTokens()) {
				String ligne = tok.nextToken();
				System.out.println("ligne:"+ligne);
				if (this.isDim(ligne)) {
					XmlVariable var = new XmlVariable();
					int i = ligne.indexOf(" EST_DU_TYPE ");
					var.nom = ligne.substring(0, i).trim();
					var.type = this.trouverType(ligne);
					prog_xml.variables.add(var);
				}
				else if (this.isAffectation(ligne)) {
					XmlInstruction instr = new XmlInstruction("affectation");
					XmlAffectation aff = new XmlAffectation(); aff.var = ""; aff.expression = "";
					instr.affectations.add(aff); aff.parent = instr;
					this.getInstructions(cur_nd).add(instr); instr.parent = cur_nd;
					int i = ligne.indexOf("PREND_LA_VALEUR");
					String gauche = ligne.substring(0, i);
					if (gauche!=null) aff.var = gauche.trim();
					String droite = ligne.substring(i+15, ligne.length());
					if (droite!=null) aff.expression = droite.trim();
				}
				else if (this.isSi(ligne)) {
					XmlInstruction instr = new XmlInstruction("si");
					XmlSi si = new XmlSi(); si.condition = "";
					instr.sis.add(si); si.parent = instr;
					this.getInstructions(cur_nd).add(instr); instr.parent = cur_nd;
					cur_nd = si;
					ligne = Divers.remplacer(ligne, "SI", "");
					ligne = Divers.remplacer(ligne, "ALORS", "");
					si.condition = ligne.trim();				
				}
				else if (this.isSinonSi(ligne)) {
					this.ajouterCommentaires(cur_nd);
					XmlInstruction instr = (XmlInstruction) cur_nd.parent;
					XmlSi sinonsi = new XmlSi(); sinonsi.condition = "";
					instr.sis.add(sinonsi); sinonsi.parent = instr;
					cur_nd = sinonsi;
					ligne = Divers.remplacer(ligne, "SINON", "");
					ligne = Divers.remplacer(ligne, "SI", "");
					ligne = Divers.remplacer(ligne, "ALORS", "");
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
					this.ajouterCommentaires(cur_nd);
					cur_nd = cur_nd.parent; // de type Instruction
					cur_nd = cur_nd.parent;
				}
				else if (this.isTantque(ligne)) {
					XmlInstruction instr = new XmlInstruction("tantque");
					XmlTantQue tq = new XmlTantQue(); tq.condition = "";
					instr.tantques.add(tq); tq.parent = instr;
					this.getInstructions(cur_nd).add(instr); instr.parent = cur_nd;
					cur_nd = tq;
					ligne = Divers.remplacer(ligne, "TANT_QUE", "");
					ligne = Divers.remplacer(ligne, "FAIRE", "");
					tq.condition = ligne.trim();				
				}
				else if (this.isFinTantQue(ligne)) {
					this.ajouterCommentaires(cur_nd);
					cur_nd = cur_nd.parent; // de type Instruction
					cur_nd = cur_nd.parent;
				}
				else if (this.isPour(ligne)) {
					XmlInstruction instr = new XmlInstruction("pour");
					XmlPour pour = new XmlPour(); 
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
					XmlInstruction instr = new XmlInstruction("lire");
					XmlArgument arg = new XmlArgument();
					arg.nom = parametres.trim();	
					arg.type = "REEL"; trouverType(arg);
					instr.arguments.add(arg); arg.parent = instr;
					if (instr.arguments.size()>0) {
						this.getInstructions(cur_nd).add(instr); instr.parent = cur_nd;
					}
				}
				else if (this.isEcrire(ligne)) {
					if (ignorerEcrire && (cur_oper==null)) continue;
					XmlInstruction instr = new XmlInstruction("ecrire");
					String parametres = Divers.remplacer(ligne, "AFFICHER", "").trim();
					XmlArgument arg = new XmlArgument();
					arg.nom = parametres.trim();	
					arg.type = "EXPR"; trouverType(arg);
					if (arg.isExpression() && !parametres.startsWith("'") && !parametres.endsWith("'")) continue;
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
					XmlInstruction instr = new XmlInstruction(prim + ligne.substring(i, j+1));
					this.getInstructions(cur_nd).add(instr); instr.parent = cur_nd;
				}
				else if (this.isRepere(ligne)) {
					int i = ligne.indexOf("repcode='");
					int j = ligne.indexOf("'",i+9);
					String repcode = ligne.substring(i+9, j);
					int k;
					k = repcode.lastIndexOf("#"); if (k<0) continue;
					repcode = repcode.substring(0, k);	// on ote gradY
					k = repcode.lastIndexOf("#"); if (k<0) continue;
					repcode = repcode.substring(0, k);	// on ote gradX
					repcode=Divers.remplacer(repcode, "#", ",");
					XmlInstruction instr = new XmlInstruction("reset(" + repcode + ")");
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
		System.out.println("traiterOper:+ligne");
		int i,j;
		i = ligne.indexOf("fctcode='");
		j = ligne.indexOf("'",i+9);
		String fctcode = ligne.substring(i+9, j);
		XmlOperation oper = new XmlOperation();
		oper.nom = "F1";
		prog_xml.operations.add(oper); oper.parent = prog_xml;
		XmlParametre param = new XmlParametre();
		param.mode = "IN"; param.type = "REEL";	param.nom = "x";
		oper.parametres.add(param);
		XmlVariable retour = new XmlVariable();
		retour.mode = "OUT"; retour.type = "REEL"; retour.nom = "retour";
		oper.retours.add(retour);
		XmlInstruction instr = new XmlInstruction("affectation");
		XmlAffectation aff = new XmlAffectation(); aff.var = "retour"; aff.expression = fctcode;
		instr.affectations.add(aff); aff.parent = instr;
		oper.instructions.add(instr); 
	}	
	
	private void traiterOperF2(String ligne) {
		int i,j;
		i = ligne.indexOf("F2para='");	j = ligne.indexOf("'",i+8);
		String f2para = ligne.substring(i+8, j);
		i = ligne.indexOf("F2lignes='"); j = ligne.indexOf("'",i+10);
		String f2lignes = ligne.substring(i+10, j);
		i = ligne.indexOf("F2defaut='"); j = ligne.indexOf("'",i+10);
		String f2defaut = ligne.substring(i+10, j);
		XmlOperation oper = new XmlOperation();
		oper.nom = "F2";
		prog_xml.operations.add(oper); oper.parent = prog_xml;
		// les parametres
		StringTokenizer tok1 = new StringTokenizer(f2para,",",false);
		while(tok1.hasMoreTokens()) {
			String parametre = tok1.nextToken();
			if (parametre==null) continue;
			XmlParametre param = new XmlParametre();
			param.mode = "IN"; param.type = "REEL"; param.nom = parametre.trim();
			trouverType(param);
			if (param.nom!=null) {
				oper.parametres.add(param);
			}
		}
		// le retour
		XmlVariable retour = new XmlVariable();
		retour.mode = "OUT"; retour.type = "REEL"; retour.nom = "retour";
		oper.retours.add(retour);
		// l'instruction conditionnelle
		XmlInstruction instr = new XmlInstruction("si");
		StringTokenizer tok2 = new StringTokenizer(f2lignes,"#",false);
		while(tok2.hasMoreTokens()) {
			String parametre = tok2.nextToken();
			if (parametre==null) continue;
			i = parametre.indexOf("@");
			if (i<0) continue;
			XmlSi si = new XmlSi(); si.condition = parametre.substring(0, i);
			instr.sis.add(si); si.parent = instr;
			XmlInstruction instr_aff = new XmlInstruction("affectation");
			si.instructions.add(instr_aff);
			XmlAffectation aff = new XmlAffectation(); 
			aff.var = retour.nom;; 
			aff.expression = parametre.substring(i+1, parametre.length());
			instr_aff.affectations.add(aff); 
		}
		if (f2defaut.trim().length()>0) {
			XmlInstruction instr_aff = new XmlInstruction("affectation");
			XmlAffectation aff = new XmlAffectation(); 
			aff.var = retour.nom;; 
			aff.expression = f2defaut;
			instr_aff.affectations.add(aff); 
			if (instr.sis.size()>0) {
				XmlSi si = new XmlSi(); si.condition = "";
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
		if (!ligne.contains("fctcode=")) return false;
		if (ligne.contains("inactif")) return false;
		return true;
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
	
	// ---------------------------------------------
	// export au format AlgoBox
	// ---------------------------------------------

	private void ecrireItem(String algoitem, String code, String fin) {
		buf_xml.append("<item algoitem=");
		buf_xml.append("\"" + algoitem + "\"");
		buf_xml.append(" code=");
		buf_xml.append("\"" + code + "\"");
		buf_xml.append(fin);
		buf_xml.append("\n");
	}

	private void ecrireFinItem(String algoitem) {
		buf_xml.append("</item>");
		buf_xml.append("\n");
	}	
	
	public String exportAlgobox() {
		// nettoyage de l'algo
		Divers.remplacer(buf_algo,"\t", "");	
		Divers.remplacer(buf_algo,"\r", "");
		for(int i=0;i<60;i++) {
			Divers.remplacer(buf_algo,"\n ", "\n");
			Divers.remplacer(buf_algo," \n", "\n");
			Divers.remplacer(buf_algo,"\n\n", "\n");
		}
		Divers.remplacer(buf_algo, "\"", "&quot;");
		Divers.remplacer(buf_algo, "<", "&lt;");
		Divers.remplacer(buf_algo, ">", "&gt;");
		Divers.remplacer(buf_algo,"FIN_SI\nSINON", "SINON");	
		Divers.remplacer(buf_algo,"AFFICHER*", "AFFICHER");	
		// creation code XML
		buf_xml = new StringBuffer();
		buf_xml.append("<Algo>"); buf_xml.append("\n");
		ecrireItem("VARIABLES","100#declarationsvariables", ">");
		StringTokenizer tok = new StringTokenizer(buf_algo.toString(),"\n\r",false);
		while(tok.hasMoreTokens()) {
			String ligne = tok.nextToken();
			if (this.isDim(ligne)) {
				int i = ligne.indexOf("EST_DU_TYPE");
				String nom = ligne.substring(0, i).trim();
				String type = ligne.substring(i+11).trim();
				String code = "1#" + type + "#" + nom; 
				ecrireItem(ligne, code, "/>");
			}
		}
		ecrireFinItem("VARIABLES");
		ecrireItem("DEBUT_ALGORITHME","101#debutalgo", ">");
		tok = new StringTokenizer(buf_algo.toString(),"\n\r",false);
		while(tok.hasMoreTokens()) {
			String ligne = tok.nextToken();
			if (this.isDim(ligne)) {
			}
			else if (this.isAffectation(ligne)) {
				int i = ligne.indexOf("PREND_LA_VALEUR");
				String gauche = ligne.substring(0, i).trim();
				String droite = ligne.substring(i+15, ligne.length()).trim();
				String code = "5#" + gauche + "#" + droite + "#pasliste"; 
				int i1 = gauche.indexOf("[");
				int i2 = gauche.lastIndexOf("]");
				if ( (i1>0) && (i2>i1) ) {
					String indice = ligne.substring(i1+1, i2).trim();
					code = "5#" + gauche.substring(0,i1) + "#" + droite + "#" + indice;
				}
				ecrireItem(ligne, code, "/>");
			}
			else if (this.isSi(ligne)) {
				int i = ligne.indexOf("ALORS");
				String condition = ligne.substring(2, i).trim();
				String code = "6#" + condition; 
				ecrireItem(ligne, code, ">");
				ecrireItem("DEBUT_SI", "7#debutsi", "/>");
			}
			else if (this.isSinonSi(ligne)) {
			}
			else if (this.isSinon(ligne)) {
				ecrireItem("FIN_SI", "8#finsi", "/>");
				ecrireItem("SINON", "9#sinon", ">");
				ecrireItem("DEBUT_SINON", "10#debutsinon", "/>");
			}
			else if (ligne.equals("FIN_SINON")) {
				ecrireItem("FIN_SINON", "11#finsinon", "/>");
				ecrireFinItem("SINON");
				ecrireFinItem("SI");
			}
			else if (this.isFinSi(ligne)) {
				ecrireItem("FIN_SI", "8#finsi", "/>");
				ecrireFinItem("SI");
			}
			else if (this.isTantque(ligne)) {
				int i = ligne.indexOf("FAIRE");
				String condition = ligne.substring(8, i).trim();
				String code = "15#" + condition; 
				ecrireItem(ligne, code, ">");
				ecrireItem("DEBUT_TANT_QUE", "16#debuttantque", "/>");	
			}
			else if (this.isFinTantQue(ligne)) {
				ecrireItem("FIN_TANT_QUE", "17#fintantque", "/>");
				ecrireFinItem("TANT_QUE");
			}
			else if (this.isPour(ligne)) {
				int i = ligne.indexOf("ALLANT_DE");
				int j = ligne.lastIndexOf(" A ");
				String var = ligne.substring(4, i).trim();
				String debut = ligne.substring(i+9, j).trim();
				String fin = ligne.substring(j+3).trim();
				String code = "12#" + var + "#" + debut + "#" + fin; 
				ecrireItem(ligne, code, ">");
				ecrireItem("DEBUT_POUR", "13#debutpour", "/>");	
			}
			else if (this.isFinPour(ligne)) {
				ecrireItem("FIN_POUR", "14#finpour", "/>");
				ecrireFinItem("POUR");
			}
			else if (this.isLire(ligne)) {
				String nom = ligne.substring(5, ligne.length()).trim();
				String code = "2#" + nom + "#pasliste"; 
				int i1 = ligne.indexOf("[");
				int i2 = ligne.lastIndexOf("]");
				if ( (i1>0) && (i2>i1) ) {
					nom = ligne.substring(5, i1).trim();
					String indice = ligne.substring(i1+1, i2).trim();
					code = "2#" + nom + "#" + indice;
				}
				ecrireItem(ligne, code, "/>");
			}
			else if (this.isEcrire(ligne)) {
				String message = ligne.substring(9, ligne.length()).trim();
				if (!message.startsWith("&quot;")) {
					String code = "3#" + message + "#1" + "#pasliste"; 
					int i1 = ligne.indexOf("[");
					int i2 = ligne.lastIndexOf("]");
					if ( (i1>0) && (i2>i1) ) {
						message = ligne.substring(9, i1).trim();
						String indice = ligne.substring(i1+1, i2).trim();
						code = "3#" + message + "#1" + "#" + indice; 
					}
					ecrireItem(ligne, code, "/>");
				}
				else {
					message = Divers.remplacer(message, "&quot;", "");
					String code = "4#" + message + "#1"; 
					ecrireItem(ligne, code, "/>");
				}
				
			}
		}
		ecrireFinItem("DEBUT_ALGORITHME");
		buf_xml.append("</Algo>"); buf_xml.append("\n");
		return buf_xml.toString();
	}

}
