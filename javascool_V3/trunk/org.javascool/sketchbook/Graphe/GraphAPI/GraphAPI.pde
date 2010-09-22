  /** 
  * 09.2010 Cécile P-L for Fuscia, ccl.picard@gmail.com
  * GRAPHES
  * Interface pédagogique sur la manipulation des concepts liés aux graphes
  *
  */
 
 boolean mouseDown = false;
 String[] listN = {"Nice", "Marseille", "Avignon", "Toulouse", "Bordeaux"};
 int index = 0;
 Graph myGraph;
   
 void setup()
 {
   
  size(500,500); 
  
  myGraph = new Graph();
  
 }
  
  
 void draw()
 {
 
   background(130);
   
 }
 
 
 void mousePressed()                                                   // souris pressée
 {
  
  mouseDown = true;
  
  if(mouseButton == LEFT) {
    
    myGraph.addNode(listN[index], mouseX, mouseY);        // ajoute nouveau noeud
    index++;
  }
  
  
  /*if(mouseButton == CENTER)
  {
    premierSelect = trouvePlusProche(mouseX,mouseY);
  }*/
  
 }
