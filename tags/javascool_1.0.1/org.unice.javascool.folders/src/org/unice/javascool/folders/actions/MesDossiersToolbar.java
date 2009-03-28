package org.unice.javascool.folders.actions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.IWorkbenchWindowPulldownDelegate;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.menus.CommandContributionItem;
import org.eclipse.jface.dialogs.MessageDialog;
import org.unice.javascool.conf.BeanDirectories;
import org.unice.javascool.conf.BeanFactory;
import org.unice.javascool.conf.ConfException;
import org.unice.javascool.folders.command.CommandAjout;
import org.unice.javascool.folders.command.CommandDetruire;
import org.unice.javascool.folders.command.CommandExplor;
import org.unice.javascool.folders.command.CommandSauver;

/**
 * Our sample action implements workbench action delegate.
 * The action proxy will be created by the workbench and
 * shown in the UI. When the user tries to use the action,
 * this delegate will be created and execution will be 
 * delegated to it.
 * @see IWorkbenchWindowActionDelegate
 */
public class MesDossiersToolbar implements IWorkbenchWindowActionDelegate,IWorkbenchWindowPulldownDelegate {
	private MenuManager menuManager=null;
	private IWorkbenchWindow window;
	
	
	
	/**
	 * The constructor.
	 */
	public MesDossiersToolbar() {
	}

	/**
	 * The action has been activated. The argument of the
	 * method represents the 'real' action sitting
	 * in the workbench UI.
	 * @see IWorkbenchWindowActionDelegate#run
	 */
	public void run(IAction action) {
		try {
			new CommandAjout().execute(null);
		} catch (ExecutionException e) {
			MessageDialog.openInformation(
			window.getShell(),
			"Mes dossiers",
			"Impossible d'effectuer cette action");
		}
	}

	/**
	 * Selection in the workbench has been changed. We 
	 * can change the state of the 'real' action here
	 * if we want, but this can only happen after 
	 * the delegate has been created.
	 * @see IWorkbenchWindowActionDelegate#selectionChanged
	 */
	public void selectionChanged(IAction action, ISelection selection) {
	}

	/**
	 * We can use this method to dispose of any system
	 * resources we previously allocated.
	 * @see IWorkbenchWindowActionDelegate#dispose
	 */
	public void dispose() {
	}

	/**
	 * We will cache window object in order to
	 * be able to provide parent shell for the message dialog.
	 * @see IWorkbenchWindowActionDelegate#init
	 */
	public void init(IWorkbenchWindow window) {
		this.window = window;
	}

	/**
	 * @param parent the parent Control panel
	 * to build a dynamic menu
	 */
	public Menu getMenu(Control parent) {
		Menu fMenu = null;
		menuManager = new MenuManager();
		fMenu = menuManager.createContextMenu(parent);
		menuManager.removeAll();
		ArrayList<BeanDirectories> bds=null;
		try {
			bds=BeanFactory.getBeanDirectories(BeanFactory.repConfFile);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ConfException e) {
			e.printStackTrace();
		}
		if(bds.size()>0){
			for(BeanDirectories bd:bds){
				MenuManager mm=new MenuManager(bd.getNom());
				Map<String,String> param=new HashMap<String,String>();
				param.put(CommandSauver.FOLDER,bd.getPath());
				CommandContributionItem save=new CommandContributionItem(PlatformUI.getWorkbench(),
						null,
						CommandSauver.ID,param, null, null, null,"Sauver sous...", null,
						null, CommandContributionItem.STYLE_PUSH);
				mm.add(save);
				param=new HashMap<String,String>();
				param.put(CommandExplor.FOLDER,bd.getPath());
				CommandContributionItem explor=new CommandContributionItem(PlatformUI.getWorkbench(),
						null,
						CommandExplor.ID,param, null, null, null,"Ouvrir ...", null,
						null, CommandContributionItem.STYLE_PUSH);
				mm.add(explor);
				menuManager.add(mm);
			}
		}else{
			menuManager.add(new CommandContributionItem(PlatformUI.getWorkbench(),
					null,
					CommandAjout.ID,null, null, null, null,"Ajouter", null,
					null, CommandContributionItem.STYLE_PUSH));
		}
		return fMenu;
	}
}