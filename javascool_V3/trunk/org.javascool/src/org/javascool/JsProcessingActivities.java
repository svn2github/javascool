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

  /** Adds all processing activities. */
  public static void addActivities(JsMain main) {
    main.addActivity(new ProcessingActivity("ExplorationSonore") {
	public String getTitle() { return "Exploration du signal sonore"; }
      });
    main.addActivity(new ProcessingActivity("CryptageRSA") {
	public String getTitle() { return "Expérimenter avec la cryptographie"; }
      });
    main.addActivity(new ProcessingActivity("EnVoiture") {
	public String getTitle() { return "Découvrir les graphes et les chemins"; }
      });
    main.addActivity(new ProcessingActivity("UnGrapheDesChemins") {
	public String getTitle() { return "Explorer les graphes et les chemins"; }
      });
  }

  // Defines a processing activity
  private static abstract class ProcessingActivity extends JsProgletActivities.JavaActivity {
    /** Constructs a processing activity.
     * @param processing The processing to use.
     * @throws IllegalArgumentExceptionif the processing is undefined.
     */
    public ProcessingActivity(String processing) { name = processing; } private String name; private int width, height;
    public void init(JsMain main) {
      main.getFrame().addTab("Editeur", (JPanel) jvsEditor, "org/javascool/doc-files/icones16/edit.png", false); 
      jvsEditor.setProglet(name);
      main.getFrame().addTab("Document de la proglet", "sketchbook/"+name+"/about-proglet.htm", "org/javascool/doc-files/icones16/help.png", true);
      init1(main);
      init2(main);
    }
    private void init2(JsMain main) {
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
	  main.getFrame().addTab(name, applet, "org/javascool/doc-files/icones16/edit.png", false);
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
