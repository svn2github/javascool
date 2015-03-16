/** Définit les fonctions de la proglet.
 *
 * @see <a href="enVoitureFunctions.java.html">code source</a>
 * @serial exclude
 */
public class enVoitureFunctions {
  private static final long serialVersionUID = 1L;
  /** Renvoie l'instance de la proglet pour accéder à ses éléments. */
  private static enVoiture getPane() {
    return org.javascool.macros.Macros.getProgletPane();
  }
  /** Ajoute ou modifie un spot au graphe (modifie dans le cas ou meme nom employé et différentes coordonnées).
   * @param n Nom du spot.
   * @param col Couleur du spot.
   * @param f forme du spot: 'B' = Box, 'P' = Pentagone, 'O' = Octogone, 'C' = Cylindre.
   * @param x Abcisse du spot.
   * @param y Ordonnée du spot.
   * @param d1 dimension1 à la base du spot.
   * @param d2 dimension2 au sommet du spot.
   * @param h hauteur du spot.
   */
  public static void addSpot(String n, int col, String f, int x, int y, float d1, float d2, float h) {
    getPane().myTrip.addSpot(n, col, f.charAt(0), x, y, d1, d2, h);
  }
  /** Cherche spot plus proche d'une position.
   * @param x Abcisse position.
   * @param y Ordonnée position.
   * @return Nom du spot.
   */
  public static String getClosestSpot(float x, float y) {
    String S_;
    S_ = getPane().myTrip.getClosestSpot(x, y);
    return S_;
  }
  /** Détruit un spot au graphe si il existe.
   * @param n Nom du spot.
   */
  public static void removeSpot(String n) {
    getPane().myTrip.removeSpot(n);
  }
  /** Ajoute ou modifie un lien entre deux spots
   * @param nA Premier spot du lien.
   * @param nB Deuxième spot du lien.
   * ici poids du lien = distance euclidienne entre les deux spots.
   */
  public static void addRoad(String nA, String nB) {
    getPane().myTrip.addLink(nA, nB);
  }
  /** Détruit un lien entre deux spots si il existe.
   * @param nA Premier spot du lien.
   * @param nB Deuxième spot du lien.
   */
  public static void removeRoad(String nA, String nB) {
    getPane().myTrip.removeLink(nA, nB);
  }
  /** Affirme si il y a lien entre 2 spots.
   * @param nA Premier spot du lien.
   * @param nB Deuxième spot du lien.
   * @return oui ou non.
   */
  public static boolean isRoad(String nA, String nB) {
    boolean link_ = false;
    link_ = getPane().myTrip.isLink(nA, nB);
    return link_;
  }
}

