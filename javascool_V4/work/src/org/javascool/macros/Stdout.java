/*********************************************************************************
* Philippe.Vienne@sophia.inria.fr, Copyright (C) 2011.  All rights reserved.    *
* Guillaume.Matheron@sophia.inria.fr, Copyright (C) 2011.  All rights reserved. *
* Thierry.Vieville@sophia.inria.fr, Copyright (C) 2009.  All rights reserved.   *
*********************************************************************************/

package org.javascool.macros;

import org.javascool.widgets.Console;

// Classes pour l'impression d'un composent ou de la consol.
import java.awt.Component;
import java.awt.print.Printable;
import java.awt.print.PrinterJob;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.RepaintManager;

/** Cette factory contient des fonctions de sortie rendues visibles à l'utilisateur de proglets.
 * <p>Elle permet de définir des fonctions statiques qui seront utilisées pour faire des programmes élèves.</p>
 * <p>Elle permet aussi avoir quelques fonctions de base lors de la création de nouvelles proglets.</p>
 *
 * @see <a href="Stdout.java.html">code source</a>
 * @serial exclude
 */
public class Stdout {
  // @factory
  private Stdout() {}

  /** Affiche dans la console une chaîne de caractères ou la représentation textuelle d'un objet sur la console.
   * - Cette fonction ne change pas le focus de javascool.
   * @param string La chaine ou l'objet à afficher sous sa représentation textuelle.
   * @see #println(String)
   */
  public static void echo(String string) {
    Console.getInstance();
    System.out.println(string);
  }
  /**
   * @see #echo(String)
   */
  public static void echo(int string) {
    echo("" + string);
  }
  /**
   * @see #echo(String)
   */
  public static void echo(char string) {
    echo("" + string);
  }
  /**
   * @see #echo(String)
   */
  public static void echo(double string) {
    echo("" + string);
  }
  /**
   * @see #echo(String)
   */
  public static void echo(boolean string) {
    echo("" + string);
  }
  /**
   * @see #echo(String)
   */
  public static void echo(Object string) {
    echo("" + string);
  }
  /**
   * @see #echo(String)
   */
  public static void echo() {
    echo("");
  }
  /** Affiche dans la console une chaîne de caractères ou la représentation textuelle d'un objet sur la console.
   * - Cette fonction ramène le focus de javascool sur la console.
   * @param string La chaine ou l'objet à afficher sous sa représentation textuelle.
   * @see #echo(String)
   */
  public static void println(String string) {
    Console.getInstance();
    System.out.println(string);
    if (org.javascool.gui.Desktop.hasInstance())
      org.javascool.gui.Desktop.getInstance().focusOnConsolePanel();
  }
  /**
   * @see #echo(String)
   */
  public static void println(int i) {
    println("" + i);
  }
  /**
   * @see #echo(String)
   */
  public static void println(char i) {
    println("" + i);
  }
  /**
   * @see #echo(String)
   */
  public static void println(double d) {
    println("" + d);
  }
  /**
   * @see #echo(String)
   */
  public static void println(boolean b) {
    println("" + b);
  }
  /**
   * @see #echo(String)
   */
  public static void println(Object o) {
    println("" + o);
  }
  /**
   * @see #echo(String)
   */
  public static void println() {
    println("");
  }
  /** Affiche dans la console une chaîne de caractères ou la représentation textuelle d'un objet sur la console sans retour à la ligne.
   * @param string La chaine ou l'objet à afficher sous sa représentation textuelle.
   */
  public static void print(String string) {
    Console.getInstance();
    System.out.print(string);
    System.out.flush();
  }
  /**
   * @see #print(String)
   */
  public static void print(int i) {
    print("" + i);
  }
  /**
   * @see #print(String)
   */
  public static void print(char i) {
    print("" + i);
  }
  /**
   * @see #print(String)
   */
  public static void print(double d) {
    print("" + d);
  }
  /**
   * @see #print(String)
   */
  public static void print(boolean b) {
    print("" + b);
  }
  /**
   * @see #print(String)
   */
  public static void print(Object o) {
    print("" + o);
  }
  /** Efface tout ce qui est écrit dans la console. */
  public static void clear() {
    Console.getInstance().clear();
  }
  /** Sauve ce qui est présentement écrit dans la console dans un fichier.
   * @param location La localisation (chemin du fichier ou localisation internet) où sauver le texte.
   */
  public static void saveConsoleOutput(String location) {
    Console.getInstance().saveConsoleOutput(location);
  }

  /** Envoie à l'imprimante la console.*/
  public static void sendConsoleToPrinter() {
    sendToPrinter(Console.getInstance());
  }
  /** Envoie à l'imprimante la console.*/
  public static void sendProgletToPrinter() {
    sendToPrinter(Macros.getProgletPane());
  }
  /** Envoie à l'imprimante un composant graphique.*/
  public static void sendToPrinter(Component c) {
    new Printer(c).print();
  }
  private static class Printer implements Printable {
    private Component component;

    private Printer(Component component) {
      this.component = component;
    }
    
    private void print() {
      PrinterJob printJob = PrinterJob.getPrinterJob();
      printJob.setPrintable(this);
      if (printJob.printDialog())
	try {
	  printJob.print();
	} catch(PrinterException pe) {
	  System.out.println("Error printing: " + pe);
	}
    }
    
    @Override
    public int print(Graphics g, PageFormat pf, int pi) {
      if (pi > 0) {
	return(NO_SUCH_PAGE);
      } else {
	Graphics2D g2d = (Graphics2D) g;
	g2d.translate(pf.getImageableX(), pf.getImageableY());
	double factor = Math.min(pf.getImageableWidth() / component.getWidth(), pf.getImageableHeight() / component.getHeight());
	g2d.scale(factor, factor);
	RepaintManager currentManager = RepaintManager.currentManager(component);
	boolean db = currentManager.isDoubleBufferingEnabled();
	currentManager.setDoubleBufferingEnabled(false);
	component.printAll(g2d);
	currentManager.setDoubleBufferingEnabled(db);
	return(PAGE_EXISTS);
      }
    }
  }
}
