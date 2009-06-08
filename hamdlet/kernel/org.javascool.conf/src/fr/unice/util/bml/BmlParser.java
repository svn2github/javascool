package fr.unice.util.bml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.io.File;

public class BmlParser {

	private BmlLexer lex;
	private String file;
	private Document root;

	BmlParser(File aFile){
		lex=new BmlLexer(aFile);
		file=aFile.getName();
	}

	Document BmlToXml() throws BmlConverterException{
		try{
			DocumentBuilderFactory dFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder db=dFactory.newDocumentBuilder();
			root=db.newDocument();
		}catch(ParserConfigurationException e){
			e.printStackTrace();
		}
		isStructWellFormed(null);
		BmlLexeme current=lex.popLexeme();
		if(current.getType()!=BmlLexeme.EOF) throw new BmlConverterException(file,current.getLine(),"EOF",current.getValue());
		/*BmlLexeme current=lex.popLexeme();
		while(true){
			if(current.getType()==BmlLexeme.EOF) break;
			System.out.println(current.getValue()+" "+current.getType());
			current=lex.popLexeme();
		}*/
		return root;
	}
	private void isStructWellFormed(Element n) throws BmlConverterException {
		Element first=null;
		BmlLexeme current=lex.popLexeme();
		if(current.getType()==BmlLexeme.RBRA){
			lex.pushLexeme(current);
			return;
		}
		BmlLexeme next=lex.popLexeme();
		if(current.getType()!=BmlLexeme.STRING) throw new BmlConverterException(file,current.getLine(),"Une String",current.getValue());
		if(next.getType()!=BmlLexeme.LBRA) throw new BmlConverterException(file,next.getLine(),"{",next.getValue());
		first=root.createElement(current.getValue());
		if(n!=null) n.appendChild(first);
		else root.appendChild(first);
		isAttributWellFormed(first);
		BmlLexeme bmlb=lex.getCurrentLexeme();
		while(bmlb.getType()!=BmlLexeme.RBRA){
			isStructWellFormed(first);
			bmlb=lex.getCurrentLexeme();
		}
		bmlb=lex.popLexeme();
		if(bmlb.getType()!=BmlLexeme.RBRA) throw new BmlConverterException(file,bmlb.getLine(),"}",bmlb.getValue());

	}

	private void isAttributWellFormed(Element n) throws BmlConverterException {
		BmlLexeme bmlString=lex.popLexeme();
		BmlLexeme bmlEqu=lex.popLexeme();
		if(bmlString.getType()==BmlLexeme.RBRA || bmlEqu.getType()==BmlLexeme.LBRA){
			lex.pushLexeme(bmlEqu);
			lex.pushLexeme(bmlString);
			return;
		}
		BmlLexeme bmlQuot1=lex.popLexeme();
		BmlLexeme bmlAttr=lex.popLexeme();
		BmlLexeme bmlQuot2=lex.popLexeme();
		BmlLexeme bmlSemiColon=lex.popLexeme();
		if(bmlString.getType()!=BmlLexeme.STRING) throw new BmlConverterException(file,bmlString.getLine(),"Une String",bmlString.getValue());
		if(bmlEqu.getType()!=BmlLexeme.EQU) throw new BmlConverterException(file,bmlEqu.getLine(),"=",bmlEqu.getValue());
		if(bmlQuot1.getType()!=BmlLexeme.QUOT) throw new BmlConverterException(file,bmlQuot1.getLine(),"\"",bmlQuot1.getValue());
		if(bmlAttr.getType()!=BmlLexeme.STRING) throw new BmlConverterException(file,bmlAttr.getLine(),"Une String",bmlAttr.getValue());
		if(bmlQuot2.getType()!=BmlLexeme.QUOT)  throw new BmlConverterException(file,bmlQuot2.getLine(),"\"",bmlQuot2.getValue());
		if(bmlSemiColon.getType()!=BmlLexeme.SCOL)  throw new BmlConverterException(file,bmlSemiColon.getLine(),";",bmlSemiColon.getValue());
		n.setAttribute(bmlString.getValue(),bmlAttr.getValue());
		BmlLexeme current=lex.popLexeme();
		BmlLexeme next=lex.popLexeme();	
		lex.pushLexeme(next);
		lex.pushLexeme(current);
		if(current.getType()==BmlLexeme.STRING && (next.getType()==BmlLexeme.EQU)){
			isAttributWellFormed(n);
		}
	}
}
