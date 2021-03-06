/*******************************************************************************
* Thievery.Vieville@sophia.inria.fr, Copyright (C) 2009.  All rights reserved. *
*******************************************************************************/

package proglet.javaprog;

// Used to define the gui
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JLayeredPane;
import javax.swing.JLabel;
import java.awt.Dimension;

import org.javascool.Utils;

/** Définit une proglet javascool qui permet d'utiliser toute les classes des swings.
 * @see <a href="doc-files/about-proglet.htm">Description</a>
 * @see <a href="doc-files/the-proglet.htm">La proglet</a>
 * @see <a href="JavaPanel.java.html">code source</a>
 * @see <a href="http://java.sun.com/docs/books/tutorial/uiswing">Java Swing tutorial</a>
 * @see <a href="http://java.sun.com/javase/6/docs/api/javax/swing/package-summary.html">Java Swing API</a>
 * @serial exclude
 */
public class JavaPanel implements org.javascool.Proglet {
  private JavaPanel() {}
  private static final long serialVersionUID = 1L;

  // This defines the panel to display
  private static class Panel extends JPanel {
    private static final long serialVersionUID = 1L;

    public Panel() {
      setLayout(new BorderLayout());
      pane = new JLayeredPane();
      add(pane, BorderLayout.CENTER);
    }
    public void reset() {
      remove(pane);
      pane = new JLayeredPane();
      add(pane, BorderLayout.CENTER);
    }
    JLayeredPane pane;
  }

  //
  // This defines the tests on the panel
  //

  /**/ public static void test() {
    new FleurEnRythme().run();
  }
  //
  // This defines the javascool interface
  //

  /** Nettoie le panneau d'affichage  la proglet. */
  public static void resetSwingPane() {
    panel.reset();
  }
  /** Renvoie le panneau d'affichage de la proglet. */
  public static JLayeredPane getSwingPane() {
    return panel.pane;
  }
  /** Crée et montre une icone sur le display en (x,y) de taille (w, h) à la profondeur p. */
  public static JLabel showIcon(String image, int x, int y, int w, int h, int p) {
    JLabel icon = new JLabel();
    icon.setIcon(Utils.getIcon(image));
    icon.setLocation(x, y);
    icon.setSize(w, h);
    panel.pane.add(icon, new Integer(p), 0);
    return icon;
  }
  /** Définition de l'interface graphique de la proglet. */
  public static final Panel panel = new Panel();
}
