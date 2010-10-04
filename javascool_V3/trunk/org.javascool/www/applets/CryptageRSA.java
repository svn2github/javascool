import processing.core.*; 
import processing.xml.*; 

import controlP5.*; 
import java.awt.*; 
import java.applet.*; 
import java.awt.event.*; 
import java.math.BigInteger; 
import java.util.Random; 

import java.applet.*; 
import java.awt.Dimension; 
import java.awt.Frame; 
import java.awt.event.MouseEvent; 
import java.awt.event.KeyEvent; 
import java.awt.event.FocusEvent; 
import java.awt.Image; 
import java.io.*; 
import java.net.*; 
import java.text.*; 
import java.util.*; 
import java.util.zip.*; 
import java.util.regex.*; 

public class CryptageRSA extends PApplet {

  /*///////////////////////////////////////////////////////////////////////////////////
  *
  * 08.2010 C\u00e9cile P-L for Fuscia, ccl.picard@gmail.com
  * CRYPTOGRAPHIE
  * Interface p\u00e9dagogique sur le codage/d\u00e9codage de messages avec cl\u00e9s RSA
  * Interface r\u00e9alis\u00e9e avec ControlP5 (bas\u00e9e sur l'example ControlP5TextfieldAdvanced)
  *
  *////////////////////////////////////////////////////////////////////////////////////
  
  
  ControlP5 controlP5;
  
  
  
  
  
  
  
  // Param\u00e8tres pour la m\u00e9thode RSA 
  Textfield myTextfield_p, myTextfield_q, myTextfield_n, myTextfield_e, myTextfield_d;
  int pq_size;
  int prime_certainty = 20;
  BigInteger p, q, n, e, d;
  
  // Cl\u00e9s
  Textfield myTextfield_kpr, myTextfield_kpu, myTextfield_kpuB;
  
  // Message
  Textfield myTextfield_Mess, myTextfield_MessBits, myTextfield_EncMessBits, myTextfield_EncMessBitsA, myTextfield_DecMessBits;
  BigInteger EncMessBits;
  
  // Param\u00e8tres de l'interface 
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
  
  // Ce qui est lanc\u00e9 une fois, au d\u00e9part
  public void setup() {
    
    frame = new Frame();
    
    size(screen.width,screen.height);
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
  
  
  // Ce qui est effectu\u00e9 tout au long de l'animation
  public void draw() {
    
    background(0);
    fill(150);
    rect(width/2+1, 0, width/2, height);
    controlP5.draw();
    
    // Fenetres informatives
    ouvreFenetreInfo();
    
  }
  
  public void keyPressed() {
    
   if(key == '1') {
     BigInteger[] myKeys = new BigInteger[3];
     myKeys = createKeys();  
     println("Private Key is: " + myKeys[0] + " // Public Key is: " + myKeys[1] + " " + myKeys[2]);
   }
   
   if(key == '2') {
     BigInteger[] myKeys = new BigInteger[3];
     myKeys = createKeys();  
     BigInteger EncMess;
     EncMess = encrypt("oh oh je suis le p\u00e8re Noel!!!", myKeys[1], myKeys[2]);
     println("Public Key is: " + myKeys[1] + " " + myKeys[2] + " // encrypted message: " + EncMess);
   }
   
   if(key == '3') {
     BigInteger[] myKeys = new BigInteger[3];
     myKeys = createKeys();  
     BigInteger EncMess;
     String decryptedMessage;
     EncMess = encrypt("oh oh je suis le p\u00e8re Noel!!!", myKeys[1], myKeys[2]);
     decryptedMessage = decrypt(EncMess, myKeys);
     println("Public Key is: " + myKeys[1] + " " + myKeys[2] + " // encrypted message: " + EncMess);
     println("Private Key is: " + myKeys[0] + " // decrypted message: " + decryptedMessage);
   }
   
  }
/**
   *
   * Fonctions g\u00e9n\u00e9rales de l'application
   *
   */
   
   
   ////////////////////////////////////////////////////
  /// Actions des controlleurs
  
  public void reset() {
    pq_size = PApplet.parseInt(random(4, 10));
    
    p = new BigInteger(pq_size + 1, prime_certainty, new Random());
    q = new BigInteger(pq_size - 1, prime_certainty, new Random());
    if (p == null) {
      p = new BigInteger(pq_size + 1, prime_certainty, new Random());
    }
    if (q == null) {
      q = new BigInteger(pq_size - 1, prime_certainty, new Random());
    }
    
    myTextfield_p.setText("P = "+ p + " ");
    myTextfield_q.setText("Q = "+ q + " ");
    myTextfield_EncMessBitsA.hide();
    controlP5.controller("decrypt_m").hide();
    myTextfield_DecMessBits.hide();

  }
  
  public void calculate_n() {
    // Calculer n = p\u00d7q
    n = p.multiply(q);
    myTextfield_n.setText("N = P x Q =   "+ n + " ");
    
  }
  
  public void launch_e() {
    // G\u00e9n\u00e9rer e tel qu'il soit premier avec (p-1)*(q-1)
    e = generate_e(p, q, 16);
    myTextfield_e.setText("E = "+ e + " ");
  }
  
  public void launch_d() {
    // Calculer d selon: 
    // Il existe un relatif entier m, tel que e \u00d7 d + m \u00d7 (p - 1)(q - 1) = 1
    // d est la cl\u00e9 priv\u00e9e
    d = calculate_d(p, q, e);
    myTextfield_d.setText("D = "+ d + " ");
    // Cl\u00e9s
    myTextfield_kpr.setText("D = "+ d + " ");
    myTextfield_kpu.setText("( N , E ) = ( "+ n + " , " + e + " ) ");
    
    // Cl\u00e9 publique r\u00e9v\u00e9l\u00e9e \u00e0 Bob
    myTextfield_kpuB.setText("( N , E ) = ( "+ n + " , " + e + " ) ");
  }
  
  public void translate_m() {
    BigInteger MessBits = new BigInteger(myTextfield_Mess.getText().getBytes());
    myTextfield_MessBits.setText(" " + MessBits +" ");
    myTextfield_EncMessBitsA.clear();
    myTextfield_DecMessBits.clear();
  }
  
  public void encrypt_m() {
    BigInteger MessBits = new BigInteger(myTextfield_Mess.getText().getBytes());
    EncMessBits = encrypt(MessBits, e, n);
    myTextfield_EncMessBits.setText(" " + EncMessBits +" ");
  }
  
  public void decrypt_m() {
    BigInteger DecMessBits = decrypt(EncMessBits, d, n);
    String decryptedMessage = new String(DecMessBits.toByteArray());
    myTextfield_DecMessBits.setText(" " + decryptedMessage +" ");
  }
  
  public void send_m() {
   myTextfield_EncMessBitsA.show();
   controlP5.controller("decrypt_m").show();
   myTextfield_DecMessBits.show();
   myTextfield_Mess.clear();
   myTextfield_MessBits.clear();
   myTextfield_EncMessBits.clear();
   myTextfield_EncMessBitsA.setText(" " + EncMessBits +" ");
  }
  
  
  
  ////////////////////////////////////////////////////
  // Fonctions pour la m\u00e9thode RSA
  
  public BigInteger generate_e(BigInteger p, BigInteger q, int bitsize) {
  
  	BigInteger e, phi_pq;
  
  	e = new BigInteger("0");
  	phi_pq = q.subtract(new BigInteger("1"));
  	phi_pq = phi_pq.multiply(p.subtract(new BigInteger("1")));
  	
  	int i = 0;
  	
  	do {
  		e = (new BigInteger(bitsize, 0, new Random())).setBit(0);
  		i = i + 1;
  	} while( i<100 && (e.gcd(phi_pq).compareTo(new BigInteger("1")) != 0));
  	return e;
  
  }
  
  
  public BigInteger calculate_d(BigInteger p, BigInteger q, BigInteger e) {
    
    	BigInteger d, phi_pq;
        
    	phi_pq = q.subtract(new BigInteger("1"));
    	phi_pq = phi_pq.multiply(p.subtract(new BigInteger("1")));
      
    	d = e.modInverse(phi_pq);
    	return d;
    
  }
  
  
  public BigInteger encrypt(BigInteger m, BigInteger e, BigInteger n) {
  	
    BigInteger c, bitmask;
  	c = new BigInteger("0");
  	int i = 0;
  	bitmask = (new BigInteger("2")).pow(n.bitLength()-1).subtract(new BigInteger("1"));
  	while (m.compareTo(bitmask) == 1) {
  		c = m.and(bitmask).modPow(e,n).shiftLeft(i*n.bitLength()).or(c);
  		m = m.shiftRight(n.bitLength()-1);
  		i = i+1;
  	}
  	c = m.modPow(e,n).shiftLeft(i*n.bitLength()).or(c);
  	return c;
  
  }
  
  
  public BigInteger decrypt(BigInteger c, BigInteger d, BigInteger n) {
    
    	BigInteger m, bitmask;
    	m = new BigInteger("0");
    	int i = 0;
    	bitmask = (new BigInteger("2")).pow(n.bitLength()).subtract(new BigInteger("1"));
    	while (c.compareTo(bitmask) == 1) {
    		m = c.and(bitmask).modPow(d,n).shiftLeft(i*(n.bitLength()-1)).or(m);
    		c = c.shiftRight(n.bitLength());
    		i = i+1;
    	}
    	m = c.modPow(d,n).shiftLeft(i*(n.bitLength()-1)).or(m);
    	
      	return m;
      
  }
  
  ////////////////////////////////////////////////////
  // Fonctions pour API
  
  /** Cr\u00e9er une cl\u00e9 priv\u00e9e et une cl\u00e9 publique pour le codage et d\u00e9codage de messages   
   * @return les cl\u00e9s
   */  
  public BigInteger[] createKeys() {
    
    BigInteger[] Keys = new BigInteger[3];
    
    int pqSize = PApplet.parseInt(random(4, 10));
    BigInteger p_ = new BigInteger(pqSize + 1, prime_certainty, new Random());
    BigInteger q_ = new BigInteger(pqSize - 1, prime_certainty, new Random());
    if (p_ == null) {
      p_ = new BigInteger(pqSize + 1, prime_certainty, new Random());
    }
    if (q_ == null) {
      q_ = new BigInteger(pqSize - 1, prime_certainty, new Random());
    }
    
    BigInteger n_ = p_.multiply(q_);
    BigInteger e_ = generate_e(p_, q_, 16);
    BigInteger d_ = calculate_d(p_, q_, e_);

    Keys[0] = d_;
    Keys[1] = e_;
    Keys[2] = n_;
    
    return Keys;
  }
  
  
  /** Encrypt un message \u00e0 l'aide de cl\u00e9s  
   * @param m message \u00e0 encrypter, \u00e0 inscrire entre "".
   * @param pk1 cl\u00e9 publique1. 
   * @param pk2 cl\u00e9 publique2. 
   * @return message encrypt\u00e9 sous forme de chiffres
   */  
  public BigInteger encrypt(String m, BigInteger pk1, BigInteger pk2) {
   
    BigInteger EncMessBits = null;
    
    BigInteger MessBits = new BigInteger(m.getBytes());
    EncMessBits = encrypt(MessBits, pk1, pk2);
    
    return EncMessBits;
    
  }
  
  /** Encrypt un message \u00e0 l'aide de cl\u00e9s  
   * @param me message encrypt\u00e9 sous forme de chiffres.
   * @param k cl\u00e9s, publique et priv\u00e9e. 
   * @return message
   */  
  public String decrypt(BigInteger me, BigInteger[] k) {
   
    String decryptedMessage = null;
    
    BigInteger DecMessBits = decrypt(me, k[0], k[2]);
    decryptedMessage = new String(DecMessBits.toByteArray());
    
    return decryptedMessage;
    
  }
  
  static CryptageRSA proglet;  
/**
   *
   * Fonctions de l'interface
   *
   */
   
  
  public void interfaceAlice() {
    
    // Information
    
    //this.frame.setUndecorated(true);
    controlP5.addButton("InfoA",10,0,0,75,15).setId(1);
    controlP5.controller("InfoA").setCaptionLabel("A L I C E : info"); // change le titre
    controlP5.controller("InfoA").captionLabel().toUpperCase(false);
    controlP5.controller("InfoA").captionLabel().setControlFont(font); // change la police  controlP5.controller("InfoA").captionLabel().setControlFontSize(8);
    controlP5.controller("InfoA").setColorActive(myRed); 
    controlP5.controller("InfoA").setColorBackground(myOr); 
    boxA = controlP5.addButton("buttonValue",0,0,-170,60,40);
    boxA.setId(2);
    boxA.setWidth(width/2);
    boxA.setHeight(170);
    boxA.setColorActive(0); 
    boxA.setColorBackground(0); 
    boxA.setColorLabel(255);
    boxA.captionLabel().setControlFont(font);
    boxA.captionLabel().setControlFontSize(12);
    boxA.captionLabel().toUpperCase(false);
    boxA.setCaptionLabel("Tu es Alice. Nous te proposons d'exp\u00e9rimenter le CODAGE et le DECODAGE de messages.\n \n" +
    "Tout d'abord, tu dois g\u00e9n\u00e9rer une 'cl\u00e9 publique' et 'une cl\u00e9 priv\u00e9e'.\n \n" +
    "Tu divulgueras ensuite la cl\u00e9 publique \u00e0 Bob, et tu garderas la cl\u00e9 priv\u00e9e pr\u00e9cieusement. \n \n" +
    "Bob encryptera son message secret \u00e0 l'aide de la cl\u00e9 publique.\n \n" +
    "Seul toi pourras d\u00e9crypter le message au moyen de la cl\u00e9 priv\u00e9e!" );
    boxA.captionLabel().style().marginLeft = 30;
    boxA.captionLabel().style().marginTop = -62;
    
    // Choisir/generer p et q
    pq_size = PApplet.parseInt(random(4, 10));
    myTextfield_p = controlP5.addTextfield("P et Q sont des nombres premier",100,height/2-height/4,width/2/10,20);
    myTextfield_p.setColorBackground(myGreen);
    //myTextfield_p.captionLabel().setControlFont(font);
    myTextfield_q = controlP5.addTextfield(" ",100+width/2/8+15,height/2-height/4,width/2/10,20);
    myTextfield_q.setColorBackground(myGreen);
    //myTextfield_q.captionLabel().setControlFont(font);
    //controlP5.addButton("reset",0,n.width/2-(100+100),150,100,20).setCaptionLabel("1. Genere P et Q");
    controlP5.addButton("reset",0,width/2-(100+width/2/4),height/2-height/4,width/2/4,20);
    controlP5.controller("reset").captionLabel().setControlFont(font);
    controlP5.controller("reset").captionLabel().setControlFontSize(11);
    controlP5.controller("reset").setCaptionLabel("    1.  G\u00e9n\u00e8re   P  et  Q");
    controlP5.controller("reset").setColorBackground(myGreen);
    controlP5.controller("reset").setColorActive(myGreenA); 
    myTextfield_p.setText("P = "+ p + " ");
    myTextfield_q.setText("Q = "+ q + " ");
    
    
    // Calculer n=pxq
    myTextfield_n = controlP5.addTextfield("N = P * Q",100,height/2-height/4+(20*2+4),width/2/3,20);
    myTextfield_n.setColorBackground(myGreen);
    //myTextfield_n.captionLabel().setControlFont(font);
    //controlP5.addButton("calculate_n",0,width/2-(100+100),200,100,20).setCaptionLabel("2. Calcule n");
    controlP5.addButton("calculate_n",0,width/2-(100+width/2/4),height/2-height/4+(20*2+4),width/2/4,20);
    controlP5.controller("calculate_n").captionLabel().setControlFont(font);
    controlP5.controller("calculate_n").captionLabel().setControlFontSize(11);
    controlP5.controller("calculate_n").setCaptionLabel("        2.  Calcule   n");
    controlP5.controller("calculate_n").setColorBackground(myGreen);
    controlP5.controller("calculate_n").setColorActive(myGreenA); 
    
    
    // Generer e tel qu'il soit premier avec (p-1)*(q-1)
    myTextfield_e = controlP5.addTextfield("e est premier avec (p-1) * (q-1)",100,height/2-height/4+(20*4+4*2),width/2/3,20);
    myTextfield_e.setColorBackground(myGreen);
    //myTextfield_e.captionLabel().setControlFont(font);
    //controlP5.addButton("launch_e",0,width/2-(100+100),250,100,20).setCaptionLabel("3. Genere e");
    controlP5.addButton("launch_e",0,width/2-(100+width/2/4),height/2-height/4+(20*4+4*2),width/2/4,20);
    controlP5.controller("launch_e").captionLabel().setControlFont(font);
    controlP5.controller("launch_e").captionLabel().setControlFontSize(11);
    controlP5.controller("launch_e").setCaptionLabel("        3.  G\u00e9n\u00e8re   e");
    controlP5.controller("launch_e").setColorBackground(myGreen);
    controlP5.controller("launch_e").setColorActive(myGreenA); 
    
    // (n, e) est la cle publique
    
    // Calculer d selon: 
    // Il existe un relatif entier m, tel que e \u00d7 d + m \u00d7 (p - 1)(q - 1) = 1
    // d est la cle privee
    myTextfield_d = controlP5.addTextfield("d verifie: e * d + m * (p-1) * (q-1) = 1",100,height/2-height/4+(20*6+4*3),width/2/3,20);
    myTextfield_d.setColorBackground(myGreen);
    //myTextfield_d.captionLabel().setControlFont(font);
    //controlP5.addButton("launch_d",0,width/2/2-(100+100),300,100,20).setCaptionLabel("4. Calcule d");
    controlP5.addButton("launch_d",0,width/2-(100+width/2/4),height/2-height/4+(20*6+4*3),width/2/4,20);
    controlP5.controller("launch_d").captionLabel().setControlFont(font);
    controlP5.controller("launch_d").captionLabel().setControlFontSize(11);
    controlP5.controller("launch_d").setCaptionLabel("        4.  Calcule   d");
    controlP5.controller("launch_d").setColorBackground(myGreen);
    controlP5.controller("launch_d").setColorActive(myGreenA); 
    
  
    // Rev\u00e8le les cl\u00e9s priv\u00e9e et publique: 
    myTextfield_kpr = controlP5.addTextfield("Cle privee",100,height/2+20+10,width/2/4,20);
    myTextfield_kpr.setColorBackground(myRed);
    myTextfield_kpu = controlP5.addTextfield("Cle publique",2*width/2/3-100,height/2+20+10,width/2/3,20);
    myTextfield_kpu.setColorBackground(myRed);
    
    // Cacher les infos confidentielles
    controlP5.addButton("hideAll",0,width/2-(200+100),height/2+(20*4),200,20);
    controlP5.controller("hideAll").setCaptionLabel("  Masquer  toute  information");
    controlP5.controller("hideAll").captionLabel().setControlFont(font);
    controlP5.controller("hideAll").captionLabel().setControlFontSize(11);
    controlP5.controller("hideAll").setColorBackground(myRed);
    controlP5.controller("hideAll").setColorActive(myOr); 
    
    // Recevoir le message encrypt\u00e9
    myTextfield_EncMessBitsA = controlP5.addTextfield("Reception du message encrypte",100,height/2+(20*7),width/2-100*2,30);
    // D\u00e9crypter
    controlP5.addButton("decrypt_m",0,width/2-(3*width/2/10+100),height/2+(20*10),3*width/2/10,20);
    //controlP5.controller("decrypt_m").setCaptionLabel("Decryptage du message");
    controlP5.controller("decrypt_m").captionLabel().setControlFont(font);
    controlP5.controller("decrypt_m").captionLabel().setControlFontSize(11);
    controlP5.controller("decrypt_m").setCaptionLabel("  D\u00e9cryptage  du  message");

    
    
    myTextfield_DecMessBits = controlP5.addTextfield("Ceci est le message de Bob",100,height/2+(20*11+10),width/2-100*2,30);
    myTextfield_DecMessBits.valueLabel().toUpperCase(true);
    

  }
  
  public void interfaceBob() {
    
    
    //controlWindow = controlP5.addControlWindow("Bob",-5,0,width/2,height);

    //controlWindow.setUndecorated(true);
    //controlWindow.hideCoordinates();
    //controlWindow.setBackground(color(150,150,150));
    // Information
    controlP5.addButton("InfoB",10,width/2+1,0,75,15).setId(1);
    controlP5.controller("InfoB").setCaptionLabel("B O B : info"); // change the content
    controlP5.controller("InfoB").captionLabel().toUpperCase(false);
    //controlP5.controller("InfoB").setWindow(controlWindow);
    controlP5.controller("InfoB").captionLabel().setControlFont(font); // change the font
    controlP5.controller("InfoB").captionLabel().setControlFontSize(10);
    controlP5.controller("InfoB").setColorActive(myRed); 
    controlP5.controller("InfoB").setColorBackground(myOr); 
    boxB = controlP5.addButton("buttonValueB",width/2,width/2+1,-110,60,40);
    boxB.setId(2);
    boxB.setWidth(width/2);
    boxB.setHeight(110);
    boxB.setColorActive(150); 
    boxB.setColorBackground(150); 
    boxB.setColorLabel(255);
    //boxB.setWindow(controlWindow);
    boxB.captionLabel().setControlFont(font);
    boxB.captionLabel().setControlFontSize(12);
    boxB.captionLabel().toUpperCase(false);
    boxB.setCaptionLabel("Tu es Bob. Nous te proposons d'exp\u00e9rimenter le CODAGE et le DECODAGE de messages.\n \n" +
    "Tu vas recevoir une 'cl\u00e9', dite 'publique', qui va te permettre d'encrypter un message secret.\n \n" +
    "Apres encryptage du message, transmet-le \u00e0 Alice qui essayera de le d\u00e9crypter! ");
    boxB.captionLabel().style().marginLeft = 30;
    boxB.captionLabel().style().marginTop = -20;
    
    // Recevoir la cl\u00e9 publique
    myTextfield_kpuB = controlP5.addTextfield("Cle public pour l'encryptage",width/2+1+100,height/2-height/4,width/2/3,20);
    myTextfield_kpuB.setColorBackground(myRed);
    //myTextfield_kpuB.setWindow(controlWindow);
    
    // Message secret
    myTextfield_Mess = controlP5.addTextfield("Ton message secret",width/2+1+100,height/2-height/4+(20*4),width/2-100*2,30);
    myTextfield_Mess.valueLabel().toUpperCase(true);
    //myTextfield_Mess.captionLabel().fixedSize(12); 
    //myTextfield_Mess.valueLabel().setFont(ControlP5.grixel);
    //myTextfield_Mess.valueLabel().setColorBackground(myOr); 
    
    //myTextfield_Mess.setWindow(controlWindow);
    myTextfield_Mess.setText("Ceci est un message secret.. ");
    //myTextfield_Mess.valueLabel().textHeight();
    //myTextfield_Mess.valueLabel().setWidth(50); 
    //myTextfield_Mess.valueLabel().setHeight(50);
    
    
    // Traduire en chiffres
    controlP5.addButton("translate_m",0,width-(240+100),height/2-height/4+(20*7),240,20);
    controlP5.controller("translate_m").captionLabel().setControlFont(font);
    controlP5.controller("translate_m").captionLabel().setControlFontSize(11);
    controlP5.controller("translate_m").setCaptionLabel(" Traduction  du  message  en  chiffres");
    //controlP5.controller("translate_m").setWindow(controlWindow);
    myTextfield_MessBits = controlP5.addTextfield("     ",width/2+1+100,height/2-height/4+(20*8+10),width/2-100*2,30);
    
    //myTextfield_MessBits.setWindow(controlWindow);
    
    // Encrypter le message
    controlP5.addButton("encrypt_m",0,width-(165+100),height/2-height/4+(20*11+10),165,20);
    controlP5.controller("encrypt_m").captionLabel().setControlFont(font);
    controlP5.controller("encrypt_m").captionLabel().setControlFontSize(11);
    controlP5.controller("encrypt_m").setCaptionLabel(" Encryptage  du  message");
    //controlP5.controller("encrypt_m").setWindow(controlWindow);
    myTextfield_EncMessBits = controlP5.addTextfield("      ",width/2+1+100,height/2-height/4+(20*13),width/2-100*2,30);
    //myTextfield_EncMessBits.setWindow(controlWindow);
    // Envoyer le message encryt\u00e9
    controlP5.addButton("send_m",0,width-(185+100),height/2-height/4+(20*17),185,20);
    controlP5.controller("send_m").captionLabel().setControlFont(font);
    controlP5.controller("send_m").captionLabel().setControlFontSize(11);
    controlP5.controller("send_m").setCaptionLabel(" Envoi  du  message  encrypt\u00e9");
    controlP5.controller("send_m").setColorBackground(myRed);
    //controlP5.controller("send_m").setWindow(controlWindow);
    controlP5.controller("send_m").setColorActive(myOr); 
    
  }
  
  
  public void ouvreFenetreInfo() {
    
    boxA.position().y += ((isOpen==true ? 15+1:-170) - boxA.position().y) * 0.2f;
    boxB.position().y += ((isOpenB==true ? 15+1:-110) - boxB.position().y) * 0.2f;
    //boxB.setWindow(controlWindow);
    
  }
  
  public void InfoA(float theValueA) {
    println("a button event. "+theValueA);
    isOpen = !isOpen;
    controlP5.controller("InfoA").setCaptionLabel((isOpen==true) ? "FERMER info":"A L I C E : info");
  }
  
  public void InfoB(float theValueB) {
    println("a button event. "+theValueB);
    isOpenB = !isOpenB;
    controlP5.controller("InfoB").setCaptionLabel((isOpenB==true) ? "fermer Info":"B O B : info");
  }
  
  public void hideAll(int theValue) {
    myTextfield_p.clear();
    myTextfield_q.clear();
    myTextfield_n.clear();
    myTextfield_d.clear();
    myTextfield_e.clear();
    myTextfield_kpr.clear(); // cl\u00e9 priv\u00e9e

  }
  
  
  /** Utilis\u00e9 pour fermer la fen\u00eatre secondaire de l'interface, par JavaScool. */
  /*public processing.core.PApplet getControl() {
    controlWindow.hide();
    return controlWindow.papplet();
    
  }*/
  
  /** Cr\u00e9er une cl\u00e9 priv\u00e9e et une cl\u00e9 publique pour le codage et d\u00e9codage de messages   
   * @return les cl\u00e9s
   */  
  
  static public void main(String args[]) {
    PApplet.main(new String[] { "--bgcolor=#DFDFDF", "CryptageRSA" });
  }
}
