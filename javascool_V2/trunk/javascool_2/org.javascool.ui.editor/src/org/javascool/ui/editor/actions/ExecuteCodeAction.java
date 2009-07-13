package org.javascool.ui.editor.actions;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.TextConsole;
import org.javascool.ui.editor.editors.JVSEditor;

public class ExecuteCodeAction implements IWorkbenchWindowActionDelegate {

	public static final String ID = "org.javascool.ui.editor.actions.executeCodeAction";

	private IWorkbenchWindow window;
	static Job job;


	@Override
	public void init(IWorkbenchWindow window) {
		this.window = window;

	}

	@Override
	public void run(IAction action) {


		clearConsole();
		JVSEditor editor = (JVSEditor) window.getWorkbench().getActiveWorkbenchWindow().
		getActivePage().getActiveEditor();
		String path = editor.getFilePath();

		final String root=path.substring(0,path.lastIndexOf(File.separator));
		final String file=path.substring(path.lastIndexOf(File.separator)+1,path.lastIndexOf("."));

		execute(root,file);
		/*
		String path = null;
		try{
			JVSEditor editor = (JVSEditor) window.getWorkbench().getActiveWorkbenchWindow().
			getActivePage().getActiveEditor();
			path = editor.getFilePath();
		}catch(Exception e){
			//e.printStackTrace();
		}

		clearConsole();


		job = new Job("execution") {
			protected IStatus run(IProgressMonitor monitor) {

				// System.out.println("execution du ficier : "+path);
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("execution");
				return Status.OK_STATUS;
			}
		};

		job.setPriority(Job.BUILD);
		job.schedule(); // start as soon as possible

		 */

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


	//emploi temporaire aide au support pour la culture scientifique

	/**
	 * Execute a file class.
	 * 
	 * @param	root	the path of the file.
	 * @file 	file	the name of the file.
	 */
	public void execute(String root,final String file){
		try{
			URL first_url=new File(root).toURI().toURL();
			ArrayList<URL> arrayListRes=new ArrayList<URL>();
			arrayListRes.add(first_url);
			String jvm_path = System.getProperty("java.class.path");
			for(String s:jvm_path.split(File.pathSeparator)) arrayListRes.add(new File(s).toURI().toURL());
			URL[] res=new URL[arrayListRes.size()];
			for(int i=0;i<arrayListRes.size();i++) res[i]=arrayListRes.get(i);

			URL[] url = res;

			URLClassLoader cl = new URLClassLoader(url,null) ;
			Class myClass = Class.forName(file,false, cl);
			final Method myMethod=myClass.getMethod("main",String[].class);
			final Object[] args0=new String[1];
			try {
				myMethod.invoke(null,(Object[])args0);
			}catch (Exception e) {
				System.err.println("Une erreur s'est produite pendant l'execution du programme");
			}

		}catch (Exception e) {
			e.printStackTrace();
		}
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
