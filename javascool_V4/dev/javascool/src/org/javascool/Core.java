/*******************************************************************************
 *           Philippe.Vienne, Copyright (C) 2011.  All rights reserved.         *
 *******************************************************************************/

package org.javascool;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.javascool.gui.Desktop;
import org.javascool.macros.Macros;
import org.javascool.tools.ErrorCatcher;

/**
 * Lanceur de l'application "apprenant" qui permet de manipuler des «proglets».
 * *
 * 
 * @see <a href="Core.java.html">source code</a>
 * @serial exclude
 */
public class Core {
	/** Aide de JVS */
	public static final String help = "org/javascool/macros/memo-macros.htm";
	private static String javascoolJar = null;

	/**
	 * Retrouve le chemin du jar courant.
	 * 
	 * @return Le chemin du jar
	 * @throws RuntimeException
	 *             lorsque l'application n'a pas été démarré depuis un jar
	 */
	public static String javascoolJar() {
		if (Core.javascoolJar != null)
			return Core.javascoolJar;
		final String url = Macros.getResourceURL("org/javascool/Core.class")
				.toString().replaceFirst("jar:file:([^!]*)!.*", "$1");
		System.err.println("Notice: javascool url is " + url);
		if (url.endsWith(".jar")) {
			try {
				String jar = URLDecoder.decode(url, "UTF-8");
				if (new File(jar).exists())
					return Core.javascoolJar = jar;
				// Ici on essaye tous les encodages possibles pour essayer de
				// détecter javascool
				{
					jar = URLDecoder.decode(url, Charset.defaultCharset()
							.name());
					if (new File(jar).exists())
						return jar;
					for (final String enc : Charset.availableCharsets()
							.keySet()) {
						jar = URLDecoder.decode(url, enc);
						if (new File(jar).exists()) {
							System.err.println("Notice: javascool file " + jar
									+ " correct decoding as " + enc);
							return Core.javascoolJar = jar;
						} else {
							System.err.println("Notice: javascool file " + jar
									+ " wrong decoding as " + enc);
						}
					}
					throw new RuntimeException(
							"Il y a un bug d'encoding sur cette plate forme");
				}
			} catch (final UnsupportedEncodingException ex) {
				throw new RuntimeException(
						"Spurious defaultCharset: this is a caveat");
			}
		} else{return System.getProperty("user.dir");}
			//throw new RuntimeException(
				//	"Java's cool n'a pas été démarré depuis un Jar");
	}

  public static String javascoolJarEnc()  {
    return null;
  }
	/**
	 * Lanceur de l'application.
	 * 
	 * @param usage
	 *            <tt>java -jar javascool.jar</tt>
	 */
	public static void main(String[] usage) {
		if (usage.length > 0
				&& (usage[0].equals("-h") || usage[0].equals("-help") || usage[0]
						.equals("--help"))) {
			System.out
					.println("Java's Cool Core - lance l'interface pour travailler avec les proglets");
			System.out.println("Usage : java -jar javascool.jar");
			System.exit(0);
		}
		// Empeche de pouvoir renommer itempestivement des folder
		UIManager.put("FileChooser.readOnly", Boolean.TRUE);
		System.err.println("" + About.title + " is starting ...");
		ErrorCatcher.checkJavaVersion(6);
		Core.setUncaughtExceptionAlert();
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				Desktop.getInstance().getFrame();
			}
		});
	}

	/** Mets en place le système d'alerte en cas d'erreur non gérée. */
	static void setUncaughtExceptionAlert() {
		ErrorCatcher
				.setUncaughtExceptionAlert(
						"<h1>Détection d'une anomalie liée à Java:</h1>\n"
								+ "Il y a un problème de compatibilité avec votre système, nous allons vous aider:<ul>\n"
								+ "  <li>Copier/Coller tous les éléments de cette fenêtre et</li>\n"
								+ "  <li>Envoyez les par mail à <b>javascool@googlegroups.com</b> avec toute information utile.</li>"
								+ " </ul>", About.revision);
	}
}
