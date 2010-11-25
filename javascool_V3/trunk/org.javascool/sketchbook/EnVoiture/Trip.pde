class Trip {
  HashMap spots;

  Trip() {
    spots = new HashMap();
  }

  /** Ajoute ou modifie un spot au graphe (modifie dans le cas ou meme nom employé et différentes coordonnées).
   * @param n Nom du spot.
   * @param x Abcisse du spot.
   * @param y Ordonnée du spot.
   */
  void addSpot(String n, int col, char f, int x, int y, float d1_, float d2_, float h_) {
    if(spots.containsKey(n)) {
      Spot S_ = (Spot) spots.get(n);
      S_.moveTo(x, y, d1_, d2_, h_);
    } else
      spots.put(n, new Spot(n, col, f, x, y, d1_, d2_, h_));
  }
  /** Renvoie l'objet Spot à partir de son nom.
   * @param n Nom du spot.
   * @return objet Spot.
   */
  Spot getSpot(String n) {
    Spot S_ = (Spot) spots.get(n);

    return S_;
  }
  /** Cherche spot plus proche d'une position.
   * @param x Abcisse position souris.
   * @param y Ordonnée position souris.
   * @return Nom du spot.
   */
  String getClosestSpot(float x, float y) {
    // println("x " + x + " // y " + y);

    float curBest = 9999;
    Spot best = (Spot) spots.get(listN[0]);
    String n_ = null;
    if(spots.size() != 0)
      for(String ni_ : (Iterable<String> )spots.keySet()) {
        Spot S_ = (Spot) spots.get(ni_);

        float d = dist(x, y, S_.x, S_.y);
        if(d < curBest) {
          curBest = d;
          best = S_;
          n_ = ni_;
        }
      }
     // println(" nom: " + best.n + " x " + best.x + " // y " + best.y);
    return n_;
  }
  /** Détruit un spot au graphe si il existe.
   * @param n Nom du spot.
   */
  void removeSpot(String n) {
    Spot S_ = (Spot) spots.get(n);
    // retire tous les liens en relation avec le noeud
    for(String ni_ : (Iterable<String> )spots.keySet())
      removeLink(n, ni_);
       // retire le noeud en question
    spots.remove(n);
  }
  /** Ajoute ou modifie un lien entre deux spots
   * @param nA Premier spot du lien.
   * @param nB Deuxième spot du lien.
   * @param p Poids du lien. // no?? poids=distance??
   */
  // void addLink(String nA, String nB, double p) {
  void addLink(String nA, String nB) {
    Spot SA_ = (Spot) spots.get(nA);
    Spot SB_ = (Spot) spots.get(nB);
    double p_ = dist(SA_.x, SA_.y, SB_.x, SB_.y) / 100;
    SA_.links.put(nB, new Link(nA, nB, p_));
    SB_.links.put(nA, new Link(nB, nA, p_));
  }
  /** Détruit un lien entre deux spots si il existe.
   * @param nA Premier spot du lien.
   * @param nB Deuxième spot du lien.
   */
  void removeLink(String nA, String nB) {
    Spot SA_ = (Spot) spots.get(nA);
    Spot SB_ = (Spot) spots.get(nB);
    if(isLink(nA, nB)) {
      SA_.links.remove(nB);
      SB_.links.remove(nA);
    }
  }
  /** Affirme si il y a lien entre 2 spots.
   * @param nA Premier spot du lien.
   * @param nB Deuxième spot du lien.
   * @return oui ou non.
   */
  boolean isLink(String nA, String nB) {
    Spot SA_ = (Spot) spots.get(nA);
    // String ni_;
    boolean link_ = false;
    for(String ni_ : (Iterable<String> )SA_.links.keySet())
      if(ni_.equals(nB))    // test si les deux string sont équivalents
        link_ = true;
    return link_;
  }
  /** Donne le poids d'un lien entre deux spots.
   * @param nA Premier spot du lien.
   * @param nB Deuxième spot du lien.
   * @return Le poids du lien où 0 si il n'y a pas de lien.
   */
  double getLink(String nA, String nB) {
    Spot SA_ = (Spot) spots.get(nA);
    Link li_;
    double p_ = 0;
    if(isLink(nA, nB)) {
      li_ = (Link) SA_.links.get(nB);
      p_ = li_.p;
    }
    return p_;
  }
  /** Donne la distance entre deux spots.
   * @param nA Premier spot.
   * @param nB Deuxième spot.
   * @return La distance.
   */
  double getDistance(String nA, String nB) {
    Spot SA_ = (Spot) spots.get(nA);
    Spot SB_ = (Spot) spots.get(nB);
    double p_ = 0.0;
    p_ = dist(SA_.x, SA_.y, SB_.x, SB_.y) / 100;

    return p_;
  }
  /**  Cherche spot intermédiaire entre sInit et sTarget tel que la distance entre sInit et sTarget soit minimal.
   * @param nInit Spot initial.
   * @param nTarget Spot cible.
   * @return Spot intermédiaire.
   */
  String exploreSpot(String sO, String sInit, String sTarget, ArrayList Visited) {
    if(sInit == sTarget)
      return sTarget;
    String next = sInit; // initialization
    Spot Sinit = (Spot) spots.get(sInit);
    Spot Starget = (Spot) spots.get(sTarget);
    double p = 99999999;
    ArrayList Visited_ = new ArrayList();
    for(int i = 0; i < Visited.size(); i++)
      Visited_.add((String) Visited.get(i));
    HashMap spotsN = new HashMap();
    for(String ni_ : (Iterable<String> )spots.keySet())
      if(!(Visited_.contains(ni_)))
        spotsN.put(ni_, (Spot) spots.get(ni_));                             // contraindre à chercher parmi les spots non visités!
    for(String ni_ : (Iterable<String> )spots.keySet())
      if((!(sInit.equals(sO) && ni_.equals(sTarget)) && !ni_.equals(sInit))) {
        Spot Si_ = (Spot) spots.get(ni_);
        if((path.indexOf(ni_) == -1) && (restricted.indexOf(ni_) == -1)) { // si le spot n'a pas été parcouru
          double pi = PVector.dist(Si_.position, Sinit.position) + PVector.dist(Starget.position, Si_.position);
          // println(pi);
          // double pi = getLink(ni_,sInit) + getLink(sTarget,ni_);
          if(pi < p) {
            p = pi;
            next = ni_;
          }
        }
      }
    if(sInit.equals(sO))
      println("Distance parcourue par le trajet: " + p / 100);
      // comp = p/100;
    path.add(sInit);
    // println(next);
    return next;
  }
  /**   Construit le trajet avec tous les spots - appel à exploreSpot
   * @param sStart Spot départ.
   * @param sEnd Spot final.
   */
  String findPath(String sStart, String sEnd, ArrayList VisitedSpots) {
    path.clear();
    restricted.clear();
    println(" " + sStart + " à " + sEnd);
    String next = sStart, interm = null;
    int its = 0;
    int tests = 0;
    while(next != sEnd && tests < 100) {
      next = exploreSpot(sStart, next, sEnd, VisitedSpots);
      its++;
      if(its > 500) {
        its = 0;
        tests++;
        restricted.add(path.get(path.size() - 1));
        next = (String) path.get(0);
        path.clear();
        println(tests);
      }
    }
    if(tests < 100)
      path.add(sEnd);
    for(int i = 0; i < path.size(); i++) {
      String p = (String) path.get(i);
      if(!(p.equals(sStart)) && !(p.equals(sEnd)))
        interm = p;
    }
    println("interm: " + interm);
    return interm;
  }
  /**   Algorithme de Dijkstra
   * @param sStart Spot départ.
   * @param sEnd Spot final.
   */
  void dijkstra(String sStart, String sEnd) {
    path.clear();
    println(" " + sStart + " à " + sEnd);
    for(String ni_ : (Iterable<String> )spots.keySet()) {
      Spot S_ = (Spot) spots.get(ni_);
      S_.init();
    }
    opened.clear();
    closed.clear();
    opened.add(sStart);
    Spot Ss = (Spot) spots.get(sStart);
    Ss.g = 0;
    while(opened.size() > 0) {
      String nCurrent = (String) opened.remove(0);
      // println("nCurrent: " + nCurrent);
      closed.add(nCurrent);
      if(nCurrent == sEnd)
        break;
      Spot Sc = (Spot) spots.get(nCurrent);
      for(String ni_ : (Iterable<String> )Sc.links.keySet()) {
        // for(String ni_ : (Iterable<String>) spots.keySet()) {
        // if(!ni_.equals(sEnd) && !ni_.equals(sStart)) {
        // println("ni_: " + ni_);
        Spot adjacent = (Spot) spots.get(ni_);
        Link a = (Link) Sc.links.get(ni_);
        if(adjacent.walkable && !arrayListContains(closed, ni_)) {
          if(!arrayListContains(opened, ni_)) {
            opened.add(ni_);
            adjacent.parent = Sc;
            adjacent.setG(a);
            // println("g ds open: " + adjacent.g);
          } else if(adjacent.g > Sc.g + a.p) {
            adjacent.parent = Sc;
            adjacent.setG(a);
            // println("g hors open: " + adjacent.g);
          }
        }
      }
      // }
    }
    // Path generation
    String pathSpot = sEnd;
    // println(pathSpot);
    double di = 0;
    if(closed.size() > 1) {
      while(!(pathSpot.equals(sStart))) {
        path.add(pathSpot);
        Spot Sp = (Spot) spots.get(pathSpot);

        di += Sp.g;
        // println(di);
        Sp = Sp.parent;

        pathSpot = Sp.n;
        // println(pathSpot);
      }
      path.add(sStart);
      Spot Sp = (Spot) spots.get(sStart);
      di += Sp.g;
      // println(di);
    }
  }
  // Utilities

  boolean arrayListContains(ArrayList c, String nA) {
    for(int i = 0; i < c.size(); i++) {
      String o = (String) c.get(i);
      if(o == nA)
        return true;
    }
    return false;
  }
}

// Taille pour l'insertion dans JavaScool
public static final int WIDTH = 1024, HEIGHT = 700;

