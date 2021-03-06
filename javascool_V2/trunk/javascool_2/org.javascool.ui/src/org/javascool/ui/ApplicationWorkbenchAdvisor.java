/*
 * Copyright (c) 2008-2010 Javascool (Java's Cool).  All rights reserved.
 *	this source file is placed under license CeCILL
 * see http://www.cecill.info/licences/Licence_CeCILL_V2-fr.html
 * or http://www.cecill.info/licences/Licence_CeCILL_V2-en.html
 */
package org.javascool.ui;

import org.eclipse.jface.preference.IPreferenceNode;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.ui.IWorkbenchPreferenceConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {

	
	public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(
			IWorkbenchWindowConfigurer configurer) {
		return new ApplicationWorkbenchWindowAdvisor(configurer);
	}

	public String getInitialWindowPerspectiveId() {
		return org.javascool.ui.editor.Perspective.ID;
	}
	
	/**
	 * method overrided for remove preference pages unused in the application
	 * and change the style of the application
	 */
	@Override
	public void initialize(IWorkbenchConfigurer configurer) {
		
		//for change style of onglets
		PlatformUI.getPreferenceStore().setValue(
				IWorkbenchPreferenceConstants.SHOW_TRADITIONAL_STYLE_TABS,
				false);

		//for remove unused preferences pages	
		PreferenceManager pm = PlatformUI.getWorkbench().getPreferenceManager();
		//get all subnodes
		IPreferenceNode[] rootSubNodes = pm.getRootSubNodes();
		//loop over subnodes
		for (IPreferenceNode subNode : rootSubNodes) {
			if (!subNode.getId().contains("org.javascool")) {
				pm.remove(subNode);
			}
		}
		super.initialize(configurer);
	}
}
