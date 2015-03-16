package org.javascool.proglets.commSerie;

import javax.swing.BorderFactory;
import javax.swing.JTextField;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JLabel;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**  Interface graphique: actuellement un convertisseur décimal en héxadécimal **/
public class ConvertisseurPanel extends JPanel {
  private static final long serialVersionUID = 1L;
  // Pour saisir la chaîne de caractères
  private JTextField enASCII;

  // Pour afficher les codes ASCII de la chaîne de caractères
  private JTextField enHEx;

  // Le constructeur
  public ConvertisseurPanel() {
    setLayout(new BorderLayout());

    // Incrustation du nom dans le bas de la fenêtre
    String texte = "<html><b><font color=\"#0000FF\">   Convertisseur ASCII->héxadécimal  </font></b></html>";
    JLabel lblAuteur = new JLabel(texte, JLabel.RIGHT);
    this.add("South", lblAuteur);

    /***                                 Panel des zones de texte                                  **/

    JPanel panelHaut = new JPanel();
    panelHaut.setLayout(new BoxLayout(panelHaut, BoxLayout.Y_AXIS));
    this.add("Center", panelHaut);

    JPanel panelDonnees = new JPanel();
    panelDonnees.setLayout(new BoxLayout(panelDonnees, BoxLayout.Y_AXIS));
    panelDonnees.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0), BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Saisir une chaîne ")));
    panelHaut.add(panelDonnees);

    /***                                  La saisie de la chaîne de caractères                      **/

    JPanel panelASCII = new JPanel(new FlowLayout());
    panelDonnees.add(panelASCII);

    JLabel lblChaine = new JLabel("Chaîne de caractères : ");
    panelASCII.add(lblChaine);

    enASCII = new JTextField();
    lblChaine.setLabelFor(enASCII);

    // les propriétés de la zone de saisie
    enASCII.setEditable(true);    // pour la saisie
    enASCII.setBackground(Color.black);
    enASCII.setForeground(Color.green);
    enASCII.setHorizontalAlignment(JTextField.CENTER);
    enASCII.setPreferredSize(new Dimension(250, 35));
    panelASCII.add(enASCII);

    /***               L'affichage des codes ASCII de la chaîne de caractères                    **/

    JPanel panelHEX = new JPanel(new FlowLayout());
    panelDonnees.add(panelHEX);

    JLabel lblHEX = new JLabel(" Codes ASCII                    : ");
    panelHEX.add(lblHEX);

    enHEx = new JTextField();
    lblHEX.setLabelFor(enHEx);

    // les propriétés de la zone d'affichage
    enHEx.setEditable(false);
    enHEx.setBackground(Color.cyan);
    enHEx.setForeground(Color.blue);
    enHEx.setHorizontalAlignment(JTextField.CENTER);
    enHEx.setPreferredSize(new Dimension(250, 35));
    panelHEX.add(enHEx);

    Font font1 = new Font("taille1", Font.BOLD, 16);
    enASCII.setFont(font1);

    Font font2 = new Font("taille2", Font.BOLD, 14);
    enHEx.setFont(font2);

    /***                                 Panel des Boutons                                     **/

    JPanel panelBoutons = new JPanel();
    panelBoutons.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0), BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "")));
    panelHaut.add(panelBoutons);

    // Bouton EFFACER
    JButton cmdEffacer = new JButton("Effacer");
    cmdEffacer.setPreferredSize(new Dimension(130, 30));
    cmdEffacer.addActionListener(new ActionListener() {
                                   public void actionPerformed(ActionEvent evt) {
                                     effacer();
                                   }
                                 }
                                 );
    panelBoutons.add(cmdEffacer);

    // Bouton CONVERTIR
    JButton cmdConvertir = new JButton("Convertir");
    cmdConvertir.setPreferredSize(new Dimension(130, 30));
    cmdConvertir.addActionListener(new ActionListener() {
                                     public void actionPerformed(ActionEvent evt) {
                                       convertir();
                                     }
                                   }
                                   );
    panelBoutons.add(cmdConvertir);
  }
  /***         convertit un tableau d'entiers en chaîne de codes Hexa (code ASCII)              **/

  static String byteArrayToHexString(byte[] bArray) {
    StringBuffer buffer = new StringBuffer();
    for(byte b : bArray) {
      buffer.append(Integer.toHexString(b));
      buffer.append(" ");
    }
    return buffer.toString().toUpperCase();
  }
  /***             Assure la conversion ASCII des caractères saisis               **/

  private void convertir() {
    String chaine = enASCII.getText();

    // traduction en tableau de code ASCII :
    byte[] bytes = chaine.getBytes();

    enHEx.setText(byteArrayToHexString(bytes));  // on affiche les codes ASCII
  }
  // Pour effacer les champs d'affichage
  private void effacer() {
    enHEx.setText("");
    enASCII.setText("");
  }
  /** Ouvre le panel en application.
   * @param usage <tt>java -cp javascool-proglets.jar org.javascool.proglets.commSerie.ConvertisseurPanel</tt>
   */
  public static void main(String[] usage) {
    new org.javascool.widgets.MainFrame().reset("Codes ASCII héxadécimal d'une chaîne de carctères", 600, 300, new ConvertisseurPanel());
  }
} // fin Panel()

