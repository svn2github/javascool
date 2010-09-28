  /*///////////////////////////////////////////////////////////////////////////////////
  *
  * 08.2010 Cécile P-L for Fuscia - ccl.picard@gmail.com
  * PERCEPTION SONORE
  * Interface pédagogique sur les notions de contenu sonore et perception
  * Utilisation de Minim pour le son et ControlP5
  *
  *////////////////////////////////////////////////////////////////////////////////////
  
  
  import ddf.minim.*;
  import ddf.minim.signals.*;
  import ddf.minim.analysis.*;
  import ddf.minim.effects.*;
  import controlP5.*;
  
  
  Minim minim;
  AudioInput in;
  FFT fft;
  
  
  // Paramètres de l'interface
  ControlP5 controlP5;
  ControlFont font;
  ControlWindow controlWindow;
  controlP5.Button wInfo;

  int w;
  PImage fade;
  float rWidth, rHeight;
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
  signal signal1;
  record record1;


  // Ce qui est lancé une fois, au départ
  void setup() {
    
    frame = new Frame();
    
    size(800,512,P3D);
    frameRate(30);
    controlP5 = new ControlP5(this);
    PFont pfont = createFont("Courrier",10,true); // police de caractère
    font = new ControlFont(pfont);
    
    // Fenetre principale: celle de l'analyseur
    //frame.setLocation(screen.width/2,screen.height/6);
    this.frame.setTitle("A N A L Y S E   D U   C O N T E N U   F R E Q U E N T I E L");

    
    // Outils relatifs au son
    minim = new Minim(this);
    in = minim.getLineIn(Minim.STEREO, 512);
    out = minim.getLineOut(Minim.STEREO);

    
    // FFT: Transformation de Fourier pour l'analyse fréquentielle en temps réel
    launchFFT();
    
    // Interface de manipulation: génère sinusoide, charge enregistrement, etc
    launchInterface(512, 512);
    
    width_ = getWidthInterface();
    height_ = getHeightInterface();
    signal1 = new signal();
    record1 = new record();
   
  }
  

  // Ce qui est effectué tout au long de l'animation
  void draw() {
    
    background(0);
    controlP5.draw();
    
    // Fenetre informative
    openInfo();
  
    if (signal1.sonne) {
      
        signal1.drawFFT();
        signal1.drawSignal();
        
    } else if (record1.sonne) {
      
        record1.drawFFT();
        record1.drawSignal();
        
    } else {
    //  Trace la FFT
    drawFFT();
    
    // Trace le signal temporel
    drawSignal();
    }
         
  }
     
     
  void keyPressed()                                                  
  {
    
    if( key=='1' ) {
          
      signal1.addSignal("sine", 1000, 0.2);
      
    }
    
    if( key=='2' ) {
     
      signal1.addSignal("square", 1000, 0.2);
      
    }
    
    if( key=='3' ) {
     
      signal1.addSignal("saw", 1000, 0.2);
      
    }
    
    if( key=='4' ) {
     
      signal1.addSignal("noise", 1000, 0.2);
      
    }
    
    if( key=='e' ) {
         
      record1.addRecord("../data/music/Ahmed_Ex2.wav");
      
    }
    
    if( key=='s' ) {
     
      StopAnySound();
      
    }
  }
  
  
    
  // Lors de la fermeture du programme, arreter tout outil de Minim  
  void stop()
  {
    
      out.close();
      if (count!=0) {  
        record1.ferme();
      }
      minim.stop();
      
      super.stop();
  }
  
  

