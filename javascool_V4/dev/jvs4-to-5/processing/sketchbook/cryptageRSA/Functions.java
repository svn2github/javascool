package org.javascool.proglets.cryptageRSA;
import java.math.BigInteger;

/** Définit les fonctions de la proglet.
 *
 * @see <a href="Functions.java.html">code source</a>
 * @serial exclude
 */
public class Functions {
  /** Crée une clé privée D et le couple de clé publiques (E, N).
   * @return Un tableau de 3 entiers avec les clés keys[] = {D, E, N};
   */
  public static BigInteger[] createKeys() {
    return cryptageRSA.createKeys();
  }
  /** Encrypte un message à l'aide de clés.
   * @param m Le message à encrypter.
   * @param E clé publique.
   * @param N clé publique.
   * @return Le message encrypté sous forme d'une suite de chiffres.
   */
  public static BigInteger encrypt(String m, BigInteger E, BigInteger N) {
    return cryptageRSA.encrypt(m, E, N);
  }
  /** Décrypte un message à l'aide de clés.
   * @param m Le message encrypté sous forme de chiffres.
   * @param k clés, publique et privée.
   * @return Le message décrypté.
   */
  public static String decrypt(BigInteger m, BigInteger[] k) {
    return cryptageRSA.decrypt(m, k);
  }
}
