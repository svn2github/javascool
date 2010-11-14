/*******************************************************************************
 * Thierry.Vieville@sophia.inria.fr, Copyright (C) 2010.  All rights reserved. *
 *******************************************************************************/

package org.javascool;

// Used for processing  activity
import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.Dimension;
import java.io.PrintStream;
import java.applet.Applet;

/** This factory defines the different JavaScool processing activities. */
public class JsProcessingActivities {

  /** Adds all processing activities. */
  public static void addActivities() {
    JsActivities.addActivity(new ProcessingActivity("ExplorationSonore") {
	public String getTitle() { return "Exploration du signal sonore"; }
	public void init(JsFrame frame) {
	  super.init(frame);
	}
      });
    JsActivities.addActivity(new ProcessingActivity("CryptageRSA") {
	public String getTitle() { return "Expérimenter avec la cryptographie"; }
	public void init(JsFrame frame) {
	  super.init(frame);
	}
      });
    JsActivities.addActivity(new ProcessingActivity("EnVoiture") {
	public String getTitle() { return "Découvrir les graphes et les chemins"; }
	public void init(JsFrame frame) {
	  super.init(frame);
	}
      });
    JsActivities.addActivity(new ProcessingActivity("UnGrapheDesChemins") {
	  public String getTitle() { return "Explorer les graphes et les chemins"; }
	public void init(JsFrame frame) {
	  super.init(frame);
	}
      });
  }

  // Defines a processing activity
  private static abstract class ProcessingActivity extends JsActivities.JavaActivity {
    private JsFrame frame;
    public Editor getEditor() { return JsActivities.jvsEditor; }
    public String getExtension() { return ".jvs"; }
    /** Constructs a processing activity.
     * @param processing The processing to use.
     * @throws IllegalArgumentExceptionif the processing is undefined.
     */
    public ProcessingActivity(String processing) { name = processing; } private String name; private int width, height;
    public void init(JsFrame f) {
      frame = f;
      if (JsActivities.jvsEditor == null) JsActivities.jvsEditor = new JvsSourceEditor(); 
      frame.getFrame().addTab("Editeur", (JPanel) JsActivities.jvsEditor, "org/javascool/doc-files/icones16/edit.png", false); 
      JsActivities.jvsEditor.setProglet(name);
      frame.getFrame().addTab("Document de la proglet", "sketchbook/"+name+"/about-proglet.htm", "org/javascool/doc-files/icones16/help.png", true);
      initCompile();
      initApplet();
    }
    private void initApplet() {
      PrintStream out = System.out;
      System.setOut(System.err);
      try {
	final boolean popup = true;
	applet = (Applet) Class.forName(name).newInstance();
	try { width = (Integer) Class.forName(name).getDeclaredField("WIDTH").get(null); } catch(Throwable e) { System.err.println("Width undefined !"); width = 800; }
	try { height = 20 + (Integer) Class.forName(name).getDeclaredField("HEIGHT").get(null); } catch(Throwable e) { System.err.println("Height undefined !"); height = 600; }
	if (popup) {
	  jframe = Utils.show(applet, name, Utils.getIcon("org/javascool/doc-files/icones16/compile.png"), width, height, false);
	  jframe.setResizable(false);
	  Macros.sleep(1000);
	} else {
	  applet.init(); applet.start();
	  Macros.sleep(1000);
	  applet.setMinimumSize(new Dimension(width, height));
	  frame.getFrame().addTab(name, applet, "org/javascool/doc-files/icones16/edit.png", false);
	}
      } catch(Throwable e) { System.out.println("Désolé, l'activité "+name+" n'est pas définie dans cette version ("+e+")."); }
      System.setOut(out);
    } 
    private Applet applet = null;
    public void stop() { 
      if (jframe != null) { jframe.dispose(); jframe = null; } 
    }
    private JFrame jframe = null;
  }
}
