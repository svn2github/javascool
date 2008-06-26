package org.unice.javascool.ui;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.console.IConsoleConstants;
import org.unice.javascool.ui.console.Console;

public class Perspective implements IPerspectiveFactory {

	public void createInitialLayout(IPageLayout layout) {

		@SuppressWarnings("unused")
		String editorArea = layout.getEditorArea();
		@SuppressWarnings("unused")
		Console console = new Console();
		
		layout.setEditorAreaVisible(false);
		layout.setFixed(true);
		
		//layout.addStandaloneView(View.ID,  false, IPageLayout.LEFT, 0.50f, editorArea);
		//layout.addView(View.ID, IPageLayout.LEFT, 0.50f, layout.getEditorArea());
		layout.addView(IConsoleConstants.ID_CONSOLE_VIEW, IPageLayout.RIGHT,0.50f, layout.getEditorArea());
	}

}
