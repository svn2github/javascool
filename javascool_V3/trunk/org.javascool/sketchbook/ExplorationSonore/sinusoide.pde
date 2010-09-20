class sinusoide {
  
  float volume;
  float frequence; 
  
  SineWave sinusoide_;
  FFT fftSinus;
  
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
    
    // FFT
    //fade = get(0, 0, width, height);
    //rWidth = width * 0.99;
    //rHeight = height * 0.99; 
    fftSinus = new FFT(out.bufferSize(), out.sampleRate());
    //fftSinus.linAverages(128);
    fftSinus.logAverages(60,6*width/(screen.width/2));
    //rectMode(CORNERS);
    
  }
  
  void joue() {
    
    // Créer un oscillateur sinusoidale avec une fréquence de 1000Hz, une amplitude de 1.0, et une fréquence d'échantillonage callée sur la ligne out
    
    sinusoide_ = new SineWave(1000, 0.2, out.sampleRate());
    // la vitesse portamento pour l'oscillateur est réglée à 20 millisecondess
    sinusoide_.portamento(20);
    // ajouter le signal à la ligne de sortie
    out.addSignal(sinusoide_);
    //sinusoide_ = new SineWave(2000, 0.2, out.sampleRate());
    //out.addSignal(sinusoide_);    
    
    sonne = true;
    
     // create an FFT object that has a time-domain buffer the same size as jingle's sample buffer
    // and a sample rate that is the same as jingle's
    // note that this needs to be a power of two 
    // and that it means the size of the spectrum will be 1024. 
    // see the online tutorial for more info.
    
    
  }
  
  void joueTt() {
    
    // Créer un oscillateur sinusoidale avec une fréquence de 1000Hz, une amplitude de 1.0, et une fréquence d'échantillonage callée sur la ligne out
    
    for (int i = 0; i < 70; i = i+1) {

      sinusoide_ = new SineWave(i*50, 0.6, out.sampleRate());
      // la vitesse portamento pour l'oscillateur est réglée à 20 millisecondess
      sinusoide_.portamento(20);
      // ajouter le signal à la ligne de sortie
      out.addSignal(sinusoide_);
      //sinusoide_ = new SineWave(2000, 0.2, out.sampleRate());
      //out.addSignal(sinusoide_);    
    }
    
    sonne = true;
    
  }
  
  void traceFFT() {
   
   //  Rendu de la FFT
    strokeWeight(10);
    tint(250, 250); //gris,alpha sinon (255, 150, 0, 250)
    image(fade, (width - rWidth) / 2, (height - rHeight) / 2, rWidth, rHeight);
    noTint();
    
    fftSinus.forward(out.mix);
    
    int w = int(width/fftSinus.avgSize());//fftSinus.specSize()/128);
  
    stroke(240, 240, 240);
    for(int i = 0; i < fftSinus.avgSize(); i++){
      
      line((i * w) + (w / 2), height, (i * w) + (w / 2), height - fftSinus.getAvg(i) *(volume+20));
      // draw a rectangle for each average, multiply the value by 5 so we can see it better
      //rect(i*w, height, i*w + w, height - fftSinus.getAvg(i)*5);
      
    }
   
    fade = get(0, 0, width, height);
    
    stroke(250,70,0);
    for(int i = 0; i < fftSinus.avgSize(); i++){
      
      line((i * w) + (w / 2), height, (i * w) + (w / 2), height - fftSinus.getAvg(i) *(volume+20));
      // draw a rectangle for each average, multiply the value by 5 so we can see it better
      //line(i*w, height, i*w + w, height - fftSinus.getAvg(i)*5);
      
    } 
    
  }
  
  void traceSignal() {
    
   stroke(255);
    strokeWeight(1.5);  
      for(int i = 0; i < out.bufferSize() - 1; i++)
      {
        //println("buffer: " + in.bufferSize());
        float x1 = map(i, 0, out.bufferSize(), 0, width);
        float x2 = map(i+1, 0, out.bufferSize(), 0, width);
        line(x1, 40 + out.left.get(i)*(volume+20)*5, x2, 40 + out.left.get(i+1)*(volume+20)*5);
        line(x1, 120 + out.right.get(i)*(volume+20)*5, x2, 120 + out.right.get(i+1)*(volume+20)*5);
      } 
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
    fenetreValeur.setText(" Freq.: " + frequence + " Hz  -  Vol.: " + vol + " ");
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
