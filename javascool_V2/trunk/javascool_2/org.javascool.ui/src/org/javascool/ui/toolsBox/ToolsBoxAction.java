/*
 * Copyright (c) 2008-2010 Javascool (Java's Cool).  All rights reserved.
 *	this source file is placed under license CeCILL
 * see http://www.cecill.info/licences/Licence_CeCILL_V2-fr.html
 * or http://www.cecill.info/licences/Licence_CeCILL_V2-en.html
 */
package org.javascool.ui.toolsBox;



import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IWorkbenchWindow;


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
	
	
	@Override
	public void dispose (){
		m_menu.dispose ();
	}



	@Override
	public Menu getMenu (Control parent){
		return m_menu.createContextMenu(parent);
	}

	@Override
	public Menu getMenu(Menu parent) {
		// TODO Auto-generated method stub
		return null;
	}

}