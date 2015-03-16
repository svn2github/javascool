/*******************************************************************************
* David.Pichardie@inria.fr, Copyright (C) 2011.           All rights reserved. *
*******************************************************************************/

package org.javascool.proglets.gogleMaps;

import java.util.Map;
import java.util.List;
import java.util.Set;

import javax.swing.SwingUtilities;
import org.javascool.macros.Macros;

/** Définit les fonctions de la proglet qui permettent de tracer des chemins sur une carte de France.
 *
 * @see <a href="Functions.java.html">code source</a>
 * @serial exclude
 */
public class Functions {
  private static final long serialVersionUID = 1L;
  // @factory
  private Functions() {}
  /** Renvoie l'instance de la proglet pour accéder à ses éléments. */
  private static Panel getPane() {
    return Macros.getProgletPane();
  }
  /** Définit l'intensité d'uen route. */
  public enum IntensiteRoute {
    LEGER(1), MOYEN(2), FORT(3);
    private int value;
    IntensiteRoute(int i) {
      value = i;
    }
  }

  /** Calcule la distance entre deux villes. */
  public static double getDistance(String ville1, String ville2) {
    double x1 = latitudes.get(ville1), y1 = longitudes.get(ville1);
    double x2 = latitudes.get(ville2), y2 = longitudes.get(ville2);
    return distanceEuclidienne(y1, x1, y2, x2);
  }
  public final static IntensiteRoute LEGER = IntensiteRoute.LEGER;
  public final static IntensiteRoute MOYEN = IntensiteRoute.MOYEN;
  public final static IntensiteRoute FORT = IntensiteRoute.FORT;

  /**  Met en évidence un point de la carte désigné par ses coordonnées géographiques (longitude,latitude). */
  public static void affichePointSurCarte(double longitude, double latitude, int idx) {
    getPane().main.affichePoint(longitude, latitude, idx);
  };

  /** Met en évidence un point de la carte désigné par ses coordonnées géographiques (longitude,latitude) en numérotant ce point avec le nombre index. */
  public static void affichePointSurCarte(double longitude, double latitude) {
    getPane().main.affichePoint(longitude, latitude);
  }
  /** Trace une ligne droite entre un point de coordonnées géographiques (longitude1,latitude1) et un autre de coordonnées géographiques (longitude2,latitude2). */
  public static void afficheRouteSurCarte(double longitude1, double latitude1, double longitude2, double latitude2, IntensiteRoute intensite) {
    getPane().main.afficheRoute(longitude1, latitude1, longitude2, latitude2, intensite.value);
  }
  /** Trace une ligne droite entre un point de coordonnées géographiques (longitude1,latitude1) et un autre de coordonnées géographiques (longitude2,latitude2) en prenant une couleur d'intensité intensite. */
  public static void afficheRouteSurCarte(double longitude1, double latitude1, double longitude2, double latitude2) {
    getPane().main.afficheRoute(longitude1, latitude1, longitude2, latitude2);
  }
  /** Calcule la distance (en km) sur la sphère terrestre entre un point de coordonnées géographiques (longitude1,latitude1) et un autre de coordonnées géographiques (longitude2,latitude2). */
  public static int distanceEuclidienne(double longitude1, double latitude1, double longitude2, double latitude2) {
    return getPane().main.distanceEuclidienne(longitude1, latitude1, longitude2, latitude2);
  }
  /** Efface la carte. */
  public static void effaceCarte() {
    getPane().main.clearMap();
  }
  /** Table de toutes les villes. */
  public static Set<String> villes;
  /** Table des latitudes associée à chaque nom de ville. */
  public static Map<String, Double> latitudes;
  /** Table des longitudes associée à chaque nom de ville. */
  public static Map<String, Double> longitudes;
  /** Table des voisins de chaque ville. */
  public static Map<String, List<String> > voisins;

  /** Calcule un chemin sous forme d'une liste de noms de ville afin de relier la ville de nom depart à celle de nom arrivee en suivant uniquement des routes de la table voisins de la proglet. */
  public static List<String> plusCourtCheminGogleMap(String depart, String arrivee) {
    return GogleMapCalculChemins.plusCourtChemin(getPane().main, depart, arrivee);
  }
  /** Parcours en largeur de la carte à partir d'un point de départ. */
  public static void parcoursEnLargeur(final String depart) {
    SwingUtilities.invokeLater(new Runnable() {
                                 @Override
                                 public void run() {
                                   getPane().main.clearMap();
                                   GogleMapParcours.afficheToutesRoutesDirectes(getPane().main);
                                   GogleMapParcours.parcoursLargeur(getPane().main, depart);
                                 }
                               }
                               );
  }
}
