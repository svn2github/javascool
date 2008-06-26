package org.unice.javascool.orphy.handlers;

import java.rmi.Naming;
import java.rmi.RemoteException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.unice.javascool.orphyRMI.IOrphy;
import org.unice.javascool.orphyRMI.OrphyRegister;
import org.unice.javascool.util.rmi.JavaScoolServerRegister;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class ReinitialiserOrphy extends AbstractHandler {
	/**
	 * The constructor.
	 */
	public ReinitialiserOrphy() {
	}

	/**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		IOrphy orphy = null;
		final String string = JavaScoolServerRegister.getProtocol() + OrphyRegister.getServer();
		try {
			orphy = (IOrphy)Naming.lookup(string);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		try {
			orphy.close("Code");
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("orphy a ete reinitialise");
		
		return null;
	}

}
