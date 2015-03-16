package org.javascool.gui;

import javax.swing.UIManager;

import org.javascool.About;
import org.javascool.Core;
import org.javascool.tools.ErrorCatcher;
import org.javascool.tools.UserConfig;

public class BetaCore extends Core{
	
	/**
	 * Lanceur de l'application JavaScool Beta.
	 * 
	 * @param usage
	 *            <tt>java -jar javascool.jar</tt>
	 */
	public static void main(String[] usage) {
		if ((usage.length > 0)
				&& (usage[0].equals("-h") || usage[0].equals("-help") || usage[0]
						.equals("--help"))) {
			System.out
					.println("Java's Cool Core - lance l'interface pour travailler avec les proglets");
			System.out.println("Usage : java -jar javascool.jar");
			System.exit(0);
		}
		About.title="JVS 4.5 Beta";
		// Empeche de pouvoir renommer itempestivement des folder
		UIManager.put("FileChooser.readOnly", Boolean.TRUE);
		System.err.println("" + About.title + " is starting ...");
		ErrorCatcher.checkJavaVersion(6);
		UserConfig.getInstance("javascool").setProperty("version", About.revision);
		Desktop.getInstance().getFrame();
	}
	
}
