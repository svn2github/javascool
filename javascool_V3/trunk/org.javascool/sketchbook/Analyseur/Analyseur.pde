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
  size(700, 270, P2D);
  background(0);
  
  f = createFont("Arial Bold",14,true);
  minim = new Minim(this);
  in = minim.getLineIn(Minim.STEREO, 512);

  nb = 128;
  
  
  // Test Sine
  // get a line out from Minim, default bufferSize is 1024, default sample rate is 44100, bit depth is 16
  out = minim.getLineOut(Minim.STEREO,2048);
  // create a sine wave Oscillator, set to 440 Hz, at 0.5 amplitude, sample rate from line out
  sine = new SineWave(440, 0.5, out.sampleRate());
  // set the portamento speed on the oscillator to 200 milliseconds
  sine.portamento(200);
  // add the oscillator to the line out
  out.addSignal(sine);
  
      fftS = new FFT(out.bufferSize(), out.sampleRate());
  // use 'nb' averages (between 64 and 1024).
  // the maximum number of averages we could ask for is half the spectrum size. 
  fftS.linAverages(nb);
  

  rectMode(CORNERS);
}

void draw()
{
  fill(255);
  
   
  // Test Sine
  fftS.forward(out.mix);
  int w = int(fftS.timeSize()/nb);
  for(int i = 0; i < fftS.avgSize(); i++)
  {
    // draw a rectangle for each average, multiply the value by 5 so we can see it better
    rect(i*w, height, i*w + w, height - fftS.getAvg(i)*5);
  }
  
  //update();
  
  printInfo();
 
  
}

void printInfo() {

  if(info) {
    fill(0);
    rect(0, 100, width, -10);
    textFont(f);
    textAlign(LEFT);
    fill(255, 150, 0);
    text("\n " + 
      ".  Clic Gauche : joue enregistrement (Nina Hagen, ''Born in Xixas'') \n " +
      ".  Clic Droit : stop la lecture, analyse de l'entrée micro \n " +
      ".  Clic Centre + souris à droite/gauche: élargit/rafine les bandes de fréquences (entre 32 et 128) ", 10, 15);

  } else {
    fill(0);
      rect(0, 100, width, -10);
      textFont(f);
      textAlign(LEFT);
      fill(255, 150, 0);
      text("- INFO - ", 10, 15);
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
  println(w);

  for(int i = 0; i < fft.avgSize(); i++)
  {
    // draw a rectangle for each average, multiply the value by 5 so we can see it better
    rect(i*w, height, i*w + w, height - coeff*fft.getAvg(i));
  }
    
}

void mouseMoved() {
  // Print info
  if(mouseX<50 && mouseY<20) {
    info = true;
  } else {
    info = false;
    // Print values of nb of frequencies per band and frequency pointed by the mouse
    //int w = int((fs+nb)/(nb));
    int w = int(fftS.specSize()/nb);
    println("/ " + nb+ "/ " +fftS.avgSize() + "/ " +fftS.specSize());
    textFont(f,12);
    fill(0);
    rect(width, 150, width/2+80, height/3);
    fill(255);
    //text("nb = "+ nb + " / Fréq. = " + w*(fft.avgSize()*mouseX)/width + " Hz", width-175, int(height/2));
    text("nb = "+ nb + " / Fréq. = " + fs/2*w/fftS.avgSize() + " Hz", width-175, int(height/2));
  }
  
}

void mouseReleased() {
  // Load file
  if(mouseButton == LEFT) {
    background(0);
    String pathN;
    //pathN = selectInput();
    pathN = "NinaHagen_BornInXixas_ext.mp3";
    if(!(pathN==path) && pathN != null && !(sounding)) {
      path = pathN;
      jingle = minim.loadFile(path);
      sounding = true;
    }
    // loop the file
    jingle.loop();
  }
  // Micro in
  if(mouseButton == RIGHT) {
    if(sounding) {
      jingle.pause();
      sounding = false;
      path = null;
      fill(0);
      rect(0, height, width/2+200, -10);
      update();
    } 
  }
  // Change width of frequency band
  if(mouseButton == CENTER) { 
    println(nb);
    if(mouseX>width/2) {
      if(nb<128) {
        background(0);
        nb*=2;
      }
    } else {
      if(nb>32) {
        background(0);
        nb/=2;
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

