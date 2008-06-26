package org.unice.javascool.editor.editors;

import org.eclipse.jface.text.rules.*;



public class JVSPartitionScanner extends RuleBasedPartitionScanner {
	public final static String JAVA_COMMENT = "__JAVA_comment";

	public JVSPartitionScanner() {

		IToken javaComment = new Token(JAVA_COMMENT);
		
		IPredicateRule[] rules = new IPredicateRule[2];

		rules[0] = new MultiLineRule("/*", "*/", javaComment);
		rules[1] = new EndOfLineRule("//", javaComment);

		setPredicateRules(rules);
	}
}
