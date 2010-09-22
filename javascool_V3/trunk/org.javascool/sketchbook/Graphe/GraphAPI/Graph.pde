class Graph {
  // http://processing.org/reference/HashMap.html
  /*
  class Node { 
  }
  HashMap nodes = new HashMap();
  nodes.put("toto", new Node(n, x, y));
  nodes.get("toto");
  */
  
  HashMap nodes;
  
  Graph() {
   
   HashMap nodes = new HashMap();
   
  }

  /** Ajoute ou modifie un noeud au graphe (modifie dans le cas ou meme nom employé et différentes coordonnées).
   * @param n Nom du noeud.
   * @param x Abcisse du noeud. 
   * @param y Ordonnée du noeud.
   */
  void addNode(String n, int x, int y) {
    
    nodes.put(n, new Node(n, x, y));
    
  }
  
  /** Renvoie l'objet Noeud à partir de son nom.
   * @param n Nom du noeud.
   * @return objet Node.
   */
   
  Node getNode(n) {
    
    Node N_;
    
    
    return N_;
    
  }

  /** Détruit un noeud au graphe si il existe.
   * @param n Nom du noeud.
   */
  void removeNode(String n) {
    
    Node N_ = (Node) nodes.get(n);
    nodes.remove(n);                  // retire le noeud en question
    N_.links.clear();                 // retire tous les liens en relation avec le noeud
     
  }

  /** Donne la liste des noeuds en lien avec un noeud donné.
   * @param n Nom du noeud dont on veut les noeuds en lien.
   * @return La liste des noms des noeuds en lien avec le noeud donné.
   */
  String[] getNodes(String n) {
    
    ListNodes = new String[];
    Node N_ = nodes.get(n);
    String ni_;
    
    Collection collectionString = N_.links.values(); //  est une collection
    Iterator iterator = collectionString.iterator();
    while (iterator.hasNext()) {
      ni_ = iterator.next();
      if (isLink(n, ni_)) {
        ListNodes.add(ni_);
      }
    }
     
  }


  /** Ajoute ou modifie un lien entre deux noeuds (modifie dans le cas ou memes noeuds et différent poids attribué).
   * @param nA Premier noeud du lien.
   * @param nB Deuxième noeud du lien.
   * @param p Poids du lien.
   */
  void addLink(String nA, String nB, double p) {
    
    Node NA_ = nodes.get(nA);
    Node NB_ = nodes.get(nB);
    NA_.links.put(nA, new Link(nA, nB, p));
      
  }

  /** Détruit un lien entre deux noeuds si il existe.
   * @param nA Premier noeud du lien.
   * @param nB Deuxième noeud du lien.
   */
  void removeLink(String nA, String nB) {
    
    Node NA_ = nodes.get(nA);
    
    if (isLink(nA, nB)) {
     
     NA_.links.remove(nB);
     
    }
    
  }

  /** Affirme si il y a lien entre 2 noeuds.
   * @param nA Premier noeud du lien.
   * @param nB Deuxième noeud du lien.
   * @return oui ou non.
   */
  boolean isLink(String nA, String nB) {
    
    Node NA_ = nodes.get(nA);
    String ni_;
    boolean link_ = false;
    
    Collection collectionString = NA_.links.values(); // est une collection
    Iterator iterator = collectionString.iterator();
    while (iterator.hasNext()) {
      ni_ = iterator.next();
      if (ni_.equals(nB)) { // test si les deux string sont équivalents
        link_ = true;
      }
    }
    
    return link_;
  }

  /** Donne le poids d'un lien entre deux noeuds.
   * @param nA Premier noeud du lien.
   * @param nB Deuxième noeud du lien.
   * @return Le poids du lien où 0 si il n'y a pas de lien.
   */
  double getLink(String nA, String nB) {
    
    Node NA_ = nodes.get(nA);
    Link li_;
    double p_ = 0;
    
    if (isLink(nA,nB)) {
     
      li_ = NA_.links.get(nB);
      p_ = li_.p;

    }
    
    return p_;
  }
  
  

}
