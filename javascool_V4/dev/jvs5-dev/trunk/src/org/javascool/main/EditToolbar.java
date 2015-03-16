package org.javascool.main;

import javax.swing.JButton;

import org.javascool.widgets.ToolBar;

/**
 * La barre d'outil de l'ide.
 * 
 * @since 4.5
 */
public class EditToolbar extends ToolBar {

    private static final long serialVersionUID = -2921512429946718150L;

    /** Instance de la classe. */
    private static EditToolbar jvstb;

    /** Permet d'avoir une instance unique de la classe. */
    public static EditToolbar getInstance() {
	if (EditToolbar.jvstb == null) {
	    EditToolbar.jvstb = new EditToolbar();
	}
	return EditToolbar.jvstb;
    }

    /** Boutons de compilation. */
    private JButton compileButton;

    public EditToolbar() {
	addLeftTool(About.getAboutMessage());
	addTool("Nouvelle activité", "org/javascool/widgets/icons/new.png", new Runnable() {

	    @Override
	    public void run() {
		Desktop.getInstance().closeProglet();
	    }
	});
	compileButton = addRightTool("Compiler", "org/javascool/widgets/icons/compile.png", new Runnable() {

	    @Override
	    public void run() {
		Desktop.getInstance().compileFile();
	    }
	});

    }

    public void enableCompileButton() {
	compileButton.setVisible(true);
	revalidate();
    }

    public void disableCompileButton() {
	compileButton.setVisible(false);
	revalidate();
    }

}
