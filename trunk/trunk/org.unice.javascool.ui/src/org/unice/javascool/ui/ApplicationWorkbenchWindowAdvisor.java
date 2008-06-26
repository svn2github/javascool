package org.unice.javascool.ui;

import org.eclipse.jface.preference.IPreferenceNode;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;


public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

	public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
		super(configurer);
	}

	public ActionBarAdvisor createActionBarAdvisor(
			IActionBarConfigurer configurer) {
		return new ApplicationActionBarAdvisor(configurer);
	}

	public void preWindowOpen() {
		IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
		configurer.setInitialSize(new Point(1000, 800));
		configurer.setShowCoolBar(true);
		configurer.setShowStatusLine(true);
		configurer.setTitle("Java's Cool");
		IWorkbenchWindow window = configurer.getWindow();
		PreferenceManager pm = window.getWorkbench( ).getPreferenceManager( );
		pm.remove( "org.eclipse.help.ui.browsersPreferencePage" );

	}


	public void postWindowOpen() {
		super.postWindowOpen();
		IWorkbenchWindow window = getWindowConfigurer().getWindow();
		removeUnusedPref(window);
	}


	private void removeUnusedPref(IWorkbenchWindow window) {
		PreferenceManager pm = window.getWorkbench().getPreferenceManager( );
		IPreferenceNode[] node = pm.getRootSubNodes();
		for(int i=0; i< node.length; i++){
			if(!node[i].getId().equals("org.unice.javascool." +
					"editor.prefrerences.ColorPreferencePage")){
				pm.remove(node[i].getId());
			}
		}
	}
	
}
