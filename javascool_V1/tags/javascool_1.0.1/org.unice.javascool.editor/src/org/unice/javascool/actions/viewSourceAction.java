package org.unice.javascool.actions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.MessageFormat;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.FileStoreEditorInput;
import org.unice.javascool.editor.editors.JVSEditor;

public class viewSourceAction implements IWorkbenchWindowActionDelegate {

	private IWorkbenchWindow window;

	@Override
	public void dispose() {
	}

	@Override
	public void init(IWorkbenchWindow window) {
		this.window = window;
	}

	@Override
	public void run(IAction action) {
		//	boolean b = MessageDialog.openConfirm(window.getShell(), "affichage du code source java",
		//			"voulez-vous vraiment affiher le code source java correspondant au fichier jvs");

		//	if(b){
		JVSEditor editor = (JVSEditor)PlatformUI.getWorkbench().getActiveWorkbenchWindow()
		.getActivePage().getActiveEditor();

		String path = editor.getFilePath();
		if(path.endsWith(".jvs")){//si fichier jvs
			path = path.replace(".jvs",".java");
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
	// TODO Auto-generated method stub

	//}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		// TODO Auto-generated method stub

	}

}
