/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2013.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.langages.carmetal;

import java.util.*;
import org.javascool.proglets.plurialgo.divers.*;
import org.javascool.proglets.plurialgo.langages.modele.*;


/**
 * Cette classe hérite de la classe homonyme du modèle.
*/
public class Classe extends ModeleClasse {
	
	public Classe() {
	}
	
	public void ecrire(Programme prog, StringBuffer buf, int indent) {
		new Constructeur();
			// pour forcer le chargement de classes : sinon Class.forName ne marche pas 
			// 		(voir methode creerObjet, classe AnalyseurXml du package langages.modele)
		Divers.ecrire(buf, "function " + nom + "()" + " {", indent);
		if ((proprietes.size()>0))
			prog.commenter(buf, "proprietes", indent+1);
		for (Iterator<ModeleVariable> iter=proprietes.iterator(); iter.hasNext();) {
			Variable prop = (Variable) iter.next();
			prop.ecrirePropriete(prog, buf, indent+1);
		}
		if ((operations.size()>0))
			prog.commenter(buf, "methodes", indent+1);
		for (Iterator<ModeleOperation> iter=operations.iterator(); iter.hasNext();) {
			Operation oper = (Operation) iter.next();
			Divers.ecrire(buf, "this." + oper.nom + "=" + oper.nom + ";", indent+1);
		}
		Divers.ecrire(buf, "}", indent);
		for (Iterator<ModeleOperation> iter=operations.iterator(); iter.hasNext();) {
			Operation oper = (Operation) iter.next();
			oper.ecrire(prog, buf, indent);
		}
	}
	
	public void ecrireEnregistrement(Programme prog, StringBuffer buf, int indent) {
		Divers.ecrire(buf, "function " + nom + "()" + " {", indent);
		for (Iterator<ModeleVariable> iter=proprietes.iterator(); iter.hasNext();) {
			Variable prop = (Variable) iter.next();
			prop.ecrirePropriete(prog, buf, indent+1);
		}
		Divers.ecrire(buf, "}", indent);
	}
	
}
