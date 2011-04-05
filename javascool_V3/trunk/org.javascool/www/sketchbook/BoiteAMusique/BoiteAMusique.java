import processing.core.*; 
import processing.xml.*; 

import arb.soundcipher.*; 
import ddf.minim.*; 
import ddf.minim.signals.*; 

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

public class BoiteAMusique extends PApplet {





SoundCipher sc = new SoundCipher(this);
Minim minim = new Minim(this);
AudioInput in;
AudioOutput out;

Freq freqs;
Pixel[][] pix;
Radar Radar_;
Potar potarVol;
Source[][] sources;
HScrollbar hs1;float topPos;

int w; // depending on the width of the interface
int vit = 10;

int bs;
int[] mySource = new int[2];
boolean hasChanged = false;

// Comment this for an automatic search
//String[] filenames = {"plink.aif", "hats.wav", "kick.wav", "SD.wav", "snare.wav", "rec0.wav", "rec1.wav", "rec2.wav", "rec3.wav"};
// Uncomment this for an automatic search
String[] filenames;
String path;

PFont f;

public void setup() {
  size(900, 700); // (screen.width-50,screen.height-50);//900,616);
  background(50);
  frameRate(30);
  f = createFont("Arial Bold", 12, true);
  mySource[0] = 0;
  mySource[1] = 0;

  in = minim.getLineIn(Minim.STEREO, 1648);
  out = minim.getLineOut(Minim.STEREO);
  // Uncomment this for an automatic search
  path = sketchPath + "/data/effects";
  filenames = listFileNames(path);
  freqs = new Freq();

  smooth();

  potarVol = new Potar(width-width/20, height/5);
  w = width / 100;
  bs = 4 * width / 9; // 2*screen.height/3;

  // Radar
  Radar_ = new Radar(16, 5);
  Radar_.drawBody();
  // Initialise sources
  Radar_.addSources(Radar_.nb, Radar_.nl);
  
  hs1 = new HScrollbar(0, 15, width, 15, 3 + 1);
}

public void draw() {
  topPos = hs1.getPos() - width / 2;
  /*textAlign(LEFT);
  fill(0, 40, 63);
  textFont(f, 13);
  text(" - I  N  S  T  R  U  C  T  I  O  N  S - \n " +
       "> 1) S\u00e9lectionner votre source sonore parmi les trois cat\u00e9gories: notes de piano, bips, enregistrements\n " +
       "> 2) S\u00e9lectionner une postion temporelle sur le radar \n " +
       "> Pour acc\u00e9l\u00e9rer/ralentir la vitesse de jeu: fl\u00e8ches '>/<' \n " +
       "> Pour remettre \u00e0 z\u00e9ro le radar: barre espace \n " +
       "> Fermer l'application: ESC ", topPos * 2, 40); 
  hs1.update();
  hs1.display();*/
  fill(153);
  strokeWeight(1);
  ellipse(Radar_.bx, height / 2, 50, 50);
  float volume = in.mix.level() * 5000;         // niveau sonore d'entr\u00e9e
  if(volume > 50)
    volume = 50;
  Radar_.drawCenter(volume);

  Radar_.displayLib();

  Radar_.activateSources(vit, Radar_.nb, Radar_.nl);
  potarVol.display();
  if(mousePressed) {
    if(potarVol.check(mouseX, mouseY))
      potarVol.move(mouseY);
    for(int i = 0; i < Radar_.nb; i++)
      for(int j = 0; j < Radar_.nl; j++) {
        if(pix[i][j].type == 2)
          pix[i][j].setMyAmp(-10 * (j + 1 - Radar_.nl) * potarVol.getValue());
        else if(pix[i][j].type == 1)
          pix[i][j].setMyAmp(-10 * (j + 1 - Radar_.nl) * potarVol.getValue() / 100); // .sine.setAmp(0.005);//(Radar_.nl-j+1)/10000*potarVol.getValue());
        else
          pix[i][j].setMyAmp((Radar_.nl - j + 1) * 10 * potarVol.getValue());
      }
  }
  
  
}
public void mousePressed() {
  for(int i = 0; i < Radar_.nb; i++)
    for(int j = 0; j < Radar_.nl; j++)
      if(pix[i][j].pospix(mouseX, mouseY) == true) {
        pix[i][j].changeColor(0);
        pix[i][j].display();
        pix[i][j].setSource(mySource[0], mySource[1]);
      }
  for(int i = 0; i < 3; i++)
    for(int j = 0; j < freqs.tab[0].length; j++) {
      if(sources[i][j].posSource(mouseX, mouseY, i) == true) {
        // println(" " + i + " // " +j);
        mySource[0] = i;
        mySource[1] = j;
        hasChanged = true;
      } else
        hasChanged = false;
    }
}
public void keyPressed() {
  if(key == ' ') // reinitialise la composition = d\u00e9sactive toute les sources activ\u00e9es

    Radar_.initSources();
   // Agit sur la vitesse de jeu
  if(keyCode == LEFT)
    vit++;
  if(keyCode == RIGHT)
    vit--;
  vit = constrain(vit, 1, 30);
  if(key == 'i') {
    println("Listing all filenames in a directory: ");

    println(filenames);
    println(filenames.length);
  }
}
// This function returns all the files in a directory as an array of Strings
public String[] listFileNames(String dir) {
  File file = new File(dir);
  if(file.isDirectory()) {
    String names[] = file.list();
    return names;
  } else
    // If it's not a directory
    return null;
}
public void stop() {
  minim.stop();
  super.stop();
}
class Freq
{
  float[][] tab = new float[2][16];
  String[] tabN = new String[16];
  String[] tabE = new String[filenames.length];

  Freq() {
    // Notes de Piano
    tab[0][0] = 48;  // do
    tab[0][1] = 50;  // r\u00e9
    tab[0][2] = 52;  // mi..
    tab[0][3] = 53;
    tab[0][4] = 55;
    tab[0][5] = 57;
    tab[0][6] = 59;
    tab[0][7] = 60;  // do
    tab[0][8] = 62;
    tab[0][9] = 64;
    tab[0][10] = 65;
    tab[0][11] = 67;
    tab[0][12] = 69;
    tab[0][13] = 71;
    tab[0][14] = 72;  // do
    tab[0][15] = 74;

    /*for(int i=0;i<tab[0].length;i++) {
     *  tab[0][i] = 48
     *  }*/
    // Noms correspondants
    tabN[0] = "C3";  // do
    tabN[1] = "D3";  // r\u00e9
    tabN[2] = "E3";  // mi..
    tabN[3] = "F3";
    tabN[4] = "G3";
    tabN[5] = "A3";
    tabN[6] = "B3";
    tabN[7] = "C4";  // do
    tabN[8] = "D4";
    tabN[9] = "E4";
    tabN[10] = "F4";
    tabN[11] = "G4";
    tabN[12] = "A4";
    tabN[13] = "B4";
    tabN[14] = "C5";  // do
    tabN[15] = "D5";

    // Signaux carr\u00e9s
    tab[1][0] = 110.00f;
    tab[1][1] = 123.47f;
    tab[1][2] = 130.81f;
    tab[1][3] = 146.83f;
    tab[1][4] = 164.81f;
    tab[1][5] = 174.61f;
    tab[1][6] = 196.00f;
    tab[1][7] = 220.00f;
    tab[1][8] = 246.94f;
    tab[1][9] = 261.63f;
    tab[1][10] = 293.66f;
    tab[1][11] = 329.63f;
    tab[1][12] = 349.23f;
    tab[1][13] = 392.00f;
    tab[1][14] = 440.00f;
    tab[1][15] = 493.88f;
    // Enregistrements
    for(int i = 0; i < filenames.length; i++)
      tabE[i] = filenames[i];
  }
}

class HScrollbar
{
  int swidth, sheight;    // width and height of bar
  int xpos, ypos;         // x and y position of bar
  float spos, newspos;    // x position of slider
  int sposMin, sposMax;   // max and min values of slider
  int loose;              // how loose/heavy
  boolean over;           // is the mouse over the slider?
  boolean locked;
  boolean show = false;
  float ratio;

  HScrollbar(int xp, int yp, int sw, int sh, int l) {
    swidth = sw;
    sheight = sh;
    int widthtoheight = sw - sh;
    ratio = (float) sw / (float) widthtoheight;
    xpos = xp - 3 * swidth / 4;
    ypos = yp - sheight / 2;
    spos = xpos; // + swidth/2 - sheight/2;
    newspos = spos;
    sposMin = xpos;
    sposMax = xpos + swidth - sheight;
    loose = l;
  }

  public void update() {
    if(over())
      newspos = width / 2; // constrain(mouseX-sheight/2, sposMin, sposMax);
    else
      newspos = 0;
    if(abs(newspos - spos) > 1)
      spos = spos + (newspos - spos) / loose;
  }
  public boolean over() {
    if((mouseX > xpos) && (mouseX < xpos + swidth) &&
       (mouseY > ypos) && (mouseY < ypos + sheight))
      return true;
    else
      return false;
  }
  public void display() {
    fill(255);
    strokeWeight(0.1f);
    if(over()) {
      fill(50);
      stroke(255);
    } else {
      stroke(255,0,0);
      strokeWeight(1);
    }
    rect(sheight / 2, ypos, sheight * 5, sheight);
    fill(130);
    textFont(f, 11);
    text("I N F O >>>", sheight / 2 + sheight / 5, ypos + 4 * sheight / 5);
  }
  public float getPos() {
    // Convert spos to be values between
    // 0 and the total width of the scrollbar
    return spos * ratio;
  }
}
class Pixel
{
  float x, y;
  int c;
  int sw, w;
  int col = 0;
  boolean fs = false;

  int type = 0;
  float ampl;

  float[] freq = new float[2];
  String myEnr;
  SquareWave sine;
  AudioSample kicki;

  Pixel(float x_, float y_, int w_, int sw_, float freq1_, float freq2_, String Enr_) {
    x = x_;
    y = y_;
    w = w_;
    sw = sw_;
    sine = new SquareWave(0, 0.5f, out.sampleRate());

    freq[0] = freq1_;
    freq[1] = freq2_;
    myEnr = Enr_;
  }

  public void setMyAmp(float myAmp) {
    ampl = myAmp;
  }
  public void setSource(int type_, int source_) {
    type = type_;
    if(type == 0)
      freq[0] = freqs.tab[0][source_];
    else if(type == 1)
      freq[1] = freqs.tab[1][source_];
    else if(type == 2) {
      if(source_ < filenames.length) {
        myEnr = freqs.tabE[source_];
        kicki = minim.loadSample("effects/" + myEnr);
      } else {
        int k = source_ - filenames.length;
        myEnr = freqs.tabE[k];
        kicki = minim.loadSample("effects/" + myEnr);
      }
    }
  }
  // Rendu visuel du pixel
  public void display() {
    strokeWeight(sw);
    fill(c);
    ellipse(x, y, w, w);
  }
  // Activation de la source sonore
  public void activate() {
    if(type == 0)
      sc.playNote(freq[0], ampl, 5);
    else if(type == 1) {
      sine.setFreq(freq[1]);
      sine.setAmp(ampl);
      out.addSignal(sine);
    } else if(type == 2) {
      kicki.setGain(ampl);
      kicki.trigger();
    }
  }
  // Position de la souris sur source
  public boolean pospix(float mx, float my) {
    boolean resp = false;
    if((mx >= x - w / 2) && (mx <= x + w / 2))
      if((my >= y - w / 2) && (my <= y + w / 2)) {
        resp = true;
        fs = !fs;                      // active ou d\u00e9sactive selon son statut courant
      }
    return resp;
  }
  public void init() {
    fs = false;
  }
  // Changement de couleur du pixel en fonction du balayage
  public void changeColor(int col_) {
    col = col_;
    if(col == 0) {                  // le balayage n'est pas au dessus de la source
      out.removeSignal(sine);
      if(fs == true) {                // source activ\u00e9e
        c = color(0, 150, 0);         // si la source est activ\u00e9e, elle s'allume en vert
        stroke(255);
      } else {
        c = color(153);             // une source non activ\u00e9e est en gris
        stroke(255);
      }
    } else {                        // le balayage est au dessus de la source
      if(fs == true) {
        c = color(255, 70, 62);       // une source activ\u00e9e et balay\u00e9e s'allume en rouge
        stroke(255, 0, 0);
        activate();                 // et sonne
      } else
        c = color(255, 170, 0);       // une source non activ\u00e9e mais balay\u00e9e s'allume en orange
        stroke(255);
    }
  }
}
class Potar
{
  float x, y, yp;
  float valeur = 1; // 50;//0.5;

  Potar(float x_, float y_) {
    x = x_;
    y = y_;
    yp = y;
  }

  public void display() {
    rectMode(CENTER);
    fill(50);
    noStroke();
    rect(x, y, 60, 225);
    stroke(255);
    strokeWeight(4);
    line(x, y - 100, x, y + 100);
    strokeWeight(1);
    fill(10);
    rect(x, yp, 45, 22);
    strokeWeight(3);
    line(x + 23, yp, x - 23, yp);
  }
  public boolean check(float x_, float y_) {
    if((x_ > x - 22) && (x_ < x + 22) && (y_ > y - 100) && (y_ < y + 100))
      return true;
    else
      return false;
  }
  public void move(float y_) {
    yp = y_;
    yp = constrain(yp, y - 100, y + 100);
    valeur = map(yp, y + 90, y - 100, 0, 2);
    valeur = constrain(valeur, 0, 2);
    println(valeur);
  }
  public float getValue() {
    return valeur;
  }
}

class Radar {
  

  int bx, by;        // positionnement
  int nb;         // nombre de sources \u00e0 enchainer dans la boucle: de 8 \u00e0 24
  int nl;            // nombre de niveaux sonores \u00e0 disposition: de 2 \u00e0 10
  int pi = 0;
  int pia;

  Radar(int nb_, int nl_) {
    bx = width / 2 + 120;
    by = height / 2;
    nb = nb_;
    nl = nl_;
  }

  Radar(int x_, int y_, int nb_, int nl_) {
    bx = x_;
    by = y_;
    nb = nb_;
    nl = nl_;
  }

  // Corps du radar
  public void drawBody() {
    float xcoo, ycoo, w, h, sw;                                        // variables pour les balises du radar

    // Dessine le corps du radar
    strokeWeight(1);
    stroke(200);
    fill(153);

    smooth();
    for(int i = 1; i < 5; i = i + 1) {                                   // cercles concentriques
      if(i == 3) {
        ellipse(bx, by, 2 * bs / i, 2 * bs / i);
        ellipse(bx, by, bs / i, bs / i);
      } else
        ellipse(bx, by, bs / i, bs / i);
    }
    line(bx - bs / 2, by, bx, by);                  // lignes d\u00e9limitant les diff\u00e9rentes zones
    line(bx, by - bs / 2, bx, by);
    line(bx + bs / 2, by, bx, by);
    line(bx, by + bs / 2, bx, by);

    line(bx - sqrt(2) * bs / 4, by - sqrt(2) * bs / 4, bx, by);
    line(bx + sqrt(2) * bs / 4, by - sqrt(2) * bs / 4, bx, by);
    line(bx + sqrt(2) * bs / 4, by + sqrt(2) * bs / 4, bx, by);
    line(bx - sqrt(2) * bs / 4, by + sqrt(2) * bs / 4, bx, by);

    rect(bx - (bs / 30) / 2, by - (bs / 30) / 2, bs / 30, bs / 30);                    // centre du radar

    // les balises \u00e0 chaque quartier
    sw = (bs / 40);                                                      // cote des petits carr\u00e9s
    noSmooth();
    int i = 1;
    while(i < 5) {
      if(i == 1) {
        w = 8 * sw;
        h = 2 * sw;
        xcoo = bx - bs / 2 - w * 3 / 4;
        ycoo = by - sw;
      } else if(i == 2) {
        w = 2 * sw;
        h = 8 * sw;
        xcoo = bx - sw;
        ycoo = by + (bs / 2) - h * 1 / 4;                           // h = largeur des balises; w = longueur des balises
      } else if(i == 3) {
        w = 2 * sw;
        h = 8 * sw;
        xcoo = bx - sw;
        ycoo = by - (bs / 2) - h * 3 / 4;
      } else {
        w = 8 * sw;
        h = 2 * sw;
        xcoo = bx + bs / 2 - w * 1 / 4;
        ycoo = by - sw;
      }
      rect(xcoo, ycoo, w, h);
      for(int j = 1; j < 5; j = j + 1) {
        if(i == 1)
          rect(xcoo + j * (h - sw) / 2 + (j - 1) * sw, ycoo + (h - sw) / 2, sw, sw);
        else if(i == 2)
          rect(xcoo + (w - sw) / 2, ycoo + (j + 1) * (w - sw) / 2 + (j) * sw, sw, sw);
        else if(i == 3)
          rect(xcoo + (w - sw) / 2, ycoo + (j) * (w - sw) / 2 + (j - 1) * sw, sw, sw);
        else
          rect(xcoo + (j + 1) * (h - sw) / 2 + (j) * sw, ycoo + (h - sw) / 2, sw, sw);
      }
      i = i + 1;
    }
    smooth();
  }
  // Centre s'illumine en fonction du volume sonore ambiant
  public void drawCenter(float level_) {
    noStroke();
    fill(255, 50);
    ellipse(bx, by, level_, level_);
    ellipse(bx, by, level_ - level_ / 4, level_ - level_ / 4);
    ellipse(bx, by, level_ / 2, level_ / 2);
    ellipse(bx, by, level_ / 4, level_ / 4);
    strokeWeight(2);
  }
  // Visualise l'ensemble des sources \u00e0 disposition
  public void displayLib() {
   
  
    sources = new Source[3][freqs.tab[0].length];

    fill(255, 0, 0);
    strokeWeight(1);
    stroke(255);
    rectMode(CORNER);
    for(int i = 0; i < 3; i++)
      rect(10, by -35 + i * 120, 210, 20);
    textAlign(LEFT);
    fill(255);
    text(" - " + freqs.tab[0].length + " notes de piano -\n", 10, by-20);
    text(" - " + freqs.tab[1].length + " bips digitaux (Hz) -\n", 10, by + 100);
    text(" - " + freqs.tabE.length + " enregistrements -\n", 10, by + 220);
    fill(50);
    noStroke();
    for(int i = 0; i < 3; i++)
      rect(10, by - 35 + 25 + i * (120), 220, 90);
     // A simplifier!!
    for(int i = 0; i < freqs.tab[0].length; i++) {
      sources[0][i] = new Source(10 + (i % 7) *30, by + 10 + ((i - (i % 7)) / 7) *20, 0, i);
      sources[1][i] = new Source(10 + (i % 7) *30, by + 100 + 30 + ((i - (i % 7)) / 7) *20, 1, i);
      sources[2][i] = new Source(10 + (i % 3) *70, by + 220 + 30 + ((i - (i % 3)) / 3) *20, 2, i);
      if(mySource[1] == i) {
        fill(0, 190, 0);
        if(mySource[0] == 0) {
          text(" " + freqs.tabN[i] + " ", sources[0][i].x, sources[0][i].y);
          fill(255);
          text(" " + PApplet.parseInt (freqs.tab[1][i]) + " ", sources[1][i].x, sources[1][i].y);
          if(i < freqs.tabE.length)
            text(" " + freqs.tabE[i] + " ", sources[2][i].x, sources[2][i].y);
        } else if(mySource[0] == 1) {
          text(" " + PApplet.parseInt (freqs.tab[1][i]) + " ", sources[1][i].x, sources[1][i].y);
          fill(255);
          text(" " + freqs.tabN[i] + " ", sources[0][i].x, sources[0][i].y);  
          if(i < freqs.tabE.length)
            text(" " + freqs.tabE[i] + " ", sources[2][i].x, sources[2][i].y);
        } else {
          text(" " + freqs.tabE[i] + " ", sources[2][i].x, sources[2][i].y);
          fill(255);
          text(" " + freqs.tabN[i] + " ", sources[0][i].x, sources[0][i].y);
          text(" " + PApplet.parseInt (freqs.tab[1][i]) + " ", sources[1][i].x, sources[1][i].y);
        }
      } else {
        fill(255);

        text(" " + freqs.tabN[i] + " ", sources[0][i].x, sources[0][i].y);

        text(" " + PApplet.parseInt (freqs.tab[1][i]) + " ", sources[1][i].x, sources[1][i].y);
        if(i < freqs.tabE.length)
          text(" " + freqs.tabE[i] + " ", sources[2][i].x, sources[2][i].y);
      }
    }
    textAlign(LEFT);
    fill(50);
    rect(0, 0, width/2-50, height/3);
    fill(153);
    textFont(f, 11);
    text(" - I  N  S  T  R  U  C  T  I  O  N  S - \n " +
         ". Composer une s\u00e9quence sonore en deux actions: \n" +
         " 1) S\u00e9lectionner votre source sonore parmi les trois cat\u00e9gories: \n" +
         "  notes de piano, bips, enregistrements\n " +
         " 2) S\u00e9lectionner une postion temporelle sur le radar \n " +
         ". Pour acc\u00e9l\u00e9rer/ralentir la vitesse de jeu: fl\u00e8ches '>/<' \n " +
         ". Pour augmenter/diminuer le volume globale, agir sur le curseur noir \n " +
         ". Pour remettre \u00e0 z\u00e9ro le radar: barre espace \n " +
         ". Fermer l'application: ESC ", topPos * 2, 40); 
    hs1.update();
    hs1.display();
  }
  /** Cr\u00e9er les sources sonores
   */
  public void addSources(int nb_, int nl_) {
    pix = new Pixel[nb_][nl_];
    float x_, y_;
    int sw = 1;
    // AudioSample kicki;
    for(int i = 0; i < nb_; i++)
      for(int j = 0; j < nl_; j++) {
        float a = (width / (30) + j * width / (nl_ * 5));
        x_ = a * cos(i * PI / (nb_ / 2)) + (bx);
        y_ = a * sin(i * PI / (nb_ / 2)) + (by);
        if(i < freqs.tab[0].length) {
          if(i < filenames.length)
            pix[i][j] = new Pixel(x_, y_, w, sw, freqs.tab[0][i], freqs.tab[1][i], freqs.tabE[i]);
          else {
            int k = i - filenames.length;
            pix[i][j] = new Pixel(x_, y_, w, sw, freqs.tab[0][i], freqs.tab[1][i], freqs.tabE[k]);
          }
        } else {
          int k = i - freqs.tab[0].length;
          pix[i][j] = new Pixel(x_, y_, w, sw, freqs.tab[0][k], freqs.tab[1][k], freqs.tabE[i]);
        }
      }
    initSources();
  }
  /** Initialise les sources sonores (les trace ttes d'un coup)
   */
  public void initSources() {
    for(int i = 0; i < nb; i++)
      for(int j = 0; j < nl; j++) {
        pix[i][j].init();
        pix[i][j].changeColor(0);
        pix[i][j].display();
      }
  }
  /** Activation des sources sonores par le signal de balayage
   */
  public void activateSources(int vit_, int nb_, int nl_) {
    if(frameCount % vit == 0) {
      if(pi == nb_) {
        pi = 0;
        pia = nb_ - 1;
      }
      for(int j = 0; j < nl_; j++) {
        pix[pi][j].changeColor(1);
        pix[pi][j].display();
        pix[pia][j].changeColor(0);
        pix[pia][j].display();
      }
      pia = pi;
      pi++;
    }
  }
}
class Source {
  float x, y; // (positionnement dans la librarie)
  int type;
  int ref;
  String refE;

  float[][] tab = new float[2][16];

  Source(float x_, float y_, int type_, int ref_) {
    x = x_;
    y = y_;
    type = type_;
    ref = ref_; // nb in the tab?
  }

  Source(float x_, float y_, int type_, String refE_) {
    x = x_;
    y = y_;
    type = type_;
    refE = refE_; // nb in the tab?
  }

  public boolean posSource(int mx, int my, int type) {
    boolean resp = false;
    if((type == 0) || (type == 1)) {
      if((mx >= x - 2) && (mx <= x + 25))
        if((my >= y - 10) && (my <= y + 5))
          resp = true;
      return resp;
    } else {
      if((mx >= x - 2) && (mx <= x + 50))
        if((my >= y - 10) && (my <= y + 5))
          resp = true;
      return resp;
    }
  }
}
  static public void main(String args[]) {
    PApplet.main(new String[] { "--bgcolor=#DFDFDF", "BoiteAMusique" });
  }
}
