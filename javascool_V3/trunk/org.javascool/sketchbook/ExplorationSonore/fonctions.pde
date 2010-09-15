/**
   *
   * Fonctions générales de l'application
   *
   */
   
  // Analyse spectrale
  void monAnalyseFFT() {
    
    fft = new FFT(in.bufferSize(), in.sampleRate());
    
    stroke(0);
    
    if (height>width) {
      fft.logAverages(60,6*(height/width)); //6 pour screen.width/2
    } else {
      fft.logAverages(60,6*(width/height));
    }
  
    w = width/fft.avgSize();
    strokeWeight(w);
    strokeCap(SQUARE);
    
    background(0);
    fade = get(0, 0, width, height);
    
    rWidth = width * 0.99;
    rHeight = height * 0.99; 
  
  }
  
  
  void traceFFT() {
   
   //  Rendu de la FFT
    strokeWeight(10);
    tint(250, 250); //gris,alpha sinon (255, 150, 0, 250)
    image(fade, (width - rWidth) / 2, (height - rHeight) / 2, rWidth, rHeight);
    noTint();
    
    fft.forward(in.mix);
  
    stroke(240, 240, 240);
    for(int i = 0; i < fft.avgSize(); i++){
      line((i * w) + (w / 2), height, (i * w) + (w / 2), height - fft.getAvg(i) * 30);
    }
   
    fade = get(0, 0, width, height);
    
    stroke(250,70,0);
    for(int i = 0; i < fft.avgSize(); i++){
      line((i * w) + (w / 2), height, (i * w) + (w / 2), height - fft.getAvg(i) * 30);
    } 
    
  }
  
  
  void traceSignal() {
    
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
   
   
  // Controlleur pour la sinusoide ou oscillateur   
  void mSine() {
    
    if (maSinusoide.sonne)  {
        
        maSinusoide.imprimeMessage();
        
    } else if (monEnregistrement.sonne)  {
      
      monEnregistrement.imprimeMessage();
      
    } else {
        
        maSinusoide.joue();
        
     }
    
  }
  
  void mPlaySound() {
    

    if (monEnregistrement.sonne)  {
      
      monEnregistrement.imprimeMessage();
      
    } else if (maSinusoide.sonne)  {
        
        maSinusoide.imprimeMessage();
        
    } else {
      
      
      monEnregistrement.joue();
      
    }
  
  }
  
  
  // Appliquer le filtrage  
  void mFilter() {
     
     if (monEnregistrement.sonne)  {
       
       if (!monEnregistrement.filtre) {
         
         monEnregistrement.appliqueFiltre() ;
         
       } else {
         
         monEnregistrement.retireFiltre();
         
       }
       
     }
     
  }
  
 
  void mouseMoved() {
   
    if (maSinusoide.sonne) {
      
      maSinusoide.changeValeur();
      maSinusoide.imprimeValeur();
      
    } else if (monEnregistrement.sonne) {
      
      monEnregistrement.changeValeur();
      monEnregistrement.imprimeValeur();
      
    }
    
  }
  
  
  // Stopper tout son  
  void mStopSound() {
    
    if (maSinusoide.sonne) {
      
      maSinusoide.stopSonne();
      
    } else if (monEnregistrement.sonne) {
      
      monEnregistrement.stopSonne();
      
    }
  
  }
  

 

