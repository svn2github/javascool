/*******************************************************************************
 * Thierry.Vieville@sophia.inria.fr, Copyright (C) 2010.  All rights reserved. *
 *******************************************************************************/

package org.javascool.tools;

// Used to report a throwable

// Used to frame a message
import java.net.URLEncoder;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.javascool.macros.Macros;

/**
 * Détecte et rapporte de manière optimisée des erreurs lors de l'exécution.
 * 
 * @see <a href="ErrorCatcher.java.html">code source</a>
 * @serial exclude
 */
public class ErrorCatcher {
    // @factory
    private ErrorCatcher() {
    }

    /**
     * Ouvre une fenêtre d'alerte en cas d'exception intempestive et non prise
     * en compte.
     * <p>
     * Installe un gestionnaire d'exception non interceptée qui recueille des
     * informations sur: les versions des composants logiciels, le nom du
     * process, la trace de la pile et l'affiche dans une fenêtre séparée afin
     * d'être recueillies et communiquées par l'utilisateur.
     * </p>
     * 
     * @param header
     *            Un texte entête en HTML expliquant à l'utilisateur quoi faire
     *            avec cette sortie d'exception.
     * @param revision
     *            Nom et/ou numéro de révision de l'application pour avoir une
     *            trace en cas d'erreur.
     * @param keyword
     *            Si différent de null, le message n'est donné à l'utilisateur
     *            que si la trace de la pile contient le mot-clé (
     *            <tt>org.javascool</tt> par défaut)
     */
    public static void setUncaughtExceptionAlert(String header, String revision, String keyword) {
	ErrorCatcher.uncaughtExceptionAlertHeader = header;
	ErrorCatcher.uncaughtExceptionKeyword = keyword;
	System.setProperty("application.revision", "" + revision);
	Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
	    @Override
	    public void uncaughtException(Thread t, Throwable e) {
		String s = "", r = "";
		if (ErrorCatcher.uncaughtExceptionAlertOnce <= 1) {
		    s += ErrorCatcher.uncaughtExceptionAlertHeader + "\n<hr><pre>";
		    for (String p : new String[] { "application.revision", "java.version", "java.home", "java.class.path",
			    "os.name", "os.arch", "os.version", "user.name", "user.home", "user.dir" }) {
			s += "> " + p + " = " + System.getProperty(p) + "\n";
		    }
		}
		try {
		    s += "> localhost = " + java.net.InetAddress.getLocalHost() + "\n";
		} catch (Exception er) {
		}
		s += "> file.enc = " + org.javascool.main.Core.javascoolJarEnc() + "\n";
		s += "> thread.name = " + t.getName() + "\n";
		s += "> throwable = " + e + "\n";
		if (0 < ErrorCatcher.uncaughtExceptionAlertOnce) {
		    s += "> count = " + ErrorCatcher.uncaughtExceptionAlertOnce + "\n";
		}
		s += "> stack-trace = «\n";
		for (int i = 0; i < t.getStackTrace().length; i++) {
		    r += e.getStackTrace()[i] + (i < t.getStackTrace().length - 1 ? "\n" : "»");
		}
		boolean alert = (ErrorCatcher.uncaughtExceptionKeyword == null || r
			.indexOf(ErrorCatcher.uncaughtExceptionKeyword) != -1)
			&& (e.toString().indexOf("java.util.ConcurrentModificationException") == -1)
			&& (e.toString()
				.indexOf(
					"com.sun.java.swing.plaf.nimbus.DerivedColor$UIResource cannot be cast to com.sun.java.swing.Painter") == -1);
		s += r + "</pre><hr>";
		System.err.println(s);
		// Ici on détecte si l'erreur vient du programme utilisateur
		if (r.toString().indexOf("JvsToJavaTranslated") != -1) {
		    org.javascool.core.Jvs2Java.report(e);
		} else {
		    try {
			if (ErrorCatcher.uncaughtExceptionAlertOnce < 3 && alert) {
			    FileManager.load("http://javascool.gforge.inria.fr?weberroreport=" + URLEncoder.encode(s, "utf-8"));
			}
		    } catch (Exception er) {
			System.err.println("Impossible de lancer l'alerte à travers le web (" + er + ")");
		    }
		    if (ErrorCatcher.uncaughtExceptionAlertOnce == 0 && alert) {
			Macros.message(s, true);
		    }
		    ErrorCatcher.uncaughtExceptionAlertOnce++;
		}
	    }
	});
    }

    public static void setUncaughtExceptionAlert(String header, String revision) {
	ErrorCatcher.setUncaughtExceptionAlert(header, revision, "org.javascool");
    }

    private static String uncaughtExceptionAlertHeader;
    private static int uncaughtExceptionAlertOnce = 0;
    private static String uncaughtExceptionKeyword = null;

    /**
     * Impose une version minimale de Java.
     * <p>
     * Si la version n'est pas correcte, l'application s'arrête et un
     * téléchargement est proposé.
     * </p>
     * 
     * @param version
     *            Version de Java 5 pour 1.5, 6 pour 1.6.
     */
    public static void checkJavaVersion(int version) {
	if (new Integer(System.getProperty("java.version").substring(2, 3)) < version) {
	    if (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(new JFrame(),
		    "<html>Vous n'avez pas une version suffisante de Java<br>" + "cette application requiert Java 1." + version
			    + " ou plus.<br>" + "Voulez vous être redirigé vers le site de téléchargement ?", "Confirmation",
		    JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE)) {
		Macros.openURL("http://www.java.com/getjava");
	    }
	    System.exit(-1);
	}
    }
}
