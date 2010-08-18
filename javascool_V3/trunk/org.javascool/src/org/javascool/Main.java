/********************************************************************************
      ______________________________________________
     | By Philippe Vienne <philoumailabo@gmail.com> |
     | Distrubuted on GNU General Public Licence    |
     | Revision 558 du SVN                          |
     | © 2010 INRIA, All rights reserved            |
     |______________________________________________|

 ********************************************************************************/

package org.javascool;

// Used to define the gui
import javax.swing.JApplet;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JToolBar;
import javax.swing.JTabbedPane;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import java.awt.event.ActionListener; 
import java.awt.event.ActionEvent; 
import proglet.ingredients.Console;

// used for file interface
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.lang.Object;

// Used to manage keystroke
import javax.swing.KeyStroke;
import javax.swing.JFrame;
import javax.swing.JComponent;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

// Used to register elements
import java.util.HashMap;


/** This is the javascool v3 interface starter.
 * <p>- It can be used either as standalone application or a certified applet.</p>
 * @author Philippe Vienne <philoumailabo@gmail.com>
 * @see <a href="Main.java.html">source code</a>
 * @serial exclude
 */
public class Main extends JApplet { /**/public Main() { }
  private static final long serialVersionUID = 1L;

  // [1] Defines the main panel and defines how to edit the toolbar, activityList and tabbedpane
  private JToolBar toolBar = new JToolBar();
  private JTabbedPane tabbedPane = new JTabbedPane();
  private JComboBox activityList = new JComboBox();
  /**/public void init() {
    JPanel toppane = new JPanel();
    toppane.setLayout(new BorderLayout());
    toppane.add(toolBar, BorderLayout.WEST);
    toppane.add(activityList, BorderLayout.EAST);  
    activityList.addActionListener(alistener);
    getContentPane().add(toppane, BorderLayout.NORTH);
    getContentPane().add(tabbedPane, BorderLayout.CENTER);
    // Adds buttons and activities using generic routines
    fileTools();
    addActivities();
    // Initializes the activity from the HTML tag or proposes a default activity
    try { setActivity(getParameter("activity")); } catch(Exception e) { setActivity((String) activityList.getSelectedItem()); }
  }
  /** Adds a button to the toolbar.
   * @param label Button label.
   * @param icon Button icon. If null do not show icon.
   * @param action Button action.
   */
  public void addTool(String label, String icon, Runnable action) {
    JButton button = icon == null ? new JButton(label) : new JButton(label, Utils.getIcon(icon));
    button.addActionListener(alistener);
    toolBar.add(button);
    buttons.put(label, button);
    actions.put(label, action);
    toolBar.revalidate();
  }
  /** Removes a button from the tool bar. */
  public void delTool(String label) {
    if (buttons.containsKey(label)) {
      toolBar.remove(buttons.get(label));
      buttons.remove(label);
      actions.remove(label);
      toolBar.revalidate();
    }
  }
  /** Adds a separator on the toolbar. */
  public void addToolSeparator() {
    toolBar.addSeparator();
  }
  /** HashMap for action list.
   * The map associate a String to a Runnable
   */
  private HashMap<String,Runnable> actions = new HashMap<String,Runnable>();
  /** HashMap for button list.
   * The map associate a String to a JButton
   */
  private HashMap<String,JButton> buttons = new HashMap<String,JButton>();
  /** Adds a tab to the tabbed panel.
   * @param label Tab label.
   * @param pane Tab panel.
   */
  public void addTab(String label, JPanel pane) {
    tabbedPane.addTab(label, null, pane, label);
    tabs.put(label, pane);
    tabbedPane.revalidate();
  }
  /** Adds a tab to the tabbed panel to display a text.
   * @param label Tab label.
   * @param location Tab Html text to display.
   */
  public void addTab(String label, String location) {
    addTab(label, new HelpDisplay().load(location));
  }
  /** A display with external links canceled. */
  private class HelpDisplay extends HtmlDisplay {
    private static final long serialVersionUID = 1L;
    public HtmlDisplay reset(String text) { 
      return super.reset(text.replaceAll("<a\\s+href=\\s*\"(http|https):[^\"]*\"[^>]*>([^<]*)</a>", "$1"));
    }
  }
  /** Removes a tab from the tabbed panel.
   * @param label Tab label.
   */
  public void delTab(String label) {
    if (tabs.containsKey(label)) {
      tabbedPane.remove(tabs.get(label));
      tabs.remove(label);
      tabbedPane.revalidate();
    }
  }
  /** Show a tab from the tabbed panel.
   * @param label Tab label.
   */
  public void showTab(String label) {
    if (tabs.containsKey(label)) {
      tabbedPane.setSelectedComponent(tabs.get(label));
      tabbedPane.revalidate();
    }
  }
  /** HashMap for tab list.
   * The map associate a String to a JPanel
   */
  private HashMap<String,JPanel> tabs = new HashMap<String,JPanel>();
  /** Adds an activity tab to the tabbed panel. 
   * @param activity Adds a predefined activity.
   */
  public void addActivity(Activity activity) {
    activities.put(activity.getTitle(), activity);
    activityList.addItem(activity.getTitle());
    activityList.revalidate();
  }
  /** Start any activity.
  * The activity must be list in the HashMap activities
  * @param name Name of Activity in the HashMap
  */
  private void setActivity(String name) {
    if (activities.containsKey(name)) {
      activity = activities.get(name);
      toolBar.removeAll();
      tabbedPane.removeAll();
      toolBar.add(new JLabel(Utils.getIcon("org/javascool/doc-files/icones32/logo_jvs.gif")));
      fileTools();
      fileChooser.resetFile();
      activity.init();
      toolBar.revalidate();
      tabbedPane.revalidate();
    }
  }
  /** Current activity, set to null at reset. */
  private Activity activity = null;
  /** HashMap for Activity list.
   * The map associate a String to an Activity
   */
  private HashMap<String,Activity> activities = new HashMap<String,Activity>();
  /** Generic action listener for all actions. */
  private ActionListener alistener = new ActionListener() {
      public void actionPerformed(ActionEvent e) {
	if (e.getSource() == activityList) {
	  if (firstActivity || fileSavePlease()) 
	    setActivity((String) ((JComboBox) e.getSource()).getSelectedItem());
	  firstActivity = false;
	} else {
	  actions.get(((JButton) e.getSource()).getText()).run();
	}
      }
    };
  private boolean firstActivity = true;

  // [2] File Open/Save management and Help
  /** Defines the JavaScool dedicated file chooser. */
  private class JsFileChooser extends JFileChooser {
    private static final long serialVersionUID = 1L;
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
      if (showOpenDialog(Main.this) == 0)
	doOpen(editor, getSelectedFile().getPath());
    }
    /** Manages an open action (no dialog). */
    public void doOpen(Editor editor, String file) {
      setSelectedFile(new File(file));
      String text = Utils.loadString(this.file = file);
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
      if (showSaveDialog(Main.this) == 0) {
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
      if (!text.matches("^[ \t]*\n.*")) text = "\n" + text;
      // Reload in editor the clean text
      if (editor != null) editor.setText(text);
      Utils.saveString(file, text);
    }
    /** Sets the next save dialog title. 
     * @param title Optional title for a specific dialog
     */
    public void setSaveDialogTitle(String title) { this.title = title; } private String title = null;
  }
  // Corrects the file name if not standard and cancel the extension
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

  private JsFileChooser fileChooser = new JsFileChooser();
  private Runnable fileOpen = new Runnable() { public void run() {
    if (activity == null) return;
    switch(!activity.getEditor().isModified() ? 1 : new JOptionPane().
	   showConfirmDialog(Main.this, 
			     "Voulez-vous enregistrer avant d'ouvrir un nouveau fichier ?", 
			     "Sauvgarder avant d'ouvrir", 
			     JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE)) {
    case 0: // Yes save
      if (fileChooser.doSaveAs(activity.getEditor(), activity.getExtension())) 
	fileChooser.doOpenAs(activity.getEditor(), activity.getExtension());
      break;
    case 1: // No need to save
      fileChooser.doOpenAs(activity.getEditor(), activity.getExtension());
      break;
    }
  }};
  private Runnable fileSave = new Runnable() { public void run() {
    if (activity == null) return;
    fileChooser.doSaveAs(activity.getEditor(), activity.getExtension());
  }};
  /** Loads a file in the current activity editor. 
   * @param file The file name.
   */
  public void setFile(String file) {
    if (activity == null) return;
    fileChooser.doOpen(activity.getEditor(), file);
  }
  /**/public void stop() {
    fileSavePlease();
  }
  /** Saves a file, before exiting or activity change. */
  private boolean fileSavePlease() {
    switch(activity == null || !activity.getEditor().isModified() ? 1 : new JOptionPane().
	   showConfirmDialog(Main.this, 
			     "Voulez-vous enregistrer avant de fermer ?", 
			     "Sauvgarder avant de fermer", 
			     JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE)) {
    case 0: // Yes save and return true if and only if the save is validated
      fileChooser.setSaveDialogTitle("Enregistrer votre fichier avant de passer à la suite");
      return fileChooser.doSaveAs(activity.getEditor(), activity.getExtension());
    case 1: // No need to save
      return true;
    default: // Ah ! Cancel
      return false;
    }
  }

  // Help show mechanism
  private Runnable helpShow = new Runnable() { public void run() {
    if(helpOn) {
      delTab("Aide");
      helpOn = false;
    } else {
      addTab("Aide", helpFile);
      showTab("Aide"); 
      helpOn = true;
    }
  }};
  private boolean helpOn = false;
  private String helpFile = "org/javascool/doc-files/about-main.htm";

  // Sets the basic tools
  private void fileTools() {
    addTool("Ouvrir", "org/javascool/doc-files/icones16/open.png", fileOpen);
    addTool("Sauver", "org/javascool/doc-files/icones16/save.png", fileSave);
    addToolSeparator();
    addTool("Aide", "org/javascool/doc-files/icones16/help.png", helpShow);
    addToolSeparator();
  }

  // Defines the key-strokes
  {
    getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_MASK), "open");
    getRootPane().getActionMap().put("open",  new AbstractAction("open") {
	private static final long serialVersionUID = 1L;
	public void actionPerformed(ActionEvent e) { 
	  fileOpen.run();
	}});
    getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_MASK), "save");
    getRootPane().getActionMap().put("save",  new AbstractAction("save") {
	private static final long serialVersionUID = 1L;
	public void actionPerformed(ActionEvent e) { 
	  fileSave.run();
	}});
    getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_H, KeyEvent.CTRL_MASK), "help");
    getRootPane().getActionMap().put("help",  new AbstractAction("help") {
	private static final long serialVersionUID = 1L;
	public void actionPerformed(ActionEvent e) { 
	  helpShow.run();
	}});
    getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_L, KeyEvent.CTRL_MASK), "validate");
    getRootPane().getActionMap().put("validate",  new AbstractAction("validate") {
	private static final long serialVersionUID = 1L;
	public void actionPerformed(ActionEvent e) { 
	  if (validate != null) validate.run();
	}});
    getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_Q, KeyEvent.CTRL_MASK), "quit");
    getRootPane().getActionMap().put("quit",  new AbstractAction("quit") {
	private static final long serialVersionUID = 1L;
	public void actionPerformed(ActionEvent e) { 
	  if(fileSavePlease())
	    activity = null;
	    Utils.unshow(Main.this);
	}});
  }
  /** This is the runnable called when the ^L keystroke is called. */
  private Runnable validate = null;
  
  // [3] Defined activities.
  /** Defines an interactive activity. */
  private interface Activity {
    /** Returns the activity title. */
    public String getTitle();
    /** Initializes the activity, adding buttons and pannels. */
    public void init();
    /** Returns the activity editor. */
    public Editor getEditor();
    /** Returns the required file extension. 
     * e.g., ".jvs" or ".pml"
     */
    public String getExtension();
  }
  /** Defines all registered activities. */
  private void addActivities() {
    addActivity(new ProgletActivity("ingredients") {
	public String getTitle() { return "Découvrir les ingrédients des algorithmes"; }
	public void init() {
	  super.init();
	  addTab("Parcours d'initiation", "proglet/ingredients/doc-files/index.htm");
	  /*
	  addTab("Séquence d'instruction", "proglet/ingredients/doc-files/sujet-hello-world.htm");
	  addTab("Se servir de variables", "proglet/ingredients/doc-files/sujet-about-variables.htm");
	  addTab("L'instruction conditionnelle", "proglet/ingredients/doc-files/sujet-about-if.htm");
	  addTab("Utiliser des fonctions", "proglet/ingredients/doc-files/sujet-about-functions.htm");
	  addTab("Programmer des boucles", "proglet/ingredients/doc-files/sujet-about-while.htm");
	  addTab("Mémo des instructions", "proglet/ingredients/doc-files/about-memo.htm");
	  */
	}
      });
    addActivity((new ProgletActivity("ingredients") {
	public String getTitle() { return "Un tutoriel sur les valeurs numériques"; }
	public void init() {
	  super.init();
	  addTab("Enoncé de l'exercice", "proglet/exosdemaths/doc-files/sujet-appli-geometry.htm");
	  addTab("Mémo des instructions", "proglet/ingredients/doc-files/about-memo.htm");
	}}));
    addActivity(new ProgletActivity("exodemaths") {
	public String getTitle() { return "Programmer un calcul géométrique"; }
	public void init() {
	  super.init();
	  addTab("Enoncé de l'exercice", "proglet/exosdemaths/doc-files/sujet-appli-geometry.htm");
	  initDoc();
	}});
    addActivity(new AlgoEditorActivity() {
	public String getTitle() { return "Découvrir les algorithmes de manière graphique"; }
      });
    addActivity(new ProgletActivity("dichotomie") {
	public String getTitle() { return "Comprendre le principe de la dichotomie"; }
	public void init() {
	  super.init();
	  addTab("Enoncé de l'exercice", "proglet/dichotomie/doc-files/sujet-appli-dicho.htm");
	  initDoc();
	}});
    addActivity(new ProgletActivity("pixelsetcie") {
	public String getTitle() { return "Comprendre la manipulation d'images"; }
	public void init() {
	  super.init();
	  addTab("Enoncé de l'exercice", "proglet/pixelsetcie/doc-files/sujet-appli-image.htm");
	  initDoc();
	}});
    addActivity(new ProgletActivity("convanalogique") {
	public String getTitle() { return "Programmer la conversion analogique-digitale"; }
	public void init() {
	  super.init();
	  addTab("Enoncé de l'exercice", "proglet/convanalogique/doc-files/sujet-appli-conva.htm");
	  initDoc();
	}});
    addActivity(new ProgletActivity("synthesons") {
	public String getTitle() { return "Découverte du signal sonore"; }
	public void init() {
	  super.init();
	  initDoc();
	}});
    addActivity(new ProgletActivity("javaprog") {
	public String getTitle() { return "Programmer directement en Java"; }
	public void init() {
	  super.init();
	  initDoc();
	}});
    addActivity(new ProgletActivity("tortuelogo") {
	public String getTitle() { return "Programmer avec la «tortue logo»"; }
	public void init() {
	  super.init();
	  initDoc();
	}});
  }

  // Defines a compilation activity 
  private abstract class JavaActivity implements Activity {
    // Compilation/execution mechanism
    protected void initCompile() {
      addTab("Console", Jvs2Java.getPanel("ingredients"));
      addTool("Compile", "org/javascool/doc-files/icones16/compil.png", validate = compile);
    }
    private Runnable compile = new Runnable() { public void run() {
      delTool("Exécuter");
      delTool("Arrêter");
      showTab("Console");
      Console.clear();
      fileChooser.doSave(activity.getEditor(), activity.getExtension());
      if (fileChooser.getFile() != null) {
	if (getEditor() instanceof AlgoEditor) {
	  fileChooser.doSave(algoViewer, Jvs2Java.reformat(((AlgoEditor) getEditor()).getJavaSource()), ".jvs");
	}
	Jvs2Java.translate(fileChooser.getFile());
	String out = Jvs2Java.compile(fileChooser.getFile());
	System.out.println(out.length() == 0 ? "Compilation réussie !" : out);
	Console.printHtml("<hr>\n");
	if (out.length() == 0) {
	  addTool("Exécuter", "org/javascool/doc-files/icones16/play.png", execute);
	  addTool("Arrêter", "org/javascool/doc-files/icones16/stop.png", stop);
	}
      } else {
	System.out.println("Impossible de compiler: le fichier n'est pas sauvegardé !");
      }
    }};
    private Runnable execute = new Runnable() { public void run() {
      Console.clear();
      Jvs2Java.load(fileChooser.getFile());
      Jvs2Java.run(true);
    }};
    private Runnable stop = new Runnable() { public void run() {
      Jvs2Java.run(false);
    }};
  }

  // Defines a proglet activity
  private abstract class ProgletActivity extends JavaActivity {
    /** Constructs a proglet activity.
     * @param proglet The proglet to use.
     * @throws IllegalArgumentExceptionif the proglet is undefined.
     */
    public ProgletActivity(String proglet) { 
      if (Jvs2Java.getPanel(proglet) == null) throw new IllegalArgumentException("Undefined proglet : "+proglet);
      this.proglet = proglet;
    } 
    private String proglet;
    // Common panels and tools
    public void init() {
      if (jvsEditor == null) jvsEditor = new JvsSourceEditor(); 
      addTab("Jvs Editor", (JPanel) jvsEditor);
      jvsEditor.setProglet(proglet);
      initCompile();
      if (!"ingredients".equals(proglet))
	addTab(proglet, Jvs2Java.getPanel(proglet));
    }
    protected void initDoc() {
      addTab("Document de la proglet", "proglet/"+proglet+"/doc-files/about-proglet.htm");
      addTab("Mémo des instructions", "proglet/ingredients/doc-files/about-memo.htm");
    }
    public Editor getEditor() { return jvsEditor; }
    public String getExtension() { return ".jvs"; }
  }
  private JvsSourceEditor jvsEditor = null;
  // Defines a AlgoTree proglet activity
  private abstract class AlgoEditorActivity extends JavaActivity {
    // Common panels and tools
    public void init() {
      if (algoEditor == null) algoEditor = new AlgoEditor(); 
      if (algoViewer == null) { algoViewer = new JvsSourceEditor(); algoViewer.reset(false); }
      addTab("Algo Editor", (JPanel) algoEditor);
      addTab("Code Viewer", (JPanel) algoViewer);
      initCompile();
      addTab("Tracé", Jvs2Java.getPanel("exodemaths"));
    }
    public Editor getEditor() { return algoEditor; }
    public String getExtension() { return ".pml"; }
  }
  private AlgoEditor algoEditor = null; private JvsSourceEditor algoViewer = null;
  
  /** Used to run a javasccol v3 as a standalone program. 
   * <p>- Starts a JavaScool "activity" which result is to be stored in a "file-name".</p>
   * @param usage <tt>java org.javascool.Main [activity [file-name]]</tt><ul>
   * <li><tt>activity</tt> specifies the activity to be done.</li>
   * <li><tt>file-name</tt> specifies the file used for the activity.</li>
   * </ul>
   */
  public static void main(String[] usage) {
    System.out.println("Hi ! V3 is coming :-)");
    Main main = new Main();
    if (usage.length >= 1) main.setActivity(usage[0]);
    if (usage.length >= 2) main.setFile(usage[1]);
    Utils.show(main, "Java'Scool v3.0-beta", Utils.getIcon("org/javascool/doc-files/icones32/logo_jvs.gif"), false);
  }
}
