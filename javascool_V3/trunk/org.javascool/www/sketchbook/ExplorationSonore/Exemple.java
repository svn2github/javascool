import static org.javascool.Macros.*;import static java.lang.Math.*;import static proglet.ingredients.Console.*;import static proglet.synthesons.SoundDisplay.*;import static proglet.exosdemaths.CurveDisplay.*;import static proglet.pixelsetcie.ImageDisplay.*;import static proglet.tortuelogo.TortueLogo.*;import static proglet.javaprog.JavaPanel.*;import static proglet.convanalogique.ConvAnalogique.*;import static proglet.dichotomie.Dichotomie.*;public class Exemple extends org.javascool.ProgletApplet implements Runnable {  private static final long serialVersionUID = 6L;  static { org.javascool.Jvs2Java.runnable = new Exemple(); }  public void run() { main(); }    
    // Programme exemple d'usage de la proglet
    
    void main() {
      // Lancement d'un signal prédéfini
      ExplorationSonore.playSignal(1, "sinus", 500, 1);
      // une seconde d'attente
      sleep(2000);
      // Lancement d'un autre signal prédéfini, sur un autre canal
      ExplorationSonore.playSignal(2, "carré", 1500, 0.333);
      // une seconde d'attente
      sleep(2000);
      // Lancement d'un 3eme signal prédéfini, sur le 3eme canal
      ExplorationSonore.playSignal(3, "scie", 2500, 0.2);
      // une seconde d'attente
      sleep(2000);
      // et on arrete tout
      ExplorationSonore.playStop();
      // une seconde d'attente
      sleep(3000);
      // Et on joue un sone prédéfini en étouffant le son 
      //  - Attention à ce que le chemin vers le fichier soit bien accessible.
      ExplorationSonore.playRecord("../sketchbook/ExplorationSonore/data/music/Ahmed_Ex1.wav", 400);
      // deux secondes d'attente
      sleep(4000);
      // et on arrete tout
      ExplorationSonore.playStop();
    }
}