package org.unice.javascool.folders.command;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.jface.wizard.WizardSelectionPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.unice.javascool.conf.BeanDirectories;
import org.unice.javascool.conf.BeanFactory;
import org.unice.javascool.conf.ConfException;

public class CommandAjout extends AbstractHandler {

	public final static String ID="org.unice.javascool.folders.command.ajout";
	private Text nameText=null;
	private Text pathText=null;

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		final Shell shell_create = new Shell(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),SWT.DIALOG_TRIM );
		shell_create.setSize(600,200);

		final Wizard wizard=new Wizard(){
			
			@Override
			public boolean canFinish(){
				File dir=new File(pathText.getText());
				boolean directory=dir.isDirectory();
				return pathText!=null && 
					   !(pathText.getText().length()==0) &&
					   nameText!=null &&
					   !(nameText.getText().length()==0) &&
					   directory;
			}
			
			@Override
			public boolean performFinish() {
				ArrayList<BeanDirectories> bds=null;
				try {
					bds=BeanFactory.getBeanDirectories(BeanFactory.repConfFile);
					BeanDirectories newBean=new BeanDirectories();
					newBean.setNom(nameText.getText());
					newBean.setPath(new Path(pathText.getText()));
					bds.add(newBean);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ConfException e) {
					e.printStackTrace();
				}
				return true;
			}
		};
		WizardSelectionPage wizardPage1=new WizardSelectionPage("Ajout"){
			public void createControl(Composite parent) {
				Composite composite = new Composite(parent, SWT.NONE);
				GridLayout gl = new GridLayout(3, false);
				composite.setLayout(gl);
				composite.setFont(parent.getFont());
				GridData gd = new GridData(GridData.FILL_HORIZONTAL);
				gd.horizontalSpan = 1;
				
				Label pathLabel = new Label(composite, SWT.NULL);
				pathLabel.setText("emplacement \ndu repertoire: ");
				pathText = new Text(composite, SWT.SINGLE | SWT.BORDER);
				pathText.addModifyListener(new ModifyListener(){
					public void modifyText(ModifyEvent e) {
						wizard.getContainer().updateButtons();
					}
				});
				pathText.setLayoutData(gd);
				Button browsePath = new Button(composite, SWT.PUSH);
				browsePath.setText("Chercher");
				browsePath.setAlignment(SWT.CENTER);
				browsePath.addSelectionListener(
					new SelectionAdapter() {
						public void widgetSelected(SelectionEvent e) {
							String directoryName;
							DirectoryDialog dialog = new DirectoryDialog(shell_create);
							directoryName = dialog.open();
							pathText.setText(directoryName);
							nameText.setText(directoryName.substring(directoryName.lastIndexOf(File.separator)+1,directoryName.length()));
						}
					}
				);
				
				Label nameLabel = new Label(composite, SWT.NULL);
				nameLabel.setText("nom du repertoire: ");
				gd=new GridData(GridData.FILL_HORIZONTAL);
				gd.horizontalSpan=1;
				nameText = new Text(composite, SWT.SINGLE | SWT.BORDER);
				nameText.addModifyListener(new ModifyListener(){
					public void modifyText(ModifyEvent e) {
						wizard.getContainer().updateButtons();
					}
				});
				nameText.setLayoutData(gd);
				setControl(composite);
			}
			
		};
		wizard.addPage(wizardPage1);
		WizardDialog dialog=new WizardDialog(shell_create,wizard);
		dialog.open();
		return null;
	}
}
