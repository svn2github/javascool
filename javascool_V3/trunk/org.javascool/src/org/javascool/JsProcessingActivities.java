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

/** This factory defines the different JavaScool v3-2 processing activities.
 * @see <a href="JsProgletActivities.java.html">source code</a>
 * @serial exclude
 */
public class JsProcessingActivities {
  /**/private JsProcessingActivities () { }

  /** Adds all processing activities. */
  public static void addActivities(JsMain main) {
    main.addActivity(new ProcessingActivity("ExplorationSonore", 800, 600) {
	public String getTitle() { return "Exploration du signal sonore"; }
      });
    main.addActivity(new ProcessingActivity("CryptageRSA", 1024, 700) {
	public String getTitle() { return "Expérimenter avec la cryptographie"; }
      });
    main.addActivity(new ProcessingActivity("EnVoiture", 1200, 700) {
	public String getTitle() { return "Découvrir les graphes et les chemins"; }
      });
    main.addActivity(new ProcessingActivity("UnGrapheDesChemins", 1024, 700) {
	public String getTitle() { return "Explorer les graphes et les chemins"; }
      });
    main.addActivity(new ProcessingActivity("BoiteAMusique", 1300, 750) {
	public String getTitle() { return "Programmer une séquence musicale"; }
      });
  }

  // Defines a processing activity
  private static abstract class ProcessingActivity extends JsProgletActivities.JavaActivity {
    /** Constructs a processing activity.
     * @param processing The processing to use.
     * @throws IllegalArgumentExceptionif the processing is undefined.
     */
    public ProcessingActivity(String n, int w, int h) { name = n; width = w + 20; height = h + 20; } private String name; private int width, height;
    public void init(JsMain main) {
      main.getFrame().addTab("Editeur", (JPanel) jvsEditor, "org/javascool/doc-files/icones16/edit.png", false, true); 
      jvsEditor.setProglet(name);
      init1(main);
      main.getFrame().addTab("Document de la proglet", "sketchbook/"+name+"/about-proglet.htm", "org/javascool/doc-files/icones16/help.png", true, true);
      init2(main);
    }
    private void init2(JsMain main) {
      PrintStream out = System.out;
      System.setOut(System.err);
      try {
	final boolean popup = false;
	applet = (Applet) Class.forName(name).newInstance();
	if (popup) {
	  jframe = Utils.show(applet, name, Utils.getIcon("org/javascool/doc-files/icones16/compile.png"), width, height, false);
	  jframe.setResizable(false);
	} else {
	  JPanel pane = new JPanel();
	  applet.init(); applet.start();
	  applet.setMinimumSize(new Dimension(width, height));
	  applet.setMaximumSize(new Dimension(width, height));
	  pane.add(applet);
	  main.getFrame().addTab(name, pane, "org/javascool/doc-files/icones16/edit.png", true, false);
	}
	Macros.sleep(1000);
	System.setOut(out);
      } catch(Throwable e) { 
	System.setOut(out);
	System.out.println("Désolé, l'activité "+name+" n'est pas définie dans cette version ("+e+")."); 
      }
    } 
    private Applet applet = null;
    public void stop() { 
      applet.stop(); applet.destroy();
      if (jframe != null) { jframe.dispose(); jframe = null; } 
    }
    private JFrame jframe = null;
  }
}
