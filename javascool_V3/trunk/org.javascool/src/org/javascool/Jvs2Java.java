/*******************************************************************************
* Thierry.Vieville@sophia.inria.fr, Copyright (C) 2009.  All rights reserved. *
*******************************************************************************/

package org.javascool;

// used for the translation
import java.io.File;

// Used to register the proglets
import javax.swing.JPanel;
import java.util.HashMap;

// Used to interface with the j5/j6 compiler
import java.io.StringWriter;
import java.io.PrintWriter;

// Used to load class
import java.net.URL;
import java.net.URLClassLoader;

/** This factory defines how a Jvs code is translated into a Java code and compiled.
 * The goal of the Jvs syntax is to ease the syntax when starting to program in an imperative language, like Java.
 * <p>- This factory calls the java compiler as in the jdk5 (and earlier) case, but still works with jdk6. It is designed to be used in standalone mode.</p>
 * @see <a href="Jvs2Java.java.html">source code</a>
 * @serial exclude
 */
public class Jvs2Java {
  private Jvs2Java() {}

  /** Tests if a word is reserved to use because it is a Java reserved word.
   * @param word The word to test.
   * @return true if it reserved, else false.
   */
  public static boolean isReserved(String word) {
    for(int i = 0; i < Reserved.length; i++)
      if(word.equals(Reserved[i]))
        return true;
    return false;
  }
  /** The list of all Java reserved words. */
  public static final String Reserved[] = {
    "abstract", "assert", "boolean", "break", "byte", "case", "catch", "char", "class", "const", "continue", "default", "double", "do",
    "else", "enum", "extends", "false", "final", "finally", "float", "for", "goto", "if", "implements", "import", "instanceof", "int", "interface",
    "long", "native", "new", "null", "package", "private", "protected", "public", "return", "short", "static", "strictfp", "super", "switch", "synchronized",
    "this", "throw", "throws", "transient", "true", "try", "void", "volatile", "while"
  };
  /** The list of all JavaScool declared words. */
  public static final String Declared[] = {
    "echo", "equal", "sqrt", "pow", "random", "now", "sleep", "check",
    "clear", "println", "readString", "readInt", "readInteger", "readDouble", "readFloat", "readBoolean",
    "dichoLength", "dichoCompare",
    "smileyReset", "smileyLoad", "smileySet", "smileyGet",
    "scopeReset", "scopeSet", "scopeAdd", "scopeAddString", "scopeAddLine", "scopeAddRectangle", "scopeAddCircle", "scopeX", "scopeY",
    "convaOut", "convaCompare",
    "synthePlay", "syntheSet", "@tone",
    "String", "main"
  };

  /** Reformats a piece of Java source code.
   * <p>- This is a restrictive mechanism dedicated to the JavaScool sub-language of Java.</p>
   * @param text The text to reformat.
   * @return The reformated text.
   */
  public static String reformat(String text) {
    char f[] = text.trim().toCharArray();
    String g = "", ln = "\n";
    int par = 0;
    for(int i = 0, j; i < f.length;) {
      // Escapes /* comments
      if((f[i] == '/') && (i < f.length - 1) && (f[i + 1] == '*')) {
        g += f[i++];
        while(i < f.length && !(f[i - 1] == '*' && f[i] == '/'))
          g += f[i++];
        if(i < f.length)
          g += f[i++] + ln;
        // Escapes // comments
      } else if((f[i] == '/') && (i < f.length - 1) && (f[i + 1] == '/')) {
        while(i < f.length && f[i] != '\n')
          g += f[i++];
        g += ln;
        i++;
        // Escapes " chars
      } else if(f[i] == '"') {
        g += f[i++];
        while(i < f.length && (f[i - 1] == '\\' || f[i] != '"') && f[i] != '\n')
          g += f[i++];
        if(i < f.length)
          g += f[i++];
        // Escapes @ pragma
      } else if(f[i] == '@') {
        while(i < f.length && f[i] != '\n')
          g += f[i++];
        g += ln;
        i++;
        // Normalizes spaces
      } else if(Character.isWhitespace(f[i])) {
        g += ' ';
        i++;
        while(i < f.length && Character.isWhitespace(f[i]))
          i++;
      } else {
        char c0 = g.length() == 0 ? ' ' : g.charAt(g.length() - 1);
        // Counts (parenthesies)
        if(f[i] == '(')
          par++;
        if(f[i] == ')')
          par--;
        // Normalize spaces around operators
        if(isOperator(f[i])) {
          if(!(Character.isWhitespace(c0) || isOperator(c0)))
            g += ' ';
          g += f[i];
          if((i < f.length - 1) && !(Character.isWhitespace(f[i + 1]) || isOperator(f[i + 1])))
            g += ' ';
        } else if(f[i] == '.') {
          if((g.length() > 0) && Character.isWhitespace(c0))
            g = g.substring(0, g.length() - 1);
          g += f[i];
          while(i < f.length - 1 && Character.isWhitespace(f[i + 1]))
            i++;
          // Normalize spaces around punctuation
        } else if((f[i] == ',') || (f[i] == ';') || (f[i] == ')')) {
          if((g.length() > 0) && Character.isWhitespace(c0))
            g = g.substring(0, g.length() - 1);
          g += f[i];
          if((par > 0) && (f[i] != ')'))
            if((i < f.length - 1) && !Character.isWhitespace(f[i + 1]))
              g += ' ';
          if((f[i] == ')') && (i < f.length - 1) && (f[i + 1] == '{'))
            g += ' ';
        } else if(f[i] == '(') {
          if((g.length() > 0) && Character.isWhitespace(c0) && (g.length() > 1) && Character.isLetterOrDigit(g.charAt(g.length() - 2)))
            g = g.substring(0, g.length() - 1);
          g += f[i];
          while(i < f.length - 1 && Character.isWhitespace(f[i + 1]))
            i++;
        } else if(f[i] == '}') {
          for(int n = 0; n < 3; n++)
            if((g.length() > 0) && Character.isWhitespace(g.charAt(g.length() - 1)))
              g = g.substring(0, g.length() - 1);
          g += f[i];
        } else
          g += f[i];
        // Reformats {blocks}
        if((f[i] == '{') || (f[i] == '}') || ((f[i] == ';') && (par == 0))) {
          if(f[i] == '{')
            ln += "   ";
          if(f[i] == '}')
            ln = ln.substring(0, ln.length() - 3);
          g += ln;
          if(ln.length() == 1)
            g += "\n";
          i++;
          while(i < f.length && Character.isWhitespace(f[i]))
            i++;
        } else
          i++;
      }
    }
    return "\n" + g.
           replaceAll("\\}\\s*else\\s*(\\{|if)", "} else $1").
           replaceAll("(while|if|for|return)\\s*[^a-z_0-9_]", "$1 ");
  }
  private static boolean isOperator(char c) {
    switch(c) {
    case '+':
    case '-':
    case '*':
    case '/':
    case '%':
    case '&':
    case '|':
    case '^':
    case '=':
    case '!':
    case '<':
    case '>':
    case ':':
      return true;
    default:
      return false;
    }
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
    String text = Utils.loadString(jpath + ".jvs");
    // Here we check and correct a missing "void main()"
    if(!text.replaceAll("[ \n\r\t]+", " ").matches(".*void[ ]+main[ ]*\\([ ]*\\).*")) {
      if(text.replaceAll("[ \n\r\t]+", " ").matches(".*main[ ]*\\([ ]*\\).*")) {
        System.out.println("Attention: il faut mettre \"void\" devant \"main()\" pour que le programme puisque se compiler");
        text = text.replaceFirst("main[ ]*\\([ ]*\\)", "void main()");
      } else {
        System.out.println("Attention: il faut un block \"void main()\" pour que le programme puisque se compiler");
        text = "\nvoid main() {\n" + text + "\n}\n";
      }
    }
    String[] lines = text.split("\n");
    StringBuffer head = new StringBuffer(), body = new StringBuffer();
    // Here is the translation loop
    {
      // Copies the user's code
      for(String line : lines) {
        if(line.matches("^\\s*(import|package)[^;]*;\\s*$")) {
          head.append(line);
          body.append("//" + line + "\n");
          if(line.matches("^\\s*package[^;]*;\\s*$"))
            System.out.println("Attention: on ne peut normallement pas définir de package Java en JavaScool\n le programme risque de ne pas s'exécuter correctement");
        } else
          body.append(translateOnce(line) + "\n");
      }
      // Imports proglet's static methods
      head.append("import static java.lang.Math.*;");
      head.append("import java.util.List;");
      head.append("import java.util.ArrayList;");
      head.append("import java.util.Map;");
      head.append("import java.util.HashMap;");
      head.append("import static org.javascool.Macros.*;");
      if(proglet.length() == 0)
        for(String p : proglets.keySet())
          head.append("import static " + proglets.get(p) + ".*;");
      else {
        head.append("import static " + proglets.get("ingredients") + ".*;");
        if(!"ingredients".equals(proglet))
          head.append("import static " + proglets.get(proglet) + ".*;");
      }
      head.append("import proglet.paintbrush.*;");
      // Declares the proglet's core as a Runnable in the Applet
      head.append("public class " + jclass + " implements Runnable {");
      head.append("  private static final long serialVersionUID = " + (uid++) + "L;");
      head.append("  public void run() { main(); }");
      body.append("}\n");
    }
    Utils.saveString(jpath + ".java", head.toString() + body.toString());
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
    return "    " + line;
  }
  // Counter used to increment the serialVersionUID in order to reload the different versions of the class
  private static int uid = 1;

  // Properly defines the java path and java class from a file name
  private static void setJpathclass(String path) {
    jpath = path.replaceAll("\\.[a-z]+$", "");
    if(!jpath.matches(".*" + File.separator + ".*"))
      jpath = System.getProperty("user.dir") + File.separator + jpath;
    String s = File.separatorChar == '\\' ? "\\\\" : File.separator;
    jclass = jpath.replaceAll(".*" + s + "([^" + s + "]+)$", "$1");
    if(isReserved(jclass) || (Utils.toName(jclass) != jclass)) throw new IllegalArgumentException("Bad Java class name \"" + jclass + "\"");
  }
  private static String jpath, jclass;

  /** Compiles a Java code source.
   * <div>The jdk <tt>tool.jar</tt> must be in the path.</div>
   * @param path The file path to compile.
   * @return An empty string if the compilation succeeds, else the error message's text.
   *
   * @throws RuntimeException if an I/O exception occurs during command execution.
   */
  public static String compile(String path) {
    setJpathclass(path);
    String args[] = { 
      // "-cp", "/home/vthierry/Work/culsci/javascool/javascool_V3/trunk/org.javascool/www/javascool.jar", // jnlp
      jpath + ".java" 
    };
    StringWriter out = new StringWriter();
    try {
      Class.forName("com.sun.tools.javac.Main").getDeclaredMethod("compile", Class.forName("[Ljava.lang.String;"), Class.forName("java.io.PrintWriter")).
      invoke(null, args, new PrintWriter(out));
    } catch(Throwable e) {
      Utils.report(e); throw new RuntimeException("Erreur: impossible de compiler, il y a une erreur d'installation (" + e + "), contacter http://javascool.gforge.inria.fr");
    }
    return error2output(out.toString().trim().replaceAll(path.replaceAll("\\\\", "\\\\\\\\"), new File(path).getName()));
  }
  // Filters the compiler error to return only one error.
  public static String error2output(String out) {
    System.err.println(out);
    int i = out.indexOf("^");
    if(i != -1)
      out = out.substring(0, i + 1);
    out = out.replaceAll(jpath.replaceAll("\\\\", "\\\\\\\\"), jclass);
    return out;
  }
  /** Dynamically loads a Java class to be used during this session.
   * @param path The path to the java class to load. The java class is supposed to belong to the "default" package, i.e. not to belong to a package.
   * @return An instantiation of this Java class. If the object is a runnable, the current runnable is set.
   *
   * @throws RuntimeException if an I/O exception occurs during command execution.
   * @throws IllegalArgumentException If the Java class name is not valid.
   */
  public static Object load(String path) {
    setJpathclass(path);
    try {
      URL[] urls = new URL[] { 
	// new URL("file:/home/vthierry/Work/culsci/javascool/javascool_V3/trunk/org.javascool/www/javascool.jar"), // jnlp
	new URL("file:" + new File(jpath).getParent() + File.separator) 
      };
      Class< ? > j_class = new URLClassLoader(urls).loadClass(jclass);
      Object o = j_class.newInstance();
      if (o instanceof Runnable)
	runnable = (Runnable) o;
      return o;
    } catch(Throwable e) { throw Utils.report(new RuntimeException("Erreur: impossible de charger " + jpath + " / " + jclass + " (" + e + ") \n . . le package est il mal défini ?"));
    }
  }
  /** Registered proglets. */
  static final HashMap<String, String> proglets = new HashMap<String, String>();
  static {
    for(String proglet : org.javascool.proglets.proglets)
      if(proglet.length() > 0)
        proglets.put(proglet.replaceFirst("^proglet\\.([^\\.]+)\\..*$", "$1"), proglet);
  }

  /** Returns the proglet panel.
   * @param proglet The proglet class name.
   * @return The panel corresponding to the proglet, if any, else null;
   */
  public static JPanel getPanel(String proglet) {
    if(Jvs2Java.proglets.containsKey(proglet)) {
      try {
        return (JPanel) Class.forName(Jvs2Java.proglets.get(proglet)).getField("panel").get(null);
      } catch(Exception e) {
        return null;
      }
    } else
      return null;
  }
  /** Runs/Stops the proglet pupil's program.
   * @param start If true starts the proglet pupil's program, if defined. If false stops the proglet pupil's program.
   */
  public static void run(boolean start) {
    if(thread != null) {
      thread.interrupt();
      thread = null;
    }
    if(start) {
      if (runnable != null) {
	(thread = new Thread(new Runnable() {
                             public void run() {
                               try {
                                 runnable.run();
                               } catch(Throwable e) {
                                 if(!"Programme arrêté !".equals(e.getMessage()))
                                   Utils.report(e);
                               }
			       thread = null;
                             }
                           }
                           )).start();
      } else {
	System.err.println("Undefined runnable");
      }
    }
  }
  /** Runs/Stops a proglet demo..
   * @param proglet The proglet name.
   */
  public static void run(String proglet) {
    if(thread != null) {
      thread.interrupt();
      thread = null;
    }
    if(Jvs2Java.proglets.containsKey(proglet)) {
      Jvs2Java.proglet = proglets.get(proglet);
      (thread = new Thread(new Runnable() {
                             public void run() {
                               try {
                                 Class.forName(Jvs2Java.proglet).getDeclaredMethod("test").invoke(null);
                               } catch(Throwable e) {
                                 Utils.report(e);
                               }
			       thread = null;
                             }
	}
                           )).start();
    }
  }
  /** Tests if the proglet run is done. */
  public static boolean isRunning() {
    return thread != null;
  }
  // This is the entry point to run  the proglet pupil's program: do not change directly !
  /**/public static Runnable runnable = null;
  private static String proglet = null;
  private static Thread thread = null;

  /** Translates Jvs files to Java files.
   * @param usage <tt>java org.javascool.Jvs2Java [-reformat] [-compile] input-file</tt>
   * <p><tt>-reformat</tt> : Reformat the Jvs code</p>
   * <p><tt>-compile</tt> : Compile the java classes</p>
   */
  public static void main(String[] usage) {
    if(usage.length > 0) {
      boolean reformat = false, compile = false;
      for(String option : usage) {
        reformat |= "-reformat".equals(option);
        compile |= "-compile".equals(option);
      }
      if(reformat)
        Utils.saveString(usage[usage.length - 1], reformat(Utils.loadString(usage[usage.length - 1])));
      translate(usage[usage.length - 1]);
      if(compile) {
        String out = Jvs2Java.compile(usage[usage.length - 1]);
        System.out.println(out.length() == 0 ? "Compilation ok !" : out);
      }
    }
  }
}
