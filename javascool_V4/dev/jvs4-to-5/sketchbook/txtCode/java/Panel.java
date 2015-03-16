package org.javascool.proglets.txtCode;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.BorderLayout;

/** Définit le panneau graphique de la proglet.
 * @see <a href="Panel.java.html">source code</a>
 * @serial exclude
 * @author Christophe Béasse <oceank2@gmail.com>
 */
public class Panel extends JPanel {

  private static final long serialVersionUID = 1L;

  // Construction de la proglet
  public Panel() {
    Font f = new Font("Courier New", Font.PLAIN, 11);
    textArea.setEditable(false);
    textArea.setEnabled(false);
    textArea.setFont(f);
    textArea.setRows(30);
    textArea.setColumns(80);
       
    add(new JScrollPane(textArea), BorderLayout.CENTER);

  }

  public static JTextArea textArea=new JTextArea();
}

