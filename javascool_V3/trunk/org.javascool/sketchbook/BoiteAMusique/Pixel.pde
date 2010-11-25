class Pixel
{
  float x, y;
  color c;
  int sw, w;
  int col = 0;
  boolean fs = false;

  int type = 0;
  float ampl;

  float[] freq = new float[2];
  String myEnr;
  SquareWave sine;
  AudioSample kicki;

  Pixel(float x_, float y_, int w_, int sw_, float freq1_, float freq2_, String Enr_) {
    x = x_;
    y = y_;
    w = w_;
    sw = sw_;
    sine = new SquareWave(0, 0.5, out.sampleRate());

    freq[0] = freq1_;
    freq[1] = freq2_;
    myEnr = Enr_;
  }

  void setMyAmp(float myAmp) {
    ampl = myAmp;
  }
  void setSource(int type_, int source_) {
    type = type_;
    if(type == 0)
      freq[0] = freqs.tab[0][source_];
    else if(type == 1)
      freq[1] = freqs.tab[1][source_];
    else if(type == 2) {
      if(source_ < filenames.length) {
        myEnr = freqs.tabE[source_];
        kicki = minim.loadSample("effects/" + myEnr);
      } else {
        int k = source_ - filenames.length;
        myEnr = freqs.tabE[k];
        kicki = minim.loadSample("effects/" + myEnr);
      }
    }
  }
  // Rendu visuel du pixel
  void display() {
    strokeWeight(sw);
    fill(c);
    ellipse(x, y, w, w);
  }
  // Activation de la source sonore
  void activate() {
    if(type == 0)
      sc.playNote(freq[0], ampl, 5);
    else if(type == 1) {
      sine.setFreq(freq[1]);
      sine.setAmp(ampl);
      out.addSignal(sine);
    } else if(type == 2) {
      kicki.setGain(ampl);
      kicki.trigger();
    }
  }
  // Position de la souris sur source
  boolean pospix(float mx, float my) {
    boolean resp = false;
    if((mx >= x - w / 2) && (mx <= x + w / 2))
      if((my >= y - w / 2) && (my <= y + w / 2)) {
        resp = true;
        fs = !fs;                      // active ou désactive selon son statut courant
      }
    return resp;
  }
  void init() {
    fs = false;
  }
  // Changement de couleur du pixel en fonction du balayage
  void changeColor(int col_) {
    col = col_;
    if(col == 0) {                  // le balayage n'est pas au dessus de la source
      out.removeSignal(sine);
      if(fs == true) {                // source activée
        c = color(0, 150, 0);         // si la source est activée, elle s'allume en vert
        stroke(255);
      } else {
        c = color(153);             // une source non activée est en gris
        stroke(255);
      }
    } else {                        // le balayage est au dessus de la source
      if(fs == true) {
        c = color(255, 70, 62);       // une source activée et balayée s'allume en rouge
        stroke(255, 0, 0);
        activate();                 // et sonne
      } else
        c = color(255, 170, 0);       // une source non activée mais balayée s'allume en orange
    }
  }
}
