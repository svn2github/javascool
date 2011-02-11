import java.math.BigInteger;import static org.javascool.Macros.*;import static java.lang.Math.*;import static proglet.ingredients.Console.*;import static proglet.synthesons.SoundDisplay.*;import static proglet.exosdemaths.CurveDisplay.*;import static proglet.pixelsetcie.ImageDisplay.*;import static proglet.tortuelogo.TortueLogo.*;import static proglet.javaprog.JavaPanel.*;import static proglet.convanalogique.ConvAnalogique.*;import static proglet.dichotomie.Dichotomie.*;public class Exemple extends org.javascool.ProgletApplet implements Runnable {  private static final long serialVersionUID = 1L;  static { org.javascool.Jvs2Java.runnable = new Exemple(); }  public void run() { main(); }    
    // Programme exemple d'usage de la proglet
    
//import java.math.BigInteger;
    
    void main() {
      // Alice crée une clé privée et une clé publique pour le codage et décodage de message
      // - keys[0] est la clé privée D, et (keys[1],keys[2]) sont les clés publiques (E, N). 
      BigInteger keys[] = CryptageRSA.createKeys();
      //
      // Alice envoie sûrement (keys[1],keys[2]) à Bob et garde secrètement keys[0]
      //
      // Bob a message secret à envoyer à Alice
      String messageDeBob = "Salut à toi, humaine, que le meilleur soit à vec toi";
      // Bob encryptpe ce message à l'aide d'une clé (publique):
      BigInteger crypt = CryptageRSA.encrypt(messageDeBob, keys[1],keys[2]);
      //
      // - Ce message encrypté est sûrement diffusé de Alice à Bob
      //
      // Alice décrypte ce message à l'aide de la clé privée du jeu de clés
      String messageChezAlice = CryptageRSA.decrypt(crypt, keys);
      //
      // - Et on peut alors vérifier que le message est correct
      println("Réception :"+equal(messageChezAlice, messageDeBob));
      // - Même si il était indéchiffrable
      println("Message envoyé: '"+crypt+"'");
    }
}
