/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2013.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.langages.php;

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
		Divers.ecrire(buf, "class " + nom + " {", indent);
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
			oper.ecrire(prog, buf, indent+1);
		}
		if ((constructeurs.size()>0))
			prog.commenter(buf, "constructeurs", indent+1);
		for (Iterator<ModeleConstructeur> iter=constructeurs.iterator(); iter.hasNext();) {
			Constructeur constr = (Constructeur) iter.next();
			constr.ecrire(prog, buf, indent+1);
		}
		Divers.ecrire(buf, "}", indent);
	}
	
}
