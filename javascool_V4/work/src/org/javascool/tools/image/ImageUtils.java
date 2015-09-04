/*******************************************************************************
* Thierry.Vieville@sophia.inria.fr, Copyright (C) 2009.  All rights reserved. *
*******************************************************************************/

package org.javascool.tools.image;

// Used to load/save images
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.net.URL;
import java.net.URLConnection;
import java.io.OutputStream;
import java.io.File;
import org.javascool.macros.Macros;

// Used to load/save PxM images
import org.javascool.tools.FileManager;
import java.io.Reader;
import java.io.StringReader;
import java.io.IOException;
import java.io.EOFException;

/** Définit comment lire/ecire une image.
 * <p>Utilise les fonctions de <a href="http://docs.oracle.com/javase/6/docs/api/javax/imageio/ImageIO.html">javax.imageio.ImageIO</a>.</p>
 * @see <a href="ImageUtils.java.html">code source</a>
 * @serial exclude
 */
public class ImageUtils {
  
  // @factory
  private ImageUtils() {}

  /** Charge une image locale ou distante.
   * <p>Reconnait les formats binaires ".png", ".jpg" et ".gif". Reconnait les fichiers ASCII au format <a href="http://fr.wikipedia.org/wiki/Portable_pixmap">".pbm", ".pgm" et ".ppm"</a> par leur extension.</p>
   *
   * @param location Une URL (Universal Resource Location) de la forme: <div id="load-format"><table>
   * <caption>URL (Universal Resource Location) prises en charge</caption>
   * <tr><td><tt>http:/<i>path-name</i></tt></td><td>pour aller chercher le contenu sur un site web</td></tr>
   * <tr><td><tt>http:/<i>path-name</i>?param_i=value_i&amp;..</tt></td><td>pour le récupérer sous forme de requête HTTP</td></tr>
   * <tr><td><tt>file:/<i>path-name</i></tt></td><td>pour le charger du système de fichier local ou en tant que ressource Java dans le CLASSPATH</td></tr>
   * <tr><td><tt>jar:/<i>jar-path-name</i>!/<i>jar-entry</i></tt></td><td>pour le charger d'une archive
   *  <div>(exemple:<tt>jar:http://javascool.gforge.inria.fr/javascool.jar!/META-INF/MANIFEST.MF</tt>)</div></td></tr>
   * </table></div>
   * @return L'image chargée.
   *
   * @throws IllegalArgumentException Si l'URL est mal formée.
   * @throws RuntimeException Si une erreur d'entrée-sortie s'est produite.
   */
  public static BufferedImage loadImage(String location) {
    BufferedImage image = null;
    try {
      String format = location.replaceFirst(".*\\.([a-z]+)", "$1").toLowerCase();
      if("pbm".equals(format) || "pgm".equals(format) || "ppm".equals(format)) {
        image = loadImagePxM(location);
      } else {
        image = ImageIO.read(Macros.getResourceURL(location));
      }
    } catch(IOException e) { throw new RuntimeException(e + " when loading: " + location + " : " + Macros.getResourceURL(location));
    }
    if(image == null) { throw new RuntimeException("Unable to load: " + location);
    }
    return image;
  }
  /** Ecrit une image locale ou distante.
   *
   * @param location Une URL (Universal Resource Location) de la forme: <div id="save-format"><table>
   * <caption>URL (Universal Resource Location) prises en charge</caption>
   * <tr><td><tt>ftp:/<i>path-name</i></tt></td><td>pour sauver sur un site FTP.</td></tr>
   * <tr><td><tt>file:/<i>path-name</i></tt></td><td>pour sauver dans le système de fichier local (le <tt>file:</tt> est optionnel).</td></tr>
   * <tr><td><tt>mailto:<i>address</i>?subject=<i>subject</i></tt></td><td>pour envoyer un courriel avec le texte en contenu.</td></tr>
   * <tr><td><tt>stdout:/</tt></td><td>pour l'imprimer dans la console.</td></tr>
   * </table></div>
   * @param image L'image à sauvegarder.
   *  <p>Reconnait le format de stockage par l'extension. Il est recommandé d'utiliser le format binaire ".pgn". Les formats  ASCIIt <a href="http://fr.wikipedia.org/wiki/Portable_pixmap">".pbm", ".pgm" et ".ppm"</a> sont disponibles aussi.</p>
   *
   * @throws IllegalArgumentException Si l'URL est mal formée.
   * @throws RuntimeException Si une erreur d'entrée-sortie s'est produite.
   */
  public static void saveImage(String location, BufferedImage image) {
    String format = location.replaceFirst(".*\\.([a-z]+)", "$1").toLowerCase();
    if("pbm".equals(format) || "pgm".equals(format) || "ppm".equals(format)) {
      saveImagePxM(location, image, format);
    } else {
      location = Macros.getResourceURL(location).toString();
      try {
        if(location.startsWith("file:")) {
          ImageIO.write(image, "png", new File(location.substring(5)));
        } else {
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
  // Sauve une image au format ACSII en PBM , PGM ou PPM, ref: http://fr.wikipedia.org/wiki/Portable_pixmap
  private static void saveImagePxM(String location, BufferedImage image, String format) {
    format = format.toLowerCase();
    // Saves the "magic number"
    StringBuffer s = new StringBuffer((format.equals("pbm") ? "P1" : format.equals("pgm") ? "P2" : "P3") + "\n");
    // Saves image sizes and bound
    s.append(image.getWidth() + " " + image.getHeight() + (format.equals("pbm") ? "" : format.equals("pgm") ? " 765" : " 255") + "\n");
    // Sets the pixel separator to get line size lower than 70 chars
    String c = image.getWidth() < (format.equals("pbm") ? 35 : format.equals("pgm") ? 17 : 5) ? " " : "\n";
    for(int j = 0; j < image.getHeight(); j++)
      for(int i = 0; i < image.getWidth(); i++) {
        // Gets the pixel value
        int rgb = image.getRGB(i, j), r = (rgb >> 16) & 0xFF, g = (rgb >> 8) & 0xFF, b = rgb & 0xFF, v = r + g + b, o = v < 383 ? 0 : 1;
        s.append((format.equals("pbm") ? "" + (b > 0 ? 1 : 0) : format.equals("pgm") ? "" + v : "" + r + " " + g + " " + b) + (i < image.getWidth() - 1 ? c : "\n"));
      }
    FileManager.save(location, s.toString());
  }
  // Charge une image au format ACSII en PBM , PGM ou PPM, ref: http://fr.wikipedia.org/wiki/Portable_pixmap
  private static BufferedImage loadImagePxM(String location) {
    try {
      PxMReader reader = new PxMReader(new StringReader(FileManager.load(location)));
      String header = reader.readString(false);
      if("P1".equals(header) || "P2".equals(header) || "P3".equals(header)) {
        int width = reader.readInteger(), height = reader.readInteger();
        double scale = 255.0 / ("P1".equals(header) ? 1 : reader.readInteger());
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for(int j = 0; j < img.getHeight(); j++)
          for(int i = 0; i < img.getWidth(); i++) {
            int r, g, b;
            if("P3".equals(header)) {
              r = reader.readPixel(scale, false);
              g = reader.readPixel(scale, false);
              b = reader.readPixel(scale, false);
            } else if ("P1".equals(header)) {
              r = g = b = 255 - reader.readPixel(scale, true);
            } else {
              r = g = b = reader.readPixel(scale, false);
            }
            img.setRGB(i, j, r << 16 | g << 8 | b);
          }
        return img;
      } else {
        return null;
      }
    } catch(Exception e) {
      System.err.println("Erreur de format lors de la lecture de "+location+" : "+e);
      return null;
    }
  }
  /** Defines a PBM/PGM/PPM token reader. */
  private static class PxMReader {
    /** Creates a PBM/PGM/PPM token readerbthat uses a default-sized input buffer.
     * @param r The reader to use.
     */
    public PxMReader(Reader r) {
      this.r = r;
    }
    private Reader r;
    int c = -2;

    /** Reads a token.
     * @return The next token in the input bufffer.
     * @throws IOException If the file is truncated or an error occurs during reading.
     */
    public String readString(boolean binary) throws IOException {
      if(c == -2) {
        c = r.read();
      }
      // Skips spaces
      while(Character.isWhitespace(c)) {
        c = r.read();
      }
      // Skips comment lines
      while(c == '#') {
        do {
          c = r.read();
        } while(c != -1 && c != '\n');
        while(Character.isWhitespace(c)) {
          c = r.read();
        }
      }
      // Detects end of file
      if(c == -1) { throw new EOFException();
      }
      // Collects a token
      StringBuffer s = new StringBuffer();
      do {
	s.append((char) c);
	c = r.read();
      } while(c != -1 && (!binary) && (!Character.isWhitespace(c)));
      return s.toString();
    }
    /** Reads a integer value.
     * @return The next token as a positive integer value.
     * @throws NumberFormatException In case of a spurious reading.
     * @throws IOException If the file is truncated or an error occurs during reading.
     */
    public int readInteger() throws IOException {
      return new Integer(readString(false));
    }
    /** Reads a scaled integer value.
     * @param scale The pixel scale value
     * @param binary If true consider one byte value
     * @return The next token as a scaled positive integer value between 0 and 255.
     * @throws NumberFormatException In case of a spurious reading.
     * @throws IOException If the file is truncated or an error occurs during reading.
     */
    public int readPixel(double scale, boolean binary) throws IOException {
      int v = (int) Math.rint(scale * new Double(readString(binary)));
      return v < 0 ? 0 : 255 < v ? 255 : v;
    }
  }
}
