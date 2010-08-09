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

// Used to load class
import java.net.URL;
import java.net.URLClassLoader;

// Used to encapsulate a proglet
import javax.swing.JPanel;
import javax.swing.JApplet;
import java.awt.BorderLayout;
import javax.swing.JToolBar;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;

/** This factory defines how a Jvs code is translated into a Java code and compiled. 
 * The goal of the Jvs syntax is to ease the syntax when starting to program in an imperative language, like Java. 
 * <p>- This factory calls the java compiler in the jdk5 (and earlier) case. It is designed to be used in standalone mode.</p>
 * @see <a href="Jvs2Java.java">source code</a>
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
	  boolean esl = text0.substring(i).startsWith("} else");
	  if (!esl) 
	    ln = tab;
	}
      }
    }
    return "\n"+text1;
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
    String[] lines = Utils.loadString(jpath+".jvs").split("\n");
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
      // - defined as a MainV2 in order to be loaded in a java page as an executable applet.
      head.append("public class "+jclass+ " extends org.javascool.MainV2 {");
      head.append("  private static final long serialVersionUID = "+ (uid++) + "L;");
      head.append("  static { Jvs2Java.runnable = new ProgletRunnableMain(); }");
      head.append("}");
      head.append("class ProgletRunnableMain implements Runnable {");
      head.append("  private static final long serialVersionUID = "+ (uid++) + "L;");
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
    line = line.replaceFirst("(while.*\\{)", "$1 sleep(10);");
    // Translates the Synthe proglet @tone macro
    line = line.replaceFirst("@tone:(.*)", 
      "proglet.synthesons.Main.tone = new org.javascool.SoundBit() { public double get(char c, double t) { return $1; } }; proglet.synthesons.Main.syntheSet(\"16 a\");");
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
    String args[] = { path };
    StringWriter out = new StringWriter();
    try { 
      Class.forName("com.sun.tools.javac.Main").getDeclaredMethod("compile", Class.forName("[Ljava.lang.String;"), Class.forName("java.io.PrintWriter")).
	invoke(null, args, new PrintWriter(out)); 
    } catch(Exception e) { 
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
    } catch(Exception e) { 
      throw new RuntimeException("Erreur: impossible de charger "+jpath+" / "+jclass+", il y a une erreur d'installation ("+e+"), contacter http://javascool.gforge.inria.fr");
    }
  }

  /** Registered proglets. */
  static final HashMap<String,String> proglets = new HashMap<String, String>();
  static {
    for (String proglet : org.javascool.proglets.proglets) if (proglet.length() > 0) proglets.put(proglet.replaceFirst("^proglet\\.([^\\.]+)\\..*$", "$1"), proglet);
  }

  /** Returns the proglet panel.
   * @param proglet The proglet class name.
   * @return The panel corresponding to the proglet.
   */
  public static JPanel getPanel(String proglet) {
    try { return (JPanel) Class.forName(Jvs2Java.proglets.get(proglet)).getField("panel").get(null); } 
    catch(Exception e) { Utils.report(e); return new JPanel(); }
  }

  /** Runs/Stops the proglet pupil's program. 
   * @param start If true starts the proglet pupil's program. If false stops the proglet pupil's program. If given as a <tt>String</tt> runs the corresponding proglet demo.
   */
  public static void run(boolean start) {
    if (thread != null) { thread.interrupt(); thread = null; }
    if (start) (thread = new Thread(new Runnable() { public void run() {
      try {
	runnable.run();
      } catch(Exception e) { Utils.report(e); } 
    }})).start(); 
  }
  /**/public static void run(String proglet) {
    if (thread != null) { thread.interrupt(); thread = null; }
    Jvs2Java.proglet = proglet;
    (thread = new Thread(new Runnable() { public void run() {
      try { 
	Class.forName(Jvs2Java.proglets.get(Jvs2Java.proglet)).getDeclaredMethod("test").invoke(null); 
      } catch(Exception e) { Utils.report(e); } 
    }})).start();
  }
  // This is the entry point to run  the proglet pupil's program: do not change directly !
  /**/public static Runnable runnable = null; private static String proglet = "ingredients"; private static Thread thread = null;

  /** Defines a proglet applet. */
  static public class ProgletApplet extends JApplet { 
    private static final long serialVersionUID = 1L;
    /** Programmatic reset of the proglet parameters. 
     * <ul><li>Usually defined in the HTML tag: 
     * <div><tt>&lt;applet code="org.javascool.Jvs2Java.ProgletApplet.class" archive="javascool.jar" width="560" height="720"></tt></div>
     * <div><tt>&lt;param name="proglet" value="ingredients"/></tt></div>
     * <div><tt>&lt;param name="demo" value="true"/></tt></div>
     * <div><tt>&lt;/applet></tt></div>
     * </li><li>Or used in a standalone program:
     * <div><tt>Utils.show(new Jvs2Java.ProgletApplet().reset("ingredients", true), "javascool proglet", 650, 720);</tt></div>
     * </li></ul>
     * @param proglet The corresponding proglet name. Default is "ingredients".
     * @param demo If true runs the demo program. If false runs the proglet pupil's program. Default is "true".
     * @return This, allowing to use the <tt>new ProgletApplet().reset(..)</tt> construct.
     */
    public ProgletApplet reset(String proglet, boolean demo) { this.proglet = proglet; this.demo = demo; return this; } 
    private String proglet = "ingredients"; private boolean demo = true;
    /**/public void init() {
      // Init the parameters from the HTML tags
      try {
	reset(getParameter("proglet"), getParameter("demo").toLowerCase().equals("true"));
      } catch(Exception e) { }
      // Builds the GUI
      JToolBar bar = new JToolBar();
      bar.setRollover(false);
      bar.setFloatable(false);
      bar.add(new JLabel("Proglet \""+proglet+"\" : "));
      bar.add(new JButton(new AbstractAction(demo ? "Démonstration" : "Executer", Utils.getIcon("org/javascool/doc-files/icones16/play.png")) {
	  private static final long serialVersionUID = 1L;
	  public void actionPerformed(ActionEvent e) {
	    if (demo) {
	      run(proglet);
	    } else {
	      run(true);
	    }
	  }}));
      bar.add(new JButton(new AbstractAction("Arrêter", Utils.getIcon("org/javascool/doc-files/icones16/Stop_16x16.png")) {
	  private static final long serialVersionUID = 1L;
	  public void actionPerformed(ActionEvent e) {
	    run(false);
	  }}));
      getContentPane().add(bar, BorderLayout.NORTH);
      getContentPane().add(getPanel(proglet), BorderLayout.CENTER);
    }
  }
}
