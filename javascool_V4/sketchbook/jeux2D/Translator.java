package org.javascool.proglets.jeux2D;

import java.lang.String;

/** Définit la traduction d'un code Jvs en code Java  pour manipuler la proglet «jeux2D» (A DÉTRUIRE SI NON UTILISÉ).
 *
 * @see <a href="Translator.java.html">code source</a>
 * @serial exclude
 */
public class Translator extends org.javascool.core.Translator {
  @Override
  public java.lang.String getImports() {
    return "import org.javascool.proglets.jeux2D.*;";
  }
  @Override
  public java.lang.String translate(java.lang.String code) {
    String[] lines = code.split("\n");
    String body = "";
    for(String line : lines) {
      line = translateLine(line);
      body += line + "\n";
    }
    return body;
  }
  public java.lang.String translateLine(java.lang.String code) {
    code.replaceAll("(.*[^a-zA-Z0-9_])([a-zA-Z0-9_]+[ \t=]*\\.getProperty[ \t=]*\\()[ \t=]*([a-zA-Z0-9_]+)[ \t=]*,([^)]*\\))(.*)", "$1(($3)$2$4)$5");
    code.replaceAll("\\(int\\)", "(Integer)");
    code.replaceAll("\\(double\\)", "(Double)");
    code.replaceAll("((^|\n)([ \t]*)(?!((public|private|protected)([ \t]+)))([A-Za-z0-9_]+)([ ]+)([A-Za-z0-9_]+)\\(([^()]*)\\)([ ]*)\\{([ \t]*)(\n|$))", "public $1");
    code.replaceAll("(^|[\n\t ])for[\n\t ]*\\(([A-Za-z0-9_.]+)[\n\t ]+([A-Za-z0-9_.]+)[\n\t ]+in[\n\t ]+([A-Za-z0-9_.]+)[\n\t ]*\\)[\n\t ]*\\{", "for (int tmpsystemi=0; tmpsystemi<$4.size(); tmpsystemi++) {$2 $3=($2)($4.get(tmpsystemi));");
    return code;
  }
}
