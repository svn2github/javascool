/*******************************************************************************
 * Thierry.Vieville@sophia.inria.fr, Copyright (C) 2009.  All rights reserved. *
 *******************************************************************************/

package synthesons;

// Used to define the gui
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.BorderLayout;

// Used to define a button
import javax.swing.JButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/** Définit une proglet javascool qui permet d'expérimenter avec des signaux sonores.
 * @see <a href="../synthesons/Main.java">code source</a>
 */
public class Main implements org.javascool.Proglet { private Main() { }
  private static final long serialVersionUID = 1L;

  // This defines the panel to display
  private static class Panel extends SoundBit.Panel {
    private static final long serialVersionUID = 1L;
    public SoundBit sound = new SoundBit.NotesSoundBit() {
	public double get(char channel, double time) { return Main.tone == null ? Math.sin(2 * Math.PI * time) : Main.tone.get(channel, time); }
      };
    public Panel() { reset(sound, 'l'); sound.reset("16 A"); }
  }

  //
  // This defines the tests on the panel
  //

  /** Test du panel. */
  static void test() {
    test(new SoundBit() { public double get(char c, double t) { return sns(t); } });
    test(new SoundBit() { public double get(char c, double t) { return 0.5 * sqr(t); } });
    test(new SoundBit() { public double get(char c, double t) { return 0.8 * tri(t) + 0.2 * noi(t); } });
    test(new SoundBit() { public double get(char c, double t) { return 0.3 * sqr(t/2) * sns(t) + 0.3 * sns(2 * t) + 0.3 * tri(3 * t); } });
    syntheSet("e5 e5b e5 e5b e5 e5b e5 b d5 c5 4 a | 1 h c e a 4 b | 1 h e g g# 4 a"); synthePlay();
  } 
  private static void test(SoundBit sound) {
    tone = sound; syntheSet("16 a"); synthePlay(); 
  }

  //
  // This defines the javascool interface
  //

  /** Definit le son à utiliser pour jouer les notes du synthétiseur. 
   * <div>Utilisé à travers la <a href="../synthesons/doc-files/about-proglet.htm">construction</a> <tt>Javascool</tt> de la forme <tt>TONE: <i>expression de la variable t</i></tt></div>
   */
  static public SoundBit tone = null;

  /** Retourne la valeur d'une sinusoïde de période une seconde et d'amplitude unité. */
  static public double sns(double t) { return Math.sin(2 * Math.PI * t); }

  /** Retourne la valeur d'un signal carré de période une seconde et d'amplitude unité. */
  static public double sqr(double t) { return (int) (t * 2) % 2 == 0 ? 1 : -1; }

  /** Retourne la valeur d'un signal triangulaire de période une seconde et d'amplitude unité. */
  static public double tri(double t) { return (int) (t * 2) % 2 == 0 ? (-1 + 4 * (t - (int) t)) : (3 - 4 * (t - (int) t)); }

  /** Retourne la valeur d'un souffle (``bruit blanc´´). */
  static public double noi(double t) { return 2 * Math.random() - 1; }

  /** Définit les notes à jouer dans cet interface. 
   * @param notes Définition des <a href="SoundBit.html#notes">notes</a> selon une syntaxe simplifiée.
   */
  static public void syntheSet(String notes) { panel.sound.reset(notes); panel.reset(panel.sound, 'l'); }

  /** Fait entendre le son sur le système audio. */
  static public void synthePlay() { try { panel.sound.play(); } catch(Exception e) { Macros.echo(e.toString()); } }

  /** Définition de l'interface graphique de la proglet. */
  public static final Panel panel = new Panel();
}
