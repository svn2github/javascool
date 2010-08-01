/*******************************************************************************
 * Hamdi.Ben_Abdallah@inria.fr, Copyright (C) 2009.  All rights reserved.      *
 *******************************************************************************/

package org.javascool;

// Used to define the widget
import javax.swing.JTextPane;

// Used to define the styles
import javax.swing.text.StyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import java.awt.Color;
import java.awt.Font;

// Used to manage the colorization
import javax.swing.text.Segment;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;

/** This widget defines a colored editor for the proglet source editor.
 * @see <a href="../../org/javascool/ColorTextPane.java">source code</a>
 */
public class ColorTextPane extends JTextPane implements Widget {
  private static final long serialVersionUID = 1L;

  public ColorTextPane() {
    doc = getStyledDocument();

    // Defines the colorization styles
    {
      setFont(new Font("Courier", getFont().getStyle(), getFont().getSize()));
      setCaretColor(Color.BLUE);
        
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
    }

    // Adds the listener which is going to colorize after a key is entered
    addKeyListener(new KeyListener() {
	public void keyPressed(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {}
	// Here colorization is required in a window {-50 .. 50} around the caret position
	public void keyReleased(KeyEvent e) { colorize(getCaretPosition() - 50, 100); }
      }); 
    // Adds the listener which is going to colorize after the document is modified
    doc.addDocumentListener(new DocumentListener() {
	public void changedUpdate(DocumentEvent e) { }
	// Here colorization must be postponed and globalized to avoid write lock and offset/length incoherence
	public void insertUpdate(DocumentEvent e) { recolorize = true ; }
	public void removeUpdate(DocumentEvent e) { recolorize = true ; }
      }); 
  }
  // Reference to this document
  private StyledDocument doc;
  // Defined styles
  private Style NormalStyle, CodeStyle, OperatorStyle, NameStyle, StringStyle, CommentStyle;

  // Interface with the text modification routines    
  public void setText(String text) { super.setText(text); colorize(0, 0); }

  // Colorizes a part of the text
  private void colorize(int offset, int length) {
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
    colorize(text);
  }
  // Global recolorization flag
  private boolean recolorize = false;

  // Colorizes a text's segment
  private void colorize(Segment text) {
    String string = new String(text.array, text.offset, text.count);
    // Resets the colorization
    doc.setCharacterAttributes(text.offset, text.count, NormalStyle, true);
    // Colorizes names : put 1st to make reserved/declared words overwrite it
    colorizeNames(text);
    // Colorizes all reserved and declared words
    for(String word : Jvs2Java.Reserved) 
      colorizeWord(word, text, string);
    for(String word : Jvs2Java.Declared)
      colorizeWord(word, text, string);
    // Colorizes operators
    colorizeOperators(text);
    // Colorizes strings : put at last, to cover other colorization
    colorizeStrings(text);
    // Colorizes comments : put at last of the last, to cover other colorization
    colorizeComments(text);
  }

  // Colorizes reserved/declared words
  private void colorizeWord(String word, Segment text, String string) {
    // Searhs all words occurences in the text
    for(int i = 0, j; (j = string.indexOf(word, i)) != -1;) { i = j + word.length();
      // Checks the word bound, avoiding to consider a charsequence within a word
      if ((j == 0 || (!isWordChar(string.charAt(j-1)))) && (i == string.length() || (!isWordChar(string.charAt(i))))) {
	doc.setCharacterAttributes(j + text.offset, word.length(), CodeStyle, true);
      }
    }
  }

  // Colorizes operators
  private void colorizeOperators(Segment text) {
    for(int i = text.offset, n = 0; n < text.count; i++, n++) {
      switch(text.array[i]) {
      case '+': case '-': case '*': case '/': case '%': 
      case '|': case '&': case '!': 
      case '=': case '(': case ')': case'{': case'}': case '[': case ']': 
	doc.setCharacterAttributes(i, 1, OperatorStyle, true);
      }
    }
  }

  // Colorizes the quoted strings of the "([^"]|\")*"
  private void colorizeStrings(Segment text) {
    boolean quoted = false; int j = 0;
    for(int i = text.offset, n = 0; n < text.count; i++, n++) {
      if ((text.array[i] == '"' && (i == 0 || text.array[i - 1] != '\\')) || (quoted && text.array[i] == '\n')) {
	if (quoted) {
	  doc.setCharacterAttributes(j, i - j + 1, StringStyle, true);
	  quoted = false;
	} else {
	  j = i;
	  quoted = true;
	}
      } 
    }
  }

  // Colorizes comments of the form (//.*\n|/\*.*\*/)
  private void colorizeComments(Segment text) {
    int comment = 0; int j = 0;
    for(int i = text.offset, n = 0; n < text.count; i++, n++)  {
      if (comment == 0) {
	if (i > 0 && text.array[i - 1] == '/' && text.array[i] == '/') {
	  j = i - 1;
	  comment = 1;
	} else if (i > 0 && text.array[i - 1] == '/' && text.array[i] == '*') {
	  j = i - 1;
	  comment = -1;
	}
      } else if ((comment == 1 && text.array[i] == '\n') ||
		 (comment == -1 && i > 0 && text.array[i - 1] == '*' && text.array[i] == '/')) {
	comment = 0;
	doc.setCharacterAttributes(j, i - j + 1, CommentStyle, true);
      }
    }
  }

  // Colorizes the variables names before [={] operators
  private void colorizeNames(Segment text) {
    for(int i = text.offset, n = 0; n < text.count; i++, n++) {  
      switch(text.array[i]) { 
      case '=': case '{': 
	// Looks for the previous word and colorizes it if any
	int i1 = i - 1; while(i1 > 0 && Character.isWhitespace(text.array[i1])) i1--;
	if (i1 > 0 && isWordChar(text.array[i1])) {
	  int i0 = i1 - 1; while(i0 > 0 && isWordChar(text.array[i0])) i0--;
	  doc.setCharacterAttributes(i0, i1 - i0 + 1, NameStyle, true);
	}
      }
    }
  }

  // Tests if the char belongs to a word
  private static boolean isWordChar(char c) { return  Character.isLetterOrDigit(c) || c == '_'; }
}

