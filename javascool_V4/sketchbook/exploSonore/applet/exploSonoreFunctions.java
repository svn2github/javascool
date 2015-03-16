/** Définit les fonctions de la proglet.
 *
 * @see <a href="exploSonoreFunctions.java.html">code source</a>
 * @serial exclude
 */
public class exploSonoreFunctions {
  private static final long serialVersionUID = 1L;
  /** Renvoie l'instance de la proglet pour accéder à ses éléments. */
  private static exploSonore getPanel() {
    return org.javascool.macros.Macros.getProgletPane();
  }
  /** Joue un signal de type choisi.
   * @param c : numero du canal à lancer: 1, 2 ou 3.
   * @param n nom du type: "sinus", "carré", "scie", "bruit".
   * @param f fréquence du signal en Herz.
   * @param a amplitude du signal entre 0 et 1.
   */
  public static void playSignal(int c, String n, double f, double a) {
    switch(c) {
    case 1:
      getPanel().signal1.setSignal(n, (float) f, (float) a, false);
      break;
    case 2:
      getPanel().signal2.setSignal(n, (float) f, (float) a, false);
      break;
    case 3:
      getPanel().signal3.setSignal(n, (float) f, (float) a, false);
      break;
    }
  }
  /** Joue un enregistrement de son choix.
   * @param path Nom de l'extrait
   * @param f fréquence de coupure du signal en Herz.
   */
  public static void playRecord(String path, double frequence) {
    getPanel().record1.setRecord(path);
    getPanel().record1.setFilter(path, (float) frequence);
  }
  /** Arrête l'émission sonore. */
  public static void playStop() {
    getPanel().StopAnySound();
  }
}

