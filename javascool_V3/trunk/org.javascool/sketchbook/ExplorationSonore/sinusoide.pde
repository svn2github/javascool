class sinusoide {
  
  float volume;
  float frequence; 
  
  SineWave sinusoide_;
  
  boolean sonne = false;
  
  Textfield message;
  Textfield fenetreValeur;
  
  
  
  
  sinusoide() {
    
    out.sound();
    
    fenetreValeur = controlP5.addTextfield(" parametres sinusoide ",largeur_-(50+221),hauteur_/2-hauteur_/10+(40+5),220,20);
    fenetreValeur.setText(" ");
    fenetreValeur.setWindow(controlWindow);
    
    message = controlP5.addTextfield(" message sinusoide ",largeur_-(50+190),hauteur_/2-hauteur_/10+(40+20+10),190,20);
    message.setColorBackground(255); 
    message.setColorForeground(255); 
    message.setWindow(controlWindow);
    
  }
  
  void joue() {
    
    // Créer un oscillateur sinusoidale avec une fréquence de 1000Hz, une amplitude de 1.0, et une fréquence d'échantillonage callée sur la ligne out
    sinusoide_ = new SineWave(1000, 0.2, out.sampleRate());
    // la vitesse portamento pour l'oscillateur est réglée à 20 millisecondess
    sinusoide_.portamento(20);
    // ajouter le signal à la ligne de sortie
    out.addSignal(sinusoide_);
    
    sonne = true;
    
  }
  
  void imprimeMessage() {
    
    message.setText("Une sinusoide est deja en train de jouer! ");
    message.captionLabel().setControlFont(font);
    message.setColorValue(myOr);
    message.setColorBackground(255); 
    message.setColorForeground(255); 
    message.setWindow(controlWindow);
    
  }
  
  void changeValeur() {
    
    frequence = map(mouseX, 0, width, 100, 8000);
    //print("La fréquence de la sinusoide est: " + freqSine );  
    // Fréquence
    sinusoide_.setFreq(frequence);
    // Volume
    volume = map(mouseY, 0, height, 0.4, 0);
    //print(" // Son volume est: " + volume + "\n"); 
    sinusoide_.setAmp(volume);
    
  }
  
  void imprimeValeur() {
    
    float vol = (volume)/0.4;
    fenetreValeur.captionLabel().setControlFontSize(8);
    fenetreValeur.setText(" Freq.: " + frequence + " Hz  -  Vol.: " + volume + " ");
    fenetreValeur.captionLabel().setControlFont(font); // change the font
    fenetreValeur.setWindow(controlWindow);
    
  }
  
  void effaceInfo() {
    
    fenetreValeur.setText(" ");
    fenetreValeur.setWindow(controlWindow);
    message.setText(" ");
    message.setWindow(controlWindow);
    
  }
  
  void stopSonne() {
    
    out.noSound();
    out.clearSignals();
    effaceInfo();
    sonne = false;
    
  }
  
  
  
  
}
