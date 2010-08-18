package proglet.javaprog;import javax.swing.*;import proglet.synthesons.FileSoundBit;import static org.javascool.Macros.*;import static proglet.ingredients.Console.*;import static proglet.synthesons.SoundDisplay.*;import static proglet.exosdemaths.CurveDisplay.*;import static proglet.pixelsetcie.ImageDisplay.*;import static proglet.tortuelogo.TortueLogo.*;import static proglet.javaprog.JavaPanel.*;import static proglet.convanalogique.ConvAnalogique.*;import static proglet.dichotomie.Dichotomie.*;public class FleurEnRythme extends org.javascool.ProgletApplet {  private static final long serialVersionUID = 1L;  static { org.javascool.Jvs2Java.runnable = new FleurEnRythmeMain(); }}class FleurEnRythmeMain implements Runnable {  private static final long serialVersionUID = 2L;  public void run() { main(); }    // Implément l'idée originale de Thomas Giraudon <ptiroccodu06@hotmail.fr> Victor Granet <victor.granet@hotmail.com>
    
//package proglet.javaprog;
    
//import javax.swing.*;
//import proglet.synthesons.FileSoundBit;
    
    JLayeredPane pane = getContentPane();
    
    // Ajoute une image sur le display en (x,y) de taille (w, h) à la profondeur p
    JLabel icon(String image, int x, int y, int w, int h, int p) {
      JLabel icon = new JLabel();
      icon.setIcon(new ImageIcon(image));
      icon.setBounds(x, y, w, h);
      pane.add(icon, new Integer(p), 0);
      return icon;
    }
    
    // Défini l'affichage des icones lors d'évènements sonores
    class FlowerSoundBit extends FileSoundBit {
      double events[];
      public void sample(int n) {  
        if (events[n] > 0) {
          icon("fleur-move.png", random(0, pane.getWidth()), random(0, pane.getHeight()), 100, 100, 2);
          echo(n);
        }
      }
    }
    
    // Programme principal
    void main() {
       // On efface tout
       pane.removeAll();
       // On affiche une image de fond
       icon("fleur-fond.jpg", 0, 0, pane.getWidth(), pane.getHeight(), 1);
       // On  charge le son
       FlowerSoundBit sound = new FlowerSoundBit();
       sound.reset("black-dog.wav");
       // On calcule les évènements qui déclenchent des fleurs
       double frequence = 100, period = 0.2, cut = 0;
       sound.events = sound.events(frequence, period, cut);
       // Envoyez la musique !!
       sound.play(period);
    }
}
