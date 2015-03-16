/*******************************************************************************
* Thierry.Vieville@sophia.inria.fr, Copyright (C) 2009.  All rights reserved. *
*******************************************************************************/

package org.javascool.widgets;

// Used to define the gui
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;

// Used to define a click
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;

// Used to define an icon/label

// Used to store labels
import java.util.ArrayList;

/** Panneau pour le tracé de courbes 2D.
 *
 * @see <a href="CurveOutput.java.html">source code</a>
 * @serial exclude
 */
public class CurveOutput extends JPanel {
  private static final long serialVersionUID = 1L;

  // @bean
  public CurveOutput() {}
  private static class point {
    double x, y;
  };
  private ArrayList<ArrayList<point> > curves = new ArrayList<ArrayList<point> >();
  private static class line {
    double x1, y1, x2, y2;
    Color c;
  }
  private ArrayList<line> lines = new ArrayList<line>();
  private static class rectangle {
    line l1, l2, l3, l4;
  }
  private ArrayList<rectangle> rectangles = new ArrayList<rectangle>();
  private static class oval {
    double x, y, w, h;
    Color c;
  }
  private ArrayList<oval> ovals = new ArrayList<oval>();
  private static class block {
    double x, y, w, h;
    Color c_f, c_b;
  }
  private ArrayList<block> blocks = new ArrayList<block>();
  private static class label {
    double x, y;
    String s;
    Color c;
  }
  private ArrayList<label> labels = new ArrayList<label>();
  {
    ReticuleMouseListener l = new ReticuleMouseListener();
    addMouseMotionListener(l);
    addMouseListener(l);
    reset(0, 0, 1, 1);
  }

  /** Routine interne de tracé, ne pas utiliser.
   *
   */
  @Override
  public void paint(Graphics g) {
    try {
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
        ArrayList<point> curve = curves.get(c);
        g.setColor(colors[c]);
        for(int i = 1; i < curve.size(); i++) {
          point p0 = curve.get(i - 1), p1 = curve.get(i);
          g.drawLine(x2i(p0.x), y2j(p0.y), x2i(p1.x), y2j(p1.y));
        }
      }
      for(line l : lines) {
        g.setColor(l.c);
        int x1 = x2i(l.x1), y1 = y2j(l.y1), x2 = x2i(l.x2), y2 = y2j(l.y2);
        if((x1 == x2) && (y1 == y2)) {
          x2++;
        }
        g.drawLine(x1, y1, x2, y2);
      }
      for(oval l : ovals) {
        g.setColor(l.c);
        g.drawOval(x2i(l.x), y2j(l.y), x2w(l.w), y2h(l.h));
      }
      for(block l : blocks) {
        g.setColor(l.c_f);
        g.fillRect(x2i(l.x), y2j(l.y), x2w(l.w), y2h(l.h));
	if (l.c_b != l.c_f) {
	  g.setColor(l.c_b);
	  g.drawRect(x2i(l.x), y2j(l.y), x2w(l.w), y2h(l.h));
	}
      }
      for(label l : labels) {
        int i = x2i(l.x), j = y2j(l.y);
        g.setColor(l.c);
        g.drawString(l.s, i, j);
        g.drawLine(i - 1, j, i + 1, j);
        g.drawLine(i, j - 1, i, j + 1);
      }
      paintReticule(g);
    } catch(Exception e) {}
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
    @Override
    public void mouseDragged(MouseEvent e) {
      paintReticule(getGraphics());
      reticuleX = i2x(e.getX());
      reticuleY = j2y(e.getY());
      paintReticule(getGraphics());
    }
    @Override
    public void mouseReleased(MouseEvent e) {
      mouseDragged(e);
      if(runnable != null) {
        new Thread(runnable).start();
      }
    }
    @Override
    public void mouseMoved(MouseEvent e) {}
    @Override
    public void mousePressed(MouseEvent e) {}
    @Override
    public void mouseClicked(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
  }

  /** Efface et initialize le tracé.
   * @param Xoffset Abscisse du point central. 0 par défaut.
   * @param Yoffset Ordonnée du point central. 0 par défaut.
   * @param Xscale Echelle horizontale, le tracé se fait dans [-Xscale+Xoffset..Xscale+Xoffset].
   * @param Yscale Echelle verticale, le tracé se fait dans  [-Yscale+Yoffset..Yscale+Yoffset].
   * @return Cet objet, permettant de définir la construction <tt>new CurveOutput().reset(..)</tt>.
   */
  public CurveOutput reset(double Xoffset, double Yoffset, double Xscale, double Yscale) {
    this.Xoffset = Xoffset;
    this.Yoffset = Yoffset;
    this.Xscale = Xscale;
    this.Yscale = Yscale;
    curves = new ArrayList<ArrayList<point> >();
    for(int c = 0; c < 10; c++)
      curves.add(new ArrayList<point>());
    lines = new ArrayList<line>();
    ovals = new ArrayList<oval>();
    blocks = new ArrayList<block>();
    labels = new ArrayList<label>();
    repaint(0, 0, getWidth(), getHeight());
    return this;
  }
  /** Efface et initialize le tracé.
   * @param Xscale Echelle horizontale.
   * @param Yscale Echelle verticale.
   * @return Cet objet, permettant de définir la construction <tt>new CurveOutput().reset(..)</tt>.
   * @see #reset(double, double, double, double)
   */
  public CurveOutput reset(double Xscale, double Yscale) {
    return reset(0, 0, Xscale, Yscale);
  }
  /**
   * @see #reset(double, double, double, double)
   */
  public CurveOutput reset() {
    return reset(0, 0, 1, 1);
  }
  private double Xoffset = 0, Yoffset = 0, Xscale = 1, Yscale = 1;

  /** Ajoute un point à une courbe.
   * @param x Abscisse du point dans [-Xscale+Xoffset..Xscale+Xoffset].
   * @param y Ordonnée du point dans [-Yscale+Yoffset..Yscale+Yoffset].
   * @param c Couleur de la courbe, dans {0, 9}. Le code des couleurs est le suivant:
   * <div id="colors"><b>Code des couleurs</b><table>
   * <tr><td>0</td><td>1</td><td>2</td><td>3</td><td>4</td><td>5</td><td>6</td><td>7</td><td>8</td><td>9</td></tr>
   * <tr><td>noir</td><td>marron</td><td>rouge</td><td>orange</td><td>jaune</td><td>vert</td><td>bleu</td><td>violet</td><td>gris</td><td>blanc</td></td>
   * </table></div>
   * @return L'objet graphique créé, utilisé pour détruire l'objet ensuite.
   */
  public Object add(double x, double y, int c) {
    point p = new point();
    p.x = (x - Xoffset) / Xscale;
    p.y = (y - Yoffset) / Yscale;
    if((0 <= c) && (c < 10)) {
      curves.get(c).add(p);
    }
    repaint(0, 0, getWidth(), getHeight());
    return p;
  }
  /** Trace une ligne.
   * <p>Pour tracer un point, tracer une ligne de longueur nulle (<tt>add(x, y, x, y, c);</tt>.</p>
   * @param x1 Abscisse du point, dans [-Xscale+Xoffset..Xscale+Xoffset].
   * @param y1 Ordonnée du point, dans [-Yscale+Yoffset..Yscale+Yoffset].
   * @param x2 Abscisse du point, dans [-Xscale+Xoffset..Xscale+Xoffset].
   * @param y2 Ordonnée du point, dans [-Yscale+Yoffset..Yscale+Yoffset].
   * @param c Couleur du tracé, <a href="#colors">dans {0, 9}</a>.
   * @return L'objet graphique créé, utilisé pour détruire l'objet ensuite.
   */
  public Object add(double x1, double y1, double x2, double y2, int c) {
    line l = new line();
    l.x1 = (x1 - Xoffset) / Xscale;
    l.y1 = (y1 - Yoffset) / Yscale;
    l.x2 = (x2 - Xoffset) / Xscale;
    l.y2 = (y2 - Yoffset) / Yscale;
    l.c = 0 <= c && c < 10 ? colors[c] : Color.BLACK;
    lines.add(l);
    repaint(0, 0, getWidth(), getHeight());
    return l;
  }
  /** Trace un rectangle.
   * @param xmin Abcisse inférieure gauche, dans [-X, X], par défaut [-1, 1].
   * @param ymin Ordonnée inférieure gauche, dans [-Y, Y], par défaut [-1, 1].
   * @param xmax Abcisse supérieure droite, dans [-X, X], par défaut [-1, 1].
   * @param ymax Ordonnée supérieure droite, dans [-Y, Y], par défaut [-1, 1].
   * @param c Numéro de la courbe: 0 (noir, défaut), 1 (brun), 2 (rouge), 3 (orange), 4 (jaune), 5 (vert), 6 (bleu), 7 (violet), 8 (gris), 9 (blanc).
   * @return L'objet graphique créé, utilisé pour détruire l'objet ensuite.
   */
  public Object addRectangle(double xmin, double ymin, double xmax, double ymax, int c) {
    rectangle r = new rectangle();
    r.l1 = (line) add(xmin, ymin, xmax, ymin, c);
    r.l2 = (line) add(xmax, ymin, xmax, ymax, c);
    r.l3 = (line) add(xmax, ymax, xmin, ymax, c);
    r.l4 = (line) add(xmin, ymax, xmin, ymin, c);
    rectangles.add(r);
    return r;  
  }
  /** Trace un cercle.
   * @param x Abscisse du centre, dans [-Xscale+Xoffset..Xscale+Xoffset].
   * @param y Ordonnée du point, dans [-Yscale+Yoffset..Yscale+Yoffset].
   * @param r Rayon du cercle.
   * @param c Couleur du tracé, <a href="#colors">dans {0, 9}</a>.
   * @return L'objet graphique créé, utilisé pour détruire l'objet ensuite.
   */
  public Object add(double x, double y, double r, int c) {
    oval l = new oval();
    l.x = (x - Xoffset - r) / Xscale;
    l.y = (y - Yoffset + r) / Yscale;
    l.w = 2 * r / Xscale;
    l.h = 2 * r / Yscale;
    l.c = 0 <= c && c < 10 ? colors[c] : Color.BLACK;
    ovals.add(l);
    repaint(0, 0, getWidth(), getHeight());
    return l;
  }
  /** Trace un block rectangulaire.
   * <p>Pour tracer un point, tracer une ligne de longueur nulle (<tt>add(x, y, x, y, c);</tt>.</p>
   * @param x Abscisse du point supérieur gauche, dans [-Xscale+Xoffset..Xscale+Xoffset].
   * @param y Ordonnée du point supérieur gauche, dans [-Yscale+Yoffset..Yscale+Yoffset].
   * @param w Largeur du block.
   * @param h Hauteur du block.
   * @param c_f Couleur du fond du tracé, <a href="#colors">dans {0, 9}</a>.
   * @param c_b Couleur du bord du tracé, <a href="#colors">dans {0, 9}</a>, -1 si pas de tracé du bord.
   * @return L'objet graphique créé, utilisé pour détruire l'objet ensuite.
   */
  public Object add(double x, double y, double w, double h, int c_f, int c_b) {
    block l = new block();
    l.x = (x - Xoffset) / Xscale;
    l.y = (y + h - Yoffset) / Yscale;
    l.w = w / Xscale;
    l.h = h / Yscale;
    l.c_f = 0 <= c_f && c_f < 10 ? colors[c_f] : Color.BLACK;
    l.c_b = 0 <= c_b && c_b < 10 ? colors[c_b] : l.c_f;
    blocks.add(l);
    repaint(0, 0, getWidth(), getHeight());
    return l;
  }
  /** Trace une chaîne de caractères.
   * @param x Abscisse du coin en haut à gauche du texte, dans [-Xscale+Xoffset..Xscale+Xoffset].
   * @param y Ordonnée du coin en haut à gauche du texte, dans [-Yscale+Yoffset..Yscale+Yoffset].
   * @param s Texte à tracer.
   * @param c Couleur du tracé, <a href="#colors">dans {0, 9}</a>.
   * @return L'objet graphique créé, utilisé pour détruire l'objet ensuite.
   */
  public Object add(double x, double y, String s, int c) {
    label l = new label();
    l.x = (x - Xoffset) / Xscale;
    l.y = (y - Yoffset) / Yscale;
    l.s = s;
    l.c = 0 <= c && c < 10 ? colors[c] : Color.BLACK;
    labels.add(l);
    repaint(0, 0, getWidth(), getHeight());
    return l;
  }
  /** Détruit l'objet graphique spécifié.
   * @param object L'objet à détruire.
   * @return La valeur true si l'objet existait, false sinon.
   */
  public boolean remove(Object object) {
    if (object instanceof line && lines.contains((line) object)) {
      lines.remove((line) object);
      return true;
    }
    if (object instanceof rectangle && rectangles.contains((rectangle) object)) {
      rectangle r = (rectangle) object;
      lines.remove(r.l1);
      lines.remove(r.l2);
      lines.remove(r.l3);
      lines.remove(r.l4);
      rectangles.remove((rectangle) object);
      return true;
    }
    if (object instanceof oval && ovals.contains((oval) object)) {
      ovals.remove((oval) object);
      return true;
    }
    if (object instanceof block && blocks.contains((block) object)) {
      blocks.remove((block) object);
      return true;
    }
    if (object instanceof label && labels.contains((label) object)) {
      labels.remove((label) object);
      return true;
    } 
    if (object instanceof point)  
      for(int c = 0; c < 10; c++)
        if (curves.get(c).contains((point) object)) {
	  curves.get(c).remove((point) object);
	  return true;
        }
    return false;
  }
  /** Renvoie la position horizontale du réticule. */
  public double getReticuleX() {
    return Xoffset + Xscale * reticuleX;
  }
  /** Renvoie la position verticale du réticule. */
  public double getReticuleY() {
    return Yoffset + Yscale * reticuleY;
  }
  /** Définit la position du réticule.
   * @param x Abscisse du réticule, dans [-Xscale+Xoffset..Xscale+Xoffset].
   * @param y Reticule ordinate, dans [-Yscale+Yoffset..Yscale+Yoffset].
   */
  public void setReticule(double x, double y) {
    x -= Xoffset;
    x /= Xscale;
    y -= Yoffset;
    y /= Yscale;
    reticuleX = x < -1 ? -1 : x > 1 ? 1 : x;
    reticuleY = y < -1 ? -1 : y > 1 ? 1 : y;
    repaint(0, 0, getWidth(), getHeight());
  }
  private double reticuleX = 0, reticuleY = 0;

  private static Color colors[] = { Color.BLACK, new Color(150, 75, 0), Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.BLUE, Color.MAGENTA, Color.GRAY, Color.WHITE };

  /** Définit une portion de code appellée à chaque modification du réticule.
   * @param runnable La portion de code à appeler, ou null si il n'y en a pas.
   * @return Cet objet, permettant de définir la construction <tt>new CurveOutput().setRunnable(..)</tt>.
   */
  public CurveOutput setRunnable(Runnable runnable) {
    this.runnable = runnable;
    return this;
  }
  private Runnable runnable = null;
}
