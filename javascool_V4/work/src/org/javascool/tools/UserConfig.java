/*******************************************************************************
*           Philippe.Vienne, Copyright (C) 2011.  All rights reserved.         *
*******************************************************************************/
package org.javascool.tools;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/** Permet de stocker des informations dans un fichier de configuration de l'utilisateur.
 *
 * @author Philippe Vienne
 * @see <a href="UserConfig.java.html">code source</a>
 * @serial exclude
 */
public class UserConfig {
  // @static-instance

  private UserConfig(String applicationName) {
    this.applicationName = applicationName;
  }
  private String applicationName;

  /** Renvoie le répertoire standard où stocker les données d'une application.
   * @return Le répertoire standard où stocker les données d'une application, ou un répertoire temporaire si celui-ci est indéfini.
   */
  public String getApplicationFolder() {
    String OS = System.getProperty("os.name").toUpperCase();
    if(OS.contains("WIN")) {
      return System.getenv("APPDATA") + "\\" + applicationName + "\\";
    } else if(OS.contains("MAC")) {
      return System.getProperty("user.home") + "/Library/Application Support/" + applicationName + "/";
    } else if(OS.contains("NUX")) {
      return System.getProperty("user.home") + "/." + applicationName + "/";
    } else {
      System.err.println("Impossible de définir un répertoire de configuration pour l'application " + applicationName + " sous le système d'exploitation «" + OS + "»");
      return createTempDir(applicationName).getAbsolutePath();
    }
  }
  /** Lit une propriété liée à cette application.
   * @param name Nom de la propriété.
   * @param value Valeur par défaut.
   * @return La valeur de la propriété, si elle définie, sinon null.
   */
  public String getProperty(String name, String value) {
    if(properties == null) {
      properties = new Properties();
      try {
        if(new File(getApplicationFolder() + "configuration.xml").exists()) {
          properties.loadFromXML(new FileInputStream(getApplicationFolder() + "configuration.xml"));
        }
	//System.err.println(applicationName + " config loaded : "+properties);
      } catch(Throwable e) {
        System.err.println("Dysfonctionnement lors la lecture du fichier de configuration de " + applicationName + " : " + e);
      }
    }
    return properties.getProperty(name, value);
  }
  /**
   * @see #getProperty(String, String)
   */
  public String getProperty(String name) {
    return getProperty(name, null);
  }
  private Properties properties = null;

  /** Ecrit une propriété liée à cette application.
   * @param name Nom de la propriété.
   * @param value Valeur de la propriété. La valeur <tt>null</tt> efface la propriété.
   * @return Cet objet, permettant de définir la construction <tt>UserConfig.getInstance(..).setProperty(..)</tt>.
   */
  public UserConfig setProperty(String name, String value) {
    String v = getProperty(name);
    if((value == null && v != null) || !value.equals(v)) {
      properties.setProperty(name, value);
      try {
        new File(getApplicationFolder()).mkdirs();
        properties.storeToXML(new FileOutputStream(getApplicationFolder() + "configuration.xml"), applicationName + " user configuration");
      } catch(Exception e) {
        System.err.println("Dysfonctionnement lors l'écriture du fichier de configuration de " + applicationName + " : " + e);
      }
    }
    //System.err.println(applicationName + " config saved : "+properties);
    return this;
  }
  /** Crée et/ou renvoie l'unique instance de l'objet.
   * <p>Une application ne peut définir qu'un seul objet de configuration.</p>
   */
  public static UserConfig getInstance(String applicationName) {
    if(userConfig == null) {
      return userConfig = new UserConfig(theApplicationName = applicationName);
    } else if(theApplicationName.equals(applicationName)) {
      return userConfig;
    } else { 
      throw new IllegalArgumentException("Appel incohérent à la configuration de l'application avec deux noms différents " + theApplicationName + " puis " + applicationName);
    }
  }
  private static String theApplicationName = null;
  private static UserConfig userConfig = null;
  /** Crée un répertoire temporaire dans le répertoire temporaire de la machine.
   * @param prefix Prefix du répertoire.
   * @throws RuntimeException Si une erreur d'entrée-sortie s'est produite.
   */
  public static File createTempDir(String prefix) {
    try {
      File d = File.createTempFile(prefix, "");
      d.delete();
      d.mkdirs();
      return d;
    } catch(IOException e) { throw new RuntimeException(e + " when creating temporary directory");
    }
  }
}
