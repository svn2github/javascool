package proglet;

import java.io.IOException;
import java.io.File;
import java.io.StringWriter;
import java.io.PrintWriter;

/** This class calls the java compiler in the jdk5 (and earlier) case. */
public class Compiler {

  /** Compiles a Java code source
   * @param filename The file path to compile
   * @return An empty string if the compilation succeeds, else the error message's text.
   */
  public static String compile(String filename) throws IOException {
    String args[] = { filename };
    StringWriter out = new StringWriter();
    com.sun.tools.javac.Main.compile(args, new PrintWriter(out));
    return out.toString().trim().replaceAll(filename, new File(filename).getName());
  }
}


