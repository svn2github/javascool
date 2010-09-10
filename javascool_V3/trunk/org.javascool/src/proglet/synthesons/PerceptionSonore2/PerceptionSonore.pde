  /*///////////////////////////////////////////////////////////////////////////////////
  *
  * 08.2010 Cécile P-L for Fuscia
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
  Textfield boxFreq;
  boolean isOpen;
  int buttonValue = 1;
  boolean msound = false;
  boolean msine = false;
  
  // Outils de Minim
  AudioOutput out;
  SineWave sine;
  AudioPlayer player;
  LowPassSP lpf;
  
  // Paramètres pour la sinusoide, et l'enregistrement chargé
  //float VolS = 1.0;
  //int FreqS = 1000;
  //float Vol = 1.0;
  String mySample;
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
  
    size(screen.width/2,2*screen.height/3,P3D);
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
    fft = new FFT(in.bufferSize(), in.sampleRate());
    
    stroke(0);
  
    fft.logAverages(60,7);
  
    w = width/fft.avgSize();
    strokeWeight(w);
    strokeCap(SQUARE);
    
    background(0);
    fade = get(0, 0, width, height);
    
    rWidth = width * 0.99;
    rHeight = height * 0.99; 
    
    
    /////////////////////////////////////////////////
    // Interface de manipulation: génère sinusoide, charge enregistrement, etc
    
    controlWindow = controlP5.addControlWindow(" I N T E R F A C E ",0,screen.height/6,screen.width/2,2*screen.height/3);
    controlWindow.hideCoordinates();
    controlWindow.setBackground(color(255));
    
    // Info
    controlP5.addButton("InfoA",10,0,0,70,15).setId(1);
    controlP5.controller("InfoA").setCaptionLabel("Info"); // change content
    controlP5.controller("InfoA").captionLabel().setControlFont(font); // change the font
    controlP5.controller("InfoA").captionLabel().setControlFontSize(10);
    controlP5.controller("InfoA").setColorActive(myOr); 
    controlP5.controller("InfoA").setColorBackground(myOr); 
    controlP5.controller("InfoA").setWindow(controlWindow);
    boxA = controlP5.addButton("buttonValue",0,-screen.width/2,20,60,50);
    boxA.setId(2);
    boxA.setWidth(screen.width/2);
    boxA.setHeight(100);
    boxA.setColorActive(255); 
    boxA.setColorBackground(255); 
    boxA.setColorLabel(myOr);
    boxA.captionLabel().setControlFont(font);
    boxA.captionLabel().setControlFontSize(12);
    boxA.captionLabel().toUpperCase(false);
    boxA.captionLabel().set(//"Notre but est de te faire découvrir les subtilités des sons. A droite se trouve un analyseur de contenu sonore. \n \n"+
    "Parles, siffles, chuchotes.., et tu verras ce qui se passe sur l'analyseur de contenu sonore (à droite).. \n \n"+
    "Tu peux aussi jouer une sinusoide ou un enregistrement issu d'une bibliothèque d'extraits. \n \n"+
    "Pour ajuster la fréquence et l'amplitude de la sinusoide, bouges la souris sur la fenetre de l'analyseur. \n \n"+
    "Pour ajuster le volume de l'enregistrement sonore, tu peux procéder de la meme manière, \n \n"+
    "tandis que le contenu fréquentiel peut etre réglé par un filtre. Expérimentes! \n \n" );
    boxA.captionLabel().style().marginLeft = 20;
    boxA.captionLabel().style().marginTop = -30;
    
  
    boxFreq = controlP5.addTextfield(" ",50,screen.height/4+5,220,20);
    boxFreq.setText(  "  ");
    boxFreq.setWindow(controlWindow);
    
    // Générer une Sinusoide
    controlP5.addButton("mSine",0,50,screen.height/4-40,220,40);
    controlP5.controller("mSine").setCaptionLabel(" Jouer une sinusoide");
    controlP5.controller("mSine").captionLabel().setControlFont(font); // change the font
    controlP5.controller("mSine").captionLabel().setControlFontSize(10);
    controlP5.controller("mSine").setWindow(controlWindow);
    // Une ligne de sortie pour la sinusoide émise, 
    // les paramètres par défaut sont: 1024 pour la taille d ebuffer, 44100 pour la fréquence d'échantillonage, 16 bit depth.
    out = minim.getLineOut(Minim.STEREO);
    
    // Charger un enregistrement
    controlP5.addButton("mPlaySound",0,50,screen.height/4+70,220,40);
    controlP5.controller("mPlaySound").setCaptionLabel(" Jouer un enregistrement sonore");
    controlP5.controller("mPlaySound").captionLabel().setControlFont(font); // change the font
    controlP5.controller("mPlaySound").captionLabel().setControlFontSize(10);
    controlP5.controller("mPlaySound").setWindow(controlWindow);
    
    // Filtre Basse Fréquence applicable sur l'enregistrement
    lpf = new LowPassSP(100, out.sampleRate());
    controlP5.addButton("mFilter",0,screen.width/2-(50+220),screen.height/4+70,220,30);
    controlP5.controller("mFilter").setCaptionLabel(" FILTRAGE - Fréquence de coupure reglable");
    controlP5.controller("mFilter").captionLabel().setControlFont(font); // change the font
    controlP5.controller("mFilter").captionLabel().setControlFontSize(10);
    controlP5.controller("mFilter").captionLabel().toUpperCase(false);
    controlP5.controller("mFilter").setWindow(controlWindow);
    controlP5.addSlider("sFreq",500,5000,600,screen.width/2-(50+220),screen.height/4+105,220,20);
    controlP5.controller("sFreq").captionLabel().setControlFont(font); // change the font
    controlP5.controller("sFreq").captionLabel().setControlFontSize(10);
    controlP5.controller("sFreq").setColorActive(myOr); 
    //controlP5.controller("sFreq").setColorValue(myOr);
    controlP5.controller("sFreq").setColorForeground(myOr);
    //controlP5.controller("sFreq").setColorBackground(0); 
    controlP5.controller("sFreq").setColorLabel(myOr);
    controlP5.controller("sFreq").captionLabel().toUpperCase(false);
    controlP5.controller("sFreq").setCaptionLabel(" (Hz)");
    controlP5.controller("sFreq").setWindow(controlWindow);
    
    // Stopper tout son
    controlP5.addButton("mStopSound",0,screen.width/2-(50+70),screen.height/4+100+(screen.height/2-(screen.height/4+20))/2,70,40);
    controlP5.controller("mStopSound").setCaptionLabel("  S  t  o  p");
    controlP5.controller("mStopSound").captionLabel().setControlFont(font); // change the font
    controlP5.controller("mStopSound").captionLabel().setControlFontSize(11);
    controlP5.controller("mStopSound").setColorBackground(myRed);
    controlP5.controller("mStopSound").setColorActive(myOr);
    controlP5.controller("mStopSound").setWindow(controlWindow);
    
  }
  
  // Informations rendus par les controlleurs      
  void controlEvent (ControlEvent theEvent) {
  
    cutoff = (int)(theEvent.controller().value());
    //print("La fréquence de coupure du filtre est à " + cutoff + "\n");  
    lpf.setFreq(cutoff);
    
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
    strokeWeight(3);  
    if (msine) {
    for(int i = 0; i < out.bufferSize() - 1; i++)
      {
        //println("buffer: " + out.bufferSize());
        float x1 = map(i, 0, out.bufferSize(), 0, width);
        float x2 = map(i+1, 0, out.bufferSize(), 0, width);
        line(x1, 40 + out.left.get(i)*30, x2, 40 + out.left.get(i+1)*30);
        line(x1, 120 + out.right.get(i)*30, x2, 120 + out.right.get(i+1)*30);
      }
    } else if (msound) {
      
      for (int i = 0; i < player.bufferSize() - 1; i++)
      {
        float x1 = map(i, 0, player.bufferSize(), 0, width);
        float x2 = map(i+1, 0, player.bufferSize(), 0, width);
        line(x1, 40 + player.left.get(i)*30, x2, 40 + player.left.get(i+1)*30);
        line(x1, 120 + player.right.get(i)*30, x2, 120 + player.right.get(i+1)*30);
      }
    } else {
      
      for(int i = 0; i < in.bufferSize() - 1; i++)
      {
        //println("buffer: " + in.bufferSize());
        float x1 = map(i, 0, in.bufferSize(), 0, width);
        float x2 = map(i+1, 0, in.bufferSize(), 0, width);
        line(x1, 40 + in.left.get(i)*50, x2, 40 + in.left.get(i+1)*50);
        line(x1, 120 + in.right.get(i)*50, x2, 120 + in.right.get(i+1)*50);
      }
      
    }

        
  }
      
  // Controlleur pour la sinusoide ou oscillateur   
  void mSine() {
    
      if (msine) { // si une sinusoide joue, on ne peut en générer une par-dessus
        println(" Une sinusoide est déjà en train de jouer!"); 
      } else {
      out.sound();
      
      // Créer un oscillateur sinusoidale avec une fréquence de 1000Hz, une amplitude de 1.0, et une fréquence d'échantillonage callée sur la ligne out
      sine = new SineWave(1000, 1.0, out.sampleRate());
      // la vitesse portamento pour l'oscillateur est réglée à 20 millisecondess
      sine.portamento(20);
      // ajouter le signal à la ligne de sortie
      out.addSignal(sine);
      msine = true;
      }      
  }
      
  // Controlleur pour l'info    
  public void InfoA(float theValueA) {
      //println("a button event. "+theValueA);
      isOpen = !isOpen;
      controlP5.controller("InfoA").setCaptionLabel((isOpen==true) ? "fermer Info":"voir Info");
  }
      
  void mPlaySound() {
    
      if (msound) { // si un enregistrement joue, on ne peut en charger un par-dessus
        println(" Un enregistrement est déjà en train de jouer. Veuillez le stopper avant de charger un nouveau morceau.");
      } else {
      
      count +=1;
      
      mySample = selectInput(); 
      player = minim.loadFile(mySample);
      player.loop();
      msound = true;
      }
  
  }
     
  // Stopper tout son  
  void mStopSound() {
      
    if (msine) {
      out.noSound();
      out.clearSignals();
      boxFreq.setText(  "  ");
      boxFreq.setWindow(controlWindow);
      msine = false;
    } else if (msound) {
      player.pause();
      msound = false;
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
    
  void mouseMoved()
  {
    if (msine) {
    // Régler les déplacements de la souris sur les paramètres de la sinusoide
    float freqSine = map(mouseX, 0, width, 100, 8000);
    //print("La fréquence de la sinusoide est: " + freqSine );  
    // Fréquence
    sine.setFreq(freqSine);
    // Volume
    float volSine = map(mouseY, 0, height, 1, 0);
    print(" // Son volume est: " + volSine + "\n"); 
    sine.setAmp(volSine);
    boxFreq.captionLabel().setControlFont(font); // change the font
    boxFreq.captionLabel().setControlFontSize(8);
    boxFreq.setText(" " + freqSine + " Hz ");
    boxFreq.setWindow(controlWindow);
    } 
    
    // change volume for loaded file
    if (msound) {
     float volSound = map(mouseY, 0, height, 10, 0);
    player.setGain(volSound);
    }
    
  }
  
  // Appliquer le filtrage  
  void mFilter() {
    
     if (msound) {
     player.addEffect(lpf);
     lpf.setFreq(cutoff);
     }
  }
    
  
  
  

