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
   
   nodes = new HashMap();
   
   
    
   
  }

  /** Ajoute ou modifie un noeud au graphe (modifie dans le cas ou meme nom employé et différentes coordonnées).
   * @param n Nom du noeud.
   * @param x Abcisse du noeud. 
   * @param y Ordonnée du noeud.
   */
  void addNode(String n, int x, int y) {
    
    if (nodes.containsKey(n)) {
      
      //removeNode(n);
      //nodes.put(n, new Node(n, x, y));
      Node N_ = (Node) nodes.get(n);
      N_.moveTo(x,y);
      
    } else {
      
      nodes.put(n, new Node(n, x, y));
      
    }
    
  }
  
  /** Renvoie l'objet Noeud à partir de son nom.
   * @param n Nom du noeud.
   * @return objet Node.
   */
   
  Node getNode(String n) {
    
    Node N_ = (Node) nodes.get(n);
    
    return N_;
    
  }
  
  /** Cherche noeud plus proche d'une position de la souris.
   * @param x Abcisse position souris.
   * @param y Ordonnée position souris.
   * @return Nom du noeud.
   */

  String getClosestNode(float x,float y)                              
  {
    //println("x " + x + " // y " + y);
    
    float curBest = 9999;
    Node best = (Node) nodes.get(listN[0]);
    String n_ = null;
    
    if (nodes.size() != 0) {
      for(String ni_ : (Iterable<String>) nodes.keySet()) 
      {   
        Node N_ = (Node) nodes.get(ni_);
  
        float d = dist(x,y,N_.x,N_.y);
        if(d < curBest)
        {
          curBest = d;
          best = N_;
          n_ = ni_;
        }
      }
    }
    //println(" nom: " + best.n + " x " + best.x + " // y " + best.y);
    return n_;
  }

  /** Détruit un noeud au graphe si il existe.
   * @param n Nom du noeud.
   */
  void removeNode(String n) {
    
      Node N_ = (Node) nodes.get(n);
                       
      // retire tous les liens en relation avec le noeud 
      for(String ni_ : (Iterable<String>) nodes.keySet()) 
      {   
        removeLink(n, ni_);
      }
      
      // retire le noeud en question
      nodes.remove(n); 
     
  }

  /** Donne la liste des noeuds en lien avec un noeud donné.
   * @param n Nom du noeud dont on veut les noeuds en lien.
   * @return La liste des noms des noeuds en lien avec le noeud donné.
   */
  String[] getNodes(String n) {
    
    String[] ListNodes = new String[10];
    Node N_ = (Node) nodes.get(n);
    int count = 0;
    
    for(String ni_ : (Iterable<String>) nodes.keySet()) 
    {
      if (isLink(n, ni_)) {
        ListNodes[count] = ni_;
        count++;
      }
    }
    
    return ListNodes;
     
  }


  /** Ajoute ou modifie un lien entre deux noeuds (modifie dans le cas ou memes noeuds et différent poids attribué).
   * @param nA Premier noeud du lien.
   * @param nB Deuxième noeud du lien.
   * @param p Poids du lien. // no?? poids=distance??
   */
  //void addLink(String nA, String nB, double p) {
  void addLink(String nA, String nB) {
    
    Node NA_ = (Node) nodes.get(nA);
    Node NB_ = (Node) nodes.get(nB);
    double p_ = dist(NA_.x,NA_.y,NB_.x,NB_.y)/100;
    NA_.links.put(nB, new Link(nA, nB, p_));
    NB_.links.put(nA, new Link(nB, nA, p_));
      
  }


  /** Détruit un lien entre deux noeuds si il existe.
   * @param nA Premier noeud du lien.
   * @param nB Deuxième noeud du lien.
   */
  void removeLink(String nA, String nB) {
    
    Node NA_ = (Node) nodes.get(nA);
    Node NB_ = (Node) nodes.get(nB);

    if (isLink(nA, nB)) {
     
     NA_.links.remove(nB);
     NB_.links.remove(nA);
     
    }
    
  }

  /** Affirme si il y a lien entre 2 noeuds.
   * @param nA Premier noeud du lien.
   * @param nB Deuxième noeud du lien.
   * @return oui ou non.
   */
  boolean isLink(String nA, String nB) {
    
    Node NA_ = (Node) nodes.get(nA);
    //String ni_;
    boolean link_ = false;
    
    for(String ni_ : (Iterable<String>) NA_.links.keySet())
    {
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
    
    Node NA_ = (Node) nodes.get(nA);
    Link li_;
    double p_ = 0;
    
    if (isLink(nA,nB)) {
     
      li_ = (Link) NA_.links.get(nB);
      p_ = li_.p;

    }
    
    return p_;
  }
  
  /**  Cherche noeud intermédiaire entre nInit et nTarget tel que la distance entre nInit et nTarget soit minimal.
   * @param nInit Noeud initial.
   * @param nTarget Noeud cible.
   * @return Noeud intermédiaire.
   */
   
  String exploreNode(String nInit, String nTarget)                            
  {
    if(nInit == nTarget)
      return nTarget;
    String next = nInit; // initialization
    Node Ninit = (Node) nodes.get(nInit);
    Node Ntarget = (Node) nodes.get(nTarget);
    float d = 999999;
    for(String ni_ : (Iterable<String>) Ninit.links.keySet())
    {
      Node Ni_ = (Node) nodes.get(ni_);
      if(path.indexOf(ni_) == -1 && restricted.indexOf(ni_) == -1) // si le noeud n'a pas été parcouru
      {
        float di = PVector.dist(Ni_.position, Ninit.position)+PVector.dist(Ntarget.position,Ni_.position);
        if(di < d )
        {
          d = di;
          next = ni_;
        }
      }
    }
    println("Distance parcourue par le trajet: " + d );
    path.add(nInit);
    return next;
  
  }
  
  
  /**   Construit le trajet avec tous les noeuds - appel à exploreNode
   * @param nStart Noeud départ.
   * @param nEnd Noeud final.
   */
   
  void findPath(String nStart, String nEnd)
  {
    
    path.clear();
    restricted.clear();
    println(" " + nStart + " à " + nEnd);
    String next = nStart;
    int its = 0;
    int tests = 0;
    while(next != nEnd && tests < 100)
    {
      next = exploreNode(next,nEnd);
      its++;
      if(its > 500)
      {
        its = 0;
        tests++;
        restricted.add(path.get(path.size()-1));
        next = (String) path.get(0);
        path.clear();
        //println(tests);
      }
    }
    if(tests < 100)
      path.add(nEnd);
    else
      background(255,0,0);
  }
    
}


// Taille pour l'insertion dans JavaScool
  public static final int WIDTH = 1024, HEIGHT = 700;
