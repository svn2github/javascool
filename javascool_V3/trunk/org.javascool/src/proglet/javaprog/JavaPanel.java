/*******************************************************************************
 * Thievery.Vieville@sophia.inria.fr, Copyright (C) 2009.  All rights reserved. *
 *******************************************************************************/

package proglet.javaprog;

// Used to define the gui
import javax.swing.JPanel;
import javax.swing.JLayeredPane;
import java.awt.Dimension;

/** Définit une proglet javascool qui permet d'utiliser toute les classes des swings.
 * <p><a href="doc-files/index.htm">En savoir plus ..</a></p>
 * @see <a href="http://java.sun.com/docs/books/tutorial/uiswing">Java Swing tutorial</a>
 * @see <a href="http://java.sun.com/javase/6/docs/api/javax/swing/package-summary.html">Java Swing API</a>
 * @see <a href="JavaPanel.java.html">code source</a>
 */
public class JavaPanel implements org.javascool.Proglet { private JavaPanel() { }
  private static final long serialVersionUID = 1L;

  // This defines the panel to display
  private static class Panel extends JPanel {
    private static final long serialVersionUID = 1L;

    public Panel() {
       pane = new JLayeredPane(); pane.setPreferredSize(new Dimension(width, height)); add(pane);
    }
    JLayeredPane pane;
    static final int width = 512, height = 512; 
  }

  //
  // This defines the tests on the panel
  //

  /**/public static void test() {
  }

  //
  // This defines the javascool interface 
  //

  /** Renvoie le panneau. */
  public static JLayeredPane getSwingPane() {
    return panel.pane;
  }

  /** Définition de l'interface graphique de la proglet. */
  public static final Panel panel = new Panel();
}
