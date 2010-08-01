/*******************************************************************************
 * Thievery.Vieville@sophia.inria.fr, Copyright (C) 2009.  All rights reserved. *
 *******************************************************************************/

package tortuelogo;

// Used to define the gui
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics;

/** Définit une proglet javascool qui permet de simuler la tortue ``logo´´.
 * @see <a href="http://fr.wikipedia.org/wiki/Logo_(langage)#Primitives_Logo">La référence du langage logo</a>
 * @see <a href="../tortuelogo/Main.java">code source</a>
 */
public class Main implements org.javascool.Proglet { private Main() { }
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
      turtle.setIcon(Proglets.getIcon("turtle.gif"));
      turtle.setBounds(width/2, height/2, 42, 35);
      add(turtle);
    }
    /** Internal routine: do not use. */
    public void paint(Graphics g) {
      super.paint(g);
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
    clear_all(); pen_up();
    int t = 0; while(t < 9000) { 
      set_color((t / 1000) % 10);
      set_position(256 + 250 * Math.cos(0.0015 * t), 256 + 250 * Math.sin(0.0045 * t));
      pen_down(); t = t + 1;
    }
  }

  //
  // This defines the javascool interface 
  //

  // Updates the turtle position and draw if required
  private static void update(int x, int y) {
    if (x < 0) x = 0; if(x > 511) x = 511;
    if (y < 0) y = 0; if(y > 511) y = 511;
    if (pen)
      draw(Main.x, x, Main.y, y);
    Main.x = x; Main.y = y;
    panel.show(x, y);
    Macros.sleep(3);
  }
  private static void draw(int x1, int x2, int y1, int y2) {
    if(Math.abs(x1 - x2) > Math.abs(y1 - y2)) {
      if (x1 < x2)
	draw_x(x1, x2, y1, y2);
      else if (x1 > x2)
	draw_x(x2, x1, y2, y1);
    } else {
      if (y1 < y2)
	draw_y(x1, x2, y1, y2);
      else if (y1 > y2)
	draw_y(x2, x1, y2, y1);
    }
  }
  private static void draw_x(int x1, int x2, int y1, int y2) {
    for(int x = x1 ; x <= x2; x++) {
      panel.add(x, y1 + ((y2 - y1) * (x - x1)) / (x2 - x1), pen_color);
    }
  }
  private static void draw_y(int x1, int x2, int y1, int y2) {
    for(int y = y1 ; y <= y2; y++) {
      panel.add(x1 + ((x2 - x1) * (y - y1)) / (y2 - y1), y, pen_color);
    }
  }

  private static int x = 0, y = 0; private static double a = 0; private static Color pen_color = Color.BLACK; private static boolean pen = true;

  /** Efface toutes traces du carré de salade de taille (512, 512). */
  public static void clear_all() { panel.clear(); }

  /** Retour au milieu du carré de salade, au point (256, 256). */
  public static void home() { update(panel.width/2, panel.height/2); }

  /** La tortue avance de n pas. */
  public static void forward(double n) { set_position(x + n * Math.cos(a), y + n * Math.sin(a)); }

  /** La tortue recule de n pas. */
  public static void backward(double n) { forward(-n); }

  /** La tortue tourne de n degrés d'angle vers la gauche. */
  public static void leftward(double n) { a += Math.PI / 180.0 * n; }

  /** La tortue tourne de n degrés d'angle vers la droite. */
  public static void rightward(double n) { leftward(-n); }

  /** Fixe la position absolue de la tortue dans le carré de salade. */
  public static void set_position(double x, double y) { update((int) x, (int) y); }

  /** Fixe le cap de la tortue de maniere absolue, selon l'angle de a degrés. */
  public static void set_heading(double a) { Main.a = Math.PI / 180.0 * a; }

  /** La tortue ne laisse pas de trace. */
  public static void pen_up() { pen = false; }

  /** La tortue laisse sa trace (par défaut). */
  public static void pen_down() { pen = true; }

  /** Change la couleur du fond, n est un entier positif entre 0 et 9. 
   * @param n : 0 (noir), 1 (brun), 2 (rouge), 3 (orange), 4 (jaune), 5 (vert), 6 (bleu), 7 (violet), 8 (gris), 9 (blanc).
   */
  public static void set_background(int n) { panel.setBackground(colors[n < 0 || n > 9 ? 0 : n]); }

  /** Change la couleur du crayon, n est un entier positif entre 0 et 9. 
   * @param n : 0 (noir), 1 (brun), 2 (rouge), 3 (orange), 4 (jaune), 5 (vert), 6 (bleu), 7 (violet), 8 (gris), 9 (blanc).
   */
  public static void set_color(int n) { pen_color = colors[n < 0 || n > 9 ? 0 : n]; }

  private static Color colors[] = { Color.BLACK, new Color(150, 75, 0), Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.BLUE, Color.MAGENTA, Color.GRAY, Color.WHITE };

  /** Définition de l'interface graphique de la proglet. */
  public static final Panel panel = new Panel();
}
