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
import javax.imageio.ImageIO;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.io.OutputStream;
import java.io.File;

// Used for string to name and file-name conversion
import java.util.HashMap;
import java.io.File;

/** This factory contains useful methods to interface javascool with the environment. 
 * @see <a href="Utils.java">source code</a>
 */
public class Utils { private Utils() { }
  private static final long serialVersionUID = 1L;

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
   * - A proper name is an identifier of lexical syntax: <tt>"[a-zA-Z_][a-zA-Z0-9_]*"</tt>.
   * - The construct <tt>toName(string) == string</tt> allows to check that the string is a proper name.
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
   * - <tt>System.out.println</tt> messages in French to the user if the file-name has to be changed.
   */
  public static String toFileName(String fileName, boolean extension) { 
    File file = new File(fileName);
    String folder = file.getParent() == null ? System.getProperty("user.dir") : file.getParent();
    String name = file.getName().replaceAll("\\.[A-Za-z]+$", "");
    if (name.equals(file.getName())) extension = false;
    String ext = file.getName().replaceAll("^.*\\.([A-Za-z]+)$", "$1");
    String main;
    if (Translator.isReserved(name)) {
      main = "my_"+name;
      System.out.println("Attention: le nom \""+name+"\" est interdit par Java,\n renommons le \""+main+"\"");
    } else {
      main = toName(name);
      if (!main.equals(name))
	System.out.println("Attention: le nom \""+name+"\" contient quelque caractère interdit par Java,\n renommons le \""+main+"\"");
    }
    return "file:" + folder + File.separatorChar + main + (extension ? "."+ext : "");
  }
}
