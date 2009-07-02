package compiler;

/**
 * <p>Titre : JFileFilter</p>
 * <p>Description : Classe permettant de définir les extensions</p>
 * <p>Copyright : Copyright (c) 2003</p>
 * <p>Société : perso</p>
 * @author Indiana_jules
 * @version 1.0
 */

//Importation des packages nécessaires
import java.io.*;
import java.util.*;


public class JFileFilter extends javax.swing.filechooser.FileFilter{
  //Variables globales
  protected String description;
  protected ArrayList exts = new ArrayList();

  /**Constructeur par défaut*/
  public JFileFilter() {
  }

  /**Méthode permettant de rajouter l'extension souhaité*/
  public void addType(String s) {
    exts.add(s);
  }

  /**Méthode permettant de savoir si on accepte le fichier*/
  public boolean accept(File f) {
    if(f.isDirectory()){
      return true;
    }
    else if(f.isFile()){
      Iterator it = exts.iterator();
      while(it.hasNext()){
        if(f.getName().endsWith((String)it.next())){
          return true;
        }
      }
    }

    return false;
  }

  /**Méthode retournant la description du filtre*/
  public String getDescription() {
    return description;
  }

  /**Méthode définissant la description de ce filtre*/
  public void setDescription(String s) {
    description = s;
  }
}
