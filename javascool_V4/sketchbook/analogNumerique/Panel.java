package org.javascool.proglets.analogNumerique;

import javax.swing.JLabel;
import java.awt.Dimension;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import java.awt.BorderLayout;

import org.javascool.widgets.NumberInput;
import org.javascool.macros.Macros;

/** Définit une proglet javascool qui permet d'implémenter un algorithme dichotomiqiue de conversion analogique-numérique.
 *
 * @see <a href="Panel.java.html">code source</a>
 * @serial exclude
 */
public class Panel extends JPanel {
  private static final long serialVersionUID = 1L;

  // @bean
  public Panel() {
    super(new BorderLayout());
    setPreferredSize(new Dimension(560, 450));
    // Adds the figure
    JLayeredPane pane = new JLayeredPane();
    pane.setPreferredSize(new Dimension(540, 300));
    JLabel fig = new JLabel();
    fig.setIcon(Macros.getIcon("org/javascool/proglets/analogNumerique/conv.png"));
    fig.setBounds(3, 0, 540, 300);
    pane.add(fig, new Integer(1), 0);
    out = new JLabel("????");
    out.setBounds(270, 78, 100, 50);
    pane.add(out, new Integer(2), 0);
    cmp = new JLabel("?");
    cmp.setBounds(190, 178, 100, 50);
    pane.add(cmp, new Integer(2), 1);
    add(pane, BorderLayout.NORTH);
    // Adds the input
    add(value = new NumberInput(), BorderLayout.CENTER);
    value.setText("tension inconnue").setScale(0, 1023, 1);
    value.setValue(300);
    JPanel border = new JPanel();
    border.setPreferredSize(new Dimension(560, 190));
    add(border, BorderLayout.SOUTH);
  }
  /** Tension inconnue d'entrée. */
  public NumberInput value;
  /** Etiquette du comparateur et de la sortie. */
  public JLabel out, cmp;
}
