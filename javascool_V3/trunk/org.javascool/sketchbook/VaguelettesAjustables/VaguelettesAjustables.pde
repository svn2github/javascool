/*////////////////////////////////////////////////////////////////////////////
 *
 * 11.2010 Cécile P-L for Fuscia - ccl.picard@gmail.com -
 *
 * Inspired from 'WaveRipples' by Steven Kay
 */

int RIPPLES = 3; // =3;
color to = color(0); // bg color cycling extreme colours
color from = color(0); // (77,0,77);
float CYCLEPERIOD = 255.0; // bg color cycling speed
float DAMPING = 1.0; // 0.998; // speed ripples fade at, low value=faster
float nbW = 5;
float ampW = 5;
int rotA = 0;
ArrayList ripples;

// Interface
color currentcolor;
TextButton[] T1 = new TextButton[8];
TextButton[] T2 = new TextButton[20];
TextButton[] T3 = new TextButton[4];
int dim;
PFont myFont;
boolean locked = false;

void setup() {
  size(600, 400, P3D);
  dim = 400;
  myFont = createFont("Arial Bold", 15);

  color baseColor = color(255, 150, 0);
  currentcolor = baseColor;
  // Define and create buttons
  fill(baseColor);
  rect(dim, 0, 200, height);
  color selected = color(255, 0, 0);
  color buttoncolor;
  color highlight;
  for(int i = 0; i < T1.length; i++) {
    buttoncolor = color(50); // (5+i*250/T1.length);
    highlight = color(150); // (50+i*200/T1.length);
    T1[i] = new TextButton(dim + 5 + (i % 4) *dim / 2 / 4, 50 + int (i / 4) *height / 12, 100, buttoncolor, highlight, selected, i + 5);
    if((i + 5) == nbW)
      T1[i].select = true;
    else
      T1[i].select = false;
  }
  for(int i = 0; i < T2.length; i++) {
    buttoncolor = color(50); // color(5+i*250/T2.length);
    highlight = color(150); // color(50+i*200/T2.length);
    T2[i] = new TextButton(dim + 5 + (i % 4) *dim / 2 / 4, 155 + int (i / 4) *height / 12, 100, buttoncolor, highlight, selected, i + 1);
    if((i + 1) == ampW)
      T2[i].select = true;
    else
      T2[i].select = false;
  }
  for(int i = 0; i < T3.length; i++) {
    buttoncolor = color(50); // color(5+i*250/T2.length);
    highlight = color(150); // color(50+i*200/T2.length);
    T3[i] = new TextButton(dim + 5 + (i % 4) *dim / 2 / 4, (height - 40) + int (i / 4) *height / 12, 10, buttoncolor, highlight, selected, 0.91 + i *0.03);
    // println((float)(i*0.1));
    if((float) (0.91 + i * 0.03) == DAMPING)
      T3[i].select = true;
    else
      T3[i].select = false;
  }
  reset();
}
void draw() {
  // if (frameCount%10==0) print(""+frameRate+"fps\n");
  background(lerpColor(from, to, .5 + .5 * (float) Math.cos(frameCount / CYCLEPERIOD)));

  fill(color(255, 150, 0));
  rect(dim, 0, 200, height);

  update(mouseX, mouseY);
  for(int i = 0; i < T1.length; i++)
    T1[i].display();
  for(int i = 0; i < T2.length; i++)
    T2[i].display();
  for(int i = 0; i < T3.length; i++)
    T3[i].display();
  fill(0);
  // stroke(255);
  // strokeWeight(10);
  textFont(myFont);
  text("LONGUEUR D'ONDE ", dim + 10, 20);
  text("AMPLITUDE ", dim + 10, 130);
  text("AMORTISSEMENT ", dim + 10, height - 65);
  noStroke();
  // fade waves out over time
  for(int i = 0; i < ripples.size(); i++) {
    Source s = (Source) ripples.get(i);
    s.fade();
  }
  translate((width - dim) / 2, dim / 2, -400);
  rotateX(rotA / 300.0);

  float totalAmp = 0.0;
  for(float y = -100.0; y < 100.0; y += 3.0)
    for(float x = -100.0; x < 100.0; x += 3.0) {
      // sum of waves from different sources at this point
      float hite = 0.0;
      for(int i = 0; i < ripples.size(); i++) {
        Source s = (Source) ripples.get(i);
        hite += s.getPart(x, y, (float) frameCount);
      }
      totalAmp += Math.abs(hite); // has to be abs() or settles far too fast ;-)

      // brightness level
      float h = 128 + (5 * hite);
      fill(h);
      if(h > 100) {
        // don't bother unless cube bright enough to see :)
        pushMatrix();
        translate(x * 4, y * 4, hite);
        box(8);
        popMatrix();
      }
    }
   // once settled down to a calm sea, start again
  // if (totalAmp<2000.0) {
  // print ("Settled down, start again");
  // reset();
  // } else {
  // if (frameCount%10==0) print ("TotalAmp="+totalAmp+"\n");
  // }
}
/*
 * Functions
 */
void reset() {
  ripples = new ArrayList();
  // RIPPLES = int(random(2,10));
  for(int i = 0; i < RIPPLES; i++)
    addNewRipple();
}
void keyPressed() {
  if(keyCode == UP)
    if(rotA < 360)
      rotA += 10;
  if(keyCode == DOWN)
    if(rotA > 0)
      rotA -= 10;
   // ajout d'ondes
  if(key == ' ')
    addNewRipple();
   // joue sur l'amorti
  if(key == '+')
    if(DAMPING < 1)
      DAMPING += 0.001;
  if(key == '-')
    if(DAMPING > 0)
      DAMPING -= 0.001;             // une très basse valeur -> les ondes se meurent rapidement
}
void addNewRipple() {
  ripples.add(new Source(mouseX - dim / 2, mouseY - dim / 2, ampW, nbW));
}
void update(int x, int y) {
  if(locked == false) {
    for(int i = 0; i < T1.length; i++)
      T1[i].update();
    for(int i = 0; i < T2.length; i++)
      T2[i].update();
    for(int i = 0; i < T3.length; i++)
      T3[i].update();
  } else
    locked = false;
  if(mousePressed) {
    for(int i = 0; i < T1.length; i++)
      if(T1[i].pressed()) {
        currentcolor = T1[i].basecolor;
        T1[i].select = true;
        nbW = T1[i].value;
        for(int j = 0; j < T1.length; j++)
          if(!(j == i))
            T1[j].select = false;
      }
    for(int i = 0; i < T2.length; i++)
      if(T2[i].pressed()) {
        currentcolor = T2[i].basecolor;
        T2[i].select = true;
        ampW = T2[i].value;
        for(int j = 0; j < T2.length; j++)
          if(!(j == i))
            T2[j].select = false;
      }
    for(int i = 0; i < T3.length; i++)
      if(T3[i].pressed()) {
        currentcolor = T3[i].basecolor;
        T3[i].select = true;
        DAMPING = T3[i].value;
        for(int j = 0; j < T3.length; j++)
          if(!(j == i))
            T3[j].select = false;
      }
    if(mouseX < dim)
      reset();
  }
}
