/********************************************************************************
      ______________________________________________
     | By Philippe Vienne <philoumailabo@gmail.com> |
     | Distrubuted on GNU General Public Licence    |
     | Revision 904 du SVN                          |
     | © 2010 INRIA, All rights reserved            |
     |______________________________________________|

 ********************************************************************************/

package org.javascool;

// Used to link to Main.java
import javax.swing.JApplet;

// Used for the file interface
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.lang.Object;

/** Defines the JavaScool dedicated file chooser.
* @author Philippe Vienne <philoumailabo@gmail.com>
 */
class JsFileChooser extends JFileChooser {
    private static final long serialVersionUID = 1L;
    private JApplet applet=null;
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
	});
    }
    private String extension = null;
    /** Gets the last selected file. */ 
    public String getFile() { return file; } private String file;
    /** Resets the selected file. */
    public void resetFile() { file = null; }
    public void setApplet(JApplet appletbis){
        applet=appletbis;
    }
    /** Manages an open dialog action. 
     * @param editor The editor where to load the file.
     * @param extension The required file extension, if any (else null).
     */
    public void doOpenAs(Editor editor, String extension) {
      if (this.extension != extension) setSelectedFile(null);
      this.extension = extension;
      setDialogTitle("Ouvrir un programme");
      setDialogType(JFileChooser.OPEN_DIALOG);
      setApproveButtonText("Ouvrir");
      if (showOpenDialog(applet) == 0)
	doOpen(editor, getSelectedFile().getPath());
    }
    /** Manages an open action (no dialog). */
    public void doOpen(Editor editor, String file) {
      setSelectedFile(new File(file));
      String text = ""; try { this.file = file; text = Utils.loadString(file); } catch(Exception e) { }
      editor.setText(text);
    }
    /** Manages a save dialog action. 
     * @param editor The editor from where the file is saved.
     * @param extension The required file extension, if any (else null).
     * @return True if the dialog is validated, else false.
     */
    public boolean doSaveAs(Editor editor, String extension) {
      if (this.extension != extension) setSelectedFile(null);
      this.extension = extension;
      if (title == null) title = "Enregister un programme";
      setDialogTitle(title);
      title = null;
      setDialogType(JFileChooser.SAVE_DIALOG);
      setApproveButtonText("Enregister");
      if (showSaveDialog(applet) == 0) {
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
      if (file == null) {
	doSaveAs(editor, extension);
      } else { 
	doSave(editor, editor.getText(), extension);
      }
    }
    /** Manages a save action (no dialog). 
     * @param editor The editor where the text comes from.
     * @param text The text to save.
     * @param extension The required file extension, if any (else null).
     */
    public void doSave(Editor editor, String text, String extension) {
      // Normalizes the file name and extension
      file = toJavaName(file, extension); setSelectedFile(new File(file));
      // Cleans spurious chars
      text = text.replaceAll("[\u00a0]", "");
      // Adds a new line if not yet done
      text = "\n"+text.trim();
      // Reload in editor the clean text
      if (editor != null) editor.setText(text);
      Utils.saveString(file, text);
    }
    /** Sets the next save dialog title. 
     * @param title Optional title for a specific dialog
     */
    public void setSaveDialogTitle(String title) { this.title = title; } private String title = null;
    private static String toJavaName(String file, String extension) {
    File f = new File(file);
    String parent = f.getParent(), extensionPattern = "(.*)(\\.[^\\.]*)$", name = f.getName().replaceAll(extensionPattern, "$1"), main;
    if (Jvs2Java.isReserved(name)) {
      main = "my_" + name;
      System.out.println("Attention: le nom \""+name+"\" est interdit par Java, renommons le \""+main+"\"");
    } else if (!name.matches("[A-Za-z_][A-Za-z0-9_]*")) {
      main = name.replaceAll("[^A-Za-z0-9_]", "_");
      System.out.println("Attention: le nom \""+name+"\" contient quelque caractère interdit par Java, renommons le \""+main+"\"");
    } else
      main = name;
    if (extension == null) { extension = f.getName().matches(extensionPattern) ? f.getName().replaceFirst(extensionPattern, "$2") : ""; }
    return parent + File.separator + main + extension;
  }
  }
