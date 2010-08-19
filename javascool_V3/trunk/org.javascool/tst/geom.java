import static org.javascool.Macros.*;import static proglet.synthesons.SoundDisplay.*;import static proglet.ingredients.Console.*;import static proglet.exosdemaths.CurveDisplay.*;import static proglet.tortuelogo.TortueLogo.*;import static proglet.pixelsetcie.ImageDisplay.*;import static proglet.javaprog.JavaPanel.*;import static proglet.convanalogique.ConvAnalogique.*;import static proglet.dichotomie.Dichotomie.*;public class geom extends org.javascool.ProgletApplet implements Runnable {  private static final long serialVersionUID = 16L;  static { org.javascool.Jvs2Java.runnable = new geom(); }  public void run() { main(); }    
    // Calcule et affiche le quatrième somme d'un parallélogramme.
    void exo2(double x_A, double y_A, double x_B, double y_B, double x_C, double y_C) {
      double x_I = (x_A + x_C) / 2;
      double y_I = (y_A + y_C) / 2;
      echo("  I = ("+ x_I +", "+y_I+")");
      double x_D = 2 * x_I - x_B;
      double y_D = 2 * y_I - y_B;
      echo("  D = ("+ x_D +", "+y_D+")");
      // Les lignes suivantes permettent de tracer les points
      scopeReset();
      scopeAdd(x_A, y_A, "A", 4);  
      scopeAdd(x_B, y_B, "B", 4);
      scopeAdd(x_C, y_C, "C", 4);
      scopeAdd(x_D, y_D, "D", 4);
      scopeAdd(x_I, y_I, "I", 2);
    }
    void main() {
      exo2(-0.8, 0.2, 0.2, 0.4, 0.4, -0.6);
    }
}
