class enregistrement {
  
  float volume;
  float frequenceCoupe = 100; 
  String monExtrait;
  
  AudioPlayer player;
  LowPassSP lpf;
  
  boolean sonne = false;
  boolean filtre = false;
  
  Textfield message;
  Textfield fenetreValeur;
  
  
  enregistrement() {
    
    fenetreValeur = controlP5.addTextfield(" ",largeur_-(50+281),hauteur_/2+hauteur_/10+(40+5),280,19);
    fenetreValeur.setText(" ");
    fenetreValeur.setWindow(controlWindow);
    
    message = controlP5.addTextfield(" ",70,hauteur_/2+hauteur_/10+(40+20+10),largeur_,20);
    message.setColorBackground(255); 
    message.setColorForeground(255); 
    message.setWindow(controlWindow);
    
    
    lpf = new LowPassSP(100, out.sampleRate());
    
  }
  
  void joue() {
   
    monExtrait = selectInput(); 
    if (monExtrait != null) {
      
      count +=1;
      player = minim.loadFile(monExtrait);
      player.loop();
      sonne = true;
      
    }
    
  }
  
  void appliqueFiltre() {
    
    filtre = true;
    player.addEffect(lpf);
    lpf.setFreq(frequenceCoupe); 
    
  }
  
  
  void retireFiltre() {
    
    filtre = false;
    player.clearEffects();
    
  }
  
  void imprimeMessage() {
    
    //message = controlP5.addTextfield(" ",70,y/2+y/10+(40+20+10),x,20);
    message.setText(" Un enregistrement est deja charge. Stoppez-le avant de choisir un nouveau morceau. ");
    message.captionLabel().setControlFont(font);
    message.setColorValue(myOr);
    message.setColorBackground(255); 
    message.setColorForeground(255); 
    //myTextfield.setColorLabel(myOr);
    message.setWindow(controlWindow);
    
  }
  
  
  void changeValeur() {
    
    volume = map(mouseY, 0, height, 0, -20);
    player.setGain(volume);
    
    if (filtre) {
      
      frequenceCoupe = map(mouseX, 0, width, 500, 5000);
      lpf.setFreq(frequenceCoupe);
    
    }
     
  }
  
  
  void imprimeValeur() {
    
    float vol = (volume+20)/20;
    fenetreValeur.captionLabel().setControlFontSize(8);
    fenetreValeur.setText(" Vol.: " + vol + " ");
    fenetreValeur.captionLabel().setControlFont(font); 
    fenetreValeur.setWindow(controlWindow);
    
    if (filtre) {
      
      fenetreValeur.setText(" Freq. de coupure: " + frequenceCoupe + " Hz  -  Vol.: " + vol + " ");
      fenetreValeur.captionLabel().setControlFont(font); 
      fenetreValeur.setWindow(controlWindow);
    
    }
    
  }
  
  void effaceInfo() {
    
    fenetreValeur.setText(" ");
    fenetreValeur.setWindow(controlWindow);
    message.clear();
    
  }
  
  void stopSonne() {
    
    player.pause();
    effaceInfo();
    monExtrait = null;
    sonne = false;
    
  }
  
  void ferme() {
    
    player.close();
    
  }
  
  
}
