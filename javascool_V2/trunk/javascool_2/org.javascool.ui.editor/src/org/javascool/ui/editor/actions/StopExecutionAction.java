/*
 * Copyright (c) 2008-2010 Javascool (Java's Cool).  All rights reserved.
 *	this source file is placed under license CeCILL
 * see http://www.cecill.info/licences/Licence_CeCILL_V2-fr.html
 * or http://www.cecill.info/licences/Licence_CeCILL_V2-en.html
 */
package org.javascool.ui.editor.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

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
