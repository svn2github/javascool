/*********************************************************************************
* Philippe.Vienne@sophia.inria.fr, Copyright (C) 2011.  All rights reserved.    *
* Guillaume.Matheron@sophia.inria.fr, Copyright (C) 2011.  All rights reserved. *
* Thierry.Vieville@sophia.inria.fr, Copyright (C) 2009.  All rights reserved.   *
*********************************************************************************/
package org.javascool.core;

import java.util.ArrayList;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import org.javascool.tools.FileManager;
import org.javascool.tools.UserConfig;

/** Définit les mécanismes de compilation, exécution, gestion de proglet.
 *
 * @see <a href="ProgletEngine.java.html">code source</a>
 * @serial exclude
 */
public class ProgletEngine {
  /** Tables des proglets. */
  private ArrayList<Proglet> proglets;

  // @static-instance

  /** Crée et/ou renvoie l'unique instance de l'engine.
   * <p>Une application ne peut définir qu'un seul engine.</p>
   */
  public static ProgletEngine getInstance() {
    if(engine == null) {
      engine = new ProgletEngine();
    }
    return engine;
  }
  private static ProgletEngine engine = null;

  private ProgletEngine() {
    String javascoolJar = Utils.javascoolJar();
    // Détection des proglets présentes dans le jar
    try {
      proglets = new ArrayList<Proglet>();
      for(String dir : FileManager.list(javascoolJar, "org.javascool.proglets.[^\\.]+.proglet.(pml|json)")) {
        String name = dir.replaceFirst("jar:[^!]*!(.*)proglet.(pml|json)", "$1");
        try {
          Proglet proglet = new Proglet().load(name);
          proglets.add(proglet);
        } catch(Exception e) {
          System.err.println("Erreur lors de la détection dans le jar de la proglet " + name + " en " + dir + " (" + e + ")");
        }
      }
    } catch(Exception er) {
      System.err.println("Erreur lors de la détection des proglets (" + er + " avec " + javascoolJar + "\n . . vous pouvez quand même utiliser JavaScool");
    }
    // Définit une proglet "vide" pour lancer l'interface
    if(proglets.isEmpty()) {
      String message = "Erreur dans javascool: cette jarre ne contient pas de proglets";
      message += "\njavascoolJar = "+javascoolJar+"\n";
      // Ajoute toutes les données de l'environnement
      for(String name : System.getProperties().stringPropertyNames())
	message += "  " + name + " = " + System.getProperty(name) + "\n";
      throw new IllegalStateException(message);
      /*
      for(int i = 0; i < 1; i++) {
        Proglet p = new Proglet();
        p.pml.set("name", "Interface");
        p.pml.set("icon-location", "org/javascool/widgets/icons/scripts.png");
        p.pml.set("help-location", "org/javascool/macros/memo-macros.htm");
        proglets.add(p);
      }
      */
    }
    // Tri des proglets par ordre alphabétique
    Collections.sort(proglets, new Comparator<Proglet>() {
                       @Override
                       public int compare(Proglet p1, Proglet p2) {
                         return p1.getName().compareTo(p2.getName());
                       }
                     }
                     );
  }
  //
  // [1] Mécanisme de compilation/exécution
  //

  /** Mécanisme de compilation du fichier Jvs.
   * @param program Nom du programme à compiler.
   * @return La valeur true si la compilation est ok, false sinon.
   */
  public boolean doCompile(String program) {
    doStop();
    // Traduction Jvs -> Java puis Java -> Class et chargement de la classe si succès
    Jvs2Java jvs2java = getProglet() != null ? getProglet().getJvs2java() : new Jvs2Java();
    String javaCode = jvs2java.translate(program);
    // Creation d'un répertoire temporaire
    String javaFile;
    try {
      File buildDir = UserConfig.createTempDir("javac");
      javaFile = buildDir + File.separator + jvs2java.getClassName() + ".java";
      FileManager.save(javaFile, javaCode);
      // Si il y a un problème avec le répertoire temporaire on se rabat sur le répertoire local
    } catch(Exception e1) {
      try {
        javaFile = new File(jvs2java.getClassName() + ".java").getAbsolutePath();
        System.err.println("Sauvegarde locale du fichier : " + javaFile);
        FileManager.save(javaFile, javaCode);
        // Sinon on signale le problème à l'utilisateur
      } catch(Exception e2) {
        System.out.println("Attention ! le répertoire '" + System.getProperty("user.dir") + "' ne peut être utilisé pour sauver des fichiers, \n il faut re-lancer javascool dans un répertoire de travail approprié.");
        return false;
      }
    }
    if(Java2Class.compile(javaFile)) {
      try {
        runnable = Java2Class.load(javaFile);
        return true;
      } catch(Exception e3) {
        System.out.println("Attention ! il y a eu une action externe de nettoyage de fichiers temporaires ou le répertoire '" + new File(javaFile).getParent() + "' ne peut être utilisé pour sauver des fichiers, \n il faut re-lancer javascool dans un répertoire de travail approprié.");
        return false;
      }
    } else {
      runnable = null;
      return false;
    }
  }
  /** Mécanisme de lancement du programme compilé. */
  public void doRun() {
    doStop();
    // Lancement du runnable dans un thread
    if(runnable != null) {
      (thread = new Thread(new Runnable() {
                             @Override
                             public void run() {
                               try {
                                 runnable.run();
                                 thread = null;
                               } catch(Throwable e) {
                                 Jvs2Java.report(e);
                               }
                             }
                           }
                           )).start();
    }
  }
  /** Mécanisme d'arrêt du programme compilé.
   * @param message Message d'erreur affiché à la console. Si null (par défaut) pas de message.
   */
  public void doStop(String message) {
    if(message != null) {
      System.out.println("Cause de l'interruption : " + message);
    }
    if(thread != null) {
      thread.interrupt();
      thread = null;
    }
  }
  /**
   * @see #doStop(String)
   */
  public void doStop() {
    doStop(null);
  }
  /** Renvoie true si le programme est en cours. */
  public boolean isRunning() {
    return thread != null;
  }
  private Thread thread = null;
  private Runnable runnable = null;

  /** Renvoie le runnable correspondant au programme utilisateur en cours.
   * @return Le runnable correspondant au programme démarré par doRun() ou null si il n'y en a pas.
   */
  public Runnable getProgletRunnable() {
    return runnable;
  }
  //
  // Mécanisme de chargement d'une proglet
  //

  /** Mécanisme de chargement d'une proglet.
   * @param proglet Le nom de la proglet.
   * @return La proglet en fonctionnement ou null si la proglet n'existe pas.
   * @throws IllegalArgumentException Si il y a tentative d'utilisation d'une proglet indéfinie
   */
  public Proglet setProglet(String proglet) {
    if(currentProglet != null) {
      currentProglet.stop();
    }
    currentProglet = null;
    for(Proglet p : getProglets())
      if(p.getName().equals(proglet)) {
        currentProglet = p;
      }
    if(currentProglet == null) { 
      throw new IllegalArgumentException("Tentative d'utilisation d'une proglet indéfinie : " + proglet);
    }
    currentProglet.start();
    return currentProglet;
  }
  /** Renvoie la proglet demandé.
   * @return la proglet ou null sinon.
   */
  public Proglet getProglet(String proglet) {
    for(Proglet p : getProglets())
      if(p.getName().equals(proglet)) {
        return p;
      }
    return null;
  }
  /** Renvoie la proglet courante.
   * * @return la proglet courante ou null sinon.
   */
  public Proglet getProglet() {
    return currentProglet;
  }
  private Proglet currentProglet = null;

  /** Renvoie toutes les proglets actuellement disponibles.
   * @return Un objet utilisable à travers la construction <tt>for(Proglet proglet: getProglets()) { .. / .. }</tt>.
   */
  public Iterable<Proglet> getProglets() {
    return proglets;
  }
  /** Renvoie le nombre de progets. */
  public int getProgletCount() {
    return proglets.size();
  }
}
