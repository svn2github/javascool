package org.javascool.proglets.algoDeMaths;
import static org.javascool.macros.Macros.*;

/** Définit les fonctions de la proglet.
 * @see <a href="Functions.java.html">source code</a>
 * @serial exclude
 */
public class Functions {
  private static final long serialVersionUID = 1L;
   // @factory
   private Functions() {}
  private static Panel getPane() {
    return getProgletPane();
  }
  /** Initialise le tracé.
   * @param Xscale Echelle maximale horizontale, l'abscisse sera tracée dans [-Xscale, Xscale], par défaut [-1, 1].
   * @param Yscale Echelle maximale verticale, l'ordonnée sera tracée dans [-Yscale, Yscale], par défaut [-1, 1].
   */
  public static void reset(double Xscale, double Yscale) {
    getPane().inputX.setScale(-Xscale, Xscale, 0.001);
    getPane().inputY.setScale(-Yscale, Yscale, 0.001);
    getPane().scope.reset(0, 0, Xscale, Yscale);
    getPane().runnable = null;
    org.javascool.gui.Desktop.getInstance().focusOnProgletPanel();
  }
  /** Initialise le tracé.
   * @param Xmin Echelle minimale horizontale, l'abscisse sera tracée dans [-Xmin, Xmax], par défaut [-1, 1].
   * @param Xmax Echelle maximale horizontale, l'abscisse sera tracée dans [-Xmin, Xmax], par défaut [-1, 1].
   * @param Ymin Echelle minimale verticale, l'abscisse sera tracée dans [-Ymin, Ymay], par défaut [-1, 1].
   * @param Ymax Echelle maximale verticale, l'abscisse sera tracée dans [-Ymin, Ymax], par défaut [-1, 1].
   */
  public static void reset(double Xmin, double Xmax, double Ymin, double Ymax) {
    getPane().inputX.setScale(Xmin, Xmax, 0.001);
    getPane().inputY.setScale(Ymin, Ymax, 0.001);
    getPane().scope.reset((Xmin + Xmax) / 2, (Ymin + Ymax) / 2, (Xmax - Xmin) / 2, (Ymax - Ymin) / 2);
    getPane().runnable = null;
    org.javascool.gui.Desktop.getInstance().focusOnProgletPanel();
  }
  /*
   * @see #reset(double, double, double, double)
   */
  public static void reset() {
    reset(1, 1);
  }
  /** Change la valeur d'un point du tracé.
   * @param x Abcisse de la courbe, dans [-X, X], par défaut [-1, 1].
   * @param y Ordonnée de la courbe, dans [-Y, Y], par défaut [-1, 1].
   * @param c Numéro de la courbe: 0 (noir, défaut), 1 (brun), 2 (rouge), 3 (orange), 4 (jaune), 5 (vert), 6 (bleu), 7 (violet), 8 (gris), 9 (blanc).
   * @return L'objet graphique créé, utilisé pour détruire l'objet ensuite.
   */
  public static Object setPoint(double x, double y, int c) {
    return getPane().scope.add(x, y, c);
  }
  /*
   * @see #setPoint(double, double, int)
   */
  public static Object setPoint(double x, double y) {
    return setPoint(x, y, 0);
  }
  /** Ajoute une chaîne de caractères au tracé.
   * @param x Abcisse du coin inférieur gauche de la chaîne, dans [-X, X], par défaut [-1, 1].
   * @param y Ordonnée du coin inférieur gauche de la chaîne, dans [-Y, Y], par défaut [-1, 1].
   * @param s Valeur de la chaîne de caractères.
   * @param c Couleur du point: 0 (noir, défaut), 1 (brun), 2 (rouge), 3 (orange), 4 (jaune), 5 (vert), 6 (bleu), 7 (violet), 8 (gris), 9 (blanc).
   * @return L'objet graphique créé, utilisé pour détruire l'objet ensuite.
   */
  public static Object addString(double x, double y, String s, int c) {
    return getPane().scope.add(x, y, s, c);
  }
  /*
   * @see #addString(double, double, double, String, int)
   */
  public static Object addString(double x, double y, String s) {
    return addString(x, y, s, 0);
  }
  /** Trace un rectangle.
   * @param xmin Abcisse inférieure gauche, dans [-X, X], par défaut [-1, 1].
   * @param ymin Ordonnée inférieure gauche, dans [-Y, Y], par défaut [-1, 1].
   * @param xmax Abcisse supérieure droite, dans [-X, X], par défaut [-1, 1].
   * @param ymax Ordonnée supérieure droite, dans [-Y, Y], par défaut [-1, 1].
   * @param c Numéro de la courbe: 0 (noir, défaut), 1 (brun), 2 (rouge), 3 (orange), 4 (jaune), 5 (vert), 6 (bleu), 7 (violet), 8 (gris), 9 (blanc).
   * @return L'objet graphique créé, utilisé pour détruire l'objet ensuite.
   */
  public static Object addRectangle(double xmin, double ymin, double xmax, double ymax, int c) {
    return getPane().scope.addRectangle(xmin, ymin, xmax, ymax, c);
  }
  /*
   * @see #addRectangle(double, double, double, double, int)
   */
  public static Object addRectangle(double xmin, double ymin, double xmax, double ymax) {
    return addRectangle(xmin, ymin, xmax, ymax, 0);
  }
  /** Trace un block rectangulaire.
   * @param xmin Abcisse inférieure gauche, dans [-X, X], par défaut [-1, 1].
   * @param ymin Ordonnée inférieure gauche, dans [-Y, Y], par défaut [-1, 1].
   * @param xmax Abcisse supérieure droite, dans [-X, X], par défaut [-1, 1].
   * @param ymax Ordonnée supérieure droite, dans [-Y, Y], par défaut [-1, 1].
   * @param c Numéro de la couleur du block: 0 (noir, défaut), 1 (brun), 2 (rouge), 3 (orange), 4 (jaune), 5 (vert), 6 (bleu), 7 (violet), 8 (gris), 9 (blanc).
   * @return L'objet graphique créé, utilisé pour détruire l'objet ensuite.
   */
  public static Object addBlock(double xmin, double ymin, double xmax, double ymax, int c) {
    return getPane().scope.add(xmin, ymin, xmax - xmin, ymax - ymin, c, -1);
  }
  /*
   * @see #addBlock(double, double, double, double, int)
   */
  public static Object addBlock(double xmin, double ymin, double xmax, double ymax) {
    return addBlock(xmin, ymin, xmax, ymax, 0);
  }
  /** Trace une ligne.
   * @param x1 Abcisse du 1er point, dans [-X, X], par défaut [-1, 1].
   * @param y1 Ordonnée du 1er point, dans [-Y, Y], par défaut [-1, 1].
   * @param x2 Abcisse du 2eme point, dans [-X, X], par défaut [-1, 1].
   * @param y2 Ordonnée du 2eme point, dans [-Y, Y], par défaut [-1, 1].
   * @param c Numéro de la courbe: 0 (noir, défaut), 1 (brun), 2 (rouge), 3 (orange), 4 (jaune), 5 (vert), 6 (bleu), 7 (violet), 8 (gris), 9 (blanc).
   * @return L'objet graphique créé, utilisé pour détruire l'objet ensuite.
   */
  public static Object addLine(double x1, double y1, double x2, double y2, int c) {
    return getPane().scope.add(x1, y1, x2, y2, c);
  }
  /*
   * @see #addLine(double, double, double, double, int)
   */
  public static Object addLine(double x1, double y1, double x2, double y2) {
    return addLine(x1, y1, x2, y2, 0);
  }
  /** Trace une point.
   * @param x1 Abcisse du 1er point, dans [-X, X], par défaut [-1, 1].
   * @param y1 Ordonnée du 1er point, dans [-Y, Y], par défaut [-1, 1].
   * @param c Numéro de la courbe: 0 (noir, défaut), 1 (brun), 2 (rouge), 3 (orange), 4 (jaune), 5 (vert), 6 (bleu), 7 (violet), 8 (gris), 9 (blanc).
   * @return L'objet graphique créé, utilisé pour détruire l'objet ensuite.
   */
  public static Object addPoint(double x1, double y1, int c) {
    return getPane().scope.add(x1, y1, x1, y1, c);
  }
  /*
   * @see #addPoint(double, double, int)
   */
  public static Object addPoint(double x1, double y1) {
    return addPoint(x1, y1, 0);
  }
  /** Trace un  cercle.
   * @param x Abcisse du centre, dans [-X, X], par défaut [-1, 1].
   * @param y Ordonnée du centre, dans [-Y, Y], par défaut [-1, 1].
   * @param r Rayon du cercle.
   * @param c Numéro de la courbe: 0 (noir, défaut), 1 (brun), 2 (rouge), 3 (orange), 4 (jaune), 5 (vert), 6 (bleu), 7 (violet), 8 (gris), 9 (blanc).
   * @return L'objet graphique créé, utilisé pour détruire l'objet ensuite.
   */
  public static Object addCircle(double x, double y, double r, int c) {
    return getPane().scope.add(x, y, r, c);
  }
  /*
   * @see #addCircle(double, double, double, int)
   */
  public static Object addCircle(double x, double y, double r) {
    return addCircle(x, y, r, 0);
  }
  /** Détruit l'objet graphique spécifié.
   * @param object L'objet à détruire.
   * @return La valeur true si l'objet existait, false sinon.
   */
  public boolean remove(Object object) {
    return getPane().scope.remove(object);
  }
  /** Renvoie la valeur horizontale du réticule. */
  public static double getX() {
    return getPane().inputX.getValue();
  }
  /** Renvoie la valeur verticale du réticule. */
  public static double getY() {
    return getPane().inputY.getValue();
  }
  /** Définit la position du réticule.
   * @param x Abscisse du réticule,  dans [-X, X], par défaut [-1, 1].
   * @param y Reticule ordinate, dans [-Y, Y], par défaut [-1, 1].
   */
  public static void setReticule(double x, double y) {
    getPane().scope.setReticule(x, y);
  }
  /** Définit une portion de code appelée à chaque modification du réticule.
   * @param runnable La portion de code à appeler, ou null si il n'y en a pas.
   */
  public static void setRunnable(Runnable runnable) {
    getPane().runnable = runnable;
  }
}
