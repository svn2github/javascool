/*******************************************************************************
 * Thierry.Vieville@sophia.inria.fr, Copyright (C) 2010.  All rights reserved. *
 *******************************************************************************/

package org.javascool;

import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Pattern;

/** Defines a PML: a Pml (Programmatic Markup Language) Memory Loader.
 * 
 * <p> A Pml (for «Programmatic Métadata Logicalstructure») is a Parametric Minimal Logical-structure, which parameters are <ul>
 *   <li>Its <i>Tag</i>, i.e. the name defining its type.</li>
 *   <li>Named <i>Attributes</i> values explicitly indexed by a name</li>
 *   <li>Indexed <i>Elements</i> values implicitly indexed by an integer <tt>>= 0</tt></li>
 * </ul>values being either a Pml or a String. And not more.</p>
 *
 * <p>It is used to manage non-trivial routine parameters (including <a href="../../org/javascool/Text.html">text</a>) or to interface with other applications.</p>
 * 
 * <p>The textual syntax is of the form:
 * <div style="margin-left: 40px"><tt>"{tag name = value .. element .. }"</tt></div>where <ul>
 * <li>Pml are encapsulated with <tt>{</tt> .. <tt>}</tt>.</li>
 * <li>String with spaces or <tt>{</tt> or <tt>}</tt> chars are encapsulated with <tt>'"'</tt> (escaped with <tt>'\"'</tt>).</li>
 * </ul>This syntax is minimal, close to any bracket (<tt>C/C++, PHP, Java</tt>) language syntax, easy to read and write and completely standard.
 * <p>The parameter logical-structure parsing from a string is <i>weak</i> in the sense that a value is always derived without generating syntax errors.</p>
 * </p>
 *
 * @see <a href="../../org/javascool/Pml.java">source code</a>
 */
public class Pml {
  private static final long serialVersionUID = 1L;

  private HashMap<String, Pml> data = new  HashMap<String, Pml>();

  /** Resets the logical-structure, parsing the given string. 
   * @param value The value following the <tt>"{tag name = value .. element .. }"</tt> syntax.
   * @return This; allowing to use the <tt>Pml pml= new Pml().reset(..)</tt> construct.
   */
  public Pml reset(String value) { 
    // Initializes the Pml
    data = new  HashMap<String, Pml>(); tag = ""; parent = null; count = -1;
    // Parses the string
    new PmlReader().reset(value, this);
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
   * @param format <ul>
   * <li>"pml" Reads a file in pml format (default value).</li>
   * <li>"xml" Reads a file in XML format.</li>
   * <li>"htm" or "html" Reads a file in HTML format.</li>
   * </ul> 
   * When not specified, detect the format extension from the file extension or used "pml" by default.
   * @return This; allowing to use the <tt>Pml pml= new Pml().reset(..)</tt> construct.
   */
  public final Pml load(String location, String format) {
    if ("xml".equals(format)) {
      return reset(Utils.xml2xml(Utils.loadString(location), xml2pml));
    } else if ("htm".equals(format) || "html".equals(format)) {
      return reset(Utils.xml2xml(Utils.htm2xml(Utils.loadString(location)), xml2pml));
    } else {
      return reset(Utils.loadString(location));
    }
  }
  public final Pml load(String location) {
    return load(location, location.replaceAll("^.*\\.([A-Za-z]+)$", "$1"));
  }
  // Pml Reader
  private static class PmlReader {
    private String string; private char chars[]; private int ichar, ichar0;
    /** Parses the string and set the parameters in the pml structure. */
    public void reset(String string, Pml pml) {
      // Initializes the buffer
      this.string = string; chars = string.toCharArray(); ichar = 0; ichar0 = 0;
      // Parses the string
      parse(pml);
      // Detects the trailer if any
      if (ichar < chars.length) {
	Pml p = new Pml(); back(); p.setTag(string.substring(ichar, chars.length)); pml.set("string_trailer", p);
      }
    }
    // Parses recursively the string
    private Pml parse(Pml pml) {
      String b = next();
      // Parses a { } Pml construct
      if ("{".equals(b)) {
	// Loops with state = 0 for tag reading, state = 1 for attribute reading, state = 2 for element reading
	for(int state = 0; true;) {
	  String t = next();
	  if ("}".equals(t)) {
	    break;
	  } else if ("{".equals(t)) {
	    state = 2; back(); Pml p = new Pml(); parse(p); pml.add(p);
	  } else if (state < 2 && "=".equals(t)) {
	    state = 1; Pml p = new Pml(); parse(p); pml.set("null", p);
	  } else {
	    String e = next();
	    if (state < 2 && "=".equals(e)) {
	      state = 1; Pml p = new Pml(); parse(p); pml.set(t, p);
	    } else if (state == 0) {
	      state = 1; back(); pml.setTag(t);
	    } else {
	      state = 2; back(); Pml p = new Pml(); p.setTag(t); pml.add(p); 
	    }
	  }
	}
      // Considers the Pml as a simple string
      } else {
	pml.setTag(b);
      }
      return pml;
    }
    // Gets the next token
    private String next() {
      // Skips spaces
      while(ichar < chars.length && Character.isWhitespace(chars[ichar])) ichar++;
      ichar0 = ichar; int i0 = ichar, i1;
      // Detects a end of file
      if (ichar >= chars.length) {
	return "}";
      // Detects a quoted string taking "{" "}" and \" constructs into account
      } else if (chars[i0] == '"') {
	while(ichar < chars.length && (ichar == i0 || chars[ichar] != '"' ||  chars[ichar-1] == '\\')) ichar++; ichar++;
	if (ichar == i0 + 3 && (chars[i0+1] == '{' || chars[i0+1] == '}')) { i1 = ichar; } else { i0++; i1 = ichar-1; } 
	return string.substring(i0, i1).replaceAll("\\\\\"", "\"");
      // Detects a meta-char
      } else if (chars[i0] == '{' || chars[i0] == '}' || chars[i0] == '=') {
	i1 = ++ichar;
      // Detects a word
      } else {
	while(ichar < chars.length && chars[ichar] != '{' && chars[ichar] != '}' && chars[ichar] != '=' && !Character.isWhitespace(chars[ichar])) ichar++;
	i1 = ichar;
      }
      return string.substring(i0, i1);
    }
    // Pushs-back one token
    private void back() {
      ichar = ichar0;
    }
  }
  private static String xml2pml = 
    "<?xml version='1.0' encoding='utf-8'?>"+
    "<sx:function name='sx:replace' xmlns:string='java:java.lang.String'>"+
    "  <xsl:param name='string'/>"+
    "  <xsl:param name='pattern'/>"+
    "  <xsl:param name='target'/>"+
    "  <sx:return select='string:replaceAll($string, $pattern, $target)'/>"+
    "</sx:function>"+
    "<xsl:stylesheet xmlns:xsl='http://www.w3.org/1999/XSL/Transform' xmlns:sx='http://icl.com/saxon' extension-element-prefixes='sx' version='1.0'>"+
    "  <xsl:output method='html'/>"+
    "  <xsl:template match='*'>{"+
    "  <xsl:value-of select='name(.)'/>"+
    "    <xsl:for-each select='@*'><xsl:value-of select='name(.)'>=\"<xsl:value-of select='sx:replace(., \"\\\"\", \"\\\\\\\"\")'/>\" "+
    "    <xsl:apply-templates/>"+
    "  }</xsl:template>"+
    "</xsl:stylesheet>";

  /** Returns this logical-structure structure as a one-line string. */
  public String toString() { 
    return new PlainWriter().toString(this, 0);
  } 
  /** Saves this logical-structure structure into a location. 
   * @param location @optional<"stdout:"> A Universal Resource Location of the form: <table>
   * <tr><td><tt>ftp:/<i>path-name</i></tt></td><td>to save onto a FTP site.</td></tr>
   * <tr><td><tt>file:/<i>path-name</i></tt></td><td>to save into a file.</td></tr>
   * <tr><td><tt>mailto:<i>address</i>?subject=<i>subject</i></tt></td><td>to send as an email in a readable form.</td></tr>
   * <tr><td><tt>stdout:/</tt></td><td>to print to the terminal standard output.</td></tr>
   * </table>
   * @param format <ul>
   * <li>"txt" To write in a normalized 2D plain text format.</li>
   * <li>"xml" To write in XML format, reducing tag and attribute names to valid XML names, and considering Pml without any attribute or elements as string.</li>
   * </ul>
   * @return This; allowing to use the <tt>Pml pml = new Pml().reset(..)</tt> construct.
   */
  public final Pml save(String location, String format) {
    Utils.saveString(location, 
		     "xml".equals(format) ? new XmlWriter().toString(this) :
		     new PlainWriter().toString(this, 180));
    return this;
  }
  // Pml Writer
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
    // Writes the data inlined
    private void write1d(Pml pml) {
      string.append("{"+quote(pml.getTag()));
      for(String name : pml.attributes()) {
	string.append(" "+quote(name)+"="); write1d(pml.getChild(name));
      }
      for(int n = 0; n < pml.getCount(); n++) {
	string.append(" "); write1d(pml.getChild(n));
      }
      string.append("}");
    }
    // Writes the data formated
    private boolean write2d(Pml pml, int n, int tab) {
      if (pml.getSize() == 0) {
	boolean ln = n >= 0 && (n == 0 || (pml.getParent() != null && pml.getParent().getChild(n-1).getSize() > 0));
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
    // Writes a tabulated newline
    private void writeln(boolean ln, int tab) {
      if (ln) {
	string.append("\n"); for(int t = 0; t < tab; t++) string.append(" "); l = tab;
      } else {
	write(" ", tab);
      }
    }
    // Writes a word
    private void write(String word, int tab) {
      if (l + word.length() > width) writeln(false, tab+1); string.append(word); l += word.length();
    }
    // Quotes a string taking "{" "}" and \" constructs into account
    private static String quote(String string) {
      return string == null ? "null" : string == Utils.toName(string) || "\"{\"".equals(string) || "\"}\"".equals(string) ? string : 
	"\""+string.replaceAll("\\\\", "\\\\\\\\").replaceAll("\"", "\\\\\"")+"\"";
    }
  }
  // Xml Writer
  private static class XmlWriter {
    private StringBuffer string;
    /** Converts the pml into a XML 1D string. */
    public String toString(Pml pml) {
      string = new StringBuffer();
      if (pml == null) return "<null/>";
      write(pml);
      return string.toString();
    }
    // Writes the data
    private void write(Pml pml) {
      if (pml.getSize() == 0) {
	string.append(" "+pml.getTag().replaceFirst("^\"([{}])\"$", "$1").replaceAll("&", "&amp").replaceAll("<", "&lt;"));
      } else {
	string.append(" <"+toName(pml.getTag()));
	for(String name : pml.attributes())
	  string.append(" "+toName(name)+"=\""+pml.getChild(name).toString().replaceFirst("^\\{(.*)\\}$", "$1").
			replaceAll("&", "&amp").replaceAll("<", "&lt;").replaceAll("\"", "&aquot;")+"\"");
	if (pml.getCount() > 0) {
	  for(int n = 0; n < pml.getCount(); n++)
	    write(pml.getChild(n));
	  string.append("</"+toName(pml.getTag())+">");
	} else
	  string.append("/>");
      }
    }
    // 
    private static String toName(String string) {
      String c_0 = string.substring(0, 1); String name = c_0.matches("_-") || Character.isLetter(c_0.charAt(0)) ? "" : "_";
      for (int i = 0; i < string.length(); i++) {
	String c_i = string.substring(i, i+1);
	name += c_i.matches("_-") || Character.isLetterOrDigit(c_i.charAt(0)) ? c_i : "_";
      }
      return name;
    }
  }

  /** Gets this logical-structure tag. */
  public final String getTag() { return tag; }
  private final void setTag(String value) { tag = value; }
  private String tag = "";

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
  
  /** Gets a parameter value as a decimal. 
   * @param name The attribute's name or element's index.
   * @param value The default value.
   * @return The value as a decimal.
   */
  public final double getDecimal(String name, double value) { try { return Double.parseDouble(getString(name, "0")); } catch(NumberFormatException e) { return value; } }
  /**/public final double getDecimal(int index, double value) { return getDecimal(Integer.toString(index), value); }
  
  /** Gets a parameter value as an integer. 
   * @param name The attribute's name or element's index.
   * @param value The default value.
   * @return The value as an integer.
   */
  public final int getInteger(String name, int value) { try { return Integer.parseInt(getString(name, "0")); } catch(NumberFormatException e) { return value; } }
  /**/public final int getInteger(int index, int value) { return getInteger(Integer.toString(index), value); }
  
  /** Sets a parameter value.
   * @param name The attribute's name or element's index.
   * @param value The parameter value, or <tt>null</tt> to cancel the value.
   * @return This; allowing to use the <tt>pml.set(..,..).set(..,..)</tt> construct.
   */
  public Pml set(String name, Pml value) { 
    if (value == null) { data.remove(name); } else { data.put(name, value); value.setParent(this); } count = -1; return this; 
  }
  /**/public final Pml set(int index, Pml value) { return set(Integer.toString(index), value); }
  /**/public final Pml set(String name, String value) { Pml v = new Pml(); v.reset(value); return set(name, v); }
  /**/public final Pml set(int index, String value) { return set(Integer.toString(index), value); }
  /**/public final Pml set(String name, double value) { return set(name, Double.toString(value)); }
  /**/public final Pml set(int index, double value) { return set(Integer.toString(index), value); }
  /**/public final Pml set(String name, int value) { return set(name, Integer.toString(value)); }
  /**/public final Pml set(int index, int value) { return set(Integer.toString(index), value); }

  /** Adds an element's value. 
   * @param value The element value.
   * @return This; allowing to use the <tt>pml.add(..).set(..,..)</tt> construct.
   */
  public final Pml add(Pml value) { int c = getCount(); set(c, value); count = ++c; return this; }
  /**/public final Pml add(String value) { Pml v = new Pml(); v.reset(value); return add(v); }
  /**/public final Pml add(double value) { return add(Double.toString(value)); }
  /**/public final Pml add(int value) {  return add(Integer.toString(value)); }

  /** Returns the number of elements. */
  public int getCount() { if (count < 0) { count = 0; for(String key : data.keySet()) if (isIndex(key)) count = Math.max(Integer.parseInt(key), count); } return count; } 
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

  // Returns true if the name is an index
  private static boolean isIndex(String name) { return index.matcher(name).matches(); }
  static Pattern index = Pattern.compile("[0-9]+");

  /** Used to check the syntax the well-formedness by mirroring the structure in a normalized format.
   * @param args Usage <tt>java org.javascool.Pml pml-file-name</tt>
   */
  public static void main(String[] args) {
    if (args.length == 1) {
      Pml pml = new Pml(); pml.load(args[0]); pml.save("stdout:", "plain");
    } else {
      System.out.println("Usage: java org.javascool.Pml pml-file-name");
    }
  }
}
