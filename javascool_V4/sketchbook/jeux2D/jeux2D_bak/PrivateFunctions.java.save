package org.javascool.proglets.jeux2D;

/**
 * PrivateFunctions.java
 * Part of Javascool proglet "game"
 * File creation : 20110704 at revision 48 by gmatheron (INRIA)
 * Last modification 20110704 by gmatheron (INRIA)
 *
 * gmatheron : guillaume.matheron.06@gmail.com
 */
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.javascool.widgets.Console;
import org.javascool.macros.Macros;

/* To use these event listeners, use this syntax :
 * void toto(MouseState s) {
 *     ...
 * }
 * void main() {
 *     onClick("toto");
 * }
 */

/**
 * Main class for this proglet, it implements EventCatcher to allow it to catch
 * events : is you simply use
 * <code>onClick("exit");</code>
 * PrivateFunctions will be used as EventCatcher (but it always return true and catches the event when triggered)
 *
 * @author gmatheron
 */
public class PrivateFunctions implements EventCatcher {
  // TODO javadoc

  /*
   * See getFunctionsElementSingleton()
   */
  public static PrivateFunctions m_singleton;

  @Override
  public boolean isDestroyed() {
    return false;
  }
  public static GamePanel getGamePanel() {
    return Panel.m_panel;
  }
  /**
   * A singleton is used more for legacy than anything else. Everything could be
   * declared static but it probably would be a mess to refactor everything...
   * @return the singleton
   */
  public static PrivateFunctions getFunctionsElementSingleton() {
    return m_singleton;
  }
  /**
   * Stores the state of each mouse button
   */
  public boolean m_mouseDown[] = { false, false, false };
  public java.util.ArrayList<Integer> m_keysPressed;

  /**
   * Stores position of the mouse wheel (in blocks) relative to its state at the
   * beginning of the user-defined program (to be checked)
   */
  public double m_mouseWheelPosition = 0;

  /* These arrays are designed to store the functions the user assigned a listener
   * A convenience type is used : EventListener
   */
  @SuppressWarnings("PublicField")
  public java.util.ArrayList<EventListener> m_onClick;
  @SuppressWarnings("PublicField")
  public java.util.ArrayList<EventListener> m_onMouseEntered;
  @SuppressWarnings("PublicField")
  public java.util.ArrayList<EventListener> m_onMouseExited;
  @SuppressWarnings("PublicField")
  public java.util.ArrayList<EventListener> m_onMousePressed;
  @SuppressWarnings("PublicField")
  public java.util.ArrayList<EventListener> m_onMouseReleased;
  @SuppressWarnings("PublicField")
  public java.util.ArrayList<EventListener> m_onMouseDown;
  @SuppressWarnings("PublicField")
  public java.util.ArrayList<EventListener> m_onMouseUp;
  @SuppressWarnings("PublicField")
  public java.util.ArrayList<EventListener> m_onMouseMoved;
  @SuppressWarnings("PublicField")
  public java.util.ArrayList<EventListener> m_onMouseDragged;
  @SuppressWarnings("PublicField")
  public java.util.ArrayList<EventListener> m_onMouseWheelUp;
  @SuppressWarnings("PublicField")
  public java.util.ArrayList<EventListener> m_onMouseWheelDown;
  @SuppressWarnings("PublicField")
  public java.util.ArrayList<EventListener> m_onMouseWheelMoved;
  @SuppressWarnings("PublicField")
  public java.util.ArrayList<EventListener> m_onFrame;
  @SuppressWarnings("PublicField")
  public java.util.ArrayList<EventListener> m_onKeyPressed;
  @SuppressWarnings("PublicField")
  public java.util.ArrayList<EventListener> m_onKeyReleased;
  @SuppressWarnings("PublicField")
  public java.util.ArrayList<EventListener> m_onKeyDown;
  @SuppressWarnings("PublicField")
  public java.util.ArrayList<EventListener> m_onKeyUp;
  @SuppressWarnings("PublicField")
  public java.awt.event.MouseListener m_mouseListener;
  @SuppressWarnings("PublicField")
  public java.awt.event.MouseMotionListener m_mouseMotionListener;
  @SuppressWarnings("PublicField")
  public java.awt.event.MouseWheelListener m_mouseWheelListener;
  @SuppressWarnings("PublicField")
  public java.awt.event.KeyListener m_keyListener;

  public static boolean classExtends(Class< ? > sub, Class< ? > superClass) {
    if(sub.getSuperclass() == superClass)
      return true;
    else if(sub.getSuperclass() == Object.class )
      return false;
    else
      return classExtends(sub.getSuperclass(), superClass);
  }
  /**
   * Calls the specified end-user-defined method, passing as a parameter the specified state.
   * If the function is undefined with one argument, it will be called with no arguments.
   * @param method The end-user-defined method to call
   * @param s The state to pass
   */
  private static void call(String method, Object s) {
    boolean found = false;
    try {
      for(int i = 0; i < Macros.getProgram().getClass().getMethods().length; i++) {
        java.lang.reflect.Method m = Macros.getProgram().getClass().getMethods()[i];
        if(m.getName().equals(method)) {
          int params = m.getParameterTypes().length;
          if(params == 0) {
            m.invoke(Macros.getProgram());
            found = true;
          } else if(params == 1)
            if(m.getParameterTypes()[0] == s.getClass()) {
              m.getParameterTypes()[0].cast(s);
              m.invoke(Macros.getProgram(), m.getParameterTypes()[0].cast(s));
              found = true;
            }
        }
      }
    } catch(IllegalAccessException ex) {
      Logger.getLogger(PrivateFunctions.class.getName()).log(Level.SEVERE, null, ex);
    } catch(IllegalArgumentException ex) {
      Logger.getLogger(PrivateFunctions.class.getName()).log(Level.SEVERE, null, ex);
    } catch(InvocationTargetException ex) {
      Logger.getLogger(PrivateFunctions.class.getName()).log(Level.SEVERE, null, ex);
    } catch(SecurityException ex) {
      Logger.getLogger(PrivateFunctions.class.getName()).log(Level.SEVERE, null, ex);
    }
    if(!found) {
      Logger.getLogger(PrivateFunctions.class.getName()).log(Level.SEVERE, null, new Exception("Callback method " + method + " not found"));
      for(int i = 0; i < Macros.getProgram().getClass().getMethods().length; i++) {
        java.lang.reflect.Method m = Macros.getProgram().getClass().getMethods()[i];
        System.out.println(m.getName());
      }
      System.out.println("");
    }
  }
  /**
   * Calls the specified end-user-defined method
   * @param method The end-user-defined method to call
   */
  private static void call(String method) {
    boolean found = false;
    try {
      for(int i = 0; i < Macros.getProgram().getClass().getMethods().length; i++) {
        java.lang.reflect.Method m = Macros.getProgram().getClass().getMethods()[i];
        if(m.getName().equals(method))
          if(m.getParameterTypes().length == 0) {
            m.invoke(Macros.getProgram());
            found = true;
          }
      }
    } catch(IllegalAccessException ex) {
      Logger.getLogger(PrivateFunctions.class.getName()).log(Level.SEVERE, null, ex);
    } catch(IllegalArgumentException ex) {
      Logger.getLogger(PrivateFunctions.class.getName()).log(Level.SEVERE, null, ex);
    } catch(InvocationTargetException ex) {
      Logger.getLogger(PrivateFunctions.class.getName()).log(Level.SEVERE, null, ex);
    } catch(SecurityException ex) {
      Logger.getLogger(PrivateFunctions.class.getName()).log(Level.SEVERE, null, ex);
    }
    if(!found)
      Logger.getLogger(PrivateFunctions.class.getName()).log(Level.SEVERE, null, new Exception("Callback method " + method + " not found"));
  }
  /**
   * This method is called after the program finishes running by Clock
   * It stops the timer and deletes the listeners
   */
  public static void stop() {
    GamePanel p = Panel.m_panel;
    p.stop();
    m_exit = true;

    /* try {
     *    m_clock.exitClean();
     *  } catch (Exception e) {
     *    System.err.println(e.getMessage());
     *  }*/
    Panel.m_panel.removeMouseListener(m_singleton.m_mouseListener);
    Panel.m_panel.removeMouseMotionListener(m_singleton.m_mouseMotionListener);
    Panel.m_panel.removeMouseWheelListener(m_singleton.m_mouseWheelListener);

    m_singleton.m_onClick.removeAll(m_singleton.m_onClick);
    m_singleton.m_onMouseDown.removeAll(m_singleton.m_onMouseDown);
    m_singleton.m_onMouseDragged.removeAll(m_singleton.m_onMouseDragged);
    m_singleton.m_onMouseEntered.removeAll(m_singleton.m_onMouseEntered);
    m_singleton.m_onMouseExited.removeAll(m_singleton.m_onMouseExited);
    m_singleton.m_onMouseMoved.removeAll(m_singleton.m_onMouseMoved);
    m_singleton.m_onMousePressed.removeAll(m_singleton.m_onMousePressed);
    m_singleton.m_onMouseReleased.removeAll(m_singleton.m_onMouseReleased);
    m_singleton.m_onMouseUp.removeAll(m_singleton.m_onMouseUp);
    m_singleton.m_onFrame.removeAll(m_singleton.m_onFrame);
    m_singleton.m_onMouseWheelDown.removeAll(m_singleton.m_onMouseWheelDown);
    m_singleton.m_onMouseWheelUp.removeAll(m_singleton.m_onMouseWheelUp);
    m_singleton.m_onMouseWheelMoved.removeAll(m_singleton.m_onMouseWheelMoved);
    m_singleton.m_onKeyDown.removeAll(m_singleton.m_onKeyDown);
    m_singleton.m_onKeyUp.removeAll(m_singleton.m_onKeyUp);
    m_singleton.m_onKeyPressed.removeAll(m_singleton.m_onKeyPressed);
    m_singleton.m_onKeyReleased.removeAll(m_singleton.m_onKeyReleased);
  }
  /**
   * Stores a running clock that ticks at each frame and triggers frame-driven events
   */
  public static Clock m_clock;

  /**
   * This method is called during init
   * It creates the listeners, the clock and the singleton
   */
  @SuppressWarnings({ "AccessingNonPublicFieldOfAnotherObject", "CollectionWithoutInitialCapacity" }
                    )
  public static void start() {}

  /**
   * Calls each function of the specified array if the EventCatcher accepts it or is set to always
   * @param functions The functions to call
   */
  public static void callback(java.util.ArrayList<EventListener> functions) {
    for(int i = 0; i < functions.size(); i++) {
      if((functions.get(i).getObject() == null&& (functions.get(i).getObject().isForMe() || functions.get(i).getAlways())) && !functions.get(i).getObject().isDestroyed())
        call(functions.get(i).getMethod());
      else if((functions.get(i).getObject().isForMe() || functions.get(i).getAlways()) && !functions.get(i).getObject().isDestroyed())
        call(functions.get(i).getMethod(), functions.get(i).getObject());
    }
  }
  /**
   * A function directly assigned to the main panel will always be catched, so return true;
   * @return true
   */
  @Override
  public boolean isForMe() {
    return true;
  }
  /**
   * Set to true when the clock needs to exit
   */
  public static boolean m_exit = false;

  /**
   * This class allows the proglet to trigger events regularly. Depending on the
   * selected framerate (see setFps(int)), a main routine will be executed at the given
   * speed. At each loop, the clock will call the tick() method.
   */
  @SuppressWarnings("PublicInnerClass")
  public static class Clock implements Runnable {
    /**
     * Defines the framerate that the clock will try to achieve
     */
    private double m_fps = 30;
    private double m_lastTick = 0;

    /**
     * Default constructor, does nothing : see run()
     */
    Clock() {}

    /**
     * The clock is intended to be run as a thread, so it implements Runnable.
     * When ran, the clock will tick regularly. Be sure to call the setFps(double)
     * before running the clock, or it should (not tested) tick at 30 fps.
     * This method will break when m_exit is set to true (latency can be a bit more
     * that 1/m_fps seconds.
     */
    @Override @SuppressWarnings("SleepWhileInLoop")
    public void run() {
      double targetTimeMs = 1000 / m_fps;
      System.err.println("Starting clock");
      while(true) {
        if(!Console.isRunning() || m_exit) {
          System.err.println("Stopped clock. Console.isRunning()=" + (Console.isRunning() ? "yes" : "no") + ", m_exit=" + (m_exit ? "yes" : "no"));
          break;
        }
        m_lastTick = System.currentTimeMillis();
        tick();
        System.err.println("tick");

        double timeMs = System.currentTimeMillis() - m_lastTick;
        double sleepMs = targetTimeMs - timeMs;
        try {
          if(sleepMs > 0)
            Thread.sleep((int) (sleepMs));
        } catch(InterruptedException ex) {
          Logger.getLogger(PrivateFunctions.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
    }
    /**
     * Sets the target framerate. Please note that the execution of the callback function
     * is modal (it will block the clock's thread) so make sure to make
     * callback functions that return quickly.
     * @param fps
     */
    public void setFps(float fps) {
      m_fps = fps;
    }
    /**
     * Triggers :
     *     - The mouseDown event if any mouse button is pressed
     *     - The mouseUp event if no mouse button is pressed
     *     - The onFrame event
     * Forces the proglet panel to repaint
     */
    @SuppressWarnings("AccessingNonPublicFieldOfAnotherObject")
    private void tick() {
      Macros.getResource("test.txt");         // DEBUG
      for(int j = 0; j < 3; j++) {
        if(getFunctionsElementSingleton().m_mouseDown[j])
          callback(getFunctionsElementSingleton().m_onMouseDown);
        else
          callback(getFunctionsElementSingleton().m_onMouseUp);
      }
      if(!m_singleton.m_keysPressed.isEmpty())
        callback(getFunctionsElementSingleton().m_onKeyDown);
      callback(getFunctionsElementSingleton().m_onKeyUp);

      callback(getFunctionsElementSingleton().m_onFrame);
      Panel.m_panel.repaint();
    }
    public void exitClean() {
      m_exit = true;
    }
  }

  /**
   * Stores the cursor's position relative to the panel's topleft corner
   */
  @SuppressWarnings("PublicField")
  public float m_mousePosRelativeToPanelX;
  @SuppressWarnings("PublicField")
  public float m_mousePosRelativeToPanelY;
  private static final Logger LOG = Logger.getLogger(PrivateFunctions.class.getName());
}

/**
 * Superclass of all the states.
 *
 * If an abstract (State) class can't be declared into a non-abstract class (PrivateFunctions)
 * maybe we could switch it to an interface or leave it non-abstract (ugly)
 *
 * PLEASE NOTE THAT ALL THE SUBCLASSES OF STATE MUST BE FINAL : ALL THE STATES MUST DIRECTLY
 * EXTEND THIS CLASS. See 'call' for details
 */
@SuppressWarnings("MultipleTopLevelClassesInFile")
class State {
  private static final Logger LOG = Logger.getLogger(State.class.getName());

  @SuppressWarnings("PublicConstructorInNonPublicClass")
  public State() {}
}
