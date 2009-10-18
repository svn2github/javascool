package org.javascool.ui.toolsBox;


import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.javascool.ui.editor.editors.JVSEditor;


public class ToolsBoxAction extends Action implements IMenuCreator{

	protected MenuManager m_menu;
	protected IWorkbenchWindow window;
	
	public ToolsBoxAction (IWorkbenchWindow window){
		super ("ToolsBox", Action.AS_DROP_DOWN_MENU);
		setToolTipText ("Boite A Outils");
		setMenuCreator (this);
		this.window = window;

		m_menu = ToolsBoxFonctions.createToolBoxMenu(window);

	}

	/*
	public boolean isEnabled(){
		IWorkbenchPage 	page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		if(page == null) return false;
		IEditorPart editor = page.getActiveEditor();
		System.out.println(editor.getClass());
		if(editor instanceof JVSEditor) return true;
		return false;
	}
	*/
	
	
	@Override
	public void run (){
		
	}


	//@Override
	public void dispose (){
		m_menu.dispose ();
	}



	//@Override
	public Menu getMenu (Control parent){
		return m_menu.createContextMenu(parent);
	}



	//@Override
	public Menu getMenu (Menu parent){
		return null;
	}


}