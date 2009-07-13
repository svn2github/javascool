/*******************************************************************************
 * Thierry.Vieville@sophia.inria.fr, Copyright (C) 2009.  All rights reserved. *
 *******************************************************************************/

//package org.javascool.konsol;

// Used to define the gui
import java.applet.Applet;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.BorderFactory;

// Used for the read/write
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Used to open an window
import javax.swing.JFrame;

/** Définit une proglet javascool qui permet de manipuler les pixels d'une image. d'expérimenter la recherche dichotomique.
 * Méthodes statiques à importer: <pre>
 * import static Smiley.echo;
 * import static Smiley.readString;
 * import static Smiley.readInteger;
 * import static Smiley.readFloat;
 * </pre>
 * Documentation: <a href="smiley-sujet.html">sujet</a> et <a href="smiley-correction.html">correction</a>.
 */
public class Smiley {

  // This defines the panel to display
  private static class Panel extends JPanel {
    public Panel() {
      setBackground(Color.WHITE); setPreferredSize(new Dimension(400, 500));
    }
    
    /** Sets a pixel value.
     * @param x Pixel abscissa.
     * @param y Pixel Ordinate.
     * @bool  v True for black, false for white.
     */
    public void draw(int x, int y, boolean v) {
    }

    /** Clears the image.
     * @param width Image width.
     * @param height Image height.
     */
    public void clear(int width, int height) {
    }
  }

  //
  // This defines the tests on the panel
  //

  /** Test du panel. */
  static void test() { 
    for(int size = 1; size < 400; size++) {
      reset(size, size);
      setPeaceSign();
    }
  }

  //
  // This defines the javascool interface
  //

  /** Initialise l'image.
   * @param width Demie largeur de l'image.
   * @param height Demie hauteur de l'image.
   */
  static public void reset(int width, int height) {
    image = new boolean[(2 * width + 1) * (2 * height + 1)]; panel.clear(2 * (Smiley.width = width) + 1, 2 * (Smiley.height = height) + 1);
  }
  static private boolean image[]; static private int width, height;

  /** Change la valeur d'un pixel de l'image. 
   * @param x Abcisse de l'image, comptée à partir du milieu.
   * @param y Ordonnée de l'image, comptée à partir du milieu.
   * @param  black Valeur noir ou blanche.
   * @return Renvoie true si le pixel est dans l'image, false si il est en dehors des limites d el'image.
   */
  static public boolean set(int x, int y, boolean black) {   
    if (-width <= x && x <= width && -height <= y && y <= height) {
      image[(x + width) + (y + height) * (1 + 2 * width)] = black;
      panel.draw(x + width, y + height, black);
      return true;
    } else {
      return false;
    }      
  }
  
  /** Lit la valeur d'un pixel de l'image.
   * @param x Abcisse de l'image, comptée à partir du milieu.
   * @param y Ordonnée de l'image, comptée à partir du milieu.
   */
  static public boolean get(int x, int y) {
    return (width <= x && x <= width && -height <= y && y <= height) ? image[(x + width) + (y + height) * (1 + 2 * width)] : false;
  }

  /** Trace un disque circulaire au centre de l'image. 
   * @param radius Rayon du disque
   */
  static public void setCircle(int radius) {
    for(int x = 0; x <= radius; x++) for(int y = 0; y <= radius; y++) if (radius * radius - x * x - y * y <= 1) { 
      set(x, y, true); set(x, -y, true); set(-x, y, true); set(-x, -y, true); }
  }

  /** Trace le signe de la paix dans l'image. */
  static public void setPeaceSign() {
    int radius = width < height ? width - 2 : height - 2;
    setCircle(radius); for(int y = 0; y <= radius; y++) { set(0, -y, true); if (y < Math.rint(1/Math.sqrt(2) * radius)) { set(y, y, true); set(-y, y, true); } }
  }
  
  //
  // This defines the javascool embedded
  //

  /** Renvoie le panel affiché. */
  static JPanel getPanel(Applet applet) { return panel; } 
  
  private static Panel panel = new Panel();
}

