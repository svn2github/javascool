package org.unice.javascool.actions;


import java.io.File;
import java.io.IOException;
import java.io.PushbackReader;
import java.net.MalformedURLException;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.TextConsole;
import org.unice.javascool.editor.editors.JVSEditor;
import org.unice.javascool.util.rmi.*;


import java.rmi.*;

/**
 * cette classe permet de definir l'action d'execution du code, precedemment compile
 * @author sebastien chalmeton
 *
 */
public class ExecuteButtonAction implements IWorkbenchWindowActionDelegate {

	public final static String ID="org.unice.javascool.editor.execute";
	private IWorkbenchWindow window;


	//@Override
	public void init(IWorkbenchWindow window) {
		this.window = window;
	}

	//@Override
	public void run(IAction action){
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
		clearConsole();
		JVSEditor editor = (JVSEditor) window.getWorkbench().getActiveWorkbenchWindow().
		getActivePage().getActiveEditor();
		String path = editor.getFilePath();
		final String root=path.substring(0,path.lastIndexOf(File.separator));
		final String file=path.substring(path.lastIndexOf(File.separator)+1,path.lastIndexOf("."));
		Thread myThread=new Thread(){
			@Override
			public void run(){
				IJavaScoolServer server;
				try {
					UtilServer.waitForBinding(string);
					server = (IJavaScoolServer)Naming.lookup(string);
					server.run(root,file,null);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (RemoteException e) {
					e.printStackTrace();
				} catch (NotBoundException e) {
					e.printStackTrace();
				}
			}};
			myThread.start();
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
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
	}
}
