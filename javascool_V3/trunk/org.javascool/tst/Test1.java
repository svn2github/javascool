import static org.javascool.Macros.*;import static proglet.synthesons.SoundDisplay.*;import static proglet.ingredients.Console.*;import static proglet.exosdemaths.CurveDisplay.*;import static proglet.tortuelogo.TortueLogo.*;import static proglet.pixelsetcie.ImageDisplay.*;import static proglet.javaprog.JavaPanel.*;import static proglet.convanalogique.ConvAnalogique.*;import static proglet.dichotomie.Dichotomie.*;public class Test1 extends org.javascool.ProgletApplet {  private static final long serialVersionUID = 1L;  static { org.javascool.Jvs2Java.runnable = new ProgletRunnableMain(); }}class ProgletRunnableMain implements Runnable {  private static final long serialVersionUID = 2L;  public void run() { main(); }    
    void main() {
     println("Bonjour, quel est ton nom ?");
     String texte = readString();
     println("Enchanté " + texte + ", et ... à bientôt !");
    }
}
