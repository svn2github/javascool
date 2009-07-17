/*******************************************************************************
 * Thierry.Vieville@sophia.inria.fr, Copyright (C) 2009.  All rights reserved. *
 *******************************************************************************/

package proglet;

// Used to define the gui
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

// Used to define an icon/label
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.net.URL;

// Used for the trace
import java.util.Vector;
import java.util.Enumeration;

/** This defines a trace output. */
class TraceOutput extends JPanel {
  public TraceOutput() {
    setBackground(Color.WHITE); setPreferredSize(new Dimension(512, 512));
    reset();
  }
  public void paint(Graphics g) {
    super.paint(g);
    g.setPaintMode(); 
    for (Enumeration<Point> e = values.elements(); e.hasMoreElements();) { Point p = e.nextElement();
      g.setColor(p.c); g.drawLine(p.x, p.y, p.x, p.y);
    }
  }
  private class Point { int x, y; Color c; } ; Vector<Point> values;
  
  /** Resets the curve value. */
  public void reset() {
    values = new Vector<Point>();
    repaint(0, 0, getWidth(), getHeight());
  }

  /** Sets the maximal trace size. 
   * @param size The maximal size of the trace. Unbounded trace if size = 0.
   */
  public void set(int size) { 
    if ((this.size = size) > 0) while(values.size() > size) { Point p = values.get(0); repaint(p.x - 1, p.y - 1, 3, 3); values.remove(0); }
  } 
  private int size = 0;

  /** Adds a trace value.
   * @param x Pixel abscissa, in [-1..1].
   * @param y Pixel Ordinate, in [-1..1].
   * @param c Channel, in {0, 9}.
   */
  public void add(double x, double y, int c) {
    int i = (int) Math.rint(255 + x * 250), j = 511 - (int) Math.rint(255 + y * 250);
    if (0 <= c && c < 10 && 0 <= i && i < 512 && 0 <= j && j < 512) {
      Point p = new Point(); p.x = i; p.y = j; p.c = colors[c]; values.add(p);
      repaint(i - 1, j - 1, 3, 3);
      set(size);
    }
  }

  private static Color colors[] = { Color.BLACK, new Color(150, 75, 0), Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.BLUE, Color.MAGENTA, Color.GRAY, Color.WHITE };
}
