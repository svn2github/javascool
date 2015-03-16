package org.javascool.proglets.jeux2D;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.awt.Graphics;
import java.util.logging.Logger;
import org.javascool.macros.Macros;

/**
 * This class allows the end-user to draw and manipulate an oval
 * @author gmatheron
 */
public class Oval extends Geometry implements Drawable {
  /**
   * True if the oval should be filled, false otherwise
   */
  private boolean m_solid;

  /**
   * Creates and draws a non-filled oval
   * @param x The X position of the topleft corner of the rectangle that fits the oval
   * @param y The Y position of the topleft corner of the rectangle that fits the oval
   * @param w The width of the topleft corner of the rectangle that fits the oval
   * @param h The height of the topleft corner of the rectangle that fits the oval
   */
  public Oval(double x, double y, double w, double h) {
    super(x, y, w, h);
    m_solid = true;
    ((Panel) Macros.getProgletPanel()).getGamePanel().addItem(this);
  }
  /**
   * Creates and draws an oval
   * @param x The X position of the topleft corner of the rectangle that fits the oval
   * @param y The Y position of the topleft corner of the rectangle that fits the oval
   * @param w The width of the topleft corner of the rectangle that fits the oval
   * @param h The height of the topleft corner of the rectangle that fits the oval
   * @param solid True if the oval should be filled, false otherwise
   */
  public Oval(double x, double y, double w, double h, boolean solid) {
    super(x, y, w, h);
    ((Panel) Macros.getProgletPanel()).getGamePanel().addItem(this);
    m_solid = solid;
  }
  /**
   * Draws the oval to a Graphics buffer.
   * @param g The Graphics buffer on which to draw the oval
   */
  @Override
  public void draw(Graphics g) {
    g.setColor(m_color);
    if(m_solid)
      g.fillOval((int) getX(), (int) getY(), (int) getWidth(), (int) getHeight());
    else
      g.drawOval((int) getX(), (int) getY(), (int) getWidth(), (int) getHeight());
  }
  private static final Logger LOG = Logger.getLogger(Oval.class.getName());
}
