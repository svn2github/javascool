/*******************************************************************************
 * Thierry.Vieville@sophia.inria.fr, Copyright (C) 2009.  All rights reserved. *
 *******************************************************************************/

//package org.javascool.konsol;

// Used to define the gui
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.BorderFactory;

// Used for the read/write
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Used to open an window
import javax.swing.JFrame;

/** Defines a javascool proglet which allows to experiment the konsoltomic search. 
 * Static method import: <pre>
 * import static Konsol.echo;
 * import static Konsol.readString;
 * import static Konsol.readInteger;
 * import static Konsol.readFloat;
 * </pre>
 * Documentation: <a href="sujet.html">subject</a> and <a href="correction.html">correction</a>.
 */
public class Konsol {

  // This defines the panel display
  private static class Panel extends JPanel {
    /** Constructs the panel. */
    public Panel() {
      super(new BorderLayout()); 
      setBackground(Color.WHITE); setPreferredSize(new Dimension(400, 500));
      out = new JTextArea(); out.setEditable(false); out.setBorder(BorderFactory.createTitledBorder("output")); 
      pane = new JScrollPane(); pane.setViewportView(out); pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); add(pane);
      JPanel bar = new JPanel(); add(bar, BorderLayout.NORTH);    
      prompt = new JLabel("input>"); bar.add(prompt);
      in = new JTextField(); in.setEditable(false); in.setPreferredSize(new Dimension(300, 30)); bar.add(in);    
      in.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {
	input = ((JTextField) e.getSource()).getText();
      }});
    }

    /** Writes a string. 
     * @param string The string to write.
     */
    public void writeString(String string) {
      out.setText(out.getText()+string+"\n"); pane.getVerticalScrollBar().setValue(pane.getVerticalScrollBar().getMaximum());
    }
    private JTextArea out; private JScrollPane pane;

    /** Reads a string. 
     * @param retry If true the reading is a retry, the previous reading being erroneous.
     */
    public String readString(boolean retry) {
      prompt.setText(retry ? "!error!" : "input>"); in.setText(retry ? in.getText() : ""); in.setEditable(true); 
      input = null; while(input == null) Thread.yield(); 
      prompt.setText("input>"); in.setEditable(false); return input;
    }
    private JLabel prompt; private JTextField in; private String input;
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
      frame.setTitle("Konsol test"); frame.setSize(400, 500); frame.getContentPane().add(getPanel()); frame.pack(); frame.setVisible(true);
    }
    // Tests the mechanism
    {
      echo("Bonjour, qui est tu ?");
      String nom = readString();
      echo ("Echanté "+nom+" ! Quel age as tu ?");
      int age = readInteger();
      for(int n = 0; n < 100; n++)
	echo("He je suis plus vieux que toi !!");
    }
   }

  //
  // This defines the javascool interface
  //

  /** Outputs a string in the output windows.
   * @param string The string to write.
   */
  public static void echo(String string) {
    panel.writeString(string);
  }

  /** Inputs a string from the input window. */
  public static String readString() {
    return panel.readString(false);
  }
  
  /** Inputs an integer number from the input window. */
  public static int readInteger() {
    for(boolean retry = false; true; retry = true) {
      try {
	return Integer.parseInt(panel.readString(retry));
      } catch(Exception e) { }
    }
  }

  /** Inputs a floating point number from the input window. */
  public static double readFloat() {
    for(boolean retry = false; true; retry = true) {
      try {
	return Double.parseDouble(panel.readString(retry));
      } catch(Exception e) { }
    }
  }

  private static Konsol.Panel panel = new Konsol.Panel();
}
