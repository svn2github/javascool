  /** Définit un signal sonore enregistré. */
  class record {
    
    /** Construction du signal et de son interaction graphique. */
    record() {
      //boxValue = controlP5.addTextfield("  ",width-280-50,2*height/3+2*height/10+10,280,39);
      //boxValue.setText(" ");
      //boxValue.setWindow(controlWindow);
      lpf = new LowPassSP(100, out.sampleRate());
    }
    
    float volume;
    float Fc = 100; 
    //String monExtrait;
  
    LowPassSP lpf;
  
    boolean sounding = false;
    boolean filtering = false;
    //Textfield boxValue;
      
    /** Joue un enregistrement de son choix
    * @param path Nom de l'extrait
    */ 
    void setRecord(String path) {
     if(sounding) {
       switchOff();
       if(filtering) {
         removeFilter();
       }
     } else if(signal1.sounding) {
       signal1.switchOff();
     }
  
     if (path != null) {
        count += 1;
        player = minim.loadFile(path);
        player.loop();
        sounding = true;
      }
    }
    
    void applyFilter() {
      if (sounding)  {
         if (!filtering) {
           filtering = true;
           player.addEffect(lpf);
           lpf.setFreq(Fc);
         } else {
           removeFilter();
         }
       }
    }
    
    /** Applique un filtre avec une fréquence de coupure ajustable sur l'enregistrement de son choix 
    * @param path Nom de l'extrait
    * @param fc fréquence de coupure du filtre (entre 100 et 10000, sinon rien)
    */
    
    void setFilter(String path, float Fc_) {
     
      if(sounding) {
        switchOff();
         if(filtering) {
           removeFilter();
         }
      } else if(signal1.sounding) {
         signal1.switchOff();
      }
      
      if (path != null) {
        count += 1;
        player = minim.loadFile(path);
        player.loop();
        sounding = true;
        if (Fc_>100 && Fc_<10000) {
          player.addEffect(lpf);
          lpf.setFreq(Fc_);
          filtering = true;
        }
      }
      
    }
    
    
    void removeFilter() {    
      deleteI();
      filtering = false;
      player.clearEffects();
      
    }
    
    
    void changeValue() {
      volume = map(mouseY, 0, height, 0, -20);
      player.setGain(volume);
      if (filtering) {
        Fc = map(mouseX, 0, width, 500, 5000);
        lpf.setFreq(Fc);
      
      }
    }
    
   
    
    void printV() {
      
      float vol = (volume+20)/20;
      boxValue.captionLabel().setControlFontSize(8);
      boxValue.setText(" Vol.: " + vol + " ");
      boxValue.captionLabel().setControlFont(font); 
      //boxValue.setWindow(controlWindow);
      
      if(filtering) {
        
        boxValue.setText(" Freq. de coupure: " + Fc + " Hz  -  Vol.: " + vol + " ");
        boxValue.captionLabel().setControlFont(font); 
        //boxValue.setWindow(controlWindow);
      
      }
    }
    
    
    void deleteI() {
      boxValue.setText(" ");
      //boxValue.setWindow(controlWindow);
    }
    
    
    void switchOff() {
      player.pause();
      deleteI();
      sounding = false;
      
    }
    
    
    void ferme() {
      player.close();
    }
  }
