/*******************************************************************************
 * Thierry.Vieville@sophia.inria.fr, Copyright (C) 2009.  All rights reserved. *
 *******************************************************************************/

package proglet.convanalogique;

import org.javascool.Utils;
import org.javascool.Macros;

// Used to define the gui
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Dimension;
import java.awt.BorderLayout;
import javax.swing.JLayeredPane;
import org.javascool.NumberInput;

/** Définit une proglet javascool qui permet d'expérimenter avec des valeurs et signaux numériques.
 * <p><applet code="org.javascool.ProgletApplet" archive="../../../javascool.jar" width="560" height="720"><param name="proglet" value="convanalogique"/></applet></p>
 * @see <a href="ConvAnalogique.java">code source</a>
 */
public class ConvAnalogique implements org.javascool.Proglet { private ConvAnalogique() { }
  private static final long serialVersionUID = 1L;

  // This defines the panel to display
  private static class Panel extends JPanel {
    private static final long serialVersionUID = 1L;

    public Panel() {
      super(new BorderLayout()); setPreferredSize(new Dimension(560, 450));
      // Adds the figure
      JLayeredPane pane = new JLayeredPane();
      pane.setPreferredSize(new Dimension(540, 300));
      JLabel fig = new JLabel();
      fig.setIcon(Utils.getIcon("proglet/convanalogique/doc-files/conv.png"));
      fig.setBounds(2, 0, 540, 300);
      pane.add(fig, new Integer(1), 0);
      out = new JLabel("????");
      out.setBounds(270, 80, 100, 50);
      pane.add(out, new Integer(2), 0);
      cmp = new JLabel("?");
      cmp.setBounds(190, 178, 100, 50);
      pane.add(cmp, new Integer(2), 1);
      add(pane, BorderLayout.NORTH);
      // Adds the input
      add(value = new NumberInput("tension inconnue"), BorderLayout.CENTER);
      value.setScale(0, 1023, 1);
      value.setValue(300);
      JPanel border = new JPanel();
      border.setPreferredSize(new Dimension(560, 190));
      add(border, BorderLayout.SOUTH);
    }
    public NumberInput value; public JLabel out, cmp;
  }

  //
  // This defines the tests on the panel
  //

  /**/public static void test() {
    /* Méthode brute
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
    */
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
	Macros.sleep(1000);
      }
      convaOut(min); convaCompare();
      Macros.echo("La valeur vaut "+min);
    }
  }

  //
  // This defines the javascool interface
  //

  /** Applique une tension en sortie.
   * @param value La tension en milli-volts entre 0 et 1023.
   */
  static public void convaOut(int value) { ConvAnalogique.value = value;  panel.out.setText(value+" mV"); } private static int value = 0;

  /** Compare la tension appliquée en sortie à la tension inconnue.
   * @return -1 si la tension inconnue est plus petite et 1 si elle plus grande ou égale.
   */
  public static int convaCompare() { int r = panel.value.getValue() < value ? -1 : 1; panel.cmp.setText(""+r); return r; }

  /** Définition de l'interface graphique de la proglet. */
  public static final Panel panel = new Panel();
}
