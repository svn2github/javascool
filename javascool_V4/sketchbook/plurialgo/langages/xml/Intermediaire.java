/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2013.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.langages.xml;

import java.util.*;
import org.javascool.proglets.plurialgo.langages.modele.*;


/**
 * Cette classe sert d'intermédiaire entre l'interface graphique (onglet Principal)
 * et le paquetage (...langages.xml) chargé de créer et transformer des programmes.
*/
public class Intermediaire {
	
	public String donnees, resultats; //, autres;
	public String reels, entiers, textes, booleens;
	public String tab_reels, tab_entiers, tab_textes, tab_booleens;
	public String mat_reels, mat_entiers, mat_textes, mat_booleens;
	public String niv_saisie, niv_calcul, niv_affichage, niv_groupement;
	public String nom_algo, nom_langage, nom_groupe;
	
	public Intermediaire() {
		nom_algo = "exemple";
		nom_langage = "vb";
		donnees = "";
		resultats = "";
		//autres = "";
		reels = "";
		entiers = "";
		textes = "";
		booleens = "";
		tab_reels = "";
		tab_entiers = "";
		tab_textes = "";
		tab_booleens = "";
		mat_reels = "";
		mat_entiers = "";
		mat_textes = "";
		mat_booleens = "";
		niv_saisie = ""; 
		niv_calcul = ""; 
		niv_affichage = ""; 
		niv_groupement = "elementaire"; 
		nom_groupe="";
	}
	
	public String getNomAlgo() {
		return nom_algo;
	}
	
	public String getNomLangage() {
		return nom_langage;
	}
		
	// ----------------------------	
	// groupements	
	// ----------------------------
	
	public String getNomGroupe() {
		if (nom_groupe.contains(":")) {
			return nom_groupe.substring(0, nom_groupe.indexOf(":"));
		}
		return nom_groupe;
	}
	
	public ArrayList<InfoTypee> getProprietesGroupe() {
		if (!nom_groupe.contains(":")) return null;
		StringTokenizer tok = new StringTokenizer(nom_groupe.substring(nom_groupe.indexOf(":")+1), " ,");
		ArrayList<InfoTypee> liste = new ArrayList<InfoTypee>();
		while (tok.hasMoreTokens()) {
			InfoTypee info = new InfoTypee(tok.nextToken());
			liste.add(info);
		}
		return liste;
	}
	
	public boolean sansRegroupement() {
		if (niv_groupement.equals("elementaire")) return true;
		if (niv_groupement.equals("elementary")) return true;
		return false;
	}
	
	public boolean avecClasse() {
		if (niv_groupement.equals("classe")) return true;
		if (niv_groupement.equals("class")) return true;
		return false;
	}
	
	public boolean avecEnregistrement() {
		if (niv_groupement.equals("enregistrement")) return true;
		if (niv_groupement.equals("record")) return true;
		return false;
	}
	
	// ----------------------------	
	// calculs	
	// ----------------------------
	
	public boolean sansCalcul() {
		if (niv_calcul.equals("elementaire")) return true;
		if (niv_calcul.equals("elementary")) return true;
		return false;
	}
	
	public boolean avecProcedureCalcul() {
		return niv_calcul.equals("procedure");
	}
	
	public boolean avecFonctionsCalcul() {
		if (niv_calcul.equals("fonctions")) return true;
		if (niv_calcul.equals("functions")) return true;
		if (niv_calcul.equals("fonction")) return true;
		if (niv_calcul.equals("function")) return true;
		return false;
	}
	
	// ----------------------------
	// affichage	
	// ----------------------------
	
	public String getNiveauAffichage() {
		return niv_affichage;
	}
	
	public boolean sansAffichage() {
		if (niv_affichage.equals("")) return true;
		if (niv_affichage.equals("sans")) return true;
		return false;
	}
	
	public boolean avecAffichageElementaire() {
		if (niv_affichage.equals("elementaire")) return true;
		if (niv_affichage.equals("elementary")) return true;
		return false;
	}
	
	public boolean avecProcedureAffichage() {
		return niv_affichage.equals("procedure");
	}
	
	public boolean avecAffichageFichierTexte() {
		if (niv_affichage.equals("fichier_texte")) return true;
		if (niv_affichage.equals("file(text)")) return true;
		if (niv_affichage.equals("html")) return true;
		return false;
	}
	
	public boolean avecAffichageSql() {
		return niv_affichage.equals("sql");
	}
	
	public boolean avecAffichageFichierEnregistrement() {
		if (niv_affichage.equals("fichier_enregistrement")) return true;
		if (niv_affichage.equals("file(record)")) return true;
		return false;
	}
	
	// ----------------------------	
	// saisie	
	// ----------------------------
	
	public String getNiveauSaisie() {
		return niv_saisie;
	}
	
	public boolean avecSaisieElementaire() {
		if (niv_saisie.equals("elementaire")) return true;
		if (niv_saisie.equals("elementary")) return true;
		//if (niv_saisie.equals("Rpad")) return true;
		return false;
	}
	
	public boolean sansSaisie() {
		if (niv_saisie.equals("")) return true;
		if (niv_saisie.equals("sans")) return true;
		return false;
	}
	
	public boolean avecProcedureSaisie() {
		return niv_saisie.equals("procedure");
	}
	
	public boolean avecFonctionsSaisie() {
		if (niv_saisie.equals("fonctions")) return true;
		if (niv_saisie.equals("functions")) return true;
		if (niv_saisie.equals("fonction")) return true;
		if (niv_saisie.equals("function")) return true;
		return false;
	}
	
	public boolean avecSaisieFichierTexte() {
		if (niv_saisie.equals("fichier_texte")) return true;
		if (niv_saisie.equals("file(text)")) return true;
		return false;
	}
	
	public boolean avecSaisieFichierEnregistrement() {
		if (niv_saisie.equals("fichier_enregistrement")) return true;
		if (niv_saisie.equals("file(record)")) return true;
		return false;
	}
	
	public boolean avecSaisieFormulaire() {
		if (niv_saisie.equals("formulaire")) return true;
		if (niv_saisie.equals("form")) return true;
		return false;
	}
	
	public boolean avecSaisieSql() {
		return niv_saisie.equals("sql");
	}
	
	public void setOption(String nom, String valeur) {
		if (nom.equalsIgnoreCase("calcul")) {
			niv_calcul = valeur;
		}
		else if (nom.equalsIgnoreCase("saisie")) {
			niv_saisie = valeur;
		}
		else if (nom.equalsIgnoreCase("affichage")) {
			niv_affichage = valeur;
		}
		else if (nom.equalsIgnoreCase("donnees")) {
			donnees = valeur;
		}
		else if (nom.equalsIgnoreCase("resultats")) {
			resultats = valeur;
		}
		else if (nom.equalsIgnoreCase("enregistrement")) {
			niv_groupement = "enregistrement"; 
			nom_groupe=valeur;
		}
		else if (nom.equalsIgnoreCase("classe")) {
			niv_groupement = "classe"; 
			nom_groupe=valeur;
		}
	}
	
}
