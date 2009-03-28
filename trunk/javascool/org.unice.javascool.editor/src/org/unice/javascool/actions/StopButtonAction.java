package org.unice.javascool.actions;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PushbackReader;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.internal.console.ConsoleManager;
import org.unice.javascool.util.rmi.IJavaScoolServer;
import org.unice.javascool.util.rmi.JavaScoolServerRegister;
import org.unice.javascool.util.rmi.UtilServer;


public class StopButtonAction implements IWorkbenchWindowActionDelegate {

	@SuppressWarnings("unused")
	private IWorkbenchWindow window;
	
	//@Override
	public void dispose() {
	}

	//@Override
	public void init(IWorkbenchWindow window) {
		this.window = window;
	}

	//@Override
	public void run(IAction action) {
		IJavaScoolServer server;
		try {
			String string=JavaScoolServerRegister.getProtocol()+JavaScoolServerRegister.getServer();
			server=(IJavaScoolServer)Naming.lookup(string);
			if(!server.isRunning()){
				MessageBox mb = new MessageBox(new Shell());
				mb.setText("Alert");
				mb.setMessage("Aucun programme en cours d'execution");
				mb.open();
				return;			
			}else{
					UtilServer.closeRessources();
				
				try{
					server.stop();
				}catch(Exception x){}
				Thread attachServer=new Thread(new UtilServer());
				attachServer.start();
				System.err.println("\nprogramme stoppe\n");
			}
		} catch (RemoteException e) {
			Thread attachServer=new Thread(new UtilServer());
			attachServer.start();
		} catch (MalformedURLException e) {
			Thread attachServer=new Thread(new UtilServer());
			attachServer.start();
		} catch (NotBoundException e) {
			MessageBox mb = new MessageBox(new Shell());
			mb.setText("Alert");
			mb.setMessage("Aucun programme en cours d'execution");
			mb.open();
			Thread attachServer=new Thread(new UtilServer());
			attachServer.start();
			return;	
		}
	}


//	@Override
	public void selectionChanged(IAction action, ISelection selection) {
	}

}
