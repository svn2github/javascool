import static org.javascool.Macros.*;import static proglet.synthesons.SoundDisplay.*;import static proglet.ingredients.Console.*;import static proglet.exosdemaths.CurveDisplay.*;import static proglet.tortuelogo.TortueLogo.*;import static proglet.pixelsetcie.ImageDisplay.*;import static proglet.javaprog.JavaPanel.*;import static proglet.convanalogique.ConvAnalogique.*;import static proglet.dichotomie.Dichotomie.*;public class analo extends org.javascool.ProgletApplet implements Runnable {  private static final long serialVersionUID = 6L;  static { org.javascool.Jvs2Java.runnable = new analo(); }  public void run() { main(); }    
    void main(){
    int v = 1023;
    while(v >= 0) { sleep(10);
     convaOut(v);
     if (convaCompare() == 1) {
      echo("valeur = "+v);
     }
     v = v - 1;
    }
    }
}
