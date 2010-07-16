/*******************************************************************************
 * Thierry.Vieville@sophia.inria.fr, Copyright (C) 2009.  All rights reserved. *
 *******************************************************************************/

package org.javascool;

// Used to define the gui
import java.applet.Applet;
import javax.swing.JPanel;
import java.awt.Dimension;

/** Définit une proglet javascool qui permet de manipuler les pixels d'une image.
 * @see <a href="Smiley.java">code source</a>
 */
public class Smiley implements Proglet { private Smiley() { }
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
    for(int size = 256; size > 0; size /= 2) {
      smileyReset(size, size);
      peace();
      Macros.sleep(1000 - size);
    }
  }

  /** Trace le signe de la paix dans l'image. */
  static private void peace() {
    int radius = width < height ? width - 2 : height - 2;
    circle(radius); 
    for(int y = 0; y <= radius; y++) { 
      smileySet(0, -y, "black"); 
      if (y < Math.rint(1/Math.sqrt(2) * radius)) { 
	smileySet(y, y, "black"); 
	smileySet(-y, y, "black"); 
      } 
    }
  }

  /** Trace un disque circulaire au centre de l'image. 
   * @param radius Rayon du disque
   */
  static private void circle(int radius) {
    for(int x = 0; x <= radius; x++) 
      for(int y = 0; y <= radius; y++) 
	if (radius * radius - x * x - y * y <= 1) { 
	  smileySet(x, y, "black"); 
	  smileySet(x, -y, "black"); 
	  smileySet(-x, y, "black"); 
	  smileySet(-x, -y, "black"); 
	}
  }

  //
  // This defines the javascool interface
  //

  /** Initialise l'image.
   * - La taille de l'image ne doit pas être trop importante (pas plus de 500^2).
   * @param width Demi largeur de l'image de taille {-width, width}.
   * @param height Demi hauteur de l'image de taille {-height, height}.
   */
  static public void smileyReset(int width, int height) {
    panel.icon.reset(2 * (Smiley.width = width) + 1, 2 * (Smiley.height = height) + 1);
  }
  static private int width, height;

  /** Charge l'image.
   * - La taille de l'image ne doit pas être trop importante (pas plus de 500^2).
   * @param image Nom de l'URL où se trouve l'image
   */
  static public void smileyLoad(String image) {
    try {
      Dimension dim = panel.icon.reset(image); width = (dim.width - 1) / 2; height = (dim.height - 1) / 2;
    } catch(Exception e) {
      smileyReset(200, 200);
      System.out.println("Impossible de charger "+image);
    }
  }

  /** Gets the witdh. */
  static public int smileyWidth() { return width; }

  /** Gets the height. */
  static public int smileyHeight() { return height; }

  /** Change la valeur d'un pixel de l'image. 
   * @param x Abcisse de l'image, comptée à partir du milieu, valeur entre {-width, width}.
   * @param y Ordonnée de l'image, comptée à partir du milieu, valeur entre  {-height, height}.
   * @param color Couleur: "black" (default), "blue", "cyan", "gray", "green", "magenta", "orange", "pink", "red", "white", "yellow".
   * @return Renvoie true si le pixel est dans l'image, false si il est en dehors des limites d el'image.
   */
  static public boolean smileySet(int x, int y, String color) {   
    return panel.icon.set(x + width, y + height, color);
  }
  
  /** Change la valeur d'un pixel de l'image. 
   * @param x Abcisse de l'image, comptée à partir du milieu, valeur entre {-width, width}.
   * @param y Ordonnée de l'image, comptée à partir du milieu, valeur entre  {-height, height}.
   * @param valeur Une valeur entre 0 et 255 (0 pour noir, 1 pour blanc).
   * @return Renvoie true si le pixel est dans l'image, false si il est en dehors des limites d el'image.
   */
  static public boolean smileySet(int x, int y, int valeur) {   
    return panel.icon.set(x + width, y + height, valeur);
  }
  
  /** Lit la valeur d'un pixel de l'image.
   * @param x Abcisse de l'image, comptée à partir du milieu, valeur entre {-width, width}.
   * @param y Ordonnée de l'image, comptée à partir du milieu, valeur entre {-height, height}.
   * @return Une valeur entre 0 et 255 (0 pour noir, 1 pour blanc);
   */
  static public int smileyGet(int x, int y) {
    return panel.icon.getIntensity(x + width, y + height);
  }

  /** Définition de l'interface graphique de la proglet. */
  public static final Panel panel = new Panel();
}

