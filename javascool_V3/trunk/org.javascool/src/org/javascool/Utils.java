/*******************************************************************************
 * Thierry.Vieville@sophia.inria.fr, Copyright (C) 2010.  All rights reserved. *
 *******************************************************************************/

package org.javascool;

// Used for URL read
import java.net.URL;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.lang.StringBuilder;
import java.net.URLEncoder;

// Used for URL write
import java.net.URL;
import java.io.IOException;
import java.lang.System; // .out.println
import java.net.URLConnection;
import java.io.OutputStreamWriter;
import java.io.File;
import java.io.FileWriter;

// Used to load/save images
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.io.OutputStream;
import java.io.File;

// Used for string to name and file-name conversion
import java.util.HashMap;
import java.io.File;

// Used for the sax interface
import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.transform.Transformer;
import javax.xml.transform.Templates;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.stream.StreamResult;

// Used to report a throwable
import java.lang.reflect.InvocationTargetException;

// Used to frame an applet as standalone application
import javax.swing.JFrame;
import java.awt.Dimension;
import javax.swing.JApplet;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.KeyboardFocusManager;
import java.awt.KeyEventDispatcher;
import java.awt.event.KeyEvent;

/** This factory contains useful methods to interface javascool with the environment. 
 * @see <a href="../../org/javascool/Utils.java">source code</a>
 */
public class Utils { private Utils() { }
  private static final long serialVersionUID = 1L;

  /** Runs an operating-system command, waits until completion and returns the output.
   * @param command The operating systems command with its arguments separated by either tabulation ("\t" char) if any else space (" " char).
   * @param timeout Timeout in second or 0 if no timeout. Default is 10.
   * @return The command output (standard output and error output) including '\n'.
   *
   * @throws RuntimeException if an I/O exception occurs during command execution.
   * @throws IllegalStateException If the command returned status is non zero or if a time-out occurs.
   */
  public static String exec(String command, int timeout) {
    try {
      StringBuffer output = new StringBuffer();
      long time = timeout > 0 ? System.currentTimeMillis() + 1000 * timeout : 0;
      Process process = Runtime.getRuntime().exec(command.trim().split((command.indexOf('\t') == -1) ? " " : "\t"));
      InputStreamReader stdout = new InputStreamReader(process.getInputStream());
      InputStreamReader stderr = new InputStreamReader(process.getErrorStream());
      for(boolean waitfor = true; waitfor;) { waitfor = false; Thread.yield();
	while (stdout.ready()) { waitfor = true; output.append((char) stdout.read()); }
	while (stderr.ready()) { waitfor = true; output.append((char) stderr.read()); }
	if (!waitfor) { try { process.exitValue(); } catch(IllegalThreadStateException e1) { try { Thread.sleep(100); } catch(Exception e2) { } waitfor = true; } }
	if ((time > 0) && (System.currentTimeMillis() > time)) throw new IllegalStateException("Command {"+command+"} timeout (>"+timeout+"s) output=["+output+"]\n");
      }
      stdout.close();
      stderr.close();
      // Terminates the process
      process.destroy(); try { process.waitFor(); } catch(Exception e) { } Thread.yield();
      if (process.exitValue() != 0) throw new IllegalStateException("Command {"+command+"} error #"+process.exitValue()+" output=[\n"+output+"\n]\n");
      return output.toString(); 
    } catch(IOException e) {
      throw new RuntimeException(e+" when executing: "+command); 
    }
  }  
  /**/public static String exec(String command) {
    return exec(command , 10);
  }

  /** Loads an URL textual contents and returns it as a string.
   *
   * @param location A Universal Resource Location of the form: <table align="center"> 
   * <tr><td><tt>http:/<i>path-name</i></tt></td><td>to load from a HTTP location</td></tr>
   * <tr><td><tt>http:/<i>path-name</i>?param_i=value_i&amp;..</tt></td><td>to get a form answer</td></tr>
   * <tr><td><tt>ftp:/<i>path-name</i></tt></td><td>to load from a FTP site</td></tr>
   * <tr><td><tt>file:/<i>path-name</i></tt></td><td>to load from a file</td></tr>
   * <tr><td><tt>jar:/<i>jar-path-name</i>!/<i>jar-entry</i></tt></td><td>to load from a JAR archive
   *  <div>(e.g.:<tt>jar:http://javascool.gforge.inria.fr/javascool.jar!/META-INF/MANIFEST.MF</tt>)</div></td></tr>
   * </table>
   *
   * @throws RuntimeException if an I/O exception has occurred.
   */
  public static String loadString(String location) {
    location = toUrl(location);
    try {
      BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(location).openStream()), 10240);
      StringBuilder buffer = new StringBuilder(); char chars[] = new char[10240];
      while(true) { int l = reader.read(chars); if (l == -1) break; buffer.append(chars, 0, l); }
      return buffer.toString();
    } catch(IOException e) {
      throw new RuntimeException(e+" when loading: "+location); 
    }
  }

  /** Saves a char-sequence in an URL textual contents.
   *
   * @param location @optional<"stdout:"> A Universal Resource Location of the form: <table>
   * <tr><td><tt>ftp:/<i>path-name</i></tt></td><td>to save onto a FTP site.</td></tr>
   * <tr><td><tt>file:/<i>path-name</i></tt></td><td>to save into a file.</td></tr>
   * <tr><td><tt>mailto:<i>address</i>?subject=<i>subject</i></tt></td><td>to send as an email in a readable form.</td></tr>
   * <tr><td><tt>stdout:/</tt></td><td>to print to the terminal standard output.</td></tr>
   * </table>
   * 
   * @param string The string to save.
   *
   * @throws RuntimeException if an I/O exception has occurred.
   */
  public static void saveString(String location, String string) {
    location = toUrl(location);
    try {
      if (location.startsWith("stdout:")) { System.out.println(location+" "+string); return; }
      OutputStreamWriter writer = location.startsWith("file:") ? getFileWriter(location.substring(5)) : getUrlWriter(location);
      for(int i = 0; i < string.length(); i++) writer.write(string.charAt(i)); writer.close();
    } catch(IOException e) {
      throw new RuntimeException(e+" when saving: "+location); 
    }
  }
  private static OutputStreamWriter getUrlWriter(String location) throws IOException {
    URL url = new URL(location); URLConnection connection = url.openConnection(); connection.setDoOutput(true); 
    OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
    if (url.getProtocol().equals("mailto")) { int i = url.toString().indexOf("?subject="); if (i != -1) writer.write("Subject: " + url.toString().substring(i + 9) + "\n"); }
    return writer;
  }
  private static OutputStreamWriter getFileWriter(String location) throws IOException {
    File file = new File(location), parent = file.getParentFile(); if ((parent != null) && (!parent.isDirectory())) parent.mkdirs();
    return new FileWriter(location);
  }

  /** Returns an icon loaded from the applet context.
   * @param file The icon file name The icon must be located in the context directory (directory on the server or on the client side or in the jar).
   * @return The related image icon or an empty icon if not loaded.
   */
  public static ImageIcon getIcon(String file) {
    try { return new ImageIcon(MainV2.class.getClassLoader().getResource(file)); } catch(Exception e1) {
      try { System.out.println("Warning: loading "+file+" via gforge");
	return new ImageIcon(new URL("http://javascool.gforge.inria.fr/v3/api/"+file)); } catch(Exception e2) {
	System.err.println("Unable to load the '"+file+"' icon, check your configuration or your imgage files ("+e1+";"+e2+")"); 
	return new ImageIcon(); 
      }
    }
  }

  /** Loads an image from the given location.
   *
   * @param location A Universal Resource Location of the form: <table> 
   * <tr><td><tt>http:/<i>path-name</i></tt></td><td>to load from a HTTP location</td></tr>
   * <tr><td><tt>ftp:/<i>path-name</i></tt></td><td>to load from a FTP site</td></tr>
   * <tr><td><tt>file:/<i>path-name</i></tt></td><td>to load from a file</td></tr>
   * <tr><td><tt>jar:/<i>jar-path-name</i>!/<i>jar-entry</i></tt></td><td>to load from a JAR archive</td></tr>
   * </table>
   *
   * @throws RuntimeException if an I/O exception has occurred.
   */
  public static BufferedImage loadImage(String location) {
    location = toUrl(location);
    BufferedImage image = null; 
    try { 
      image = ImageIO.read(new URL(location)); 
    } catch(IOException e) { 
      throw new RuntimeException(e+" when loading: "+location); 
    } 
    if (image == null)
      throw new RuntimeException("Unable to load: "+location); 
    return image;
  }

  /** Saves an image at the given location in <tt>png</tt> format.
   *
   * @param location A Universal Resource Location of the form: <table>
   * <tr><td><tt>ftp:/<i>path-name</i></tt></td><td>to save onto a FTP site.</td></tr>
   * <tr><td><tt>file:/<i>path-name</i></tt></td><td>to save into a file.</td></tr>
   * </table>
   * 
   * @param image The image to save.
   *
   * @throws RuntimeException if an I/O exception has occurred.
   */
  public static void saveImage(String location, BufferedImage image) {
    location = toUrl(location);
    try {
      if (location.startsWith("file:")) {
	ImageIO.write(image, "png", new File(location.substring(5)));
      } else {
	URLConnection connection = new URL(location).openConnection(); connection.setDoOutput(true); OutputStream writer = connection.getOutputStream();
	ImageIO.write(image, "png", writer);
	writer.close();
      }
    } catch(IOException e) {
      throw new RuntimeException(e+" when saving: "+location); 
    }
  }

  /** Converts a location to a weel-formed URL. */
  private static String toUrl(String location) {
    return location.matches("(file|ftp|http|https|jar|mailto|stdout):.*") ? location : "file:"+location;
  }

  /** Converts a string to proper name.
   * <p>- A proper name is an identifier of lexical syntax: <tt>"[a-zA-Z_][a-zA-Z0-9_]*"</tt>.</p>
   * <p>- The construct <tt>toName(string) == string</tt> allows to check that the string is a proper name.</p>
   * @return The string itself if it is a proper name, else a new string: <ul>
   * <li>With punctuation replaced by <tt>'_'</tt></li>
   * <li>With some symbols replaced by their Latin name</li>
   * <li>With French accents letter replaced by the corresponding unaccentuated letter</li>
   * <li>With other spurious char replaced by the string <tt>null</tt></li>
   * <ul>
   */
  public static String toName(String string) {
    if (string == null) return "null";
    if (string.matches("[a-zA-Z_][a-zA-Z0-9_]*")) return string;
    if (string.length() == 0) return "null";
    String c_0 = string.substring(0, 1); String name = c_0.matches("[a-zA-Z_]") || string2name.containsKey(c_0) ? "" : "_";
    for (int i = 0; i < string.length(); i++) {
      String c_i = string.substring(i, i+1);
      name += c_i.matches("[a-zA-Z0-9_]") ? c_i : string2name.get(c_i);
    }
    return name;
  }
  // string2name conversion table
  private static HashMap<String,String> string2name;
  static {
    string2name = new HashMap<String,String>();
    string2name.put(" ", "_");
    string2name.put("!", "_");
    string2name.put("?", "_");
    string2name.put(",", "_");
    string2name.put(";", "_");
    string2name.put(".", "_");
    string2name.put("à", "a");
    string2name.put("â", "a");
    string2name.put("é", "e");
    string2name.put("ê", "e");
    string2name.put("è", "e");
    string2name.put("ë", "e");
    string2name.put("î", "i");
    string2name.put("ï", "i");
    string2name.put("ô", "o");
    string2name.put("ö", "o");
    string2name.put("ù", "u");
    string2name.put("ç", "c");
    string2name.put("+", "plus");
    string2name.put("-", "minus");
    string2name.put("&", "et");
    string2name.put("|", "ou");
  }

  /** Converts a file-name to a location with a proper file-name.
   * @param fileName The local fileName (not an URL) to be converted.
   * @param extension In false the extension is cancelled.
   * @return A <tt>file:</tt> location with a proper name and separator-char. 
   * <p>- <tt>System.out.println</tt> messages in French to the user if the file-name has to be changed.</p>
   */
  public static String toFileName(String fileName, boolean extension) { 
    File file = new File(fileName);
    String folder = file.getParent() == null ? System.getProperty("user.dir") : file.getParent();
    String name = file.getName().replaceAll("\\.[A-Za-z]+$", "");
    if (name.equals(file.getName())) extension = false;
    String ext = file.getName().replaceAll("^.*\\.([A-Za-z]+)$", "$1");
    String main;
    if (Jvs2Java.isReserved(name)) {
      main = "my_"+name;
      System.out.println("Attention: le nom \""+name+"\" est interdit par Java,\n renommons le \""+main+"\"");
    } else {
      main = toName(name);
      if (!main.equals(name))
	System.out.println("Attention: le nom \""+name+"\" contient quelque caractère interdit par Java,\n renommons le \""+main+"\"");
    }
    return "file:" + folder + File.separatorChar + main + (extension ? "."+ext : "");
  }

  /** Converts a XML string to another XML string using a XSL string. 
   * <div>The <tt>saxon.jar</tt> must be in the path.</div>
   * @param xml The XML input string. If null simply cash the XSL.
   * @param xsl The XSL transformation string.
   * @return The tranformed string.
   *
   * @throws RuntimeException if an I/O exception has occurred.
   * @throws IllegalArgumentException if a syntax error has occurred.
   * 
   */
  public static String xml2xml(String xml, String xsl) {
    // Compile the XSL tranformation 
    try {
      if (!tranformers.containsKey(xsl)) {
	tranformers.put(xsl, tfactory.newTemplates(new StreamSource(new StringReader(xsl))).newTransformer());
      }
    } catch(TransformerConfigurationException e) {
      throw new RuntimeException(e+" when compiling: "+xsl); 
    }
    // Apply the transformation
    try {
      if (xml == null) xml = "<null/>";
      StringWriter writer = new StringWriter();
      tranformers.get(xsl).transform(new StreamSource(new StringReader(xml)), new StreamResult(writer));
      return writer.toString();
    } catch(TransformerException e) {
      // FOR DEBUG ONLY: Uses an external transformer to get a better error report
      try {
	saveString("/tmp/xml2xml.xml", xml);
	saveString("/tmp/xml2xml.xsl", xsl);
	System.out.println(exec("java -jar /home/vthierry/bin/saxon.jar -o /tmp/xml2xml.out /tmp/xml2xml.xml /tmp/xml2xml.xsl"));
      } catch(Exception e2) { }
      throw new IllegalArgumentException(e.getMessageAndLocation());
    }
  }
  // Cash mechanism      
  private static TransformerFactory tfactory = TransformerFactory.newInstance();
  private static HashMap<String,Transformer> tranformers = new HashMap<String,Transformer>();
  static {
    System.setProperty("javax.xml.parsers.SAXParserFactory", "com.icl.saxon.aelfred.SAXParserFactoryImpl");
    System.setProperty("javax.xml.transform.TransformerFactory", "com.icl.saxon.TransformerFactoryImpl");  
    System.setProperty("javax.xml.parsers.DocumentBuilderFactory", "com.icl.saxon.om.DocumentBuilderFactoryImpl");
  }

  /** Converts a HTML string to a XHTML string.
   * <p>- Accentuation is re-encoded in UTF-*, spurious constructs (comments, instructions, ..) are deleted and empty tags are terminated by '/>'.</p>
   * <p>- This is a fragible command in the sense that unexpected spurious or non-well formed HTML syntax may defeat it.</p>
   * @param htm The HTML string.
   * @return The XHTML converted string.
   */
  public static String htm2xml(String htm) {
    return htm.
      // Eliminate html accentuation
      replaceAll("&agrave;", "à").
      replaceAll("&acirc;", "â").
      replaceAll("&eacute;", "é").
      replaceAll("&egrave;", "è").
      replaceAll("&euml;", "ë").
      replaceAll("&ecirc;", "ê").
      replaceAll("&iuml;", "ï").
      replaceAll("&icirc;", "î").
      replaceAll("&ouml;", "ö").
      replaceAll("&ocirc;", "ô").
      replaceAll("&ugrave;", "ù").
      replaceAll("&ccedil;", "ç").
      // Eliminate spurious constructs
      replaceAll("<[!?][^>]*>","").
      replaceAll("&nbsp;"," ").
      // Encapsulate non XML constructs
      replaceAll("(<(meta|img|hr|br|link)[^>/]*)/?>","$1/>");
  }

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

  /** Opens an applet in a standalone frame.
   * @param applet The applet to display.
   * @param title  Frame title. If null no title.
   * @param width  Applet width.
   * @param height Applet height.
   * @return The opened frame.  Use <tt>frame.dispose()</tt> to close the frame.
   */
  public static JFrame show(JApplet applet, String title, int width, int height) {
    AppletFrame f = new AppletFrame(); f.open(applet, title, width, height); return f;
  }
  private static class AppletFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    private JApplet applet = null;
    // Opens an applet in a standalone frame.
    public void open(JApplet applet, String title, int width, int height) {
      this.applet = applet; getContentPane().add(applet); applet.init(); pack(); frames++; 
      if (title != null) setTitle(title); setMinimumSize(new Dimension(width, height)); 
      setVisible(true); applet.start(); 
    }
    // Closes the frame and dispose, force exit if all frames are closed.
    public void dispose() {
      applet.stop(); applet.destroy(); getContentPane().remove(applet); super.dispose(); System.gc(); frames--; if (frames == 0) System.exit(0);
    } 
    // Defines a listener for focus, iconification and dispose.
    {
      addWindowListener(new WindowListener() {
	  public void windowOpened(WindowEvent e) { e.getWindow().requestFocus();  }
	  public void windowClosing(WindowEvent e) { dispose(); }
	  public void windowClosed(WindowEvent e) { }
	  public void windowIconified(WindowEvent e) { if (applet != null) applet.stop(); }
	  public void windowDeiconified(WindowEvent e) { if (applet != null) applet.start(); }
	  public void windowActivated(WindowEvent e) { e.getWindow().requestFocus(); }
	  public void windowDeactivated(WindowEvent e) { }
	});
    }
    // Defines an exit on CTRL+Q input code.
    { 
      KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
	  public boolean dispatchKeyEvent(KeyEvent e) { if (e.getKeyChar() == '') { dispose(); }  return false; }
	});
    }
  }
  private static int frames = 0;
}
