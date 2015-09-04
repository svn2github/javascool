/**************************************************************
* Philippe VIENNE, Copyright (C) 2011.  All rights reserved. *
**************************************************************/
package org.javascool.core;

import java.io.File;

// Used to report a throwable
import java.lang.reflect.InvocationTargetException;
import java.lang.ArrayIndexOutOfBoundsException;
import org.javascool.tools.FileManager;

/** Implémente le mécanisme de base de traduction d'un code Jvs en code Java standard.
 * <p>Les erreurs de traduction sont affichées dans la console.</p>
 *
 * @see <a href="Jvs2Java.java.html">source code</a>
 * @serial exclude
 */
public class Jvs2Java extends Translator {
  // @bean
  public Jvs2Java() {}
 
  /** Définit la proglet dans l'environnement du quel se fait la traduction.
   * @param proglet La proglet dans l'environnement du quel se fait la traduction.
   * @return Cet objet, permettant de définir la construction <tt>Jvs2Java translator = new Jvs2Java().setProglet(..)</tt>.
   */
  public Jvs2Java setProglet(Proglet proglet) {
    // @bean-parameter(Translator, progletTranslator, w);
    progletTranslator = proglet.getTranslator();
    progletPackageName = proglet.hasFunctions() ? "org.javascool.proglets." + proglet.getName() : null;
    this.proglet = proglet;
    return this;
  }
  // Le mécanisme de traduction spécifique d'une proglet donnée.
  private Translator progletTranslator = null;
  // Le nom complet du package de la proglet.
  private String progletPackageName = null;
  // La proglet du contexte
  private Proglet proglet = null;

  /** Traduit un code javascool en langage java.
   * @param jvsCode Le code javascool à traduire
   * @param jvsName Le nom du code javascool (par défaut le nom de la proglet)
   * @return Le code java traduit
   */
  public String translate(String jvsCode, String jvsName) {
    String text = jvsCode.replace((char) 160, ' ');
    // Ici on ajoute
    if(!text.replaceAll("[ \n\r\t]+", " ").matches(".*void[ ]+main[ ]*\\([ ]*\\).*")) {
      if(text.replaceAll("[ \n\r\t]+", " ").matches(".*main[ ]*\\([ ]*\\).*")) {
        System.out.println("Attention: il faut mettre \"void\" devant \"main()\" pour que le programme puisse se compiler");
        text = text.replaceFirst("main[ ]*\\([ ]*\\)", "void main()");
      } else {
        System.out.println("Attention: il faut un block \"void main()\" pour que le programme puisse se compiler");
        text = "\nvoid main() {\n" + text + "\n}\n";
      }
    }
    String[] lines = text.split("\n");
    StringBuilder head = new StringBuilder();
    StringBuilder tail = new StringBuilder();
    StringBuilder body = new StringBuilder();
    // Here is the translation loop
    {
      int i = 1;
      // Copies the user's code
      for(String line : lines) {
        if(line.matches("^\\s*(import|package)[^;]*;\\s*$")) {
          head.append(line);
          body.append("//").append(line).append("\n");
          if(line.matches("^\\s*package[^;]*;\\s*$")) {
            System.out.println("Attention: on ne peut normalement pas définir de package Java en JavaScool\n le programme risque de ne pas s'exécuter correctement");
          }
        } else if(line.matches("^\\s*include[^;]*;\\s*$")) {
          String name = line.replaceAll("^\\s*include([^;]*);\\s*$", "$1").trim();
          body.append("/* include ").append(name).append("; */ ");
          try {
            String include = FileManager.load(name + ".jvs");
            for(String iline : include.split("\n"))
              if(iline.matches("^\\s*import[^;]*;\\s*$")) {
                head.append(iline);
              } else if(!iline.matches("^\\s*package[^;]*;\\s*$")) {
                body.append(iline);
              }
          } catch(Exception e) {
            body.append(" - Impossible de lire correctement le fichier inclut !!");
          }
          body.append("\n");
        } else {
          body.append(line).append("\n");
        }
        i++;
      }
      // Imports proglet's static methods
      head.append("import static java.lang.Math.*;");
      head.append("import static org.javascool.macros.Macros.*;");
      head.append("import static org.javascool.macros.Stdin.*;");
      head.append("import static org.javascool.macros.Stdout.*;");
      if(progletPackageName != null) {
        head.append("import static ").append(progletPackageName).append(".Functions.*;");
      }
      if(progletTranslator != null) {
        head.append(progletTranslator.getImports());
      }
      // Declares the proglet's core as a Runnable in the Applet
      uid++;
      head.append("public class JvsToJavaTranslated").append(uid).append(" implements Runnable{");
      //-tail.append("\nvoid error_at_end_of_javascool_main() { }\n");     
      head.append("  private static final long serialVersionUID = ").append(uid).append("L;");
      head.append("  public void run() {");
      head.append("   try{ main(); } catch(Throwable e) { ");
      head.append("    if (e.toString().matches(\".*Interrupted.*\"))println(\"\\n-------------------\\nProgramme arrêté !\\n-------------------\\n\");");
      head.append("    else println(\"\\n-------------------\\nErreur lors de l'exécution de la proglet\\n\"+org.javascool.core.Jvs2Java.report(e)+\"\\n-------------------\\n\");}");
      head.append("  }");
      // Adds a main wrapper to run the proglet jvs runnable
      if (proglet != null) {
	if (jvsName == null)
	  jvsName = proglet.getName()+" démo";
	String main = 
	  "public static void main(String[] usage) {" + 
	  "    new org.javascool.widgets.MainFrame().reset(\""+jvsName+"\", "+(proglet.getDimension().width+200)+", "+(proglet.getDimension().height+100)+", "+
	  (proglet.getPane() != null ? "org.javascool.core.ProgletEngine.getInstance().setProglet(\""+proglet.getName()+"\").getProgletPane()" : "org.javascool.widgets.Console.getInstance()") +
	  ").setRunnable(new JvsToJavaTranslated"+uid+"());" +
	  "}";
	head.append(main);
      }
    }
    if (body.toString().indexOf("while") != -1)
      System.out.println("Notice: à la boucle while une minuterie de 1ms est ajoutée pour éviter qu'une boucle infinie ne bloque tout.\n");
    String finalBody = body.toString().
                       replaceAll("(while.*\\{)", "$1 sleep(1);");
    if(progletTranslator != null) {
      finalBody = progletTranslator.translate(finalBody);
    }
    return head.toString() + finalBody + "\n\n"+ tail.toString() + "}";
  }
  /**
   * @see #translate(String, String)
   */
  @Override
  public String translate(String jvsCode) {
    return translate(jvsCode, null);
  }

  /** Renvoie le nom de la dernière classe Java générée lors de la traduction. */
  public String getClassName() {
    return "JvsToJavaTranslated" + uid;
  }

  // Counter used to increment the serialVersionUID in order to reload the different versions of the class
  private static int uid = 0;

  /** Rapporte une erreur survenue lors de l'exécution d'un prograamme Jvs.
   * @param error L'erreur ou exception à rapporter.
   * @return Le rapport d'erreur.
   */
  public static String report(Throwable error) {
    if(error instanceof InvocationTargetException) {
      return report(error.getCause());
    }
    String s = "";
    if(error instanceof ArrayIndexOutOfBoundsException) {
      s += "Erreur lors de l'utilisation d'un tableau, l'index utilisé (" + error.toString().split(": ")[1] + ") n'est pas valide.\n\n";
    } else if(error instanceof NullPointerException) {
      s += "Utilisation d'une variable non initialisée (pointeur de valeur nul).\n\n";
    } else {
      s += error.toString() + "\n\n";
    }
    s += "========\nTrace d'exécution du programme\n";
    for(int i = 0; i < 4 && i < error.getStackTrace().length; i++) {
      String s_i = "" + error.getStackTrace()[i];
      if (s_i.startsWith("JvsToJavaTranslated")) {
        s_i = s_i.replaceAll("JvsToJavaTranslated[0-9]*", "");
        s_i = s_i.replaceFirst("\\.", "");
        s_i = s_i.replaceFirst("\\.java:", ") l\\.");
        s_i = s_i.substring(0, s_i.length() - 1);
        String[] s_is = s_i.split(" ");
        s += s_is[1] + ": " + s_is[0] + "\n";
      }
    }
    return s;
  }

  /** Compile sous forme de fichier java compilable à l'extérieur de javascool un programme javascool d'une proglet donnée.
   * @param name Nom de la proglet.
   * @param jvsFile Le fichier source en .jvs.
   * @param javaFile Le nom du fichier java généré, par défaut construit à partir du nom jvs.
   *
   * @throws RuntimeException Si une erreur d'entrée-sortie s'est produite lors de la compilation ou construction.
   */
  public static void build(String name, String jvsFile, String javaFile) {
    System.out.println("Compilation de "+new File(javaFile).getName()+"..");
    Proglet proglet = new Proglet().load("org/javascool/proglets/" + name);
    Jvs2Java jvs2java = proglet.getJvs2java();
    String javaCode = jvs2java.translate(FileManager.load(jvsFile), new File(jvsFile).getName().replaceFirst("\\.jvs$", ""));
    FileManager.save(javaFile, (javaCode.trim()+"\n").replaceAll("\n", System.getProperty("line.separator")));
    System.out.println(" achevée avec succès :\nLe fichier '"+javaFile+"' est disponible");
  }
  /**
   * @see #build(String, String, String)
   */
  public static void build(String name, String jvsFile) {
    build(name, jvsFile, jvsFile.replaceFirst("\\.jvs$", ".java"));
  }
  /**
   * @see #build(String, String, String)
   */
  public static void build(String jvsFile) {
    build(Utils.javascoolProglet(), jvsFile, jvsFile.replaceFirst("\\.jvs$", ".java"));
  }
}
