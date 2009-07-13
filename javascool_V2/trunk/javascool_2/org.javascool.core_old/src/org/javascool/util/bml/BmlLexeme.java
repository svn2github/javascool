package org.javascool.util.bml;

public class BmlLexeme {
	public final static int STRING=1;
	public final static int EOF=-1;
	public final static int EQU=2;
	public final static int QUOT=3;
	public final static int SCOL=4;
	public final static int LBRA=5;
	public final static int RBRA=6;
	private String value;
	private int type;
	private int line;
	BmlLexeme(String v,int t,int l){
		value=v;
		type=t;
		line=l;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public int getLine() {
		return line;
	}
	public void setLine(int line) {
		this.line = line;
	}
}
