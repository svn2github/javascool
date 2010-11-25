import processing.core.*; 
import processing.xml.*; 

import toxi.geom.*; 
import toxi.geom.mesh.*; 
import toxi.math.*; 
import toxi.processing.*; 
import processing.opengl.*; 
import javax.media.opengl.*; 

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

public class EnVoiture extends PApplet {

/*///////////////////////////////////////////////////////////////////////////////////
 * 10.2010 C\u00e9cile P-L for Fuscia, ccl.picard@gmail.com
 * GRAPHES
 * Interface p\u00e9dagogique sur la manipulation des concepts li\u00e9s aux graphes
 *
 * Inspir\u00e9e de 'Steering Car' de toxiclibs par Karsten Schmidt
 */









PGraphicsOpenGL pgl;
GL _gl;

float NOISE_SCALE = 0.07f;
int DIM = 80;

Terrain terrain;
ToxiclibsSupport gfx;
TriangleMesh mesh;
AABB b;
Car car;
Trip myTrip;

Vec3D camOffset = new Vec3D(0, 100, 300);
Vec3D eyePos = new Vec3D(0, 1000, 0);

PFont Verdana, Arial;
int gX, gY;
int indN = 0;
boolean mouseDown = false, info = false;
double distance, distanceC;  // ,  comp; // calcule distance parcourue
String[] listN = { "Nice", "Marseille", "Avignon", "Toulouse", "Bordeaux", "Dijon", "Fr\u00e9jus", "Strasbourg", "Caen", "Grenoble", "Lille", "Rennes" };
String firstSelect, secondSelect, start, end;
ArrayList path, restricted;
ArrayList opened, closed;

int[] colors = new int[listN.length];
char[] form = { 'B', 'P', 'O', 'C' };

public void setup() {
  size(1000, 600, OPENGL);  // 1024, 576, OPENGL);
  // Ces deux lignes permettent l'interface avec JavaScool
  proglet = this;
  frame = new Frame();

  Verdana = loadFont("Verdana-48.vlw");
  Arial = loadFont("ArialMT-48.vlw");

  // Pour cr\u00e9er les 2 vues
  pgl = (PGraphicsOpenGL) g;
  _gl = pgl.gl;

  colors[0] = color(0xffFF9900);
  colors[1] = color(100, 200, 0);
  colors[2] = color(0xffFFFF00);
  colors[3] = color(200);
  colors[4] = color(150);
  colors[5] = color(100);
  colors[6] = color(0xff0000FF);
  colors[7] = color(0, 0, 100);
  colors[8] = color(0xff00FFFF);
  colors[9] = color(0xffFF00FF);
  colors[10] = color(100, 0, 100);
  colors[11] = color(0, 70, 75);

  // Cr\u00e9er le terrain et son d\u00e9nivel\u00e9
  terrain = new Terrain(DIM, DIM, 50);
  float[] el = new float[DIM * DIM];
  noiseSeed(23);
  for(int z = 0, i = 0; z < DIM; z++)
    for(int x = 0; x < DIM; x++)
      el[i++] = noise(x * NOISE_SCALE, z * NOISE_SCALE) * 400;
  terrain.setElevation(el);
  // maillage
  mesh = terrain.toMesh(0);
  // voiture
  car = new Car(0, 0);
  myTrip = new Trip();
  // utilitaires de dessin
  gfx = new ToxiclibsSupport(this);

  path = new ArrayList();
  restricted = new ArrayList();
  opened = new ArrayList();
  closed = new ArrayList();
}
public void draw() {
  smooth();

  gX = frame.getX();
  gY = frame.getY();

  // in 2D
  b = mesh.getBoundingBox();

  // in 3D
  // Met \u00e0 jour voiture et les spots
  car.update();
  for(String ni_ : (Iterable<String> )myTrip.spots.keySet()) {
    Spot S_ = (Spot) myTrip.spots.get(ni_);
    S_.update();
  }
  // Ajuste la cam\u00e9ra (hauteur et angle) derri\u00e8re la voiture, en se basant sur l'angle de direction
  Vec3D camPos = car.pos.add(camOffset.getRotatedY(car.currTheta + HALF_PI));
  camPos.constrain(mesh.getBoundingBox());
  float y = terrain.getHeightAtPoint(camPos.x, camPos.z);
  if(!Float.isNaN(y))
    camPos.y = max(camPos.y, y + 100);
  eyePos.interpolateToSelf(camPos, 0.05f);
  background(0xffaaeeff);
  camera(eyePos.x, eyePos.y, eyePos.z, car.pos.x, car.pos.y, car.pos.z, 0, -1, 0);

  // Scinder la vision 3D et planaire
  _gl.glViewport(0, 0, width, height);

  directionalLight(82, 62, 62, 0, -0.5f, -0.5f);   // Lumi\u00e8res
  directionalLight(0, 100, 30, 0.5f, -0.1f, 0.5f);

  fill(255);
  noStroke();
  // Dessine le terrain et la voiture
  gfx.mesh(mesh, false);
  directionalLight(255, 255, 255, 0.05f, -0.005f, 0.05f);
  car.draw();
  for(String ni_ : (Iterable<String> )myTrip.spots.keySet()) {
    Spot S_ = (Spot) myTrip.spots.get(ni_);
    if(ni_.equals(start) || ni_.equals(end)) {
      strokeWeight(3);
      stroke(255, 0, 0);
    } else
      noStroke();
    S_.draw();
  }
  noStroke();

  // Vision planaire
  camera(15, 10, -90,   // eyeX, eyeY, eyeZ
         114, 219, width / 2, // centerX, centerY, centerZ
         0.0f, 1.0f, 0.0f);   // upX, upY, upZ
  lights();

  rotateY(-PI);
  if((distance != 0) && (abs((float) (distance - distanceC)) < 0.01f)) {
    textFont(Arial, 10.0f);
    fill(255, 0, 0);
    text("BRAVO!", -100, 10);
  }
  if(info) {
    textAlign(LEFT);
    fill(255, 70, 0);
    textFont(Arial, 2.5f);
    text(" - I  N  S  T  R  U  C  T  I  O  N  S - \n " +
         "> Navigation: \n" + "    . voiture = les 4 fleches \n" + "    . camera: '+/-' pour zoom/d\u00e9zoom \n" +
         "> Noeud: \n" + "    . ajout = clic droit \n" +
         "> Lien: \n" + "    . ajout/suppression: clic centre + glisse \n " +
         "> G\u00e9n\u00e9rer tous les noeuds = 'a' \n" +
         "> Jouer \u00e0 trouver le plus court chemin entre 2 villes tir\u00e9es au hasard: \n" + "    . 'p' pour une seul escale \n" + "    . 'q' pour deux escales\n " +
         "> Afficher/cacher les instructions: 'i' \n " +
         "> Fermer l'application: ESC ", -100, -10);
  }
  _gl.glClear(GL.GL_DEPTH_BUFFER_BIT);
  _gl.glLoadIdentity();
  _gl.glDisable(GL.GL_BLEND);   // turns off blending
  _gl.glViewport(0, 0, width / 2, height / 2);

  rotateY(PI);
  pushMatrix();
  translate(0, -2, 0);
  rotateZ(-5 * PI / height);

  fill(30, 75, 35);
  rect(0, 0, 100, 100);
  fill(255, 30, 0);
  strokeWeight(2);
  stroke(0);
  translate(0, 0, -1);
  ellipse(car.y2D, car.x2D, 2, 2);
  for(String ni_ : (Iterable<String> )myTrip.spots.keySet()) {
    Spot S_ = (Spot) myTrip.spots.get(ni_);
    fill(S_.col);
    strokeWeight(1.1f);
    if((start != null) && (end != null)) {
      if(ni_.equals(start) || ni_.equals(end)) {
        strokeWeight(3);
        stroke(255, 0, 25);
      } else { strokeWeight(1.1f);
               stroke(0);
      }
    }
    if(S_.f == form[0])
      rect(S_.y2D, S_.x2D, 3, 3);   // inversed otherwise from bottom
    else if(S_.f == form[1])
      triangle(S_.y2D, S_.x2D, S_.y2D - 4, S_.x2D, S_.y2D, S_.x2D - 4);
    else if(S_.f == form[2])
      ellipse(S_.y2D, S_.x2D, 3, 3);
    else if(S_.f == form[3])
      ellipse(S_.y2D, S_.x2D, 3, 3);
    strokeWeight(1.1f);
    stroke(0);

    // textFont(Arial, 5.0);

    /*String n = S_.n.substring(0, 1);
     *  translate(0,0,-1);
     *  if(S_.y2D+10<100 && S_.y2D+10>0 && S_.x2D+10<100 && S_.x2D+10>0 )  text(n, S_.y2D+10, S_.x2D+10);
     *  else  text(n, S_.y2D-10, S_.x2D-10);*/
    // Pour chaque spot, les liens sont d\u00e9tect\u00e9s pour les tracer en noir
    for(String nj_ : (Iterable<String> )myTrip.spots.keySet())
      if(myTrip.isLink(ni_, nj_) && !(ni_.equals(nj_))) {
        Spot S2_ = (Spot) myTrip.spots.get(nj_);
        double p_ = myTrip.getLink(ni_, nj_);

        fill(0);
        stroke(0);
        strokeWeight(1);
        line(S_.y2D, S_.x2D, S2_.y2D, S2_.x2D);

        /*if (info) {
         *  s.textFont(Arial, 10.0);
         *  s.fill(180);
         *  s.text(" " + (float) p_, abs((S_.x2D+S2_.x2D)/2), abs((S_.y2D+S2_.y2D)/2));
         *  }*/
      }
  }
  popMatrix();
}
public void mousePressed() {
  // path.clear();
  mouseDown = true;
  if(mouseButton == RIGHT) {
    float d1_, d2_, h_;
    d1_ = random(50, 100);
    d2_ = random(50, 100);
    h_ = random(100, 200);
    // Add Spot at current car position
    int x_ = (int) car.x;
    int y_ = (int) car.y;
    if(indN < listN.length) {
      int col_ = color((int) (indN * 255 / listN.length), (int) (indN * 255 / listN.length), 0);
      myTrip.addSpot(listN[indN], col_, form[(indN % 4)], x_, y_, d1_, d2_, h_);
      indN++;
    } else {
      indN = 0;
      int col_ = color((int) (indN * 255 / listN.length), (int) (indN * 255 / listN.length), 0);
      myTrip.addSpot(listN[indN], col_, form[(indN % 4)], x_, y_, d1_, d2_, h_);
      indN++;
    }
  }
  if(mouseButton == CENTER) {
    if(myTrip.spots.size() != 0)
      firstSelect = myTrip.getClosestSpot(car.x, car.y);
    println(firstSelect);
  }
}
public void mouseReleased() {  // appel\u00e9 a chaque moment que la souris est relach\u00e9e
  mouseDown = false;
  // Trace lien ou retire lien entre 2 noeuds
  if(firstSelect != null) {
    secondSelect = myTrip.getClosestSpot(car.x, car.y);
    if(secondSelect != firstSelect) {
      if(myTrip.isLink(firstSelect, secondSelect)) {
        distance -= myTrip.getLink(firstSelect, secondSelect);
        println("distance parcourue: " + distance);
        myTrip.removeLink(firstSelect, secondSelect);
      } else {
        myTrip.addLink(firstSelect, secondSelect);
        distance += myTrip.getLink(firstSelect, secondSelect);
        println("un lien cr\u00e9\u00e9: " + firstSelect + " - " + secondSelect);
        println("distance parcourue: " + distance);
      }
    }
  }
  firstSelect = null;
  secondSelect = null;
}
public void keyPressed() {
  // Conduit la voiture
  if(keyCode == UP)
    car.accelerate(1);
  if(keyCode == DOWN)
    car.accelerate(-1);
  if(keyCode == LEFT)
    car.steer(0.1f);
  if(keyCode == RIGHT)
    car.steer(-0.1f);
    // Zoom +/-
  Vec3D addOn = new Vec3D(0, 10, 10);
  Vec3D subStract = new Vec3D(0, -10, -10);
  Vec3D newCam;
  if(key == '-') { // plus loin
    newCam = camOffset.add(addOn);
    newCam.constrain(mesh.getBoundingBox());
    camOffset = newCam;
  }
  if(key == '+') {  // plus pr\u00e8s
    newCam = camOffset.add(subStract);
    newCam.constrain(mesh.getBoundingBox());
    camOffset = newCam;
  }
  // G\u00e9n\u00e8re les noeuds de mani\u00e8re al\u00e9atoire
  if(key == 'a')
    for(int i = 0; i < listN.length; i++) { // string array -> length
      int x_, y_;
      float d1_, d2_, h_;
      x_ = (int) random(b.getMin().to2DXZ().x, b.getMax().to2DXZ().x);
      y_ = (int) random(b.getMin().to2DXZ().y, b.getMax().to2DXZ().y);

      d1_ = random(50, 100);
      d2_ = random(50, 100);
      h_ = random(100, 200);

      myTrip.addSpot(listN[i], colors[i], form[(i % 4)], x_, y_, d1_, d2_, h_);
    }
    // Remise \u00e0 zero de la distance parcourue, \u00e9limine tout lien cr\u00e9\u00e9
  if(key == 'd') {
    distance = 0;
    for(String ni_ : (Iterable<String> )myTrip.spots.keySet())
      for(String nj_ : (Iterable<String> )myTrip.spots.keySet())
        if(myTrip.isLink(ni_, nj_) && !(ni_.equals(nj_)))
          myTrip.removeLink(ni_, nj_);
  }
  // Joue \u00e0 trouver le plus court chemin entre 2 villes, hors chemin direct \u00e9videmment
  if(key == 'p') {
    for(String ni_ : (Iterable<String> )myTrip.spots.keySet())
      for(String nj_ : (Iterable<String> )myTrip.spots.keySet())
        if(myTrip.isLink(ni_, nj_) == false)
          myTrip.addLink(ni_, nj_);
    distanceC = 0;
    String intermediate = null;
    path.clear();
    if(myTrip.spots.size() == listN.length) {
      int k = (int) random(listN.length);
      start = listN[k];
      k = (int) random(listN.length);
      while(listN[k] == start)
        k = (int) random(listN.length);
      end = listN[k];

      myTrip.removeLink(start, end);

      ArrayList pathTemp = new ArrayList();
      // intermediate = myTrip.findPath(start,end, pathTemp);
      myTrip.dijkstra(start, end);
      // distanceC += myTrip.getDistance(start, intermediate) + myTrip.getDistance(intermediate, end);
      distanceC += myTrip.getDistance((String) path.get(0), (String) path.get(1)) + myTrip.getDistance((String) path.get(1), (String) path.get(2));
      // println("Ville interm\u00e9diaire: " + intermediate + " - Distance parcourue: " + distanceC);
      for(int i = 0; i < path.size(); i++) {
        String p = (String) path.get(i);
        println(p);
      }
      println(distanceC);
    }
    for(String ni_ : (Iterable<String> )myTrip.spots.keySet())
      for(String nj_ : (Iterable<String> )myTrip.spots.keySet())
        if(myTrip.isLink(ni_, nj_) == true)
          myTrip.removeLink(ni_, nj_);
  }
  // Joue \u00e0 trouver le plus court chemin entre 2 stations, en visitant obligatoirement 2 stations, sachant une d\u00e9ja donn\u00e9e
  if(key == 'q') {
    for(String ni_ : (Iterable<String> )myTrip.spots.keySet())
      for(String nj_ : (Iterable<String> )myTrip.spots.keySet())
        if(myTrip.isLink(ni_, nj_) == false)
          myTrip.addLink(ni_, nj_);
    if(myTrip.spots.size() == listN.length) {
      if((start == null) || (path.size() == 0))
        println("Rejoue la premi\u00e8re \u00e9tape!");
      else {
        String interm1 = null, interm2 = null;
        distanceC = 0;
        ArrayList pathTemp = new ArrayList();
        // myTrip.findPath(start,end, pathTemp);
        // distanceC += comp;
        for(int i = 0; i < path.size(); i++)
          pathTemp.add((String) path.get(i));                                 // initialization
        for(int i = 0; i < pathTemp.size(); i++) {
          String p = (String) pathTemp.get(i);
          println(p);
        }
        myTrip.removeLink(start, (String) pathTemp.get(1));
        myTrip.removeLink((String) pathTemp.get(1), end);
        // interm1 = myTrip.findPath(start, (String) pathTemp.get(1), pathTemp);
        myTrip.dijkstra(start, (String) pathTemp.get(1));
        interm1 = (String) path.get(1);
        // interm2 = myTrip.findPath((String) pathTemp.get(1), end, pathTemp);
        myTrip.dijkstra((String) pathTemp.get(1), end);
        interm2 = (String) path.get(1);
        if((myTrip.getDistance((String) pathTemp.get(1), interm2) + myTrip.getDistance(interm2, end))
           > (myTrip.getDistance(start, interm1) + myTrip.getDistance(interm1, (String) pathTemp.get(1))))
        {
          distanceC += (myTrip.getDistance(start, interm1)
                        + myTrip.getDistance(interm1, (String) pathTemp.get(1))
                        + myTrip.getDistance((String) pathTemp.get(1), end));
          pathTemp.add(1, interm1);
        } else {
          distanceC += (myTrip.getDistance(start, (String) pathTemp.get(1))
                        + myTrip.getDistance((String) pathTemp.get(1), interm2)
                        + myTrip.getDistance(interm2, end));
          pathTemp.add(2, interm2);
        }
        path = pathTemp;
        for(int i = 0; i < path.size(); i++) {
          String p = (String) path.get(i);
          println("___" + p);
        }
        println(distanceC);
      }
    }
    for(String ni_ : (Iterable<String> )myTrip.spots.keySet())
      for(String nj_ : (Iterable<String> )myTrip.spots.keySet())
        if(myTrip.isLink(ni_, nj_) == true)
          myTrip.removeLink(ni_, nj_);
    path.clear();
  }
  // Montrer ou non les instructions
  if(key == 'i') {
    if(info)

      info = false;

    else

      info = true;
  }
  // G\u00e9n\u00e8re tous les liens possibles entre les noeuds

  /*if( key == 'l' ) {
   *
   *  for(String ni_ : (Iterable<String>) myTrip.spots.keySet())
   *  {
   *   for(String nj_ : (Iterable<String>) myTrip.spots.keySet())
   *   {
   *     if (myTrip.isLink(ni_,nj_) == false) {
   *       myTrip.addLink(ni_,nj_);
   *     }
   *
   *   }
   *  }
   *  } */
}
class Car extends Vec2D {
  Vec3D currNormal = Vec3D.Y_AXIS.copy();
  Vec3D pos;
  IsectData3D isec;

  float currTheta;
  float targetTheta;
  float targetSpeed;
  float speed;
  float x2D, y2D;

  public Car(float x, float y) {
    super(x, y);
    pos = new Vec3D(0, 500, 0);
  }

  public void accelerate(float a) {
    targetSpeed += a;
    targetSpeed = MathUtils.clip(targetSpeed, -20, 20);
  }
  public void draw() {
    // create an axis aligned box and convert to mesh
    TriangleMesh box = new AABB(new Vec3D(), new Vec3D(20, 10, 10)).toMesh();
    // align to terrain normal
    box.pointTowards(currNormal);
    // rotate into direction of movement
    box.rotateAroundAxis(currNormal, currTheta);
    // move to correct position
    box.translate(pos);
    // and draw
    fill(255, 0, 0);
    gfx.mesh(box);
  }
  public void steer(float t) {
    targetTheta += t;
  }
  public void update() {
    // slowly decay target speed
    targetSpeed *= 0.992f;
    // interpolate steering & speed
    currTheta += (targetTheta - currTheta) * 0.1f;
    speed += (targetSpeed - speed) * 0.1f;
    // update position
    addSelf(Vec2D.fromTheta(currTheta).scaleSelf(speed));
    // constrain position to terrain size in XZ plane
    AABB b = mesh.getBoundingBox();
    constrain(new Rect(b.getMin().to2DXZ(), b.getMax().to2DXZ()).scale(0.99f));
    // compute intersection point on terrain surface
    isec = terrain.intersectAtPoint(x, y);
    // println(isec);
    if(isec.isIntersection) {
      // smoothly update normal
      currNormal.interpolateToSelf(isec.normal, 0.25f);
      // move bot slightly above terrain
      Vec3D newPos = isec.pos.add(0, 10, 0);
      pos.interpolateToSelf(newPos, 0.25f);
    }
    x2D = (((x + b.getMax().to2DXZ().x) / (2 * b.getMax().to2DXZ().x)) * 100);
    y2D = (((y + b.getMax().to2DXZ().y) / (2 * b.getMax().to2DXZ().y)) * 100);
  }
}

static EnVoiture proglet;

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

  HashMap links; // tous les liens qui sont accol\u00e9s au spot

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

  public void moveTo(int x_, int y_, float d1_, float d2_, float h_) {
    x = x_;
    y = y_;

    pos = new Vec3D(x, h, y);
    position = new PVector(x, y);

    x2D = (((x_ + b.getMax().to2DXZ().x) / (2 * b.getMax().to2DXZ().x)) * 100);
    y2D = (((y_ + b.getMax().to2DXZ().x) / (2 * b.getMax().to2DXZ().x)) * 100);
  }
  public void init() {
    parent = null;
    g = 0;
  }
  // Calculate G
  public void setG(Link o) {
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

class Trip {
  HashMap spots;

  Trip() {
    spots = new HashMap();
  }

  /** Ajoute ou modifie un spot au graphe (modifie dans le cas ou meme nom employ\u00e9 et diff\u00e9rentes coordonn\u00e9es).
   * @param n Nom du spot.
   * @param x Abcisse du spot.
   * @param y Ordonn\u00e9e du spot.
   */
  public void addSpot(String n, int col, char f, int x, int y, float d1_, float d2_, float h_) {
    if(spots.containsKey(n)) {
      Spot S_ = (Spot) spots.get(n);
      S_.moveTo(x, y, d1_, d2_, h_);
    } else
      spots.put(n, new Spot(n, col, f, x, y, d1_, d2_, h_));
  }
  /** Renvoie l'objet Spot \u00e0 partir de son nom.
   * @param n Nom du spot.
   * @return objet Spot.
   */
  public Spot getSpot(String n) {
    Spot S_ = (Spot) spots.get(n);

    return S_;
  }
  /** Cherche spot plus proche d'une position.
   * @param x Abcisse position souris.
   * @param y Ordonn\u00e9e position souris.
   * @return Nom du spot.
   */
  public String getClosestSpot(float x, float y) {
    // println("x " + x + " // y " + y);

    float curBest = 9999;
    Spot best = (Spot) spots.get(listN[0]);
    String n_ = null;
    if(spots.size() != 0)
      for(String ni_ : (Iterable<String> )spots.keySet()) {
        Spot S_ = (Spot) spots.get(ni_);

        float d = dist(x, y, S_.x, S_.y);
        if(d < curBest) {
          curBest = d;
          best = S_;
          n_ = ni_;
        }
      }
     // println(" nom: " + best.n + " x " + best.x + " // y " + best.y);
    return n_;
  }
  /** D\u00e9truit un spot au graphe si il existe.
   * @param n Nom du spot.
   */
  public void removeSpot(String n) {
    Spot S_ = (Spot) spots.get(n);
    // retire tous les liens en relation avec le noeud
    for(String ni_ : (Iterable<String> )spots.keySet())
      removeLink(n, ni_);
       // retire le noeud en question
    spots.remove(n);
  }
  /** Ajoute ou modifie un lien entre deux spots
   * @param nA Premier spot du lien.
   * @param nB Deuxi\u00e8me spot du lien.
   * @param p Poids du lien. // no?? poids=distance??
   */
  // void addLink(String nA, String nB, double p) {
  public void addLink(String nA, String nB) {
    Spot SA_ = (Spot) spots.get(nA);
    Spot SB_ = (Spot) spots.get(nB);
    double p_ = dist(SA_.x, SA_.y, SB_.x, SB_.y) / 100;
    SA_.links.put(nB, new Link(nA, nB, p_));
    SB_.links.put(nA, new Link(nB, nA, p_));
  }
  /** D\u00e9truit un lien entre deux spots si il existe.
   * @param nA Premier spot du lien.
   * @param nB Deuxi\u00e8me spot du lien.
   */
  public void removeLink(String nA, String nB) {
    Spot SA_ = (Spot) spots.get(nA);
    Spot SB_ = (Spot) spots.get(nB);
    if(isLink(nA, nB)) {
      SA_.links.remove(nB);
      SB_.links.remove(nA);
    }
  }
  /** Affirme si il y a lien entre 2 spots.
   * @param nA Premier spot du lien.
   * @param nB Deuxi\u00e8me spot du lien.
   * @return oui ou non.
   */
  public boolean isLink(String nA, String nB) {
    Spot SA_ = (Spot) spots.get(nA);
    // String ni_;
    boolean link_ = false;
    for(String ni_ : (Iterable<String> )SA_.links.keySet())
      if(ni_.equals(nB))    // test si les deux string sont \u00e9quivalents
        link_ = true;
    return link_;
  }
  /** Donne le poids d'un lien entre deux spots.
   * @param nA Premier spot du lien.
   * @param nB Deuxi\u00e8me spot du lien.
   * @return Le poids du lien o\u00f9 0 si il n'y a pas de lien.
   */
  public double getLink(String nA, String nB) {
    Spot SA_ = (Spot) spots.get(nA);
    Link li_;
    double p_ = 0;
    if(isLink(nA, nB)) {
      li_ = (Link) SA_.links.get(nB);
      p_ = li_.p;
    }
    return p_;
  }
  /** Donne la distance entre deux spots.
   * @param nA Premier spot.
   * @param nB Deuxi\u00e8me spot.
   * @return La distance.
   */
  public double getDistance(String nA, String nB) {
    Spot SA_ = (Spot) spots.get(nA);
    Spot SB_ = (Spot) spots.get(nB);
    double p_ = 0.0f;
    p_ = dist(SA_.x, SA_.y, SB_.x, SB_.y) / 100;

    return p_;
  }
  /**  Cherche spot interm\u00e9diaire entre sInit et sTarget tel que la distance entre sInit et sTarget soit minimal.
   * @param nInit Spot initial.
   * @param nTarget Spot cible.
   * @return Spot interm\u00e9diaire.
   */
  public String exploreSpot(String sO, String sInit, String sTarget, ArrayList Visited) {
    if(sInit == sTarget)
      return sTarget;
    String next = sInit; // initialization
    Spot Sinit = (Spot) spots.get(sInit);
    Spot Starget = (Spot) spots.get(sTarget);
    double p = 99999999;
    ArrayList Visited_ = new ArrayList();
    for(int i = 0; i < Visited.size(); i++)
      Visited_.add((String) Visited.get(i));
    HashMap spotsN = new HashMap();
    for(String ni_ : (Iterable<String> )spots.keySet())
      if(!(Visited_.contains(ni_)))
        spotsN.put(ni_, (Spot) spots.get(ni_));                             // contraindre \u00e0 chercher parmi les spots non visit\u00e9s!
    for(String ni_ : (Iterable<String> )spots.keySet())
      if((!(sInit.equals(sO) && ni_.equals(sTarget)) && !ni_.equals(sInit))) {
        Spot Si_ = (Spot) spots.get(ni_);
        if((path.indexOf(ni_) == -1) && (restricted.indexOf(ni_) == -1)) { // si le spot n'a pas \u00e9t\u00e9 parcouru
          double pi = PVector.dist(Si_.position, Sinit.position) + PVector.dist(Starget.position, Si_.position);
          // println(pi);
          // double pi = getLink(ni_,sInit) + getLink(sTarget,ni_);
          if(pi < p) {
            p = pi;
            next = ni_;
          }
        }
      }
    if(sInit.equals(sO))
      println("Distance parcourue par le trajet: " + p / 100);
      // comp = p/100;
    path.add(sInit);
    // println(next);
    return next;
  }
  /**   Construit le trajet avec tous les spots - appel \u00e0 exploreSpot
   * @param sStart Spot d\u00e9part.
   * @param sEnd Spot final.
   */
  public String findPath(String sStart, String sEnd, ArrayList VisitedSpots) {
    path.clear();
    restricted.clear();
    println(" " + sStart + " \u00e0 " + sEnd);
    String next = sStart, interm = null;
    int its = 0;
    int tests = 0;
    while(next != sEnd && tests < 100) {
      next = exploreSpot(sStart, next, sEnd, VisitedSpots);
      its++;
      if(its > 500) {
        its = 0;
        tests++;
        restricted.add(path.get(path.size() - 1));
        next = (String) path.get(0);
        path.clear();
        println(tests);
      }
    }
    if(tests < 100)
      path.add(sEnd);
    for(int i = 0; i < path.size(); i++) {
      String p = (String) path.get(i);
      if(!(p.equals(sStart)) && !(p.equals(sEnd)))
        interm = p;
    }
    println("interm: " + interm);
    return interm;
  }
  /**   Algorithme de Dijkstra
   * @param sStart Spot d\u00e9part.
   * @param sEnd Spot final.
   */
  public void dijkstra(String sStart, String sEnd) {
    path.clear();
    println(" " + sStart + " \u00e0 " + sEnd);
    for(String ni_ : (Iterable<String> )spots.keySet()) {
      Spot S_ = (Spot) spots.get(ni_);
      S_.init();
    }
    opened.clear();
    closed.clear();
    opened.add(sStart);
    Spot Ss = (Spot) spots.get(sStart);
    Ss.g = 0;
    while(opened.size() > 0) {
      String nCurrent = (String) opened.remove(0);
      // println("nCurrent: " + nCurrent);
      closed.add(nCurrent);
      if(nCurrent == sEnd)
        break;
      Spot Sc = (Spot) spots.get(nCurrent);
      for(String ni_ : (Iterable<String> )Sc.links.keySet()) {
        // for(String ni_ : (Iterable<String>) spots.keySet()) {
        // if(!ni_.equals(sEnd) && !ni_.equals(sStart)) {
        // println("ni_: " + ni_);
        Spot adjacent = (Spot) spots.get(ni_);
        Link a = (Link) Sc.links.get(ni_);
        if(adjacent.walkable && !arrayListContains(closed, ni_)) {
          if(!arrayListContains(opened, ni_)) {
            opened.add(ni_);
            adjacent.parent = Sc;
            adjacent.setG(a);
            // println("g ds open: " + adjacent.g);
          } else if(adjacent.g > Sc.g + a.p) {
            adjacent.parent = Sc;
            adjacent.setG(a);
            // println("g hors open: " + adjacent.g);
          }
        }
      }
      // }
    }
    // Path generation
    String pathSpot = sEnd;
    // println(pathSpot);
    double di = 0;
    if(closed.size() > 1) {
      while(!(pathSpot.equals(sStart))) {
        path.add(pathSpot);
        Spot Sp = (Spot) spots.get(pathSpot);

        di += Sp.g;
        // println(di);
        Sp = Sp.parent;

        pathSpot = Sp.n;
        // println(pathSpot);
      }
      path.add(sStart);
      Spot Sp = (Spot) spots.get(sStart);
      di += Sp.g;
      // println(di);
    }
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
public static final int WIDTH = 1024, HEIGHT = 700;

  static public void main(String args[]) {
    PApplet.main(new String[] { "--bgcolor=#FFFFFF", "EnVoiture" });
  }
}
