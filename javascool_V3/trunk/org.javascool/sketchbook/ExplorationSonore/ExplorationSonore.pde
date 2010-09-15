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
  controlP5.Button fenetreInfo;

  int w;
  PImage fade;
  float rWidth, rHeight;
  int myOr = color(255,100,0);
  int myRed = color(255,0,0);
  int largeur_;
  int hauteur_;
  boolean isOpen;
  int buttonValue = 1;

  
  // Outils pour le son
  AudioOutput out;
  SineWave sine;
  // Paramètres pour la sinusoide, et l'enregistrement chargé
  int count =0;
  sinusoide maSinusoide;
  enregistrement monEnregistrement;


  // Ce qui est lancé une fois, au départ
  void setup() {
    
    frame = new Frame();
    
    size(1024,2*screen.height/3,P3D);
    frameRate(30);
    controlP5 = new ControlP5(this);
    PFont pfont = createFont("Courrier",10,true); // police de caractère
    font = new ControlFont(pfont);
    
    // Fenetre principale: celle de l'analyseur
    frame.setLocation(screen.width/2,screen.height/6);
    this.frame.setTitle("A N A L Y S E   D U   C O N T E N U   F R E Q U E N T I E L");

    
    // Outils relatifs au son
    minim = new Minim(this);
    in = minim.getLineIn(Minim.STEREO, 512);
    out = minim.getLineOut(Minim.STEREO);

    
    // FFT: Transformation de Fourier pour l'analyse fréquentielle en temps réel
    monAnalyseFFT();
    
    
    // Interface de manipulation: génère sinusoide, charge enregistrement, etc
    monInterface(512, 512);
    
    largeur_ = InterfaceLargeur();
    hauteur_ = InterfaceHauteur();
    maSinusoide = new sinusoide();
    monEnregistrement = new enregistrement();
    
  }
  

  
  // Ce qui est effectué tout au long de l'animation
  void draw() {
    
    background(0);
    controlP5.draw();
    
    // Fenetre informative
    ouvreFenetreInfo();
  
    //  Trace la FFT
    traceFFT();
    
    // Trace le signal temporel
    traceSignal();
         
  }
     
    
  // Lors de la fermeture du programme, arreter tout outil de Minim  
  void stop() {
    
      out.close();
      if (count!=0) {
      //player.close();
      monEnregistrement.ferme();}
      minim.stop();
      
      super.stop();
  }
  
  

