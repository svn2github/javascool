package org.javascool.proglets.jeux2D;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.logging.Logger;

/**
 *
 * @author gmatheron
 */
public class Functions {
  /**
   * Returns the mouse X position relative to the top-left corner of the
   * proglet panel
   * @return the mouse X position relative to the top-left corner of the
   * proglet panel
   */
  public static double mouseX() {
    return (double) PrivateFunctions.getFunctionsElementSingleton().m_mousePosRelativeToPanelX;
  }
  /**
   * Returns the mouse Y position relative to the top-left corner of the
   * proglet panel
   * @return the mouse Y position relative to the top-left corner of the
   * proglet panel
   */
  public static double mouseY() {
    return (double) PrivateFunctions.getFunctionsElementSingleton().m_mousePosRelativeToPanelY;
  }
  /**
   * Used to create a listener that will callback the specified function
   * with one MouseState argument
   * @param s The function to callback
   */
  public static void onClick(String s) {
    PrivateFunctions.getFunctionsElementSingleton().m_onClick.add(new EventListener(s, PrivateFunctions.getFunctionsElementSingleton()));
  }
  /**
   * Used to create a listener that will callback the specified function
   * with one MouseState argument
   * @param s The function to callback
   */
  public static void onMouseEntered(String s) {
    PrivateFunctions.getFunctionsElementSingleton().m_onMouseEntered.add(new EventListener(s, PrivateFunctions.getFunctionsElementSingleton()));
  }
  /**
   * Used to create a listener that will callback the specified function
   * with one MouseState argument
   * @param s The function to callback
   */
  public static void onMouseExited(String s) {
    PrivateFunctions.getFunctionsElementSingleton().m_onMouseExited.add(new EventListener(s, PrivateFunctions.getFunctionsElementSingleton()));
  }
  /**
   * Used to create a listener that will callback the specified function
   * with one MouseState argument
   * @param s The function to callback
   */
  public static void onMousePressed(String s) {
    PrivateFunctions.getFunctionsElementSingleton().m_onMousePressed.add(new EventListener(s, PrivateFunctions.getFunctionsElementSingleton()));
  }
  /**
   * Used to create a listener that will callback the specified function
   * with one MouseState argument
   * @param s The function to callback
   */
  public static void onMouseReleased(String s) {
    PrivateFunctions.getFunctionsElementSingleton().m_onMouseReleased.add(new EventListener(s, PrivateFunctions.getFunctionsElementSingleton()));
  }
  /**
   * Used to create a listener that will callback the specified function
   * with one MouseState argument
   * @param s The function to callback
   */
  public static void onMouseDown(String s) {
    PrivateFunctions.getFunctionsElementSingleton().m_onMouseDown.add(new EventListener(s, PrivateFunctions.getFunctionsElementSingleton()));
  }
  /**
   * Used to create a listener that will callback the specified function
   * with one MouseState argument
   * @param s The function to callback
   */
  public static void onMouseUp(String s) {
    PrivateFunctions.getFunctionsElementSingleton().m_onMouseUp.add(new EventListener(s, PrivateFunctions.getFunctionsElementSingleton()));
  }
  /**
   * Used to create a listener that will callback the specified function
   * with one MouseState argument
   * @param s The function to callback
   */
  public static void onMouseMoved(String s) {
    PrivateFunctions.getFunctionsElementSingleton().m_onMouseMoved.add(new EventListener(s, PrivateFunctions.getFunctionsElementSingleton()));
  }
  /**
   * Used to create a listener that will callback the specified function
   * with one MouseState argument
   * @param s The function to callback
   */
  public static void onMouseDragged(String s) {
    PrivateFunctions.getFunctionsElementSingleton().m_onMouseDragged.add(new EventListener(s, PrivateFunctions.getFunctionsElementSingleton()));
  }
  /**
   * Used to create a listener that will callback the specified function
   * with one MouseWheelState argument
   * @param s The function to callback
   */
  public static void onMouseWheelUp(String s) {
    PrivateFunctions.getFunctionsElementSingleton().m_onMouseWheelUp.add(new EventListener(s, PrivateFunctions.getFunctionsElementSingleton()));
  }
  /**
   * Used to create a listener that will callback the specified function
   * with one MouseWheelState argument
   * @param s The function to callback
   */
  public static void onMouseWheelDown(String s) {
    PrivateFunctions.getFunctionsElementSingleton().m_onMouseWheelDown.add(new EventListener(s, PrivateFunctions.getFunctionsElementSingleton()));
  }
  /**
   * Used to create a listener that will callback the specified function
   * with one MouseWheelState argument
   * @param s The function to callback
   */
  public static void onMouseWheelMoved(String s) {
    PrivateFunctions.getFunctionsElementSingleton().m_onMouseWheelMoved.add(new EventListener(s, PrivateFunctions.getFunctionsElementSingleton()));
  }
  /**
   * Used to create a listener that will callback the specified function
   * with one MouseWheelState argument
   * @param s The function to callback
   */
  public static void onKeyPressed(String s) {
    PrivateFunctions.getFunctionsElementSingleton().m_onKeyPressed.add(new EventListener(s, PrivateFunctions.getFunctionsElementSingleton()));
  }
  /**
   * Used to create a listener that will callback the specified function
   * with one MouseWheelState argument
   * @param s The function to callback
   */
  public static void onKeyReleased(String s) {
    PrivateFunctions.getFunctionsElementSingleton().m_onKeyReleased.add(new EventListener(s, PrivateFunctions.getFunctionsElementSingleton()));
  }
  /**
   * Used to create a listener that will callback the specified function
   * with one MouseWheelState argument
   * @param s The function to callback
   */
  public static void onKeyDown(String s) {
    PrivateFunctions.getFunctionsElementSingleton().m_onKeyDown.add(new EventListener(s, PrivateFunctions.getFunctionsElementSingleton()));
  }
  /**
   * Used to create a listener that will callback the specified function
   * with one MouseWheelState argument
   * @param s The function to callback
   */
  public static void onKeyUp(String s) {
    PrivateFunctions.getFunctionsElementSingleton().m_onKeyUp.add(new EventListener(s, PrivateFunctions.getFunctionsElementSingleton()));
  }
  /**
   * Used to create a listener that will callback the specified function
   * @param s The function to callback
   */
  @SuppressWarnings("AccessingNonPublicFieldOfAnotherObject")
  public static void onFrame(String s) {
    PrivateFunctions.getFunctionsElementSingleton().m_onFrame.add(new EventListener(s, PrivateFunctions.getFunctionsElementSingleton()));
  }
  public static boolean keyDown(int code) {
    for(int i = 0; i < PrivateFunctions.getFunctionsElementSingleton().m_keysPressed.size(); i++)
      if(PrivateFunctions.getFunctionsElementSingleton().m_keysPressed.get(i) == code)
        return true;
    return false;
  }
  public static boolean collisionCircleToRect(double x1, double y1, double r1, double x2, double y2, double w2, double h2) {
    // if (distance(x1,y1,x2+w2/2,y2+h2/2)>Math.sqrt((w2*w2)/4+(h2*h2)/4)) return false;
    double xc = x1 + r1;
    double yc = y1 + r1;
    if((yc > y2) && (yc < y2 + h2))
      return Math.abs(xc - (x2 + w2 / 2)) < r1 + w2 / 2;
    if((xc > x2) && (xc < x2 + w2))
      return Math.abs(yc - (y2 + h2 / 2)) < r1 + h2 / 2;
    if((yc < y2) && (xc < x2))
      return distance(xc, yc, x2, y2) < r1;
    if((yc < y2) && (xc > x2 + w2))
      return distance(xc, yc, x2 + w2, y2) < r1;
    if((yc > y2 + h2) && (xc < x2))
      return distance(xc, yc, x2, y2 + h2) < r1;
    if((yc > y2 + h2) && (xc > x2 + w2))
      return distance(xc, yc, x2 + w2, y2 + h2) < r1;
    return false;
  }
  /* //FIXME all coll functions */
  // TESTME
  public static boolean collisionCircleToCircle(double x1, double y1, double r1, double x2, double y2, double r2) {
    return distance(x1, y1, x2, y2) > r1 + r2;
  }
  // TESTME
  public static boolean collisionRectToRect(double x1, double y1, double w1, double h1, double x2, double y2, double w2, double h2) {
    return x1 < x2 + w2 && x1 + w1 > x2 && y1 < y2 + h2 && y1 + h1 > y2;
  }
  public static double distance(double x1, double y1, double x2, double y2) {
    return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
  }
  public static char KEY_A = 65, KEY_B = 66, KEY_C = 67, KEY_D = 68, KEY_E = 69, KEY_F = 70, KEY_G = 71, KEY_H = 72, KEY_I = 73, KEY_J = 74;
  public static char KEY_K = 75, KEY_L = 76, KEY_M = 77, KEY_N = 78, KEY_O = 79, KEY_P = 80, KEY_Q = 81, KEY_R = 82, KEY_S = 83, KEY_T = 84;
  public static char KEY_U = 85, KEY_V = 86, KEY_W = 87, KEY_X = 88, KEY_Y = 89, KEY_Z = 90;
  public static char KEY_SHIFT = 16, KEY_ESC = 27, KEY_F1 = 112, KEY_F2 = 113, KEY_F3 = 114, KEY_F4 = 115, KEY_F5 = 116, KEY_F6 = 117;
  public static char KEY_F7 = 118, KEY_F8 = 119, KEY_F9 = 120, KEY_F10 = 121, KEY_F11 = 122, KEY_F12 = 123, KEY_SCROLLLOCK = 145;
  public static char KEY_PAUSE = 19, KEY_BACKSPACE = 8, KEY_DOLLAR = 515, KEY_RETURN = 10, KEY_CAPSLOCK = 20, KEY_STAR = 151;
  public static char KEY_INFERIOR = 153, KEY_CTRL = 17, KEY_WIN = 524, KEY_ALT = 18, KEY_SPACE = 32, KEY_MENU = 525, KEY_LEFT = 37;
  public static char KEY_DOWN = 40, KEY_RIGHT = 39, KEY_UP = 38, KEY_HOME = 36, KEY_PAGEUP = 33, KEY_END = 35, KEY_PAGEDOWN = 34;
  public static char KEY_DEL = 127, KEY_INSERT = 155, KEY_NUMLOCK = 144;

  private static final Logger LOG = Logger.getLogger(Functions.class.getName());

  private Functions() {}
}
