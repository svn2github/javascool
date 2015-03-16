package org.javascool.gui.editor;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.Gutter;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Event;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import org.javascool.gui.Desktop;
import org.javascool.tools.FileManager;
import org.javascool.widgets.MainFrame;
import org.javascool.widgets.TabbedPane;
import org.javascool.widgets.ToolBar;

/**
 * Define a JVSEditor Use JVSEditor to edit jvs files, it can be used as a panel
 * 
 * @author Philippe VIENNE
 * @todo Add a print command
 * @todo Open File with AWT for Mac and/or Windows
 */
class JVSEditor extends JPanel implements EditorKit {

	private static final long serialVersionUID = 1L;

	/** Tests if on MacIntosh. */
	private static boolean isMac() {
		return System.getProperty("os.name").toUpperCase().contains("MAC");
	}

	/** The editor */
	private final RSyntaxTextArea textPane;
	/** The scroll pane */
	private final RTextScrollPane scrollPane;
	/** The ToolBar */
	private final ToolBar toolBar;
	/** The Completion Provider */
	private final JVSAutoCompletionProvider jacp;
	/** Opened file */
	private FileReference file;

	/**
	 * Create a new JVSEditor Common setup
	 */
	public JVSEditor(FileReference file) {
		super(new BorderLayout());
		TabbedPane.setComponentClosable(this);
		this.file = file;
		textPane = createTextArea();

		jacp = new JVSAutoCompletionProvider(textPane);
		jacp.setShowDescWindow(true);

		scrollPane = new RTextScrollPane(textPane);
		scrollPane.getGutter().setBorderColor(Color.BLACK);

		add(scrollPane);

		toolBar = new ToolBar();
		toolBar.add(new FormatCodeAction(textPane));

		add(toolBar, BorderLayout.NORTH);

		setText(file.getContent());
	}

	/**
	 * TextArea initialization Creates the text area for this application.
	 * 
	 * @return The text area.
	 */
	private RSyntaxTextArea createTextArea() {
		final RSyntaxTextArea textArea = new RSyntaxTextArea();
		textArea.setCaretPosition(0);
		textArea.requestFocusInWindow();
		textArea.setMarkOccurrences(true);
		textArea.setAntiAliasingEnabled(true);
		textArea.setText("");
		textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);

		KeyStroke key = KeyStroke.getKeyStroke(KeyEvent.VK_S, Event.CTRL_MASK);
		if (JVSEditor.isMac()) {
			key = KeyStroke.getKeyStroke(KeyEvent.VK_S, Event.META_MASK);
		}
		KeyStroke.getKeyStroke(KeyEvent.VK_C, Event.CTRL_MASK);
		if (JVSEditor.isMac()) {
			KeyStroke.getKeyStroke(KeyEvent.VK_C, Event.META_MASK);
		}
		textArea.getInputMap().put(key, "save");
		textArea.getActionMap().put("save", new AbstractAction() {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				save();
			}
		});
		textArea.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent e) {
				file.setContent(getText());
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
			}
		});
		return textArea;
	}

	/**
	 * @see org.javascool.gui.editor.Editor#getFile()
	 */
	@Override
	public FileReference getFile() {
		return file;
	}

	/**
	 * @see org.javascool.gui.editor.Editor#getName()
	 */
	@Override
	public String getName() {
		if (file == null)
			return "Nouveau Fichier";
		if (file.isTmp())
			return "Nouveau Fichier";
		else
			return file.getName();
	}

	/** Get the RSyntaxTextArea */
	public RSyntaxTextArea getRTextArea() {
		return textPane;
	}

	/** Retourne le RTextScrollPane de l'éditeur */
	public RTextScrollPane getScrollPane() {
		return scrollPane;
	}

	/**
	 * @see org.javascool.gui.editor.Editor#getText()
	 */
	@Override
	public String getText() {
		return textPane.getText();
	}

	/**
	 * @see org.javascool.gui.editor.Editor#hasToSave()
	 */
	@Override
	public Boolean hasToSave() {
		return file.hasToSave();
	}

	/**
	 * @see org.javascool.gui.editor.Editor#removeLineSignals()
	 */
	@Override
	public void removeLineSignals() {
		getScrollPane().getGutter().removeAllTrackingIcons();
	}

	/**
	 * @see org.javascool.gui.editor.Editor#save()
	 */
	@Override
	public Boolean save() {
		if (file.isTmp())
			return saveAs();
		else {
			file.setContent(getText());
			setFile(file);
			return file.save();
		}
	}

	/**
	 * @see org.javascool.gui.editor.Editor#saveAs()
	 */
	@Override
	public boolean saveAs() {
		final JFileChooser jfc = new JFileChooser();
		jfc.setApproveButtonText(file.isTmp() ? "Sauvegarder"
				: "Sauvegarder sous");
		if (jfc.showSaveDialog(MainFrame.getFrame()) == JFileChooser.APPROVE_OPTION) {
			if (!jfc.getSelectedFile().getName()
					.endsWith(FileReference.SOURCE_EXTENTION)) {
				jfc.setSelectedFile(new File(jfc.getSelectedFile()
						.getParentFile(), jfc.getSelectedFile().getName()
						+ FileReference.SOURCE_EXTENTION));
			}
			if (jfc.getSelectedFile().exists()) {
				if (JOptionPane.showConfirmDialog(MainFrame.getFrame(),
						"Êtes vous sûr de vouloir effacer ce fichier ?",
						"Confirmation", JOptionPane.YES_NO_OPTION,
						JOptionPane.ERROR_MESSAGE) != JOptionPane.OK_OPTION)
					return save();
			}
			FileManager.save(jfc.getSelectedFile().getAbsolutePath(), "",
					false, true);
			file.setFile(jfc.getSelectedFile());
			return save();
		} else
			return false;
	}

	/**
	 * @see org.javascool.gui.editor.Editor#saveBeforeClose()
	 */
	@Override
	public boolean saveBeforeClose() {
		if (!hasToSave())
			return true;
		final int result = JOptionPane.showConfirmDialog(Desktop.getInstance()
				.getFrame(), "Voulez vous enregistrer " + getName()
				+ " avant de continuer ?");
		if (result == JOptionPane.YES_OPTION)
			return save();
		else if (result == JOptionPane.NO_OPTION)
			return true;
		else
			return false;
	}

	/**
	 * @see org.javascool.gui.editor.Editor#setFile(org.javascool.gui.editor.JVSFileReferance)
	 */
	@Override
	public void setFile(FileReference file) {
		this.file = file;
		textPane.setText(file.getContent());
		firePropertyChange("name", null, getName());
	}

	/**
	 * @see org.javascool.gui.editor.Editor#setText(java.lang.String)
	 */
	@Override
	public void setText(String text) {
		textPane.setText(text);
	}

	/**
	 * @see org.javascool.gui.editor.Editor#signalLine(int)
	 */
	@Override
	public void signalLine(int line) {
		final Gutter gutter = getScrollPane().getGutter();
		gutter.setBookmarkingEnabled(true);
		ImageIcon icon = null;
		BufferedImage img;
		try {
			img = ImageIO
					.read(ClassLoader
							.getSystemResourceAsStream("org/javascool/widgets/icons/error.png"));
			icon = new ImageIcon(img);
		} catch (final IOException ex1) {
			System.err.println("Dysfonctionnement innatendu ici " + ex1);
		}
		try {
			getRTextArea().setCaretPosition(
					getRTextArea().getLineStartOffset(line - 1));
			getScrollPane().getGutter().addLineTrackingIcon(line - 1, icon);
		} catch (final BadLocationException ex) {
			System.err.println("Dysfonctionnement innatendu ici " + ex);
		}
	}

}
