/** Définit un signal sonore de forme prédéfini. */
class signal {
  /** Construction du signal et de son interaction graphique. */
  signal() {
    out.sound();
  }
  float volume;
  float frequence;
  SineWave sinus_;
  SquareWave square_;
  SawWave saw_;
  WhiteNoise wnoise_;

  String type;
  boolean sounding = false;

  /** Joue un signal de type choisi
   * @param n nom du type: sinus, square, triangle, saw, white noise.
   * @param f fréquence du signal.
   * @param a amplitude du signal.
   */
  public void setSignal(String n, float f, float a) {
    type = n;
    if(sounding)
      switchOff();
    else if(record1.sounding)
      record1.switchOff();
    // Créer un oscillateur sinusoidale avec une fréquence de 1000Hz, une amplitude de 1.0, et une fréquence d'échantillonage callée sur la ligne out
    if(n.equals("sinus")) {
      sinus_ = new SineWave(f, a, out.sampleRate());
      sinus_.portamento(20);
      out.addSignal(sinus_);
    } else if(n.equals("carré")) {
      square_ = new SquareWave(f, a, out.sampleRate());
      square_.portamento(20);
      out.addSignal(square_);
    } else if(n.equals("scie")) {
      saw_ = new SawWave(f, a, out.sampleRate());
      saw_.portamento(20);
      out.addSignal(saw_);
    } else if(n.equals("bruit")) {
      wnoise_ = new WhiteNoise(a);
      out.addSignal(wnoise_);
    }
    sounding = true;
  }
  /** Mise à jour des valeurs lors du déplacement de la souris. */
  void changeValue() {
    frequence = map(mouseX, 0, width, 100, 4000);
    // constrain(mouseX, 0, width-500);

    volume = map(mouseY, 0, height, 0.2, 0);
    if(type.equals("sinus")) {
      sinus_.setFreq(frequence);
      sinus_.setAmp(volume);
    } else if(type.equals("carré")) {
      square_.setFreq(frequence);
      square_.setAmp(volume);
    } else if(type.equals("scie")) {
      saw_.setFreq(frequence);
      saw_.setAmp(volume);
    } else if(type.equals("bruit"))
      wnoise_.setAmp(volume);
  }
  /** Affichage de la valeur dans l'interface. */
  void printV() {
    float vol = (volume) / 0.4;
    fill(0);
    rect(0, height - 175, width / 2, 30);
    fill(myOr);
    if(type.equals("bruit"))
      text("Vol.: " + vol + " ", 10, height - 155);
    else
      text(" Freq.: " + frequence + " Hz  -  Vol.: " + vol + " ", 10, height - 155);
  }
  /** Arrêt de la sortie sonore. */
  void switchOff() {
    out.noSound();
    out.clearSignals();
    sounding = false;
  }
}
