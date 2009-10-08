package proglet;

import java.io.IOException;
import java.io.File;
import java.io.StringWriter;
import java.io.PrintWriter;

/** This factory calls the java compiler in the jdk5 (and earlier) case. 
 * It is used in standalone mode. 
 * @see <a href="Compiler.java">source code</a>
 */
public class Compiler { private Compiler() { }

  /** Compiles a Java code source
   * @param filename The file path to compile
   * @return An empty string if the compilation succeeds, else the error message's text.
   */
  static String compile(String filename) throws IOException {
    String args[] = { filename };
    StringWriter out = new StringWriter();
    com.sun.tools.javac.Main.compile(args, new PrintWriter(out));
    return out.toString().trim().replaceAll(filename.replaceAll("\\\\", "\\\\\\\\"), new File(filename).getName());
  }
}


