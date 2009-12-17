/*
 * Copyright (c) 2008-2010 Javascool (Java's Cool).  All rights reserved.
 *	this source file is placed under license CeCILL
 * see http://www.cecill.info/licences/Licence_CeCILL_V2-fr.html
 * or http://www.cecill.info/licences/Licence_CeCILL_V2-en.html
 */
package org.javascool.conf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * to hold info about macros
 * @author Unice
 *
 */
public class BeanMacros extends BeanTools {

	/** the translation for that macro (to complete jvs code) */
	private String traduction=null;

	private static HashMap<String,ArrayList<BeanMacros>> beans=new HashMap<String,ArrayList<BeanMacros>>();

	/**
	 * default constructor to allow introspection allocation
	 */
	public BeanMacros(){}

	/**
	 * to get macros' info from a conf file
	 * @param file the conf file
	 * @return arraylist filled up with info
	 * @throws IOException
	 * @throws ConfException
	 */
	static ArrayList<BeanMacros> getBeanMacros(String file) throws IOException, ConfException{
		ArrayList<BeanMacros> res=beans.get(file);
		if(res==null){
			BeanMacros temp=new BeanMacros();
			beans.put(file,(ArrayList<BeanMacros>) temp.init(file));
			return beans.get(file);
		}else return res;
	}

	@Override
	/**
	 * to save beanmacros' info
	 */
	public void save(Document root){
		Element head=null;
		NodeList heads=root.getElementsByTagName("macros");
		if(heads.getLength()==0) {
			head=root.createElement("macros");
			root.getDocumentElement().appendChild(head);
		}else head=(Element) heads.item(0);
		Element myElement=null;
		NodeList ne=head.getElementsByTagName(getNom());
		if(ne.getLength()>0) myElement=(Element) ne.item(0);
		else myElement=root.createElement(getNom());
		myElement.setAttribute("traduction",getTraduction().replace("\"","\\\""));
		myElement.setAttribute("signature",getSignature().replace("\"","\\\""));
		head.appendChild(myElement);
	}

	/**
	 * traduction's getter
	 * @return this bean traduction for the macro
	 */
	public String getTraduction() {
		return traduction;
	}

	/**
	 * traduction's setter
	 * @param traduction the new traduction for that macro
	 */
	public void setTraduction(String traduction) {
		this.traduction = traduction;
	}

	@Override
	/**
	 * to get usefull pieces of informations from the xml root struct
	 */
	protected ArrayList<BeanMacros> workOnIt(Element root) {
		ArrayList<BeanMacros> res=new ArrayList<BeanMacros>();
		NodeList macrs=root.getElementsByTagName("macros");
		if(macrs.getLength()>0){
			for(int i=0;i<macrs.getLength();i++){
				Node macr=macrs.item(i);
				if(macr instanceof Element){
					NodeList nl=macr.getChildNodes();
					for(int k=0;k<nl.getLength();k++){
						Node n=nl.item(k);
						if(n instanceof Element){
							NamedNodeMap nnm=n.getAttributes();
							Attr traduction=(Attr)nnm.getNamedItem("traduction");
							Attr signature=(Attr)nnm.getNamedItem("signature");
							BeanMacros newBean=new BeanMacros();
							newBean.setNom(n.getNodeName());
							newBean.setSignature(signature.getValue());
							newBean.setTraduction(traduction.getValue());
							res.add(newBean);
						}
					}
				}
			}
		}
		return res;
	}

	public static void saveAll() {
		Set<String> cles = beans.keySet();
		Iterator<String> it = cles.iterator();
		while (it.hasNext()){
			String cle = it.next(); 
			Document root=null;
			try {
				root=refresh(initSave(cle));
			} catch (ConfException e) {
				e.printStackTrace();
			}
			ArrayList<BeanMacros> toSave = beans.get(cle); 
			for(BeanMacros bd : toSave) bd.save(root);
			AbstractBean.persists(root,cle);
		}
	}

	private static Document refresh(Document root) {
		Element el=root.getDocumentElement();
		NodeList heads=root.getElementsByTagName("macros");
		for(int i=0;i<heads.getLength();i++) el.removeChild(heads.item(i));
		el.appendChild(root.createElement("macros"));
		return root;
	}

}
