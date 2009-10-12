package org.javascool.ui.editor.actions;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.TextConsole;
import org.javascool.compilation.Execution;
import org.javascool.ui.editor.editors.JVSEditor;

public class ExecuteCodeAction implements IWorkbenchWindowActionDelegate {

	public static final String ID = "org.javascool.ui.editor.actions.executeCodeAction";

	private IWorkbenchWindow window;
	static Job job;
	static boolean jobStarted = false; 
	static Thread threadExecute  = new Thread();

	static boolean stopThread = false;

	@Override
	public void init(IWorkbenchWindow window) {
		this.window = window;

	}

	@Override
	public void run(IAction action) {

		//check the unique execution
		if(jobStarted){
			MessageDialog.openWarning(window.getShell(), "Execution", 
			"Une programme est deja en cours d'execution");
			return;
		}
		


		JVSEditor editor = (JVSEditor) window.getWorkbench().getActiveWorkbenchWindow().
		getActivePage().getActiveEditor();
		String path = editor.getFilePath();

		//the path of the file
		final String pathFile = path.substring(0,path.lastIndexOf(File.separator));
		//the name of the file without extension
		String className = path.substring(path.lastIndexOf(File.separator)+1,path.lastIndexOf("."));

		Path classPathPluginMacro = null;
		Path classPathpluginProglet = null;
		try{
			//here we indicate the position of plugin javascool.core an proglet

			URL url = Platform.getBundle(org.javascool.core.Activator.PLUGIN_ID).getEntry("/");
			url = FileLocator.resolve(url);
			classPathPluginMacro = new Path(url.getPath());

			URL urlProglet = Platform.getBundle(activator.Activator.PLUGIN_ID).getEntry("/");
			urlProglet = FileLocator.resolve(urlProglet);
			classPathpluginProglet = new Path(urlProglet.getPath());

		}catch(Exception ex){
			System.err.println("org.javascool.core.execution -> create classPath from plugin");

			ex.printStackTrace();
		}
		String[] classPath = {classPathPluginMacro.toOSString()+File.separator+"bin",
				classPathpluginProglet.toOSString()+File.separator+"bin"};  

		//clearConsole(); //clear the output
		//Execution.execute(pathFile, className, classPath);	//execute the code

		final String classNameFinal = className;
		final String[] classPathFinal = Arrays.copyOf(classPath, classPath.length);

		/*
		threadExecute = new Thread(){
			@Override
			public void run(){
					Execution.execute(pathFile, classNameFinal, classPathFinal);	//execute the code
			}

	//		public synchronized void stop()
		};
		 */

		clearConsole(); //clear the output

		//threadExecute.start();


		job = new Job("Execution"){
			protected IStatus run(IProgressMonitor monitor) {
				jobStarted = true;
				monitor.beginTask("Execution", 100);

				Execution.execute(pathFile, classNameFinal, classPathFinal);	//execute the code
				jobStarted = false;
				return Status.OK_STATUS;
			}
		};


		job.addJobChangeListener(new JobChangeAdapter() {

			@Override
			public void done(IJobChangeEvent event) {

				
				if (event.getResult().isOK()) {
					MessageDialog.openInformation(window.getShell(), "Arret de l'execution",
					"Execution terminée avec succés");
				}else{
					MessageDialog.openWarning(window.getShell(), "Arret de l'execution",
					"Execution du programme interrompue");
				}
			}
		});


		//job.shouldRun()
		//int state = job.getState();
		//if(state == job.RUNNING || state == job.WAITING || state == job.SLEEPING ){
		
		job.schedule(); // start as soon as possible	
	}


	/**
	 * this method clear the default console of the application.
	 */
	private void clearConsole(){
		ConsolePlugin tmp = org.eclipse.ui.console.ConsolePlugin.getDefault();
		IConsole[] b = tmp.getConsoleManager().getConsoles();

		TextConsole console = (TextConsole) b[0];
		console.getDocument().set("");
	}



	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}


	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		// TODO Auto-generated method stub

	}

}
