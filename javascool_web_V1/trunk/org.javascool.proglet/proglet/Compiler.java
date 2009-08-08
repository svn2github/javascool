package proglet;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

/** This class calls the java compiler. */
public class Compiler {

  /** Compiles a Java code source
   * @param filename The file path to compile
   * @param classPath The path of configuration files
   * @param offest Source line offset for Jvs file
   * @return true if the compilation was successful
   */
  public static boolean compile(String filename, String classPath, int offset) throws IOException {
    JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
    
    if(compiler == null) {
      System.out.println("Aucun JDK disponible: demander de l'aide au responsable informatique.");
      return false;	
    }
		
    DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
    StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);
    Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromStrings(Arrays.asList(filename));
    Iterable<String> options = Arrays.asList("-classpath", classPath);
    
    JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, options, null, compilationUnits);

    boolean success = task.call();
    if(success) {
      // inutile // System.out.println("Compilation ok.");
    } else {
      for (int i = 0; i < diagnostics.getDiagnostics().size(); i++) {
	Diagnostic diag = diagnostics.getDiagnostics().get(i);
	String errorMess  = diag.getMessage(null);
	errorMess = errorMess.substring(errorMess.lastIndexOf(File.separator)+1, errorMess.length());
	long start = diag.getStartPosition();
	long end = diag.getEndPosition() - start;
	if(offset > 0) {
	  long line = diag.getLineNumber();
	  errorMess = errorMess.replace(""+line, ""+(line - offset));
	  errorMess = errorMess.replace(".java", ".jvs");
	}
	System.out.println(errorMess);
      }
    }
    fileManager.close();
    return success;
  }
}


