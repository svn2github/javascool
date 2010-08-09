/*******************************************************************************
 * Thierry.Vieville@sophia.inria.fr, Copyright (C) 2009.  All rights reserved. *
 *******************************************************************************/

package org.javascool;

// used for the translation
import java.io.File;

// Used to register the proglets
import java.util.HashMap;

// Used to interface with the j5 compiler
import java.io.StringWriter;
import java.io.PrintWriter;

/** This factory defines how a Jvs code is translated into a Java code and compiled. 
 * The goal of the Jvs syntax is to ease the syntax when starting to program in an imperative language, like Java. 
 * <p>- This factory calls the java compiler in the jdk5 (and earlier) case. It is designed to be used in standalone mode.</p>
 * @see <a href="Jvs2Java.java">source code</a>
 */
public class Jvs2Java { private Jvs2Java() { }

  /** Registered proglets. */
  static final HashMap<String,String> proglets = new HashMap<String, String>();
  static {
    for (String proglet : Proglets.proglets) if (proglet.length() > 0) proglets.put(proglet.replaceFirst("^proglet\\.([^\\.]+)\\..*$", "$1"), proglet);
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

  /** Reformats a piece of Java source code. 
   * <p>- This is a restrictive mechanism dedicated to the JavaScool sub-language of Java.</p>
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

  /** Translates a Jvs code source.
   * @param filename The file path to translate: A <tt>.jvs</tt> file is read and the corresponding <tt>.java</tt> file is written.
   * @param proglet The proglet to use. Default is to make all proglets usable.
   *
   * @throws RuntimeException if an I/O exception occurs during file translation.
   */
  public static void translate(String filename, String proglet) {
    String file = filename.replaceAll("\\.[a-z]+$", "");
    String s = File.separatorChar == '\\' ? "\\\\" : File.separator, main = filename.replaceAll(".*"+s+"([^"+s+"]+)\\.[a-z]+$", "$1");
    String[] lines = Utils.loadString(file+".jvs").split("\n");
    StringBuffer head = new StringBuffer(), body = new StringBuffer();
    // Here is the translation loop
    {
      // Copies the user's code
      for(String line : lines) {
	if(line.matches("^\\s*import[^;]*;\\s*$")) {
	  head.append(line);
	  body.append("//"+line+"\n");
	} else {
	  body.append(translateOnce(line)+"\n");
	}
      }
      // Imports proglet's static methods
      head.append("import static org.javascool.Macros.*;");
      head.append("import org.javascool.Jvs2Java;");
      if (proglet.length() == 0) {	
	for(String p : proglets.keySet()) 
	head.append("import static "+proglets.get(p)+".*;");
      } else {
	head.append("import static "+proglets.get(proglet)+".*;");
      }
      // Declares the proglet's core as a Runnable in the Applet
      head.append("public class "+main+ " extends org.javascool.MainV2 {");
      head.append("  private static final long serialVersionUID = "+ (uid++) + "L;");
      head.append("  static { Jvs2Java.runnable = new ProgletRunnableMain(); }");
      head.append("}");
      head.append("class ProgletRunnableMain implements Runnable {");
      head.append("  private static final long serialVersionUID = "+ (uid++) + "L;");
      head.append("  public void run() { main(); }");
      body.append("}\n");
    }
    Utils.saveString(file+".java", head.toString()+body.toString());
  }
  static void translate(String filename) {
    translate(filename, "");
  }
  // Translates one line of the source file
  private static String translateOnce(String line) {
    // Translates the while statement with sleep
    line = line.replaceFirst("(while.*\\{)", "$1 sleep(10);");
    // Translates the Synthe proglet @tone macro
    line = line.replaceFirst("@tone:(.*)", 
      "proglet.synthesons.Main.tone = new org.javascool.SoundBit() { public double get(char c, double t) { return $1; } }; proglet.synthesons.Main.syntheSet(\"16 a\");");
    return "    "+line;
  }
  // Counter used to increment the serialVersionUID in order to reload the different versions of the class
  private static int uid = 1;


  /** Compiles a Java code source
   * <div>The jdk5 <tt>tool.jar</tt> must be in the path.</div>
   * @param filename The file path to compile
   * @return An empty string if the compilation succeeds, else the error message's text.
   *
   * @throws RuntimeException if an I/O exception occurs during command execution.
   */
  public static String compile(String filename) {
    String args[] = { filename };
    StringWriter out = new StringWriter();
    try { 
      Class.forName("com.sun.tools.javac.Main").getDeclaredMethod("compile", Class.forName("[Ljava.lang.String;"), Class.forName("java.io.PrintWriter")).
	invoke(null, args, new PrintWriter(out)); 
    } catch(Exception e) { 
      throw new RuntimeException("Erreur: impossible de compiler, il y a une erreur d'installation ("+e+"), contacter http://javascool.gforge.inria.fr/proglet");
    }
    return out.toString().trim().replaceAll(filename.replaceAll("\\\\", "\\\\\\\\"), new File(filename).getName());
  }

  /** Executes a Java source code proglet.
   * @param jclass The Java class to compile.
   *
   * @throws RuntimeException if an I/O exception occurs during command execution.
   */
  public static void execute(String jclass) {
  }

  /** This is the entry point to run the proglet pupil's program: do not modify manually !!. */
  public static Runnable runnable = null;
}
