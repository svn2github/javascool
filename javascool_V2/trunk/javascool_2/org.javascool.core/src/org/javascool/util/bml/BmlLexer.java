package org.javascool.util.bml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PushbackReader;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Stack;

public class BmlLexer{

	private PushbackReader bf;
	private Stack<BmlLexeme> myStack;
	private int line;

	BmlLexer(File aFile){
		line=1;
		myStack=new Stack<BmlLexeme>();
		try {
			bf=new PushbackReader(new FileReader(aFile));
			workOnIt();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (BmlLexerException e) {
			e.printStackTrace();
		}
	}

	BmlLexeme popLexeme(){
		try{
			return myStack.pop();
		}catch(EmptyStackException e){
			return new BmlLexeme("",BmlLexeme.EOF,line);
		}
	}

	BmlLexeme getCurrentLexeme(){
		try{
			return myStack.peek();
		}catch(EmptyStackException e){
			return new BmlLexeme("",BmlLexeme.EOF,line);
		}
	}

	void pushLexeme(BmlLexeme b){
		myStack.push(b);
	}

	private void workOnIt() throws BmlLexerException{
		ArrayList<BmlLexeme> myArrayList=new ArrayList<BmlLexeme>();
		try {
			int ch;
			while((ch=bf.read())>=0){
				if(Character.isWhitespace(ch)){
					if((char)ch=='\n') line++;
					continue;
				}
				switch((char)ch){
				case '"': myArrayList.add(new BmlLexeme(""+(char)ch,BmlLexeme.QUOT,line));
				myArrayList.add(new BmlLexeme(getQuotedString(),BmlLexeme.STRING,line));
				myArrayList.add(new BmlLexeme(""+(char)bf.read(),BmlLexeme.QUOT,line));
				break;
				case ';': myArrayList.add(new BmlLexeme(""+(char)ch,BmlLexeme.SCOL,line));
				break;
				case '=': myArrayList.add(new BmlLexeme(""+(char)ch,BmlLexeme.EQU,line));
				break;
				case '{': myArrayList.add(new BmlLexeme(""+(char)ch,BmlLexeme.LBRA,line));
				break;
				case '}': myArrayList.add(new BmlLexeme(""+(char)ch,BmlLexeme.RBRA,line));
				break;
				default:{ 					
					bf.unread(ch);
					String res=getString();
					myArrayList.add(new BmlLexeme(res,BmlLexeme.STRING,line));
				}
				}
			}
			myArrayList.add(new BmlLexeme("",BmlLexeme.EOF,line));
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		for(int i=myArrayList.size()-1;i>=0;i--) myStack.push(myArrayList.get(i));
	}

	private String getQuotedString() {
		StringBuffer sb=new StringBuffer();
		int ch;
		try {
			while((ch=bf.read())!='"') 
				{
					if(ch=='\\') ch=bf.read();
					sb.append((char)ch);
				}
			bf.unread(ch);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	private String getString() {
		StringBuffer sb=new StringBuffer();
		int ch;
		try {
			while((ch=bf.read())!='{' && ch!='"' && ch!='=') sb.append((char)ch);
			bf.unread(ch);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString().trim();
	}
}