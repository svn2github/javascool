package org.unice.javascool.ui;

import org.eclipse.core.runtime.IExtension;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ContributionItemFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.internal.registry.ActionSetRegistry;
import org.eclipse.ui.internal.registry.IActionSetDescriptor;
import org.unice.javascool.ui.toolsBox.ToolsBoxAction;
import org.unice.javascool.ui.toolsBox.ToolsBoxFonctions;


/**
 * An action bar advisor is responsible for creating, adding, and disposing of
 * the actions added to a workbench window. Each window will be populated with
 * new actions.
 */
public class ApplicationActionBarAdvisor extends ActionBarAdvisor {
	// Actions - important to allocate these only in makeActions, and then use
	// them
	// in the fill methods. This ensures that the actions aren't recreated
	// when fillActionBars is called with FILL_PROXY.


	public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
		super(configurer);
		
	}

	protected void makeActions(final IWorkbenchWindow window) {
		// Creates the actions and registers them.
		// Registering is needed to ensure that key bindings work.
		// The corresponding commands keybindings are defined in the plugin.xml
		// file.
		// Registering also provides automatic disposal of the actions when
		// the window is closed.
		IWorkbenchAction save=ActionFactory.SAVE.create(window);
		save.setText(Messages.getString("IU.Strings.0")); //$NON-NLS-1$
		registerAsGlobal(save);
		
		IWorkbenchAction saveAs=ActionFactory.SAVE_AS.create(window);
		saveAs.setText(Messages.getString("IU.Strings.1")); //$NON-NLS-1$
		registerAsGlobal(saveAs);
		
		IWorkbenchAction about=ActionFactory.ABOUT.create(window);
		about.setText(Messages.getString("IU.Strings.2")); //$NON-NLS-1$
		registerAsGlobal(about);
		
		IWorkbenchAction undo=ActionFactory.UNDO.create(window);
		undo.setText(Messages.getString("IU.Strings.3")); //$NON-NLS-1$
		registerAsGlobal(undo);
		
		IWorkbenchAction redo=ActionFactory.REDO.create(window);
		redo.setText(Messages.getString("IU.Strings.4")); //$NON-NLS-1$
		registerAsGlobal(redo);
		
		IWorkbenchAction cut=ActionFactory.CUT.create(window);
		cut.setText(Messages.getString("IU.Strings.5")); //$NON-NLS-1$
		registerAsGlobal(cut);
		
		IWorkbenchAction copy=ActionFactory.COPY.create(window);
		copy.setText(Messages.getString("IU.Strings.6")); //$NON-NLS-1$
		registerAsGlobal(copy);
		
		IWorkbenchAction paste=ActionFactory.PASTE.create(window);
		paste.setText(Messages.getString("IU.Strings.7")); //$NON-NLS-1$
		registerAsGlobal(paste);
		
		IWorkbenchAction delete=ActionFactory.DELETE.create(window);
		delete.setText(Messages.getString("IU.Strings.8")); //$NON-NLS-1$
		registerAsGlobal(delete);
		
		IWorkbenchAction selectAll=ActionFactory.SELECT_ALL.create(window);
		selectAll.setText(Messages.getString("IU.Strings.9")); //$NON-NLS-1$
		registerAsGlobal(selectAll);
		
		IWorkbenchAction find=ActionFactory.FIND.create(window);
		find.setText(Messages.getString("IU.Strings.10")); //$NON-NLS-1$
		registerAsGlobal(find);
		
		IWorkbenchAction close=ActionFactory.CLOSE.create(window);
		close.setText(Messages.getString("IU.Strings.11")); //$NON-NLS-1$
		registerAsGlobal(close);
		
		IWorkbenchAction print=ActionFactory.PRINT.create(window);
		print.setText(Messages.getString("IU.Strings.12")); //$NON-NLS-1$
		registerAsGlobal(print);
		
		IWorkbenchAction quit=ActionFactory.QUIT.create(window);
		quit.setText(Messages.getString("IU.Strings.13")); //$NON-NLS-1$
		registerAsGlobal(quit);
		
		
		registerAsGlobal(ActionFactory.PREFERENCES.create(window));
		
		
		IWorkbenchAction help = ActionFactory.HELP_CONTENTS.create(window);
		help.setText(Messages.getString("IU.Strings.14")); //$NON-NLS-1$
		registerAsGlobal(help);
	}

	private void registerAsGlobal(IAction action) {
		getActionBarConfigurer().registerGlobalAction(action);
		register(action);
	}

	/**fonction de definition de la barre des boutons
	 **/
	protected void fillCoolBar(ICoolBarManager coolBar) {
		IWorkbenchWindow window = getActionBarConfigurer().getWindowConfigurer().getWindow();
		IToolBarManager toolbar = new ToolBarManager(SWT.LEFT);
		coolBar.add(new ToolBarContributionItem(toolbar, Messages.getString("IU.Strings.15"))); //$NON-NLS-1$
		toolbar.add(new GroupMarker(Messages.getString("IU.Strings.16"))); //$NON-NLS-1$


		/*IWorkbenchAction open = ActionFactory..create(window);
		open.setImageDescriptor(Activator.getImageDescriptor("icons/charger.png"));
		toolbar.add(open);*/

		IWorkbenchAction save = ActionFactory.SAVE.create(window);
		save.setImageDescriptor(Activator.getImageDescriptor(Messages.getString("IU.Strings.17"))); //$NON-NLS-1$
		save.setDisabledImageDescriptor(Activator.getImageDescriptor(Messages.getString("IU.Strings.18"))); //$NON-NLS-1$
		save.setHoverImageDescriptor(Activator.getImageDescriptor(Messages.getString("IU.Strings.19"))); //$NON-NLS-1$
		save.setText(Messages.getString("IU.Strings.20")); //$NON-NLS-1$
		save.setToolTipText(Messages.getString("IU.Strings.21")); //$NON-NLS-1$
		toolbar.add(save);

		IWorkbenchAction print = ActionFactory.PRINT.create(window);
		print.setImageDescriptor(Activator.getImageDescriptor(Messages.getString("IU.Strings.22"))); //$NON-NLS-1$
		print.setDisabledImageDescriptor(Activator.getImageDescriptor(Messages.getString("IU.Strings.23"))); //$NON-NLS-1$
		print.setHoverImageDescriptor(Activator.getImageDescriptor(Messages.getString("IU.Strings.24"))); //$NON-NLS-1$
		print.setText(Messages.getString("IU.Strings.25")); //$NON-NLS-1$
		print.setToolTipText(Messages.getString("IU.Strings.26")); //$NON-NLS-1$
		toolbar.add(print);


		toolbar.add(new GroupMarker(Messages.getString("IU.Strings.27"))); //$NON-NLS-1$
		toolbar.add(new Separator());
		IWorkbenchAction find = ActionFactory.FIND.create(window);
		find.setImageDescriptor(Activator.getImageDescriptor(Messages.getString("IU.Strings.28"))); //$NON-NLS-1$
		//find.setDisabledImageDescriptor(Activator.getImageDescriptor(Messages.getString("IU.Strings.29"))); //$NON-NLS-1$
		//find.setHoverImageDescriptor(Activator.getImageDescriptor(Messages.getString("IU.Strings.30"))); //$NON-NLS-1$
		find.setText(Messages.getString("IU.Strings.31")); //$NON-NLS-1$
		find.setToolTipText(Messages.getString("IU.Strings.32")); //$NON-NLS-1$
		toolbar.add(find);

		/*toolbar.add(ActionFactory.COPY.create(window));
		toolbar.add(ActionFactory.CUT.create(window));
		toolbar.add(ActionFactory.PASTE.create(window));
		toolbar.add(new Separator());*/

		//TODO Rrgler le bug d'icon non charger undo / redo entre l'editeur et la console
		/*IWorkbenchAction undo = ActionFactory.UNDO.create(window);
		undo.setImageDescriptor(Activator.getImageDescriptor("icons/undo.gif"));
		undo.setDisabledImageDescriptor(Activator.getImageDescriptor("icons/undo.gif"));
		undo.setHoverImageDescriptor(Activator.getImageDescriptor("icons/undo.gif"));
		toolbar.add(undo);

		IWorkbenchAction redo = ActionFactory.REDO.create(window);
		redo.setImageDescriptor(Activator.getImageDescriptor("icons/redo.gif"));
		redo.setDisabledImageDescriptor(Activator.getImageDescriptor("icons/redo.gif"));
		redo.setHoverImageDescriptor(Activator.getImageDescriptor("icons/redo.gif"));
		toolbar.add(redo);*/

		
		//toolbar.add(ActionFactory.REDO.create(window));*/

		//toolbox2.png
		ToolsBoxAction toolBox = new ToolsBoxAction(window);
		toolBox.setImageDescriptor(Activator.getImageDescriptor(Messages.getString("IU.Strings.33"))); //$NON-NLS-1$
		toolBox.setDisabledImageDescriptor(Activator.getImageDescriptor(Messages.getString("IU.Strings.34"))); //$NON-NLS-1$
		toolBox.setHoverImageDescriptor(Activator.getImageDescriptor(Messages.getString("IU.Strings.35"))); //$NON-NLS-1$
		toolbar.add(toolBox);
		
		
		//action_toolsBox.setMenuCreator(new IMenuCreator(){});
	
		//coolBar.add(new ToolBarContributionItem(toolbar2, "main2"));
		//toolbar2.add(ActionFactory.SHOW_VIEW_MENU.create(window));

		
		IToolBarManager toolbar2 = new ToolBarManager(SWT.RIGHT | SWT.FLAT | SWT.HORIZONTAL);
		
		coolBar.add(new ToolBarContributionItem(toolbar2, Messages.getString("IU.Strings.36")));	 //$NON-NLS-1$
		toolbar2.add(new GroupMarker(Messages.getString("IU.Strings.37"))); //$NON-NLS-1$
		toolbar2.add(new GroupMarker(Messages.getString("IU.Strings.38"))); //$NON-NLS-1$
	}


	/**
	 * fonction de definition de la barre des menus 
	 **/
	protected void fillMenuBar(IMenuManager menuBar){
		IWorkbenchWindow window = getActionBarConfigurer().getWindowConfigurer().getWindow();
		menuBar.add(createFileMenu(window));
		menuBar.add(createEditMenu());
		menuBar.add(ToolsBoxFonctions.createToolBoxMenu(window));
		menuBar.add(createHelpMenu(window));
	}



	/**
	 * Creates and returns the 'File' menu.
	 */
	private MenuManager createFileMenu(	IWorkbenchWindow window) {
		MenuManager menu = new MenuManager(Messages.getString("IU.Strings.39"),IWorkbenchActionConstants.M_FILE); //$NON-NLS-1$
		

		menu.add(new GroupMarker(IWorkbenchActionConstants.FILE_START));

		IContributionItem[] items=menu.getItems();
		
		
		menu.add(new GroupMarker(IWorkbenchActionConstants.NEW_EXT));
		
		
		/**/
		/**/
		
		menu.add(getAction(ActionFactory.CLOSE.getId()));
		menu.add(new GroupMarker(IWorkbenchActionConstants.CLOSE_EXT));

		menu.add(new Separator());
		
		menu.add(getAction(ActionFactory.SAVE.getId()));
		menu.add(getAction(ActionFactory.SAVE_AS.getId()));
		//menu.add(getAction(ActionFactory.REVERT.getId()));
		menu.add(new Separator());
		menu.add(getAction(ActionFactory.PRINT.getId()));
		
		menu.add(ContributionItemFactory.REOPEN_EDITORS.create(window));

		menu.add(new Separator());

		menu.add(getAction(ActionFactory.QUIT.getId()));
		menu.add(new GroupMarker(IWorkbenchActionConstants.FILE_END));

		
		//remove convert line delimiter and open file doublon
		ActionSetRegistry reg = WorkbenchPlugin.getDefault().getActionSetRegistry();
		IActionSetDescriptor[] actionSets = reg.getActionSets();
		//String actionSetId = "org.eclipse.ui.edit.text.actionSet.navigation"; //$NON-NLS-1$
		String actionSetId = "org.eclipse.ui.edit.text.actionSet.convertLineDelimitersTo"; //$NON-NLS-1$
		String actionSetId2 = "org.eclipse.ui.actionSet.openFiles"; //$NON-NLS-1$
		// Removing convert line delimiters menu.

		for (int i = 0; i <actionSets.length; i++)
		{
			if ((!actionSets[i].getId().equals(actionSetId)) && (!actionSets[i].getId().equals(actionSetId2)))
				continue;
			IExtension ext = actionSets[i].getConfigurationElement()
				.getDeclaringExtension();
			reg.removeExtension(ext, new Object[] { actionSets[i] });
		}
		return menu;
	}


	/**
	 * Creates and returns the 'Edit' menu.
	 */
	private MenuManager createEditMenu() {
		MenuManager menu = new MenuManager("&Edition", Messages.getString("IU.Strings.40")); //$NON-NLS-1$ //$NON-NLS-2$
		menu.add(new GroupMarker(Messages.getString("IU.Strings.41"))); //$NON-NLS-1$

		menu.add(getAction(ActionFactory.UNDO.getId()));
		menu.add(getAction(ActionFactory.REDO.getId()));;

		menu.add(new GroupMarker(IWorkbenchActionConstants.UNDO_EXT));
		menu.add(new Separator());
		menu.add(getAction(ActionFactory.CUT.getId()));
		menu.add(getAction(ActionFactory.COPY.getId()));
		menu.add(getAction(ActionFactory.PASTE.getId()));
		menu.add(new GroupMarker(IWorkbenchActionConstants.CUT_EXT));
		menu.add(new Separator());
		menu.add(getAction(ActionFactory.DELETE.getId()));
		menu.add(getAction(ActionFactory.SELECT_ALL.getId()));
		menu.add(getAction(ActionFactory.FIND.getId()));
		menu.add(new Separator());
		menu.add(getAction(ActionFactory.PREFERENCES.getId()));
		return menu;
	}
	
	
	/**
	 * Creates and returns the 'Help' menu.
	 * @param window 
	 */
	private MenuManager createHelpMenu(IWorkbenchWindow window) {
		MenuManager menu = new MenuManager(Messages.getString("IU.Strings.42")); //$NON-NLS-1$
		
		
		menu.add(getAction(ActionFactory.HELP_CONTENTS.getId()));	
		menu.add(new Separator());
		IWorkbenchAction about = ActionFactory.ABOUT.create(window);
		about.setText(Messages.getString("IU.Strings.43")); //$NON-NLS-1$
		menu.add(about);
		
		return menu;	
	}
}
