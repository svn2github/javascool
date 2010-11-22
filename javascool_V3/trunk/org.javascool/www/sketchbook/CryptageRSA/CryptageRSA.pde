  /*///////////////////////////////////////////////////////////////////////////////////
  *
  * 08.2010 Cécile P-L for Fuscia, ccl.picard@gmail.com
  * CRYPTOGRAPHIE
  * Interface pédagogique sur le codage/décodage de messages avec clés RSA
  * Interface réalisée avec ControlP5 (basée sur l'example ControlP5TextfieldAdvanced)
  *
  *////////////////////////////////////////////////////////////////////////////////////
  
  import controlP5.*;
  ControlP5 controlP5;
  
  import java.awt.*;
  import java.applet.*;
  import java.awt.event.*;
  import java.math.BigInteger;
  import java.util.Random;
  
  // Paramètres pour la méthode RSA 
  Textfield myTextfield_p, myTextfield_q, myTextfield_n, myTextfield_e, myTextfield_d;
  int pq_size;
  int prime_certainty = 20;
  BigInteger p, q, n, e, d;
  
  // Clés
  Textfield myTextfield_kpr, myTextfield_kpu, myTextfield_kpuB;
  
  // Message
  Textfield myTextfield_Mess, myTextfield_MessBits, myTextfield_EncMessBits, myTextfield_EncMessBitsA, myTextfield_DecMessBits;
  BigInteger EncMessBits;
  
  // Paramètres de l'interface 
  ControlWindow controlWindow;
  PFont pfont;
  ControlFont font;
  controlP5.Button boxA, boxB;
  int buttonValue = 1;
  int buttonValueB = 1;
  boolean isOpen, isOpenB;
  int myOr = color(255,100,0);
  int myGreen = color(0,100,30);
  int myGreenA = color(0,200,100);
  int myRed = color(255,0,0);
  
  // Ce qui est lancé une fois, au départ
  void setup() {
    
    frame = new Frame();
    
     size(WIDTH, HEIGHT); // au lieu de size(screen.width,screen.height);
    frameRate(30);
    pfont = createFont("Courrier",10,true); // use true/false for smooth/no-smooth
    font = new ControlFont(pfont);
  
    controlP5 = new ControlP5(this);
    
    ////////////////////////////////////////////////////
    /// ALICE
    
    this.frame.setTitle("Alice"); // interface principale: celle d'Alice
    //this.frame.setUndecorated(true);
    
    interfaceAlice();
    
  
    ////////////////////////////////////////////////////
    /// BOB 
    interfaceBob();
    
  }
  
  
  // Ce qui est effectué tout au long de l'animation
  void draw() {
    
    background(0);
    fill(150);
    rect(width/2+1, 0, width/2, height);
    controlP5.draw();
    
    // Fenetres informatives
    ouvreFenetreInfo();
    
  }
  
  void keyPressed() {
    
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
