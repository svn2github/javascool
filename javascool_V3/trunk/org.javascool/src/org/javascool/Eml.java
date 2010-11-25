/*******************************************************************************
* Thierry.Vieville@linux-azur.org, Copyright (C) 2010.  All rights reserved.  *
*******************************************************************************/

/* Missing features
 * out: Array assignment
 * in : c ? : e1 : e0
 */

/* Caveats
 * in : Any difference between a() and a
 * out: Pml attributes generate a non parsable construct
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
public class Eml extends Pml {
  /**/ public Eml() {}
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
    if("eml".equals(format)) {
      reset(new ExpressionReader().read(value));
      return this;
    } else
      return super.reset(value, format);
  }
  // READ INTERFACE  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  /** Defines an expression reader. */
  private static class ExpressionReader extends TokenReader {
    /** Parses the string and set the parameters in the pml structure. */
    public Pml read(String string) {
      System.out.println("Parsing :\n---------------------------------------------------\n" + string + "\n---------------------------------------------------\n");
      reset(string);
      Pml pml = parseInstruction();
      check(trailer().length() == 0, "il reste du texte qui n'a pu être analysé");
      System.out.println("Parsed : " + pml + "\n---------------------------------------------------\n");
      return pml;
    }
    // Parses a algorithmic construct
    private Pml parseInstruction() {
      Pml pml;
      if((pml = parseSeq()) != null)
        return pml;
      if((pml = parseLet()) != null)
        return pml;
      if((pml = parseIf()) != null)
        return pml;
      if((pml = parseWhile()) != null)
        return pml;
      return parseCall();
    }
    // Parses an assignment of syntax: "[type] name = value;"
    private Pml parseLet() {
      if("=".equals(current(2))) {
        String t = current();
        next();
        String n = current();
        next(2);
        Pml pml = new Pml().setTag("let").add(new Pml().setTag(t)).add(new Pml().setTag(n)).add(parseExpression(0));
        check(";".equals(current()), "le caractère ';' a été oublié");
        if(";".equals(current()))
          next();
        return pml;
      } else if("=".equals(current(1))) {
        String n = current();
        next(2);
        Pml pml = new Pml().setTag("let").add("void").add(new Pml().setTag(n)).add(parseExpression(0));
        check(";".equals(current()), "le caractère ';' a été oublié");
        if(";".equals(current()))
          next();
        return pml;
      }
      return null;
    }
    // Parses a conditional of syntax: "if (c) { e1 } else { e0 }"
    private Pml parseIf() {
      if("if".equals(current())) {
        next();
        Pml pml = new Pml().setTag("if");
        pml.add(parseExpression(0));
        pml.add(parseInstruction());
        if("else".equals(current())) {
          next();
          pml.add(parseInstruction());
        }
        return pml;
      } else
        return null;
    }
    // Parses a conditional of syntax: "while (c) { e }"
    private Pml parseWhile() {
      if("while".equals(current())) {
        next();
        Pml pml = new Pml().setTag("while");
        pml.add(parseExpression(0));
        pml.add(parseInstruction());
        return pml;
      } else
        return null;
    }
    // Parses a sequence a instruction sequence "{ (i1 (; ik)*)? }"
    private Pml parseSeq() {
      if("{".equals(current())) {
        next();
        Pml pml = new Pml().setTag("seq");
        while(true) {
          pml.add(parseInstruction());
          if("}".equals(current())) {
            next();
            break;
          }
        }
        return pml;
      } else
        return null;
    }
    // Parses a function call
    private Pml parseCall() {
      Pml pml = parseExpression(0);
      check(";".equals(current()), "le caractère ';' a été oublié");
      if(";".equals(current()))
        next();
      return pml;
    }
    // Parses an expression with infix operator of syntax: item1 "O" item2
    private Pml parseExpression(int precedence) {
      // Stores the infix expression sequence
      Pml tokens = new Pml();
      while(true) {
        tokens.add(parseToken());
        if((getPrecedence(current()) == -999) || (getPrecedence(current()) >= precedence))
          break;
        tokens.add(new Pml().setTag(current()));
        next();
      }
      check((tokens.getCount() % 2) == 1, "il manque un argument à l'expression");
      // Reduces the expression sequence
      for(int i = 1; 1 < tokens.getCount();) {
        if(((i == 1) || (getPrecedence(tokens.getChild(i).getTag()) < getPrecedence(tokens.getChild(i - 2).getTag()))) &&
           ((i == tokens.getCount() - 2) || (getPrecedence(tokens.getChild(i).getTag()) <= getPrecedence(tokens.getChild(i + 2).getTag()))))
        {
          tokens.set(i - 1, new Pml().setTag(tokens.getChild(i).getTag()).add(tokens.getChild(i - 1)).add(tokens.getChild(i + 1)));
          tokens.del(i);
          tokens.del(i);
          i = 1;
        } else
          i += 2;
      }
      if(tokens.getCount() != 1) throw new IllegalStateException("Spurious parser state: " + tokens);
      return tokens.getChild(0);
    }
    private Pml parseToken() {
      Pml pml;
      if((pml = parseParenthesis()) != null)
        return pml;
      if((pml = parseTerm()) != null)
        return pml;
      if((pml = parsePrefix()) != null)
        return pml;
      { pml = new Pml();
        pml.setTag(current());
        next();
        return pml;
      }
    }
    // Parses an expression between parentheses
    private Pml parseParenthesis() {
      String t = current();
      if("(".equals(t)) {
        next();
        Pml pml = parseExpression(0);
        check(")".equals(current()), "le caractère ')' a été oublié");
        if(")".equals(current()))
          next();
        return pml;
      }
      return null;
    }
    // Parses a term of syntax: "name { att=val, ..}  (element, ..)"
    private Pml parseTerm() {
      if("(".equals(current(1)) || "{".equals(current(1))) {
        Pml pml = new Pml().setTag(current());
        next();
        // Parses an attribute's list
        if("{".equals(current())) {
          next();
          while(true) {
            if("}".equals(current())) {
              next();
              break;
            } else if("=".equals(current(1))) {
              String n = current();
              next(2);
              pml.set(n, parseExpression(0));
            } else {
              check(!"=".equals(current()), "il manque un nom ou une value");
              if("=".equals(current()))
                next();
              String n = current();
              next();
              pml.set(n, "true");
            }
          }
        }
        // Parses the terms's elements
        if("(".equals(current())) {
          next();
          while(true) {
            check(!"}".equals(current()), "un terme est tronqué");
            if("}".equals(current()))
              break;
            else if(")".equals(current())) {
              next();
              break;
            } else {
              pml.add(parseExpression(0));
              check(",".equals(current()) || ")".equals(current()), "il manque une ',' ou une ')'");
              if(",".equals(current()))
                next();
            }
          }
        }
        return pml;
      } else
        return null;
    }
    // Parses an expression with prefix operator of syntax: "O" item1
    private Pml parsePrefix() {
      String t = current();
      if("!".equals(t) || "-".equals(t)) {
        next();
        Pml pml = new Pml().setTag(t).add(parseExpression(0));
        return pml;
      }
      return null;
    }
  }

  // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  /** Returns this logical-structure structure as a one-line string.
   * @param format <ul>
   * <li>"raw" To write in a normalized 1D plain text format (default).</li>
   * <li>"eml" To write as a standard expression.</li>
   * <li>or other PML <a href="Pml.html#output-format">output format</a>.
   * </ul>
   */
  public String toString(String format) {
    if("eml".equals(format))
      return Jvs2Java.reformat(toString(this, 0));
    else
      return super.toString(format);
  }
  // WRITE INTERFACE ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  /** Writes a PML as an expression. */
  private static String toString(Pml value, int precedence) {
    if(value == null)
      return "(null)";
    StringBuffer b = new StringBuffer();
    // Write an operator
    String name = value.getTag();
    int length = value.getCount();
    int precedence0 = getPrecedence(name);
    // Writes an operator
    if(precedence0 > -999) {
      // Write a parenthesised value.
      if(precedence0 > precedence)
        return b.append("(").append(toString(value, 0)).append(")").toString();
      // Write an prefix operator
      if((length == 1) && (name.equals("!") || name.equals("-")))
        return b.append(name).append(toString(value.getChild(0), precedence0)).toString();
      // Write a infix operator
      {
        for(int i = 0; i < length; i++) {
          b.append(toString(value.getChild(i), precedence0));
          if((i == 0) || (i < length - 1))
            b.append(" ").append(name).append(" ");
        }
        return b.toString();
      }
      // Write the sequence operator
    } else if((length > 0) && name.equals("seq")) {
      b.append("{");
      for(int i = 0; i < length; i++)
        b.append(toString(value.getChild(i), 999));
      b.append("}");
      // Write the assigment operator.
    } else if((length == 3) && name.equals("let") && (value.getChild(0).getSize() == 0) && (value.getChild(1).getSize() == 0)) {
      if(!"void".equals(value.getChild(0).getTag()))
        b.append(value.getChild(0).getTag()).
        append(" ");
      b.append(value.getChild(1).getTag()).
      append(" = ").append(toString(value.getChild(2), 0)).
      append("; ");
      // Write the conditional operator.
    } else if(((length == 2) || (length == 3)) && name.equals("if")) {
      b.append(" if (").append(toString(value.getChild(0), 0)).
      append(") ").append(toString(value.getChild(1), 0));
      if(length == 3)
        b.append(" else ").append(toString(value.getChild(2), 0));
      return b.toString();
      // Write the iteration operators.
    } else if((length == 2) && name.equals("while"))
      b.append(" while (").append(toString(value.getChild(0), 0)).
      append(") ").append(toString(value.getChild(1), 0));
      // Write a functional term
    else {
      b.append(name);
      if(value.getCount() < value.getSize()) {
        b.append("{");
        boolean next = false;
        for(String aname : value.attributes()) {
          b.append(aname).append(" = ").append(toString(value.getChild(aname), 0));
          if(next)
            b.append(", ");
          next = true;
        }
        b.append("}");
      }
      if(0 < value.getCount()) {
        b.append("(");
        for(int i = 0; i < length; i++) {
          b.append(toString(value.getChild(i), 0));
          if(i < length - 1)
            b.append(", ");
        }
        b.append(")");
      }
      if(precedence == 999)
        b.append(";");
    }
    return b.toString();
  }
  // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  //
  // Defines operator precedences: infix operators as it, prefix operators are prefixed by a space, postfix operators are postfixed by a space
  //
  private static int getPrecedence(String op) {
    for(int i = 0; i < precedences.length; i++)
      if(precedences[i].equals(op))
        return -i;
    return -999;
  }
  private static final String[] precedences = new String[]
  { "=", "||", "&&", "!", "==", "!=", ">=", "<=", ">", "<", "+", "-", "*", "/", "%", ".", "-" };

  /** Used to check the syntax the well-formedness by mirroring the Eml structure in a normalized format.
   * @param usage <tt>java org.javascool.Eml input-file [output-file]</tt>
   * <p>- The file name be a EML, PML, or XML file name, with the corresponding extensions</p>.
   */
  public static void main(String[] usage) {
    if(usage.length > 0)
      new Eml().load(usage[0], "eml").save(usage.length > 1 ? usage[0] : "stdout:", "eml");
  }
}
