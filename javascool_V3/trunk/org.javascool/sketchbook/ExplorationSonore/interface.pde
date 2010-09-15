/**
   *
   * Fonctions relatives à l'interface
   *
   */
   
   
  void monInterface(int largeur, int hauteur) {
    
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
    fenetreInfo = controlP5.addButton("buttonValue",0,-largeur,20,60,50);
    fenetreInfo.setId(2);
    fenetreInfo.setWidth(largeur);
    fenetreInfo.setHeight(100);
    fenetreInfo.setColorActive(255); 
    fenetreInfo.setColorBackground(255); 
    fenetreInfo.setColorLabel(myOr);
    fenetreInfo.captionLabel().setControlFont(font);
    fenetreInfo.captionLabel().setControlFontSize(12);
    fenetreInfo.captionLabel().toUpperCase(false);
    fenetreInfo.captionLabel().set(
    "Parles, siffles, chuchotes.., \n \n"+
    "et tu verras ce qui se passe sur l'analyseur de contenu sonore (à droite).. \n \n"+
    "Tu peux aussi jouer une sinusoide ou un enregistrement de ton choix. \n \n"+
    "Pour ajuster la fréquence et l'amplitude de la sinusoide, \n \n"+
    "bouges la souris sur la fenetre de l'analyseur. \n \n"+
    "Pour ajuster le volume de l'enregistrement sonore, \n \n"+
    "tu peux procéder de la meme manière, \n \n"+
    "tandis que le contenu fréquentiel peut etre réglé par un filtre. \n \n"+
    "Expérimentes! \n \n" );
    fenetreInfo.captionLabel().style().marginLeft = 45;
    fenetreInfo.captionLabel().style().marginTop = -45;
    
    // Générer une Sinusoide
    controlP5.addButton("mSine",0,50,hauteur/2-hauteur/10,largeur-100,40);
    controlP5.controller("mSine").setCaptionLabel(" Jouer une sinusoide");
    controlP5.controller("mSine").captionLabel().setControlFont(font); // change the font
    controlP5.controller("mSine").captionLabel().setControlFontSize(10);
    controlP5.controller("mSine").setWindow(controlWindow);
    // Une ligne de sortie pour la sinusoide émise, 
    // les paramètres par défaut sont: 1024 pour la taille d ebuffer, 44100 pour la fréquence d'échantillonage, 16 bit depth.
    //out = minim.getLineOut(Minim.STEREO);
    //boxSinus = controlP5.addTextfield(" ",largeur-(50+221),hauteur/2-hauteur/10+(40+5),220,20);
    //boxSinus.setText(  "  ");
    //boxSinus.setWindow(controlWindow);
    
    // Charger un enregistrement
    controlP5.addButton("mPlaySound",0,50,hauteur/2+hauteur/10,largeur-100,40);
    controlP5.controller("mPlaySound").setCaptionLabel(" Jouer un enregistrement sonore");
    controlP5.controller("mPlaySound").captionLabel().setControlFont(font); // change the font
    controlP5.controller("mPlaySound").captionLabel().setControlFontSize(10);
    controlP5.controller("mPlaySound").setWindow(controlWindow);
    
    // Filtre Basse Fréquence applicable sur l'enregistrement
    controlP5.addButton("mFilter",0,largeur-(50+280+70+5),hauteur/2+hauteur/10+(40+5),70,20);
    controlP5.controller("mFilter").setCaptionLabel(" FILTRAGE"); // - Fréquence de coupure reglable
    controlP5.controller("mFilter").captionLabel().setControlFont(font); // change the font
    controlP5.controller("mFilter").captionLabel().setControlFontSize(10);
    controlP5.controller("mFilter").captionLabel().toUpperCase(false);
    controlP5.controller("mFilter").setWindow(controlWindow);
    /*boxFiltre = controlP5.addTextfield(" ",largeur-(50+281),hauteur/2+hauteur/10+(40+5),280,19);
    boxFiltre.setText(  "  ");
    boxFiltre.captionLabel().setControlFont(font);
    boxFiltre.setWindow(controlWindow);*/
    
    // Stopper tout son
    controlP5.addButton("mStopSound",0,largeur-(50+70),hauteur/2+135+(hauteur/2-(hauteur/4+20))/2,70,40);
    controlP5.controller("mStopSound").setCaptionLabel("  S  t  o  p");
    controlP5.controller("mStopSound").captionLabel().setControlFont(font); // change the font
    controlP5.controller("mStopSound").captionLabel().setControlFontSize(11);
    controlP5.controller("mStopSound").setColorBackground(myRed);
    controlP5.controller("mStopSound").setColorActive(myOr);
    controlP5.controller("mStopSound").setWindow(controlWindow);
    
  }
   
  // Controlleur pour l'info    
  void info(float theValueA) {
    
    isOpen = !isOpen;
    controlP5.controller("info").setCaptionLabel((isOpen==true) ? "fermer Info":"voir Info");
    
  }
  
  
  void ouvreFenetreInfo() {
   
    fenetreInfo.position().x += ((isOpen==true ? 0:-width/2-3) - fenetreInfo.position().x) * 0.2;
    fenetreInfo.setWindow(controlWindow); 
    
  }
  
  
  int InterfaceLargeur() {
 
    return controlWindow.papplet().getWidth();
  }
  
  
  int InterfaceHauteur() {
 
    return controlWindow.papplet().getHeight();
  }
  
  
  public processing.core.PApplet getControl() {
    
    controlWindow.hide();
    return controlWindow.papplet();
    
  }
