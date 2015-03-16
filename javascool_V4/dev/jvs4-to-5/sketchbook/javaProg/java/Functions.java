/*******************************************************************************
* Thierry.Vieville@sophia.inria.fr, Copyright (C) 2009.  All rights reserved . *
*******************************************************************************/

package org.javascool.proglets.javaProg;

import static org.javascool.macros.Macros.*;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.ImageIcon;

// Used to define a click
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;

/** Définit les fonctions de la proglet qui permet d'utiliser toute les classes des swings.
 *
 * @see <a href="http://java.sun.com/docs/books/tutorial/uiswing">Java Swing tutorial</a>
 * @see <a href="http://java.sun.com/javase/6/docs/api/javax/swing/package-summary.html">Java Swing API</a>
 * @see <a href="Functions.java.html">code source</a>
 * @serial exclude
 */
public class Functions {
  private static final long serialVersionUID = 1L;
   // @factory
   private Functions() {}
  /** Nettoie le panneau d'affichage  la proglet. */
  public static void removeAll() {
    getPane().removeAll();
  }
  /** Renvoie le panneau d'affichage de la proglet. */
  public static JLayeredPane getPane() {
    return getProgletPane();
  }
  /** Crée et montre une icône sur le display en (x,y) de taille (w, h) à la profondeur p.
   * @param location  L'URL (Universal Resource Location) où se trouve l'icone.
   * @param x Abcisse du coin inférieur gauche de l'image.
   * @param y Ordonnée du coin inférieur gauche de l'image.
   * @param w Largeur de l'icône (on peut ainsi tronquer l'image mais elle ne se retaille pas).
   * @param h Hauteur de l'icône (on peut ainsi tronquer l'image mais elle ne se retaille pas).
   * @param p Profondeur du tracé de 1 le plus "profond" avec des valeurs plus grandes pour les plans de devant.
   * @return L'objet correspondant à l'icône qui peut être:
   * <p>manipulé ensuite avec la construction <tt>JLabel icon = showIcon(..);</tt></p>
   * <p>- déplacé avec la construction <tt>icon.setLocation(x, y);</tt></p>
   * <p>- rendu visible/invisible avec la construction <tt>icon.setVisible(trueOrFalse);</tt></p>
   * <p>- modifié par la construction <tt>icon.setIcon(getIcon("nouvelle-icône"));</tt></p>
   */
  public static JLabel showIcon(String location, int x, int y, int w, int h, int p) {
    JLabel icon = new JLabel();
    ImageIcon image = getIcon(location);
    icon.setIcon(image);
    icon.setLocation(x, y);
    if((w > 0) && (h > 0)) {
      icon.setSize(w, h);
    } else {
      icon.setSize(image.getIconWidth(), image.getIconHeight());
    }
    getPane().add(icon, new Integer(p), 0);
    return icon;
  }
  /**
   * @see #showIcon(String, int, int, int, int, int)
   */
  public static JLabel showIcon(String location, int x, int y, int p) {
    return showIcon(location, x, y, 0, 0, p);
  }
  /** Crée et montre un texte sur le display en (x,y) à la profondeur p.
   * @param text Le texte à montrer.
   * @param x Abcisse du coin inférieur gauche de l'image.
   * @param y Ordonnée du coin inférieur gauche de l'image.
   * @param p Profondeur du tracé de 1 le plus "profond" avec des valeurs plus grandes pour les plans de devant.
   * @return L'objet correspondant à l'icône qui peut être:
   * <p>manipulé ensuite avec la construction <tt>JLabel icon = showText(..);</tt></p>
   * <p>- déplacé avec la construction <tt>icon.setLocation(x, y);</tt></p>
   * <p>- rendu visible/invisible avec la construction <tt>icon.setVisible(trueOrFalse);</tt></p>
   * <p>- modifié par la construction <tt>icon.setText("nouveau-texte"));</tt></p>
   */
  public static JLabel showText(String text, int x, int y, int p) {
    JLabel label = new JLabel(text);
    label.setSize(label.getPreferredSize());
    label.setLocation(x, y);
    getPane().add(label, new Integer(p), 0);
    return label;
  }
  
  /** Définit une portion de code appellée à chaque clic de souris.
   * @param runnable La portion de code à appeler, ou null si il n'y en a pas.
   */
  public static void setMouseListener(Runnable runnable) {
    mouseRunnable = runnable;
    if (runnable == null) {
      if (listener != null)
	getPane().removeMouseListener(listener);
    } else if (listener == null) {
      getPane().addMouseListener(listener = new MouseListener() {
	  private static final long serialVersionUID = 1L;
	  @Override
	    public void mouseReleased(MouseEvent e) {
	    clicX = e.getX();
	    clicY = e.getY();
	    if(mouseRunnable != null) {
	      new Thread(mouseRunnable).start();
	    }
	  }
	  @Override
	    public void mousePressed(MouseEvent e) {}
	  @Override
	    public void mouseClicked(MouseEvent e) {}
	  @Override
	    public void mouseEntered(MouseEvent e) {}
	  @Override
	    public void mouseExited(MouseEvent e) {}
	});
    }
  }
  private static Runnable mouseRunnable = null;
  private static MouseListener listener = null;
  /** Renvoie la position horizontale du dernier clic de souris dans l'image. */
  public static int getClicX() {
    return clicX;
  }
  /** Renvoie la position verticale du dernier clic de souris dans l'image. */
  public static int getClicY() {
    return clicY;
  }
  private static int clicX = 0, clicY = 0;

}
