/*******************************************************************************
 * Copyright (c) 2004, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.unice.javascool.actions;

import java.io.File;
import java.text.MessageFormat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;

import org.eclipse.core.runtime.CoreException;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;

import org.eclipse.ui.editors.text.EditorsUI;

import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.ide.FileStoreEditorInput;
import org.unice.javascool.editor.editors.JVSEditor;


public class OpenFileAction extends Action implements IWorkbenchWindowActionDelegate {

	private IWorkbenchWindow fWindow;

	public OpenFileAction() {
		setEnabled(true);
	}

	public void dispose() {
		fWindow= null;
	}

	public void init(IWorkbenchWindow window) {
		fWindow= window;
	}

	public void selectionChanged(IAction action, ISelection selection) {
	}

	private File queryFile() {
		FileDialog dialog= new FileDialog(fWindow.getShell(), SWT.OPEN);
		dialog.setText("Ouvrir Fichier"); //$NON-NLS-1$
		String path= dialog.open();
		if (path != null && path.length() > 0)
			return new File(path);
		return null;
	}

	/*
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run(IAction action) {
		System.out.println("action : OpenFileAction");
		
		
		File file= queryFile();
		if (file != null) {
			IWorkbenchPage page= fWindow.getActivePage();
			try {
				IFileStore fileStore= EFS.getStore(file.toURI());
				page.openEditor(new FileStoreEditorInput(fileStore), JVSEditor.ID);
			} catch (CoreException e) {
				e.printStackTrace();
			}
		} else if (file != null) {
			String msg = MessageFormat.format("File is null: {0}", new String[] { file.getName() }); //$NON-NLS-1$
			MessageDialog.openWarning(fWindow.getShell(), "Probleme", msg); //$NON-NLS-1$
		}
	}
}