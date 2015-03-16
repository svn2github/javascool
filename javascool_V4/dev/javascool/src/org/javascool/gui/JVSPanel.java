package org.javascool.gui;

import java.io.Console;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import java.awt.BorderLayout;

import org.javascool.core.ProgletEngine;
import org.javascool.gui.editor.FileKit;
import org.javascool.gui.editor.FileReference;
import org.javascool.gui.editor.JVSEditorsPane;
import org.javascool.tools.UserConfig;

/**
 * The main panel for Java's cool This class wich is very static contain all
 * that we need to run Java's cool like save and open file command. This class
 * can only be called by JVSPanel on instance otherwise it can throw very big
 * errors
 * 
 * @author Philippe Vienne
 */
class JVSPanel extends JPanel {

	private static final long serialVersionUID = -913356947524067017L;

	private static JVSPanel desktop = null;

	/** Access to the unique instance of the JVSPanel object. */
	public static JVSPanel getInstance() {
		if (JVSPanel.desktop == null) {
			JVSPanel.desktop = new JVSPanel();
		}
		return JVSPanel.desktop;
	}

	/** Contain the current CompileAction. */
	private final CompileAction ca = new CompileAction();

	private JVSPanel() {
		setVisible(true);
		setLayout(new BorderLayout());
		add(JVSStartPanel.getInstance());
		revalidate();
	}

	/**
	 * Handle the close application task Check if all files are saved and if the
	 * user want to close the application
	 * 
	 * @return True mean that app can be close and false that app can NOT be
	 *         closed
	 */
	public boolean close() {
		return closeAllFiles("Voulez vous vraiment quitter Java's cool ?");
	}

	/**
	 * Ask to user if he want to continue and save files Check if all files are
	 * saved and if the user wants to continue
	 * 
	 * @return True mean that you can continue and false not
	 */
	public boolean closeAllFiles() {
		return closeAllFiles("Voulez vous vraiment continuer ?");
	}

	/**
	 * Ask to user if he want to save files Check if all files are saved and if
	 * the user wants to continue
	 * 
	 * @param messageIfAllFilesAreSaved
	 *            Message to ask to the user
	 * @return True mean that you can continue and false not
	 */
	public boolean closeAllFiles(String messageIfAllFilesAreSaved) {
		if (getEditorTabs().isAllFilesSaved()) {
			final int n = JOptionPane.showConfirmDialog(Desktop.getInstance()
					.getFrame(), messageIfAllFilesAreSaved, "Confirmation",
					JOptionPane.YES_NO_OPTION);
			if (n == JOptionPane.YES_OPTION)
				return true;
			else
				return false;
		}
		return getEditorTabs().saveAllFiles();
	}

	public void closeFile() {
		getEditorTabs().closeCurrentFile();
	}

	/**
	 * Close the current proglet.
	 * 
	 */
	public void closeProglet() {
		if (closeAllFiles()) {
			removeAll();
			setOpaque(true);
			this.repaint();
			validate();
			this.repaint();
			add(JVSStartPanel.getInstance());
			this.repaint();
			revalidate();
			this.repaint();
			if (ProgletEngine.getInstance().getProglet() != null) {
				ProgletEngine.getInstance().getProglet().stop();
			}
		}
	}

	/**
	 * Compile edited file in the editor.
	 */
	public void compileFile() {
		ca.actionPerformed(null);
	}

	/**
	 * Get the current EditorTabs.
	 * 
	 * @return An EditorTabs
	 * @throws IllegalStateException
	 *             if no EditorTabs opened in the left part
	 */
	public FileKit getEditorTabs() throws IllegalStateException {
		if (JVSCenterPanel.getInstance().getLeftComponent() instanceof FileKit)
			return (FileKit) JVSCenterPanel.getInstance().getLeftComponent();
		throw new IllegalStateException("Left component is not an EditorTabs");
	}

	/**
	 * Charge une nouvelle proglet dans l'interface utilisateur.
	 * 
	 * @param name
	 *            Le nom de code de la proglet (ex:abcdAlgos)
	 * @see org.javascool.core.ProgletEngine
	 */
	public void loadProglet(String name) {
		System.gc();
		removeAll();
		try {
			JVSToolBar.getInstance().disableDemoButton();
			revalidate();
			this.add(JVSToolBar.getInstance(), BorderLayout.NORTH);
			this.add(JVSCenterPanel.getInstance(), BorderLayout.CENTER);
			revalidate();
			JVSCenterPanel.getInstance().revalidate();
			JVSCenterPanel.getInstance().setDividerLocation(getWidth() / 2);
			JVSCenterPanel.getInstance().revalidate();
			JVSWidgetPanel.getInstance().setProglet(name);
			if (ProgletEngine.getInstance().getProglet().hasDemo()) {
				JVSToolBar.getInstance().enableDemoButton();
			} else {
				JVSToolBar.getInstance().disableDemoButton();
			}
			Desktop.getInstance().openNewFile();
		} catch (final Exception e) {
			throw new RuntimeException("Unable to load proglet " + name, e);
		}
	}

	/**
	 * Open a new file in the editor
	 * 
	 * @see JVSEditorsPane
	 */
	public void newFile() {
		getEditorTabs().openFile(new FileReference());
	}

	/**
	 * Open a file. Start a file chooser and open selected file to the current
	 * EditorTabs
	 * 
	 * @see JFileChooser
	 * @see JVSEditorsPane
	 */
	public void openFile() {
		final JFileChooser fc = new JFileChooser();
		if (UserConfig.getInstance("javascool").getProperty("dir") != null) {
			fc.setCurrentDirectory(new File(UserConfig.getInstance("javascool")
					.getProperty("dir")));
		} else if (System.getProperty("os.name").toLowerCase().contains("nix")
				|| System.getProperty("os.name").toLowerCase().contains("nux")) {
			fc.setCurrentDirectory(new File(System.getProperty("user.dir")));
		} else if (System.getProperty("home.dir") != null) {
			fc.setCurrentDirectory(new File(System.getProperty("home.dir")));
		}
		if (fc.showOpenDialog(Desktop.getInstance().getFrame()) == JFileChooser.APPROVE_OPTION) {
			if (!fc.getSelectedFile().exists()) {
				JOptionPane.showMessageDialog(Desktop.getInstance().getFrame(),
						"Le fichier indiqué n'existe pas !!!", "Erreur",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			UserConfig.getInstance("javascool").setProperty("dir",
					fc.getSelectedFile().getParentFile().getAbsolutePath());
			openFile(new FileReference(fc.getSelectedFile()));
		}
	}

	/** Open a specified file. */
	public void openFile(FileReference jvsFileReference) {
		getEditorTabs().openFile(jvsFileReference);
	}

	@Deprecated
	public void reportApplicationBug(String ex) {
		Dialog.error("Erreur dans Java's Cool", ex);
	}

	/**
	 * Throw to the user a compile error. Show a compile error for an human Open
	 * a dialog with compile error explains and hightlight the error line
	 * 
	 * @param line
	 *            The line error
	 * @param explication
	 *            Human explain for that error
	 * @see Console
	 */
	public void reportCompileError(int line, String explication) {
		org.javascool.widgets.Console.getInstance().clear();
		JVSWidgetPanel.getInstance().focusOnConsolePanel();
		if (ca.getCompiledEditor() != null) {
			ca.getCompiledEditor().signalLine(line);
		}
	}

	/**
	 * Report a runtime bug to user. Report a bug to the user without
	 * ErrorCatcher. It is used by a proglet to report a bug while execute the
	 * user code.
	 * 
	 * @param ex
	 *            The bug to report
	 */
	public void reportRuntimeBug(String ex) {
		final StackTraceElement[] stack = Thread.currentThread()
				.getStackTrace();
		int line = 0;
		for (final StackTraceElement elem : stack) {
			if (elem.getFileName().startsWith("JvsToJavaTranslated")) {
				line = elem.getLineNumber();
			} else {
				System.err.println(elem.getClassName());
			}
		}
		if (ca.getCompiledEditor() != null) {
			ca.getCompiledEditor().signalLine(line);
		}
		ProgletEngine.getInstance().doStop();
		JOptionPane.showMessageDialog(Desktop.getInstance().getFrame(), ex,
				"Erreur du logiciel à la ligne " + line,
				JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * @see saveFile()
	 */
	public boolean saveAsFile() {
		if (getEditorTabs().saveAsCurrentFile())
			return true;
		return false;
	}

	/**
	 * Save the current file. Ask to the user where save the file if is tmp or
	 * simple save
	 * 
	 * @see JVSFile
	 * @return true on success or false if the file could not be saved
	 */
	public boolean saveFile() {
		if (getEditorTabs().saveCurrentFile())
			return true;
		return false;
	}

	@Deprecated
	public static class Dialog {

		/** Show an error dialog */
		public static void error(String title, String message) {
			JOptionPane.showMessageDialog(Desktop.getInstance().getFrame(),
					message, title, JOptionPane.ERROR_MESSAGE);
		}

		/** Show a success dialog */
		public static void success(String title, String message) {
			JOptionPane.showMessageDialog(Desktop.getInstance().getFrame(),
					message, title, JOptionPane.INFORMATION_MESSAGE);
		}
	}
}
