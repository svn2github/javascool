package org.javascool.util.bml;

@SuppressWarnings("serial")
public class BmlLexerException extends Exception {
	
	public BmlLexerException(String tok){
		super("Token inconnu: "+tok);
	}

}
