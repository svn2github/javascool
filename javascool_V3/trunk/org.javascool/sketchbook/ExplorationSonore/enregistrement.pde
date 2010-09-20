class enregistrement {
  
  float volume;
  float frequenceCoupe = 100; 
  String monExtrait;
  
  AudioPlayer player;
  LowPassSP lpf;
  
  FFT fftEnr;
  
  boolean sonne = false;
  boolean filtre = false;
  
  Textfield message;
  Textfield fenetreValeur;
  
  
  enregistrement() {
    
    fenetreValeur = controlP5.addTextfield(" parametres enregistrement ",largeur_-(50+281),hauteur_/2+hauteur_/10+(40+5),280,19);
    fenetreValeur.setText(" ");
    fenetreValeur.setWindow(controlWindow);
    
    message = controlP5.addTextfield(" message enregistrement ",70,hauteur_/2+hauteur_/10+(40+20+10),largeur_,20);
    message.setColorBackground(255); 
    message.setColorForeground(255); 
    message.setWindow(controlWindow);
    
    lpf = new LowPassSP(100, out.sampleRate());
    
    
    
  }
  
  void joue() {
   
    monExtrait = selectInput(); 
    if (monExtrait != null) {
      
      count += 1;
      player = minim.loadFile(monExtrait);
      player.loop();
      
      //FFT
      fftEnr = new FFT(player.bufferSize(), player.sampleRate());
      //fftEnr.linAverages(128);
      fftEnr.logAverages(60,6*width/(screen.width/2));
      //rectMode(CORNERS);
    
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
    
    message.setText(" Un enregistrement est deja charge. Stoppez-le avant de choisir un nouveau morceau. ");
    message.captionLabel().setControlFont(font);
    message.setColorValue(myOr);
    message.setColorBackground(255); 
    message.setColorForeground(255); 
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
  
  
  void traceFFT() {
   
   //  Rendu de la FFT
    strokeWeight(10);
    tint(250, 250); //gris,alpha sinon (255, 150, 0, 250)
    image(fade, (width - rWidth) / 2, (height - rHeight) / 2, rWidth, rHeight);
    noTint();
    
    fftEnr.forward(player.mix);
    
    int w = int(width/fftEnr.avgSize());//fftEnr.specSize()/128);
  
    stroke(240, 240, 240);
    for(int i = 0; i < fftEnr.avgSize(); i++){
      
      line((i * w) + (w / 2), height, (i * w) + (w / 2), height - fftEnr.getAvg(i) * (volume+20));
      // draw a rectangle for each average, multiply the value by 5 so we can see it better
      //rect(i*w, height, i*w + w, height - fftEnr.getAvg(i)*5);
      
    }
   
    fade = get(0, 0, width, height);
    
    stroke(250,70,0);
    for(int i = 0; i < fftEnr.avgSize(); i++){
      
      line((i * w) + (w / 2), height, (i * w) + (w / 2), height - fftEnr.getAvg(i) * (volume+20));
      // draw a rectangle for each average, multiply the value by 5 so we can see it better
      //line(i*w, height, i*w + w, height - fftEnr.getAvg(i)*5);
      
    } 
    
  }
  
  void traceSignal() {
    
   stroke(255);
    strokeWeight(1.5);  
      for(int i = 0; i < player.bufferSize() - 1; i++)
      {
        //println("buffer: " + in.bufferSize());
        float x1 = map(i, 0, player.bufferSize(), 0, width);
        float x2 = map(i+1, 0, player.bufferSize(), 0, width);
        line(x1, 40 + player.left.get(i)*(volume+20)*3, x2, 40 + player.left.get(i+1)*(volume+20)*3);
        line(x1, 120 + player.right.get(i)*(volume+20)*3, x2, 120 + player.right.get(i+1)*(volume+20)*3);
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
