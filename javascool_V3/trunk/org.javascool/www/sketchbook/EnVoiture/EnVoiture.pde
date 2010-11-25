/**
 * 10.2010 Cécile P-L for Fuscia, ccl.picard@gmail.com
 * GRAPHES
 * Interface pédagogique sur la manipulation des concepts liés aux graphes
 *
 * Inspirée de 'Steering Car' de toxiclibs par Karsten Schmidt
 */

import toxi.geom.*;
import toxi.geom.mesh.*;
import toxi.math.*;
import toxi.processing.*;

import processing.opengl.*;
import javax.media.opengl.*;

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
String[] listN = { "Nice", "Marseille", "Avignon", "Toulouse", "Bordeaux", "Dijon", "Fréjus", "Strasbourg", "Caen", "Grenoble", "Lille", "Rennes" };
String firstSelect, secondSelect, start, end;
ArrayList path, restricted;
ArrayList opened, closed;

color[] colors = new color[listN.length];
char[] form = { 'B', 'P', 'O', 'C' };

void setup() {
  size(1200, 700, OPENGL);  // 1024, 576, OPENGL);
  // Ces deux lignes permettent l'interface avec JavaScool
  proglet = this;
  frame = new Frame();

  Verdana = loadFont("Verdana-48.vlw");
  Arial = loadFont("ArialMT-48.vlw");

  // Pour créer les 2 vues
  pgl = (PGraphicsOpenGL) g;
  _gl = pgl.gl;

  colors[0] = color(# FF9900);
  colors[1] = color(100, 200, 0);
  colors[2] = color(# FFFF00);
  colors[3] = color(200);
  colors[4] = color(150);
  colors[5] = color(100);
  colors[6] = color(# 0000FF);
  colors[7] = color(0, 0, 100);
  colors[8] = color(# 00FFFF);
  colors[9] = color(# FF00FF);
  colors[10] = color(100, 0, 100);
  colors[11] = color(0, 70, 75);

  // Créer le terrain et son dénivelé
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
void draw() {
  smooth();

  gX = frame.getX();
  gY = frame.getY();

  // in 2D
  b = mesh.getBoundingBox();

  // in 3D
  // Met à jour voiture et les spots
  car.update();
  for(String ni_ : (Iterable<String> )myTrip.spots.keySet()) {
    Spot S_ = (Spot) myTrip.spots.get(ni_);
    S_.update();
  }
  // Ajuste la caméra (hauteur et angle) derrière la voiture, en se basant sur l'angle de direction
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

  directionalLight(82, 62, 62, 0, -0.5f, -0.5f);   // Lumières
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
         0.0, 1.0, 0.0);   // upX, upY, upZ
  lights();

  rotateY(-PI);
  if((distance != 0) && (abs((float) (distance - distanceC)) < 0.01)) {
    textFont(Arial, 10.0);
    fill(255, 0, 0);
    text("BRAVO!", -100, 10);
  }
  if(info) {
    textAlign(LEFT);
    fill(255, 70, 0);
    textFont(Arial, 2.5);
    text(" - I  N  S  T  R  U  C  T  I  O  N  S - \n " +
         "> Navigation: \n" + "    . voiture = les 4 fleches \n" + "    . camera: '+/-' pour zoom/dézoom \n" +
         "> Noeud: \n" + "    . ajout = clic droit \n" +
         "> Lien: \n" + "    . ajout/suppression: clic centre + glisse \n " +
         "> Générer tous les noeuds = 'a' \n" +
         "> Jouer à trouver le plus court chemin entre 2 villes tirées au hasard: \n" + "    . 'p' pour une seul escale \n" + "    . 'q' pour deux escales\n " +
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
    strokeWeight(1.1);
    if((start != null) && (end != null)) {
      if(ni_.equals(start) || ni_.equals(end)) {
        strokeWeight(3);
        stroke(255, 0, 25);
      } else { strokeWeight(1.1);
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
    strokeWeight(1.1);
    stroke(0);

    // textFont(Arial, 5.0);

    /*String n = S_.n.substring(0, 1);
     *  translate(0,0,-1);
     *  if(S_.y2D+10<100 && S_.y2D+10>0 && S_.x2D+10<100 && S_.x2D+10>0 )  text(n, S_.y2D+10, S_.x2D+10);
     *  else  text(n, S_.y2D-10, S_.x2D-10);*/
    // Pour chaque spot, les liens sont détectés pour les tracer en noir
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
void mousePressed() {
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
void mouseReleased() {  // appelé a chaque moment que la souris est relachée
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
        println("un lien créé: " + firstSelect + " - " + secondSelect);
        println("distance parcourue: " + distance);
      }
    }
  }
  firstSelect = null;
  secondSelect = null;
}
void keyPressed() {
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
  if(key == '+') {  // plus près
    newCam = camOffset.add(subStract);
    newCam.constrain(mesh.getBoundingBox());
    camOffset = newCam;
  }
  // Génère les noeuds de manière aléatoire
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
    // Remise à zero de la distance parcourue, élimine tout lien créé
  if(key == 'd') {
    distance = 0;
    for(String ni_ : (Iterable<String> )myTrip.spots.keySet())
      for(String nj_ : (Iterable<String> )myTrip.spots.keySet())
        if(myTrip.isLink(ni_, nj_) && !(ni_.equals(nj_)))
          myTrip.removeLink(ni_, nj_);
  }
  // Joue à trouver le plus court chemin entre 2 villes, hors chemin direct évidemment
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
      // println("Ville intermédiaire: " + intermediate + " - Distance parcourue: " + distanceC);
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
  // Joue à trouver le plus court chemin entre 2 stations, en visitant obligatoirement 2 stations, sachant une déja donnée
  if(key == 'q') {
    for(String ni_ : (Iterable<String> )myTrip.spots.keySet())
      for(String nj_ : (Iterable<String> )myTrip.spots.keySet())
        if(myTrip.isLink(ni_, nj_) == false)
          myTrip.addLink(ni_, nj_);
    if(myTrip.spots.size() == listN.length) {
      if((start == null) || (path.size() == 0))
        println("Rejoue la première étape!");
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
  // Génère tous les liens possibles entre les noeuds

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
