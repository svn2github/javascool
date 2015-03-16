/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2013.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.langages.modele;

import java.util.ArrayList;
import java.util.StringTokenizer;

import org.javascool.proglets.plurialgo.divers.*;


/**
 * Cette classe doit être étendue pour chaque langage de programmation implanté.
*/
public class ModeleArgument extends InfoTypee {	
	
	// variables utilisées par BindingCastor
	public ArrayList<ModeleInstruction> instructions;	// pour arguments de type fichier
	
	public ModeleArgument() {
		instructions = new ArrayList<ModeleInstruction>();
	}
	
	public ModeleArgument(String nom, String type, String mode) {
		instructions = new ArrayList<ModeleInstruction>();
		this.nom = nom;
		this.type = type;
		this.mode = mode;
	}
	
	public void ecrire(ModeleProgramme prog, StringBuffer buf) {
		Divers.ecrire(buf, nom);
	}
	
	// ---------------------------------------------
	// lecture et ecriture de tableaux
	// ---------------------------------------------	
	
	public final String getDim(int dim) {
		if (mode==null) return null;
		if (mode.equals("")) return null;
		if (mode.equalsIgnoreCase("MAX_TAB")) return "MAX_TAB";
		if (mode.equalsIgnoreCase("MAXTAB")) return "MAX_TAB";
		int i=mode.indexOf(",");
		if ((i<0) && (dim==1)) return mode;
		if ((i<0) && (dim>1)) return null;
		if (dim==1) {
			return mode.substring(0,i);
		}
		else {
			ModeleArgument arg = new ModeleArgument(this.nom, this.type, mode.substring(i+1));
			return arg.getDim(dim-1);
		}
	}
	
	public final String oteDim(int nbdim) {
		if (mode==null) return null;
		if (mode.equals("")) return null;
		int i=mode.indexOf(",");
		if (i<0) return null;
		if (nbdim==1) {
			return mode.substring(i+1);
		}
		else {
			ModeleArgument arg = new ModeleArgument(this.nom, this.type, mode.substring(i+1));
			return arg.oteDim(nbdim-1);
		}
	}
	
	// ---------------------------------------------
	// formulaires
	// ---------------------------------------------	
	
	public final boolean avecTexteListe() {
		if (type==null) return false;
		if (type.contains("TEXTE:") || type.contains("TEXT:")) return true;
		return false;
	}
	
	public final boolean avecTexteRadio() {
		if (type==null) return false;
		if (type.contains("TEXTE:") && (type.toUpperCase().endsWith("RADIO"))) return true;
		if (type.contains("TEXT:") && (type.toUpperCase().endsWith("RADIO"))) return true;
		return false;
	}
	
	public final String[] getTexteListe() {
		StringTokenizer tok = new StringTokenizer(type.substring(type.indexOf(":")+1), " ,");
		String liste[] = new String[tok.countTokens()];
		int i=0;
		while (tok.hasMoreTokens()) {
			liste[i]=tok.nextToken();
			i=i+1;
		}
		return liste;
	}
	
	public final String[] getTexteRadio() {
		StringTokenizer tok = new StringTokenizer(type.substring(type.indexOf(":")+1), " ,");
		String liste[] = new String[tok.countTokens()-1];
		for(int i=0; i<liste.length; i++) {
			liste[i]=tok.nextToken();
		}
		return liste;
	}
		
	// ---------------------------------------------
	// sql
	// ---------------------------------------------
	
	public final void ecrireSql(ModeleProgramme prog, StringBuffer bufInto, StringBuffer bufVal, StringBuffer bufPrep) {
		if ( isSimple() ) {
			ecrireSimpleSql(prog, bufInto, bufVal, bufPrep);
		}
		if ( isEnregistrement(prog) ) {
			ecrireEnregistrementSql(prog, bufInto, bufVal, bufPrep);
		}
		if ( isClasse(prog) ) {
			ecrireEnregistrementSql(prog, bufInto, bufVal, bufPrep);
		}
	}
	
	private void ecrireSimpleSql(ModeleProgramme prog, StringBuffer bufInto, StringBuffer bufVal, StringBuffer bufPrep) {
		Divers.ecrire(bufInto, this.nom + ",");
		Divers.ecrire(bufPrep, "?,"); 
		if (this.isTexte()) {
			Divers.ecrire(bufVal, "'" + this.nom + "'" + ",");
		}
		else {
			Divers.ecrire(bufVal, this.nom + ",");
		}
	}
	
	private void ecrireEnregistrementSql(ModeleProgramme prog, StringBuffer bufInto, StringBuffer bufVal, StringBuffer bufPrep) {
		
	}

	// ---------------------------------------------
	// Xml
	// ---------------------------------------------
	
	void ecrireXml(StringBuffer buf, int indent) {
		Divers.ecrire(buf, "<argument", indent);
		Divers.ecrireAttrXml(buf, "nom", nom);
		Divers.ecrireAttrXml(buf, "type", type);
		Divers.ecrireAttrXml(buf, "mode", mode);
		Divers.ecrire(buf, "/>");
	}
	
	void ecrireRetourXml(StringBuffer buf, int indent) {
		Divers.ecrire(buf, "<return", indent);
		Divers.ecrireAttrXml(buf, "nom", nom);
		Divers.ecrireAttrXml(buf, "type", type);
		Divers.ecrireAttrXml(buf, "mode", mode);
		Divers.ecrire(buf, "/>");
	}
	
	void ecrireObjetXml(StringBuffer buf, int indent) {
		Divers.ecrire(buf, "<objet", indent);
		Divers.ecrireAttrXml(buf, "nom", nom);
		Divers.ecrireAttrXml(buf, "type", type);
		Divers.ecrireAttrXml(buf, "mode", mode);
		Divers.ecrire(buf, "/>");
	}
	
	void ecrireOptionXml(StringBuffer buf, int indent) {
		Divers.ecrire(buf, "<option", indent);
		Divers.ecrireAttrXml(buf, "nom", nom);
		Divers.ecrireAttrXml(buf, "type", type);
		Divers.ecrireAttrXml(buf, "mode", mode);
		Divers.ecrire(buf, "/>");
	}

}
