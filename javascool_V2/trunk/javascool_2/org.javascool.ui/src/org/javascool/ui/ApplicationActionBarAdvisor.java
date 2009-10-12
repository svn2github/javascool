package org.javascool.ui;

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
import org.javascool.ui.toolsBox.ToolsBoxAction;
import org.javascool.ui.toolsBox.ToolsBoxFonctions;
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
	private IWorkbenchAction exitAction;

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
				
		
		IWorkbenchAction save = ActionFactory.SAVE.create(window);
		save.setText("Sauvegarder");
		registerAsGlobal(save);
		
		IWorkbenchAction saveAs = ActionFactory.SAVE_AS.create(window);
		saveAs.setText("Sauvegarder Sous...");
		registerAsGlobal(saveAs);
		
		/*
		IWorkbenchAction about=ActionFactory.ABOUT.create(window);
		about.setText("A Propos");
		registerAsGlobal(about);
		*/
		IWorkbenchAction undo = ActionFactory.UNDO.create(window);
		undo.setText("Defaire");
		registerAsGlobal(undo);
		
		IWorkbenchAction redo = ActionFactory.REDO.create(window);
		redo.setText("Refaire");
		registerAsGlobal(redo);
		
		IWorkbenchAction cut = ActionFactory.CUT.create(window);
		cut.setText("Couper");
		registerAsGlobal(cut);
		
		IWorkbenchAction copy=ActionFactory.COPY.create(window);
		copy.setText("Copier");
		registerAsGlobal(copy);
		
		IWorkbenchAction paste=ActionFactory.PASTE.create(window);
		paste.setText("Coller");
		registerAsGlobal(paste);
		
		IWorkbenchAction delete = ActionFactory.DELETE.create(window);
		delete.setText("Supprimer");
		registerAsGlobal(delete);
		
		IWorkbenchAction selectAll = ActionFactory.SELECT_ALL.create(window);
		selectAll.setText("Selectionner tout");
		registerAsGlobal(selectAll);
		
		IWorkbenchAction find = ActionFactory.FIND.create(window);
		find.setText("Chercher/Remplacer...");
		registerAsGlobal(find);
		
		IWorkbenchAction close=ActionFactory.CLOSE.create(window);
		close.setText("Fermer");
		registerAsGlobal(close);
		
		IWorkbenchAction close_all = ActionFactory.CLOSE_ALL.create(window);
		close_all.setText("Fermer tous");
		registerAsGlobal(close_all);
		
		IWorkbenchAction print=ActionFactory.PRINT.create(window);
		print.setText("Imprimer");
		registerAsGlobal(print);
		
		IWorkbenchAction quit=ActionFactory.QUIT.create(window);
		quit.setText("Quitter");
		registerAsGlobal(quit);
		
		
		registerAsGlobal(ActionFactory.PREFERENCES.create(window));
		
		
		IWorkbenchAction help = ActionFactory.HELP_CONTENTS.create(window);
		help.setText("&Aide");
		registerAsGlobal(help);
	}

	private void registerAsGlobal(IAction action) {
		getActionBarConfigurer().registerGlobalAction(action);
		register(action);
	}
	
	
	
	/**
	 * create the cool bar of the application
	 */
	protected void fillCoolBar(ICoolBarManager coolBar) {
		IWorkbenchWindow window = getActionBarConfigurer().getWindowConfigurer().getWindow();
		IToolBarManager toolbar = new ToolBarManager(SWT.LEFT);
		coolBar.add(new ToolBarContributionItem(toolbar, "main")); 
	
		toolbar.add(new GroupMarker("FileGroup"));
		toolbar.add(new Separator());
		
		/*IWorkbenchAction open = ActionFactory..create(window);
		open.setImageDescriptor(Activator.getImageDescriptor("icons/charger.png"));
		toolbar.add(open);*/

		IWorkbenchAction save = ActionFactory.SAVE.create(window);
		save.setImageDescriptor(Activator.getImageDescriptor("img/Save.png"));
		save.setDisabledImageDescriptor(Activator.getImageDescriptor("img/Save.png")); //$NON-NLS-1$
		save.setHoverImageDescriptor(Activator.getImageDescriptor("img/Save.png")); //$NON-NLS-1$
		save.setText("Sauver");
		save.setToolTipText("Sauver");
		toolbar.add(save);

		IWorkbenchAction print = ActionFactory.PRINT.create(window);
		print.setImageDescriptor(Activator.getImageDescriptor("img/imprimante.png"));
		print.setDisabledImageDescriptor(Activator.getImageDescriptor("img/imprimante.png"));
		print.setHoverImageDescriptor(Activator.getImageDescriptor("img/imprimante.png"));
		print.setText("Imprimer"); 
		print.setToolTipText("Imprimer"); 
		toolbar.add(print);


		toolbar.add(new GroupMarker("EditgGroup"));
		toolbar.add(new Separator());
		
		IWorkbenchAction find = ActionFactory.FIND.create(window);
		find.setImageDescriptor(Activator.getImageDescriptor("img/Find-replace.png")); //$NON-NLS-1$
		//find.setDisabledImageDescriptor(Activator.getImageDescriptor(Messages.getString("IU.Strings.29"))); //$NON-NLS-1$
		//find.setHoverImageDescriptor(Activator.getImageDescriptor(Messages.getString("IU.Strings.30"))); //$NON-NLS-1$
		find.setText("Chercher");
		find.setToolTipText("Chercher"); 
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

		//TODO here for the toolbox
		ToolsBoxAction toolBox = new ToolsBoxAction(window);
		toolBox.setImageDescriptor(Activator.getImageDescriptor("img/toolbox.png"));
		toolBox.setDisabledImageDescriptor(Activator.getImageDescriptor("img/toolbox.png"));
		toolBox.setHoverImageDescriptor(Activator.getImageDescriptor("img/toolbox.png"));
		toolbar.add(toolBox);
		
		
		//action_toolsBox.setMenuCreator(new IMenuCreator(){});
	
		//coolBar.add(new ToolBarContributionItem(toolbar2, "main2"));
		//toolbar2.add(ActionFactory.SHOW_VIEW_MENU.create(window));

		
		//IToolBarManager toolbar2 = new ToolBarManager(SWT.RIGHT | SWT.FLAT | SWT.HORIZONTAL);
		
		//coolBar.add(new ToolBarContributionItem(toolbar2, "console"));
		//toolbar2.add(new GroupMarker(Messages.getString("IU.Strings.37"))); //$NON-NLS-1$
		//toolbar2.add(new GroupMarker(Messages.getString("IU.Strings.38"))); //$NON-NLS-1$
	}
	
	/**
	 * Fill the menu bar
	 */
	protected void fillMenuBar(IMenuManager menuBar) {
		IWorkbenchWindow window = getActionBarConfigurer().getWindowConfigurer().getWindow();
		
		menuBar.add(createFileMenu(window));
		menuBar.add(createEditMenu());
		menuBar.add(createWindowMenu(window));
		menuBar.add(ToolsBoxFonctions.createToolBoxMenu(window));
		menuBar.add(createHelpMenu(window));
		
		
	}
	
	/**
	 * create the Menu "File" of the application
	 * @param window
	 * @return
	 */
	private MenuManager createFileMenu(	IWorkbenchWindow window) {
		MenuManager fileMenu = new MenuManager("&Fichier", IWorkbenchActionConstants.M_FILE);
		

		fileMenu.add(new GroupMarker(IWorkbenchActionConstants.FILE_START));

		IContributionItem[] items = fileMenu.getItems();
		
		fileMenu.add(new GroupMarker(IWorkbenchActionConstants.NEW_EXT));
		
		fileMenu.add(new Separator());
		
		fileMenu.add(getAction(ActionFactory.CLOSE.getId()));
		fileMenu.add(getAction(ActionFactory.CLOSE_ALL.getId()));
		
		fileMenu.add(new GroupMarker(IWorkbenchActionConstants.CLOSE_EXT));

		fileMenu.add(new Separator());

		fileMenu.add(getAction(ActionFactory.SAVE.getId()));
		fileMenu.add(getAction(ActionFactory.SAVE_AS.getId()));
		
		fileMenu.add(new Separator());
		fileMenu.add(getAction(ActionFactory.PRINT.getId()));
		
		fileMenu.add(ContributionItemFactory.REOPEN_EDITORS.create(window));

		fileMenu.add(new Separator());

		fileMenu.add(getAction(ActionFactory.QUIT.getId()));
		fileMenu.add(new GroupMarker(IWorkbenchActionConstants.FILE_END));

		
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
		return fileMenu;
	}


	/**
	 * create the menu "Edit" of the application
	 * @return
	 */
	private MenuManager createEditMenu() {
		MenuManager menu = new MenuManager("&Edition", "Edition"); 
		
		menu.add(new GroupMarker("Edit")); 

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
	
	private MenuManager createWindowMenu(IWorkbenchWindow window) {
		MenuManager windowMenu = new MenuManager("&Fenetre", IWorkbenchActionConstants.M_WINDOW);
					
		IWorkbenchAction perspective = ActionFactory.OPEN_PERSPECTIVE_DIALOG.create(window);
		perspective.setText("Perspective...");
		windowMenu.add(perspective);
		
		return windowMenu;	
	}
	
	
	/**
	 * Creates the 'Help' menu.
	 * @param window 
	 */
	private MenuManager createHelpMenu(IWorkbenchWindow window) {
		MenuManager helpMenu = new MenuManager("&Aide", IWorkbenchActionConstants.M_HELP);
			
		
		helpMenu.add(getAction(ActionFactory.HELP_CONTENTS.getId()));	
		helpMenu.add(new Separator());
		IWorkbenchAction about = ActionFactory.ABOUT.create(window);
		about.setText("A Propos");
		helpMenu.add(about);
		
		return helpMenu;	
	}

}
