/*******************************************************************************
 * Thierry.Vieville@sophia.inria.fr, Copyright (C) 2009.  All rights reserved. *
 *******************************************************************************/

/*
  Ajouter load/store et storeXml
  Parse/Print complet d'une expression/term/bracket
*/

package org.javascool;

import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Pattern;

/** Defines a parametric logical-structure. 
 *
 * <h1>NOT YET IMPLEMENTED: DO NOT USE</h1>
 *
 * <p> A BML (for «Bracket Métadata Logicalstructure») is a parametric logical-structure is a generic lightweight data structure of the form:
 *  <div style="margin-left: 40px"><tt>"{ name = value, .. element .. }"</tt></div>where <ul>
 * <li>strings with spaces or <tt>{</tt> or <tt>}</tt> chars are encapsulated with <tt>'"'</tt> (escaped with <tt>'\"'</tt>),</li>
 * <li>the default value being <tt>'true'</tt>.</li>
 * </ul> This syntax is minimal, easy to read and write and completely standard.</p>
 * 
 * <p>This defines parameters, which are either: <ul>
 *   <li><i>Attributes</i> explicitly indexed by names, or</li>
 *   <li><i>Elements</i> implicitly indexed by integer >= 0</li>
 * </ul> (eventually recursive) and not more.</p>
 *
 * <ul>
 *   <li>It is implemented as an associative table and can be viewed as XML logical-structure, but with a very light syntax.</li>
 *   <li>The parameter logical-structure parsing from a string is <i>weak</i> in the sense that a value is always derived without generating syntax errors.</li>
 *   <li>This is used to manage non-trivial routine parameters or to interface with other applications.</li>
 * </ul>
 */
/*public*/ class BML {
  private static final long serialVersionUID = 1L;

  private HashMap<String, BML> data = new  HashMap<String, BML>();

  /** Resets the logical-structure, parsing the given string. */
  public void reset(String value) { 
    /// IMPLEMENTS BML2STRING
  }

  /** Converts the logical-structure structure as a string. */
  public String toString() { 
    if (string == null) {
      /// IMPLEMENTS BML2STRING
    }
    return string; 
  } 
  private String string = null;

  /** Gets values defined by a given path.
   * @param path The path defining the values.
   * @return The corresponding values as a list of BML elements
   */
  public BML getPath(String path) {
    /// IMPLEMENTS THE PATH2BML
    return null;
  }

  /** Tests if a parameter value is defined.
   * @param name The attribute's name or element's index.
   * @return True if the value neither null nor equal to the empty string, else false.
   */
  public final boolean isDefined(String name) { return data.containsKey(name); }
  public final boolean isDefined(int index) { return isDefined(Integer.toString(index)); }  
  
  /** Gets a parameter as a logical-structure. 
   * @param name The attribute's name or element's index.
   * @return A reference to the logical-structure's value if any, else null.
   */
  public final BML getChild(String name) { return data.get(name); }
  public final BML getChild(int index) { return getChild(Integer.toString(index)); }

  /** Gets this logical-structure parent's reference if any. */
  public BML getParent() { return parent; }
  private void setParent(BML value) { if (parent != null) parent = value; }
  private BML parent = null;

  /** Gets a parameter value as a string. 
   * @param name The attribute's name or element's index.
   * @param value The default value.
   * @return The value as a string or the empty string if undefined.
   */
  public final String getString(String name, String value) { String v = data.get(name).toString(); return v != null ? v : value != null ? value : ""; }
  public final String getString(int index, String value) { return getString(Integer.toString(index), value); }
  
  /** Gets a parameter value as a decimal. 
   * @param name The attribute's name or element's index.
   * @param value The default value.
   * @return The value as a decimal.
   */
  public final double getDecimal(String name, double value) { try { return Double.parseDouble(getString(name, "0")); } catch(NumberFormatException e) { return value; } }
  public final double getDecimal(int index, double value) { return getDecimal(Integer.toString(index), value); }
  
  /** Gets a parameter value as an integer. 
   * @param name The attribute's name or element's index.
   * @param value The default value.
   * @return The value as an integer.
   */
  public final int getInteger(String name, int value) { try { return Integer.parseInt(getString(name, "0")); } catch(NumberFormatException e) { return value; } }
  public final int getInteger(int index, int value) { return getInteger(Integer.toString(index), value); }
  
  /** Sets a parameter value.
   * @param name The attribute's name or element's index.
   * @param value The parameter value, or <tt>null</tt> to cancel the value.
   * @return This allowing to use the <tt>bml.set(..,..).set(..,..)</tt> construct.
   */
  public final BML set(String name, BML value) { 
    if (value == null) data.remove(name); else data.put(name, value); value.setParent(this); 
    string = null; count = -1; return this; 
  }
  public final BML set(int index, BML value) { return set(Integer.toString(index), value); }
  public final BML set(String name, String value) { BML v = new BML(); v.reset(value); return set(name, v); }
  public final BML set(int index, String value) { return set(Integer.toString(index), value); }
  public final BML set(String name, double value) { return set(name, Double.toString(value)); }
  public final BML set(int index, double value) { return set(Integer.toString(index), value); }
  public final BML set(String name, int value) { return set(name, Integer.toString(value)); }
  public final BML set(int index, int value) { return set(Integer.toString(index), value); }

  /** Adds an element's value. 
   * @param value The element value.
   * @return This allowing to use the <tt>bml.add(..).set(..,..)</tt> construct.
   */
  public final BML add(BML value) { int c = getCount(); set(c, value); count = ++c; return this; }
  public final BML add(String value) { BML v = new BML(); v.reset(value); return add(v); }
  public final BML add(double value) { return add(Double.toString(value)); }
  public final BML add(int value) {  return add(Integer.toString(value)); }

  /** Returns the number of elements. */
  public int getCount() { if (count < 0) for(String key : data.keySet()) if (isIndex(key)) count = Math.max(Integer.parseInt(key), count); return count; } 
  private int count = -1;

  /** Returns an attribute's names iterator. 
   * -To be used in a <tt>for(String name : bml.attributes()) { .. }</tt> construct.
   */
  public final Iterable attributes() { return new Iterable() {
      public Iterator<String> iterator() {
	return new Iterator<String>() {
	  Iterator<String> keys = data.keySet().iterator(); String key; { next(); }
	  public String next() { for(key = null; keys.hasNext() && isIndex(key = keys.next()); key = null); return key; }
	  public boolean hasNext() { return key != null; }
	  public void remove() { throw new UnsupportedOperationException(); }
	};
      }
    };
  }

  /** Returns an element's values iterator.
   * -To be used in a <tt>for(BML value : bml.elements()) { .. }</tt> construct.
   */
  public final Iterable elements() { return new Iterable() {
      public Iterator<BML> iterator() {
	return new Iterator<BML>() {
	  Iterator<String> keys = data.keySet().iterator(); String key; { nextKey(); }
	  private void nextKey() { for(key = null; keys.hasNext() && !isIndex(key = keys.next()); key = null); }
	  public BML next() { nextKey(); return key != null ? data.get(key) : null ; }
	  public boolean hasNext() { return key != null; }
	  public void remove() { throw new UnsupportedOperationException(); }
	};
      }
    };
  }
  
  // Returns true if the name is an index
  private static boolean isIndex(String name) { return index.matcher(name).matches(); }
  static Pattern index = Pattern.compile("[0-9]+");

  /** Used for tests. */
  public static void main(String[] arg) {
    BML bml = new BML();
    bml.isDefined(10);
    System.out.println("Hi !");
  }
}
