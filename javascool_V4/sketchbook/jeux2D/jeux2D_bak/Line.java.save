package org.javascool.proglets.jeux2D;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
// TODO update javadoc

import java.awt.Graphics;
import java.util.logging.Logger;
import org.javascool.macros.Macros;

/**
 * This class allows the end-user to draw and manipulate an oval
 * @author gmatheron
 */
public class Line extends Geometry implements Drawable {
  /**
   * Creates and draws a non-filled oval
   * @param x The X position of the topleft corner of the rectangle that fits the oval
   * @param y The Y position of the topleft corner of the rectangle that fits the oval
   * @param w The width of the topleft corner of the rectangle that fits the oval
   * @param h The height of the topleft corner of the rectangle that fits the oval
   */
  public Line(double x, double y, double w, double h) {
    super(x, y, w, h);
    PrivateFunctions.getGamePanel().addItem(this);
  }
  /**
   * Draws the oval to a Graphics buffer.
   * @param g The Graphics buffer on which to draw the oval
   */
  @Override
  public void draw(Graphics g) {
    g.drawLine((int) getX(), (int) getY(), (int) getWidth(), (int) getHeight());
  }
  public double getX2() {
    return m_w;
  }
  // TODO update doc
  public double getY2() {
    return m_h;
  }
  public void setX2(double x2) {
    m_w = x2;
  }
  public void setY2(double y2) {
    m_h = y2;
  }
  public void position2(double x2, double y2) {
    m_w = x2;
    m_h = y2;
  }
  private static final Logger LOG = Logger.getLogger(Oval.class.getName());
}
