package fr.unice.util.bml;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class BmlConverter {

	private BmlConverter(){
	}

	public static void bmlToXml(File from,File dest){
		BmlParser bmlp=new BmlParser(from);
		Document res=null;
		try{
			res=bmlp.BmlToXml();
		}catch(BmlConverterException e){
			e.printStackTrace();
		}
		transform(res,dest);
	}

	public static void xmlToBml(File from,File dest){
		DocumentBuilderFactory df=DocumentBuilderFactory.newInstance();
		DocumentBuilder db;
		Document root;
		PrintWriter pw=null;
		try {
			db = df.newDocumentBuilder();
			root=db.parse(from);
			pw=new PrintWriter(new BufferedWriter(new FileWriter(dest)));
			processXmlToBml(root.getDocumentElement(),pw,0);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(pw!=null)
				pw.close();
		}
	}

	private static void processXmlToBml(Element doc, PrintWriter writer,int ident) {
		if(doc==null) return;
		for(int i=ident;i>0;i--)writer.print("\t");
		writer.println(doc.getNodeName()+" {");
		NamedNodeMap nnm=doc.getAttributes();
		if(nnm!=null)
			for(int i=0;i<nnm.getLength();i++){
				for(int j=ident+1;j>0;j--)writer.print("\t");
				Attr a=(Attr) nnm.item(i);
				writer.println(a.getName()+"=\""+a.getValue()+"\";");
			}
		Node n=doc.getFirstChild();
		while(n!=null){
			if(n instanceof Element)
				processXmlToBml((Element)n, writer, ident+1);
			if(n instanceof Text)
				processXmltoText((Text)n,writer,ident+1);
			n=n.getNextSibling();
		}
		for(int i=ident;i>0;i--)writer.print("\t");
		writer.println("}");
	}

	private static void processXmltoText(Text text, PrintWriter writer, int ident) {
		String t=text.getTextContent().trim();
		if(t.length()==0) return;
		for(int i=ident;i>0;i--) writer.print("\t");
		writer.println("text{");
		for(int i=ident;i>0;i--) writer.print("\t");
		writer.println(t);
		for(int i=ident;i>0;i--) writer.print("\t");
		writer.println("}");
	}

	public static void transform(Document d,File file){
		TransformerFactory tf=TransformerFactory.newInstance();
		Transformer t=null;
		StreamResult res=null;
		try {
			t = tf.newTransformer();
			res=new StreamResult(new FileWriter(file));
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		t.setOutputProperty(OutputKeys.INDENT, "yes");
		t.setOutputProperty(OutputKeys.METHOD, "xml");
		t.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
		t.setOutputProperty(OutputKeys.STANDALONE,"yes");
		try {
			t.transform(new DOMSource(d),res);
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		finally{
			try {
				if(res!=null) res.getWriter().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
