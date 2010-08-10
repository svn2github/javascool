/*******************************************************************************
 * Thierry.Vieville@sophia.inria.fr, Copyright (C) 2009.  All rights reserved. *
 *******************************************************************************/

package proglet.exosdemaths;

// Used to define the gui
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.BorderLayout;
import org.javascool.CurveOutput;
import org.javascool.NumberInput;

// Used to recall the runnable
import org.javascool.Jvs2Java;

/** Définit une proglet javascool qui permet d'expérimenter avec des valeurs et signaux numériques.
 * <p><applet code="org.javascool.ProgletApplet" archive="../../../javascool.jar" width="560" height="600"><param name="proglet" value="exosdemaths"/></applet></p>
 * @see <a href="CurveDisplay.java">code source</a>
 */
public class CurveDisplay implements org.javascool.Proglet { private CurveDisplay() { }
  private static final long serialVersionUID = 1L;

  // This defines the panel to display
  private static class Panel extends JPanel {
    private static final long serialVersionUID = 1L;

    public Panel() {
      super(new BorderLayout());
      setPreferredSize(new Dimension(560, 580));
      add(scope = new CurveOutput(), BorderLayout.NORTH);
      JPanel input = new JPanel(new BorderLayout());
      input.add(inputX = new NumberInput("X"), BorderLayout.NORTH);
      input.add(inputY = new NumberInput("Y"), BorderLayout.SOUTH);
      Runnable run1 = new Runnable() { public void run() {
	inputX.setValue(scope.getReticuleX()); inputY.setValue(scope.getReticuleY());
	Jvs2Java.run(true); 
      }};
      Runnable run2 = new Runnable() { public void run() {
	scope.setReticule(inputX.getValue(), inputY.getValue());
	Jvs2Java.run(true);
      }};
      scope.setRunnable(run1);
      inputX.setRunnable(run2);
      inputY.setRunnable(run2);
      add(input, BorderLayout.SOUTH);
      reset(1, 1);
     }
    public CurveOutput scope; public NumberInput inputX, inputY; 

    /** Resets the scope display and set scales.
     * @param Xscale Horizontal scale.
     * @param Yscale Vertical scale.
     */
    public void reset(double Xscale, double Yscale) {
      inputX.setScale(-Xscale, Xscale, 0.001);
      inputY.setScale(-Yscale, Yscale, 0.001);
      scope.reset(Xscale, Yscale);
    }
  }

  //
  // This defines the tests on the panel
  //

  /**/public static void test() {
    scopeReset();
    for(double x = -1; x <= 1; x += 0.001) {
      scopeSet(x, 0.5 * Math.sin(10 * x) + 0.5, 6);
      scopeSet(x, -Math.exp(-(x + 1)), 7);
    }
  }

  //
  // This defines the javascool interface
  //

  /** Initialise le tracé. 
   * @param X Echelle maximale horizontale, l'abscisse sera tracée dans [-X, X], par défaut [-1, 1]. 
   * @param Y Echelle maximale verticale, l'ordonnée sera tracée dans [-Y, Y], par défaut [-1, 1].
   */
  public static void scopeReset(double X, double Y) {
    panel.reset(X, Y);
  }
  /**/public static void scopeReset() {
    panel.reset(1, 1);
  }

  /** Change la valeur d'un point du tracé.
   * @param x Abcisse de la courbe, dans [-X, X], par défaut [-1, 1].
   * @param y Ordonnée de la courbe, dans [-Y, Y], par défaut [-1, 1].
   * @param c Numéro de la courbe: 0 (noir), 1 (brun), 2 (rouge), 3 (orange), 4 (jaune), 5 (vert), 6 (bleu), 7 (violet), 8 (gris), 9 (blanc).
   */
  public static void scopeSet(double x, double y, int c) {
    panel.scope.add(x, y, c);
  }

  /** Ajoute un chaîne de caratère au tracé.
   * @param x Abcisse du coin inférieur gauche de la chaîne, dans [-X, X], par défaut [-1, 1].
   * @param y Ordonnée du coin inférieur gauche de la chaîne, dans [-Y, Y], par défaut [-1, 1].
   * @param s Valeur de la chaîne de caratère.
   * @param c Couleur de la chaîne de caratère: 0 (noir), 1 (brun), 2 (rouge), 3 (orange), 4 (jaune), 5 (vert), 6 (bleu), 7 (violet), 8 (gris), 9 (blanc).
   */
  public static void scopeAddString(double x, double y, String s, int c) {
    panel.scope.add(x, y, s, c);
  }
  /**/public static void scopeAddString(double x, double y, String s) {
    scopeAddString(x, y, s, 0);
  }
  /**/public static void scopeAdd(double x, double y, String s, int c) {
    scopeAddString(x, y, s, c);
  }

  /** Trace un rectangle. 
   * @param xmin Abcisse inférieure gauche, dans [-X, X], par défaut [-1, 1].
   * @param ymin Ordonnée inférieure gauche, dans [-Y, Y], par défaut [-1, 1].
   * @param xmax Abcisse supérieure droite, dans [-X, X], par défaut [-1, 1].
   * @param ymax Ordonnée supérieure droite, dans [-Y, Y], par défaut [-1, 1].
   * @param c Numéro de la courbe: 0 (noir), 1 (brun), 2 (rouge), 3 (orange), 4 (jaune), 5 (vert), 6 (bleu), 7 (violet), 8 (gris), 9 (blanc).
   */
  public static void scopeAddRectangle(double xmin, double ymin, double xmax, double ymax, int c) {
    scopeAddLine(xmin, ymin, xmax, ymin, c);
    scopeAddLine(xmax, ymin, xmax, ymax, c);
    scopeAddLine(xmax, ymax, xmin, ymax, c);
    scopeAddLine(xmin, ymax, xmin, ymin, c);
  }
  /**/public static void scopeAddRectangle(double xmin, double ymin, double xmax, double ymax) {
    scopeAddRectangle(xmin, ymin, xmax, ymax, 0);
  }

  /** Trace une ligne. 
   * @param x1 Abcisse du 1er point, dans [-X, X], par défaut [-1, 1].
   * @param y1 Ordonnée du 1er point, dans [-Y, Y], par défaut [-1, 1].
   * @param x2 Abcisse du 2eme point, dans [-X, X], par défaut [-1, 1].
   * @param y2 Ordonnée du 2eme point, dans [-Y, Y], par défaut [-1, 1].
   * @param c Numéro de la courbe: 0 (noir), 1 (brun), 2 (rouge), 3 (orange), 4 (jaune), 5 (vert), 6 (bleu), 7 (violet), 8 (gris), 9 (blanc).
   */
  public static void scopeAddLine(double x1, double y1, double x2, double y2, int c) {
    panel.scope.add(x1, y1, x2, y2, c);
  }
  /**/public static void scopeAddLine(double x1, double y1, double x2, double y2) {
    scopeAddLine(x1, y1, x2, y2, 0);
  }

  /** Trace un  cercle. 
   * @param x Abcisse du centre, dans [-X, X], par défaut [-1, 1].
   * @param y Ordonnée du centre, dans [-Y, Y], par défaut [-1, 1].
   * @param r Rayon du cercle.
   * @param c Numéro de la courbe: 0 (noir), 1 (brun), 2 (rouge), 3 (orange), 4 (jaune), 5 (vert), 6 (bleu), 7 (violet), 8 (gris), 9 (blanc).
   */
  public static void scopeAddCircle(double x, double y, double r, int c) {
    panel.scope.add(x, y, r, c);
  }
  /**/public static void scopeAddCircle(double x, double y, double r) {
    scopeAddCircle(x, y, r, 0);
  }

  /** Renvoie la valeur horizontale du réticule. */
  public static double scopeX() { return panel.inputX.getValue(); }

  /** Renvoie la valeur verticale du réticule. */
  public static double scopeY() { return panel.inputY.getValue(); }

  /** Définition de l'interface graphique de la proglet. */
  public static final Panel panel = new Panel();
}
