class Node { 
  
  int x;
  int y;
  String n;
  
  HashMap links; // tous les liens qui sont accolés au noeud
  
  PVector position;
  
  Node(String n_, int x_, int y_) {
    
    x = x_;
    y = y_;
    n = n_;

    position = new PVector(x, y);
    links = new HashMap();
    
  }
  
  
  void moveTo(int x_, int y_) {
    
    x = x_;
    y = y_;
    
    position = new PVector(x, y);
    
  }
  
}
