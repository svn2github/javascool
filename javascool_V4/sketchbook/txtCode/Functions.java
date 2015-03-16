package org.javascool.proglets.txtCode;

import static org.javascool.macros.Macros.*;
import java.awt.Color;
import javax.swing.JTextArea;
import java.lang.Character;
import java.net.Socket;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.EOFException;
import java.io.FileNotFoundException;

import org.javascool.tools.socket.SocketServer;
import org.javascool.tools.socket.SocketClient;

/** Définit les fonctions de la proglet.
 * @see <a href="Functions.java.html">source code</a>
 * @serial exclude
 * @author Christophe Béasse <oceank2@gmail.com>
 */
public class Functions {

   // @factory
   private Functions() {}

  /** Renvoie l'instance de la proglet. */
  private static Panel getPane() {
    return getProgletPane();
  }  

  /*
   * Méthodes liées au filedump
   */
  
  public static void focusOnConsolePanel() {
     org.javascool.gui.Desktop.getInstance().focusOnConsolePanel();  
}

  private static DataInputStream fileR = null;
  private static DataOutputStream fileW = null;    
  

/**
* Ouverture du fichier en lecture.
* @param nomFichier nom du fichier à ouvrir  
*/   
  public static void openFileReader(String nomFichier) {
    try {  
        fileR = new DataInputStream(new BufferedInputStream
			              (new FileInputStream(nomFichier)));
    }catch (FileNotFoundException e) { 
    }            
  }

/**
* Lecture du code  suivant dans le fichier. 
* @return valeur du code lu.
*/   
  public static int readNextCode() {
    int c = -1;
    
    if (fileR == null)
	    throw new RuntimeException("Le fichier READER n'est pas ouvert ! ");  

    try {	    
        c = fileR.readUnsignedByte();
    }catch (EOFException e) { 
    }catch (IOException e) { 
    }        	           
    return c;
  }  

/**
* Fermeture du fichier ouvert en lecture. 
*/
  
  public static void closeFileReader() {
    try {  
            fileR.close();
    }catch (IOException e) { 
    }            
  }  
  
public static void filedump(String nomFichier){

  int [] buffer = new int[16];

  openFileReader(nomFichier);
  int c;
  int offset = 0;
  int i = 0;
  affiche("=======================================\n");	
  affiche("txtCode fileDump :["+nomFichier+"]\n");	
  affiche("=======================================\n");
  sautDeLigne();  
  affiche("      00 01 02 03 04 05 06 07 08 09 0A 0B 0C 0D 0E 0F \n");
  while ((c = readNextCode()) != -1) {
      buffer[i++] = c;
      if (i==16) {
         afficheligne(buffer,i,offset);
			 offset++; 		 
       i = 0;
      } // End if            	
  }// End while
  
  afficheligne(buffer,i,offset);       

  closeFileReader();
}    
  
  static void afficheligne(int [] buffer, int i, int offset) {
    if (i>0) {
        affiche(String.format("%04x",offset)+"  ");
			  for (int j=0; j < i ; j++){
            	  affiche(code2HexStr(buffer[j]));
            	  affiche(" ");
			  } 
			  for (int j=i; j < 16 ; j++){
            	  affiche("   ");
			  }                    	
			  affiche(" ");
			  for (int j=0; j < i ; j++){
			    if ((buffer[j]>31) && (buffer[j]<127))
              affiche(code2CarStr(buffer[j]));
          else affiche(".");
			 }
			 sautDeLigne();	 
    } // End if 
  
  
  }

/**
* Ouverture du fichier en Ecriture.
* @param nomFichier nom du fichier à ouvrir  
*/ 
  public static void openFileWriter(String nomFichier) {
    try {  
       fileW = new DataOutputStream(new BufferedOutputStream
			            (new FileOutputStream(nomFichier)));
    }catch (IOException e) { 
    }            
  }

/**
* Ecriture du code suivant dans le fichier ouvert en écriture.
* @param c code à ecrire  
*/   
  public static void writeNextCode(int c) {
    
    if (fileW == null)
	    throw new RuntimeException("Le fichier WRITER n'est pas ouvert ! ");  
	    
    try {
        fileW.writeByte(c);
    }catch (IOException e) { 
    }        	           
  }  
  
/**
* Fermeture du fichier ouvert en Ecriture. 
*/   
  public static void closeFileWriter() {
    try {  
            fileW.close();
    }catch (IOException e) { 
    }            
  }    

  public static void affiche(String str) {
	  org.javascool.proglets.txtCode.Panel.textArea.append(str);
  }
     
  public static void affiche(char c) {
	  org.javascool.proglets.txtCode.Panel.textArea.append(Character.toString(c));
  }
  
  public static void affiche(int n) {
	  org.javascool.proglets.txtCode.Panel.textArea.append(Integer.toString(n));
  }  
  
  public static void afficheCodeAuFormatHex(int c) {
	  org.javascool.proglets.txtCode.Panel.textArea.append(code2HexStr(c));
  }
  
  public static void afficheCodeAuFormatDec(int c) {
	  org.javascool.proglets.txtCode.Panel.textArea.append(Integer.toString(c));
  }  
  
  public static void afficheCodeAuFormatBin(int c) {
	  org.javascool.proglets.txtCode.Panel.textArea.append(Integer.toBinaryString(c));
  }  
  
  public static void afficheCodeAuFormatCar(int c) {
	  org.javascool.proglets.txtCode.Panel.textArea.append(code2CarStr(c));
  }   
     
  public static void sautDeLigne() {
	  org.javascool.proglets.txtCode.Panel.textArea.append("\n");
  }  
  
  public static String code2HexStr(int code) {
    if (code < 16) return ("0"+Integer.toHexString(code));
    else return Integer.toHexString(code); 
  }
  
  public static String code2CarStr(int code) {

    return(Character.toString((char) code));
  
  }  

  /** Remets à zéro la zone d'affichage du proglet */
  public static void resetConsole() {

	org.javascool.proglets.txtCode.Panel.textArea.setText(" ");
	org.javascool.proglets.txtCode.Panel.textArea.setForeground(Color.BLACK);    

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
