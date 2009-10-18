package org.javascool.ui.toolsBox;

import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.javascool.conf.BeanFactory;
import org.javascool.conf.BeanFonctions;
import org.javascool.conf.BeanMacros;
import org.javascool.conf.ConfException;
import org.javascool.ui.editor.editors.JVSEditor;

public class ToolsBoxFonctions {

	public static MenuManager createToolBoxMenu(IWorkbenchWindow window) {
		MenuManager m1 = new MenuManager("&Boite a Outils","Boiteaoutils");
		MenuManager macroManager; 
		MenuManager fctManager;
		try{
			//for macros
			macroManager = createMacroSubMenu(window);
			m1.add(macroManager);//ajout du menu des macros fonctions

			//for functions
			ArrayList<BeanFonctions>  list_fcts = BeanFactory.getBeanFonctions(BeanFactory.fonConfFile);
			ArrayList<String> tab_type  = new ArrayList<String>();
			for(int i=0; i<list_fcts.size(); i++){
				String type = list_fcts.get(i).getType();			
				if(!tab_type.contains(type)){
					tab_type.add(type);
				}
			}

			for(int i=0; i<tab_type.size(); i++){
				fctManager = createFctSubMenu(window, tab_type.get(i));	
				m1.add(fctManager);
			}
			
			
			//TODO here add for proglet
			MenuManager progletManager  = new MenuManager("Proglets");
			ArrayList<BeanFonctions>  list_proglet = BeanFactory.getBeanFonctions(BeanFactory.progletConFile);
			ArrayList<String> tab_proglet_type  = new ArrayList<String>();
			for(int i=0; i<list_proglet.size(); i++){
				String type = list_proglet.get(i).getType();			
				if(!tab_proglet_type.contains(type)){
					tab_proglet_type.add(type);
				}
			}

			for(int i=0; i<tab_proglet_type.size(); i++){
				fctManager = createProgletSubMenu(window, tab_proglet_type.get(i));	
				progletManager.add(fctManager);
			}
			
			m1.add(progletManager);
		
		}catch(Exception e){ e.printStackTrace();}
		
		return m1;
	}
	

	/*fonction qui cree le sous menu des macros fonctions*/
	private static MenuManager createMacroSubMenu(final IWorkbenchWindow window) throws IOException, ConfException{
		MenuManager manager = new MenuManager("Macros Fonctions");
		ArrayList<BeanMacros> macros = BeanFactory.getBeanMacros(BeanFactory.macConfFile);
		
		for(int i = 0; i<macros.size(); i++){
			final BeanMacros bean = macros.get(i);
			Action action;
			action = new Action() {
				public void run() {
					insert_code(bean.getSignature(), window);
				}

				private void insert_code(String sign, IWorkbenchWindow window) {
					JVSEditor editor = (JVSEditor)PlatformUI.getWorkbench().getActiveWorkbenchWindow().
					getActivePage().getActiveEditor();

					//insertion de la signature de la methode
					editor.insertText(sign, editor.getOffset());

				}
			};
			action.setText(bean.getNom());
			manager.add(action);
		}
		return manager;
	}

	private static MenuManager createFctSubMenu(final IWorkbenchWindow window, String type) throws IOException, ConfException{
		MenuManager manager = new MenuManager("Fonctions de "+type);
		ArrayList<BeanFonctions> fcts = BeanFactory.getBeanFonctions(BeanFactory.fonConfFile);

		for(int i = 0; i<fcts.size(); i++){
			final BeanFonctions bean = fcts.get(i);
			if(bean.getType().equals(type)){
				Action action;
				action = new Action() {
					public void run() {
							//insertion de la signature de la methode dans le code
							insert_code(bean.getSignature(), window);
					}

					/**
					 * this method insert the string sign in the actuve editor at the current
					 * cursor position
					 * @param sign : String to insert
					 * @param window :  IWorkbenchWindow
					 **/
					private void insert_code(String sign, IWorkbenchWindow window) {
						JVSEditor editor = (JVSEditor)PlatformUI.getWorkbench().getActiveWorkbenchWindow().
						getActivePage().getActiveEditor();

						//insertion de la signature de la methode
						editor.insertText(sign, editor.getOffset());

					}
				};
				action.setText(bean.getNom());
				action.setToolTipText(bean.getDesc());

				manager.add(action);
			}
		}
		return manager;
	}
	
	private static MenuManager createProgletSubMenu(final IWorkbenchWindow window, String type) throws IOException, ConfException{
		MenuManager manager = new MenuManager(type);
		ArrayList<BeanFonctions> proglet = BeanFactory.getBeanFonctions(BeanFactory.progletConFile);

		for(int i = 0; i<proglet.size(); i++){
			final BeanFonctions bean = proglet.get(i);
			if(bean.getType().equals(type)){
				Action action;
				action = new Action() {
					public void run() {
							//insertion de la signature de la methode dans le code
							insert_code(bean.getSignature(), window);
					}

					/**
					 * this method insert the string sign in the actuve editor at the current
					 * cursor position
					 * @param sign : String to insert
					 * @param window :  IWorkbenchWindow
					 **/
					private void insert_code(String sign, IWorkbenchWindow window) {
						JVSEditor editor = (JVSEditor)PlatformUI.getWorkbench().getActiveWorkbenchWindow().
						getActivePage().getActiveEditor();

						//insertion de la signature de la methode
						editor.insertText(sign, editor.getOffset());

					}
				};
				action.setText(bean.getNom());
				action.setToolTipText(bean.getDesc());

				manager.add(action);
			}
		}
		return manager;
	}

	
}
