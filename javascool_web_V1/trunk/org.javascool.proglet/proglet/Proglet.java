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
import java.awt.Point;

// Used to test as applet
import javax.swing.JApplet;
import javax.swing.JButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Used to report a throwable
import java.lang.reflect.InvocationTargetException;

/** This factory allows to interface with proglets.
 * By contract, a proglet must: <ul>
 *  <li>Define <tt>public static final JPanel panel;</tt> which is the static instantiation of the proglet's swing panel.</li>
 *  <li>Its sizes as the panel preferred sizes must be within <tt>[540 x 580]</tt>.</li>
 *  <li>Provide a <tt>static void test()</tt> method to test/demo the proglet panel.</li>
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
    System.out.println(error.toString());
    System.err.println(error.toString());
    for(int i = 0; i < 4 && i < error.getStackTrace().length; i++)
      System.err.println(error.getStackTrace()[i]);
  }

  /** Used to test a proglet as a standalone program. 
   * @param usage <tt>java proglet.Proglet &lt;edit|run> &lt;proglet-name></tt>
   * <hr/>Applet usage: <tt>&lt;applet code="proglet.InterfacePrincipale.class" width="560" height="720">&lt;param name="proglet" value=" &lt;proglet-name>"/>&lt;/applet></tt>
   */
  public static void main(String usage[]) { 
    InterfacePrincipale applet = new InterfacePrincipale(); applet.setEdit("edit".equals(usage[0])); applet.setProglet(usage[1]); 
    show(applet, "javascool'proglet editor for «"+usage[1].toLowerCase()+"»", new Point(570, 0), 560, 720);
  }

  /** Opens an applet in a standalone frame.
   * @param applet The applet to display.
   * @param title  Frame title. If null no title.
   * @param where  Position on the screen. If null set to the default value.
   * @param width  Applet width.
   * @param height Applet height.
   * @return The opened frame.  Use <tt>frame.dispose()</tt> to close the frame.
   */
  public static JFrame show(JApplet applet, String title, Point where, int width, int height) {
    JFrame f = new JFrame(); f.getContentPane().add(applet); applet.init(); f.pack(); 
    if (title != null) f.setTitle(title); f.setSize(width, height); if (where != null) { f.setLocationByPlatform(false); f.setLocation(where); } f.setVisible(true); 
    return f;
  }
}



