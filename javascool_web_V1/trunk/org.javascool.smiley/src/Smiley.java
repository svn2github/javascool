/*******************************************************************************
 * Thierry.Vieville@sophia.inria.fr, Copyright (C) 2009.  All rights reserved. *
 *******************************************************************************/

// Used to define the gui
import java.applet.Applet;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

/** Définit une proglet javascool qui permet de manipuler les pixels d'une image.
 * Méthodes statiques à importer: <pre>
 * import static Smiley.reset;
 * import static Smiley.get;
 * import static Smiley.set;
 * import static Smiley.circle;
 * import static Smiley.peace;
 * </pre>
 * Documentation: <a href="smiley-sujet.html">sujet</a> et <a href="smiley-correction.html">correction</a>.
 */
public class Smiley {

  // This defines the panel to display
  private static class Panel extends JPanel {
    public Panel() {
      setBackground(Color.WHITE); setPreferredSize(new Dimension(550, 550));
    }
    
    public void paint(Graphics g) {
      super.paint(g);
      g.setPaintMode(); 
      setBounds();
      for(int j = 0, ij = 0; j < height; j++)
	for(int i = 0; i < width; i++, ij++) {
	  g.setColor(image[ij]);
	  g.fillRect(i0 + i * di, j0 + j * dj, di, dj);
	}
    }
    private void setBounds() {
      di = getWidth() / width; if (di <= 0) di = 1; i0 = (getWidth() - width * di) / 2; 
      dj = getHeight() / height; if (dj <= 0) dj = 1; j0 = (getHeight() - height * dj) / 2;
    }
    
    /** Sets a pixel value.
     * @param x Pixel abscissa, in {0, width{.
     * @param y Pixel Ordinate, in {0, height{.
     * @param  c Color: "black" (default), "blue", "cyan", "gray", "green", "magenta", "orange", "pink", "red", "white", "yellow"
     * @return True if the pixel location is in the image bounds.
     */
    public boolean set(int x, int y, String c) {
      if (0 <= x && x < width && 0 <= y && y < height) {
	Color v; try { v = (Color) Class.forName("java.awt.Color").getField(c).get(null); } catch(Exception e) { v = Color.BLACK; c = "black"; }
	setBounds(); int ij = x + y * width;
	image[ij] = v; color[ij] = c;
	repaint(i0 + x * di, j0 + y * dj, di, dj);
	Thread.yield();
	return true;
      } else
	return false;
    }

    /** Gets a pixel value.
     * @param x Pixel abscissa, in {0, width{.
     * @param y Pixel Ordinate, in {0, height{.
     */
    public String get(int x, int y) {
      if (0 <= x && x < width && 0 <= y && y < height) {
	return color[x + y * width];
      } else
	return "white";
    }

    /** Clears the image.
     * @param width Image width.
     * @param height Image height.
     */
    public void reset(int width, int height) {
      image = new Color[(this.width = (width > 0 ? width : 1)) * (this.height = (height > 0 ? height : 1))]; color = new String[width * height];
      for(int ij = 0; ij < width * height; ij++) { image[ij] = Color.WHITE; color[ij] = "white"; };
      repaint(0, 0, getWidth(), getHeight());
      Thread.yield();
    }
    private Color image[]; private String color[]; private int width, height, i0, j0, di, dj;
  } 

  //
  // This defines the tests on the panel
  //

  /** Test du panel. */
  static void test() { 
    for(int size = 1, SIZE = 512; size < SIZE; size *= 2) {
      reset(size, size);
      setPeaceSign();
      try { Thread.sleep(1000 - size); } catch(Exception e) { }
    }
  }

  //
  // This defines the javascool interface
  //

  /** Initialise l'image.
   * @param width Demie largeur de l'image de taille {-width, width}.
   * @param height Demie hauteur de l'image de taille {-height, height}.
   */
  static public void reset(int width, int height) {
    panel.reset(2 * (Smiley.width = width) + 1, 2 * (Smiley.height = height) + 1);
  }
  static private int width, height;

  /** Change la valeur d'un pixel de l'image. 
   * @param x Abcisse de l'image, comptée à partir du milieu, valeur entre {-width, width}.
   * @param y Ordonnée de l'image, comptée à partir du milieu, valeur entre  {-height, height}.
   * @param  black Valeur noire ou blanche.
   * @return Renvoie true si le pixel est dans l'image, false si il est en dehors des limites d el'image.
   */
  static public boolean set(int x, int y, boolean black) {   
    return panel.set(x + width, y + height, black ? "black" : "white");
  }
  
  /** Lit la valeur d'un pixel de l'image.
   * @param x Abcisse de l'image, comptée à partir du milieu, valeur entre {-width, width}.
   * @param y Ordonnée de l'image, comptée à partir du milieu, valeur entre {-height, height}.
   */
  static public boolean get(int x, int y) {
    return panel.get(x + width, y + height).equals("black");
  }

  /** Trace un disque circulaire au centre de l'image. 
   * @param radius Rayon du disque
   */
  static public void circle(int radius) {
    for(int x = 0; x <= radius; x++) for(int y = 0; y <= radius; y++) if (radius * radius - x * x - y * y <= 1) { 
      set(x, y, true); set(x, -y, true); set(-x, y, true); set(-x, -y, true); }
  }

  /** Trace le signe de la paix dans l'image. */
  static public void peace() {
    int radius = width < height ? width - 2 : height - 2;
    circle(radius); for(int y = 0; y <= radius; y++) { set(0, -y, true); if (y < Math.rint(1/Math.sqrt(2) * radius)) { set(y, y, true); set(-y, y, true); } }
  }
  
  //
  // This defines the javascool embedded
  //

  /** Renvoie le panel affiché. */
  static JPanel getPanel(Applet applet) { return panel; } 
  
  private static Panel panel = new Panel();
}

