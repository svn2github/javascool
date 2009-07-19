/*******************************************************************************
 * Thierry.Vieville@sophia.inria.fr, Copyright (C) 2009.  All rights reserved. *
 *******************************************************************************/

package proglet;

// Used to define the gui
import java.applet.Applet;
import javax.swing.JPanel;
import java.awt.BorderLayout;

// Used to load the icon
import javax.swing.ImageIcon;
import java.net.URL;

// Used to test as standalone
import javax.swing.JFrame;

// Used to test as applet
import javax.swing.JApplet;
import javax.swing.JButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Used to report a throwable
import java.lang.reflect.InvocationTargetException;

/** This factory allows to interface with proglets.
 * By contract, a proglet must define: <ul>
 *  <li><tt>public static final JPanel panel;</tt> which is the static instantiation of the proglet's swing panel.</li>
 *  <li>Its sizes as the panel preferred sizes.</li>
 *  <li>A <tt>static void test()</tt> to test the proglet panel.</li>
 * </ul>
 */
public class Proglet {
  /** Constructs a proglet attached to the related applet.
   * @param applet The related applet, set to null when run in a standalone application context.
   * @param proglet The proglet class name.
   * @return The static instanciation of the proglet.
   */
  public static JPanel getPanel(Applet applet, String proglet) {
    base = (Proglet.applet = applet) == null ? "file:img/" : applet.getCodeBase().toString()+"/img/"; 
    try { return (JPanel) Class.forName("proglet."+proglet).getField("panel").get(null); } 
    catch(Exception e) { System.err.println(e+" (unkown proglet "+proglet+")"); return new JPanel(); }
  }
  static private Applet applet = null;

  /** Runs one proglet's test.
   * @param proglet The proglet class name.
   */
  public static void test(String proglet) {
    Proglet.proglet = proglet;
    new Thread(new Runnable() { public void run() {
      try { Class.forName("proglet."+Proglet.proglet).getDeclaredMethod("test").invoke(null); } catch(Exception error) { report(error); }
    }}).start();
  }
  private static String proglet;
  // Reports a throwable with the related context.
  private static void report(Throwable error) {
    if (error instanceof InvocationTargetException) report(error.getCause());
    System.err.println("At:\n"+error.getStackTrace()[0]+"\n"+error.getStackTrace()[1]+"\n"+error.getStackTrace()[2]+"\n"+error.getStackTrace()[3]+"\n");
    System.err.println(error.toString());
  }

  /** Returns an icon loaded from in the applet context.
   * @param file The icon file name. The icon must be located in the img directory.
   * @return The related image icon or an empty icon if not loaded.
   */
  public static ImageIcon getIcon(String file) {
    try { return new ImageIcon(new URL(base+file)); } catch(Exception err) { System.err.println(err); return new ImageIcon(); }
  }
  private static String base = "file:img/";

  /** Sleeps and purge the graphic's drawings.
   * @param delay Delay in msec.
   */
  public static void sleep(int delay) {
    try { if (delay > 0) Thread.sleep(delay); else Thread.yield(); } catch(Exception e) { }
  }
  static boolean purge = true;

  /** Used to test a proglet as a standalone program. 
   * @param usage <tt>java Proglet &lt;proglet-name></tt>
   */
  public static void main(String usage[]) { 
    JFrame f = new JFrame(); f.setSize(550, 550); f.getContentPane().add(getPanel(null, usage[0])); f.pack(); f.setVisible(true); test(usage[0]);
  }

  /** Used to test a proglet in a browser. 
   * Usage: <tt>&lt;applet code="Proglet$Test.class" width="550" height="550">&lt;param name="proglet" value=" &lt;proglet-name>"/>&lt;/applet></tt>
   */
  public static class Test extends JApplet { 
    public void init() { 
      getContentPane().setLayout(new BorderLayout()); 
      JButton t = new JButton("test"); getContentPane().add(t, BorderLayout.NORTH); 
      t.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { 
	Proglet.test(getParameter("proglet"));
      }});
      getContentPane().add(Proglet.getPanel(this, getParameter("proglet")));
    }
  }
}


