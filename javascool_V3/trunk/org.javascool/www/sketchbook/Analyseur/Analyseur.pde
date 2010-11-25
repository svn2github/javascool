/*////////////////////////////////////////////////////////////////////////////
 *
 * 08.2010 Cécile P-L for Fuscia - ccl.picard@gmail.com - 
 * Analyseur FFT avec bandes de fréquences ajustables 
 * pour noter l'influence de la résolution des bandes sur le spectre
 *
 * Inspired from Linear Averages
 * by Damien Di Fede.
 *  
 */

import ddf.minim.analysis.*;
import ddf.minim.*;
import ddf.minim.signals.*;

Minim minim;
AudioInput in;
AudioPlayer jingle;
FFT fft,fftS;
AudioOutput out;
SineWave sine;

int nb;
String path = null;
PFont f;
boolean info = false, sounding = false;
float fs = 44100;

void setup()
{
  size(512, 240, P2D);
  background(0);
  
  f = createFont("Arial Bold",12,true);
  minim = new Minim(this);
  in = minim.getLineIn(Minim.STEREO, 512);

  nb = 128;
  
  fft = new FFT(in.bufferSize(), in.sampleRate());
  // use 'nb' averages (between 64 and 1024).
  // the maximum number of averages we could ask for is half the spectrum size. 
  fft.linAverages(nb);

  rectMode(CORNERS);
}

void draw()
{
  fill(255);
  if(path != null) {
    fft = new FFT(jingle.bufferSize(), jingle.sampleRate());
    fs = jingle.sampleRate();
    fft.linAverages(nb);
    fft.forward(jingle.mix);
  } 
  else {
    fft.forward(in.mix);
  }
  int w = int(fft.specSize()/nb);

  for(int i = 0; i < fft.avgSize(); i++)
  {
    // draw a rectangle for each average, multiply the value by 5 so we can see it better
    rect(i*w, height, i*w + w, height - fft.getAvg(i)*5);
  }
  
  
  
  // Info
  if(info) {
    fill(0);
    rect(0, 100, width/2+200, -10);
    textFont(f,12);
    textAlign(LEFT);
    fill(255, 150, 0);
    text(" - COMMANDES - \n " + 
      ".  L / l : chargement d'un fichier son:  accès dans une base de données \n " +
      ".  + / - : raffine/élargit les bandes de fréquences pour la FFT (entre 64 et 256) \n " +
      ".  S / s : stop/redémarre la lecture \n " +
      ".  I / i : afficher/cacher les instructions \n " +
      ".  ESC: Fermer l'application ", 10, 15);

  } else {
    fill(0);
      rect(0, 100, width/2+200, -10);
      textFont(f,12);
      textAlign(LEFT);
      fill(255, 150, 0);
      text("'i' pour info ", 10, 15);
  }
  
  
}

void mouseMoved() {
  int w = int((fs+nb)/(nb));
  println("/ " + nb+ "/ " +fft.avgSize() + "/ " +fft.specSize());
  textFont(f,12);
  fill(0);
  rect(width, 150, width/2+80, height/3);
  fill(255);
  text("Fréquence pointée = " + w*(fft.avgSize()*mouseX)/width, width-170, int(height/2));
}
void keyPressed()                                                  
{
  if (key == '+') {
    if(nb<256) {
      background(0);
      nb*=2;
    } else {
    }
  }
  if (key == '-') {
    if(nb>32) {
      background(0);
      nb/=2;
    }
  }
  if (key == 'l') {
    background(0);
    String pathN;

    pathN = selectInput();
    if(pathN != null) {
      path = pathN;
      jingle = minim.loadFile(path);
      sounding = true;
    }
    // loop the file
    jingle.loop();
  }
  if (key == 'i') {
    if(info) {
      info = false;
    } else {
      info = true;
    }
  }
  if (key == 's') {
    if(sounding) {
      jingle.pause();
      sounding = false;
    } else {
      if(path != null) {
        jingle = minim.loadFile(path);
        sounding = true;
        jingle.loop();
      }
    }
  }
}

void stop()
{
  if(path != null) {
    jingle.close();
  }
  minim.stop();

  super.stop();
}

