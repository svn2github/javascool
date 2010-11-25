/*******************************************************************************
* Thierry.Vieville@sophia.inria.fr, Copyright (C) 2009.  All rights reserved. *
*******************************************************************************/

package proglet.pixelsetcie;

import org.javascool.Utils;

// Used to load/save images
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.io.OutputStream;
import java.io.File;

/** Définit comment lire/ecire une imaage.
 * @see <a href="ImageUtils.java.html">code source</a>
 * @serial exclude
 */
public class ImageUtils {
  private ImageUtils() {}

  /** Loads an image from the given location.
   *
   * @param location A Universal Resource Location of the form: <table>
   * <tr><td><tt>http:/<i>path-name</i></tt></td><td>to load from a HTTP location</td></tr>
   * <tr><td><tt>ftp:/<i>path-name</i></tt></td><td>to load from a FTP site</td></tr>
   * <tr><td><tt>file:/<i>path-name</i></tt></td><td>to load from a file</td></tr>
   * <tr><td><tt>jar:/<i>jar-path-name</i>!/<i>jar-entry</i></tt></td><td>to load from a JAR archive</td></tr>
   * </table>
   *
   *
   * @throws IllegalArgumentException If the URL is malformed.
   * @throws RuntimeException if an I/O exception has occurred.
   */
  public static BufferedImage loadImage(String location) {
    BufferedImage image = null;
    try {
      image = ImageIO.read(Utils.toUrl(location));
    } catch(IOException e) { throw new RuntimeException(e + " when loading: " + location);
    }
    if(image == null) throw new RuntimeException("Unable to load: " + location);
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
   * @throws IllegalArgumentException If the URL is malformed.
   * @throws RuntimeException if an I/O exception has occurred.
   */
  public static void saveImage(String location, BufferedImage image) {
    location = Utils.toUrl(location).toString();
    try {
      if(location.startsWith("file:"))
        ImageIO.write(image, "png", new File(location.substring(5)));
      else {
        URLConnection connection = new URL(location).openConnection();
        connection.setDoOutput(true);
        OutputStream writer = connection.getOutputStream();
        ImageIO.write(image, "png", writer);
        writer.close();
      }
    } catch(IOException e) { throw new RuntimeException(e + " when saving: " + location);
    }
  }
}

