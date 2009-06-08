package compilation;
import java.io.File;
import java.io.IOException;
import java.rmi.Naming;
import java.util.Arrays;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import translation.Translator;



/**
 * cette classe contient les methodes necessaire a la definition et a l'utilisation 
 * du bouton compiler
 */
public class Compile {

	
	//@Override
	public static void run(String filePath, String classPath) {
		
		if(filePath.endsWith(".jvs")){	//compilation d'un fichier jvs
			try {
				filePath = filePath.replace(".jvs",".java");
				//first Compilation
				boolean comp = compiler(filePath, classPath, true, false);
				if(comp){
					Translator.SetSignatureCode(filePath);//set signature of methods and fields
						
					//second compilation
					boolean comp2 = compiler(filePath, classPath, true, true);
					if(!comp2)
						System.err.println("Erreur"+ 
								"une erreur inattendue lors de la traduction du code javasccol" +
								" en java a eu lieu. \n\n une modification de votre code est succeptible " +
								"de corriger le probleme.\n\n merci de nous faire parvenir par email à l'adresse suivante : bugs@javascool.com\n\nle code source que vous essayez\n\n" +
								"de compiler pour que l'on puisse corriger le problème.");
					
				}
			} catch (Exception e) {}
		}else{//compilation d'un fichier java
			try {
				compiler(filePath, classPath, false, true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * this method permits to compile a JVS or JAVA code source
	 * @param filename the file path to compile
	 * @param classPath the path of configuration files
	 * @param isJvsFile boolean indicating whether it is a JVS file. <code>true</code> if is a JVS file
	 * @param printDiag boolean indicating if we want to display the result of the compilation
	 * @return true if the compilation was successful
	 */
	public static boolean compiler(String filename, String classPath, boolean isJvsFile, boolean printDiag) throws IOException{
		
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		
		if(compiler==null){
			String ls=System.getProperty("line.separator");
			System.err.println("Aucun JDK detecte."+ls+"Assurez vous d'installer la version 6"+ls+"et de le rendre accessible par la variable PATH");
			return false;	
		}
		
		DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
		StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);
		Iterable<? extends JavaFileObject> compilationUnits = fileManager
		.getJavaFileObjectsFromStrings(Arrays.asList(filename));
		//add the classpath for conf file
		Iterable<String> options=Arrays.asList("-classpath",classPath);
	
		JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, options, null, compilationUnits);
		
		boolean success = task.call();//compilation
		
		//print compilation errors
		if(printDiag)
			printDiagnostic(diagnostics, success, isJvsFile);

		fileManager.close();
		return success;
	}

	
	
	/**
	 * this method print the diagnostics of compilation
	 * @param diagnostics the collection of diagnostics
	 * @param success the succes of compilation <code>true</code> if the compilation succeeds
	 * @param isJvsFile boolean indicating if the file is to compile a "JVS" or "JAVA" file. <code>true</code> if file is type of "JVS"
	 * 
	 **/
	private static void printDiagnostic(DiagnosticCollector<JavaFileObject> diagnostics, boolean success, boolean isJvsFile){
		if(success)
			System.out.println("Compilation reussie avec succes");

		else{
			for (int i=0; i< diagnostics.getDiagnostics().size(); i++){
				Diagnostic diag = diagnostics.getDiagnostics().get(i);
				
				String errorMess  = diag.getMessage(null);
				
				errorMess = errorMess.substring(errorMess.lastIndexOf(File.separator)+1,errorMess.length());
				//la ou il y a un erreur
				long start = diag.getStartPosition();
				long end = diag.getEndPosition() - start;
			
				if(isJvsFile){
					long line = diag.getLineNumber();
					errorMess = errorMess.replace(""+line, ""+(line - Translator.nbLigne));
					errorMess = errorMess.replace(".java", ".jvs");
				}
				//surlignage de l'erreur dans l'editeur
			//	marqueError((int)start, Math.max(1,(int)end));
			
				//AFFICHAGE DES ERREURS
				System.err.println(errorMess);
				
			}
		}
	}

}


