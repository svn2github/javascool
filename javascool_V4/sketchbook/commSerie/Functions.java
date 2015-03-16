/*******************************************************************************
* Thierry.Vieville@sophia.inria.fr, Copyright (C) 2009.  All rights reserved . *
*******************************************************************************/

package org.javascool.proglets.commSerie;

import static org.javascool.macros.Macros.*;
import javax.swing.JPanel;

/** Définit les fonctions de la proglet qui permet d'utiliser toute les classes des swings.
 *
 * @see <a href="http://java.sun.com/docs/books/tutorial/uiswing">Java Swing tutorial</a>
 * @see <a href="http://java.sun.com/javase/6/docs/api/javax/swing/package-summary.html">Java Swing API</a>
 * @see <a href="Functions.java.html">code source</a>
 * @serial exclude
 */
public class Functions {
  private static final long serialVersionUID = 1L;
   // @factory
   private Functions() {}
  /** Renvoie le panneau d'affichage de la proglet. */
  public static Panel getPane() {
    return getProgletPane();
  }
  /** Initialise le panneau avec le mode d'affichage souhaité.
   * @param displayMode Précise si:<ul>
   *  <li>"C" : le panneau de contrôle des paramètres et d'ouverture/fermeture du port est affiché</li>
   *  <li>"D" : le panneau de dialogue entrée/sortie avec le port est affiché</li>
   *  <li>"CD" : les deux panneaux sont affichés (défaut)</li>
   *  <li>"" : rien n'est affiché.</li>
   * </ul>
   */
  public static void removeAll(String displayMode) {
    getPane().removeAll(displayMode);
  }
  /** Renvoie l'interface série du panneau d'affichage de la proglet. */
  public static SerialInterface getSerialInterface() {
    return getPane().serial;
  }
  /** Envoie une chaine à travers l'interface.
   * @param string La chaîne à envoyer.
   */
  public static void writeString(String string) {
    getPane().serial.write(string);
  }
  /** Reçoit un caractère à travers l'interface.
   * <p>L'octet lu est retiré du buffer de lecture, après l'appel de cette fonction.</p>
   * @return La valeur de l'octet à lire ou -1 si il n'y a pas d'octet à lire.
   */
  public static int readChar() {
    return getPane().serial.read();
  }
  /** Renvoie tous les octets actuellement dans le buffer.
   * @return Un tableau avec tous les octets actuellement dans le buffer.
   */
  public static int[] getChars() {
    return getPane().serial.getChars();
  }
}
