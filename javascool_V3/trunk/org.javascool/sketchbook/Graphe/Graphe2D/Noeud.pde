class Noeud
{
  PVector pos;
  ArrayList liens;
  boolean enLien = false;
  
  Noeud(float inputX,float inputY)
  {
    pos = new PVector(inputX,inputY);
    liens = new ArrayList();
  }
  
  
  void faitLien(Noeud autreN)
  {
    liens.add(autreN); // dans ce cas nb liens = (nb noeuds)*(nb noeuds -1) cad tient compte du sens
  }
  
  
  void retireLien(Noeud autreN)
  {
    liens.remove(autreN); 
  }
  
  
  void effaceLiens()
  {
    liens.clear();
  }
  
  
  boolean questionneLien(Noeud autreN)
  {
    for(int noeud=0;noeud<liens.size();noeud++)
    {
      if (autreN == ((Noeud) liens.get(noeud))) {
      
        enLien = true;
      
       } else {
      
        enLien = false;
      }
    }
    
    return enLien;
  }
  
  
  int rendLien(Noeud autreN) {
    
    int l = 0;
    for(int noeud=0;noeud<liens.size();noeud++)
    {
      if (autreN == ((Noeud) liens.get(noeud))) 
        l = noeud;
    }
   
    return l;
  }
  
  
}
