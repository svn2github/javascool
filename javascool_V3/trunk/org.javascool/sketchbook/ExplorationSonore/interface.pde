/* Fonctions relatives à l'interface. */
   
  
void launchInterface(int largeur, int hauteur) {
    
  // Interface de manipulation: génère sinusoide, charge enregistrement, etc
  
  controlWindow = controlP5.addControlWindow(" I N T E R F A C E ",0,hauteur/6,largeur,hauteur);
  controlWindow.hideCoordinates();
  //controlWindow.setUndecorated(true);
  controlWindow.setBackground(color(255));
  
  // Info
  controlP5.addButton("info",10,0,0,70,15).setId(1);
  controlP5.controller("info").setCaptionLabel("Info"); // change content
  controlP5.controller("info").captionLabel().setControlFont(font); // change the font
  controlP5.controller("info").captionLabel().setControlFontSize(10);
  controlP5.controller("info").setColorActive(myOr); 
  controlP5.controller("info").setColorBackground(myOr); 
  controlP5.controller("info").setWindow(controlWindow);
  wInfo = controlP5.addButton("buttonValue",0,-largeur,20,60,50);
  wInfo.setId(2);
  wInfo.setWidth(largeur);
  wInfo.setHeight(100);
  wInfo.setColorActive(255); 
  wInfo.setColorBackground(255); 
  wInfo.setColorLabel(myOr);
  wInfo.captionLabel().setControlFont(font);
  wInfo.captionLabel().setControlFontSize(12);
  wInfo.captionLabel().toUpperCase(false);
  wInfo.captionLabel().set(
			   "Parles, siffles, chuchotes.., \n \n"+
			   "et tu verras ce qui se passe sur l'analyseur de contenu sonore (à droite).. \n \n"+
			   "Tu peux aussi jouer une signal ou un enregistrement de ton choix. \n \n"+
			   "Pour ajuster la fréquence et l'amplitude du signal, \n \n"+
			   "bouges la souris sur la fenetre de l'analyseur. \n \n"+
			   "Pour faire varier le volume de l'enregistrement sonore, \n \n"+
			   "tu peux procéder pareillement, tandis que le contenu fréquentiel \n \n"+ 
			   "peut s'ajuster par un filtre. Expérimentes! \n \n" );
  wInfo.captionLabel().style().marginLeft = 45;
  wInfo.captionLabel().style().marginTop = -45;
  
  String[] ListN = {"sinusoide", "carré", "scie", "bruit"};
  MultiList l = controlP5.addMultiList("myList", 50,hauteur/2-hauteur/10,largeur-100-80,40);
  MultiListButton b;
  b = l.add("Jouer un signal",1);
  b.captionLabel().setControlFont(font);
  b.captionLabel().setControlFontSize(10);
  for(int i=0;i<4;i++) {
    MultiListButton c = b.add("signal"+(i+1),i+1);
    c.captionLabel().setControlFont(font);
    c.setLabel(" "+ListN[i]);
    c.captionLabel().setControlFontSize(10);
    c.setHeight(20);
    c.setWidth(80);
  }
  b.setWindow(controlWindow);
  
  // Charger un enregistrement
  controlP5.addButton("PlayRecord",0,50,hauteur/2+hauteur/10,largeur-100,40);
  controlP5.controller("PlayRecord").setCaptionLabel(" Jouer un enregistrement sonore");
  controlP5.controller("PlayRecord").captionLabel().setControlFont(font); // change the font
  controlP5.controller("PlayRecord").captionLabel().setControlFontSize(10);
  controlP5.controller("PlayRecord").setWindow(controlWindow);
  
  // Filtre Basse Fréquence applicable sur l'enregistrement
  controlP5.addButton("FilterRecord",0,largeur-(50+280+70+5),hauteur/2+hauteur/10+(40+5),70,20);
  controlP5.controller("FilterRecord").setCaptionLabel(" FILTRAGE"); // - Fréquence de coupure reglable
  controlP5.controller("FilterRecord").captionLabel().setControlFont(font); // change the font
  controlP5.controller("FilterRecord").captionLabel().setControlFontSize(10);
  controlP5.controller("FilterRecord").captionLabel().toUpperCase(false);
  controlP5.controller("FilterRecord").setWindow(controlWindow);
  
  // Stopper tout son
  controlP5.addButton("StopAnySound",0,largeur-(50+70),hauteur/2+135+(hauteur/2-(hauteur/4+20))/2,70,40);
  controlP5.controller("StopAnySound").setCaptionLabel("  S  t  o  p");
  controlP5.controller("StopAnySound").captionLabel().setControlFont(font); // change the font
  controlP5.controller("StopAnySound").captionLabel().setControlFontSize(11);
  controlP5.controller("StopAnySound").setColorBackground(myRed);
  controlP5.controller("StopAnySound").setColorActive(myOr);
  controlP5.controller("StopAnySound").setWindow(controlWindow);
}
  
void controlEvent(ControlEvent theEvent) {
  //println(theEvent.controller().name()+" = "+theEvent.value());  

  if(theEvent.value()==1) {
    signal1.setSignal("sine", 1000, 0.2);
  } else if(theEvent.value()==2) {
    signal1.setSignal("square", 1000, 0.2);
  } else if(theEvent.value()==3) {
    signal1.setSignal("saw", 1000, 0.2);
  } else if(theEvent.value()==4) {
    signal1.setSignal("noise", 1000, 0.2);
  }
  // uncomment the line below to remove a multilist item when clicked.
  // theEvent.controller().remove();
}
 
  
void PlayRecord() {
  record1.setRecord(selectInput());
}

void FilterRecord() {
  record1.applyFilter() ;
}
 
void mouseMoved() {
  if (signal1.sounding) {
    signal1.changeValue();
    signal1.printV();
  } else if (record1.sounding) {
    record1.changeValue();
    record1.printV();
  }
}
  
  
/** Stop tout son  */  
void StopAnySound() {
  if (signal1.sounding) {
    signal1.switchOff();
  } else if (record1.sounding) {
    record1.switchOff();
  }
}
   
  
// Lance l'analyse spectrale
void launchFFT() {
  fft = new FFT(in.bufferSize(), in.sampleRate());
  
  stroke(0);

  fft.logAverages(60,6*width/(screen.width/2)); //6 pour screen.width/2

  w = width/fft.avgSize();
  strokeWeight(w);
  strokeCap(SQUARE);
  
  background(0);
  fade = get(0, 0, width, height);
  
  rWidth = width * 0.99;
  rHeight = height * 0.99; 
}
  
  

/** Tracé 3D du spectre au fil du temps. */
void drawFFT(String n) {

  String type = n;
  strokeWeight(10);
  tint(250, 250); //gris,alpha sinon (255, 150, 0, 250)
  image(fade, (width - rWidth) / 2, (height - rHeight) / 2, rWidth, rHeight);
  noTint();
  
  if(n.equals("out")) {
    fft.forward(out.mix);
  } else if(n.equals("player")) {
    fft.forward(player.mix);
  } else {
    fft.forward(in.mix);
  }


  stroke(240, 240, 240);
  for(int i = 0; i < fft.avgSize(); i++) {
    line((i * w) + (w / 2), 19*height/20, (i * w) + (w / 2), 19*height/20 - fft.getAvg(i) * 20);
  }
 
  fade = get(0, 0, width, height);
  
  stroke(250,70,0);
  textFont(f,14);
  text("                  100     125                  250                500             1000             2000              4000                8000                              Hz", 0, height-6);
  for(int i = 0; i < fft.avgSize(); i++){
    line((i * w) + (w / 2), 19*height/20, (i * w) + (w / 2), 19*height/20 - fft.getAvg(i) * 20);
  } 
}

  
/** Tracé temporel du signal. */
void drawSignal(String n) {
  
  stroke(255);
  strokeWeight(1.5);  
  int k; 
  if(n.equals("out")) {
    k = out.bufferSize();
  } else if(n.equals("player")) {
    k = player.bufferSize();
  } else {
    k = in.bufferSize();
  }
  for(int i = 0; i < k - 1; i++) {
    //println("buffer: " + in.bufferSize());

    float x1 = map(i, 0, k, 0, width);
    float x2 = map(i+1, 0, k, 0, width);
    if(n.equals("out")) {
      line(x1, 40 + out.left.get(i)*100, x2, 40 + out.left.get(i+1)*100);
      line(x1, 120 + out.right.get(i)*100, x2, 120 + out.right.get(i+1)*100);
    } else if(n.equals("player")) {
      line(x1, 40 + player.left.get(i)*100, x2, 40 + player.left.get(i+1)*100);
      line(x1, 120 + player.right.get(i)*100, x2, 120 + player.right.get(i+1)*100);
    } else {
      line(x1, 40 + in.left.get(i)*100, x2, 40 + in.left.get(i+1)*100);
      line(x1, 120 + in.right.get(i)*100, x2, 120 + in.right.get(i+1)*100);
    }
  } 
}
  
  
// Controlleur pour l'info    
void info(float theValueA) {
  isOpen = !isOpen;
  controlP5.controller("info").setCaptionLabel((isOpen==true) ? "fermer Info":"voir Info");
}

void openInfo() {
  wInfo.position().x += ((isOpen==true ? 0:-screen.width/2-3) - wInfo.position().x) * 0.2;
  wInfo.setWindow(controlWindow); 
  
}

  
// Fonctions pour l'insertion dans javascool
int getWidthInterface() {
  return controlWindow.papplet().getWidth();
}


int getHeightInterface() {
  return controlWindow.papplet().getHeight();
}


/** Utilisé pour fermer la fenêtre secondaire de l'interface, par JavaScool. */
public processing.core.PApplet getControl() {
  controlWindow.hide();
  return controlWindow.papplet();
}


/** Joue un signal de type choisi  
 * @param n nom du type: sinus, square, triangle, saw, white noise.
 * @param f fréquence du signal. 
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

/** Applique un filtre avec une fréquence de coupure ajustable sur l'enregistrement de son choix 
 * @param path Nom de l'extrait
 * @param f fréquence de coupure du filtre (entre 100 et 10000, sinon rien)
 */ 
public static void setFilter(String path, float fc) {
  proglet.record1.setFilter(path, fc);
}

/** Arrête l'émission sonore. */
public static void playStop() {
  proglet.StopAnySound();
}

static ExplorationSonore proglet;
