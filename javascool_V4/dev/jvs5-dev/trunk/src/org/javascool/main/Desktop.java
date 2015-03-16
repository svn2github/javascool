/*********************************************************************************
 * Philippe.Vienne@sophia.inria.fr, Copyright (C) 2011.  All rights reserved.    *
 * Guillaume.Matheron@sophia.inria.fr, Copyright (C) 2011.  All rights reserved. *
 * Thierry.Vieville@sophia.inria.fr, Copyright (C) 2009.  All rights reserved.   *
 *********************************************************************************/
package org.javascool.main;

import java.awt.Component;
import java.awt.Container;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.JFrame;

import org.apache.commons.codec.binary.Base64;
import org.javascool.core.ProgletEngine;
import org.javascool.tools.UserConfig;
import org.javascool.widgets.MainFrame;
import org.javascool.widgets.TextEditor;
import org.javascool.widgets.ToolBar;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

// Used to define the frame
/**
 * Définit les functions d'interaction avec l'interface graphique de JavaScool.
 * 
 * @see <a href="Desktop.java.html">code source</a>
 * @serial exclude
 */
public class Desktop {

    // @static-instance

    /**
     * Crée et/ou renvoie l'unique instance du desktop.
     * <p>
     * Une application ne peut définir qu'un seul desktop.
     * </p>
     */
    public static Desktop getInstance() {
	if (Desktop.desktop == null) {
	    Desktop.desktop = new Desktop();
	}
	return Desktop.desktop;
    }

    private static Desktop desktop = null;
    
    private DesktopState state;

    private Desktop() {
	MainFrame.getFrame();
	String state=UserConfig.getInstance("javascool").getProperty("state");
	if(state!=null){
	    this.state=new DesktopState(state);
	}
    }

    /**
     * Renvoie la fenêtre racine de l'interface graphique.
     */
    public JFrame getFrame() {
	if (frame == null) {
	    mainPanel = StartPanel.getInstance();
	    frame = (new MainFrame() {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1727893520791296658L;

		@Override
		public boolean isClosable() {
		    // On vérifie que l'on peut fermer
		    if(!Desktop.getInstance().isClosable())return false;
		    // On sauvegarde l'état de Java's Cool
		    DesktopState ds=new DesktopState();
		    UserConfig.getInstance("javascool").setProperty("state", ds.toString());
		    return true;
		}
	    }).reset(About.title, About.logo, mainPanel);
	    if(state!=null){
		if(state.getOpenProglet()!=null){
		    this.openProglet(state.getOpenProglet());
		}
	    }
	}
	return frame;
    }

    private MainFrame frame;

    private Container mainPanel;

    /**
     * Retourne la bare d'outils de Java's cool
     */
    public ToolBar getToolBar() {
	return EditToolbar.getInstance();
    }

    /**
     * Demande la fermeture du desktop à la fin du programme.
     * 
     * @return La valeur true si le desktop peut être fermé sans dommage pour
     *         l'utilisateur, sinon la valeur fausse.
     */
    public boolean isClosable() {
	boolean close = TextFilesEditor.getInstance().isCloseable();
	if (close && ProgletEngine.getInstance().getProglet() != null) {
	    ProgletEngine.getInstance().getProglet().stop();
	}
	return close;
    }

    /**
     * Ferme la proglet en cours d'édition. La fonction vérifie auparavent si
     * tous les fichiers ont bien été sauvegardés
     */
    public void closeProglet() {
	if (!TextFilesEditor.getInstance().isCloseable())
	    return;
	if (ProgletEngine.getInstance().getProglet() != null)
	    ProgletEngine.getInstance().getProglet().stop();
	setCurrentComponent(StartPanel.getInstance());
    }

    /**
     * Change le contenu de la fenêtre principale.
     * 
     * @param comp
     *            Le composant à mettre en place
     */
    private void setCurrentComponent(Component comp) {
	frame.getContentPane().removeAll();
	frame.getContentPane().add(comp);
	frame.getContentPane().repaint();
	frame.getContentPane().validate();
	frame.getContentPane().repaint();
    }

    /**
     * Ouvre une proglet
     * 
     * @param proglet
     *            Le nom de code de la Proglet
     * @return True si tous les fichier ont été sauvegardé et la proglet
     *         sauvegardé
     */
    public boolean openProglet(String proglet) {
	if (!TextFilesEditor.getInstance().isCloseable())
	    return false;
	System.gc();
	RunToolbar.getInstance().disableDemoButton();
	setCurrentComponent(MainPanel.getInstance());
	MainPanel.getInstance().revalidate();
	MainPanel.getInstance().setDividerLocation(frame.getWidth() / 2);
	MainPanel.getInstance().revalidate();
	WidgetPanel.getInstance().setProglet(proglet);
	if (ProgletEngine.getInstance().getProglet().hasDemo()) {
	    RunToolbar.getInstance().enableDemoButton();
	} else {
	    RunToolbar.getInstance().disableDemoButton();
	}
	if(TextFilesEditor.getInstance().getTabCount()==0)
	    TextFilesEditor.getInstance().openNewFile();
	MainPanel.getInstance().revalidate();
	return true;
    }

    /**
     * Ouvre un nouvel onglet de navigation Ouvre un onglet HTML3 dans le
     * JVSWidgetPanel, cet onglet peut être fermé
     * 
     * @param url
     *            L'adresse à ouvrir sous forme de chaîne de caractères ou
     *            d'URL.
     * @param name
     *            Le titre du nouvel onglet
     */
    public void openBrowserTab(URL url, String name) {
	openBrowserTab(url.toString(), name);
    }

    /**
     * @see #openBrowserTab(URL, String)
     */
    public void openBrowserTab(String url, String name) {
	WidgetPanel.getInstance().openWebTab(url, name);
    }

    /**
     * Affiche la console.
     */
    public void focusOnConsolePanel() {
	WidgetPanel.getInstance().focusOnConsolePanel();
    }

    /**
     * Affiche la console.
     */
    public void focusOnProgletPanel() {
	WidgetPanel.getInstance().focusOnProgletPanel();
    }

    /**
     * Compile un Fichier Java's cool. La fonction ne prend aucun paramètre et
     * effectue la compilation du fichier en cours d'édition dans Java's Cool.
     * Les actions effectué sont :
     * <ul>
     * <li>Demander de sauvegarder le fichier</li>
     * <li>Compiler le programme avec le ProgletEngine
     * <ul>
     * <li>S'il y a un echec alors on arrête la compilation</li>
     * </ul>
     * </li>
     * <li>Affiche la console</li>
     * <li>Activer les boutons de lançement du programme</li>
     * </ul>
     * 
     * @return Vrai en cas de compilation réussie, faux dans tout cas contraire.
     */
    public boolean compileFile() {
	RunToolbar.getInstance().disableStartStopButton(); // On désactive les
	// bouttons de
	// lançement du
	// précédent
	// programme
	/**
	 * Les onglets d'éditeur actifs.
	 */
	TextFilesEditor ed = TextFilesEditor.getInstance();
	if (!(ed.getSelectedComponent() instanceof TextFileEditor)) // On
	    // vérifie
	    // s'il y a
	    // un
	    // fichier
	    // ouvert
	    return false;
	TextFileEditor currentEditor = (TextFileEditor) ed.getSelectedComponent();
	if (!ed.isCompilable()) // On sauvegarde le fichier courant
	    return false;
	ed.removeLineSignals(); // On enlève les repère actifs dans tous les
	// fichiers ouverts
	if (!ProgletEngine.getInstance().doCompile(currentEditor.getText())) // On
	    // compile
	    return false;
	RunToolbar.getInstance().enableStartStopButton();
	return true;
    }

    /**
     * Ouvre un nouveau fichier à partur d'une URL.
     * 
     * @param url
     *            L'adresse du fichier
     */
    public void openFile(URL url) {
	TextFilesEditor.getInstance().openFile(url);
    }

    /** 
     * @return Le déscriteur de l'état de la précédente instance 
     * de Java's Cool ou null si il n'y en a pas.
     */
    public DesktopState getOlderState(){
	if(state!=null){
	    return state;
	}else{return null;}
    }
    
    /**
     * Descripteur de l'état de Java's Cool
     */
    public class DesktopState{

	/**
	 * Tableau savegardant la liste des fichiers ouverts.
	 */
	private ArrayList<String> openFiles=new ArrayList<String>();
	/**
	 * Contien le nom de la proglet ouverte.
	 * Est a null si on est sur la page de démarage
	 */
	private String openProglet=null;

	/**
	 * Construit un descripteur à partir de la chaîne textuel 
	 * compressé fourni par la fonction toString()
	 * @param code La chaîne de description compressé
	 */
	public DesktopState(String code){
	    code=new String(Base64.decodeBase64(code));
	    try{
		JSONParser parser=new JSONParser();
		JSONObject conf=(JSONObject) parser.parse(code);
		this.openProglet=(String) conf.get("proglet");
		JSONArray files=(JSONArray)conf.get("files");
		for(Object file:files){
		    addOpenFiles((String) file);
		}
	    }catch(Exception e){
		System.err.println("Unable to read the JVS old state");
	    }
	}
	/**
	 * Construit un descripteur à partir de l'état courant de Java's Cool.
	 */
	public DesktopState(){
	    // On récupère les différentes instances
	    ProgletEngine pe=ProgletEngine.getInstance();
	    TextFilesEditor editors=TextFilesEditor.getInstance();
	    // On enregistre leurs valeurs
	    setOpenProglet(pe.getProglet()==null?null:pe.getProglet().getName());
	    for(int i=0;i<editors.getTabCount();i++){
		if(editors.getComponentAt(i) instanceof TextFileEditor){
		    TextFileEditor editor=(TextFileEditor) editors.getComponentAt(i);
		    if(!editor.isTmp())
			addOpenFiles(editor.getFileLocation());
		}
	    }
	}

	/**
	 * Retourne les fichiers enregistré dans le descripteur.
	 * @return Le tableau retourné contient les URL des fichiers dans le système 
	 * local. Il se peut donc que le fichier n'existe plus ou ait été supprimé. 
	 * Ce n'est pas la charge de cette classe que de faire se travail.
	 */
	public ArrayList<String> getOpenFiles() {
	    if(this.openFiles==null)this.openFiles=new ArrayList<String>();
	    return openFiles;
	}
	/**
	 * Ajoute un fichier à la liste des fichiers ouverts.
	 * La fonction vérifie que l'URL qui lui est donné n'est pas déjà présente 
	 * dans la classe.
	 * @param openFile L'URL au format d'une chaîne de caractère du fichier à
	 * ajouter au descripteur.
	 */
	public void addOpenFiles(String openFile) {
	    if(this.openFiles==null)this.openFiles=new ArrayList<String>();
	    if(this.openFiles.contains(openFile))return;
	    this.openFiles.add(openFile);
	}
	/**
	 * Retourne le nom de la proglet ouverte
	 * @return le nom du package proglet ouverte sous la forme d'une chaine 
	 * de caractère ou alors null dans le cas ou aucune proglet n'était ouverte
	 * et que Java's cool était sur la page de démarrage.
	 */
	public String getOpenProglet() {
	    return openProglet;
	}
	/**
	 * Configure la proglet ouverte
	 * @param openProglet Le nom du package de la proglet ou null pour spécifier la
	 * page de démarrage.
	 */
	public void setOpenProglet(String openProglet) {
	    this.openProglet = openProglet;
	}

	@SuppressWarnings("unchecked")
	public String toString(){
	    JSONObject state=new JSONObject();
	    state.put("files", openFiles);
	    state.put("proglet", openProglet);
	    return Base64.encodeBase64String(state.toString().getBytes());
	}

    }
}
