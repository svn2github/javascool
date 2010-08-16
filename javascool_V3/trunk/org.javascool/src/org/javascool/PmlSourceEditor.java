/*******************************************************************************
 * Philippe.Vienne@linux-azur.org, Copyright (C) 2010.  All rights reserved.   *
 *******************************************************************************/

package org.javascool;

// Used to manage the colorization
import javax.swing.text.Segment;

/** This widget defines a Pml structure source editor.
 * @see <a href="doc-files/about-keystrokes.htm">key-strokes (in French)</a>
 * @see <a href="PmlSourceEditor.java.html">source code</a>
 * @serial exclude
 */
public class PmlSourceEditor extends SourceEditor implements Widget {
  private static final long serialVersionUID = 1L;

  public void doReformat() { 
    // A definir
  }

  public void doColorize(Segment text) {
    String string = new String(text.array, text.offset, text.count);
    // Resets the colorization
    setCharacterAttributes(text.offset, text.count, NormalStyle);
    // Colorizes names : put 1st to make reserved/declared words overwrite it
    doColorizeNames(text);
    // Colorizes operators
    doColorizeOperators(text);
    // Colorizes strings : put at last, to cover other colorization
    doColorizeStrings(text);
    // Colorizes comments : put at last of the last, to cover other colorization
    doColorizeComments(text);
  }
  // Colorizes reserved/declared words
  private void doColorizeWord(String word, Segment text, String string) {
    // Search all words occurences in the text
    for(int i = 0, j; (j = string.indexOf(word, i)) != -1;) { i = j + word.length();
      // Checks the word bound, avoiding to consider a charsequence within a word
      if ((j == 0 || (!isWordChar(string.charAt(j-1)))) && (i == string.length() || (!isWordChar(string.charAt(i))))) {
	setCharacterAttributes(j + text.offset, word.length(), CodeStyle);
      }
    }
  }
  
  // Colorizes operators
  private void doColorizeOperators(Segment text) {
    for(int i = text.offset, n = 0; n < text.count; i++, n++) {
      switch(text.array[i]) {
      case '+': case '-': case '*': case '/': case '%': 
      case '|': case '&': case '!': 
      case '=': case '(': case ')': case'{': case'}': case '[': case ']': 
	setCharacterAttributes(i, 1, OperatorStyle);
      }
    }
  }
  
  // Colorizes the quoted strings of the "([^"]|\")*"
  private void doColorizeStrings(Segment text) {
    boolean quoted = false; int j = 0;
    for(int i = text.offset, n = 0; n < text.count; i++, n++) {
      if ((text.array[i] == '"' && (i == 0 || text.array[i - 1] != '\\')) || (quoted && text.array[i] == '\n')) {
	if (quoted) {
	  setCharacterAttributes(j, i - j + 1, StringStyle);
	  quoted = false;
	} else {
	  j = i;
	  quoted = true;
	}
      } 
    }
  }
  
  // Colorizes comments of the form (//.*\n|/\*.*\*/)
  private void doColorizeComments(Segment text) {
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
	setCharacterAttributes(j, i - j + 1, CommentStyle);
      }
    }
  }
  
  // Colorizes the variables names before [={] operators
  private void doColorizeNames(Segment text) {
    for(int i = text.offset, n = 0; n < text.count; i++, n++) {  
      switch(text.array[i]) { 
      case '=': case '{': 
	// Looks for the previous word and colorizes it if any
	int i1 = i - 1; while(i1 > 0 && Character.isWhitespace(text.array[i1])) i1--;
	if (i1 > 0 && isWordChar(text.array[i1])) {
	  int i0 = i1 - 1; while(i0 > 0 && isWordChar(text.array[i0])) i0--;
	  setCharacterAttributes(i0, i1 - i0 + 1, NameStyle);
	}
      }
    }
  }
  private static boolean isWordChar(char c) { return  Character.isLetterOrDigit(c) || c == '_'; }
}
