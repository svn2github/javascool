/*******************************************************************************
 * Hamdi.Ben_Abdallah@inria.fr, Copyright (C) 2009.  All rights reserved.      *
 *******************************************************************************/

package proglet;

// Used to define the widget
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JTextArea;
import javax.swing.text.JTextComponent;
import javax.swing.text.html.HTMLDocument;
import javax.swing.JScrollPane;
import javax.swing.JLabel;

// Used to define menu and menu's action
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.Action;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;

// Used for key binding
import java.awt.Event;
import java.awt.event.KeyEvent;
import javax.swing.KeyStroke;
import javax.swing.text.DefaultEditorKit;

// Used to implement the undo functionality
import javax.swing.undo.UndoManager;
import javax.swing.event.UndoableEditListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.CannotRedoException;

// Used to enter find/replace field
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;

// Used to track the line number
import javax.swing.event.CaretListener;
import javax.swing.event.CaretEvent;

// Used to show the font
import java.awt.Font;

// Used for the print interface
import java.awt.print.PrinterJob;
import java.awt.print.Printable;
import java.awt.print.PageFormat;
import java.awt.Graphics2D;
import java.awt.Graphics;

/** This widget defines the proglet source editor.
 * @see <a href="SourceEditor.java">source code</a>
 */
public class SourceEditor extends JPanel {
  private static final long serialVersionUID = 1L;

  // Just used for development
  public static void main(String usage[]) { 
    javax.swing.JFrame f = new javax.swing.JFrame(); f.getContentPane().add(new SourceEditor()); f.pack(); f.setSize(600, 700); f.setVisible(true); 
  }

  /** Sets the editing text. 
   * @param text The text to edit.
   */
  public void setText(String text) { pane.setText(text); pane.setCaretPosition(0); }

  /** Gets the edited text. */
  public String getText() { return pane.getText(); }

  // Widget construction
  private JTextArea pane; private JLabel line; private int iline = 0; private JMenuBar bar;
  {
    // Builds the widget
    setLayout(new BorderLayout());
    bar = new JMenuBar();
    add(bar, BorderLayout.NORTH);
    pane = new JTextArea();
    pane.setEditable(true);
    pane.setFont(new Font("Dialog", Font.PLAIN, 16));
    JScrollPane scroll = new JScrollPane(pane);
    add(scroll, BorderLayout.CENTER);

    // Defines the line number tracker
    {
      bar.add(line = new JLabel("ligne :   0 | "));
      pane.addCaretListener(new CaretListener() {
	  public void caretUpdate(CaretEvent e) {
	    try { 
	      int l = 1 + pane.getLineOfOffset(e.getDot());
	      if (l != iline) {
		line.setText("ligne : " + (l < 10 ? "  " : l < 100 ? " " : "") + l + " | ");
		iline = l;
	      }
	    } catch(Exception err) {
	      line.setText(err.getMessage());
	    }
	  }});
    }
    
    // Defines the Edit menu 
    {
      JMenu menu = new JMenu();
      menu.setText("Edition");
      bar.add(menu);
      TextUndoManager undo = new TextUndoManager(pane);
      menu.add(undo.getUndoItem());
      menu.add(undo.getRedoItem());
      menu.addSeparator();
      JMenuItem item;
      menu.add(item = new JMenuItem(getAction(pane, DefaultEditorKit.copyAction))); item.setText("^C Copy");
      menu.add(item = new JMenuItem(getAction(pane, DefaultEditorKit.cutAction))); item.setText("^X Cut");
      menu.add(item = new JMenuItem(getAction(pane, DefaultEditorKit.pasteAction))); item.setText("^V Paste");
      menu.addSeparator();
      menu.add(item = new JMenuItem(getAction(pane, DefaultEditorKit.selectAllAction))); item.setText("^A Select all");
      menu.addSeparator();     
      // Adds a print interface
      AbstractAction print = new AbstractAction("^P Print") {
	  private static final long serialVersionUID = 1L;
	  public void actionPerformed(ActionEvent evt) { 
	    doPrint();
	  }
	};
      menu.add(new JMenuItem(print));
      addBinding(pane, KeyEvent.VK_P, print);

      // Textfind/replace manager: to be improved before use
      // menu.addSeparator();
      // TextReplaceManager find = new TextReplaceManager(pane);
      // menu.add(find.getSearchItem(menu));
      // menu.add(find.getReplaceItem(menu));
      
      // Emacs like bindings: note used here !
      // addBinding(pane, KeyEvent.VK_B, getAction(pane, DefaultEditorKit.backwardAction));
      // addBinding(pane, KeyEvent.VK_F, getAction(pane, DefaultEditorKit.forwardAction));
      // addBinding(pane, KeyEvent.VK_P, getAction(pane, DefaultEditorKit.upAction));
      // addBinding(pane, KeyEvent.VK_N, getAction(pane, DefaultEditorKit.downAction));
      // addBinding(pane, KeyEvent.VK_A, getAction(pane, DefaultEditorKit.beginLineAction));
      // addBinding(pane, KeyEvent.VK_E, getAction(pane, DefaultEditorKit.endLineAction));
      // addBinding(pane, KeyEvent.VK_D, getAction(pane, DefaultEditorKit.deleteNextCharAction));

    }

    // Ajoute le formatage du code
    {
      JMenu menu = new JMenu();
      menu.setText("Reformate/Zoom");
      bar.add(menu);
      menu.add(new JMenuItem(new AbstractAction("Reformate le code") {
	  private static final long serialVersionUID = 1L;
	  public void actionPerformed(ActionEvent evt) { 
	    doReformat();
	  }}));
      menu.addSeparator();
      menu.add(new JMenuItem(new AbstractAction("Zoom -") {
	  private static final long serialVersionUID = 1L;
	  public void actionPerformed(ActionEvent evt) { 
	    pane.setFont(new Font("Dialog", Font.PLAIN, 12));
	  }}));
      menu.add(new JMenuItem(new AbstractAction("Zoom 0") {
	  private static final long serialVersionUID = 1L;
	  public void actionPerformed(ActionEvent evt) { 
	    pane.setFont(new Font("Dialog", Font.PLAIN, 16));
	  }}));
      menu.add(new JMenuItem(new AbstractAction("Zoom +") {
	  private static final long serialVersionUID = 1L;
	  public void actionPerformed(ActionEvent evt) { 
	    pane.setFont(new Font("Dialog", Font.PLAIN, 22));
	  }}));
    }

    // Defines the tool-box menu
    setProglet("");
  }

  /** Sets the insertion menu for a given proglet.
   * @param proglet The proglet currently used.
   */
  void setProglet(String proglet) {
    JMenu menu = new JMenu();
    menu.setText("Insertions");
    if (bar.getComponentCount() == 4)
      bar.remove(2); 
    bar.add(menu, 2);  
    menu.add(new JMenuItem(new InsertAction("void main",   "void main() {\n  \n}\n", 16)));
    menu.add(new JMenuItem(new InsertAction("if",          "  if() {\n  \n  } else {\n  \n  }", 5)));
    menu.add(new JMenuItem(new InsertAction("while",       "  while() {\n  \n  }", 8)));
    if (proglet == "Konsol") {
      menu.addSeparator();
      menu.add(new JMenuItem(new InsertAction("println",              "  println(\"\");", 11)));
      menu.add(new JMenuItem(new InsertAction("readString",           "  String   = readString();", 10)));
      menu.add(new JMenuItem(new InsertAction("readInteger",          "  int   = readInteger();", 7)));
      menu.add(new JMenuItem(new InsertAction("readDouble",           "  double   = readDouble();", 10)));
    }
    if (proglet == "Dicho") {
      menu.addSeparator();
      menu.add(new JMenuItem(new InsertAction("dichoLength",          " dichoLength()", 0)));
      menu.add(new JMenuItem(new InsertAction("dichoCompare",         " dichoCompare( , )", 0)));
    }
    if (proglet == "Smiley") {
      menu.addSeparator();
      menu.add(new JMenuItem(new InsertAction("smileyReset",           "  smileyReset( , );", 14)));
      menu.add(new JMenuItem(new InsertAction("smileyLoad",            "  smileyLoad( , );", 13)));
      menu.add(new JMenuItem(new InsertAction("smileySet",             "  smileySet( , , );", 12)));
      menu.add(new JMenuItem(new InsertAction("smileyGet",             " smileyGet( , )", 11)));
    }
    if (proglet == "Scope") {
      menu.addSeparator();
      menu.add(new JMenuItem(new InsertAction("scopeReset",           "  scopeReset();", 0)));
      menu.add(new JMenuItem(new InsertAction("scopeSet",             "  scopeSet( , , );", 12)));
      menu.add(new JMenuItem(new InsertAction("scopeAdd",             "  scopeAdd( , , , );", 12)));
      menu.add(new JMenuItem(new InsertAction("scopeAddLine",         "  scopeAddLine( , , , );", 16)));
      menu.add(new JMenuItem(new InsertAction("scopeAddCircle",       "  scopeAddCircle( , , );", 18)));
      menu.add(new JMenuItem(new InsertAction("scopeAddRectangle",    "  scopeAddRectangle( , , , );", 21)));
      menu.add(new JMenuItem(new InsertAction("scopeX",               "  scopeX();", 0)));
      menu.add(new JMenuItem(new InsertAction("scopeY",               "  scopeY();", 0)));
     }
    if (proglet == "Conva") {
      menu.addSeparator();
      menu.add(new JMenuItem(new InsertAction("convaOut",             " convaOut()", 0)));
      menu.add(new JMenuItem(new InsertAction("convaCompare",         " convaCompare( )", 0)));
    }
    if (proglet == "Synthe") {
      menu.addSeparator();
      menu.add(new JMenuItem(new InsertAction("synthePlay",           " synthePlay();", 0)));
      menu.add(new JMenuItem(new InsertAction("syntheSet",            " syntheSet(\" \");", 12)));
    }
    {
      menu.addSeparator();
      menu.add(new JMenuItem(new InsertAction("equal (entre String)", " equal( , );", 6)));
      menu.add(new JMenuItem(new InsertAction("pow  (x^y)",           "Math.pow( , );", 9)));
      menu.add(new JMenuItem(new InsertAction("sqrt (racine)",        "Math.sqrt( );", 10)));
      menu.add(new JMenuItem(new InsertAction("aleat entre 0 et 1",   "  double   = random();", 10)));
      menu.add(new JMenuItem(new InsertAction("echo",                   "  echo(\" \")", 8)));
      menu.add(new JMenuItem(new InsertAction("sleep",                  "  sleep()", 8)));
    }
    bar.validate();
  }

  // Defines the text reformat
  private void doReformat() {
    setText(Translator.reformat(pane.getText()));
  }

  // Defines a  text insertion
  private class InsertAction extends AbstractAction {
    private static final long serialVersionUID = 1L;
    /** Constructs an insert acton for the given word.
     * @param name   The name of the insertion.
     * @param string The string to insert.
     * @param offset The offset of the caret within the string.
     */
    public InsertAction(String name, String string, int offset) { super(name); this.string = string; this.offset = offset; } private String string; private int offset;
    public void actionPerformed(ActionEvent evt) { 
      try {
	int offset = pane.getCaretPosition();
	pane.getDocument().insertString(offset, string, null);
	pane.setCaretPosition(offset + this.offset);
      } catch(Exception e) { Proglets.report(e); }
    }
  }

  // Defines a standard text undo manager.
  private static class TextUndoManager extends UndoManager {
    private static final long serialVersionUID = 1L;
    /** Constructs a text undo/redo manager for the given pane. */
    public TextUndoManager(JTextComponent pane) {
      pane.getDocument().
	addUndoableEditListener(new UndoableEditListener() {
	    private static final long serialVersionUID = 1L;
	    public void undoableEditHappened(UndoableEditEvent e) {
	      addEdit(e.getEdit());
	      undoUpdate();
	    }
	  });
      undoUpdate();
      addBinding(pane, KeyEvent.VK_Z, undo_action);
      addBinding(pane, KeyEvent.VK_Y, redo_action);
    }
    private Action undo_action = new AbstractAction("^Z Undo") {
	private static final long serialVersionUID = 1L;
	public void actionPerformed(ActionEvent evt) { 
	  try { if (canUndo()) undo(); undoUpdate(); } catch (Exception e) { }
	}
      };
    private Action redo_action = new AbstractAction("^Y Redo") {
	private static final long serialVersionUID = 1L;
	public void actionPerformed(ActionEvent evt) { 
	  try { if (canRedo()) redo(); undoUpdate(); } catch (Exception e) { }
	}
      };
    /** Returns the undo button. */
    public JMenuItem getUndoItem() { return undo_item; }
    private JMenuItem undo_item = new JMenuItem(undo_action);
    /** Returns the redo button. */
    public JMenuItem getRedoItem() { return redo_item; }
    private JMenuItem redo_item = new JMenuItem(redo_action);
    private void undoUpdate() {
      if (undo_item != null) undo_item.setEnabled(canUndo());
      if (redo_item != null) redo_item.setEnabled(canRedo());
    }
  }

  // Defines the text print in Jdk5 context
  // - http://java.sun.com/docs/books/tutorial/2d/printing/index.html
  private void doPrint() {
    new Thread(new Runnable() { public void run() {
      System.out.println("Lancement de l'impression . . ");
      try {
	Printable printable = new Printable() {
	    public int print(Graphics g, PageFormat f, int page) {
	      if (page > 0) {
		return NO_SUCH_PAGE;
	      } else {
		Graphics2D g2d = (Graphics2D) g;
		g2d.translate(f.getImageableX(), f.getImageableY());
		pane.print(g2d);
		return PAGE_EXISTS;
	      }
	    }
	  };
	PrinterJob job = PrinterJob.getPrinterJob();
	job.setPrintable(printable);
	if (job.printDialog())
	  job.print();
      } catch(Exception e) {
	System.out.println("Echec de l'impression ("+e+") !");
      }
    }}).start();
  }

  /* Defines a text search and replace manager.
  private static class TextReplaceManager {
    private static final long serialVersionUID = 1L;
    /** Constructs a text search/replace manager for the given pane. * /
    public TextReplaceManager(JTextComponent pane) {
      this.pane = pane;
      addBinding(pane, KeyEvent.VK_S, search_action);
      addBinding(pane, KeyEvent.VK_R, replace_action);
    }
    private JTextComponent pane;
    private Action search_action = new AbstractAction("^S   Search:") {
	private static final long serialVersionUID = 1L;
	public void actionPerformed(ActionEvent e) { 
	  if (search != null)
	    doFindReplace(search.getText(), null);
	}
      };
    private Action replace_action = new AbstractAction("^R Replace:") {
	private static final long serialVersionUID = 1L;
	public void actionPerformed(ActionEvent e) {
	  if ((search != null) && (replace != null))
	    doFindReplace(search.getText(), replace.getText());
	}
      };
    /** Returns the search button. * /
    public JPanel getSearchItem(JMenu menu) {
      if (search == null)
	search = new JMenuField(menu, "^S   Search", search_action);
      return search;
    }
    /** Returns the replace button. * /
    public JPanel getReplaceItem(JMenu menu) {
      if (replace == null)
	replace = new JMenuField(menu, "^R Replace", replace_action);
      return replace;
    }
    private JMenuField search = null, replace = null;

    /** Closes a menu and its sub-menu. * /
    public static void close(JMenu menu) {
      menu.setPopupMenuVisible(false);
      menu.setSelected(false);
      for (int i = 0; i < menu.getItemCount(); i++)
	if (menu.getItem(i) instanceof JMenu) 
	  close((JMenu) menu.getItem(i));
    }

    private boolean doFindReplace(String source, String target) {
      if ((source == null) || (source.length() == 0)) return false;
      int i0 = pane.getCaretPosition();
      if (target != null) { i0 -= source.length(); if (i0 < 0) i0 = 0; }
      int  l = pane.getDocument().getEndPosition().getOffset();
      for (int i = i0, j = i0 + source.length(); j < l; i++, j++) {
	pane.setCaretPosition(i); pane.moveCaretPosition(j);
	if (pane.getSelectedText().equals(source)) {
	  pane.setSelectionStart(i);
	  pane.setSelectionEnd(j);
	  pane.requestFocus();
	  if (target != null) {
	    pane.replaceSelection(target);
	    pane.setSelectionStart(i);
	    pane.setSelectionEnd(i+target.length());
	  }
	  return true;
	}
      }
      pane.setCaretPosition(0);
      pane.setSelectionStart(0);
      pane.setSelectionEnd(0); 
      return false;
    }

    /** Defines a menu textual field. * /
    static class JMenuField extends JPanel {
      private static final long serialVersionUID = 1L;
      /** Constructs a menu textual field.
       * @param menu this textual field is added to.
       * @param name Field name.
       * @param action Action to be fired when the textual field is input.
       * /
      public JMenuField(JMenu menu, String name, Action action) { 
	this.menu = menu;
	add(new JLabel(name));
	this.action = action;
	add(field =  new JTextField(12) {
	    private static final long serialVersionUID = 1L;
	    {
	      addActionListener(JMenuField.this.action);
	      addActionListener(new AbstractAction() { 
		  private static final long serialVersionUID = 1L;
		  public void actionPerformed(ActionEvent e) { 
		    close(JMenuField.this.menu);
		  }});
	    }});
      }
      /** Returns the input textual field. * /
      public String getText() { return field.getText(); }
      private JMenu menu; private Action action; private JTextField field;
    }
  }
  */
  
  // Adds a key binding
  private static void addBinding(JTextComponent pane, int key, Action action) {
    pane.getKeymap().addActionForKeyStroke(KeyStroke.getKeyStroke(key, Event.CTRL_MASK), action);
  }
  // Returns an action from its name
  private static Action getAction(JTextComponent pane, String action) {
    Action[] a = pane.getActions(); for (int i = 0; i < a.length; i++) if (a[i].getValue(Action.NAME).equals(action))  return a[i]; return null;
  }
}
