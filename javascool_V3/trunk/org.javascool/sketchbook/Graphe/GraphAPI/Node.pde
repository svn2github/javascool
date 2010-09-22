class Node { 
  
  int x;
  int y;
  String n;
  
  HashMap links; // tous les liens qui sont accolés au noeud
  
  PVector position;
  
  Node(String n_) {
    
    n = n_;
    
  }
  
  Node(String n_, int x_, int y_) {
    
    position = new PVector(x_, y_);
    n = n_;
    HashMap links = new HashMap();
    
  }
  
  
}
