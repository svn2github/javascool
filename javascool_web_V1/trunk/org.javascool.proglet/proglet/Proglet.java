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
  private static final long serialVersionUID = 1L;

  /** Constructs a proglet attached to the related applet.
   * @param applet The related applet, set to null when run in a standalone application context.
   * @param proglet The proglet class name.
   * @return The static instanciation of the proglet.
   */
  static JPanel getPanel(Applet applet, String proglet) {
    try {
      base = applet.getCodeBase().toString()+"/img/"; 
    } catch(Exception e) {
      base  = "file:img/";
    }
    try { return (JPanel) Class.forName("proglet."+proglet).getField("panel").get(null); } 
    catch(Exception e) { System.err.println(e+" (unkown proglet "+proglet+")"); return new JPanel(); }
  }
  static private Applet applet = null;

  /** Runs one proglet's test.
   * @param proglet The proglet class name.
   */
  static void test(String proglet) {
    Proglet.proglet = proglet;
    new Thread(new Runnable() { public void run() {
      try { Class.forName("proglet."+Proglet.proglet).getDeclaredMethod("test").invoke(null); } catch(Exception error) { report(error); }
    }}).start();
  }
  private static String proglet;

  /** Reports a throwable with the related context.
   * @param error The error or exception to report.
   */
  static void report(Throwable error) {
    if (error instanceof InvocationTargetException) report(error.getCause());
    System.err.println(error.toString());
    System.err.println(error.getStackTrace()[0]+"\n"+error.getStackTrace()[1]+"\n"+error.getStackTrace()[2]+"\n"+error.getStackTrace()[3]);
  }

  /** Echos a string in the console.
   * @param string The string to echo.
   */
  public static void echo(String string) {
    System.out.println(string);
  }

  /** Returns an icon loaded from in the applet context.
   * @param file The icon file name. The icon must be located in the img directory.
   * @return The related image icon or an empty icon if not loaded.
   */
  static ImageIcon getIcon(String file) {
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
   * @param usage <tt>java proglet.Proglet &lt;proglet-name> [-test]</tt>
   * <hr/>Applet usage: <tt>&lt;applet code="proglet.InterfacePrincipale.class" width="920" height="720">&lt;param name="proglet" value=" &lt;proglet-name>"/>&lt;/applet></tt>
   */
  public static void main(String usage[]) { 
    InterfacePrincipale applet = new InterfacePrincipale(); applet.setProglet(usage[0]);
    JFrame f = new JFrame(); f.getContentPane().add(applet); applet.init(); f.pack(); f.setSize(920, 720); f.setVisible(true); 
    if (usage.length >= 2 && "-test".equals(usage[1])) 
      test(usage[0]);
  }

  /*
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
  */
}



