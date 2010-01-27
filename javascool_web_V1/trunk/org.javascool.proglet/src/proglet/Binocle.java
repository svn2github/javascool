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
  private static final int base = WIDTH0 / 2, base2 = base / 2;
  // Elevation of the eyes w.r.t neck
  private static final int elevation = 0;
  // Field of view in degree 
  private static final double field = 45;
  // Eye time constant
  private static final double tau_eye = 0;
  // Neck time constant
  private static final double tau_neck = 0;

  // This defines the panel to display
  private static class Panel extends JPanel {
    private static final long serialVersionUID = 1L;

    public Panel() {
      addMouseListener(new MouseListener() {
	  private static final long serialVersionUID = 1L;
	  public void mouseReleased(MouseEvent e) { 
	    set(0, e.getX() - MX, MY - e.getY(), elevation);
	  }
	  public void mousePressed(MouseEvent e) { }
	  public void mouseClicked(MouseEvent e) {  }	  
	  public void mouseEntered(MouseEvent e) {  }
	  public void mouseExited(MouseEvent e) {  }
	});
      set(0, 30, 0, 0, 0); 
    }

    // Constants for drawing
    private static final int EM = 20, EM2 = EM/2, EW = WIDTH0/2 - EM, EY = 2*HEIGHT0/3 - EM, EH = HEIGHT0/3 - EM, MX = WIDTH0/2, MY = 2*HEIGHT0/3 - base/2 + EM;

    public void paint(Graphics g) {
      super.paint(g);
      g.setPaintMode(); 
      // Draws the retina's borders
      g.setColor(Color.BLUE);
      g.drawRect(EM, EY, EW, EH);
      g.drawRect(EM + EW, EY, EW, EH);
      // Draws the binocular mount
      g.drawLine(u_l, v_l, u_r, v_r);
      g.drawOval(MX - 10, MY - 10, 20, 20);
      g.drawPolygon(ue_l, ve_l, N);
      g.drawPolygon(ue_r, ve_r, N);
      g.setColor(Color.YELLOW);
      g.drawLine(u_l, v_l, u, v);
      g.drawLine(u_r, v_r, u, v);
      // Draws the retina's targets
      for(int i = 0; i < ntargets; i++) {
	target t = targets[i];
	if (t.n[0] != ' ') {
	  g.setColor(t.c);
	  if (t.u_l >= 0 )
	    g.drawChars(t.n, 0, 1, t.u_l, t.v_l);
	  if (t.u_r >= 0 )
	    g.drawChars(t.n, 0, 1, t.u_r, t.v_r);
	  g.drawChars(t.n, 0, 1, t.u, t.v);
	}
      }
    }

    /** Sets the mount coordinates.
     * @param gaze Gaze horizontal counter-clockwise angle in degree.
     * @param vergence Gaze horizontal counter-clockwise vergence angle in degree.
     * @param pitch Gaze vertical upper angle in degree.
     * @param pan Neck horizontal counter-clockwise angle in degree.
     * @param tilt Neck vertical upper angle in degree.
     */
    public void set(double gaze, double vergence, double pitch, double pan, double tilt) {
      // Time filtering
      this.gaze = gaze = this.gaze + (1 - tau_eye) * (gaze - this.gaze);
      this.vergence = vergence = this.vergence + (1 - tau_eye) * (vergence - this.vergence);
      this.pitch = pitch = this.pitch + (1 - tau_eye) * (pitch - this.pitch);
      this.pan = pan = this.pan + (1 - tau_neck) * (pan - this.pan);
      this.tilt = tilt = this.tilt + (1 - tau_neck) * (tilt - this.tilt);
      // Bounds and converts angles
      double K = Math.PI/180; 
      if (gaze < -45) gaze = -45; if (gaze > 45) gaze = 45; gaze *= K;
      if (vergence < 0.01) vergence = 0.01; if (vergence > 60) vergence = 60; vergence *= K;
      if (pitch < -30) pitch = -30; if (pitch > 30) pitch = 30; pitch *= K;
      if (pan < -30) pan = -30; if (pan > 30) pan = 30; pan *= K;
      if (tilt < -30) tilt = -30; if (tilt > 30) tilt = 30; tilt *= K;
      // Generates the horizontal drawing points
      double c_pan = Math.cos(pan), s_pan = Math.sin(pan);
      u_l = (int) (MX - base2 * c_pan); u_r = (int) (MX + base2 * c_pan);
      v_l = (int) (MY - base2 * s_pan); v_r = (int) (MY + base2 * s_pan);
      double a_lr = pan + gaze + Math.PI/2, a_l = a_lr + vergence, a_r =  a_lr - vergence;
      // eq := {u = u_l + k_l * cos(a_l), u = u_r + k_r * cos(a_r), v = v_l + k_l * sin(a_l), v = v_r + k_r * sin(a_r)}:
      // collect(solve(eq, {u, v, k_l, k_r}), {u_l, v_l, u_r, v_r}, factor);
      double d = Math.sin(a_r - a_l), c_a_l = Math.cos(a_l), s_a_l = Math.sin(a_l), c_a_r = Math.cos(a_r), s_a_r = Math.sin(a_r);
      double u = c_a_l * c_a_r * (v_l - v_r) + c_a_l * s_a_r * u_l - s_a_l * c_a_r * u_r;
      double v = s_a_l * s_a_r * (u_r - u_l) + c_a_l * s_a_r * v_l - s_a_l * c_a_r * v_r;
      this.u = (int) (u / d); this.v = (int) (v / d);
      // Generates the vertical drawing points
      double dl = EM2 * (1 + Math.sin(pitch + tilt));
      drawOval(ue_l, ve_l, u_l, v_l, a_l, EM, dl);
      drawOval(ue_r, ve_r, u_r, v_r, a_r, EM, dl);
      // Repaints
      repaint(); 
    }
    private double gaze, vergence, pitch, pan, tilt;
    private int u_l, v_l, u_r, v_r, u, v, N = 256, ue_l[] = new int[N], ve_l[] = new int[N], ue_r[] = new int[N], ve_r[] = new int[N];

    // Generates the polygon of an oval of center (u0, v0), angle a and axes (L, l)
    private static void drawOval(int u[], int v[], int u0, int v0, double a, double L, double l) {
      int N = u.length; if (v.length < N) N = v.length;
      for(int n = 0; n < N; n++) {
	double t = 2 * n * Math.PI / (N - 1);
	u[n] = (int) (u0 + L * Math.cos(t) * Math.sin(a) + l * Math.sin(t) * Math.cos(a));
	v[n] = (int) (v0 + l * Math.sin(t) * Math.sin(a) - L * Math.cos(t) * Math.cos(a));
      }
    }

    // Defines a punctual target
    private static class target { char n[] = {' '}; Color c; int u_l, v_l, u_r, v_r, u, v; }; private static final int ntargets = 4; 
    private target targets[] = new target[ntargets]; { for(int i = 0; i < ntargets; i++) targets[i] = new target(); }

    /** Sets the location of a target.
     * @param i Target's index, between {0, 4{.
     * @param X Horizontal 3D location.
     * @param Y Depth 3D location.
     * @param Z Vertical 3D location.
     */
    public void set(int i, double X, double Y, double Z) {
      if(0 <= i && i < 4) {
	target t = targets[i]; t.n[0] = '+'; t.c = i == 0 ? Color.RED : i == 1 ? Color.GREEN : i == 2 ? Color.MAGENTA : Color.BLACK;
	// Projection in the top-view
	t.u = (int) (MX + X); t.v = (int) (MY - Y); 
	System.out.println("P = (" + X + ", " + Y + ", " + Z+ ")");
	// Projection on each retina
	double Pl0, Pl1, Pl2, Pr0, Pr1, Pr2;
	{
	  Pl0 = Math.cos(gaze - vergence + pan) * X - Math.sin(gaze - vergence + pan) * Y + Math.cos(gaze - vergence) * base / 0.2e1;
	  Pl1 = Math.sin(gaze - vergence + pan) * X + Math.cos(gaze - vergence + pan) * Y + Math.sin(gaze - vergence) * base / 0.2e1;
	  Pl2 = Z + elevation;
	  Pr0 = Math.cos(gaze + vergence + pan) * X - Math.sin(gaze + vergence + pan) * Y - Math.cos(gaze + vergence) * base / 0.2e1;
	  Pr1 = Math.sin(gaze + vergence + pan) * X + Math.cos(gaze + vergence + pan) * Y - Math.sin(gaze + vergence) * base / 0.2e1;
	  Pr2 = Z + elevation;
	}
	System.out.println("pl = (" + Pl0 + ", " + Pl1 + ", " + Pl2 + ") pr = (" + Pr0 + ", " + Pr1 + ", " + Pr2 + ")");
	if (Math.abs(Pl0) < Pl1) {
	  t.u_l = EM + EW/2 + (int) (EW/2.1 * (Pl0 / Pl1)); 
	  t.v_l = EY + EH/2 + (int) (EH/2 * (Pl2 / Pl1));
	} else {
	  t.u_l = t.v_l = - 1;
	}
	if (Math.abs(Pr0) < Pr1) {
	  t.u_r = EM + 3*EW/2 + (int) (EW/2.1 * (Pr0 / Pr1)); 
	  t.v_r = EY + EH/2   + (int) (EH/2.1 * (Pr2 / Pr1));
	} else {
	  t.u_r = t.v_r = - 1;
	}
	// Repaints
	repaint(); 
      }
    }
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
