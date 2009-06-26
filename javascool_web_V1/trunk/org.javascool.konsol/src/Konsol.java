/*******************************************************************************
 * Thierry.Vieville@sophia.inria.fr, Copyright (C) 2009.  All rights reserved. *
 *******************************************************************************/

//package org.javascool.konsol;

// Used to define the gui
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.BorderLayout;
import javax.swing.JLayeredPane;
import java.awt.Dimension;

// Used to define an icon/label
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.net.URL;

// Used to define a button
import javax.swing.JButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Used to open an window
import javax.swing.JFrame;

/** Defines a javascool panel which allows to experiment the konsoltomic search. 
 * Static method import: <pre>
 * import static Konsol.echo;
 * import static Konsol.readString;
 * import static Konsol.readInt;
 * import static Konsol.readDouble;
 * </pre>
 * Documentation: <a href="sujet.html">subject</a> and <a href="correction.html">correction</a>.
 */
public class Konsol {
  private static class Panel extends JPanel {
    /** Constructs the panel. */
    public Panel() {
      super(new BorderLayout()); 
      setBackground(Color.WHITE);
      // Adds the background icon
     }
  }

  // Static instantiation of a panel
  /** Returns the panel. */
  public static JPanel getPanel() { return panel; } 
  
  /** Used to test this panel.
   * <div>Simply use: <tt>javac Konsol.java ; java Konsol</tt> to test.</div>
   * @param arguments No argument, do not user.
   */
  public static void main(String arguments[]) {
    // Opens the panel in a frame
    {
      JFrame frame = new JFrame();
      frame.setTitle("Konsol test"); frame.setSize(550, 400); frame.getContentPane().add(getPanel()); frame.pack(); frame.setVisible(true);
    }
   }

  private static Konsol.Panel panel = new Konsol.Panel();
}
