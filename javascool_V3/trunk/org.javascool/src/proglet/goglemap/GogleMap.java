/*******************************************************************************
* David.Pichardie@inria.fr, Copyright (C) 2011.           All rights reserved. *
*******************************************************************************/

package proglet.goglemap;

// Used to define the gui
import java.util.Map;
import java.util.List;

import javax.swing.JPanel;

import org.javascool.Jvs2Java;

import proglet.ingredients.Console;

/** Définit une proglet javascool qui permet de tracer des chemins sur une carte de France.
 * @see <a href="doc-files/about-proglet.htm">Description</a>
 * @see <a href="doc-files/the-proglet.htm">La proglet</a>
 * @see <a href="GogleMap.java.html">code source</a> 
 * @serial exclude
 */
public class GogleMap implements org.javascool.Proglet {
  private GogleMap() {}
  private static final long serialVersionUID = 1L;
  // This defines the panel to display
  private static class Panel extends JPanel {
    private static final long serialVersionUID = 1L;
    public Panel() {
      add(main = new GogleMapPanel());
      voisins = main.arcs;
      latitudes = main.latitudes;
      longitudes = main.longitudes;
    }
    private GogleMapPanel main;
  }

  //
  // This defines the javascool interface
  //
  public enum Intensite { LEGER (1), MOYEN (2), FORT (3);
  	private int value;	
  	Intensite(int i) { value = i; } 
  }
  
  public final static Intensite LEGER = Intensite.LEGER;
  public final static Intensite MOYEN = Intensite.MOYEN;
  public final static Intensite FORT = Intensite.FORT;
  
  public static void affichePoint(double longitude, double latitude, int idx) {
	  panel.main.affichePoint(longitude,latitude,idx);
  };
  
  public static void affichePoint(double longitude, double latitude) {
	  panel.main.affichePoint(longitude,latitude);
  }

  public static void afficheRoute(double longitude1, double latitude1, double longitude2, double latitude2, Intensite intensite) {
	  panel.main.afficheRoute(longitude1,latitude1,longitude2,latitude2,intensite.value);
  }

  public static void afficheRoute(double longitude1, double latitude1, double longitude2, double latitude2) {
	  panel.main.afficheRoute(longitude1,latitude1,longitude2,latitude2);
  }

  public static int distanceEuclidienne(double longitude1, double latitude1, double longitude2, double latitude2) {
	  return panel.main.distanceEuclidienne(longitude1,latitude1,longitude2,latitude2); 
  }
  
  public static void efface() {
	  panel.main.clearMap();	
  }
	  
  public static Map<String,Double> latitudes;
  public static Map<String,Double> longitudes;
  public static Map<String,List<String>> voisins;

  //
  // This defines the tests on the panel
  //
  public static void test() {

  }
  /** Définition de l'interface graphique de la proglet. */
  public static final Panel panel = new Panel();
}

