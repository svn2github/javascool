/*******************************************************************************
 * Thierry.Vieville@sophia.inria.fr, Copyright (C) 2010.  All rights reserved. *
 *******************************************************************************/

package proglet;


// Used to define the gui
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

// Used to define a click
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

/** Définit une proglet javascool qui permet d'expérimenter la recherche dichotomiqule suivit d'une cible par une tête artificielle.
 * @see <a href="Binocle.java">code source</a>
 */
public class Binocle implements Proglet { private Binocle() { }
  private static final long serialVersionUID = 1L;

  // Defines the binocular mount parameters
  private static final int WIDTH0  = 540;
  private static final int HEIGHT0 = 580;
  // Distance between both eyes
  private static final int base = WIDTH0 / 2;
  // Elevation of the eyes w.r.t neck
  private static final int elevation = base / 4;
  // Field of view in degree 
  private static final double field = 45;

  // This defines the panel to display
  private static class Panel extends JPanel {
    private static final long serialVersionUID = 1L;

    public Panel() {
      addMouseListener(new MouseListener() {
	  private static final long serialVersionUID = 1L;
	  public void mouseReleased(MouseEvent e) { 
	    //- set(e.getX(), e.getY());
	  }
	  public void mousePressed(MouseEvent e) { }
	  public void mouseClicked(MouseEvent e) {  }	  
	  public void mouseEntered(MouseEvent e) {  }
	  public void mouseExited(MouseEvent e) {  }
	});
    }

    // Constants for drawing
    private static final int EM = 20, EW = WIDTH0/2 - EM, EY = 2*HEIGHT0/3 - EM, EH = HEIGHT0/3 - EM, MX = WIDTH0/2, MY = 2*HEIGHT0/3 - base/2 + EM;

    public void paint(Graphics g) {
      super.paint(g);
      g.setPaintMode(); 
      // Draws the retina's borders and targets
      g.setColor(Color.BLUE);
      g.drawRect(EM, EY, EW, EH);
      g.drawRect(EM + EW, EY, EW, EH);
      for(int i = 0; i < ntargets; i++) {
	target t = targets[i];
	if (t.n[0] != ' ') {
	  g.setColor(t.c);
	  g.drawChars(t.n, 0, 1, t.u_l, t.v_l);
	  g.drawChars(t.n, 0, 1, t.u_r, t.v_r);
	  g.drawChars(t.n, 0, 1, t.u, t.v);
	}
      }
      // Draws the binocular mount
      g.drawLine(u_l, v_l, u_r, v_r);
      g.drawOval(MX - 10, MY - 10, 20, 20);
      g.drawPolygon(ue_l, ve_l, N);
      g.drawPolygon(ue_r, ve_r, N);
      g.setColor(Color.YELLOW);
      g.drawLine(u_l, v_l, u, v);
      g.drawLine(u_r, v_r, u, v);
    }

    /** Sets the mount coordinates.
     * @param gaze Gaze horizontal counter-clockwise angle in degree.
     * @param vergence Gaze horizontal counter-clockwise vergence angle in degree.
     * @param pitch Gaze vertical upper angle in degree.
     * @param pan Neck horizontal counter-clockwise angle in degree.
     * @param tilt Neck vertical upper angle in degree.
     */
    public void set(double gaze, double vergence, double pitch, double pan, double tilt) {
      // Bounds and converts angles
      double K = Math.PI/180; 
      if (vergence < 0.01) vergence = 0.01; if (vergence > 60) vergence = 60; vergence *= K;
      if (gaze < -45) gaze = -45; if (gaze > 45) gaze = 45; gaze *= K; gaze += Math.PI/2;
      if (pitch < -30) pitch = -30; if (pitch > 30) pitch = 30; pitch *= K;
      if (pan < -30) pan = -30; if (pan > 30) pan = 30; pan *= K;
      if (tilt < -30) tilt = -30; if (tilt > 30) tilt = 30; tilt *= K;
      // Generates the horizontal drawing points
      u_l = (int) (MX - base/2 * Math.cos(pan)); u_r = (int) (MX + base/2 * Math.cos(pan));
      v_l = (int) (MY - base/2 * Math.sin(pan)); v_r = (int) (MY + base/2 * Math.sin(pan));
      double a_l = pan + gaze + vergence, a_r =  pan + gaze - vergence;
      // eq := {u = u_l + k_l * cos(a_l), u = u_r + k_r * cos(a_r), v = v_l + k_l * sin(a_l), v = v_r + k_r * sin(a_r)}:
      // collect(solve(eq, {u, v, k_l, k_r}), {u_l, v_l, u_r, v_r}, factor);
      double d = Math.sin(a_r - a_l);
      double u = Math.cos(a_l) * Math.cos(a_r) * (v_l - v_r) + Math.cos(a_l) * Math.sin(a_r) * u_l - Math.sin(a_l) * Math.cos(a_r) * u_r;
      double v = Math.sin(a_l) * Math.sin(a_r) * (u_r - u_l) + Math.cos(a_l) * Math.sin(a_r) * v_l - Math.sin(a_l) * Math.cos(a_r) * v_r;
      this.u = (int) (u / d); this.v = (int) (v / d);
      // Generates the vertical drawing points
      double dn = elevation * Math.sin(tilt), de = elevation * Math.sin(pitch + tilt);

      for(int n = 0; n < N; n++) {
	double a = 2 * n * Math.PI / (N - 1), dl = EM/2;
	ue_l[n] = (int) (u_l + EM * Math.cos(a));
	ve_l[n] = (int) (v_l + dl * Math.sin(a));
	ue_r[n] = (int) (u_r + EM * Math.cos(a));
	ve_r[n] = (int) (v_r + dl * Math.sin(a));
      }
    }
    private int u_l, v_l, u_r, v_r, u, v, N = 256, ue_l[] = new int[N], ve_l[] = new int[N], ue_r[] = new int[N], ve_r[] = new int[N];

    // Defines a punctual target
    private static class target { char n[] = {' '}; Color c; int u_l, v_l, u_r, v_r, u, v; }; private static final int ntargets = 4; 
    private target targets[] = new target[ntargets]; { for(int i = 0; i < ntargets; i++) targets[i] = new target(); }
    

    { set(-30, 45, 0, 30, 0); }
  }

  //
  // This defines the tests on the panel
  //

  /** Test du panel. */
  static void test() {
  }

  /** Définition de l'interface graphique de la proglet. */
  public static final Panel panel = new Panel();
}
