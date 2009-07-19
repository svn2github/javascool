package org.javascool.ui.editor;

import org.eclipse.ui.IStartup;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.javascool.ui.editor.actions.NewAction;

public class AutoOpenEditorAtStart implements IStartup {

	public void earlyStartup() {
		final IWorkbench workbench = PlatformUI.getWorkbench();
		workbench.getDisplay().asyncExec(new Runnable() {
			public void run() {
				try{
				  NewAction.action();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
	}

}