/*******************************************************************************
 * Thierry.Vieville@sophia.inria.fr, Copyright (C) 2009.  All rights reserved. *
 *******************************************************************************/

package org.javascool;

// Used to read/write jvs2java files
import java.io.File;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.FileWriter;

import java.util.regex.Pattern;

/** This factory defines how a Jvs code is translated into a Java code and compiled. 
 * The goal of the Jvs syntax is to ease the syntax when starting to program in an imperative language, like Java. 
 * - This factory calls the java compiler in the jdk5 (and earlier) case. It is designed to be used in standalone mode. 
 * @see <a href="Jvs2Java.java">source code</a>
 */
public class Jvs2Java { private Jvs2Java() { }

  // Counter used to increment the serialVersionUID in order to reload the different versions of the class
  static int uid = 1;

  /** Translates a Jvs code source.
   * @param filename The file path to translate.
   */
  static void translate(String filename) throws IOException {
    String s = File.separatorChar == '\\' ? "\\\\" : File.separator, 
      main = filename.replaceAll(".*"+s+"([^"+s+"]+)\\.[a-z]+$", "$1"), file = filename.replaceAll("\\.[a-z]+$", "");
    File jvs = new File(file+".jvs"), jav = new File(file+".java");
    if (!jvs.exists()) throw new IOException("File not found: "+jvs);
    BufferedReader in = new BufferedReader(new FileReader(file+".jvs"));
    PrintWriter out = new PrintWriter(new FileWriter(file+".java"));
    // Here is the translation loop
    {
      // Imports for general swing programming
      out.print("import org.javascool.*;");
      // Imports proglet's static methods
      out.print("import static org.javascool.Macros.*;");
      /***************************************
      for(String proglet : Proglets.proglets) 
	out.print("import static org.javascool."+proglet+".*;");
      ****************************************/
      // Declares the proglet's core as a Runnable in the Applet
      out.print("public class "+main+ " extends org.javascool.InterfacePrincipale {");
      out.print("  private static final long serialVersionUID = "+ (uid++) + "L;");
      out.print("  static { runnable = new ProgletRunnableInterfacePrincipale(); }");
      out.print("}");
      out.print("class ProgletRunnableInterfacePrincipale implements Runnable {");
      out.print("  private static final long serialVersionUID = "+ (uid++) + "L;");
      out.print("  public void run() { main(); }");
      // Copies the user's code
      for(String line; (line = in.readLine()) != null; ) {
	out.println(translateOnce(line));
      }
      out.println("}");
    }
    in.close();
    out.close();
  }

  // Translates one line of the source file
  private static String translateOnce(String line) {
    // Translates the while statement with sleep
    line = line.replaceFirst("(while.*\\{)", "$1 sleep(10);");
    // Translates the Synthe proglet @tone macro
    line = line.replaceFirst("@tone:(.*)", "org.javascool.Synthe.tone = new org.javascool.SoundBit() { public double get(char c, double t) { return $1; } }; org.javascool.Synthe.syntheSet(\"16 a\");");
    return "    "+line;
  }

  /** Reformats a piece of Java source code. 
   * - This is a restrictive mechanism dedicated to the JavaScool sub-language of Java.
   * @param text The text to reformat.
   * @return The reformated text.
   */
  public static String reformat(String text) {    
    int TAB = 3;
    // Gets the text and reduce all spaces
    String text0 = text.trim(), text1 = "";
    for(int i = 0, tab = 0, par = 0, esc = 0, ln = -1; i < text0.length(); i++) {
      char c0 = text0.charAt(i), c1 = i == 0 ? ' ' : text0.charAt(i - 1);
      // Writeln if required
      if (ln >= 0) { if (c0  == '}') ln -= TAB; text1 += "\n"; for(int n = 0; n < ln; n++) text1 += " "; ln = -1; }
      // Escapes /* comments 
      if (c1 == '/' && c0 == '*' && esc == 0) esc = 3;
      if (c1 == '*' && c0 == '/' && esc == 3) esc = -3;
      // Escapes // comments
      if (c1 == '/' && c0 == '/' && esc == 0) esc = 2;
      if (c0 == '\n' && esc == 2) esc = -2;
      // Escapes "strings" until end-of-line
      if (c0 == '"' && c1 != '\\' && esc == 0) esc = 1;
      else if (((c0 == '"' && c1 != '\\') || c0 == '\n') && esc == 1) esc = -1;
      // Mirrors char if escaping
      if (esc != 0) {
	if (esc != -2) text1 += c0;
	if (esc < -1) ln = tab;
	if (esc < 0) esc = 0;
      // Normalizes spaces
      } else if (!(Character.isWhitespace(c0) && Character.isWhitespace(c1))) {
	if (Character.isWhitespace(c0)) c0 = ' ';
	text1 += c0;
	// Counts (parenthesies)
	if (c0 == '(') par++;
	if (c0 == ')') par--;
	// Reformats {blocks}
	if (c0 == '{' || c0 == '}' || (c0 == ';' && par == 0)) {
	  if (c0 == '{') { tab += TAB; }
	  if (c0 == '}') { tab -= TAB; }
	  // Cleans spaces after symbol
	  for(;i < text0.length() - 1 && Character.isWhitespace(text0.charAt(i + 1)); i++);
	  // Decides ln
	  boolean esl = text0.substring(i).startsWith("} else");
	  if (!esl) 
	    ln = tab;
	}
      }
    }
    return "\n"+text1;
  }

  /** Tests if a word is reserved to use because it is a Java reserved word. 
   * @param word The word to test.
   * @return true if it reserved, else false.
   */
  public static boolean isReserved(String word) {
    for(int i = 0; i < Reserved.length; i++) if (word.equals(Reserved[i])) return true; return false;
  }
  /** The list of all Java reserved words. */
  public static final String Reserved[] = { 
    "abstract", "assert", "boolean", "break", "byte", "case", "catch", "char", "class", "const", "continue", "default", "double", "do", 
    "else", "enum", "extends", "false", "final", "finally", "float", "for", "goto", "if", "implements", "import", "instanceof", "int", "interface", 
    "long", "native", "new", "null", "package", "private", "protected", "public", "return", "short", "static", "strictfp", "super", "switch", "synchronized", 
    "this", "throw", "throws", "transient", "true", "try", "void", "volatile", "while"};
  /** The list of all JavaScool declared words. */
  public static final String Declared[] = { 
    "echo", "equal", "sqrt", "pow", "random", "now", "sleep", "show",
    "clear", "println", "readString", "readInt", "readInteger", "readDouble", "readFloat", "readBoolean", 
    "dichoLength","dichoCompare",
    "smileyReset", "smileyLoad", "smileySet", "smileyGet",  
    "scopeReset", "scopeSet", "scopeAdd",  "scopeAddLine", "scopeAddRectangle", "scopeAddCircle", "scopeX", "scopeY",
    "convaOut",  "convaCompare", 
    "synthePlay", "syntheSet", "@tone",
    "String", "main"
  };

  /** Compiles a Java code source
   * @param filename The file path to compile
   * @return An empty string if the compilation succeeds, else the error message's text.
   */
  static String compile(String filename) throws IOException {
    String args[] = { filename };
    StringWriter out = new StringWriter();
    // Implements com.sun.tools.javac.Main.compile(args, new PrintWriter(out)); in reflexive form to avoid external compilation problems
    try { 
      Class.forName("com.sun.tools.javac.Main").getDeclaredMethod("compile", Class.forName("[Ljava.lang.String;"), Class.forName("java.io.PrintWriter")).
	invoke(null, args, new PrintWriter(out)); 
    } catch(Exception e) { 
      System.out.println("Error: impossible de compiler, il y a une erreur d'installation ("+e+"), contacter http://javascool.gforge.inria.fr/proglet");
    }
    return out.toString().trim().replaceAll(filename.replaceAll("\\\\", "\\\\\\\\"), new File(filename).getName());
  }
}

