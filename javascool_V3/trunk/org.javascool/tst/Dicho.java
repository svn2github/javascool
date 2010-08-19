import static org.javascool.Macros.*;import static proglet.synthesons.SoundDisplay.*;import static proglet.ingredients.Console.*;import static proglet.exosdemaths.CurveDisplay.*;import static proglet.tortuelogo.TortueLogo.*;import static proglet.pixelsetcie.ImageDisplay.*;import static proglet.javaprog.JavaPanel.*;import static proglet.convanalogique.ConvAnalogique.*;import static proglet.dichotomie.Dichotomie.*;public class Dicho extends org.javascool.ProgletApplet implements Runnable {  private static final long serialVersionUID = 2L;  static { org.javascool.Jvs2Java.runnable = new Dicho(); }  public void run() { main(); }    
    int getPage(String pays) {
     int debut = 0, fin = dichoLength();
     while(true) { sleep(10);
      int c = dichoCompare(pays, debut);
      if (c == 0) {
       return debut;
      } else{
       debut = debut +1;
      }
     }
    }
    void main(){
    getPage("France");
    }
}
