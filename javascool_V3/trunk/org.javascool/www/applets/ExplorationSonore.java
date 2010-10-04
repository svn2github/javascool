import processing.core.*; 
import processing.xml.*; 

import processing.opengl.*; 
import javax.media.opengl.*; 
import ddf.minim.*; 
import ddf.minim.signals.*; 
import ddf.minim.analysis.*; 
import ddf.minim.effects.*; 
import controlP5.*; 

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

  /*///////////////////////////////////////////////////////////////////////////////////
   *
   * 08.2010 C\u00e9cile P-L for Fuscia - ccl.picard@gmail.com
   * PERCEPTION SONORE
   * Interface p\u00e9dagogique sur les notions de contenu sonore et perception
   * Utilisation de Minim pour le son et ControlP5
   *
   *////////////////////////////////////////////////////////////////////////////////////
    
  
  
  
  
  
  
  
  
  
    
  Minim minim;
  AudioInput in;
  AudioPlayer player;
  FFT fft;
  
    
  // Param\u00e8tres de l'interface
  ControlP5 controlP5;
  ControlFont font;
  //ControlWindow controlWindow;
  controlP5.Button wInfo;
  Textfield boxValue;
  
  int w;
  PImage fade;
  float rWidth, rHeight;
  Frame frame;
  int myOr = color(255,100,0);
  int myRed = color(255,0,0);
  int myBlue = color(100,100,255);
  int width_;
  int height_;
  boolean isOpen;
  int buttonValue = 1;
  
    
  // Outils pour le son
  AudioOutput out;
  
  // Param\u00e8tres pour la sinusoide, et l'enregistrement charg\u00e9
  int count = 0;
  signal signal1;
  record record1;
  PFont f;
  
  
  // Ce qui est lanc\u00e9 une fois, au d\u00e9part
  public void setup() {
    frameRate(60);
    // Ces deux lignes permettent l'interface avec JavaScool
    proglet = this;
    frame = new Frame();
  
    f = createFont("Arial Bold",12,true);
    size(800, 600);//size(800,512);//,P3D);//OPENGL);
    frameRate(30);
    controlP5 = new ControlP5(this);
    font = new ControlFont(f);
    
    // Fenetre principale: celle de l'analyseur
    //frame.setLocation(screen.width/2,screen.height/6);
    this.frame.setTitle("A N A L Y S E   D U   C O N T E N U   F R E Q U E N T I E L");
    
    
    // Outils relatifs au son
    minim = new Minim(this);
    in = minim.getLineIn(Minim.STEREO, 512);
    out = minim.getLineOut(Minim.STEREO);
    
    signal1 = new signal();
    record1 = new record();
    
    // FFT: Transformation de Fourier pour l'analyse fr\u00e9quentielle en temps r\u00e9el
    launchFFT();
    
    // Interface de manipulation: g\u00e9n\u00e8re sinusoide, charge enregistrement, etc
    launchInterface();
    
    width_ = this.frame.getWidth();//getWidthInterface();
    height_ = this.frame.getHeight();//getHeightInterface();
    
  }
    
  
  // Ce qui est effectu\u00e9 tout au long de l'animation
  public void draw() {
    
    background(0);
    controlP5.draw();
    
    // Fenetre informative
    openInfo();
   
    if (signal1.sounding) {
      fft = new FFT(out.bufferSize(), out.sampleRate());
      fft.logAverages(60,6*width/(screen.width/2));
      drawFFT("out");                                               // Trace la FFT
      drawSignal("out");                                            // Trace le signal temporel
    } else if (record1.sounding) {
      fft = new FFT(player.bufferSize(), player.sampleRate());
      fft.logAverages(60,6*width/(screen.width/2));
      drawFFT("player");
      drawSignal("player");
    } else {
      fft = new FFT(in.bufferSize(), in.sampleRate());
      fft.logAverages(60,6*width/(screen.width/2));
      drawFFT("in");
      drawSignal("in");
    }
  }
       
       
  public void keyPressed()                                                  
  {
    if (key == '1') {
      signal1.setSignal("sine", 1000, 0.2f);
    }
    if (key == '2') {
      signal1.setSignal("square", 1000, 0.2f);
    }
    if (key == '3') {
      signal1.setSignal("saw", 1000, 0.2f);
    }
    if (key == '4') {
      signal1.setSignal("noise", 1000, 0.2f);
    }
    if (key == 'e') {
      record1.setRecord("data/music/Ahmed_Ex2.wav");
    }
    if ( key=='f') {
      record1.setFilter("data/music/Ahmed_Ex2.wav", 500);
    }
    if ( key=='s') {
      StopAnySound();
    }
  }
    
  // Lors de la fermeture du programme, arreter tout outil de Minim  
  public void stop()
  {
    out.close();
    if (count != 0) {  
      record1.ferme();
    }
    minim.stop();
    super.stop();
  }
    
  /* Fonctions relatives \u00e0 l'interface. */
  
  
  public void launchInterface() {
  
    // Interface de manipulation: g\u00e9n\u00e8re sinusoide, charge enregistrement, etc
  
  strokeWeight(0.5f);  
    String[] ListN = {
      "sinusoide", "carr\u00e9", "scie", "bruit"
    };
    MultiList l = controlP5.addMultiList("myList", 50,2*height/3+height/10,300,35);
    MultiListButton b;
    b = l.add("  Jouer  un  signal",1);
    b.captionLabel().setControlFont(font);
    b.captionLabel().setControlFontSize(10);
    for(int i=0;i<4;i++) {
      MultiListButton c = b.add("signal"+(i+1),i+1);
      c.captionLabel().setControlFont(font);
      c.setLabel("  "+ListN[i]+"  ");
      c.captionLabel().setControlFontSize(10);
      c.setHeight(17);
      c.setWidth(80);
    }
    
    boxValue = controlP5.addTextfield("  ",width-50-300,2*height/3+height/7,300,20);
    boxValue.setText(" ");
  
  
    // Charger un enregistrement
    controlP5.addButton("PlayRecord",0,50,2*height/3+2*height/10+15,300,35);
    controlP5.controller("PlayRecord").setCaptionLabel("  Jouer  un  enregistrement  sonore");
    controlP5.controller("PlayRecord").captionLabel().setControlFont(font); // change the font
    controlP5.controller("PlayRecord").captionLabel().setControlFontSize(10);
    
    //record1.boxValue = controlP5.addTextfield("  ",width-(50+281),2*height/3+2*height/10,280,19);
    
  
    
    // Filtre Basse Fr\u00e9quence applicable sur l'enregistrement
    controlP5.addButton("FilterRecord",0,50+300+1,2*height/3+2*height/10+15,80,35);
    controlP5.controller("FilterRecord").setCaptionLabel("   FILTRAGE"); // - Fr\u00e9quence de coupure reglable
    controlP5.controller("FilterRecord").captionLabel().setControlFont(font); // change the font
    controlP5.controller("FilterRecord").captionLabel().setControlFontSize(10);
    controlP5.controller("FilterRecord").captionLabel().toUpperCase(false);
  
  
    // Stopper tout son
    controlP5.addButton("StopAnySound",0,width-50-70,height-50,70,35);
    controlP5.controller("StopAnySound").setCaptionLabel("  S  t  o  p");
    controlP5.controller("StopAnySound").captionLabel().setControlFont(font); // change the font
    controlP5.controller("StopAnySound").captionLabel().setControlFontSize(11);
    controlP5.controller("StopAnySound").setColorBackground(myRed);
    controlP5.controller("StopAnySound").setColorActive(myOr);
  
  
    
     // Info
    controlP5.addButton("info",10,4,2*height/3+25,60,15).setId(1);
    controlP5.controller("info").setCaptionLabel("Info"); // change content
    controlP5.controller("info").captionLabel().setControlFont(font); // change the font
    controlP5.controller("info").captionLabel().setControlFontSize(10);
    controlP5.controller("info").setColorActive(myOr); 
    controlP5.controller("info").setColorBackground(myOr); 
    wInfo = controlP5.addButton("buttonValue",0,-width,2*height/3+25+15,0,50);
    wInfo.setId(2);
    wInfo.setWidth(width-10);
    wInfo.setHeight(height/3-50);
    wInfo.setColorActive(255); 
    wInfo.setColorBackground(255); 
    wInfo.setColorLabel(myOr);
    wInfo.captionLabel().setControlFont(font);
    wInfo.captionLabel().setControlFontSize(12);
    wInfo.captionLabel().toUpperCase(false);
    wInfo.captionLabel().set(
    "Parles, siffles, chuchotes.., et tu verras ce qui se passe sur l'analyseur de contenu sonore (\u00e0 droite).. \n \n"+
      "Tu peux aussi jouer une signal ou un enregistrement de ton choix. \n \n"+
      "Pour ajuster la fr\u00e9quence et l'amplitude du signal, bouges la souris sur la fenetre de l'analyseur. \n \n"+
      "Pour faire varier le volume de l'enregistrement sonore, tu peux proc\u00e9der pareillement, \n \n"+
      "tandis que le contenu fr\u00e9quentiel peut s'ajuster par un filtre. Exp\u00e9rimentes! \n \n" );
    wInfo.captionLabel().style().marginLeft = 100;
    wInfo.captionLabel().style().marginTop = -height/12;
    
  }
  
  public void controlEvent(ControlEvent theEvent) {
    //println(theEvent.controller().name()+" = "+theEvent.value());  
  
    if(theEvent.value()==1) {
      signal1.setSignal("sine", 1000, 0.2f);
    } 
    else if(theEvent.value()==2) {
      signal1.setSignal("square", 1000, 0.2f);
    } 
    else if(theEvent.value()==3) {
      signal1.setSignal("saw", 1000, 0.2f);
    } 
    else if(theEvent.value()==4) {
      signal1.setSignal("noise", 1000, 0.2f);
    }
    // uncomment the line below to remove a multilist item when clicked.
    // theEvent.controller().remove();
  }
  
  
  public void PlayRecord() {
    record1.setRecord(selectInput());
  }
  
  public void FilterRecord() {
    record1.applyFilter() ;
  }
  
  public void mouseMoved() {
    if (signal1.sounding) {
      signal1.changeValue();
      signal1.printV();
    } 
    else if (record1.sounding) {
      record1.changeValue();
      record1.printV();
    }
  }
  
  
  /** Stop tout son  */
  public void StopAnySound() {
    if (signal1.sounding) {
      signal1.switchOff();
    } 
    else if (record1.sounding) {
      record1.switchOff();
    }
  }
  
  
  // Lance l'analyse spectrale
  public void launchFFT() {
    fft = new FFT(in.bufferSize(), in.sampleRate());
  
    stroke(0);
  
    fft.logAverages(60,6);///(screen.width/2)); //6 pour screen.width/2
  
      w = width/fft.avgSize();
    strokeWeight(w);
    strokeCap(SQUARE);
  
    background(0);
    fade = get(0, 0, width, height);//fade = get(0, 0, 100, 100);
  
    rWidth = width * 0.99f;
    rHeight = height * 0.99f;
  }
  
  
  
  /** Trac\u00e9 3D du spectre au fil du temps. */
  public void drawFFT(String n) {
  
    String type = n;
    float factor;
    strokeWeight(10);
    tint(250, 250); //gris,alpha sinon (255, 150, 0, 250)
    image(fade, (width - rWidth) / 2, (height - rHeight) / 2, rWidth, rHeight);
    noTint();
  
    if(n.equals("out")) {
      fft.forward(out.mix);
      factor = (signal1.volume+20);
    } 
    else if(n.equals("player")) {
      fft.forward(player.mix);
      factor = (record1.volume+20);
    } 
    else {
      fft.forward(in.mix);
      factor = 20;
    }
  
  
    stroke(240, 240, 240);
    for(int i = 0; i < fft.avgSize(); i++) {
      line((i * w) + (w / 2), 2*height/3, (i * w) + (w / 2), 2*height/3 - fft.getAvg(i) * factor);
    }
  
    fade = get(0, 0, width, height);
  
    stroke(250,70,0);
    textFont(f,14);
    text("                  100     125                  250                    500                 1000                2000                4000                      8000 Hz", 0,2*height/3+height/30);
    for(int i = 0; i < fft.avgSize(); i++) {
      line((i * w) + (w / 2), 2*height/3, (i * w) + (w / 2), 2*height/3 - fft.getAvg(i) * factor);
    }
    strokeWeight(0.5f);
  }
  
  
  /** Trac\u00e9 temporel du signal. */
  public void drawSignal(String n) {
  
    stroke(255);
    strokeWeight(1);  
    int k; 
    if(n.equals("out")) {
      k = out.bufferSize();
    } 
    else if(n.equals("player")) {
      k = player.bufferSize();
    } 
    else {
      k = in.bufferSize();
    }
    for(int i = 0; i < k - 1; i++) {
      //println("buffer: " + in.bufferSize());
  
      float x1 = map(i, 0, k, 0, width);
      float x2 = map(i+1, 0, k, 0, width);
      if(n.equals("out")) {
        line(x1, 40 + out.left.get(i)*(signal1.volume+20)*5, x2, 40 + out.left.get(i+1)*(signal1.volume+20)*5);
        line(x1, 120 + out.right.get(i)*(signal1.volume+20)*5, x2, 120 + out.right.get(i+1)*(signal1.volume+20)*5);
      } 
      else if(n.equals("player")) {
        line(x1, 40 + player.left.get(i)*(record1.volume+20)*3, x2, 40 + player.left.get(i+1)*(record1.volume+20)*3);
        line(x1, 120 + player.right.get(i)*(record1.volume+20)*3, x2, 120 + player.right.get(i+1)*(record1.volume+20)*3);
      } 
      else {
        line(x1, 40 + in.left.get(i)*100, x2, 40 + in.left.get(i+1)*100);
        line(x1, 120 + in.right.get(i)*100, x2, 120 + in.right.get(i+1)*100);
      }
    }
    strokeWeight(0.5f);
  }
  
  
  // Controlleur pour l'info    
  public void info(float theValueA) {
    isOpen = !isOpen;
    controlP5.controller("info").captionLabel().setControlFontSize(8);
    controlP5.controller("info").setCaptionLabel((isOpen==true) ? "fermer Info":"voir Info");
  }
  
  public void openInfo() {
    wInfo.position().x += ((isOpen==true ? 5:-width+5) - wInfo.position().x) * 0.2f;
    //wInfo.setWindow(controlWindow);
  }
  
  
  // Fonctions pour l'insertion dans javascool
  /*int getWidthInterface() {
    return controlWindow.papplet().getWidth();
  }
  
  
  int getHeightInterface() {
    return controlWindow.papplet().getHeight();
  }*/
  
  
  /** Utilis\u00e9 pour fermer la fen\u00eatre secondaire de l'interface, par JavaScool. */
  /*public processing.core.PApplet getControl() {
    controlWindow.hide();
    return controlWindow.papplet();
  }*/
  
  /** Joue un signal de type choisi  
   * @param n nom du type: sinus, square, triangle, saw, white noise.
   * @param f fr\u00e9quence du signal. 
   * @param a amplitude du signal. 
   */
  public static void playSignal(String n, float f, float a) {
    proglet.signal1.setSignal(n, f, a);
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
  public static void setFilter(String path, float fc) {
    proglet.record1.setFilter(path, fc);
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
      //boxValue = controlP5.addTextfield("  ",width-280-50,2*height/3+2*height/10+10,280,39);
      //boxValue.setText(" ");
      //boxValue.setWindow(controlWindow);
      lpf = new LowPassSP(100, out.sampleRate());
    }
    
    float volume;
    float Fc = 100; 
    //String monExtrait;
  
    LowPassSP lpf;
  
    boolean sounding = false;
    boolean filtering = false;
    //Textfield boxValue;
      
    /** Joue un enregistrement de son choix
    * @param path Nom de l'extrait
    */ 
    public void setRecord(String path) {
     if(sounding) {
       switchOff();
       if(filtering) {
         removeFilter();
       }
     } else if(signal1.sounding) {
       signal1.switchOff();
     }
  
     if (path != null) {
        count += 1;
        player = minim.loadFile(path);
        player.loop();
        sounding = true;
      }
    }
    
    public void applyFilter() {
      if (sounding)  {
         if (!filtering) {
           filtering = true;
           player.addEffect(lpf);
           lpf.setFreq(Fc);
         } else {
           removeFilter();
         }
       }
    }
    
    /** Applique un filtre avec une fr\u00e9quence de coupure ajustable sur l'enregistrement de son choix 
    * @param path Nom de l'extrait
    * @param fc fr\u00e9quence de coupure du filtre (entre 100 et 10000, sinon rien)
    */
    
    public void setFilter(String path, float Fc_) {
     
      if(sounding) {
        switchOff();
         if(filtering) {
           removeFilter();
         }
      } else if(signal1.sounding) {
         signal1.switchOff();
      }
      
      if (path != null) {
        count += 1;
        player = minim.loadFile(path);
        player.loop();
        sounding = true;
        if (Fc_>100 && Fc_<10000) {
          player.addEffect(lpf);
          lpf.setFreq(Fc_);
          filtering = true;
        }
      }
      
    }
    
    
    public void removeFilter() {    
      deleteI();
      filtering = false;
      player.clearEffects();
      
    }
    
    
    public void changeValue() {
      volume = map(mouseY, 0, height, 0, -20);
      player.setGain(volume);
      if (filtering) {
        Fc = map(mouseX, 0, width, 500, 5000);
        lpf.setFreq(Fc);
      
      }
    }
    
   
    
    public void printV() {
      
      float vol = (volume+20)/20;
      boxValue.captionLabel().setControlFontSize(8);
      boxValue.setText(" Vol.: " + vol + " ");
      boxValue.captionLabel().setControlFont(font); 
      //boxValue.setWindow(controlWindow);
      
      if(filtering) {
        
        boxValue.setText(" Freq. de coupure: " + Fc + " Hz  -  Vol.: " + vol + " ");
        boxValue.captionLabel().setControlFont(font); 
        //boxValue.setWindow(controlWindow);
      
      }
    }
    
    
    public void deleteI() {
      boxValue.setText(" ");
      //boxValue.setWindow(controlWindow);
    }
    
    
    public void switchOff() {
      player.pause();
      deleteI();
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
      //boxValue = controlP5.addTextfield("  ",width-280-50,2*height/3+height/12,280,39);
      //boxValue.setText(" ");
      //boxValue.setWindow(controlWindow);
    }
    float volume;
    float frequence; 
    SineWave sinus_; 
    SquareWave square_;
    SawWave saw_;
    WhiteNoise wnoise_;
  
    String type;
    boolean sounding = false;
    //Textfield boxValue; 
    
    
    /** Joue un signal de type choisi  
     * @param n nom du type: sinus, square, triangle, saw, white noise.
     * @param f fr\u00e9quence du signal. 
     * @param a amplitude du signal. 
     */  
    public void setSignal(String n, float f, float a) {
      type = n;
      if(sounding) switchOff();
      else if(record1.sounding) record1.switchOff();
      // Cr\u00e9er un oscillateur sinusoidale avec une fr\u00e9quence de 1000Hz, une amplitude de 1.0, et une fr\u00e9quence d'\u00e9chantillonage call\u00e9e sur la ligne out
      if(n.equals("sine")) {
        sinus_ = new SineWave(f, a, out.sampleRate());
        sinus_.portamento(20);
        out.addSignal(sinus_);
      } else if(n.equals("square")) {
        square_ = new SquareWave(f, a, out.sampleRate());
        square_.portamento(20);
        out.addSignal(square_);
      } else if(n.equals("saw")) {
        saw_ = new SawWave(f, a, out.sampleRate());
        saw_.portamento(20);
        out.addSignal(saw_);
      } else if(n.equals("noise")) {
        wnoise_ = new WhiteNoise(a);
        out.addSignal(wnoise_);
      }     
      sounding = true;
    }
    
    /** Mise \u00e0 jour des valeurs lors du d\u00e9placement de la souris. */
    public void changeValue() {
      frequence = map(mouseX, 0, width, 100, 4000);
      //constrain(mouseX, 0, width-500);
      
      volume = map(mouseY, 0, height, 0.2f, 0); 
      if(type.equals("sine")) {
        println("freq: " + frequence);
        sinus_.setFreq(frequence);
        sinus_.setAmp(volume);
      } else if(type.equals("square")){
        square_.setFreq(frequence);
        square_.setAmp(volume);
      } else if(type.equals("saw")){
        saw_.setFreq(frequence);
        saw_.setAmp(volume);
      } else if(type.equals("noise")){
        wnoise_.setAmp(volume);
      }
    }
    
    /** Affichage de la valeur dans l'interface. */  
    public void printV() {
      float vol = (volume)/0.4f;
      boxValue.captionLabel().setControlFontSize(8);
      if(type.equals("noise")) {
        boxValue.setText("Vol.: " + vol + " ");
      } else {
        boxValue.setText(" Freq.: " + frequence + " Hz  -  Vol.: " + vol + " ");
      }
      boxValue.captionLabel().setControlFont(font); // change the font
      //boxValue.setWindow(controlWindow);
    }
    
    /** Effacement de l'affichage. */
    public void deleteI() {
      boxValue.setText(" ");
      //boxValue.setWindow(controlWindow);
    }
    
    /** Arr\u00eat de la sortie sonore. */
    public void switchOff() {
      out.noSound();
      out.clearSignals();
      deleteI();
      sounding = false;
      
    }
  }
  static public void main(String args[]) {
    PApplet.main(new String[] { "--bgcolor=#DFDFDF", "ExplorationSonore" });
  }
}
