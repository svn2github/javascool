package org.javascool.widgets;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import org.fife.ui.autocomplete.AutoCompletion;
import org.fife.ui.autocomplete.BasicCompletion;
import org.fife.ui.autocomplete.DefaultCompletionProvider;
import org.fife.ui.autocomplete.LanguageAwareCompletionProvider;
import org.fife.ui.autocomplete.ShorthandCompletion;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rtextarea.Gutter;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.javascool.macros.Macros;
import org.javascool.tools.Pml;

/**
 * Définit un panneau éditeur de texte qui intègre les fonctions de colorisation
 * et de complétion automatique.
 * 
 * @author Philippe Vienne
 */
public class TextEditor extends JPanel {
    private static final long serialVersionUID = 1L;

    /** Barre de commande du panneau. */
    protected ToolBar toolBar;
    /** Panneau de l'éditeur. */
    protected RSyntaxTextArea textArea;
    // Panneau de glissières
    private RTextScrollPane scrollPane;
    // Gestionnaire d'autocomplétion
    DefaultCompletionProvider completionsProvider;
    /**
     * Tables des raccourcis de l'éditeur. - Il faut y ajouter des éléments de
     * la forme:
     * "&lt;tr>&lt;td>&lt;tt>Ctrl+<i>X</i>&lt;/tt>&lt;/td>&lt;td><i>description du raccourci</i>&lt;/td>&lt;/tr>\n"
     * +
     */
    String helpText = "<tr><td><tt>Ctrl+a</tt></td><td>Sélectionne tout le texte</td></tr>\n"
	    + "<tr><td><tt>Ctrl+c</tt></td><td>Copie du texte sous le curseur</td></tr>\n"
	    + "<tr><td><tt>Ctrl+x</tt></td><td>Coupe du texte sous le curseur</td></tr>\n"
	    + "<tr><td><tt>Ctrl+v</tt></td><td>Colle du texte précédemment copié ou collé</td></tr>\n"
	    + "<tr><td></td><td></td></tr>\n"
	    + "<tr><td><tt>Ctrl+z</tt></td><td>Annule la prédédente commande d'édition</td></tr>\n"
	    + "<tr><td><tt>Ctrl+y</tt></td><td>Rétabli la prédédente commande d'édition</td></tr>\n"
	    + "<tr><td></td><td></td></tr>\n" + "<tr><td><tt>Ctrl+Espace</tt></td><td>Lance l'auto-complétion</td></tr>\n"
	    + "<tr><td></td><td></td></tr>\n";

    /** Construit un panneau d'édition. */
    public TextEditor() {
	setLayout(new BorderLayout());
	// Creation de la barre de commande
	{
	    toolBar = new ToolBar();
	    add(toolBar, BorderLayout.NORTH);
	}
	// Creation de la zone d'édition
	{
	    textArea = new RSyntaxTextArea(25, 70);
	    textArea.setCaretPosition(0);
	    textArea.requestFocusInWindow();
	    textArea.setMarkOccurrences(true);
	    textArea.setText("");
	    scrollPane = new RTextScrollPane(textArea, true);
	    Gutter gutter = scrollPane.getGutter();
	    gutter.setBorderColor(Color.BLUE);
	    add(scrollPane, BorderLayout.CENTER);
	}
	// Définition du mécanisme de complétion
	{
	    completionsProvider = new DefaultCompletionProvider();
	    LanguageAwareCompletionProvider lacp = new LanguageAwareCompletionProvider(completionsProvider);
	    AutoCompletion ac = new AutoCompletion(lacp) {
		@Override
		public void doCompletion() {
		    if (isAutoCompleteEnabled()) {
			super.doCompletion();
		    }
		}
	    };
	    ac.install(textArea);
	    ac.setAutoCompleteSingleChoices(false);
	    ac.setAutoActivationEnabled(true);
	    ac.setAutoActivationDelay(1500);
	    ac.setShowDescWindow(true);
	}
	// Ajout de l'aide à l'édition
	{
	    JPopupMenu j = toolBar.addRightTool("Aide");
	    j.add(new JLabel("<html>\n<b>Commandes d'édition</b><br><table>\n" + helpText + "</table>"));
	}
    }

    // Teste si le système est un mac
    protected static boolean isMac() {
	return System.getProperty("os.name").toUpperCase().contains("MAC");
    }

    /**
     * Définit la colorisation de cet éditeur.
     * 
     * @param syntax
     *            Nom de la syntaxe : "Java", "Jvs", "None".
     * @return Cet objet, permettant de définir la construction
     *         <tt>new TextEditor().setProperty(..)</tt>.
     */
    public TextEditor setSyntax(String syntax) {
	syntax = syntax.toLowerCase();
	toolBar.removeTool("Reformater le code");
	clearCompletions();
	if ("jvs".equals(syntax) || "java".equals(syntax)) {
	    textArea.setSyntaxEditingStyle(org.fife.ui.rsyntaxtextarea.SyntaxConstants.SYNTAX_STYLE_JAVA);
	}
	if ("jvs".equals(syntax)) {
	    toolBar.addTool("Reformater le code", new Runnable() {
		@Override
		public void run() {
		    setText(org.javascool.core.JvsBeautifier.run(getText()));
		}
	    });
	    addCompletions("org/javascool/macros/completion-langage.xml")
		    .addCompletions("org/javascool/macros/completion-stdout.xml")
		    .addCompletions("org/javascool/macros/completion-stdin.xml")
		    .addCompletions("org/javascool/macros/completion-macros.xml");
	}
	return this;
    }

    /**
     * Ajoute à cet éditeur les complétions définies dans le fichier.
     * <p>
     * Les complétions sont déclenchées par Ctrl+Space.
     * </p>
     * 
     * @param completions
     *            Le nom du fichier contenant la définition des complétions.
     * @return Cet objet, permettant de définir la construction
     *         <tt>new TextEditor().addCompletions(..)</tt>.
     */
    public TextEditor addCompletions(String completions) {
	// Initialise le menu si il n'est pas définit
	if (insertions == null) {
	    insertions = toolBar.addTool("Auto insertion");
	}
	// Lit le fichier d'autocomplétion
	Pml defs = new Pml().load(completions);
	String title = defs.getString("title");
	if (title != null) {
	    insertions.add(new JLabel("<html><b>" + title + "</b>"));
	}
	// Pour chaque item . .
	for (int i = 0; i < defs.getCount(); i++) {
	    // .. recupère les infos de nom, titre, doc et code
	    Pml def = defs.getChild(i);
	    String name = def.getString("name"), desc = def.getString("title"), code = null, doc = null;
	    for (int j = 0; j < def.getCount(); j++) {
		if ("code".equals(def.getChild(j).getTag()) && def.getChild(j).getChild(0) != null && code == null) {
		    code = def.getChild(j).getChild(0).getTag().replaceAll("\\\\\"", "\"");
		}
		if ("doc".equals(def.getChild(j).getTag()) && def.getChild(j).getChild(0) != null && doc == null) {
		    doc = def.getChild(j).getChild(0).getTag().replaceAll("\\\\\"", "\"");
		}
	    }
	    // .. et ajoute la raccourci si il est bien défini
	    if (name != null) {
		// Ajoute le raccourci à l'éditeur
		BasicCompletion bc = code == null ? new BasicCompletion(completionsProvider, name) : new ShorthandCompletion(
			completionsProvider, name, code);
		if (desc != null) {
		    bc.setShortDescription(desc);
		}
		if (doc != null) {
		    bc.setSummary(doc);
		}
		completionsProvider.addCompletion(bc);
		// Ajoute le raccourci au menu
		if (code != null) {
		    insertions.add(new JMenuItem(new InsertAction(name, code, def.getInteger("offset", code.length()))));
		}
	    }
	}
	return this;
    }

    // Definit une action d'insertion de texte
    private class InsertAction extends AbstractAction {
	private static final long serialVersionUID = 1L;

	public InsertAction(String name, String string, int offset) {
	    super(name);
	    this.string = string;
	    this.offset = offset;
	}

	private String string;
	private int offset;

	@Override
	public void actionPerformed(ActionEvent evt) {
	    try {
		int where = textArea.getCaretPosition();
		textArea.getDocument().insertString(where, string, null);
		textArea.setCaretPosition(where + offset);
	    } catch (Exception e) {
	    }
	}
    }

    private JPopupMenu insertions = null;

    /** Remet à zéro toutes les complétions définies pour cet éditeur. */
    void clearCompletions() {
	completionsProvider.clear();
	toolBar.removeTool("Auto insertion");
	insertions = null;
    }

    /** Renvoie le texte actuellement édité. */
    public String getText() {
	return textArea.getText();
    }

    /**
     * Initialise le texte à éditer.
     * 
     * @param text
     *            Le texte à éditer.
     */
    public void setText(String text) {
	textArea.setText(initialText = text);
    }

    /**
     * Teste si le texte a été édité ou si il reste inchangé.
     * 
     * @return Renvoie la valeur true si getText() et la dernière valeur donnée
     *         à setText() ne sont pas les mêmes
     */
    public boolean isTextModified() {
	return !getText().equals(initialText);
    }

    private String initialText = "";

    /**
     * Marque une ligne du texte avec une icône.
     * 
     * @param line
     *            Numéro de la ligne du texte.
     * @param icon
     *            Nom de l'icône à utiliser (une icône indiquant une erreur par
     *            défaut).
     */
    public void signalLine(int line, String icon) {
	Gutter gutter = scrollPane.getGutter();
	gutter.setBookmarkingEnabled(true);
	try {
	    textArea.setCaretPosition(textArea.getLineStartOffset(line - 1));
	    scrollPane.getGutter().addLineTrackingIcon(line - 1,
		    Macros.getIcon(icon == null ? "org/javascool/widgets/icons/error.png" : icon));
	} catch (Exception e) {
	}
    }

    /** @see #signalLine(int, String) */
    public void signalLine(int line) {
	signalLine(line, null);
    }

    /** Efface toutes les marques mises sur le code. */
    public void removeLineSignals() {
	scrollPane.getGutter().removeAllTrackingIcons();
    }
}
