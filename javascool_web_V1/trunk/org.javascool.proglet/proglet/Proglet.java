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
  public static JPanel getPanel(Applet applet, String proglet) {
    Proglet.applet = applet;
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

  /** Reports a throwable with the related context.
   * @param error The error or exception to report.
   */
  public static void report(Throwable error) {
    if (error instanceof InvocationTargetException) report(error.getCause());
    System.err.println(error.toString());
    System.err.println(error.getStackTrace()[0]+"\n"+error.getStackTrace()[1]+"\n"+error.getStackTrace()[2]+"\n"+error.getStackTrace()[3]);
  }

  /** Echos a string in the console.
   * @param string The string to echo.
   */
  public static void echo(String string) {
    try { ((InterfacePrincipale) applet).echo(string, 'c'); } catch(Exception e) { System.out.println(string); }
  }

  /** Returns true of two strings are equals else false.
   * @param string1 The string to compare.
   * @param string2 The other string to compre.
   */
  public static boolean equals(String string1, String string2) { return string1.equals(string2); }

  /** Returns an icon loaded from the applet context.
   * @param file The icon file name. The icon must be located in the <tt>img/</tt> directory (directory on the server or on the client side or in the jar).
   * @return The related image icon or an empty icon if not loaded.
   */
  static ImageIcon getIcon(String file) {
    try { return new ImageIcon(ClassLoader.getSystemResource("img/"+file)); } catch(Exception e1) {
      try { return new ImageIcon(new URL(applet.getCodeBase().toString()+"/img/"+file)); } catch(Exception e2) { 
	try { return new ImageIcon(new URL("file:img/"+file)); } catch(Exception e3) { 
	  System.err.println("Unable to load the '"+file+"' icon, check your configuration or your img/ files"); return new ImageIcon(); 
	}
      }
    }
  }

  /** Sleeps and purge the graphic's drawings.
   * @param delay Delay in msec.
   */
  public static void sleep(int delay) {
    try { if (delay > 0) Thread.sleep(delay); else Thread.yield(); } catch(Exception e) { }
  }

  /** Used to test a proglet as a standalone program. 
   * @param usage <tt>java proglet.Proglet &lt;proglet-name></tt>
   * <hr/>Applet usage: <tt>&lt;applet code="proglet.InterfacePrincipale.class" width="920" height="720">&lt;param name="proglet" value=" &lt;proglet-name>"/>&lt;/applet></tt>
   */
  public static void main(String usage[]) { 
    InterfacePrincipale applet = new InterfacePrincipale(); applet.setProglet(usage[0]);
    JFrame f = new JFrame(); f.getContentPane().add(applet); applet.init(); f.pack(); f.setSize(920, 720); f.setVisible(true); 
  }
}



