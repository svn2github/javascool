/*******************************************************************************
 * Thievery.Vieville@sophia.inria.fr, Copyright (C) 2009.  All rights reserved. *
 *******************************************************************************/

package proglet;

// Used to define the gui
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Color;
import javax.swing.JLayeredPane;
import javax.swing.JLabel;


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
      setPreferredSize(new Dimension(512, 512));
      setBackground(Color.GREEN); 
      // Adds the turtle
      turtle = new JLabel();
      turtle.setIcon(Proglet.getIcon("turtle.gif"));
      turtle.setBounds(256, 256, 42, 35);
      add(turtle);
      // Adds the garden
      garden = new IconOutput();
      garden.reset(500, 500);
      // add(garden)
    }
    
    /** Show the turtle at a given location. 
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
      
    public IconOutput garden; public JLabel turtle;
  }

  //
  // This defines the tests on the panel
  //

  /** Test du panel. */
  static void test() {
    clean();
    for(int t = 0; t < 9000; t++) {
      double x = Math.cos(0.0015 * t), y = Math.sin(0.0045 * t); int c = (t / 1000) % 10;
      // panel.jardin.set(x, y, c);
      panel.show((int) (255 + x * 250), (int) (255 + y * 250));
      Macros.sleep(3);
    }
  }

  //
  // This defines the javascool interface 
  //
  private static int x = 0, y = 0; private static boolean pen = true;

  /** Efface toutes traces du carré de salade de taille (500, 500). */
  public static void clean() { panel.garden.reset(500, 500); }

  /** Retour au milieu du carré de salade, au point (250, 250). */
  public static void home() { x = panel.garden.getWidth()/2; y = panel.garden.getHeight()/2; }

  /** La tortue ne laisse pas de trace. */
  public static void pen_up() { pen = false; }

  /** La tortue laisse sa trace (par défaut). */
  public static void pen_down() { pen = true; }

  /** Définition de l'interface graphique de la proglet. */
  public static final Panel panel = new Panel();
}
