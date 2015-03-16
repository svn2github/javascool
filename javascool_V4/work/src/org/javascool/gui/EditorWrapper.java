package org.javascool.gui;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.fife.ui.rtextarea.RTextArea;

/**
 * Cette classe est utilisee par la proglet PluriAlgo
*/ 
public class EditorWrapper {
	
	private static JVSFileTabs tabs = JVSFileTabs.getInstance();
	
	public static String getText() {
		String fileId = tabs.getCurrentFileId(); 
		return tabs.getEditor(fileId).getText();
	}
	
	public static void setText(String txt) {
		String fileId = tabs.getCurrentFileId(); 
		tabs.getEditor(fileId).setText(txt);
	}
	
	public static RTextArea getRTextArea() {
		String fileId = tabs.getCurrentFileId(); 
		return tabs.getEditor(fileId).getRTextArea();
	}
	
	public static Map<String,String> getOthers() {
		TreeMap<String,String> others = new TreeMap<String,String>();
		Iterator<String> iter = JVSFileTabs.fileIds.keySet().iterator();
		while (iter.hasNext()) {
			String nom_fich = iter.next();
			String id_fich = JVSFileTabs.fileIds.get(nom_fich);
			if (id_fich.equals(tabs.getCurrentFileId())) continue;
			String contenu = tabs.getEditor(id_fich).getText();
			others.put(nom_fich, contenu);
		}
		return others;
	}

}
