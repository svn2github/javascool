package org.javascool.ui.editor;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleConstants;
import org.javascool.ui.editor.console.JVSConsole;

public class Perspective implements IPerspectiveFactory {

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
        String editorArea = layout.getEditorArea();
       // layout.setEditorAreaVisible(false);
        // Place navigator and outline to left of
        // editor area.
        /*
        IFolderLayout left = layout.createFolder("left", IPageLayout.LEFT, (float) 0.26, editorArea);
        left.addView(IPageLayout.ID_RES_NAV);
        left.addView(IPageLayout.ID_OUTLINE);*/
    	layout.addView(IConsoleConstants.ID_CONSOLE_VIEW, IPageLayout.RIGHT, 0.5f, layout.getEditorArea());
		layout.getViewLayout(IConsoleConstants.ID_CONSOLE_VIEW).setCloseable(false);
		layout.getViewLayout(IConsoleConstants.ID_CONSOLE_VIEW).setMoveable(true);
		
		
		ConsolePlugin.getDefault().getConsoleManager().addConsoles( new IConsole[] { new JVSConsole() } );
 
}
	
	/*
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);
		layout.setFixed(true);
		
	//	layout.addStandaloneView(View.ID,  false, IPageLayout.LEFT, 1.0f, editorArea);
		
		
		layout.addView(IConsoleConstants.ID_CONSOLE_VIEW, IPageLayout.RIGHT, 0.5f, layout.getEditorArea());
		layout.getViewLayout(IConsoleConstants.ID_CONSOLE_VIEW).setCloseable(false);
		layout.getViewLayout(IConsoleConstants.ID_CONSOLE_VIEW).setMoveable(false);
		
		
		ConsolePlugin.getDefault().getConsoleManager().addConsoles( new IConsole[] { new JVSConsole() } );
 }
 */
	
	
}
