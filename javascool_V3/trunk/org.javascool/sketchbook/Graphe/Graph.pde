class Graph {
  // http://processing.org/reference/HashMap.html
  /*
  class Node { 
  }
  HashMap nodes = new HashMap();
  nodes.put("toto", new Node(n, x, y));
  nodes.get("toto");
  */

  /** Ajoute ou modifie un noeud au graphe.
   * @param n Nom du noeud.
   * @param x Abcisse du noeud. 
   * @param y Ordonnée du noeud.
   */
  void addNode(String n, int x, int y) {
  }

  /** Détruit un noeud au graphe si il existe.
   * @param n Nom du noeud.
   */
  void removeNode(String n) {
  }

  /** Donne la liste des noeuds en lien avec un noeud donné.
   * @param n Nom du noeud dont on veut les noeuds en lien.
   * @return La liste des noms des noeuds en lien avec le noeud donné.
   */
  String[] getNodes(String n) {
  }

  /** Ajoute ou modifie un lien entre deux noeuds.
   * @param nA Premier noeud du lien.
   * @param nB Deuxième noeud du lien.
   * @param p Poids du lien.
   */
  void addLink(String nA, String nB, double p) {
  }

  /** Détruit un lien entre deux noeuds si il existe.
   * @param nA Premier noeud du lien.
   * @param nB Deuxième noeud du lien.
   */
  void removeLink(String nA, String nB) {
  }

  /** Donne le poids d'un lien entre deux noeuds.????
   * @param nA Premier noeud du lien.
   * @param nB Deuxième noeud du lien.
   * @return Le poids du lien où 0 si il n'y a pas de lien.????
   */
  boolean isLink(String nA, String nB) {
  }

  /** Donne le poids d'un lien entre deux noeuds.
   * @param nA Premier noeud du lien.
   * @param nB Deuxième noeud du lien.
   * @return Le poids du lien où 0 si il n'y a pas de lien.
   */
  double getLink(String nA, String nB) {
  }

}
