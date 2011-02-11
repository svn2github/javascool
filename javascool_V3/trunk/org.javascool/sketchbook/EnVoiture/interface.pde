
/* Fonctions pour javascool. */

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

public static void addSpot(String n, int col, char f, int x, int y, float d1, float d2, float h) {
  if (proglet == null) return;
  proglet.myTrip.addSpot(n, col, f, x, y, d1, d2, h);
}

/** Cherche spot plus proche d'une position.
 * @param x Abcisse position.
 * @param y Ordonnée position.
 * @return Nom du spot.
 */
public static String getClosestSpot(float x, float y) {
  if (proglet == null) return null;
  String S_;
  S_ = proglet.myTrip.getClosestSpot(x, y);
  return S_;
}

/** Détruit un spot au graphe si il existe.
 * @param n Nom du spot.
 */
public static void removeSpot(String n) {
  if (proglet == null) return;
  proglet.myTrip.removeSpot(n);  
}

/** Ajoute ou modifie un lien entre deux spots
 * @param nA Premier spot du lien.
 * @param nB Deuxième spot du lien.
 * ici poids du lien = distance euclidienne entre les deux spots.
 */
public static void addLink(String nA, String nB) {
  if (proglet == null) return;
  proglet.myTrip.addLink(nA, nB);  
}

/** Détruit un lien entre deux spots si il existe.
   * @param nA Premier spot du lien.
   * @param nB Deuxième spot du lien.
   */
public static void removeLink(String nA, String nB) {
  if (proglet == null) return;
  proglet.myTrip.removeLink(nA, nB);
}

/** Affirme si il y a lien entre 2 spots.
 * @param nA Premier spot du lien.
 * @param nB Deuxième spot du lien.
 * @return oui ou non.
 */
public static boolean isLink(String nA, String nB) {
  if (proglet == null) return false;
  boolean link_ = false;
  link_ = proglet.myTrip.isLink(nA, nB);
  return link_;
}

 /** Donne le poids d'un lien entre deux spots.
 * @param nA Premier spot du lien.
 * @param nB Deuxième spot du lien.
 * @return Le poids du lien où 0 si il n'y a pas de lien.
 */
public static double getLink(String nA, String nB) {
  if (proglet == null) return 0;
  double p_ = 0;
  p_ = proglet.myTrip.getLink(nA, nB);
  return p_;
}

 static EnVoiture proglet; 

