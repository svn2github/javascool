/***********************************************************************************
 * Christophe Béasse <oceank2@gmail.com>, Copyright (C) 2011.  All rights reserved. *
 ************************************************************************************/

package org.javascool.proglets.ticTacToe;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.net.UnknownHostException;

/** Définit un dialogue minimal via une socket entre deux programmes.
 * @see <a href="SocketServer.java.html">source code</a>
 * @serial exclude
 * @deprecated
 * ATTENTION CETTE CLASSE EST DEPRECIEE : UTILISER org.javascool.tools.socket.SocketServer SVP.
 * @author Christophe Béasse 
 */
@Deprecated
public class SocketServer {
  // Variables de la socket
  private ServerSocket server;
  private Socket socket;
  private BufferedReader plec;
  private PrintWriter pred;

  /** Ouvre le port de dialogue de la socket pour initier le dialogue.
   * @param port Numéro de port de la socket.
   * <p>Choisir un numéro entre <a href="http://en.wikipedia.org/wiki/List_of_TCP_and_UDP_port_numbers">49152 et 65535</a> garantit de ne pas interférer avec les autres services TCP/UDP.</p>
   * @return Cet objet, permettant de définir la construction SocketServer socket= new SocketServer().open(..).
   *
   * @throws IllegalArgumentException Si le serveur n'existe pas: c'est à dire que le nom de machine est erronné ou qu'il n'y pas de socket à connecter.
   * @throws RuntimeException Si il y a une erreur d'entrée sortie (par exemple que les autorisations interdisent la connection).
   */
  public SocketServer open(int port) {
    try {
      server = new ServerSocket(port);
      if(server == null) { throw new RuntimeException("Impossible d'ouvrir une serveur sur le port " + port);
      }
      socket = server.accept();
      if(socket == null) { throw new RuntimeException("Impossible d'ouvrir une socket sur le port " + port);
      }
      plec = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      pred = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
    } catch(UnknownHostException e1) { throw new IllegalArgumentException("Erreur serveur inconnu ou inacessible (" + e1 + ")");
    } catch(SecurityException e2) { throw new RuntimeException("Erreur les mécanismes de sécurité interdisent d'utiliser cette fonction (" + e2 + ")");
    } catch(IOException e3) { throw new RuntimeException("Erreur d'entrée sortie à la création de la socket (" + e3 + ")");
    }
    return this;
  }
  /** Ferme la socket pour libérer le port de dialogue. */
  public void close() {
    try {
      if(plec != null) {
        plec.close();
      }
      plec = null;
    } catch(IOException e) {}
    {
      if(pred != null) {
        pred.close();
      }
      pred = null;
    }
    try {
      if(socket != null) {
        socket.close();
      }
      socket = null;
    } catch(IOException e) {}
    try {
      if(server != null) {
        server.close();
      }
      server = null;
    } catch(IOException e) {}
  }
  /** Renvoie la socket actuellement utilisée.
   * @return La socket utilisée ou null si il n'y en a paas d'ouverte.
   */
  public Socket getSocket() {
    return socket;
  }
  /** Envoie un message.
   * @param text Le message à envoyer.
   * @throws RuntimeException Si il y a une erreur d'entrée sortie.
   */
  public void sendMessage(String text) {
    if(pred != null) {
      pred.println(text);
    }
  }
  /** Renvoie le message reçu.
   * @return Le message reçu (qui sera donc effacé) ou la valeur null si il n'y a pas de message.
   * @throws RuntimeException Si il y a une erreur d'entrée sortie.
   */
  public String getMessage() {
    try {
      return plec == null ? null : plec.readLine();
    } catch(IOException e) { throw new RuntimeException("Erreur d'entrée sortie à la lecture du message sur la socket client (" + e + ")");
    }
  }
}

// Ref: http://www.cafeaulait.org/slides/sd2003west/sockets/Java_Socket_Programming.html
// Ref: http://download.oracle.com/javase/tutorial/networking/sockets/clientServer.html
