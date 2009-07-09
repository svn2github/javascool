package org.javascool.ui.editor.editors;


import org.eclipse.jface.text.source.DefaultCharacterPairMatcher;


public class JVSPairMatcher extends DefaultCharacterPairMatcher{

	final static char[] chars = new char[] {'(', ')', '{', '}', '[', ']','<','>'};

	
	public JVSPairMatcher() {
		super(chars);
	}


}
