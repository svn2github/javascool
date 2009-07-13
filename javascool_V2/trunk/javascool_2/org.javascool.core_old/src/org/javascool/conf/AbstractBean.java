package org.javascool.conf;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.javascool.util.bml.BmlConverter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;



/**
 * @author Unice
 * Abstract class implementing bases functions of our beans info
 */
public abstract class AbstractBean{

	/**
	 * To Initialize the work for the subclasses
	 * @param file with bml or xml format holding the conf
	 * @return an arraylist with the bean objects containing pieces of information
	 * @throws ConfException if invalid file format
	 */
	protected ArrayList<? extends AbstractBean> init(String file) throws ConfException {
		File xmlConf=null;
		if(file.endsWith(".xml")){
			xmlConf=new File(file);
		}else if(file.endsWith(".bml")){
			File bmlFile=new File(file);
			xmlConf=new File(file.replace(".bml",".xml"));
			BmlConverter.bmlToXml(bmlFile,xmlConf);
		}else throw new ConfException("Invalid File Format");
		DocumentBuilderFactory df=DocumentBuilderFactory.newInstance();
		DocumentBuilder db;
		Document docElem=null;
		try {
			db = df.newDocumentBuilder();
			docElem=db.parse(xmlConf);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Element root=docElem.getDocumentElement();
		return workOnIt(root);
	}

	/**
	 * To allow the subclasses to get usefull information
	 * @param root the root Document element (xml or bml)
	 * @return the list holding all the beans info
	 */
	protected abstract ArrayList<? extends AbstractBean> workOnIt(Element root);

	/**
	 * To save a configuration
	 * @param root the root of the doc
	 */
	protected abstract void save(Document root);

	/**
	 * To save beans holding pieces of information.
	 * @param al the arraylist of bean info, is destructed
	 * @param file the dest file
	 * @param append true -> overriding already here conf, adding new one.<br />false -> writing a brand new conf file 
	 * @throws ConfException if invalide file format
	 */
	public static Document initSave(String file) throws ConfException{
		Document root=null;
		File xmlFile=null;
		File bmlFile=null;
		if(file.endsWith(".xml")){
			xmlFile=new File(file);
			bmlFile=new File(file.replace(".xml",".bml"));
		}else{
			bmlFile=new File(file);
			xmlFile=new File(file.replace(".bml",".xml"));
			BmlConverter.bmlToXml(bmlFile,xmlFile);
		}
		try{
			DocumentBuilderFactory dFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder db=dFactory.newDocumentBuilder();
			root=db.parse(xmlFile);
		}catch(ParserConfigurationException e){
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return root;
	}

	public static void persists(Document root, String file) {
		File xmlFile=null;
		File bmlFile=null;
		if(file.endsWith(".xml")){
			xmlFile=new File(file);
			bmlFile=new File(file.replace(".xml",".bml"));
		}else{
			bmlFile=new File(file);
			xmlFile=new File(file.replace(".bml",".xml"));
		}
		BmlConverter.transform(root,xmlFile);
		BmlConverter.xmlToBml(xmlFile,bmlFile);
	}
}