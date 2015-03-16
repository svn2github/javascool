package org.javascool.proglets.analogNumerique;
import static org.javascool.macros.Macros.*;

/**  Définit les fonctions de la proglet qui permet un algorithme dichotomiqiue de conversion analogique-numérique.
 *
 * @see <a href="Functions.java.html">code source</a>
 * @serial exclude
 */
public class Functions {
  private static final long serialVersionUID = 1L;
   // @factory
   private Functions() {}
  private static Panel getPane() {
    return getProgletPane();
  }
  /** Applique une tension en sortie.
   * @param value La tension en milli-volts entre 0 et 1023.
   */
  static public void output(int value) {
    Functions.value = value;
    getPane().out.setText(value + " mV");
  }
  private static int value = 0;

  /** Compare la tension appliquée en sortie à la tension inconnue.
   * @return -1 si la tension inconnue est plus petite et 1 si elle plus grande ou égale.
   */
  public static int compare() {
    int r = getPane().value.getValue() < Functions.value ? -1 : 1;
    getPane().cmp.setText("" + r);
    return r;
  }
}
