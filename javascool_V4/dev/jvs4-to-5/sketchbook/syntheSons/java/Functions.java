/*******************************************************************************
* Thierry.Vieville@sophia.inria.fr, Copyright (C) 2009.  All rights reserved. *
*******************************************************************************/
package org.javascool.proglets.syntheSons;
import static org.javascool.macros.Macros.*;

import org.javascool.tools.sound.SoundBit;

/** Défines the JavaScool user functions for this proglet.
 *
 * @see <a href="Functions.java.html">code source</a>
 * @serial exclude
 */
public class Functions {
  private static final long serialVersionUID = 1L;
  // @factory
  private Functions() {}
  /** Renvoie l'instance de la proglet pour accéder à ses éléments. */
  private static Panel getPane() {
    return getProgletPane();
  }
  /** Definit le son à utiliser pour jouer les notes du synthétiseur.
   * <div>Utilisé à travers la construction de la forme <tt>TONE: <i>expression de la variable t</i></tt></div>
   */
  static public SoundBit tone = null;

  /** Retourne la valeur d'une sinusoïde de période une seconde et d'amplitude unité. */
  static public double sns(double t) {
    return Math.sin(2 * Math.PI * t);
  }
  /** Retourne la valeur d'un signal carré de période une seconde et d'amplitude unité. */
  static public double sqr(double t) {
    return (int) (t * 2) % 2 == 0 ? 1 : -1;
  }
  /** Retourne la valeur d'un signal triangulaire de période une seconde et d'amplitude unité. */
  static public double tri(double t) {
    return (int) (t * 2) % 2 == 0 ? (-1 + 4 * (t - (int) t)) : (3 - 4 * (t - (int) t));
  }
  /** Retourne la valeur d'un souffle (``bruit blanc´´). */
  static public double noi(double t) {
    return 2 * Math.random() - 1;
  }
  /** Définit les notes à jouer dans cet interface.
   * @param notes Définition des <a href="NotesSoundBit.html#reset(java.lang.String)">notes</a> selon une syntaxe simplifiée.
   */
  static public void setNotes(String notes) {
    getPane().sound.reset(notes);
    getPane().reset(getPane().sound, 'l');
  }
  /** Fait entendre le son à travers le système audio. */
  static public void play() {
    try { getPane().sound.play();
    } catch(Exception e) {
      System.out.println(e.toString());
    }
  }
}
