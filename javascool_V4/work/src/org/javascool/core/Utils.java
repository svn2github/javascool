/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.javascool.core;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import org.javascool.macros.Macros;

import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Collationne des fonctions générales pour faire marcher javascool.
 */
public class Utils {
  // @factory
  private Utils() {}
   /** Retrouve le chemin du jar courant.
   * @return Le chemin du jar
   * @throws RuntimeException lorsque l'application n'a pas été démarré depuis un jar
   */
  public static String javascoolJar() {
    if(javascoolJar != null) {
      return javascoolJar;
    }
    String url = Macros.getResourceURL("org/javascool/core/Utils.class").toString().replaceFirst("jar:file:([^!]*)!.*", "$1");
    //- System.err.println("Notice: javascool url is " + url);
    if(url.endsWith(".jar")) {
      try {
        String jar = URLDecoder.decode(url, "UTF-8");
        if(new File(jar).exists()) {
          return javascoolJar = jar;
        }
        // Ici on essaye tous les encodages possibles pour essayer de détecter javascool
        {
          jar = URLDecoder.decode(url, Charset.defaultCharset().name());
          if(new File(jar).exists()) {
            javascoolJarEnc = Charset.defaultCharset().name();
            return jar;
          }
          for(String enc : Charset.availableCharsets().keySet()) {
            jar = URLDecoder.decode(url, enc);
            if(new File(jar).exists()) {
              javascoolJarEnc = enc;
              System.err.println("Notice: javascool file " + jar + " correct decoding as " + enc);
              return javascoolJar = jar;
            } else {
              System.err.println("Notice: javascool file " + jar + " wrong decoding as " + enc);
            }
          } throw new RuntimeException("Il y a un bug d'encoding sur cette plate forme");
        }
      } catch(UnsupportedEncodingException ex) { throw new RuntimeException("Spurious defaultCharset: this is a caveat");
      }
    } else { return "";
    }
    // throw new RuntimeException("Java's cool n'a pas été démarré depuis un Jar");
  }
  public static String javascoolJarEnc() {
    javascoolJar();
    return javascoolJarEnc;
  }
  private static String javascoolJar = null, javascoolJarEnc = null;

  /** Retrouve la proglet du jar, si elle est unique.
   * @return Le nom de la proglet ou null si il y a une ambiguïté.
   */
  public static String javascoolProglet() {
    if (ProgletEngine.getInstance().getProgletCount() == 1)
      for(Proglet proglet: ProgletEngine.getInstance().getProglets())
	return proglet.getName();
    return null;
  }

  /** Exécute dans un autre process une commande java.
   * <p>Les sorties du process java sont renvoyés en mémoire au stdin/stderr de la présente exécution.</p>
   * @param command Les arguments de la ligne de commande de l'exécutable java.
   * @param timeout La durée maximale d'exécution de la commande.
   * @return Le status de l'exécutable java, 0 si pas d'erreur.
   * @throws IllegalStateException en cas de time-out.
   */
  public static boolean javaStart(String command, int timeout) {
    try {
      String javaCommand = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java"; 
      if (System.getProperty("os.name").toUpperCase().contains("WIN"))
	javaCommand +=".exe";
      return exec(javaCommand+(command.indexOf('\t') == -1 ? " " : "\t")+command, timeout) == 0;
    } catch(Exception e) {
      System.out.println("Impossible de lancer la command '$java " + command + "' : " + e);
      return false;
    }
  }
  // Lance une commande avec echo des stdout/stderr
  private static int exec(String command, int timeout) throws IOException  {
    Process process = Runtime.getRuntime().exec(command.trim().split((command.indexOf('\t') == -1) ? " " : "\t"));
    InputStreamReader stdout = new InputStreamReader(process.getInputStream());
    InputStreamReader stderr = new InputStreamReader(process.getErrorStream());
    long now = System.currentTimeMillis();
    while(true) {
      try { Thread.sleep(300); } catch(Exception e) { }
      while(stdout.ready())
	System.out.print((char) stdout.read());
      while(stderr.ready())
	System.err.print((char) stderr.read());
      try {
	return process.exitValue();
      } catch(Exception e) { }
      if (timeout > 0 && System.currentTimeMillis() > now + timeout * 1000)
	throw new IllegalStateException("Timeout T > "+timeout+"s when running '"+command+"'");
    }
  }
   
}
