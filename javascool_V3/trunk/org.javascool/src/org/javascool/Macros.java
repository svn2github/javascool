/*******************************************************************************
* Thierry.Vieville@sophia.inria.fr, Copyright (C) 2009.  All rights reserved. *
*******************************************************************************/

package org.javascool;

import java.util.Calendar;

/** Cette factory contient des functions générales rendues visibles à l'utilisateur de proglets.
 * Elle permet de définir des fonctions statiques qui seront utilisées pour faire des programmes élèves.
 * @see <a href="Macros.java.html">code source</a>
 * @serial exclude
 */
public class Macros {
  private Macros() {}

  /** Affiche une chaîne de caractères sur la console.
   * @param string La chaine à afficher.
   */
  public static void echo(String string) {
    System.out.println(string);
  }
  public static void echo(int string) {
    echo("" + string);
  }
  public static void echo(double string) {
    echo("" + string);
  }
  public static void echo(boolean string) {
    echo("" + string);
  }
  public static < A, B > void echo(java.util.Map<A, B> map) {
    echo("" + map);
  }
  public static < A > void echo(java.util.List<A> list) {
    echo("" + list);
  }
  public static < A > void echo(java.util.Set<A> set) {
    echo("" + set);
  }
  /** Renvoie un nombre entier aléatoire uniformément distribué entre deux valeurs (maximum inclus).
   */
  public static int random(int min, int max) {
    return (int) Math.floor(min + (0.999 + max - min) * Math.random());
  }
  /** Renvoie true si deux chaines de caratères sont égales, faux sinon.
   * @param string1 L'une des chaines à comparer.
   * @param string2 L'autre des chaines à comparer.
   */
  public static boolean equal(String string1, String string2) {
    return string1.equals(string2);
  }
  public final static int maxInteger = Integer.MAX_VALUE;

  /** Renvoie le temps actuel en milli-secondes.
   * @return Renvoie la différence, en millisecondes, entre le temps actuel et celui du 1 Janvier 2000, minuit, en utilisant le temps universel coordonné.
   */
  public static double now() {
    return System.currentTimeMillis() - offset;
  }
  private static long offset;
  static {
    Calendar ref = Calendar.getInstance();
    ref.set(2000, 0, 1, 0, 0, 0);
    offset = ref.getTimeInMillis();
  }

  /** Temporise une durée fixée.
   * Cela permet aussi de mettre à jour l'affichage.
   * @param delay Durée d'attente en milli-secondes.
   */
  public static void sleep(int delay) {
    try {
      if(delay > 0)
        Thread.sleep(delay);
      else
        Thread.sleep(0, 10000);
    } catch(Exception e) { throw new RuntimeException("Programme arrêté !");
    }
  }
  /** Vérifie une assertion et arrête le code si elle est fausse.
   * @param condition Si la condition n'est pas vérifiée, le code JavaScool va s'arrêter.
   * @param message Un message s'imprime sur la console pour signaler l'erreur.
   */
  public static void assertion(boolean condition, String message) {
    System.err.println("#" + condition + " : " + message);
    if(!condition) {
      System.out.println(message);
      Jvs2Java.run(false);
      sleep(500);
    }
  }
}
