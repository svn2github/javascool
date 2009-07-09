package org.javascool.ui.editor.actions;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.FileStoreEditorInput;
import org.eclipse.ui.internal.editors.text.EditorsPlugin;
import org.javascool.ui.editor.editors.JVSEditor;

public class NewEditorAction implements IWorkbenchWindowActionDelegate {

	public static final String ID = "org.javascool.ui.editor.actions.newEditorAction";
	
	
	public static int nbFile = 0 ;

	@Override
	public void init(IWorkbenchWindow window) {
		// TODO Auto-generated method stub

	}

	@Override
	public void run(IAction action) {
		IPath stateLocation = EditorsPlugin.getDefault().getStateLocation();
		IPath path= stateLocation.append("/Tmp" +nbFile+".jvs"); //$NON-NLS-1$
		++nbFile;
		
		IFileStore fileStore= EFS.getLocalFileSystem().getStore(path);
		
		IWorkbenchPage page= PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		try {
			page.openEditor(new FileStoreEditorInput(fileStore), JVSEditor.ID);
		} catch (PartInitException e) {
			e.printStackTrace();
		}
		

	}

	
	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}
	
	
	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		// TODO Auto-generated method stub

	}

}
