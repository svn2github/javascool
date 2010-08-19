import static org.javascool.Macros.*;import static proglet.synthesons.SoundDisplay.*;import static proglet.ingredients.Console.*;import static proglet.exosdemaths.CurveDisplay.*;import static proglet.tortuelogo.TortueLogo.*;import static proglet.pixelsetcie.ImageDisplay.*;import static proglet.javaprog.JavaPanel.*;import static proglet.convanalogique.ConvAnalogique.*;import static proglet.dichotomie.Dichotomie.*;public class Algo extends org.javascool.ProgletApplet implements Runnable {  private static final long serialVersionUID = 1L;  static { org.javascool.Jvs2Java.runnable = new Algo(); }  public void run() { main(); }    
    // DEBUT_PROGRAMME
    void main() {
       // NOMBRE_DECIMAL test <- 36.1;  
       double test = 36.1;
       // AFFICHER ("Test vaut : "+test);  
       println ("Test vaut : "+test);
       // FIN_PROGRAMME 
    }
}
