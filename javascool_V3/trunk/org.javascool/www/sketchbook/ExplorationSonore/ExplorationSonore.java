import processing.core.*; 
import processing.xml.*; 

import processing.opengl.*; 
import javax.media.opengl.*; 
import ddf.minim.*; 
import ddf.minim.signals.*; 
import ddf.minim.analysis.*; 
import ddf.minim.effects.*; 

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

public class ExplorationSonore extends PApplet {

/*////////////////////////////////////////////////////////////////////////////
 *
 * 08.2010 C\u00e9cile P-L for Fuscia - ccl.picard@gmail.com - 
 * PERCEPTION SONORE:
 * Interface p\u00e9dagogique sur les notions de contenu sonore et perception; 
 * Utilisation de Minim pour le son -  
 *
 * Inspired from a tutorial of Andy Best http://andybest.net/
 **/                                                                                                                                                                                                                                                                                                                    // /////////////////////////////////////////////////////////////////////////////////









Minim minim;
AudioInput in;
AudioOutput out;
AudioPlayer player;
FFT fft;

int w;
PImage fade;
float rWidth, rHeight;
Frame frame;
int myOr = color(255, 100, 0);
int myRed = color(255, 0, 0);
int myBlue = color(153);  // (120,140,150);//(20,70,105);
int width_;
int height_;
boolean isOpen;
int buttonValue = 1;

TextButton[] T1 = new TextButton[8];
boolean locked = false;
// boolean info = false;

String[] ListN = { "sinus", "carr\u00e9", "scie", "bruit", " extrait ", " extrait filtr\u00e9 ", " S T O P ", " - Info - " };
String sig = "null";

// Param\u00e8tres pour la sinusoide, et l'enregistrement charg\u00e9
int count = 0;
signal signal1, signal2;
record record1;
PFont f;

int c = 0;

// Ce qui est lanc\u00e9 une fois, au d\u00e9part
public void setup() {
  frameRate(60);
  // Ces deux lignes permettent l'interface avec JavaScool
  proglet = this;
  frame = new Frame();

  f = createFont("Arial Bold", 14, true);
  size(800, 600, P3D);  // size(800,512);//,P3D);//OPENGL);

  // frame.setLocation(screen.width/2,screen.height/6);
  this.frame.setTitle("A N A L Y S E   D U   C O N T E N U   F R E Q U E N T I E L");

  // Outils relatifs au son
  minim = new Minim(this);
  in = minim.getLineIn(Minim.STEREO, 512);
  out = minim.getLineOut(Minim.STEREO);

  signal1 = new signal();
  signal2 = new signal();
  record1 = new record();

  // FFT: Transformation de Fourier pour l'analyse fr\u00e9quentielle en temps r\u00e9el
  launchFFT();

  // Interface de manipulation: g\u00e9n\u00e8re sinusoide, charge enregistrement, etc
  Arrays.fill(((PGraphics3D) g).zbuffer, Float.MAX_VALUE);
  // Define and create buttons
  fill(0);
  rect(0, height - 100, width, 100);
  fill(myBlue);
  int buttoncolor;
  int highlight;
  for(int i = 0; i < T1.length; i++) {
    if(i < 6)
      T1[i] = new TextButton(5 + ((i % 4) * 2 * width / 3 / T1.length), height - 100 + 60 *(PApplet.parseInt (i / 4)), 60 + PApplet.parseInt (i / 5) *35, 25, color(255), myBlue, myRed, myOr, ListN[i]);
    else if(i == 6)
      T1[i] = new TextButton(5 + ((i % 4) * 2 * width / 3 / T1.length) + 60, height - 40, 70, 30, color(255), myRed, myOr, color(0), ListN[i]);
    else
      T1[i] = new TextButton(5 + ((i % 4) * 2 * width / 3 / T1.length) + width / 2 + 120, height - 155, 60, 25, color(255), color(0), myOr, color(0), ListN[i]);
  }
  width_ = this.frame.getWidth();
  height_ = this.frame.getHeight();
}
// Ce qui est effectu\u00e9 tout au long de l'animation
public void draw() {
  background(0);
  pushMatrix();
  if(signal1.sounding) {
    c += 1;
    fft = new FFT(out.bufferSize(), out.sampleRate());
    fft.logAverages(60, 6 * width / (640));
    drawFFT("out");                                                 // Trace la FFT
    // drawSignal("out");                                          // Trace le signal temporel
    if((c) % 2 == 0)
      drawSignal("out");
  } else if(record1.sounding) {
    fft = new FFT(player.bufferSize(), player.sampleRate());

    fft.logAverages(60, 6 * width / (640));
    drawFFT("player");
    drawSignal("player");
  } else {
    fft = new FFT(in.bufferSize(), in.sampleRate());
    fft.logAverages(60, 6 * width / (640));
    drawFFT("in");
    drawSignal("in");
  }
  popMatrix();

  Arrays.fill(((PGraphics3D) g).zbuffer, Float.MAX_VALUE);

  fill(0);
  rect(0, height - 150, width, 150);
  fill(myBlue);
  textFont(f, 12);
  text("S I G N A U X  N U M E R I Q U E S", 6, height - 125);
  text("E N R E G I S T R E M E N T", 6, height - 65);
  update(mouseX, mouseY);
  for(int i = 0; i < T1.length; i++)
    T1[i].display();
     // Fenetre informative
  myInfo();
}
// Un acc\u00e8s rapide aux fonctions via le clavier
public void keyPressed() {
  if(key == '0')
    signal1.setSignal("sinus", 1000, 0.2f);
  if(key == '1') {
    signal1.setSignal("sinus", 1000, 0.2f);
    signal2.setSignal("sinus", 4000, 0.2f);
  }
  if(key == '2')
    signal1.setSignal("carr\u00e9", 1000, 0.2f);
  if(key == '3')
    signal1.setSignal("scie", 1000, 0.2f);
  if(key == '4')
    signal1.setSignal("bruit", 1000, 0.2f);
  if(key == 'e')
    record1.setRecord("../data/music/Ahmed_Ex2.wav");
  if(key == 'f')
    record1.setFilter("../data/music/Ahmed_Ex2.wav", 500);
  if(key == 's')
    StopAnySound();
}
// Update les \u00e9tats des boutons
public void update(int x, int y) {
  if(locked == false)
    for(int i = 0; i < T1.length; i++)
      T1[i].update();
  else
    locked = false;
  if(mousePressed)
    for(int i = 0; i < T1.length; i++)
      if(T1[i].pressed() && !(T1[i].select)) {
        T1[i].select = true;
        if(i < 4)
          signal1.setSignal(T1[i].value, 1000, 0.2f);
        else if(i == 4)
          record1.setRecord("data/music/Ahmed_Ex2.wav");
        else if(i == 5) {
          record1.setRecord("data/music/Ahmed_Ex2.wav");
          record1.applyFilter();
        } else if(i == 6)
          StopAnySound();
        for(int j = 0; j < T1.length - 1; j++)
          if(!(j == i))
            T1[j].select = false;
      }
}
// Fenetre informative
public void myInfo() {
  if(T1[7].over()) {
    fill(255);
    rect(0, height - 145, width, 130);
    fill(myOr);
    text(" Parles, siffles, chuchotes.., et tu verras ce qui se passe sur l'analyseur de contenu sonore.. \n " +
         "Tu peux aussi jouer une signal ou un enregistrement de ton choix. \n " +
         "Pour ajuster la fr\u00e9quence et l'amplitude du signal, bouges la souris sur la fenetre de l'analyseur. \n " +
         "Pour faire varier le volume de l'enregistrement sonore, tu peux proc\u00e9der pareillement, \n " +
         "tandis que le contenu fr\u00e9quentiel peut s'ajuster par un filtre. Exp\u00e9rimentes! \n ", 50, height - 110);
  }
}
// Lors de la fermeture du programme, arreter tout outil de Minim
public void stop() {
  out.close();
  if(count != 0)
    record1.ferme();
  minim.stop();
  super.stop();
}
class Button
{
  int x, y;
  int L, h;
  int basecolor, highlightcolor, selectcolor;
  int currentcolor, fcolor;
  String value;
  boolean over = false;
  boolean pressed = false;
  boolean select = false;

  public void update() {
    if(over())
      currentcolor = highlightcolor;
    else if(select)
      currentcolor = selectcolor;
    else
      currentcolor = basecolor;
  }
  public boolean pressed() {
    if(over) {
      locked = true;
      return true;
    } else {
      locked = false;
      return false;
    }
  }
  public boolean over() {
    return true;
  }
  public boolean overText(int x, int y, int width, int height) {
    if((mouseX >= x) && (mouseX <= x + width) &&
       (mouseY >= y - height / 2) && (mouseY <= y + height / 2))
      return true;
    else
      return false;
  }
}

class TextButton extends Button
{
  TextButton(int ix, int iy, int iL, int ih, int ifcolor, int icolor, int ihighlight, int iselect, String itext) {
    x = ix;
    y = iy;
    L = iL;
    h = ih;
    fcolor = ifcolor;
    basecolor = icolor;
    highlightcolor = ihighlight;
    selectcolor = iselect;
    currentcolor = basecolor;
    value = itext;
  }

  public boolean over() {
    if(overText(x, y, L, h)) {
      over = true;
      return true;
    } else {
      over = false;
      return false;
    }
  }
  public void display() {
    stroke(255);
    strokeWeight(1);

    fill(currentcolor);
    rect(x, y - PApplet.parseInt (h / 2 + h / 4), L, h);

    noStroke();
    fill(fcolor);
    textFont(f);

    text(value, x + (L - value.length() * 6) / 2, y);
  }
}

/* Fonctions relatives \u00e0 l'interface. */

public void mouseMoved() {
  if(signal1.sounding) {
    signal1.changeValue();
    signal1.printV();
  } else if(record1.sounding) {
    record1.changeValue();
    record1.printV();
  }
}
/** Stop tout son  */
public void StopAnySound() {
  if(signal1.sounding) {
    signal1.switchOff();
    sig = "null";
  } else if(record1.sounding)
    record1.switchOff();
  if(signal2.sounding)
    signal2.switchOff();
}
// Lance l'analyse spectrale
public void launchFFT() {
  fft = new FFT(in.bufferSize(), in.sampleRate());

  stroke(0);

  fft.logAverages(60, 6); // /(screen.width/2)); //6 pour screen.width/2

  w = width / fft.avgSize();
  strokeWeight(w);
  strokeCap(SQUARE);

  background(0);
  fade = get(0, 0, width, height);  // fade = get(0, 0, 100, 100);

  rWidth = width * 0.99f;
  rHeight = height * 0.99f;
}
/** Trac\u00e9 3D du spectre au fil du temps. */
public void drawFFT(String n) {
  String type = n;
  float factor;
  strokeWeight(10);
  tint(250, 250);   // gris,alpha sinon (255, 150, 0, 250)
  image(fade, (width - rWidth) / 2, (height - rHeight) / 2, rWidth, rHeight);
  noTint();
  if(n.equals("out")) {
    fft.forward(out.mix);
    factor = (signal1.volume + 20);
  } else if(n.equals("player")) {
    fft.forward(player.mix);
    factor = (record1.volume + 20);
  } else {
    fft.forward(in.mix);
    factor = 20;
  }
  stroke(240, 240, 240);
  for(int i = 0; i < fft.avgSize(); i++)
    line((i * w) + (w / 2), 2 * height / 3, (i * w) + (w / 2), 2 * height / 3 - fft.getAvg(i) * factor);
  fade = get(0, 0, width, height);

  stroke(250, 70, 0);
  textFont(f, 14);
  fill(255);
  text("100", width / 10, 2 * height / 3 + height / 30);
  text("125", width / 6, 2 * height / 3 + height / 30);
  text("250", width / 5 + width / 12, 2 * height / 3 + height / 30);
  text("500", width / 2 - width / 12, 2 * height / 3 + height / 30);
  text("1000", width / 2 + width / 36, 2 * height / 3 + height / 30);
  text("2000", 2 * width / 3 - width / 100, 2 * height / 3 + height / 30);
  text("4000", 4 * width / 5 - width / 50, 2 * height / 3 + height / 30);
  text("8000  Hz", width - width / 10, 2 * height / 3 + height / 30);
  for(int i = 0; i < fft.avgSize(); i++)
    line((i * w) + (w / 2), 2 * height / 3, (i * w) + (w / 2), 2 * height / 3 - fft.getAvg(i) * factor);
  strokeWeight(0.5f);
}
/** Trac\u00e9 temporel du signal. */
public void drawSignal(String n) {
  stroke(255);
  strokeWeight(1);
  int k;
  if(n.equals("out"))
    k = out.bufferSize();
  else if(n.equals("player"))

    k = player.bufferSize();
  else
    k = in.bufferSize();
  for(int i = 0; i < k - 1; i++) {
    // println("buffer: " + in.bufferSize());

    float x1 = map(i, 0, k, 0, width);
    float x2 = map(i + 1, 0, k, 0, width);
    if(n.equals("out")) {
      line(x1, 40 + out.left.get(i) * (signal1.volume + 20) * 5, x2, 40 + out.left.get(i + 1) * (signal1.volume + 20) * 5);
      line(x1, 120 + out.right.get(i) * (signal1.volume + 20) * 5, x2, 120 + out.right.get(i + 1) * (signal1.volume + 20) * 5);
    } else if(n.equals("player")) {
      line(x1, 40 + player.left.get(i) * (record1.volume + 20) * 3, x2, 40 + player.left.get(i + 1) * (record1.volume + 20) * 3);
      line(x1, 120 + player.right.get(i) * (record1.volume + 20) * 3, x2, 120 + player.right.get(i + 1) * (record1.volume + 20) * 3);
    } else {
      line(x1, 40 + in.left.get(i) * 100, x2, 40 + in.left.get(i + 1) * 100);
      line(x1, 120 + in.right.get(i) * 100, x2, 120 + in.right.get(i + 1) * 100);
    }
  }
  strokeWeight(0.5f);
}
/* Fonctions pour javascool. */

/** Joue un signal de type choisi
 * @param n nom du type: sinus, square, triangle, saw, white noise.
 * @param f fr\u00e9quence du signal.
 * @param a amplitude du signal.
 */
public static void playSignal(String n, double f, double a) {
  proglet.signal1.setSignal(n, (float) f, (float) a);
}
/** Joue un enregistrement de son choix
 * @param path Nom de l'extrait
 */
public static void playRecord(String path) {
  proglet.record1.setRecord(path);
}
/** Applique un filtre avec une fr\u00e9quence de coupure ajustable sur l'enregistrement de son choix
 * @param path Nom de l'extrait
 * @param f fr\u00e9quence de coupure du filtre (entre 100 et 10000, sinon rien)
 */
public static void setFilter(String path, double fc) {
  proglet.record1.setFilter(path, (float) fc);
}
/** Arr\u00eate l'\u00e9mission sonore. */
public static void playStop() {
  proglet.StopAnySound();
}
static ExplorationSonore proglet;

/** D\u00e9finit un signal sonore enregistr\u00e9. */
class record {
  /** Construction du signal et de son interaction graphique. */
  record() {
    lpf = new LowPassSP(100, out.sampleRate());
  }

  float volume;
  float Fc = 100;

  LowPassSP lpf;

  boolean sounding = false;
  boolean filtering = false;

  /** Joue un enregistrement de son choix
   * @param path Nom de l'extrait
   */
  public void setRecord(String path) {
    if(sounding) {
      switchOff();
      if(filtering)
        removeFilter();
    } else if(signal1.sounding)
      signal1.switchOff();
    if(path != null) {
      count += 1;
      player = minim.loadFile(path);
      changeValue();
      player.loop();
      sounding = true;
    }
  }
  public void applyFilter() {
    if(sounding) {
      if(!filtering) {
        filtering = true;
        player.addEffect(lpf);
        lpf.setFreq(Fc);
      } else
        removeFilter();
    }
  }
  /** Applique un filtre avec une fr\u00e9quence de coupure ajustable sur l'enregistrement de son choix
   * @param path Nom de l'extrait
   * @param fc fr\u00e9quence de coupure du filtre (entre 100 et 10000, sinon rien)
   */
  public void setFilter(String path, float Fc_) {
    if(sounding) {
      switchOff();
      if(filtering)
        removeFilter();
    } else if(signal1.sounding)
      signal1.switchOff();
    if(path != null) {
      count += 1;
      player = minim.loadFile(path);
      changeValue();
      player.loop();
      sounding = true;
      if((Fc_ > 100) && (Fc_ < 10000)) {
        player.addEffect(lpf);
        lpf.setFreq(Fc_);
        filtering = true;
      }
    }
  }
  public void removeFilter() {
    filtering = false;
    player.clearEffects();
  }
  public void changeValue() {
    volume = map(mouseY, 0, height, 0, -20);
    player.setGain(volume);
    if(filtering) {
      Fc = map(mouseX, 0, width, 500, 5000);
      lpf.setFreq(Fc);
    }
  }
  public void printV() {
    float vol = (volume + 20) / 20;
    fill(0);
    rect(0, height - 175, width / 2, 30);
    fill(myOr);
    text("Vol.: " + vol + " ", 10, height - 155);
    if(filtering) {
      fill(0);
      rect(0, height - 175, width / 2, 30);
      fill(myOr);
      text(" Freq. de coupure: " + Fc + " Hz  -  Vol.: " + vol + " ", 10, height - 155);
    }
  }
  public void switchOff() {
    player.pause();
    sounding = false;
  }
  public void ferme() {
    player.close();
  }
}
/** D\u00e9finit un signal sonore de forme pr\u00e9d\u00e9fini. */
class signal {
  /** Construction du signal et de son interaction graphique. */
  signal() {
    out.sound();
  }
  float volume;
  float frequence;
  SineWave sinus_;
  SquareWave square_;
  SawWave saw_;
  WhiteNoise wnoise_;

  String type;
  boolean sounding = false;

  /** Joue un signal de type choisi
   * @param n nom du type: sinus, square, triangle, saw, white noise.
   * @param f fr\u00e9quence du signal.
   * @param a amplitude du signal.
   */
  public void setSignal(String n, float f, float a) {
    type = n;
    if(sounding)
      switchOff();
    else if(record1.sounding)
      record1.switchOff();
    // Cr\u00e9er un oscillateur sinusoidale avec une fr\u00e9quence de 1000Hz, une amplitude de 1.0, et une fr\u00e9quence d'\u00e9chantillonage call\u00e9e sur la ligne out
    if(n.equals("sinus")) {
      sinus_ = new SineWave(f, a, out.sampleRate());
      sinus_.portamento(20);
      changeValue();
      out.addSignal(sinus_);
    } else if(n.equals("carr\u00e9")) {
      square_ = new SquareWave(f, a, out.sampleRate());
      square_.portamento(20);
      changeValue();
      out.addSignal(square_);
    } else if(n.equals("scie")) {
      saw_ = new SawWave(f, a, out.sampleRate());
      saw_.portamento(20);
      changeValue();
      out.addSignal(saw_);
    } else if(n.equals("bruit")) {
      wnoise_ = new WhiteNoise(a);
      changeValue();
      out.addSignal(wnoise_);
    }
    sounding = true;
  }
  /** Mise \u00e0 jour des valeurs lors du d\u00e9placement de la souris. */
  public void changeValue() {
    frequence = map(mouseX, 0, width, 100, 4000);
    // constrain(mouseX, 0, width-500);

    volume = map(mouseY, 0, height, 0.2f, 0);
    if(type.equals("sinus")) {
      sinus_.setFreq(frequence);
      sinus_.setAmp(volume);
    } else if(type.equals("carr\u00e9")) {
      square_.setFreq(frequence);
      square_.setAmp(volume);
    } else if(type.equals("scie")) {
      saw_.setFreq(frequence);
      saw_.setAmp(volume);
    } else if(type.equals("bruit"))
      wnoise_.setAmp(volume);
  }
  /** Affichage de la valeur dans l'interface. */
  public void printV() {
    float vol = (volume) / 0.4f;
    fill(0);
    rect(0, height - 175, width / 2, 30);
    fill(myOr);
    if(type.equals("bruit"))
      text("Vol.: " + vol + " ", 10, height - 155);
    else
      text(" Freq.: " + frequence + " Hz  -  Vol.: " + vol + " ", 10, height - 155);
  }
  /** Arr\u00eat de la sortie sonore. */
  public void switchOff() {
    out.noSound();
    out.clearSignals();
    sounding = false;
  }
}
  static public void main(String args[]) {
    PApplet.main(new String[] { "--bgcolor=#FFFFFF", "ExplorationSonore" });
  }
}
