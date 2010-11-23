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
    AudioOutput out;
  AudioPlayer player;
  FFT fft;

  
  int w;
  PImage fade;
  float rWidth, rHeight;
  Frame frame;
  int myOr = color(255,100,0);
  int myRed = color(255,0,0);
  int myBlue = color(20,70,105);
  int width_;
  int height_;
  boolean isOpen;
  int buttonValue = 1;
  
  TextButton[] T1 = new TextButton[8];
  boolean locked = false;
  boolean info = false;
  
  String[] ListN = { "sine", "square", "saw", "noise", "extrait", " filtre", " S T O P", " - Info - "};
  String sig = "null";

  
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
  
  
    f = createFont("Arial Bold",14,true);
    size(800, 600, P3D);//size(800,512);//,P3D);//OPENGL);

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
    // Define and create buttons
    fill(0);
    rect(0,height-100,width,100);
    fill(myBlue);
    color buttoncolor; color highlight; 
    for(int i=0; i< T1.length; i++)
    {
      if(i<6) {
        buttoncolor = color(250); highlight = color(150);
      } else if(i==6){
        buttoncolor = myRed; highlight = color(150);
      } else {
        buttoncolor = myBlue; highlight = color(150);
      }
      T1[i] = new TextButton(5+((i%4)*2*width/3/T1.length)+(int(i/6))*60+(int(i/7))*(width/2+60), height-100+60*(int(i/4)) -(int(i/7))*110, 100, buttoncolor, highlight, myOr, ListN[i]);

    }
  
    width_ = this.frame.getWidth();//getWidthInterface();
    height_ = this.frame.getHeight();//getHeightInterface();
  }
  
  
  // Ce qui est effectué tout au long de l'animation
  void draw() {
 
    background(0);
    pushMatrix();
    
    
    
    if (signal1.sounding) {
      c+=1;
      fft = new FFT(out.bufferSize(), out.sampleRate());
      fft.logAverages(60,6*width/(640));
      drawFFT("out");                                               // Trace la FFT
      //drawSignal("out");                                          // Trace le signal temporel
      if((c)%2 == 0) {
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
  
    Arrays.fill(((PGraphics3D)g).zbuffer,Float.MAX_VALUE);

    
    fill(0);
    rect(0,height-150,width,150);
    fill(myBlue);
    textFont(f, 12);
    text("S I G N A U X  N U M E R I Q U E S", 6, height-125);
    text("E N R E G I S T R E M E N T", 6, height-65);
    update(mouseX, mouseY);
    for(int i=0; i<T1.length; i++) 
    {
      if(i<6)  {
        fill(myBlue);
        rect(5+((i%4)*2*width/3/T1.length)-5, height-118+60*(int(i/4)),60,25);
      }
      T1[i].display();
    }
    
    // Fenetre informative
    myInfo();
    

  }
  
  // Un accès rapide aux fonctions via le clavier
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
  
  
  // Update les états des boutons
  void update(int x, int y)
  {
    if(locked == false) {
  
      for(int i=0; i<T1.length; i++) 
      {
        T1[i].update();
      }
    
    } 
    else {
      locked = false;
    }
  
    if(mousePressed) {
      for(int i=0; i<T1.length; i++) 
      {
        if(T1[i].pressed() && i==7) {
          if(T1[i].select==true) { 
            T1[i].select = false; info = false;
          } else {
            T1[i].select = true; info = true;
          }
        } else if(T1[i].pressed() && !(T1[i].select)) {
          T1[i].select = true;
          if(i<4) {
            signal1.setSignal(T1[i].value, 1000, 0.2);
          } else if(i==4) {
            record1.setRecord(selectInput());
          } else if(i==5) {
            record1.applyFilter() ;
          } else if(i==6) {
            StopAnySound();
          } 
        }

          for(int j=0; j<T1.length-1; j++) 
          {
            if(!(j==i)) T1[j].select = false;
          }
        }
      }

  }
  
  
  // Fenetre informative
  void myInfo() {
    if(T1[7].over()) {
    fill(255);
    rect(0,height-145,width,130);
    fill(myOr);
    text(" Parles, siffles, chuchotes.., et tu verras ce qui se passe sur l'analyseur de contenu sonore (à droite).. \n "+
      "Tu peux aussi jouer une signal ou un enregistrement de ton choix. \n "+
      "Pour ajuster la fréquence et l'amplitude du signal, bouges la souris sur la fenetre de l'analyseur. \n "+
      "Pour faire varier le volume de l'enregistrement sonore, tu peux procéder pareillement, \n "+
      "tandis que le contenu fréquentiel peut s'ajuster par un filtre. Expérimentes! \n ", 50, height-110 );
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

