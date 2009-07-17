/*******************************************************************************
 * Thievery.Vieville@sophia.inria.fr, Copyright (C) 2009.  All rights reserved. *
 *******************************************************************************/

// Used to define the gui
import javax.swing.JPanel;
import java.awt.Dimension;

/** Définit une proglet javascool qui permet d'expérimenter la recherche tortue ``logo´´.
 * Méthodes statiques à importer: <pre>
 * import static Tortue.reset;
 * import static Tortue.plot;
 * </pre>
 * <a href="http://fr.wikipedia.org/wiki/Logo_(langage)#Primitives_Logo">ref</a>
 */
public class Tortue {

  // This defines the panel to display
  private static class Panel extends JPanel {
    public Panel() {
      setPreferredSize(new Dimension(512, 512));
      add(tortue = new TraceOutput());
    }
    public TraceOutput tortue;
  }

  //
  // This defines the tests on the panel
  //

  /** Test du panel. */
  static void test() {
    reset();
    panel.tortue.set(1000);
    for(int t = 0; t < 9000; t++) {
      add(Math.cos(0.0015 * t), Math.sin(0.0045 * t), (t / 1000) % 10 );
      Proglet.sleep(10);
    }
  }

  //
  // This defines the javascool interface
  //

  /** Initialise le tracé. */
  static public void reset() {
    panel.tortue.reset(); 
  }

  /** Ajoute une valeur au tracé.
   * @param x Abcisse du tracé, dans [-1, 1].
   * @param y Ordonnée du tracé, dans [-1, 1].
   * @param c Couleur du tracé: 0 (noir), 1 (brun), 2 (rouge), 3 (orange), 4 (jaune), 5 (vert), 6 (bleu), 7 (violet), 8 (gris), 9 (blanc).
   */
  static public void add(double x, double y, int c) {
    panel.tortue.add(x, y, c);
  }

  public static final Panel panel = new Panel();
}
