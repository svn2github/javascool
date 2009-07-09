package org.javascool.ui.editor.editors;

import org.eclipse.jface.text.*;
import org.eclipse.jface.text.rules.*;

public class JVSTagScanner extends RuleBasedScanner {

	public JVSTagScanner(ColorManager manager) {
		IToken string =
			new Token(
				new TextAttribute(manager.getColor(JVSColorConstants.STRING)));

		IRule[] rules = new IRule[3];

		// Add rule for double quotes
		rules[0] = new SingleLineRule("\"", "\"", string, '\\');
		// Add a rule for single quotes
		rules[1] = new SingleLineRule("'", "'", string, '\\');
		// Add generic whitespace rule.
		rules[2] = new WhitespaceRule(new JVSWhitespaceDetector());

		setRules(rules);
	}
}
