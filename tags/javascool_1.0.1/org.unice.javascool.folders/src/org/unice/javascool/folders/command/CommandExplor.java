package org.unice.javascool.folders.command;
import java.io.File;
import java.io.FileFilter;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
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
import org.eclipse.ui.ide.FileStoreEditorInput;


public class CommandExplor extends AbstractHandler {

	public static final String ID="org.unice.javascool.folders.command.explor";
	public static final String FOLDER="org.unice.javascool.command.explor.param.folder";
	public static final String ACTIVEEDITOR="org.unice.javascool.commad.explor.param.activeEditor";

	public Object execute(ExecutionEvent event) throws ExecutionException {
		FileDialog fd = new FileDialog(new Shell(), SWT.OPEN);
		fd.setFilterExtensions(new String[] {"*.jvs","*.java"});
		File dir=new Path(event.getParameter(FOLDER)).toFile();
		String pathFilter=dir.getAbsolutePath();
		fd.setFilterPath(new Path(pathFilter).toOSString());
		fd.setText("Ouvrir");

		IEditorPart editor = null;
		if(fd.open() != null) {
			File file = new File(fd.getFilterPath() + File.separator + fd.getFileName());
			IEditorInput input = createEditorInput(file);
			
			IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			try {
				if(!file.exists())
					throw new Exception("Fichier non trouve");
				IEditorDescriptor desc = PlatformUI.getWorkbench().getEditorRegistry().getDefaultEditor(file.getName());
				editor = page.openEditor(input,desc.getId());
			}catch (Exception error) {
				MessageBox mb = new MessageBox(new Shell());
				mb.setText("Alert");
				mb.setMessage("Impossible d'ouvrir le fichier" + error.getMessage()+
						"Note: extensions reconnues java, jvs");
				mb.open();
				if(editor != null)
					editor.dispose();
			}
		}
		return null;
	}
	
	public FileStoreEditorInput createEditorInput(File file) {
		Path location = new Path(file.getAbsolutePath());
		FileStoreEditorInput input = new FileStoreEditorInput(new PlayPathEditorInput(location));
		return input;
		}
}