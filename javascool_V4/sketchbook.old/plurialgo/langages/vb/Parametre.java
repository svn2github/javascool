/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2013.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.langages.vb;

import org.javascool.proglets.plurialgo.divers.*;
import org.javascool.proglets.plurialgo.langages.modele.*;

/**
 * Cette classe hérite de la classe homonyme du modèle.
*/
public class Parametre extends ModeleParametre {
	
	public Parametre() {
	}
	
	public void ecrire(Programme prog, StringBuffer buf) {
		if (this.isIn()) {
			if (this.isSimple()) {
				Divers.ecrire(buf, "ByVal ");
			}
		}
		else {
			Divers.ecrire(buf, "ByRef ");
		}
		Divers.ecrire(buf, nom);
		Divers.ecrire(buf, " ");
		this.ecrireType(prog, buf, this);
	}
	
	public void ecrireType(Programme prog, StringBuffer buf, org.javascool.proglets.plurialgo.langages.modele.InfoTypee info) {
		if (info.isEntier()) Divers.ecrire(buf, "as integer");
		else if (info.isReel()) Divers.ecrire(buf, "as double");
		else if (info.isTexte()) Divers.ecrire(buf, "as String");
		else if (info.isBooleen()) Divers.ecrire(buf, "as boolean");
		else if (info.isTabEntiers()) Divers.ecrire(buf, "() as integer");
		else if (info.isTabReels()) Divers.ecrire(buf, "() as double");
		else if (info.isTabTextes()) Divers.ecrire(buf, "() as string");
		else if (info.isTabBooleens()) Divers.ecrire(buf, "() as boolean");
		else if (info.isTabClasse(prog)) {
			Classe cl = (Classe) info.getClasseOfTab(prog);
			Divers.ecrire(buf, "() as " + cl.nom );
		}
		else if (info.isMatEntiers()) Divers.ecrire(buf, "() as integer");
		else if (info.isMatReels()) Divers.ecrire(buf, "() as double");
		else if (info.isMatTextes()) Divers.ecrire(buf, "() as string");
		else if (info.isMatBooleens()) Divers.ecrire(buf, "() as boolean");
		else if (info.isMatClasse(prog)) {
			Classe cl = (Classe) info.getClasseOfTab(prog);
			Divers.ecrire(buf, "() as " + cl.nom);
		}
		else Divers.ecrire(buf, "as " + info.type);
	}
	
}
