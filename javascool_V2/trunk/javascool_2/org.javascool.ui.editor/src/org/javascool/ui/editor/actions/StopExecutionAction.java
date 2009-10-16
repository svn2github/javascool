package org.javascool.ui.editor.actions;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceNode;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;
import org.javascool.compilation.Execution;

public class StopExecutionAction implements IWorkbenchWindowActionDelegate {

	public static final String ID = "org.javascool.ui.editor.actions.stopAction";
	
	private IWorkbenchWindow window;
	
	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(IWorkbenchWindow window) {
		this.window = window;
	}

	@Override
	public void run(IAction action) {
		
		
		//TODO version JOB
		//check the execution started
		if(!ExecuteCodeAction.jobStarted){
			MessageDialog.openWarning(window.getShell(), "Stop Execution", "Aucun programme en cours d'execution");
			return;
		}
		try{
			ExecuteCodeAction.job.cancel();
			System.err.println("arret de l'execution du programme");		
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		//TODO version thread
		/*
		if(ExecuteCodeAction.threadExecute.isAlive()){
			ExecuteCodeAction.threadExecute.stop();
			ExecuteCodeAction.stopThread = true;
			System.err.println("arret de l'execution du programme");
		}else{
			MessageDialog.openWarning(window.getShell(), "Stop Execution", "Aucun programme en cours d'execution");
		}*/
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		// TODO Auto-generated method stub

	}

}
