/*
 * Copyright (c) 2008-2010 Javascool (Java's Cool).  All rights reserved.
 *	this source file is placed under license CeCILL
 * see http://www.cecill.info/licences/Licence_CeCILL_V2-fr.html
 * or http://www.cecill.info/licences/Licence_CeCILL_V2-en.html
 */
package org.javascool.ui.editor.actions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.FileStoreEditorInput;
import org.javascool.ui.editor.editors.JVSEditor;

public class ShowJavaSourceAction implements IWorkbenchWindowActionDelegate {

	public static final String ID = "org.javascool.ui.editor.actions.showJavaSourceAction";
	
	private IWorkbenchWindow window;
	
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
	}

	@Override
	public void init(IWorkbenchWindow window) {
		this.window = window;

	}

	@Override
	public void run(IAction action) {
		JVSEditor editor = (JVSEditor)PlatformUI.getWorkbench().getActiveWorkbenchWindow()
		.getActivePage().getActiveEditor();

		String path = editor.getFilePath();
		if(path.endsWith(".jvs")){//si fichier jvs
			path = path.replace(".jvs",".java");
			@SuppressWarnings("unused")
			FileReader doc = null;
			try {
				doc = new FileReader(path);
				IWorkbenchPage page= window.getActivePage();
				try {
					File file = new File(path);
					IFileStore fileStore= EFS.getStore(file.toURI());
					page.openEditor(new FileStoreEditorInput(fileStore), JVSEditor.ID);
				} catch (CoreException e) {
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				MessageDialog.openWarning(window.getShell(), "affichage du code source java",
						"Le fichier : "+path+"\n"+
				"doit etre precedemment compile");
			} 
		}else{
			MessageDialog.openError(window.getShell(), "affichage du code source java",
			"Operation impossible\n\nVous editez deja un fichier java");
		}

	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		// TODO Auto-generated method stub

	}

}
