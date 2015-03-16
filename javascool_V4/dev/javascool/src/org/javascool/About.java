package org.javascool;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import org.javascool.macros.Macros;

/**
 * Définit le panneau de "about" de javascool et son bouton d'ouverture.
 */
public class About {

	/** Titre de l'application. */
	public static final String title = "Java's Cool 4.2.β";
	/** Logode l'application. */
	public static final String logo = "org/javascool/widgets/icons/logo.png";

	/** Numéro de révision de l'application. */
	public static final String revision = "4.2.beta"; 

	/**
	 * Renvoie une bouton (sous forme de logo) qui affiche le panneau de about
	 * lors de son clic.
	 */
	public static JLabel getAboutMessage() {
		final JLabel logoLabel = new JLabel(new ImageIcon(Macros.getIcon(About.logo).getImage().getScaledInstance(26,26,  java.awt.Image.SCALE_SMOOTH)));
		logoLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				About.showAboutMessage();
			}
		});
		return logoLabel;
	}

	/** Affiche le message de "about". */
	public static void showAboutMessage() {
		Macros.message(
				About.title
						+ " ("
						+ About.revision
						+ ") est un logiciel conçu par : <br/><center>"
						+ "Philippe VIENNE<br/>"
						+ "Guillaume MATHERON<br/>"
						+ " et Inria<br/>"
						+ "</center>"
						+ "en collaboration avec David Pichardie, Philippe Lucaud, etc.. et le conseil de Robert Cabane<br/><br/>"
						+ "Il est distribué sous les conditions de la licence CeCILL et GNU GPL V3<br/>" +
						"ATTENTION CETTE VERSION EST EN BETA TEST",
				true);
	}
}
