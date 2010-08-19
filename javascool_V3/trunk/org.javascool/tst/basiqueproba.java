import static org.javascool.Macros.*;import static proglet.synthesons.SoundDisplay.*;import static proglet.ingredients.Console.*;import static proglet.exosdemaths.CurveDisplay.*;import static proglet.tortuelogo.TortueLogo.*;import static proglet.pixelsetcie.ImageDisplay.*;import static proglet.javaprog.JavaPanel.*;import static proglet.convanalogique.ConvAnalogique.*;import static proglet.dichotomie.Dichotomie.*;public class basiqueproba extends org.javascool.ProgletApplet implements Runnable {  private static final long serialVersionUID = 23L;  static { org.javascool.Jvs2Java.runnable = new basiqueproba(); }  public void run() { main(); }    
    // Renvoie 1 si le lièvre a gagné, et 2 si la tortue a gagné
    int jeu() {
       int tortue = 0;
       while(true) { sleep(10);
          int de = random(1, 6);
          // Un 6 est sorti le lievre a gagné
          if (de == 6) {
             return 1;
          } else {
             tortue = tortue + 1;
             // La tortue arrive à 6, elle a gagné
             if(tortue == 6) {
                return 2;
             }
          }
       }
    }
    void main() {
       int l=0;
       int t=0;
    int i=0;
    echo("Calcule en cours ...");
       while(i<1000) { sleep(10);
          if (jeu() == 1) {
             //println("Le lièvre a gagné");
             l=l+1;
          } else {
            // println("La tortue a gagné");
             t=t+1;
          }
          i=i+1;
       }
       echo("Le lièvre a gagné "+l+" fois et la tortue "+t+" fois");
    }
}
