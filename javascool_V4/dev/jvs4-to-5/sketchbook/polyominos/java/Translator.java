/*======================================================================
 * Translator.java			Lionel Avon	 2012-06-09
 * 	Time-stamp:					"2012-12-28"
 *
 * Traducteur de Javascool (proglet Polyominos) vers Java
 * selon les spécifications de JavaScool
 *
 * D'après des exemples trouvés sur http://javascool.gforge.inria.fr
 *======================================================================
 */

package org.javascool.proglets.polyominos;

/*
 * Traducteur de Jvs vers Java
 * pour manipuler la proglet polyominos
 *
 * @see <a href="Translator.java.html">code source</a>
 * @serial exclude
 */
public class Translator extends org.javascool.core.Translator {
    @Override
	public String getImports() {
	return "import org.dnsalias.avon.polyominos.*; "
	    + "import java.lang.Math.*; "
	    + "import java.math.*; "
	    + "import java.util.*; "
	    /*
	    + "import java.text.*; "
	    */
	    ;
    }
    
    @Override
	public String translate(String code) {
	return code;
    }
}
