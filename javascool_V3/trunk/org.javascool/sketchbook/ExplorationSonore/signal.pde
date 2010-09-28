class signal {
  
  float volume;
  float frequence; 
  
  SineWave sinus_; 
  SquareWave square_;
  SawWave saw_;
  WhiteNoise wnoise_;
  
  FFT fftSignal;
  
  String type;
  
  boolean sonne = false;
  
  Textfield boxValue; 
  
  
  signal() {
    
    out.sound();
    
    boxValue = controlP5.addTextfield(" parametres sinusoide ",50,height_/2-height_/10+(40+5),220,20);
    boxValue.setText(" ");
    boxValue.setWindow(controlWindow);
    
    // FFT
    fftSignal = new FFT(out.bufferSize(), out.sampleRate());
    fftSignal.logAverages(60,6*width/(screen.width/2));
    
  }
  
  
  /** Joue un signal de type choisi  
   * @param n nom du type: sinus, square, triangle, saw, white noise.
   * @param f fréquence du signal. 
   * @param a amplitude du signal. 
   */  
  void addSignal(String n, float f, float a) {
    
    type = n;
    if(sonne) stopSonne();
    else if(record1.sonne) record1.stopSonne();
    // Créer un oscillateur sinusoidale avec une fréquence de 1000Hz, une amplitude de 1.0, et une fréquence d'échantillonage callée sur la ligne out
    if(n.equals("sine")) {
      sinus_ = new SineWave(f, a, out.sampleRate());
      sinus_.portamento(20);
      out.addSignal(sinus_);
    } else if(n.equals("square")) {
      square_ = new SquareWave(f, a, out.sampleRate());
      square_.portamento(20);
      out.addSignal(square_);
    } else if(n.equals("saw")) {
      saw_ = new SawWave(f, a, out.sampleRate());
      saw_.portamento(20);
      out.addSignal(saw_);
    } else if(n.equals("noise")) {
      wnoise_ = new WhiteNoise(a);
      out.addSignal(wnoise_);
    }     
    
    sonne = true;
    
  }
  
  
  void drawFFT() {
   
   //  Rendu de la FFT
    strokeWeight(10);
    tint(250, 250); //gris,alpha sinon (255, 150, 0, 250)
    image(fade, (width - rWidth) / 2, (height - rHeight) / 2, rWidth, rHeight);
    noTint();
    
    fftSignal.forward(out.mix);
    
    int w = int(width/fftSignal.avgSize());//fftSignal.specSize()/128);
  
    stroke(240, 240, 240);
    for(int i = 0; i < fftSignal.avgSize(); i++){
      
      line((i * w) + (w / 2), height, (i * w) + (w / 2), height - fftSignal.getAvg(i) *(volume+20));
      
    }
   
    fade = get(0, 0, width, height);
    
    stroke(250,70,0);
    for(int i = 0; i < fftSignal.avgSize(); i++){
      
      line((i * w) + (w / 2), height, (i * w) + (w / 2), height - fftSignal.getAvg(i) *(volume+20));
      // draw a rectangle for each average, multiply the value by 5 so we can see it better
      //line(i*w, height, i*w + w, height - fftSignal.getAvg(i)*5);
      
    } 
  }
  
  
  void drawSignal() {
    
   stroke(255);
    strokeWeight(1.5);  
      for(int i = 0; i < out.bufferSize() - 1; i++)
      {
        float x1 = map(i, 0, out.bufferSize(), 0, width);
        float x2 = map(i+1, 0, out.bufferSize(), 0, width);
        line(x1, 40 + out.left.get(i)*(volume+20)*5, x2, 40 + out.left.get(i+1)*(volume+20)*5);
        line(x1, 120 + out.right.get(i)*(volume+20)*5, x2, 120 + out.right.get(i+1)*(volume+20)*5);
      } 
  }
  
  
  void changeValeur() {
    
    // Fréquence
    frequence = map(mouseX, 0, width, 100, 8000);
    // Volume
    volume = map(mouseY, 0, height, 0.4, 0); 
    
    if(type.equals("sine")) {
      sinus_.setFreq(frequence);
      sinus_.setAmp(volume);
    } else if(type.equals("square")){
      square_.setFreq(frequence);
      square_.setAmp(volume);
    } else if(type.equals("saw")){
      saw_.setFreq(frequence);
      saw_.setAmp(volume);
    } else if(type.equals("noise")){
      wnoise_.setAmp(volume);
    }
  }
  
  
  void imprimeValeur() {
    
    float vol = (volume)/0.4;
    boxValue.captionLabel().setControlFontSize(8);
    
    if(type.equals("noise")) {
      
      boxValue.setText("Vol.: " + vol + " ");
      
    } else {
      
      boxValue.setText(" Freq.: " + frequence + " Hz  -  Vol.: " + vol + " ");
      
    }
    boxValue.captionLabel().setControlFont(font); // change the font
    boxValue.setWindow(controlWindow);
    
  }
  
  
  void effaceInfo() {
    
    boxValue.setText(" ");
    boxValue.setWindow(controlWindow);
    
  }
  
  
  void stopSonne() {
    
    out.noSound();
    out.clearSignals();
    effaceInfo();
    sonne = false;
    
  }
  
}
