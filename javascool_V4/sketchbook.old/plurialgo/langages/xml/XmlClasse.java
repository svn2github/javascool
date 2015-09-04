/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2013.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.langages.xml;

import java.util.*;
import org.javascool.proglets.plurialgo.langages.modele.*;

/**
 * Cette classe hérite de la classe homonyme du modèle.
*/
public class XmlClasse extends ModeleClasse {
	
	public XmlClasse() {
	}
	
	public XmlClasse(String nom, boolean isRecord) {
		this.nom = nom;
		this.isRecord = isRecord;
	}
	
	// -----------------------------------------------------------
	// création des propriétés de la classe
	// -----------------------------------------------------------
	
	ArrayList<InfoTypee> creerProprietes(ArrayList<InfoTypee> infos, ArrayList<InfoTypee> infos_prop) {
		if (infos_prop==null) {
			return this.creerProprietes(infos);
		}
		ArrayList<InfoTypee> infos_bis = new ArrayList<InfoTypee>();
		// trouver le type et le mode des futures proprietes
		for(Iterator<InfoTypee> iter=infos_prop.iterator(); iter.hasNext(); ) {
			InfoTypee info_prop = iter.next();
			InfoTypee info = InfoTypeeList.getInfo(info_prop.nom, infos);
			if (info!=null) {
				info_prop.type = info.type;
				info_prop.mode = info.mode;
			}
		}
		// creer les proprietes et les variables
		XmlVariable prop;
		InfoTypee info_obj;
		for(Iterator<InfoTypee> iter=infos.iterator(); iter.hasNext(); ) {
			InfoTypee info = iter.next();
			InfoTypee info_prop = InfoTypeeList.getInfo(info.nom, infos_prop);
			if (info_prop==null) {	// ce n'est pas une propriete
				infos_bis.add(info);
			}
			else {	// c'est une propriete
				InfoTypeeList liste = new InfoTypeeList();
				liste.addVariables(proprietes);
				if (liste.getInfo(info.nom)==null) {
					prop = new XmlVariable(info.nom, info.type, null);
					proprietes.add(prop);
					info_obj = InfoTypeeList.getInfo(indicerObjet(-1), infos_bis);
					if (info_obj==null) {
						info_obj = new InfoTypee(indicerObjet(-1), this.nom, info.mode);
						infos_bis.add(info_obj);
					}
					else {
						if (info_obj.isIn() && info.isOut()) info_obj.mode = "INOUT";
						else if (info_obj.isOut() && info.isIn()) info_obj.mode = "INOUT";
						else if (info_obj.isInOut() || info.isInOut()) info_obj.mode = "INOUT";
						else if (!info.isAutre()) info_obj.mode = info.mode;
					}
					if ( (info_obj.dim==null) && (info.dim!=null)) {
						info_obj.dim = info.dim;
					}
				}
			}
		}
		//Classe.afficherListe(infos_bis);
		return infos_bis;
	}
	
	private String indicerObjet(int indice) {
		if (indice<0) return "objet";
		else return "objet" + indice;
	}
	
	final int getIndice(String nom) {
		int indice = -1;
		try {
			int fin = nom.length();
			String chiffre = nom.substring(fin-1, fin);
			indice = new Integer(chiffre).intValue();
		}
		catch (Exception e) {}
		return indice;
	}
	
	final String getSansIndice(String nom) {
		int indice = getIndice(nom);
		if (indice>=0) {
			return nom.substring(0, nom.length()-1);
		}
		return nom;
	}
		
	private ArrayList<InfoTypee> creerProprietes(ArrayList<InfoTypee> infos) {
		ArrayList<InfoTypee> infos_bis = new ArrayList<InfoTypee>();
		XmlVariable prop;
		InfoTypee info_obj;
		for(Iterator<InfoTypee> iter=infos.iterator(); iter.hasNext(); ) {
			InfoTypee info = iter.next();
			int indice = getIndice(info.nom);
			if (indice<0) {
				infos_bis.add(info);
			}
			else {
				info.nom = getSansIndice(info.nom);
				InfoTypeeList liste = new InfoTypeeList();
				liste.addVariables(proprietes);
				if (liste.getInfo(info.nom)==null) {
					prop = new XmlVariable(info.nom, info.type, null);
					proprietes.add(prop);
					info_obj = InfoTypeeList.getInfo(indicerObjet(indice), infos_bis);
					if (info_obj==null) {
						info_obj = new InfoTypee(indicerObjet(indice), this.nom, info.mode);
						infos_bis.add(info_obj);
					}
					else {
						if (info_obj.isIn() && info.isOut()) info_obj.mode = "INOUT";
						else if (info_obj.isOut() && info.isIn()) info_obj.mode = "INOUT";
						else if (info_obj.isInOut() || info.isInOut()) info_obj.mode = "INOUT";
						else if (!info.isAutre()) info_obj.mode = info.mode;
						//else info_obj.mode = info.mode;
					}
				}
				else {
					info_obj = InfoTypeeList.getInfo(indicerObjet(indice), infos_bis);
					if (info_obj==null) {
						info_obj = new InfoTypee(indicerObjet(indice), this.nom, info.mode);
						infos_bis.add(info_obj);
					}
					else {
						if (info_obj.isIn() && info.isOut()) info_obj.mode = "INOUT";
						else if (info_obj.isOut() && info.isIn()) info_obj.mode = "INOUT";
						else if (info_obj.isInOut() || info.isInOut()) info_obj.mode = "INOUT";
						else if (!info.isAutre()) info_obj.mode = info.mode;
					}
				}
				if ( (info_obj.dim==null) && (info.dim!=null)) {
					info_obj.dim = info.dim;
				}
			}
		}
		//Classe.afficherListe(infos_bis);
		return infos_bis;
	}

	static void afficherListe(ArrayList<InfoTypee> infos) {
		System.out.println("----------------");
		for(Iterator<InfoTypee> iter=infos.iterator(); iter.hasNext(); ) {
			InfoTypee info = iter.next();
			System.out.print("nom:" + info.nom + " ");
			if (info.type!=null) System.out.print("type:" + info.type + " ");
			if (info.mode!=null) System.out.print("mode:" + info.mode + " ");
			if (info.dim!=null) System.out.print("dim:" + info.dim + " ");
			System.out.println();
		}
		System.out.println("----------------");
	}
	
	// -----------------------------------------------------------
	// fusion de classes
	// -----------------------------------------------------------

	void fusionner(XmlClasse cl) {
		if (!this.nom.equals(cl.nom)) return;
		for(Iterator<ModeleOperation> iter_oper=cl.operations.iterator();iter_oper.hasNext();) {
			XmlOperation oper = (XmlOperation) iter_oper.next();
			if (this.getOperation(oper.nom)==null) {
				this.operations.add(oper);
			}
		}
		for(Iterator<ModeleVariable> iter_prop=cl.proprietes.iterator();iter_prop.hasNext();) {
			XmlVariable prop = (XmlVariable) iter_prop.next();
			if (this.getPropriete(prop.nom)==null) {
				this.proprietes.add(prop);
			}
		}
	}
}
