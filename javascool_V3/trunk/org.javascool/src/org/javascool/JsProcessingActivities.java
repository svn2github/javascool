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
  /**/ private JsProcessingActivities() {}

  /** Adds all processing activities. */
  public static void addActivities(JsMain main) {
    main.addActivity(new ProcessingActivity("ExplorationSonore", "Objet numérique: le son", "Exploration du signal sonore", 800, 600) {
                       public void init2(JsFrame frame) {
                         frame.addTab("Un tutoriel sur les signaux sonores.", "sketchbook/ExplorationSonore/sujet-creation-de-sons.htm",
                                      "org/javascool/doc-files/icones16/globe.png", true, false);
                       }
                     }
                     );
    main.addActivity(new ProcessingActivity("CryptageRSA", "Aller plus loin en programmation", "Expérimenter avec la cryptographie", 1024, 700) {
                       public void init2(JsFrame frame) {
                         frame.addTab("Un tutoriel sur le cryptage RSA", "sketchbook/CryptageRSA/sujet-about-cryptageRSA.htm",
                                      "org/javascool/doc-files/icones16/globe.png", true, false);
                       }
                     }
                     );
    main.addActivity(new ProcessingActivity("EnVoiture", "Objet numérique: le graphe", "Découvrir les graphes et les chemins", 1200, 700) {
                       public void init2(JsFrame frame) {
                         frame.addTab("Un tutoriel sur les graphes (1ère partie)", "sketchbook/EnVoiture/sujet-about-voiture.htm",
                                      "org/javascool/doc-files/icones16/globe.png", true, false);
                       }
                     }
                     );
    main.addActivity(new ProcessingActivity("UnGrapheDesChemins", "Objet numérique: le graphe", "Explorer les graphes et les chemins", 1024, 700) {
                       public void init2(JsFrame frame) {
                         frame.addTab("Un tutoriel sur les graphes (2ème partie)", "sketchbook/UnGrapheDesChemins/sujet-about-chemins.htm",
                                      "org/javascool/doc-files/icones16/globe.png", true, false);
                       }
                     }
                     );
  }
  // Defines a processing activity
  private static abstract class ProcessingActivity extends JsProgletActivities.JavaActivity {
    /** Constructs a processing activity.
     * @param processing The processing to use.
     * @throws IllegalArgumentExceptionif the processing is undefined.
     */
    public ProcessingActivity(String n, String c, String t, int w, int h) {
      name = n;
      type = c;
      // This sets the type to null if the processing class is not loadable
      try { Class.forName(name);
      } catch(Throwable e) {
        type = null;
      }
      title = t;
      width = w + 20;
      height = h + 20;
    }
    // Checks that the processing class is buildable
    private String name, type, title;
    public String getType() {
      return type;
    }
    public String getTitle() {
      return title;
    }
    private int width, height;
    public void init(JsMain main) {
      main.getFrame().addTab("Editeur", (JPanel) jvsEditor, "org/javascool/doc-files/icones16/edit.png", false, true);
      jvsEditor.setProglet(name);
      init1(main);
      main.getFrame().addTab("Document de la proglet", "sketchbook/" + name + "/about-proglet.htm", "org/javascool/doc-files/icones16/help.png", true, true);
      init1b(main);
      init2(main.getFrame());
    }
    protected void init2(JsFrame frame) {}
    private void init1b(JsMain main) {
      PrintStream out = System.out;
      System.setOut(System.err);
      try {
        final boolean popup = false;
        applet = (Applet) Class.forName(name).newInstance();
        if(popup) {
          jframe = Utils.show(applet, name, Utils.getIcon("org/javascool/doc-files/icones16/compile.png"), width, height, false);
          jframe.setResizable(false);
        } else {
          JPanel pane = new JPanel();
          applet.init();
          applet.start();
          applet.setMinimumSize(new Dimension(width, height));
          applet.setMaximumSize(new Dimension(width, height));
          pane.add(applet);
          main.getFrame().addTab(name, pane, "org/javascool/doc-files/icones16/edit.png", true, false);
        }
        Macros.sleep(1000);
        System.setOut(out);
      } catch(Throwable e) {
        System.setOut(out);
        System.out.println("Désolé, l'activité " + name + " n'est pas définie dans cette version (" + e + ").");
      }
    }
    private Applet applet = null;
    public void stop() {
      applet.stop();
      applet.destroy();
      if(jframe != null) {
        jframe.dispose();
        jframe = null;
      }
    }
    private JFrame jframe = null;
  }
}
