ArrayList trajet, noeuds, restricted;
boolean sourisActivee = false, trajetSelect = false;
Noeud premierSelect,secondSelect,depart,fin;
color trajetC = color(0,124,30);

void setup()
{
  size(500,500);
  trajet = new ArrayList();
  noeuds = new ArrayList();
  restricted = new ArrayList();
}



void draw()
{
  background(130);
  
  //
  if(!trajetSelect)
    depart = null;
  
  // Selection noeud pour recherche lien
  if(premierSelect != null)
  {
    noStroke();
    fill(255);
    ellipse(premierSelect.pos.x,premierSelect.pos.y,30,30);
  }
 
  // Trace plus court trajet en vert, en fonction des noeuds de depart et fin selectionnés
  // Les noeuds de début et fin sont surlignés en vert
  if(depart != null)
  {
    noStroke();
    fill(0,124,30);
    ellipse(depart.pos.x,depart.pos.y,30,30);
  }
  for(int i=0;i<trajet.size();i++)
  {
    Noeud pN = (Noeud) trajet.get(i);
    fill(trajetC);
    ellipse(pN.pos.x,pN.pos.y,30,30);
    for(int j=0;j<pN.liens.size();j++)
    {
      Noeud t2 = (Noeud) pN.liens.get(j);
      if(trajet.indexOf(t2)>-1)
      {
        strokeWeight(15);
        stroke(trajetC);
        line(pN.pos.x,pN.pos.y,t2.pos.x,t2.pos.y);
        noStroke();
      }
    }
  }
  
  
  // Pour chaque noeud, les liens sont détectés pour les tracer en noir
  for(int i=0;i<noeuds.size();i++)
  {
    Noeud t = (Noeud) noeuds.get(i);
    //render all the noeuds in black.
    fill(0);
    ellipse(t.pos.x,t.pos.y,20,20);
    for(int j=0;j<t.liens.size();j++)
    {
      Noeud t2 = (Noeud) t.liens.get(j);
      strokeWeight(5);
      stroke(0);
      line(t.pos.x,t.pos.y,t2.pos.x,t2.pos.y);
      noStroke();
    }
    smooth();
  }
  
  
}


