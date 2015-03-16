package org.javascool.proglets.jeux2D;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.javascool.macros.Macros;
import java.io.FileInputStream;

/**
 * This class defines a sprite that can be drawn in the render area and addressed events
 * @author gmatheron
 */
public class Sprite extends Geometry implements Drawable {
  // TODO update doc and javadoc
  public void makeColorTransparent(float r, float g, float b) {
    final Color color = new Color(r / 255, g / 255, b / 255);
    ImageFilter filter = new RGBImageFilter() {
      // the color we are looking for... Alpha bits are set to opaque
      public int markerRGB = color.getRGB() | 0xFF000000;
      @Override
      public final int filterRGB(int x, int y, int rgb) {
        if((rgb | 0xFF000000) == markerRGB) {
          // Mark the alpha bits as zero - transparent
          return 0x00FFFFFF & rgb;
        } else {
          return rgb;
        }
      }
    };
    ImageProducer ip = new FilteredImageSource(m_image.getSource(), filter);
    Image image = Toolkit.getDefaultToolkit().createImage(ip);
    m_image = new BufferedImage(
      image.getWidth(null), image.getHeight(null),
      BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2 = m_image.createGraphics();
    g2.drawImage(image, 0, 0, null);
    g2.dispose();
  }
  /**
   * The image
   */
  private BufferedImage m_image;

  /**
   * Creates the image and registers it into the render area
   * @param x The X position of the topleft corner of the image
   * @param y The Y position of the topleft corner of the image
   * @param w The width of the image
   * @param h The height of the image
   */
  public Sprite(double x, double y, double w, double h) {
    super(x, y, w, h);
    Panel.getPane().getGamePanel().addItem(this);
  }
  /**
   * Loads the image from a file. This must be done before drawing starts.
   * If the file is not found a bug will be reported
   *      //TODO make this exception catchable
   * @param fileName  The image file from which the image will be loaded
   *      //TODO test supported formats
   */
  public void load(String fileName) {
    try {
      m_image = ImageIO.read(new File(fileName));
    } catch(IOException e) {
      try {
        InputStream stream = Macros.getResourceURL(fileName).openStream();
        if(stream == null) {
          org.javascool.core.ProgletEngine.getInstance().doStop("Le fichier " + fileName + " n'extste pas");
        } else {
          try {
            m_image = ImageIO.read(stream);
          } catch(IOException ex) {
            org.javascool.core.ProgletEngine.getInstance().doStop("Le fichier " + fileName + " est illisible");
          }
        }
      } catch(IOException e2) {
        org.javascool.core.ProgletEngine.getInstance().doStop("Le fichier " + fileName + " est illisible");
      }
    }
  }
  /**
   * Draws the sprite to the specified Graphics buffer. It the image is not loaded
   * it won't be displayed but it can still catch events !
   * @param g The Graphics buffer on which to draw the sprite
   */
  @Override
  public void draw(Graphics g) {
    if(m_image != null) {
      g.drawImage(m_image, (int) getX(), (int) getY(), (int) getWidth(), (int) getHeight(), null);
    }
  }
  private static final Logger LOG = Logger.getLogger(Sprite.class.getName());
}
