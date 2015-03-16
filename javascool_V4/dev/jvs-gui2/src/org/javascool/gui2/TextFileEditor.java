package org.javascool.gui2;

import java.io.File;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import org.javascool.tools.FileManager;
import org.javascool.tools.UserConfig;
import org.javascool.widgets.TextEditor;

/**
 * Définit un panneau éditeur d'un fichier texte qui intègre les fonctions de
 * colorisation et de complétion automatique.
 *
 * @author Philippe Vienne
 */
class TextFileEditor extends TextEditor {
  private static final long serialVersionUID = 1L;

  // Empeche de pouvoir renommer itempestivement des folder
  static {
    UIManager.put("FileChooser.readOnly", Boolean.TRUE);
  }

  /**
   * Renvoie la localisation du fichier.
   *
   * @return La localisation du fichier ou null si indéfini.
   */
  public String getFileLocation() {
    return location;
  }
  /** Construit un éditeur de Fichier
   * Surcharge la classe
   */
  public TextFileEditor() {
    super();
    setName("Nouveau Fichier");
  }
  private String location = null;

  // Date de dernière modification du fichier
  private long lastModified = 0;

  // @todo: Ce n'est pas la meilleur solution
// /**
// * Renvoie le nom du fichier définit par la localisation.
// *
// * @return Le nom du fichier (sans extension si celle si est forcée), ou
// *         null si indéfini.
// */
// @Override
// public String getName() {
// String name = location == null ? "Fichier sans nom" : location.replaceAll(".*/", "");
// return name == null ? name : name.replaceFirst("\\.[^.]*$", ""); // @todo: Pourquoi le cacher à l'utilisateur ?
// }

  /**
   * Définit l'extension du fichier si celle si est forcée.
   * <p>
   * Cette extension définie la syntaxe de l'éditeur.
   * </p>
   *
   * @param extension
   *            Extension du fichier par exemple "java" ou "jvs", ou "txt"
   *            (valeur par défaut) pour pas fixer d'extension.
   * @return Cet objet, permettant de définir la construction
   *         <tt>new TextFileEditor().setExtension(..)</tt>.
   */
  public TextFileEditor setExtension(String extension) {
    setSyntax(this.extension = extension);
    return this;
  }
  private String extension = "txt";

  /**
   * Charge le texte à partir d'un fichier local.
   * <p>
   * Lance un dialogue avec l'utilisateur pour choisir le fichier.
   * </p>
   *
   * @return La valeur true si le dialogue a abouti, faux si il a échoué
   */
  public boolean load() {
    try {
      File f = FileManager.openFile(TextFileEditor.getWorkingDir());
      TextFileEditor.setWorkingDir(f.getParentFile().getAbsolutePath());
      try {
        load(f.getAbsolutePath());
        setName(f.getName());
        return true;
      } catch(Exception e) {
        JOptionPane
        .showMessageDialog(
          null,
          "Le fichier ne peut pas être lu, il est inaccessible en tant que fichier texte.",
          "Erreur de lecture", JOptionPane.ERROR_MESSAGE);
        return false;
      }
    } catch(IllegalStateException e) { // L'utilisateur a annulé la demande
      return false;
    } /*
       *  JFileChooser fc = new JFileChooser();
       *  String dir = TextFileEditor.getWorkingDir();
       *  if (dir != null) {
       *  fc.setCurrentDirectory(new File(dir));
       *  }
       *  int returnVal = fc.showOpenDialog(null);
       *  if (returnVal == JFileChooser.APPROVE_OPTION) {
       *  TextFileEditor.setWorkingDir(fc.getSelectedFile().getParentFile()
       *   .getAbsolutePath());
       *  try {
       *  load(fc.getSelectedFile().getAbsolutePath());
       *  return true;
       *  } catch (Exception e) {
       *  JOptionPane
       *     .showMessageDialog(
       *         null,
       *         "Le fichier ne peut pas être lu, il est inaccessible en tant que fichier texte.",
       *         "Erreur de lecture", JOptionPane.ERROR_MESSAGE);
       *  return false;
       *  }
       *  } else
       *  return false;*/
  }
  // Récupère le répertoire par défaut de javascool
  private static String getWorkingDir() {
    if(UserConfig.getInstance("javascool").getProperty("dir") != null) {
      return UserConfig.getInstance("javascool").getProperty("dir");
    } else if(System.getProperty("os.name").toLowerCase().contains("nix")
              || System.getProperty("os.name").toLowerCase().contains("nux"))
    {
      return System.getProperty("user.dir");
    } else if(System.getProperty("home.dir") != null) {
      return System.getProperty("home.dir");
    } else {
      return null;
    }
  }
  // Met à jour le répertoire par défaut de javascool
  private static void setWorkingDir(String dir) {
    UserConfig.getInstance("javascool").setProperty("dir", dir);
  }
  /**
   * Charge le texte à partir d'une localisation.
   *
   * @param location
   *            Une URL (Universal Resource Location) de la forme: <div
   *            id="load-format">
   *            <table align="center">
   *            <tr>
   *            <td><tt>http:/<i>path-name</i></tt></td>
   *            <td>pour aller chercher le contenu sur un site web</td>
   *            </tr>
   *            <tr>
   *            <td><tt>http:/<i>path-name</i>?param_i=value_i&amp;..</tt></td>
   *            <td>pour le récupérer sous forme de requête HTTP</td>
   *            </tr>
   *            <tr>
   *            <td><tt>file:/<i>path-name</i></tt></td>
   *            <td>pour le charger du système de fichier local ou en tant que
   *            ressource Java dans le CLASSPATH</td>
   *            </tr>
   *            <tr>
   *            <td><tt>jar:/<i>jar-path-name</i>!/<i>jar-entry</i></tt></td>
   *            <td>pour le charger d'une archive <div>(exemple:
   *            <tt>jar:http://javascool.gforge.inria.fr/javascool.jar!/META-INF/MANIFEST.MF</tt>
   *            )</div></td>
   *            </tr>
   *            </table>
   *            </div>
   * @param utf8
   *            Si la valeur est vraie, force l'encodage en UTF-8 à la
   *            lecture. Par défaut (false) utilise l'encodage local.
   *
   * @throws IllegalArgumentException
   *             Si l'URL est mal formée.
   * @throws RuntimeException
   *             Si une erreur d'entrée-sortie s'est produite.
   */
  public void load(String location, boolean utf8) {
    setText(FileManager.load(this.location = location, utf8));
    lastModified = FileManager.getLastModified(location);
  }
  /**
   * @see #load(String, boolean)
   */
  public void load(String location) {
    load(location, false);
  }
  /**
   * Sauve le texte dans un fichier local.
   * <p>
   * Lance un dialogue avec l'utilisateur pour choisir le fichier.
   * </p>
   *
   * @return La valeur true si le dialogue a abouti, faux si il a échoué
   */
  public boolean saveAs() {
    try {
      File f = FileManager.saveFile(false, TextFileEditor.getWorkingDir());
      if(!f.getName().endsWith('.' + extension)) {
        f = (new File(f.getAbsolutePath() + '.' + extension));
      }
      TextFileEditor.setWorkingDir(f.getParentFile().getAbsolutePath());
      save(f.getAbsolutePath());
      setName(f.getName());
      return true;
    } catch(IllegalStateException e) { // L'utilisateur a annulé la demande
      return false;
    }
    /*JFileChooser fc = new JFileChooser();
     *  String dir = TextFileEditor.getWorkingDir();
     *  if (dir != null) {
     *  fc.setCurrentDirectory(new File(dir));
     *  }
     *  fc.setApproveButtonText("Enregistrer");
     *  fc.setDialogTitle("Enregistrer");
     *  int returnVal = fc.showOpenDialog(this.getParent());
     *  if (returnVal == JFileChooser.APPROVE_OPTION) {
     *  TextFileEditor.setWorkingDir(fc.getSelectedFile().getParentFile()
     *     .getAbsolutePath());
     *  save(fc.getSelectedFile().getAbsolutePath());
     *  return true;
     *  } else
     *  return false;*/
  }
  /**
   * Sauve le texte à la dernière location choisie, si le texte a été modifié.
   *
   * @param confirm
   *            Si true demande à l'utilisateur confirmation avant de sauver.
   * @return La valeur true si le dialogue a abouti, faux si il a échoué
   */
  public boolean save(boolean confirm) {
    if(isTextModified()) {
      if(confirm) {
        int r = JOptionPane.showConfirmDialog(null,
                                              "Voulez vous enregistrer " + getName()
                                              + " avant de continuer ?");
        if(r == JOptionPane.YES_OPTION) {
          return save(false);
        } else {
          return r == JOptionPane.NO_OPTION;
        }
      } else {
        try {
          if(location == null) {
            return saveAs();
          } else {
            long l = FileManager.getLastModified(location);
            if((lastModified != 0) && (l != 0) && (lastModified < l)) {
              int r = JOptionPane
                      .showConfirmDialog(
                null,
                "Le fichier "
                + getName()
                + " a été modifié par un autre logiciel : voulez-vous écraser ces modifications ?");
              if(r == JOptionPane.NO_OPTION) {
                return saveAs();
              } else if(r != JOptionPane.YES_OPTION) {
                return false;
              }
            }
            save(location);
            return true;
          }
        } catch(Exception e) {
          JOptionPane
          .showMessageDialog(
            null,
            "Le fichier ne peut pas être écrit ici, choisisez un nouvel endroit.",
            "Erreur d'écriture",
            JOptionPane.ERROR_MESSAGE);
          return false;
        }
      }
    } else {
      return true;
    }
  }
  /**
   * Sauve le texte dans une localisation.
   *
   * @param location
   *            Une URL (Universal Resource Location) de la forme: <div
   *            id="save-format">
   *            <table>
   *            <tr>
   *            <td><tt>ftp:/<i>path-name</i></tt></td>
   *            <td>pour sauver sur un site FTP.</td>
   *            </tr>
   *            <tr>
   *            <td><tt>file:/<i>path-name</i></tt></td>
   *            <td>pour sauver dans le système de fichier local (le
   *            <tt>file:</tt> est optionnel).</td>
   *            </tr>
   *            <tr>
   *            <td><tt>mailto:<i>address</i>?subject=<i>subject</i></tt></td>
   *            <td>pour envoyer un courriel avec le texte en contenu.</td>
   *            </tr>
   *            <tr>
   *            <td><tt>stdout:/</tt></td>
   *            <td>pour l'imprimer dans la console.</td>
   *            </tr>
   *            </table>
   *            </div>
   * @param backup
   *            Si true, dans le cas d'un fichier, crée une sauvegarde d'un
   *            fichier existant. Par défaut false. *
   *            <p>
   *            Le fichier sauvegardé est doté d'un suffixe numérique unique.
   *            </p>
   * @param utf8
   *            Si la valeur est vraie, force l'encodage en UTF-8 à la
   *            lecture. Par défaut (false) utilise l'encodage local.
   *
   * @throws IllegalArgumentException
   *             Si l'URL est mal formée.
   * @throws RuntimeException
   *             Si une erreur d'entrée-sortie s'est produite.
   */
  public void save(String location, boolean backup, boolean utf8) {
    if(extension != null) {
      location = location.replaceFirst("\\.[^.]*$", "") + "." + extension;
    }
    FileManager.save(this.location = location, getText(), backup, utf8);
    lastModified = FileManager.getLastModified(location);
  }
  /*
   * @see #save(String, boolean, boolean)
   */
  public void save(String location, String ext) {
    save(location, false, false);
  }
  /**
   * @see #save(String, boolean, boolean)
   */
  public void save(String location) {
    save(location, false, false);
  }
  /** Dit si le fichier est temporaire (uniquement en mémoire).
   *
   */
  public boolean isTmp() {
    return location == null;
  }
  @Override
  public String toString() {
    return "<TextFileEditor location=\"" + location + "\" name=\""
           + getName() + "\"/>";
  }
}
