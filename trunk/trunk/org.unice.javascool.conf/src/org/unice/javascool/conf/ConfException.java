package org.unice.javascool.conf;

@SuppressWarnings("serial")
public class ConfException extends Exception {
	
	public ConfException(){
		super();
	}
	
	public ConfException(String mess){
		super(mess);
	}
}
