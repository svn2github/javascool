package org.javascool.ui.editor.editors;

import org.eclipse.jface.text.rules.IWhitespaceDetector;

public class JVSWhitespaceDetector implements IWhitespaceDetector {

	public boolean isWhitespace(char c) {
		return (c == ' ' || c == '\t' || c == '\n' || c == '\r');
	}
}
