package org.javascool.gui;

import javax.swing.JSplitPane;

import org.javascool.gui.editor.JVSEditorsPane;

/**
 * Le double pannau central de Java's cool Le JVSCenterPanel est utilisé pour
 * plaçer correctemment le JVSWidgetPanel à droite et le JVSFileTabs à gauche
 * 
 * @see org.javascool.gui.JVSEditorsPane
 * @see org.javascool.gui.JVSWidgetPanel
 */
class JVSCenterPanel extends JSplitPane {
	private static final long serialVersionUID = 1L;
	/** L'instance en cour de cette classe */
	private static JVSCenterPanel jvssplitpane;

	/** Retourne l'instance actuelle de la classe */
	public static JVSCenterPanel getInstance() {
		if (JVSCenterPanel.jvssplitpane == null) {
			JVSCenterPanel.jvssplitpane = new JVSCenterPanel();
		}
		return JVSCenterPanel.jvssplitpane;
	}

	/** Constructeur de la classe */
	private JVSCenterPanel() {
		super(JSplitPane.HORIZONTAL_SPLIT);
		setLeftComponent(JVSEditorsPane.getInstance());
		setRightComponent(JVSWidgetPanel.getInstance());
		setVisible(true);
		validate();
	}
}
