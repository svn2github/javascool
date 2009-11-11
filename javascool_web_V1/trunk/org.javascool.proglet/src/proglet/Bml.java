/*******************************************************************************
 * Thierry.Vieville@sophia.inria.fr, Copyright (C) 2009.  All rights reserved. *
 *******************************************************************************/

package proglet;

import java.util.Properties;

/** Defines a parametric logical-structure. 
 *
 * - A parameter logical-structure is a generic lightweight data structure of the form:
 *  <div style="margin-left: 40px"><tt>"{ -name value .. element .. }"</tt></div>where <ul>
 * <li>the <tt>'- -element'</tt> construct allows to have an element starting with <tt>'-'</tt>, while</li>
 * <li>strings with spaces or <tt>{</tt> or <tt>}</tt> chars are encapsulated with <tt>'"'</tt> (escaped with <tt>'\"'</tt>),</li>
 * <li>the default value being <tt>'true'</tt>.</li>
 * </ul> This syntax is minimal, easy to read and write and completely standard.
 * 
 * This defines: <ul>
 *   <li><i>Attributes</i> explicitly indexed by names</li>
 *   <li><i>Elements</i> implicitly indexed by integer >= 0</li>
 * </ul> (eventually recursive) and not more.
 *
 * - It is implemented as an associative table and can be viewed as XML logical-structure, but with a very light syntax.
 * - The parameter logical-structure parsing from a string is <i>weak</i> in the sense that a value is always derived without generating syntax errors.
 * - This is used to manage non-trivial routine parameters or to interface with other applications.
 */
class Bml extends Properties {
  private static final long serialVersionUID = 1L;

  /** Resets the logical-structure.
   * @param string The logical-structure fields given as a string, using a syntax of the form:
   * <div style="margin-left: 40px"><tt>"{ -name value .. element .. }"</tt></div>
   * while, if the string has a tail with unparsed elements it is ignored.
   */
  public void reset(String string) {
  }

/*
  public void reset(String string) {
    $ll = strlen($string); $ii = 0;
    // Skips the '{'
    self::skip_space($ii, $string, $ll); if ($string[$ii] == '{') { $ii++; self::skip_space($ii, $string, $ll); }
    // Scans the string
    $nn = 0; while(true) {
      // Checks for end of loop
      if ($ii > $ll || $string[$ii] == '}') return;
      // Detects attribute or element
      $i0 = $ii; self::skip_value($ii, $string, $ll); $name = substr($string, $i0, $ii - $i0); self::skip_space($ii, $string, $ll);
      if ($name == "-") {
	$i0 = $ii; self::skip_value($ii, $string, $ll); $this->set($nn++, self::unquote_value(substr($string, $i0, $ii - $i0)));
      } else if($name[0] == '-') {
	if ($string[$ii] == '-') {
	  $this->set(substr($name, 1), "true");
	} else {
	  $i0 = $ii; self::skip_value($ii, $string, $ll); $this->set(substr($name, 1), self::unquote_value(substr($string, $i0, $ii - $i0)));
	}
      } else {
	$this->set($nn++, self::unquote_value($name));
      }
      self::skip_space($ii, $string, $ll);
    }
  }
  private static function unquote_value($value) {
    $ll = strlen($value) - 1;
    if ($value[0] == '"' && $value[$ll] == '"') {
      $string = ""; for($ii = 1; $ii < $ll; $ii++) $string .= ($value[$ii] == '\\' && $ii < $ll && $value[$ii+1] == '"') ? '"' : $value[$ii]; return $string;
    } else 
      return $value;
  }
  private static function skip_space(&$ii, $string, $ll) { 
    for(; $ii <= $ll && ctype_space($string[$ii]); $ii++); 
  }
  private static function skip_value(&$ii, $string, $ll) {
    switch($string[$ii]) {
    case '"': 
      self::skip_quote($ii, $string, $ll); break;
    case '{': { 
      $dd = 1; for($ii++; $ii <= $ll && $dd > 0; $ii++) switch($string[$ii]) {
      case '"': self::skip_quote($ii, $string, $ll); break;
      case '{': $dd++; break;
      case '}': $dd--; break;
      }}; break;
    default:
      for(; $ii <= $ll && $string[$ii] != '}' && !ctype_space($string[$ii]); $ii++); break;
    }
  }
  private static function skip_quote(&$ii, $string, $ll) { 
    for($ii++; $ii <= $ll && $string[$ii] != '"'; $ii++) if ($string[$ii] == '\\' && $ii < $ll && $string[$ii+1] == '"') $ii++;
  }
*/

  /** Converts the logical-structure structure as a string. */
  public String toString() {
    return null;
  }

/*
  public function asString() {
    $string = "{";
    foreach($this as $name => $value) if (!is_index($name)) { $string .= " -" . $name . " "; self::append_value($string, $value); }
    foreach($this as $name => $value) if (is_index($name))  { $string .= " "; self::append_value($string, $value); }
    $string .= " }";
    return $string;
  }
  private static function append_value(&$string, $value) {
    $unquoted = true; for($ii = 0; $unquoted && $ii < strlen($value); $ii++) 
      if (ctype_space($value[$ii]) || $value[$ii] == '"' || $value[$ii] == '{' || $value[$ii] == '}') $unquoted = false;
    if ($unquoted) { $string .= $value; } else {
      $string .= "\""; for($ii = 0; $ii < strlen($value); $ii++) if ($value[$ii] == '"') $string .= "\\\""; else $string .= $value[$ii]; $string .= '"'; 
    }
  }
  private function __toString() { return $this->asString(); }
*/

  /** Used for tests. */
  public static void main(String[] arg) {
    System.out.println("Hi !");
  }
}
