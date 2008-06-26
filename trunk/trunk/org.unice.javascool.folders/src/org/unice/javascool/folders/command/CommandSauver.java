package org.unice.javascool.folders.command;

import java.io.File;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.editors.text.*;
import org.eclipse.ui.ide.FileStoreEditorInput;

public class CommandSauver extends AbstractHandler {

	public static final String ID="org.unice.javascool.folders.command.save";
	public static final String FOLDER="org.unice.javascool.command.save.param.folder";
	
	@Override
	public boolean isEnabled(){
		IWorkbenchPage myPage=null;
		try{
			myPage=PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		}catch(NullPointerException e){
			return false;
		}
		if(myPage!=null){
			return myPage.getActiveEditor()!=null;
		}else return false;
	}

	@Override
	public Object execute(ExecutionEvent arg0) throws ExecutionException {
		FileDialog dialog = new FileDialog(new Shell(), SWT.SAVE); 
		File dir=new File(arg0.getParameter(FOLDER));
		String pathFilter=(dir.list().length!=0) ? dir.getAbsolutePath()+File.separator+dir.list()[0] : dir.getAbsolutePath()+File.separator;
		dialog.setFilterPath(pathFilter);
		String res=null;
		res=dialog.open();
		if(res != null){
			IEditorDescriptor desc = PlatformUI.getWorkbench().getEditorRegistry().getDefaultEditor(res);
			IWorkbenchPage myPage =PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			IEditorPart myEditor = myPage.getActiveEditor();
			IEditorInput input = myEditor.getEditorInput();
			IDocumentProvider provider=(myEditor instanceof TextEditor) ? ((TextEditor)myEditor).getDocumentProvider() : null;
			if(provider == null){
				MessageBox mb = new MessageBox(new Shell());
				mb.setText("Alert");
				mb.setMessage("Impossible de deplacer le fichier\n");
				mb.open();
				return null;
			}
			IEditorInput newInput = new FileStoreEditorInput(new PlayPathEditorInput(new Path(res)));
			provider.aboutToChange(newInput);
			try {
				provider.saveDocument(null, newInput, provider.getDocument(input), true);
				myPage.closeEditor(myEditor,false);
				myPage.openEditor(newInput,desc.getId());
			} catch (CoreException e) {
				MessageBox mb = new MessageBox(new Shell());
				mb.setText("Alert");
				mb.setMessage("Impossible de deplacer le fichier\n");
				mb.open();
				return null;
			}
		}
		return null;
	}
}