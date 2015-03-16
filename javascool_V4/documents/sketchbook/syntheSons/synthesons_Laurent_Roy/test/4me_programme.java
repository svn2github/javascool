import static java.lang.Math.*;import static org.javascool.macros.Macros.*;import static org.javascool.macros.Stdin.*;import static org.javascool.macros.Stdout.*;import static org.javascool.proglets.syntheSons.Functions.*;import org.javascool.tools.sound.InputSoundBit;import org.javascool.tools.sound.FileSoundBit;import org.javascool.tools.sound.SoundBit;import org.javascool.proglets.syntheSons.NotesSoundBit;import org.javascool.proglets.syntheSons.SoundBitPanel;public class JvsToJavaTranslated21 implements Runnable{  private static final long serialVersionUID = 21L;  public void run() {   try{ main(); } catch(Throwable e) {     if (e.toString().matches(".*Interrupted.*"))println("\n-------------------\nProgramme arrêté !\n-------------------\n");    else println("\n-------------------\nErreur lors de l'exécution de la proglet\n"+org.javascool.core.Jvs2Java.report(e)+"\n-------------------\n");}  }public static void main(String[] usage) {    new org.javascool.widgets.MainFrame().reset("4ème_programme", 500, 500, org.javascool.core.ProgletEngine.getInstance().setProglet("syntheSons").getProgletPane()).setRunnable(new JvsToJavaTranslated21());}
void main() {
   /* @tone: sns(t) @<nojavac*/org.javascool.proglets.syntheSons.Functions.tone = new org.javascool.tools.sound.SoundBit() { public double get(char c, double t) { return  sns(t); } }; org.javascool.proglets.syntheSons.Functions.setNotes("16 a");/*@nojavac>*/
   String Mesures ="";
   for (int i=0;i<10;i++) {
		Mesures = Mesures + "c3 c4 c3 c4 ";
   		Mesures = Mesures + "c3 c4 c3 c4 ";
   		Mesures = Mesures + "e3 e4 e3 e4 ";
   		Mesures = Mesures + "e3 e4 e3 e4 ";
   		Mesures = Mesures + "d3 d4 d3 d4 ";
   		Mesures = Mesures + "d3 d4 d3 d4 ";
   		Mesures = Mesures + "c3 c4 c3 c4 ";
   		Mesures = Mesures + "c3 c4 c3 c4 ";
   }
println(Mesures);
setNotes(Mesures);
play();
}


}
