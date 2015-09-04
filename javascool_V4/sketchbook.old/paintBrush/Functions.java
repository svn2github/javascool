/*******************************************************************************
* David.Pichardie@inria.fr, Copyright (C) 2011.           All rights reserved. *
*******************************************************************************/

package org.javascool.proglets.paintBrush;

// Used to define the gui
import javax.swing.JPanel;
import org.javascool.macros.Macros;

/** Définit les fonctions de la proglet qui permettent de faire des tracés dans une image.
 *
 * @see <a href="PaintBrush.java.html">code source</a>
 * @see <a href="PaintBrushMain.java.html">PaintBrushMain.java</a>, <a href="PaintBrushImage.java.html">PaintBrushImage.java</a>,  <a href="PaintBrushManipImage.java.html">PaintBrushManipImage.java</a>
 * @serial exclude
 */
public class Functions {
  private static final long serialVersionUID = 1L;
  // @factory
  private Functions() {}
  /** Renvoie l'instance de la proglet pour accéder à ses éléments. */
  private static Panel getPane() {
    return Macros.getProgletPane();
  }
  /** Remet l'affichage sur la proglet. */
  static public void reset() {
    org.javascool.gui.Desktop.getInstance().focusOnProgletPanel();
  }
  /** Définit le mécanisme de dessin de l'image à partir d'un codage de l'utilisateur.
   * @param manipImage L'implémentation de dessin à utiliser.
   */
  public static void setManipImage(PaintBrushManipImage manipImage) {
    getPane().setManipImage(manipImage);
  }
  /** Change la valeur d'un pixel de l'image.
   * @param x Abcisse de l'image, entre 0 et 31 inclut.
   * @param y Ordonnée de l'image, entre 0 et 31 inclut.
   * @return La couleur, entre 0 pour la couleur noire et 15 pour la couleur blanche (notée F en format hexadécimal), les autres valeurs correspondant à la palette de couleur.
   */
  public static int getPixel(int x, int y) {
    return PaintBrushImage.getPixel(x, y);
  };

  /** Change la valeur d'un pixel de l'image.
   * @param x Abcisse de l'image, entre 0 et 31 inclut.
   * @param y Ordonnée de l'image, entre 0 et 31 inclut.
   * @param col Couleur, entre 0 pour la couleur noire et 15 pour la couleur blanche (notée F en format hexadécimal), les autres valeurs correspondant à la palette de couleur.
   */
  public static void setPixel(int x, int y, int col) {
    PaintBrushImage.setPixel(x, y, col);
    getPane().repaint(0, 0, getPane().getWidth(), getPane().getHeight());
  };
}
