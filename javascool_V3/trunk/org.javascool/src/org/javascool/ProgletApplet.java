/*******************************************************************************
 * Thierry.Vieville@sophia.inria.fr, Copyright (C) 2009.  All rights reserved. *
 *******************************************************************************/

package org.javascool;

// Used to encapsulate a proglet
import javax.swing.JPanel;
import javax.swing.JApplet;
import java.awt.BorderLayout;
import javax.swing.JToolBar;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;

/** Defines a proglet applet. */
public class ProgletApplet extends JApplet { 
  private static final long serialVersionUID = 1L;
  /** Programmatic reset of the proglet parameters. 
   * <ul><li>Usually defined in the HTML tag: 
   * <div><tt>&lt;applet code="org.javascool.ProgletApplet" archive="javascool.jar" width="560" height="720"></tt></div>
   * <div><tt>&lt;param name="proglet" value="ingredients"/></tt></div>
   * <div><tt>&lt;param name="demo" value="true"/></tt></div>
   * <div><tt>&lt;/applet></tt></div>
   * </li><li>Or used in a standalone program:
   * <div><tt>Utils.show(new Jvs2Java.ProgletApplet().reset("ingredients", true), "javascool proglet", 650, 720);</tt></div>
   * </li></ul>
   * @param proglet The corresponding proglet name. Default is "ingredients".
   * @param demo If true runs the demo program. If false runs the proglet pupil's program. Default is "true".
   * @return This, allowing to use the <tt>new ProgletApplet().reset(..)</tt> construct.
   *
   * @see <a href="ProgletApplet.java.html">source code</a>
   * @serial exclude
   */
  public ProgletApplet reset(String proglet, boolean demo) { this.proglet = proglet; this.demo = demo; return this; } 
  private String proglet = "ingredients"; private boolean demo = true;
  /**/public void init() {
    // Init the parameters from the HTML tags
    try { String p  = getParameter("proglet"); if (p != null) proglet = p; } catch(Exception e) { }
    try { String p  = getParameter("demo"); if (p != null) demo = p.toLowerCase().equals("true"); } catch(Exception e) { }
    // Builds the GUI
    JToolBar bar = new JToolBar();
    bar.setRollover(false);
    bar.setFloatable(false);
    bar.add(new JLabel("Proglet \""+proglet+"\" : "));
    bar.add(new JButton(new AbstractAction(demo ? "Démonstration" : "Executer", Utils.getIcon("org/javascool/doc-files/icones16/play.png")) {
	private static final long serialVersionUID = 1L;
	public void actionPerformed(ActionEvent e) {
	  if (demo) {
	    Jvs2Java.run(proglet);
	  } else {
	    Jvs2Java.run(true);
	  }
	}}));
    bar.add(new JButton(new AbstractAction("Arrêter", Utils.getIcon("org/javascool/doc-files/icones16/Stop_16x16.png")) {
	private static final long serialVersionUID = 1L;
	public void actionPerformed(ActionEvent e) {
	  Jvs2Java.run(false);
	}}));
    getContentPane().add(bar, BorderLayout.NORTH);
    getContentPane().add(Jvs2Java.getPanel(proglet), BorderLayout.CENTER);
  }
}
