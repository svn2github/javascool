/*******************************************************************************
 * Thierry.Vieville@sophia.inria.fr, Copyright (C) 2009.  All rights reserved. *
 *******************************************************************************/

package proglet;

// Used to define the gui
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.BorderLayout;

/** Définit une proglet javascool qui permet d'expérimenter avec des valeurs et signaux numériques.
 * Fichiers utilisés: <pre>
 * img/scope_screen.png
 * </pre>
 * @see <a href="Scope.java">code source</a>
 */
public class Scope implements Proglet { private Scope() { }
  private static final long serialVersionUID = 1L;

  // This defines the panel to display
  private static class Panel extends JPanel {
    private static final long serialVersionUID = 1L;

    public Panel() {
      setPreferredSize(new Dimension(560, 620));
      add(scope = new CurveOutput());
    }
    public CurveOutput scope;
  }

  //
  // This defines the tests on the panel
  //

  /** Test du panel. */
  static void test() {
    scopeReset();
    for(double x = -1; x <= 1; x += 0.001) {
      scopeSet(x, 0.5 * Math.sin(10 * x) + 0.5, 6);
      scopeSet(x, -Math.exp(-(x + 1)), 7);
    }
  }

  //
  // This defines the javascool interface
  //

  /** Initialise le tracé. */
  static public void scopeReset() {
    panel.scope.reset();
  }

  /** Change la valeur d'un point du tracé.
   * @param x Abcisse de la courbe, dans [-1, 1].
   * @param y Ordonnée de la courbe, dans [-1, 1].
   * @param c Numéro de la courbe: 0 (noir), 1 (brun), 2 (rouge), 3 (orange), 4 (jaune), 5 (vert), 6 (bleu), 7 (violet), 8 (gris), 9 (blanc).
   */
  static public void scopeSet(double x, double y, int c) {
    panel.scope.add(x, y, c);
  }

  /** Ajoute un chaîne de caratère au tracé.
   * @param x Abcisse du coin inférieur gauche de la chaîne, dans [-1, 1].
   * @param y Ordonnée du coin inférieur gauche de la chaîne, dans [-1, 1].
   * @param s Valeur de la chaîne de caratère.
   * @param c Couleur de la chaîne de caratère: 0 (noir), 1 (brun), 2 (rouge), 3 (orange), 4 (jaune), 5 (vert), 6 (bleu), 7 (violet), 8 (gris), 9 (blanc).
   */
  static public void scopeAdd(double x, double y, String s, int c) {
    panel.scope.add(x, y, s, c);
  }

  /** Trace un rectangle. 
   * @param xmin Abcisse inférieure gauche, dans [-1, 1].
   * @param ymin Ordonnée inférieure gauche, dans [-1, 1].
   * @param xmax Abcisse supérieure droite, dans [-1, 1].
   * @param ymax Ordonnée supérieure droite, dans [-1, 1].
   * @param c Numéro de la courbe: 0 (noir), 1 (brun), 2 (rouge), 3 (orange), 4 (jaune), 5 (vert), 6 (bleu), 7 (violet), 8 (gris), 9 (blanc).
   */
  static public void scopeAddRectangle(double xmin, double ymin, double xmax, double ymax, int c) {
    scopeAddLine(xmin, ymin, xmax, ymin, c);
    scopeAddLine(xmax, ymin, xmax, ymax, c);
    scopeAddLine(xmax, ymax, xmin, ymax, c);
    scopeAddLine(xmin, ymax, xmin, ymin, c);
  }

  /** Trace une ligne. 
   * @param x1 Abcisse du 1er point, dans [-1, 1].
   * @param y1 Ordonnée du 1er point, dans [-1, 1].
   * @param x2 Abcisse du 2eme point, dans [-1, 1].
   * @param y2 Ordonnée du 2eme point, dans [-1, 1].
   * @param c Numéro de la courbe: 0 (noir), 1 (brun), 2 (rouge), 3 (orange), 4 (jaune), 5 (vert), 6 (bleu), 7 (violet), 8 (gris), 9 (blanc).
   */
  static public void scopeAddLine(double x1, double y1, double x2, double y2, int c) {
    panel.scope.add(x1, y1, x2, y2, c);
  }

  /** Définition de l'interface graphique de la proglet. */
  public static final Panel panel = new Panel();
}
