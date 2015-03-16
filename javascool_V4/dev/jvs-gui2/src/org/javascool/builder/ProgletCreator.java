/*********************************************************************************
* Philippe.Vienne@sophia.inria.fr, Copyright (C) 2011.  All rights reserved.    *
* Guillaume.Matheron@sophia.inria.fr, Copyright (C) 2011.  All rights reserved. *
* Thierry.Vieville@sophia.inria.fr, Copyright (C) 2009.  All rights reserved.   *
*********************************************************************************/

package org.javascool.builder;

import java.io.File;

import org.javascool.tools.FileManager;

/**
 * Cette factory contient les mécanismes de construction d'une nouvelle proglet.
 * <h1>Ebauche non finalisée: ce mécanisme est au stade d'ébauche.</h1>
 *
 * @see <a href="ProgletCreator.java.html">code source</a>
 * @serial exclude
 */
public class ProgletCreator {
  // @factory
  private ProgletCreator() {}
  /**
   * Crée un répertoire et les fichiers de base de la proglet.
   *
   * @param location
   *            Emplacement de la proglet, le nom du répertoire correspond à
   *            celui de la proglet.
   * @return La valeur true si la construction a pu se faire, false sinon.
   *
   * @throws IllegalArgumentException
   *             Si le nom de la proglet n'est pas un nom Java standard.
   * @throws RuntimeException
   *             Si il y a eu une erreur d'entrée sortie lors de la création.
   */
  public static boolean mkdirProglet(String location) {
    String name = new File(location).getName();
    if(!name.matches("[a-z][a-zA-Z][a-zA-Z][a-zA-Z]+")) {
      System.out
      .println("Le nom de la proglet «"
               + name
               + "» est bizarre: \n il ne doit contenir que des lettres, faire au moins quatre caractères, et démarrer par une minuscule.");
      System.out.println("Impossible de créer une telle proglet");
      return false;
    }
    if(new File(location).isDirectory()) {
      System.out.println("Le répertoire «" + location
                         + "» existe déjà, les fichiers existants seront renommés");
    }
    if((!new File(location).isDirectory()) && !new File(location).mkdirs()) {
      String tail = new File(location).exists() ? "un fichier existe à cet emplacement"
                    : "il doit être interdit de créer le répertoire ici";
      System.out.println("Impossible de créer le répertoire «" + location
                         + "» de la proglet, " + tail);
    }
    FileManager.save(location + File.separator + "proglet.pml",
                     ProgletCreator.progletPattern.replaceAll("@name", name), true);
    FileManager.save(location + File.separator + "help.xml",
                     ProgletCreator.helpPattern.replaceAll("@name", name), true);
    FileManager.save(location + File.separator + "Panel.java",
                     ProgletCreator.panelPattern.replaceAll("@name", name), true);
    FileManager
    .save(location + File.separator + "Functions.java",
          ProgletCreator.functionsPattern.replaceAll("@name",
                                                     name), true);
    FileManager.save(location + File.separator + "completion.xml",
                     ProgletCreator.completionPattern.replaceAll("@name", name),
                     true);
    FileManager.save(location + File.separator + "Translator.java",
                     ProgletCreator.translatorPattern.replaceAll("@name", name),
                     true);
    System.out.println("La proglet «" + name + "» est crée dans "
                       + location);
    return true;
  }
  private static final String progletPattern = "title=\"Exemple de «proglet»\"\n"
                                               + "author=\"Prenom Nom<email@serveur.com>\"\n"
                                               + "icon=\"sample.png\"\n";
  private static final String helpPattern = "<div title=\"La «proglet» @name\">\n"
                                            + "  <div class=\"objectif\">\n"
                                            + "  </div>\n"
                                            + "  <div class=\"intros\">\n"
                                            + "    <div title=\"item 1\">\n"
                                            + "    </div>\n"
                                            + "  etc..\n"
                                            + "  </div>\n"
                                            + "  <div class=\"works\">\n"
                                            + "    <div title=\"item 1\">\n"
                                            + "    </div>\n"
                                            + "  etc..\n"
                                            + "  </div>\n"
                                            + "  <div class=\"notes\">\n"
                                            + "   <!-- référencées par des tags de la forme <l class=\"note\" link=\"1\"/> -->\n"
                                            + "    <div title=\"item 1\">\n"
                                            + "    </div>\n"
                                            + "  etc..\n"
                                            + "  </div>\n" + "</div>\n";
  private static final String panelPattern = "package org.javascool.proglets.@name;\n"
                                             + "import static org.javascool.macros.Macros.*;\n"
                                             + "import static org.javascool.proglets.@name.Functions.*;\n"
                                             + "import javax.swing.JPanel;\n"
                                             + "\n"
                                             + "/** Définit le panneau graphique de la proglet «@name» (A DÉTRUIRE SI NON UTILISÉ).\n"
                                             + " *\n"
                                             + " * @see <a href=\"Panel.java.html\">code source</a>\n"
                                             + " * @serial exclude\n"
                                             + " */\n"
                                             + "public class Panel extends JPanel /* ou tout autre Component pertinent. */ {\n"
                                             + "\n"
                                             + "  // @bean\n"
                                             + " public Panel() {"
                                             + "  // @"
                                             + "todo Définir ici la construction de l'objet graphique\n"
                                             + "  }\n"
                                             + "\n"
                                             + "  /** Démo de la proglet. */\n"
                                             + "  public void start() {\n"
                                             + "  // @"
                                             + "todo Définir ici le code de démo de la proglet.\n"
                                             + "  }\n"
                                             + "\n" + "}\n";
  private static final String functionsPattern = "package org.javascool.proglets.@name;\n"
                                                 + "import static org.javascool.macros.Macros.*;\n"
                                                 + "\n"
                                                 + "/** Définit les fonctions pour manipuler la proglet «@name» (A DÉTRUIRE SI NON UTILISÉ).\n"
                                                 + " *\n"
                                                 + " * @see <a href=\"Functions.java.html\">code source</a>\n"
                                                 + " * @serial exclude\n"
                                                 + " */\n"
                                                 + "public class Functions {\n"
                                                 + "  private static final long serialVersionUID = 1L;\n"
                                                 + "  // @factory\n"
                                                 + "  private Functions() {}\n"
                                                 + "  /** Renvoie l'instance de la proglet pour accéder à ses éléments.\n"
                                                 + "   * <p> Utilisé dans une construction de type <tt>getPane().appelDeMethode(..)</tt>.</p>\n"
                                                 + "   */\n"
                                                 + "  private static Panel getPane() {\n"
                                                 + "     return getProgletPane();\n"
                                                 + "  }\n"
                                                 + "\n"
                                                 + "  //@"
                                                 + "todo Définir ici les fonctions <tt>public static</tt>\n"
                                                 + "\n"
                                                 + "}\n";

  private static final String completionPattern = "<keywords>\n"
                                                  + "  <keyword \n"
                                                  + "    name=\"nom de la complétion\" \n"
                                                  + "    title=\"description en ligne\">\n"
                                                  + "    <code>texte source de la complétion</code>\n"
                                                  + "    <doc>Texte qui documente la fonction de l'on complète</doc>\n"
                                                  + "  </keyword>\n" + "  <!-- autres keyword -->\n"
                                                  + "</keywords>\n";

  private static final String translatorPattern = "package org.javascool.proglets.@name;\n"
                                                  + "\n"
                                                  + "/** Définit la traduction d'un code Jvs en code Java  pour manipuler la proglet «@name» (A DÉTRUIRE SI NON UTILISÉ).\n"
                                                  + " *\n"
                                                  + " * @see <a href=\"Translator.java.html\">code source</a>\n"
                                                  + " * @serial exclude\n"
                                                  + " */\n"
                                                  + "public class Translator extends org.javascool.core.Translator {\n"
                                                  + "    @Override\n"
                                                  + "     public String getImports() {\n"
                                                  + "    return \"\";\n"
                                                  + "  }\n"
                                                  + "    @Override\n"
                                                  + "  public String translate(String code) {\n"
                                                  + "    return code;\n" + "  }\n" + "}\n";
}
