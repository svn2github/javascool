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

/** This factory allows to integrate proglets in the Java environments.
 * @see <a href="Proglets.java">source code</a>
 */
public class Proglets { private Proglets() { }
  private static final long serialVersionUID = 1L;

  /** Defined all declared proglets. */
  static final String proglets[] = new String[] { "Konsol", "Dicho", "Smiley", "Scope", "Conva", "Synthe", "Tortue" };

  /** Constructs a proglet attached to the related applet.
   * @param proglet The proglet class name.
   * @return The static instanciation of the proglet.
   */
  static public JPanel getPanel(String proglet) {
    try { return (JPanel) Class.forName("proglet."+proglet).getField("panel").get(null); } 
    catch(Exception e) { System.err.println(e+" (unkown proglet "+proglet+")"); return new JPanel(); }
  }

  /** Returns an icon loaded from the applet context.
   * @param file The icon file name. The icon must be located in the <tt>img/</tt> directory (directory on the server or on the client side or in the jar).
   * @return The related image icon or an empty icon if not loaded.
   */
  static ImageIcon getIcon(String file) {
    try { return newImageIcon(file, Thread.currentThread().getContextClassLoader().getResource("img/"+file)); } catch(Exception e1) {
      try { return newImageIcon(file, new URL("http://javascool.gforge.inria.fr/proglet/img/"+file)); } catch(Exception e2) {
	System.err.println("Unable to load the '"+file+"' icon, check your configuration or your img/ files ("+e1+";"+e2+")"); return new ImageIcon(); 
      }
    }
  }
  private static ImageIcon newImageIcon(String file, URL url) throws Exception { /*System.err.println("Notice: loading " + file + " @ " + url);*/ return new ImageIcon(url); }

  /** Runs one proglet's test.
   * @param proglet The proglet class name.
   */
  static public void test(String proglet) {
    proglet = toUcfirst(proglet);
    try { Class.forName("proglet."+proglet).getDeclaredMethod("test").invoke(null); } catch(Exception error) { }
  }
  private static String toUcfirst(String string) { return Character.toUpperCase(string.charAt(0)) + string.substring(1).toLowerCase(); }

  /** Reports a throwable with the related context.
   * @param error The error or exception to report.
   */
  static void report(Throwable error) {
    if (error instanceof InvocationTargetException) report(error.getCause());
    System.out.println(error.toString());
    System.err.println(error.toString());
    for(int i = 0; i < 4 && i < error.getStackTrace().length; i++)
      System.err.println(error.getStackTrace()[i]);
  }

  /** Used to run a proglet as a standalone program. 
   * @param usage <tt>java proglet.Proglets [edit|run] [proglet-name]</tt>
   */
  public static void main(String usage[]) { 
    InterfacePrincipale applet = new InterfacePrincipale(); 
    String proglet = usage.length == 2 ? usage[1] : usage.length == 1 ? usage[0] : "Konsol"; applet.setProglet(proglet); 
    boolean edit = usage.length == 2 ? "edit".equals(usage[0]) : true; applet.setEdit(edit); 
    show(applet, "javascool proglet editor", new Point(570, 0), 560, 720);
  }

  /** Opens an proglet pannel in a standalone frame.
   * @param proglet  Proglets name.
   */
  public static void show(String proglet) {
    proglet = toUcfirst(proglet);
    InterfacePrincipale applet = new InterfacePrincipale(); applet.setProglet(proglet); applet.init();
    JFrame f = new JFrame(); f.getContentPane().add(applet); f.setTitle(proglet); f.pack(); f.setSize(560, 695); f.setVisible(true); 
  }

  /** Opens an applet in a standalone frame.
   * @param applet The applet to display.
   * @param title  Frame title. If null no title.
   * @param where  Position on the screen. If null set to the default value.
   * @param width  Applet width.
   * @param height Applet height.
   * @return The opened frame.  Use <tt>frame.dispose()</tt> to close the frame.
   */
  static JFrame show(JApplet applet, String title, Point where, int width, int height) {
    JFrame f = new JFrame(); f.getContentPane().add(applet); applet.init(); f.pack(); 
    if (title != null) f.setTitle(title); f.setSize(width, height); if (where != null) { f.setLocationByPlatform(false); f.setLocation(where); } f.setVisible(true); 
    return f;
  }
}