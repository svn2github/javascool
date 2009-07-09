package org.javascool.ui.editor.editors;

import org.eclipse.jface.text.rules.*;

public class JVSPartitionScanner extends RuleBasedPartitionScanner {
	public final static String JAVA_COMMENT = "__java_comment";
	//TODO remove by me public final static String XML_TAG = "__xml_tag";

	/*
	public JVSPartitionScanner() {

		IToken xmlComment = new Token(XML_COMMENT);
		//TODO remove by meIToken tag = new Token(XML_TAG);

		IPredicateRule[] rules = new IPredicateRule[2];

		rules[0] = new MultiLineRule("<!--", "-->", xmlComment);
		rules[1] = new TagRule(tag);

		setPredicateRules(rules);
	}*/
	
	public JVSPartitionScanner() {

		IToken javaComment = new Token(JAVA_COMMENT);
		
		IPredicateRule[] rules = new IPredicateRule[2];

		rules[0] = new MultiLineRule("/*", "*/", javaComment);
		rules[1] = new EndOfLineRule("//", javaComment);

		setPredicateRules(rules);
	}
}
