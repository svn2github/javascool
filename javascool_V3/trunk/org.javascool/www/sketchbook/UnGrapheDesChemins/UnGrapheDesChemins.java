import processing.core.*;
import processing.xml.*;

import java.applet.*;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.MouseEvent;
import java.awt.event.KeyEvent;
import java.awt.event.FocusEvent;
import java.awt.Image;
import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;
import java.util.zip.*;
import java.util.regex.*;

public class UnGrapheDesChemins extends PApplet {
/*////////////////////////////////////////////////////////////////////////////
 *
 * 09.2010 C\u00e9cile P-L for Fuscia, ccl.picard@gmail.com
 * GRAPHES Part 2
 * Interface p\u00e9dagogique sur la manipulation des concepts li\u00e9s aux graphes
 *
 **/

  PFont font;
  boolean mouseDown = false, info = false, pathSelect = false;
  String[] listN = { "Ahmed", "Barbara", "Charlotte", "Diego", "Elliot", "Ida", "Jelena", "Pontus", "R\u00e9da", "Samiha" }; // {"Nice", "Marseille", "Avignon", "Toulouse", "Bordeaux", "Dijon", "Fr\u00e9jus"};//, "Strasbourg", "Caen", "Grenoble", "Lille", "Rennes"};
  double[] listP = { 10, 15, 20, 10, 20 };
  int indN = 0, indP = 0;
  Graph myGraph;
  String firstSelect, secondSelect, start = null, end = null;
  ArrayList path, restricted;
  ArrayList opened, closed;
  int pathC = color(0, 124, 30);
  HScrollbar hs1;
  int topWidth; // width of text

  public void setup() {
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
  public void draw() {
    background(130);
    font = createFont("Arial Bold", 14, true);
    float topPos = hs1.getPos() - width / 2;

    textAlign(LEFT);
    fill(0, 40, 63);
    textFont(font, 13);
    text(" - I  N  S  T  R  U  C  T  I  O  N  S - \n " +
         "> Noeud: \n" + "    . ajout = clic droit \n" + "    . d\u00e9placement: 'm'\n" + "    . suppression: clic gauche + 'd' \n " +
         "> Lien: \n" + "    . ajout/suppression: clic centre + glisse \n " +
         "> G\u00e9n\u00e9rer: \n" + "    . tous les noeuds = 'a': \n" + "    . tous les liens = 'l': \n" + "    . quelques liens = 'r' \n " +
         "> Afficher la pond\u00e9ration des liens: 'i' \n " +
         "> Trouver le plus court chemin entre 2 noeuds: \n" + "    . clic gauche + 'b' pour le noeud de d\u00e9part \n" + "    . clic gauche + 'e' pour le noeud d'arriv\u00e9e\n " +
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
    // Trace plus court trajet en vert, en fonction des noeuds de depart et fin selectionn\u00e9s
    // Les noeuds de d\u00e9but et fin sont surlign\u00e9s en vert
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
      for(String ni_ : (Iterable<String>)myGraph.nodes.keySet()) {
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
    // Pour chaque noeud, les liens sont d\u00e9tect\u00e9s pour les tracer en noir
    for(String ni_ : (Iterable<String>)myGraph.nodes.keySet()) {
      Node N_ = (Node) myGraph.nodes.get(ni_);
      fill(255, 150, 0);
      stroke(0);
      strokeWeight(1);
      ellipse(N_.x, N_.y, 20, 20);
      noStroke();
      textFont(font, width * 14 / 800);
      text(N_.n, N_.x + 12, N_.y);
      for(String nj_ : (Iterable<String>)myGraph.nodes.keySet())
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
  public void mousePressed() {
    path.clear();
    mouseDown = true;
    if(mouseButton == RIGHT) {
      if(indN < listN.length) {
        println("ind " + indN);
        myGraph.addNode(listN[indN], mouseX, mouseY);      // ajoute nouveau noeud
        indN++;
      } else {
        indN = 0;
        myGraph.addNode(listN[indN], mouseX, mouseY);      // ajoute nouveau noeud
        indN++;
      }
    }
    if(mouseButton == CENTER)
      if(myGraph.nodes.size() != 0)
        firstSelect = myGraph.getClosestNode(mouseX, mouseY);
    if(keyPressed) {                                                  // controles par touches clavier
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
  public void mouseReleased() {                                                // appel\u00e9 a chaque moment que la souris est relach\u00e9e
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
  public void keyPressed() {
    // Efface tous les liens trac\u00e9s
    if(key == 's') {
      path.clear();
      for(String ni_ : (Iterable<String>)myGraph.nodes.keySet())
        for(String nj_ : (Iterable<String>)myGraph.nodes.keySet())
          if(myGraph.isLink(ni_, nj_) && !(ni_.equals(nj_)))
            myGraph.removeLink(ni_, nj_);
    }
    // D\u00e9place un noeud existant
    if(key == 'm') {
      path.clear();
      String n_ = myGraph.getClosestNode(mouseX, mouseY);
      myGraph.addNode(n_, mouseX, mouseY);
      for(String ni_ : (Iterable<String>)myGraph.nodes.keySet())
        if(myGraph.isLink(n_, ni_) && !(n_.equals(ni_))) {
          myGraph.removeLink(n_, ni_);
          myGraph.addLink(n_, ni_);
        }
    }
    // G\u00e9n\u00e8re les noeuds de mani\u00e8re al\u00e9atoire
    if(key == 'a') {
      path.clear();
      for(int i = 0; i < listN.length; i++) { // string array -> length
        int x_ = (int) random(width - 100) + 50;
        int y_ = (int) random(height - 100) + 50;

        myGraph.addNode(listN[i], x_, y_);
      }
    }
    /*if(key == 'b') {
     *  String[] myList;
     *  myList = myGraph.getAllNodes();
     *  println(myList[0]);
     *  }*/
    // G\u00e9n\u00e8re tous les liens possibles entre les noeuds
    if(key == 'l')
      for(String ni_ : (Iterable<String>)myGraph.nodes.keySet())
        for(String nj_ : (Iterable<String>)myGraph.nodes.keySet())
          if(myGraph.isLink(ni_, nj_) == false)
            myGraph.addLink(ni_, nj_);
    // G\u00e9n\u00e8re des liens possibles entre les noeuds de mani\u00e8re al\u00e9atoire
    if(key == 'r')
      if(myGraph.nodes.size() == listN.length) {
        for(String ni_ : (Iterable<String>)myGraph.nodes.keySet())
          for(String nj_ : (Iterable<String>)myGraph.nodes.keySet())
            if(myGraph.isLink(ni_, nj_) && !(ni_.equals(nj_)))
              myGraph.removeLink(ni_, nj_);
        for(String ni_ : (Iterable<String>)myGraph.nodes.keySet()) {
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
    // Montrer ou non les pond\u00e9rations
    if(key == 'i') {
      if(info)

        info = false;

      else

        info = true;
    }
  }
  public void keyReleased() {                                                 // appel\u00e9 a chaque moment qu'une touche est relachee
    if(key == 'p')
      pathSelect = false;
  }
  class Graph {
    // http://processing.org/reference/HashMap.html

    /*
     *  class Node {
     *  }
     *  HashMap nodes = new HashMap();
     *  nodes.put("toto", new Node(n, x, y));
     *  nodes.get("toto");
     */

    HashMap nodes;

    Graph() {
      nodes = new HashMap();
    }
    /** Ajoute ou modifie un noeud au graphe (modifie dans le cas ou meme nom employ\u00e9 et diff\u00e9rentes coordonn\u00e9es).
     * @param n Nom du noeud.
     * @param x Abcisse du noeud.
     * @param y Ordonn\u00e9e du noeud.
     */
    public void addNode(String n, int x, int y) {
      if(nodes.containsKey(n)) {
        // removeNode(n);
        // nodes.put(n, new Node(n, x, y));
        Node N_ = (Node) nodes.get(n);
        if(N_ != null)
          N_.moveTo(x, y);
      } else

        nodes.put(n, new Node(n, x, y));
    }
    /** Renvoie l'objet Noeud \u00e0 partir de son nom.
     * @param n Nom du noeud.
     * @return objet Node.
     */
    public Node getNode(String n) {
      Node N_ = (Node) nodes.get(n);

      return N_;
    }
    /** Cherche noeud plus proche d'une position.
     * @param x Abcisse position.
     * @param y Ordonn\u00e9e position.
     * @return Nom du noeud.
     */
    public String getClosestNode(float x, float y) {
      // println("x " + x + " // y " + y);

      float curBest = 9999;
      Node best = (Node) nodes.get(listN[0]);
      String n_ = null;
      if(nodes.size() != 0)
        for(String ni_ : (Iterable<String>)nodes.keySet()) {
          Node N_ = (Node) nodes.get(ni_);

          float d = dist(x, y, N_.x, N_.y);
          if(d < curBest) {
            curBest = d;
            best = N_;
            n_ = ni_;
          }
        }
      // println(" nom: " + best.n + " x " + best.x + " // y " + best.y);
      return n_;
    }
    /** D\u00e9truit un noeud au graphe si il existe.
     * @param n Nom du noeud.
     */
    public void removeNode(String n) {
      Node N_ = (Node) nodes.get(n);
      if(N_ != null) {
        // retire tous les liens en relation avec le noeud
        for(String ni_ : (Iterable<String>)nodes.keySet())
          removeLink(n, ni_);
        // retire le noeud en question
        nodes.remove(n);
      }
    }
    /** Donne la liste de tous les noeuds.
     * @return La liste des noms des noeuds.
     */
    public String[] getAllNodes() {
      String[] ListNodes = new String[50];
      int count = 0;
      for(String ni_ : (Iterable<String>)nodes.keySet()) {
        ListNodes[count] = ni_;
        count++;
      }
      return ListNodes;
    }
    /** Donne la liste des noeuds en lien avec un noeud donn\u00e9.
     * @param n Nom du noeud dont on veut les noeuds en lien.
     * @return La liste des noms des noeuds en lien avec le noeud donn\u00e9.
     */
    public String[] getNodes(String n) {
      String[] ListNodes = new String[10];
      Node N_ = (Node) nodes.get(n);
      if(N_ != null) {
        int count = 0;
        for(String ni_ : (Iterable<String>)nodes.keySet())
          if(isLink(n, ni_)) {
            ListNodes[count] = ni_;
            count++;
          }
      }
      return ListNodes;
    }
    /** Ajoute ou modifie un lien entre deux noeuds (modifie dans le cas ou memes noeuds et diff\u00e9rent poids attribu\u00e9).
     * @param nA Premier noeud du lien.
     * @param nB Deuxi\u00e8me noeud du lien.
     * @param p Poids du lien.
     */
    public void addLink(String nA, String nB, double p) {
      if(!(nA.equals(nB))) { // pas de lien de et vers un meme noeud
        Node NA_ = (Node) nodes.get(nA);
        Node NB_ = (Node) nodes.get(nB);
        if(NA_ != null&& NB_ != null) {
          NA_.links.put(nB, new Link(nA, nB, p));
          NB_.links.put(nA, new Link(nB, nA, p));
        }
      }
    }
    /** Ajoute ou modifie un lien entre deux noeuds (modifie dans le cas ou memes noeuds et diff\u00e9rent poids attribu\u00e9).
     * @param nA Premier noeud du lien.
     * @param nB Deuxi\u00e8me noeud du lien.
     * ici poids du lien = distance euclidienne entre les deux noeuds
     */
    public void addLink(String nA, String nB) {
      if(!(nA.equals(nB))) { // pas de lien de et vers un meme noeud
        Node NA_ = (Node) nodes.get(nA);
        Node NB_ = (Node) nodes.get(nB);
        if(NA_ != null&& NB_ != null) {
          // ici le poids = distance euclidienne entre les deux noeuds
          double p_ = (PVector.dist(NA_.position, NB_.position)) / 100;
          NA_.links.put(nB, new Link(nA, nB, p_));
          NB_.links.put(nA, new Link(nB, nA, p_));
        }
      }
    }
    /** D\u00e9truit un lien entre deux noeuds si il existe.
     * @param nA Premier noeud du lien.
     * @param nB Deuxi\u00e8me noeud du lien.
     */
    public void removeLink(String nA, String nB) {
      Node NA_ = (Node) nodes.get(nA);
      Node NB_ = (Node) nodes.get(nB);
      if(NA_ != null&& NB_ != null)
        if(isLink(nA, nB)) {
          NA_.links.remove(nB);
          NB_.links.remove(nA);
        }
    }
    /** Affirme si il y a lien entre 2 noeuds.
     * @param nA Premier noeud du lien.
     * @param nB Deuxi\u00e8me noeud du lien.
     * @return oui ou non.
     */
    public boolean isLink(String nA, String nB) {
      Node NA_ = (Node) nodes.get(nA);
      Node NB_ = (Node) nodes.get(nB);
      if(NA_ != null&& NB_ != null) {
        boolean link_ = false;
        for(String ni_ : (Iterable<String>)NA_.links.keySet())
          if(ni_.equals(nB)) // test si les deux string sont \u00e9quivalents
            link_ = true;
        // et inverse aussi!
        for(String ni_ : (Iterable<String>)NB_.links.keySet())
          if(ni_.equals(nA)) // test si les deux string sont \u00e9quivalents
            link_ = true;
        return link_;
      } else
        return false;
    }
    /** Donne le poids d'un lien entre deux noeuds.
     * @param nA Premier noeud du lien.
     * @param nB Deuxi\u00e8me noeud du lien.
     * @return Le poids du lien o\u00f9 0 si il n'y a pas de lien.
     */
    public double getLink(String nA, String nB) {
      double p_ = 0;
      if(isLink(nA, nB)) {
        Node NA_ = (Node) nodes.get(nA);
        Link li_ = (Link) NA_.links.get(nB);
        p_ = li_.p;
      }
      return p_;
    }
    /**  Cherche noeud interm\u00e9diaire entre nInit et nTarget tel que la distance entre nInit et nTarget soit minimal.
     * @param nInit Noeud initial.
     * @param nTarget Noeud cible.
     * @return Noeud interm\u00e9diaire.
     */

    /*String exploreNode(String nInit, String nTarget) {
     *  if(nInit == nTarget)
     *   return nTarget;
     *  String next = nInit; // initialization
     *  Node Ninit = (Node) nodes.get(nInit);
     *  Node Ntarget = (Node) nodes.get(nTarget);
     *  double p = 999999;
     *  for(String ni_ : (Iterable<String> )Ninit.links.keySet())
     *   if(!(ni_.equals(nInit))) {
     *     Node Ni_ = (Node) nodes.get(ni_);
     *     if((path.indexOf(ni_) == -1) && (restricted.indexOf(ni_) == -1)) { // si le noeud n'a pas \u00e9t\u00e9 parcouru
     *       // double di = PVector.dist(Ni_.position, Ninit.position)+PVector.dist(Ntarget.position,Ni_.position);
     *       double pi = getLink(nInit, ni_) + getLink(ni_, nTarget);
     *       if(pi < p) {
     *         p = pi;
     *         next = ni_;
     *       }
     *     }
     *   }
     *  // println("Distance parcourue par le trajet: " + p );
     *  path.add(nInit);
     *  return next;
     *  }*/

    /**   Construit le trajet avec tous les noeuds - appel \u00e0 exploreNode
     * @param nStart Noeud d\u00e9part.
     * @param nEnd Noeud final.
     */

    /*void findPath(String nStart, String nEnd) {
     *  path.clear();
     *  restricted.clear();
     *  println(" " + nStart + " \u00e0 " + nEnd);
     *  String next = nStart;
     *  int its = 0;
     *  int tests = 0;
     *  while(next != nEnd && tests < 100) {
     *   next = exploreNode(next, nEnd);
     *   // next = exploreNode(nStart,next,nEnd);
     *   its++;
     *   if(its > 500) {
     *     its = 0;
     *     tests++;
     *     restricted.add(path.get(path.size() - 1));
     *     next = (String) path.get(0);
     *     path.clear();
     *     // println(tests);
     *   }
     *  }
     *  if(tests < 100)
     *   path.add(nEnd);
     *  else
     *   background(255, 0, 0);
     *  }*/

    /**   Algorithme de Dijkstra
     * @param nStart Noeud d\u00e9part.
     * @param nEnd Noeud final.
     */
    public void dijkstra(String nStart, String nEnd) {
      if((nodes.get(nStart) == null) || (nodes.get(nEnd) == null))
        return;
      path.clear();
      for(String ni_ : (Iterable<String>)nodes.keySet()) {
        Node N_ = (Node) nodes.get(ni_);
        N_.init();
      }
      opened.clear();
      closed.clear();
      opened.add(nStart);
      Node Ns = (Node) nodes.get(nStart);
      Ns.g = 0;
      while(opened.size() > 0) {
        String nCurrent = (String) opened.remove(0);
        // println("nCurrent: " + nCurrent);
        closed.add(nCurrent);
        if(nCurrent == nEnd)
          break;
        Node Nc = (Node) nodes.get(nCurrent);
        for(String ni_ : (Iterable<String>)Nc.links.keySet()) {
          // println("ni_: " + ni_);
          Node adjacent = (Node) nodes.get(ni_);
          Link a = (Link) Nc.links.get(ni_);
          if(adjacent.walkable && !arrayListContains(closed, ni_)) {
            if(!arrayListContains(opened, ni_)) {
              opened.add(ni_);
              adjacent.parent = Nc;
              adjacent.setG(a);
              // println("g ds open: " + adjacent.g);
            } else if(adjacent.g > Nc.g + a.p) {
              adjacent.parent = Nc;
              adjacent.setG(a);
              // println("g hors open: " + adjacent.g);
            }
          }
        }
      }
      // Path generation
      String pathNode = nEnd;
      // println(pathNode);
      double di = 0;
      while(!(pathNode.equals(nStart))) {
        path.add(pathNode);
        Node Np = (Node) nodes.get(pathNode);
        di += Np.g;
        println(di);
        Np = Np.parent;

        pathNode = Np.n;
      }
      path.add(nStart);
      Node Np = (Node) nodes.get(nStart);
      di += Np.g;
      println(di);
    }
    // Utilities

    public boolean arrayListContains(ArrayList c, String nA) {
      for(int i = 0; i < c.size(); i++) {
        String o = (String) c.get(i);
        if(o == nA)
          return true;
      }
      return false;
    }
  }

// Taille pour l'insertion dans JavaScool
  public static final int WIDTH = 900, HEIGHT = 500;

  class HScrollbar {
    int swidth, sheight;  // width and height of bar
    int xpos, ypos;       // x and y position of bar
    float spos, newspos;  // x position of slider
    int sposMin, sposMax; // max and min values of slider
    int loose;            // how loose/heavy
    boolean over;         // is the mouse over the slider?
    boolean locked;
    boolean show = false;
    float ratio;

    HScrollbar(int xp, int yp, int sw, int sh, int l) {
      swidth = sw;
      sheight = sh;
      int widthtoheight = sw - sh;
      ratio = (float) sw / (float) widthtoheight;
      xpos = xp - 3 * swidth / 4;
      ypos = yp - sheight / 2;
      spos = xpos; // + swidth/2 - sheight/2;
      newspos = spos;
      sposMin = xpos;
      sposMax = xpos + swidth - sheight;
      loose = l;
    }
    public void update() {
      if(over())
        newspos = width / 2;  // constrain(mouseX-sheight/2, sposMin, sposMax);
      else
        newspos = 0;
      if(abs(newspos - spos) > 1)
        spos = spos + (newspos - spos) / loose;
    }
    public boolean over() {
      if((mouseX > xpos) && (mouseX < xpos + swidth) &&
         (mouseY > ypos) && (mouseY < ypos + sheight))
        return true;
      else
        return false;
    }
    public void display() {
      fill(255);
      strokeWeight(0.1f);
      if(over())
        fill(0, 40, 63);
      else
        fill(255, 150, 0);
      rect(sheight / 2, ypos, sheight * 5, sheight);
      fill(130);
      textFont(font, 11);
      text("I N F O >>>", sheight / 2 + sheight / 5, ypos + 4 * sheight / 5);
    }
    public float getPos() {
      // Convert spos to be values between
      // 0 and the total width of the scrollbar
      return spos * ratio;
    }
  }
  class Link {
    String n0;
    String n1;
    double p;

    Link(String n0_, String n1_, double p_) {
      n0 = n0_;
      n1 = n1_;
      p = p_;
    }
  }
  class Node {
    int x;
    int y;
    String n;
    Node parent = null; // Parent Node setting
    double g = 0; // Cost of reaching goal
    boolean walkable = true; // Is this Node to be ignored?

    HashMap links; // tous les liens qui sont accol\u00e9s au noeud

    PVector position;

    Node(String n_, int x_, int y_) {
      x = x_;
      y = y_;
      n = n_;

      position = new PVector(x, y);
      links = new HashMap();
    }
    public void moveTo(int x_, int y_) {
      x = x_;
      y = y_;

      position = new PVector(x, y);
    }
    public void init() {
      parent = null;
      g = 0;
    }
    // Calculate G
    public void setG(Link o) {
      g = parent.g + o.p;
    }
  }
/* Fonctions pour javascool. */

/** Ajoute ou modifie un noeud au graphe (modifie dans le cas ou meme nom employ\u00e9 et diff\u00e9rentes coordonn\u00e9es).
 * @param n Nom du noeud.
 * @param x Abcisse du noeud.
 * @param y Ordonn\u00e9e du noeud.
 */
  public static void addNode(String n, int x, int y) {
    if(proglet == null)
      return;
    proglet.myGraph.addNode(n, x, y);
  }
/** Renvoie l'objet Noeud \u00e0 partir de son nom.
 * @param n Nom du noeud.
 * @return objet Node.
 */
  public static Node getNode(String n) {
    if(proglet == null)
      return null;
    Node N_;
    N_ = proglet.myGraph.getNode(n);
    return N_;
  }
/** Cherche noeud plus proche d'une position.
 * @param x Abcisse position.
 * @param y Ordonn\u00e9e position.
 * @return Nom du noeud.
 */
  public static String getClosestNode(float x, float y) {
    if(proglet == null)
      return null;
    String n_ = null;
    n_ = proglet.myGraph.getClosestNode(x, y);
    return n_;
  }
/** D\u00e9truit un noeud au graphe si il existe.
 * @param n Nom du noeud.
 */
  public static void removeNode(String n) {
    if(proglet == null)
      return;
    proglet.myGraph.removeNode(n);
  }
/** Donne la liste de tous les noeuds.
 * @return La liste des noms des noeuds.
 */
  public static String[] getAllNodes() {
    if(proglet == null)
      return null;
    String[] ListN_ = new String[50];
    ListN_ = proglet.myGraph.getAllNodes();
    return ListN_;
  }
/** Donne la liste des noeuds en lien avec un noeud donn\u00e9.
 * @param n Nom du noeud dont on veut les noeuds en lien.
 * @return La liste des noms des noeuds en lien avec le noeud donn\u00e9.
 */
  public static String[] getNodes(String n) {
    if(proglet == null)
      return null;
    String[] ListN_ = new String[50];
    ListN_ = proglet.myGraph.getNodes(n);
    return ListN_;
  }
/** Ajoute ou modifie un lien entre deux noeuds (modifie dans le cas ou memes noeuds et diff\u00e9rent poids attribu\u00e9).
 * @param nA Premier noeud du lien.
 * @param nB Deuxi\u00e8me noeud du lien.
 * @param p Poids du lien.
 */
  public static void addLink(String nA, String nB, double p) {
    if(proglet == null)
      return;
    proglet.myGraph.addLink(nA, nB, p);
  }
/** Ajoute ou modifie un lien entre deux noeuds (modifie dans le cas ou memes noeuds et diff\u00e9rent poids attribu\u00e9).
 * @param nA Premier noeud du lien.
 * @param nB Deuxi\u00e8me noeud du lien.
 * ici poids du lien = distance euclidienne entre les deux noeuds.
 */
  public static void addLink(String nA, String nB) {
    if(proglet == null)
      return;
    proglet.myGraph.addLink(nA, nB);
  }
/** D\u00e9truit un lien entre deux noeuds si il existe.
 * @param nA Premier noeud du lien.
 * @param nB Deuxi\u00e8me noeud du lien.
 */
  public static void removeLink(String nA, String nB) {
    if(proglet == null)
      return;
    proglet.myGraph.removeLink(nA, nB);
  }
/** Affirme si il y a lien entre 2 noeuds.
 * @param nA Premier noeud du lien.
 * @param nB Deuxi\u00e8me noeud du lien.
 * @return "vrai" ou "faux".
 */
  public static boolean isLink(String nA, String nB) {
    if(proglet == null)
      return false;
    boolean a_ = false;
    a_ = proglet.myGraph.isLink(nA, nB);
    return a_;
  }
/** Donne le poids d'un lien entre deux noeuds.
 * @param nA Premier noeud du lien.
 * @param nB Deuxi\u00e8me noeud du lien.
 * @return Le poids du lien o\u00f9 0 si il n'y a pas de lien.
 */
  public static double getLink(String nA, String nB) {
    double p_ = 0;
    p_ = proglet.myGraph.getLink(nA, nB);
    return p_;
  }
/**   Algorithme de Dijkstra
 * @param nStart Noeud d\u00e9part.
 * @param nEnd Noeud final.
 */
  public static void dijkstra(String nStart, String nEnd) {
    if(proglet == null)
      return;
    proglet.myGraph.dijkstra(nStart, nEnd);
  }
  static UnGrapheDesChemins proglet;
  static public void main(String args[]) {
    PApplet.main(new String[] { "--bgcolor=#DFDFDF", "UnGrapheDesChemins" }
                 );
  }
}
