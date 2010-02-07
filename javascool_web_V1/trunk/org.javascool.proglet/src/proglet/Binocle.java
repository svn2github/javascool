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
  // Eye time constant
  private static final double tau_eye = 1, a_eye = 1 - Math.exp(Math.log(0.1)/tau_eye);
  // Neck time constant
  private static final double tau_neck = 5, a_neck = 1 - Math.exp(Math.log(0.1)/tau_neck);

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
      set(0, 0, 0, 0, 0); 
    }

    // Constants for drawing
    private static final int EM = 20, EM2 = EM/2, EW = WIDTH0/2 - EM, EY = 2*HEIGHT0/3 - EM, EH = HEIGHT0/3 - EM, MX = WIDTH0/2, MY = 2*HEIGHT0/3 - base/2 + EM;

    // Paints the mound and targets on the panel
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
      g.setColor(Color.MAGENTA);
      g.drawLine(u_l, v_l, u0_l, v0_l);
      g.drawLine(u_r, v_r, u0_r, v0_r);
      // Draws the retina's targets
      for(int i = 0; i < ntargets; i++) {
	target t = targets[i];
	int x = g.getFontMetrics().charWidth(t.n[0])/2, y = g.getFontMetrics().getDescent();
	if (t.n[0] != ' ') {
	  g.setColor(t.c);
	  if (t.u_l >= 0 )
	    g.drawChars(t.n, 0, 1, t.u_l - x, t.v_l + y);
	  if (t.u_r >= 0 )
	    g.drawChars(t.n, 0, 1, t.u_r - x, t.v_r + y);
	  g.drawChars(t.n, 0, 1, t.u - x, t.v + y);
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
      setMount(gaze, vergence, pitch, pan, tilt);
      // Resets the targets
      for(int i = 0; i < ntargets; i++)
	if (targets[i].n[0] != ' ')
	  setTarget(i, targets[i].X, targets[i].Y, targets[i].Z);
      // Repaints
      repaint(); 
      Thread.yield();
    }

    // Calculates the mount parameters
    private void setMount(double gaze, double vergence, double pitch, double pan, double tilt) {
      // Time filtering
      this.gaze = this.gaze + a_eye * (gaze - this.gaze);
      this.vergence = this.vergence + a_eye * (vergence - this.vergence);
      this.pitch = this.pitch + a_eye * (pitch - this.pitch);
      this.pan = this.pan + a_neck * (pan - this.pan);
      this.tilt = this.tilt + a_neck * (tilt - this.tilt);
      // Bounds and converts angles
      double K = Math.PI/180; 
      if (this.gaze < -45) this.gaze = -45; if (this.gaze > 45) this.gaze = 45; this.gaze *= K;
      if (this.vergence < 0) this.vergence = 0; if (this.vergence > 60) this.vergence = 60; this.vergence *= K;
      if (this.pitch < -30) this.pitch = -30; if (this.pitch > 30) this.pitch = 30; this.pitch *= K;
      if (this.pan < -30) this.pan = -30; if (this.pan > 30) this.pan = 30; this.pan *= K;
      if (this.tilt < -30) this.tilt = -30; if (this.tilt > 30) this.tilt = 30; this.tilt *= K;
      // Generates the horizontal drawing points
      double c_pan = Math.cos(this.pan), s_pan = Math.sin(this.pan);
      u_l = (int) (MX - base2 * c_pan); u_r = (int) (MX + base2 * c_pan);
      v_l = (int) (MY - base2 * s_pan); v_r = (int) (MY + base2 * s_pan);
      double a_lr = this.pan + this.gaze + Math.PI/2, a_l = a_lr + this.vergence, a_r =  a_lr - this.vergence;
      double c_a_l = Math.cos(a_l), s_a_l = Math.sin(a_l), c_a_r = Math.cos(a_r), s_a_r = Math.sin(a_r);
      u0_l = (int) (u_l - 1e3 * c_a_l); v0_l = (int) (v_l - 1e3 * s_a_l);
      u0_r = (int) (u_r - 1e3 * c_a_r); v0_r = (int) (v_r - 1e3 * s_a_r);
      // Generates the vertical drawing ellipse
      double dl = EM2 * (1 + Math.sin(this.pitch + this.tilt));
      drawOval(ue_l, ve_l, u_l, v_l, a_l, EM, dl);
      drawOval(ue_r, ve_r, u_r, v_r, a_r, EM, dl);
    }
    private double gaze, vergence, pitch, pan, tilt;
    private int u_l, v_l, u_r, v_r, u0_l, v0_l, u0_r, v0_r, N = 256, ue_l[] = new int[N], ve_l[] = new int[N], ue_r[] = new int[N], ve_r[] = new int[N];

    // Generates the polygon of an oval of center (u0, v0), angle a and axes (L, l)
    private static void drawOval(int u[], int v[], int u0, int v0, double a, double L, double l) {
      int N = u.length; if (v.length < N) N = v.length;
      for(int n = 0; n < N; n++) {
	double t = 2 * n * Math.PI / (N - 1), c_t = Math.cos(t), s_t = Math.sin(t), c_a = Math.cos(a), s_a = Math.sin(a);
	u[n] = (int) (u0 + L * c_t * s_a + l * s_t * c_a);
	v[n] = (int) (v0 + l * s_t * s_a - L * c_t * c_a);
      }
    }

    // Defines a punctual target
    private static class target { char n[] = {' '}; Color c; double X, Y, Z; int u_l, v_l, u_r, v_r, u, v; }; private static final int ntargets = 4; 
    private target targets[] = new target[ntargets]; { for(int i = 0; i < ntargets; i++) targets[i] = new target(); }

    /** Sets the location of a target.
     * @param i Target's index, between {0, 4{.
     * @param X Horizontal 3D location.
     * @param Y Depth 3D location.
     * @param Z Vertical 3D location.
     */
    public void set(int i, double X, double Y, double Z) {
      if(0 <= i && i < 4) {
	setTarget(i, X, Y, Z);
	// Repaints
	repaint(); 
	Thread.yield();
      }
    }

    // Calculates the target projection
    private void setTarget(int i, double X, double Y, double Z) {
      target t = targets[i]; t.X = X; t.Y = Y; t.Z = Z; t.n[0] = '+'; t.c = i == 0 ? Color.RED : i == 1 ? Color.GREEN : i == 2 ? Color.MAGENTA : Color.BLACK;
      // Projection in the top-view
      t.u = (int) (MX + X); t.v = (int) (MY - Y); 
      //- System.out.println("P = (" + X + ", " + Y + ", " + Z+ ")");
      // Projection on each retina
      double Pl0, Pl1, Pl2, Pr0, Pr1, Pr2;
      {
	if (false) {
	  Pl0 = Math.cos(gaze + vergence + pan) * X - Math.sin(gaze + vergence + pan) * Y + Math.cos(gaze + vergence) * base / 0.2e1;
	  Pl1 = Math.sin(gaze + vergence + pan) * X + Math.cos(gaze + vergence + pan) * Y + Math.sin(gaze + vergence) * base / 0.2e1;
	  Pl2 = Z + elevation;
	  Pr0 = Math.cos(gaze - vergence + pan) * X - Math.sin(gaze - vergence + pan) * Y - Math.cos(gaze - vergence) * base / 0.2e1;
	  Pr1 = Math.sin(gaze - vergence + pan) * X + Math.cos(gaze - vergence + pan) * Y - Math.sin(gaze - vergence) * base / 0.2e1;
	  Pr2 = Z + elevation;
	} else {
	  Pl0 = Math.cos(gaze + vergence) * base / 0.2e1 + X * Math.cos(-gaze - vergence + pan) / 0.2e1 + X * Math.cos(gaze + vergence + pan) / 0.2e1 - X * Math.cos(pan - gaze - vergence - pitch) / 0.4e1 + X * Math.cos(pan + gaze + vergence + pitch) / 0.4e1 - X * Math.cos(pan - gaze - vergence + pitch) / 0.4e1 + X * Math.cos(pan + gaze + vergence - pitch) / 0.4e1 - Y * Math.sin(-tilt + gaze + vergence + pan) / 0.4e1 - Y * Math.sin(tilt - gaze - vergence + pan) / 0.4e1 - Y * Math.sin(-tilt - gaze - vergence + pan) / 0.4e1 - Y * Math.sin(tilt + pan + gaze + vergence + pitch) / 0.8e1 - Y * Math.sin(-tilt + pan + gaze + vergence + pitch) / 0.8e1 + Y * Math.sin(tilt + pan - gaze - vergence - pitch) / 0.8e1 + Y * Math.sin(-tilt + pan - gaze - vergence - pitch) / 0.8e1 - Y * Math.sin(tilt + pan + gaze + vergence - pitch) / 0.8e1 - Y * Math.sin(-tilt + pan + gaze + vergence - pitch) / 0.8e1 + Y * Math.sin(tilt + pan - gaze - vergence + pitch) / 0.8e1 + Y * Math.sin(-tilt + pan - gaze - vergence + pitch) / 0.8e1 + Y * Math.sin(tilt + gaze + vergence - pitch) / 0.4e1 - Y * Math.sin(-tilt + gaze + vergence - pitch) / 0.4e1 - Y * Math.sin(tilt + gaze + vergence + pitch) / 0.4e1 + Y * Math.sin(-tilt + gaze + vergence + pitch) / 0.4e1 + Z * Math.cos(-tilt + gaze + vergence + pan) / 0.4e1 - Z * Math.cos(tilt + gaze + vergence + pan) / 0.4e1 + Z * Math.cos(-tilt - gaze - vergence + pan) / 0.4e1 - Z * Math.cos(tilt - gaze - vergence + pan) / 0.4e1 + Z * Math.cos(-tilt + pan + gaze + vergence + pitch) / 0.8e1 - Z * Math.cos(tilt + pan + gaze + vergence + pitch) / 0.8e1 - Z * Math.cos(-tilt + pan - gaze - vergence - pitch) / 0.8e1 + Z * Math.cos(tilt + pan - gaze - vergence - pitch) / 0.8e1 + Z * Math.cos(-tilt + pan + gaze + vergence - pitch) / 0.8e1 - Z * Math.cos(tilt + pan + gaze + vergence - pitch) / 0.8e1 - Z * Math.cos(-tilt + pan - gaze - vergence + pitch) / 0.8e1 + Z * Math.cos(tilt + pan - gaze - vergence + pitch) / 0.8e1 + Z * Math.cos(-tilt + gaze + vergence - pitch) / 0.4e1 + Z * Math.cos(tilt + gaze + vergence - pitch) / 0.4e1 - Z * Math.cos(-tilt + gaze + vergence + pitch) / 0.4e1 - Z * Math.cos(tilt + gaze + vergence + pitch) / 0.4e1 - Y * Math.sin(tilt + gaze + vergence + pan) / 0.4e1 + elevation * Math.cos(gaze + vergence - pitch) / 0.2e1 - elevation * Math.cos(gaze + vergence + pitch) / 0.2e1;
	  Pl1 = Math.sin(gaze + vergence) * base / 0.2e1 + X * Math.sin(gaze + vergence + pan) / 0.2e1 - X * Math.sin(-gaze - vergence + pan) / 0.2e1 + X * Math.sin(pan + gaze + vergence - pitch) / 0.4e1 + X * Math.sin(pan - gaze - vergence + pitch) / 0.4e1 + X * Math.sin(pan + gaze + vergence + pitch) / 0.4e1 + X * Math.sin(pan - gaze - vergence - pitch) / 0.4e1 - Y * Math.cos(-tilt - gaze - vergence + pan) / 0.4e1 - Y * Math.cos(tilt - gaze - vergence + pan) / 0.4e1 + Y * Math.cos(-tilt + gaze + vergence + pan) / 0.4e1 + Y * Math.cos(tilt + gaze + vergence + pan) / 0.4e1 + Y * Math.cos(-tilt + pan - gaze - vergence + pitch) / 0.8e1 + Y * Math.cos(tilt + pan - gaze - vergence + pitch) / 0.8e1 + Y * Math.cos(-tilt + pan + gaze + vergence - pitch) / 0.8e1 + Y * Math.cos(tilt + pan + gaze + vergence - pitch) / 0.8e1 + Y * Math.cos(-tilt + pan - gaze - vergence - pitch) / 0.8e1 + Y * Math.cos(tilt + pan - gaze - vergence - pitch) / 0.8e1 + Y * Math.cos(-tilt + pan + gaze + vergence + pitch) / 0.8e1 + Y * Math.cos(tilt + pan + gaze + vergence + pitch) / 0.8e1 - Y * Math.cos(-tilt + gaze + vergence + pitch) / 0.4e1 + Y * Math.cos(tilt + gaze + vergence + pitch) / 0.4e1 + Y * Math.cos(-tilt + gaze + vergence - pitch) / 0.4e1 - Y * Math.cos(tilt + gaze + vergence - pitch) / 0.4e1 - Z * Math.sin(-tilt - gaze - vergence + pan) / 0.4e1 - Z * Math.sin(tilt + gaze + vergence + pan) / 0.4e1 + Z * Math.sin(-tilt + gaze + vergence + pan) / 0.4e1 - Z * Math.sin(tilt + pan - gaze - vergence + pitch) / 0.8e1 + Z * Math.sin(-tilt + pan - gaze - vergence + pitch) / 0.8e1 - Z * Math.sin(tilt + pan + gaze + vergence - pitch) / 0.8e1 + Z * Math.sin(-tilt + pan + gaze + vergence - pitch) / 0.8e1 - Z * Math.sin(tilt + pan - gaze - vergence - pitch) / 0.8e1 + Z * Math.sin(-tilt + pan - gaze - vergence - pitch) / 0.8e1 - Z * Math.sin(tilt + pan + gaze + vergence + pitch) / 0.8e1 + Z * Math.sin(-tilt + pan + gaze + vergence + pitch) / 0.8e1 - Z * Math.sin(tilt + gaze + vergence + pitch) / 0.4e1 - Z * Math.sin(-tilt + gaze + vergence + pitch) / 0.4e1 + Z * Math.sin(tilt + gaze + vergence - pitch) / 0.4e1 + Z * Math.sin(-tilt + gaze + vergence - pitch) / 0.4e1 - elevation * Math.sin(gaze + vergence + pitch) / 0.2e1 + elevation * Math.sin(gaze + vergence - pitch) / 0.2e1 + Z * Math.sin(tilt - gaze - vergence + pan) / 0.4e1;
	  Pl2 = X * Math.cos(-pitch + pan) / 0.2e1 - X * Math.cos(pitch + pan) / 0.2e1 + Y * Math.sin(tilt + pitch + pan) / 0.4e1 + Y * Math.sin(-tilt + pitch + pan) / 0.4e1 - Y * Math.sin(tilt - pitch + pan) / 0.4e1 - Y * Math.sin(-tilt - pitch + pan) / 0.4e1 + Y * Math.sin(pitch + tilt) / 0.2e1 - Y * Math.sin(pitch - tilt) / 0.2e1 - Z * Math.cos(-tilt + pitch + pan) / 0.4e1 + Z * Math.cos(tilt + pitch + pan) / 0.4e1 + Z * Math.cos(-tilt - pitch + pan) / 0.4e1 - Z * Math.cos(tilt - pitch + pan) / 0.4e1 + Z * Math.cos(pitch - tilt) / 0.2e1 + Z * Math.cos(pitch + tilt) / 0.2e1 + Math.cos(pitch) * elevation;
	  Pr0 = -Math.cos(gaze - vergence) * base / 0.2e1 + X * Math.cos(-gaze + vergence + pan) / 0.2e1 + X * Math.cos(gaze - vergence + pan) / 0.2e1 - X * Math.cos(pan - gaze + vergence - pitch) / 0.4e1 + X * Math.cos(pan + gaze - vergence + pitch) / 0.4e1 - X * Math.cos(pan - gaze + vergence + pitch) / 0.4e1 + X * Math.cos(pan + gaze - vergence - pitch) / 0.4e1 - Y * Math.sin(tilt + gaze - vergence + pan) / 0.4e1 - Y * Math.sin(-tilt + gaze - vergence + pan) / 0.4e1 - Y * Math.sin(tilt - gaze + vergence + pan) / 0.4e1 - Y * Math.sin(-tilt - gaze + vergence + pan) / 0.4e1 - Y * Math.sin(tilt + pan + gaze - vergence + pitch) / 0.8e1 - Y * Math.sin(-tilt + pan + gaze - vergence + pitch) / 0.8e1 + Y * Math.sin(tilt + pan - gaze + vergence - pitch) / 0.8e1 + Y * Math.sin(-tilt + pan - gaze + vergence - pitch) / 0.8e1 - Y * Math.sin(tilt + pan + gaze - vergence - pitch) / 0.8e1 - Y * Math.sin(-tilt + pan + gaze - vergence - pitch) / 0.8e1 + Y * Math.sin(tilt + pan - gaze + vergence + pitch) / 0.8e1 + Y * Math.sin(-tilt + pan - gaze + vergence + pitch) / 0.8e1 + Y * Math.sin(tilt + gaze - vergence - pitch) / 0.4e1 - Y * Math.sin(-tilt + gaze - vergence - pitch) / 0.4e1 - Y * Math.sin(tilt + gaze - vergence + pitch) / 0.4e1 + Y * Math.sin(-tilt + gaze - vergence + pitch) / 0.4e1 + Z * Math.cos(-tilt + gaze - vergence + pan) / 0.4e1 - Z * Math.cos(tilt + gaze - vergence + pan) / 0.4e1 + Z * Math.cos(-tilt - gaze + vergence + pan) / 0.4e1 - Z * Math.cos(tilt - gaze + vergence + pan) / 0.4e1 + Z * Math.cos(-tilt + pan + gaze - vergence + pitch) / 0.8e1 - Z * Math.cos(tilt + pan + gaze - vergence + pitch) / 0.8e1 - Z * Math.cos(-tilt + pan - gaze + vergence - pitch) / 0.8e1 + Z * Math.cos(tilt + pan - gaze + vergence - pitch) / 0.8e1 + Z * Math.cos(-tilt + pan + gaze - vergence - pitch) / 0.8e1 - Z * Math.cos(tilt + pan + gaze - vergence - pitch) / 0.8e1 - Z * Math.cos(-tilt + pan - gaze + vergence + pitch) / 0.8e1 + Z * Math.cos(tilt + pan - gaze + vergence + pitch) / 0.8e1 + Z * Math.cos(-tilt + gaze - vergence - pitch) / 0.4e1 + Z * Math.cos(tilt + gaze - vergence - pitch) / 0.4e1 - Z * Math.cos(-tilt + gaze - vergence + pitch) / 0.4e1 - Z * Math.cos(tilt + gaze - vergence + pitch) / 0.4e1 + elevation * Math.cos(gaze - vergence - pitch) / 0.2e1 - elevation * Math.cos(gaze - vergence + pitch) / 0.2e1;
	  Pr1 = -Math.sin(gaze - vergence) * base / 0.2e1 - Y * Math.cos(tilt - gaze + vergence + pan) / 0.4e1 + Y * Math.cos(-tilt + gaze - vergence + pan) / 0.4e1 + Y * Math.cos(tilt + gaze - vergence + pan) / 0.4e1 + Y * Math.cos(-tilt + pan - gaze + vergence + pitch) / 0.8e1 + Y * Math.cos(tilt + pan - gaze + vergence + pitch) / 0.8e1 + Y * Math.cos(-tilt + pan + gaze - vergence - pitch) / 0.8e1 + Y * Math.cos(tilt + pan + gaze - vergence - pitch) / 0.8e1 + Y * Math.cos(-tilt + pan - gaze + vergence - pitch) / 0.8e1 + Y * Math.cos(tilt + pan - gaze + vergence - pitch) / 0.8e1 + Y * Math.cos(-tilt + pan + gaze - vergence + pitch) / 0.8e1 + Y * Math.cos(tilt + pan + gaze - vergence + pitch) / 0.8e1 - Y * Math.cos(-tilt + gaze - vergence + pitch) / 0.4e1 + Y * Math.cos(tilt + gaze - vergence + pitch) / 0.4e1 + Y * Math.cos(-tilt + gaze - vergence - pitch) / 0.4e1 - Y * Math.cos(tilt + gaze - vergence - pitch) / 0.4e1 + X * Math.sin(gaze - vergence + pan) / 0.2e1 - X * Math.sin(-gaze + vergence + pan) / 0.2e1 + X * Math.sin(pan + gaze - vergence - pitch) / 0.4e1 + X * Math.sin(pan - gaze + vergence + pitch) / 0.4e1 + X * Math.sin(pan + gaze - vergence + pitch) / 0.4e1 + X * Math.sin(pan - gaze + vergence - pitch) / 0.4e1 - Y * Math.cos(-tilt - gaze + vergence + pan) / 0.4e1 + Z * Math.sin(tilt - gaze + vergence + pan) / 0.4e1 - Z * Math.sin(-tilt - gaze + vergence + pan) / 0.4e1 - Z * Math.sin(tilt + gaze - vergence + pan) / 0.4e1 + Z * Math.sin(-tilt + gaze - vergence + pan) / 0.4e1 - Z * Math.sin(tilt + pan - gaze + vergence + pitch) / 0.8e1 + Z * Math.sin(-tilt + pan - gaze + vergence + pitch) / 0.8e1 - Z * Math.sin(tilt + pan + gaze - vergence - pitch) / 0.8e1 + Z * Math.sin(-tilt + pan + gaze - vergence - pitch) / 0.8e1 - Z * Math.sin(tilt + pan - gaze + vergence - pitch) / 0.8e1 + Z * Math.sin(-tilt + pan - gaze + vergence - pitch) / 0.8e1 - Z * Math.sin(tilt + pan + gaze - vergence + pitch) / 0.8e1 + Z * Math.sin(-tilt + pan + gaze - vergence + pitch) / 0.8e1 - Z * Math.sin(tilt + gaze - vergence + pitch) / 0.4e1 - Z * Math.sin(-tilt + gaze - vergence + pitch) / 0.4e1 + Z * Math.sin(tilt + gaze - vergence - pitch) / 0.4e1 + Z * Math.sin(-tilt + gaze - vergence - pitch) / 0.4e1 - elevation * Math.sin(gaze - vergence + pitch) / 0.2e1 + elevation * Math.sin(gaze - vergence - pitch) / 0.2e1;
	  Pr2 = X * Math.cos(-pitch + pan) / 0.2e1 - X * Math.cos(pitch + pan) / 0.2e1 + Y * Math.sin(tilt + pitch + pan) / 0.4e1 + Y * Math.sin(-tilt + pitch + pan) / 0.4e1 - Y * Math.sin(tilt - pitch + pan) / 0.4e1 - Y * Math.sin(-tilt - pitch + pan) / 0.4e1 + Y * Math.sin(pitch + tilt) / 0.2e1 - Y * Math.sin(pitch - tilt) / 0.2e1 - Z * Math.cos(-tilt + pitch + pan) / 0.4e1 + Z * Math.cos(tilt + pitch + pan) / 0.4e1 + Z * Math.cos(-tilt - pitch + pan) / 0.4e1 - Z * Math.cos(tilt - pitch + pan) / 0.4e1 + Z * Math.cos(pitch - tilt) / 0.2e1 + Z * Math.cos(pitch + tilt) / 0.2e1 + Math.cos(pitch) * elevation;
	}
      }
      double x_l = Pl0 / Pl1, y_l = Pl2 / Pl1, x_r = Pr0 / Pr1, y_r = Pr2 / Pr1;
      if (0 < Pl1 && Math.abs(x_l) < 1 && Math.abs(y_l) < 1) {
	t.u_l = EM + EW/2 + (int) (EW/2.1 * x_l);
	t.v_l = EY + EH/2 + (int) (EH/2.1 * y_l);
      } else {
	t.u_l = t.v_l = -1;
      }
      if (0 < Pr1 && Math.abs(x_r) < 1 && Math.abs(y_r) < 1) {
	t.u_r = EM + 3*EW/2 + (int) (EW/2.1 * x_r);
	t.v_r = EY + EH/2   + (int) (EH/2.1 * y_r);
      } else {
	t.u_r = t.v_r = -1;
      }
    }
  }

  //
  // This defines the tests on the panel
  //

  /** Test du panel. */
  static void test() {
    panel.set(0, 0, 200, 0);
    panel.set(1, 100, 100, 0);
    for(int a = -45; a <= 45; a += 9) {
      panel.set(a, a < 0 ? 0 : a / 2, 0, a / 4, 0);
      Macros.sleep(2000);
    }
    //-panel.set(30, 0, 0, 0, 0);
  }

  /** Définition de l'interface graphique de la proglet. */
  public static final Panel panel = new Panel();
}
