package org.javascool.gui2;

import org.javascool.widgets.TabbedPane;
import org.javascool.widgets.ToolBar;
import javax.swing.JOptionPane;

import org.javascool.widgets.MainFrame;
import java.awt.BorderLayout;

/** Définit un panneau éditeur de plusieurs fichiers texte qui intègre les fonctions de colorisation et de complétion automatique.
 * @author Philippe Vienne
 */
class TextFilesEditor extends TabbedPane {
  private static final long serialVersionUID = 1L;

  /** Ouvre un éditeur dans une fenêtre autonome.
   * @param title Le titre de la fenêtre.
   * @param width Largeur de la fenêtre. Si 0 on prend tout l'écran.
   * @param height Hauteur de la fenêtre. Si 0 on prend tout l'écran.
   * @param extension Extension des fichiers par exemple "java" ou "jvs", ou null (valeur par défaut) pour pas fixer d'extension
   * @return La fenêtre principale qui a été ouverte.
   */
  public static MainFrame newTextFilesEditor(String title, int width, int height, String extension) {
    return new MainEditorFrame().reset(title, width, height, new TextFilesEditor().setExtension(extension));
  }
  private static class MainEditorFrame extends MainFrame {
    public MainFrame reset(String title, int width, int height, TextFilesEditor editor) {
      e = editor;
      ToolBar b = new ToolBar();
      e.addTools(b);
      add(b, BorderLayout.NORTH);
      super.reset(title, width, height, e);
      return this;
    }
    @Override
    public boolean isClosable() {
      return e == null ? true : e.isCloseable();
    }
    private TextFilesEditor e;
  }

  /** Ajoute à une barre de menu les boutons de contrôle de ce panneau.
   * @param toolbar La barre de menu à utiliser.
   */
  public void addTools(ToolBar toolbar) {
    toolbar.addTool("Nouveau fichier", "org/javascool/widgets/icons/new.png", new Runnable() {
                      @Override
                      public void run() {
                        openFile(false);
                      }
                    }
                    );
    toolbar.addTool("Ouvrir un fichier", "org/javascool/widgets/icons/open.png", new Runnable() {
                      @Override
                      public void run() {
                        openFile(true);
                      }
                    }
                    );
    toolbar.addTool("Sauver", "org/javascool/widgets/icons/save.png", new Runnable() {
                      @Override
                      public void run() {
                        saveFile(false);
                      }
                    }
                    );
    toolbar.addTool("Sauver sous", "org/javascool/widgets/icons/saveas.png", new Runnable() {
                      @Override
                      public void run() {
                        saveFile(true);
                      }
                    }
                    );
  }
  /** Définit l'extension des fichiers si celle si est forcée.
   * @param extension Extension des fichiers par exemple "java" ou "jvs", ou null (valeur par défaut) pour pas fixer d'extension.
   * @return Cet objet, permettant de définir la construction <tt>new TextFilesEditor().setExtension(..)</tt>.
   */
  public TextFilesEditor setExtension(String extension) {
    this.extension = extension;
    if("jvs".equals(extension)) {
      TextFileEditor file = openFile(false);
      file.setText("\nvoid main() {\n\n}\n");
    }
    return this;
  }
  private String extension = null;

  // Ouvre une nouvelle fenêtre d'édition, vide ou à partir d'un dialogue utilisateur.
  private TextFileEditor openFile(boolean dialog) {
    TextFileEditor file = new TextFileEditor().setExtension(extension);
    if(dialog) {
      if(file.load() && canOpen(file)) {
        addTab(file.getName(), file, true);
      }
    } else {
      addTab("Nouveau fichier", file, true);
    }
    return file;
  }
  // Teste si un fichier n'est pas déjà ouvert à moins de souhaiter forcer son ouverture
  private boolean canOpen(TextFileEditor file) {
    boolean ok = file.getFileLocation() != null;
    for(int i = 0; ok && i < getTabCount(); i++)
      if(getComponentAt(i) instanceof TextFileEditor && file.getFileLocation().equals(((TextFileEditor) getComponentAt(i)).getFileLocation())) {
        ok = false;
      }
    if(!ok) {
      int r = JOptionPane.showConfirmDialog(null,
                                            "Le fichier " + file.getName() + " est déjà ouvert, voulez-vous vraiment éditer «sur» l'autre version ?",
                                            "Erreur d'ouverture",
                                            JOptionPane.YES_NO_OPTION);
      ok = r == JOptionPane.YES_OPTION;
    }
    return ok;
  }
  // Sauve le fichier courant.
  private void saveFile(boolean saveAs) {
    if(getSelectedFile(true) != null) {
      if(saveAs) {
        selectedFile.saveAs();
      } else {
        selectedFile.save(false);
      }
      setTitleAt(getSelectedIndex(), selectedFile.getName());
    }
  }
  @Override
  protected boolean isCloseable(int index) {
    return getSelectedFile(false) != null ? selectedFile.save(true) : true;
  }
  // Renvoie le fichier en cours d'édition ou null sinon
  private TextFileEditor getSelectedFile(boolean verbose) {
    if((getSelectedIndex() != -1) && getComponentAt(getSelectedIndex()) instanceof TextFileEditor) {
      return selectedFile = (TextFileEditor) getComponentAt(getSelectedIndex());
    } else {
      if(verbose) {
        JOptionPane.showMessageDialog(null,
                                      "Il n'y a aucun fichier sélectionné !",
                                      "Erreur",
                                      JOptionPane.ERROR_MESSAGE);
      }
      return null;
    }
  }
  private TextFileEditor selectedFile = null;

  /** Ferme tous les fichiers.
   * @return La valeur vraie si les dialogues ont abouti, faux sinon.
   */
  public boolean isCloseable() {
    boolean ok = true;
    for(int i = 0; ok && i < getTabCount(); i++)
      ok &= isCloseable(i);
    return ok;
  }
  /** Lanceur du mécanisme d'éditon.
   * @param usage <tt>java org.javascool.gui2.TextFilesEditor</tt>
   */
  public static void main(String[] usage) {
    TextFilesEditor.newTextFilesEditor("editor", 800, 600, "jvs");
  }
}
