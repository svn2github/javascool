package compiler;
import java.awt.*;
import java.io.*;
import java.lang.*;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.event.*;
import java.applet.*;

import java.io.InputStream ;
import java.io.DataInputStream ;
  class LireFichier
 {
 private DataInputStream lecteur;
 private String LigneLue;
 private String FileName;


 // Constructeur classe LireFichier-initialisation= ouverture fichier
 LireFichier( String NomFichier) throws IOException {
 FileName = NomFichier;
 try{
  lecteur = new DataInputStream( new FileInputStream(FileName) );
 // ---> cela génère une erreur type sécurité...
 //lecteur = new DataInputStream( (new URL( getCodeBase(), FileName )).openStream() ) ;
 }
 catch (IOException exc){
 System.out.println(" probleme ouverture avec le fichier " + FileName);
 }
 }


 String lire() throws IOException {
 
 LigneLue = lecteur.readLine();
 return LigneLue ;
 }

 void Fermer() throws IOException {
 lecteur.close(); }

 } 