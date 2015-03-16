package org.javascool.core;

import org.javascool.tools.UserConfig;
import java.io.File;
import org.javascool.tools.FileManager;
import org.javascool.widgets.Console;

import org.javascool.gui.Core;

/** Définit le mécanisme de lancement du programme principal.
 * Les différentes fonctionnalités de javascool sont déclenchées à partir de cet emballage. <ul>
 *  <li><b>Lancement en WebService</b>. Si un mécanisme Web a créé le fichier: 
 *    <br><tt><i>APPDATA</i>javascool/webcommand.mf</tt> avec la ligne de commande:
 *    <br><tt><i>command</i> = <i>chemin</i></tt> où: <ul>
 *      <li><tt><i>APPDATA</i>javascool</tt> est le répertoire standard où stocker les données d'une application 
 *        <br>(<small><tt>$HOME/.javascool</tt> sous <tt>Linux</tt>, <tt>$HOME/Library/Application Support/javascool</tt> sous MacOS, <tt>:\Documents and Settings\USER\Application Data\javascool</tt> sous Windows récent, ..</small>)</li>
 *      <li><tt><i>command</i></tt> vaut : <ul>
 *        <li><tt><a href="Jvs2Jar.html">Jvs2Jar</a></tt> pour compiler un fichier javascool en <tt>.jar</tt> avec la proglet qui est définit dans l'exécutable.</li>
 *        <li><tt><a href="Proglet2Html.html">Proglet2Html</a></tt> pour compiler le dossier d'une proglet à partir de ses sources.</li>
 *        <li><tt><a href="ProgletCreate.html">ProgletCreate</a></tt> pour initier des fichiers lors de la création d'une proglet.</li>
 *      </ul></li>
 *      <li><tt><i>chemin</i></tt> est le chemin, dans le système de fichier local de l'utilisateur, 
 *        <br>du fichier javascool à compiler ou
 *        <br>du répertoire des sources de la proglet,</li>
 *    </ul> alors le mécamisme correspondant est lancé et une console affiche les messages d'erreur ou de succès.</li>
 * <li><b>Lancement en application</b>. Si le nom de la jarre est de la forme 
 *   <tt>javascool-jvs2jar-*.jar</tt> alors l'interface de <a href="Jvs2Jar.html">Jvs2Jar</a> est lancé, 
 *   sinon l'interface utilisateur de la proglet est lancé.</li>
 * </ul>
 * @see <a href="MainWrapper.java.html">code source</a>
 * @serial exclude
 */
public class MainWrapper {
  // @factory
  private MainWrapper() {}

  // Implémentation du mécanisme de gestion du Web Service
  private static boolean webWrapper() {
    // Lit le fichier de commande
    String wrapperCommandFile = UserConfig.getInstance("javascool").getApplicationFolder() + "webcommand.mf";
    if (new File(wrapperCommandFile).exists()) {
      String wrapperCommandText = FileManager.load(wrapperCommandFile).replaceAll("\n+", " ").replaceAll("\\s*=\\s*", " = ").trim();
      new File(wrapperCommandFile).delete();
      Console.newInstance(true);
      try {
	// Parse la ligne de commande du fichier
	String wrapperCommand[] = wrapperCommandText.split("=");
	if (wrapperCommand.length == 2) {
	  String wrapperCommandName = wrapperCommand[0].trim(), wrapperCommandPath = wrapperCommand[1].trim();
	  // Ventile la commande à exécuter
	  if ("Jvs2Jar".equals(wrapperCommandName)) {
	    Jvs2Jar.build(wrapperCommandPath, true);
	    System.out.println("\n> la webcommand.mf «"+wrapperCommandText+"» est effectuée");
	    return true;
	  } else if ("Proglet2Html".equals(wrapperCommandName)) {
	    Proglet2Html.build(wrapperCommandPath, true);
	    System.out.println("\n>  la webcommand.mf «"+wrapperCommandText+"» est effectuée");
	    return true;
	  } else if ("ProgletCreate".equals(wrapperCommandName)) {
	    ProgletCreate.build(wrapperCommandPath, true);
	    System.out.println("\n>  la webcommand.mf «"+wrapperCommandText+"» est effectuée");
	    return true;
	  }
	}
	throw new IllegalStateException("Un fichier $APPDATA/javascool/webcommand.mf avec la commande erronnée «"+wrapperCommandText+"» est détecté");
      } catch (Throwable e) {
	System.out.println(e);
	e.printStackTrace();
	try { new File(wrapperCommandFile).delete(); } catch(Exception x) {}
	return true;
      }
    } else
      return false;
  }
    
  /** Mécanisme de lancement du programme principal selon le mode détecté.
   * @param usage <tt>java org.javascool.core.MainWrapper</tt>
   */
  public static void main(String[] usage) {
    // @main
    if(!webWrapper()) {
      String javascoolJar = Utils.javascoolJar();
      if (new File(javascoolJar).getName().startsWith("javascool-jvs2jar-")) {
	Jvs2Jar.main(usage);
      } else if (new File(javascoolJar).getName().startsWith("javascool-proglet-")) {
	Core.main(usage);
      } else
	Proglet2Html.main(usage);
    }
  }
}



	
