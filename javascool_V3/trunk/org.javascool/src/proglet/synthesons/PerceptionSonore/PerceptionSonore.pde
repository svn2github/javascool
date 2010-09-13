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
  
  ControlP5 controlP5;
  ControlFont font;
  ControlWindow controlWindow;
  controlP5.Button boxA;
  Textfield boxSinus;
  Textfield boxFiltre;
  boolean isOpen;
  int buttonValue = 1;
  boolean msound = false;
  boolean mfiltre = false;
  boolean msine = false;
  
  // Outils de Minim
  AudioOutput out;
  SineWave sine;
  AudioPlayer player;
  LowPassSP lpf;
  
  // Paramètres pour la sinusoide, et l'enregistrement chargé
  String mySample;
  float volume;
  float freqSine = 0;
  float cutoff = 100;
  int count =0;
  
  // Paramètres de l'interface
  int w;
  PImage fade;
  float rWidth, rHeight;
  int myOr = color(255,100,0);
  int myRed = color(255,0,0);
  
  void setup() {
    
    frame = new Frame();
    
    //size(screen.width/2,2*screen.height/3,P3D);
    size(512,2*screen.height/3,P3D);
    frameRate(30);
    controlP5 = new ControlP5(this);
    PFont pfont = createFont("Courrier",10,true); // true/false pour smooth/no-smooth
    font = new ControlFont(pfont);
    frame.setLocation(screen.width/2,screen.height/6);
    this.frame.setTitle("A N A L Y S E   D U   C O N T E N U   F R E Q U E N T I E L");
    
    /////////////////////////////////////////////////
    // FFT: Transformation de Fourier pour l'analyse fréquentielle en temps réel
    
    minim = new Minim(this);
    in = minim.getLineIn(Minim.STEREO, 512);
    out = minim.getLineOut(Minim.STEREO);
    
    monAnalyseFFT();
    
    
    // Interface de manipulation: génère sinusoide, charge enregistrement, etc
    monInterface();
    
  }
  

  
  // Rendu
  void draw() {
    
    background(0);
    controlP5.draw();
    
    // Animation de l'info
    boxA.position().x += ((isOpen==true ? 0:-screen.width/2-3) - boxA.position().x) * 0.2;
    boxA.setWindow(controlWindow);
  
    //  Rendu de la FFT
    strokeWeight(10);
    tint(250, 250); //gris,alpha sinon (255, 150, 0, 250)
    image(fade, (width - rWidth) / 2, (height - rHeight) / 2, rWidth, rHeight);
    noTint();
    
    fft.forward(in.mix);
  
    stroke(240, 240, 240);
    for(int i = 0; i < fft.avgSize(); i++){
      line((i * w) + (w / 2), height, (i * w) + (w / 2), height - fft.getAvg(i) * 30);
    }
   
    fade = get(0, 0, width, height);
    
    stroke(250,70,0);
    for(int i = 0; i < fft.avgSize(); i++){
      line((i * w) + (w / 2), height, (i * w) + (w / 2), height - fft.getAvg(i) * 30);
    }
    
    // Draw the waveforms
    stroke(255);
    strokeWeight(1.5);  
      for(int i = 0; i < in.bufferSize() - 1; i++)
      {
        //println("buffer: " + in.bufferSize());
        float x1 = map(i, 0, in.bufferSize(), 0, width);
        float x2 = map(i+1, 0, in.bufferSize(), 0, width);
        line(x1, 40 + in.left.get(i)*100, x2, 40 + in.left.get(i+1)*100);
        line(x1, 120 + in.right.get(i)*100, x2, 120 + in.right.get(i+1)*100);
      }
        
  }
     
    
      // Lors de la fermeture du programme, arreter tout outil de Minim  
  void stop() {
    
      out.close();
      if (count!=0) {
      player.close();}
      minim.stop();
      
      super.stop();
  }
  
  

