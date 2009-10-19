/*******************************************************************************
 * Thierry.Vieville@sophia.inria.fr, Copyright (C) 2009.  All rights reserved. *
 *******************************************************************************/

package proglet;

import java.io.File;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.FileWriter;

import java.util.regex.Pattern;

/** This factory defines how a Jvs code is translated into a Java code. 
 * The goal is to ease the syntax when starting to program in an imperative language, like Java. 
 * @see <a href="Translator.java">source code</a>
 */
public class Translator { private Translator() { }

  // Counter used to increment the serialVersionUID in order to reload the different versions of the class
  static int uid = 1;

  /** Translates a Jvs code source.
   * @param filename The file path to translate.
   * @param proglet The static imported proglet name.
   */
  static void translate(String filename, String proglet) throws IOException {
    String s = File.separatorChar == '\\' ? "\\\\" : File.separator, 
      main = filename.replaceAll(".*"+s+"([^"+s+"]+)\\.[a-z]+$", "$1"), file = filename.replaceAll("\\.[a-z]+$", "");
    File jvs = new File(file+".jvs"), jav = new File(file+".java");
    if (!jvs.exists()) throw new IOException("File not found: "+jvs);
    BufferedReader in = new BufferedReader(new FileReader(file+".jvs"));
    PrintWriter out = new PrintWriter(new FileWriter(file+".java"));
    // Here is the translation loop
    {
      // Imports proglet's static methods
      out.print("import static proglet.Macros.*;");
      if (!proglet.equals("Konsol"))
	out.print("import static proglet.Konsol.*;");
      out.print("import static proglet."+proglet+".*;");
      // Declares the proglet's core as a Runnable in the Applet
      out.print("public class "+main+ " extends proglet.InterfacePrincipale {");
      out.print("  private static final long serialVersionUID = "+ (uid++) + "L;");
      out.print("  { runnable = new ProgletRunnableInterfacePrincipale(); }");
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
    // Translates the Synthe proglet TONE macro
    line = line.replaceFirst("TONE:(.*)", "proglet.Synthe.tone = new proglet.SoundBit() { public double get(char c, double t) { return $1; } }; proglet.Synthe.set(\"16 a\");");
    return "    "+line;
  }
}


