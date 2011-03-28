/*******************************************************************************
* Thierry.Vieville@sophia.inria.fr, Copyright (C) 2009.  All rights reserved. *
*******************************************************************************/

package proglet.ingredients;

// Used to define the gui
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.BorderFactory;

// Used for the read/write
import javax.swing.JToolBar;
import javax.swing.JLabel;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Used to load/save
import org.javascool.Utils;

// Used to redirect System.out
import java.io.PrintStream;
import java.io.ByteArrayOutputStream;

/** Définit une proglet javascool qui permet de faire des entrées/sorties au clavier.
 * @see <a href="doc-files/about-proglet.htm">Description</a>
 * @see <a href="doc-files/the-proglet.htm">La proglet</a>
 * @see <a href="Console.java.html">code source</a>
 * @serial exclude
 */
public class Console implements org.javascool.Proglet {
  private Console() {}
  private static final long serialVersionUID = 1L;

  // This defines the panel to display
  private static class Panel extends JPanel {
    private static final long serialVersionUID = 1L;

    public Panel() {
      super(new BorderLayout());
      setPreferredSize(new Dimension(400, 500));
      out = new JEditorPane();
      out.setEditable(false);
      out.setContentType("text/html; charset=UTF-8");
      out.setBackground(Color.WHITE);
      pane = new JScrollPane();
      pane.setViewportView(out);
      pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
      add(pane);
      pane.setBorder(BorderFactory.createTitledBorder("Affichage de la sortie"));
      JToolBar bar = new JToolBar();
      add(bar, BorderLayout.NORTH);
      bar.setOrientation(JToolBar.HORIZONTAL);
      bar.setBorderPainted(false);
      prompt = new JLabel(label);
      bar.add(prompt);
      in = new JTextField();
      in.setEditable(false);
      in.setPreferredSize(new Dimension(300, 30));
      bar.add(in);
      in.addActionListener(new ActionListener() {
                             public void actionPerformed(ActionEvent e) {
                               // Reads the input text and reset the interface
                               input = ((JTextField) e.getSource()).getText();
                               in.setEditable(false);
                             }
                           }
                           );
      JButton clear = new JButton("<html><body><center>Effacer<br>la<br>sortie</center></body></html>");
      add(clear, BorderLayout.EAST);
      clear.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent e) {
                                  clear();
                                }
                              }
                              );
    }
    /** Writes a string.
     * @param string The string to write.
     * @param html If true writes in html else in plain text
     */
    public void writeString(String string, boolean html) {
      output += (html ? string : quote(string));
      out.setText("<html><body> " + output + " </body></html>");
      // Select the end of the text
      out.selectAll();
      int i = out.getSelectionEnd();
      out.select(0, 0);
      out.setCaretPosition(i);
      out.revalidate();
    }
    private JEditorPane out;
    private JScrollPane pane;

    /** Clears the output. */
    public void clear() {
      inputs = "";
      out.setText(output = "");
      prompt.setText(label);
      in.setText("");
    }
    /** Reads a string.
     * @return The read string.
     */
    public String readString() {
      if(inputs.length() == 0) {
        // Interaction loop with in action listener
        in.setText("");
        in.setEditable(true);
        in.requestFocus();
        try {
          while(in.isEditable())
            Thread.sleep(100);
        } catch(Exception e) {
          in.setText("");
          in.setEditable(false);
          prompt.setText(label); throw new RuntimeException("Programme arrêté !");
        }
        prompt.setText(label);
        return input == null ? "" : input;
      } else {
        // Input from batch data
        int i = inputs.indexOf("\n");
        input = inputs.substring(0, i);
        in.setText(input);
        org.javascool.Macros.sleep(500);
        inputs = inputs.substring(i + 1);
        return input;
      }
    }
    private JLabel prompt;
    private String label = "Entrée au clavier > ";
    private JTextField in;
    private String inputs = "", input, output = "";

    /** Returns the present state of the console output. */
    public String getStdout() {
      return output;
    }
    /** Adds a string to feed the next input.
     * @param string The string to add.
     */
    public void addStdin(String string) {
      inputs += string.trim() + "\n";
    }
  }

  // Redirect the System.out to the console
  static {
    System.setOut(new PrintStream(new ByteArrayOutputStream() {
                                    public void write(byte[] b, int off, int len) {
                                      super.write(b, off, len);
                                      String string = toString();
                                      reset();
                                      panel.writeString(string, false);
                                    }
                                  }
                                  ));
  }

  /** Quotes a string for HTML. */
  private static String quote(String string) {
    return string.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(" ", "&nbsp;").replace("\n", "<br>\n");
  }
  //
  // This defines the tests on the panel
  //

  /**/public static void test() {
    clear();
    // System.out.println(new org.javascool.Pml().reset("<p>This is translated from <tt>XML</tt> !</p>", "xml"));
    println("Bonjour, qui es tu ?");
    String nom = readString();
    println("Enchanté " + nom + " ! Quel age as tu ?");
    int age = readInteger();
    // for(int i = 0; i < 100; i++)
    println("He je suis plus vieux que toi !!");
  }
  //
  // This defines the javascool interface
  //

  /** Ecrit une chaine de caractères dans la fenêtre de sortie (output) et passe à la ligne.
   * @param string La chaine à écrire.
   */
  public static void println(String string) {
    panel.writeString(string, false);
    panel.writeString("<br/>\n", true);
  }
  /**/public static void println(long string) {
    println("" + string);
  }
  /**/public static void println(double string) {
    println("" + string);
  }
  /**/public static void println(char string) {
    println("" + string);
  }
  /**/public static void println(boolean string) {
    println("" + string);
  }
  /**/public static void println(Object string) {
    println("" + string);
  }
  /** Ecrit une chaine de caractères dans la fenêtre de sortie (output) et sans passer à la ligne.
   * @param string La chaine à écrire.
   */
  public static void print(String string) {
    panel.writeString(string, false);
  }
  /**/public static void print(long string) {
    print("" + string);
  }
  /**/public static void print(double string) {
    print("" + string);
  }
  /**/public static void print(char string) {
    print("" + string);
  }
  /**/public static void print(boolean string) {
    print("" + string);
  }
  /**/public static void print(Object string) {
    print("" + string);
  }
  /** Ecrit une chaine de caractères colorée dans la fenêtre de sortie (output) et passe à la ligne.
   * @param string La chaine à écrire.
   * @param couleur La couleur: "black" (default), "blue", "cyan", "gray", "green", "magenta", "orange", "pink", "red", "white", "yellow".
   */
  public static void println(String string, String couleur) {
    panel.writeString("<span style='color:" + couleur + "'>" + quote(string) + "</span>", true);
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
  /** Efface tout ce qui est écrit ou à lire dans la console. */
  public static void clear() {
    org.javascool.JsMain.getMain().getFrame().showTab("Console");
    panel.clear();
  }
  /** Sauve ce qui est présentement écrit dans la console dans un fichier au format HTML.
   * @param location La localisation (chemin du fichier ou localisation internet) où sauver le texte.
   */
  public static void saveConsoleOutput(String location) {
    Utils.saveString(location, panel.getStdout());
  }
  /** Charge le contenu d'un fichier pour que son contenu serve d'entrée à la console.
   * @param location La localisation (chemin du fichier ou localisation internet) d'où charger le texte.
   */
  public static void loadConsoleInput(String location) {
    panel.addStdin(Utils.loadString(location));
  }
  /** Charge une chaine de caractère pour que son contenu serve d'entrée à la console.
   * @param string La chaine de caractère à ajouter.
   */
  public static void addConsoleInput(String string) {
    panel.addStdin(string);
  }
  /** Lit une chaîne de caractère dans la fenêtre d'entrée (input).
   * @return La chaîne lue.
   */
  public static String readString() {
    return panel.readString();
  }
  /** Lit un nombre entier dans la fenêtre d'entrée (input).
   * @return Le nombre entier lu ou 0 en cas d'erreur.
   * <br> Voir <a href="#readInt()">readInt</a> dont il est synonyme.
   */
  public static int readInteger() {
    return (int) readFloat();
  }
  /** Lit un nombre entier dans la fenêtre d'entrée (input).
   * @return Le nombre entier lu ou 0 en cas d'erreur.
   * <br> Voir <a href="#readInteger()">readInteger</a> dont il est synonyme.
   */
  public static int readInt() {
    return readInteger();
  }
  /** Lit un nombre décimal dans la fenêtre d'entrée (input).
   * @return Le nombre décimal lu ou 0 en cas d'erreur.
   * <br> Voir <a href="#readDouble()">readDouble</a> dont il est synonyme.
   */
  public static double readFloat() {
    while(true) {
      String rep = panel.readString().replaceAll("([0-9]),([0-9])", "$1.$2");
      try {
        return Double.parseDouble(rep);
      } catch(Exception e) {
        println("Erreur de saisie: ``" + rep + "´´ n'est pas un nombre");
      }
    }
  }
  /** Lit un nombre décimal dans la fenêtre d'entrée (input).
   * @return Le nombre décimal lu ou 0 en cas d'erreur.
   * <br> Voir <a href="#readFloat()">readFloat</a> dont il est synonyme.
   */
  public static double readDouble() {
    return readFloat();
  }
  /** Lit un booléen dans la fenêtre d'entrée (input).
   * @return Le booléen lu
   */
  public static boolean readBoolean() {
    while(true) {
      String rep = panel.readString().toLowerCase().trim();
      if(rep.matches("(vrai|vraie|v|true|t|oui|o|yes|y|1|ok)"))
        return true;
      if(rep.matches("(faux|fausse|f|false|non|n|no|0|ko)"))
        return false;
      println("Erreur de saisie: ``" + rep + "´´ n'est pas un booleen (utiliser ``vrai´´ ou ``faux´´)");
    }
  }
  /** Définition de l'interface graphique de la proglet. */
  public static final Panel panel = new Panel();
}
