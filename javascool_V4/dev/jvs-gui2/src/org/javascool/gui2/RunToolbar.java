package org.javascool.gui2;

import javax.swing.JButton;

import org.javascool.core.ProgletEngine;
import org.javascool.widgets.StartStopButton;
import org.javascool.widgets.ToolBar;

/**
 * Barre d'outils pour lancer et arrêter un programme.
 * @since 4.5
 *
 */
public class RunToolbar extends ToolBar {
  private static final long serialVersionUID = -7160154215237177683L;

  /** Boutons de l'interface. */
  private JButton demoButton;
  private StartStopButton runButton;

  /** Instance de la classe. */
  private static RunToolbar jvstb;

  /** Permet d'avoir une instance unique de la classe.*/
  public static RunToolbar getInstance() {
    if(RunToolbar.jvstb == null) {
      RunToolbar.jvstb = new RunToolbar();
    }
    return RunToolbar.jvstb;
  }
  public RunToolbar() {
    addTool("Executer", runButton = new StartStopButton() {
              private static final long serialVersionUID = 1L;

              @Override
              public void start() {
                WidgetPanel.getInstance().focusOnProgletPanel();
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
    this.demoButton = addTool("Demo",
                              "org/javascool/widgets/icons/play.png", new Runnable() {
                                @Override
                                public void run() {
                                  WidgetPanel.getInstance().focusOnProgletPanel();
                                  ProgletEngine.getInstance().getProglet().doDemo();
                                }
                              }
                              );
    demoButton.setVisible(false);
  }
  public void enableDemoButton() {
    demoButton.setVisible(true);
    revalidate();
  }
  public void disableDemoButton() {
    demoButton.setVisible(false);
    revalidate();
  }
  public void enableStartStopButton() {
    runButton.setVisible(true);
    revalidate();
  }
  public void disableStartStopButton() {
    runButton.setVisible(false);
    revalidate();
  }
}
