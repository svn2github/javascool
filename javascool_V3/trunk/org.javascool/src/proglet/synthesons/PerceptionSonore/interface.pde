/**
   *
   * Fonctions relatives à l'interface
   *
   */
   
   
  void monInterface(){
    
    /////////////////////////////////////////////////
    // Interface de manipulation: génère sinusoide, charge enregistrement, etc
    
    //controlWindow = controlP5.addControlWindow(" I N T E R F A C E ",0,screen.height/6,screen.width/2,2*screen.height/3);
    controlWindow = controlP5.addControlWindow(" I N T E R F A C E ",0,screen.height/6,512,2*screen.height/3);
    controlWindow.hideCoordinates();
    controlWindow.setBackground(color(255));
    
    // Info
    controlP5.addButton("FenetreInfo",10,0,0,70,15).setId(1);
    controlP5.controller("FenetreInfo").setCaptionLabel("Info"); // change content
    controlP5.controller("FenetreInfo").captionLabel().setControlFont(font); // change the font
    controlP5.controller("FenetreInfo").captionLabel().setControlFontSize(10);
    controlP5.controller("FenetreInfo").setColorActive(myOr); 
    controlP5.controller("FenetreInfo").setColorBackground(myOr); 
    controlP5.controller("FenetreInfo").setWindow(controlWindow);
    boxA = controlP5.addButton("buttonValue",0,-width,20,60,50);
    boxA.setId(2);
    boxA.setWidth(width);
    boxA.setHeight(100);
    boxA.setColorActive(255); 
    boxA.setColorBackground(255); 
    boxA.setColorLabel(myOr);
    boxA.captionLabel().setControlFont(font);
    boxA.captionLabel().setControlFontSize(12);
    boxA.captionLabel().toUpperCase(false);
    boxA.captionLabel().set(//"Notre but est de te faire découvrir les subtilités des sons. A droite se trouve un analyseur de contenu sonore. \n \n"+
    "Parles, siffles, chuchotes.., \n \n"+
    "et tu verras ce qui se passe sur l'analyseur de contenu sonore (à droite).. \n \n"+
    "Tu peux aussi jouer une sinusoide ou un enregistrement de ton choix. \n \n"+
    "Pour ajuster la fréquence et l'amplitude de la sinusoide, \n \n"+
    "bouges la souris sur la fenetre de l'analyseur. \n \n"+
    "Pour ajuster le volume de l'enregistrement sonore, \n \n"+
    "tu peux procéder de la meme manière, \n \n"+
    "tandis que le contenu fréquentiel peut etre réglé par un filtre. \n \n"+
    "Expérimentes! \n \n" );
    boxA.captionLabel().style().marginLeft = 30;
    boxA.captionLabel().style().marginTop = -40;
    
  
    
    
    // Générer une Sinusoide
    controlP5.addButton("mSine",0,50,height/2-height/10,width-100,40);
    controlP5.controller("mSine").setCaptionLabel(" Jouer une sinusoide");
    controlP5.controller("mSine").captionLabel().setControlFont(font); // change the font
    controlP5.controller("mSine").captionLabel().setControlFontSize(10);
    controlP5.controller("mSine").setWindow(controlWindow);
    // Une ligne de sortie pour la sinusoide émise, 
    // les paramètres par défaut sont: 1024 pour la taille d ebuffer, 44100 pour la fréquence d'échantillonage, 16 bit depth.
    //out = minim.getLineOut(Minim.STEREO);
    boxSinus = controlP5.addTextfield(" ",width-(50+221),height/2-height/10+(40+5),220,20);
    boxSinus.setText(  "  ");
    boxSinus.setWindow(controlWindow);
    
    // Charger un enregistrement
    controlP5.addButton("mPlaySound",0,50,height/2+height/10,width-100,40);
    controlP5.controller("mPlaySound").setCaptionLabel(" Jouer un enregistrement sonore");
    controlP5.controller("mPlaySound").captionLabel().setControlFont(font); // change the font
    controlP5.controller("mPlaySound").captionLabel().setControlFontSize(10);
    controlP5.controller("mPlaySound").setWindow(controlWindow);
    
    // Filtre Basse Fréquence applicable sur l'enregistrement
    lpf = new LowPassSP(100, out.sampleRate());
    controlP5.addButton("mFilter",0,width-(50+300+70+5),height/2+height/10+(40+5),70,20);
    controlP5.controller("mFilter").setCaptionLabel(" FILTRAGE"); // - Fréquence de coupure reglable
    controlP5.controller("mFilter").captionLabel().setControlFont(font); // change the font
    controlP5.controller("mFilter").captionLabel().setControlFontSize(10);
    controlP5.controller("mFilter").captionLabel().toUpperCase(false);
    controlP5.controller("mFilter").setWindow(controlWindow);
    boxFiltre = controlP5.addTextfield(" ",width-(50+301),height/2+height/10+(40+5),300,19);
    boxFiltre.setText(  "  ");
    boxFiltre.captionLabel().setControlFont(font);
    boxFiltre.setWindow(controlWindow);
    /*controlP5.addSlider("sFreq",500,5000,600,width-(50+220),height/2+height/10+(40+25+5*2),220,20);
    controlP5.controller("sFreq").captionLabel().setControlFont(font); // change the font
    controlP5.controller("sFreq").captionLabel().setControlFontSize(10);
    controlP5.controller("sFreq").setColorActive(myOr); 
    //controlP5.controller("sFreq").setColorValue(myOr);
    controlP5.controller("sFreq").setColorForeground(myOr);
    //controlP5.controller("sFreq").setColorBackground(0); 
    controlP5.controller("sFreq").setColorLabel(myOr);
    controlP5.controller("sFreq").captionLabel().toUpperCase(false);
    controlP5.controller("sFreq").setCaptionLabel(" (Hz)");
    controlP5.controller("sFreq").setWindow(controlWindow);*/
    
    // Stopper tout son
    controlP5.addButton("mStopSound",0,width-(50+70),height/2+135+(height/2-(height/4+20))/2,70,40);
    controlP5.controller("mStopSound").setCaptionLabel("  S  t  o  p");
    controlP5.controller("mStopSound").captionLabel().setControlFont(font); // change the font
    controlP5.controller("mStopSound").captionLabel().setControlFontSize(11);
    controlP5.controller("mStopSound").setColorBackground(myRed);
    controlP5.controller("mStopSound").setColorActive(myOr);
    controlP5.controller("mStopSound").setWindow(controlWindow);
    
  }
   
  // Controlleur pour l'info    
  void FenetreInfo(float theValueA) {
    
    //println("a button event. "+theValueA);
    isOpen = !isOpen;
    controlP5.controller("FenetreInfo").setCaptionLabel((isOpen==true) ? "fermer Info":"voir Info");
    
  }
  
  
  void InfoInteractive(){
    
    if (msine) {
      
      float vol = (volume)/0.4;
      boxSinus.captionLabel().setControlFontSize(8);
      boxSinus.setText(" Freq.: " + freqSine + " Hz  -  Vol.: " + vol + " ");
      boxSinus.captionLabel().setControlFont(font); // change the font
      boxSinus.setWindow(controlWindow);
      
    } else if (msound && mfiltre) {

      float vol = (volume+20)/20;
      boxFiltre.captionLabel().setControlFontSize(8);
      boxFiltre.setText(" Freq. de coupure: " + cutoff + " Hz  -  Vol.: " + vol + " ");
      boxFiltre.captionLabel().setControlFont(font); // change the font
      boxFiltre.setWindow(controlWindow);
    
    } 
    
  }
