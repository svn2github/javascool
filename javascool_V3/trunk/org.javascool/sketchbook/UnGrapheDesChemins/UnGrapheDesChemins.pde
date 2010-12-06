/*////////////////////////////////////////////////////////////////////////////
 *
 * 09.2010 Cécile P-L for Fuscia, ccl.picard@gmail.com
 * GRAPHES Part 2
 * Interface pédagogique sur la manipulation des concepts liés aux graphes
 *
 **/

PFont font;
boolean mouseDown = false, info = false, pathSelect = false;
String[] listN = { "Ahmed", "Barbara", "Charlotte", "Diego", "Elliot", "Ida", "Jelena", "Pontus", "Réda", "Samiha" }; // {"Nice", "Marseille", "Avignon", "Toulouse", "Bordeaux", "Dijon", "Fréjus"};//, "Strasbourg", "Caen", "Grenoble", "Lille", "Rennes"};
double[] listP = { 10, 15, 20, 10, 20 };
int indN = 0, indP = 0;
Graph myGraph;
String firstSelect, secondSelect, start = null, end = null;
ArrayList path, restricted;
ArrayList opened, closed;
color pathC = color(0, 124, 30);
HScrollbar hs1;
int topWidth;  // width of text

void setup() {
  // Ces deux lignes permettent l'interface avec JavaScool
  proglet = this;
  frame = new Frame();
  
  size(900, 500); // screen.width,screen.height);//1200,800);
  smooth();
  myGraph = new Graph();

  path = new ArrayList();
  restricted = new ArrayList();
  opened = new ArrayList();
  closed = new ArrayList();

  hs1 = new HScrollbar(0, 15, width, 15, 3 + 1);
}
void draw() {
  background(130);
  font = createFont("Arial Bold", 14, true);
  float topPos = hs1.getPos() - width / 2;

  textAlign(LEFT);
  fill(0, 40, 63);
  textFont(font, 13);
  text(" - I  N  S  T  R  U  C  T  I  O  N  S - \n " +
       "> Noeud: \n" + "    . ajout = clic droit \n" + "    . déplacement: 'm'\n" + "    . suppression: clic gauche + 'd' \n " +
       "> Lien: \n" + "    . ajout/suppression: clic centre + glisse \n " +
       "> Générer: \n" + "    . tous les noeuds = 'a': \n" + "    . tous les liens = 'l': \n" + "    . quelques liens = 'r' \n " +
       "> Afficher la pondération des liens: 'i' \n " +
       "> Trouver le plus court chemin entre 2 noeuds: \n" + "    . clic gauche + 'b' pour le noeud de départ \n" + "    . clic gauche + 'e' pour le noeud d'arrivée\n " +
       "> Fermer l'application: ESC ", topPos * 2, 40); 
  hs1.update();
  hs1.display();
  if(!pathSelect)
    start = null;
  // Selection noeud pour recherche lien
  if(firstSelect != null) {
    Node N_ = (Node) myGraph.nodes.get(firstSelect);
    noStroke();
    fill(255);
    ellipse(N_.x, N_.y, 30, 30);
    stroke(0);
  }
  // Trace plus court trajet en vert, en fonction des noeuds de depart et fin selectionnés
  // Les noeuds de début et fin sont surlignés en vert
  if(start != null) {
    Node N_ = (Node) myGraph.nodes.get(start);
    noStroke();
    fill(0, 124, 30);
    ellipse(N_.x, N_.y, 30, 30);
    stroke(0);
  }
  // println(path.size());
  for(int i = 0; i < path.size(); i++) {
    String p = (String) path.get(i);
    Node pN = (Node) myGraph.nodes.get(p);
    fill(pathC);
    ellipse(pN.x, pN.y, 30, 30);
    for(String ni_ : (Iterable<String> )myGraph.nodes.keySet()) {
      Node t2 = (Node) myGraph.nodes.get(ni_);
      if(myGraph.isLink(p, ni_))
        if(path.indexOf(ni_) > -1) {
          strokeWeight(15);
          stroke(pathC);
          line(pN.x, pN.y, t2.x, t2.y);
          strokeWeight(1);
          noStroke();
        }
    }
  }
  // Pour chaque noeud, les liens sont détectés pour les tracer en noir
  for(String ni_ : (Iterable<String> )myGraph.nodes.keySet()) {
    Node N_ = (Node) myGraph.nodes.get(ni_);
    fill(255, 150, 0);
    stroke(0);
    strokeWeight(1);
    ellipse(N_.x, N_.y, 20, 20);
    noStroke();
    textFont(font, width * 14 / 800);
    text(N_.n, N_.x + 12, N_.y);
    for(String nj_ : (Iterable<String> )myGraph.nodes.keySet())
      if(myGraph.isLink(ni_, nj_) && !(ni_.equals(nj_))) {
        Node N2_ = (Node) myGraph.nodes.get(nj_);
        double p_ = myGraph.getLink(ni_, nj_);

        fill(0);
        strokeWeight(3);
        stroke(0);
        line(N_.x, N_.y, N2_.x, N2_.y);
        noStroke();
        if(info) {
          textFont(font, width * 10 / 800);
          fill(180);
          text(" " + (float) p_, abs((N_.x + N2_.x) / 2), abs((N_.y + N2_.y) / 2) + 10);
        }
        strokeWeight(1);
      }
  }
}
void mousePressed() {
  path.clear();
  mouseDown = true;
  if(mouseButton == RIGHT) {
    if(indN < listN.length) {
      println("ind " + indN);
      myGraph.addNode(listN[indN], mouseX, mouseY);        // ajoute nouveau noeud
      indN++;
    } else {
      indN = 0;
      myGraph.addNode(listN[indN], mouseX, mouseY);        // ajoute nouveau noeud
      indN++;
    }
  }
  if(mouseButton == CENTER)
    if(myGraph.nodes.size() != 0)
      firstSelect = myGraph.getClosestNode(mouseX, mouseY);
  if(keyPressed) {                                                    // controles par touches clavier
    // Cherche trajet entre noeuds depart et fin
    // A VERIFIER
    if((key == 'b') && (myGraph.nodes.size() > 1)) {
      pathSelect = true;
      start = myGraph.getClosestNode(mouseX, mouseY);
    }
    if((key == 'e') && (start != null)) {
      pathSelect = true;

      end = myGraph.getClosestNode(mouseX, mouseY);
      println("start: " + start + " // end: " + end);
      if(end != start)
        myGraph.dijkstra(start, end);
      start = null;

      end = null;
    }
   
    // Efface un noeud (le plus proche du curseur de la souris)
    if(key == 'd') {
      path.clear();
      if(myGraph.nodes.size() != 0) {
        String n_ = myGraph.getClosestNode(mouseX, mouseY);
        myGraph.removeNode(n_);
      }
    }
  }
}
void mouseReleased() {                                                  // appelé a chaque moment que la souris est relachée
  mouseDown = false;
  // Trace lien ou retire lien entre 2 noeuds
  if(firstSelect != null) {
    secondSelect = myGraph.getClosestNode(mouseX, mouseY);
    if(secondSelect != firstSelect) {
      if(myGraph.isLink(firstSelect, secondSelect))
        myGraph.removeLink(firstSelect, secondSelect);
      else
        myGraph.addLink(firstSelect, secondSelect);
    }
  }
  firstSelect = null;
  secondSelect = null;
}
void keyPressed() {

  // Efface tous les liens tracés
  if(key == 's') {
    path.clear();
    for(String ni_ : (Iterable<String> )myGraph.nodes.keySet())
      for(String nj_ : (Iterable<String> )myGraph.nodes.keySet())
        if(myGraph.isLink(ni_, nj_) && !(ni_.equals(nj_)))
          myGraph.removeLink(ni_, nj_);
  }
  
  // Déplace un noeud existant
  if(key == 'm') {
    path.clear();
    String n_ = myGraph.getClosestNode(mouseX, mouseY);
    myGraph.addNode(n_, mouseX, mouseY);
    for(String ni_ : (Iterable<String> )myGraph.nodes.keySet())
      if(myGraph.isLink(n_, ni_) && !(n_.equals(ni_))) {
        myGraph.removeLink(n_, ni_);
        myGraph.addLink(n_, ni_);
      }
  }
  
  // Génère les noeuds de manière aléatoire
  if(key == 'a') {
    path.clear();
    for(int i = 0; i < listN.length; i++) {  // string array -> length
      int x_ = (int) random(width - 100) + 50;
      int y_ = (int) random(height - 100) + 50;

      myGraph.addNode(listN[i], x_, y_);
    }
  }
  /*if(key == 'b') {
    String[] myList;
    myList = myGraph.getAllNodes();
    println(myList[0]);
  }*/
  
  // Génère tous les liens possibles entre les noeuds
  if(key == 'l')
    for(String ni_ : (Iterable<String> )myGraph.nodes.keySet())
      for(String nj_ : (Iterable<String> )myGraph.nodes.keySet())
        if(myGraph.isLink(ni_, nj_) == false)
          myGraph.addLink(ni_, nj_);
  
  // Génère des liens possibles entre les noeuds de manière aléatoire
  if(key == 'r')
    if(myGraph.nodes.size() == listN.length) {
      for(String ni_ : (Iterable<String> )myGraph.nodes.keySet())
        for(String nj_ : (Iterable<String> )myGraph.nodes.keySet())
          if(myGraph.isLink(ni_, nj_) && !(ni_.equals(nj_)))
            myGraph.removeLink(ni_, nj_);
      for(String ni_ : (Iterable<String> )myGraph.nodes.keySet()) {
        int done = 0;
        String nk_ = null;
        while(done < (listN.length / 4)) {
          int k = (int) random(listN.length);
          nk_ = listN[k];
          if(!(myGraph.isLink(ni_, nk_))) {
            done += 1;
            myGraph.addLink(ni_, nk_);
          }
          // println(nk_);
        }
      }
      
    }
  
  // Montrer ou non les pondérations
  if(key == 'i') {
    if(info)

      info = false;

    else

      info = true;
  }
}

void keyReleased() {                                                   // appelé a chaque moment qu'une touche est relachee
  if(key == 'p')
    pathSelect = false;
}




/* Fonctions pour javascool. */

/** Ajoute ou modifie un noeud au graphe (modifie dans le cas ou meme nom employé et différentes coordonnées).
 * @param n Nom du noeud.
 * @param x Abcisse du noeud.
 * @param y Ordonnée du noeud.
 */
public static void addNode(String n, int x, int y) {
  proglet.myGraph.addNode(n, x, y); 
}

/** Renvoie l'objet Noeud à partir de son nom.
 * @param n Nom du noeud.
 * @return objet Node.
 */
public static Node getNode(String n) {
  Node N_;
  N_ = proglet.myGraph.getNode(n);
  return N_;
}

/** Cherche noeud plus proche d'une position.
 * @param x Abcisse position.
 * @param y Ordonnée position.
 * @return Nom du noeud.
 */
public static String getClosestNode(float x, float y) {
  String n_ = null;
  n_ = proglet.myGraph.getClosestNode(x, y);
  return n_;
}

/** Détruit un noeud au graphe si il existe.
 * @param n Nom du noeud.
 */
public static void removeNode(String n) {
  proglet.myGraph.removeNode(n);
}

/** Donne la liste de tous les noeuds.
   * @return La liste des noms des noeuds.
   */
public static String[] getAllNodes() {
  String[] ListN_ = new String[50];
  ListN_ = proglet.myGraph.getAllNodes();
  return ListN_;
}

/** Donne la liste des noeuds en lien avec un noeud donné.
 * @param n Nom du noeud dont on veut les noeuds en lien.
 * @return La liste des noms des noeuds en lien avec le noeud donné.
 */
public static String[] getNodes(String n) {
  String[] ListN_ = new String[50];
  ListN_ = proglet.myGraph.getNodes(n);
  return ListN_;
}

/** Ajoute ou modifie un lien entre deux noeuds (modifie dans le cas ou memes noeuds et différent poids attribué).
 * @param nA Premier noeud du lien.
 * @param nB Deuxième noeud du lien.
 * @param p Poids du lien. 
 */
public static void addLink(String nA, String nB, double p) {
  proglet.myGraph.addLink(nA, nB, p);
}

/** Ajoute ou modifie un lien entre deux noeuds (modifie dans le cas ou memes noeuds et différent poids attribué).
 * @param nA Premier noeud du lien.
 * @param nB Deuxième noeud du lien.
 * ici poids du lien = distance euclidienne entre les deux noeuds.
 */
public static void addLink(String nA, String nB) {
  proglet.myGraph.addLink(nA, nB);    
}

/** Détruit un lien entre deux noeuds si il existe.
 * @param nA Premier noeud du lien.
 * @param nB Deuxième noeud du lien.
 */
public static void removeLink(String nA, String nB) {
  proglet.myGraph.removeLink(nA, nB);
}

/** Affirme si il y a lien entre 2 noeuds.
 * @param nA Premier noeud du lien.
 * @param nB Deuxième noeud du lien.
 * @return "vrai" ou "faux".
 */
public static boolean isLink(String nA, String nB) {
  boolean a_ = false;
  a_ = proglet.myGraph.isLink(nA, nB);
  return a_;
}

/** Donne le poids d'un lien entre deux noeuds.
   * @param nA Premier noeud du lien.
   * @param nB Deuxième noeud du lien.
   * @return Le poids du lien où 0 si il n'y a pas de lien.
   */
public static double getLink(String nA, String nB) {
  double p_ = 0;
  p_ = proglet.myGraph.getLink(nA, nB);
  return p_;
}

/**   Algorithme de Dijkstra
 * @param nStart Noeud départ.
 * @param nEnd Noeud final.
 */
public static void dijkstra(String nStart, String nEnd) {
  proglet.myGraph.dijkstra(nStart, nEnd);
}

static UnGrapheDesChemins proglet;
