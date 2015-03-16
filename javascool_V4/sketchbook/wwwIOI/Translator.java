/*******************************************************************************
* Thierry.Vieville@sophia.inria.fr, Copyright (C) 2009.  All rights reserved. *
*******************************************************************************/
package org.javascool.proglets.wwwIOI;

import java.util.ArrayList;

/** Defines a Jvs code to Java standard code translation for this proglet.
 * @see <a href="Translator.java.html">code source</a>
 * @serial exclude
 */
public class Translator extends org.javascool.core.Translator {
  @Override
  public String translate(String code) {
    for(int i0 = 0, count = 0; i0 < code.length();) {
      // Identification des constructions : repeat ( .cond. ) { .expr. }
      int offset, icond[], iexpr[];
      if(code.startsWith("repeat", i0)) {
        // -System.err.println("Repeat 1/2: "+code.substring(i0).replace('\n', ' '));
        offset = i0;
        i0 += 6;
        icond = scanConstruct(code, i0, "(", ")", null, true, true);
        if(icond[0] != -1) {
          // -System.err.println("Repeat 2/3 ..");
          iexpr = scanInstruction(code, icond[1]);
          if(iexpr[0] != -1) {
            count++;
            // -System.err.println("Repeat 3/3 ..");
            String
            // Découpe du code selon les briques de l'instruction repeat
              before = code.substring(0, offset),
              head = code.substring(offset, icond[0]),
              cond = code.substring(icond[0], icond[1]),
              and = code.substring(icond[1], iexpr[0]),
              expr = code.substring(iexpr[0], iexpr[1]),
              after = code.substring(iexpr[1]),
            // Définition du prefix de la variable locale
              prefix = "jvsInternalVariable" + count + "_",
            // Dérivation du code java qui implémente le repeat
              begin = "for(int " + prefix + "N = " + cond + ", " + prefix + "I = 0; " + prefix + "I < " + prefix + "N; " + prefix + "I++) { int " + prefix + "N0 = " + cond + ";",
              end = "if (" + prefix + "N0 != " + cond + ") System.out.println(\"Attention une boucle repeat() tente de modifier sa valeur à l'interieur de son code\"); }",
            // Assemblage du code dérivé en ajoutant les tags pour éliminer le code dérivé lors d'une erreur de compilation
              code0 = before + "/*" + (head + cond).replaceAll("\\*/", "* /") + "@<nojavac*/" + begin + "/*@nojavac>*/",
              code1 = and + expr + "/*@<nojavac*/" + end + "/*@nojavac>*/" + after;
            i0 = code0.length();
            code = code0 + code1;
            // -System.err.println(("Code dérivé <repeat head='"+head+"' cond='"+cond+"' and='"+and+"' expr='"+expr+"'/> : "+code).replace('\n', ' '));
          }
        }
      } else {
        i0 = scanComments(code, i0, true);
      }
    }
    return code;
  }
  /* Voici un code de mise au point.
   *  public static void main(String usage[]) {
   *  String code = "void main() {\n  // Quelques repeats\n"+
   *   "  repeat(3 /* et de \"un\" !* /) {\n"+
   *   "    repeat  /* et de \"deux\" !* / (5 - 12 / 4) println(\"ok les repeat!\");\n"+
   *   "  }\n"+
   *   "}\n";
   *  System.err.println("RepeatOut:\n"+new Translator().translate(code));
   *  }
   */
}

