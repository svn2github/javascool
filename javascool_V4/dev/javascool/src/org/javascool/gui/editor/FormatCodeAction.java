package org.javascool.gui.editor;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JTextArea;

import java.awt.event.ActionEvent;

import org.javascool.core.JvsBeautifier;

public class FormatCodeAction extends AbstractAction {

	private static final long serialVersionUID = -1107044277364712002L;

	private final JTextArea textArea;

	public FormatCodeAction(JTextArea jta) {
		super();
		putValue(Action.NAME, "Formater le code");
		textArea = jta;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		textArea.setText(JvsBeautifier.run(textArea.getText()));
	}

}
