/*******************************************************************************
 *           Philippe.Vienne, Copyright (C) 2011.  All rights reserved.         *
 *******************************************************************************/
package org.javascool.tools;

import java.io.File;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Permet de stocker des informations dans un fichier de configuration de
 * l'utilisateur.
 * 
 * @see <a href="UserConfig.java.html">code source</a>
 * @serial exclude
 */
public class UserConfig {
    // @static-instance

    private UserConfig(String applicationName) {
	this.applicationName = applicationName;
	setProperty("appName", applicationName);
    }

    private String applicationName;

    /**
     * Renvoie le répertoire standard où stocker les données d'une application.
     * 
     * @return Le répertoire standard où stocker les données d'une application,
     *         ou un répertoire temporaire si celui-ci est indéfini.
     */
    public String getApplicationFolder() {
	String OS = System.getProperty("os.name").toUpperCase();
	if (OS.contains("WIN"))
	    return System.getenv("APPDATA") + "\\" + applicationName + "\\";
	else if (OS.contains("MAC"))
	    return System.getProperty("user.home") + "/Library/Application Support/" + applicationName + "/";
	else if (OS.contains("NUX"))
	    return System.getProperty("user.home") + "/." + applicationName + "/";
	else {
	    System.err.println("Impossible de définir un répertoire de configuration pour l'application " + applicationName
		    + " sous le système d'exploitation «" + OS + "»");
	    return FileManager.createTempDir(applicationName).getAbsolutePath();
	}
    }

    /**
     * Renvoie l'URL du fichier de configuration de l'application.
     * 
     * @return L'adresse du fichier
     */
    private String getConfigurationFileLocation() {
	return getApplicationFolder() + "configuration.json";
    }

    /**
     * Charge le fichier des propriétés.
     * 
     */
    private void loadConfiguration() {
	if (properties != null)
	    return; // Le fichier est déjà chargé
	if (!new File(getConfigurationFileLocation()).exists()) { // Le fichier
								  // n'existe
								  // pas
	    JSONObject conf = new JSONObject();
	    FileManager.save(getConfigurationFileLocation(), conf.toJSONString());
	}
	try {
	    JSONParser jp = new JSONParser();
	    String configuration = FileManager.load(getConfigurationFileLocation(), true);
	    properties = (JSONObject) jp.parse(configuration);
	} catch (ParseException pe) {
	    System.err.println("Impossible de lire le fichier de configuration, il peut être corrompu. Debug : ");
	    System.out.println("Position: " + pe.getPosition());
	    System.out.println(pe);
	    properties = new JSONObject();
	} catch (Exception e) {
	    System.err.println("Impossible de lire le fichier de configuration, il peut être corrompu. Debug : ");
	    e.printStackTrace(System.err);
	    properties = new JSONObject();
	}
    }

    /**
     * Lit une propriété liée à cette application.
     * 
     * @param name
     *            Nom de la propriété.
     * @param value
     *            Valeur par défaut.
     * @return La valeur de la propriété, si elle définie, sinon null.
     */
    public String getProperty(String name, String value) {
	loadConfiguration();
	return properties.get(name) != null ? ((String) properties.get(name)) : (value != null ? value : null);
    }

    /** @see #getProperty(String, String) */
    public String getProperty(String name) {
	return getProperty(name, null);
    }

    private JSONObject properties = null;

    /**
     * Ecrit une propriété liée à cette application.
     * 
     * @param name
     *            Nom de la propriété.
     * @param value
     *            Valeur de la propriété.
     * @return Cet objet, permettant de définir la construction
     *         <tt>UserConfig.getInstance(..).setProperty(..)</tt>.
     */
    @SuppressWarnings("unchecked")
    public UserConfig setProperty(String name, String value) {
	String v = getProperty(name);
	if ((value == null && v != null) || !value.equals(v)) {
	    properties.put(name, value);
	    // On écrit le fichier de configuration
	    FileManager.save(getConfigurationFileLocation(), properties.toJSONString());
	}
	return this;
    }

    /**
     * Crée et/ou renvoie l'unique instance de l'objet.
     * <p>
     * Une application ne peut définir qu'un seul objet de configuration.
     * </p>
     */
    public static UserConfig getInstance(String applicationName) {
	if (UserConfig.userConfig == null)
	    return UserConfig.userConfig = new UserConfig(UserConfig.theApplicationName = applicationName);
	else if (UserConfig.theApplicationName.equals(applicationName))
	    return UserConfig.userConfig;
	else
	    throw new IllegalArgumentException(
		    "Appel incohérent à la configuration de l'application avec deux noms différents "
			    + UserConfig.theApplicationName + " puis " + applicationName);
    }

    public static String MAC_OSX = "mac";
    public static String LINUX = "lin";
    public static String WINDOWS = "win";

    /**
     * Détecte le système courrant de l'utilisateur.
     * 
     * @return L'un des drapeau de la classe (MAC_OSX, LINUX, WINDOWS)
     */
    public static String getOS() {
	String os = System.getProperty("os.name").toLowerCase();
	if (os.indexOf("win") >= 0)
	    return WINDOWS;
	if (os.indexOf("mac") >= 0)
	    return MAC_OSX;
	return LINUX;
    }

    private static String theApplicationName = null;
    private static UserConfig userConfig = null;
}
