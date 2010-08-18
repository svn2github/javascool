/*******************************************************************************
 * Thievery.Vieville@sophia.inria.fr, Copyright (C) 2009.  All rights reserved. *
 *******************************************************************************/

package proglet.javaprog;

// Used to define the gui
import javax.swing.JPanel;
import javax.swing.JLayeredPane;
import java.awt.Dimension;

/** Définit une proglet javascool qui permet d'utiliser toute les classes des swings.
 * @see <a href="doc-files/about-proglet.htm">Description</a>
 * @see <a href="doc-files/the-proglet.htm">La proglet</a>
 * @see <a href="JavaPanel.java.html">code source</a>
 * @see <a href="http://java.sun.com/docs/books/tutorial/uiswing">Java Swing tutorial</a>
 * @see <a href="http://java.sun.com/javase/6/docs/api/javax/swing/package-summary.html">Java Swing API</a>
 * @serial exclude
 */
public class JavaPanel implements org.javascool.Proglet { private JavaPanel() { }
  private static final long serialVersionUID = 1L;

  // This defines the panel to display
  private static class Panel extends JPanel {
    private static final long serialVersionUID = 1L;

    public Panel() {
       pane = new JLayeredPane(); add(pane);
    }
    JLayeredPane pane;
  }

  //
  // This defines the tests on the panel
  //

  /**/public static void test() {
    // new FleurEnRythmeMain().run();
  }

  //
  // This defines the javascool interface 
  //

  /** Renvoie le panneau d'affichage de la proglet. */
  public static JLayeredPane getContentPane() {
    return panel.pane;
  }

  /** Définition de l'interface graphique de la proglet. */
  public static final Panel panel = new Panel();
}
