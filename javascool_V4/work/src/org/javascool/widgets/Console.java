/**************************************************************
* Philippe VIENNE, Copyright (C) 2011.  All rights reserved. *
**************************************************************/

package org.javascool.widgets;

import java.util.ArrayList;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

// redirectStdout/Stderr
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.IOException;

import org.javascool.tools.FileManager;

/** Définit une zone d'affichage qui permet de recevoir les messages de la console.
 *
 * @author Philippe Vienne
 * @see <a href="Console.java.html">code source</a>
 * @serial exclude
 */
public class Console extends JPanel {
  private static final long serialVersionUID = 1L;

  /** Enregistrement des consoles. */
  private static ArrayList<Console> consoles = new ArrayList<Console>();

  // @static-instance

  /** Crée et/ou renvoie une nouvelle instance de console connectée à stdout.
   * @param popup Génère un popup avec la console affichée. False par défaut.
   */
  public static Console newInstance(boolean popup) {
    //{ try { throw new Throwable("Test console="+console+" popup="+popup); } catch(Throwable e) { e.printStackTrace(); } }
    console = new Console();
    if (popup)
      new MainFrame().reset("Console", "org/javascool/widgets/icons/copyAll.png", 600, 400, console);
    consoles.add(console);
    return console;
  }
  /*
   * @see #newInstance(boolean)
   */
  public static Console newInstance() {
    return newInstance(false);
  }

  /** Déconnecte une console.
   * @param console La console à déconnecter.
   */
  public static void removeInstance(Console console) {
    if(console != null)
      consoles.remove(console);
  }

  /** Renvoie la dernière instance de console crée.
   * @see #newInstance(boolean)
   */
  public static Console getInstance() {
    return console == null ? newInstance(true) : console;
  }
  private static Console console = null;

  /** Redirige le System.out vers la console. */
  private static void redirectStdout(boolean redirect) {
    if (redirect && !redirectedStdout) {
      OutputStream out = new OutputStream() {
	  @Override
	  public void write(int b) throws IOException {
	    printAll(String.valueOf((char) b));
	    defaultStdout.write(b);
	  }
	  @Override
	  public void write(byte[] b, int off, int len) throws IOException {
	    printAll(new String(b, off, len));
	    defaultStdout.write(b, off, len);
	  }
	  @Override
	  public void write(byte[] b) throws IOException {
	    write(b, 0, b.length);
	    defaultStdout.write(b);
	  }
	};
      System.setOut(new PrintStream(out, true));
      redirectedStdout = true;
    } else if (redirectedStdout && !redirect) {
      System.setOut(defaultStdout);
      redirectedStdout = false;
    }
  }
  private static PrintStream defaultStdout = System.out;
  private static boolean redirectedStdout = false;

  /** Redirige le System.err vers la console. 
   * @param redirect Selon la valeur True/False active/désactive la redirection.
   */
  public static void redirectStderr(boolean redirect) {
    if (redirect && !redirectedStderr) {
      OutputStream err = new OutputStream() {
	  @Override
	  public void write(int b) throws IOException {
	    printAll(String.valueOf((char) b));
	    defaultStderr.write(b);
	  }
	  @Override
	  public void write(byte[] b, int off, int len) throws IOException {
	    printAll(new String(b, off, len));
	    defaultStderr.write(b, off, len);
	  }
	  @Override
	  public void write(byte[] b) throws IOException {
	    write(b, 0, b.length);
	    defaultStderr.write(b);
	  }
	};
      System.setErr(new PrintStream(err, true));
      redirectedStderr = true;
    } else if (redirectedStderr && !redirect) {
      System.setErr(defaultStderr);
      redirectedStderr = false;
    }
  }
  private static PrintStream defaultStderr = System.err;
  private static boolean redirectedStderr = false;


  //Affiche du texte dans les consoles.
  private static void printAll(String text) {	  
    for(Console console : consoles)
      console.print(text);
  }
  /** Zone d'affichage */
  private JTextArea outputPane;
  /** Barre de menu */
  private ToolBar toolbar;
  /** Zone d'affichage du statut. */
  private JLabel status;

  private Console() {
    BorderLayout layout = new BorderLayout();
    this.setLayout(layout);
    // Construit la zone d'affichage
    outputPane = new JTextArea();
    outputPane.setEditable(false);
    outputPane.setFont(new Font("Monospaced", Font.PLAIN, 12));
    float[] bg = Color.RGBtoHSB(200, 200, 200, null);
    outputPane.setBackground(Color.getHSBColor(bg[0], bg[1], bg[2]));
    JScrollPane scrolledOutputPane = new JScrollPane(outputPane);
    this.add(scrolledOutputPane, BorderLayout.CENTER);
    // Construit la zone des bouttons
    toolbar = new ToolBar();
    toolbar.addTool("Effacer", "org/javascool/widgets/icons/erase.png", new Runnable() {
                      @Override
                      public void run() {
                        clear();
                      }
                    }
                    );
    toolbar.addTool("Copier tout", "org/javascool/widgets/icons/copyAll.png", new Runnable() {
                      @Override
                      public void run() {
                        copyAll();
                      }
                    }
                    );
    toolbar.addTool("Copier sélection", "org/javascool/widgets/icons/copySelection.png", new Runnable() {
                      @Override
                      public void run() {
                        copySelection();
                      }
                    }
                    );
    toolbar.addSeparator();
    toolbar.addTool("status", status = new JLabel("                                         "));
    this.add(toolbar, BorderLayout.NORTH);
    // Met en place la redirection
    redirectStdout(true);
  }
  /** Efface le contenu de la console. */
  public void clear() {
    outputPane.setText("");
  }
  /** Copie le contenu de la console dans le presse-papier. */
  private void copyAll() {
    outputPane.selectAll();
    outputPane.copy();
  }
  /** Copie le contenu de la sélection dans le presse-papier. */
  private void copySelection() {
    outputPane.copy();
  }
  /** Affiche une information dans la barre de statut.
   * @param text Texte à afficher.
   */
  public void show(String text) {
    status.setText(text);
  }
  /** Affiche du texte dans la console.
   * @param text Le texte à afficher.
   */
  public void print(String text) {
    for(String p : prefixes)
      if(text.startsWith(p)) {
        return;
      }
    outputPane.append(text);
  }
  // Messages parasites supprimés à l'affichage
  private static final String prefixes[] = {
    "=== Minim Error ===",
    "=== Likely buffer underrun in AudioOutput.",
    "==== JavaSound Minim Error ===="
  };

  /** Renvoie le contenu actuel de la console.
   * @return Ce qui affiché dans la console.
   */
  public String getText() {
    return outputPane.getText();
  }
  /** Renvoie la barre de menu de la console pour ajouter des éléments.
   * @return La barre de menu de la console.
   */
  public ToolBar getToolBar() {
    return toolbar;
  }
  /** Sauve ce qui est présentement écrit dans la console dans un fichierL.
   * @param location La localisation (chemin du fichier ou localisation internet) où sauver le texte.
   */
  public void saveConsoleOutput(String location) {
    FileManager.save(location, getText());
  }
}
