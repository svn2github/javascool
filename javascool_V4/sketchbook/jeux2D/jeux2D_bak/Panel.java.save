package org.javascool.proglets.jeux2D;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.applet.Applet;
import java.awt.Color;
import java.util.logging.Logger;
import javax.swing.JApplet;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.javascool.widgets.Console;
import org.javascool.macros.Macros;

/**
 *
 * @author gmatheron
 */
public class Panel extends JApplet {
  private static final long serialVersionUID = 1L;
  private static final Logger LOG = Logger.getLogger(Panel.class.getName());
  public static GamePanel m_panel;

  public Panel() {
    m_panel = new GamePanel();
    setContentPane(m_panel);
  }
  public static GamePanel getGamePanel() {
    return m_panel;
  }
  @Override
  public void init() {
    /* A singleton is used to be able to define non-static classes inside PrivateFunctions
     * The functions the end-user can call are all static, but they refer
     * to non-static attributes using this singleton static attribute
     */

    System.err.println("in Panel.init()");
    if(PrivateFunctions.m_singleton != null)
      stop();
    PrivateFunctions.m_exit = false;

    PrivateFunctions.m_singleton = new PrivateFunctions();

    PrivateFunctions.m_singleton.m_keysPressed = new java.util.ArrayList<Integer>();

    /* These arrays store the listeners that should be called when an event occurs
     */
    PrivateFunctions.m_singleton.m_onClick = new java.util.ArrayList<EventListener>();
    PrivateFunctions.m_singleton.m_onMouseDown = new java.util.ArrayList<EventListener>();
    PrivateFunctions.m_singleton.m_onMouseDragged = new java.util.ArrayList<EventListener>();
    PrivateFunctions.m_singleton.m_onMouseEntered = new java.util.ArrayList<EventListener>();
    PrivateFunctions.m_singleton.m_onMouseExited = new java.util.ArrayList<EventListener>();
    PrivateFunctions.m_singleton.m_onMouseMoved = new java.util.ArrayList<EventListener>();
    PrivateFunctions.m_singleton.m_onMousePressed = new java.util.ArrayList<EventListener>();
    PrivateFunctions.m_singleton.m_onMouseReleased = new java.util.ArrayList<EventListener>();
    PrivateFunctions.m_singleton.m_onMouseUp = new java.util.ArrayList<EventListener>();
    PrivateFunctions.m_singleton.m_onFrame = new java.util.ArrayList<EventListener>();
    PrivateFunctions.m_singleton.m_onMouseWheelDown = new java.util.ArrayList<EventListener>();
    PrivateFunctions.m_singleton.m_onMouseWheelUp = new java.util.ArrayList<EventListener>();
    PrivateFunctions.m_singleton.m_onMouseWheelMoved = new java.util.ArrayList<EventListener>();
    PrivateFunctions.m_singleton.m_onKeyDown = new java.util.ArrayList<EventListener>();
    PrivateFunctions.m_singleton.m_onKeyUp = new java.util.ArrayList<EventListener>();
    PrivateFunctions.m_singleton.m_onKeyPressed = new java.util.ArrayList<EventListener>();
    PrivateFunctions.m_singleton.m_onKeyReleased = new java.util.ArrayList<EventListener>();

    /* The clock object will 'tick' each 1/30s. it will then call the PrivateFunctions.callback
     * functions for onMouseDown, onMouseUp, etc if needed
     */
    System.err.println("Creating clock");
    PrivateFunctions.m_clock = new PrivateFunctions.Clock();
    PrivateFunctions.m_clock.setFps(30);
    (new Thread(PrivateFunctions.m_clock)).start();

    /* Define a few anonymous classes that will define the proglet's behavior
     * when an event is performed. Uually the proglet will call all the PrivateFunctions.callback
     * functions for this event.
     * Possible optimization : only register a listener if an event is defined
     */
    // <editor-fold defaultstate="collapsed" desc="Anonymous classes">
    {
      System.err.println("Creating listeners");
      /********* START ANONYMOUS CLASSES ***************/
      PrivateFunctions.m_singleton.m_mouseListener = new java.awt.event.MouseListener() {
        @Override
        public void mouseClicked(java.awt.event.MouseEvent evt) {
          PrivateFunctions.callback(PrivateFunctions.m_singleton.m_onClick);
          m_panel.grabFocus();
        }
        @Override
        public void mouseEntered(java.awt.event.MouseEvent evt) {
          PrivateFunctions.callback(PrivateFunctions.m_singleton.m_onMouseEntered);
        }
        @Override
        public void mouseExited(java.awt.event.MouseEvent evt) {
          PrivateFunctions.callback(PrivateFunctions.m_singleton.m_onMouseExited);
        }
        @Override
        public void mousePressed(java.awt.event.MouseEvent evt) {
          PrivateFunctions.callback(PrivateFunctions.m_singleton.m_onMousePressed);
          if(evt.getButton() == java.awt.event.MouseEvent.BUTTON1)
            PrivateFunctions.m_singleton.m_mouseDown[0] = true;
          else if(evt.getButton() == java.awt.event.MouseEvent.BUTTON2)
            PrivateFunctions.m_singleton.m_mouseDown[1] = true;
          else if(evt.getButton() == java.awt.event.MouseEvent.BUTTON3)
            PrivateFunctions.m_singleton.m_mouseDown[2] = true;
        }
        @Override
        public void mouseReleased(java.awt.event.MouseEvent evt) {
          PrivateFunctions.callback(PrivateFunctions.m_singleton.m_onMouseReleased);
          if(evt.getButton() == java.awt.event.MouseEvent.BUTTON1)
            PrivateFunctions.m_singleton.m_mouseDown[0] = false;
          else if(evt.getButton() == java.awt.event.MouseEvent.BUTTON2)
            PrivateFunctions.m_singleton.m_mouseDown[1] = false;
          else if(evt.getButton() == java.awt.event.MouseEvent.BUTTON3)
            PrivateFunctions.m_singleton.m_mouseDown[2] = false;
        }
      };
      m_panel.addMouseListener(PrivateFunctions.m_singleton.m_mouseListener);

      PrivateFunctions.m_singleton.m_mouseMotionListener = new java.awt.event.MouseMotionListener() {
        @Override
        public void mouseDragged(MouseEvent e) {
          PrivateFunctions.m_singleton.m_mousePosRelativeToPanelX = e.getX();
          PrivateFunctions.m_singleton.m_mousePosRelativeToPanelY = e.getY();
          PrivateFunctions.callback(PrivateFunctions.m_singleton.m_onMouseDragged);
          PrivateFunctions.callback(PrivateFunctions.m_singleton.m_onMouseMoved);
        }
        @Override
        public void mouseMoved(MouseEvent e) {
          PrivateFunctions.m_singleton.m_mousePosRelativeToPanelX = e.getX();
          PrivateFunctions.m_singleton.m_mousePosRelativeToPanelY = e.getY();
          PrivateFunctions.callback(PrivateFunctions.m_singleton.m_onMouseMoved);
        }
      };
      m_panel.addMouseMotionListener(PrivateFunctions.m_singleton.m_mouseMotionListener);

      PrivateFunctions.m_singleton.m_mouseWheelListener = new java.awt.event.MouseWheelListener() {
        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
          double copy = PrivateFunctions.m_singleton.m_mouseWheelPosition;
          PrivateFunctions.m_singleton.m_mouseWheelPosition += e.getWheelRotation();
          if(copy > PrivateFunctions.m_singleton.m_mouseWheelPosition)
            PrivateFunctions.callback(PrivateFunctions.m_singleton.m_onMouseWheelDown);
          else
            PrivateFunctions.callback(PrivateFunctions.m_singleton.m_onMouseWheelUp);
          PrivateFunctions.callback(PrivateFunctions.m_singleton.m_onMouseWheelMoved);
        }
      };
      m_panel.addMouseWheelListener(PrivateFunctions.m_singleton.m_mouseWheelListener);

      PrivateFunctions.m_singleton.m_keyListener = new java.awt.event.KeyListener() {
        @Override
        public void keyTyped(KeyEvent e) {}
        @Override
        public void keyPressed(KeyEvent e) {
          if(e.getKeyChar() == 65635)
            return;
          PrivateFunctions.m_singleton.m_keysPressed.add(e.getKeyCode());
          PrivateFunctions.callback(PrivateFunctions.m_singleton.m_onKeyPressed);
        }
        @Override
        public void keyReleased(KeyEvent e) {
          // Object cast so that the char given isn't taken as an index number
          PrivateFunctions.m_singleton.m_keysPressed.remove((Integer) e.getKeyCode());
          PrivateFunctions.callback(PrivateFunctions.m_singleton.m_onKeyReleased);
        }
      };
      m_panel.addKeyListener(PrivateFunctions.m_singleton.m_keyListener);
      /********* END ANONYMOUS CLASSES ***************/
    }
    // </editor-fold>
    m_panel.grabFocus();
  }
  Group demo_poissons;

  void demo_seRegrouper(Oval poisson) {
    double nbPoissons = demo_poissons.size();
    double sommeX = 0;
    double sommeY = 0;
    double sumCoef = 0;
    double sumForce = 0;
    for(int i = 0; i < demo_poissons.size(); i++) {
      Oval autrePoisson = (Oval) demo_poissons.get(i);
      if(!autrePoisson.equals(poisson)) {
        double deltaX = autrePoisson.getX() - poisson.getX();
        double deltaY = autrePoisson.getY() - poisson.getY();

        double force = (float) (4 / (Math.sqrt(deltaX * deltaX + deltaY * deltaY)));
        sumForce += force;

        double vecX = autrePoisson.getX() - poisson.getX();
        double vecY = autrePoisson.getY() - poisson.getY();
        double vecNorm = (double) (Math.sqrt(vecX * vecX + vecY * vecY));
        vecX /= vecNorm;
        vecY /= vecNorm;
        vecX *= force;
        vecY *= force;
        sommeX = sommeX + vecX;
        sommeY = sommeY + vecY;
      }
    }
    poisson.position(poisson.getX() + (double) (sommeX), poisson.getY() + (double) (sommeY));
    for(int i = 0; i < demo_poissons.size(); i++) {
      Oval autrePoisson = (Oval) demo_poissons.get(i);
      if(autrePoisson != poisson) {
        double ax = poisson.getX();
        double ay = poisson.getY();
        double bx = autrePoisson.getX();
        double by = autrePoisson.getY();
        double vecABX = bx - ax;
        double vecABY = by - ay;
        double normAB = (double) Math.sqrt(vecABX * vecABX + vecABY * vecABY);
        if(normAB < 2 * 5) {
          double overlap = 2 * 5 - normAB;
          vecABX /= normAB;
          vecABY /= normAB;
          vecABX *= overlap / 2;
          vecABY *= overlap / 2;
          autrePoisson.position(autrePoisson.getX() + vecABX, autrePoisson.getY() + vecABY);
          poisson.position(poisson.getX() - vecABX, poisson.getY() - vecABY);
        }
      }
    }
  }
  @Override
  public void start() {
    demo_poissons = new Group();
    for(int i = 0; i < 20; i++) {
      Oval poisson = new Oval(Math.random() * 1000, Math.random() * 1000, 2 * 5, 2 * 5);
      demo_poissons.add(poisson);
    }
    while(true)
      for(int i = 0; i < demo_poissons.size(); i++)
        demo_seRegrouper((Oval) demo_poissons.get(i));
  }
  @Override
  public void stop() {}
  @Override
  public void destroy() {
    System.err.println("destroy was called");
    PrivateFunctions.stop();
  }
  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {
    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 512, Short.MAX_VALUE)
      );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 391, Short.MAX_VALUE)
      );
  }  // </editor-fold>//GEN-END:initComponents
     // Variables declaration - do not modify//GEN-BEGIN:variables
     // End of variables declaration//GEN-END:variables
}
