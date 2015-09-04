package org.javascool.proglets.plurialgo.langages.modele;

import org.javascool.proglets.plurialgo.divers.Divers;

/**
 * Cette classe doit être étendue pour chaque langage de programmation implanté.
*/
public class ModeleVariable extends InfoTypee {
	
	public ModeleVariable() {
	}
	
	public void ecrire(ModeleProgramme prog, StringBuffer buf, int indent) {
		// à redéfinir pour chaque langage
	}
	
	void ecrireXml(StringBuffer buf, int indent) {
		Divers.ecrire(buf, "<variable", indent);
		Divers.ecrireAttrXml(buf, "nom", nom);
		Divers.ecrireAttrXml(buf, "type", type);
		Divers.ecrire(buf, "/>");
	}
	
	void ecrireRetourXml(StringBuffer buf, int indent) {
		Divers.ecrire(buf, "<return", indent);
		Divers.ecrireAttrXml(buf, "nom", nom);
		Divers.ecrireAttrXml(buf, "type", type);
		Divers.ecrire(buf, "/>");
	}
	
	void ecrireProprieteXml(StringBuffer buf, int indent) {
		Divers.ecrire(buf, "<propriete", indent);
		Divers.ecrireAttrXml(buf, "nom", nom);
		Divers.ecrireAttrXml(buf, "type", type);
		Divers.ecrire(buf, "/>");
	}
}
