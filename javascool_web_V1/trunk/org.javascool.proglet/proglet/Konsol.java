/*******************************************************************************
 * Thierry.Vieville@sophia.inria.fr, Copyright (C) 2009.  All rights reserved. *
 *******************************************************************************/

package proglet;

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

/** Définit une proglet javascool qui permet d'expérimenter la recherche dichotomique.
 * Méthodes statiques à importer: <pre>
 * import static Konsol.println;
 * import static Konsol.readString;
 * import static Konsol.readInteger;
 * import static Konsol.readFloat;
 * </pre>
 */
public class Konsol {
  private static final long serialVersionUID = 1L;

  // This defines the panel to display
  private static class Panel extends JPanel {
    private static final long serialVersionUID = 1L;

    public Panel() {
      super(new BorderLayout()); 
      setBackground(Color.WHITE); setPreferredSize(new Dimension(400, 500));
      out = new JTextArea(); out.setEditable(false); 
      pane = new JScrollPane(); pane.setViewportView(out); pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); add(pane);
      pane.setBorder(BorderFactory.createTitledBorder("output")); 
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
      out.setText(out.getText()+string+"\n"); 
      pane.getVerticalScrollBar().setValue(pane.getVerticalScrollBar().getMaximum());
    }
    private JTextArea out; private JScrollPane pane;

    /** Clears the output. */
    public void clear() {
      out.setText("");
    }

    /** Reads a string.
     * @param retry Set to true in retry mode, after a wrong input.
     * @return The read string.
     */
    public String readString(boolean retry) {
      prompt.setText(retry ? "!error!" : "input>"); in.setText(retry ? in.getText() : ""); in.setEditable(true); 
      input = null; while(input == null) Thread.yield(); 
      prompt.setText("input>"); in.setEditable(false); return input;
    }
    private JLabel prompt; private JTextField in; private String input;
  }

  //
  // This defines the tests on the panel
  //

  /** Test du panel. */
  static void test() {
    clear();
    println("Bonjour, qui est tu ?");
    String nom = readString();
    println ("Echanté "+nom+" ! Quel age as tu ?");
    int age = readInteger();
    for(int n = 0; n < 100; n++)
      println("He je suis plus vieux que toi !!");
  }

  //
  // This defines the javascool interface
  //

  /** Ecrit une chaine de caractères dans la fenêtre de sortie (output).
   * @param string La chaine à écrire.
   */
  public static void println(String string) {
    panel.writeString(string);
  }

  /** Efface tout ce qui est déjà écrit dans la console. */
  public static void clear() {
    panel.clear();
  }

  /** Lit une chaîne de caractère dans la fenêtre d'entrée (input).
   * @return La chaîne lue.
   */
  public static String readString() {
    return panel.readString(false);
  }
  
  /** Lit un nombre entier dans la fenêtre d'entrée (input).
   * @return Le nombre entier lu.
   */
  public static int readInteger() {
    for(boolean retry = false; true; retry = true) {
      try {
	return Integer.parseInt(panel.readString(retry));
      } catch(Exception e) { }
    }
  }

  /** Lit un nombre flottant dans la fenêtre d'entrée (input).
   * @return Le nombre flottant lu.
   */
  public static double readFloat() {
    for(boolean retry = false; true; retry = true) {
      try {
	return Double.parseDouble(panel.readString(retry));
      } catch(Exception e) { }
    }
  }

  public static final Panel panel = new Panel();
}
