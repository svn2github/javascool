/*
 * Copyright (c) 2008-2010 Javascool (Java's Cool).  All rights reserved.
 *	this source file is placed under license CeCILL
 * see http://www.cecill.info/licences/Licence_CeCILL_V2-fr.html
 * or http://www.cecill.info/licences/Licence_CeCILL_V2-en.html
 */
package org.javascool.ui.editor;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleConstants;
import org.javascool.ui.editor.console.JVSConsole;

public class Perspective implements IPerspectiveFactory {

	
	public static final String ID = "org.javascool.ui.editor.perspectiveEditor";
	
	public void createInitialLayout(IPageLayout layout) {

		defineActions(layout);
		defineLayout(layout);
	}

	public void defineActions(IPageLayout layout) {
		// Add "new wizards".
		// layout.addNewWizardShortcut("org.eclipse.ui.wizards.new.folder");
		// layout.addNewWizardShortcut("org.eclipse.ui.wizards.new.file");

		// Add "show views".
		// layout.addShowViewShortcut(IPageLayout.ID_RES_NAV);
		// layout.addShowViewShortcut(IPageLayout.ID_BOOKMARKS);
		// layout.addShowViewShortcut(IPageLayout.ID_OUTLINE);
		// layout.addShowViewShortcut(IPageLayout.ID_PROP_SHEET);
		// layout.addShowViewShortcut(IPageLayout.ID_TASK_LIST);
	}


	public void defineLayout(IPageLayout layout) {
		// Editors are placed for free.
		@SuppressWarnings("unused")
		String editorArea = layout.getEditorArea();

		layout.addView(IConsoleConstants.ID_CONSOLE_VIEW, IPageLayout.RIGHT, 0.5f, layout.getEditorArea());
		layout.getViewLayout(IConsoleConstants.ID_CONSOLE_VIEW).setCloseable(false);
		layout.getViewLayout(IConsoleConstants.ID_CONSOLE_VIEW).setMoveable(true);

		ConsolePlugin.getDefault().getConsoleManager().addConsoles( new IConsole[] { new JVSConsole() } );

	}

}
