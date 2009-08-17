/*******************************************************************************
 * Thierry.Vieville@sophia.inria.fr, Copyright (C) 2009.  All rights reserved. *
 *******************************************************************************/

package proglet;

// Used to define the gui
import java.applet.Applet;
import javax.swing.JPanel;
import java.awt.Dimension;

/** Définit une proglet javascool qui permet de manipuler les pixels d'une image.
 * @see <a href="Smiley.java">code source</a>
 */
public class Smiley { private Smiley() { }
  private static final long serialVersionUID = 1L;

  // This defines the panel to display
  private static class Panel extends JPanel {
    private static final long serialVersionUID = 1L;

    public Panel() {
      add(icon = new IconOutput());
    }
    private IconOutput icon;
  }

  //
  // This defines the tests on the panel
  //

  /** Test du panel. */
  static void test() { 
    for(int size = 512; size > 0; size /= 2) {
      reset(size, size);
      peace();
      Macros.sleep(1000 - size);
    }
  }

  /** Trace le signe de la paix dans l'image. */
  static private void peace() {
    int radius = width < height ? width - 2 : height - 2;
    circle(radius); for(int y = 0; y <= radius; y++) { set(0, -y, "black"); if (y < Math.rint(1/Math.sqrt(2) * radius)) { set(y, y, "black"); set(-y, y, "black"); } }
  }

  /** Trace un disque circulaire au centre de l'image. 
   * @param radius Rayon du disque
   */
  static private void circle(int radius) {
    for(int x = 0; x <= radius; x++) for(int y = 0; y <= radius; y++) if (radius * radius - x * x - y * y <= 1) { 
      set(x, y, "black"); set(x, -y, "black"); set(-x, y, "black"); set(-x, -y, "black"); }
  }

  //
  // This defines the javascool interface
  //

  /** Initialise l'image.
   * @param width Demi largeur de l'image de taille {-width, width}.
   * @param height Demi hauteur de l'image de taille {-height, height}.
   */
  static public void reset(int width, int height) {
    panel.icon.reset(2 * (Smiley.width = width) + 1, 2 * (Smiley.height = height) + 1);
  }
  static private int width, height;
  /** Change la valeur d'un pixel de l'image. 
   * @param x Abcisse de l'image, comptée à partir du milieu, valeur entre {-width, width}.
   * @param y Ordonnée de l'image, comptée à partir du milieu, valeur entre  {-height, height}.
   * @param color Couleur: "black" (default), "blue", "cyan", "gray", "green", "magenta", "orange", "pink", "red", "white", "yellow".
   * @return Renvoie true si le pixel est dans l'image, false si il est en dehors des limites d el'image.
   */
  static public boolean set(int x, int y, String color) {   
    return panel.icon.set(x + width, y + height, color);
  }
  
  /** Lit la valeur d'un pixel de l'image.
   * @param x Abcisse de l'image, comptée à partir du milieu, valeur entre {-width, width}.
   * @param y Ordonnée de l'image, comptée à partir du milieu, valeur entre {-height, height}.
   * @return Un mot anglais désignant la couleur du pixel si celui si est dans l'image, sinon "undefined";
   */
  static public String get(int x, int y) {
    return panel.icon.get(x + width, y + height);
  }

  /** Définition de l'interface graphique de la proglet. */
  public static final Panel panel = new Panel();
}

