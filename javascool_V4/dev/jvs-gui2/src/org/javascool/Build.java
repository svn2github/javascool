package org.javascool;

import java.io.File;
import java.io.IOException;

import org.javascool.builder.ProgletsBuilder;
import org.javascool.tools.ErrorCatcher;
import org.javascool.tools.Pml;

/**
 * Lanceur de l'application "formateur" qui permet de construire des «proglets».
 *
 * @see <a href="Build.java.html">code source</a>
 * @serial exclude
 */
public class Build {
  /** Logo de l'application. */
  public static final String logo = "org/javascool/widgets/icons/logo-builder.png";

  /**
   * Lanceur de la conversion Jvs en Java.
   *
   * @param usage
   *            <tt>java org.javascool.Build  [-q [targetDir]]</tt>
   */
  public static void main(String[] usage) {
    Pml args = new Pml().reset(usage);
    ErrorCatcher.checkJavaVersion(6);
    Core.setUncaughtExceptionAlert();
    ProgletsBuilder.setVerbose(args.getBoolean("v")
                               || args.getBoolean("verbose"));
    if(args.getBoolean("h") || args.getBoolean("help")) {
      System.out
      .println("Java's Cool Builder - Construit un jar avec les proglets souhaitées");
      System.out
      .println("Usage : java -jar javascool-builder.jar [-q] [-w] [-v] [-target target-dir] [-proglets proglet-list]");
      System.out.println("Options : ");
      System.out
      .println("\t-q\tPermet de lancer l'application en console sur toutes les proglets disponibles et sans interface graphique.");
      System.out
      .println("\t-w\tPermet de lancer l'application en console et génère les fichiers javadoc et jars des proglets.");
      System.out
      .println("\t-v\tPermet de lancer l'application en mode verbose, toute les étapes sont affiché.");
      System.out
      .println("\t-target target-dir\tLe répertoire cible dans lequel la construction se fait (c'est le répertoire temporaire .build par défaut).");
      System.out
      .println("\t-proglets proglet-dir\tLes proglets à prendre en compte (elle le sont toutes part défaut).");
    } else if(!(args.getBoolean("q") || args.getBoolean("w"))) {
      org.javascool.builder.DialogFrame.startFrame();
    } else {
      try {
        if(args.isDefined("target")
           && new File(".").getCanonicalPath().equals(
             new File(args.getString("target"))
             .getCanonicalPath()))
        { throw new IllegalArgumentException(
                  "Le répertoire du sketchbook et celui du build ne peuvent pas être identiques !!!");
        }
      } catch(IOException e) { throw new IllegalArgumentException(
                                       "Le répertoire du build n'est pas utilisable.");
      }
      ProgletsBuilder.setVerbose(args.getBoolean("v")
                                 || args.getBoolean("verbose"));
      String target = args.isDefined("target") ? args.getString("target")
                      : null;
      String names[] = args.isDefined("proglets") ? args
                       .getString("proglets").trim().split("[, \t]+") : null;
      System.exit(ProgletsBuilder.build(
                    ProgletsBuilder.getProglets(names), target,
                    args.getBoolean("w")) ? 0 : -1);
    }
  }
}
