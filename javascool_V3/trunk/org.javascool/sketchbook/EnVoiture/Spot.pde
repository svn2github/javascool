class Spot extends Vec2D {
  Vec3D currNormal = Vec3D.Y_AXIS.copy();

  Vec3D pos;
  PVector position;
  String n;
  Spot parent = null; // Parent Node setting
  double g = 0; // Cost of reaching goal
  boolean walkable = true; // Is this Node to be ignored?
  int col;
  char f;
  float h;
  float d1, d2;
  float x2D, y2D;

  HashMap links; // tous les liens qui sont accolés au spot

  IsectData3D isec;

  public Spot(String n_, int col_, char f_, int x_, int y_, float d1_, float d2_, float h_) {
    x = x_;
    y = y_;
    d1 = d1_;
    d2 = d2_;
    h = h_;
    n = n_;
    col = col_;
    f = f_;

    pos = new Vec3D(x, h, y);
    position = new PVector(x, y);

    x2D = (((x_ + b.getMax().to2DXZ().x) / (2 * b.getMax().to2DXZ().x)) * 100);
    y2D = (((y_ + b.getMax().to2DXZ().x) / (2 * b.getMax().to2DXZ().x)) * 100);

    links = new HashMap();
  }

  void moveTo(int x_, int y_, float d1_, float d2_, float h_) {
    x = x_;
    y = y_;

    pos = new Vec3D(x, h, y);
    position = new PVector(x, y);

    x2D = (((x_ + b.getMax().to2DXZ().x) / (2 * b.getMax().to2DXZ().x)) * 100);
    y2D = (((y_ + b.getMax().to2DXZ().x) / (2 * b.getMax().to2DXZ().x)) * 100);
  }
  void init() {
    parent = null;
    g = 0;
  }
  // Calculate G
  void setG(Link o) {
    g = parent.g + o.p;
  }
  public void draw() {
    // create an axis aligned box and convert to mesh
    TriangleMesh building = new AABB(new Vec3D(), new Vec3D(d1, d2, h)).toMesh();
    if(f == form[0])
      building = new AABB(new Vec3D(), new Vec3D(d1, d2, h)).toMesh();
    else if(f == form[1])
      building = new Cone(new Vec3D(), new Vec3D(10, 10, 150), d1, d2, h).toMesh(10);
    else if(f == form[2])
      building = new Cone(new Vec3D(), new Vec3D(10, 10, 150), d1, d2, h).toMesh(30);
    else if(f == form[3])
      building = new Cone(new Vec3D(), new Vec3D(10, 10, 150), d1, d2, h).toMesh(100);
     /*if(n==listN[0] || n==listN[1]) {
      *  building = new AABB(new Vec3D(), new Vec3D(d1, d2, h)).toMesh();
      *  } else if(n==listN[2]) {
      *  building = new Cone(new Vec3D(), new Vec3D(10, 10, 150), d1, d2, h).toMesh(10);
      *  } else if(n==listN[3]) {
      *  building = new Cone(new Vec3D(), new Vec3D(10, 10, 150), d1, d2, h).toMesh(50);
      *  } else if(n==listN[4]) {
      *  building = new Cone(new Vec3D(), new Vec3D(10, 10, 150), d1, d2, h).toMesh(100);
      *  }*/
    // align to terrain normal
    building.pointTowards(currNormal);
    // move to correct position
    building.translate(pos);
    // and draw
    // fill(255,100,0);
    fill(col);
    gfx.mesh(building);

    /*textFont(Arial, 100.0);
     *  String n_ = n.substring(0, 1);
     *  //rotateX(PI);
     *  text(n_, y-50, x-50);*/
  }
  public void update() {
    AABB b = mesh.getBoundingBox();
    constrain(new Rect(b.getMin().to2DXZ(), b.getMax().to2DXZ()).scale(0.99f));
    // compute intersection point on terrain surface
    isec = terrain.intersectAtPoint(x, y);
    // println(isec);
    if(isec.isIntersection) {
      // move slightly above terrain
      Vec3D newPos = isec.pos.add(0, 10, 0);
      pos.interpolateToSelf(newPos, 0.25f);
    }
  }
}

