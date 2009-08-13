package proglet;

import java.io.File;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.FileWriter;

import java.util.regex.Pattern;

public class Translator {
  static int uid = 1;

  /** Translates a Jvs code source.
   * @param filename The file path to translate.
   * @param proglet The static imported proglet name.
   */
  public static void translate(String filename, String proglet) throws IOException {
    String main = filename.replaceAll(".*/([^/]+)\\.[a-z]+$", "$1"), file = filename.replaceAll("\\.[a-z]+$", "");
    File jvs = new File(file+".jvs"), jav = new File(file+".java");
    if (!jvs.exists()) throw new IOException("File not found: "+jvs);
    BufferedReader in = new BufferedReader(new FileReader(file+".jvs"));
    PrintWriter out = new PrintWriter(new FileWriter(file+".java"));
    // Here is the translation loop
    {
      out.print("import static proglet.Macros.*;");
      out.print("import static java.lang.Math.*;");
      out.print("import static proglet."+proglet+".*;");
      out.print("public class "+main+ " extends proglet.InterfacePrincipale {");
      out.print("  private static final long serialVersionUID = "+ (uid++) + "L;");
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
    // Adds the void to main
    line = line.replaceFirst("main[ \t]*\\(\\)", "public void main()");
    return line;
  }
}


