/*******************************************************************************
 * Thierry.Vieville@sophia.inria.fr, Copyright (C) 2009.  All rights reserved. *
 *******************************************************************************/

package org.javascool;

// used for the translation
import java.io.File;

// Used to register the proglets
import javax.swing.JPanel;
import java.util.HashMap;

// Used to interface with the j5 compiler
import java.io.StringWriter;
import java.io.PrintWriter;

// Used to load class
import java.net.URL;
import java.net.URLClassLoader;

/** This factory defines how a Jvs code is translated into a Java code and compiled. 
 * The goal of the Jvs syntax is to ease the syntax when starting to program in an imperative language, like Java. 
 * <p>- This factory calls the java compiler in the jdk5 (and earlier) case. It is designed to be used in standalone mode.</p>
 * @see <a href="Jvs2Java.java.html">source code</a>
 * @serial exclude
 */
public class Jvs2Java { private Jvs2Java() { }

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
	  ln = tab;
	}
      }
    }
    return "\n"+text1.replaceAll("\\}\\s*else\\s*\\{", "} else {");
  }

  /** Translates a Jvs code source.
   * @param path The file path to translate: A <tt>.jvs</tt> file is read and the corresponding <tt>.java</tt> file is written.
   * @param proglet The proglet to use. Default is to make all proglets usable.
   *
   * @throws RuntimeException if an I/O exception occurs during file translation.
   * @throws IllegalArgumentException If the Java class name is not valid.
   */
  public static void translate(String path, String proglet) {
    setJpathclass(path);
    String text = Utils.loadString(jpath+".jvs");
    String[] lines = text.split("\n");
    StringBuffer head = new StringBuffer(), body = new StringBuffer();
    // Here is the translation loop
    {
      // Copies the user's code
      for(String line : lines) {
	if(line.matches("^\\s*(import|package)[^;]*;\\s*$")) {
	  head.append(line);
	  body.append("//"+line+"\n");
	} else {
	  body.append(translateOnce(line)+"\n");
	}
      }
      // Imports proglet's static methods
      head.append("import static org.javascool.Macros.*;");
      head.append("import static java.lang.Math.*;");
      if (proglet.length() == 0) {	
	for(String p : proglets.keySet()) 
	head.append("import static "+proglets.get(p)+".*;");
      } else {
	head.append("import static "+proglets.get("ingredients")+".*;");
	if (!"ingredients".equals(proglet))
	  head.append("import static "+proglets.get(proglet)+".*;");
      }
      // Declares the proglet's core as a Runnable in the Applet
      // - defined as a ProgletApplet in order to be loaded as an executable applet.
      head.append("public class "+jclass+ " extends org.javascool.ProgletApplet implements Runnable {");
      head.append("  private static final long serialVersionUID = "+ (uid++) + "L;");
      head.append("  static { org.javascool.Jvs2Java.runnable = new "+jclass+ "(); }");
      head.append("  public void run() { main(); }");
      body.append("}\n");
    }
    Utils.saveString(jpath+".java", head.toString()+body.toString());
  }
  static void translate(String path) {
    translate(path, "");
  }
  // Translates one line of the source file
  private static String translateOnce(String line) {
    // Translates the while statement with sleep
    line = line.replaceAll("(while.*\\{)", "$1 sleep(0);");
    // Translates the Synthe proglet @tone macro
    line = line.replaceFirst("@tone:(.*)\\s*;", 
      "proglet.synthesons.SoundDisplay.tone = new org.javascool.SoundBit() { public double get(char c, double t) { return $1; } }; proglet.synthesons.SoundDisplay.syntheSet(\"16 a\");");
    return "    "+line;
  }
  // Counter used to increment the serialVersionUID in order to reload the different versions of the class
  private static int uid = 1;

  // Properly defines the java path and java class from a file name
  private static void setJpathclass(String path) {
    jpath = path.replaceAll("\\.[a-z]+$", "");
    if (!jpath.matches(".*"+File.separator+".*")) jpath = System.getProperty("user.dir")+File.separator+jpath;
    String s = File.separatorChar == '\\' ? "\\\\" : File.separator;
    jclass = jpath.replaceAll(".*"+s+"([^"+s+"]+)$", "$1");
    if (isReserved(jclass) || Utils.toName(jclass) != jclass) throw new IllegalArgumentException("Bad Java class name \""+jclass+"\"");
  }
  private static String jpath, jclass;

  /** Compiles a Java code source.
   * <div>The jdk5 <tt>tool.jar</tt> must be in the path.</div>
   * @param path The file path to compile.
   * @return An empty string if the compilation succeeds, else the error message's text.
   *
   * @throws RuntimeException if an I/O exception occurs during command execution.
   */
  public static String compile(String path) {
    setJpathclass(path);
    String args[] = { jpath+".java" };
    StringWriter out = new StringWriter();
    try { 
      Class.forName("com.sun.tools.javac.Main").getDeclaredMethod("compile", Class.forName("[Ljava.lang.String;"), Class.forName("java.io.PrintWriter")).
	invoke(null, args, new PrintWriter(out)); 
    } catch(Throwable e) { 
      throw new RuntimeException("Erreur: impossible de compiler, il y a une erreur d'installation ("+e+"), contacter http://javascool.gforge.inria.fr");
    }
    return out.toString().trim().replaceAll(path.replaceAll("\\\\", "\\\\\\\\"), new File(path).getName());
  }

  /** Dynamically loads a Java class to be used during this session.
   * @param path The path to the java class to load. The java class is supposed to belong to the "default" package, i.e. not to belong to a package.
   * @return An instantiation of this Java class.
   *
   * @throws RuntimeException if an I/O exception occurs during command execution.
   * @throws IllegalArgumentException If the Java class name is not valid.
   */
  public static Object load(String path) {  
    setJpathclass(path);
    try {
      URL[] urls = new URL[] { new URL("file:"+new File(jpath).getParent()+File.separator) };
      Class<?> j_class = new URLClassLoader(urls).loadClass(jclass);
      return j_class.newInstance(); 
    } catch(Throwable e) { 
      throw Utils.report(new RuntimeException("Erreur: impossible de charger "+jpath+" / "+jclass+" ("+e+") \n . . le package est il mal défini ?"));
    }
  }

  /** Compiles and saves a HTML launcher page in order to run the compile proglet as an applet.
   * DISCLAIMER: DO NOT USE, TO BE VALIDATED + SEE HOW TO USE JAVASCRIPT TO AVOID RELOAD
   * @param activity The activity name or index.
   * @param path The file path of the proglet code.
   *
   * @throws RuntimeException if an I/O exception occurs during command execution.
   * @throws IllegalArgumentException If the Java class name is not valid.
   * /
  public static void saveHtmlLauncher(String activity, String path) { 
    setJpathclass(path);
    Utils.saveString(path + ".html", 
      "<html><head><meta http-equiv='pragma' content='no-cache'/></head><body><table><tr><td><div id='js-exec-bar'>\n" +
      "</div></td></tr><tr><td>\n" +
      "<p><applet code='org.javascool.Main' archive='http://javascool.gforge.inria.fr/v3/javascool.jar' width='800' height='600'>" +
      "  <param name='activity' value='"+activity+"'/><!--param name='file' value='"+jpath+"'/--></applet></p>\n" +
      "</td></tr></table></body></html>\n");
  }
  */

  /** Registered proglets. */
  static final HashMap<String,String> proglets = new HashMap<String, String>();
  static {
    for (String proglet : org.javascool.proglets.proglets) if (proglet.length() > 0) proglets.put(proglet.replaceFirst("^proglet\\.([^\\.]+)\\..*$", "$1"), proglet);
  }

  /** Returns the proglet panel.
   * @param proglet The proglet class name.
   * @return The panel corresponding to the proglet, if any, else null;
   */
  public static JPanel getPanel(String proglet) {
    if (Jvs2Java.proglets.containsKey(proglet)) {
      try { return (JPanel) Class.forName(Jvs2Java.proglets.get(proglet)).getField("panel").get(null); } catch(Exception e) { return null; }
    } else
      return null;
  }

  /** Runs/Stops the proglet pupil's program. 
   * @param start If true starts the proglet pupil's program, if defined. If false stops the proglet pupil's program. 
   */
  public static void run(boolean start) {
    if (thread != null) { thread.interrupt(); thread = null; }
    if (start && runnable != null) (thread = new Thread(new Runnable() { public void run() {
      try { runnable.run(); } catch(Throwable e) { if ("Programme arrêté !".equals(e.getMessage())) System.out.println(e); else Utils.report(e); } 
    }})).start(); 
  }
  /** Runs/Stops a proglet demo.. 
   * @param proglet The proglet name. 
   */
  public static void run(String proglet) {
    if (thread != null) { thread.interrupt(); thread = null; }
    if (Jvs2Java.proglets.containsKey(proglet)) {
      Jvs2Java.proglet = proglets.get(proglet);
      (thread = new Thread(new Runnable() { public void run() {
	try { Class.forName(Jvs2Java.proglet).getDeclaredMethod("test").invoke(null); } catch(Throwable e) { Utils.report(e); } 
      }})).start();
    }
  }
  // This is the entry point to run  the proglet pupil's program: do not change directly !
  /**/public static Runnable runnable = null; private static String proglet = null; private static Thread thread = null;

  /** Translates Jvs files to Java files.
   * @param usage <tt>java org.javascool.Jvs2Java [-reformat] [-compile] input-file</tt>
   * <p><tt>-reformat</tt> : Reformat the Jvs code</p>
   * <p><tt>-compile</tt> : Compile the java classes</p>
   */
  public static void main(String[] usage) {
    if (usage.length > 0) {
      boolean reformat = false, compile = false; for(String option : usage) { reformat |= "-reformat".equals(option);  compile |= "-compile".equals(option); }
      if (reformat) Utils.saveString(usage[usage.length-1], reformat(Utils.loadString(usage[usage.length-1])));
      translate(usage[usage.length-1]);
      if (compile) { String out = Jvs2Java.compile(usage[usage.length-1]); System.out.println(out.length() == 0 ? "Compilation ok !" : out); }
    }
  }
}
