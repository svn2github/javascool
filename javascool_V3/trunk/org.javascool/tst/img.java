import static org.javascool.Macros.*;import static proglet.synthesons.SoundDisplay.*;import static proglet.ingredients.Console.*;import static proglet.exosdemaths.CurveDisplay.*;import static proglet.tortuelogo.TortueLogo.*;import static proglet.pixelsetcie.ImageDisplay.*;import static proglet.javaprog.JavaPanel.*;import static proglet.convanalogique.ConvAnalogique.*;import static proglet.dichotomie.Dichotomie.*;public class img extends org.javascool.ProgletApplet implements Runnable {  private static final long serialVersionUID = 5L;  static { org.javascool.Jvs2Java.runnable = new img(); }  public void run() { main(); }    
    //Fonction appliquée à chaque pixel
    void filtrePixel(int i, int j) {
     int valeur = (smileyGet(i, j) + smileyGet(i + 1, j)+ smileyGet(i, j + 1)+ smileyGet(i - 1, j)+ smileyGet(i, j - 1)) / 5;
     smileySet(i, j, valeur);
    }
    //Boucle sur tous les pixels
    void filtre() {
     for(int j = -smileyHeight() + 1; j <= smileyHeight() - 1; j = j + 1) {
      for(int i = -smileyHeight() + 1; i <= smileyHeight() - 1; i = i + 1) {
       filtrePixel(i, j);
      }
     }
    }
    
    void main() {
     smileyLoad("http://facets.inria.fr/javascool/doisneaubuffon.jpg");
     for(int n = 0; n < 2; n = n + 1) {
      filtre();
     }
    }
}
