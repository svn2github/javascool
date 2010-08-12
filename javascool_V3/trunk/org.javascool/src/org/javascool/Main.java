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
import javax.swing.JButton;
import java.awt.event.ActionListener; 
import java.awt.event.ActionEvent; 
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import java.io.File;

// Used to register elements
import java.util.HashMap;

/** This is the javascool v3 interface starter.
 * <p>- It can be used either as standalone application or a certified applet.</p>
 * @author Philippe Vienne <philoumailabo@gmail.com>
 * @see <a href="Main.java">source code</a>
 */
public class Main extends JApplet { /**/public Main() { }
  private static final long serialVersionUID = 1L;

  // [1] Defines the main panel and defines how to edit the toolbar, actList and tabbedpane
  JToolBar tools = new JToolBar();
  JTabbedPane tabbedPane = new JTabbedPane();
  JComboBox actList = new JComboBox();
  /**/public void init() {
    JPanel toppane = new JPanel();
    toppane.setLayout(new BorderLayout());
    toppane.add(tools, BorderLayout.WEST);
    toppane.add(actList, BorderLayout.EAST);  
    actList.addActionListener(alistener);
    getContentPane().add(toppane, BorderLayout.NORTH);
    getContentPane().add(tabbedPane, BorderLayout.CENTER);
    // Adds buttons and activities using generic routines
    basicTools();
    addActivity(pmlActivity);
    addActivity(jvsActivity);
    addActivity(algActivity);
    // Initializes the activity from the HTML tag or proposes a default activity
    try { setActivity(getParameter("activity")); } catch(Exception e) { setActivity(""); }
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
    buttons.put(label, button);
    actions.put(label, action);
  }
  /** Removes a button from the tool bar. */
  public void delTool(String label) {
    if (buttons.containsKey(label)) {
      tools.remove(buttons.get(label));
      buttons.remove(label);
      actions.remove(label);
    }
  }
  /** Adds a separator on the toolbar. */
  public void addToolSeparator() {
    tools.addSeparator();
  }
  private HashMap<String,Runnable> actions = new HashMap<String,Runnable>();
  private HashMap<String,JButton> buttons = new HashMap<String,JButton>();
  /** Adds a tab to the tabbed panel.
   * @param label Tab label.
   * @param icon Tab icon.
   * @param pane Tab panel or Html text.
   */
  public void addTab(String label, String icon, JPanel pane) {
    tabbedPane.addTab(label, icon == null ? null : Utils.getIcon(icon), pane, label);
  }
  /**/public void addTab(String label, String icon, String text) {
    addTab(label, icon, new HtmlDisplay().reset(text));
  }
  /** Removes a tab from the tabbed panel.
   * @param label Tab label.
   * @param pane Tab panel or Html text.
   */
  public void delTab(String label) {
    if (tabs.containsKey(label)) {
      tabbedPane.remove(tabs.get(label));
      tabs.remove(label);
    }
  }
  private HashMap<String,JPanel> tabs = new HashMap<String,JPanel>();
  /** Defines an interactive activity. */
  public static class Activity {
    /** Returns the activity title. */
    public String getTitle() { return ""; }
    /** Initializes the activity, adding buttons and pannels. */
    public void init(Main main) { }
  }
  /** Adds an activity tab to the tabbed panel. 
   * @param activity Adds a predefined activity.
   */
  public void addActivity(Activity activity) {
    activities.put(activity.getTitle(), activity);
    actList.addItem(activity.getTitle());
  }
  // Install the activity
  private void setActivity(String name) {
    Activity activity = activities.get(name);
    if (activity == null) activity = activities.get("Démonstration de l'éditeur Pml");
    tools.removeAll();
    tabbedPane.removeAll();
    basicTools();
    activity.init(this);
    tools.revalidate();
    tabbedPane.revalidate();
  }
  private HashMap<String,Activity> activities = new HashMap<String,Activity>();
  // Generic action listener for all actions
  private ActionListener alistener = new ActionListener() {
      public void actionPerformed(ActionEvent e) {
	if (e.getSource() == actList) {
	  setActivity((String) ((JComboBox) e.getSource()).getSelectedItem());
	} else {
	  actions.get(((JButton) e.getSource()).getText()).run();
	}
      }
    };

  // [2] File new/open/save management
  // - gerer les multi fichiers et le filtre des extensions
  private SourceEditor se = new SourceEditor();
  private JFileChooser fc = new JFileChooser();
  private String fcTitle = null;
  private File file = null;
  public void setFile(String filename) {
    try { se.setText(Utils.loadString((file = new File(filename)).getPath())); } catch(Exception e) { }
  }
  private Runnable newFile = new Runnable() { public void run() {
    file = null;
    se.setText("");
  }};
  private Runnable openFile = new Runnable() { public void run() {
    fc.setDialogTitle("Ouvrir un programme");
    fc.setDialogType(JFileChooser.OPEN_DIALOG);
    fc.setApproveButtonText("Ouvrir");
    if (fc.showOpenDialog(Main.this) == 0) {
      file = fc.getSelectedFile();
      se.setText(Utils.loadString(file.getPath()));
    }
  }};
  private Runnable saveFile = new Runnable() { public void run() {
    if(file == null) {
      fc.setDialogTitle(fcTitle == null ? "Enregister un programme" : fcTitle);
      fc.setDialogType(JFileChooser.SAVE_DIALOG);
      fc.setApproveButtonText("Enregister");
      if (fc.showSaveDialog(Main.this) == 0) {
	file = fc.getSelectedFile();
	Utils.saveString(file.getPath(), se.getText());
      } 
    } else {
      Utils.saveString(file.getPath(), se.getText());
    }
  }};
  private void pleaseSaveFile() {
    if(se.getText().length() > 0)
      fcTitle = "Enregistrer votre fichier avant de passer à la suite";
    saveFile.run();
    fcTitle = null;
  }
  private Runnable showHelp = new Runnable() { public void run() {
    addTab("Aide", "", new HtmlDisplay().
	   reset("<html><head><title>Ma page</title></head><body><h1>Titre</h1>Bienvenu dans l'aide de Java's cool :-)</body></html>")); 
  }};
  private Runnable nothing = new Runnable() { public void run() {
  }};
  private void basicTools() {
    addTool("", "org/javascool/doc-files/icones16/new.png", newFile);
    addTool("Ouvrir", "org/javascool/doc-files/icones16/open.png", openFile);
    addTool("Sauver", "org/javascool/doc-files/icones16/save.png", saveFile);
    addToolSeparator();
    addTool("Compiler", "org/javascool/doc-files/icones16/compil.png", nothing);
    addTool("Exécuter", "org/javascool/doc-files/icones16/play.png", nothing);
    addTool("Aide", "org/javascool/doc-files/icones16/help.png", showHelp);
  }

  // [3] Basic activities.
  private Activity pmlActivity = new Activity() {
      public String getTitle() { return "Démonstration de l'éditeur Pml"; }
      public void init(Main main) {
	main.addTab("Pml Editor", "", se);
      }
    };
  private Activity jvsActivity = new Activity() {
      public String getTitle() { return "Démonstration de l'éditeur Jvs"; }
      public void init(Main main) {
	main.addTab("Jvs Editor", "", se);
	main.addTab("Console", "", Jvs2Java.getPanel("ingredients"));
      }
    };
  private Activity algActivity = new Activity() {
      public String getTitle() { return "Démonstration de l'éditeur d'Algo"; }
      public void init(Main main) {
	main.addTab("Jvs Editor", "", new AlgoTree());
	main.addTab("Console", "", Jvs2Java.getPanel("ingredients"));
      }
    };

  /** Used to run a javasccol v3 as a standalone program. 
   * <p>- Using javascool means: doing an "activity" which result is to be stored in a "file-name".</p>
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
