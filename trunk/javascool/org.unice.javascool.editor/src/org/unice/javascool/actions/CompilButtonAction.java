package org.unice.javascool.actions;

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
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.TextConsole;
import org.unice.javascool.editor.editors.JVSEditor;
import org.unice.javascool.traducteurJVS.Translator;
import org.unice.javascool.util.rmi.IJavaScoolServer;
import org.unice.javascool.util.rmi.JavaScoolServerRegister;
import org.unice.javascool.util.rmi.UtilServer;

/**
 * cette classe contient les methodes necessaire a la definition et a l'utilisation 
 * du bouton compiler
 */
public class CompilButtonAction implements IWorkbenchWindowActionDelegate {

	public static final String ID = "org.javascool.ui.editor.actions.compilAction";

	@SuppressWarnings("unused")
	private IWorkbenchWindow window;

	//@Override
	public void dispose(){}

	//@Override
	public void init(IWorkbenchWindow window) {
		this.window = window;
	}

	//@Override
	public void run(IAction action) {
		final String string=JavaScoolServerRegister.getProtocol()+JavaScoolServerRegister.getServer();
		IJavaScoolServer server;
		try {
			UtilServer.waitForBinding(string);
			server = (IJavaScoolServer)Naming.lookup(string);
			if(server.isRunning()){
				MessageBox mb = new MessageBox(new Shell());
				mb.setText("Alert");
				mb.setMessage("Un programme est deja en cours d'execution");
				mb.open();
				return;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		clearConsole();//on efface le contenu de la console
		//l'editeur associÃ© Ã  la vue
		JVSEditor editor = (JVSEditor) window.getWorkbench().getActiveWorkbenchWindow().
		getActivePage().getActiveEditor();
		editor.doSave(null);//sauvegarde de l'editeur courant


		String path = editor.getFilePath();
		if(path.endsWith(".jvs")){	//compilation d'un fichier jvs
			try {
				Translator.translate(path);
				path = path.replace(".jvs",".java");
				boolean comp = compiler(path,true, true);
				if(comp){
					Translator.SetSignatureCode(path);
					boolean comp2 = compiler(path,true, false);
					if(!comp2)
						MessageDialog.openError(window.getShell(), "Erreur", 
								"une erreur inattendue lors de la traduction du code javasccol" +
								" en java a eu lieu. \n\n une modification de votre code est succeptible " +
								"de corriger le probleme.\n\n merci de nous faire parvenir par email à l'adresse suivante : bugs@javascool.com\n\nle code source que vous essayez\n\n" +
								"de compiler pour que l'on puisse corriger le problème.");
				}
			} catch (Exception e) {}
		}else{//compilation d'un fichier java
			try {
				compiler(path, false, true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * cette methode permet de compiler un fichier java ou javascool
	 * @param filename le chemin du fichier a compiler
	 * @param isJvsFile booleen a mettre a true si le fichier a compiler est un fichier javascool
	 * @param printDiag boolean a mettre a true si on souhaite afficher les erreurs de compilation
	 * @return true si la compilation s'est effectue avec succes
	 * @throws IOException
	 */
	public static boolean compiler(String filename, boolean isJvsFile, boolean printDiag) throws IOException{
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		if(compiler==null){
			String ls=System.getProperty("line.separator");
			MessageBox mb = new MessageBox(new Shell());
			mb.setText("Alert");
			mb.setMessage("Aucun JDK detecte."+ls+"Assurez vous d'installer la version 6"+ls+"et de le rendre accessible par la variable PATH");
			mb.open();
			return false;	
		}
		DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
		StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);
		Iterable<? extends JavaFileObject> compilationUnits = fileManager
		.getJavaFileObjectsFromStrings(Arrays.asList(filename));
		String rootDir=UtilServer.getPluginFolder(org.unice.javascool.editor.Activator.PLUGIN_ID);
		String editDir=rootDir+"bin";
		String serverSide=rootDir+"ServerSide";
		Iterable<String> options=Arrays.asList("-classpath",editDir+File.pathSeparator+serverSide);
	
		JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics,options,null, compilationUnits);
		boolean success = task.call();
		//affichage des erreurs
		if(printDiag)
			printDiagnostic(diagnostics, success, isJvsFile);

		fileManager.close();
		return success;
	}

	/**
	 * this method print the diagnostics of compilation
	 * @param diagnostics the collection of diagnostics
	 * @param success the succes of compilation true if no error
	 * @param success the isJvsFile true if a javscool file
	 * 
	 */
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

	
	/**
	 * cette methode permet de surligner l'erreur dans le fichier source
	 * @param offset l'offset du debut de l'erreur
	 * @param length l'offset de la fin de l'erreur
	 */
	void marqueError(int offset, int length){
		JVSEditor editor = (JVSEditor) window.getWorkbench().getActiveWorkbenchWindow().
		getActivePage().getActiveEditor();
		editor.getSourceViewer2().setSelectedRange(offset, length);
	}

	
	/**
	 * this method clear the default console of the application
	 */
	private void clearConsole(){
		ConsolePlugin tmp = org.eclipse.ui.console.ConsolePlugin.getDefault();
		IConsole[] b = tmp.getConsoleManager().getConsoles();

		TextConsole console = (TextConsole) b[0];
		console.getDocument().set("");
	}

	//@Override
	public void selectionChanged(IAction action, ISelection selection) {}

}


