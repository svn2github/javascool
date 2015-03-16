package org.javascool.gui;

import javax.swing.AbstractAction;

import java.awt.event.ActionEvent;

import org.javascool.core.ProgletEngine;
import org.javascool.gui.editor.EditorKit;
import org.javascool.gui.editor.FileKit;
import org.javascool.widgets.Console;

/** Compile Action for all JVSFileReferance. */
public class CompileAction extends AbstractAction {

	private static final long serialVersionUID = -6432472821088070514L;

	private FileKit tabs;
	private boolean success;
	private EditorKit compiledEditor;

	public CompileAction() {
		// this.tabs=JVSEditorsPane.getInstance();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			tabs = JVSPanel.getInstance().getEditorTabs();
			if (tabs.saveCurrentFile() == false) {
				setSuccess(false);
				return;
			}
			JVSWidgetPanel.getInstance().focusOnConsolePanel();
			setCompiledEditor(tabs.getCurrentEditor());
			if (ProgletEngine.getInstance().doCompile(
					tabs.getCurrentEditor().getText())) {
				Console.getInstance().clear();
				System.out.println("Compilation réussie !");
				setSuccess(true);
			} else {
				setSuccess(false);
			}
			if (isSuccess()) {
				JVSToolBar.getInstance().enableStartStopButton();
			} else {
				JVSToolBar.getInstance().disableStartStopButton();
			}
		} catch (final Exception e1) {
			e1.printStackTrace();
		}
	}

	public EditorKit getCompiledEditor() {
		return compiledEditor;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setCompiledEditor(EditorKit compiledEditor) {
		this.compiledEditor = compiledEditor;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

}
