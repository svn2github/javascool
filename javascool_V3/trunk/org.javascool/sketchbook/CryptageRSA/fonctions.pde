/**
   *
   * Fonctions générales de l'application
   *
   */
   
   
   ////////////////////////////////////////////////////
  /// Actions des controlleurs
  
  void reset() {
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
  
  ////////////////////////////////////////////////////
  // Fonctions pour API
  
  /** Créer une clé privée et une clé publique pour le codage et décodage de messages   
   * @return les clés
   */  
  BigInteger[] createKeys() {
    
    BigInteger[] Keys = new BigInteger[3];
    
    int pqSize = int(random(4, 10));
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
  
  
  /** Encrypt un message à l'aide de clés  
   * @param m message à encrypter, à inscrire entre "".
   * @param pk1 clé publique1. 
   * @param pk2 clé publique2. 
   * @return message encrypté sous forme de chiffres
   */  
  BigInteger encrypt(String m, BigInteger pk1, BigInteger pk2) {
   
    BigInteger EncMessBits = null;
    
    BigInteger MessBits = new BigInteger(m.getBytes());
    EncMessBits = encrypt(MessBits, pk1, pk2);
    
    return EncMessBits;
    
  }
  
  /** Encrypt un message à l'aide de clés  
   * @param me message encrypté sous forme de chiffres.
   * @param k clés, publique et privée. 
   * @return message
   */  
  String decrypt(BigInteger me, BigInteger[] k) {
   
    String decryptedMessage = null;
    
    BigInteger DecMessBits = decrypt(me, k[0], k[2]);
    decryptedMessage = new String(DecMessBits.toByteArray());
    
    return decryptedMessage;
    
  }
  
  static CryptageRSA proglet;  
