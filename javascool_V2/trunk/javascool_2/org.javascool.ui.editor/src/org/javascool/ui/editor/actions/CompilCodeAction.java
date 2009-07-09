package org.javascool.ui.editor.actions;


import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.TextConsole;
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
		clearConsole();
		System.out.println("compilation");
	}

	
	/**
	 * this method clear the default console of the application/
	 */
	private void clearConsole(){
		ConsolePlugin tmp = org.eclipse.ui.console.ConsolePlugin.getDefault();
		IConsole[] b = tmp.getConsoleManager().getConsoles();

		TextConsole console = (TextConsole) b[0];
		//TODO console.getDocument().set("fonction a completer");
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
