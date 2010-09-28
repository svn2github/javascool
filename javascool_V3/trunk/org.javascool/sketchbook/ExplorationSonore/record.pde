class record {
  
  float volume;
  float frequenceCoupe = 100; 
  String monExtrait;
  
  AudioPlayer player;
  LowPassSP lpf;
  
  FFT fftEnr;
  
  boolean sonne = false;
  boolean filtre = false;
  
  Textfield boxValue;
  
  
  record() {
    
    boxValue = controlP5.addTextfield(" parametres enregistrement ",width_-(50+281),height_/2+height_/10+(40+5),280,19);
    boxValue.setText(" ");
    boxValue.setWindow(controlWindow);
    
    lpf = new LowPassSP(100, out.sampleRate());
    
  }
  
    
  /** Joue un enregistrement de son choix
  * @param n Nom de l'extrait
  */ 
  void addRecord(String n) {
   
   
   if(sonne) {
     
     stopSonne();
     
     if(filtre) {
       
       retireFiltre();
       
     }
   } else if(signal1.sonne) {
     
     signal1.stopSonne();
     
   }
   
   monExtrait = n; 
   
   //println("record: " + monExtrait);
   if (monExtrait != null) {
      
      count += 1;
      player = minim.loadFile(monExtrait);
      player.loop();
      
      //FFT
      fftEnr = new FFT(player.bufferSize(), player.sampleRate());
      fftEnr.logAverages(60,6*width/(screen.width/2));
    
      sonne = true;
      
    }
    
  }
  
  void appliqueFiltre() {
    
    if (sonne)  {
       
       if (!filtre) {
         
         filtre = true;
         player.addEffect(lpf);
         lpf.setFreq(frequenceCoupe);
         
       } else {
         
         retireFiltre();
         
       }
     }
  }
  
  
  void retireFiltre() {
    
    effaceInfo();
    filtre = false;
    player.clearEffects();
    
  }
  
  
  void changeValeur() {
    
    volume = map(mouseY, 0, height, 0, -20);
    player.setGain(volume);
    
    if (filtre) {
      
      frequenceCoupe = map(mouseX, 0, width, 500, 5000);
      lpf.setFreq(frequenceCoupe);
    
    }
  }
  
  
  void drawFFT() {
   
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
      
    }
   
    fade = get(0, 0, width, height);
    
    stroke(250,70,0);
    for(int i = 0; i < fftEnr.avgSize(); i++){
      
      line((i * w) + (w / 2), height, (i * w) + (w / 2), height - fftEnr.getAvg(i) * (volume+20));
      
    } 
  }
  
  
  void drawSignal() {
    
    stroke(255);
    strokeWeight(1.5);  
    for(int i = 0; i < player.bufferSize() - 1; i++)
    {
      float x1 = map(i, 0, player.bufferSize(), 0, width);
      float x2 = map(i+1, 0, player.bufferSize(), 0, width);
      line(x1, 40 + player.left.get(i)*(volume+20)*3, x2, 40 + player.left.get(i+1)*(volume+20)*3);
      line(x1, 120 + player.right.get(i)*(volume+20)*3, x2, 120 + player.right.get(i+1)*(volume+20)*3);
    } 
  }
  
  
  void imprimeValeur() {
    
    float vol = (volume+20)/20;
    boxValue.captionLabel().setControlFontSize(8);
    boxValue.setText(" Vol.: " + vol + " ");
    boxValue.captionLabel().setControlFont(font); 
    boxValue.setWindow(controlWindow);
    
    if(filtre) {
      
      boxValue.setText(" Freq. de coupure: " + frequenceCoupe + " Hz  -  Vol.: " + vol + " ");
      boxValue.captionLabel().setControlFont(font); 
      boxValue.setWindow(controlWindow);
    
    }
  }
  
  
  void effaceInfo() {
    
    boxValue.setText(" ");
    boxValue.setWindow(controlWindow);
    
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
