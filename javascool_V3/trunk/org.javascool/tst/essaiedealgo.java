import static org.javascool.Macros.*;import static java.lang.Math.*;import static proglet.synthesons.SoundDisplay.*;import static proglet.ingredients.Console.*;import static proglet.exosdemaths.CurveDisplay.*;import static proglet.tortuelogo.TortueLogo.*;import static proglet.pixelsetcie.ImageDisplay.*;import static proglet.javaprog.JavaPanel.*;import static proglet.convanalogique.ConvAnalogique.*;import static proglet.dichotomie.Dichotomie.*;public class essaiedealgo extends org.javascool.ProgletApplet implements Runnable {  private static final long serialVersionUID = 9L;  static { org.javascool.Jvs2Java.runnable = new essaiedealgo(); }  public void run() { main(); }    
    // DEBUT_PROGRAMME
    void main() {
       // TRACE_LIGNE (0,0,0.5,0.5,6);  
       scopeAddLine (0,0,0.5,0.5,6);
       // TRACE_MOT (-0.5,0.0,"By Philippe",1);  
       scopeAdd (-0.5,0.0,"By Philippe",1);
       // FIN_PROGRAMME 
    }
}
