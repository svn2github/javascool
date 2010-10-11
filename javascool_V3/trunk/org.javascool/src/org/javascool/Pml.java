/*******************************************************************************
 * Thierry.Vieville@sophia.inria.fr, Copyright (C) 2010.  All rights reserved. *
 *******************************************************************************/

package org.javascool;

import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Pattern;

import java.util.Vector;

/** Defines a PML: a Pml (Programmatic Markup Language) Memory Loader.
 * 
 * <p> A Pml (for «Programmatic Métadata Logicalstructure») is a Parametric Minimal Logical-structure, which parameters are <ul>
 *   <li>Its <i>Tag</i>, i.e. the name defining its type.</li>
 *   <li>Named <i>Attributes</i> values explicitly indexed by a name</li>
 *   <li>Indexed <i>Elements</i> values implicitly indexed by an integer <tt>>= 0</tt></li>
 * </ul>values being either a Pml or a String. And not more.</p>
 *
 * <p>It is used to manage non-trivial routine parameters (including <a href="Hml.html">text</a>) or to interface with other applications.</p>
 * 
 * <p>The textual syntax is of the form:
 * <div style="margin-left: 40px"><tt>"{tag name = value .. element .. }"</tt></div>where <ul>
 * <li>Pml are encapsulated with <tt>{</tt> .. <tt>}</tt>.</li>
 * <li>String with spaces or <tt>{</tt> or <tt>}</tt> chars are encapsulated with <tt>'"'</tt> (escaped with <tt>'\"'</tt>).</li>
 * </ul>This syntax is minimal, close to any bracket (<tt>C/C++, PHP, Java</tt>) language syntax, easy to read and write and completely standard.
 * <p>The parameter logical-structure parsing from a string is <i>weak</i> in the sense that a value is always derived without generating syntax errors.</p>
 * </p>
 *
 * @see <a href="Pml.java.html">source code</a>
 * @serial exclude
 */
public class Pml { /**/public Pml() { }
  private static final long serialVersionUID = 1L;

  private HashMap<String, Pml> data = new  HashMap<String, Pml>();

  /** Resets the logical-structure, parsing the given string. 
   * @param value The value following the <tt>"{tag name = value .. element .. }"</tt> syntax.
   * @param format <div id="input-format"><ul>
   * <li>"pml" Reads in pml format (default value).</li>
   * <li>"xml" Reads in XML format.</li>
   * <li>"htm" or "html" Reads in raw HTML format (without translating HTML structure to HML).</li>
   * </ul></div>
   * @return This, allowing to use the <tt>Pml pml= new Pml().reset(..)</tt> construct.
   */
  public Pml reset(String value, String format) { 
   if ("xml".equals(format)) {
      return reset(Utils.xml2xml(value, xml2pml), "pml");
    } else if ("htm".equals(format) || "html".equals(format)) {
     return reset(Utils.xml2xml(Utils.htm2xml(value), xml2pml), "pml");
    } else {
     // Initializes the Pml
     data = new  HashMap<String, Pml>(); tag = ""; parent = null; count = -1;
     // Parses the string
     new PmlReader().read(value, this);
     return this;
   }
  }  
  /**/public final Pml reset(String value) { 
    return reset(value, "pml");
  }
  /** Copies one logical-structure in this.
   * @param pml The logical-structure to copy. If null resets to an empty logical-structure.
   */
  public Pml reset(Pml pml) {
    // Initializes the Pml
    data = new  HashMap<String, Pml>(); tag = ""; parent = null; count = -1;
    if (pml != null) {
      setTag(pml.getTag());
      for(String name : pml.attributes())
	set(name, pml.getChild(name));
      for(int i = 0; i < pml.getCount(); i++)
	set(i, pml.getChild(i));
    }
    return this;
  }

  /** Loads this logical-structure structure from a location.    *
   * @param location A Universal Resource Location of the form: <table align="center"> 
   * <tr><td><tt>http:/<i>path-name</i></tt></td><td>to load from a HTTP location</td></tr>
   * <tr><td><tt>http:/<i>path-name</i>?param_i=value_i&amp;..</tt></td><td>to get a form answer</td></tr>
   * <tr><td><tt>ftp:/<i>path-name</i></tt></td><td>to load from a FTP site</td></tr>
   * <tr><td><tt>file:/<i>path-name</i></tt></td><td>to load from a file</td></tr>
   * <tr><td><tt>jar:/<i>jar-path-name</i>!/<i>jar-entry</i></tt></td><td>to load from a JAR archive
   *  <div>(e.g.:<tt>jar:http://javascool.gforge.inria.fr/javascool.jar!/META-INF/MANIFEST.MF</tt>)</div></td></tr>
   * </table>
   * @param format The <a href="#input-format">input format</a> as defined by the <a href="#reset(java.lang.String, java.lang.String)">reset()</a> routine.
   * When not specified, detect the format extension from the file extension or used "pml" by default.
   * @return This, allowing to use the <tt>Pml pml= new Pml().reset(..)</tt> construct.
   */
  public final Pml load(String location, String format) {
    return reset(Utils.loadString(location), format);
  }
  /**/public final Pml load(String location) {
    return load(location, location.replaceAll("^.*\\.([A-Za-z]+)$", "$1"));
  }
  /** Defines a token reader, reading a string word by word, normalizing spaces and quoting string with the '"' char. */
  protected static class TokenReader {
    /** Defines the information of each lexical token. */
    private static class token { String string; int line; token(String s, int i0, int i1, int l) { string = s.substring(i0, i1); line = l; } }
    Vector<token> tokens; int itoken;
    /** Resets the reader. */
    public TokenReader reset(String string) {
      // Initializes the buffer
      tokens = new Vector<token>(); itoken = 0;
      // Split the string into tokens
      {
	char[] chars = string.toCharArray(); 
	for(int ichar = 0, ln = 0; ichar < chars.length;) {
	  // Skips spaces
	  while(ichar < chars.length && Character.isWhitespace(chars[ichar])) {
	    if (chars[ichar] == '\n')
	      ln++;
	    ichar++;
	  }
	  if (ichar < chars.length) {
	    int ichar0 = ichar;
	    // Detects a quoted string taking "{" "}" and \" constructs into account
	    if (chars[ichar0] == '"') {
	      while(ichar < chars.length && (ichar == ichar0 || chars[ichar] != '"' ||  chars[ichar-1] == '\\')) ichar++; ichar++;
	      int ichar1;
	      if (ichar == ichar0 + 3 && (chars[ichar0+1] == '{' || chars[ichar0+1] == '}')) { ichar1 = ichar; } else { ichar0++; ichar1 = ichar-1; } 
	      tokens.add(new token(string, ichar0, ichar1, ln));
            // Detects a name
	    } else if(Character.isLetter(chars[ichar0]) || chars[ichar0] == '_') {
	      while(ichar < chars.length && (Character.isLetterOrDigit(chars[ichar]) || chars[ichar0] == '_')) ichar++;
	      tokens.add(new token(string, ichar0, ichar, ln));
            // Detects a number
	    } else if(Character.isDigit(chars[ichar0]) || chars[ichar0] == '.') {
	      while(ichar < chars.length && Character.isDigit(chars[ichar])) ichar++;
	      if (ichar < chars.length && chars[ichar] == '.') { ichar++;
		while(ichar < chars.length && Character.isDigit(chars[ichar])) ichar++;
	      }
	      if (ichar < chars.length && (chars[ichar] == 'E' || chars[ichar] == 'e')) { ichar++;
		if (ichar < chars.length && (chars[ichar] == '+' || chars[ichar] == '-')) ichar++;
		while(ichar < chars.length && Character.isDigit(chars[ichar])) ichar++;
	      }
	      tokens.add(new token(string, ichar0, ichar, ln));
            // Detects operators and punctuation
	    } else if (isOperator(chars[ichar0])) {
	      while(ichar < chars.length && isOperator(chars[ichar])) ichar++;
	      tokens.add(new token(string, ichar0, ichar, ln));
	    } else {
	      tokens.add(new token(string, ichar0, ++ichar, ln));
	    }
	  }
	}
      }
      itoken = 0;
      // used to test: for(token t : tokens) System.out.println(t.line+"> \""+t.string+"\""); System.out.println("..");
      return this;
    }
    private static boolean isOperator(char c) {
      switch(c) {
      case '+': case '-': case '*': case '/': case '%': case '&': case '|': case '^': case '=': case '!': case '<': case '>': case '.': case ':':
	return true;
      default:
	return false;
      }
    }
    /** Gets the current token or '}' if end of file. */
    public String current(int next) { 
      String current = itoken+next < tokens.size() ? tokens.get(itoken+next).string : "}";
      return current;
    }
    /**/public String current() { 
      return current(0);
    } 
    /** Tests if there is more tokens. */
    public boolean isNext() {
      return itoken < tokens.size();
    }
    /** Gets the next token. */
    public void next(int next) {
      itoken += next;
    }
    /**/public void next() {
      next(1);
    }
    /** Returns the string trailer. */
    public String trailer() {
      String t = ""; while(itoken < tokens.size()) t += " "+tokens.get(itoken).string; return t.trim();
    }
    /** Checks a syntax condition. */
    public void check(boolean ok, String message) {
      if (!ok) System.out.println("Erreur de syntaxe \""+message+"\", ligne "+(itoken < tokens.size() ? ""+tokens.get(itoken).line+" vers \""+current()+"\"" : "finale"));
    }
  }
  /** Defines a PML reader. */
  private static class PmlReader extends TokenReader {
    /** Parses the string and set the parameters in the pml structure. */
    public void read(String string, Pml pml) {
      reset(string);
      // Parses the string
      parse(pml);
      // Detects the trailer if any
      String trailer = trailer();
      if (trailer.length() > 0) {
	Pml p = new Pml(); p.setTag(trailer); pml.set("string_trailer", p);
      }
    }
    /** Parses recursively the string. */
    private Pml parse(Pml pml) {
      String b = current();
      // Parses a { } Pml construct
      if ("{".equals(b)) { next();
	for(boolean start = true; true; start = false) {
	  String t = current();
	  if ("}".equals(t)) {
	    next();
	    break;
	  } else if ("{".equals(t)) { 
	    Pml p = new Pml(); parse(p); pml.add(p);
	  } else if ("=".equals(current(1))) { next(2); 
	    Pml p = new Pml(); parse(p); pml.set(t, p);
	  } else if (start) { next();
	    pml.setTag(t);
	  } else {
	    pml.add(t);
	  }
	}
      // Considers the Pml as a simple string
      } else {
	pml.setTag(b); next();
      }
      return pml;
    }
  }
  protected static String xml2pml = 
    "<?xml version='1.0' encoding='utf-8'?>\n"+
    "<xsl:stylesheet xmlns:xsl='http://www.w3.org/1999/XSL/Transform' version='1.0'>\n"+
    "  <xsl:output method='xml' encoding='utf-8' omit-xml-declaration='yes'/>\n"+
    "  <xsl:template match='*'>\n"+
    "  {<xsl:value-of select='name(.)'/><xsl:text> </xsl:text>\n"+
    "    <xsl:for-each select='@*'><xsl:value-of select='name(.)'/>=\"<xsl:value-of select=\"translate(., '&quot;','¨')\"/>\"<xsl:text> </xsl:text></xsl:for-each>\n"+
    "    <xsl:apply-templates/>\n"+
    "  }</xsl:template>\n"+
    "</xsl:stylesheet>";

  /** Returns this logical-structure structure as a one-line string.
   * @param format <div id="output-format"><ul>
   * <li>"raw" To write in a normalized 1D plain text format (default).</li>
   * <li>"txt" To write in a normalized 2D plain text format.</li>
   * <li>"xml" To write in XML format, reducing tag and attribute names to valid XML names, and considering Pml without any attribute or elements as string.</li>
   * </ul></div>
   */
  public String toString(String format) { 
    return 
      "xml".equals(format) ? new XmlWriter().toString(this) :
      "raw".equals(format) ? new PlainWriter().toString(this, 0) :
      new PlainWriter().toString(this, 180);
  }
  /**/public final String toString() { 
    return toString("raw");
  }
  /** Saves this logical-structure structure into a location. 
   * @param location @optional<"stdout:"> A Universal Resource Location of the form: <table>
   * <tr><td><tt>ftp:/<i>path-name</i></tt></td><td>to save onto a FTP site.</td></tr>
   * <tr><td><tt>file:/<i>path-name</i></tt></td><td>to save into a file.</td></tr>
   * <tr><td><tt>mailto:<i>address</i>?subject=<i>subject</i></tt></td><td>to send as an email in a readable form.</td></tr>
   * <tr><td><tt>stdout:/</tt></td><td>to print to the terminal standard output.</td></tr>
   * </table>
   * @param format The <a href="#output-format">output format</a> as defined by the <a href="#toString(java.lang.String)">toString()</a> routine.
   * When not specified, detect the format extension from the file extension or used "pml" by default.
   * @return This, allowing to use the <tt>Pml pml = new Pml().reset(..)</tt> construct.
   */
  public final Pml save(String location, String format) {
    Utils.saveString(location, toString(format));
    return this;
  }
  /**/public final Pml save(String location) {
    return save(location, location.replaceAll("^.*\\.([A-Za-z]+)$", "$1"));
  }
  /** Defines a PML writer. */
  private static class PlainWriter {
    private StringBuffer string; int width, l;
    /** Converts the pml into a 1D string if width == 0, else a 2D string of the given width. */
    public String toString(Pml pml, int width) {
      if (pml == null) return "null";
      // Initializes the variables
      string = new StringBuffer(); 
      if (width == 0) {
	write1d(pml);
      } else {
	this.width = width; l = 0; write2d(pml, 0, 0);
      }
      return string.toString();
    }
    /** Writes the data inlined. */
    private void write1d(Pml pml) {
      if (pml == null) { string.append(" {null} "); return; }
      string.append("{"+quote(pml.getTag()));
      for(String name : pml.attributes()) {
	string.append(" "+quote(name)+"="); write1d(pml.getChild(name));
      }
      for(int n = 0; n < pml.getCount(); n++) {
	string.append(" "); write1d(pml.getChild(n));
      }
      string.append("}");
    }
    /** Writes the data formated. */
    private boolean write2d(Pml pml, int n, int tab) {
      if (pml == null) { string.append(" {null} "); return false; }
      if (pml.getSize() == 0) {
	boolean ln = n >= 0 && (n == 0 || (pml.getParent() != null && pml.getParent().getChild(n-1) != null && pml.getParent().getChild(n-1).getSize() > 0));
	writeln(ln, tab); write(quote(pml.getTag()), tab);
	return ln;
      } else {
	boolean ln = pml.getTag().length() > 1 || "p".equals(pml.getTag());
	writeln(n >= 0 && ln, tab); write("{"+quote(pml.getTag()), tab);
	ln = false;
	for(String name : pml.attributes()) {
	  write(" "+quote(name)+" =", tab); ln |= write2d(pml.getChild(name), -1, tab+1);
	}
	for(int i = 0; i < pml.getCount(); i++) {
	  ln |= write2d(pml.getChild(i), i, tab+2);
	}
	writeln(ln, tab); write("}", tab);
	return ln;
      }
    }
    /** Writes a tabulated newline. */
    private void writeln(boolean ln, int tab) {
      if (ln) {
	string.append("\n"); for(int t = 0; t < tab; t++) string.append(" "); l = tab;
      } else {
	string.append(" ");
      }
    }
    /** Writes a word. */
    private void write(String word, int tab) {
      if (l + word.length() > width) writeln(false, tab+1); string.append(word); l += word.length();
    }
    /** Quotes a string taking "{" "}" and \" constructs into account. */
    private static String quote(String string) {
      return string == null ? "null" : string == Utils.toName(string) || "\"{\"".equals(string) || "\"}\"".equals(string) ? string : 
	"\""+string.replaceAll("\\\\", "\\\\\\\\").replaceAll("\"", "\\\\\"")+"\"";
    }
  }
  /** Defines a XML writer. */
  private static class XmlWriter {
    private StringBuffer string;
    /** Converts the pml into a XML 1D string. */
    public String toString(Pml pml) {
      string = new StringBuffer();
      if (pml == null) return "<null/>";
      write(pml);
      return string.toString();
    }
    /** Writes the data. */
    private void write(Pml pml) {
      if (pml.getSize() == 0) {
	string.append(" "+pml.getTag().replaceFirst("^\"([{}])\"$", "$1").replaceAll("&", "&amp;").replaceAll("<", "&lt;"));
      } else {
	string.append(" <"+toName(pml.getTag()));
	for(String name : pml.attributes())
	  string.append(" "+toName(name)+"=\""+pml.getChild(name).toString().replaceFirst("^\\{(.*)\\}$", "$1").replaceFirst("^\"(.*)\"$", "$1").
			replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll("\"", "&quot;")+"\"");
	if (pml.getCount() > 0) {
	  string.append(">");
	  for(int n = 0; n < pml.getCount(); n++)
	    write(pml.getChild(n));
	  string.append("</"+toName(pml.getTag())+">");
	} else
	  string.append("/>");
      }
    }
    /** Converts a string to a name. */
    private static String toName(String string) {
      String c_0 = string.substring(0, 1); String name = c_0.matches("_-") || Character.isLetter(c_0.charAt(0)) ? "" : "_";
      for (int i = 0; i < string.length(); i++) {
	String c_i = string.substring(i, i+1);
	name += c_i.matches("_-") || Character.isLetterOrDigit(c_i.charAt(0)) ? c_i : "_";
      }
      return name;
    }
  }

  /** Gets this logical-structure tag. 
   * @return The tag name if defined when reseting the data structure, otherwise the Java class name.
   */
  public final String getTag() { return tag; }
  protected final Pml setTag(String value) { tag = value; return this; }
  private String tag = getClass().getName();

  /** Gets this logical-structure parent's reference if any. */
  public final Pml getParent() { return parent; }
  private final void setParent(Pml value) { if (parent == null) parent = value; }
  private Pml parent = null;

  /** Tests if a parameter value is defined.
   * @param name The attribute's name or element's index.
   * @return True if the value neither null nor equal to the empty string, else false.
   */
  public final boolean isDefined(String name) { return data.containsKey(name); }
  /**/public final boolean isDefined(int index) { return isDefined(Integer.toString(index)); }  
  
  /** Gets a parameter as a logical-structure. 
   * @param name The attribute's name or element's index.
   * @return A reference to the logical-structure's value if any, else null.
   */
  public Pml getChild(String name) { return data.get(name); }
  /**/public final Pml getChild(int index) { return getChild(Integer.toString(index)); }

  /** Gets a parameter value as a string. 
   * @param name The attribute's name or element's index.
   * @param value The default value.
   * @return The value as a string or the empty string if undefined.
   */
  public final String getString(String name, String value) { String v = data.get(name).toString(); return v != null ? v : value != null ? value : ""; }
  /**/public final String getString(int index, String value) { return getString(Integer.toString(index), value); }
  /**/public final String getString(String name) { return getString(name, null); }
  /**/public final String getString(int index) { return getString(index, null); }
  
  /** Gets a parameter value as a decimal. 
   * @param name The attribute's name or element's index.
   * @param value The default value.
   * @return The value as a decimal.
   */
  public final double getDecimal(String name, double value) { try { return Double.parseDouble(getString(name, "0")); } catch(NumberFormatException e) { return value; } }
  /**/public final double getDecimal(int index, double value) { return getDecimal(Integer.toString(index), value); }
  /**/public final double getDecimal(String name) { return getDecimal(name, 0); }
  /**/public final double getDecimal(int index) { return getDecimal(index, 0); }
  
  /** Gets a parameter value as an integer. 
   * @param name The attribute's name or element's index.
   * @param value The default value.
   * @return The value as an integer.
   */
  public final int getInteger(String name, int value) { try { return Integer.parseInt(getString(name, "0")); } catch(NumberFormatException e) { return value; } }
  /**/public final int getInteger(int index, int value) { return getInteger(Integer.toString(index), value); }
  /**/public final int getInteger(String name) { return getInteger(name, 0); }
  /**/public final int getInteger(int index) { return getInteger(index, 0); }
  
  /** Sets a parameter value.
   * @param name The attribute's name or element's index.
   * @param value The parameter value, or <tt>null</tt> to unset the value.
   * @return This, allowing to use the <tt>pml.set(..,..)...</tt> construct.
   */
  public Pml set(String name, Pml value) { 
    // Deletes the attribute value
    if (value == null) { 
      try {
	// Shifts removed elements to avoid "null wholes"
	int i = Integer.parseInt(name), l = getCount() - 1; 
	if (0 <= i && i <= l) {
	  for(int j = i; j < l; j++)
	    data.put(Integer.toString(j), data.get(Integer.toString(j + 1)));
	  data.remove(Integer.toString(l));
	} else {
	  data.remove(name); 
	}
      } catch(NumberFormatException e) { 
	data.remove(name); 
      }
    // Adds the parameter value
    } else { 
      data.put(name, value); value.setParent(this); 
    } 
    count = -1; return this; 
  }
  /**/public final Pml set(int index, Pml value) { return set(Integer.toString(index), value); }
  /**/public final Pml set(String name, String value) { Pml v = new Pml(); v.reset(value); return set(name, v); }
  /**/public final Pml set(int index, String value) { return set(Integer.toString(index), value); }
  /**/public final Pml set(String name, double value) { return set(name, Double.toString(value)); }
  /**/public final Pml set(int index, double value) { return set(Integer.toString(index), value); }
  /**/public final Pml set(String name, int value) { return set(name, Integer.toString(value)); }
  /**/public final Pml set(int index, int value) { return set(Integer.toString(index), value); }

  /** Unsets a parameter value.
   * @param name The attribute's name or element's index.
   * @return This, allowing to use the <tt>pml.del(..,..)...</tt> construct.
   */
  public Pml del(String name) { return set(name, (Pml) null); }
  /**/public final Pml del(int index) { return set(Integer.toString(index), (Pml) null); }

  /** Adds an element's value. 
   * @param value The element value.
   * @return This, allowing to use the <tt>pml.add(..)...</tt> construct.
   */
  public final Pml add(Pml value) { int c = getCount(); set(c, value); count = ++c; return this; }
  /**/public final Pml add(String value) { Pml v = new Pml(); v.reset(value); return add(v); }
  /**/public final Pml add(double value) { return add(Double.toString(value)); }
  /**/public final Pml add(int value) {  return add(Integer.toString(value)); }

  /** Returns the number of elements. */
  public int getCount() { if (count < 0) { count = 0; for(String key : data.keySet()) if (isIndex(key)) count = Math.max(Integer.parseInt(key)+1, count); } return count; } 
  private int count = -1;

  /** Returns the number of parameters (attributes and elements). */
  public int getSize() { return data.size(); }

  /** Returns an attribute's names iterator. 
   * <p>- Attributes are enumerated using the construct <tt>for(String name : pml.attributes()) { Pml value = pml.getChild(name); .. }</tt>.</p>
   * <p>- Elements are enumerated using the construct <tt>for(int n = 0; n &lt; pml.getCount(); n++) { Pml value = pml.getChild(n); .. }</tt>.</p>
   */
  public final Iterable<String> attributes() { return new Iterable<String>() {
      public Iterator<String> iterator() {
	return new Iterator<String>() {
	  Iterator<String> keys = data.keySet().iterator(); String key; { nextKey(); }
	  private void nextKey() { for(key = null; keys.hasNext() && isIndex(key = keys.next()); key = null); }
	  public String next() { String value = key; nextKey(); return value; }
	  public boolean hasNext() { return key != null; }
	  public void remove() { throw new UnsupportedOperationException(); }
	};
      }
    };
  }

  // @return true if the name is an index
  private static boolean isIndex(String name) { return index.matcher(name).matches(); }
  static Pattern index = Pattern.compile("[0-9]+");

  /** Checks the well-formedness of a file by mirroring its Pml structure in a normalized format.
   * @param usage <tt>java org.javascool.Pml input-file [output-file]</tt>
   * <p>- The file name be a PML, XML or HTML file name, with the corresponding extensions</p>.
   */
  public static void main(String[] usage) {
    if (usage.length > 0) new Pml().load(usage[0]).save(usage.length > 1 ? usage[0] : "stdout:");
  }
}
