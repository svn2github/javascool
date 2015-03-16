package org.javascool.proglets.commSerie;

import javax.swing.JPanel;
import java.awt.BorderLayout;

/** Définit une proglet javascool qui permet d'utiliser toute les classes des swings.
 *
 * @see <a href="Panel.java.html">code source</a>
 * @serial exclude
 */
public class Panel extends JPanel {
  private static final long serialVersionUID = 1L;
  public Panel() {
    setLayout(new BorderLayout());
    removeAll();
  }
  public void removeAll(String displayMode) {
    super.removeAll();
    if(displayMode.length() > 0) {
      add(spanel = new SerialInterfacePanel(displayMode), BorderLayout.NORTH);
    }
    serial = spanel.getSerialInterface();
  }
  public void removeAll() {
    removeAll("CD");
  }
  SerialInterfacePanel spanel;
  SerialInterface serial;
}
