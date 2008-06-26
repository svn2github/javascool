package org.unice.javascool.orphy.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.jface.dialogs.MessageDialog;
import org.unice.javascool.editor.editors.JVSEditor;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class DemarrerOrphy extends AbstractHandler {
	/**
	 * The constructor.
	 */
	public DemarrerOrphy() {
	}

	/**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		insert_code("void Orphy.demarrer()", window);
		return null;
	}
	
	private void insert_code(String sign, IWorkbenchWindow window) {
		JVSEditor editor = (JVSEditor)PlatformUI.getWorkbench().getActiveWorkbenchWindow().
		getActivePage().getActiveEditor();

		//insertion de la signature de la methode
		editor.insertText(sign, editor.getOffset());

	}
}
