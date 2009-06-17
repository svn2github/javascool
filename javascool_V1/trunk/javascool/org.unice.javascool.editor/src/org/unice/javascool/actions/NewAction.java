package org.unice.javascool.actions;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.runtime.IPath;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.FileStoreEditorInput;
import org.eclipse.ui.internal.editors.text.EditorsPlugin;
import org.unice.javascool.editor.editors.JVSEditor;

public class NewAction {

	public static int nbFile = 0 ;
	
	
	@SuppressWarnings("restriction")
	private static IFileStore queryFileStore() {
		IPath stateLocation = EditorsPlugin.getDefault().getStateLocation();
		IPath path= stateLocation.append("/Tmp" +nbFile+".jvs"); //$NON-NLS-1$
		++nbFile;
		return EFS.getLocalFileSystem().getStore(path);
	}



	public static boolean action() {
		IFileStore fileStore= queryFileStore();
		IWorkbenchPage page= PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		try {
			page.openEditor(new FileStoreEditorInput(fileStore), JVSEditor.ID);
		} catch (PartInitException e) {
			return false;
		}
		return true;
	}


}
