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

// Used to store labels
import java.util.Vector;

/** This widget defines a multi-curve output.
 * File used: <pre>
 * img/scope_screen.png
 * </pre>
 * @see <a href="CurveOutput.java">source code</a>
 */
public class CurveOutput extends JPanel {
  private static final long serialVersionUID = 1L;

  private JLabel icon; 
  private static class point { int i; int j; }; private Vector<Vector<point>> curves = new Vector<Vector<point>>();
  private static class line { int i1; int j1; int i2; int j2; Color c; } private Vector<line> lines = new Vector<line>();
  private static class label { int i; int j; String s; Color c; } private Vector<label> labels = new Vector<label>();
  {
    setBackground(Color.WHITE); setPreferredSize(new Dimension(512, 430));
    icon = new JLabel(); icon.setIcon(Proglets.getIcon("scope_screen.png")); icon.setLocation(0, 0); add(icon); 
    reset();
  }
  /** Internal routine: do not use. */
  public void paint(Graphics g) {
    super.paint(g);
    g.setPaintMode(); 
    int i0 = (int) icon.getLocation().getX(), j0 = (int) icon.getLocation().getY();
    for(int c = 0; c < 10; c++) {
      Vector<point> curve = curves.get(c);
      g.setColor(colors[c]);
      for(int i = 1; i < curve.size(); i++) {
	point p0 = curve.get(i - 1), p1 = curve.get(i);
	g.drawLine(i0 + p0.i, j0 + p0.j, i0 + p1.i, j0 + p1.j);
      }
    }
    for(line l : lines) {
      g.setColor(l.c);
      g.drawLine(i0 + l.i1, j0 + l.j1, i0 + l.i2, j0 + l.j2);
    }
    for(label l : labels) {
      g.setColor(l.c);
      g.drawString(l.s, i0 + l.i, i0 + l.j);
      g.drawLine(i0 + l.i - 1, j0 + l.j, i0 + l.i + 1, j0 + l.j);
      g.drawLine(i0 + l.i, j0 + l.j - 1, i0 + l.i, j0 + l.j + 1);
    }
  }
  private static int x2i(double x) { return  x < -1 ? 0 : x > 1 ? 511 : (int) Math.rint(255 + x * 233); }
  private static int y2j(double y) { return  y < -1 ? 511 : y > 1 ? 0 : 421 - (int) Math.rint(210 + y * 185); }
  
  /** Resets the display. */
  public void reset() {
    curves = new Vector<Vector<point>>(); for(int c = 0; c < 10; c++) curves.add(new Vector<point>());
    lines = new Vector<line>();
    labels = new Vector<label>();
    repaint(0, 0, getWidth(), getHeight());
  }
  
  /** Adds a curve value.
   * @param x Pixel abscissa, in [-1..1].
   * @param y Pixel Ordinate, in [-1..1].
   * @param c Channel, in {0, 9}.
   */
  public void add(double x, double y, int c) {
    point p = new point(); p.i = x2i(x); p.j = y2j(y); 
    if (0 <= c && c < 10)
      curves.get(c).add(p);
    repaint(0, 0, getWidth(), getHeight());
  }

  /** Adds a line.
   * @param x1 Pixel abscissa, in [-1..1].
   * @param y1 Pixel Ordinate, in [-1..1].
   * @param x2 Pixel abscissa, in [-1..1].
   * @param y2 Pixel Ordinate, in [-1..1].
   * @param c Channel, in {0, 9}.
   */
  public void add(double x1, double y1, double x2, double y2, int c) {
    line l = new line(); l.i1 = x2i(x1); l.j1 = y2j(y1); l.i2 = x2i(x2); l.j2 = y2j(y2); l.c = 0 <= c && c < 10 ? colors[c] : Color.BLACK;
    lines.add(l);
  }

  /** Adds a label.
   * @param x Label left-bottom abscissa, in [-1..1].
   * @param y Pixel left-bottom ordinate, in [-1..1].
   * @param s Label value.
   * @param c Channel, in {0, 9}.
   */
  public void add(double x, double y, String s, int c) {
    label l = new label(); l.i = x2i(x); l.j = y2j(y); l.s = s; l.c = 0 <= c && c < 10 ? colors[c] : Color.BLACK;
    labels.add(l);
  }

  private static Color colors[] = { Color.BLACK, new Color(150, 75, 0), Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.BLUE, Color.MAGENTA, Color.GRAY, Color.WHITE };
}
