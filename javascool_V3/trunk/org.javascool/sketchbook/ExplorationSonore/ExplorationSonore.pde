  /*///////////////////////////////////////////////////////////////////////////////////
   *
   * 08.2010 Cécile P-L for Fuscia - ccl.picard@gmail.com
   * PERCEPTION SONORE
   * Interface pédagogique sur les notions de contenu sonore et perception
   * Utilisation de Minim pour le son et ControlP5
   *
   *////////////////////////////////////////////////////////////////////////////////////
  
  import processing.opengl.*;
  import javax.media.opengl.*;
  
  import ddf.minim.*;
  import ddf.minim.signals.*;
  import ddf.minim.analysis.*;
  import ddf.minim.effects.*;
  import controlP5.*;
  
  
  Minim minim;
  AudioInput in;
  AudioPlayer player;
  FFT fft;
  
  
  // Paramètres de l'interface
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
  
  // Paramètres pour la sinusoide, et l'enregistrement chargé
  int count = 0;
  signal signal1, signal2;
  record record1;
  PFont f;
  
  int c = 0;
  
  // Ce qui est lancé une fois, au départ
  void setup() {
    frameRate(60);
    // Ces deux lignes permettent l'interface avec JavaScool
    proglet = this;
    frame = new Frame();
  
  
    f = createFont("ArialMT",14,true);
    //f= textFont(loadFont("ArialMT-48.vlw"),24);
    size(800, 600, P3D);//size(800,512);//,P3D);//OPENGL);
    //frameRate(30);
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
    signal2 = new signal();
    record1 = new record();
  
    // FFT: Transformation de Fourier pour l'analyse fréquentielle en temps réel
    launchFFT();
  
    // Interface de manipulation: génère sinusoide, charge enregistrement, etc
    Arrays.fill(((PGraphics3D)g).zbuffer,Float.MAX_VALUE);
    launchInterface();
  
    width_ = this.frame.getWidth();//getWidthInterface();
    height_ = this.frame.getHeight();//getHeightInterface();
  }
  
  
  // Ce qui est effectué tout au long de l'animation
  void draw() {
  
  
    //unhint(DISABLE_DEPTH_TEST);
    background(0);
    pushMatrix();
    if (signal1.sounding) {
      c+=1;
      fft = new FFT(out.bufferSize(), out.sampleRate());
      fft.logAverages(60,6*width/(640));
      drawFFT("out");                                               // Trace la FFT
      //drawSignal("out");                                            // Trace le signal temporel
      if((c)%2 == 0) {
        //println(c);
        drawSignal("out");
      }
    } 
    else if (record1.sounding) {
      fft = new FFT(player.bufferSize(), player.sampleRate());
      
      fft.logAverages(60,6*width/(640));
      drawFFT("player");
      drawSignal("player");
    } 
    else {
      fft = new FFT(in.bufferSize(), in.sampleRate());
      fft.logAverages(60,6*width/(640));
      drawFFT("in");
      drawSignal("in");
    }
    popMatrix();
  
    //hint(DISABLE_DEPTH_TEST);
    //hint(ENABLE_NATIVE_FONTS);
    //textMode(SCREEN);
    //textMode(SHAPE);
    Arrays.fill(((PGraphics3D)g).zbuffer,Float.MAX_VALUE);
    controlP5.draw();
    text(boxValue.getText(),width-300,2*height/3+height/6);
    // Fenetre informative
    openInfo();
    //textMode(MODEL);
  }
  
  
  void keyPressed()                                                  
  {
    if (key == '0') {
      signal1.setSignal("sine", 1000, 0.2);
    }
    if (key == '1') {
      signal1.setSignal("sine", 1000, 0.2);
      signal2.setSignal("sine", 4000, 0.2);
    }
    if (key == '2') {
      signal1.setSignal("square", 1000, 0.2);
    }
    if (key == '3') {
      signal1.setSignal("saw", 1000, 0.2);
    }
    if (key == '4') {
      signal1.setSignal("noise", 1000, 0.2);
    }
    if (key == 'e') {
      record1.setRecord("../data/music/Ahmed_Ex2.wav");
    }
    if ( key=='f') {
      record1.setFilter("../data/music/Ahmed_Ex2.wav", 500);
    }
    if ( key=='s') {
      StopAnySound();
    }
  }
  
  // Lors de la fermeture du programme, arreter tout outil de Minim  
  void stop()
  {
    out.close();
    if (count != 0) {  
      record1.ferme();
    }
    minim.stop();
    super.stop();
  }

