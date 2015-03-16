/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2013.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.langages.java;

import java.util.*;
import org.javascool.proglets.plurialgo.divers.*;


/**
 * Cette classe hérite de la classe homonyme du modèle.
*/
public class Classe extends org.javascool.proglets.plurialgo.langages.modele.Classe {
	
	public Classe() {
	}
	
	public void ecrire(Programme prog, StringBuffer buf, int indent) {
		Divers.ecrire(buf, "public class " + nom + " {", indent);
		if ((proprietes.size()>0))
			prog.commenter(buf, "proprietes", indent+1);
		for (Iterator<org.javascool.proglets.plurialgo.langages.modele.Variable> iter=proprietes.iterator(); iter.hasNext();) {
			Variable prop = (Variable) iter.next();
			prop.ecrire(prog, buf, indent+1);
		}
		if ((operations.size()>0))
			prog.commenter(buf, "methodes", indent+1);
		for (Iterator<org.javascool.proglets.plurialgo.langages.modele.Operation> iter=operations.iterator(); iter.hasNext();) {
			Operation oper = (Operation) iter.next();
			oper.ecrire(prog, buf, indent+1);
		}
		if ((constructeurs.size()>0))
			prog.commenter(buf, "constructeurs", indent+1);
		for (Iterator<org.javascool.proglets.plurialgo.langages.modele.Constructeur> iter=constructeurs.iterator(); iter.hasNext();) {
			Constructeur constr = (Constructeur) iter.next();
			constr.ecrire(prog, buf, indent+1);
		}
		Divers.ecrire(buf, "}", indent);
	}
	
	public void ecrireEnregistrement(Programme prog, StringBuffer buf, int indent) {
		Divers.ecrire(buf, "class " + nom + " {", indent);
		for (Iterator<org.javascool.proglets.plurialgo.langages.modele.Variable> iter=proprietes.iterator(); iter.hasNext();) {
			Variable prop = (Variable) iter.next();
			prop.ecrire(prog, buf, indent+1);
		}
		Divers.ecrire(buf, "}", indent);
	}
	
}
