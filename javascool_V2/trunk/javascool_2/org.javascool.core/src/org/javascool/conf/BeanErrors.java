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
 * 
 * @author Unice
 * class holding info about errors
 */
public class BeanErrors extends AbstractBean {

	/** to generate id an automatic way */
	private static int num=1;
	/** error's id */
	private String id=null;
	/** java error */
	private String java=null;
	/** the translation of the error */
	private String traduction=null;

	private static HashMap<String,ArrayList<BeanErrors>> beans=new HashMap<String,ArrayList<BeanErrors>>();

	/** default constructor to allow dynamic introspection allocation */
	public BeanErrors(){
		setId("id"+num);
		num++;
	}

	/**
	 * to get pieces information about errors from the File file
	 * @param file the configuration File
	 * @return all the bean errors holding information from the file
	 * @throws IOException
	 * @throws ConfException
	 */
	public static ArrayList<BeanErrors> getBeanErrors(String file) throws IOException, ConfException{
		ArrayList<BeanErrors> res=beans.get(file);
		if(res==null){
			num--;
			BeanErrors temp=new BeanErrors();
			beans.put(file,(ArrayList<BeanErrors>) temp.init(file));
			return beans.get(file);
		}else return res;
	}

	@Override
	/**
	 * to save info from the bean in a xml dom struct
	 * @param root the conf doc root
	 */
	protected void save(Document root) {
		Element head=null;
		NodeList heads=root.getElementsByTagName("erreurs");
		if(heads.getLength()>0) head=(Element) heads.item(0);
		else{
			head=root.createElement("erreurs");
			root.getDocumentElement().appendChild(head);
		}
		Element myElement=null;
		NodeList me=head.getElementsByTagName(getId());
		if(me.getLength()>0) myElement=(Element) me.item(0);
		else myElement=root.createElement(getId());	
		myElement.setAttribute("java", getJava().replace("\"","\\\""));
		myElement.setAttribute("traduction",getTraduction().replace("\"","\\\""));
		head.appendChild(myElement);
	}

	@Override
	/**
	 * to get goods pieces of information from the xml dom struct
	 * @param root the struct holding info about errors
	 * @return arraylist of usefull beanerrors with info
	 */
	protected ArrayList<BeanErrors> workOnIt(Element root) {
		ArrayList<BeanErrors> res=new ArrayList<BeanErrors>();
		NodeList erreurs=root.getElementsByTagName("erreurs");
		if(erreurs.getLength()>0){
			for(int i=0;i<erreurs.getLength();i++){
				Node erreur=erreurs.item(i);
				if(erreur instanceof Element){
					NodeList nl=erreur.getChildNodes();
					for(int k=0;k<nl.getLength();k++){
						Node n=nl.item(k);
						if(n instanceof Element){
							NamedNodeMap nnm=n.getAttributes();
							Attr java=(Attr)nnm.getNamedItem("java");
							Attr traduction=(Attr)nnm.getNamedItem("traduction");
							BeanErrors newBean=new BeanErrors();
							newBean.setJava(java.getValue());
							newBean.setTraduction(traduction.getValue());
							newBean.setId(n.getNodeName());
							res.add(newBean);
						}
					}
				}
			}
		}
		return res;
	}

	/**
	 * java error's getter
	 * @return this bean's error
	 */
	public String getJava() {
		return java;
	}

	/**
	 * java error's setter
	 * @param java the new java error
	 */
	public void setJava(String java) {
		this.java = java;
	}

	/**
	 * traduction's getter
	 * @return this bean's traduction
	 */
	public String getTraduction() {
		return traduction;
	}

	/**
	 * traduction's setter
	 * @param traduction the new traduction
	 */
	public void setTraduction(String traduction) {
		this.traduction = traduction;
	}

	/**
	 * id's getter
	 * @return the id of this bean
	 */
	public String getId() {
		return id;
	}

	/**
	 * id's setter
	 * @param id the new id
	 */
	private void setId(String id) {
		this.id = id;
		String num=id.replace("id","");
		try{
			int maxNum=new Integer(Integer.parseInt(num)).intValue();
			if(maxNum>BeanErrors.num) BeanErrors.num=maxNum+1;
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
			ArrayList<BeanErrors> toSave = beans.get(cle); 
			for(BeanErrors bd : toSave) bd.save(root);
			AbstractBean.persists(root,cle);
		}
	}

	private static Document refresh(Document root) {
		Element el=root.getDocumentElement();
		NodeList heads=root.getElementsByTagName("erreurs");
		for(int i=0;i<heads.getLength();i++) el.removeChild(heads.item(i));
		el.appendChild(root.createElement("erreurs"));
		return root;
	}

}
