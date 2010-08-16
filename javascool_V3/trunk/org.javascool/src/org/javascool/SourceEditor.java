/*******************************************************************************
 * Hamdi.Ben_Abdallah@inria.fr, Copyright (C) 2009.  All rights reserved.      *
 *******************************************************************************/

package org.javascool;

// Used to define the widget
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.text.JTextComponent;
import javax.swing.text.html.HTMLDocument;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import java.awt.Dimension;
import javax.swing.JLabel;

// Used to define menu and menu's action
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.Action;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;

// Used for key binding
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

// Used to define the widget
import javax.swing.JTextPane;

// Used to define the styles
import javax.swing.text.StyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import java.awt.Color;
import java.awt.Font;
import java.util.HashMap;

// Used to manage the colorization
import javax.swing.text.Segment;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;

/** This widget defines a general proglet source editor.
 * @see <a href="doc-files/about-keystrokes.htm">key-strokes (in French)</a>
 * @see <a href="SourceEditor.java.html">source code</a>
 * @serial exclude
 */
public class SourceEditor extends JPanel implements Widget,Editor {
  private static final long serialVersionUID = 1L;

  // Implements the org.javascool interface
  public String getText() { return pane.getText(); }
  public void setText(String text) { pane.setText(text); pane.setCaretPosition(0); doColorize(0, 0); }

  // Reference to this document with its menu-bar and contained
  private JMenuBar bar; private JTextPane pane; private JScrollPane scroll; private StyledDocument doc;
  // Line counting management
  private JLabel line; private int iline = 0; 
  // Widget construction
  {
    // Builds the widget
    setLayout(new BorderLayout());
    bar = new JMenuBar();
    add(bar, BorderLayout.NORTH);
    pane = new JTextPane();
    pane.setEditable(true);
    pane.setFont(new Font("Dialog", Font.PLAIN, 16));
    doc = pane.getStyledDocument();
    scroll = new JScrollPane(pane, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
    add(scroll, BorderLayout.CENTER);
    // Defines the line number tracker
    {
      bar.add(line = new JLabel("ligne :   0 | "));
      pane.addCaretListener(new CaretListener() {
	  public void caretUpdate(CaretEvent e) {
	    try { 
	      int l = pane.getDocument().getRootElements()[0].getElementIndex(pane.getCaretPosition()) + 1;
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
      menu.add(item = new JMenuItem(getAction(pane, DefaultEditorKit.copyAction))); item.setText("Copier");
      menu.add(item = new JMenuItem(getAction(pane, DefaultEditorKit.cutAction))); item.setText("Couper");
      menu.add(item = new JMenuItem(getAction(pane, DefaultEditorKit.pasteAction))); item.setText("Coller");
      menu.addSeparator();
      menu.add(item = new JMenuItem(getAction(pane, DefaultEditorKit.selectAllAction))); item.setText("Tout sélectionner");
      menu.addSeparator();     
      // Adds a print interface
      AbstractAction print = new AbstractAction("Imprimer") {
	  private static final long serialVersionUID = 1L;
	  public void actionPerformed(ActionEvent evt) { 
	    doPrint();
	  }
	};
      menu.add(new JMenuItem(print));
      // addBinding(pane, KeyEvent.VK_P, print);

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

    // Defines the view menu
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
  }

  /** Defines the text reformat mechanism.
   * To be overloaded to define the proper reformat mechanism
   */
  public void doReformat() { }

  /** Resets the predefined insertion in the insertion menu. */
  public void resetInsertion() {
    if (imenu != null) { bar.remove(imenu); imenu = null; }
  }

  /** Adds an predefined insertion in the insertion menu. 
   * @param label The insertion label.
   * @param text The insertion text.
   * @param offset The carret offset in the inserted text.
   */
  public void addInsertion(String label, String text, int offset) {
    // Adds the menu if not yet done
    if (imenu == null) { 
      imenu = new JMenu();
      imenu.setText("Insertions");
      if (bar.getComponentCount() == 4)
	bar.remove(2); 
      bar.add(imenu, 2);  
    }
    imenu.add(new JMenuItem(new InsertAction(label, text, offset)));
    bar.validate();
  }

  /** Adds a separator in the insertion menu.  */
  public void addInsertionSeparator() {
    if (imenu != null) imenu.addSeparator();
  }

  private JMenu imenu = null;

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
      } catch(Exception e) { System.err.println(e); }
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
    private Action undo_action = new AbstractAction("Annuler") {
	private static final long serialVersionUID = 1L;
	public void actionPerformed(ActionEvent evt) { 
	  try { if (canUndo()) undo(); undoUpdate(); } catch (Exception e) { }
	}
      };
    private Action redo_action = new AbstractAction("Restaurer") {
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
    private Action search_action = new AbstractAction("Chercher") {
	private static final long serialVersionUID = 1L;
	public void actionPerformed(ActionEvent e) { 
	  if (search != null)
	    doFindReplace(search.getText(), null);
	}
      };
    private Action replace_action = new AbstractAction("Remplacer") {
	private static final long serialVersionUID = 1L;
	public void actionPerformed(ActionEvent e) {
	  if ((search != null) && (replace != null))
	    doFindReplace(search.getText(), replace.getText());
	}
      };
    /** Returns the search button. * /
    public JPanel getSearchItem(JMenu menu) {
      if (search == null)
	search = new JMenuField(menu, "Chercher", search_action);
      return search;
    }
    /** Returns the replace button. * /
    public JPanel getReplaceItem(JMenu menu) {
      if (replace == null)
      replace = new JMenuField(menu, "Remplacer", replace_action);
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
    pane.getKeymap().addActionForKeyStroke(KeyStroke.getKeyStroke(key, KeyEvent.CTRL_MASK), action);
  }
  // Returns an action from its name
  private static Action getAction(JTextComponent pane, String action) {
    Action[] a = pane.getActions(); for (int i = 0; i < a.length; i++) if (a[i].getValue(Action.NAME).equals(action))  return a[i]; return null;
  }

  /** Predefined coolorization style. */
  public final Style NormalStyle, CodeStyle, OperatorStyle, NameStyle, StringStyle, CommentStyle;
  // Defines the colorization styles
  {
    pane.setCaretColor(Color.BLUE);
    
    // Style Normal: it must ``cancel´´ all other style effects
    NormalStyle = doc.addStyle("Normal", null);
    StyleConstants.setForeground(NormalStyle, Color.BLACK);
    StyleConstants.setBold(NormalStyle, false);
    
    // Style Code: for reserved words
    CodeStyle = doc.addStyle("Code", null);
    StyleConstants.setForeground(CodeStyle, new Color(0xaa4444)); // Orange
    StyleConstants.setBold(CodeStyle, true);
    
    // Style String: for quoted strings
    StringStyle = doc.addStyle("String", null);
    StyleConstants.setForeground(StringStyle, new Color(0x008800)); // Green
    StyleConstants.setBold(StringStyle, false);
    
    // Style Operator: for operators chars
    OperatorStyle = doc.addStyle("Operateur", null);
    StyleConstants.setForeground(OperatorStyle, Color.BLACK);
    StyleConstants.setBold(OperatorStyle, true);
    
    // Style Name: for identificator of declared variables (used in BML)
    NameStyle = doc.addStyle("Name", null);
    StyleConstants.setForeground(NameStyle, Color.GRAY);
    StyleConstants.setBold(NameStyle, true);
    
    // Style Comment: for comments added to the text
    CommentStyle = doc.addStyle("Comment", null);
    StyleConstants.setForeground(CommentStyle, new Color(0x0000ee)); // Blue
    StyleConstants.setBold(CommentStyle, true);
  
    // Adds the listener which is going to colorize after a key is entered
    pane.addKeyListener(new KeyListener() {
	public void keyPressed(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {}
	// Here colorization is required in a window {-50 .. 50} around the caret position
	public void keyReleased(KeyEvent e) { doColorize(pane.getCaretPosition() - 50, 100); }
      }); 
    // Adds the listener which is going to colorize after the document is modified
    doc.addDocumentListener(new DocumentListener() {
	public void changedUpdate(DocumentEvent e) { }
	// Here colorization must be postponed and globalized to avoid write lock and offset/length incoherence
	public void insertUpdate(DocumentEvent e) { recolorize = true ; }
	public void removeUpdate(DocumentEvent e) { recolorize = true ; }
      }); 
  }

  /** Colorizes a text's segment
   * @param text The text segment to [re]colorize.
   */
  public void doColorize(Segment text) {  }

  /** Sets the content element attributes in the document. 
   * @param offset The start index of the change.
   * @param count The length of the change.
   * @param style The predefined style: <tt>SourceEditor.(NormalStyle|CodeStyle|OperatorStyle|NameStyle|StringStyle|CommentStyle)</tt>
   */
  public void setCharacterAttributes(int offset, int count, Style style) {
    doc.setCharacterAttributes(offset, count, style, true);
  }

  // Colorizes a part of the text
  private void doColorize(int offset, int length) {
    // Manages a global recolorization
    if (recolorize) { offset = length = 0; recolorize = false; }
    // Gets the text to colorize and adjust the bounds to the closest beginning/end of lines
    Segment text = new Segment(); try { doc.getText(0, doc.getLength(), text); } catch(Exception e) { }
    if (offset < 0) offset = 0; 
    while(offset > 0 && text.array[offset] != '\n') offset--;
    if (length == 0) length = text.count; 
    if (offset + length > text.count) length = text.count - offset; 
    while(offset + length < text.count && text.array[offset + length - 1] != '\n') length++;
    text.offset = offset; text.count = length;
    doColorize(text);
  }
  // Global recolorization flag
  private boolean recolorize = false;
}
