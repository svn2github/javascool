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
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JToolBar;
import javax.swing.JTabbedPane;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import java.awt.event.ActionListener; 
import java.awt.event.ActionEvent; 
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

  // [1] Defines the main panel and defines how to edit the toolbar, actList and tabbedpane
  JToolBar tools = new JToolBar();
  JTabbedPane tabbedPane = new JTabbedPane();
  JComboBox actList = new JComboBox();
  String aide = new String(Utils.loadString("org/javascool/doc-files/helpdoc/index.html"));
  String actuproglet=new String();
  private Boolean notfirstrun=false;
  /**/public void init() {
    JPanel toppane = new JPanel();
    toppane.setLayout(new BorderLayout());
    toppane.add(tools, BorderLayout.WEST);
    toppane.add(actList, BorderLayout.EAST);  
    actList.addActionListener(alistener);
    getContentPane().add(toppane, BorderLayout.NORTH);
    getContentPane().add(tabbedPane, BorderLayout.CENTER);

    // Adds buttons and activities using generic routines
    fileTools();
    addActivity(jvsActivity);
    addActivity(tortueActivity);
    addActivity(pmlActivity);
    //addActivity(algActivity);
    
    // Initializes the activity from the HTML tag or proposes a default activity
    try { setActivity(getParameter("activity")); } catch(Exception e) { setActivity("Démonstration de l'éditeur Jvs");}
    actuproglet="ingredients";

  }
  /** Adds a button to the toolbar.
   * @param label Button label.
   * @param icon Button icon. If null do not show icon.
   * @param action Button action.
   */
  public void addTool(String label, String icon, Runnable action) {
    JButton button = icon == null ? new JButton(label) : new JButton(label, Utils.getIcon(icon));
    button.addActionListener(alistener);
    tools.add(button);
    //if(mnemonic)
    //.setMnemonic(KeyEvent.VK_A);
    buttons.put(label, button);
    actions.put(label, action);
    tools.revalidate();
  }
  /** Removes a button from the tool bar. */
  public void delTool(String label) {
    if (buttons.containsKey(label)) {
      tools.remove(buttons.get(label));
      buttons.remove(label);
      actions.remove(label);
      tools.revalidate();
    }
  }
  /** Adds a separator on the toolbar. */
  public void addToolSeparator() {
    tools.addSeparator();
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
   * @param icon Tab icon.
   * @param pane Tab panel.
   */
  public void addTab(String label, String icon, JPanel pane) {
    tabbedPane.addTab(label, icon == null ? null : Utils.getIcon(icon), pane, label);
    tabs.put(label, pane);
    tabbedPane.revalidate();
  }
  /** Adds a tab to the tabbed panel.
   * @param label Tab label.
   * @param icon Tab icon.
   * @param pane Tab Html textfor the pane in string.
   */public void addTab(String label, String icon, String text) {
    addTab(label, icon, new HtmlDisplay().reset(text));
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
  /** Defines an interactive activity. */
  public static class Activity {
    /** Returns the activity title. */
    public String getTitle() { return ""; }
    /** Initializes the activity, adding buttons and pannels. */
    public void init(Main main) { }
    public Editor editor;
    public void setText(String text){editor.setText(text);}
    public String getText(){return editor.getText();}
  }
  /** Adds an activity tab to the tabbed panel. 
   * @param activity Adds a predefined activity.
   */
  public void addActivity(Activity activity) {
    activities.put(activity.getTitle(), activity);
    actList.addItem(activity.getTitle());
    actList.revalidate();
  }
  /** Start any activity.
  * The activity must be list in the HashMap activities
  * @param name Name of Activity in the HashMap
  */
  private void setActivity(String name) {
    activity = activities.get(name);
    if (activity == null) activity = activities.get("Démonstration de l'éditeur Pml");
    tools.removeAll();
    tabbedPane.removeAll();
    fileTools();
    activity.init(this);
    tools.revalidate();
    tabbedPane.revalidate();
  }
  /**Set activity to null.*/
  private Activity activity = null;
  /** HashMap for Activity list.
  *The map associate a String to an Activity
  */
  private HashMap<String,Activity> activities = new HashMap<String,Activity>();
  /** Generic action listener for all actions.*/
  private ActionListener alistener = new ActionListener() {
      public void actionPerformed(ActionEvent e) {
	if (e.getSource() == actList) {
	  if(notfirstrun) { 
	    pleaseSaveFile(); 
	  } else { 
	    notfirstrun = true;
	  }
	  setActivity((String) ((JComboBox) e.getSource()).getSelectedItem());
	} else {
	  actions.get(((JButton) e.getSource()).getText()).run();
	}
      }
    };

  // [2] File Open/Save management
  private SourceEditor se = new SourceEditor();
  private JFileChooser fc = new JFileChooser();
  private String fcTitle = null;
  private File file = null;
  private boolean checksave = true;
  private boolean verisave = true;
  {
    JFileFilter filtre = new JFileFilter();
    filtre.addType("jvs");
    filtre.addType("pml");
    filtre.setDescription("Fichiers JVS et PML");
    fc.setFileFilter(filtre);
  }
  public String returnastring(String toreturn){return toreturn;};
  /** Load a file. */
  public void setFile(String filename) {
    file = new File(filename);
    activity.setText(Utils.loadString(file.getPath()));
  }
  private Runnable openFile = new Runnable() { public void run() {
    int result = 1000;
    if(activity.getText().length() == 0) {
      result=1;
    } else {
      JOptionPane d = new JOptionPane();
      result = 
	d.showConfirmDialog(Main.this, 
			    "Voulez-vous enregistrer avant d'ouvrir un nouveau fichier ?", 
			    "Sauvgarder avant d'ouvrir", 
			    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
    }
    if(result == 0) {
      checksave=false;
      saveFile.run();
      checksave = true;
      if(verisave == false) {
	return;
      }
      fc.setDialogTitle("Ouvrir un programme");
      fc.setDialogType(JFileChooser.OPEN_DIALOG);
      fc.setApproveButtonText("Ouvrir");
      if (fc.showOpenDialog(Main.this) == 0) {
	file = fc.getSelectedFile();
	activity.setText(Utils.loadString(file.getPath()));
      }
    } else if(result == 1) {
      fc.setDialogTitle("Ouvrir un programme");
      fc.setDialogType(JFileChooser.OPEN_DIALOG);
      fc.setApproveButtonText("Ouvrir");
      if (fc.showOpenDialog(Main.this) == 0) {
	file = fc.getSelectedFile();
	activity.setText(Utils.loadString(file.getPath()));
      }
    }
  }};
  private Runnable saveFile = new Runnable() { public void run() {
    fc.setDialogTitle(fcTitle == null ? "Enregister un programme" : fcTitle);
    fc.setDialogType(JFileChooser.SAVE_DIALOG);
    fc.setApproveButtonText("Enregister");
    if(file == null) {
      fc.setCurrentDirectory(new File(System.getProperty("user.dir")));
      int out=fc.showSaveDialog(Main.this);
      if (out == 0) {
	file = fc.getSelectedFile();
	Utils.saveString(file.getPath(), activity.getText());
	verisave = true;
      } else {
        verisave = false;
      }
    } else {
      fc.setDialogTitle(fcTitle == null ? "Enregister un programme" : fcTitle);
      fc.setDialogType(JFileChooser.SAVE_DIALOG);
      fc.setApproveButtonText("Enregister");
      int out = fc.showSaveDialog(Main.this);
      if (out == 0) {
	file = fc.getSelectedFile();
	Utils.saveString(file.getPath(), activity.getText());
	verisave = true;
      } 
      Utils.saveString(file.getPath(), activity.getText());
      verisave = true;
    }
  }};
  private void pleaseSaveFile() {
    fcTitle = "Enregistrer votre fichier avant de passer à la suite";
    saveFile.run();
    fcTitle = null;
  }
  private Runnable showHelp = new Runnable() { public void run() {
    addTab("Aide", "", aide); 
  }};
  private Runnable nothing = new Runnable() { public void run() {
    getParent().setSize(800,600);
  }};
  private void fileTools() {
    addTool("Ouvrir", "org/javascool/doc-files/icones16/open.png", openFile);
    addTool("Sauver", "org/javascool/doc-files/icones16/save.png", saveFile);
    addToolSeparator();
    addTool("Aide", "org/javascool/doc-files/icones16/help.png", showHelp);
    addToolSeparator();
  }
  // Defines the key-stroke
  {
    getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_MASK), "open");
    getRootPane().getActionMap().put("open",  new AbstractAction("open") {
	private static final long serialVersionUID = 1L;
	public void actionPerformed(ActionEvent e) { 
	  for(Object action : getRootPane().getActionMap().allKeys()) System.err.println("> A = "+action);
	  openFile.run();
	}});
    getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_MASK), "save");
    getRootPane().getActionMap().put("save",  new AbstractAction("save") {
	private static final long serialVersionUID = 1L;
	public void actionPerformed(ActionEvent e) { 
	  saveFile.run();
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
	  System.err.println("Bye guy !!!!");
	  // PREVOIR LES SAUVEGARDES ICI , , ,
	  getRootPane().getActionMap().get("exit").actionPerformed(e);
	}});
  }
  /** This is the runnable called when the ^L keystroke is called. */
  private Runnable validate = null;
  
  // [3] Defined activities.

  private Activity jvsActivity = new Activity() {
      public String getTitle() { return "Démonstration de l'éditeur Jvs"; }
      public void init(Main main) {
      editor = new JvsSourceEditor();
	main.addTab("Jvs Editor", "", (JPanel) editor);
	main.addTab("Console", "", Jvs2Java.getPanel("ingredients"));
	addTool("Compile", "org/javascool/doc-files/icones16/compil.png", validate = cmpJvs);
      }
	};
  private Runnable cmpJvs = new Runnable() { public void run() {
    if(verisave == true)
      Utils.saveString(file.getPath(), activity.getText());
    delTool("Exécuter");
    delTool("Arrêter");
    showTab("Console");
    Jvs2Java.translate(file.getPath());
    String out = Jvs2Java.compile(file.getPath());
    System.out.println(out.length() == 0 ? "Compilation réussie !" : out);
    proglet.ingredients.Console.printHtml("<hr>\n");
    if (out.length() == 0) {
      addTool("Exécuter", "org/javascool/doc-files/icones16/play.png", runJvs);
      addTool("Arrêter", "org/javascool/doc-files/icones16/stop.png", stpJvs);
    }
  }};
   private Runnable runJvs = new Runnable() { public void run() {
     Jvs2Java.load(file.getPath());
     Jvs2Java.run(true);
  }};
   private Runnable stpJvs = new Runnable() { public void run() {
     Jvs2Java.run(false);
  }};
    private Activity tortueActivity = new Activity() {
      public String getTitle() { return "Tortue"; }
      public void init(Main main) {
      editor = new JvsSourceEditor();
      ((JvsSourceEditor)editor).setProglet("tortuelogo");
	main.addTab("Jvs Editor", "",(JPanel) editor);
	main.addTab("Console", "", Jvs2Java.getPanel("tortuelogo"));
	addTool("Compile", "org/javascool/doc-files/icones16/compil.png", cmpJvs);
      }
    };

  private Activity pmlActivity = new Activity() {
      public String getTitle() { return "Démonstration de l'éditeur Pml"; }
      public void init(Main main) {
      editor = new SourceEditor();
	  main.addTab("Pml Editor", "",(JPanel) editor);
      }
    };

  private Activity algActivity = new Activity() {
      public String getTitle() { return "Démonstration de l'éditeur d'Algo"; }
      public void init(Main main) {
      editor=new AlgoEditor();
	main.addTab("Algo Editor", "",(JPanel) editor);
	main.addTab("Console", "", Jvs2Java.getPanel("ingredients"));
      }
    };
  private static class JFileFilter extends FileFilter {
    private ArrayList<String> exts = new ArrayList<String>();
    private String description;
    public void addType(String s) {
      exts.add(s);
    }
    public boolean accept(File f) {
      if(f.isDirectory()) {
	return true;
      } else if(f.isFile()){
	Iterator it = exts.iterator();
	while(it.hasNext()) 
	  if(f.getName().endsWith((String)it.next()))
	    return true;
      }
      return false;
    }
    public String getDescription() {
      return description;
    }
    public void setDescription(String s) {
      description = s;
    }
  }




  /** Used to run a javasccol v3 as a standalone program. 
   * <p>- Starts a JavaScool "activity" which result is to be stored in a "file-name".</p>
   * @param usage <tt>java org.javascool.Main [activity [file-name]]</tt><ul>
   * <li><tt>activity</tt> specifies the activity to be done.</li>
   * <li><tt>file-name</tt> specifies the file used for the activity.</li>
   * </ul>
   */
  public static void main(String[] usage) {
    System.out.println("Hi ! V3 is comming :-)");
    Main main = new Main();
    if (usage.length >= 1) main.setActivity(usage[0]);
    if (usage.length >= 2) main.setFile(usage[1]);
    Utils.show(main, "Java'Scool v3.0");
  }
}
