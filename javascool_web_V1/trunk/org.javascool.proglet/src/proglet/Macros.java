/*******************************************************************************
 * Thierry.Vieville@sophia.inria.fr, Copyright (C) 2009.  All rights reserved. *
 *******************************************************************************/

package proglet;

/** Cette factory contient des functions générales pour l'utilisateur de proglets.  
 * Elle permet de définir des fonctions statiques qui seront utilisées pour faire des programmes élèves.
 * @see <a href="Macros.java">code source</a>
 */
public class Macros { private Macros() { }

  /** Affiche une chaîne de caractères sur la console.
   * @param string La chaine à afficher.
   */
  public static void echo(String string) { System.out.println(string); }
  public static void echo(int string) { echo (""+string); }
  public static void echo(double string) { echo (""+string); }
  public static void echo(boolean string) { echo (""+string); }

  /** Renvoie x à la puissance y.
   * @param x 1er argument.
   * @param y 2eme argument.
   */
  public static double pow(double x, double y) { return Math.pow(x, y); }

  /** Renvoie un nombre aléatoire uniformément distribué entre 0 et 1.
   */
  public static double random() { return Math.random(); }

  /** Renvoie true si deux chaines de caratères sont égales, faux sinon.
   * @param string1 L'une des chaines à comparer.
   * @param string2 L'autre des chaines à comparer.
   */
  public static boolean equal(String string1, String string2) { return string1.equals(string2); }

  /** Temporise une durée fixée.
   * Cela permet aussi de mettre à jour l'affichage.
   * @param delay Durée d'attente en milli-secondes.
   */
  public static void sleep(int delay) { try { if (delay > 0) Thread.sleep(delay); else Thread.yield(); } 
    catch(Exception e) { throw new RuntimeException("Program stopped !"); } 
  }
}
