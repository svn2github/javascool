/*******************************************************************************
 * Thierry.Vieville@sophia.inria.fr, Copyright (C) 2009.  All rights reserved. *
 *******************************************************************************/

// Used to define the gui
import java.applet.Applet;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.BorderLayout;

// Used to define an icon/label
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.net.URL;

// Used to define the numeric input
import javax.swing.BorderFactory;

// Used for the text field
import javax.swing.JTextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Used for the slider
import javax.swing.JSlider;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/** Définit une proglet javascool qui permet d'expérimenter la recherche scopetomique.
 * Méthodes statiques à importer: <pre>
 * import static Scope.reset;
 * import static Scope.plot;
 * </pre>
 * Fichiers utilisés: <pre>
 * ./scope_screen.png
 * </pre>
 * Documentation: <a href="scope-sujet.html">sujet</a> et <a href="scope-correction.html">correction</a>.
 */
public class Scope {

  // This defines the panel to display
  private static class Panel extends JPanel {
    public Panel() {
      super(new BorderLayout()); setBackground(Color.WHITE); setPreferredSize(new Dimension(512, 421 + 50 * 2));
      add(scope = new CurveOutput(), BorderLayout.NORTH);
      JPanel panel = new JPanel(new BorderLayout());
      panel.add(new NumberInput("angle", -3.14, 3.14, 0.1, 3.14/2), BorderLayout.NORTH);
      panel.add(new NumberInput("force", -1, 1, 0.1, -0.5), BorderLayout.SOUTH);
      add(panel, BorderLayout.SOUTH);
    }
    public CurveOutput scope;
  }

  // This defines a scope ouput
  private static class CurveOutput extends JPanel {
    public CurveOutput() {
      setBackground(Color.WHITE); setPreferredSize(new Dimension(512, 421));
      JLabel icon = new JLabel(); icon.setIcon(getIcon("scope_screen.png")); add(icon);
    }
    public void paint(Graphics g) {
      super.paint(g);
      g.setPaintMode(); 
      for(int j = 0, ij = 1; j < 10; j++, ij++) {
	g.setColor(colors[j]);
	for(int i = 1; i < 512; i++, ij++) 
	  if (values[ij] >= 0 && values[ij - 1] >= 0) {
	    g.drawLine(i - 1, values[ij - 1], i, values[ij]);
	  }
      }
    }
    private int values[];

    /** Resets the curve value. */
    public void reset() {
      values = new int[512 * 10]; for (int ij = 0; ij < 512 * 10; ij++) values[ij] = -1;
      repaint(0, 0, getWidth(), getHeight());
    }

    /** Sets a curve value.
     * @param x Pixel abscissa, in [-1..1].
     * @param y Pixel Ordinate, in [-1..1].
     * @param c Channel, in {0, 9}.
     */
    public void set(double x, double y, int c) {
      int i = (int) Math.rint(255 + x * 233);
      if (0 <= c && c < 10 && 0 <= i && i < 512) {
	values[i + 512 * c] = -1 <= y & y <= 1 ? 425 - (int) Math.rint(210 + y * 185) : -1;
	repaint(i - 1, 0, 2, getHeight());
      }
    }
    private static Color colors[] = { Color.BLACK, new Color(150, 75, 0), Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.BLUE, Color.MAGENTA, Color.GRAY, Color.WHITE };
  }

  // This defines a numeric input
  private static class NumberInput extends JPanel {
    public NumberInput(String name, double min, double max, double step, double value) {
      setBorder(BorderFactory.createTitledBorder(name)); setPreferredSize(new Dimension(400, 50));
      field = new JTextField(12);
      field.addActionListener(new ActionListener() {
	  public void actionPerformed(ActionEvent evt) {
	    try { set(new Double(field.getText()).doubleValue(), 'T'); } catch(NumberFormatException e) { } 
	  }
	});
      add(field);
      slider = new JSlider();
      slider.addMouseListener(new MouseListener() {
	  public void mouseClicked(MouseEvent e) { } 
	  public void mouseEntered(MouseEvent e) { }
	  public void mouseExited(MouseEvent e) { }
	  public void mousePressed(MouseEvent e) { }
	  public void mouseReleased(MouseEvent e) {
	    set((NumberInput.this.max - NumberInput.this.min) / 100.0 * slider.getValue() + NumberInput.this.min, 'S');
	  }
	});
      add(slider);
      this.min = min; this.max = max; this.step = step; set(value, ' ');
    }
    private JTextField field; private JSlider slider;
    // Display the value
    private void set(double value, char from) {
      // Retrain value to be step by step and in the min-max interval
      value = step <= 0 ? value : min + step * Math.rint((value-min)/step); value = value < min ? min : value > max ? max : value;
      // Patches the .0* outputs
      String input = new Double(value).toString().replaceFirst("(99999|00000).*$", "").replaceFirst(".0$", "");
      field.setText(input);
      if (from != 'S') slider.setValue((int) ((max > min) ? 100.0 * (value - min) / (max - min) : value));
      this.value = value;
    }
    /** Gets the input value. */
    public double getValue() { return value; }
    private double min, max, step, value;
  }

  // This defines an applet context image loader
  private static ImageIcon getIcon(String file) {
    try { return new ImageIcon(new URL(base+file)); } catch(Exception e) { System.out.println(e); return new ImageIcon(); }
  }
  private static String base = "file:";

  //
  // This defines the tests on the panel
  //

  /** Test du panel. */
  static void test() {
    reset();
    for(double x = -1; x <= 1; x += 0.001) {
      set(x, 0.5 * Math.sin(10 * x) + 0.5, 6);
      set(x, -Math.exp(-(x + 1)), 7);
    }
  }

  //
  // This defines the javascool interface
  //

  /** Initialise le tracé. */
  static public void reset() {
    panel.scope.reset();
  }

  /** Change la valeur d'un point du tracé.
   * @param x Abcisse de la courbe, dans [-1, 1].
   * @param y Ordonnée de la courbe, dans [-1, 1].
   * @param c Numéro de la courbe: 0 (noir), 1 (brun), 2 (rouge), 3 (orange), 4 (jaune), 5 (vert), 6 (bleu), 7 (violet), 8 (gris), 9 (blanc).
   */
  static public void set(double x, double y, int c) {
    panel.scope.set(x, y, c);
  }

  //
  // This defines the javascool embedded
  //

  /** Renvoie le panel affiché. */
  static JPanel getPanel(Applet applet) { base = applet == null ? "file:" : applet.getCodeBase().toString()+"/"; return panel = new Panel(); } 
  
  private static Panel panel = new Panel();
}
