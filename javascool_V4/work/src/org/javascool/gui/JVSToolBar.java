package org.javascool.gui;

import org.javascool.core.ProgletEngine;

import org.javascool.widgets.ToolBar;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.javascool.macros.Macros;
import org.javascool.widgets.StartStopButton;
//import org.javascool.builder.DialogFrame;
//import org.javascool.builder.ProgletsBuilder;

/** La barre d'outils de Java's cool
 * Elle est placée en haut de l'interface. Elle contient les boutons de gestion
 * des fichiers, de compilation et d'éxecution.
 * @see org.javascool.widgets.StartStopButton
 * @see org.javascool.gui.JVSPanel
 */
class JVSToolBar extends ToolBar {
  private static final long serialVersionUID = 1L;
  /** Boutons de l'interface. */
  private JButton compileButton;
  //private JButton demoButton;
  private StartStopButton runButton;
  /** Instance de la classe */
  private static JVSToolBar jvstb;

  public static JVSToolBar getInstance() {
    if(jvstb == null) {
      jvstb = new JVSToolBar();
    }
    return jvstb;
  }
  private JVSToolBar() {
    setName("Java's cool ToolBar");
    init();
  }
  /** Initialize la barre d'outils en créant les bouttons */
  private void init() {
    if (ProgletEngine.getInstance().getProgletCount() > 1)
      addTool("Nouvelle activité", "org/javascool/widgets/icons/new.png", new Runnable() {
	  @Override
	  public void run() {
	    JVSPanel.getInstance().closeProglet();
	  }
	}
	);
    addTool("Nouveau fichier", "org/javascool/widgets/icons/new.png", new Runnable() {
              @Override
              public void run() {
                JVSPanel.getInstance().newFile();
              }
            }
            );
    addTool("Ouvrir un fichier", "org/javascool/widgets/icons/open.png", new Runnable() {
              @Override
              public void run() {
                JVSPanel.getInstance().openFile();
              }
            }
            );
    addTool("Sauver", "org/javascool/widgets/icons/save.png", new Runnable() {
              @Override
              public void run() {
                JVSPanel.getInstance().saveFile();
              }
            }
            );
    {
      JPopupMenu menu = addTool("Sauver en ...", "org/javascool/widgets/icons/saveas.png");
      JLabel l = new JLabel("Sauvegarde du fichier Java'sCool courant:");
      l.setIcon(Macros.getIcon("org/javascool/widgets/icons/saveas.png"));
      menu.add(l);
      menu.add("   .. en javascool (.jvs)").addActionListener(new ActionListener() {
	  private static final long serialVersionUID = 1L;
	  @Override
	  public void actionPerformed(ActionEvent e) {
	    JVSPanel.getInstance().saveAsFile();
	  }
	});
      menu.add("   .. source forme de source java (.java)").addActionListener(new ActionListener() {
	  private static final long serialVersionUID = 1L;
	  @Override
	  public void actionPerformed(ActionEvent e) {   
	    new Thread(new Runnable() { public void run() {
	      JVSPanel.getInstance().saveAsJavaFile();
	    }}).start();
	  }
	});
      menu.add("   .. sous forme exécutable (.jar)").addActionListener(new ActionListener() {
	  private static final long serialVersionUID = 1L;
	  @Override
	  public void actionPerformed(ActionEvent e) {
	    new Thread(new Runnable() { public void run() {
	      JVSPanel.getInstance().saveAsJarFile();
	    }}).start();
	  }
	});
    }

    compileButton = addTool("Compiler", "org/javascool/widgets/icons/compile.png", new Runnable() {
                              @Override
                              public void run() {
                                JVSPanel.getInstance().compileFile();
                              }
                            }
                            );

    addTool("Executer", runButton = new StartStopButton() {
              private static final long serialVersionUID = 1L;

              @Override
              public void start() {
                JVSWidgetPanel.getInstance().focusOnProgletPanel();
                ProgletEngine.getInstance().doRun();
              }

              @Override
              public void stop() {
                ProgletEngine.getInstance().doStop();
              }

              @Override
              public boolean isRunning() {
                return ProgletEngine.getInstance().isRunning();
              }
            }
            );
    runButton.setVisible(false);
    /*
    this.demoButton = addTool("Demo", "org/javascool/widgets/icons/play.png", new Runnable() {
                                @Override
                                public void run() {
                                  JVSWidgetPanel.getInstance().focusOnProgletPanel();
                                  ProgletEngine.getInstance().getProglet().doDemo();
                                }
                              }
                              );
    demoButton.setVisible(false);
    */
    /* Crée le menu de construction de proglets si pertinent
    if(ProgletsBuilder.canBuildProglets()) {
      pbutton = addRightTool("Proglet Builder", new Runnable() {
                               @Override
                               public void run() {
                                 DialogFrame.startFrame();
                               }
                             }
                             );
    }*/
    this.add(About.getAboutMessage(), 0);
    revalidate();
    repaint();
  }
  // @ inner-class-variable
  private JButton pbutton;

  public void enableCompileButton() {
    compileButton.setVisible(true);
    revalidate();
  }
  public void disableCompileButton() {
    compileButton.setVisible(false);
    revalidate();
  }
  /*
  public void enableDemoButton() {
    demoButton.setVisible(true);
    revalidate();
  }
  public void disableDemoButton() {
    demoButton.setVisible(false);
    revalidate();
  }
  */
  public void enableStartStopButton() {
    runButton.setVisible(true);
    revalidate();
  }
  public void disableStartStopButton() {
    runButton.setVisible(false);
    revalidate();
  }
}
