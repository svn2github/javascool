package org.javascool.proglets.commSerie;

/**
 *  Adaptation for JavaScool of the PSerial - class for serial port goodness
 *  Part of the Processing project - http://processing.org
 *  Copyright (c) 2004-05 Ben Fry & Casey Reas
 */

import gnu.io.CommPortIdentifier;
import java.util.Enumeration;
import gnu.io.SerialPort;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.IOException;
import gnu.io.SerialPortEventListener;
import gnu.io.SerialPortEvent;
import gnu.io.CommPortOwnershipListener;
import java.util.ArrayList;

/** Définit les primitives pour s'interfacer avec un port série.
 * <p>Les erreurs d'entrée-sortie sont affichée dans la console (<tt>System.out</tt>).</p>
 * <p>Ce code utilise <a href="http://www.jcontrol.org">RXTX</a> (documenté <a href="http://www.jcontrol.org/docs/api">ici</a>)
 *   et s'est largement inspiré du travail de l'outil proposé par <a href="http://processing.org/reference/libraries/serial/index.html">processing</a>.</p>
 * <p>Note: La librairie  <a href="http://www.jcontrol.org">RXTX</a> est une bonne alternative
 *   à <a href="http://download.oracle.com/docs/cd/E17802_01/products/products/javacomm/reference/api">javax.comm</a>
 *   analysée <a href="http://www.javaworld.com/javaworld/jw-05-1998/jw-05-javadev.html">ici</a>.</p>
 * @see <a href="SerialInterface.java.html">code source</a>
 * @serial exclude
 */
public class SerialInterface {
  /** Définit le nom du port série.
   * @param name Le nom du port, "COM1" par défaut.
   * @return Cet objet permettant la construction <tt>new SerialInterface().setName(..</tt>.
   */
  public SerialInterface setName(String name) {
    this.name = name;
    return this;
  }
  /** Renvoie le nom du port série. */
  public String getName() {
    return name;
  }
  private String name = "COM1";

  /** Définit le débit du port série.
   * @param rate Le débit du port en bauds (bits par seconde), 9600 par défaut.
   * @return Cet objet permettant la construction <tt>new SerialInterface().setRate(..</tt>.
   */
  public SerialInterface setRate(int rate) {
    this.rate = rate;
    return this;
  }
  /** Renvoie le débit du port série. */
  public int getRate() {
    return rate;
  }
  private int rate = 9600;

  /** Définit la parité du port série.
   * @param parity La parité du port : 'N' (par défaut) si pas de parité, 'E' pour even si parité paire, 'O' pour odd si parité impaire.
   * @return Cet objet permettant la construction <tt>new SerialInterface().setParity(..</tt>.
   */
  public SerialInterface setParity(char parity) {
    this.parity = parity;
    return this;
  }
  /** Renvoie la parité du port série. */
  public int getParity() {
    return parity;
  }
  private char parity = 'N';

  /** Définit la taille des mots du port série.
   * @param size La taille des mots : 8 (par défaut) ou 7..
   * @return Cet objet permettant la construction <tt>new SerialInterface().setSize(..</tt>.
   */
  public SerialInterface setSize(int size) {
    this.size = size;
    return this;
  }
  /** Renvoie la taille des mots du port série. */
  public int getSize() {
    return size;
  }
  private int size = 8;

  /** Définit le nombre de bits de stop du port série.
   * @param stop Le nombre de bits de stop : 1 (par défaut), 1.5 ou 2.
   * @return Cet objet permettant la construction <tt>new SerialInterface().setStop(..</tt>.
   */
  public SerialInterface setStop(double stop) {
    this.stop = stop;
    return this;
  }
  /** Renvoie le nombre de bits de stop du port série. */
  public double getStop() {
    return stop;
  }
  private double stop = 1;

  /** Ouvre le port série avec les paramètres actuels.
   * @return Cet objet permettant la construction <tt>new SerialInterface().set*(.. ). open()</tt>.
   */
  public SerialInterface open() {
    close();
    data.clear();
    try {
      for(Enumeration< ? > list = CommPortIdentifier.getPortIdentifiers(); list.hasMoreElements();) {
        CommPortIdentifier id = (CommPortIdentifier) list.nextElement();
        if((id.getPortType() == CommPortIdentifier.PORT_SERIAL) && id.getName().equals(name)) {
          open(id);
          return this;
        }
      } throw new IOException("Impossible d'ouvrir un port série de nom :" + name + " (il n'existe pas)");
    } catch(Throwable e) {
      output = null;
      input = null;
      port = null;
      System.out.println("Erreur à l'ouverture du port série \n\t" + this + "\n\t : " + e);
      return this;
    }
  }
  // Opens on a given port identifier
  private void open(CommPortIdentifier id) throws Exception {
    int timeout = 5000; // milliseconds
    port = (SerialPort) id.open("Java'sCool commSerie", timeout);
    output = port.getOutputStream();
    input = port.getInputStream();
    id.addPortOwnershipListener(new CommPortOwnershipListener() {
                                  @Override
                                  public void ownershipChange(int type) {
                                    switch(type) {
                                    case PORT_OWNED :
                                      System.out.println("Notice: le port vient d'être pris par javascool");
                                      break;
                                    case PORT_UNOWNED:
                                      System.out.println("Notice: le port vient d'être relaché par javascool");
                                      break;
                                    case PORT_OWNERSHIP_REQUESTED:
                                      System.out.println("Notice: une demande de prise en main du port par une autre application est émise");
                                      break;
                                    default:
                                      System.out.println("Notice: une demande de prise en main parasite est émise");
                                      break;
                                    }
                                  }
                                }
                                );
    port.setSerialPortParams(rate, size,
                             stop == 2.0 ? SerialPort.STOPBITS_2 : stop == 1.5 ? SerialPort.STOPBITS_1_5 : SerialPort.STOPBITS_1,
                             parity == 'E' ? SerialPort.PARITY_EVEN : parity == 'O' ? SerialPort.PARITY_ODD : SerialPort.PARITY_NONE);
    port.addEventListener(listener = new SerialPortEventListener() {
                            synchronized public void serialEvent(SerialPortEvent serialEvent) {
                              if(serialEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
                                try {
                                  while(input.available() > 0) {
                                    reading(input.read());
                                  }
                                } catch(Exception e) {
                                  System.out.println("Erreur à la réception d'un caractère sur le port série \n\t" + this + "\n\t : " + e);
                                }
                              }
                            }
                          }
                          );
    port.notifyOnDataAvailable(true);
  }
  private SerialPort port = null;
  private OutputStream output = null;
  private InputStream input = null;
  private Reader reader = null;
  private Writer writer = null;
  private ArrayList<Integer> data = new ArrayList<Integer>();
  private SerialPortEventListener listener = null;

  /** Ferme le port série. */
  public void close() {
    try {
      if (listener != null) {
	port.removeEventListener();
	listener = null;
      }
      if(output != null) {
        output.close();
        output = null;
      }
      if(input != null) {
        input.close();
        input = null;
      }
      if(port != null) {
        port.close();
        port = null;
      }
    } catch(Exception e) {
      output = null;
      input = null;
      port = null;
      System.out.println("Erreur à la fermeture du port série \n\t" + this + "\n\t : " + e);
    }
  }
  /** Teste si le port est ouvert.
   * @return Renvoie true si le port a été ouvert sans erreur.
   */
  public boolean isOpen() {
    return port != null;
  }
  /** Ecrit un octet sur le port série.
   * @param value L'octet à écrire.
   */
  public void write(int value) {
    try {
      if(writer != null) {
        writer.writing(value);
      }
      if(output == null) { 
	throw new IOException("Port série fermé ou en erreur");
      }
      output.write(value & 0xff);
      output.flush();
    } catch(Exception e) {
      System.out.println("Erreur à l'écriture de '" + value + "' sur le port série \n\t" + this + "\n\t : " + e);
    }
  }
  /** Ecrit les octets d'une chaîne de caractère sur le port série.
   * @param value Les octets à écrire. La chaîne ne doit contenir que des caractères ASCII.
   * @return La valeur true, si la chaîne est bien de l'ASCII, et fausse sinon. Dans ce cas aucun caratère n'est écrit.
   */
  public boolean write(String value) {
    if(isASCII(value)) {
      for(char c : value.toCharArray())
        write((int) c);
      return true;
    } else {
      return false;
    }
  }
  /** Teste si une  chaîne ne contient que des caractères l'ASCII.
   * @param value La chaîne à tester.
   * @return La valeur true, si la chaîne est bien de l'ASCII, et fausse sinon.
   */
  public static boolean isASCII(String value) {
    boolean ok = true;
    for(char c : value.toCharArray()) {
      int v = (int) c;
      if((c < 0) || (255 < c)) {
        System.out.println("Impossible d'écrire la chaîne \"" + value + "\" qui contient le caratère non-ASCII '" + c + "'");
        ok = false;
      }
    }
    return ok;
  }
  /** Définit un moniteur des caractères émis. */
  public interface Writer {
    /** Routine appellée à l'écriture de chaque caractère.
     * @param c Le caractère écrit.
     */
    public void writing(int c);
  }

  /** Connecte un writer au port série.
   * @param writer Un writer qui est appellé à chaque caratère écrit.
   * @return Cet objet permettant la construction <tt>new SerialInterface().setWriter(..</tt>.
   */
  public SerialInterface setWriter(Writer writer) {
    this.writer = writer;
    return this;
  }
  /** Lit un octet sur le port série.
   * <p>L'octet lu est retiré du buffer de lecture, après l'appel de cette fonction.</p>
   * @return La valeur de l'octet à lire ou -1 si il n'y a pas d'octet à lire.
   */
  public int read() {
    if(data.size() == 0) {
      return -1;
    } else {
      int value = data.get(0);
      data.remove(0);
      return value;
    }
  }
  // Lecture de l'octet à son arrivée
  private void reading(int c) {
    data.add(c);
    if(reader != null) {
      reader.reading(c);
    }
  }
  /** Renvoie tous les octets actuellement dans le buffer.
   * @return Un tableau avec tous les octets actuellement  dans le buffer.
   */
  int[] getChars() {
    int chars[] = new int[data.size()], i = 0;
    for(int c : data)
      chars[i++] = c;
    return chars;
  }
  /** Définit un lecteur de caractère. */
  public interface Reader {
    /** Routine appellée à l'arrivée de chaque caractère.
     * @param c Le caractère reçu.
     */
    public void reading(int c);
  }

  /** Connecte un reader au port série.
   * @param reader Un reader qui est appellé à l'arrivée de chaque caractère en lecture.
   * @return Cet objet permettant la construction <tt>new SerialInterface().setReader(..</tt>.
   */
  public SerialInterface setReader(Reader reader) {
    this.reader = reader;
    return this;
  }
  /** Ajoute des octets entrée pour simuler une lecture.
   * @param string Les octets en entrée.
   */
  public void simulReading(String string) {
    if(isASCII(string)) {
      for(char c : string.toCharArray())
        reading((int) c);
    }
  }
  /** Renvoie une description des paramètres du port série. */
  public String toString() {
    return "<port name=\"" + name + "\" rate=\"" + rate + "\" parity=\"" + parity + "\" size=\"" + size + "\" stop=\"" + stop +
           "\" open=\"" + isOpen() + "\" input-buffer-size =\"" + data.size() + "\"/>";
  }
  /** Renvoie la liste des noms de ports séries disponibles. */
  public static String[] getPortNames() {
    try {
      ArrayList<String> names = new ArrayList<String>();
      for(Enumeration< ? > list = CommPortIdentifier.getPortIdentifiers(); list.hasMoreElements();) {
	CommPortIdentifier id = (CommPortIdentifier) list.nextElement();
	show(id);
	if(id.getPortType() == CommPortIdentifier.PORT_SERIAL) {
	  names.add(id.getName());
	}
      }
      return names.toArray(new String[names.size()]);
    } catch(Throwable e) {
      return new String[0];
    }
  }
  // Affiche les paramètres du port.
  private static void show(CommPortIdentifier id) {
    try {
      System.out.println("Le port " +
                         (id.getPortType() == CommPortIdentifier.PORT_SERIAL ? "(série)" :
                          id.getPortType() == CommPortIdentifier.PORT_PARALLEL ? "(parallèle)" :
                          "(de type inconnu)") +
                         " " + id.getName() + " est détecté, " +
                         " il appartient à " + id.getCurrentOwner() + " et est actuellement " + (id.isCurrentlyOwned() ? "pris" : "non-pris"));
    } catch(Exception e) {
      System.out.println("Le port " + id.getName() + " est détecté mais génère une erreur lors de l'affichage de ses paramètres");
    }
  }
  /** Renvoie la liste des des noms de ports séries disponibles ce qui teste l'installation des librairies.
   * @param usage <tt>java -cp javascool-proglets.jar org.javascool.proglets.commSerie.SerialInterface</tt>
   */
  public static void main(String usage[]) {
    String names[] = getPortNames();
    {
      System.out.print("Il y a " + names.length + " ports série:");
      for(String name : names)
        System.out.print(" " + name);
      System.out.println();
    }
  }
}
