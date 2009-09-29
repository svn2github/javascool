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
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/** Définit une proglet javascool qui permet de faire des entrées/sorties au clavier.
 * @see <a href="Konsol.java">code source</a>
 */
public class Konsol { private Konsol() { }
  private static final long serialVersionUID = 1L;

  // This defines the panel to display
  private static class Panel extends JPanel {
    private static final long serialVersionUID = 1L;

    public Panel() {
      super(new BorderLayout()); 
      setBackground(Color.WHITE); setPreferredSize(new Dimension(400, 500));
      out = new JEditorPane(); out.setEditable(false);  out.setContentType("text/html; charset=UTF-8");
      pane = new JScrollPane(); pane.setViewportView(out); pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); add(pane);
      pane.setBorder(BorderFactory.createTitledBorder("output")); 
      JPanel bar = new JPanel(); add(bar, BorderLayout.NORTH);    
      prompt = new JLabel("input>"); bar.add(prompt);
      in = new JTextField(); in.setEditable(false); in.setPreferredSize(new Dimension(300, 30)); bar.add(in);    
      in.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) {
	// Reads the input text and reset the interface
	input = ((JTextField) e.getSource()).getText(); in.setEditable(false);
      }});
      bar.add(new JLabel("|")); JButton clear = new JButton("clear output"); bar.add(clear);
      clear.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) {
	clear();
      }});
    }

    /** Writes a string.
     * @param string The string to write.
     * @param html If true writes in html else in plain text
     */
    public void writeString(String string, boolean html) {
      output += (html ? string : quote(string))+"<br/>\n";
      out.setText("<html><body>"+output+"</body></html>"); 
      pane.getVerticalScrollBar().setValue(pane.getVerticalScrollBar().getMaximum());
    }
    private JEditorPane out; private JScrollPane pane;

    /** Clears the output. */
    public void clear() {
      out.setText(output = "");
      prompt.setText("input>"); 
      in.setText(""); 
    }

    /** Reads a string.
     * @param retry Set to true in retry mode, after a wrong input.
     * @return The read string.
     */
    public String readString(boolean retry) {
      if (retry) prompt.setText("!error!"); else in.setText(""); 
      // Interaction loop with in action listener
      in.setEditable(true); 
      try {
	while(in.isEditable()) 
	  Thread.sleep(100);
      } catch(Exception e) {
	in.setEditable(false); 
	prompt.setText("input>"); 
	throw new RuntimeException("Program stopped !");
      }
      prompt.setText("input>"); 
      return input == null ? "" : input;
    }
    private JLabel prompt; private JTextField in; private String input, output = "";
  }

  // Quotes a string for HTML
  private static String quote(String string) { return string.replaceAll("&", "&amp;").replaceAll("<", "&lt;"); }

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
    //for(int i = 0; i < 100; i++)
    println("He je suis plus vieux que toi !!");
  }

  //
  // This defines the javascool interface
  //

  /** Ecrit une chaine de caractères dans la fenêtre de sortie (output).
   * @param string La chaine à écrire.
   */
  public static void println(String string) {
    panel.writeString(string, false);
  }

  /** Ecrit une chaine de caractères dans la fenêtre de sortie (output).
   * @param string La chaine à écrire.
   */
  public static void println(long string) {
    panel.writeString(""+string, false);
  }

  /** Ecrit une chaine de caractères dans la fenêtre de sortie (output).
   * @param string La chaine à écrire.
   */
  public static void println(double string) {
    panel.writeString(""+string, false);
  }

  /** Ecrit une chaine de caractères dans la fenêtre de sortie (output).
   * @param string La chaine à écrire.
   */
  public static void println(boolean string) {
    panel.writeString(""+string, false);
  }

  /** Ecrit une chaine de caractères colorée dans la fenêtre de sortie (output).
   * @param string La chaine à écrire.
   * @param couleur La couleur: "black" (default), "blue", "cyan", "gray", "green", "magenta", "orange", "pink", "red", "white", "yellow".
   */
  public static void println(String string, String couleur) {
    panel.writeString("<span style='color:"+couleur+"'>"+quote(string)+"</span>", true);
  }

  /** Ecrit une chaine de caractères avec des codes HTML dans la fenêtre de sortie (output).
   * <br>-Pour écrire en couleur on peut par exemple utiliser: 
   * <div><tt>printHtml("Oh, je vois &lt;span style='color:red;font-weight:bold'>rouge&lt;/span> !");</tt></div> 
   * où le code HTML spécifie la couleur du texte en rouge (<tt>color:red</tt>) et d'utiliser des caractères gras (<tt>font-weight:bold</tt>).
   * @param string La chaine à écrire.
   */
  public static void printHtml(String string) {
    panel.writeString(string, true);
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
   * <br> Voir <a href="#readInt()">readInt</a> dont il est synonyme.
   */
  public static int readInteger() {
    for(boolean retry = false; true; retry = true) {
      try {
	return Integer.parseInt(panel.readString(retry));
      } catch(Exception e) { }
    }
  }
  
  /** Lit un nombre entier dans la fenêtre d'entrée (input).
   * @return Le nombre entier lu.
   * <br> Voir <a href="#readInteger()">readInteger</a> dont il est synonyme.
   */
  public static int readInt() {
    return readInteger();
  }

  /** Lit un nombre décimal dans la fenêtre d'entrée (input).
   * @return Le nombre décimal lu.
   * <br> Voir <a href="#readDouble()">readDouble</a> dont il est synonyme.
   */
  public static double readFloat() {
    for(boolean retry = false; true; retry = true) {
      try {
	return Double.parseDouble(panel.readString(retry));
      } catch(Exception e) { }
    }
  }

  /** Lit un nombre décimal dans la fenêtre d'entrée (input).
   * @return Le nombre décimal lu.
   * <br> Voir <a href="#readFloat()">readFloat</a> dont il est synonyme.
   */
  public static double readDouble() {
    return readFloat();
  }

  /** Lit un booléen dans la fenêtre d'entrée (input).
   * @return Le booléen lu
   */
  public static boolean readBoolean() {
    for(boolean retry = false; true; retry = true) {
      String rep = panel.readString(retry).toLowerCase().trim();
      if (rep.matches("(vrai|v|true|t|oui|o|yes|y|1|ok)")) return true;
      if (rep.matches("(faux|f|false|non|n|no|0|ko)")) return false;
    }
  }

  /** Définition de l'interface graphique de la proglet. */
  public static final Panel panel = new Panel();
}
