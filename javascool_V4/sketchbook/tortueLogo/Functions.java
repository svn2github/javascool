package org.javascool.proglets.tortueLogo;
import static org.javascool.macros.Macros.*;

import java.awt.Color;

/** Définit les fonctions de la proglet qui permet de simuler la tortue logo.
 *
 * @see <a href="http://fr.wikipedia.org/wiki/Logo_(langage)#Primitives_graphiques">La référence du langage logo</a>
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
  // Updates the turtle position and draw if required
  private static void update(double x, double y) {
    if(x < 0) {
      x = 0;
    }
    if(x > 511) {
      x = 511;
    }
    if(y < 0) {
      y = 0;
    }
    if(y > 511) {
      y = 511;
    }
    if(pen) {
      draw((int) Functions.x,(int) x, (int) Functions.y,(int) y);
    }
    Functions.x = x;
    Functions.y = y;
    if (turtle_shown) {
      getPane().show((int) x,(int) y,turtle_shown);
      sleep(1);
    }
  }
  private static void draw(int x1, int x2, int y1, int y2) {
    if(Math.abs(x1 - x2) > Math.abs(y1 - y2)) {
      if(x1 < x2) {
        draw_x(x1, x2, y1, y2);
      } else if(x1 > x2) {
        draw_x(x2, x1, y2, y1);
      }
    } else {
      if(y1 < y2) {
        draw_y(x1, x2, y1, y2);
      } else if(y1 > y2) {
        draw_y(x2, x1, y2, y1);
      }
    }
  }
  private static void draw_x(int x1, int x2, int y1, int y2) {
    for(int x = x1; x <= x2; x++)
      getPane().add(x, y1 + ((y2 - y1) * (x - x1)) / (x2 - x1), pen_color);
  }
  private static void draw_y(int x1, int x2, int y1, int y2) {
    for(int y = y1; y <= y2; y++)
      getPane().add(x1 + ((x2 - x1) * (y - y1)) / (y2 - y1), y, pen_color);
  }
  private static double x = 0, y = 0;
  private static double a = 0;
  private static Color pen_color = Color.BLACK;
  private static boolean pen = true;
  private static boolean turtle_shown = true;

  /** Efface toutes traces du carré de salade de taille (512, 512). */
  public static void clear_all() {
    getPane().clear();
  }
  /** Retour au milieu du carré de salade, au point (256, 256). */
  public static void home() {
    update(org.javascool.proglets.tortueLogo.Panel.width / 2, org.javascool.proglets.tortueLogo.Panel.height / 2);
  }
  /** La tortue avance de n pas. */
  public static void forward(double n) {
    set_position(x + n * Math.cos(a), y + n * Math.sin(a));
  }
  /** La tortue recule de n pas. */
  public static void backward(double n) {
    forward(-n);
  }
  /** La tortue tourne de n degrés d'angle vers la gauche. */
  public static void leftward(double n) {
    a -= Math.PI / 180.0 * n;
  }
  /** La tortue tourne de n degrés d'angle vers la droite. */
  public static void rightward(double n) {
    leftward(-n);
  }
  /** Fixe la position absolue de la tortue dans le carré de salade. */
  public static void set_position(double x, double y) {
    update( x, y);
  }
  /** Fixe le cap de la tortue de maniere absolue, selon l'angle de a degrés. */
  public static void set_heading(double a) {
    Functions.a = Math.PI / 180.0 * a;
  }
  /** La tortue ne laisse pas de trace. */
  public static void pen_up() {
    pen = false;
  }
  /** La tortue laisse sa trace (par défaut). */
  public static void pen_down() {
    pen = true;
  }
  /** Change la couleur du fond, n est un entier positif entre 0 et 9.
   * @param n : 0 (noir), 1 (brun), 2 (rouge), 3 (orange), 4 (jaune), 5 (vert), 6 (bleu), 7 (violet), 8 (gris), 9 (blanc).
   */
  public static void set_background(int n) {
    getPane().setBackground(colors[n < 0 || n > 9 ? 0 : n]);
  }
  /** Change la couleur du crayon, n est un entier positif entre 0 et 9.
   * @param n : 0 (noir), 1 (brun), 2 (rouge), 3 (orange), 4 (jaune), 5 (vert), 6 (bleu), 7 (violet), 8 (gris), 9 (blanc).
   */
  public static void set_color(int n) {
    pen_color = colors[n < 0 || n > 9 ? 0 : n];
  }
  private static Color colors[] = { Color.BLACK, new Color(150, 75, 0), Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.BLUE, Color.MAGENTA, Color.GRAY, Color.WHITE };
  public static void show_turtle() {
    turtle_shown = true;
    getPane().show((int) x,(int) y,turtle_shown);
  }
  public static void hide_turtle() {
    turtle_shown = false;
    getPane().show((int) x,(int) y,turtle_shown);
  }

}
