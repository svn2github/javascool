package org.javascool.ui.editor.actions;


import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

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
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.TextConsole;
import org.eclipse.ui.handlers.HandlerUtil;
import org.javascool.compilation.Compile;
import org.javascool.translation.Translator;
import org.javascool.ui.editor.Activator;
import org.javascool.ui.editor.editors.JVSEditor;

public class CompilCodeAction implements IWorkbenchWindowActionDelegate {

	public static final String ID = "org.javascool.ui.editor.actions.compilCodeAction";

	private IWorkbenchWindow window;



	@Override
	public void init(IWorkbenchWindow window) {
		this.window = window;

	}

	@Override
	public void run(IAction action) {

		//verify no execution
		if(ExecuteCodeAction.jobStarted){
			MessageDialog.openWarning(window.getShell(), "Compilation", 
			"Impossible de compiler le fichier, un programme est en cours d'execution");
			return;
		}
		clearConsole();//on efface le contenu de la console
		//l'editeur associe  la vue
		JVSEditor editor = (JVSEditor) window.getWorkbench().getActiveWorkbenchWindow().
		getActivePage().getActiveEditor();
		editor.doSave(null);//sauvegarde de l'editeur courant

		Path res = null;
		Path resProglet = null;

		try{
			//here we indicate the position of plugin javascool.core an proglet

			URL url = Platform.getBundle(org.javascool.core.Activator.PLUGIN_ID).getEntry("/");
			url = FileLocator.resolve(url);
			res = new Path(url.getPath());

			URL urlProglet = Platform.getBundle(activator.Activator.PLUGIN_ID).getEntry("/");
			urlProglet = FileLocator.resolve(urlProglet);
			resProglet = new Path(urlProglet.getPath());

		}catch(Exception ex){
			System.err.println("org.javascool.core.CompileCodeAction -> create classPath");
			ex.printStackTrace();
		}
		String classPath = res.toOSString()+"bin";
		classPath+=";"+resProglet.toOSString()+"bin";

		String path = editor.getFilePath();
		//TODO simple trace it's necessary to comment this line
		//System.out.println("compilation du fichier : "+path);

		//translate just JVS file
		if(path.endsWith(".jvs"))
			Translator.translate(path);

		//TODO simple trace it's necessary to comment this line
		//System.out.println("classPath = "+classPath);
		Compile.run(path, classPath);

		
		/*
		Job jobCompil = new Job("Compilation"){

			@Override
			protected IStatus run(IProgressMonitor monitor) {
				monitor.beginTask("Compilation", 100);
				boolean workFinished = false;
				int count = 0;
				while(!workFinished){
					count ++;
					System.out.println("1");
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println("2");
					if (monitor.isCanceled()) return Status.CANCEL_STATUS;
					System.out.println("<b>count</b>");
					if(count == 10) workFinished = true;
				}
				return Status.OK_STATUS;
			}
		};
		
		jobCompil.addJobChangeListener(new JobChangeAdapter() {
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
		//  jobCompil.setSystem(true);

		jobCompil.schedule();
		*/
	}


	/**
	 * this method clear the default console of the application/
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
