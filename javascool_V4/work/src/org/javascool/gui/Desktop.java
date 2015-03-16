/*********************************************************************************
* Philippe.Vienne@sophia.inria.fr, Copyright (C) 2011.  All rights reserved.    *
* Guillaume.Matheron@sophia.inria.fr, Copyright (C) 2011.  All rights reserved. *
* Thierry.Vieville@sophia.inria.fr, Copyright (C) 2009.  All rights reserved.   *
*********************************************************************************/
package org.javascool.gui;

import java.io.File;
import java.net.URL;
import javax.swing.JFrame;
import org.javascool.core.ProgletEngine;
import org.javascool.macros.Macros;
import org.javascool.tools.FileManager;
import org.javascool.widgets.MainFrame;
import org.javascool.widgets.ToolBar;

// Used to define the frame

/** Définit les functions d'interaction avec l'interface graphique de JavaScool.
 *
 * @see <a href="Desktop.java.html">code source</a>
 * @serial exclude
 */
public class Desktop {
  // @static-instance

  /** Crée et/ou renvoie l'unique instance du desktop.
   * <p>Une application ne peut définir qu'un seul desktop.</p>
   */
  public static Desktop getInstance() {
    if(desktop == null) {
      desktop = new Desktop();
    }
    return desktop;
  }
  private static Desktop desktop = null;

  private Desktop() {}
  /** Renvoie la fenêtre racine de l'interface graphique. */
  public JFrame getFrame() {
    if(frame == null) {
      frame = (new MainFrame() {
                 @Override
                 public boolean isClosable() {
                   return org.javascool.gui.Desktop.getInstance().isClosable();
                 }
               }
               ).reset(About.title, About.logo, JVSPanel.getInstance());
    }
    return frame;
  }
  private MainFrame frame;

  /** Retourne la bare d'outils de Java's cool */
  public ToolBar getToolBar() {
    return (ToolBar) JVSToolBar.getInstance();
  }
  /** Demande la fermeture du desktop à la fin du programme.
   * @return La valeur true si le desktop peut être fermé sans dommage pour l'utilisateur, sinon la valeur fausse.
   */
  public boolean isClosable() {
    boolean close = JVSPanel.getInstance().close();
    if(close && (ProgletEngine.getInstance().getProglet() != null)) {
      ProgletEngine.getInstance().getProglet().stop();
    }
    return close;
  }
  /** Crée un nouveau fichier.
   * @return  La valeur true si le fichier est bien créé.
   */
  public boolean openNewFile() {
    try {
      JVSPanel.getInstance().newFile();
      return true;
    } catch(Throwable th) {
      System.out.println("Aie impossible de créer un nouveau fichier " + th);
      return false;
    }
  }
  /** Ouvre un fichier
   * Demande à l'utilisateur de choisir un fichier et l'ouvre
   * @param file Le fichier à ouvrir.
   * <p>-Avec la valeur null une boîte de dialogue le demandera à l'utilisateur.</p>
   * <p>- Si le fichier est une URL, une copie locale du fichier (avec sauvegarde du fichier existant) est effectuée avant ouverture.</p>
   */
  public boolean openFile(File file) {
    try {
      if(file == null) {
        JVSPanel.getInstance().openFile();
      } else {
        JVSFileTabs.getInstance().open(file.getAbsolutePath());
      }
      return true;
    } catch(Throwable th) {
      System.out.println("Aie impossible d'ouvrir le fichier " + th);
      return false;
    }
  }
  /**
   * @see #openFile(File)
   */
  public boolean openFile(URL url) {
    try {
      System.err.println(url.getProtocol());
      if(url.getProtocol().equals("jar")) {
        JVSFileTabs.getInstance().openFile(new JVSFile(FileManager.load(url.toExternalForm())));
        return true;
      }
      return openFile(new File(url.toURI()));
    } catch(Exception ex) {
      System.out.println("Aie impossible d'ouvrir le fichier " + ex);
      System.err.println("Error : ");
      ex.printStackTrace(System.err);
      return false;
    }
  }
  /**
   * @see #openFile(File)
   */
  public boolean openFile(String file) {
    return openFile(Macros.getResourceURL(file));
  }
  /**
   * @see #openFile(File)
   */
  public boolean openFile() {
    return openFile((File) null);
  }
  /** Demande à l'utilisateur de sauvegarder le fichier courant.
   * @return La valeur true si le fichier est bien sauvegardé.
   */
  public boolean saveCurrentFile() {
    return JVSPanel.getInstance().saveFile();
  }
  /** Ferme le fichier en cours d'édition. */
  public void closeFile() {
    JVSPanel.getInstance().closeFile();
  }
  /** Compile le fichier en cours d'édition. */
  public void compileFile() {
    JVSPanel.getInstance().compileFile();
  }
  /** Ferme la proglet en cours d'édition. */
  public void closeProglet() {
    JVSPanel.getInstance().closeProglet();
  }
  /** Ouvre une proglet.
   * @param proglet Le nom de code de la Proglet
   * @param closed Si false ferme les proglets précédentes.
   * @return True si tous les fichier ont été sauvegardé et la proglet sauvegardé
   */
  public boolean openProglet(String proglet, boolean closed) {
    if(closed || JVSPanel.getInstance().closeAllFiles()) {
      JVSPanel.getInstance().loadProglet(proglet);
      return true;
    } else {
      return false;
    }
  }
  /** Ouvre une proglet.
   * @see #openProglet(String, boolean)
   */
  public boolean openProglet(String proglet) {
    return openProglet(proglet, false);
  }
  /** Ouvre un nouvel onglet de navigation
   * Ouvre un onglet HTML3 dans le JVSWidgetPanel, cet onglet peut être fermé
   * @param url L'adresse à ouvrir sous forme de chaîne de caractères ou d'URL.
   * @param name Le titre du nouvel onglet
   */
  public void openBrowserTab(URL url, String name) {
    openBrowserTab(url.toString(), name);
  }
  /**
   * @see #openBrowserTab(URL, String)
   */
  public void openBrowserTab(String url, String name) {
    JVSWidgetPanel.getInstance().openWebTab(url, name);
  }
  /** Affiche la console. */
  public void focusOnConsolePanel() {
    JVSWidgetPanel.getInstance().focusOnConsolePanel();
  }
  /** Affiche la console. */
  public void focusOnProgletPanel() {
    JVSWidgetPanel.getInstance().focusOnProgletPanel();
  }
}
