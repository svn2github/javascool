  /*///////////////////////////////////////////////////////////////////////////////////
  *
  * 08.2010 Cécile P-L for Fuscia
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
  ControlFont font;
  controlP5.Button boxA;
  controlP5.Button boxB;
  int buttonValue = 1;
  int buttonValueB = 1;
  boolean isOpen;
  boolean isOpenB;
  int myOr = color(255,100,0);
  int myRed = color(255,0,0);
  
  
  void setup() {
    
    size(screen.width/2,screen.height);
    frameRate(30);
    // load a new font.
    // to display a font as smooth or non-smooth, use true/false as 3rd parameter
    // when creating a PFont:
    PFont pfont = createFont("Courrier",10,true); // use true/false for smooth/no-smooth
    font = new ControlFont(pfont);
    PFont myFont = createFont(PFont.list()[2], 20);
    textFont(myFont);
  
    controlP5 = new ControlP5(this);
    controlP5.setAutoDraw(false);
    
    ////////////////////////////////////////////////////
    /// ALICE
    
    this.frame.setTitle("Alice");
    frame.setLocation(screen.width/2+5,0);
    // Information
    controlP5.addButton("InfoA",10,0,0,70,15).setId(1);
    controlP5.controller("InfoA").setCaptionLabel("Info"); // change le titre
    controlP5.controller("InfoA").captionLabel().setControlFont(font); // change la police  controlP5.controller("InfoA").captionLabel().setControlFontSize(8);
    controlP5.controller("InfoA").setColorActive(myOr); 
    controlP5.controller("InfoA").setColorBackground(myOr); 
    boxA = controlP5.addButton("buttonValue",0,-screen.width/2,20,60,40);
    boxA.setId(2);
    boxA.setWidth(screen.width/2);
    boxA.setHeight(120);
    boxA.setColorActive(255); 
    boxA.setColorBackground(255); 
    boxA.setColorLabel(myOr);
    boxA.captionLabel().setControlFont(font);
    boxA.captionLabel().setControlFontSize(12);
    boxA.captionLabel().toUpperCase(false);
    boxA.captionLabel().set("Tu es Alice. Nous te proposons d'expérimenter le CODAGE et le DECODAGE de messages.\n \n" +
    "Tout d'abord, tu dois générer une 'clé publique' et 'une clé privée'.\n \n" +
    "Tu divulgueras ensuite la clé publique à Bob, et tu garderas la clé privée précieusement. \n \n" +
    "Bob encryptera son message secret à l'aide de la clé publique.\n \n" +
    "Seul toi pourras décrypter le message grace à la clé privée!" );
    boxA.captionLabel().style().marginLeft = 30;
    boxA.captionLabel().style().marginTop = -42;
    
    // Choisir/générer p et q
    pq_size = int(random(4, 10));
    myTextfield_p = controlP5.addTextfield(" ",100,170,100,20);
    myTextfield_q = controlP5.addTextfield(" ",220,170,100,20);
    //controlP5.addButton("reset",0,screen.width/2-(100+100),150,100,20).setCaptionLabel("1. Genere P et Q");
    controlP5.addButton("reset",0,screen.width/2-(100+120),170,120,20).setCaptionLabel("1.  Génère   P  et  Q");
    controlP5.controller("reset").captionLabel().setControlFont(font);
    controlP5.controller("reset").captionLabel().setControlFontSize(11);
    myTextfield_p.setText("P = "+ p + " ");
    myTextfield_q.setText("Q = "+ q + " ");
    
    // Calculer n=pxq
    myTextfield_n = controlP5.addTextfield(" ",100,220,220,20);
    //controlP5.addButton("calculate_n",0,screen.width/2-(100+100),200,100,20).setCaptionLabel("2. Calcule n");
    controlP5.addButton("calculate_n",0,screen.width/2-(100+120),220,120,20).setCaptionLabel("2.  Calcule   n");
    controlP5.controller("calculate_n").captionLabel().setControlFont(font);
    controlP5.controller("calculate_n").captionLabel().setControlFontSize(11);
    
    // Générer e tel qu'il soit premier avec (p-1)*(q-1)
    myTextfield_e = controlP5.addTextfield(" ",100,270,220,20);
    //controlP5.addButton("launch_e",0,screen.width/2-(100+100),250,100,20).setCaptionLabel("3. Genere e");
    controlP5.addButton("launch_e",0,screen.width/2-(100+120),270,120,20).setCaptionLabel("3.  Génère   e");
    controlP5.controller("launch_e").captionLabel().setControlFont(font);
    controlP5.controller("launch_e").captionLabel().setControlFontSize(11);
    // (n, e) est la clé publique
    
    // Calculer d selon: 
    // Il existe un relatif entier m, tel que e × d + m × (p - 1)(q - 1) = 1
    // d est la clé privée
    myTextfield_d = controlP5.addTextfield(" ",100,320,220,20);
    //controlP5.addButton("launch_d",0,screen.width/2-(100+100),300,100,20).setCaptionLabel("4. Calcule d");
    controlP5.addButton("launch_d",0,screen.width/2-(100+120),320,120,20).setCaptionLabel("4.  Calcule   d");
    controlP5.controller("launch_d").captionLabel().setControlFont(font);
    controlP5.controller("launch_d").captionLabel().setControlFontSize(11);
  
    // Révèle les clés privée et publique: 
    myTextfield_kpr = controlP5.addTextfield("Cle privee",100,380,220,20);
    myTextfield_kpr.setColorBackground(myRed);
    myTextfield_kpu = controlP5.addTextfield("Cle publique",100,430,220,20);
    myTextfield_kpu.setColorBackground(myRed);
    
    // Cacher les infos confidentielles
    controlP5.addButton("hideAll",0,screen.width/2-(200+100),490,200,20);
    controlP5.controller("hideAll").setCaptionLabel("  Masquer  toute  information");
    controlP5.controller("hideAll").captionLabel().setControlFont(font);
    controlP5.controller("hideAll").captionLabel().setControlFontSize(11);
    controlP5.controller("hideAll").setColorBackground(myRed);
    controlP5.controller("hideAll").setColorActive(myOr); 
    
    // Recevoir le message encrypté
    myTextfield_EncMessBitsA = controlP5.addTextfield("Reception du message encrypte",100,570,screen.width/2-100*2,50);
    // Décrypter
    controlP5.addButton("decrypt_m",0,screen.width/2-(170+100),640,170,20);
    //controlP5.controller("decrypt_m").setCaptionLabel("Decryptage du message");
    controlP5.controller("decrypt_m").setCaptionLabel("  Décryptage  du  message");
    controlP5.controller("decrypt_m").captionLabel().setControlFont(font);
    controlP5.controller("decrypt_m").captionLabel().setControlFontSize(11);
    
    myTextfield_DecMessBits = controlP5.addTextfield("Ceci est le message de Bob",100,670,screen.width/2-100*2,50);
    myTextfield_DecMessBits.valueLabel().toUpperCase(true);
  
    ////////////////////////////////////////////////////
    /// BOB 
    
    controlWindow = controlP5.addControlWindow("Bob",-5,0,screen.width/2,screen.height);
    controlWindow.hideCoordinates();
    controlWindow.setBackground(color(150,150,150));
    // Information
    controlP5.addButton("InfoB",0,5,0,70,15).setId(1);
    controlP5.controller("InfoB").setCaptionLabel("Info"); // change the content
    controlP5.controller("InfoB").setWindow(controlWindow);
    controlP5.controller("InfoB").captionLabel().setControlFont(font); // change the font
    controlP5.controller("InfoB").captionLabel().setControlFontSize(10);
    controlP5.controller("InfoB").setColorActive(myOr); 
    controlP5.controller("InfoB").setColorBackground(myOr); 
    boxB = controlP5.addButton("buttonValueB",0,-screen.width/2,20,60,40);
    boxB.setId(2);
    boxB.setWidth(screen.width/2);
    boxB.setHeight(100);
    boxB.setColorActive(255); 
    boxB.setColorBackground(255); 
    boxB.setColorLabel(myOr);
    boxB.setWindow(controlWindow);
    boxB.captionLabel().setControlFont(font);
    boxB.captionLabel().setControlFontSize(12);
    boxB.captionLabel().toUpperCase(false);
    boxB.captionLabel().set("Tu es Bob. Nous te proposons d'expérimenter le CODAGE et le DECODAGE de messages.\n \n" +
    "Tu vas recevoir une 'clé', dite 'publique', qui va te permettre d'encrypter un message secret.\n \n" +
    "Après encryptage du message, transmet-le à Alice qui essayera de le décrypter! ");
    boxB.captionLabel().style().marginLeft = 30;
    boxB.captionLabel().style().marginTop = -20;
    
    // Recevoir la clé publique
    myTextfield_kpuB = controlP5.addTextfield("Cle public pour l'encryptage",100,150,220,20);
    myTextfield_kpuB.setColorBackground(myRed);
    myTextfield_kpuB.setWindow(controlWindow);
    
    // Message secret
    myTextfield_Mess = controlP5.addTextfield("Ton message secret",100,250,screen.width/2-100*2,50);
    myTextfield_Mess.valueLabel().toUpperCase(true);
    //myTextfield_Mess.captionLabel().fixedSize(12); 
    //myTextfield_Mess.valueLabel().setFont(ControlP5.grixel);
    //myTextfield_Mess.valueLabel().setColorBackground(myOr); 
    
    myTextfield_Mess.setWindow(controlWindow);
    myTextfield_Mess.setText("Ceci est un message secret.. ");
    //myTextfield_Mess.valueLabel().textHeight();
    //myTextfield_Mess.valueLabel().setWidth(50); 
    //myTextfield_Mess.valueLabel().setHeight(50);
    
    
    // Traduire en chiffres
    controlP5.addButton("translate_m",0,screen.width/2-(240+100),320,240,20);
    controlP5.controller("translate_m").setCaptionLabel(" Traduction  du  message  en  chiffres");
    controlP5.controller("translate_m").captionLabel().setControlFont(font);
    controlP5.controller("translate_m").captionLabel().setControlFontSize(11);
    controlP5.controller("translate_m").setWindow(controlWindow);
    myTextfield_MessBits = controlP5.addTextfield(" ",100,350,screen.width/2-100*2,50);
    
    myTextfield_MessBits.setWindow(controlWindow);
    
    // Encrypter le message
    controlP5.addButton("encrypt_m",0,screen.width/2-(165+100),420,165,20);
    controlP5.controller("encrypt_m").setCaptionLabel(" Encryptage  du  message");
    controlP5.controller("encrypt_m").captionLabel().setControlFont(font);
    controlP5.controller("encrypt_m").captionLabel().setControlFontSize(11);
    controlP5.controller("encrypt_m").setWindow(controlWindow);
    myTextfield_EncMessBits = controlP5.addTextfield(" ",100,450,screen.width/2-100*2,50);
    myTextfield_EncMessBits.setWindow(controlWindow);
    // Envoyer le message encryté
    controlP5.addButton("send_m",0,screen.width/2-(185+100),570,185,20);
    controlP5.controller("send_m").setCaptionLabel(" Envoi  du  message  encrypté");
    controlP5.controller("send_m").captionLabel().setControlFont(font);
    controlP5.controller("send_m").captionLabel().setControlFontSize(11);
    controlP5.controller("send_m").setColorBackground(myRed);
    controlP5.controller("send_m").setWindow(controlWindow);
    controlP5.controller("send_m").setColorActive(myOr); 
    
  }
  
  void draw() {
    
    background(0);
    controlP5.draw();
    // Animer les bo^tes informatives
    boxA.position().x += ((isOpen==true ? 0:-screen.width/2-3) - boxA.position().x) * 0.2;
    boxB.position().x += ((isOpenB==true ? 0:-screen.width/2) - boxB.position().x) * 0.2;
    boxB.setWindow(controlWindow);
    
  }
  
  
  ////////////////////////////////////////////////////
  /// Actions des controlleurs
  
  void reset(int theValue) {
    pq_size = int(random(4, 10));
    
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
  
  void calculate_n() {
    // Calculer n = p×q
    n = p.multiply(q);
    myTextfield_n.setText("N = P x Q =   "+ n + " ");
  }
  
  void launch_e() {
    // Générer e tel qu'il soit premier avec (p-1)*(q-1)
    e = generate_e(p, q, 16);
    myTextfield_e.setText("E = "+ e + " ");
  }
  
  void launch_d() {
    // Calculer d selon: 
    // Il existe un relatif entier m, tel que e × d + m × (p - 1)(q - 1) = 1
    // d est la clé privée
    d = calculate_d(p, q, e);
    myTextfield_d.setText("D = "+ d + " ");
    // Clés
    myTextfield_kpr.setText("D = "+ d + " ");
    myTextfield_kpu.setText("( N , E ) = ( "+ n + " , " + e + " ) ");
    
    // Clé publique révélée à Bob
    myTextfield_kpuB.setText("( N , E ) = ( "+ n + " , " + e + " ) ");
  }
  
  void translate_m() {
    BigInteger MessBits = new BigInteger(myTextfield_Mess.getText().getBytes());
    myTextfield_MessBits.setText(" " + MessBits +" ");
    myTextfield_EncMessBitsA.clear();
    myTextfield_DecMessBits.clear();
  }
  
  void encrypt_m() {
    BigInteger MessBits = new BigInteger(myTextfield_Mess.getText().getBytes());
    EncMessBits = encrypt(MessBits, e, n);
    myTextfield_EncMessBits.setText(" " + EncMessBits +" ");
  }
  
  void decrypt_m() {
    BigInteger DecMessBits = decrypt(EncMessBits, d, n);
    String decryptedMessage = new String(DecMessBits.toByteArray());
    myTextfield_DecMessBits.setText(" " + decryptedMessage +" ");
  }
  
  void send_m() {
   myTextfield_EncMessBitsA.show();
   controlP5.controller("decrypt_m").show();
   myTextfield_DecMessBits.show();
   myTextfield_Mess.clear();
   myTextfield_MessBits.clear();
   myTextfield_EncMessBits.clear();
   myTextfield_EncMessBitsA.setText(" " + EncMessBits +" ");
  }
  
  public void InfoA(float theValueA) {
    println("a button event. "+theValueA);
    isOpen = !isOpen;
    controlP5.controller("InfoA").setCaptionLabel((isOpen==true) ? "fermer Info":"voir Info");
  }
  
  public void InfoB(float theValueB) {
    println("a button event. "+theValueB);
    isOpenB = !isOpenB;
    controlP5.controller("InfoB").setCaptionLabel((isOpenB==true) ? "fermer Info":"voir Info");
  }
  
  void hideAll(int theValue) {
    myTextfield_p.clear();
    myTextfield_q.clear();
    myTextfield_n.clear();
    myTextfield_d.clear();
    myTextfield_e.clear();
    myTextfield_kpr.clear(); // clé privée
  }
  
  
  ////////////////////////////////////////////////////
  // Fonctions pour la méthode RSA
  
  BigInteger generate_e(BigInteger p, BigInteger q, int bitsize) {
  
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
  
  
  BigInteger calculate_d(BigInteger p, BigInteger q, BigInteger e) {
    
    	BigInteger d, phi_pq;
        
    	phi_pq = q.subtract(new BigInteger("1"));
    	phi_pq = phi_pq.multiply(p.subtract(new BigInteger("1")));
      
    	d = e.modInverse(phi_pq);
    	return d;
    
  }
  
  
  BigInteger encrypt(BigInteger m, BigInteger e, BigInteger n) {
  	
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
  
  
  BigInteger decrypt(BigInteger c, BigInteger d, BigInteger n) {
    
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
     
