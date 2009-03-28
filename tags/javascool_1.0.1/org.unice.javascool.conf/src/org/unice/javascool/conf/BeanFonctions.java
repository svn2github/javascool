package org.unice.javascool.conf;

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
 * to hold pieces of information about functions
 * @author Unice
 *
 */
public class BeanFonctions extends BeanTools {

	/** the type of the function */
	private String type=null;
	/** package of that function */
	private String imp=null;
	/** this function's description */
	private String desc=null;

	private static HashMap<String,ArrayList<BeanFonctions>> beans=new HashMap<String,ArrayList<BeanFonctions>>();

	/**
	 * to get all functions'info from a file
	 * @param file the conf file
	 * @return an arraylist full of beanfunctions with info
	 * @throws IOException
	 * @throws ConfException
	 */
	static ArrayList<BeanFonctions> getBeanFonctions(String file) throws IOException, ConfException{
		ArrayList<BeanFonctions> res=beans.get(file);
		if(res==null){
			BeanFonctions temp=new BeanFonctions();
			beans.put(file,(ArrayList<BeanFonctions>) temp.init(file));
			return beans.get(file);
		}else return res;
	}

	@Override
	/**
	 * to get goods info from the xml dom struct root
	 */
	protected ArrayList<BeanFonctions> workOnIt(Element root){
		ArrayList<BeanFonctions> res=new ArrayList<BeanFonctions>();
		NodeList funcs=root.getElementsByTagName("fonctions");
		if(funcs.getLength()>0){
			for(int i=0;i<funcs.getLength();i++){
				Node func=funcs.item(i);
				if(func instanceof Element){
					NodeList nl=func.getChildNodes();
					for(int k=0;k<nl.getLength();k++){
						Node n=nl.item(k);
						if(n instanceof Element){
							NamedNodeMap nnm=n.getAttributes();
							Attr type=(Attr)nnm.getNamedItem("type");
							Attr imp=(Attr)nnm.getNamedItem("import");
							Attr signature=(Attr)nnm.getNamedItem("signature");
							Attr desc=(Attr)nnm.getNamedItem("desc");
							BeanFonctions newBean=new BeanFonctions();
							newBean.setNom(n.getNodeName());
							newBean.setType(type.getValue());
							newBean.setImport(imp.getValue());
							newBean.setSignature(signature.getValue());
							newBean.setDesc(desc.getValue());
							res.add(newBean);
						}
					}
				}
			}
		}
		return res;
	}

	/**
	 * default constructor to allow introspection allocation
	 */
	public BeanFonctions(){}

	@Override
	/**
	 * to save bean's info in the xml struct
	 */
	public void save(Document root){
		Element head=null;
		NodeList heads=root.getElementsByTagName("fonctions");
		if(heads.getLength()==0) {
			head=root.createElement("fonctions");
			root.getDocumentElement().appendChild(head);
		}else head=(Element) heads.item(0);
		Element myElement=null;
		NodeList ne=head.getElementsByTagName(getNom());
		if(ne.getLength()>0) myElement=(Element) ne.item(0);
		else myElement=root.createElement(getNom());
		myElement.setAttribute("type",getType().replace("\"","\\\""));
		myElement.setAttribute("import",getImport().replace("\"","\\\""));
		myElement.setAttribute("signature",getSignature().replace("\"","\\\""));
		myElement.setAttribute("desc",getDesc().replace("\"","\\\""));
		head.appendChild(myElement);
	}

	/**
	 * desc's getter
	 * @return the desc of that function
	 */
	public String getDesc() {
		return desc;
	}
	/**
	 * desc setter
	 * @param desc the new function desc
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}

	/**
	 * import's getter 
	 * @return the package of that function
	 */
	public String getImport() {
		return imp;
	}

	/**
	 * import's setter
	 * @param imp the new package
	 */
	public void setImport(String imp) {
		this.imp = imp;
	}

	/**
	 * the type's getter
	 * @return the type of the function
	 */
	public String getType() {
		return type;
	}
	/**
	 * the type's setter 
	 * @param type the new type
	 */
	public void setType(String type) {
		this.type = type;
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
			ArrayList<BeanFonctions> toSave = beans.get(cle); 
			for(BeanFonctions bd : toSave) bd.save(root);
			AbstractBean.persists(root,cle);
		}
	}

	private static Document refresh(Document root) {
		Element el=root.getDocumentElement();
		NodeList heads=root.getElementsByTagName("fonctions");
		for(int i=0;i<heads.getLength();i++) el.removeChild(heads.item(i));
		el.appendChild(root.createElement("fonctions"));
		return root;
	}
}