class Node {
  int x;
  int y;
  String n;
  Node parent = null; // Parent Node setting
  double g = 0; // Cost of reaching goal
  boolean walkable = true; // Is this Node to be ignored?

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
  void init() {
    parent = null;
    g = 0;
  }
  // Calculate G
  void setG(Link o) {
    g = parent.g + o.p;
  }
}
