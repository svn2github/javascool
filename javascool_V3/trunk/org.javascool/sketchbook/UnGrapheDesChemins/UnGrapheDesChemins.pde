  /** 
  * 09.2010 Cécile P-L for Fuscia, ccl.picard@gmail.com
  * GRAPHES
  * Interface pédagogique sur la manipulation des concepts liés aux graphes
  *
  */
 PFont Verdana;
 boolean mouseDown = false, info = false, pathSelect = false;
 String[] listN = {"Nice", "Marseille", "Avignon", "Toulouse", "Bordeaux", "Dijon", "Fréjus"};//, "Strasbourg", "Caen", "Grenoble", "Lille", "Rennes"};
 double[] listP = {10, 15, 20, 10, 20};
 int indN = 0, indP = 0;
 Graph myGraph;
 String firstSelect, secondSelect, start=null, end=null;
 ArrayList path, restricted;
 ArrayList opened, closed;
 color pathC = color(0,124,30); 
 HScrollbar hs1;
 int topWidth; // width of text
   
 void setup()
 {
   
  size(1024,700); //screen.width,screen.height);//1200,800); 
  smooth();
  myGraph = new Graph();
  
  path = new ArrayList();
  restricted = new ArrayList();
  opened = new ArrayList();
  closed = new ArrayList();
  
  hs1 = new HScrollbar(0, 15, width, 15, 3+1);
  
 }
  
  
 void draw()
 {
 
   background(130);
   Verdana = loadFont("Verdana-48.vlw");
   float topPos = hs1.getPos()-width/2;
   
   textAlign(LEFT);
   fill(0, 40, 63);
   textFont(Verdana, 13);
   text(" - INSTRUCTIONS - \n " + 
    ". Ajouter un noeud: clic gauche // Tracer des liens ou effacer des liens existants: clic centre // Effacer un noeud:  'd' // Bouger un noeud via la souris: 'm' \n " +
    ". Génerer un placement de noeuds aléatoire: 'a' // Générer tous les liens possibles entre les noeuds: 'l' \n " +
    ". Faire apparaitre la pondération des liens: 'i' \n " +
    ". Trouver le plus court chemin entre 2 noeuds: clic + 'p' \n " +
    ". Arreter l'application: ESC " , topPos*2, 40);//10 ,20);
   hs1.update();
   hs1.display();
  
   if(!pathSelect)
    start = null;
    
   // Selection noeud pour recherche lien
   if(firstSelect != null)
   {
      Node N_ = (Node) myGraph.nodes.get(firstSelect);
      noStroke();
      fill(255);
      ellipse(N_.x,N_.y,30,30);
      stroke(0);
   }
      
   // Trace plus court trajet en vert, en fonction des noeuds de depart et fin selectionnés
   // Les noeuds de début et fin sont surlignés en vert
   if(start != null)
   {
     Node N_ = (Node) myGraph.nodes.get(start);
     noStroke();
     fill(0,124,30);
     ellipse(N_.x,N_.y,30,30);
     stroke(0);
   }
   //println(path.size());
   for(int i=0;i<path.size();i++)
   {
     String p = (String) path.get(i);
     Node pN = (Node) myGraph.nodes.get(p);
     fill(pathC);
     ellipse(pN.x,pN.y,30,30);
     for(String ni_ : (Iterable<String>) myGraph.nodes.keySet())
     {
       Node t2 = (Node) myGraph.nodes.get(ni_);
       if(myGraph.isLink(p,ni_)) {
       if(path.indexOf(ni_)>-1)
       {
         strokeWeight(15);
         stroke(pathC);
         line(pN.x,pN.y,t2.x,t2.y);
         strokeWeight(1);
         noStroke();
       }
       }
     }
   }
   // Pour chaque noeud, les liens sont détectés pour les tracer en noir   
   for(String ni_ : (Iterable<String>) myGraph.nodes.keySet()) {   
     
      Node N_ = (Node) myGraph.nodes.get(ni_);
      fill(255, 150, 0);
      stroke(0);
      strokeWeight(1);
      ellipse(N_.x,N_.y,20,20);
      noStroke();
      textFont(Verdana, width*14/800);
      text(N_.n,N_.x+12,N_.y);

      for(String nj_ : (Iterable<String>) myGraph.nodes.keySet())
      {
        
        if (myGraph.isLink(ni_,nj_) && !(ni_.equals(nj_))) {
          Node N2_ = (Node) myGraph.nodes.get(nj_);
          double p_ = myGraph.getLink(ni_,nj_);
          
          fill(0);
          strokeWeight(3);
          stroke(0);
          line(N_.x, N_.y, N2_.x, N2_.y);
          noStroke();
          
          if (info) {
            textFont(Verdana, width*10/800);
            fill(180);
            text(" " + (float) p_, abs((N_.x+N2_.x)/2), abs((N_.y+N2_.y)/2)+10);
          }
          strokeWeight(1);
        }
        
      }
      
   }
   
    
 }
 
 
 void mousePressed()                                                   
 {
  path.clear();
  mouseDown = true;
  
  if(mouseButton == RIGHT) {
    
    if (indN<listN.length) {
      
      println("ind " + indN);
      myGraph.addNode(listN[indN], mouseX, mouseY);        // ajoute nouveau noeud
      indN++;
      
    } else {
      
      indN = 0;
      myGraph.addNode(listN[indN], mouseX, mouseY);        // ajoute nouveau noeud
      indN++;
      
    }
    
  }
    
  
  if(mouseButton == CENTER) {
    
    if (myGraph.nodes.size() != 0) {
      firstSelect = myGraph.getClosestNode(mouseX,mouseY);
     }
    
  }
  
  
  if(keyPressed)                                                      // controles par touches clavier
  {
   
    // Cherche trajet entre noeuds depart et fin
    // MARCHE PAS TRÈS BIEN!!!!
    if(key == 'p' && myGraph.nodes.size() > 1)                        
      {
        pathSelect = true;
        double p1 = 0.0, p2 = 0.0;
        if(start == null) {
          //path.clear();
          start = myGraph.getClosestNode(mouseX,mouseY);
        } else {
          end = myGraph.getClosestNode(mouseX,mouseY);
          println("start: " + start + " // end: " + end);
          if(end != start)
          {
            //myGraph.findPath(start,end);
            myGraph.dijkstra(start, end);
            /*for(int i=0; i<path.size(); i++) {
              String n = (String) path.get(i);
              println("___" + n);
            }*/
          }
          start = null;  
        }
        end = null;
      }
      
   
    
  }
  
 
  
 }
 
 
 void mouseReleased()                                                   // appelé a chaque moment que la souris est relachée
 {
   
  mouseDown = false;
  
    // Trace lien ou retire lien entre 2 noeuds
    if(firstSelect != null) {
      
      secondSelect = myGraph.getClosestNode(mouseX,mouseY);
      if(secondSelect != firstSelect) {
       
        if(myGraph.isLink(firstSelect, secondSelect)) {
          myGraph.removeLink(firstSelect, secondSelect);
        } else {
          myGraph.addLink(firstSelect, secondSelect);
        }
      }
      
    }
    firstSelect = null;
    secondSelect = null;


 }

  
void keyPressed()                                                  
 {
 
  // Efface un noeud (le plus proche du curseur de la souris)
  if( key == 'd' ) { 
    
    path.clear();
    if(myGraph.nodes.size() != 0) {
      String n_ = myGraph.getClosestNode(mouseX,mouseY);
      myGraph.removeNode(n_);
    }
    
  }
  
  // Efface tous les liens tracés
  if( key == 's' ) { 
    
    path.clear();
    for(String ni_ : (Iterable<String>) myGraph.nodes.keySet())
    {
      for(String nj_ : (Iterable<String>) myGraph.nodes.keySet())
      {
        if(myGraph.isLink(ni_,nj_) && !(ni_.equals(nj_))) {
          myGraph.removeLink(ni_, nj_);
        }
      }
    }
    
  }
  
 
  // Déplace un noeud existant
  if( key == 'm' ) {
    
    path.clear();
    String n_ = myGraph.getClosestNode(mouseX,mouseY);
    myGraph.addNode(n_, mouseX, mouseY);
    for(String ni_ : (Iterable<String>) myGraph.nodes.keySet())
    {
        
        if(myGraph.isLink(n_,ni_) && !(n_.equals(ni_))) {
          myGraph.removeLink(n_, ni_);
          myGraph.addLink(n_, ni_);
        }
          
    }
  }
  
  // Génère les noeuds de manière aléatoire
  if( key == 'a' ) { 
    
    path.clear();
    
    for(int i=0; i<listN.length; i++) {  //string array -> length
      
      int x_ = (int) random(width-100)+50;
      int y_ = (int) random(height-100)+50;
      
      myGraph.addNode(listN[i], x_, y_);
      
    }
    
  }
  
  // Génère tous les liens possibles entre les noeuds
  if( key == 'l' ) {
   
    for(String ni_ : (Iterable<String>) myGraph.nodes.keySet())
    {
      for(String nj_ : (Iterable<String>) myGraph.nodes.keySet())
      {
        if (myGraph.isLink(ni_,nj_) == false) {
          myGraph.addLink(ni_,nj_);
        }
        
      }
    }
  } 
  
  // Génère des liens possibles entre les noeuds de manière aléatoire
  if( key == 'r' ) {
   
    for(String ni_ : (Iterable<String>) myGraph.nodes.keySet())
    {
      for(String nj_ : (Iterable<String>) myGraph.nodes.keySet())
      {
        if(myGraph.isLink(ni_,nj_) && !(ni_.equals(nj_))) {
          myGraph.removeLink(ni_, nj_);
        }
      }
      
      boolean done = false;
      String nk_ = null;
      while(!done) {
        int k = (int) random(listN.length);
        nk_ = listN[k];
        if(!(myGraph.isLink(ni_,nk_))) done = true;
        //println(nk_);
      }
      for(String nj_ : (Iterable<String>) myGraph.nodes.keySet())
      {
        if(!(nj_.equals(nk_)) && (myGraph.isLink(ni_,nj_) == false)) {
          myGraph.addLink(ni_,nj_);
          
        }

      }
      
    }
  }
  
  /*if(key == 'p' && myGraph.nodes.size() > 1)                               // cherche trajet entre noeuds depart et fin
    {
      pathSelect = true;
      
      if(start == null)
        start = myGraph.getClosestNode(mouseX,mouseY);
      else
      {
        end = myGraph.getClosestNode(mouseX,mouseY);
        if(end != start)
        {
          println("trajet? " + pathSelect);
          myGraph.findPath(start,end);
        }
        start = null;  
      }
    }*/
    
    
  // Montrer ou non les pondérations
  if( key == 'i' ) { 
    
    if (info) {
      
      info = false;
      
    } else {
      
      info = true;
      
    }
    
  }
  
  
 }
 
 
 void keyReleased()                                                    // appelé a chaque moment qu'une touche est relachee
 {
  if(key == 'p')
    {
      pathSelect = false;
    }
 }
 
 
 
 
 
  
