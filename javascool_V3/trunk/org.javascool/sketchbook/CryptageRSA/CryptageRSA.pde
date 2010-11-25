/*///////////////////////////////////////////////////////////////////////////////////
 *
 * 08.2010 Cécile P-L for Fuscia, ccl.picard@gmail.com
 * CRYPTOGRAPHIE
 * Interface pédagogique sur le codage/décodage de messages avec clés RSA
 * Interface réalisée avec ControlP5 (basée sur l'example ControlP5TextfieldAdvanced)
 *
 */                                                                                                                                                                                                                                                                                                                                                   // /////////////////////////////////////////////////////////////////////////////////


import java.awt.*;
import java.applet.*;
import java.awt.event.*;
import java.math.BigInteger;
import java.util.Random;

// Paramètres pour la méthode RSA
int pq_size;
int prime_certainty = 20;
BigInteger p, q, n, e, d, A;
BigInteger EncMessBits;

// Paramètres de l'interface
PFont pfont;
int space;
int myOr = color(255, 100, 0);
int myGreen = color(0, 100, 30);
int myGreenA = color(0, 200, 100);
int myRed = color(255, 0, 0);
int myBlue = color(120);  // (120,140,150);
int myBlueA = color(100);  // (120,165,175);

rectButton[] T0 = new rectButton[4];
TextButton[] T1 = new TextButton[4];
rectButton[] T2 = new rectButton[3];
rectButton[] T3 = new rectButton[5];
TextButton[] T4 = new TextButton[4];
TextButton[] T5 = new TextButton[2];   // infos
TextButton T6;  // = new TextButton; //masquer
boolean locked = false;
boolean info = false;

String[] ListN1 = { "1. Génère   P  et  Q", "2. Calcule N", "3. Génère E", "4. Calcule D" };
String[] ListN2 = { "Clé privée = ", "Clé publique = ", "Clé publique = " };
String[] ListN3 = { "Traduction  du  message  en  chiffres", "Encryptage du message", "E N V O I   du message encrypté", "Décryptage du message" };
String[] ListN4 = { "Tu es Alice. Nous te proposons d'expérimenter le CODAGE et le DECODAGE de messages.\n" +
                    "Tout d'abord, tu dois générer une 'clé publique' et 'une clé privée'.\n" +
                    "Tu divulgueras ensuite la clé publique à Bob, et tu garderas la clé privée précieusement. \n" +
                    "Bob encryptera son message secret à l'aide de la clé publique.\n" +
                    "Seul toi pourras décrypter le message au moyen de la clé privée!",
                    "Tu es Bob. Nous te proposons d'expérimenter le CODAGE et le DECODAGE de messages.\n" +
                    "Tu vas recevoir une 'clé', dite 'publique', qui te permettra d'encrypter un message secret.\n" +
                    "Apres encryptage du message, transmet-le à Alice qui essayera de le décrypter! " };
String lastInput = new String();

// Ce qui est lancé une fois, au départ
void setup() {
  frame = new Frame();

  size(1150, 650);
  space = 50;
  frameRate(30);
  pfont = createFont("Arial Bold", 12, true); // use true/false for smooth/no-smooth

  p = new BigInteger("0");
  q = new BigInteger("0");
  n = new BigInteger("0");
  e = new BigInteger("0");
  d = new BigInteger("0");
  A = new BigInteger("0");

  this.frame.setTitle("Alice");   // interface principale: celle d'Alice
  for(int i = 0; i < T1.length; i++) {
    T0[i] = new rectButton(100, height / 2 - height / 4 + i *40, 160, 25, myGreen);
    T1[i] = new TextButton(width / 2 - (100 + 160), height / 2 - height / 4 + i *40, 160, 25, color(255), myGreen, myGreenA, ListN1[i]);
  }
  for(int i = 0; i < T2.length; i++) {
    if(i == 2)
      T2[i] = new rectButton(width / 2 + 100, height / 2 - height / 4, 200, 25, myRed);
    else
      T2[i] = new rectButton(((i + 1) % 2) *100 + int ((i + 1) / 2) *(width / 2 - (100 + 160)) - i *40, height / 2 + height / 20, 160 + i *40, 25, myRed);
    T2[i].setText(ListN2[i]);
  }
  for(int i = 0; i < T3.length; i++) {
    if(i < 3) {
      if(i == 0)
        T3[i] = new rectButton(width / 2 + 100, height / 2 - height / 6 + i *90, width / 2 - (100 * 2), 30, myBlue);
      else
        T3[i] = new rectButton(width / 2 + 100, height / 2 - height / 6 + i *90 + int (i / 2) *30, width / 2 - (100 * 2), 60, myBlue);
    } else
      T3[i] = new rectButton(100, height / 2 - height / 4 + int (i / 2) *30 + i *90, width / 2 - (100 * 2), 30 + (i % 2) *30, myBlue);
  }
  for(int i = 0; i < T4.length; i++) {
    if(i < 2)
      T4[i] = new TextButton(width - (100 + 250), height / 2 - height / 6 + 55 + i *120, 250, 25, color(255), myBlue, myBlueA, ListN3[i]);
    // T4[i] = new rectButton(width/2+100, height/2-height/8 +i*90, width/2-200, 30, myBlue);
    else if(i == 2)
      T4[i] = new TextButton(width - (100 + 250), height / 2 - height / 6 + 115 + i *90, 250, 25, color(255), myRed, myOr, ListN3[i]);
    else
      T4[i] = new TextButton(width / 2 - (100 + 250), height / 2 - height / 6 + 60 + i *90, 250, 25, color(255), myBlue, myBlueA, ListN3[i]);
    // T4[i] = new rectButton(100, height/2-height/8 +i*90, width/2-200, 30, myBlue);
  }
  for(int i = 0; i < T5.length; i++)
    T5[i] = new TextButton(10 + i *width / 2, 12, 70, 22, color(255), color(i * 153), myOr, "- info -");
  T6 = new TextButton(width / 2 - 100 - 170, height / 2 + height / 10, 170, 25, color(255), myRed, myOr, "Masquer toute information");

  lastInput = "Ecrit ton message, il s'inscrira ici ";
  T3[0].setText(lastInput);
}
// Ce qui est effectué tout au long de l'animation
void draw() {
  background(0);
  fill(153);
  rect(width / 2 + 1, 0, width / 2, height);

  update(mouseX, mouseY);
  for(int i = 0; i < T1.length; i++) {
    T0[i].display();
    T1[i].display();
  }
  for(int i = 0; i < T2.length; i++)
    T2[i].display();
  for(int i = 0; i < T3.length; i++)
    T3[i].display();
  for(int i = 0; i < T4.length; i++)
    T4[i].display();
  for(int i = 0; i < T5.length; i++)
    T5[i].display();
  T6.display();
}
void keyPressed() {
  if(!(key == CODED)) {
    if(lastInput.length() > 62)
      lastInput = lastInput.substring(0, 62);
    if(key == BACKSPACE) {
      if(lastInput.length() > 1) {
        lastInput = lastInput.substring(0, lastInput.length() - 2);
        T3[0].setText(lastInput);
        T3[0].display();
      }
    } else {
      // println((T3[0].value).length());
      lastInput = lastInput + key;
      T3[0].setText(lastInput);
      T3[0].display();
    }
  }
  if(key == '1') {
    BigInteger[] myKeys = new BigInteger[3];
    myKeys = createKeys();
    println("Private Key is: " + myKeys[0] + " // Public Key is: " + myKeys[1] + " " + myKeys[2]);
  }
  if(key == '2') {
    BigInteger[] myKeys = new BigInteger[3];
    myKeys = createKeys();
    BigInteger EncMess;
    EncMess = encrypt("oh oh je suis le père Noel!!!", myKeys[1], myKeys[2]);
    println("Public Key is: " + myKeys[1] + " " + myKeys[2] + " // encrypted message: " + EncMess);
  }
  if(key == '3') {
    BigInteger[] myKeys = new BigInteger[3];
    myKeys = createKeys();
    BigInteger EncMess;
    String decryptedMessage;
    EncMess = encrypt("oh oh je suis le père Noel!!!", myKeys[1], myKeys[2]);
    decryptedMessage = decrypt(EncMess, myKeys);
    println("Public Key is: " + myKeys[1] + " " + myKeys[2] + " // encrypted message: " + EncMess);
    println("Private Key is: " + myKeys[0] + " // decrypted message: " + decryptedMessage);
  }
}
// Update les états des boutons
void update(int x, int y) {
  for(int i = 0; i < T5.length; i++) {
    T5[i].update();
    if(T5[i].over)
      myInfo(ListN4[i], 0 + i * width / 2, 110 - i * 40);
  }
  if(locked == false) {
    for(int i = 0; i < T1.length; i++)
      T1[i].update();
    for(int i = 0; i < T4.length; i++)
      T4[i].update();
    T6.update();
  } else
    locked = false;
  if(mousePressed) {
    // Actions pour génération des clés
    for(int i = 0; i < T1.length; i++) {
      if(T1[i].pressed()) {
        T1[i].select = true;
        if(i == 0) {
          reset();
          T0[i].setText("  P = " + p + "      Q = " + q);
        } else if(i == 1) {
          if(!(p == A))
            calculate_n();
          T0[i].setText("  N = " + n);
        } else if(i == 2) {
          if(!(p == A))
            launch_e();
          T0[i].setText("  E = " + e);
        } else if(i == 3)
          if(!(e == A)) {
            launch_d();
            T0[i].setText("  D = " + d);
            T2[0].setText(ListN2[0] + d);
            T2[1].setText(ListN2[1] + "(" + n + "; " + e + ")");
            T2[2].setText(ListN2[2] + "(" + n + "; " + e + ")");
          }
      }
      for(int j = 0; j < T1.length - 1; j++)
        if(!(j == i))
          T1[j].select = false;
    }
    // Masquage des infos
    if(T6.pressed()) {
      T6.select = true;
      for(int i = 0; i < T0.length; i++)
        T0[i].setText("");
      T2[0].setText("");
    }
    // Actions pour l'encryptage du message
    for(int i = 0; i < T4.length; i++) {
      if(T4[i].pressed()) {
        T4[i].select = true;
        if(i == 0)
          translate_m();
        else if(i == 1) {
          if(!(n.equals(A)))
            encrypt_m();
        } else if(i == 2) {
          if(!(n.equals(A))) {
            send_m();
            lastInput = "";
            T3[0].setText(lastInput);
            T2[0].setText(ListN2[0] + "");
            // T3[0].display();
          }
        } else if(i == 3)
          if(!(n.equals(A)))
            decrypt_m();
      }
      for(int j = 0; j < T4.length - 1; j++)
        if(!(j == i))
          T4[j].select = false;
    }
  }
}
// Fenetre informative
void myInfo(String sinfo, int ix, int isize) {
  fill(255);
  rect(ix, 20, width / 2, isize);
  fill(myOr);
  text(sinfo, ix + 20, 40);
}
