/**
 * 
 */
package org.javascool.main;

import java.awt.BorderLayout;

import javax.swing.JPanel;

/**
 * Le panneau contenant les composants media (Page web, console). Il contient
 * par défault la une RunToolbar et un WidgetPanel contenant les différents
 * médias.
 * 
 * @since 4.5
 */
public class MediaPanel extends JPanel {

    private static final long serialVersionUID = -1720019604962318148L;

    /** Crée un nouveau MediaPanel. */
    public MediaPanel() {
	super(new BorderLayout());
	add(RunToolbar.getInstance(), BorderLayout.NORTH);
	add(WidgetPanel.getInstance());
    }

    /** Instance de la classe. */
    private static MediaPanel panel;

    /** Permet d'avoir une instance unique de la classe. */
    public static MediaPanel getInstance() {
	if (MediaPanel.panel == null) {
	    MediaPanel.panel = new MediaPanel();
	}
	return MediaPanel.panel;
    }

}
