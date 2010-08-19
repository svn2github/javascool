import static org.javascool.Macros.*;import static java.lang.Math.*;import static proglet.synthesons.SoundDisplay.*;import static proglet.ingredients.Console.*;import static proglet.exosdemaths.CurveDisplay.*;import static proglet.tortuelogo.TortueLogo.*;import static proglet.pixelsetcie.ImageDisplay.*;import static proglet.javaprog.JavaPanel.*;import static proglet.convanalogique.ConvAnalogique.*;import static proglet.dichotomie.Dichotomie.*;public class son extends org.javascool.ProgletApplet implements Runnable {  private static final long serialVersionUID = 1L;  static { org.javascool.Jvs2Java.runnable = new son(); }  public void run() { main(); }    
    void main(){
       proglet.synthesons.SoundDisplay.tone = new org.javascool.SoundBit() { public double get(char c, double t) { return  0.3 * sqr(t/2) * sin(t) + 0.3 * sin(2 * t) + 0.3 * tri(3 * t); } }; proglet.synthesons.SoundDisplay.syntheSet("16 a");
       syntheSet("e5 e5b e5 e5b e5 e5b e5 b d5 c5 4 a | 1 h c e a 4 b | 1 h e g g# 4 a");
       synthePlay();
    }
}
