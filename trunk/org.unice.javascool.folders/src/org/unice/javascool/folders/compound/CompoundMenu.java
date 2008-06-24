package org.unice.javascool.folders.compound;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.CompoundContributionItem;
import org.eclipse.ui.menus.CommandContributionItem;
import org.unice.javascool.conf.*;
import org.unice.javascool.folders.command.CommandDetruire;
import org.unice.javascool.folders.command.CommandExplor;
import org.unice.javascool.folders.command.CommandSauver;

public class CompoundMenu extends CompoundContributionItem {

	public CompoundMenu() {
	}

	public CompoundMenu(String id) {
		super(id);
	}

	@Override
	protected IContributionItem[] getContributionItems() {
		IContributionItem[] list = new IContributionItem[0];
		ArrayList<BeanDirectories> bds=null;
		try {
			bds=BeanFactory.getBeanDirectories(BeanFactory.repConfFile);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ConfException e) {
			e.printStackTrace();
		}
		if(bds.size()>0){
			list=new IContributionItem[bds.size()+2];
			list[0]=new CommandContributionItem(PlatformUI.getWorkbench(),
					null,
					CommandDetruire.ID,null, null, null, null,"Detruire Dossier(s)", null,
					null, CommandContributionItem.STYLE_PUSH);
			list[1]=new Separator();
		}else list=new IContributionItem[bds.size()];
		int i=2;
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
			list[i]=mm;
			i++;
		}
		return list;
	}
}
