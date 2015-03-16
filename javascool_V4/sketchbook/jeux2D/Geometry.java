package org.javascool.proglets.jeux2D;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.awt.Color;
import java.util.logging.Logger;

/**
 * This class is parent of all the objects that are rectangular, that can be
 * placed on the proglet's panel and recieve events if the mouse pointer is
 * above them.
 * @author gmatheron
 */
public abstract class Geometry extends Accessible implements Drawable {
  /**
   * Stores the width, height and xy position of the object (relative to the
   * origin of the panel and in pixels)
   */
  protected double m_w, m_h, m_x, m_y;
  protected Color m_color;

  private boolean m_visible = true;
  private boolean m_deleted = false;

  /**
   * Constructs an object based solely on its position (its size will be 0x0)
   * @param x The X coordinate of the object's position
   * @param y The Y coordinate of the object's position
   */
  public Geometry(double x, double y) {
    super();
    m_x = x;
    m_y = y;
    m_w = 0;
    m_h = 0;
    m_color = new Color(1, 1, 1, 1);
  }
  /**
   * Constructs an object based on its position and size
   * @param x The X coordinate of the object's position
   * @param y The Y coordinate of the object's position
   * @param w The object's width
   * @param h The object's height
   */
  public Geometry(double x, double y, double w, double h) {
    super();
    m_x = x;
    m_y = y;
    m_w = w;
    m_h = h;
    m_color = new Color((float) 1.0, (float) 1.0, (float) 1.0, (float) 1.0);
  }
  /**
   * Constructs an object places at position (0,0) and with size 0x0
   */
  public Geometry() {
    super();
    m_w = 0;
    m_h = 0;
    m_x = 0;
    m_y = 0;
    m_color = new Color(1, 1, 1, 1);
  }
  /**
   * Get the object's width
   * @return the object's width
   */
  public double getWidth() {
    return m_w;
  }
  /**
   * Get the object's height
   * @return the object's height
   */
  public double getHeight() {
    return m_h;
  }
  /**
   * Get the object's X coordinate
   * @return the object's X coordinate
   */
  public double getX() {
    return m_x;
  }
  /**
   * Get the object's Y coordinate
   * @return the object's Y coordinate
   */
  public double getY() {
    return m_y;
  }
  /**
   * Sets the object's width
   * @param w the object's width
   */
  public void setWidth(double w) {
    m_w = w;
  }
  /**
   * Sets the object's height
   * @param h the object's height
   */
  public void setHeight(double h) {
    m_h = h;
  }
  /**
   * Sets the object's X position
   * @param x the object's X position
   */
  public void setX(double x) {
    m_x = x;
  }
  /**
   * Sets the object's Y position
   * @param y the object's Y position
   */
  public void setY(double y) {
    m_y = y;
  }
  /**
   * Sets the object's size
   * @param w the object's width
   * @param h the object's height
   */
  public void scale(double w, double h) {
    m_w = w;
    m_h = h;
  }
  /**
   * Sets the object's position
   * @param x the object's X position
   * @param y the object's Y position
   */
  public void position(double x, double y) {
    m_x = x;
    m_y = y;
  }
  public void setColor(double r, double g, double b, double a) {
    try {
      m_color = new Color((float) (r / 255), (float) (g / 255), ((float) b / 255), (float) a);
    } catch(IllegalArgumentException e) {
      org.javascool.core.ProgletEngine.getInstance().doStop("Impossible de créer une couleur si les valeurs spécifiées ne sont pas comprises entre 0 et 255 pour r, g et b et entre 0 et 1 pour alpha");
    }
  }
  private static final Logger LOG = Logger.getLogger(Geometry.class.getName());

  /**
   * Returns true if the mouse is over the object
   * @return true if the mouse is over the object
   */
  @Override
  public boolean isForMe() {
    return Functions.mouseX() > m_x && Functions.mouseX() < m_x + m_w && Functions.mouseY() > m_y && Functions.mouseY() < m_y + m_h;
  }
  public void show() {
    m_visible = true;
  }
  public void hide() {
    m_visible = false;
  }
  public boolean isVisible() {
    return m_visible;
  }
  public void delete() {
    hide();
    destroy();
    m_deleted = true;
    Group.updateAll();
  }
  public boolean isDeleted() {
    return m_deleted;
  }
  @Override
  public Color initColor() {
    System.out.println(m_color);
    return m_color;
  }
}
