/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2014.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.interaction2;

import java.io.File;

import javax.script.*;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.javascool.gui.EditorWrapper;
import org.javascool.proglets.plurialgo.divers.Divers;
import org.javascool.proglets.plurialgo.langages.xml.*;
import org.javascool.tools.FileManager;
import org.javascool.tools.UserConfig;


/**
 * Cette classe ajoute des menus à l'éditeur Javascool
 */
public class MenusEditeur {

	static ScriptEngineManager manager = null;		 
    static ScriptEngine engine = null;
    
	// ---------------------------------------------
	// Ajout des styles et des menus
	// ---------------------------------------------

	public static void  modifierStyles(PanelInteraction pInter) {
		RSyntaxTextArea editArea = (RSyntaxTextArea)EditorWrapper.getRTextArea();
		try {
			editArea.setCodeFoldingEnabled(true);
			if (pInter.isJavascript()) {
				editArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);
			}
			else if (pInter.isPython()) {
				editArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_PYTHON);
			}
			else if (pInter.isVb()) {
				editArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_DELPHI);
			}
			else if (pInter.isPhp()) {
				editArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_PHP);
			}
		} catch (Exception e) {
			editArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
		}
	}
	
	public static void addPopupMenu(PanelInteraction pInter) {
		final PanelInteraction inter = pInter;
		JPopupMenu popup = EditorWrapper.getRTextArea().getPopupMenu();
		//popup.addSeparator();
		JMenuItem menu;
		if (pInter.isJavascool()) {
			menu = new JMenuItem("Compiler+"); 
			menu.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					CompilateurJVS.doCompile(inter,false);
				}
			});
			popup.add(menu);
		}
		if (pInter.isJavascript()) {
			menu = new JMenuItem("Web"); 
			menu.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					doJavascript(inter);
				}
			});
			popup.add(menu);
		}
		if (pInter.isAlgobox()) {
			menu = new JMenuItem("Algobox"); 
			menu.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					doAlgobox(inter);
				}
			});
			popup.add(menu);
			menu = new JMenuItem("Affectation"); 
			menu.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					int indent = Divers.getIndent(EditorWrapper.getRTextArea());
					String tabus = tabuler(indent);
					String txt = tabus + "var PREND_LA_VALEUR ..."; 
					Divers.inserer(EditorWrapper.getRTextArea(), txt);
				}
			});
			popup.add(menu);
		}
		if (pInter.isPython()) {
			menu = new JMenuItem("Python"); 
			menu.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					doPython(inter);
				}
			});
			popup.add(menu);
		}
		if (pInter.isCarmetal()) {
			menu = new JMenuItem("CarMetal"); 
			menu.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					doCarmetal(inter);
				}
			});
			popup.add(menu);
		}
	}
		
	public static void addToolBar(PanelInteraction pInter) {
		// final PanelInteraction inter = pInter;
		// boutons
		/*
		Desktop.getInstance().getToolBar().addTool("Exporter", new Runnable() {
			public void run() {
				exporter(inter);
			}
		});
		*/
		// menu popup
		/*
		JPopupMenu popup = Desktop.getInstance().getToolBar().addTool("......");
		JMenuItem menu;
		menu = new JMenuItem("Exporter"); 
		menu.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				exporter(inter);
			}
		});
		popup.add(menu);
		popup.pack();
		popup.setLocation(50, 50);
		*/
	}
	
	// ---------------------------------------------
	// Utilitaires
	// ---------------------------------------------

	private static String saveFile(String code, String nom) {
		String nomComplet;
		try {
			File buildDir = UserConfig.createTempDir("javac");
			nomComplet = buildDir + File.separator + nom;
			FileManager.save(nomComplet, code);
	        // Si il y a un problème avec le répertoire temporaire on se rabat sur le répertoire local
		} catch(Exception e1) {
			try {
				nomComplet = new File(nom).getAbsolutePath();
				System.err.println("Sauvegarde locale du fichier : " + nomComplet);
				FileManager.save(nomComplet, code);
	          // Sinon on signale le problème à l'utilisateur
			} catch(Exception e2) {
				System.out.println("Attention ! le répertoire '" + System.getProperty("user.dir") + "' ne peut etre utilisé pour sauver des fichiers, \n il faut re-lancer javascool dans un répertoire de travail approprié.");
				return null;
	        }
		}
		return nomComplet;
	}
	
	private static boolean openFileDesktop(String nomComplet) {
		if(java.awt.Desktop.isDesktopSupported()){
			if (java.awt.Desktop.getDesktop().isSupported(java.awt.Desktop.Action.OPEN)){
				java.awt.Desktop dt = java.awt.Desktop.getDesktop();
				try {
					File f = new File(nomComplet);
					dt.open(f);
					return true;
				} catch (Exception ex) {
					System.err.println("\n"+"exception:"+ex.getMessage()+"\n");
					JOptionPane.showMessageDialog(null, ex.getMessage());
					return false;
				} 
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}		
	}
	
	private static boolean browseFileDesktop(String nomComplet) {
		if(java.awt.Desktop.isDesktopSupported()){
			if (java.awt.Desktop.getDesktop().isSupported(java.awt.Desktop.Action.BROWSE)){
				java.awt.Desktop dt = java.awt.Desktop.getDesktop();
				try {
					File f = new File(nomComplet);
					dt.browse(f.toURI());
					return true;
				} catch (Exception ex) {
					//System.err.println("\n"+"exception:"+ex.getMessage()+"\n");
					return false;
				} 
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
	}	
	
	private static String tabuler(int indent) {
		String txt_indent = "\n";
		for(int i=0; i<indent; i++) {
		txt_indent = txt_indent + "\t";
		}
		return txt_indent;
	}    
	
	// ---------------------------------------------
	// Les commandes des menus
	// ---------------------------------------------
	
	private static boolean doBrowse(PanelInteraction pInter, String nom) {
		String code = pInter.getText();
		String nomComplet = saveFile(code, nom);
		if (nomComplet==null) return false;
		// execution
		return browseFileDesktop(nomComplet);
	}
	
	private static boolean doJavascript(PanelInteraction pInter) {
		return doBrowse(pInter, "test.html");
	}
	
	private static boolean doOpen(PanelInteraction pInter, String nom) {
		String code = pInter.getText();
		String nomComplet = saveFile(code, nom);
		if (nomComplet==null) return false;
		// execution
		return openFileDesktop(nomComplet);
	}
	
	private static boolean doR(PanelInteraction pInter) {
		return doOpen(pInter, "test.R");
	}
	
	private static boolean doPython(PanelInteraction pInter) {
		return doOpen(pInter, "test.py");
	}
	
	private static boolean doAlgobox(PanelInteraction pInter) {
		String code = pInter.getText();
		int i = code.indexOf("<?xml");
		if (i>=0) {
			code = code.substring(i);
		}
		else {
			AnalyseurAlgobox analyseur = new AnalyseurAlgobox(code);
			code = analyseur.exportAlgobox();
		}
		//pInter.writeConsole("["+code+"]");
		String nom = "test"+ ".alg";
		String nomComplet = saveFile(code, nom);
		if (nomComplet==null) return false;
		// execution
		return openFileDesktop(nomComplet);
	}

	
	private static void doCarmetal(PanelInteraction pInter) {
		//if (engine==null) {
			manager = new ScriptEngineManager();		 
			engine = manager.getEngineByName("js");
		//}
	    String fonctionsJS = ""
	    		+ "function Input(message) {\n"
	            + "var reponse = javax.swing.JOptionPane.showInputDialog(null,message,\"\",javax.swing.JOptionPane.PLAIN_MESSAGE);\n"
	            + "if (reponse==null) throw Error('Exit');\n"
	            + "return reponse;\n"
	            + "}\n"
	    		+ "function Println(message) {\n"
	            + "java.lang.System.out.println(message);\n"
	            + "}\n"
	    		+ "function Print(message) {\n"
	            + "java.lang.System.out.print(message);\n"
	            + "}\n"
	            ;
		String code = pInter.getText();
	    try {
	    	pInter.clearConsole(); pInter.writeConsole("");
	    	engine.eval(code+fonctionsJS);
	    	/*
	    	Bindings map = engine.getBindings(ScriptContext.ENGINE_SCOPE);
	    	for(Iterator<String> iter = map.keySet().iterator();iter.hasNext();){
	    		String cle = iter.next();
	    		System.out.println(cle + ":" + map.get(cle));
	    	}
	    	*/
	    }
	    catch (javax.script.ScriptException ex) {
	    	String msg = ex.getMessage();
	    	int i;
	    	i = msg.lastIndexOf("Error:");
	    	if (i>0) msg = msg.substring(i+6, msg.length());
	    	i = msg.lastIndexOf("Exception:");
	    	if (i>0) msg = msg.substring(i+10, msg.length());
	    	i = msg.indexOf("(<");
	    	if (i>0) msg = msg.substring(0, i);
	    	pInter.writeConsole("erreur ligne " + ex.getLineNumber() + " : " + msg );
	    	pInter.writelnConsole();
	    	//pInter.writeConsole("erreur complete :" + ex.getMessage() + "\n");
			try {
				int lig = ex.getLineNumber();
				javax.swing.JTextArea area = EditorWrapper.getRTextArea();
				int nb_lig = area.getLineCount();
				if (lig<=nb_lig) {
					int debut_lig = area.getLineStartOffset(lig-1);	// premiere ligne : ligne 0	
					area.setCaretPosition(debut_lig);
				}
			}
			catch(Exception ex1) {
				return;
			}
	    }
	}
	
	public static void exporter(PanelInteraction pInter) {
		String nom_lang = pInter.pPrincipal.getNomLangage().toLowerCase();
		if (pInter.isJavascript()) {
			doJavascript(pInter);
			return;
		}
		if (pInter.isAlgobox()) {
			doAlgobox(pInter);
			return;
		}
		if (nom_lang.equals("python")) {
			doPython(pInter);
			return;
		}
		if (nom_lang.equals("r")) {
			doR(pInter);
			return;
		}
		if (nom_lang.equals("carmetal")) {
			doCarmetal(pInter);
			return;
		}
	}
}
