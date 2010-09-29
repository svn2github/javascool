/**
   *
   * Fonctions relatives à l'interface
   *
   */
   
  
void launchInterface(int largeur, int hauteur) {
    
  /////////////////////////////////////////////////
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
    signal1.addSignal("sine", 1000, 0.2);
  } else if(theEvent.value()==2) {
    signal1.addSignal("square", 1000, 0.2);
  } else if(theEvent.value()==3) {
    signal1.addSignal("saw", 1000, 0.2);
  } else if(theEvent.value()==4) {
    signal1.addSignal("noise", 1000, 0.2);
  }
  // uncomment the line below to remove a multilist item when clicked.
  // theEvent.controller().remove();
}
 
  
void PlayRecord() {
    
    record1.addRecord(selectInput());

}


void FilterRecord() {
     
    record1.appliqueFiltre() ;
   
}

 
void mouseMoved() {
 
  if (signal1.sonne) {
    
    signal1.changeValeur();
    signal1.imprimeValeur();
    
  } else if (record1.sonne) {
    
    record1.changeValeur();
    record1.imprimeValeur();
    
  }
}
  
  
/** Stop tout son  
*/  
void StopAnySound() {
  
  if (signal1.sonne) {
    
     signal1.stopSonne();
    
  } else if (record1.sonne) {
    
    record1.stopSonne();
    
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
  
  
// Fft pour le signal capté par le micro, signla par défaut
void drawFFT() {
 
  strokeWeight(10);
  tint(250, 250); //gris,alpha sinon (255, 150, 0, 250)
  image(fade, (width - rWidth) / 2, (height - rHeight) / 2, rWidth, rHeight);
  noTint();
  
  fft.forward(in.mix);

  stroke(240, 240, 240);
  for(int i = 0; i < fft.avgSize(); i++){
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
  
// Tracé temporel du signal capté par le micro, signla par défaut  
void drawSignal() {
  
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


public processing.core.PApplet getControl() {
  
  controlWindow.hide();
  return controlWindow.papplet();
  
}
