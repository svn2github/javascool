/*******************************************************************************
 * Thierry.Vieville@sophia.inria.fr, Copyright (C) 2009.  All rights reserved. *
 *******************************************************************************/

package org.javascool;

// Used to define the gui
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

// Used to define a click
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;

// Used to define an icon/label
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.net.URL;

// Used to store labels
import java.util.Vector;

/** This widget defines a multi-curve output.
 * @see <a href="CurveOutput.java">source code</a>
 */
public class CurveOutput extends JPanel {
  private static final long serialVersionUID = 1L;

  private static class point { int i; int j; }; private Vector<Vector<point>> curves = new Vector<Vector<point>>();
  private static class line { int i1; int j1; int i2; int j2; Color c; } private Vector<line> lines = new Vector<line>();
  private static class oval { int i; int j; int w; int h; Color c; } private Vector<oval> ovals = new Vector<oval>();
  private static class label { int i; int j; String s; Color c; } private Vector<label> labels = new Vector<label>();
  {
    setBackground(Color.WHITE); setPreferredSize(new Dimension(522, 430));
    ReticuleMouseListener l = new ReticuleMouseListener();
    addMouseMotionListener(l);
    addMouseListener(l);
    reset(1, 1);
  }
  // Paints the scope background
  private static void paintBg(Graphics g) {
    g.setPaintMode(); 
    g.setColor(Color.CYAN);
    g.fillRoundRect(1, 1, 520, 428, 30, 30);
    g.setColor(Color.BLACK);
    g.drawRoundRect(1, 1, 520, 428, 30, 30);
    g.setColor(Color.LIGHT_GRAY);
    g.fillRect(11, 10, 500, 410);
    g.setColor(Color.BLACK);
    g.drawRect(11, 10, 500, 410);
    g.drawLine(260, 11, 260, 420);
    g.drawLine(11, 210, 510, 210);
  }

  /** Internal routine: do not use. */
  public void paint(Graphics g) {
    super.paint(g);
    paintBg(g);
    g.setPaintMode(); 
    for(int c = 0; c < 10; c++) {
      Vector<point> curve = curves.get(c);
      g.setColor(colors[c]);
      for(int i = 1; i < curve.size(); i++) {
	point p0 = curve.get(i - 1), p1 = curve.get(i);
	g.drawLine(p0.i, p0.j, p1.i, p1.j);
      }
    }
    for(line l : lines) {
      g.setColor(l.c);
      g.drawLine(l.i1, l.j1, l.i2, l.j2);
    }
    for(oval l : ovals) {
      g.setColor(l.c);
      g.drawOval(l.i, l.j, l.w, l.h);
    }
    for(label l : labels) {
      g.setColor(l.c);
      g.drawString(l.s, l.i, l.j);
      g.drawLine(l.i - 1, l.j, l.i + 1, l.j);
      g.drawLine(l.i, l.j - 1, l.i, l.j + 1);
    }
    paintReticule(g);
  }
  private int x2i(double x) { return  x < -Xscale ? 0 : x > Xscale ? 511 : (int) Math.rint(260 + x / Xscale * 240); }
  private int y2j(double y) { return  y < -Yscale ? 511 : y > Yscale ? 0 : (int) Math.rint(210 - y / Yscale * 200); }
  private int x2w(double x) { return  (int) Math.rint(x / Xscale * 240); }
  private int y2h(double y) { return  (int) Math.rint(y / Yscale * 200); }
  private double i2x(int i) { return  Xscale * (i - 260.0) / 240.0; }
  private double j2y(int j) { return  Yscale * (210.0 - j) / 200.0; }
  private double Xscale = 1, Yscale = 1;

  private class ReticuleMouseListener implements MouseMotionListener, MouseListener {
    private static final long serialVersionUID = 1L;
    public void mouseDragged(MouseEvent e) {
      if (reticuleX >= 0 && reticuleY >= 0) 
	paintReticule(getGraphics());
      reticuleX = e.getX(); reticuleY = e.getY();
      paintReticule(getGraphics());
    }
    public void mouseReleased(MouseEvent e) { 
      mouseDragged(e);
      if (run != null) run.run();
    }
    public void mouseMoved(MouseEvent e) {  }
    public void mousePressed(MouseEvent e) { }
    public void mouseClicked(MouseEvent e) {  }
    public void mouseEntered(MouseEvent e) {  }
    public void mouseExited(MouseEvent e) {  }
  }

  /** Resets the display.
   * @param Xscale Horizontal scale.
   * @param Yscale Vertical scale.
   */
  public void reset(double Xscale, double Yscale) {
    this.Xscale = Xscale; this.Yscale = Yscale;
    curves = new Vector<Vector<point>>(); for(int c = 0; c < 10; c++) curves.add(new Vector<point>());
    lines = new Vector<line>();
    ovals = new Vector<oval>();
    labels = new Vector<label>();
    repaint(0, 0, getWidth(), getHeight());
  }
  
  /** Adds a curve value.
   * @param x Point abscissa, in [-Xscale..Xscale].
   * @param y Point Ordinate, in [-Yscale..Yscale].
   * @param c Channel, in {0, 9}.
   */
  public void add(double x, double y, int c) {
    point p = new point(); p.i = x2i(x); p.j = y2j(y); 
    if (0 <= c && c < 10)
      curves.get(c).add(p);
    repaint(0, 0, getWidth(), getHeight());
  }

  /** Adds a line.
   * @param x1 Point abscissa, in [-Xscale..Xscale].
   * @param y1 Point Ordinate, in [-Yscale..Yscale].
   * @param x2 Point abscissa, in [-Xscale..Xscale].
   * @param y2 Point Ordinate, in [-Yscale..Yscale].
   * @param c Channel, in {0, 9}.
   */
  public void add(double x1, double y1, double x2, double y2, int c) {
    line l = new line(); l.i1 = x2i(x1); l.j1 = y2j(y1); l.i2 = x2i(x2); l.j2 = y2j(y2); l.c = 0 <= c && c < 10 ? colors[c] : Color.BLACK;
    lines.add(l);
  }

  /** Adds a circle.
   * @param x Center abscissa, in [-Xscale..Xscale].
   * @param y Center Ordinate, in [-Yscale..Yscale].
   * @param r Center radius.
   * @param c Channel, in {0, 9}.
   */
  public void add(double x, double y, double r, int c) {
    oval l = new oval(); l.i = x2i(x - r); l.j = y2j(y + r); l.w = x2w(2 * r); l.h = y2h(2 * r); l.c = 0 <= c && c < 10 ? colors[c] : Color.BLACK;
    ovals.add(l);
  }

  /** Adds a label.
   * @param x Label left-bottom abscissa, in [-Xscale..Xscale].
   * @param y Label left-bottom ordinate, in [-Yscale..Yscale].
   * @param s Label value.
   * @param c Channel, in {0, 9}.
   */
  public void add(double x, double y, String s, int c) {
    label l = new label(); l.i = x2i(x); l.j = y2j(y); l.s = s; l.c = 0 <= c && c < 10 ? colors[c] : Color.BLACK;
    labels.add(l);
  }

  /** Sets the reticule position.
   * @param x Reticule abscissa, in [-Xscale..Xscale].
   * @param y Reticule ordinate, in [-Yscale..Yscale].
   */
  public void setReticule(double x, double y) {
    reticuleX = -1 < x && x < 1 ? x2i(x) : -1;
    reticuleY = -1 < y && y < 1 ? y2j(y) : -1;
    repaint(0, 0, getWidth(), getHeight());
  }

  /** Gets the horizontal reticule position. */
  public double getReticuleX() { return reticuleX < 0 ? 0 : i2x(reticuleX); }

  /** Gets the vertical reticule position. */
  public double getReticuleY() { return reticuleY < 0 ? 0 : j2y(reticuleY); }

  void paintReticule(Graphics g) {
    if (reticuleX >= 0 && reticuleY >= 0) {
      g.setColor(Color.white); 
      g.setXORMode(Color.black);
      g.drawLine(0, reticuleY, getWidth(), reticuleY);
      g.drawLine(reticuleX, 0, reticuleX, getHeight());
    }
  }
  protected int reticuleX = -1, reticuleY = -1;

  private static Color colors[] = { Color.BLACK, new Color(150, 75, 0), Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.BLUE, Color.MAGENTA, Color.GRAY, Color.WHITE };

  /** Sets the runnable called when the input is changed. 
   * @param run The runnable to call, set to null if no runnable.
   */
  public void setRunnable(Runnable run) { this.run = run; } private Runnable run = null;
}
