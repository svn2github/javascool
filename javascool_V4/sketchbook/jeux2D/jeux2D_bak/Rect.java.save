package org.javascool.proglets.jeux2D;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.awt.Graphics;
import java.util.logging.Logger;
import org.javascool.macros.Macros;

/**
 * This class allows the end-user to draw and manipulate an rect
 * @author gmatheron
 */
public class Rect extends Geometry implements Drawable {
  /**
   * True if the rect should be filled, false otherwise
   */
  private boolean m_solid;

  /**
   * Creates and draws a non-filled rect
   * @param x The X position of the topleft corner of the rectangle that fits the rect
   * @param y The Y position of the topleft corner of the rectangle that fits the rect
   * @param w The width of the topleft corner of the rectangle that fits the rect
   * @param h The height of the topleft corner of the rectangle that fits the rect
   */
  public Rect(double x, double y, double w, double h) {
    super(x, y, w, h);
    m_solid = true;
    ((Panel) Macros.getProgletPanel()).getGamePanel().addItem(this);
  }
  /**
   * Creates and draws an rect
   * @param x The X position of the topleft corner of the rectangle that fits the rect
   * @param y The Y position of the topleft corner of the rectangle that fits the rect
   * @param w The width of the topleft corner of the rectangle that fits the rect
   * @param h The height of the topleft corner of the rectangle that fits the rect
   * @param solid True if the rect should be filled, false otherwise
   */
  public Rect(double x, double y, double w, double h, boolean solid) {
    super(x, y, w, h);
    ((Panel) Macros.getProgletPanel()).getGamePanel().addItem(this);
    m_solid = solid;
  }
  /**
   * Draws the rect to a Graphics buffer.
   * @param g The Graphics buffer on which to draw the rect
   */
  @Override
  public void draw(Graphics g) {
    if(m_solid)
      g.fillRect((int) getX(), (int) getY(), (int) getWidth(), (int) getHeight());
    else
      g.drawRect((int) getX(), (int) getY(), (int) getWidth(), (int) getHeight());
  }
  private static final Logger LOG = Logger.getLogger(Rect.class.getName());
}
