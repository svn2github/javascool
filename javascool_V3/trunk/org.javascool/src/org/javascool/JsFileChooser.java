/********************************************************************************
*      ______________________________________________
| By Philippe Vienne <philoumailabo@gmail.com> |
| Distrubuted on GNU General Public Licence    |
| Revision 904 du SVN                          |
| © 2010 INRIA, All rights reserved            |
|||______________________________________________|
|
********************************************************************************/

package org.javascool;

// Used to link to the frame
import javax.swing.JApplet;

// Used for the file interface
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import java.io.File;

// Used for the dialogs
import javax.swing.JOptionPane;

/** Defines the JavaScool v3-2 file load/save interactions.
 * @author Philippe Vienne <philoumailabo@gmail.com>
 * @see <a href="JsFileChooser.java.html">source code</a>
 * @serial exclude
 */
public class JsFileChooser extends JFileChooser {
  private static final long serialVersionUID = 1L;

  /** Constructs a file chooser.
   * @param parent The dialog gparent.
   */
  public JsFileChooser(JApplet parent) {
    this.parent = parent;
  }
  private JApplet parent;
  // Initializes the file chooser
  {
    // Use the user.dir to start the interaction
    setCurrentDirectory(new File(System.getProperty("user.dir")));
    // Defines a Jvs/Pml extension filter
    setFileFilter(new FileFilter() {
                    public String getDescription() {
                      return extension == null ? "Tous les Fichiers" : "Fichiers " + extension.substring(1).toUpperCase();
                    }
                    public boolean accept(File file) {
                      return file.isDirectory() ? true : extension == null ? true : file.getName().endsWith(extension);
                    }
                  }
                  );
  }
  private String extension = null;
  /** Gets the last selected file. */
  public String getFile() {
    return file;
  }
  private String file;
  /** Resets the selected file. */
  public void resetFile() {
    file = null;
  }
  /** Manages an open dialog action after saving the file.
   * @param editor The editor where to load the file.
   * @param extension The required file extension, if any (else null).
   */
  public void doSaveOpenAs(Editor editor, String extension) {
    switch(!editor.isModified() ? 1 : new JOptionPane().
           showConfirmDialog(parent,
                             "Voulez-vous enregistrer avant d'ouvrir un nouveau fichier ?",
                             "Sauvgarder avant d'ouvrir",
                             JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE))
    {
    case 0: // Yes save
      if(doSaveAs(editor, extension))
        doOpenAs(editor, extension);
      break;
    case 1: // No need to save
      doOpenAs(editor, extension);
      break;
    }
  }
  /** Saves a file, before exiting or activity change. */
  public boolean doSaveCloseAs(Editor editor, String extension) {
    switch((editor == null) || !editor.isModified() ? 1 : new JOptionPane().
           showConfirmDialog(parent,
                             "Voulez-vous enregistrer avant de fermer ?",
                             "Sauvgarder avant de fermer",
                             JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE))
    {
    case 0: // Yes save and return true if and only if the save is validated
      setSaveDialogTitle("Enregistrer votre fichier avant de passer à la suite");
      return doSaveAs(editor, extension);
    case 1: // No need to save
      return true;
    default: // Ah ! Cancel
      return false;
    }
  }
  /** Manages an open dialog action.
   * @param editor The editor where to load the file.
   * @param extension The required file extension, if any (else null).
   */
  private void doOpenAs(Editor editor, String extension) {
    if(this.extension != extension)
      setSelectedFile(null);
    this.extension = extension;
    setDialogTitle("Ouvrir un programme");
    setDialogType(JFileChooser.OPEN_DIALOG);
    setApproveButtonText("Ouvrir");
    if(showOpenDialog(parent) == 0)
      doOpen(editor, getSelectedFile().getPath());
  }
  /** Manages an open action (no dialog). */
  public void doOpen(Editor editor, String file) {
    setSelectedFile(new File(file));
    String text = "";
    try { this.file = file;
          text = Utils.loadString(file);
    } catch(Exception e) {}
    editor.setText(text);
  }
  /** Manages a save dialog action.
   * @param editor The editor from where the file is saved.
   * @param extension The required file extension, if any (else null).
   * @return True if the dialog is validated, else false.
   */
  public boolean doSaveAs(Editor editor, String extension) {
    if(this.extension != extension)
      setSelectedFile(null);
    this.extension = extension;
    if(title == null)
      title = "Enregister un programme";
    setDialogTitle(title);
    title = null;
    setDialogType(JFileChooser.SAVE_DIALOG);
    setApproveButtonText("Enregister");
    if(showSaveDialog(parent) == 0) {
      file = getSelectedFile().getPath();
      doSave(editor, extension);
      return true;
    } else
      return false;
  }
  /** Manages a save action (no dialog).
   * @param editor The editor from where the file is saved.
   * @param extension The required file extension, if any (else null).
   */
  public void doSave(Editor editor, String extension) {
    if(file == null)
      doSaveAs(editor, extension);
    else
      doSave(editor, editor.getText(), extension);
  }
  /** Manages a save action (no dialog).
   * @param editor The editor where the text comes from.
   * @param text The text to save.
   * @param extension The required file extension, if any (else null).
   */
  public void doSave(Editor editor, String text, String extension) {
    // Normalizes the file name and extension
    file = toJavaName(file, extension);
    setSelectedFile(new File(file));
    // Cleans spurious chars
    text = text.replaceAll("[\u00a0]", "");
    // Adds a new line if not yet done
    text = "\n" + text.trim();
    // Reload in editor the clean text
    if(editor != null)
      editor.setText(text);
    Utils.saveString(file, text);
  }
  /** Sets the next save dialog title.
   * @param title Optional title for a specific dialog
   */
  private void setSaveDialogTitle(String title) {
    this.title = title;
  }
  private String title = null;

  /** Returns a correct Java name with a specific extension from the given name.
   * @param path The original file path to checck/correct.
   * @param extension The extension to set
   * @return The checked/corrected path. A warning message is printed if the name has to be coreected.
   */
  private static String toJavaName(String file, String extension) {
    File f = new File(file);
    String parent = f.getParent(), extensionPattern = "(.*)(\\.[^\\.]*)$", name = f.getName().replaceAll(extensionPattern, "$1"), main;
    if(Jvs2Java.isReserved(name)) {
      main = "my_" + name;
      System.out.println("Attention: le nom \"" + name + "\" est interdit par Java, renommons le \"" + main + "\"");
    } else if(!name.matches("[A-Za-z_][A-Za-z0-9_]*")) {
      main = name.replaceAll("[^A-Za-z0-9_]", "_");
      System.out.println("Attention: le nom \"" + name + "\" contient quelque caractère interdit par Java, renommons le \"" + main + "\"");
    } else
      main = name;
    if(extension == null)
      extension = f.getName().matches(extensionPattern) ? f.getName().replaceFirst(extensionPattern, "$2") : "";
    return parent + File.separator + main + extension;
  }
}
