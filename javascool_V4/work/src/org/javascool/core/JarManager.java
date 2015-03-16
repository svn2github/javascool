/*********************************************************************************
* Philippe.Vienne@sophia.inria.fr, Copyright (C) 2011.  All rights reserved.    *
* Guillaume.Matheron@sophia.inria.fr, Copyright (C) 2011.  All rights reserved. *
* Thierry.Vieville@sophia.inria.fr, Copyright (C) 2009.  All rights reserved.   *
*********************************************************************************/

package org.javascool.core;

import org.javascool.tools.FileManager;

import java.io.File;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.StringReader;
import java.io.IOException;

import java.util.Enumeration;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;

/** Mets à disposition des fonctions de gestion de jar et répertoires de déploiement. */
public class JarManager {
  // @factory
  private JarManager() {}

  /** Extrait une arborescence d'un jar.
   * @param jarFile Jarre dont on extrait les fichiers.
   * @param destDir Dossier où on déploie les fichiers.
   */
  public static void jarExtract(String jarFile, String destDir) {
    try {
      JarFile jf = new JarFile(jarFile);
      JarInputStream jip = new JarInputStream(new FileInputStream(jarFile));
      Enumeration<JarEntry> entries = jf.entries();
      JarEntry je;
      while((je = jip.getNextJarEntry()) != null) {
        if(!je.isDirectory() && !je.getName().contains("META-INF")) {
          File dest = new File(destDir + File.separator + je.getName());
          dest.getParentFile().mkdirs();
          copyStream(jip, new FileOutputStream(dest));
        }
      }
      jip.close();
    } catch(Exception ex) { 
      throw new IllegalStateException(ex);
    }
  }
  /** Crée un jar à partir d'une arborescence.
   * @param jarFile Le fichier .jar ou .zip à construire. Il est détruit avant d'être créé.
   * @param mfData Contenu du fichier de manifeste (obligatoire) si c'est un fichier en .jar.
   * @param srcDir Dossier source avec les fichiers à mettre en jarre.
   */
  public static void jarCreate(String jarFile, String mfData, String srcDir) {
    try {
      File parent = new File(jarFile).getParentFile();
      if(parent != null)
        parent.mkdirs();
      new File(jarFile).delete();
      srcDir = new File(srcDir).getCanonicalPath();
      // Ajout du manifeste
      if (mfData != null) {
	new File(srcDir + File.separator + "META-INF").mkdirs();
	FileManager.save(srcDir + File.separator + "META-INF" + File.separator + "MANIFEST.MF", mfData);
      }
      JarOutputStream target = new JarOutputStream(new FileOutputStream(jarFile));
      copyFileToJar(new File(srcDir), target, new File(srcDir));
      target.close();
    } catch(Exception ex) {
      ex.printStackTrace(); throw new RuntimeException(ex);
    }
  }
  /** Copie un répertoire/fichier dans un autre en oubliant les svn.
   * @param srcDir Dossier source.
   * @param dstDir Dossier cible.
   * @param recurse Si true (valeur par défaut) copie les sous-répertoires.
   */
  public static void copyFiles(String srcDir, String dstDir, boolean recurse) throws IOException {
    if(new File(srcDir).isDirectory()) {
      if(!new File(srcDir).getName().equals(".svn")) {
        for(String s : FileManager.list(srcDir)) {
          String d = dstDir + File.separator + new File(s).getAbsoluteFile().getName();
	  if (recurse)
	    copyFiles(s, d, true);
	  else if (!new File(s).isDirectory())
	    copyFile(s, d);
        }
      }
    } else
      copyFile(srcDir, dstDir);
  }
  /**
   * #@see copyFiles(String, String, String)
   */
  public static void copyFiles(String srcDir, String dstDir) throws IOException {
    copyFiles(srcDir, dstDir, true);
  }
  /** Copie un fichier dans une destination.
   * @param srcFile Fichier source.
   * @param dstFile Fichier cible.
   */
  public static void copyFile(String srcFile, String dstFile) throws IOException {
    new File(dstFile).getParentFile().mkdirs();
    copyStream(new FileInputStream(srcFile), new FileOutputStream(dstFile));
  }
  /** Copie une ressource dans une destination.
   * @param srcRes Ressource source.
   * @param dstFile Fichier cible.
   */
  public static void copyResource(String srcRes, String dstFile) throws IOException {
    copyStream(JarManager.class.getClassLoader().getResourceAsStream(srcRes), new FileOutputStream(new File(dstFile)));
  }
  // Ajoute un stream a un jar
  private static void copyFileToJar(File source, JarOutputStream target, File root) throws IOException {
    BufferedInputStream in = null;
    try {
      if(source.isDirectory()) {
        String name = source.getPath().replace(root.getAbsolutePath() + File.separator, "").replace(File.separator, "/");
        if(!name.isEmpty() && (!source.equals(root))) {
          if(!name.endsWith("/")) {
            name += "/";
          }
          JarEntry entry = new JarEntry(name);
          entry.setTime(source.lastModified());
          target.putNextEntry(entry);
          target.closeEntry();
        }
        for(File nestedFile : source.listFiles())
          copyFileToJar(nestedFile, target, root);
    } else {
        JarEntry entry = new JarEntry(source.getPath().replace(root.getAbsolutePath() + File.separator, "").replace(File.separator, "/"));
        entry.setTime(source.lastModified());
        target.putNextEntry(entry);
        copyStream(new BufferedInputStream(new FileInputStream(source)), target);
      }
    } catch(Throwable e) {
      e.printStackTrace(System.out); throw new IllegalStateException(e);
    }
  }
  // Copy un stream dans un autre
  private static void copyStream(InputStream in, OutputStream out) throws IOException {
    InputStream i = in instanceof JarInputStream ? in : new BufferedInputStream(in, 2048);
    OutputStream o = out instanceof JarOutputStream ? out : new BufferedOutputStream(out, 2048);
    byte data[] = new byte[2048];
    for(int c; (c = i.read(data, 0, 2048)) != -1;)
      o.write(data, 0, c);
    if(o instanceof JarOutputStream) {
      ((JarOutputStream) o).closeEntry();
    } else {
      o.close();
    }
    if(i instanceof JarInputStream) {
      ((JarInputStream) i).closeEntry();
    } else {
      i.close();
    }
  }
  /** Détruit récursivement un fichier ou répertoire.
   * <p>Irréversible: à utiliser avec la plus grande prudence.</p>
   */
  public static void rmDir(File dir) {
    if(dir.isDirectory()) {
      for(File f : dir.listFiles())
        rmDir(f);
    }
    dir.delete();
  }
}
