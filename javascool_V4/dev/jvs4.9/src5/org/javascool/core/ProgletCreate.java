package org.javascool.core;

import java.io.File;
import java.util.HashMap;
import org.javascool.tools.FileManager;

import org.javascool.widgets.MainFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.JTextField;
import javax.swing.JButton;
import org.javascool.macros.Macros;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFileChooser;

/** Définit le mécanisme de création d'une proglet vide dans sa version jvs5.
 * @see <a href="ProgletCreate.java.html">code source</a>
 * @serial exclude
 */
public class ProgletCreate {
  // @factory

  private ProgletCreate() {}

  /** Crée un répertoire d'une proglet et y installe des modèles de fichiers.
   * @param progletDir Le répertoire où se trouvent les fichiers de la proglet.
   * @return La valeur true en cas de succès, false si il y a des erreurs de compilation.
   *
   * @throws RuntimeException Si une erreur d'entrée-sortie s'est produite lors de la compilation ou construction.
   */
  public static boolean build(String progletDir) {
    try {
      String name = new File(progletDir).getName();
      Proglet2Jar.checkProgletName(name);
      // Copie les fichiers exemple dans le répertoire
      {
	new File(progletDir).mkdirs();
	for (String fileName : filePatterns.keySet()) {
	  String pathName = progletDir + File.separator + fileName;
	  if (new File(pathName).exists()) {
	    System.out.println("le fichier " + fileName + " existe déjà, on ne le modifie pas");
	  } else {
	    FileManager.save(pathName, filePatterns.get(fileName).replaceAll("@name", name), true);
	  }
	}
	System.out.println("La proglet «" + name + "» est crée dans " + progletDir + ".");
      }
      return true;
    } catch (Throwable e) {
      System.out.println(e);
      e.printStackTrace();
      return false;
    }
  }
  /**
   * @see #build(String)
   */
  public static boolean build(String ProgletDir, boolean verbose) {
    if (verbose)
      System.out.println("Compilation de "+new File(ProgletDir).getName()+"..");
    boolean built = build(ProgletDir);
    if (verbose && built)
      System.out.println("achevée avec succès :\n Le répertoire '"+ProgletDir+" est disponible");
    return built;
  }

  /** Lanceur de la création dune proglet.
   * @param usage <tt>java org.javascool.core.ProgletCreate progletDir</tt>
   */
  public static void main(String[] usage) {
    if (usage.length == 1) {
      build(usage[0]);
    }
  }

  private static HashMap<String,String> filePatterns = new HashMap<String,String>();

  static {

    filePatterns.put("proglet.json", 
		     "{\n" +
		     "  \"name\"   : \"@name\",\n" +
		     "  \"title\"  : \"Exemple de «proglet»\",\n" +
		     "  \"author\" : \"Prénom Nom\",\n" +
		     "  \"email\"  : \"email@serveur.com\",\n" +
		     "  \"icon\"   : \"sample.png\"\n" +
		     "}\n");

    filePatterns.put("help.html", 
		     "<div><h2>\"La «proglet» @name\"</h2>\n" +
		     "  <div class=\"objectif\">\n" +
		     "  </div>\n" +
		     "  <div class=\"intros\">\n" +
		     "    <div><h3>titre item</h3>\n" +
		     "    </div>\n" +
		     "  etc..\n" +
		     "  </div>\n" +
		     "  <div class=\"works\">\n" +
		     "    <div><h3>titre item</h3>\n" +
		     "    </div>\n" +
		     "  etc..\n" +
		     "  </div>\n" +
		     "  <div class=\"notes\">\n" +
		     "    <div id=\"référence note\"><h3>titre item</h3>\n" +
		     "    </div>\n" +
		     "  etc..\n" +
		     "  </div>\n" +
		     "</div>\n");

    filePatterns.put("completion.json", 
		     "[\n" +
		     "  {\n" +
		     "    \"name\"  : \"nom de la complétion\",\n" +
		     "    \"title\" : \"description en ligne\",\n" +
		     "    \"code\"  : \"texte source de la complétion\",\n" +
		     "    \"doc\"   : \"Texte qui documente la fonction de l'on complète\"\n" +
		     "  } \n" +
		     "]\n");
		     
    filePatterns.put("Functions.java", 
		     "package org.javascool.proglets.@name;\n" +
		     "import static org.javascool.macros.Macros.*;\n" +
		     "\n" +
		     "/** Définit les fonctions pour manipuler la proglet «@name» (FICHIER À DÉTRUIRE SI NON UTILISÉ).\n" +
		     " *\n" +
		     " * @see <a href=\"Functions.java.html\">code source</a>\n" +
		     " * @serial exclude\n" +
		     " */\n" +
		     "public class Functions {\n" +
		     "  private static final long serialVersionUID = 1L;\n" +
		     "  // @factory\n" +
		     "  private Functions() {}\n" +
		     "  /** Renvoie l'instance de la proglet pour accéder à ses éléments.\n" +
		     "   * <p> Utilisé dans une construction de type <tt>getPane().appelDeMethode(..)</tt>.</p>\n" +
		     "   */\n" +
		     "  private static Panel getPane() {\n" +
		     "     return org.javascool.macros.Pane.getProgletPane();\n" +
		     "  }\n" +
		     "\n" +
		     "  //@todo Définir ici les fonctions <tt>public static</tt>\n" +
		     "\n" +
		     "}\n");

    filePatterns.put("Panel.java", 
		     "package org.javascool.proglets.@name;\n" +
		     "import static org.javascool.macros.Macros.*;\n" +
		     "import static org.javascool.proglets.@name.Functions.*;\n" +
		     "import javax.swing.JPanel;\n" +
		     "\n" +
		     "/** Définit le panneau graphique de la proglet «@name» (FICHIER À DÉTRUIRE SI NON UTILISÉ).\n" +
		     " *\n" +
		     " * @see <a href=\"Panel.java.html\">code source</a>\n" +
		     " * @serial exclude\n" +
		     " */\n" +
		     "public class Panel extends JPanel /* ou tout autre Component pertinent. */ {\n" +
		     "  private static final long serialVersionUID = 1L;\n" +
		     "\n" +
		     "  // @bean\n" +
		     "public Panel() {  // @todo Définir ici la construction de l'objet graphique\n" +
		     "  }\n" +
		     "\n" +
		     "}\n");

    filePatterns.put("Translator.java", 
		     "package org.javascool.proglets.@name;\n" +
		     "\n" +
		     "/** Définit la traduction d'un code Jvs en code Java  pour manipuler la proglet «@name» (FICHIER À DÉTRUIRE SI NON UTILISÉ).\n" +
		     " *\n" +
		     " * @see <a href=\"Translator.java.html\">code source</a>\n" +
		     " * @serial exclude\n" +
		     " */\n" +
		     "public class Translator extends org.javascool.core.Translator {\n" +
		     "    @Override\n" +
		     "     public String getImports() {\n" +
		     "    return \"\";\n" +
		     "  }\n" +
		     "    @Override\n" +
		     "  public String translate(String code) {\n" +
		     "    return code;\n" +
		     "  }\n" +
		     "}\n");
  }
}
