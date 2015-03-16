package org.javascool.proglets.tortueLogo;
import static org.javascool.macros.Macros.*;
import static org.javascool.proglets.tortueLogo.Functions.*;

import javax.swing.JPanel;
import javax.swing.ImageIcon;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics;

/** Définit le panneau graphique de la proglet qui permet de simuler la tortue logo.
 * @see <a href="http://fr.wikipedia.org/wiki/Logo_(langage)#Primitives_graphiques">La référence du langage logo</a>
 * @see <a href="Panel.java.html">code source</a>
 * @serial exclude
 */
public class Panel extends JPanel {
  private static final long serialVersionUID = 1L;

  // @bean
  public Panel() {
    setPreferredSize(new Dimension(width, height));
    setBackground(new Color(10, 100, 10));
    // Adds the garden
    clear();
    // Adds the turtle
    turtle = getIcon("org/javascool/proglets/tortueLogo/turtle.gif");
  }
  /** Routine interne de tracé, ne pas utiliser.
   *
   */
  @Override
  public void paint(Graphics g) {
    super.paint(g);
    for(int j = 0; j < height; j++)
      for(int i = 0; i < width; i++)
        if(garden[i + j * width] != null) {
          g.setColor(garden[i + j * width]);
          g.fillRect(i, j, 1, 1);
        }
    if(turtle_shown) {
      g.drawImage(turtle.getImage(), turtle_x, turtle_y, getBackground(), turtle.getImageObserver());
    }
  }
  static final int width = 512, height = 512;
  private Color garden[];
  private ImageIcon turtle;
  private int turtle_x = width / 2, turtle_y = height / 2;
  private boolean turtle_shown = true;

  /** Clears the garden. */
  public final void clear() {
    garden = new Color[width * height];
    repaint(0, 0, 0, getWidth(), getHeight());
  }
  /** Shows the turtle at a given location.
   * @param x Turtle horizontal position, not shown if &lt; 0.
   * @param y Turtle vertical position, not shown if &lt; 0.
   */
  public void show(int x, int y, boolean turtle_visibility) {
    repaint(turtle_x - 1, turtle_y - 1, turtle.getIconWidth() + 3, turtle.getIconHeight() + 3);
    if((x < 0) || (y < 0)) {
      turtle_shown = false;
    } else {
      turtle_x = x;
      turtle_y = y;
      turtle_shown = turtle_visibility;
    }
    repaint(turtle_x - 1, turtle_y - 1, turtle.getIconWidth() + 3, turtle.getIconHeight() + 3);
  }
  /** Adds a trace value.
   * @param x Pixel abscissa, in [-1..1].
   * @param y Pixel Ordinate, in [-1..1].
   * @param c Color in {0, 9}.
   */
  public void add(int x, int y, Color c) {
    if(x < 0) {
      x = 0;
    }
    if(x > width-1) {
      x = width-1;
    }
    if(y < 0) {
      y = 0;
    }
    if(y > height-1) {
      y = height-1;
    }
    garden[x + y * width] = c;
    repaint(x - 1, y - 1, 3, 3);
  }
}
