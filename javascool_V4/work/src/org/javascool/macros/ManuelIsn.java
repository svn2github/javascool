//  Ces programmes sont sous licence CeCILL-B V1.
// ----------------------------------------------------------------------
// Isn.java
// ----------------------------------------------------------------------
// Julien Cervelle et Gilles Dowek, version javascool.

package org.javascool.macros;
import static org.javascool.macros.Macros.*;

// io
import java.util.Scanner;
import java.io.OutputStreamWriter;

/** Définit les fonctions string et entrée/sortiers qui permettent de faire les exercices Isn.
 *
 * @see <a href="ManuelIsn.java.html">code source</a>
 * @serial exclude
 */
public class ManuelIsn {
  private static final long serialVersionUID = 1L;

  // ----------------------------------------------------------------------
  // Strings
  // ----------------------------------------------------------------------

  /** Renvoie vrai si les deux chaînes sont égales, faux sinon. */
  public static boolean stringEqual(String s1, String s2) {
    return s1.equals(s2);
  }  
  /** Renvoie vrai si la chaîne s1 précède dans l'ordre alphabétique la chaîne s2 ou si les chaînes sont égales, faux sinon. */
  public static boolean stringAlph(String s1, String s2) {
    return s1.compareTo(s2) <= 0;
  }
  /** Renvoie le n-ième caractère de la chaîne. */
  public static String stringNth(String s, int n) {
    return s.substring(n, n + 1);
  }
  /** Renvoie la longueur de la chaîne. */
  public static int stringLength(String s) {
    return s.length();
  }
  /** Renvoie le caractère de code ASCII n. */
  public static String asciiString(int n) {
    byte[] b;
    b = new byte[1];
    b[0] = (byte) n;
    return new String(b);
  }
  /** Renvoie le code ASCII du 1er caractère de la chaîne. */
  public static int stringCode(String s) {
    return (int) (s.charAt(0));
  }
  
  // ----------------------------------------------------------------------
  // io
  // ----------------------------------------------------------------------

  private static final Object readMonitor = new Object();

  private static Scanner scanner(java.io.Reader in) {
    Scanner scanner = new Scanner(in);
    scanner.useLocale(java.util.Locale.US);
    return scanner;
  }
  private static final Object writeMonitor = new Object();

  private static volatile java.io.PrintStream output = System.out;

  /** Ouvre un fichier en lecture. */
  public static Scanner openIn(String name) {
    try {
      java.io.FileInputStream fis = new java.io.FileInputStream(name);
      Scanner scanner = scanner(new java.io.InputStreamReader(fis));
      return scanner;
    } catch(java.io.FileNotFoundException e) {
      return scanner(new java.io.InputStreamReader(System.in));
    }
  }
  /** Ferme un fichier en lecture. */
  public static void closeIn(Scanner s) {
    s.close();
  }
  /** Lit un entier à partir d'un fichier. */
  public static int readIntFromFile (Scanner s) {
    synchronized (readMonitor) {
      return s.nextInt();}}

  /** Lit un nombre décimal à partir d'un fichier. */
  public static double readDoubleFromChar(Scanner s) {
    synchronized (readMonitor) {return s.nextDouble();}}

  private static final java.util.regex.Pattern DOT = java.util.regex.Pattern.compile(".", java.util.regex.Pattern.DOTALL);

  /** Lit un caratère à partir d'un fichier. */
  public static String readCharacterFromFile(Scanner s) {
    synchronized (readMonitor) {
      return String.valueOf(s.findWithinHorizon(DOT,1).charAt(0));}}

  private static final java.util.regex.Pattern EOLN = java.util.regex.Pattern.compile(".*?(?:\r(?!\n)|\n|\r\n)");
  private static final java.util.regex.Pattern ALL = java.util.regex.Pattern.compile(".*+");

  /** Lit un chaîne à partir d'un fichier. */
  public static String readStringFromFile (Scanner s) {
    String r = s.findWithinHorizon(EOLN,0);
    if (r == null)
      return s.findWithinHorizon(ALL,0);
    if (r.length() == 1)
      return "";
    int pos=r.charAt(r.length()-2) == '\r' ? r.length()-2 : r.length()-1;  
    return r.substring(0,pos);
  }

  /** Ouvre un fichier en écriture. */
  public static OutputStreamWriter openOut(String name) {
    try {
      java.io.FileOutputStream fos = new java.io.FileOutputStream(name);
      java.io.OutputStreamWriter out = new java.io.OutputStreamWriter(fos, "UTF-8");
      return out;
    } catch(java.io.FileNotFoundException e) {
      return new java.io.OutputStreamWriter(System.out);
    } catch(java.io.UnsupportedEncodingException e) {
      return new java.io.OutputStreamWriter(System.out);
    }
  }
  /** Ferme un fichier en écriture. */
  public static void closeOut(OutputStreamWriter s) {
    try {
      s.close();
    } catch(java.io.IOException e) {}
  }
  /** Ecrit un passage à la ligne dans un fichier. */
  public static void printlnToFile(OutputStreamWriter s) {
    try { 
      s.write(System.getProperty("line.separator"));
    } catch(java.io.IOException e) {}
  }
  /** Ecrit un passage une chaîne dans un fichier. */
  public static void printToFile(OutputStreamWriter s, String n) {
    try { 
      s.write(n);
    } catch(java.io.IOException e) {}
  }
  /** Ecrit un passage une chaîne et un passage à la ligne dans un fichier. */
  public static void printlnToFile(OutputStreamWriter s, String n) {
    printToFile(s, n);
    printlnToFile(s);
  }
  /** Ecrit un passage un entier dans un fichier. */
  public static void printToFile(OutputStreamWriter s, int n) {
    try {
      s.write(String.valueOf(n));
    } catch(java.io.IOException e) {}
  }
  /** Ecrit un passage un entier et un passage à la ligne dans un fichier. */
  public static void printlnToFile(OutputStreamWriter s, int n) {
    printToFile(s, n);
    printlnToFile(s);
  }
  /** Ecrit un passage un nombre décimal dans un fichier. */
  public static void printToFile(OutputStreamWriter s, double n) {
    try {
      s.write(String.valueOf(n));
    } catch(java.io.IOException e) {}
  }
  /** Ecrit un passage un nombre décimal et un passage à la ligne dans un fichier. */
  public static void printlnToFile(OutputStreamWriter s, double n) {
    printToFile(s, n);
    printlnToFile(s);
  }
}
