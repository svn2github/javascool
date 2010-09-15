/**
   *
   * Fonctions générales de l'application
   *
   */
   
   
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
     
