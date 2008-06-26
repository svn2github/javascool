package org.unice.javascool.folders.command;

import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.jface.wizard.WizardSelectionPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.unice.javascool.conf.BeanDirectories;
import org.unice.javascool.conf.BeanFactory;
import org.unice.javascool.conf.ConfException;


public class CommandDetruire extends AbstractHandler {

	private ArrayList<BeanDirectories> bds=null;
	private ArrayList<Button> buttons=new ArrayList<Button>();
	private Shell shell_create;
	
	public static final String ID="org.unice.javascool.folders.command.detruire";


	@Override
	public Object execute(ExecutionEvent arg0) throws ExecutionException {
		shell_create = new Shell(SWT.DIALOG_TRIM );
		Wizard wizard=new Wizard(){

			@Override
			public boolean canFinish(){
				return true;
			}

			@Override
			public boolean performFinish() {
				for(Button bt:buttons) 
					if(bt.getSelection())
						bds.remove(bt.getData());
				buttons.clear();
				return true;
			}
			
			@Override
			public boolean performCancel() {
				buttons.clear();
				return true;
			}
		};
		WizardSelectionPage wizardPage1=new WizardSelectionPage("Destruction"){
			public void createControl(Composite parent) {
				Composite composite = new Composite(parent, SWT.NONE);
				GridLayout gl = new GridLayout(2,true);
				composite.setLayout(gl);

				Label enteteLabel = new Label(composite, SWT.NULL);
				enteteLabel.setLayoutData(new GridData(SWT.CENTER,SWT.CENTER,true,true,2,0));
				enteteLabel.setText("Selectionner le(s) repertoire(s) a detruire:");
				try {
					bds=BeanFactory.getBeanDirectories(BeanFactory.repConfFile);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ConfException e) {
					e.printStackTrace();
				}
				for(BeanDirectories bd:bds){
					Label label = new Label(composite, SWT.NULL);
					label.setText(bd.getNom());
					GridData gd=new GridData();
					gd.horizontalAlignment=SWT.RIGHT;
					gd.verticalAlignment=SWT.CENTER;
					label.setLayoutData(gd);
					Button check = new Button(composite, SWT.CHECK);
					check.setData(bd);
					buttons.add(check);
					gd=new GridData(20,20);
					gd.horizontalAlignment=SWT.CENTER;
					gd.verticalAlignment=SWT.CENTER;
					check.setLayoutData(gd);
				}
				composite.pack();
				setControl(composite);
			}
		};
		wizard.addPage(wizardPage1);
		WizardDialog dialog=new WizardDialog(shell_create,wizard);
		dialog.open();
		return null;
	}

}
