/**
 *
 * Fonctions de l'interface
 *
 */
void interfaceAlice() {
  // Information

  // this.frame.setUndecorated(true);
  controlP5.addButton("InfoA", 10, 0, 0, 75, 15).setId(1);
  controlP5.controller("InfoA").setCaptionLabel("A L I C E : info");   // change le titre
  controlP5.controller("InfoA").captionLabel().toUpperCase(false);
  controlP5.controller("InfoA").captionLabel().setControlFont(font);   // change la police  controlP5.controller("InfoA").captionLabel().setControlFontSize(8);
  controlP5.controller("InfoA").setColorActive(myRed);
  controlP5.controller("InfoA").setColorBackground(myOr);
  boxA = controlP5.addButton("buttonValue", 0, 0, -170, 60, 40);
  boxA.setId(2);
  boxA.setWidth(width / 2);
  boxA.setHeight(170);
  boxA.setColorActive(0);
  boxA.setColorBackground(0);
  boxA.setColorLabel(255);
  boxA.captionLabel().setControlFont(font);
  boxA.captionLabel().setControlFontSize(12);
  boxA.captionLabel().toUpperCase(false);
  boxA.setCaptionLabel("Tu es Alice. Nous te proposons d'expérimenter le CODAGE et le DECODAGE de messages.\n \n" +
                       "Tout d'abord, tu dois générer une 'clé publique' et 'une clé privée'.\n \n" +
                       "Tu divulgueras ensuite la clé publique à Bob, et tu garderas la clé privée précieusement. \n \n" +
                       "Bob encryptera son message secret à l'aide de la clé publique.\n \n" +
                       "Seul toi pourras décrypter le message au moyen de la clé privée!");
  boxA.captionLabel().style().marginLeft = 30;
  boxA.captionLabel().style().marginTop = -62;

  // Choisir/generer p et q
  pq_size = int (random(4, 10));
  myTextfield_p = controlP5.addTextfield("P et Q sont des nombres premier", 100, height / 2 - height / 4, width / 2 / 10, 20);
  myTextfield_p.setColorBackground(myGreen);
  // myTextfield_p.captionLabel().setControlFont(font);
  myTextfield_q = controlP5.addTextfield(" ", 100 + width / 2 / 8 + 15, height / 2 - height / 4, width / 2 / 10, 20);
  myTextfield_q.setColorBackground(myGreen);
  // myTextfield_q.captionLabel().setControlFont(font);
  // controlP5.addButton("reset",0,n.width/2-(100+100),150,100,20).setCaptionLabel("1. Genere P et Q");
  controlP5.addButton("reset", 0, width / 2 - (100 + width / 2 / 4), height / 2 - height / 4, width / 2 / 4, 20);
  controlP5.controller("reset").captionLabel().setControlFont(font);
  controlP5.controller("reset").captionLabel().setControlFontSize(11);
  controlP5.controller("reset").setCaptionLabel("    1.  Génère   P  et  Q");
  controlP5.controller("reset").setColorBackground(myGreen);
  controlP5.controller("reset").setColorActive(myGreenA);
  myTextfield_p.setText("P = " + p + " ");
  myTextfield_q.setText("Q = " + q + " ");

  // Calculer n=pxq
  myTextfield_n = controlP5.addTextfield("N = P * Q", 100, height / 2 - height / 4 + (20 * 2 + 4), width / 2 / 3, 20);
  myTextfield_n.setColorBackground(myGreen);
  // myTextfield_n.captionLabel().setControlFont(font);
  // controlP5.addButton("calculate_n",0,width/2-(100+100),200,100,20).setCaptionLabel("2. Calcule n");
  controlP5.addButton("calculate_n", 0, width / 2 - (100 + width / 2 / 4), height / 2 - height / 4 + (20 * 2 + 4), width / 2 / 4, 20);
  controlP5.controller("calculate_n").captionLabel().setControlFont(font);
  controlP5.controller("calculate_n").captionLabel().setControlFontSize(11);
  controlP5.controller("calculate_n").setCaptionLabel("        2.  Calcule   n");
  controlP5.controller("calculate_n").setColorBackground(myGreen);
  controlP5.controller("calculate_n").setColorActive(myGreenA);

  // Generer e tel qu'il soit premier avec (p-1)*(q-1)
  myTextfield_e = controlP5.addTextfield("e est premier avec (p-1) * (q-1)", 100, height / 2 - height / 4 + (20 * 4 + 4 * 2), width / 2 / 3, 20);
  myTextfield_e.setColorBackground(myGreen);
  // myTextfield_e.captionLabel().setControlFont(font);
  // controlP5.addButton("launch_e",0,width/2-(100+100),250,100,20).setCaptionLabel("3. Genere e");
  controlP5.addButton("launch_e", 0, width / 2 - (100 + width / 2 / 4), height / 2 - height / 4 + (20 * 4 + 4 * 2), width / 2 / 4, 20);
  controlP5.controller("launch_e").captionLabel().setControlFont(font);
  controlP5.controller("launch_e").captionLabel().setControlFontSize(11);
  controlP5.controller("launch_e").setCaptionLabel("        3.  Génère   e");
  controlP5.controller("launch_e").setColorBackground(myGreen);
  controlP5.controller("launch_e").setColorActive(myGreenA);

  // (n, e) est la cle publique

  // Calculer d selon:
  // Il existe un relatif entier m, tel que e × d + m × (p - 1)(q - 1) = 1
  // d est la cle privee
  myTextfield_d = controlP5.addTextfield("d verifie: e * d + m * (p-1) * (q-1) = 1", 100, height / 2 - height / 4 + (20 * 6 + 4 * 3), width / 2 / 3, 20);
  myTextfield_d.setColorBackground(myGreen);
  // myTextfield_d.captionLabel().setControlFont(font);
  // controlP5.addButton("launch_d",0,width/2/2-(100+100),300,100,20).setCaptionLabel("4. Calcule d");
  controlP5.addButton("launch_d", 0, width / 2 - (100 + width / 2 / 4), height / 2 - height / 4 + (20 * 6 + 4 * 3), width / 2 / 4, 20);
  controlP5.controller("launch_d").captionLabel().setControlFont(font);
  controlP5.controller("launch_d").captionLabel().setControlFontSize(11);
  controlP5.controller("launch_d").setCaptionLabel("        4.  Calcule   d");
  controlP5.controller("launch_d").setColorBackground(myGreen);
  controlP5.controller("launch_d").setColorActive(myGreenA);

  // Revèle les clés privée et publique:
  myTextfield_kpr = controlP5.addTextfield("Cle privee", 100, height / 2 + 20 + 10, width / 2 / 4, 20);
  myTextfield_kpr.setColorBackground(myRed);
  myTextfield_kpu = controlP5.addTextfield("Cle publique", 2 * width / 2 / 3 - 100, height / 2 + 20 + 10, width / 2 / 3, 20);
  myTextfield_kpu.setColorBackground(myRed);

  // Cacher les infos confidentielles
  controlP5.addButton("hideAll", 0, width / 2 - (200 + 100), height / 2 + (20 * 4), 200, 20);
  controlP5.controller("hideAll").setCaptionLabel("  Masquer  toute  information");
  controlP5.controller("hideAll").captionLabel().setControlFont(font);
  controlP5.controller("hideAll").captionLabel().setControlFontSize(11);
  controlP5.controller("hideAll").setColorBackground(myRed);
  controlP5.controller("hideAll").setColorActive(myOr);

  // Recevoir le message encrypté
  myTextfield_EncMessBitsA = controlP5.addTextfield("Reception du message encrypte", 100, height / 2 + (20 * 7), width / 2 - 100 * 2, 30);
  // Décrypter
  controlP5.addButton("decrypt_m", 0, width / 2 - (3 * width / 2 / 10 + 100), height / 2 + (20 * 10), 3 * width / 2 / 10, 20);
  // controlP5.controller("decrypt_m").setCaptionLabel("Decryptage du message");
  controlP5.controller("decrypt_m").captionLabel().setControlFont(font);
  controlP5.controller("decrypt_m").captionLabel().setControlFontSize(11);
  controlP5.controller("decrypt_m").setCaptionLabel("  Décryptage  du  message");

  myTextfield_DecMessBits = controlP5.addTextfield("Ceci est le message de Bob", 100, height / 2 + (20 * 11 + 10), width / 2 - 100 * 2, 30);
  myTextfield_DecMessBits.valueLabel().toUpperCase(true);
}
void interfaceBob() {
  // controlWindow = controlP5.addControlWindow("Bob",-5,0,width/2,height);

  // controlWindow.setUndecorated(true);
  // controlWindow.hideCoordinates();
  // controlWindow.setBackground(color(150,150,150));
  // Information
  controlP5.addButton("InfoB", 10, width / 2 + 1, 0, 75, 15).setId(1);
  controlP5.controller("InfoB").setCaptionLabel("B O B : info");   // change the content
  controlP5.controller("InfoB").captionLabel().toUpperCase(false);
  // controlP5.controller("InfoB").setWindow(controlWindow);
  controlP5.controller("InfoB").captionLabel().setControlFont(font);   // change the font
  controlP5.controller("InfoB").captionLabel().setControlFontSize(10);
  controlP5.controller("InfoB").setColorActive(myRed);
  controlP5.controller("InfoB").setColorBackground(myOr);
  boxB = controlP5.addButton("buttonValueB", width / 2, width / 2 + 1, -110, 60, 40);
  boxB.setId(2);
  boxB.setWidth(width / 2);
  boxB.setHeight(110);
  boxB.setColorActive(150);
  boxB.setColorBackground(150);
  boxB.setColorLabel(255);
  // boxB.setWindow(controlWindow);
  boxB.captionLabel().setControlFont(font);
  boxB.captionLabel().setControlFontSize(12);
  boxB.captionLabel().toUpperCase(false);
  boxB.setCaptionLabel("Tu es Bob. Nous te proposons d'expérimenter le CODAGE et le DECODAGE de messages.\n \n" +
                       "Tu vas recevoir une 'clé', dite 'publique', qui va te permettre d'encrypter un message secret.\n \n" +
                       "Apres encryptage du message, transmet-le à Alice qui essayera de le décrypter! ");
  boxB.captionLabel().style().marginLeft = 30;
  boxB.captionLabel().style().marginTop = -20;

  // Recevoir la clé publique
  myTextfield_kpuB = controlP5.addTextfield("Cle public pour l'encryptage", width / 2 + 1 + 100, height / 2 - height / 4, width / 2 / 3, 20);
  myTextfield_kpuB.setColorBackground(myRed);
  // myTextfield_kpuB.setWindow(controlWindow);

  // Message secret
  myTextfield_Mess = controlP5.addTextfield("Ton message secret", width / 2 + 1 + 100, height / 2 - height / 4 + (20 * 4), width / 2 - 100 * 2, 30);
  myTextfield_Mess.valueLabel().toUpperCase(true);
  // myTextfield_Mess.captionLabel().fixedSize(12);
  // myTextfield_Mess.valueLabel().setFont(ControlP5.grixel);
  // myTextfield_Mess.valueLabel().setColorBackground(myOr);

  // myTextfield_Mess.setWindow(controlWindow);
  myTextfield_Mess.setText("Ceci est un message secret.. ");
  // myTextfield_Mess.valueLabel().textHeight();
  // myTextfield_Mess.valueLabel().setWidth(50);
  // myTextfield_Mess.valueLabel().setHeight(50);

  // Traduire en chiffres
  controlP5.addButton("translate_m", 0, width - (240 + 100), height / 2 - height / 4 + (20 * 7), 240, 20);
  controlP5.controller("translate_m").captionLabel().setControlFont(font);
  controlP5.controller("translate_m").captionLabel().setControlFontSize(11);
  controlP5.controller("translate_m").setCaptionLabel(" Traduction  du  message  en  chiffres");
  // controlP5.controller("translate_m").setWindow(controlWindow);
  myTextfield_MessBits = controlP5.addTextfield("     ", width / 2 + 1 + 100, height / 2 - height / 4 + (20 * 8 + 10), width / 2 - 100 * 2, 30);

  // myTextfield_MessBits.setWindow(controlWindow);

  // Encrypter le message
  controlP5.addButton("encrypt_m", 0, width - (165 + 100), height / 2 - height / 4 + (20 * 11 + 10), 165, 20);
  controlP5.controller("encrypt_m").captionLabel().setControlFont(font);
  controlP5.controller("encrypt_m").captionLabel().setControlFontSize(11);
  controlP5.controller("encrypt_m").setCaptionLabel(" Encryptage  du  message");
  // controlP5.controller("encrypt_m").setWindow(controlWindow);
  myTextfield_EncMessBits = controlP5.addTextfield("      ", width / 2 + 1 + 100, height / 2 - height / 4 + (20 * 13), width / 2 - 100 * 2, 30);
  // myTextfield_EncMessBits.setWindow(controlWindow);
  // Envoyer le message encryté
  controlP5.addButton("send_m", 0, width - (185 + 100), height / 2 - height / 4 + (20 * 17), 185, 20);
  controlP5.controller("send_m").captionLabel().setControlFont(font);
  controlP5.controller("send_m").captionLabel().setControlFontSize(11);
  controlP5.controller("send_m").setCaptionLabel(" Envoi  du  message  encrypté");
  controlP5.controller("send_m").setColorBackground(myRed);
  // controlP5.controller("send_m").setWindow(controlWindow);
  controlP5.controller("send_m").setColorActive(myOr);
}
void ouvreFenetreInfo() {
  boxA.position().y += ((isOpen == true ? 15 + 1 : -170) - boxA.position().y) * 0.2;
  boxB.position().y += ((isOpenB == true ? 15 + 1 : -110) - boxB.position().y) * 0.2;
  // boxB.setWindow(controlWindow);
}
public void InfoA(float theValueA) {
  println("a button event. " + theValueA);
  isOpen = !isOpen;
  controlP5.controller("InfoA").setCaptionLabel((isOpen == true) ? "FERMER info" : "A L I C E : info");
}
public void InfoB(float theValueB) {
  println("a button event. " + theValueB);
  isOpenB = !isOpenB;
  controlP5.controller("InfoB").setCaptionLabel((isOpenB == true) ? "fermer Info" : "B O B : info");
}
void hideAll(int theValue) {
  myTextfield_p.clear();
  myTextfield_q.clear();
  myTextfield_n.clear();
  myTextfield_d.clear();
  myTextfield_e.clear();
  myTextfield_kpr.clear();   // clé privée
}
/** Utilisé pour fermer la fenêtre secondaire de l'interface, par JavaScool. */

/*public processing.core.PApplet getControl() {
 *  controlWindow.hide();
 *  return controlWindow.papplet();
 *
 *  }*/

/** Créer une clé privée et une clé publique pour le codage et décodage de messages
 * @return les clés
 */

// Taille pour l'insertion dans JavaScool
public static final int WIDTH = 1024, HEIGHT = 700;
