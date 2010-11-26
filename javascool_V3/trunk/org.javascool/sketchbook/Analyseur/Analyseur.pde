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

  rectMode(CORNERS);
}

void draw()
{
  fill(255);
  
  update();
  
  printInfo();
 
  
}

void printInfo() {

  if(info) {
    fill(0);
    rect(0, 100, width/2+200, -10);
    textFont(f,12);
    textAlign(LEFT);
    fill(255, 150, 0);
    text(" - COMMANDES - \n " + 
      ".  L / l : chargement d'un fichier son (Nina Hagen, Born in Xixas) \n " +
      ".  + / - : raffine/élargit les bandes de fréquences pour la FFT (entre 64 et 256) \n " +
      ".  S / s : stop la lecture, analyse de l'entrée micro \n " +
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

void update() {
    int coeff;
    if(path != null) {
    fft = new FFT(jingle.bufferSize(), jingle.sampleRate());
    fs = jingle.sampleRate();
    fft.linAverages(nb);
    fft.forward(jingle.mix);
    coeff = 1;
  } 
  else {
    fft = new FFT(in.bufferSize(), in.sampleRate());
    fft.linAverages(nb);
    fft.forward(in.mix);
    coeff = 5;
  }
  int w = int(fft.specSize()/nb);

  for(int i = 0; i < fft.avgSize(); i++)
  {
    // draw a rectangle for each average, multiply the value by 5 so we can see it better
    rect(i*w, height, i*w + w, height - coeff*fft.getAvg(i));
  }
    
}

void mouseMoved() {
  int w = int((fs+nb)/(nb));
  //println("/ " + nb+ "/ " +fft.avgSize() + "/ " +fft.specSize());
  textFont(f,12);
  fill(0);
  rect(width, 150, width/2+80, height/3);
  fill(255);
  text("Fréquence pointée = " + w*(fft.avgSize()*mouseX)/width, width-170, int(height/2));
}

void keyPressed()                                                  
{
  if (key == '+') {
    // use 'nb' averages (between 32 and 256).
    if(nb<128) {
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

    //pathN = selectInput();
    pathN = "NinaHagen_BornInXixas_ext.mp3";
    if(pathN != null && !(sounding)) {
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
      path = null;
      fill(0);
      rect(0, height, width/2+200, -10);
      update();
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

