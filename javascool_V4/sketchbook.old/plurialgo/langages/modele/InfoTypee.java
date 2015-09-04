/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2013.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.langages.modele;

import java.util.*;

/**
 * Cette classe permet de gérer les caractéristiques communes aux classes Variable, Argument et Parametre.
 * 
 * <p>
 * Elle est également utilisée par le paquetage (...langages.xml) 
 * chargé de créer et transformer des programmes. 
 * </p>
*/
public class InfoTypee extends Noeud {
	
	// variables utilisées par BindingCastor
	/**
	 * Nom de variable, de paramètre ou d'argument d'instruction.
	 */
	public String nom;
	/**
	 * Type simple (ENTIER, REEL, TEXTE, BOOLEEN), 
	 * vecteur (TAB_ENTIER, TAB_REEL, TAB_TEXTE, TAB_BOOLEEN), 
	 * matrice (MAT_ENTIER, MAT_REEL, MAT_TEXTE, MAT_BOOLEEN)...
	 */
	public String type;
	/**
	 * Nature d'un passage de paramètre (IN, OUT, INOUT) ou
	 * sauvegarde de l'attribut dim.
	 */
	public String mode;
	// autres variables
	/**
	 * Attribut (destiné uniquement au paquetage raffinat.inout.langages.xml) 
	 * indiquant le nombre d'éléments à lire ou à écrire pour un argument de
	 * type tableau.  
	 */
	public String dim;
	
	public InfoTypee() {
		nom = "";
		type = "REEL";
	}
	public InfoTypee(String nom) {
		this.nom = nom;
		type = "REEL";
	}
	public InfoTypee(String nom, String type, String mode) {
		this.nom = nom;
		this.type = type;
		this.mode = mode;
	}
	
	// ---------------------------------------------
	// Noeuds
	// ---------------------------------------------
	
	public final void parcoursEnfants() {}
	
	// ---------------------------------------------
	// tests sur les modes
	// ---------------------------------------------
	
	public final boolean isIn() {
		if (mode==null) return false;
		return mode.equalsIgnoreCase("IN");
	}
	
	public final boolean isOut() {
		if (mode==null) return false;
		return mode.equalsIgnoreCase("OUT");
	}
	
	public final boolean isInOut() {
		if (mode==null) return false;
		return mode.equalsIgnoreCase("INOUT");
	}
	
	public final boolean isAutre() {
		if (mode==null) return true;
		return mode.equalsIgnoreCase("AUTRE");
	}
	
	// ------------------------------------------
	// fonctions booléennes
	// ------------------------------------------
	
	public final boolean isType(String nom_type) {
		if (type==null) return false;
		if (type.equalsIgnoreCase("FICHIER_TEXTE") && nom_type.equalsIgnoreCase("TEXTE")) return false;
		if (type.equalsIgnoreCase("FILE_TEXT") && nom_type.equalsIgnoreCase("TEXT")) return false;
		if (type.startsWith(nom_type)) return true;
		if (type.endsWith(nom_type)) return true;
		return false;
	}
	
	public final boolean isEntier() {
		if (type==null) return false;
		return type.equalsIgnoreCase("ENTIER") || type.equalsIgnoreCase("INTEGER");
	}
	
	public final boolean isReel() {
		if (type==null) return false;
		return type.equalsIgnoreCase("REEL") || type.equalsIgnoreCase("REAL");
	}
	
	public final boolean isTexte() {
		if (type==null) return false;
		if (type.equalsIgnoreCase("TEXTE") || type.equalsIgnoreCase("TEXT")) return true;
		if (type.startsWith("TEXTE:") || type.startsWith("TEXT:")) return true;
		return false;
	}

	public final boolean isBooleen() {
		if (type==null) return false;
		return type.equalsIgnoreCase("BOOLEEN") || type.equalsIgnoreCase("BOOLEAN");
	}
	
	public final boolean isSimple() {
		return isEntier() || isReel() || isTexte() || isBooleen() ;
	}
	
	public final boolean isTabEntiers() {
		if (type==null) return false;
		return type.equalsIgnoreCase("TAB_ENTIER") || type.equalsIgnoreCase("TAB_INTEGER");
	}
	
	public final boolean isTabReels() {
		if (type==null) return false;
		return type.equalsIgnoreCase("TAB_REEL") || type.equalsIgnoreCase("TAB_REAL");
	}
	
	public final boolean isTabTextes() {
		if (type==null) return false;
		if (type.equalsIgnoreCase("TAB_TEXTE") || type.equalsIgnoreCase("TAB_TEXT")) return true;
		if (type.startsWith("TAB_TEXTE:") || type.startsWith("TAB_TEXT:")) return true;
		return false;
	}
	
	public final boolean isTabBooleens() {
		if (type==null) return false;
		return type.equalsIgnoreCase("TAB_BOOLEEN") || type.equalsIgnoreCase("TAB_BOOLEAN");
	}
	
	public final boolean isTabSimple() {
		return isTabEntiers() || isTabReels() || isTabTextes() || isTabBooleens() ;
	}
	
	public final boolean isMatEntiers() {
		if (type==null) return false;
		return type.equalsIgnoreCase("MAT_ENTIER") || type.equalsIgnoreCase("MAT_INTEGER");
	}
	
	public final boolean isMatReels() {
		if (type==null) return false;
		return type.equalsIgnoreCase("MAT_REEL") || type.equalsIgnoreCase("MAT_REAL");
	}
	
	public final boolean isMatTextes() {
		if (type==null) return false;
		if (type.equalsIgnoreCase("MAT_TEXTE") || type.equalsIgnoreCase("MAT_TEXT")) return true;
		if (type.startsWith("MAT_TEXTE:") || type.startsWith("MAT_TEXT:")) return true;
		return false;
	}
	
	public final boolean isMatBooleens() {
		if (type==null) return false;
		return type.equalsIgnoreCase("MAT_BOOLEEN") || type.equalsIgnoreCase("MAT_BOOLEAN");
	}
	
	public final boolean isMatSimple() {
		return isMatEntiers() || isMatReels() || isMatTextes() || isMatBooleens() ;
	}
	
	public final boolean isExpression() {
		if (type==null) return false;
		return type.equalsIgnoreCase("EXPR");
	}
	
	public final boolean isFichierTexte() {
		if (type==null) return false;
		if (type.equalsIgnoreCase("FICHIER_TEXTE")) return true;
		if (type.equalsIgnoreCase("FILE_TEXT")) return true;
		return false;
	}
	
	public final boolean isSQL() {
		if (type==null) return false;
		if (type.equalsIgnoreCase("SQL")) return true;
		return false;
	}
	
	public final boolean isFormulaire() {
		if (type==null) return false;
		if (type.equalsIgnoreCase("FORM")) return true;
		if (type.startsWith("FORM_")) return true;
		return false;
	}

	public final boolean isFichierClasse(ModeleProgramme prog) {
		return (getClasseOfFichier(prog)!=null);
	}
	
	public final boolean isTabClasse(ModeleProgramme prog) {
		ModeleClasse cl = getClasseOfTab(prog);
		return (cl!=null);
	}
	
	public final boolean isMatClasse(ModeleProgramme prog) {
		return (getClasseOfMat(prog)!=null);
	}
	
	public final boolean isNombre() {
		return (isEntier() || isReel());
	}
	
	public final boolean isTabNombres() {
		return (isTabEntiers() || isTabReels());
	}
	
	public final boolean isClasse(ModeleProgramme prog) {
		ModeleClasse cl = getClasse(prog);
		if (cl==null) return false;
		return cl.isClasse();
	}
	
	public final boolean isEnregistrement(ModeleProgramme prog) {
		ModeleClasse cl = getClasse(prog);
		if (cl==null) return false;
		return cl.isEnregistrement();
	}

	// ---------------------------------------------
	// fonctions get
	// ---------------------------------------------
	
	public final ModeleClasse getClasseOfFichier(ModeleProgramme prog) {
		if (type==null) return null;
		if (!type.startsWith("FICHIER_")) return null;
		String nom_cl = type.substring( type.indexOf("_") + 1 );
		for (Iterator<ModeleClasse> iter=prog.classes.iterator(); iter.hasNext();) {
			ModeleClasse cl = iter.next();
			if (cl.nom.equalsIgnoreCase(nom_cl)) return cl;
		}
		return null;
	}
	
	public final String getTypeOfTab() {
		if (type==null) return null;
		if (!type.startsWith("TAB_")) return null;
		return type.substring( type.indexOf("_") + 1 );
	}
	
	public final ModeleClasse getClasseOfTab(ModeleProgramme prog) {
		if (type==null) return null;
		if (!type.startsWith("TAB_")) return null;
		String nom_cl = type.substring( type.indexOf("_") + 1 );
		for (Iterator<ModeleClasse> iter=prog.classes.iterator(); iter.hasNext();) {
			ModeleClasse cl = iter.next();
			if (cl.nom.equalsIgnoreCase(nom_cl)) return cl;
		}
		return null;
	}
	
	public final String getTypeOfMat() {
		if (type==null) return null;
		if (!type.startsWith("MAT_")) return null;
		return type.substring( type.indexOf("_") + 1 );
	}
	
	public final ModeleClasse getClasseOfMat(ModeleProgramme prog) {
		if (type==null) return null;
		if (!type.startsWith("MAT_")) return null;
		String nom_cl = type.substring( type.indexOf("_") + 1 );
		for (Iterator<ModeleClasse> iter=prog.classes.iterator(); iter.hasNext();) {
			ModeleClasse cl = iter.next();
			if (cl.nom.equalsIgnoreCase(nom_cl)) return cl;
		}
		return null;
	}
	
	public final ModeleClasse getClasse(ModeleProgramme prog) {
		for (Iterator<ModeleClasse> iter=prog.classes.iterator(); iter.hasNext();) {
			ModeleClasse cl = iter.next();
			if (cl.nom.equalsIgnoreCase(this.type)) return cl;
		}
		return null;
	}

}
