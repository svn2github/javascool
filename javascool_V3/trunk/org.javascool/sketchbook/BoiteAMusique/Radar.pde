class Radar {
  PFont f = createFont("Arial Bold", 12, true);

  int bx, by;        // positionnement
  int nb;         // nombre de sources à enchainer dans la boucle: de 8 à 24
  int nl;            // nombre de niveaux sonores à disposition: de 2 à 10
  int pi = 0;
  int pia;

  Radar(int nb_, int nl_) {
    bx = width / 2 + 100;
    by = height / 2;
    nb = nb_;
    nl = nl_;
  }

  Radar(int x_, int y_, int nb_, int nl_) {
    bx = x_;
    by = y_;
    nb = nb_;
    nl = nl_;
  }

  // Corps du radar
  void drawBody() {
    float xcoo, ycoo, w, h, sw;                                        // variables pour les balises du radar

    // Dessine le corps du radar
    strokeWeight(1);
    stroke(200);
    fill(153);

    smooth();
    for(int i = 1; i < 5; i = i + 1) {                                   // cercles concentriques
      if(i == 3) {
        ellipse(bx, by, 2 * bs / i, 2 * bs / i);
        ellipse(bx, by, bs / i, bs / i);
      } else
        ellipse(bx, by, bs / i, bs / i);
    }
    line(bx - bs / 2, by, bx, by);                  // lignes délimitant les différentes zones
    line(bx, by - bs / 2, bx, by);
    line(bx + bs / 2, by, bx, by);
    line(bx, by + bs / 2, bx, by);

    line(bx - sqrt(2) * bs / 4, by - sqrt(2) * bs / 4, bx, by);
    line(bx + sqrt(2) * bs / 4, by - sqrt(2) * bs / 4, bx, by);
    line(bx + sqrt(2) * bs / 4, by + sqrt(2) * bs / 4, bx, by);
    line(bx - sqrt(2) * bs / 4, by + sqrt(2) * bs / 4, bx, by);

    rect(bx - (bs / 30) / 2, by - (bs / 30) / 2, bs / 30, bs / 30);                    // centre du radar

    // les balises à chaque quartier
    sw = (bs / 40);                                                      // cote des petits carrés
    noSmooth();
    int i = 1;
    while(i < 5) {
      if(i == 1) {
        w = 8 * sw;
        h = 2 * sw;
        xcoo = bx - bs / 2 - w * 3 / 4;
        ycoo = by - sw;
      } else if(i == 2) {
        w = 2 * sw;
        h = 8 * sw;
        xcoo = bx - sw;
        ycoo = by + (bs / 2) - h * 1 / 4;                           // h = largeur des balises; w = longueur des balises
      } else if(i == 3) {
        w = 2 * sw;
        h = 8 * sw;
        xcoo = bx - sw;
        ycoo = by - (bs / 2) - h * 3 / 4;
      } else {
        w = 8 * sw;
        h = 2 * sw;
        xcoo = bx + bs / 2 - w * 1 / 4;
        ycoo = by - sw;
      }
      rect(xcoo, ycoo, w, h);
      for(int j = 1; j < 5; j = j + 1) {
        if(i == 1)
          rect(xcoo + j * (h - sw) / 2 + (j - 1) * sw, ycoo + (h - sw) / 2, sw, sw);
        else if(i == 2)
          rect(xcoo + (w - sw) / 2, ycoo + (j + 1) * (w - sw) / 2 + (j) * sw, sw, sw);
        else if(i == 3)
          rect(xcoo + (w - sw) / 2, ycoo + (j) * (w - sw) / 2 + (j - 1) * sw, sw, sw);
        else
          rect(xcoo + (j + 1) * (h - sw) / 2 + (j) * sw, ycoo + (h - sw) / 2, sw, sw);
      }
      i = i + 1;
    }
    smooth();
  }
  // Centre s'illumine en fonction du volume sonore ambiant
  void drawCenter(float level_) {
    noStroke();
    fill(255, 50);
    ellipse(bx, by, level_, level_);
    ellipse(bx, by, level_ - level_ / 4, level_ - level_ / 4);
    ellipse(bx, by, level_ / 2, level_ / 2);
    ellipse(bx, by, level_ / 4, level_ / 4);
    strokeWeight(2);
  }
  // Visualise l'ensemble des sources à disposition
  void displayLib() {
    sources = new Source[3][freqs.tab[0].length];

    fill(255, 0, 0);
    strokeWeight(1);
    stroke(255);
    rectMode(CORNER);
    for(int i = 0; i < 3; i++)
      rect(10, by -35 + i * 120, 210, 20);
    textAlign(LEFT);
    fill(255);
    text(" - " + freqs.tab[0].length + " notes de piano -\n", 10, by-20);
    text(" - " + freqs.tab[1].length + " bips digitaux (Hz) -\n", 10, by + 100);
    text(" - " + freqs.tabE.length + " enregistrements -\n", 10, by + 220);
    fill(50);
    noStroke();
    for(int i = 0; i < 3; i++)
      rect(10, by - 35 + 25 + i * (120), 220, 90);
     // A simplifier!!
    for(int i = 0; i < freqs.tab[0].length; i++) {
      sources[0][i] = new Source(10 + (i % 7) *30, by + 10 + ((i - (i % 7)) / 7) *20, 0, i);
      sources[1][i] = new Source(10 + (i % 7) *30, by + 100 + 30 + ((i - (i % 7)) / 7) *20, 1, i);
      sources[2][i] = new Source(10 + (i % 3) *70, by + 220 + 30 + ((i - (i % 3)) / 3) *20, 2, i);
      if(mySource[1] == i) {
        fill(0, 190, 0);
        if(mySource[0] == 0) {
          text(" " + freqs.tabN[i] + " ", sources[0][i].x, sources[0][i].y);
          fill(255);
          text(" " + int (freqs.tab[1][i]) + " ", sources[1][i].x, sources[1][i].y);
          if(i < freqs.tabE.length)
            text(" " + freqs.tabE[i] + " ", sources[2][i].x, sources[2][i].y);
        } else if(mySource[0] == 1) {
          text(" " + int (freqs.tab[1][i]) + " ", sources[1][i].x, sources[1][i].y);
          fill(255);
          text(" " + freqs.tabN[i] + " ", sources[0][i].x, sources[0][i].y);  
          if(i < freqs.tabE.length)
            text(" " + freqs.tabE[i] + " ", sources[2][i].x, sources[2][i].y);
        } else {
          text(" " + freqs.tabE[i] + " ", sources[2][i].x, sources[2][i].y);
          fill(255);
          text(" " + freqs.tabN[i] + " ", sources[0][i].x, sources[0][i].y);
          text(" " + int (freqs.tab[1][i]) + " ", sources[1][i].x, sources[1][i].y);
        }
      } else {
        fill(255);

        text(" " + freqs.tabN[i] + " ", sources[0][i].x, sources[0][i].y);

        text(" " + int (freqs.tab[1][i]) + " ", sources[1][i].x, sources[1][i].y);
        if(i < freqs.tabE.length)
          text(" " + freqs.tabE[i] + " ", sources[2][i].x, sources[2][i].y);
      }
    }
  }
  /** Créer les sources sonores
   */
  void addSources(int nb_, int nl_) {
    pix = new Pixel[nb_][nl_];
    float x_, y_;
    int sw = 1;
    // AudioSample kicki;
    for(int i = 0; i < nb_; i++)
      for(int j = 0; j < nl_; j++) {
        float a = (width / (30) + j * width / (nl_ * 5));
        x_ = a * cos(i * PI / (nb_ / 2)) + (bx);
        y_ = a * sin(i * PI / (nb_ / 2)) + (by);
        if(i < freqs.tab[0].length) {
          if(i < filenames.length)
            pix[i][j] = new Pixel(x_, y_, w, sw, freqs.tab[0][i], freqs.tab[1][i], freqs.tabE[i]);
          else {
            int k = i - filenames.length;
            pix[i][j] = new Pixel(x_, y_, w, sw, freqs.tab[0][i], freqs.tab[1][i], freqs.tabE[k]);
          }
        } else {
          int k = i - freqs.tab[0].length;
          pix[i][j] = new Pixel(x_, y_, w, sw, freqs.tab[0][k], freqs.tab[1][k], freqs.tabE[i]);
        }
      }
    initSources();
  }
  /** Initialise les sources sonores (les trace ttes d'un coup)
   */
  void initSources() {
    for(int i = 0; i < nb; i++)
      for(int j = 0; j < nl; j++) {
        pix[i][j].init();
        pix[i][j].changeColor(0);
        pix[i][j].display();
      }
  }
  /** Activation des sources sonores par le signal de balayage
   */
  void activateSources(int vit_, int nb_, int nl_) {
    if(frameCount % vit == 0) {
      if(pi == nb_) {
        pi = 0;
        pia = nb_ - 1;
      }
      for(int j = 0; j < nl_; j++) {
        pix[pi][j].changeColor(1);
        pix[pi][j].display();
        pix[pia][j].changeColor(0);
        pix[pia][j].display();
      }
      pia = pi;
      pi++;
    }
  }
}
