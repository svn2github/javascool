/*******************************************************************************
 * Thierry.Vieville@sophia.inria.fr, Copyright (C) 2009.  All rights reserved. *
 *******************************************************************************/

package proglet;

// Used to define the gui
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

/** This widget defines a pixelic color icon panel. 
 * @see <a href="IconOutput.java">source code</a>
 */
public class IconOutput extends JPanel {
  private static final long serialVersionUID = 1L;
  {
    setBackground(Color.WHITE); setPreferredSize(new Dimension(550, 550));
    reset(550, 550);
  }
  /** Internal routine: do not use. */
  public void paint(Graphics g) {
    super.paint(g);
    g.setPaintMode(); 
    setBounds();
    for(int j = 0; j < height; j++)
      for(int i = 0; i < width; i++) {
	g.setColor(image[i + j * width]);
	g.fillRect(i0 + i * di, j0 + j * dj, di, dj);
      }
  }
  private void setBounds() {
    di = width > 0 ? getWidth() / width : 1; if (di <= 0) di = 1; i0 = (getWidth() - width * di) / 2; 
    dj = height > 0 ? getHeight() / height : 1; if (dj <= 0) dj = 1; j0 = (getHeight() - height * dj) / 2;
  }
  
  /** Clears the image.
   * @param width Image width.
   * @param height Image height.
   */
  public void reset(int width, int height) {
    image = new Color[(this.width = (width > 0 ? width : 1)) * (this.height = (height > 0 ? height : 1))]; color = new String[this.width * this.height];
    for(int ij = 0; ij < this.width * this.height; ij++) { image[ij] = Color.WHITE; color[ij] = "white"; };
    repaint(0, 0, getWidth(), getHeight());
  }

  /** Sets a pixel value.
   * @param x Pixel abscissa, in {0, width{.
   * @param y Pixel Ordinate, in {0, height{.
   * @param  c Color: "black" (default), "blue", "cyan", "gray", "green", "magenta", "orange", "pink", "red", "white", "yellow".
   * @return True if the pixel location is in the image bounds.
   */
  public boolean set(int x, int y, String c) {
    if (0 <= x && x < width && 0 <= y && y < height) {
      Color v; try { v = (Color) Class.forName("java.awt.Color").getField(c).get(null); } catch(Exception e) { v = Color.BLACK; c = "black"; }
      setBounds(); int ij = x + y * width; image[ij] = v; color[ij] = c;
      repaint(i0 + x * di, j0 + y * dj, di, dj);
      return true;
    } else
      return false;
  }
  
  /** Gets a pixel value.
   * @param x Pixel abscissa, in {0, width{.
   * @param y Pixel Ordinate, in {0, height{.
   * @return The pixel color, if the pixel location is in the image bounds, else "undefined".
   */
  public String get(int x, int y) {
    if (0 <= x && x < width && 0 <= y && y < height) {
      return color[x + y * width];
    } else
      return "undefined";
  }
  
  private Color image[]; private String color[]; private int width, height, i0, j0, di, dj;
} 
