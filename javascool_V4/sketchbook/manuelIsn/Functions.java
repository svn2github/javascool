//  Ces programmes sont sous licence CeCILL-B V1.
// ----------------------------------------------------------------------
// Isn.java
// ----------------------------------------------------------------------
// Julien Cervelle et Gilles Dowek, version javascool.

package org.javascool.proglets.manuelIsn;
import static org.javascool.macros.Macros.*;

// graphisme
import java.awt.Color;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.Point;
// tracé de texte
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.Color;
import java.awt.Font;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.AffineTransform;

/** Définit les fonctions de la proglet qui permettent de faire les exercices Isn.
 *
 * @see <a href="Functions.java.html">code source</a>
 * @serial exclude
 */
public class Functions {
  private static final long serialVersionUID = 1L;
  // @factory
  private Functions() {}
  /** Renvoie l'instance de la proglet pour accéder à ses éléments. */
  private static Panel getPane() {
    return getProgletPane();
  }
  
  // ----------------------------------------------------------------------
  // graphisme
  // ----------------------------------------------------------------------

  /** Initialise le graphique avec un tracé de tailles (w, h).
   * - Dans ce contexte le titre "s" et la position (x, y) sont sans importance, on pourra donc utiliser : <tt>initDrawing("", 0, 0, w, h);</tt>
   */
  public static void initDrawing(String s, int x, int y, int w, int h) { 
    initDrawing(w, h);
  }
  /**
   * @see #initDrawing(String, int, int, int, int)
   */
  public static void initDrawing(int w, int h) {    
    getPane().reset(w, h);
    org.javascool.gui.Desktop.getInstance().focusOnProgletPanel();
  }
  /** Peint un pixel en (x, y) de couleur RGB = (c1, c2, c3). */
  public static void drawPixel(double x, double y, int c1, int c2, int c3) {
    getPane().add(new Rectangle2D.Double(x, y, 0, 0), new Color(c1, c2, c3), Color.WHITE);
  }
  /** Trace un rectangle de coin supérieur gauche (x, y), de tailles (a, b) et de couleur RGB = (c1, c2, c3). */
  public static void drawRect(double x, double y, double a, double b, int c1, int c2, int c3) {
    getPane().add(new Rectangle2D.Double(x, y, a, b), new Color(c1, c2, c3), Color.WHITE);
  }
  /** Remplit un rectangle de coin supérieur gauche (x, y), de tailles (a, b) avec la couleur RGB = (c1, c2, c3). */
  public static void paintRect(double x, double y, double a, double b, int c1, int c2, int c3) {
    getPane().add(new Rectangle2D.Double(x, y, a, b), new Color(c1, c2, c3), new Color(c1, c2, c3));
  }
  /** Trace un segment de droite de (x1, y1) à (x2, y2) et de couleur RGB = (c1, c2, c3). */
  public static void drawLine(double x1, double y1, double x2, double y2, int c1, int c2, int c3) {
    getPane().add(new Line2D.Double(x1, y1, x2, y2), new Color(c1, c2, c3), Color.WHITE);
  }
  /** Trace un cercle de centre (cx, cy) de rayon r et de couleur RGB = (c1, c2, c3). */
  public static void drawCircle(double cx, double cy, double r, int c1, int c2, int c3) {
    getPane().add(new Ellipse2D.Double(cx - r, cy - r, 2 * r, 2 * r), new Color(c1, c2, c3), null);
  }
  /** Remplit un cercle de centre (cx, cy) de rayon r avec la couleur RGB = (c1, c2, c3). */
  public static void paintCircle(double cx, double cy, double r, int c1, int c2, int c3) {
    getPane().add(new Ellipse2D.Double(cx - r, cy - r, 2 * r, 2 * r), new Color(c1, c2, c3), new Color(c1, c2, c3));
  }
  /** Affiche une imagette à un endroit donné et renvoie sa position pour permettre de la déplacer. */
  public static Point drawImage(String location, int x, int y) {
    return getPane().add(location, x, y);
  }

  /** Écrit un texte au point (x, y) avec une fonte de taille size et la couleur RGB = (c1, c2, c3). */
  public static Rectangle2D drawText(String text, double x, double y, int size, int c1, int c2, int c3) {
    FontRenderContext ctx = new FontRenderContext(null, true, true);
    Font font = new Font(Font.SANS_SERIF, Font.PLAIN,size);
    GlyphVector vector = font.createGlyphVector(ctx, text);
    Color color = new Color(c1, c2, c3);
    Shape shape = vector.getOutline();
    Area area = new Area(shape);
    area = area.createTransformedArea(AffineTransform.getTranslateInstance(x, y));
    getPane().add(area, color, color);
    return area.getBounds2D();
  }
  /** Affiche une image stockée dans le fichier à la location donnée. */
  public static void showImage(String location) {
    getPane().reset(location);
  }
}
