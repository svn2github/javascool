import static org.javascool.Macros.*;import static proglet.synthesons.SoundDisplay.*;import static proglet.ingredients.Console.*;import static proglet.exosdemaths.CurveDisplay.*;import static proglet.tortuelogo.TortueLogo.*;import static proglet.pixelsetcie.ImageDisplay.*;import static proglet.javaprog.JavaPanel.*;import static proglet.convanalogique.ConvAnalogique.*;import static proglet.dichotomie.Dichotomie.*;public class baseingre extends org.javascool.ProgletApplet implements Runnable {  private static final long serialVersionUID = 25L;  static { org.javascool.Jvs2Java.runnable = new baseingre(); }  public void run() { main(); }    
    // Effectue un tirage
    //  et renvoie le nombre de dates d'anniversaire identiques
    int tirage() {
      // Tirage de 30 dates entre 1 et 365
      int dates[] = new int[1200];
      for(int i = 0; i < dates.length; i++) {
         dates[i] = random(1, 365);
      }
      // Comptage des dates identiques
      int doublons = 0;
      for(int i = 0; i < dates.length; i++) {
        for(int j = i + 1; j < dates.length; j++) {
          if(dates[i] == dates[j]) {
            doublons = doublons + 1;
          }
        }
      }
      return doublons;
    }
    // Calcule la moyenne sur un grand nombre de tirages
    void main() {
      double N = 100000;
      double M = 0;
      for(int n = 0; n < N; n++) {
        M = M + tirage();
      }
      M = M / N;
      println(" Nombre de dates identiques en moyenne: " + M);
    }
}
