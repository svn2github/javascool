package org.javascool.gui;

import java.awt.BorderLayout;

import javax.swing.JPanel;

/**
 * Panneau de l'IDE, contient l'éditeur et une barre d'outils.
 *
 */
public class IdePanel extends JPanel {

	private static final long serialVersionUID = 394304915314172236L;
	
	public IdePanel(){
		super(new BorderLayout());
		add(EditToolbar.getInstance(),BorderLayout.NORTH);
		add(TextFilesEditor.getInstance(),BorderLayout.CENTER);
		try{
			TextFilesEditor.getInstance().addTools(EditToolbar.getInstance());
		}catch(IllegalArgumentException e){}
	}
	
	/** Instance de la classe. */
	private static IdePanel panel;
	
	/** Permet d'avoir une instance unique de la classe.*/
	public static IdePanel getInstance() {
		if (IdePanel.panel == null) {
			IdePanel.panel = new IdePanel();
		}
		return IdePanel.panel;
	}

}
