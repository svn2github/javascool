package org.javascool.proglets.ticTacToe;

import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.GridLayout;

/** Définit le panneau graphique de la proglet.
 * @see <a href="Panel.java.html">source code</a>
 * @serial exclude
 * @author Christophe Béasse <oceank2@gmail.com>
 */
public class Panel extends JPanel {
  private static final long serialVersionUID = 1L;

  // Construction de la proglet
  public Panel() {
    Font f = new Font("Dialog", Font.BOLD, 84);
    setLayout(new GridLayout(3, 3));
    for(int i = 0; i < 3; i++)
      for(int j = 0; j < 3; j++) {
        tictac[i][j] = new JButton(" ");
        tictac[i][j].setEnabled(false);
        tictac[i][j].setFont(f);
        add(tictac[i][j]);
      }
  }
  /** Tableau de boutons formant la grille 3x3 du jeu de tic-tac-toe. */
  public static JButton tictac[][] = new JButton[3][3];
}

