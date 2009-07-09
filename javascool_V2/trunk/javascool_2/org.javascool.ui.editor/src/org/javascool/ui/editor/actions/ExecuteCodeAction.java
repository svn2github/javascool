package org.javascool.ui.editor.actions;

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


				
	}


	/**
	 * this method clear the default console of the application
	 */
	private void clearConsole(){
		ConsolePlugin tmp = org.eclipse.ui.console.ConsolePlugin.getDefault();
		IConsole[] b = tmp.getConsoleManager().getConsoles();

		TextConsole console = (TextConsole) b[0];
		console.clearConsole();
		//console.getDocument().set("");
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
