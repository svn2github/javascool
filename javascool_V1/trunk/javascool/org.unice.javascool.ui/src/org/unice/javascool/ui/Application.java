package org.unice.javascool.ui;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.unice.javascool.conf.BeanFactory;
import org.unice.javascool.ui.unique.UniqueInstance;

/**
 * This class controls all aspects of the application's execution
 */
public class Application implements IApplication {

	private Display display=null ;

	final int PORT = 32164;
	final UniqueInstance uniqueInstance=new UniqueInstance(PORT);
	final Runnable hook=new Runnable(){
		@Override public void run(){
			uniqueInstance.stop();
		}
	};


	public Object start(IApplicationContext context) { 
		if(uniqueInstance.launch()){
			Runtime.getRuntime().addShutdownHook(new Thread(hook));
			BeanFactory.init();
			display = PlatformUI.createDisplay();
			try {
				int returnCode = PlatformUI.createAndRunWorkbench(display, new ApplicationWorkbenchAdvisor());
				if (returnCode == PlatformUI.RETURN_RESTART) {
					return IApplication.EXIT_RESTART;
				}
				return IApplication.EXIT_OK;
			} finally {
				display.dispose();
			}
		}else{
			Display dis=PlatformUI.createDisplay();
			MessageBox mb = new MessageBox(new Shell());
			mb.setText("Alert");
			mb.setMessage("JavasCool est deja en cours d'execution");
			mb.open();
			dis.dispose();
			return IApplication.EXIT_OK;
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.equinox.app.IApplication#stop()
	 */
	public void stop() {
		final IWorkbench workbench = PlatformUI.getWorkbench();
		if (workbench == null)
			return;
		final Display display = workbench.getDisplay();
		display.syncExec(new Runnable() {
			public void run() {
				if (!display.isDisposed())
					workbench.close();
			}
		});
	}
}
