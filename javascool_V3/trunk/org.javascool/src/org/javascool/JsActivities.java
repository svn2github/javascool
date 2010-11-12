/********************************************************************************
      ______________________________________________
     | By Philippe Vienne <philoumailabo@gmail.com> |
     | Distributed on GNU General Public Licence    |
     | © 2010 INRIA, All rights reserved            |
     |______________________________________________|

 ********************************************************************************/

package org.javascool;

// Proglets used for Java activities
import proglet.ingredients.Console;
import proglet.exosdemaths.CurveDisplay;

// Used fot the activity chooser
import javax.swing.JList;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

// Used to store the activities
import java.util.ArrayList;

/** This factory defines the different JavaScool activities. */
public class JsActivities {

  // [0] Activity interface

  /** Defines a JavaScool interactive activity. */
  public interface Activity {
    /** Returns the activity title. */
    public String getTitle();
    /** Initializes the activity, adding buttons and pannels and start it. */
    public void init(JsFrame frame);
    /** Stops the activity. */
    public void stop();
    /** Returns the activity editor. */
    public Editor getEditor();
    /** Returns the required file extension. 
     * e.g., ".jvs" or ".pml"
     */
    public String getExtension();
  }
  
  // [1] Activity table

  static private ArrayList<Activity> activities = new ArrayList<Activity>();
 
  /** Gets the activity titles. */
  public static String[] getTitles() {
    String titles[] = new String[activities.size()];
    int n = 0; for(Activity activity : activities) titles[n++] = activity.getTitle();
    return titles;
  }
  
  /** Gets an activity by its title. */
  public static Activity getActivity(String name) {
    for(Activity activity : activities) 
      if (activity.getTitle().equals(name))
	return activity;
    return null;
  }

  /** Gets an activity by its index. */
  public static Activity getActivity(int index) {
    return activities.get(index);
  }

  // [2] Registered activities
  static {

    // This is the "home" activity allowing to choose other activities
    activities.add(new HomeActivity());
  }

  /** Defines the "home" activity allowing the user to select an activity. */
  private static class HomeActivity implements Activity {
    public void init(JsFrame f) {
      this.frame = f;
      list = new JList(getTitles());
      list.setSelectedIndex(0);
      list.addListSelectionListener(new ListSelectionListener() {
	  public void valueChanged(ListSelectionEvent e) {
	    frame.setActivity((String) list.getSelectedValue());
	  }});
      frame.addTab("Choisir son activité", list, "org/javascool/doc-files/icones16/new.png", false);
    }
    private JsFrame frame;
    private JList list;
    // The "home" activity is recognized by JsFrame as being the only one with an empty name.
    public String getTitle() { return ""; }
    public Editor getEditor() { return null; }
    public String getExtension() { return null; }
    public void stop() { }
  }
  
  /** Defines a compilation activity. */
  private abstract class JavaActivity implements Activity {
    JsFrame frame; public JavaActivity(JsFrame frame) { this.frame = frame; }
    // Compilation/execution mechanism
    protected void initCompile() {
      frame.addTab("Console", Jvs2Java.getPanel("ingredients"), "org/javascool/doc-files/icones16/compile.png", true);
      ////////////frame.addTool("Compiler", "org/javascool/doc-files/icones16/compile.png", validate = compile);
    }
    /*
    private Runnable compile = new Runnable() { public void run() {
      frame.delTool("Exécuter");
      frame.delTool("Arrêter");
      frame.showTab("Console");
      Console.clear();
      if (getEditor().getText().trim().length() > 0) {
	frame.fileChooser.doSave(getEditor(), getExtension());
	if (frame.fileChooser.getFile() != null) {
	  if (getEditor() instanceof AlgoEditor) {
	    frame.fileChooser.doSave(algoViewer, Jvs2Java.reformat(((AlgoEditor) getEditor()).getJavaSource()), ".jvs");
	  }
	  Jvs2Java.translate(frame.fileChooser.getFile());
	  String out = Jvs2Java.compile(frame.fileChooser.getFile());
	  System.out.println(out.length() == 0 ? "Compilation réussie !" : out);
	  Console.printHtml("<hr>\n");
	  if (out.length() == 0) {
	    frame.addTool("Exécuter", "org/javascool/doc-files/icones16/play.png", execute);
	    frame.addTool("Arrêter", "org/javascool/doc-files/icones16/stop.png", stop);
	  }
	} else {
	  System.out.println("Impossible de compiler: le fichier n'est pas sauvegardé !");
	}
      }  else {
	System.out.println("Rien à compiler: il n'y pas de code dans l'éditeur !");
      }
    }};
    */
    private Runnable execute = new Runnable() { public void run() {
      Console.clear();
      CurveDisplay.scopeReset();
      //////////////Jvs2Java.load(frame.fileChooser.getFile());
      Jvs2Java.run(true);
    }};
    private Runnable stop = new Runnable() { public void run() {
      Jvs2Java.run(false);
    }};
    public void stop() { }
  }
}
