/*******************************************************************************
*           Philippe.Vienne, Copyright (C) 2011.  All rights reserved.         *
*******************************************************************************/

package org.javascool.gui;

import javax.swing.UIManager;
import org.javascool.core.Utils;
import org.javascool.tools.ErrorCatcher;

/** Lanceur de l'application "apprenant" qui permet de manipuler des «proglets».  *
 *
 * @see <a href="Core.java.html">source code</a>
 * @serial exclude
 */
public class Core {
  /** Aide de JVS */
  public static final String help = "org/javascool/macros/memo-macros.htm";
  /** Mets en place le système d'alerte en cas d'erreur non gérée. */
  public static void setUncaughtExceptionAlert() {
    ErrorCatcher.setUncaughtExceptionAlert("<h1>Détection d'une anomalie liée à Java:</h1>\n" +
                                           "Il y a un problème de compatibilité avec votre système, nous allons vous aider:<ul>\n" +
                                           "  <li>Copier/Coller tous les éléments de cette fenêtre et</li>\n" +
                                           "  <li>Envoyez les par mail à <b>javascool@googlegroups.com</b> avec toute information utile.</li>" +
                                           " </ul>",
                                           About.revision);
  }
  /** Lanceur de l'application.
   * @param usage <tt>java -jar javascool-proglets.jar [proglet]</tt>
   */
  public static void main(String[] usage) {
    if((usage.length > 0) && (usage[0].equals("-h") || usage[0].equals("-help") || usage[0].equals("--help"))) {
      System.out.println("Java's Cool Core - lance l'interface pour travailler avec les proglets");
      System.out.println("Usage : java -jar javascool.jar");
      System.exit(0);
    }
    // Empeche de pouvoir renommer itempestivement des folder, attention un problème sous Mac avec la jvs7
    if (!System.getProperty("os.name").toLowerCase().startsWith("mac"))
      UIManager.put("FileChooser.readOnly", Boolean.TRUE);
    System.err.println("" + About.title + " is starting ...");
    //ErrorCatcher.checkJavaVersion(6);
    setUncaughtExceptionAlert();
    // Lance le panneau général ou une proglet directement
    Desktop.getInstance().getFrame();
    // Lance une proglet sur la ligne de commande
    String pname = usage.length > 0 ? usage[usage.length - 1] : null;
    try {
      if (pname.matches(".*javascool.*")) {
	pname = null; 
      } else {
	Desktop.getInstance().openProglet(pname, true);
      }
    } catch(Exception e) {
      pname = null;
    }
    // Lance une proglet si elle est unique dans le jar
    if (pname == null) {
      pname = Utils.javascoolProglet();
      if (pname != null) {
	System.err.println("Ouverture de la proglet «"+pname+"»");
	Desktop.getInstance().openProglet(pname, true);
      }
    }
  }
}
