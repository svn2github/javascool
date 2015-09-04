/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2014.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.langages.R;

import java.util.*;

import org.javascool.proglets.plurialgo.divers.*;
import org.javascool.proglets.plurialgo.langages.modele.*;

/**
 * Cette classe hérite de la classe homonyme du modèle.
 * 
 * @author Raffinat Patrick
*/
public class Classe extends ModeleClasse {
	
	public Classe() {
	}
	
	public void ecrire(Programme prog, StringBuffer buf, int indent) {
		new Constructeur();
			// pour forcer le chargement de classes : sinon Class.forName ne marche pas 
			// 		(voir methode creerObjet, classe AnalyseurXml du package langages.modele)
		Divers.indenter(buf, indent);
		Divers.ecrire(buf, "setClass(" + prog.quote(nom) + ", representation(");
		for (Iterator<ModeleVariable> iter=proprietes.iterator(); iter.hasNext();) {
			Variable prop = (Variable) iter.next();
			prop.ecrireRepresentation(prog, buf, -1);
			if (iter.hasNext()) Divers.ecrire(buf, ",");
		}
		Divers.ecrire(buf, "))");
		for (Iterator<ModeleOperation> iter=operations.iterator(); iter.hasNext();) {
			Operation oper = (Operation) iter.next();
			oper.ecrireSpec(prog, buf, indent);
			Divers.indenter(buf, indent);
			Divers.ecrire(buf, "setMethod(" + prog.quote(oper.nom) + ", ");
			Divers.ecrire(buf, prog.quote(this.nom) + ", ");
			oper.ecrireLambda(prog, buf, indent);
			Divers.ecrire(buf, ") ");
		}
	}

}
