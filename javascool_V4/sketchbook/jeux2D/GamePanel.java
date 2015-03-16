package org.javascool.proglets.jeux2D;

/*
 * GamePanel.java
 *
 * Created on 1 juil. 2011, 11:02:55
 */
import java.awt.Color;
import java.util.logging.Logger;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JApplet;
import javax.swing.JPanel;

/**
 * Defines the proglet's main panel and render area
 * @author gmatheron
 */
public class GamePanel extends JPanel {
  private static final long serialVersionUID = 1L;

  /**
   * Dynamic list of objects that should be drawn to the fame render area
   */
  private java.util.ArrayList<Drawable> m_items;

  /** Creates new GamePanel and initiates the list of Drawable */
  @SuppressWarnings("CollectionWithoutInitialCapacity")
  public GamePanel() {
    m_items = new java.util.ArrayList<Drawable>();
    initComponents();
  }
  /**
   * Removes all the Drawable from the render area
   */
  public void stop() {
    System.err.println("In GamePanel.stop()");
    m_items.removeAll(m_items);
  }
  /**
   * Paints the current frame to the specified Graphics buffer
   * @param g The Graphics buffer of which to draw
   */
  @Override
  public void paintComponent(Graphics g) {
    // Clear the screen
    g.setColor(Color.BLACK);
    g.fillRect(0, 0, this.getWidth(), this.getHeight());

    // Set default color
    g.setColor(Color.WHITE);

    // Create backbuffer
    BufferedImage backBuffer = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_ARGB);
    // Draw all Drawable
    for(int i = 0; i < m_items.size(); i++) {
      if(m_items.get(i).isVisible()) {
        backBuffer.getGraphics().setColor(m_items.get(i).initColor());
      }
      if(m_items.get(i).isVisible()) {
        m_items.get(i).draw(backBuffer.getGraphics());
      }
    }
    // Blit !
    g.drawImage(backBuffer, 0, 0, null);
  }
  /**
   * Adds a Drawable to the render scene
   * @param d The Drawable to add to the render scene
   */
  public void addItem(Drawable d) {
    m_items.add(d);
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
  private static final Logger LOG = Logger.getLogger(GamePanel.class.getName());
}
