package org.javascool.proglets.ticTacToe;

import static org.javascool.macros.Macros.*;
import java.awt.Color;

import java.net.Socket;

import org.javascool.tools.socket.SocketServer;
import org.javascool.tools.socket.SocketClient;

/** Définit les fonctions de la proglet.
 * @see <a href="Functions.java.html">source code</a>
 * @serial exclude
 * @author Christophe Béasse
 */
public class Functions {
   // @factory
   private Functions() {}
  /** Renvoie l'instance de la proglet. */
  private static Panel getPane() {
    return getProgletPane();
  }
  /*
   * Méthodes liées au jeu de tic-tac-toe
   */

  /** Permet de positionner une marque sur la grille du panel de la proglet
   * @param i Position horizontale entre 1 et 3.
   * @param j Position verticale entre 1 et 3.
   * @param mark Marque du tictactoe soit 'X', soit 'O', sinon la marque est effacée.
   */
  public static void setGrille(int i, int j, char mark) {
    if((0 < i) && (i < 4) && (0 < j) && (j < 4)) {
      if(mark == 'O') {
        org.javascool.proglets.ticTacToe.Panel.tictac[i - 1][j - 1].setText("O");
        org.javascool.proglets.ticTacToe.Panel.tictac[i - 1][j - 1].setForeground(Color.BLUE);
      } else if(mark == 'X') {
        org.javascool.proglets.ticTacToe.Panel.tictac[i - 1][j - 1].setText("X");
        org.javascool.proglets.ticTacToe.Panel.tictac[i - 1][j - 1].setForeground(Color.GREEN);
      } else {
        org.javascool.proglets.ticTacToe.Panel.tictac[i - 1][j - 1].setText(" ");
      }
    }
  }
  /** Permet de récupérer la marque sur la grille du panel de la proglet .
   * @param i Position horizontale entre 1 et 3.
   * @param j Position verticale entre 1 et 3.
   * @return mark La marque du tictactoe soit 'X', soit 'O', soit ' ' si il n'y a pas de marque.
   */
  public static char getGrille(int i, int j) {
    return (0 < i && i < 4 && 0 < j && j < 4) ? org.javascool.proglets.ticTacToe.Panel.tictac[i - 1][j - 1].getText().charAt(0) : ' ';
  }
  /** Remets à zéro le jeu du tic-tac-toe. */
  public static void resetGrille() {
    for(int i = 0; i < 3; i++)
      for(int j = 0; j < 3; j++) {
        org.javascool.proglets.ticTacToe.Panel.tictac[i][j].setText(" ");
        org.javascool.proglets.ticTacToe.Panel.tictac[i][j].setForeground(Color.BLACK);
      }
  }
  /*
   * Méthodes liées à la connection serveur.
   */
  private static SocketServer server = new SocketServer();

  /** Ouverture du socket server.
   * @see SocketServer#open(int)
   */
  public static void openSocketServer(int numport) {
    server.open(numport);
  }
  /** Permet de récupérer un message via le socket server. */
  public static String getMessageViaSocketServer() {
    return server.getMessage();
  }
  /** Permet d'écrire un message sur le socket server. */
  public static void sendMessageViaSocketServer(String text) {
    server.sendMessage(text);
  }
  /** Renvoie la socket elle-même pour accéder aux fonctions bas-niveau. */
  public static Socket getSocketServer() {
    return server.getSocket();
  }
  /** Fermeture du socket server.  */
  public static void closeSocketServer() {
    server.close();
  }
  /*
   * Méthodes liées à la connection serveur.
   */
  private static SocketClient client = new SocketClient();

  /** Ouverture du socket client.
   * @see SocketClient#open(String, int)
   */
  public static void openSocketClient(String hostname, int numport) {
    client.open(hostname, numport);
  }
  /** Permet de récupérer un message via le socket client. */
  public static String getMessageViaSocketClient() {
    return client.getMessage();
  }
  /** Permet d'écrire un message sur le socket client. */
  public static void sendMessageViaSocketClient(String text) {
    client.sendMessage(text);
  }
  /** Renvoie la socket elle-même pour accéder aux fonctions bas-niveau. */
  public static Socket getSocketClient() {
    return client.getSocket();
  }
  /** Fermeture du socket client.  */
  public static void closeSocketClient() {
    client.close();
  }
} // class functions
