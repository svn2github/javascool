/********************************************************************************
*      ______________________________________________
| By Philippe Vienne <philoumailabo@gmail.com> |
| Distributed on GNU General Public Licence    |
| © 2010 INRIA, All rights reserved            |
|||______________________________________________|
|
********************************************************************************/

package org.javascool;

import javax.swing.JApplet;

// Used to set Win look and feel
import javax.swing.UIManager;

// Used to manage keystroke
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

// Used for the about dialogs
import javax.swing.JOptionPane;
import javax.swing.JLabel;

// Used to store the activities
import java.util.ArrayList;
import java.util.LinkedHashMap;

// Used for the JsHome activity chooser
import javax.swing.JPanel;
import java.awt.Color;
import javax.swing.BoxLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

// Used to test if a file exists in main()
import java.io.File;

// Used to show a text in a frame
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;

/** Defines the JavaScool v3-2 launcher.
 * JavaScool 3.2 graphic user interface components: <ul>
 * <li>The frame with buttons and panels is defined in <a href="JsFrame.html">JsFrame</a>.</li>
 * <li>The file load/save namagement defined in <a href="JsFileChooser.html">JsFileChooser</a>.</li>
 * <li>The activities are defined in:<ul>
 * <li><a href="JsProgletActivities.html">JsProgletActivities</a> and</li>
 * <li><a href="JsProcessingActivities.html">JsProcessingActivities</a>.</li>
 * </ul></li></ul>
 * @author Philippe Vienne <philoumailabo@gmail.com>
 * @see <a href="JsMain.java.html">source code</a>
 * @serial exclude
 */
public class JsMain extends JApplet {
  /**/public JsMain() {}
  private static final long serialVersionUID = 1L;

  static final String title = "Java'Scool 3.2bis";

  // [0] Defines the look and field.
  static {
    String os = System.getProperty("os.name");
    if(os.startsWith("Windows")) {
      try { UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
      } catch(Exception e) {}
    } else {
      try { System.setProperty("com.apple.mrj.application.apple.menu.about.name", title);
      } catch(Exception e) {}
      try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      } catch(Exception e) {
        System.err.println("Note: Utilisaton du thème Java (et non du système)");
      }
    }
  }

  // [1] Builds the JavaSccol standard G.U.I.
  private Runnable newActivity = new Runnable() {
    public void run() {
      if(fileChooser.doSaveCloseAs(activity.getEditor(), activity.getExtension())) {
        fileChooser.resetFile();
        setActivity("");
      }
    }
  };
  private Runnable fileOpen = new Runnable() {
    public void run() {
      fileChooser.doSaveOpenAs(activity.getEditor(), activity.getExtension());
    }
  };
  private Runnable fileSave = new Runnable() {
    public void run() {
      fileChooser.doSaveAs(activity.getEditor(), activity.getExtension());
    }
  };
  private Runnable fileLock = new Runnable() {
    public void run() {
      fileChooser.doLockUnlockAs(activity.getEditor(), activity.getExtension());
    }
  };
  private Runnable helpShow = new Runnable() {
    boolean on = false;
    public void run() {
      if(on = !on)
        jsFrame.addTab("Aide", "org/javascool/doc-files/about-main.htm", "org/javascool/doc-files/icones16/help.png", false, true);
      else
        jsFrame.delTab("Aide");
    }
  };

  /** Sets an activity.
   * @param name The name of the activity to set.
   */
  private void setActivity(String name) {
    jsFrame.reset("org/javascool/doc-files/icones32/logo_jvs.gif");
    activity = getActivity(name);
    if(activity != null) {
      if(activity.getTitle().length() > 0) {
        jsFrame.addTool("Nouvelle Activité", "org/javascool/doc-files/icones16/new.png", newActivity);
        jsFrame.addTool("Ouvrir", "org/javascool/doc-files/icones16/open.png", fileOpen);
        jsFrame.addTool("Sauver", "org/javascool/doc-files/icones16/save.png", fileSave);
        jsFrame.addToolSeparator();
      }
      jsFrame.addTool("Aide", "org/javascool/doc-files/icones16/help.png", helpShow);
      jsFrame.addToolSeparator();
      activity.init(this);
    }
  }
  private Activity activity;

  /** Returns the frame layout. */
  public JsFrame getFrame() {
    return jsFrame;
  }
  private JsFrame jsFrame = new JsFrame();

  /** Returns the frame file chooser. */
  public JsFileChooser getFileChooser() {
    return fileChooser;
  }
  private JsFileChooser fileChooser = new JsFileChooser(this);

  {
    getContentPane().add(jsFrame);
  }

  // [1.1] Defines the key-strokes
  {
    getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_MASK), "new");
    getRootPane().getActionMap().put("open", new AbstractAction("new") {
                                       private static final long serialVersionUID = 1L;
                                       public void actionPerformed(ActionEvent e) {
                                         newActivity.run();
                                       }
                                     }
                                     );
    getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_MASK), "open");
    getRootPane().getActionMap().put("open", new AbstractAction("open") {
                                       private static final long serialVersionUID = 1L;
                                       public void actionPerformed(ActionEvent e) {
                                         fileOpen.run();
                                       }
                                     }
                                     );
    getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_MASK), "save");
    getRootPane().getActionMap().put("save", new AbstractAction("save") {
                                       private static final long serialVersionUID = 1L;
                                       public void actionPerformed(ActionEvent e) {
                                         fileSave.run();
                                       }
                                     }
                                     );
    getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_E, KeyEvent.CTRL_MASK), "lock");
    getRootPane().getActionMap().put("lock", new AbstractAction("lock") {
                                       private static final long serialVersionUID = 1L;
                                       public void actionPerformed(ActionEvent e) {
                                         fileLock.run();
                                       }
                                     }
                                     );
    getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_H, KeyEvent.CTRL_MASK), "help");
    getRootPane().getActionMap().put("help", new AbstractAction("help") {
                                       private static final long serialVersionUID = 1L;
                                       public void actionPerformed(ActionEvent e) {
                                         helpShow.run();
                                       }
                                     }
                                     );
    getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_Q, KeyEvent.CTRL_MASK), "quit");
    getRootPane().getActionMap().put("quit", new AbstractAction("quit") {
                                       private static final long serialVersionUID = 1L;
                                       public void actionPerformed(ActionEvent e) {
                                         if(fileChooser.doSaveCloseAs(activity.getEditor(), activity.getExtension()))
                                           activity = null;
                                         Utils.unshow(JsMain.this);
                                       }
                                     }
                                     );
    getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_I, KeyEvent.CTRL_MASK), "info");
    getRootPane().getActionMap().put("info", new AbstractAction("info") {
                                       private static final long serialVersionUID = 1L;
                                       public void actionPerformed(ActionEvent e) {
                                         JOptionPane.showMessageDialog(JsMain.this, new JLabel("<html><body><h4><hr>" +
                                                                                               Utils.loadString("org/javascool/js-manifest.mf").
                                                                                               replaceFirst("Summary", "Nom").
                                                                                               replaceFirst("Manifest-version", "Version").
                                                                                               replaceFirst("Created-By", "Créé-Par").
                                                                                               replaceFirst("Implementation-URL", "Site-web").
                                                                                               replaceFirst("©", "\nLicence: ©").
                                                                                               replaceAll("(Implementation-(Vendor|Version)|Main-Class).*\n", "").
                                                                                               replaceAll(":", " :").
                                                                                               replaceAll("\n", "<hr>") +
                                                                                               "</h4></body></html>",
                                                                                               Utils.getIcon("org/javascool/doc-files/icones32/logo_jvs.gif"),
                                                                                               JLabel.CENTER),
                                                                       "About " + title + "...", JOptionPane.INFORMATION_MESSAGE);
                                       }
                                     }
                                     );
  }

  // [2] Activity interface

  /** Defines a JavaScool interactive activity. */
  public interface Activity {
    /** Returns the activity category.
     * @return The string type as a non empty string or null if the activity is undefined in this context.
     */
    public String getType();
    /** Returns the activity title. */
    public String getTitle();
    /** Initializes the activity, adding buttons and pannels and start it. */
    public void init(JsMain main);
    /** Stops the activity. */
    public void stop(JsMain main);
    /** Returns the activity editor. */
    public Editor getEditor();
    /** Returns the required file extension.
     * e.g., ".jvs" or ".pml"
     */
    public String getExtension();
  }

  // [2.1] Activity table

  /** Gets an activity by its title. */
  private Activity getActivity(String name) {
    name = name.trim();
    if(name.matches("[0-9]+")) {
      int i = Integer.parseInt(name);
      if((0 <= i) && (i < activities.size()))
        return activities.get(i);
    }
    for(Activity activity : activities)
      if(activity.getTitle().equals(name))
        return activity;
    return activities.get(0);
  }
  /** Adds an activity to the application list. */
  public void addActivity(Activity activity) {
    if (activity.getType() != null) {
      activities.add(activity);
      if (activity.getType().length() > 0) {
	if (!types.containsKey(activity.getType()))
	  types.put(activity.getType(), new ArrayList<String>());
	types.get(activity.getType()).add(activity.getTitle());
      }
    }
  }
  // Register all activities
  {
    types.put("Apprendre à programmer", new ArrayList<String>());
    addActivity(new HomeActivity());
    JsProcessingActivities.addActivities(this);
    JsProgletActivities.addActivities(this);
    setActivity("");
  }
  static private ArrayList<Activity> activities = new ArrayList<Activity>();
  static private LinkedHashMap<String, ArrayList<String>> types = new LinkedHashMap<String, ArrayList<String>>();

  // [2.2] JsHome activity (activity chooser)

  /** Defines the "home" activity allowing the user to select an activity. */
  private static class HomeActivity implements Activity {
    public void init(JsMain main) {
      this.main = main;
      JPanel panel = new JPanel();
      panel.setBackground(new Color(20,255,20));
      panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.yellow, 4), BorderFactory.createEmptyBorder(30, 50, 0, 0)));
      panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
      for(String type : types.keySet()) {
	JLabel label = new JLabel(" " +type+" :");
	label.setAlignmentX(JPanel.LEFT_ALIGNMENT);
	panel.add(label);
	for(String title : types.get(type)) {
	  JButton button = new JButton(title, Utils.getIcon("org/javascool/doc-files/icones16/compile.png"));
	  button.setAlignmentX(JPanel.LEFT_ALIGNMENT);
	  button.setContentAreaFilled(false);
	  button.setRolloverEnabled(true);
	  button.setRolloverIcon(Utils.getIcon("org/javascool/doc-files/icones16/open.png"));
	  button.addActionListener(listener);
	  panel.add(button);
	}
      }
      this.main.getFrame().addTab("Choisir (cliquer sur le titre) son activité", panel, "org/javascool/doc-files/icones16/new.png", false, true);
    }
    ActionListener listener = new ActionListener() {
	public void actionPerformed(ActionEvent e) {
	  main.setActivity(((JButton) e.getSource()).getText());
	}
      };
    private JsMain main;
    // The "home" activity is recognized by JsFrame as being the only one with an empty name.
    public String getTitle() {
      return "";
    }
    public String getType() {
      return "";
    }
    public Editor getEditor() {
      return null;
    }
    public String getExtension() {
      return null;
    }
    public void stop(JsMain main) {}
  }

  // Sets the javascool version from the manifest file
  private static void setJavascoolVersion() {
    try { 
      System.setProperty("javascool.version", Utils.loadString("org/javascool/js-manifest.mf").replaceFirst("(.|\n)*Manifest-version:(.*)(.|\n)*", "$2")); 
    } catch(Throwable z) { }
  }

  /** Used to run a JavaScool 3.2 as a standalone program.
   * <p>- Starts a JavaScool "activity" which result is to be stored in a "file-name".</p>
   * @param usage <tt>java org.javascool.Main [activity [file-name]]</tt><ul>
   * <li><tt>activity</tt> specifies the activity to be done (its index or name).</li>
   * <li><tt>file-name</tt> specifies the file used for the activity.</li>
   * </ul>
   */
  public static void main(String[] usage) {
    System.out.println("---------------------\n" + title + "\n---------------------\nstarting..");
    setJavascoolVersion();
    Utils.setUncaughtExceptionAlert("Problème de configuration détecté!", "Oh: il y a un problème de compatibilité avec "+title+"!\n\nPour vous aider:\n -1- copier tout ce message et \n -2- envoyer le à science-participative@sophia.inria.fr :\n -3- nous essayerons de vous dépanner au plus vite.\n");
    JsMain main = new JsMain();
    if(usage.length > 0)
      main.setActivity(usage[0]);
    if((usage.length > 0) && new File(usage[usage.length - 1]).exists() && (main.activity.getEditor() != null))
      main.getFileChooser().doOpen(main.activity.getEditor(), usage[usage.length - 1]);
    Utils.show(main, title, Utils.getIcon("org/javascool/doc-files/icones32/logo_jvs.gif"), false);
  }
}
