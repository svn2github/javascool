/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2013.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.langages.modele;

import org.javascool.proglets.plurialgo.divers.Divers;

/**
 * Cette classe doit être étendue pour chaque langage de programmation implanté.
 * 
 * @author Raffinat Patrick
*/
public class Parametre extends InfoTypee {
	
	public Parametre() {
		mode = "IN";
	}

	// ---------------------------------------------
	// Xml
	// ---------------------------------------------
	
	void ecrireXml(StringBuffer buf, int indent) {
		Divers.ecrire(buf, "<parametre", indent);
		Divers.ecrireAttrXml(buf, "nom", nom);
		Divers.ecrireAttrXml(buf, "type", type);
		Divers.ecrireAttrXml(buf, "mode", mode);
		Divers.ecrire(buf, "/>");
	}
	
}
