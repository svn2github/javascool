/*******************************************************************************
* David.Pichardie@inria.fr, Copyright (C) 2011.           All rights reserved. *
*******************************************************************************/

package proglet.goglemap;
import java.util.*;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Image;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import java.awt.image.BufferedImage;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.BasicStroke;
import java.awt.Toolkit;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Font;
import java.awt.geom.Line2D;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import org.javascool.Macros;
import org.javascool.Utils;

class GogleMapPanel extends JPanel implements ActionListener {
  private static final long serialVersionUID = 1L;

  Map < String, List < String >> arcs;
  Map<String, Double> latitudes;
  Map<String, Double> longitudes;
  private Image ici_bleu;
  private Image ici_rouge;
  private Image france;
  private Set<PointAAfficher> pointsAfficheAvecNumero;
  private Set<PointAAfficher> pointsAfficheSansNumero;
  private Set<ArcAAfficher> arcsAffiche;
  private CartePanel carte;
  static private String buttonDFSString = "Parcours en profondeur";
  static private String buttonBFSString = "Parcours en largeur";
  private JButton buttonDFS;
  private JButton buttonBFS;
  private GogleMapPanel me = this;

  void clearMap() {
    pointsAfficheAvecNumero.clear();
    pointsAfficheSansNumero.clear();
    arcsAffiche.clear();
    carte.repaint();
  }
  private void drawRoad(Graphics g, double longitude1, double latitude1, double longitude2, double latitude2) {
    int xi = getX(longitude1) + 9;
    int yi = getY(latitude1) + 29;
    int xj = getX(longitude2) + 9;
    int yj = getY(latitude2) + 29;
    Graphics2D g2 = (Graphics2D) g;
    g2.setRenderingHint
      (RenderingHints.KEY_ANTIALIASING,
      RenderingHints.VALUE_ANTIALIAS_ON);
    g2.setStroke(new BasicStroke(6));
    g2.draw(new Line2D.Double(xi, yi, xj, yj));
  }
  void drawPoint(Graphics2D g, int x, int y, int indice) {
    // System.out.println("x ="+x+" y="+y+" i="+indice);
    g.drawImage((indice != -1) ? ici_bleu : ici_rouge, x, y, null);
    if((indice != -1) && (indice < 10))
      g.drawString("" + indice, x + 7, y + 13);
    else if(indice != -1)
      g.drawString("" + indice, x + 4, y + 13);
    // g.setColor(Color.BLACK);
    // g.fillRect(x+5, y+25, 12,12);
  }
  public void affichePoint(double longitude, double latitude, int idx) {
    pointsAfficheAvecNumero.add(new PointAAfficher(longitude, latitude, idx));
    carte.repaint();
  }
  public void affichePoint(double longitude, double latitude) {
    pointsAfficheSansNumero.add(new PointAAfficher(longitude, latitude, -1));
    carte.repaint();
  }
  // @param intensite: entier entre 1 et 5 pour l'intensite du tracé
  public void afficheRoute(double longitude1, double latitude1, double longitude2, double latitude2, int intensite) {
    arcsAffiche.add(new ArcAAfficher(longitude1, latitude1, longitude2, latitude2, intensite));
    carte.repaint();
  }
  public void afficheRoute(double longitude1, double latitude1, double longitude2, double latitude2) {
    arcsAffiche.add(new ArcAAfficher(longitude1, latitude1, longitude2, latitude2, 2));
    carte.repaint();
  }
  static int getX_Icon(double d) {
    return getX(d);
  }
  static int getX(double d) {
    return inv_lin_x(d);
  }
  static int getY_Icon(double d) {
    return getY(d);
  }
  static int getY(double d) {
    return inv_lin_y(d);
  }
  static int scaleX(int x) {
    return (int) Math.round(.981 * ((double) x)) - 9;
  }
  static int scaleY(int y) {
    return (int) Math.round(.98 * ((double) y)) - 3;
  }
  static double linTransf(int x, int x0, int x1, double y0, double y1) {
    // y - y0 = (y1-y0)/(x1-x0) * (x - x0)
    return y0 + (y1 - y0) / (((double) x1) - ((double) x0)) * (((double) x) - ((double) x0));
  }
  static double errTransf(int err, int x0, int x1, double y0, double y1) {
    return (y1 - y0) / (((double) x1) - ((double) x0)) * err;
  }
  static int invLinTransf(double x, double x0, double x1, int y0, int y1) {
    // y - y0 = (y1-y0)/(x1-x0) * (x - x0)
    return (int) Math.round(((double) y0) + ((double) (y1 - y0)) / (x1 - x0) * (x - x0));
  }
  static double lin_y(int latitude) {
    return linTransf(latitude, 192, 179, 48.392168, 48.586601);
  }
  static double lin_x(int longitude) {
    return linTransf(longitude, 6, 546, -4.486885, 7.745018);
  }
  static double err_y(int err) {
    return linTransf(err, 192, 179, 48.392168, 48.586601);
  }
  static double err_x(int err) {
    return linTransf(err, 6, 546, -4.486885, 7.745018);
  }
  static int inv_lin_y(double latitude) {
    return invLinTransf(latitude, 48.392168, 48.586601, 192, 179);
  }
  static int inv_lin_x(double longitude) {
    return invLinTransf(longitude, -4.486885, 7.745018, 6, 546);
  }
  static double square(double x) {
    return x * x;
  }

  int distanceEuclidienne(double longitude1, double latitude1, double longitude2, double latitude2) {
    double longitude = (longitude1 - longitude2) * Math.PI / 180;
    double aux = Math.cos(latitude1 * Math.PI / 180) * Math.cos(latitude2 * Math.PI / 180) * Math.cos(longitude);
    aux = aux + Math.sin(latitude1 * Math.PI / 180) * Math.sin(latitude2 * Math.PI / 180);
    return (int) Math.round(6378 * Math.acos(aux));
  }
  void ajoute(int i, String ville, double _latitude, double _longitude) {
    latitudes.put(ville, _latitude);
    longitudes.put(ville, _longitude);
  }
  private class ParcoursEnLargeur extends SwingWorker<Void, Void>{
    protected Void doInBackground() {
      me.clearMap();
      GogleMapParcours.afficheToutesRoutesDirectes(me);
      GogleMapParcours.parcoursLargeur(me, "Paris");
      me.buttonBFS.setEnabled(true);
      me.buttonDFS.setEnabled(true);
      return null;
    }
  }

  private class ParcoursEnProfondeur extends SwingWorker<Void, Void>{
    protected Void doInBackground() {
      me.clearMap();
      GogleMapParcours.afficheToutesRoutesDirectes(me);
      GogleMapParcours.parcoursProfondeur(me, "Paris");
      me.buttonBFS.setEnabled(true);
      me.buttonDFS.setEnabled(true);
      return null;
    }
  }

  public void actionPerformed(ActionEvent e) {
    String action = e.getActionCommand();
    final GogleMapPanel me = this;
    if(action.equals(buttonBFSString)) {
      buttonBFS.setEnabled(false);
      buttonDFS.setEnabled(false);
      (new ParcoursEnLargeur()).execute();
    } else if(action.equals(buttonDFSString)) {
      buttonBFS.setEnabled(false);
      buttonDFS.setEnabled(false);
      (new ParcoursEnProfondeur()).execute();
    }
  }
  GogleMapPanel() {
    super(new BorderLayout());

    buttonDFS = new JButton(buttonDFSString);
    buttonBFS = new JButton(buttonBFSString);
    buttonDFS.setActionCommand(buttonDFSString);
    buttonBFS.setActionCommand(buttonBFSString);
    buttonDFS.addActionListener(this);
    buttonBFS.addActionListener(this);
    JPanel groupBoutons = new JPanel(new GridLayout(1, 0));
    groupBoutons.add(buttonDFS);
    groupBoutons.add(buttonBFS);
    add(carte = new CartePanel(), BorderLayout.CENTER);
    add(groupBoutons, BorderLayout.SOUTH);

    try {
      ici_bleu = Utils.getIcon("proglet/goglemap/doc-files/ici_bleu.png").getImage();
      ici_rouge = Utils.getIcon("proglet/goglemap/doc-files/ici_rouge.png").getImage();
      france = Utils.getIcon("proglet/goglemap/doc-files/carteDeFrance.png").getImage();
    } catch(Exception e) {
      System.out.println("Erreur au read : " + e);
    }
    latitudes = new HashMap<String, Double>();
    longitudes = new HashMap<String, Double>();

    ajoute(0, "Dunkerque", 51.069360, 2.376571);
    ajoute(1, "Calais", 50.979622, 1.855583);
    ajoute(2, "Lille", 50.650582, 3.056121);
    ajoute(3, "Béthune", 50.545887, 2.648391);
    ajoute(4, "Lens", 50.381367, 3.056121);
    ajoute(5, "Valenciennes", 50.366410, 3.531806);
    ajoute(6, "Amiens", 49.887806, 2.308616);
    ajoute(7, "Le Havre", 49.483984, 0.134056);
    ajoute(8, "Rouen", 49.439114, 1.108078);
    ajoute(9, "Reims", 49.259638, 4.007492);
    ajoute(10, "Thionville", 49.364333, 6.182052);
    ajoute(11, "Metz", 49.110074, 6.182052);
    ajoute(12, "Strasbourg", 48.586601, 7.745017);
    ajoute(13, "Nancy", 48.691295, 6.204704);
    ajoute(14, "Paris", 48.855815, 2.353920);
    ajoute(15, "Caen", 49.169900, -0.386932);
    ajoute(16, "Troyes", 48.287473, 4.052795);
    ajoute(17, "Brest", 48.392168, -4.486885);
    ajoute(18, "Lorient", 47.749043, -3.376953);
    ajoute(19, "Rennes", 48.107996, -1.678077);
    ajoute(20, "Le Mans", 48.003301, 0.202011);
    ajoute(21, "Orléans", 47.913563, 1.900886);
    ajoute(22, "Tours", 47.405046, 0.700347);
    ajoute(23, "Angers", 47.479828, -0.568145);
    ajoute(24, "Nantes", 47.240526, -1.564819);
    ajoute(25, "Saint-Nazaire", 47.285395, -2.199066);
    ajoute(26, "Dijon", 47.330264, 5.049469);
    ajoute(27, "Mulhouse", 47.763999, 7.337287);
    ajoute(28, "Montbéliard", 47.509741, 6.793647);
    ajoute(29, "Besançon", 47.270439, 6.023490);
    ajoute(30, "Annemasse", 46.268361, 6.227355);
    ajoute(31, "Annecy", 45.969233, 6.159400);
    ajoute(32, "Chambéry", 45.670105, 5.932884);
    ajoute(33, "Grenoble", 45.296196, 5.706367);
    ajoute(34, "Lyon", 45.834626, 4.800300);
    ajoute(35, "Saint-Etienne", 45.550454, 4.369918);
    ajoute(36, "Valence", 45.071850, 4.890907);
    ajoute(37, "Nice", 43.950121, 7.269332);
    ajoute(38, "Toulon", 43.426648, 5.932884);
    ajoute(39, "Marseille", 43.591168, 5.343940);
    ajoute(40, "Avigon", 44.174467, 4.822952);
    ajoute(41, "Nîmes", 44.084729, 4.347267);
    ajoute(42, "Montpellier", 43.860383, 3.871582);
    ajoute(43, "Perpignan", 43.037782, 2.874908);
    ajoute(44, "Toulouse", 43.860383, 1.425201);
    ajoute(45, "Pau", 43.591168, -0.364280);
    ajoute(46, "Bayonne", 43.755688, -1.496864);
    ajoute(47, "Bordeaux", 44.997068, -0.593449);
    ajoute(48, "Clermont-Ferrand", 45.879495, 3.078773);
    ajoute(49, "Limoges", 45.909408, 1.243988);
    ajoute(50, "Angoulême", 45.744887, 0.156707);
    ajoute(51, "La Rochelle", 46.238448, -1.157089);
    ajoute(52, "Poitiers", 46.627314, 0.315269);

    arcs = new HashMap < String, List < String >> ();
    for(String ville : latitudes.keySet())
      arcs.put(ville, new ArrayList<String>());
    ajouteArc("Brest", "Lorient");
    ajouteArc("Brest", "Rennes");
    ajouteArc("Lorient", "Rennes");
    ajouteArc("Rennes", "Nantes");
    ajouteArc("Nantes", "Saint-Nazaire");
    ajouteArc("Rennes", "Le Mans");
    ajouteArc("Le Mans", "Paris");
    ajouteArc("Paris", "Orléans");
    ajouteArc("Le Mans", "Tours");
    ajouteArc("Orléans", "Limoges");
    ajouteArc("Le Mans", "Angers");
    ajouteArc("Nantes", "La Rochelle");
    ajouteArc("La Rochelle", "Angoulême");
    ajouteArc("Nantes", "Angoulême");
    ajouteArc("Angers", "Nantes");
    ajouteArc("Poitiers", "Angoulême");
    ajouteArc("Tours", "Poitiers");
    ajouteArc("Angoulême", "Bordeaux");
    ajouteArc("Bordeaux", "Bayonne");
    ajouteArc("Bayonne", "Pau");
    ajouteArc("Pau", "Toulouse");
    ajouteArc("Bordeaux", "Toulouse");
    ajouteArc("Toulouse", "Perpignan");
    ajouteArc("Toulouse", "Montpellier");
    ajouteArc("Montpellier", "Nîmes");
    ajouteArc("Nîmes", "Avigon");
    ajouteArc("Avigon", "Marseille");
    ajouteArc("Marseille", "Toulon");
    ajouteArc("Toulon", "Nice");
    ajouteArc("Avigon", "Valence");
    ajouteArc("Valence", "Grenoble");
    ajouteArc("Grenoble", "Chambéry");
    ajouteArc("Chambéry", "Annecy");
    ajouteArc("Annecy", "Annemasse");
    ajouteArc("Valence", "Saint-Etienne");
    ajouteArc("Lyon", "Saint-Etienne");
    ajouteArc("Lyon", "Grenoble");
    ajouteArc("Clermont-Ferrand", "Saint-Etienne");
    ajouteArc("Clermont-Ferrand", "Limoges");
    ajouteArc("Limoges", "Angoulême");
    ajouteArc("Paris", "Troyes");
    ajouteArc("Troyes", "Dijon");
    ajouteArc("Dijon", "Besançon");
    ajouteArc("Dijon", "Lyon");
    ajouteArc("Besançon", "Montbéliard");
    ajouteArc("Montbéliard", "Mulhouse");
    ajouteArc("Mulhouse", "Strasbourg");
    ajouteArc("Strasbourg", "Nancy");
    ajouteArc("Nancy", "Paris");
    ajouteArc("Troyes", "Nancy");
    ajouteArc("Nancy", "Metz");
    ajouteArc("Metz", "Thionville");
    ajouteArc("Metz", "Reims");
    ajouteArc("Reims", "Paris");
    ajouteArc("Paris", "Rouen");
    ajouteArc("Rouen", "Le Havre");
    ajouteArc("Caen", "Rennes");
    ajouteArc("Rouen", "Caen");
    ajouteArc("Calais", "Dunkerque");
    ajouteArc("Dunkerque", "Béthune");
    ajouteArc("Lille", "Béthune");
    ajouteArc("Béthune", "Lens");
    ajouteArc("Lens", "Valenciennes");
    ajouteArc("Lens", "Lille");
    ajouteArc("Lens", "Paris");
    ajouteArc("Amiens", "Paris");
    ajouteArc("Amiens", "Lens");
    ajouteArc("Reims", "Lens");
    ajouteArc("Lens", "Lille");

    pointsAfficheAvecNumero = new TreeSet<PointAAfficher>();
    pointsAfficheSansNumero = new TreeSet<PointAAfficher>();
    arcsAffiche = new HashSet<ArcAAfficher>();
  }
  // int searchPoint(double x, double y) {
  // int error = 6;
  // for (int i=0; i<nb_villes; i++)
  // if (Math.abs(inv_lin_x(longitude[i])-x)<=error && Math.abs(inv_lin_y(latitude[i])-y)<=error)
  // return i;
  // return -1;
  // }

  void ajouteArc(String depart, String arrivee) {
    ajouteArcAux(depart, arrivee);
    ajouteArcAux(arrivee, depart);
  }
  void ajouteArcAux(String depart, String arrivee) {
    arcs.get(depart).add(arrivee);
  }
  class CartePanel extends JPanel {
    private static final long serialVersionUID = 1L;
    CartePanel() {
      setPreferredSize(new Dimension(640, 640));
    }
    protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      g.drawImage(france, 0, 0, null);
      for(ArcAAfficher a : arcsAffiche) {
        g.setColor(new Color(1.f, 0.f, 0.f, a.intensite * .3f));
        drawRoad(g, a.longitude1, a.latitude1, a.longitude2, a.latitude2);
      }
      int screenRes = Toolkit.getDefaultToolkit().getScreenResolution();
      int fontSize = (int) Math.round(10.0 * screenRes / 72.0);
      Font font = new Font("Arial", Font.BOLD, fontSize);
      Graphics2D g2d = (Graphics2D) g;
      g2d.setFont(font);
      g2d.setColor(Color.WHITE);
      g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      for(PointAAfficher p : pointsAfficheSansNumero)
        drawPoint(g2d, GogleMapPanel.getX(p.x), GogleMapPanel.getY(p.y), p.idx);
      for(PointAAfficher p : pointsAfficheAvecNumero)
        drawPoint(g2d, GogleMapPanel.getX(p.x), GogleMapPanel.getY(p.y), p.idx);
    }
  }
}

class PointAAfficher implements Comparable {
  double x;
  double y;
  int idx;
  PointAAfficher(double _x, double _y, int _idx) {
    x = _x;
    y = _y;
    idx = _idx;
  }
  public int compareTo(Object o) {
    PointAAfficher p = (PointAAfficher) o;
    int cmp1 = (int) Math.round(1000 * (p.x - x));
    if(cmp1 != 0)
      return cmp1;
    else
      return (int) Math.round(1000 * (p.y - y));
  }
}

class ArcAAfficher {
  double longitude1;
  double latitude1;
  double longitude2;
  double latitude2;
  int intensite;
  ArcAAfficher(double _longitude1, double _latitude1, double _longitude2, double _latitude2, int _intensite) {
    longitude1 = _longitude1;
    latitude1 = _latitude1;
    longitude2 = _longitude2;
    latitude2 = _latitude2;
    intensite = _intensite;
  }
}

