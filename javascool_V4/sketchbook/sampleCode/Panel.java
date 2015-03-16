package org.javascool.proglets.sampleCode;
import static org.javascool.macros.Macros.*;
import static org.javascool.proglets.sampleCode.Functions.*;
import javax.swing.JPanel;
import javax.swing.JLabel;

/** Définit le panneau graphique de la proglet.
 * @see <a href="Panel.java.html">source code</a>
 * @serial exclude
 */
public class Panel extends JPanel {
  private static final long serialVersionUID = 1L;
  // Construction de la proglet
  public Panel() {
    // On crée un label
    label = new JLabel("");
    // Et on l'ajoute au panel
    add(label);
  }
  // Ce label sera utilisé par la routine Functions.setMessage()
  JLabel label;
}

