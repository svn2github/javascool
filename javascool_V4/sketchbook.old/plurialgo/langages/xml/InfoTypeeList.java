/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2013.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.langages.xml;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;
import org.javascool.proglets.plurialgo.langages.modele.*;


/**
 * Cette classe permet de gérer des listes de quadruplets (nom, type, mode, dim)
 * afin d'interpréter les informations de l'interface graphique (onglet principal)
 * pour créer ou transformer des programmes.
 * 
 * @see org.javascool.proglets.plurialgo.langages.modele.InfoTypee
*/
public class InfoTypeeList {
	
	private ArrayList<InfoTypee> infos;
	
	public InfoTypeeList() {
		infos = new ArrayList<InfoTypee>();
	}
	
	public ArrayList<InfoTypee> getInfos() {
		return infos;
	}

	// ---------------------------------------------
	// fonctions d'ajout dans la liste
	// ---------------------------------------------

	public void addIntermediaire (Intermediaire inter) {
		addModes( inter.donnees, "IN" );
		addModes( inter.resultats, "OUT" );
		//addModes( inter.autres, "AUTRE" );
		addTypes( inter.reels, "REEL" );
		addTypes( inter.entiers, "ENTIER" );
		addTypes( inter.textes, "TEXTE" );
		addTypes( inter.booleens, "BOOLEEN" );
		addTypes( inter.tab_reels, "TAB_REEL" );
		addTypes( inter.tab_entiers, "TAB_ENTIER");
		addTypes( inter.tab_textes, "TAB_TEXTE");
		addTypes( inter.tab_booleens, "TAB_BOOLEEN");
		addTypes( inter.mat_reels, "MAT_REEL" );
		addTypes( inter.mat_entiers, "MAT_ENTIER");
		addTypes( inter.mat_textes, "MAT_TEXTE");
		addTypes( inter.mat_booleens, "MAT_BOOLEEN");
	}
	
	void addModes(String vars, String mode) {
		InfoTypee info;
		StringTokenizer tok= new StringTokenizer(vars, " ,");
		while (tok.hasMoreTokens()) {
			String nom = tok.nextToken();
			info = this.getInfo(nom);
			if (info==null) {
				info = new InfoTypee(nom);
				info.mode = mode;
				infos.add(info);
			}
			else {
				if (info.isIn() && mode.equals("OUT")) info.mode="INOUT";
				else info.mode = mode;
			}
		}
	}
	
	void addTypes(String vars, String type) {
		InfoTypee info;
		StringTokenizer tok= new StringTokenizer(vars, " ,");
		while (tok.hasMoreTokens()) {
			String nom = tok.nextToken();
			info = this.getInfo(nom);
			if (info==null) {
				info = new InfoTypee(nom);
				info.type = type;
				infos.add(info);
			}
			else {
				info.type = type;
			}
		}
	} 
	
	void addDim(String var, String dim) {
		InfoTypee info = this.getInfo(var);
		if (info==null) return;
		info.dim = dim;
	} 
	
	public void addInfo(InfoTypee info) {
		if (getInfo(info.nom)!=null) return;
		infos.add(info);
	}
	
	public void addInfos(ArrayList<InfoTypee> liste) {
		infos.addAll(liste);
	}
	
	void addVariables(ArrayList<ModeleVariable> liste) {
		infos.addAll(liste);
	}
	
	void addParametres(ArrayList<ModeleParametre> liste) {
		infos.addAll(liste);
	}
	
	// ---------------------------------------------
	// fonctions de recherche dans la liste
	// ---------------------------------------------
	
	public InfoTypee getInfo(String nom) {
		for(Iterator<InfoTypee> iter=infos.iterator(); iter.hasNext(); ) {
			InfoTypee info = iter.next();
			if (nom.equalsIgnoreCase(info.nom)) return info;
		}
		return null;
	}

	public static InfoTypee getInfo(String nom, ArrayList<InfoTypee> liste) {
		for(Iterator<InfoTypee> iter=liste.iterator(); iter.hasNext(); ) {
			InfoTypee info = iter.next();
			if (nom.equalsIgnoreCase(info.nom)) return info;
		}
		return null;
	}
	
	// ---------------------------------------------
	// fonctions de comptage dans la liste
	// ---------------------------------------------

	int compterMode(String mode) {
		int nb=0;
		for(Iterator<InfoTypee> iter=infos.iterator(); iter.hasNext(); ) {
			InfoTypee info = iter.next();
			if (mode.equalsIgnoreCase(info.mode)) nb++;
		}
		return nb;
	}	
	
}
