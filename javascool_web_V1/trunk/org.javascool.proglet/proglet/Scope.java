/*******************************************************************************
 * Thierry.Vieville@sophia.inria.fr, Copyright (C) 2009.  All rights reserved. *
 *******************************************************************************/

package proglet;

// Used to define the gui
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.BorderLayout;

/** Définit une proglet javascool qui permet d'expérimenter la recherche scopetomique.
 * Fichiers utilisés: <pre>
 * img/scope_screen.png
 * </pre>
 * @see <a href="Scope.java">code source</a>
 */
public class Scope { private Scope() { }
  private static final long serialVersionUID = 1L;

  /** Définition de l'interface graphique de la proglet. */
  public static final Panel panel = new Panel();

  // This defines the panel to display
  private static class Panel extends JPanel {
    private static final long serialVersionUID = 1L;

    public Panel() {
      super(new BorderLayout()); setPreferredSize(new Dimension(512, 421 + 50 * 2));
      add(scope = new CurveOutput(), BorderLayout.NORTH);
      JPanel panel = new JPanel(new BorderLayout());
      panel.add(new NumberInput("angle", -3.14, 3.14, 0.1, 3.14/2), BorderLayout.NORTH);
      panel.add(new NumberInput("force", -1, 1, 0.1, -0.5), BorderLayout.SOUTH);
      add(panel, BorderLayout.SOUTH);
    }
    public CurveOutput scope;
  }

  //
  // This defines the tests on the panel
  //

  /** Test du panel. */
  static void test() {
    reset();
    for(double x = -1; x <= 1; x += 0.001) {
      set(x, 0.5 * Math.sin(10 * x) + 0.5, 6);
      set(x, -Math.exp(-(x + 1)), 7);
    }
  }

  //
  // This defines the javascool interface
  //

  /** Initialise le tracé. */
  static public void reset() {
    panel.scope.reset();
  }

  /** Change la valeur d'un point du tracé.
   * @param x Abcisse de la courbe, dans [-1, 1].
   * @param y Ordonnée de la courbe, dans [-1, 1].
   * @param c Numéro de la courbe: 0 (noir), 1 (brun), 2 (rouge), 3 (orange), 4 (jaune), 5 (vert), 6 (bleu), 7 (violet), 8 (gris), 9 (blanc).
   */
  static public void set(double x, double y, int c) {
    panel.scope.set(x, y, c);
  }
}
