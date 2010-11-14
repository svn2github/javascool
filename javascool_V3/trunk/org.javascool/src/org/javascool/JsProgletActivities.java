/*******************************************************************************
 * Thierry.Vieville@sophia.inria.fr, Copyright (C) 2010.  All rights reserved. *
 *******************************************************************************/

package org.javascool;

import javax.swing.JPanel;

/** This factory defines the different JavaScool proglet activities. */
public class JsProgletActivities {

  /** Adds all proglet activities. */
  public static void addActivities() {
    JsActivities.addActivity(new ProgletActivity("ingredients") {
	public String getTitle() { return "Découvrir les ingrédients des algorithmes"; }
	public void init(JsFrame frame) {
	  super.init(frame);
	  frame.getFrame().addTab("Parcours d'initiation", "proglet/ingredients/doc-files/index.htm", "org/javascool/doc-files/icones16/globe.png", true);
	}
      });
    JsActivities.addActivity(new ProgletActivity("exosdemaths") {
	public String getTitle() { return "Programmer des calculs numériques et géométrique"; }
	public void init(JsFrame frame) {
	  super.init(frame);
	  frame.getFrame().addTab("Propositions d'exercices", "proglet/exosdemaths/doc-files/index.htm", "org/javascool/doc-files/icones16/globe.png", true);
	  initDoc();
	}});
    JsActivities.addActivity(new AlgoEditorActivity() {
	public String getTitle() { return "Découvrir les algorithmes de manière graphique"; }
      });
    JsActivities.addActivity(new ProgletActivity("dichotomie") {
	public String getTitle() { return "Comprendre le principe de la dichotomie"; }
	public void init(JsFrame frame) {
	  super.init(frame);
	  frame.getFrame().addTab("Enoncé de l'exercice", "proglet/dichotomie/doc-files/sujet-appli-dicho.htm","org/javascool/doc-files/icones16/globe.png", true);
	  initDoc();
	}});
    JsActivities.addActivity(new ProgletActivity("pixelsetcie") {
	public String getTitle() { return "Comprendre la manipulation d'images"; }
	public void init(JsFrame frame) {
	  super.init(frame);
	  frame.getFrame().addTab("Enoncé de l'exercice", "proglet/pixelsetcie/doc-files/sujet-appli-image.htm","org/javascool/doc-files/icones16/globe.png", true);
	  initDoc();
	}});
    JsActivities.addActivity(new ProgletActivity("convanalogique") {
	public String getTitle() { return "Programmer la conversion analogique-digitale"; }
	public void init(JsFrame frame) {
	  super.init(frame);
	  frame.getFrame().addTab("Enoncé de l'exercice", "proglet/convanalogique/doc-files/sujet-appli-conva.htm","org/javascool/doc-files/icones16/globe.png", true);
	  initDoc();
	}});
    JsActivities.addActivity(new ProgletActivity("synthesons") {
	public String getTitle() { return "Découverte du signal sonore"; }
	public void init(JsFrame frame) {
	  super.init(frame);
	  initDoc();
	}});
    JsActivities.addActivity(new ProgletActivity("javaprog") {
	public String getTitle() { return "Programmer directement en Java"; }
	public void init(JsFrame frame) {
	  super.init(frame);
	  initDoc();
	}});
    JsActivities.addActivity(new ProgletActivity("tortuelogo") {
	public String getTitle() { return "Programmer avec la «tortue logo»"; }
	public void init(JsFrame frame) {
	  super.init(frame);
	  initDoc();
	}});
  }

  // Defines a proglet activity
  private static abstract class ProgletActivity extends JsActivities.JavaActivity {
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
    public void init(JsFrame f) {
      frame = f;
      if (JsActivities.jvsEditor == null) JsActivities.jvsEditor = new JvsSourceEditor(); 
      frame.getFrame().addTab("Editeur", (JPanel) JsActivities.jvsEditor, "org/javascool/doc-files/icones16/edit.png", false);
      JsActivities.jvsEditor.setProglet(proglet);
      initCompile();
      if (!"ingredients".equals(proglet)) {
	String name = "exosdemaths".equals(proglet) ? "Tracé" : proglet;
	frame.getFrame().addTab(name, Jvs2Java.getPanel(proglet), "org/javascool/doc-files/icones16/compile.png", true);
      }
    }
    protected void initDoc() {
      frame.getFrame().addTab("Document de la proglet", "proglet/"+proglet+"/doc-files/about-proglet.htm", "org/javascool/doc-files/icones16/help.png", true);
      frame.getFrame().addTab("Mémo des instructions", "proglet/ingredients/doc-files/about-memo.htm", "org/javascool/doc-files/icones16/help.png", true);
    }
    public Editor getEditor() { return JsActivities.jvsEditor; }
    public String getExtension() { return ".jvs"; }
    public void stop() { }
  }

  // Defines a AlgoTree proglet activity
  private static abstract class AlgoEditorActivity extends JsActivities.JavaActivity {
    // Common panels and tools
    public void init(JsFrame f) {
      frame = f;
      if (algoEditor == null) algoEditor = new AlgoEditor(); 
      if (algoViewer == null) { algoViewer = new JvsSourceEditor(); algoViewer.reset(false); }
      frame.getFrame().addTab("Editeur d'Algo.", (JPanel) algoEditor, "org/javascool/doc-files/icones16/edit.png", false);
      frame.getFrame().addTab("Voir le code en JVS", (JPanel) algoViewer, "org/javascool/doc-files/icones16/zoom-in.png", true);
      initCompile();
      frame.getFrame().addTab("Tracé", Jvs2Java.getPanel("exosdemaths"), "org/javascool/doc-files/icones16/compile.png", true);
      frame.getFrame().addTab("Documentation", "org/javascool/doc-files/about-algo-editor.htm", "org/javascool/doc-files/icones16/help.png", true);
    }
    public Editor getEditor() { return algoEditor; }
    public String getExtension() { return ".pml"; }
    public void stop() { }
  }
  static AlgoEditor algoEditor = null; static JvsSourceEditor algoViewer = null;
}
