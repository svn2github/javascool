/*******************************************************************************
* Hamdi.Ben_Abdallah@inria.fr, Copyright (C) 2009.  All rights reserved.      *
*******************************************************************************/

package org.javascool;

// Used to manage the colorization
import javax.swing.text.Segment;

/** This widget defines a Jvs code source editor.
 * @see <a href="doc-files/about-keystrokes.htm">key-strokes (in French)</a>
 * @see <a href="JvsSourceEditor.java.html">source code</a>
 * @see Jvs2Java
 * @serial exclude
 */
public class JvsSourceEditor extends SourceEditor implements Widget {
  private static final long serialVersionUID = 1L;

  /** Sets the insertion menu for a given proglet.
   * @param proglet The proglet currently used.
   */
  public void setProglet(String proglet) {
    resetInsertion();
    addInsertion("void main", "void main() {\n  \n}\n", 16);
    addInsertion("if", "  if() {\n  \n  } else {\n  \n  }", 5);
    addInsertion("while", "  while() {\n  \n  }", 8);
    if(proglet.equals("ingredients")) {
      addInsertionSeparator();
      addInsertion("println", "  println(\"\");", 11);
      addInsertion("readString", "  String   = readString();", 10);
      addInsertion("readInteger", "  int   = readInteger();", 7);
      addInsertion("readDouble", "  double   = readDouble();", 10);
    }
    if(proglet.equals("dichotomie")) {
      addInsertionSeparator();
      addInsertion("dichoLength", " dichoLength()", 0);
      addInsertion("dichoCompare", " dichoCompare( , )", 0);
    }
    if(proglet.equals("pixelsetcie")) {
      addInsertionSeparator();
      addInsertion("smileyReset", "  smileyReset( , );", 14);
      addInsertion("smileyLoad", "  smileyLoad( , );", 13);
      addInsertion("smileySet", "  smileySet( , , );", 12);
      addInsertion("smileyGet", " smileyGet( , )", 11);
    }
    if(proglet.equals("exosdemaths")) {
      addInsertionSeparator();
      addInsertion("scopeReset", "  scopeReset();", 0);
      addInsertion("scopeSet", "  scopeSet( , , );", 12);
      addInsertion("scopeAdd", "  scopeAdd( , , , );", 12);
      addInsertion("scopeAddLine", "  scopeAddLine( , , , );", 16);
      addInsertion("scopeAddCircle", "  scopeAddCircle( , , );", 18);
      addInsertion("scopeAddRectangle", "  scopeAddRectangle( , , , );", 21);
      addInsertion("scopeAddString", "  scopeAddString( , , , );", 18);
      addInsertion("scopeX", "  scopeX();", 0);
      addInsertion("scopeY", "  scopeY();", 0);
    }
    if(proglet.equals("convanalogique")) {
      addInsertionSeparator();
      addInsertion("convaOut", " convaOut()", 0);
      addInsertion("convaCompare", " convaCompare( )", 0);
    }
    if(proglet.equals("syntheson")) {
      addInsertionSeparator();
      addInsertion("synthePlay", " synthePlay();", 0);
      addInsertion("syntheSet", " syntheSet(\" \");", 12);
    }
    if(proglet.equals("ExplorationSonore")) {
      addInsertionSeparator();
      addInsertion("playSignal", " playSignal(\" \", , );", 13);
      addInsertion("playRecord", " playRecord(\" \", );", 14);
      addInsertion("playStop", " playStrop();", 0);
    }
    {
      addInsertionSeparator();
      addInsertion("equal (entre String)", " equal( , );", 6);
      addInsertion("pow  (x^y)", "Math.pow( , );", 9);
      addInsertion("sqrt (racine)", "Math.sqrt( );", 10);
      addInsertion("aleat entre 0 et 1", "  double   = random();", 10);
      addInsertion("echo", "  echo(\" \");", 8);
      addInsertion("sleep", "  sleep();", 8);
    }
  }
  public void doReformat() {
    setText(Jvs2Java.reformat(getText()));
  }
  public void doColorize(int position, Segment text) {
    String string = new String(text.array, text.offset, text.count);
    // Resets the colorization
    setCharacterAttributes(text.offset, text.count, NormalStyle);
    // Colorizes active block
    doColorizeActiveBlock(position, text);
    // Colorizes names : put 1st to make reserved/declared words overwrite it
    doColorizeNames(text);
    // Colorizes all reserved and declared words
    for(String word : Jvs2Java.Reserved)
      doColorizeWord(word, text, string);
    for(String word : Jvs2Java.Declared)
      doColorizeWord(word, text, string);
    // Colorizes operators
    doColorizeOperators(text);
    // Colorizes comments : put at last, to cover other colorization
    doColorizeComments(text);
    // Colorizes strings : put at last of the last, to cover other colorization
    doColorizeStrings(text);
  }
  // Colorizes reserved/declared words
  private void doColorizeWord(String word, Segment text, String string) {
    // Search all words occurences in the text
    for(int i = 0, j; (j = string.indexOf(word, i)) != -1;) {
      i = j + word.length();
      // Checks the word bound, avoiding to consider a charsequence within a word
      if(((j == 0) || (!isWordChar(string.charAt(j - 1)))) && ((i == string.length()) || (!isWordChar(string.charAt(i)))))
        setCharacterAttributes(j + text.offset, word.length(), CodeStyle);
    }
  }
  // Colorizes operators
  private void doColorizeOperators(Segment text) {
    for(int i = text.offset, n = 0; n < text.count; i++, n++) {
      switch(text.array[i]) {
      case '+':
      case '-':
      case '*':
      case '/':
      case '%':
      case '|':
      case '&':
      case '!':
      case '=':
      case '(':
      case ')':
      case '[':
      case ']':
        setCharacterAttributes(i, 1, OperatorStyle);
	break;
      case '{':
      case '}':
        setCharacterAttributes(i, 1, BracketStyle);
	break;
      }
    }
  }
  // Colorizes the quoted strings of the "([^"]|\")*"
  private void doColorizeStrings(Segment text) {
    boolean quoted = false;
    int j = 0;
    for(int i = text.offset, n = 0; n < text.count; i++, n++)
      if(((text.array[i] == '"') && ((i == 0) || (text.array[i - 1] != '\\'))) || (quoted && (text.array[i] == '\n'))) {
        if(quoted) {
          setCharacterAttributes(j, i - j + 1, StringStyle);
          quoted = false;
        } else {
          j = i;
          quoted = true;
        }
      }
  }
  // Colorizes comments of the form (//.*\n|/\*.*\*/)
  private void doColorizeComments(Segment text) {
    int comment = 0;
    int j = 0;
    for(int i = text.offset, n = 0; n < text.count; i++, n++) {
      if(comment == 0) {
        if((i > 0) && (text.array[i - 1] == '/') && (text.array[i] == '/')) {
	  // Test that the // is not in a string
	  boolean string = false; 
	  for(int k = i - 1; k >= 0 && text.array[k] != '\n'; k--)
	    if (text.array[k] == '"') {
	      if (string && k > 0 && text.array[k] != '\\')
		string = false;
	      else if (!string)
		string = true;
	    }
	  if (!string) {
	    j = i - 1;
	    comment = 1;
	  }
        } else if((i > 0) && (text.array[i - 1] == '/') && (text.array[i] == '*')) {
          j = i - 1;
          comment = -1;
        }
      } else if(((comment == 1) && (text.array[i] == '\n')) ||
                ((comment == -1) && (i > 0) && (text.array[i - 1] == '*') && (text.array[i] == '/')))
      {
        comment = 0;
        setCharacterAttributes(j, i - j + 1, CommentStyle);
      }
    }
  }
  // Colorizes the variables names before [={] operators
  private void doColorizeNames(Segment text) {
    for(int i = text.offset, n = 0; n < text.count; i++, n++) {
      switch(text.array[i]) {
      case '=':
      case '{':
        // Looks for the previous word and colorizes it if any
        int i1 = i - 1;
        while(i1 > 0 && Character.isWhitespace(text.array[i1]))
          i1--;
        if((i1 > 0) && isWordChar(text.array[i1])) {
          int i0 = i1 - 1;
          while(i0 > 0 && isWordChar(text.array[i0]))
            i0--;
          setCharacterAttributes(i0, i1 - i0 + 1, NameStyle);
        }
      }
    }
  }
  // Tests if the char belongs to a word
  private static boolean isWordChar(char c) {
    return Character.isLetterOrDigit(c) || c == '_';
  }
  // Colorizes the active block
  private void doColorizeActiveBlock(int position, Segment text) {
    int ileft, iright, level;
    if (position > 0 && text.array[position-1] == '}') position -= 2;
    for(ileft = position, level = 0; 0 <= ileft && 0 <= level; ileft--)
      level = text.array[ileft] == '}' ? level+1 : text.array[ileft] == '{' ? level-1 : level;
    for(iright = position, level = 0; iright < text.array.length && 0 <= level; iright++)
      level = text.array[iright] == '{' ? level+1 : text.array[iright] == '}' ? level-1 : level;
    if (ileft > 0) {
      setCharacterAttributes(ileft+1, 2, ActiveBlockStyle);
      setCharacterAttributes(iright-1, 2, ActiveBlockStyle);
    }
  }
  /** Shows a Jvs source file.
   * @param usage <tt>java org.javascool.JvsSourceEditor location</tt>
   */
  public static void main(String[] usage) {
    if(usage.length > 0)
      Utils.show((SourceEditor) new JvsSourceEditor().reset(true).setText(Utils.loadString(usage[0])), "Jvs Source File Shower", 1024, 800);
  }
}

