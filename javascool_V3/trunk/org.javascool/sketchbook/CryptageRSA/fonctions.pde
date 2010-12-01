/**
 *
 * Fonctions générales de l'application
 *
 */

// //////////////////////////////////////////////////
// / Actions des controlleurs

void reset() {
  pq_size = int (random(4, 10));

  p = new BigInteger(pq_size + 1, prime_certainty, new Random());
  q = new BigInteger(pq_size - 1, prime_certainty, new Random());
  if(p == null)
    p = new BigInteger(pq_size + 1, prime_certainty, new Random());
  if(q == null)
    q = new BigInteger(pq_size - 1, prime_certainty, new Random());
}
void calculate_n() {
  // Calculer n = p×q
  n = p.multiply(q);
}
void launch_e() {
  e = generate_e(p, q, 16);
}
void launch_d() {
  // Calculer d selon:
  // Il existe un relatif entier m, tel que e × d + m × (p - 1)(q - 1) = 1
  // d est la clé privée
  d = calculate_d(p, q, e);
}
void translate_m() {
  BigInteger MessBits = new BigInteger(lastInput.getBytes());
  T3[1].setText(MessBits + " ");
}
void encrypt_m() {
  BigInteger MessBits = new BigInteger(lastInput.getBytes());
  EncMessBits = encrypt(MessBits, e, n);
  T3[2].setText(EncMessBits + " ");
}
void decrypt_m() {
  BigInteger DecMessBits = decrypt(EncMessBits, d, n);
  String decryptedMessage = new String(DecMessBits.toByteArray());
  T3[4].setText(decryptedMessage + " ");
}
void send_m() {
  T3[3].setText(EncMessBits + " ");
}
// //////////////////////////////////////////////////
// Fonctions pour la méthode RSA

static BigInteger generate_e(BigInteger p, BigInteger q, int bitsize) {
  BigInteger e, phi_pq;

  e = new BigInteger("0");
  phi_pq = q.subtract(new BigInteger("1"));
  phi_pq = phi_pq.multiply(p.subtract(new BigInteger("1")));

  int i = 0;
  do {
    e = (new BigInteger(bitsize, 0, new Random())).setBit(0);
    i = i + 1;
  } while(i < 100 && (e.gcd(phi_pq).compareTo(new BigInteger("1")) != 0));
  return e;
}
static BigInteger calculate_d(BigInteger p, BigInteger q, BigInteger e) {
  BigInteger d, phi_pq;

  phi_pq = q.subtract(new BigInteger("1"));
  phi_pq = phi_pq.multiply(p.subtract(new BigInteger("1")));

  d = e.modInverse(phi_pq);
  return d;
}
static BigInteger encrypt(BigInteger m, BigInteger e, BigInteger n) {
  BigInteger c, bitmask;
  c = new BigInteger("0");
  int i = 0;
  bitmask = (new BigInteger("2")).pow(n.bitLength() - 1).subtract(new BigInteger("1"));
  while(m.compareTo(bitmask) == 1) {
    c = m.and (bitmask).modPow(e, n).shiftLeft(i * n.bitLength()).or (c);
    m = m.shiftRight(n.bitLength() - 1);
    i = i + 1;
  }
  c = m.modPow(e, n).shiftLeft(i * n.bitLength()).or (c);
  return c;
}
static BigInteger decrypt(BigInteger c, BigInteger d, BigInteger n) {
  BigInteger m, bitmask;
  m = new BigInteger("0");
  int i = 0;
  bitmask = (new BigInteger("2")).pow(n.bitLength()).subtract(new BigInteger("1"));
  while(c.compareTo(bitmask) == 1) {
    m = c.and (bitmask).modPow(d, n).shiftLeft(i * (n.bitLength() - 1)).or (m);
    c = c.shiftRight(n.bitLength());
    i = i + 1;
  }
  m = c.modPow(d, n).shiftLeft(i * (n.bitLength() - 1)).or (m);

  return m;
}
// //////////////////////////////////////////////////
// Fonctions pour API

/** Créer une clé privée D et le couple de clé publiques (E, N).
 * @return Un tableau de 3 entiers avec les clés keys[] = {D, E, N};
 */
static BigInteger[] createKeys() {
  BigInteger[] Keys = new BigInteger[3];

  int pqSize = (int) (4 + 6 * Math.random());
  BigInteger p_ = new BigInteger(pqSize + 1, prime_certainty, new Random());
  BigInteger q_ = new BigInteger(pqSize - 1, prime_certainty, new Random());
  if(p_ == null)
    p_ = new BigInteger(pqSize + 1, prime_certainty, new Random());
  if(q_ == null)
    q_ = new BigInteger(pqSize - 1, prime_certainty, new Random());
  BigInteger n_ = p_.multiply(q_);
  BigInteger e_ = generate_e(p_, q_, 16);
  BigInteger d_ = calculate_d(p_, q_, e_);

  Keys[0] = d_;
  Keys[1] = e_;
  Keys[2] = n_;

  return Keys;
}
/** Encrypte un message à l'aide de clés.
 * @param m Le message à encrypter.
 * @param E clé publique.
 * @param N clé publique.
 * @return Le message encrypté sous forme d'une suite de chiffres.
 */
static BigInteger encrypt(String m, BigInteger E, BigInteger N) {
  BigInteger EncMessBits = null;

  BigInteger MessBits = new BigInteger(m.getBytes());
  EncMessBits = encrypt(MessBits, E, N);

  return EncMessBits;
}
/** Décrypte un message à l'aide de clés.
 * @param m Le message encrypté sous forme de chiffres.
 * @param k clés, publique et privée.
 * @return Le message décrypté.
 */
String decrypt(BigInteger m, BigInteger[] k) {
  String decryptedMessage = null;

  BigInteger DecMessBits = decrypt(m, k[0], k[2]);
  decryptedMessage = new String(DecMessBits.toByteArray());

  return decryptedMessage;
}
static CryptageRSA proglet;
