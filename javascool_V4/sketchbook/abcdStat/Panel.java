package org.javascool.proglets.abcdStat;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;

//import org.javascool.widgets.CurveOutput;
import org.javascool.widgets.NumberInput;

/** Définit le panneau graphique de la proglet (adaptation de org.javascool.proglets.algoDeMaths.Panel)
 * @see <a href="Panel.java.html">source code</a>
 * @serial exclude
 */
public class Panel extends JPanel {
  private static final long serialVersionUID = 1L;

  // @bean
  public Panel() {
    super(new BorderLayout());
    add(scope = new CurveOutput().reset(0, 0, 1, 1), BorderLayout.CENTER);
    scope.setBackground(Color.white);
    JPanel input = new JPanel(new BorderLayout());
    input.add(inputX = new NumberInput().setText("X").setScale(-1, 1, 0.001), BorderLayout.NORTH);
    input.add(inputY = new NumberInput().setText("Y").setScale(-1, 1, 0.001), BorderLayout.SOUTH);
    Runnable run1 = new Runnable() {
      @Override
      public void run() {
        inputX.setValue(scope.getReticuleX());
        inputY.setValue(scope.getReticuleY());
        if(runnable != null) {
          runnable.run();
        }
      }
    };
    Runnable run2 = new Runnable() {
      @Override
      public void run() {
        scope.setReticule(inputX.getValue(), inputY.getValue());
      }
    };
    scope.setRunnable(run1);
    inputX.setRunnable(run2);
    inputY.setRunnable(run2);
    add(input, BorderLayout.SOUTH);
  }
  /** Tracé de courbes. */
  public CurveOutput scope;
  /** Entrées de valeurs numériques. */
  public NumberInput inputX, inputY;
  /** Runnable à appeler. */
  public Runnable runnable = null;
}

