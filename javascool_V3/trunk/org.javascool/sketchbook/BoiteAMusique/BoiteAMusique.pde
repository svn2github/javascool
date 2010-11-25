import arb.soundcipher.*;
import ddf.minim.*;
import ddf.minim.signals.*;

SoundCipher sc = new SoundCipher(this);
Minim minim = new Minim(this);
AudioInput in;
AudioOutput out;

Freq freqs;
Pixel[][] pix;
Radar Radar_;
Potar potarVol;
Source[][] sources;

int w; // depending on the width of the interface
int vit = 10;

int bs;
int[] mySource = new int[2];
boolean hasChanged = false;

String[] filenames;
String path;

void setup() {
  size(1000, 600); // (screen.width-50,screen.height-50);//900,616);
  background(50);
  frameRate(30);
  mySource[0] = 0;
  mySource[1] = 0;

  in = minim.getLineIn(Minim.STEREO, 1648);
  out = minim.getLineOut(Minim.STEREO);
  path = sketchPath + "/data/effects";
  filenames = listFileNames(path);
  freqs = new Freq();

  smooth();

  potarVol = new Potar(40, 170);
  w = width / 100;
  bs = 4 * width / 9; // 2*screen.height/3;

  // Radar
  Radar_ = new Radar(16, 5);
  Radar_.drawBody();
  // Initialise sources
  Radar_.addSources(Radar_.nb, Radar_.nl);
}
void draw() {
  fill(153);
  strokeWeight(1);
  ellipse(Radar_.bx, height / 2, 50, 50);
  float volume = in.mix.level() * 5000;         // niveau sonore d'entrée
  if(volume > 50)
    volume = 50;
  Radar_.drawCenter(volume);

  Radar_.displayLib();

  Radar_.activateSources(vit, Radar_.nb, Radar_.nl);
  potarVol.display();
  if(mousePressed) {
    if(potarVol.check(mouseX, mouseY))
      potarVol.move(mouseY);
    for(int i = 0; i < Radar_.nb; i++)
      for(int j = 0; j < Radar_.nl; j++) {
        if(pix[i][j].type == 2)
          pix[i][j].setMyAmp(-10 * (j + 1 - Radar_.nl) * potarVol.getValue());
        else if(pix[i][j].type == 1)
          pix[i][j].setMyAmp(-10 * (j + 1 - Radar_.nl) * potarVol.getValue() / 100); // .sine.setAmp(0.005);//(Radar_.nl-j+1)/10000*potarVol.getValue());
        else
          pix[i][j].setMyAmp((Radar_.nl - j + 1) * 10 * potarVol.getValue());
      }
  }
}
void mousePressed() {
  for(int i = 0; i < Radar_.nb; i++)
    for(int j = 0; j < Radar_.nl; j++)
      if(pix[i][j].pospix(mouseX, mouseY) == true) {
        pix[i][j].changeColor(0);
        pix[i][j].display();
        pix[i][j].setSource(mySource[0], mySource[1]);
      }
  for(int i = 0; i < 3; i++)
    for(int j = 0; j < freqs.tab[0].length; j++) {
      if(sources[i][j].posSource(mouseX, mouseY, i) == true) {
        // println(" " + i + " // " +j);
        mySource[0] = i;
        mySource[1] = j;
        hasChanged = true;
      } else
        hasChanged = false;
    }
}
void keyPressed() {
  if(key == ' ') // reinitialise la composition = désactive toute les sources activées

    Radar_.initSources();
   // Agit sur la vitesse de jeu
  if(keyCode == LEFT)
    vit++;
  if(keyCode == RIGHT)
    vit--;
  vit = constrain(vit, 1, 30);
  if(key == 'i') {
    println("Listing all filenames in a directory: ");

    println(filenames);
    println(filenames.length);
  }
}
// This function returns all the files in a directory as an array of Strings
String[] listFileNames(String dir) {
  File file = new File(dir);
  if(file.isDirectory()) {
    String names[] = file.list();
    return names;
  } else
    // If it's not a directory
    return null;
}
void stop() {
  minim.stop();
  super.stop();
}
