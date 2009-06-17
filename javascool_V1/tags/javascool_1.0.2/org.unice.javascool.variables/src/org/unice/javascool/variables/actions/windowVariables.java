package org.unice.javascool.variables.actions;

import org.unice.javascool.editor.editors.*;
import org.unice.javascool.util.rmi.*;

import java.io.File;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;

/**
 * Our sample action implements workbench action delegate.
 * The action proxy will be created by the workbench and
 * shown in the UI. When the user tries to use the action,
 * this delegate will be created and execution will be 
 * delegated to it.
 * @see IWorkbenchWindowActionDelegate
 */
public class windowVariables implements IWorkbenchWindowActionDelegate {


	private boolean isDisposed=false;
	private Shell fenetre;
	private Button  change, close;
	private Listener closelistener, changelistener, inspectlistener;
	private HashMap<String,Object> myFields;
	private HashMap<String,Text> selected;
	private GridLayout gridLayout ;
	private List<Button> buttonlist;
	private List<Boolean> buttonSelection;
	private String classPath;
	private String className;
	/**
	 * The constructor.
	 */
	public windowVariables() {
	}

	/**
	 * The action has been activated. The argument of the
	 * method represents the 'real' action sitting
	 * in the workbench UI.
	 * @see IWorkbenchWindowActionDelegate#run
	 */
	public void run(IAction action) {

		IEditorPart input=PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		if(!(input instanceof JVSEditor)) return;
		classPath = ((JVSEditor)input).getFilePath();
		className = classPath.substring(classPath.lastIndexOf(File.separator)+1,classPath.lastIndexOf("."));
		classPath = classPath.substring(0,classPath.lastIndexOf(File.separator));

		buttonlist = new ArrayList <Button> ();
		buttonSelection = new ArrayList<Boolean>();

		Display display = Display.getCurrent();
		fenetre = new Shell(display, SWT.SHELL_TRIM | SWT.TOOL);
		fenetre.setSize(300	, 320);
		fenetre.setLocation(400, 400);

		fenetre.setText("variables");
		gridLayout = new GridLayout();

		gridLayout.numColumns = 2;

		fenetre.setLayout(gridLayout);
		//String folder=UtilServer.getPluginFolder(org.unice.javascool.variables.Activator.PLUGIN_ID);
		//final Image bg_fenetre = new Image(display, folder+File.separator+"icons"+File.separator+"iconeVariables.gif");
		//fenetre.setBackgroundImage(bg_fenetre);
		fenetre.open();

		closelistener = new Listener() {
			public void handleEvent(Event event) {
				fenetre.close();
			}
		};

		changelistener = new Listener() {
			public void handleEvent(Event event) {
				Control[] children = fenetre.getChildren();
				for(int i = 0 ; i<children.length ; i++){
					children[i].dispose();
				}
				selected.clear();
				myFields.clear();
				getVariablesName();
			}
		};

		inspectlistener = new Listener() {
			public void handleEvent(Event event) {

				for(int i = 0; i<buttonlist.size(); i++){
					if(buttonlist.get(i).getSelection() == true){	
						buttonSelection.add(true);
					}
					else{
						buttonSelection.add(false);
					}
				}
				Control[] children = fenetre.getChildren();
				for(int i = 0 ; i<children.length ; i++){
					children[i].dispose();
				}
				getVariableValue();
				majVariableValue();

			}

		};

		getVariablesName();

	}

	public void majVariableValue() {
		Thread myThread=new Thread(){
			@Override
			public void run(){
				final String string=JavaScoolServerRegister.getProtocol()+JavaScoolServerRegister.getServer();
				UtilServer.waitForBinding(string);
				try {
					final IJavaScoolServer server = (IJavaScoolServer)Naming.lookup(string);
					while(!isDisposed && server.isRunning()){
						try{
							fenetre.getDisplay().syncExec(new Runnable(){
								public void run(){
									try{
										myFields = server.getFields(classPath,className);
										Set<String> cle=selected.keySet();
										Iterator<String> it=cle.iterator();
										while(it.hasNext()){
											String theKey=it.next();
											if(!isDisposed){
												Object field=myFields.get(theKey);
												Text text=selected.get(theKey);
												if(field!=null)
													text.setText(field.toString());
												else
													text.setText("null");
											}else {
												return;
											}
										}
										return;
									} catch (Exception e){
										return;
									}
								}
							});
							sleep(200);
						}catch(Exception e){
							return;
						}
					}
				} catch (Exception e) {
				}
				return;
			}
		};
		myThread.start();
		return;
	}

	public void getVariableValue(){
		gridLayout = new GridLayout();
		gridLayout.numColumns = 2;

		try{
			Set<String> cles = myFields.keySet();
			Iterator<String> it = cles.iterator();
			selected=new HashMap<String,Text>();
			int i=0;
			while (it.hasNext()){
				String cle = it.next(); 
				Object valeur = myFields.get(cle);
				if(buttonSelection.get(i) == true){
					GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
					Label label = new Label(fenetre, SWT.NULL);
					label.setText(cle);
					Text text = new Text(fenetre,SWT.BORDER);
					if(valeur!=null)
						text.setText(valeur.toString());
					text.setLayoutData(gridData);
					selected.put(cle,text);
					fenetre.layout();
				}
				i++;
			}

		}

		catch (Exception e) {
			e.printStackTrace();
		}

		buttonSelection.clear();
		buttonlist.clear();

		change = new Button(fenetre, SWT.PUSH);
		change.setText("Changer");
		close = new Button(fenetre, SWT.PUSH);
		close.setText("fermer");

		change.addListener(SWT.Selection, changelistener);
		close.addListener(SWT.Selection, closelistener);
		fenetre.layout();
	}

	public void getVariablesName(){
		final String string=JavaScoolServerRegister.getProtocol()+JavaScoolServerRegister.getServer();
		try {
			UtilServer.waitForBinding(string);
			IJavaScoolServer server=(IJavaScoolServer)Naming.lookup(string);
			myFields = server.getFields(classPath,className);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
		Set<String> cles = myFields.keySet();
		Iterator<String> it = cles.iterator();
		int i=0;
		while (it.hasNext()){
			String cle = it.next();
			Label label = new Label(fenetre,SWT.NULL);
			label.setText(cle);
			Button check = new Button(fenetre, SWT.CHECK);
			buttonlist.add(check);
			i++;
		}
		if(i!=0){
			Button inspect = new Button(fenetre, SWT.PUSH);
			inspect.setText("Inspecter");
			inspect.addListener(SWT.Selection, inspectlistener);
		}else{
			Label label = new Label(fenetre,SWT.NULL);
			label.setText("Aucun champ a observer");
		}
		fenetre.layout();
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
		isDisposed=true;
		fenetre.dispose();
	}

	/**
	 * We will cache window object in order to
	 * be able to provide parent shell for the message dialog.
	 * @see IWorkbenchWindowActionDelegate#init
	 */
	public void init(IWorkbenchWindow window) {
	}
}