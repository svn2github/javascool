/*******************************************************************************
* Thierry.Vieville@sophia.inria.fr, Copyright (C) 2010.  All rights reserved. *
*******************************************************************************/

package org.javascool;

import javax.swing.JPanel;

// Proglets used for Java activities
import proglet.ingredients.Console;
import proglet.exosdemaths.CurveDisplay;

/** This factory defines the different JavaScool v3-2 proglet activities.
 * @see <a href="JsProgletActivities.java.html">source code</a>
 * @serial exclude
 */
public class JsProgletActivities {
  /**/ private JsProgletActivities() {}

  /** Adds all proglet activities. */
  public static void addActivities(JsMain main) {
    main.addActivity(new ProgletActivity("ingredients", "Apprendre à programmer", "Découvrir les ingrédients des algorithmes") {
                       public void init2(JsFrame frame) {
                         frame.addTab("Parcours d'initiation", "proglet/ingredients/doc-files/index.htm", "org/javascool/doc-files/icones16/globe.png", true, false);
                       }
                     }
                     );
    main.addActivity(new ProgletActivity("exosdemaths", "Apprendre à programmer", "Programmer des calculs numériques et géométrique") {
	               public void init2(JsFrame frame) {
                         frame.addTab("Propositions d'exercices", "proglet/exosdemaths/doc-files/index.htm", "org/javascool/doc-files/icones16/globe.png", true, false);
                       }
                     }
                     );
    main.addActivity(new ProgletActivity("ingredients", "Apprendre à programmer", "Apprendre à programmer avec des tableaux") {
	               public void init2(JsFrame frame) {
                         frame.addTab("Propositions d'exercices", "proglet/exosdemaths/doc-files/sujet-about-tableaux.htm", "org/javascool/doc-files/icones16/globe.png", true, false);
                       }
                     }
                     );
    main.addActivity(new AlgoEditorActivity());
    main.addActivity(new ProgletActivity("dichotomie", "Algorithmes dichotomiques", "Comprendre le principe de la dichotomie") {
                       public void init2(JsFrame frame) {
                         frame.addTab("Enoncé de l'exercice", "proglet/dichotomie/doc-files/sujet-appli-dicho.htm", "org/javascool/doc-files/icones16/globe.png", true, false);
                       }
                     }
                     );
    main.addActivity(new ProgletActivity("pixelsetcie", "Objet numérique: les images", "Comprendre la manipulation d'images") {
                       public void init2(JsFrame frame) {
                         frame.addTab("Enoncé de l'exercice", "proglet/pixelsetcie/doc-files/sujet-appli-image.htm", "org/javascool/doc-files/icones16/globe.png", true, false);
                       }
                     }
                     );
    main.addActivity(new ProgletActivity("convanalogique", "Algorithmes dichotomiques", "Programmer la conversion analogique-digitale") {
                       public void init2(JsFrame frame) {
                         frame.addTab("Enoncé de l'exercice", "proglet/convanalogique/doc-files/sujet-appli-conva.htm", "org/javascool/doc-files/icones16/globe.png", true, false);
                       }
                     }
                     );
    main.addActivity(new ProgletActivity("synthesons", "Objet numérique: le son", "Manipuler un signal sonore") {
	              public void init2(JsFrame frame) {}
                     }
                     );
    main.addActivity(new ProgletActivity("javaprog", "Aller plus loin en programmation", "Programmer directement en Java") {
	public void init2(JsFrame frame) {}
                     }
                     );
    main.addActivity(new ProgletActivity("tortuelogo", "Aller plus loin en programmation", "Programmer avec la «tortue logo»") {
	public void init2(JsFrame frame) {}
                     }
                     );
  }
  /** Defines a compilation activity. */
  public static abstract class JavaActivity implements JsMain.Activity {
    protected JsMain main;
    protected static JvsSourceEditor jvsEditor = new JvsSourceEditor();
    private Runnable compile = new Runnable() {
      public void run() {
        main.getFrame().delTool("Exécuter");
        main.getFrame().delTool("Arrêter");
        main.getFrame().showTab("Console");
        Console.clear();
        if(getEditor().getText().trim().length() > 0) {
          main.getFileChooser().doSave(getEditor(), getExtension());
          if(main.getFileChooser().getFile() != null) {
            if(getEditor() instanceof AlgoEditor)
              main.getFileChooser().doSave(jvsEditor, Jvs2Java.reformat(((AlgoEditor) getEditor()).getJavaSource()), ".jvs");
            Jvs2Java.translate(main.getFileChooser().getFile());
            String out = Jvs2Java.compile(main.getFileChooser().getFile());
            System.out.println(out.length() == 0 ? "Compilation réussie !" : out);
            Console.printHtml("<hr>\n");
            if(out.length() == 0) {
              main.getFrame().addTool("Exécuter", "org/javascool/doc-files/icones16/play.png", execute);
              main.getFrame().addTool("Arrêter", "org/javascool/doc-files/icones16/stop.png", stop);
            }
          } else
            System.out.println("Impossible de compiler: le fichier n'est pas sauvegardé !");
        } else
          System.out.println("Rien à compiler: il n'y pas de code dans l'éditeur !");
      }
    };
    private Runnable execute = new Runnable() {
      public void run() {
        Console.clear();
        CurveDisplay.scopeReset();
        Jvs2Java.load(main.getFileChooser().getFile());
        Jvs2Java.run(true);
      }
    };
    private Runnable stop = new Runnable() {
      public void run() {
        Jvs2Java.run(false);
      }
    };
    protected void init1(JsMain main) {
      this.main = main;
      main.getFrame().addTab("Console", Jvs2Java.getPanel("ingredients"), "org/javascool/doc-files/icones16/compile.png", true, true);
      main.getFrame().addTool("Compiler", "org/javascool/doc-files/icones16/compile.png", compile);
    }
    public Editor getEditor() {
      return jvsEditor;
    }
    public String getExtension() {
      return ".jvs";
    }
    public void stop(JsMain main) {}
  }

  /** Defines an AlgoTree activity. */
  private static class AlgoEditorActivity extends JavaActivity {
    private static AlgoEditor algoEditor = new AlgoEditor();
    public void init(JsMain main) {
      jvsEditor.reset(false);
      main.getFrame().addTab("Editeur d'Algo.", (JPanel) algoEditor, "org/javascool/doc-files/icones16/edit.png", false, true);
      main.getFrame().addTab("Voir le code en JVS", (JPanel) jvsEditor, "org/javascool/doc-files/icones16/zoom-in.png", true, false);
      init1(main);
      main.getFrame().addTab("Tracé", Jvs2Java.getPanel("exosdemaths"), "org/javascool/doc-files/icones16/compile.png", true, false);
      main.getFrame().addTab("Documentation", "org/javascool/doc-files/about-algo-editor.htm", "org/javascool/doc-files/icones16/help.png", true, false);
    }
    public String getType() {
      return "Apprendre à programmer";
    }
    public String getTitle() {
      return "Découvrir les algorithmes de manière graphique";
    }
    public Editor getEditor() {
      return algoEditor;
    }
    public String getExtension() {
      return ".pml";
    }
  }

  /** Defines a proglet activity. */
  private static abstract class ProgletActivity extends JavaActivity {
    private String proglet, type, title;
    public String getType() {
      return type;
    }
    public String getTitle() {
      return title;
    }
    /** Constructs a proglet activity.
     * @param proglet The proglet to use.
     * @throws IllegalArgumentExceptionif the proglet is undefined.
     */
    public ProgletActivity(String proglet, String type, String title) {
      if(Jvs2Java.getPanel(proglet) == null) throw new IllegalArgumentException("Undefined proglet : " + proglet);
      this.type= type;
      this.title = title;
      this.proglet = proglet;
    }
    public void init(JsMain main) {
      main.getFrame().addTab("Editeur", (JPanel) jvsEditor, "org/javascool/doc-files/icones16/edit.png", false, true);
      jvsEditor.setProglet(proglet);
      init1(main);
      if(!"ingredients".equals(proglet)) {
        String name = "exosdemaths".equals(proglet) ? "Tracé" : proglet;
        main.getFrame().addTab(name, Jvs2Java.getPanel(proglet), "org/javascool/doc-files/icones16/compile.png", true, true);
      }
      init2(main.getFrame());
      main.getFrame().addTab("Document de la proglet", "proglet/" + proglet + "/doc-files/about-proglet.htm", "org/javascool/doc-files/icones16/help.png", true, false);
      main.getFrame().addTab("Mémo des instructions", "proglet/ingredients/doc-files/about-memo.htm", "org/javascool/doc-files/icones16/help.png", true, false);
      main.getFrame().showTab("ingredients".equals(proglet) ? "Console" : "exosdemaths".equals(proglet) ? "Tracé" : proglet);
    }
    // Proglet specific pannels
    protected void init2(JsFrame frame) {}
  }
}
