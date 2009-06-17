package org.unice.javascool.ui.unique;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 * Cette classe permet d'assurer l'unicité de l'instance de l'application. Deux applications ne peuvent pas être lancées
 * simultanément. Voici un exemple typique d'utilisation :
 * @author Unice
 */
public class UniqueInstance {

	/** Port d'écoute utilisé pour l'unique instance de l'application. */
	private int port;

	/** Message à envoyer à l'éventuelle application déjà lancée. */
	private String message;

	/** Actions à effectuer lorsqu'une autre instance de l'application a indiqué qu'elle avait essayé de démarrer. */
	private Runnable runOnReceive;

	private ServerSocket server;
	/**
	 * Créer un gestionnaire d'instance unique de l'application.
	 * 
	 * @param port
	 *            Port d'écoute utilisé pour l'unique instance de l'application.
	 * @param message
	 *            Message à envoyer à l'éventuelle application déjà lancée, {@code null} si aucune action.
	 * @param runOnReceive
	 *            Actions à effectuer lorsqu'une autre instance de l'application a indiqué qu'elle avait essayé de
	 *            démarrer, {@code null} pour aucune action.
	 * @param runOnReceive
	 *            Actions à effectuer lorsqu'une autre instance de l'application a indiqué qu'elle
	 *            avait essayé de démarrer, {@code null} pour aucune action.
	 * @throws IllegalArgumentException
	 *             si le port n'est pas compris entre 1 et 65535, ou si
	 *             {@code runOnReceive != null && message == null} (s'il y a des actions à
	 *             effectuer, le message ne doit pas être {@code null}.
	 */
	public UniqueInstance(int port, String message, Runnable runOnReceive) {
		if (port == 0 || (port & 0xffff0000) != 0)
			throw new IllegalArgumentException("Le port doit être compris entre 1 et 65535 : " + port + ".");
		if (runOnReceive != null && message == null)
			throw new IllegalArgumentException("runOnReceive != null ==> message == null.");

		this.port = port;
		this.message = message;
		this.runOnReceive = runOnReceive;
	}

	/**
	 * Créer un gestionnaire d'instance unique de l'application. Ce constructeur désactive la communication entre
	 * l'instance déjà lancée et l'instance qui essaye de démarrer.
	 * 
	 * @param port
	 *            Port d'écoute utilisé pour l'unique instance de l'application.
	 */
	public UniqueInstance(int port) {
		this(port, null, null);
	}

	/**
	 * Essaye de démarrer le gestionnaire d'instance unique. Si l'initialisation a réussi, c'est que l'instance est
	 * unique. Sinon, c'est qu'une autre instance de l'application est déjà lancée. L'appel de cette méthode prévient
	 * l'application déjà lancée qu'une autre vient d'essayer de se connecter.
	 * 
	 * @return {@code true} si l'instance de l'application est unique.
	 */
	public boolean launch() {
		boolean unique;
		try {
			server = new ServerSocket(port);
			unique = true;

			if(runOnReceive != null) {

				Thread portListenerThread = new Thread("UniqueInstance-PortListenerThread") {

					@Override public void run() {
						while(true) {
							try {
								final Socket socket = server.accept();
								new Thread("UniqueInstance-SocketReceiver") {
									@Override public void run() {
										receive(socket);
									}
								}.start();
							} catch(IOException e) {
								Logger.getLogger("UniqueInstance").warning("Attente de connexion de socket echouee.");
							}
						}
					}
				};
				portListenerThread.start();
			}
		} catch(IOException e) {
			unique = false;
			if(runOnReceive != null) {
				send();
			}
		}
		return unique;
	}

	/**
	 * Envoie un message à l'instance de l'application déjà ouverte.
	 */
	private void send() {
		PrintWriter pw = null;
		try {
			Socket socket = new Socket("localhost", port);
			pw = new PrintWriter(socket.getOutputStream());
			pw.write(message);
		} catch(IOException e) {
			Logger.getLogger("UniqueInstance").warning("Ecriture sur flux de sortie de la socket echouee.");
		} finally {
			if(pw != null)
				pw.close();
		}
	}

	/**
	 * Reçoit un message d'une socket s'étant connectée au serveur d'écoute. Si ce message est le message de l'instance
	 * unique, l'application demande le focus.
	 * 
	 * @param socket
	 *            Socket connectée au serveur d'écoute.
	 */
	private synchronized void receive(Socket socket) {
		Scanner sc = null;
		try {
			socket.setSoTimeout(5000);
			sc = new Scanner(socket.getInputStream());
			String s = sc.nextLine();
			if(message.equals(s)) {
				runOnReceive.run();
			}
		} catch(IOException e) {
		} catch(Exception e){}
		finally {
			if(sc != null)
				sc.close();
		}

	}

	public void stop(){
		try {
			server.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}