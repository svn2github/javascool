/*******************************************************************************
 * Thierry.Vieville@linux-azur.org, Copyright (C) 2010.  All rights reserved.  *
 *******************************************************************************/

/* To be implemented
 * out: Array assignment
 * in : distinguer inst/exp
 * in : c ? : e1 : e0
 * out: Pml attributes generate a non parsable construct
 * in: instruction sequence ; ; ; 
 */

package org.javascool;

/** Defines an expression as a PML data structure.
 * 
 * <div>An expression is a <a href="Pml.html">logical-structure</a> which syntax corresponds to usual <i>expressions</i> and <i>pieces of code</i>.</div>
 * 
 * <div>The basic element is a functional term. A <i>functional-term</i> is a construct of the form <tt>name(element_1, ..)</tt>, 
 * which equivalent PML syntax is <tt>{ name element_1 ..}</tt>. </div>
 * 
 * <div><b>Functional terms and operators.</b>
 * As a syntax extension usual <tt>prefix</tt>, <tt>infix</tt>, or <tt>postfix</tt> operators of different class are understood: <div><table>
 *  <tr><td valign="top"><div>[<tt>f</tt>] <i>funcional operators</i></div></td>
 *  <td valign="top"><div> <tt>=</tt> for variable assignment, <tt>.</tt> for method invocation</div></td></tr> 
 *  <tr><td valign="top"><div>[<tt>a</tt>] <i>algebraic operators</i></div></td>
 *   <td valign="top"><div> <tt>+ - * / %</tt> for sum, subtraction, product, division and remainder</div></td></tr> 
 *  <tr><td valign="top"><div>[<tt>l</tt>] <i>logical operators</i></div></td>
 *   <td valign="top"><div> <tt>&amp;&amp;</tt> means <i>and</i>, i.e. conjunction, <tt>||</tt> means <i>or</i>, i.e. disjunction, <tt>!</tt> means <i>not</i>, i.e. negation</div></td></tr>
 *  <tr><td valign="top"><div>[<tt>r</tt>] <i>relational operators</i></div></td>
 *   <td valign="top"><div> <tt>== != &lt; &gt; &lt;= &gt;=</tt> for equality and inequalities.</div></td></tr>
 * </table></div>  Summarizing: <div><table>
 *   <tr> <td valign="top"><div><b>Symbol.</b></div></td> <td valign="top"><div align="center"><tt>=</tt></div></td> 
 * <td valign="top"><div align="center"><tt>||</tt></div></td> <td valign="top"><div align="center"><tt>&amp;&amp;</tt></div></td> <td valign="top"><div align="center"><tt>!</tt></div></td> 
 * <td valign="top"><div align="center"><tt>==</tt></div></td> <td valign="top"><div align="center"><tt>!=</tt></div></td> <td valign="top"><div align="center"><tt>&gt;=</tt></div></td> <td valign="top"><div align="center"><tt>&lt;=</tt></div></td> <td valign="top"><div align="center"><tt>&gt;</tt></div></td> <td valign="top"><div align="center"><tt>&lt;</tt></div></td> 
 * <td valign="top"><div align="center"><tt>+</tt></div></td> <td valign="top"><div align="center"><tt>-</tt></div></td> <td valign="top"><div align="center"><tt>*</tt></div></td> <td valign="top"><div align="center"><tt>/</tt></div></td> <td valign="top"><div align="center"><tt>%</tt></div></td> 
 * <td valign="top"><div align="center"><tt>.</tt></div></td> <td valign="top"><div align="center"><tt>-</tt></div></td> 
 * </tr>
 * <tr> <td valign="top"><div><b>Type.</b></div></td> <td valign="top"><div align="center">infix</div></td> 
 * <td valign="top"><div align="center">infix</div></td> <td valign="top"><div align="center">infix</div></td> <td valign="top"><div align="center">prefix</div></td>
 * <td valign="top"><div align="center">infix</div></td> <td valign="top"><div align="center">infix</div></td> <td valign="top"><div align="center">infix</div></td> <td valign="top"><div align="center">infix</div></td> <td valign="top"><div align="center">infix</div></td> <td valign="top"><div align="center">infix</div></td> 
 * <td valign="top"><div align="center">infix</div></td> <td valign="top"><div align="center">infix</div></td> <td valign="top"><div align="center">infix</div></td> <td valign="top"><div align="center">infix</div></td> <td valign="top"><div align="center">infix</div></td> <td valign="top"><div align="center">infix</div></td>
 * <td valign="top"><div align="center">prefix</div></td> 
 * </tr>
 * <tr> <td valign="top"><div><b>Class.</b></div></td> <td valign="top"><div align="center">[f]</div></td> 
 * <td valign="top"><div align="center">[l]</div></td> <td valign="top"><div align="center">[l]</div></td> <td valign="top"><div align="center">[l]</div></td> 
 * <td valign="top"><div align="center">[r]</div></td> <td valign="top"><div align="center">[r]</div></td> <td valign="top"><div align="center">[r]</div></td> <td valign="top"><div align="center">[r]</div></td> <td valign="top"><div align="center">[r]</div></td> <td valign="top"><div align="center">[r]</div></td> 
 * <td valign="top"><div align="center">[a]</div></td> <td valign="top"><div align="center">[a]</div></td> <td valign="top"><div align="center">[a]</div></td><td valign="top"><div align="center">[a]</div></td> <td valign="top"><div align="center">[a]</div></td> <td valign="top"><div align="center">[f]</div></td> <td valign="top"><div align="center">[a]</div></td> 
 * </tr>
 * </table></div> shown here in precedence decreasing order and corresponding to usual functional term (e.g.:<tt>a+b</tt> represents <tt>'+'(a ,b)</tt>).</div> 
 * 
 *  <div><b>Additional constructs.</b><table>
 *   <tr><td valign="top"><div><i>Variable assignment: </i></div></td>
 *     <td valign="top"><div><tt><i>type</i> <i>name</i> =  <i>expression</i>;</tt></div></td></tr>
 *     <tr><td valign="top"><div/></td><td valign="top"><div>corresponds to the term <tt>let(<i>type</i>, <i>name</i>, <i>expression</i>)</tt></div></td></tr>
 *     <tr><td valign="top"><div/></td><td valign="top"><div>which semantic is 
 *      «defines <i>name</i> as new variable of the given <i>type</i>, which initial value is the result of the present evaluation of the <i>expression</i>»</div></td></tr>
 *     <tr><td valign="top"><div/></td><td valign="top"><div>as a functional term, the initial value is returned</div></td></tr>
 *   <tr><td valign="top"><div><i>Array assignment: </i></div></td>
 *     <td valign="top"><div><tt><i>type</i>[] <i>name</i> =  {<i>expression</i>, ...};</tt></div></td></tr>
 *     <tr><td valign="top"><div/></td><td valign="top"><div>corresponds to the term <tt>let(<i>type</i>, <i>name</i>, <i>expression</i>)</tt></div></td></tr>
 *     <tr><td valign="top"><div/></td><td valign="top"><div>which semantic is 
 *      «defines <i>name</i> as new variable of the given array <i>type</i>, which initial value is the result of the present evaluation of the list of <i>expression</i>»</div></td></tr>
 *     <tr><td valign="top"><div/></td><td valign="top"><div>as a functional term, the initial value is returned</div></td></tr>
 *   <tr><td valign="top"><div><i>Conditional expressions: </i></div></td>
 *     <td valign="top"><div><tt>if (<i>condition</i>) { <i>true-expression</i> } else { <i>false-expression</i>} </tt></div></td></tr>
 *     <tr><td valign="top"><div/></td><td valign="top"><div>corresponds to the term <tt>if(<i>condition</i>, <i>true-expression</i>, <i>false-expression</i>)</tt> the <tt>else</tt> term being optional </div></td></tr>
 *     <tr><td valign="top"><div/></td><td valign="top"><div>which semantic is
 *      «evaluates the <i>condition</i> and evaluates the <i>true-expression</i> if <tt>true</tt>, and the <i>false-expression</i> otherwise, if any»</div></td></tr>
 *     <tr><td valign="top"><div/></td><td valign="top"><div>as a functional term, the evaluated expression value is returned</div></td></tr>
 *   <tr><td valign="top"><div><i>Loop iteration: </i></div></td>
 *     <td valign="top"><div><tt>while (<i>condition</i>) { <i>iterative-expression</i> }</tt></div></td></tr>
 *     <tr><td valign="top"><div/></td><td valign="top"><div>corresponds to the term <tt>while(<i>condition</i>, <i>iterative-expression</i>)</tt></div></td></tr>
 *     <tr><td valign="top"><div/></td><td valign="top"><div>which semantic is
 *      «evaluates the <i>condition</i> and, if <tt>true</tt>, evaluates the <i>loop-expression</i> and repeat»</div></td></tr>
 *     <tr><td valign="top"><div/></td><td valign="top"><div>as a functional term, the last evaluated expression value is returned</div></td></tr>
 * </table></div>
 * </div>
 *
 * @see <a href="Eml.java.html">source code</a>
 * @serial exclude
 */
public class Eml extends Pml { /*public*/ Eml() { }
  private static final long serialVersionUID = 1L;

  /** Resets the logical-structure, parsing the given string. 
   * @param value The value following the usual expression syntax.
   * @param format <ul>
   * <li>"pml" Reads in pml format (default value).</li>
   * <li>"eml" Reads in eml format.</li>
   * <li>or other PML <a href="Pml.html#input-format">input format</a>.
   * </ul> 
   * @return This, allowing to use the <tt>new Eml().reset(..)</tt> construct.
   */
  public Pml reset(String value, String format) {
    if ("eml".equals(format)) {
      reset(new ExpressionReader().read(value));
      return this;
    } else {     
      return super.reset(value, format);
    }
  }
  
  // READ INTERFACE  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  /** Defines an expression reader. */
  private static class ExpressionReader extends TokenReader {
    /** Parses the string and set the parameters in the pml structure. */
    public Pml read(String string) {
      Pml pml = parse();
      check(trailer().length() == 0, "il reste du texte inutilisé");
      return pml;
    }
    /** Parses recursively the string. */
    private Pml parse() {
      Pml pml;
      if ((pml = parseIf()) != null) return pml;
      if ((pml = parseLet()) != null) return pml;
      if ((pml = parseParenthesis()) != null) return pml;
      return parseExpression(0);
    }
    // Parses a sequence a instruction sequence "{ (i1 (, ik)*)? }"
    private Pml parseBracket() {
      Pml pml = new Pml().setTag("seq");
      if ("{".equals(current())) { next();
	while(true) {
	  pml.add(parse());
	  if ("}".equals(current())) { next();
	    break;
	  }
	}
      } else
	pml.add(parse());
      return pml;
    }
    // Parses a conditional of syntax: "if (c) { e1 } else { e0 }"
    private Pml parseIf() {
      if ("if".equals(current())) { next();
	Pml pml = new Pml().setTag("if");
	pml.add(parseExpression(0));
	pml.add(parseBracket());
	if ("else".equals(current())) { next();
	  pml.add(parseBracket());
	}
	return pml;
      } else
	return null;
    }
    // Parses an assignment of syntax: "[type] name = value;"
    private Pml parseLet() {
      if ("=".equals(current(2))) { 
	String t = current(); next(); String n = current(); next();
	Pml pml = new Pml().setTag("let").add(t).add(n).add(parse());
	return pml;
      } else if ("=".equals(current(1))) { 
	String n = current(); next();
	Pml pml = new Pml().setTag("let").add("void").add(n).add(parse());
	return pml;
      }
      check(";".equals(current()), "le caractère ';' a été oublié");
      if (";".equals(current())) next();
      return null;
    }
    // Parses an expression with infix operator of syntax: item1 "O" item2
    private Pml parseExpression(int precedence) {
      // Stores the infix expression sequence
      Pml tokens = new Pml();
      while(true) {
	tokens.add(parse());
	String word = current();
	if (getPrecedence(word) == -999 || getPrecedence(word) > precedence) 
	  break;
	else
	  next();
	tokens.add(word);
      }
      check((tokens.getCount() % 2) == 1, "il manque un argument à l'expression");
      // Reduces the expression sequence
      for (int i = 1; i < tokens.getCount() - 1;) {
	if (((i == 1) || (getPrecedence(tokens.getString(i-2)) > getPrecedence(tokens.getString(i)))) &&
	    ((i == tokens.getCount() - 2) || (getPrecedence(tokens.getString(i+2)) >= getPrecedence(tokens.getString(i))))) {
	  tokens.set(i-1, new Pml().setTag(tokens.getString(i)).add(tokens.getChild(i-1)).add(tokens.getChild(i+1)));
	  for(int j = i; j < tokens.getCount();)
	    tokens.set(j, j < tokens.getCount() - 2 ? tokens.getChild(j + 2) : (Pml) null);
	  i = 1;
	} else
	  i += 2;
      }
      if(tokens.getCount() != 1) 
	throw new IllegalStateException("Spurious parser state: "+tokens);
      return tokens.getChild(0);
    }
    // Parses an expression with prefix operator of syntax: "O" item1
    private Pml parsePrefix() {
      String t = current();
      if ("!"..equals(t) || "-".equals(t)) { next();
	Pml pml = new Pml().setTag(t).add(parse());
	return pml;
      } 
      return null;
    }
    // Parses an expression between parentheses
    private Pml parseParenthesis() {
      String t = current();
      if ("(".equals(t)) { next();
	Pml pml = readExpression(0);
	check(")".equals(current()), "le caractère ')' a été oublié");
	if (")".equals(current())) next();
	return pml;
      }
      return null;
    }
  }

  /*


    // Parses a token in an expression
    private Value readToken() {
      Value token = null;
      if ((token = readIf()) != null) { return token; }
      if ((token = readFor()) != null) { return token; }
      // Parses prefix operators
      Value word = reader.readWord(); 
      if (word.equals("!") || word.equals("$")|| word.equals("#") || word.equals("-")) { 
        return new Structure(word.stringValue()).add(readExpression(getPrecedence(word.stringValue())));
      } 
      // Parses a list of the form "[element, .. ]"
      if (word.equals("[")) { reader.unreadWord(); return readList(new Structure("list"), "[", "]"); }
      // Parses an expression between parentheses
      if (word.equals("(")) {
	token = readExpression(0);
	reader.check(reader.readWord().equals(")"), "')' expected");
	// Parses postfix operators
	word = reader.readWord();
	if (word.equals("?") || word.equals("?*")) {
	  token = new Structure(word.stringValue()).add(token); 
	} else
	  reader.unreadWord(); 
	return token;
      }       
      // Parses a term
      {
	// Parses an attribute's list of syntax: "name { att=val, ..} "
	Value next = reader.readWord();
	if (next.equals("{")) {
	  token = new Structure(word.stringValue());
	  reader.unreadWord(); 
	  Value atts = readList(new Structure(), "{", "}");
	  for (int i = 0; i < atts.getCount(); i++) {
	    Value eqs = atts.get(i);
	    if (eqs.getType().equals("=")) {
	      token.set(eqs.get(0).stringValue(), eqs.get(1));
	    } else {
	      token.set(eqs.toString(), "true");
	    }
	  }
	  next = reader.readWord();
	}
	// Parses a functional term of syntax: "name (element, ..)"
	if (next.equals("(")) {
	  if (token == null) token = new Structure(word.stringValue());
	  reader.unreadWord(); 
	  token = readList(token, "(", ")");
	} else reader.unreadWord();
      }
      // Parses a word
      if (token == null) token = word;
      return token;
    }

    // Parses a conditional of syntax: "if c then e1 (elif c then e1)* else e0"
    private Value readIf(boolean isnext) {
      if ((!isnext) && (!reader.readWord().equals("if"))) { reader.unreadWord(); return null; }
      Value expression = new Structure("if");
      expression.add(readExpression(0));
      reader.check(reader.readWord().equals("then"), "then expected");
      expression.add(readExpression(0));
      Value next = reader.readWord();
      if (next.equals("elif"))
	expression.add(readIf(true));
      else if (next.equals("else"))
	expression.add(readExpression(0)); 
      else
	reader.unreadWord(); 
      return expression;
    }
    private Value readIf() { return readIf(false); }

    // Parses a parenthesized list of syntax: "B" item, .. "E"
    private Value readList(Value expression, String B, String E) {
      if (!reader.readWord().equals(B)) { reader.unreadWord(); return null; }
      while (true) {
	Value word = reader.readWord();
	reader.check(!word.equals(""), "unexpected end of file");
	if (word.equals(E)) return expression;
	if (!word.equals(",")) reader.unreadWord();
	expression.add(readExpression(0));
      }
    }

    // Parses an expression with infix operator of syntax: item1 "O" item2
    private Value readExpression(int precedence) {
      Value tokens = new Structure();
      // Stores the infix expression sequence
      while(true) {
	Value token = readToken();
	tokens.add(token);
	Value word = reader.readWord();
	// Manage the lexixal << x -#>> construct
	if (word.getType().equals("imp:numeric") &&
	    word.stringValue().startsWith("-")) {
	  tokens.add(new Chars().setValue("+"));
	  tokens.add(word);
	  word = reader.readWord();
	}
	if (getPrecedence(word.stringValue()) == -999 || getPrecedence(word.stringValue()) > precedence) {
	  reader.unreadWord();
	  break;
	}
	tokens.add(word);
      }
      reader.check((tokens.length() % 2) == 1, "unexpected token");
      // Reduces the expression sequence
      for (int i = 1; i < tokens.length()-1;) {
	if (((i == 1) || (getPrecedence(tokens.get(i-2).stringValue()) > getPrecedence(tokens.get(i).stringValue()))) &&
	    ((i == tokens.length()-2) || (getPrecedence(tokens.get(i+2).stringValue()) >= getPrecedence(tokens.get(i).stringValue())))) {
	  tokens.set(i-1, new Structure(tokens.get(i).stringValue()).add(tokens.get(i-1)).add(tokens.get(i+1)));
	  tokens.add(i+1, NULL);
	  tokens.add(i, NULL);
	  i = 1;
	} else
	  i += 2;
      }
      if(tokens.length() != 1) 
	throw new IllegalStateException("Spurious parser state: "+tokens);
      return tokens.get(0);
    }
  }
  */

  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  /** Returns this logical-structure structure as a one-line string.
   * @param format <ul>
   * <li>"raw" To write in a normalized 1D plain text format (default).</li>
   * <li>"eml" To write as a standard expression.</li>
   * <li>or other PML <a href="Pml.html#output-format">output format</a>.
   * </ul>
   */
  public String toString(String format) { 
    if ("eml".equals(format)) {
      return toString(this, 0); 
    } else {     
      return super.toString(format);
    }
  }

  // WRITE INTERFACE ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  /** Writes a PML as an expression. */
  private static String toString(Pml value, int precedence) {
    StringBuffer b = new StringBuffer();
    // Write an operator
    String name = value.getTag(); int length = value.getCount(); int precedence0 = getPrecedence(name);
    // Writes an operator
    if (precedence0 > -999) { 
      // Write a parenthesised value.
      if (precedence0 > precedence) {
	return b.append("(").append(toString(value, 0)).append(")").toString();
      }
      // Write an prefix operator 
      if ((length == 1) && (name.equals("!") || name.equals("-"))) {
	return b.append(name).append(toString(value.getChild(0), precedence0)).toString();
      }
      // Write a infix operator
      {
	for (int i = 0; i < length; i++) {
	  b.append(toString(value.getChild(i), precedence0));
	  if (i == 0 || i < length-1) b.append(" ").append(name).append(" ");
	}
	return b.toString();
      }
      // Write the assignation operaator
    } else  if (length == 3 && name.equals("set")) {
      b.append(toString(value.getChild(0), 999)).
	append(" ").append(toString(value.getChild(1), 999)).
	append(" = ").append(toString(value.getChild(2), 1)).
	append("; ");
      // Write the conditional operator.
    } else if ((length == 2 || length == 3) && name.equals("if")) {
      b.append(" if (").append(toString(value.getChild(0), 0)).
	append(" ) { ").append(toString(value.getChild(1), 0));
      if (length == 3)
	b.append(" } else { ").append(toString(value.getChild(2), 0));
      b.append(" } ");
      return b.toString();
      // Write the iteration operators.
    } else if (length == 2 && name.equals("while")) {
      b.append(" while (").append(toString(value.getChild(0), 0)).
	append(" ) { ").append(toString(value.getChild(1), 0)).
	append(" } ");
      // Write a functional term
    } else {
      b.append(name);
      if (value.getCount() < value.getSize()) {
	b.append("{");
	boolean next = false;
	for(String aname : value.attributes()) {
	  b.append(aname).append(" = ").append(toString(value.getChild(aname), 0));
	  if (next) b.append(", ");
	  next = true;
	}	
	b.append("}");
      }
      if (0 < value.getCount()) {
	b.append("(");
	for (int i = 0; i < length; i++) {
	  b.append(toString(value.getChild(i), 0));
	  if (i < length-1) b.append(", ");
	}
	b.append(")");
      }
    }
    return b.toString();
  }

  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  //
  // Defines operator precedences: infix operators as it, prefix operators are prefixed by a space, postfix operators are postfixed by a space
  //
  private static int getPrecedence(String op) { for(int i = 0; i < precedences.length; i++) if (precedences[i].equals(op)) return -1-i; return -999; }
  private static final String[] precedences = new String[]
    { "=", "||", "&&", "!", "==", "!=", ">=", "<=", ">", "<", "+", "-", "*", "/", "%", ".", "-"};

  /** Used to check the syntax the well-formedness by mirroring the Eml structure in a normalized format.
   * @param usage <tt>java org.javascool.Eml input-file [output-file]</tt>
   * <p>- The file name be a EML, PML, or XML file name, with the corresponding extensions</p>.
   */
  public static void main(String[] usage) {
    if (usage.length > 0) new Eml().load(usage[0]).save(usage.length > 1 ? usage[0] : "stdout:");
  }
}
