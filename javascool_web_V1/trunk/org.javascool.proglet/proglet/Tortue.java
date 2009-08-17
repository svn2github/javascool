/*******************************************************************************
 * Thievery.Vieville@sophia.inria.fr, Copyright (C) 2009.  All rights reserved. *
 *******************************************************************************/

package proglet;

// Used to define the gui
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics;


/** Définit une proglet javascool qui permet de simuler la tortue ``logo´´.
 * @see <a href="http://fr.wikipedia.org/wiki/Logo_(langage)#Primitives_Logo">La référence du langage logo</a>
 * @see <a href="Tortue.java">code source</a>
 */
public class Tortue { private Tortue() { }
  private static final long serialVersionUID = 1L;

  // This defines the panel to display
  private static class Panel extends JPanel {
    private static final long serialVersionUID = 1L;

    public Panel() {
      setPreferredSize(new Dimension(width, height));
      setBackground(new Color(10, 100, 10)); 
      // Adds the garden
      clear();
      // Adds the turtle
      turtle = new JLabel();
      turtle.setIcon(Proglet.getIcon("turtle.gif"));
      turtle.setBounds(width/2, height/2, 42, 35);
      add(turtle);
    }
    /** Internal routine: do not use. */
    public void paint(Graphics g) {
      super.paint(g);
      g.setPaintMode(); 
      for(int j = 0; j < height; j++)
	for(int i = 0; i < width; i++)
	  if (garden[i + j * width] != null) {
	    g.setColor(garden[i + j * width]);
	    g.fillRect(i, j, 1, 1);
	  }
    }
    private Color garden[]; public JLabel turtle; static final int width = 512, height = 512; 

    /** Clears the garden. */
    public void clear() {
      garden = new Color[width * height];
    }

    /** Shows the turtle at a given location. 
     * @param x Turtle horizontal position, not shown if &lt; 0.
     * @param y Turtle vertical position, not shown if &lt; 0.
     */
    public void show(int x, int y) {
      if (x < 0 || y < 0) {
	turtle.setVisible(false);
      } else {
	turtle.setBounds(x, y, 42, 35);
	turtle.setVisible(true);
      }
    }

    /** Adds a trace value.
     * @param x Pixel abscissa, in [-1..1].
     * @param y Pixel Ordinate, in [-1..1].
     * @param c Color in {0, 9}.
     */
    public void add(int x, int y, Color c) {
      garden[x + y * width] = c;
    }
  }

  //
  // This defines the tests on the panel
  //

  /** Test du panel. */
  static void test() {
    clear();
    for(int t = 0; t < 9000; t++) {
      double x = Math.cos(0.0015 * t), y = Math.sin(0.0045 * t); 
      int i = (int) (Panel.width/2 + x * (Panel.width/2 - 20)), j = (int) (Panel.height/2 + y *  (Panel.height/2 - 20)), c = (t / 1000) % 10;
      pen = t > 0;
      pen_color = colors[c];
      panel.add(i, j, pen_color); panel.show(i, j);
      Macros.sleep(2);
    }
    /*
      clear(); pen_up(); set_position(256 + 250, 256); pen_down();
      int t = 0; while(t < 9000) { 
      set_color((t / 1000) % 10);
      set_position(256 + 250 * Math.cos(0.0015 * t), 256 + 250 * Math.sin(0.0045 * t))
      t = t + 1;
      }
    */
  }

  //
  // This defines the javascool interface 
  //

  // Updates the turtle position and draw if required
  private static void update(int x, int y, double a) {
    if (pen) {
      if (Math.abs(x - Tortue.x) > Math.abs(y - Tortue.y) && Math.abs(x - Tortue.x) > 0) {
	for(int i = Tortue.x ; i <= x; i++) {
	  int j = Tortue.y + ((y - Tortue.y) * (i - Tortue.x)) / (x - Tortue.x);
	  panel.add(i, j, pen_color);
	}
      } else if (Math.abs(y - Tortue.y) > 0) {
	for(int j = Tortue.y ; j <= y; j++) {
	  int i = Tortue.x + ((x - Tortue.x) * (j - Tortue.y)) / (y - Tortue.y);
	  panel.add(i, j, pen_color);
	}
      }
    }
    Tortue.x = x; Tortue.y = y;
    panel.show(x, y);
    Macros.sleep(2);
  }
  private static int x = 0, y = 0; private static double a = 0; private static Color pen_color = Color.BLACK; private static boolean pen = true;

  /** Efface toutes traces du carré de salade de taille (512, 512). */
  public static void clear() { panel.clear(); }

  /** Retour au milieu du carré de salade, au point (256, 256). */
  public static void home() { update(x = panel.width/2, y = panel.height/2, a); }

  /** La tortue avance de n pas. */
  public static void forward(double n) { set_position(x + n * Math.cos(a), y + n * Math.sin(a)); }

  /** La tortue recule de n pas. */
  public static void backward(double n) { forward(-n); }

  /** La tortue tourne de n degrés d'angle vers la gauche. */
  public static void leftward(double n) { a += Math.PI / 180.0 * n; }

  /** La tortue tourne de n degrés d'angle vers la droite. */
  public static void rightward(double n) { a += Math.PI / 180.0 * n; }

  /** Fixe la position absolue de la tortue. */
  public static void set_position(double x, double y) { update((int) x, (int) y, a); }

  /** Fixe le cap de la tortue de maniere absolue, selon l'angle de n degrés. */
  public static void set_heading(double a) { Tortue.a = Math.PI / 180.0 * a; }

  /** La tortue ne laisse pas de trace. */
  public static void pen_up() { pen = false; }

  /** La tortue laisse sa trace (par défaut). */
  public static void pen_down() { pen = true; }

  /** Change la couleur du fond, n est un entier positif. */
  public static void set_background(int n) { panel.setBackground(colors[n < 0 || n > 0 ? 0 : n]); }

  /** Change la couleur du crayon, n est un entier positif. */
  public static void set_color(int n) { pen_color = colors[n < 0 || n > 0 ? 0 : n]; }

  /** Définition de l'interface graphique de la proglet. */
  public static final Panel panel = new Panel();

  private static Color colors[] = { Color.BLACK, new Color(150, 75, 0), Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.BLUE, Color.MAGENTA, Color.GRAY, Color.WHITE };
}
