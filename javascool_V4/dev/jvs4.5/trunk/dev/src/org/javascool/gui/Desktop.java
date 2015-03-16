/*********************************************************************************
 * Philippe.Vienne@sophia.inria.fr, Copyright (C) 2011.  All rights reserved.    *
 * Guillaume.Matheron@sophia.inria.fr, Copyright (C) 2011.  All rights reserved. *
 * Thierry.Vieville@sophia.inria.fr, Copyright (C) 2009.  All rights reserved.   *
 *********************************************************************************/
package org.javascool.gui;

import java.awt.Component;
import java.awt.Container;
import java.net.URL;

import javax.swing.JFrame;

import org.javascool.About;
import org.javascool.core.ProgletEngine;
import org.javascool.widgets.MainFrame;
import org.javascool.widgets.ToolBar;

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

	private Desktop() {
		MainFrame.getFrame();
	}

	/** Renvoie la fenêtre racine de l'interface graphique. */
	public JFrame getFrame() {
		if (frame == null) {
			mainPanel=StartPanel.getInstance();
			frame = (new MainFrame() {

				/**
				 * 
				 */
				private static final long serialVersionUID = 1727893520791296658L;

				@Override
				public boolean isClosable() {
					return Desktop.getInstance().isClosable();
				}
			}).reset(About.title, About.logo, mainPanel);
		}
		return frame;
	}

	private MainFrame frame;

	private Container mainPanel;

	/** Retourne la bare d'outils de Java's cool */
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

	/** Ferme la proglet en cours d'édition. 
	 * La fonction vérifie auparavent si tous les fichiers ont bien été sauvegardés
	 */
	public void closeProglet() {
		if(!TextFilesEditor.getInstance().isCloseable())
			return;
		if (ProgletEngine.getInstance().getProglet() != null)
			ProgletEngine.getInstance().getProglet().stop();
		setCurrentComponent(StartPanel.getInstance());
	}

	/** Change le contenu de la fenêtre principale.
	 * @param comp
	 * 			Le composant à mettre en place
	 */
	private void setCurrentComponent(Component comp){
		frame.getContentPane().removeAll();
		frame.getContentPane().add(comp);
		frame.getContentPane().repaint();
		frame.getContentPane().revalidate();
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
		if(!TextFilesEditor.getInstance().isCloseable())
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

	/** Affiche la console. */
	public void focusOnConsolePanel() {
		WidgetPanel.getInstance().focusOnConsolePanel();
	}

	/** Affiche la console. */
	public void focusOnProgletPanel() {
		WidgetPanel.getInstance().focusOnProgletPanel();
	}

	/** Compile un Fichier Java's cool.
	 * La fonction ne prend aucun paramètre et effectue la compilation du fichier
	 * en cours d'édition dans Java's Cool. Les actions effectué sont :
	 * <ul>
	 * <li>Demander de sauvegarder le fichier</li>
	 * <li>Compiler le programme avec le ProgletEngine
	 * 		<ul><li>S'il y a un echec alors on arrête la compilation</li></ul>
	 * </li>
	 * <li>Affiche la console</li>
	 * <li>Activer les boutons de lançement du programme</li>
	 * </ul>
	 * @return Vrai en cas de compilation réussie, faux dans tout cas contraire.
	 */
	public boolean compileFile(){
		RunToolbar.getInstance().disableStartStopButton(); // On désactive les bouttons de lançement du précédent programme
		/** Les onglets d'éditeur actifs. */
		TextFilesEditor ed=TextFilesEditor.getInstance();
		if(!(ed.getSelectedComponent() instanceof TextFileEditor)) // On vérifie s'il y a un fichier ouvert
			return false;
		TextFileEditor currentEditor=(TextFileEditor) ed.getSelectedComponent();
		if(!ed.isCompilable()) // On sauvegarde le fichier courant
			return false;
		ed.removeLineSignals(); // On enlève les repère actifs dans tous les fichiers ouverts
		if(!ProgletEngine.getInstance().doCompile(currentEditor.getText())) // On compile
			return false;
		RunToolbar.getInstance().enableStartStopButton();
		return true;
	}

	/** Ouvre un nouveau fichier à partur d'une URL.
	 * 
	 * @param url L'adresse du fichier
	 */
	public void openFile(URL url) {
		TextFilesEditor.getInstance().openFile(url);
	}
}
