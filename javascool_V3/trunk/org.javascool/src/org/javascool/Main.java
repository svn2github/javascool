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
import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JToolBar;
import javax.swing.JTabbedPane;
import javax.swing.JSplitPane;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import java.awt.event.ActionListener; 
import java.awt.event.ActionEvent; 

// Processing interface
import java.awt.Container;
import java.applet.Applet;
import java.io.PrintStream;

// Proglets used
import proglet.ingredients.Console;
import proglet.exosdemaths.CurveDisplay;

// Used for the file interface
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.lang.Object;

/* Used for the location/size control when in floatable mode
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;
*/

// Used to manage keystroke
import javax.swing.KeyStroke;
import javax.swing.JFrame;
import javax.swing.JComponent;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

// Used to register elements
import java.util.HashMap;

// Used to set Win LOOK
import javax.swing.UIManager;

/** This is the javascool v3 interface starter.
 * <p>- It can be used either as standalone application or a certified applet.</p>
 * @author Philippe Vienne <philoumailabo@gmail.com>
 * @see <a href="doc-files/about-main.htm">user documentation (in French)</a>
 * @see <a href="Main.java.html">source code</a>
 * @serial exclude
 */
public class Main extends JApplet { /**/public Main() { }
  private static final long serialVersionUID = 1L;
  static final String title = "Java'Scool 3.0";
  static final boolean withProcessing = true; // If true includes the processing applets

  // [0] Defines the look and field.
  static {
    String os = System.getProperty("os.name");
    if (os.startsWith("Windows")) {
      try { UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel"); } catch(Exception e) { }
    } else {
      System.err.println("Note: Vous n'êtes pas sous Windows, bravo !!!!");
      try { System.setProperty("com.apple.mrj.application.apple.menu.about.name", title); } catch(Exception e) { }
      try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch(Exception e) { System.err.println("Note: Utilisaton du thème Java (et non du système)"); }
    } 
  }

  // [1] Defines the main panel and defines how to edit the toolbar, activityList and tabbedpane
  private JToolBar toolBar = new JToolBar(title, JToolBar.HORIZONTAL);
  private JTabbedPane westPane = new JTabbedPane(), eastPane = new JTabbedPane();
  private JComboBox activityList = new JComboBox();
  /** Builds the GUI. */
  private void initGUI() {
    JPanel toppane = new JPanel();
    toppane.setLayout(new BorderLayout());
    toolBar.setBorderPainted(false);
    JPanel toppaneWest = new JPanel();
    toppaneWest.add(toolBar);
    toppaneWest.add(new JLabel(" "));
    toppane.add(toppaneWest, BorderLayout.WEST);
    JToolBar activityBar = new JToolBar(title, JToolBar.HORIZONTAL);
    activityList.addActionListener(alistener);
    activityBar.setBorderPainted(false);
    activityBar.add(activityList);
    JPanel toppaneEast = new JPanel();
    toppaneEast.add(new JLabel("Activité : "));
    toppaneEast.add(activityBar);
    toppane.add(toppaneEast, BorderLayout.EAST);  
    getContentPane().add(toppane, BorderLayout.NORTH);
    JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, westPane, eastPane);
    westPane.setMinimumSize(new Dimension(100, 100));
    eastPane.setMinimumSize(new Dimension(100, 100));
    splitPane.setResizeWeight(0.6);
    splitPane.setContinuousLayout(true); 
    splitPane.setOneTouchExpandable(false);
    getContentPane().add(splitPane, BorderLayout.CENTER);
    // Adds buttons and activities using generic routines
    fileTools();
    addActivities();
    // Initializes the activity from the HTML tag if any
    try { setActivityAs(getParameter("activity")); } catch(Exception e) { }
    try { setFileAs(getParameter("file")); } catch(Exception e) { }
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
   * @param icon Location of the icon for the tab. If null, no icon.
   * @param east If true use the east pane, else the west pane.
   */
  public void addTab(String label, Container pane, String icon, boolean east) {
    if (east) { 
      eastPane.addTab(label, icon == null ? null : Utils.getIcon(icon), pane, label);
      eastPane.revalidate();
    } else {
      westPane.addTab(label, icon == null ? null : Utils.getIcon(icon), pane, label);
      westPane.revalidate();
    }
    jtabs.put(label, pane);
    wtabs.put(label, east);
  }
  /** Adds a tab to the tabbed panel to display a text.
   * @param label Tab label.
   * @param location Tab Html text to display.
   * @param icon Location of the icon for the tab. If null, no icon.
   * @param east If true use the east pane, else the west pane.
   */
  public void addTab(String label, String location, String icon, boolean east) {
    addTab(label, new HelpDisplay().load(location),icon,east);
  }
  /** A display with external links canceled. */
  private class HelpDisplay extends HtmlDisplay {
    private static final long serialVersionUID = 1L;
    public HtmlDisplay load(String location) { 
      if(location.matches("^(http|https|rtsp|mailto):.*$")) {
	reset("Cette page est à l'adresse internet: <tt>"+location.replaceFirst("^(mailto):.*", "$1: ...")+"</tt> (non accessible ici).");  
      } else {
        super.load(location);
      }
      return this;
    }
  }
  /** Removes a tab from the tabbed panel.
   * @param label Tab label.
   */
  public void delTab(String label) {
    if (jtabs.containsKey(label)) {
      if(wtabs.get(label)) {
	eastPane.remove(jtabs.get(label));
	eastPane.revalidate();
      } else {
	westPane.remove(jtabs.get(label));
	westPane.revalidate();
      }
      jtabs.remove(label);
      wtabs.remove(label);
    }
  }
  /** Show a tab from the tabbed panel.
   * @param label Tab label.
   */
  public void showTab(String label) {
    if (jtabs.containsKey(label)) {
      if(wtabs.get(label)) {
	eastPane.setSelectedComponent(jtabs.get(label));
	eastPane.revalidate();
      } else {
	westPane.setSelectedComponent(jtabs.get(label));
	westPane.revalidate();
      }
    }
  }
  /** HashMap for tab list.
   * The map associate a String to a JPanel
   */
  private HashMap<String,Container> jtabs = new HashMap<String,Container>();
  /** HashMap for west/east location of the tab list.
   * The map associate a String to a JPanel
   */
  private HashMap<String,Boolean> wtabs = new HashMap<String,Boolean>();

  /** Adds an activity tab to the tabbed panel. 
   * @param activity Adds a predefined activity.
   */
  public void addActivity(Activity activity) {
    activities.put(activity.getTitle(), activity);
    activityList.addItem(activity.getTitle());
    activityList.revalidate();
  }
  /** Starts a unique activity.
   * @param name Name of Activity in the HashMap or Index in the chooser
   */
  private void setActivityAs(String name) {
    if (activities.containsKey(name)) {
      System.out.println("Activité : "+name);
      setActivity(name);
      activityList.getParent().getParent().getParent().remove(activityList.getParent().getParent());
    } else if (name != null) {
      int index = name.matches("^[0-9]+$") ? Integer.parseInt(name) : 0; 
      if (0 <= index && index < activityList.getItemCount())
	setActivityAs((String) activityList.getItemAt(index));
    }
  }
  /** Sets an any activity.
   * The activity must be list in the HashMap activities
   * @param name Name of Activity in the HashMap
   */
  private void setActivity(String name) {
    if (activity != null) 
      activity.stop();
    if (activities.containsKey(name)) {
      activity = activities.get(name);
      toolBar.removeAll();
      westPane.removeAll();
      eastPane.removeAll();
      toolBar.add(new JLabel(Utils.getIcon("org/javascool/doc-files/icones32/logo_jvs.gif")));
      fileTools();
      fileChooser.resetFile();
      activity.init();
      toolBar.revalidate();
      westPane.revalidate();
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
      text = "\n"+text.trim();
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
  public void setFileAs(String file) {
    if (activity == null) return;
    fileChooser.doOpen(activity.getEditor(), file);
  }
  /**/public void stop() {
    fileSavePlease();
  }
  /** Saves a file, before exiting or activity change. */
  private boolean fileSavePlease() {
    switch(activity == null || activity.getEditor() == null || !activity.getEditor().isModified() ? 1 : new JOptionPane().
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
      addTab("Aide", helpFile, "org/javascool/doc-files/icones16/help.png", false);
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
    getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_I, KeyEvent.CTRL_MASK), "info");
    getRootPane().getActionMap().put("info",  new AbstractAction("info") {
	private static final long serialVersionUID = 1L;
	public void actionPerformed(ActionEvent e) { 
	  JOptionPane.showMessageDialog(Main.this, new JLabel("<html><body><h4><hr>"+
							      Utils.loadString("org/javascool/js-manifest.mf").
							      replaceFirst("Summary", "Nom").
							      replaceFirst("Manifest-version", "Version").
							      replaceFirst("Created-By", "Créé-Par").
							      replaceFirst("Implementation-URL", "Site-web").
							      replaceFirst("©", "\nLicence: ©").
							      replaceAll("(Implementation-(Vendor|Version)|Main-Class).*\n", "").
							      replaceAll(":", " :").
							      replaceAll("\n", "<hr>")+
							      "</h4></body></html>", 
							      Utils.getIcon("org/javascool/doc-files/icones32/logo_jvs.gif"), 
							      JLabel.CENTER), 
					"About "+title+"...", JOptionPane.INFORMATION_MESSAGE);
	}});
  }
  /** This is the runnable called when the ^L keystroke is called. */
  private Runnable validate = null;
  
  // [3] Defined activities.
  /** Defines an interactive activity. */
  private interface Activity {
    /** Returns the activity title. */
    public String getTitle();
    /** Initializes the activity, adding buttons and pannels and start it. */
    public void init();
    /** Stops the activity. */
    public void stop();
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
	  addTab("Parcours d'initiation", "proglet/ingredients/doc-files/index.htm", "org/javascool/doc-files/icones16/globe.png", true);
	}
      });
    addActivity((new ProgletActivity("ingredients") {
	public String getTitle() { return "Un tutoriel sur les valeurs numériques"; }
	public void init() {
	  super.init();
	  addTab("Enoncé de l'exercice", "proglet/exosdemaths/doc-files/sujet-appli-geometry.htm", "org/javascool/doc-files/icones16/globe.png", true);
	  addTab("Mémo des instructions", "proglet/ingredients/doc-files/about-memo.htm", "org/javascool/doc-files/icones16/globe.png", true);
	}}));
    addActivity(new ProgletActivity("exosdemaths") {
	public String getTitle() { return "Programmer un calcul géométrique"; }
	public void init() {
	  super.init();
	  addTab("Enoncé de l'exercice", "proglet/exosdemaths/doc-files/sujet-appli-geometry.htm", "org/javascool/doc-files/icones16/globe.png", true);
	  initDoc();
	}});
    addActivity(new AlgoEditorActivity() {
	public String getTitle() { return "Découvrir les algorithmes de manière graphique"; }
      });
    addActivity(new ProgletActivity("dichotomie") {
	public String getTitle() { return "Comprendre le principe de la dichotomie"; }
	public void init() {
	  super.init();
	  addTab("Enoncé de l'exercice", "proglet/dichotomie/doc-files/sujet-appli-dicho.htm","org/javascool/doc-files/icones16/globe.png", true);
	  initDoc();
	}});
    addActivity(new ProgletActivity("pixelsetcie") {
	public String getTitle() { return "Comprendre la manipulation d'images"; }
	public void init() {
	  super.init();
	  addTab("Enoncé de l'exercice", "proglet/pixelsetcie/doc-files/sujet-appli-image.htm","org/javascool/doc-files/icones16/globe.png", true);
	  initDoc();
	}});
    addActivity(new ProgletActivity("convanalogique") {
	public String getTitle() { return "Programmer la conversion analogique-digitale"; }
	public void init() {
	  super.init();
	  addTab("Enoncé de l'exercice", "proglet/convanalogique/doc-files/sujet-appli-conva.htm","org/javascool/doc-files/icones16/globe.png", true);
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
    // Adds processing activities
    if (withProcessing) {
      addActivity(new ProcessingActivity("ExplorationSonore", 1024, 768) {
	  public String getTitle() { return "Exploration du signal sonore"; }
	  public void init() {
	    super.init();
	  }
	});
      addActivity(new ProcessingActivity("CryptageRSA", 600, 600) {
	  public String getTitle() { return "Expérimenter avec la cryptographie"; }
	  public void init() {
	    super.init();
	  }
	});
    }
  }
  
  // Defines a compilation activity 
  private abstract class JavaActivity implements Activity {
    // Compilation/execution mechanism
    protected void initCompile() {
      addTab("Console", Jvs2Java.getPanel("ingredients"), "org/javascool/doc-files/icones16/compile.png", true);
      addTool("Compiler", "org/javascool/doc-files/icones16/compile.png", validate = compile);
    }
    private Runnable compile = new Runnable() { public void run() {
      delTool("Exécuter");
      delTool("Arrêter");
      showTab("Console");
      Console.clear();
      if (getEditor().getText().trim().length() > 0) {
	fileChooser.doSave(getEditor(), getExtension());
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
      }  else {
	System.out.println("Rien à compiler: il n'y pas de code dans l'éditeur !");
      }
    }};
    private Runnable execute = new Runnable() { public void run() {
      Console.clear();
      CurveDisplay.scopeReset();
      Jvs2Java.load(fileChooser.getFile());
      Jvs2Java.run(true);
    }};
    private Runnable stop = new Runnable() { public void run() {
      Jvs2Java.run(false);
    }};
    public void stop() { }
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
      addTab("Editeur", (JPanel) jvsEditor, "org/javascool/doc-files/icones16/edit.png", false);
      jvsEditor.setProglet(proglet);
      initCompile();
      if (!"ingredients".equals(proglet)) {
	String name = "exosdemaths".equals(proglet) ? "Tracé" : proglet;
	addTab(name, Jvs2Java.getPanel(proglet), "org/javascool/doc-files/icones16/compile.png", true);
      }
    }
    protected void initDoc() {
      addTab("Document de la proglet", "proglet/"+proglet+"/doc-files/about-proglet.htm", "org/javascool/doc-files/icones16/help.png", true);
      addTab("Mémo des instructions", "proglet/ingredients/doc-files/about-memo.htm", "org/javascool/doc-files/icones16/help.png", true);
    }
    public Editor getEditor() { return jvsEditor; }
    public String getExtension() { return ".jvs"; }
  }
  private JvsSourceEditor jvsEditor = null;

  // Defines a processing activity
  private abstract class ProcessingActivity extends JavaActivity {
    public Editor getEditor() { return jvsEditor; }
    public String getExtension() { return ".jvs"; }

    /** Constructs a processing activity.
     * @param processing The processing to use.
     * @throws IllegalArgumentExceptionif the processing is undefined.
     */
    public ProcessingActivity(String processing, int w, int h) { name = processing; width = w; height = h; } private String name; private int width, height;

    public void init() {
      if (jvsEditor == null) jvsEditor = new JvsSourceEditor(); 
      addTab("Editeur", (JPanel) jvsEditor, "org/javascool/doc-files/icones16/edit.png", false); 
      jvsEditor.setProglet(name);
      addTab("Document de la proglet", "sketchbook/"+name+"/about-proglet.htm", "org/javascool/doc-files/icones16/help.png", true);
      initCompile();
      initApplet();
    }
    private void initApplet() {
      PrintStream out = System.out;
      System.setOut(System.err);
      try {
	final boolean popup = true;
	applet = (Applet) Class.forName(name).newInstance();
	if (popup) {
	  frame = Utils.show(applet, name, Utils.getIcon("org/javascool/doc-files/icones16/compile.png"), width, height, false);
	  frame.setResizable(false);
	  Macros.sleep(1000);
	} else {
	  applet.init(); applet.start();
	  Macros.sleep(1000);
	  addTab(name+"1", applet, "org/javascool/doc-files/icones16/edit.png", true);
	  initControl();
	  addTab(name+"2", control, "org/javascool/doc-files/icones16/edit.png", false);
	}
      } catch(Throwable e) { System.out.println("Désolé, l'activité "+name+" n'est pas définie dans cette version ("+e+")."); }
      System.setOut(out);
    } 
    private Applet applet = null;
    private void initControl() {  
      if (applet != null) {
	try {
	  control = (Applet) Class.forName(name).getDeclaredMethod("getControl").invoke(applet);
	} catch(Throwable e) { Utils.report(e); System.err.println("Undefined processing control ("+e+") : "+name); }
      }
    }
    private Applet control = null;
    public void stop() { 
      if (frame != null) { frame.dispose(); frame = null; } 
      initControl(); if (control != null) { control.setVisible(false); }
    }
    private JFrame frame = null;
  }
  /* This deos NOT work keep here . . to copy/cut for other developments
  // Adds a component mover
  private void addProcessingDriver() {
    if (!frameDriver) {
      System.err.println("DRV");
      frameDriver = true;
      ((JFrame) getParent()).addWindowListener(new WindowListener() {
	  public void windowOpened(WindowEvent e) { }
	  public void windowClosing(WindowEvent e) { }
	  public void windowClosed(WindowEvent e) { if (rightFrame != null) rightFrame.dispose(); }
	  public void windowIconified(WindowEvent e) { System.err.println("MIN"); if (rightFrame != null) rightFrame.setVisible(false); }
	  public void windowDeiconified(WindowEvent e) { System.err.println("MAX"); if (rightFrame != null) rightFrame.setVisible(true); relocateFrames(); }
	  public void windowActivated(WindowEvent e) { }
	  public void windowDeactivated(WindowEvent e) { }
	});
    }
  }
  private void relocateFrames() {
    if (rightFrame != null) {
      /*
      rightFrame.setLocationRelativeTo(this);
      rightFrame.setLocation(20, 100);
      * /
      int x = getX() + getWidth() - rightFrame.getWidth() - 20, y = getY() + getHeight() - rightFrame.getHeight() - 20;      
      System.err.println("move ("+x+" "+y+")");
      // rightFrame.setLocation(x, y);
    }
  }
  private JFrame rightFrame = null; private boolean frameDriver = false;
  */

  // Defines a AlgoTree proglet activity
  private abstract class AlgoEditorActivity extends JavaActivity {
    // Common panels and tools
    public void init() {
      if (algoEditor == null) algoEditor = new AlgoEditor(); 
      if (algoViewer == null) { algoViewer = new JvsSourceEditor(); algoViewer.reset(false); }
      addTab("Editeur d'Algo.", (JPanel) algoEditor, "org/javascool/doc-files/icones16/edit.png", false);
      addTab("Voir le code en JVS", (JPanel) algoViewer, "org/javascool/doc-files/icones16/zoom-in.png", true);
      initCompile();
      addTab("Tracé", Jvs2Java.getPanel("exosdemaths"), "org/javascool/doc-files/icones16/compile.png", true);
      addTab("Documentation", "org/javascool/doc-files/about-algo-editor.htm", "org/javascool/doc-files/icones16/help.png", true);
    }
    public Editor getEditor() { return algoEditor; }
    public String getExtension() { return ".pml"; }
  }
  private AlgoEditor algoEditor = null; private JvsSourceEditor algoViewer = null;

  { initGUI(); }
  
  /** Used to run a javasccol v3 as a standalone program. 
   * <p>- Starts a JavaScool "activity" which result is to be stored in a "file-name".</p>
   * @param usage <tt>java org.javascool.Main [activity [file-name]]</tt><ul>
   * <li><tt>activity</tt> specifies the activity to be done (its index or name).</li>
   * <li><tt>file-name</tt> specifies the file used for the activity.</li>
   * </ul>
   */
  public static void main(String[] usage) {
    System.out.println("Note: Hi ! V3 is here :-)");
    Main main = new Main();
    if (usage.length >= 1) main.setActivityAs(usage[0]);
    if (usage.length >= 2) main.setFileAs(usage[1]);
    Utils.show(main, title, Utils.getIcon("org/javascool/doc-files/icones32/logo_jvs.gif"), true);
  }
}
