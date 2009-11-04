/*
 * Copyright (c) 2008-2010 Javascool (Java's Cool).  All rights reserved.
 *	this source file is placed under license CeCILL
 * see http://www.cecill.info/licences/Licence_CeCILL_V2-fr.html
 * or http://www.cecill.info/licences/Licence_CeCILL_V2-en.html
 */
package org.javascool.ui.editor.actions;

import java.io.File;
import java.text.MessageFormat;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.ide.FileStoreEditorInput;
import org.javascool.ui.editor.editors.JVSEditor;

public class OpenFileAction implements IWorkbenchWindowActionDelegate {

	public static final String ID = "org.javascool.ui.editor.actions.openFileAction";

	
	private IWorkbenchWindow window;

	
	@Override
	public void init(IWorkbenchWindow window) {
		this.window = window;
	}

	@Override
	public void run(IAction action) {
		FileDialog fd = new FileDialog(window.getShell(), SWT.OPEN);
		fd.setFilterExtensions(new String[] {"*.jvs", "*.java"});
		fd.setText("Open...");

		if(fd.open() != null) {

			File file = new File(fd.getFilterPath() + "/" + fd.getFileName());
		
			if (file != null) {
				IWorkbenchPage page= window.getActivePage();
				try {
					IFileStore fileStore= EFS.getStore(file.toURI());
					page.openEditor(new FileStoreEditorInput(fileStore), JVSEditor.ID);
				} catch (CoreException e) {
					e.printStackTrace();
				}
			} else if (file != null) {
				String msg = MessageFormat.format("File is null: {0}", new String[] { file.getName() }); 
				MessageDialog.openWarning(window.getShell(), "Probleme", msg);
			}
		}
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}



}
