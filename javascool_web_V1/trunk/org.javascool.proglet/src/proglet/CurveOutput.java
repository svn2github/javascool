/*******************************************************************************
 * Thierry.Vieville@sophia.inria.fr, Copyright (C) 2009.  All rights reserved. *
 *******************************************************************************/

package proglet;

// Used to define the gui
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

// Used to define an icon/label
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.net.URL;

/** This widget defines a multi-curve output.
 * File used: <pre>
 * img/scope_screen.png
 * </pre>
 * @see <a href="CurveOutput.java">source code</a>
 */
public class CurveOutput extends JPanel {
  private static final long serialVersionUID = 1L;

  private JLabel icon; private int values[];
  {
    setBackground(Color.WHITE); setPreferredSize(new Dimension(512, 421));
    icon = new JLabel(); icon.setIcon(Proglets.getIcon("scope_screen.png")); icon.setLocation(0, 0); add(icon); 
    reset();
  }
  /** Internal routine: do not use. */
  public void paint(Graphics g) {
    super.paint(g);
    g.setPaintMode(); 
    int i0 = (int) icon.getLocation().getX(), j0 = (int) icon.getLocation().getY();
    for(int j = 0, ij = 1; j < 10; j++, ij++) {
      g.setColor(colors[j]);
      for(int i = 1; i < 512; i++, ij++) 
	if (values[ij] >= 0 && values[ij - 1] >= 0) {
	  g.drawLine(i0 + i - 1, j0 + values[ij - 1], i0 + i, j0 + values[ij]);
	}
    }
  }
  
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
      values[i + 512 * c] = -1 <= y & y <= 1 ? 421 - (int) Math.rint(210 + y * 185) : -1;
      repaint(i - 1, 0, 2, getHeight());
    }
  }

  private static Color colors[] = { Color.BLACK, new Color(150, 75, 0), Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.BLUE, Color.MAGENTA, Color.GRAY, Color.WHITE };
}
