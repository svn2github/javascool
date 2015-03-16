/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2013.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.langages.xml;

/**
 * Cette interface énumère les méthodes que doit implanter un analyseur de code
 * (Visual Basic, Javascool ou Larp).
*/
public interface iAnalyseur {
	public Programme getProgramme();
	public StringBuffer getXml();
}
