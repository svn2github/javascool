/**
   *
   * Fonctions générales de l'application
   *
   */
   
  // Analyse spectrale
  void monAnalyseFFT() {
    
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
  
  }
   
   
  // Controlleur pour la sinusoide ou oscillateur   
  void mSine() {
    
      if (msine) { // si une sinusoide joue, on ne peut en générer une par-dessus
        println(" Une sinusoide est déjà en train de jouer!"); 
      } else {
      out.sound();
      
      // Créer un oscillateur sinusoidale avec une fréquence de 1000Hz, une amplitude de 1.0, et une fréquence d'échantillonage callée sur la ligne out
      sine = new SineWave(1000, 0.2, out.sampleRate());
      // la vitesse portamento pour l'oscillateur est réglée à 20 millisecondess
      sine.portamento(20);
      // ajouter le signal à la ligne de sortie
      out.addSignal(sine);
      msine = true;
      }      
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
  
    // Appliquer le filtrage  
  void mFilter() {
    
     if (msound && !(mfiltre)) {
       player.addEffect(lpf);
       lpf.setFreq(cutoff);
       mfiltre = true;
     } else {
       player.clearEffects();
       boxFiltre.setText(" ");
       boxFiltre.setWindow(controlWindow);
       mfiltre = false;
     }
  }
  

  
   void mouseMoved()
  {
    if (msine) {
    // Régler les déplacements de la souris sur les paramètres de la sinusoide
    freqSine = map(mouseX, 0, width, 100, 8000);
    //print("La fréquence de la sinusoide est: " + freqSine );  
    // Fréquence
    sine.setFreq(freqSine);
    // Volume
    volume = map(mouseY, 0, height, 0.4, 0);
    //print(" // Son volume est: " + volume + "\n"); 
    sine.setAmp(volume);
    
    InfoInteractive();
    
    } 
    
    // change volume for loaded file
    if (msound && mfiltre) {
    volume = map(mouseY, 0, height, 0, -20);
    player.setGain(volume);
    cutoff = map(mouseX, 0, width, 500, 5000);
    print(" // cutoff: " + cutoff + "\n"); 
    lpf.setFreq(cutoff);
    
    InfoInteractive();
    }
    
  }
  
  
  // Stopper tout son  
  void mStopSound() {
      
    if (msine) {
      
      out.noSound();
      out.clearSignals();
      boxSinus.setText(" ");
      boxSinus.setWindow(controlWindow);
      msine = false;
      
    } else if (msound) {
      
      player.pause();
      boxFiltre.setText(" ");
      boxFiltre.setWindow(controlWindow);
      msound = false;
      
    }
  
  }
  

 

