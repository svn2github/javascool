/*******************************************************************************
 * Thierry.Vieville@sophia.inria.fr, Copyright (C) 2009.  All rights reserved. *
 *******************************************************************************/

package proglet;

/** Cette factory contient des functions générales pour l'utilisateur de proglets. */
public class Macros {

  /** Echos a string in the console.
   * @param string The string to echo.
   */
  public static void echo(String string) { System.out.println(string); }

  /** Returns true of two strings are equals else false.
   * @param string1 The string to compare.
   * @param string2 The other string to compre.
   */
  public static boolean equals(String string1, String string2) { return string1.equals(string2); }

  /** Sleeps and purge the graphic's drawings.
   * @param delay Delay in msec.
   */
  public static void sleep(int delay) { try { if (delay > 0) Thread.sleep(delay); else Thread.yield(); } catch(Exception e) { } }
}
