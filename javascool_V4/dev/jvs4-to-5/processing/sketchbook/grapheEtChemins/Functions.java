package org.javascool.proglets.grapheEtChemins;
/** Définit les fonctions de la proglet.
 *
 * @see <a href="Functions.java.html">code source</a>
 * @serial exclude
 */
public class Functions {
  private static final long serialVersionUID = 1L;
  /** Renvoie l'instance de la proglet pour accéder à ses éléments. */
  private static grapheEtChemins getPane() {
    return org.javascool.macros.Macros.getProgletPane();
  }
  /** Ajoute ou modifie les coordonnées d'un noeud du graphe.
   * @param n Nom du noeud.
   * @param x Abcisse du noeud.
   * @param y Ordonnée du noeud.
   */
  public static void addNode(String n, int x, int y) {
    getPane().myGraph.addNode(n, x, y);
  }
  /** Renvoie le noeud le plus proche d'une position.
   * @param x Abcisse position.
   * @param y Ordonnée position.
   * @return Nom du noeud ou null si il n'y a pas de noeud.
   */
  public static String getClosestNode(double x, double y) {
    String n_ = null;
    n_ = getPane().myGraph.getClosestNode((float) x, (float) y);
    return n_;
  }
  /** Détruit un noeud du graphe si il existe.
   * @param n Nom du noeud.
   */
  public static void removeNode(String n) {
    getPane().myGraph.removeNode(n);
  }
  /** Donne la liste de tous les noeuds.
   * @return La liste des noms des noeuds.
   */
  public static String[] getAllNodes() {
    String[] ListN_ = new String[50];
    ListN_ = getPane().myGraph.getAllNodes();
    return ListN_;
  }
  /** Donne la liste des noeuds en lien avec un noeud donné.
   * @param n Nom du noeud dont on veut les noeuds en lien.
   * @return La liste des noms des noeuds en lien avec le noeud donné.
   */
  public static String[] getLinkedNodes(String n) {
    String[] ListN_ = new String[50];
    ListN_ = getPane().myGraph.getNodes(n);
    return ListN_;
  }
  /** Ajoute ou modifie un lien entre deux noeuds (modifie dans le cas ou memes noeuds et différent poids attribué).
   * @param nA Premier noeud du lien.
   * @param nB Deuxième noeud du lien.
   * @param p Poids du lien.
   */
  public static void addLink(String nA, String nB, double p) {
    getPane().myGraph.addLink(nA, nB, p);
  }
  /** Détruit un lien entre deux noeuds si il existe.
   * @param nA Premier noeud du lien.
   * @param nB Deuxième noeud du lien.
   */
  public static void removeLink(String nA, String nB) {
    getPane().myGraph.removeLink(nA, nB);
  }
  /** Affirme si il y a lien entre 2 noeuds.
   * @param nA Premier noeud du lien.
   * @param nB Deuxième noeud du lien.
   * @return "vrai" ou "faux".
   */
  public static boolean isLink(String nA, String nB) {
    boolean a_ = false;
    a_ = getPane().myGraph.isLink(nA, nB);
    return a_;
  }
  /** Donne le poids d'un lien entre deux noeuds.
   * @param nA Premier noeud du lien.
   * @param nB Deuxième noeud du lien.
   * @return Le poids du lien où 0 si il n'y a pas de lien.
   */
  public static double getLink(String nA, String nB) {
    double p_ = 0;
    p_ = getPane().myGraph.getLink(nA, nB);
    return p_;
  }
  /** Lance l'algorithme de Dijkstra entre deux noeuds.
   * @param nStart Noeud départ.
   * @param nEnd Noeud final.
   */
  public static void dijkstra(String nStart, String nEnd) {
    getPane().myGraph.dijkstra(nStart, nEnd);
  }
}

