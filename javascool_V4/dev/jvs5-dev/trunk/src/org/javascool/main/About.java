package org.javascool.main;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;

import org.javascool.macros.Macros;

/**
 * Définit le panneau de "about" de javascool et son bouton d'ouverture.
 * 
 * @see <a href="About.java.html">source code</a>
 * @serial exclude
 */
public class About {

    /** Titre de l'application. */
    public static String title = "Java's Cool 5 - DEV";
    /** Logode l'application. */
    public static final String logo = "org/javascool/widgets/icons/logo.png";

    /** Numéro de révision de l'application. */
    public static final String revision = "5.0"; // @revision

    // automatiquement mis à
    // jour par ant -f
    // work/build.xml
    // classes

    /** Affiche le message de "about". */
    public static void showAboutMessage() {
	Macros.message(About.title + " (" + About.revision + ") est un logiciel conçu par : <br/><center>"
		+ "Philippe VIENNE<br/>" + "Guillaume MATHERON<br/>" + " et Inria<br/>" + "</center>"
		+ "en collaboration avec David Pichardie, Philippe Lucaud, etc.. et le conseil de Robert Cabane<br/><br/>"
		+ "Il est distribué sous les conditions de la licence CeCILL et GNU GPL V3<br/>", true);
    }

    /**
     * Renvoie une bouton (sous forme de logo) qui affiche le panneau de about
     * lors de son clic.
     */
    public static JLabel getAboutMessage() {
	JLabel logoLabel = new JLabel(Macros.getIcon(About.logo));
	logoLabel.addMouseListener(new MouseAdapter() {
	    @Override
	    public void mouseClicked(MouseEvent e) {
		About.showAboutMessage();
	    }
	});
	return logoLabel;
    }
}
