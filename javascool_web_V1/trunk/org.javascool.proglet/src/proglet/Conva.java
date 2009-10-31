/*******************************************************************************
 * Thierry.Vieville@sophia.inria.fr, Copyright (C) 2009.  All rights reserved. *
 *******************************************************************************/

package proglet;

// Used to define the gui
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Dimension;
import java.awt.BorderLayout;

/** Définit une proglet javascool qui permet d'expérimenter avec des valeurs et signaux numériques.
 * Fichiers utilisés: <pre>
 * img/conv.png
 * </pre>
 * @see <a href="Conva.java">code source</a>
 */
public class Conva implements Proglet { private Conva() { }
  private static final long serialVersionUID = 1L;

  // This defines the panel to display
  private static class Panel extends JPanel {
    private static final long serialVersionUID = 1L;

    public Panel() {
      super(new BorderLayout()); setPreferredSize(new Dimension(560, 450));
      // Adds the figure
      JLabel fig = new JLabel();
      fig.setIcon(Proglets.getIcon("conv.png"));
      fig.setBounds(2, 0, 540, 350);
      add(fig, BorderLayout.NORTH);
      // Adds the input
      JPanel panel = new JPanel(new BorderLayout());
      panel.add(value = new NumberInput("tension inconnue", 0, 1024, 1, 500), BorderLayout.SOUTH);
      add(panel, BorderLayout.CENTER);
    }
    public NumberInput value;
  }

  //
  // This defines the tests on the panel
  //

  /** Test du panel. */
  static void test() {
    // Méthode brute
    { 
      int v = 1023; while(v >= 0) {
	convaOut(v);
	if (convaCompare() == 1) {
	  Macros.echo("valeur = "+v);
	  break;
	}
	v = v - 1;
      }
    }
    // Méthode dichotomique
    {
      int min = 0, max = 1024;
      while(min < max - 1) {
	Macros.echo("La valeur est comprise entre " + (min) + " et " + (max - 1));
	int milieu = (min + max) / 2;
	convaOut(milieu); if (convaCompare() == 1) {
	  min = milieu;
	} else {
	  max = milieu;
	}
      }
      Macros.echo("La valeur vaut "+min);
    }
  }

  //
  // This defines the javascool interface
  //

  /** Applique une tension en sortie.
   * @param value La tension en milli-volts entre 0 et 1023.
   */
  static public void convaOut(int value) { Conva.value = value; } private static int value = 0;

  /** Compare la tension appliquée en sortie à la tension inconnue.
   * @return -1 si la tension inconnue est plus petite et 1 si elle plus grande ou égale.
   */
  public static int convaCompare() { return panel.value.getValue() < value ? -1 : 1; }

  /** Définition de l'interface graphique de la proglet. */
  public static final Panel panel = new Panel();
}
