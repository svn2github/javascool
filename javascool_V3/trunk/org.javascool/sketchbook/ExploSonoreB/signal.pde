  /** Définit un signal sonore de forme prédéfini. */
  class signal {
    
    /** Construction du signal et de son interaction graphique. */
    signal() {
      out.sound();
      //boxValue = controlP5.addTextfield("  ",width-280-50,2*height/3+height/12,280,39);
      //boxValue.setText(" ");
      //boxValue.setWindow(controlWindow);
    }
    float volume;
    float frequence; 
    SineWave sinus_; 
    SquareWave square_;
    SawWave saw_;
    WhiteNoise wnoise_;
  
    String type;
    boolean sounding = false;
    //Textfield boxValue; 
    
    
    /** Joue un signal de type choisi  
     * @param n nom du type: sinus, square, triangle, saw, white noise.
     * @param f fréquence du signal. 
     * @param a amplitude du signal. 
     */  
    public void setSignal(String n, float f, float a) {
      type = n;
      if(sounding) switchOff();
      else if(record1.sounding) record1.switchOff();
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
      sounding = true;
    }
    
    /** Mise à jour des valeurs lors du déplacement de la souris. */
    void changeValue() {
      frequence = map(mouseX, 0, width, 100, 4000);
      //constrain(mouseX, 0, width-500);
      
      volume = map(mouseY, 0, height, 0.2, 0); 
      if(type.equals("sine")) {
        println("freq: " + frequence);
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
    
    /** Affichage de la valeur dans l'interface. */  
    void printV() {
      float vol = (volume)/0.4;
      boxValue.captionLabel().setControlFontSize(8);
      if(type.equals("noise")) {
        boxValue.setText("Vol.: " + vol + " ");
      } else {
        boxValue.setText(" Freq.: " + frequence + " Hz  -  Vol.: " + vol + " ");
      }
      boxValue.captionLabel().setControlFont(font); // change the font
      //boxValue.setWindow(controlWindow);
    }
    
    /** Effacement de l'affichage. */
    void deleteI() {
      boxValue.setText(" ");
      //boxValue.setWindow(controlWindow);
    }
    
    /** Arrêt de la sortie sonore. */
    void switchOff() {
      out.noSound();
      out.clearSignals();
      deleteI();
      sounding = false;
      
    }
  }
