/*******************************************************************************
 * Thierry.Vieville@sophia.inria.fr, Copyright (C) 2009.  All rights reserved. *
 *******************************************************************************/

package org.javascool.old;

// Used to define the gui
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.HashMap;

// Used to manipulate the image
import java.awt.image.BufferedImage;
import java.awt.Dimension;
import javax.imageio.ImageIO;
import java.net.URL;
import java.io.IOException;

/** This widget defines a pixelic color icon panel. 
 * @see <a href="IconOutput.java">source code</a>
 */
public class IconOutput extends JPanel {
  private static final long serialVersionUID = 1L;
  {
    setBackground(Color.GRAY); setPreferredSize(new Dimension(550, 550));
    reset(550, 550);
  }
  /** Internal routine: do not use. */
  public void paint(Graphics g) {
    super.paint(g);
    setBounds();
    g.setPaintMode(); 
    for(int j = 0; j < height; j++)
      for(int i = 0; i < width; i++) {
	g.setColor(image[i + j * width]);
	g.fillRect(i0 + i * dij, j0 + j * dij, dij, dij);
      }
  }
  private void setBounds() {
    int di = width > 0 ? getWidth() / width : 1, dj = height > 0 ? getHeight() / height : 1; dij = di < dj ? di : dj;
    i0 = (getWidth() - width * dij) / 2; j0 = (getHeight() - height * dij) / 2;
  }
  
  /** Resets the image.
   * @param width Image width.
   * @param height Image height.
   */
  public void reset(int width, int height) {
    if (width * height > 550 * 550) throw new IllegalArgumentException("Image size too big !");
    if (width % 2 == 0) width--;if (height % 2 == 0) height--;
    image = new Color[(this.width = (width > 0 ? width : 1)) * (this.height = (height > 0 ? height : 1))];
    for(int ij = 0; ij < this.width * this.height; ij++) { image[ij] = Color.WHITE; };
    repaint(0, 0, getWidth(), getHeight());
  }

  /** Resets the image.
   * @param location The image location.
   * @return The image dimension.
   */
  public Dimension reset(String location) throws IOException {
    BufferedImage img = ImageIO.read(new URL(location));
    if (img != null) {
      reset(img.getWidth(), img.getHeight());
      for(int j = 0, ij = 0; j < height; j++)
	for(int i = 0; i < width; i++, ij++)
	  image[ij] = new Color(img.getRGB(i, j));
      return new Dimension(width, height);
    } else
      throw new IOException("Unable to load the image "+location);
  }

  /** Sets a pixel value.
   * @param x Pixel abscissa, in {0, width{.
   * @param y Pixel Ordinate, in {0, height{.
   * @param  c Color: "black" (default), "blue", "cyan", "gray", "green", "magenta", "orange", "pink", "red", "white", "yellow".
   * @return True if the pixel location is in the image bounds.
   */
  public boolean set(int x, int y, String c) { return set(x, y, getColor(c)); }
  
  /** Sets a pixel value.
   * @param x Pixel abscissa, in {0, width{.
   * @param y Pixel Ordinate, in {0, height{.
   * @param v Monochrome intensity between 0 and 255.
   * @return True if the pixel location is in the image bounds.
   */
  public boolean set(int x, int y, int v) { v = v < 0 ? 0 : v > 255 ? 255 : v; return set(x, y, new Color(v, v, v)); }

  private boolean set(int x, int y, Color c) {
     if (0 <= x && x < width && 0 <= y && y < height) {
      setBounds(); int ij = x + y * width; image[ij] = c;
      repaint(i0 + x * dij, j0 + y * dij, dij, dij);
      return true;
    } else
      return false;
  }

  /** Gets a pixel value.
   * @param x Pixel abscissa, in {0, width{.
   * @param y Pixel Ordinate, in {0, height{.
   * @return The pixel intensity, or -1 if out of bounds.
   */
  public int getIntensity(int x, int y) {
    if (0 <= x && x < width && 0 <= y && y < height) {
      Color c = image[x + y * width]; return (c.getRed() + c.getGreen() + c.getBlue()) / 3;
    } else
      return -1;
  }
  
  /** Gets a pixel value.
   * @param x Pixel abscissa, in {0, width{.
   * @param y Pixel Ordinate, in {0, height{.
   * @return The pixel color, if the pixel location is in the image bounds, else "undefined".
   */
  public String getColor(int x, int y) {
    if (0 <= x && x < width && 0 <= y && y < height) {
      Color c = image[x + y * width]; return colors.containsKey(c) ? colors.get(c) : c.toString();
    } else
      return "undefined";
  }
  
  private Color image[]; private int width, height, i0, j0, dij;

  private static HashMap<Color,String> colors = new HashMap<Color,String>();
  private static Color getColor(String color) {
    try { return (Color) Class.forName("java.awt.Color").getField(color).get(null); } catch(Exception e) { return Color.BLACK; }
  }
  private static void putColors(String color) { colors.put(getColor(color), color); }
  static {
    putColors("black");
    putColors("blue");
    putColors("cyan");
    putColors("gray");
    putColors("green");
    putColors("magenta");
    putColors("orange");
    putColors("pink");
    putColors("red");
    putColors("white");
    putColors("yellow");
  }
} 
