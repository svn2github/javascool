package org.javascool.proglets.sampleCode;
import static org.javascool.macros.Macros.*;

/** Définit les fonctions de la proglet.
 * @see <a href="Functions.java.html">source code</a>
 * @serial exclude
 */
public class Functions {
   // @factory
   private Functions() {}
  /** Renvoie l'instance de la proglet. */
  private static Panel getPane() {
    return getProgletPane();
  }
  /** Permet de changer le message affiché sur le panel de la proglet */
  public static void setMessage(String text) {
    getPane().label.setText(text);
  }
}
