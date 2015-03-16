/*======================================================================
 * Functions.java
 * 	Time-stamp:					"2014-03-22"
 * 
 * @author Lionel Avon
 * @since 2012-06-19
 *
 * Définition des fonctions de proglet polyominos
 * conformément aux spécifications de Javascool
 *======================================================================
 */

package org.javascool.proglets.polyominos;

import fr.free.hd.avon.polyominos.*;
import org.javascool.macros.Macros.*;

/**
 * Les fonctions de la proglet polyominos, accessibles depuis Javascool.
 *
 * @author Lionel Avon
 * @since 2012-06-19
 */
public class Functions {

    public static String version() {
        return "Proglet Polyominos, version du 22 mars 2014";
    }

    /*::::::::::::::::::::::::::::::
     * pour corriger un bug étrange :
     * erreur à l'exécution du programme jvs lorsqu'on utilise setOmbre()
     * sans avoir préalablement utilisé setRunnableMAJOmbre()
     */
    private static boolean runnableMAJOmbreSet = false;
    //::::::::::::::::::::::::::::::

    /**
     * @return l'instance de la proglet.
     */
    private static Panel getPane() {
        return org.javascool.macros.Macros.getProgletPane();
    }

    public static void setTexte(String s) {
        getPane().setTexte(s);
        getPane().repaint();
    }

    public static void setOmbre(Ombre ombre) {
	//::::::::::::::::::::::::::::::
	if (!runnableMAJOmbreSet) {
	    setRunnableMAJOmbre(new Runnable() { public void run() {}});
	}
	//::::::::::::::::::::::::::::::
	getPane().getPlanN2JPanel().setOmbre(ombre);
        getPane().repaint();
    }

    public static Ombre getOmbre() {
        return getPane().getPlanN2JPanel().getOmbre();
    }

    public static void setAssemblage(Assemblage assemblage) {
        getPane().getPlanN2JPanel().setAssemblage(assemblage);
        getPane().repaint();
    }

    public static void setRunnableGo(Runnable r) {
        getPane().setRunnableGo(r);
    }

    public static void setRunnableMAJOmbre(Runnable r) {
        getPane().getPlanN2JPanel().setRunnableMAJOmbre(r);
	runnableMAJOmbreSet = true;
    }

}
