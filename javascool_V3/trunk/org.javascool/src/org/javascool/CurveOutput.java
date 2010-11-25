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
 * @see <a href="CurveOutput.java.html">source code</a>
 * @serial exclude
 */
public class CurveOutput extends JPanel implements Widget {
  private static final long serialVersionUID = 1L;

  private static class point {
    double x, y;
  };
  private Vector < Vector < point >> curves = new Vector < Vector < point >> ();
  private static class line {
    double x1, y1, x2, y2;
    Color c;
  }
  private Vector<line> lines = new Vector<line>();
  private static class oval {
    double x, y, w, h;
    Color c;
  }
  private Vector<oval> ovals = new Vector<oval>();
  private static class label {
    double x, y;
    String s;
    Color c;
  }
  private Vector<label> labels = new Vector<label>();
  {
    ReticuleMouseListener l = new ReticuleMouseListener();
    addMouseMotionListener(l);
    addMouseListener(l);
    reset(1, 1);
  }

  /** Internal routine: do not use.
   *
   */public void paint(Graphics g) {
    width = getWidth();
    height = getHeight();
    i0 = width / 2;
    j0 = height / 2;
    w0 = i0 - 10;
    h0 = j0 - 10;
    super.paint(g);
    paintBackground(g);
    g.setPaintMode();
    for(int c = 0; c < 10; c++) {
      Vector<point> curve = curves.get(c);
      g.setColor(colors[c]);
      for(int i = 1; i < curve.size(); i++) {
        point p0 = curve.get(i - 1), p1 = curve.get(i);
        g.drawLine(x2i(p0.x), y2j(p0.y), x2i(p1.x), y2j(p1.y));
      }
    }
    for(line l : lines) {
      g.setColor(l.c);
      g.drawLine(x2i(l.x1), y2j(l.y1), x2i(l.x2), y2j(l.y2));
    }
    for(oval l : ovals) {
      g.setColor(l.c);
      g.drawOval(x2i(l.x), y2j(l.y), x2w(l.w), y2h(l.h));
    }
    for(label l : labels) {
      int i = x2i(l.x), j = y2j(l.y);
      g.setColor(l.c);
      g.drawString(l.s, i, j);
      g.drawLine(i - 1, j, i + 1, j);
      g.drawLine(i, j - 1, i, j + 1);
    }
    paintReticule(g);
  }
  private void paintReticule(Graphics g) {
    int i = x2i(reticuleX), j = y2j(reticuleY);
    g.setColor(Color.white);
    g.setXORMode(Color.black);
    g.drawLine(i0 - w0, j, i0 + w0, j);
    g.drawLine(i, j0 - h0, i, j0 + h0);
  }
  private void paintBackground(Graphics g) {
    g.setPaintMode();
    g.setColor(Color.CYAN);
    g.fillRoundRect(1, 1, width - 2, height - 2, 30, 30);
    g.setColor(Color.BLACK);
    g.drawRoundRect(1, 1, width - 2, height - 2, 30, 30);
    g.setColor(Color.DARK_GRAY);
    g.fillRect(i0 - w0, j0 - h0, 2 * w0, 2 * h0);
    g.setColor(Color.WHITE);
    g.drawRect(i0 - w0, j0 - h0, 2 * w0, 2 * h0);
    g.drawLine(i0, j0 - h0, i0, j0 + h0);
    g.drawLine(i0 - w0, j0, i0 + w0, j0);
  }
  private int x2i(double x) {
    return (int) Math.rint(i0 + w0 * x);
  }
  private int y2j(double y) {
    return (int) Math.rint(j0 - h0 * y);
  }
  private int x2w(double x) {
    return (int) Math.rint(w0 * x);
  }
  private int y2h(double y) {
    return (int) Math.rint(h0 * y);
  }
  private double i2x(int i) {
    return ((double) (i - i0)) / w0;
  }
  private double j2y(int j) {
    return ((double) (j0 - j)) / h0;
  }
  private int width, height, i0, j0, w0, h0;

  private class ReticuleMouseListener implements MouseMotionListener, MouseListener {
    private static final long serialVersionUID = 1L;
    public void mouseDragged(MouseEvent e) {
      paintReticule(getGraphics());
      reticuleX = i2x(e.getX());
      reticuleY = j2y(e.getY());
      paintReticule(getGraphics());
    }
    public void mouseReleased(MouseEvent e) {
      mouseDragged(e);
      if(run != null)
        run.run();
    }
    public void mouseMoved(MouseEvent e) {}
    public void mousePressed(MouseEvent e) {}
    public void mouseClicked(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
  }

  /** Resets the display.
   * @param Xscale Horizontal scale.
   * @param Yscale Vertical scale.
   */
  public void reset(double Xscale, double Yscale) {
    this.Xscale = Xscale;
    this.Yscale = Yscale;
    curves = new Vector < Vector < point >> ();
    for(int c = 0; c < 10; c++)
      curves.add(new Vector<point>());
    lines = new Vector<line>();
    ovals = new Vector<oval>();
    labels = new Vector<label>();
    repaint(0, 0, getWidth(), getHeight());
  }
  private double Xscale = 1, Yscale = 1;

  /** Adds a curve value.
   * @param x Point abscissa, in [-Xscale..Xscale].
   * @param y Point Ordinate, in [-Yscale..Yscale].
   * @param c Channel, in {0, 9}.
   */
  public void add(double x, double y, int c) {
    point p = new point();
    p.x = x / Xscale;
    p.y = y / Yscale;
    if((0 <= c) && (c < 10))
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
    line l = new line();
    l.x1 = x1 / Xscale;
    l.y1 = y1 / Yscale;
    l.x2 = x2 / Xscale;
    l.y2 = y2 / Yscale;
    l.c = 0 <= c && c < 10 ? colors[c] : Color.BLACK;
    lines.add(l);
  }
  /** Adds a circle.
   * @param x Center abscissa, in [-Xscale..Xscale].
   * @param y Center Ordinate, in [-Yscale..Yscale].
   * @param r Center radius.
   * @param c Channel, in {0, 9}.
   */
  public void add(double x, double y, double r, int c) {
    oval l = new oval();
    l.x = (x - r) / Xscale;
    l.y = (y + r) / Yscale;
    l.w = 2 * r / Xscale;
    l.h = 2 * r / Yscale;
    l.c = 0 <= c && c < 10 ? colors[c] : Color.BLACK;
    ovals.add(l);
  }
  /** Adds a label.
   * @param x Label left-bottom abscissa, in [-Xscale..Xscale].
   * @param y Label left-bottom ordinate, in [-Yscale..Yscale].
   * @param s Label value.
   * @param c Channel, in {0, 9}.
   */
  public void add(double x, double y, String s, int c) {
    label l = new label();
    l.x = x / Xscale;
    l.y = y / Yscale;
    l.s = s;
    l.c = 0 <= c && c < 10 ? colors[c] : Color.BLACK;
    labels.add(l);
  }
  /** Gets the horizontal reticule position. */
  public double getReticuleX() {
    return Xscale * reticuleX;
  }
  /** Gets the vertical reticule position. */
  public double getReticuleY() {
    return Yscale * reticuleY;
  }
  /** Sets the reticule position.
   * @param x Reticule abscissa, in [-Xscale..Xscale].
   * @param y Reticule ordinate, in [-Yscale..Yscale].
   */
  public void setReticule(double x, double y) {
    x /= Xscale;
    y /= Yscale;
    reticuleX = x < -1 ? -1 : x > 1 ? 1 : x;
    reticuleY = y < -1 ? -1 : y > 1 ? 1 : y;
    repaint(0, 0, getWidth(), getHeight());
  }
  private double reticuleX = 0, reticuleY = 0;

  private static Color colors[] = { Color.BLACK, new Color(150, 75, 0), Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.BLUE, Color.MAGENTA, Color.GRAY, Color.WHITE };

  /** Sets the runnable called when the input is changed.
   * @param run The runnable to call, set to null if no runnable.
   */
  public void setRunnable(Runnable run) {
    this.run = run;
  }
  private Runnable run = null;
}
