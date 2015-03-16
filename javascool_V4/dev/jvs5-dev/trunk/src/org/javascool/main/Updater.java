package org.javascool.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import javax.swing.JOptionPane;

import org.javascool.core.Exec;
import org.javascool.tools.UserConfig;
import org.javascool.widgets.MainFrame;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Mise à jour automatique de Java's cool et des proglets.
 * 
 * @author Philippe VIENNE
 */
public class Updater {

    private final String updateServer = "http://jvs-update/";

    private JSONArray packageToUpgrade = new JSONArray();

    /** Instance unique de la classe. */
    private static Updater instance;

    /**
     * Permet d'obtenir la classe unique.
     * 
     * @return L'instance publique de cette classe
     */
    public static Updater getInstance() {
	if (instance == null)
	    instance = new Updater();
	return instance;
    }

    private Updater() {

    }

    /**
     * Demande au serveur s'il y a de nouvelles versions.
     * 
     * @return vrai dans le cas ou une ou plus de mise à jours est disponible,
     *         faux dans les autres cas
     */
    public boolean hasNewVersions() {
	if (!isConnectedToInternet())
	    return false;
	try {
	    URL url = new URL(updateServer + "?isJVSApp&hasUpdate&time="
		    + UserConfig.getInstance("javascool").getProperty("updateTime", "0"));
	    BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
	    String inputLine = "", l = "";
	    while ((inputLine = in.readLine()) != null) {
		l = l + inputLine;
	    }
	    in.close();
	    JSONObject r = (JSONObject) new JSONParser().parse(l);
	    if (r.get("status").equals("upgrade")) {
		packageToUpgrade = (JSONArray) r.get("packages");
		for (Object o : packageToUpgrade) {
		    String s = (String) o;
		    System.err.println("Upgrade : " + s);
		}
		return true;
	    } else
		return false;
	} catch (IOException e) {
	} catch (ParseException e) {
	} // En cas d'erreur, on ne met rien à jours
	return false;
    }

    /**
     * Demande à l'utilisateur s'il veut mettre à jours JVS. Si JVS n'a jamais
     * été mis à jours, il le présente alors comme un téléchargement de Java's
     * Cool. Cela est définit par un update timestamp à 0.
     * 
     * @return vrai dans le cas ou l'utilisateur veux mettre à jours, faux
     *         sinon.
     */
    public boolean askIfWeUpgrade() {
	MainFrame.getFrame(); // Call to main frame to set Look and Feel
	String m = "";
	if (UserConfig.getInstance("javascool").getProperty("updateTime", "0") == "0") {
	    m = "Voulez-vous installer Java's Cool sur votre session ?";
	} else {
	    m = packageToUpgrade.size() + " mise(s) à jours sont disponibles, voulez-vous les installer ?";
	}
	int p = JOptionPane.showConfirmDialog(null, m);
	if (p == JOptionPane.OK_OPTION)
	    return true;
	return false;
    }

    private int isConnected = -1;

    /**
     * Vérifie si on est connecté à internet. Pour cela, on demande la page de
     * Google. En cas d'erreur, on estime qu'il n'y a pas de connexion. Le
     * résultat est ensuite stocké dans une variable pour éviter l'attente du
     * Timeout à chaque appel de la fonction.
     * 
     * @return
     */
    public boolean isConnectedToInternet() {
	if (isConnected != -1)
	    return isConnected == 1 ? true : false;
	try {
	    URL url = new URL("http://74.125.93.132"); // une des IPs de Google
	    HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
	    urlConn.setConnectTimeout(500);
	    urlConn.connect();
	    isConnected = 1;
	    return true;
	} catch (IOException e) {
	    isConnected = 0;
	    return false;
	}
    }

    /**
     * Fonction de lancement de l'Updater.
     * 
     * @param args
     *            Les arguments de lançement de l'application
     */
    public static void main(String[] args) {
	// System.out.println("Connected : "+getInstance().isConnectedToInternet());
	if (getInstance().hasNewVersions() && getInstance().askIfWeUpgrade()) {
	    getInstance().upgrade();
	}
	System.out.println(getInstance().getClassPath());
	Updater.getInstance().runJavaSCool(new String[0]);
    }

    /** Télécharge et installe les mises à jours. */
    public void upgrade() {
	if (!isConnectedToInternet())
	    return;
	File jarLocationStorage = new File(UserConfig.getInstance("javascool").getApplicationFolder(), "jar");
	jarLocationStorage.mkdirs();
	for (Object o : packageToUpgrade) {
	    String pck = (String) o;
	    try {
		URL url = new URL(updateServer + "?isJVSApp&getUpdate&package=" + pck);
		File dest = new File(jarLocationStorage, pck);
		ReadableByteChannel rbc = Channels.newChannel(url.openStream());
		FileOutputStream fos = new FileOutputStream(dest);
		fos.getChannel().transferFrom(rbc, 0, 1 << 24);
	    } catch (IOException e) {
	    } // En cas d'erreur on ignore et on passe au package suivant
	}
	packageToUpgrade = new JSONArray();
	long updateTime = System.currentTimeMillis() / 1000;
	UserConfig.getInstance("javascool").setProperty("updateTime", "" + updateTime);
    }

    /**
     * Liste les Jar à mettre dans le classpath.
     * 
     * @return Le classpath
     */
    public String getClassPath() {
	String cp = "";
	File jarLocationStorage = new File(UserConfig.getInstance("javascool").getApplicationFolder(), "jar");
	for (File f : jarLocationStorage.listFiles()) {
	    if (!cp.equals("")) {
		cp += File.pathSeparator;
	    }
	    cp += f.getAbsolutePath();
	}
	cp += ".";
	return cp;
    }

    /** Lance Java's Cool. */
    public void runJavaSCool(String[] args) {
	String arg = "";
	for (String s : args) {
	    arg += "\t" + s;
	}
	String jvmArgs = " -cp \"" + getClassPath() + "\" org.javascool.main.Core";
	Exec.run("\"" + System.getProperty("java.home") + File.separator + "bin" + File.separator + "java\"" + jvmArgs + arg, 0);
    }

}
