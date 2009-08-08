package proglet;

import java.io.File;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.FileWriter;

public class Translator {

  /** Translates a Jvs code source.
   * @param filename The file path to translate.
   * @param proglet The static imported proglet name.
   * @return The Java line number offset with respect to the Jvs source. -1 if it fails.
   */
  public static int translate(String filename, String proglet) throws IOException {
    String main = filename.replaceAll(".*/([^/]+)\\.[a-z]+$", "$1"), file = filename.replaceAll("\\.[a-z]+$", "");
    File jvs = new File(file+".jvs"), jav = new File(file+".java");
    if (!jvs.exists()) {
      System.err.println("Le fichier "+file+".jvs n'existe pas!");
      return -1;	
    }     
    try {
      BufferedReader in = new BufferedReader(new FileReader(file+".jvs"));
      PrintWriter out = new PrintWriter(new FileWriter(file+".java"));
      // Here is the translation loop
      final int offset = 2;
      {
	out.println("import static java.lang.Math.*;");
	out.println("import static proglet.Proglet.echo;");
	out.println("import static proglet."+proglet+".*;");
	out.println("public class "+main+ " {");
	for(String line; (line = in.readLine()) != null; ) {
	  out.println(translateOnce(line));
	}
	out.println("}");
      }
      in.close();
      out.close();
      return offset;
    } catch(Exception e) {
      Proglet.report(e);
      return -1;
    }
  }

  // Translates one line of the source file
  private static String translateOnce(String line) {
    // Expands the static public modifiers
    line = line.replaceFirst("main[ \t]*\\(\\)", "static public void main()");
    return line;
  }
}


