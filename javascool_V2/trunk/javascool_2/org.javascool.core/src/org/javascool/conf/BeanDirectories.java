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

import org.eclipse.core.runtime.Path;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * 
 * @author Unice
 * class to hold info about directories
 *
 */
public class BeanDirectories extends AbstractBean{
	/** the path to the favorite directory*/
	private Path path=null;
	/** the name of the directory */
	private String nom=null;
	/** the id of the info */
	private String id=null;

	private static int num=1;

	private static HashMap<String,ArrayList<BeanDirectories>> beans=new HashMap<String,ArrayList<BeanDirectories>>();

	/** default constructor allowing intospection declaration */
	public BeanDirectories(){
		setId("id"+num);
		num++;
	}

	/** 
	 * name's getter
	 * @return the name of the directory
	 */
	public String getNom() {
		return nom;
	}

	/**
	 * name's setter
	 * @param nom the new name
	 */
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	 * path's getter
	 * @return the pass of the folder
	 */
	public String getPath() {
		return path.toOSString();
	}

	/**
	 * path's setter
	 * @param path2 the new path
	 */
	public void setPath(Path path2) {
		this.path = path2;
	}

	/**
	 * To get singleton arraylist of bean directories 
	 * @param file the configuration's file
	 * @return an arraylist full of bean directories for information
	 * @throws IOException
	 * @throws ConfException
	 */
	static ArrayList<BeanDirectories> getBeanDirectories(String file) throws IOException, ConfException{
		ArrayList<BeanDirectories> res=beans.get(file);
		if(res==null){
			num--;
			BeanDirectories temp=new BeanDirectories();
			beans.put(file,(ArrayList<BeanDirectories>) temp.init(file));
			return beans.get(file);
		}else return res;
	}

	/**
	 * to save a bean directory information
	 */
	@Override
	protected void save(Document root) {
		Element head=null;
		NodeList heads=root.getElementsByTagName("repertoires");
		head=(Element) heads.item(0);
		Element myElement=root.createElement(getId());
		myElement.setAttribute("path",path.toOSString().replace("\\","\\\\").replace(" ","\\ "));
		myElement.setAttribute("nom",getNom().replace("\"","\\\""));
		head.appendChild(myElement);
	}

	/**
	 * to hold goods pieces of informations from root element (xml)
	 */
	@Override
	protected ArrayList<? extends AbstractBean> workOnIt(Element root) {
		ArrayList<BeanDirectories> res=new ArrayList<BeanDirectories>();
		NodeList reps=root.getElementsByTagName("repertoires");
		if(reps.getLength()>0){
			for(int i=0;i<reps.getLength();i++){
				Node rep=reps.item(i);
				if(rep instanceof Element){
					NodeList nl=rep.getChildNodes();
					for(int k=0;k<nl.getLength();k++){
						Node n=nl.item(k);
						if(n instanceof Element){
							NamedNodeMap nnm=n.getAttributes();
							Attr path=(Attr)nnm.getNamedItem("path");
							Attr name=(Attr)nnm.getNamedItem("nom");
							BeanDirectories newBean=new BeanDirectories();
							newBean.setNom(name.getValue());
							newBean.setPath(new Path(path.getValue()));
							newBean.setId(n.getNodeName());
							res.add(newBean);
						}
					}
				}
			}
		}
		return res;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
		String num=id.replace("id","");
		try{
			int maxNum=new Integer(Integer.parseInt(num)).intValue();
			if(maxNum>BeanDirectories.num) BeanDirectories.num=maxNum+1;
		}catch(Exception e){}
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
			ArrayList<BeanDirectories> toSave = beans.get(cle); 
			for(BeanDirectories bd : toSave) bd.save(root);
			AbstractBean.persists(root,cle);
		}
	}

	private static Document refresh(Document root) {
		Element el=root.getDocumentElement();
		NodeList heads=root.getElementsByTagName("repertoires");
		for(int i=0;i<heads.getLength();i++) el.removeChild(heads.item(i));
		el.appendChild(root.createElement("repertoires"));
		return root;
	}
}
