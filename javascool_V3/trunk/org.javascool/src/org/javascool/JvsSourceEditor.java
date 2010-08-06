/*******************************************************************************
 * Hamdi.Ben_Abdallah@inria.fr, Copyright (C) 2009.  All rights reserved.      *
 *******************************************************************************/

package org.javascool;


/** This widget defines the proglet source editor.
 * @see <a href="../../org/javasccol/SourceEditor.java">source code</a>
 */
public class JvsSourceEditor extends SourceEditor implements Widget {
  private static final long serialVersionUID = 1L;

  {

    // Defines the tool-box menu
    setProglet("");
  }

  /** Sets the insertion menu for a given proglet.
   * @param proglet The proglet currently used.
   */
  void setProglet(String proglet) {
    addInsertion("void main",   "void main() {\n  \n}\n", 16);
    addInsertion("if",          "  if() {\n  \n  } else {\n  \n  }", 5);
    addInsertion("while",       "  while() {\n  \n  }", 8);
    if (proglet == "Konsol") {
      addInsertionSeparator();
      addInsertion("println",              "  println(\"\");", 11);
      addInsertion("readString",           "  String   = readString();", 10);
      addInsertion("readInteger",          "  int   = readInteger();", 7);
      addInsertion("readDouble",           "  double   = readDouble();", 10);
    }
    if (proglet == "Dicho") {
      addInsertionSeparator();
      addInsertion("dichoLength",          " dichoLength()", 0);
      addInsertion("dichoCompare",         " dichoCompare( , )", 0);
    }
    if (proglet == "Smiley") {
      addInsertionSeparator();
      addInsertion("smileyReset",           "  smileyReset( , );", 14);
      addInsertion("smileyLoad",            "  smileyLoad( , );", 13);
      addInsertion("smileySet",             "  smileySet( , , );", 12);
      addInsertion("smileyGet",             " smileyGet( , )", 11);
    }
    if (proglet == "Scope") {
      addInsertionSeparator();
      addInsertion("scopeReset",           "  scopeReset();", 0);
      addInsertion("scopeSet",             "  scopeSet( , , );", 12);
      addInsertion("scopeAdd",             "  scopeAdd( , , , );", 12);
      addInsertion("scopeAddLine",         "  scopeAddLine( , , , );", 16);
      addInsertion("scopeAddCircle",       "  scopeAddCircle( , , );", 18);
      addInsertion("scopeAddRectangle",    "  scopeAddRectangle( , , , );", 21);
      addInsertion("scopeX",               "  scopeX();", 0);
      addInsertion("scopeY",               "  scopeY();", 0);
     }
    if (proglet == "Conva") {
      addInsertionSeparator();
      addInsertion("convaOut",             " convaOut()", 0);
      addInsertion("convaCompare",         " convaCompare( )", 0);
    }
    if (proglet == "Synthe") {
      addInsertionSeparator();
      addInsertion("synthePlay",           " synthePlay();", 0);
      addInsertion("syntheSet",            " syntheSet(\" \");", 12);
    }
    {
      addInsertionSeparator();
      addInsertion("equal (entre String)", " equal( , );", 6);
      addInsertion("pow  (x^y)",           "Math.pow( , );", 9);
      addInsertion("sqrt (racine)",        "Math.sqrt( );", 10);
      addInsertion("aleat entre 0 et 1",   "  double   = random();", 10);
      addInsertion("echo",                   "  echo(\" \");", 8);
      addInsertion("sleep",                  "  sleep();", 8);
    }
  }

  public void doReformat() {
    setText(Jvs2Java.reformat(getText()));
  }
}
