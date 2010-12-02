/* Fonctions relatives à l'interface. */

void mouseMoved() {
  if(signal1.sounding) {
    signal1.changeValue();
    signal1.printV();
  } else if(record1.sounding) {
    record1.changeValue();
    record1.printV();
  }
}
/** Stop tout son  */
void StopAnySound() {
  if(signal1.sounding) {
    signal1.switchOff();
    sig = "null";
  } else if(record1.sounding)
    record1.switchOff();
  if(signal2.sounding)
    signal2.switchOff();
}
// Lance l'analyse spectrale
void launchFFT() {
  fft = new FFT(in.bufferSize(), in.sampleRate());

  stroke(0);

  fft.logAverages(60, 6); // /(screen.width/2)); //6 pour screen.width/2

  w = width / fft.avgSize();
  strokeWeight(w);
  strokeCap(SQUARE);

  background(0);
  fade = get(0, 0, width, height);  // fade = get(0, 0, 100, 100);

  rWidth = width * 0.99;
  rHeight = height * 0.99;
}
/** Tracé 3D du spectre au fil du temps. */
void drawFFT(String n) {
  String type = n;
  float factor;
  strokeWeight(10);
  tint(250, 250);   // gris,alpha sinon (255, 150, 0, 250)
  image(fade, (width - rWidth) / 2, (height - rHeight) / 2, rWidth, rHeight);
  noTint();
  if(n.equals("out")) {
    fft.forward(out.mix);
    factor = (signal1.volume + 20);
  } else if(n.equals("player")) {
    fft.forward(player.mix);
    factor = (record1.volume + 20);
  } else {
    fft.forward(in.mix);
    factor = 20;
  }
  stroke(240, 240, 240);
  for(int i = 0; i < fft.avgSize(); i++)
    line((i * w) + (w / 2), 2 * height / 3, (i * w) + (w / 2), 2 * height / 3 - fft.getAvg(i) * factor);
  fade = get(0, 0, width, height);

  stroke(250, 70, 0);
  textFont(f, 14);
  fill(255);
  text("100", width / 10, 2 * height / 3 + height / 30);
  text("125", width / 6, 2 * height / 3 + height / 30);
  text("250", width / 5 + width / 12, 2 * height / 3 + height / 30);
  text("500", width / 2 - width / 12, 2 * height / 3 + height / 30);
  text("1000", width / 2 + width / 36, 2 * height / 3 + height / 30);
  text("2000", 2 * width / 3 - width / 100, 2 * height / 3 + height / 30);
  text("4000", 4 * width / 5 - width / 50, 2 * height / 3 + height / 30);
  text("8000  Hz", width - width / 10, 2 * height / 3 + height / 30);
  for(int i = 0; i < fft.avgSize(); i++)
    line((i * w) + (w / 2), 2 * height / 3, (i * w) + (w / 2), 2 * height / 3 - fft.getAvg(i) * factor);
  strokeWeight(0.5);
}
/** Tracé temporel du signal. */
void drawSignal(String n) {
  stroke(255);
  strokeWeight(1);
  int k;
  if(n.equals("out"))
    k = out.bufferSize();
  else if(n.equals("player"))

    k = player.bufferSize();
  else
    k = in.bufferSize();
  for(int i = 0; i < k - 1; i++) {
    // println("buffer: " + in.bufferSize());

    float x1 = map(i, 0, k, 0, width);
    float x2 = map(i + 1, 0, k, 0, width);
    if(n.equals("out")) {
      line(x1, 40 + out.left.get(i) * (signal1.volume + 20) * 5, x2, 40 + out.left.get(i + 1) * (signal1.volume + 20) * 5);
      line(x1, 120 + out.right.get(i) * (signal1.volume + 20) * 5, x2, 120 + out.right.get(i + 1) * (signal1.volume + 20) * 5);
    } else if(n.equals("player")) {
      line(x1, 40 + player.left.get(i) * (record1.volume + 20) * 3, x2, 40 + player.left.get(i + 1) * (record1.volume + 20) * 3);
      line(x1, 120 + player.right.get(i) * (record1.volume + 20) * 3, x2, 120 + player.right.get(i + 1) * (record1.volume + 20) * 3);
    } else {
      line(x1, 40 + in.left.get(i) * 100, x2, 40 + in.left.get(i + 1) * 100);
      line(x1, 120 + in.right.get(i) * 100, x2, 120 + in.right.get(i + 1) * 100);
    }
  }
  strokeWeight(0.5);
}
/* Fonctions pour javascool. */

/** Joue un signal de type choisi.
 * @param c : numero du canal à lancer: 1, 2 ou 3.
 * @param n nom du type: sinus, square, triangle, saw, white noise.
 * @param f fréquence du signal.
 * @param a amplitude du signal.
 */
public static void playSignal(int c, String n, double f, double a) {
  swicth(c) {
  case 1:
    proglet.signal1.setSignal(n, (float) f, (float) a);
    break;
  case 2:
    proglet.signal2.setSignal(n, (float) f, (float) a);
    break;
  case 3:
    proglet.signal3.setSignal(n, (float) f, (float) a);
    break;
  }
}
/** Joue un enregistrement de son choix.
 * @param path Nom de l'extrait
 */
public static void playRecord(String path) {
  proglet.record1.setRecord(path);
}
/** Applique un filtre avec une fréquence de coupure ajustable sur l'enregistrement de son choix.
 * @param path Nom de l'extrait
 * @param f fréquence de coupure du filtre (entre 100 et 10000, sinon rien)
 */
public static void setFilter(String path, double fc) {
  proglet.record1.setFilter(path, (float) fc);
}
/** Arrête l'émission sonore. */
public static void playStop() {
  proglet.StopAnySound();
}
static ExplorationSonore proglet;

