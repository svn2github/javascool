/**
 *
 * Fonctions générales de l'application
 *
 */
   
void mousePressed()                                                   // souris pressée
{
  
  trajet.clear();
  sourisActivee = true;

  
  if(mouseButton == RIGHT)
    noeuds.add(new Noeud(mouseX,mouseY));                             // ajoute nouveau noeud
  
  
  if(mouseButton == CENTER)
  {
    premierSelect = trouvePlusProche(mouseX,mouseY);
  }
  
  
  if(keyPressed)                                                      // controles par touches clavier
  {
    if(key == 'n')
      noeuds.add(new Noeud(mouseX,mouseY));                           // ajoute nouveau noeud
    
    if(key == 'l' && noeuds.size() > 0)
    {
      premierSelect = trouvePlusProche(mouseX,mouseY);                // ajoute nouveau lien entre noeuds
    }
    
    if(key == 'p' && noeuds.size() > 1)                               // cherche trajet entre noeuds depart et fin
    {
      trajetSelect = true;
      if(depart == null)
        depart = trouvePlusProche(mouseX,mouseY);
      else
      {
        fin = trouvePlusProche(mouseX,mouseY);
        if(fin != depart)
        {
          trouveTrajet(depart,fin);
        }
        depart = null;  
      }
    }
    
    // Construit une population de noeuds aléatoirement
    if(key == 'a')
    {
      int nombre = 5; 
      for(int i=0;i<nombre;i++) {
      
        float posx = random(width); 
        float posy = random(height);
        println(" x: " + posx + " // y: " + posy);
        noeuds.add(new Noeud(posx,posy)); 
        
      }
      
    }
    
    
    if(key == 'b')
    {
      
      traceLiens(noeuds);
      
    }
    
    
    // Effacer le noeud selectionné 
    if ( key == 'd' ) { 
      
      effaceNoeud(trouvePlusProche(mouseX,mouseY));
      
    }
    
    if ( key == 'z' ) { 
      
      effaceTout();
      
    }
    
  }
  
  
}

// A optimiser pour ne pas tracer deux fois le lien?
void traceLiens(ArrayList mesNoeuds) {
 
  int count = 0;
      for(int i = 0;i<mesNoeuds.size();i++)
      {
        premierSelect = (Noeud) mesNoeuds.get(i);
        if(premierSelect != null)
        {
          for(int j = 0;j<mesNoeuds.size();j++) 
          {
            secondSelect = (Noeud) mesNoeuds.get(j);
            if(secondSelect != premierSelect)
            {
              if (premierSelect.questionneLien(secondSelect) || secondSelect.questionneLien(premierSelect)) 
                println("deja: i " + premierSelect + "// j " + secondSelect);
              else {

                println("i " + premierSelect + "// j " + secondSelect);
                count++;
                //int valid = (int) (random(9)-5);
                //println("valid " + valid);
                //if (valid>0) {
                //secondSelect.faitLien(premierSelect);                           // créer lien
                premierSelect.faitLien(secondSelect);
                //}
              
              }
              println("lien num " + premierSelect.rendLien(secondSelect));
              secondSelect = null;
            }
            
          }
          premierSelect = null;
        }
        

      }
      
      fin = null;
      println(count); 
}


void effaceNoeud(Noeud selectN)
{
    
  Noeud t;

  for(int i = 0; i < noeuds.size(); i++){
    
    t = (Noeud) noeuds.get(i);
    
    if ( t == selectN ) {

      t.effaceLiens();

      noeuds.remove(i);
      
    } else if (t.questionneLien(selectN)) {
      
      t.retireLien(selectN);
      
    }
  }
  
  
}



void effaceTout() {
  
  noeuds.clear();
  Noeud t1;
  for(int i = 0;i<noeuds.size();i++) {
    t1 = (Noeud) noeuds.get(i);
    t1.effaceLiens();
  }

  
}
  
void mouseReleased()                                                   // appelé a chaque moment que la souris est relachée
{
  sourisActivee = false;
  if(premierSelect != null)
  {
    secondSelect = trouvePlusProche(mouseX,mouseY);
    if(secondSelect != premierSelect)
    {
      //secondSelect.faitLien(premierSelect);                           // créer lien
      premierSelect.faitLien(secondSelect);
    }
  }
  premierSelect = null;
  secondSelect = null;
  fin = null;
}

void keyReleased()                                                    // appelé a chaque moment qu'une touche est relachee
{
  if(key == 'p')
    {
      trajetSelect = false;
    }
}


// Cherche noeud plus proche d'une position de la souris
Noeud trouvePlusProche(float x,float y)                              
{
  float curBest = 9999;
  Noeud best = (Noeud) noeuds.get(0);
  Noeud t; //temporary noeud placeholder
  for(int i = 0;i<noeuds.size();i++)
  {
    t = (Noeud) noeuds.get(i);
    float d = dist(x,y,t.pos.x,t.pos.y);
    if(d < curBest)
    {
      curBest = d;
      best = t;
    }
  }
  return best;
}


// Cherche noeud intermédiaire entre actuelN et cibleN tel que la distance entre actuelN et cible soit minimal
Noeud explore(Noeud actuelN,Noeud cibleN)                            
{
  if(actuelN == cibleN)
    return cibleN;
  Noeud next = actuelN;
  float d = 999;
  for(int noeud=0;noeud<actuelN.liens.size();noeud++)
  {
    Noeud tN = (Noeud) actuelN.liens.get(noeud);
    if(trajet.indexOf(tN) == -1 && restricted.indexOf(tN) == -1) // si le noeud n'a pas été parcouru
    {
      float di = PVector.dist(tN.pos,actuelN.pos)+PVector.dist(cibleN.pos,tN.pos);
      if(di < d )
      {
        d = di;
        next = tN;
      }
    }
  }
  println("Distance parcourue par le trajet: " + d );
  trajet.add(actuelN);
  return next;

}


// Construit le trajet avec tous les noeuds
// appel à explore
void trouveTrajet(Noeud noeudDepart,Noeud noeudFin)
{
  trajetC = color(0,124,30);
  trajet.clear();
  restricted.clear();
  Noeud prochainNoeud = noeudDepart;
  int coups = 0;
  int essais = 0;
  while(prochainNoeud != noeudFin && essais < 100)
  {
    prochainNoeud = explore(prochainNoeud,noeudFin);
    coups++;
    if(coups > 500)
    {
      coups = 0;
      essais++;
      restricted.add(trajet.get(trajet.size()-1));
      prochainNoeud = (Noeud) trajet.get(0);
      trajet.clear();
      println(essais);
    }
  }
  if(essais < 100)
    trajet.add(noeudFin);
  else
    background(255,0,0);
}
