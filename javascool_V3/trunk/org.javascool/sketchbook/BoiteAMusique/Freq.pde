class Freq
{
  float[][] tab = new float[2][16];
  String[] tabN = new String[16];
  String[] tabE = new String[filenames.length];

  Freq() {
    // Notes de Piano
    tab[0][0] = 48;  // do
    tab[0][1] = 50;  // ré
    tab[0][2] = 52;  // mi..
    tab[0][3] = 53;
    tab[0][4] = 55;
    tab[0][5] = 57;
    tab[0][6] = 59;
    tab[0][7] = 60;  // do
    tab[0][8] = 62;
    tab[0][9] = 64;
    tab[0][10] = 65;
    tab[0][11] = 67;
    tab[0][12] = 69;
    tab[0][13] = 71;
    tab[0][14] = 72;  // do
    tab[0][15] = 74;

    /*for(int i=0;i<tab[0].length;i++) {
     *  tab[0][i] = 48
     *  }*/
    // Noms correspondants
    tabN[0] = "C3";  // do
    tabN[1] = "D3";  // ré
    tabN[2] = "E3";  // mi..
    tabN[3] = "F3";
    tabN[4] = "G3";
    tabN[5] = "A3";
    tabN[6] = "B3";
    tabN[7] = "C4";  // do
    tabN[8] = "D4";
    tabN[9] = "E4";
    tabN[10] = "F4";
    tabN[11] = "G4";
    tabN[12] = "A4";
    tabN[13] = "B4";
    tabN[14] = "C5";  // do
    tabN[15] = "D5";

    // Signaux carrés
    tab[1][0] = 110.00;
    tab[1][1] = 123.47;
    tab[1][2] = 130.81;
    tab[1][3] = 146.83;
    tab[1][4] = 164.81;
    tab[1][5] = 174.61;
    tab[1][6] = 196.00;
    tab[1][7] = 220.00;
    tab[1][8] = 246.94;
    tab[1][9] = 261.63;
    tab[1][10] = 293.66;
    tab[1][11] = 329.63;
    tab[1][12] = 349.23;
    tab[1][13] = 392.00;
    tab[1][14] = 440.00;
    tab[1][15] = 493.88;
    // Enregistrements
    for(int i = 0; i < filenames.length; i++)
      tabE[i] = filenames[i];
  }
}

