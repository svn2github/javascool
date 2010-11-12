/********************************************************************************
      ______________________________________________
     | By Philippe Vienne <philoumailabo@gmail.com> |
     | Distributed on GNU General Public Licence    |
     | © 2010 INRIA, All rights reserved            |
     |______________________________________________|

 ********************************************************************************/

package org.javascool;

// Used to set Win look and feel
import javax.swing.UIManager;

public class JsFrame extends JsLayout { 
  /**/public JsFrame() { }
  private static final long serialVersionUID = 1L;
  
  static final String title = "Java'Scool 3.1";

  // [0] Defines the look and field.
  static {
    String os = System.getProperty("os.name");
    if (os.startsWith("Windows")) {
      try { UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel"); } catch(Exception e) { }
    } else {
      try { System.setProperty("com.apple.mrj.application.apple.menu.about.name", title); } catch(Exception e) { }
      try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch(Exception e) { System.err.println("Note: Utilisaton du thème Java (et non du système)"); }
    } 
  }

  // [1] Builds the JavaSccol standard G.U.I.

  /** Sets an activity.
   * @param activity The name of the activity to set.
   */
  public void setActivity(String name) {
    reset("org/javascool/doc-files/icones32/logo_jvs.gif");
    activity = JsActivities.getActivity(name);
    if (activity != null) {
      if (activity.getTitle().length() > 0) {
	addTool("Nouvelle Activité", "org/javascool/doc-files/icones16/new.png", newActivity);
	addTool("Ouvrir", "org/javascool/doc-files/icones16/open.png", fileOpen);
	addTool("Sauver", "org/javascool/doc-files/icones16/save.png", fileSave);
	fileChooser.resetFile();
	addToolSeparator();
      }
      addTool("Aide", "org/javascool/doc-files/icones16/help.png", helpShow);
      addToolSeparator();
      activity.init(this);
    }
  }
  private JsActivities.Activity activity;

  private Runnable newActivity = new Runnable() { public void run() {
    setActivity("");
  }};

  private JsFileChooser fileChooser = new JsFileChooser(this);

  private Runnable fileOpen = new Runnable() { public void run() {
    if (activity == null) return;
    fileChooser.doSaveOpenAs(activity.getEditor(), activity.getExtension());
   }};

  private Runnable fileSave = new Runnable() { public void run() {
    if (activity == null) return;
    fileChooser.doSaveAs(activity.getEditor(), activity.getExtension());
  }};
  
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
  }
  private boolean helpOn = false;
  private String helpFile = "org/javascool/doc-files/about-main.htm";
  };
  
  {
    setActivity("");
  }

  public static void main(String[] usage) {
    JsFrame main = new JsFrame();
    Utils.show(main, title, Utils.getIcon("org/javascool/doc-files/icones32/logo_jvs.gif"), true);
  }
}
